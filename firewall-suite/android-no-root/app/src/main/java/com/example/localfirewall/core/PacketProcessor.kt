package com.example.localfirewall.core

import android.net.ConnectivityManager
import android.net.VpnService
import android.system.OsConstants
import com.example.localfirewall.data.GlobalMode
import com.example.localfirewall.data.UidPolicyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetAddress
import java.nio.ByteBuffer

class PacketProcessor(
    private val vpnService: VpnService,
    private val policyStore: UidPolicyStore,
    private val input: FileInputStream,
    private val output: FileOutputStream
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var loopJob: Job? = null
    private val forwarder = UserSpaceForwarder(vpnService)

    fun start() {
        if (loopJob?.isActive == true) return

        loopJob = scope.launch {
            val buffer = ByteBuffer.allocate(32767)
            while (isActive) {
                buffer.clear()
                val length = input.read(buffer.array())
                if (length <= 0) continue

                val packet = buffer.array()
                if (shouldAllowPacket(packet, length)) {
                    forwarder.forward(packet, length, output)
                }
            }
        }
    }

    suspend fun stop() {
        loopJob?.cancelAndJoin()
    }

    private fun shouldAllowPacket(packet: ByteArray, length: Int): Boolean {
        val ownerUid = resolveUidForPacket(packet, length) ?: return false
        val mode = policyStore.getGlobalMode()

        return when (mode) {
            GlobalMode.BLOCK_ALL -> false
            GlobalMode.ALLOW_ONLY_SELECTED -> policyStore.getAllowedUids().contains(ownerUid)
        }
    }

    private fun resolveUidForPacket(packet: ByteArray, length: Int): Int? {
        if (length < 1) return null
        val version = packet[0].toInt().ushr(4)

        return when (version) {
            4 -> resolveIpv4Uid(packet, length)
            6 -> resolveIpv6Uid(packet, length)
            else -> null
        }
    }

    private fun resolveIpv4Uid(packet: ByteArray, length: Int): Int? {
        if (length < IPV4_MIN_HEADER_BYTES) return null

        val protocol = packet[9].toInt() and 0xFF
        val srcIp = InetAddress.getByAddress(packet.copyOfRange(12, 16))
        val dstIp = InetAddress.getByAddress(packet.copyOfRange(16, 20))
        val headerLength = (packet[0].toInt() and 0x0F) * 4
        val srcPort = readPort(packet, headerLength)
        val dstPort = readPort(packet, headerLength + 2)

        return resolveUid(srcIp, dstIp, srcPort, dstPort, protocol)
    }

    private fun resolveIpv6Uid(packet: ByteArray, length: Int): Int? {
        if (length < IPV6_MIN_HEADER_BYTES) return null

        val protocol = packet[6].toInt() and 0xFF
        val srcIp = InetAddress.getByAddress(packet.copyOfRange(8, 24))
        val dstIp = InetAddress.getByAddress(packet.copyOfRange(24, 40))
        val srcPort = readPort(packet, IPV6_MIN_HEADER_BYTES)
        val dstPort = readPort(packet, IPV6_MIN_HEADER_BYTES + 2)

        return resolveUid(srcIp, dstIp, srcPort, dstPort, protocol)
    }

    private fun resolveUid(
        srcIp: InetAddress,
        dstIp: InetAddress,
        srcPort: Int,
        dstPort: Int,
        protocol: Int
    ): Int? {
        if (srcPort < 0 || dstPort < 0) return null

        val ipProto = when (protocol) {
            OsConstants.IPPROTO_TCP -> OsConstants.IPPROTO_TCP
            OsConstants.IPPROTO_UDP -> OsConstants.IPPROTO_UDP
            else -> return null
        }

        val connectivityManager = vpnService.getSystemService(ConnectivityManager::class.java)
        return runCatching {
            connectivityManager.getConnectionOwnerUid(ipProto, srcIp, srcPort, dstIp, dstPort)
        }.getOrNull()
    }

    private fun readPort(packet: ByteArray, offset: Int): Int {
        if (offset < 0 || packet.size <= offset + 1) return -1
        return ((packet[offset].toInt() and 0xFF) shl 8) or (packet[offset + 1].toInt() and 0xFF)
    }

    companion object {
        private const val IPV4_MIN_HEADER_BYTES = 20
        private const val IPV6_MIN_HEADER_BYTES = 40
    }
}

/**
 * Production note: replace passthrough with full userspace forwarding (TCP/UDP session tracking,
 * protected sockets via `VpnService.protect`, NAT mapping, retransmit handling, and idle cleanup).
 */
internal class UserSpaceForwarder(private val vpnService: VpnService) {
    fun forward(packet: ByteArray, length: Int, output: FileOutputStream) {
        output.write(packet, 0, length)
    }
}

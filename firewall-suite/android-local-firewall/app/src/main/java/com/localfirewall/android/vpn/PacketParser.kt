package com.localfirewall.android.vpn

import java.nio.ByteBuffer

object PacketParser {
    fun parseIpv4(packet: ByteBuffer): Ipv4Packet? {
        if (packet.remaining() < 20) return null

        val start = packet.position()
        val versionIhl = packet.get(start).toInt() and 0xFF
        val version = versionIhl ushr 4
        if (version != 4) return null

        val ihlBytes = (versionIhl and 0x0F) * 4
        if (packet.remaining() < ihlBytes + 4) return null

        val protocol = packet.get(start + 9).toInt() and 0xFF

        val srcIp = (0..3).joinToString(".") { i -> (packet.get(start + 12 + i).toInt() and 0xFF).toString() }
        val dstIp = (0..3).joinToString(".") { i -> (packet.get(start + 16 + i).toInt() and 0xFF).toString() }

        var srcPort: Int? = null
        var dstPort: Int? = null

        if (protocol == 6 || protocol == 17) { // TCP or UDP
            srcPort = ((packet.get(start + ihlBytes).toInt() and 0xFF) shl 8) or
                (packet.get(start + ihlBytes + 1).toInt() and 0xFF)
            dstPort = ((packet.get(start + ihlBytes + 2).toInt() and 0xFF) shl 8) or
                (packet.get(start + ihlBytes + 3).toInt() and 0xFF)
        }

        return Ipv4Packet(srcIp, dstIp, protocol, srcPort, dstPort)
    }
}

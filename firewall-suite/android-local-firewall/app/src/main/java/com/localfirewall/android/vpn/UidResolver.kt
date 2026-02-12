package com.localfirewall.android.vpn

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.system.OsConstants
import java.net.InetSocketAddress

class UidResolver(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun resolveUid(packet: Ipv4Packet): Int? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null

        val protocol = when (packet.protocol) {
            6 -> OsConstants.IPPROTO_TCP
            17 -> OsConstants.IPPROTO_UDP
            else -> return null
        }

        val localPort = packet.srcPort ?: return null
        val remotePort = packet.dstPort ?: return null

        val local = InetSocketAddress(packet.srcIp, localPort)
        val remote = InetSocketAddress(packet.dstIp, remotePort)

        return connectivityManager.getConnectionOwnerUid(protocol, local, remote)
    }
}

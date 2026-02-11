package com.localfirewall.android.vpn

data class Ipv4Packet(
    val srcIp: String,
    val dstIp: String,
    val protocol: Int,
    val srcPort: Int?,
    val dstPort: Int?
)

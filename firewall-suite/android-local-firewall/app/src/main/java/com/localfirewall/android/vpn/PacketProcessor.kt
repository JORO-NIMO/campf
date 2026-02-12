package com.localfirewall.android.vpn

import com.localfirewall.android.data.PolicyRepository
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class PacketProcessor(
    private val tunIn: FileInputStream,
    private val tunOut: FileOutputStream,
    private val uidResolver: UidResolver,
    private val policyRepository: PolicyRepository
) {
    private val buffer = ByteBuffer.allocate(32767)

    fun processForever() {
        while (true) {
            buffer.clear()
            val bytesRead = tunIn.read(buffer.array())
            if (bytesRead <= 0) continue

            buffer.limit(bytesRead)
            val packet = PacketParser.parseIpv4(buffer) ?: continue
            val uid = uidResolver.resolveUid(packet)

            val allow = uid != null && policyRepository.shouldAllow(uid)
            if (allow) {
                // Allowed packet is written back to VPN stack for routing.
                tunOut.write(buffer.array(), 0, bytesRead)
            }
            // else drop silently
        }
    }
}

package com.localfirewall.android.vpn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.localfirewall.android.data.PolicyRepository
import com.localfirewall.android.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

class FirewallVpnService : VpnService() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())
        startVpn()
        return START_STICKY
    }

    override fun onDestroy() {
        vpnInterface?.close()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun startVpn() {
        val builder = Builder()
            .setSession("LocalFirewall")
            .addAddress("10.10.0.2", 24)
            .addRoute("0.0.0.0", 0)
            .setMtu(1500)

        vpnInterface = builder.establish() ?: return
        val fd = vpnInterface ?: return

        val policyRepository = PolicyRepository(applicationContext)
        val processor = PacketProcessor(
            tunIn = FileInputStream(fd.fileDescriptor),
            tunOut = FileOutputStream(fd.fileDescriptor),
            uidResolver = UidResolver(applicationContext),
            policyRepository = policyRepository
        )

        serviceScope.launch {
            processor.processForever()
        }
    }

    private fun buildNotification(): Notification {
        val channelId = "firewall_active"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Firewall",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Firewall active")
            .setContentText("Only allowed apps can access internet")
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 77
    }
}

package com.example.localfirewall.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.example.localfirewall.R
import com.example.localfirewall.data.UidPolicyStore
import com.example.localfirewall.ui.MainActivity
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
    private var processor: PacketProcessor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        startVpnIfNeeded()
        return START_STICKY
    }

    override fun onDestroy() {
        serviceScope.launch { stopVpn() }
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun startVpnIfNeeded() {
        if (vpnInterface != null) return

        vpnInterface = Builder()
            .setSession("LocalFirewall")
            .setMtu(1500)
            .addAddress("10.9.0.2", 24)
            .addDnsServer("1.1.1.1")
            .addRoute("0.0.0.0", 0)
            .addRoute("::", 0)
            .establish() ?: return

        val fd = vpnInterface?.fileDescriptor ?: return
        val input = FileInputStream(fd)
        val output = FileOutputStream(fd)
        processor = PacketProcessor(this, UidPolicyStore(this), input, output)
        processor?.start()
    }

    private suspend fun stopVpn() {
        processor?.stop()
        processor = null
        vpnInterface?.close()
        vpnInterface = null
    }

    private fun buildNotification(): Notification {
        val openIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shield)
            .setContentTitle("Firewall active")
            .setContentText("Filtering app network access locally")
            .setOngoing(true)
            .setContentIntent(openIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "Local Firewall", NotificationManager.IMPORTANCE_LOW)
        )
    }

    companion object {
        private const val CHANNEL_ID = "firewall_channel"
        private const val NOTIFICATION_ID = 8
    }
}

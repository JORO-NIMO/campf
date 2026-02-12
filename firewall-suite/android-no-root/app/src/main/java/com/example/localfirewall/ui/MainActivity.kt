package com.example.localfirewall.ui

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localfirewall.R
import com.example.localfirewall.core.FirewallVpnService
import com.example.localfirewall.data.GlobalMode
import com.example.localfirewall.data.InstalledAppsRepository
import com.example.localfirewall.data.UidPolicyStore

class MainActivity : AppCompatActivity() {

    private lateinit var policyStore: UidPolicyStore
    private lateinit var appsRepository: InstalledAppsRepository
    private lateinit var adapter: AppPolicyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        policyStore = UidPolicyStore(this)
        appsRepository = InstalledAppsRepository(this)

        val modeGroup = findViewById<RadioGroup>(R.id.modeGroup)
        val startBtn = findViewById<Button>(R.id.startFirewallButton)
        val stopBtn = findViewById<Button>(R.id.stopFirewallButton)
        val recycler = findViewById<RecyclerView>(R.id.appsRecyclerView)

        adapter = AppPolicyAdapter(emptyList()) { app, checked ->
            policyStore.setUidAllowed(app.uid, checked)
            loadApps()
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        modeGroup.check(
            if (policyStore.getGlobalMode() == GlobalMode.ALLOW_ONLY_SELECTED) {
                R.id.modeAllowSelected
            } else {
                R.id.modeBlockAll
            }
        )

        modeGroup.setOnCheckedChangeListener { _, checkedId ->
            val mode = if (checkedId == R.id.modeAllowSelected) {
                GlobalMode.ALLOW_ONLY_SELECTED
            } else {
                GlobalMode.BLOCK_ALL
            }
            policyStore.setGlobalMode(mode)
        }

        startBtn.setOnClickListener { showConsentThenStart() }
        stopBtn.setOnClickListener { stopService(Intent(this, FirewallVpnService::class.java)) }

        loadApps()
    }

    private fun showConsentThenStart() {
        AlertDialog.Builder(this)
            .setTitle("Enable local firewall")
            .setMessage(
                "This app uses Android VPN permission locally to filter by app. " +
                    "No traffic is sent to external VPN servers."
            )
            .setPositiveButton("Continue") { _, _ ->
                val prepareIntent = VpnService.prepare(this)
                if (prepareIntent != null) startActivityForResult(prepareIntent, REQUEST_VPN)
                else startService(Intent(this, FirewallVpnService::class.java))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadApps() {
        val allowedUids = policyStore.getAllowedUids()
        adapter.submitList(appsRepository.listApps(allowedUids))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VPN && resultCode == RESULT_OK) {
            startService(Intent(this, FirewallVpnService::class.java))
        }
    }

    companion object {
        private const val REQUEST_VPN = 1100
    }
}

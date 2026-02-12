package com.localfirewall.android.ui

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.localfirewall.android.R
import com.localfirewall.android.data.FirewallMode
import com.localfirewall.android.data.PolicyRepository
import com.localfirewall.android.vpn.FirewallVpnService

class MainActivity : AppCompatActivity() {
    private lateinit var policyRepository: PolicyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        policyRepository = PolicyRepository(this)

        setupModeSpinner()
        setupAppList(findViewById(R.id.apps_list))
        findViewById<Button>(R.id.start_firewall_button).setOnClickListener { startFirewall() }
        findViewById<Button>(R.id.stop_firewall_button).setOnClickListener {
            stopService(Intent(this, FirewallVpnService::class.java))
        }
    }

    private fun setupModeSpinner() {
        val spinner = findViewById<Spinner>(R.id.mode_spinner)
        val modes = listOf("Allow only selected apps", "Block all apps")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, modes)
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val mode = if (position == 0) FirewallMode.ALLOW_ONLY_SELECTED else FirewallMode.BLOCK_ALL
                policyRepository.setMode(mode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun setupAppList(listView: ListView) {
        // Placeholder list. Replace with PackageManager query + per-row switch.
        val appDisplay = listOf("Browser - Allow", "Maps - Block", "Messenger - Block")
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, appDisplay)
    }

    private fun startFirewall() {
        val prepareIntent = VpnService.prepare(this)
        if (prepareIntent != null) {
            startActivity(prepareIntent)
            return
        }

        startService(Intent(this, FirewallVpnService::class.java))
    }
}

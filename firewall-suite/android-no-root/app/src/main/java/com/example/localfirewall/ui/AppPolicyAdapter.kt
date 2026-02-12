package com.example.localfirewall.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.localfirewall.R
import com.example.localfirewall.model.AppPolicyItem

class AppPolicyAdapter(
    private var items: List<AppPolicyItem>,
    private val onToggle: (AppPolicyItem, Boolean) -> Unit
) : RecyclerView.Adapter<AppPolicyAdapter.AppPolicyViewHolder>() {

    fun submitList(newItems: List<AppPolicyItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppPolicyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_policy, parent, false)
        return AppPolicyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppPolicyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AppPolicyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val appNameText: TextView = view.findViewById(R.id.appNameText)
        private val packageText: TextView = view.findViewById(R.id.packageText)
        private val allowSwitch: Switch = view.findViewById(R.id.allowSwitch)

        fun bind(item: AppPolicyItem) {
            appNameText.text = item.appName
            packageText.text = item.packageName
            allowSwitch.setOnCheckedChangeListener(null)
            allowSwitch.isChecked = item.allowInternet
            allowSwitch.setOnCheckedChangeListener { _, checked ->
                onToggle(item, checked)
            }
        }
    }
}

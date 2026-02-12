package com.example.localfirewall.data

import android.content.Context
import android.content.SharedPreferences

enum class GlobalMode {
    ALLOW_ONLY_SELECTED,
    BLOCK_ALL
}

class UidPolicyStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("firewall_prefs", Context.MODE_PRIVATE)

    fun getAllowedUids(): Set<Int> =
        prefs.getStringSet(KEY_ALLOWED_UIDS, emptySet())
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()

    fun setUidAllowed(uid: Int, allowed: Boolean) {
        val current = getAllowedUids().toMutableSet()
        if (allowed) current.add(uid) else current.remove(uid)
        prefs.edit().putStringSet(KEY_ALLOWED_UIDS, current.map { it.toString() }.toSet()).apply()
    }

    fun getGlobalMode(): GlobalMode =
        runCatching {
            val raw = prefs.getString(KEY_GLOBAL_MODE, GlobalMode.ALLOW_ONLY_SELECTED.name)
            GlobalMode.valueOf(raw!!)
        }.getOrDefault(GlobalMode.ALLOW_ONLY_SELECTED)

    fun setGlobalMode(mode: GlobalMode) {
        prefs.edit().putString(KEY_GLOBAL_MODE, mode.name).apply()
    }

    companion object {
        private const val KEY_ALLOWED_UIDS = "allowed_uids"
        private const val KEY_GLOBAL_MODE = "global_mode"
    }
}

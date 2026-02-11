package com.localfirewall.android.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In production, back this with DataStore.
 */
class PolicyRepository(context: Context) {
    private val allowedUids = mutableSetOf<Int>()

    private val _mode = MutableStateFlow(FirewallMode.ALLOW_ONLY_SELECTED)
    val mode: StateFlow<FirewallMode> = _mode.asStateFlow()

    fun setMode(mode: FirewallMode) {
        _mode.value = mode
    }

    fun setAppAllowed(uid: Int, allowed: Boolean) {
        if (allowed) allowedUids.add(uid) else allowedUids.remove(uid)
    }

    fun snapshotAllowedUids(): Set<Int> = allowedUids.toSet()

    fun shouldAllow(uid: Int): Boolean {
        return when (_mode.value) {
            FirewallMode.BLOCK_ALL -> false
            FirewallMode.ALLOW_ONLY_SELECTED -> allowedUids.contains(uid)
        }
    }
}

package com.localfirewall.android.ui

data class AppEntry(
    val uid: Int,
    val packageName: String,
    val appName: String,
    var allowed: Boolean
)

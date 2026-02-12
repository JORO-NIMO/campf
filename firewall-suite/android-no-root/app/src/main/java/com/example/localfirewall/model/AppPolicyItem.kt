package com.example.localfirewall.model

data class AppPolicyItem(
    val appName: String,
    val packageName: String,
    val uid: Int,
    val allowInternet: Boolean
)

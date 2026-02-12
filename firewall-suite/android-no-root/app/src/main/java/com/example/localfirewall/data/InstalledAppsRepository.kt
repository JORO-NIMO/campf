package com.example.localfirewall.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Process
import com.example.localfirewall.model.AppPolicyItem

class InstalledAppsRepository(private val context: Context) {
    fun listApps(allowedUids: Set<Int>): List<AppPolicyItem> {
        val pm = context.packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .asSequence()
            .filter { info ->
                pm.getLaunchIntentForPackage(info.packageName) != null &&
                    info.uid >= Process.FIRST_APPLICATION_UID &&
                    (info.flags and ApplicationInfo.FLAG_SYSTEM) == 0
            }
            .map { info ->
                AppPolicyItem(
                    appName = pm.getApplicationLabel(info).toString(),
                    packageName = info.packageName,
                    uid = info.uid,
                    allowInternet = allowedUids.contains(info.uid)
                )
            }
            .sortedBy { it.appName.lowercase() }
            .toList()
    }
}

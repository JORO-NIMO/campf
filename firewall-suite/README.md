# Local Firewall Android App (Play Store package-ready scaffold)

This repository now focuses on the Android app required for Play Store upload preparation.

## What was removed as unneeded for Play Store upload

- Windows WFP desktop prototype files.
- Separate remaining-work checklist file (merged into this document).

## Folder structure

```text
firewall-suite/
├── README.md
└── android-no-root/
    ├── build.gradle.kts
    ├── gradle.properties
    ├── settings.gradle.kts
    └── app/
        ├── build.gradle.kts
        ├── proguard-rules.pro
        └── src/main/
            ├── AndroidManifest.xml
            ├── java/com/example/localfirewall/
            │   ├── core/
            │   │   ├── FirewallVpnService.kt
            │   │   └── PacketProcessor.kt
            │   ├── data/
            │   │   ├── InstalledAppsRepository.kt
            │   │   └── UidPolicyStore.kt
            │   ├── model/
            │   │   └── AppPolicyItem.kt
            │   └── ui/
            │       ├── AppPolicyAdapter.kt
            │       └── MainActivity.kt
            └── res/
                ├── drawable/ic_shield.xml
                ├── layout/activity_main.xml
                ├── layout/item_app_policy.xml
                ├── mipmap-anydpi-v26/ic_launcher.xml
                └── values/
                    ├── strings.xml
                    └── themes.xml
```

## Implemented APIs and components

### Android APIs used
- `VpnService` (`Builder`, `prepare`) for local VPN lifecycle.
- `ConnectivityManager.getConnectionOwnerUid` for UID ownership resolution.
- Foreground service + persistent notification for active firewall state.
- RecyclerView + Switch controls for per-app allowlist management.
- SharedPreferences policy storage for app UID allowlist and global mode.

### Core behavior implemented
- App listing and per-app allow/block toggles.
- Global mode:
  - Allow only selected apps
  - Block all apps
- Start/Stop firewall controls.
- Explicit user consent dialog before VPN start.
- IPv4 + IPv6 header parsing for UID lookup pipeline.
- Default deny behavior if UID cannot be determined.

## Permissions
- `INTERNET`
- `FOREGROUND_SERVICE`
- `POST_NOTIFICATIONS`
- `QUERY_ALL_PACKAGES`
- `BIND_VPN_SERVICE` (for VPN service declaration)

## Build in this container (offline-safe)

```bash
cd firewall-suite/android-no-root
gradle :app:assembleDebug
```

This repository includes an offline-safe Gradle task for container validation where access to Google Maven is blocked.
For real APK/AAB generation, open in Android Studio and run a standard Android build with AGP + Android SDK installed.

## Play Store readiness checklist

### Implemented here
- Project Gradle structure for Android App Bundle/APK builds.
- Release build type + ProGuard file.
- Foreground service declaration.
- User-facing consent and local-only privacy text.

### Still mandatory before publishing to production users
- Replace `UserSpaceForwarder` passthrough with full userspace TCP/UDP forwarding using protected sockets.
- Add robust crash reporting and QA matrix (OEM/device/API testing).
- Add Data safety form + Privacy Policy URL in Play Console.
- Add proper app signing, Play App Signing enrollment, and internal testing rollout.

## Privacy and security guarantees
- No HTTPS interception/decryption.
- No external VPN/proxy tunnel.
- Local policy enforcement on device.

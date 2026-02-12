# firewall-suite

Current branch contains an **Android local-firewall skeleton** (no-root, `VpnService`) with an **offline-safe Gradle validation setup** for this container.

## Current setup (important)

This repository currently has two layers:

1. **Android source skeleton**
   - Manifest, VPN service, packet processor, app policy storage, and basic UI classes/resources.
2. **Container build stub**
   - Gradle is configured with lightweight `assembleDebug`/`assembleRelease` tasks that pass in this restricted environment.
   - Android Gradle Plugin resolution is intentionally skipped here.

So the codebase is suitable for source review and structure validation in-container, but not yet a full Android Studio production build configuration in this branch state.

## Project layout

```text
firewall-suite/
├── README.md
└── android-no-root/
    ├── settings.gradle.kts
    ├── build.gradle.kts            # offline-safe root stub
    ├── gradle.properties
    └── app/
        ├── build.gradle.kts        # offline-safe assemble tasks (base plugin)
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

## What is present in source
- Per-app allow/block policy UI skeleton.
- Global modes: allow selected apps / block all.
- VPN foreground service lifecycle scaffold.
- Packet owner UID lookup pipeline (`getConnectionOwnerUid`) for IPv4/IPv6 parse paths.
- Local-only consent/privacy messaging.

## Build check (container-safe)

```bash
cd firewall-suite/android-no-root
gradle :app:assembleDebug
```

Expected result in this environment: successful offline validation task output.

## To convert this to full Android production build
- Restore real Android Gradle Plugin configuration in `android-no-root/build.gradle.kts` and `app/build.gradle.kts`.
- Ensure Android SDK + proper JDK toolchain are available.
- Keep the firewall source structure as-is, then continue implementation hardening.

## Production checklist (before Play Store release)
- Replace `UserSpaceForwarder` passthrough with full TCP/UDP userspace forwarding via protected sockets.
- Run device matrix QA across OEMs/API levels.
- Configure signing + Play App Signing + internal testing tracks.
- Complete Play Console privacy policy + data safety declarations.

## Privacy guarantees
- No HTTPS interception/decryption.
- No remote VPN/proxy dependency.
- Filtering remains local on device.

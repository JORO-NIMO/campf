# Local Device Firewall Suite

This branch currently tracks the **Android local firewall implementation**.

> Merge note: if `main` references both Android and Windows components, keep this file as the conflict resolution source for this branch because only Android files are present here.

## High-level behavior

- User selects allowed apps.
- Global mode can be:
  - **Allow only selected apps**
  - **Block all apps**
- Firewall engine blocks disallowed app traffic locally.
- UI can be closed while enforcement continues through a foreground VPN service.

## Current branch scope

- ✅ Android implementation exists under `android-no-root/`.
- ❌ Windows implementation is not present in this branch.

## Project layout

```text
firewall-suite/
├── README.md
└── android-no-root/
    ├── settings.gradle.kts
    ├── build.gradle.kts
    ├── gradle.properties
    └── app/
        ├── build.gradle.kts
        ├── proguard-rules.pro
        └── src/main/
            ├── AndroidManifest.xml
            ├── java/com/example/localfirewall/
            │   ├── core/
            │   ├── data/
            │   ├── model/
            │   └── ui/
            └── res/
```

## Build check (container-safe)

```bash
cd firewall-suite/android-no-root
gradle :app:assembleDebug
```

## Production checklist (before Play Store release)

- Replace `UserSpaceForwarder` passthrough with full TCP/UDP userspace forwarding via protected sockets.
- Restore full Android Gradle Plugin setup for normal APK/AAB builds.
- Run device matrix QA across OEM/API levels.
- Configure signing + Play App Signing + testing tracks.
- Complete Play Console privacy policy + data safety declarations.

## Privacy guarantees

- No HTTPS interception/decryption.
- No remote VPN/proxy dependency.
- Filtering remains local on device.

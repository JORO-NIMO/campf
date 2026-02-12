# firewall-suite

Android local firewall project (no-root) using `VpnService` for on-device traffic filtering.

## Merge-conflict resolution note
This README is intentionally concise and Android-focused to avoid recurring merge conflicts with older dual-platform text.

## Scope decision
- Current branch scope: **Android implementation only**.
- Older references to `windows-local-firewall` / dual-platform layout are intentionally removed from this branch.

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

## What is implemented
- Per-app allow/block policy UI.
- Global modes: allow selected apps / block all.
- VPN foreground service lifecycle.
- Packet owner UID lookup pipeline (IPv4/IPv6 parse + `getConnectionOwnerUid`).
- Local-only consent/privacy messaging.

## Build (container-safe)

```bash
cd firewall-suite/android-no-root
gradle :app:assembleDebug
```

## Production checklist (before Play Store release)
- Replace `UserSpaceForwarder` passthrough with full TCP/UDP userspace forwarding via protected sockets.
- Run device matrix QA across OEMs/API levels.
- Configure signing + Play App Signing + internal testing tracks.
- Complete Play Console privacy policy + data safety declarations.

## Privacy guarantees
- No HTTPS interception/decryption.
- No remote VPN/proxy dependency.
- Filtering remains local on device.

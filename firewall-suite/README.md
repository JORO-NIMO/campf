# Local Device Firewall Suite

This folder contains two **local-only** firewall implementations:

1. `android-local-firewall` (Kotlin, Android `VpnService`, no root)
2. `windows-local-firewall` (C#, Windows Filtering Platform)

Both implementations are designed to enforce per-application internet access policy on-device, without external VPN/proxy servers, and without HTTPS interception.

## High-level behavior

- User selects allowed apps.
- Global mode can be:
  - **Allow only selected apps**
  - **Block all apps**
- Firewall engine blocks disallowed app traffic locally.
- UI can be closed while enforcement continues through a background service.

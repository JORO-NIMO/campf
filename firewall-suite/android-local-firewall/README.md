# Android Local Firewall (No Root)

## Architecture

- **UI Layer** (`ui/`): Lists installed apps, lets user toggle allow/block, start/stop firewall.
- **Data Layer** (`data/`): Stores app whitelist + global mode in `DataStore`.
- **VPN Layer** (`vpn/`): `VpnService` implementation reading packets from TUN fd, mapping packet -> UID, then allow/drop.
- **Foreground Control** (`service/`): Starts VPN service with persistent notification.

## Key classes

- `FirewallVpnService`: Establishes local TUN interface and runs packet loop.
- `PacketProcessor`: Parses IPv4 packets and resolves owner UID.
- `UidResolver`: Uses Android `ConnectivityManager` APIs (`getConnectionOwnerUid`) where supported.
- `PolicyRepository`: Maintains whitelist and mode.
- `MainActivity`: App list + mode + start/stop UI.

## Permissions

- `android.permission.INTERNET`
- `android.permission.FOREGROUND_SERVICE`
- `android.permission.QUERY_ALL_PACKAGES` (optional; can be replaced with targeted queries)
- `BIND_VPN_SERVICE` (service declaration permission)

## Build

```bash
./gradlew assembleDebug
```

Install:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

Run app, grant VPN consent, and press **Start Firewall**.

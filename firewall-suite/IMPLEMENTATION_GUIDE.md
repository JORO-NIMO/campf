# Local-Only Device Firewall Implementation Guide

## 1) Project folder structure

```text
firewall-suite/
├── README.md
├── IMPLEMENTATION_GUIDE.md
├── android-local-firewall/
│   ├── README.md
│   └── app/src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/localfirewall/android/
│       │   ├── data/
│       │   │   ├── FirewallMode.kt
│       │   │   └── PolicyRepository.kt
│       │   ├── ui/
│       │   │   ├── AppEntry.kt
│       │   │   └── MainActivity.kt
│       │   └── vpn/
│       │       ├── FirewallVpnService.kt
│       │       ├── Ipv4Packet.kt
│       │       ├── PacketParser.kt
│       │       ├── PacketProcessor.kt
│       │       └── UidResolver.kt
│       └── res/layout/activity_main.xml
└── windows-local-firewall/
    ├── README.md
    ├── Firewall.Core/
    │   ├── FirewallPolicyMode.cs
    │   ├── FirewallRuleModel.cs
    │   └── WfpEngine.cs
    ├── Firewall.Service/
    │   ├── FirewallPolicyStore.cs
    │   ├── FirewallWorker.cs
    │   └── Program.cs
    └── Firewall.Desktop/
        ├── MainWindow.xaml
        └── MainWindowViewModel.cs
```

## 2) Key classes and responsibilities

## Android

- `MainActivity`: Lists apps, mode selector, start/stop buttons.
- `PolicyRepository`: Stores allowlist + mode (`ALLOW_ONLY_SELECTED` / `BLOCK_ALL`).
- `FirewallVpnService`: Builds and owns local VPN/TUN interface, runs packet worker, shows foreground notification.
- `PacketParser`: Reads IPv4 headers and TCP/UDP ports.
- `UidResolver`: Maps packet sockets to UID using `ConnectivityManager.getConnectionOwnerUid`.
- `PacketProcessor`: Applies policy (allow/drop) per packet.

## Windows

- `MainWindowViewModel`: Discovers apps/processes and edits rule intents.
- `FirewallRuleModel`: App executable rule (`AllowInternet` boolean).
- `WfpEngine`: Local firewall backend; this sample uses `netsh advfirewall` (no remote servers). Can be swapped to direct WFP P/Invoke.
- `FirewallPolicyStore`: Reads persisted JSON policy.
- `FirewallWorker`: Service loop that enforces selected policy continuously.

## 3) Core firewall logic

## Android packet filtering flow (local VPN)

1. `VpnService.Builder` creates TUN (`addRoute(0.0.0.0/0)` means all IPv4 traffic enters tunnel).
2. Packet loop reads packet bytes from TUN fd.
3. Parse IPv4 + ports.
4. Resolve UID owner from socket tuple.
5. If UID is in allowlist and mode allows it -> allow.
6. Otherwise drop packet.

Important constraints:
- HTTPS remains encrypted (no MITM/decrypt).
- No packet forwarding to external VPN endpoint.
- Filtering remains fully local.

## Windows filtering flow

1. UI writes policy (`executablePath` + allow/block + global mode).
2. Service runs elevated and reads policy.
3. Service creates/removes local firewall rules per executable for inbound/outbound traffic.
4. Rules remain active even if UI exits.

## 4) UI structure

## Android

Single screen includes:
- Mode dropdown
- Installed app list with allow/block toggle per app
- Start Firewall / Stop Firewall buttons

Foreground notification displays active state.

## Windows

Main window includes:
- Mode dropdown
- Data grid with executable path and allow toggle
- Apply and Refresh buttons

Service remains active in background.

## 5) Permissions required

## Android

- `INTERNET`
- `FOREGROUND_SERVICE`
- `QUERY_ALL_PACKAGES` (or package visibility declarations)
- VPN service declaration with `android.permission.BIND_VPN_SERVICE`

## Windows

- Installer / service must request administrator privileges.
- Rule creation requires elevated rights.

## 6) Build and run

## Android

1. Open `android-local-firewall` in Android Studio.
2. Ensure min SDK/API supports your chosen UID mapping implementation.
3. Build and install debug APK.
4. Start app, grant VPN consent, press **Start Firewall**.

## Windows

1. Build `Firewall.Core`, `Firewall.Service`, and `Firewall.Desktop` with .NET SDK.
2. Install service as administrator.
3. Launch desktop UI, choose app allowlist/mode, click apply.
4. Close UI; service keeps enforcement.

## 7) Security and privacy guarantees

- No remote VPN servers.
- No proxying to external infrastructure.
- No HTTPS decryption/interception.
- No browsing telemetry collection in this design.
- Explicit user consent required before firewall activation.

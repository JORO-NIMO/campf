# Windows Local Firewall (WFP-based)

## Architecture

- `Firewall.Core`: Rule model and WFP facade.
- `Firewall.Service`: Windows service that applies persistent allow/block rules.
- `Firewall.Desktop`: WPF/WinUI UI to select apps and set policy mode.
- `Installer`: MSI/WiX scripts requesting admin rights and installing service.

## Key classes

- `FirewallRuleModel`: App executable path + action.
- `WfpEngine`: Opens WFP engine and creates/removes filters.
- `FirewallPolicyStore`: Persists selected app rules.
- `FirewallWorker`: Background loop in service that syncs desired policy to WFP.
- `MainWindowViewModel`: Enumerates apps and sends updates to service.

## Build

```powershell
dotnet build .\Firewall.Desktop\Firewall.Desktop.csproj
dotnet build .\Firewall.Service\Firewall.Service.csproj
```

Install service (admin):

```powershell
sc create LocalAppFirewall binPath= "C:\Program Files\LocalFirewall\Firewall.Service.exe"
sc start LocalAppFirewall
```

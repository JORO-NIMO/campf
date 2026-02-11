using System.Collections.ObjectModel;
using System.Diagnostics;
using LocalFirewall.Core;

namespace LocalFirewall.Desktop;

public sealed class MainWindowViewModel
{
    public ObservableCollection<FirewallRuleModel> Rules { get; } = new();
    public FirewallPolicyMode Mode { get; set; } = FirewallPolicyMode.AllowOnlySelected;

    public void LoadRunningApps()
    {
        Rules.Clear();

        var seen = new HashSet<string>(StringComparer.OrdinalIgnoreCase);
        foreach (var process in Process.GetProcesses())
        {
            try
            {
                var path = process.MainModule?.FileName;
                if (string.IsNullOrWhiteSpace(path) || !seen.Add(path)) continue;
                Rules.Add(new FirewallRuleModel { ExecutablePath = path, AllowInternet = false });
            }
            catch
            {
                // Access denied for protected process; ignore.
            }
        }
    }
}

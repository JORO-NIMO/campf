using System.Diagnostics;
using System.IO;

namespace LocalFirewall.Core;

/// <summary>
/// Minimal facade. Production version should call FWPM APIs (FwpmEngineOpen0, FwpmFilterAdd0, etc.) via P/Invoke.
/// This sample uses netsh AdvFirewall commands as an approachable local-only implementation.
/// </summary>
public sealed class WfpEngine
{
    public void ApplyRule(FirewallRuleModel rule)
    {
        var ruleName = RuleName(rule.ExecutablePath);

        DeleteRule(ruleName);

        if (!rule.AllowInternet)
        {
            Run(
                $"advfirewall firewall add rule name=\"{ruleName}\" dir=out action=block program=\"{rule.ExecutablePath}\" enable=yes profile=any"
            );
            Run(
                $"advfirewall firewall add rule name=\"{ruleName}_in\" dir=in action=block program=\"{rule.ExecutablePath}\" enable=yes profile=any"
            );
        }
    }

    public void ClearRule(string executablePath)
    {
        DeleteRule(RuleName(executablePath));
    }

    private static string RuleName(string executablePath) =>
        $"LocalAppFirewall::{Path.GetFileName(executablePath)}::{Math.Abs(executablePath.GetHashCode())}";

    private static void DeleteRule(string ruleName)
    {
        Run($"advfirewall firewall delete rule name=\"{ruleName}\"");
        Run($"advfirewall firewall delete rule name=\"{ruleName}_in\"");
    }

    private static void Run(string args)
    {
        using var process = Process.Start(new ProcessStartInfo
        {
            FileName = "netsh",
            Arguments = args,
            UseShellExecute = false,
            CreateNoWindow = true
        });
        process?.WaitForExit();
    }
}

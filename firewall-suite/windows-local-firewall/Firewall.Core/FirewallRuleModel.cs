namespace LocalFirewall.Core;

public sealed class FirewallRuleModel
{
    public required string ExecutablePath { get; init; }
    public bool AllowInternet { get; init; }
}

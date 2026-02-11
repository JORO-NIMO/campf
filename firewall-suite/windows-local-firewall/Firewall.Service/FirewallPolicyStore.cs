using System.Text.Json;
using System.IO;
using System.Collections.Generic;
using LocalFirewall.Core;

namespace LocalFirewall.Service;

public sealed class FirewallPolicyStore
{
    private readonly string policyPath;

    public FirewallPolicyStore(string policyPath)
    {
        this.policyPath = policyPath;
    }

    public async Task<PolicySnapshot> LoadAsync(CancellationToken cancellationToken)
    {
        if (!File.Exists(policyPath))
        {
            return new PolicySnapshot(FirewallPolicyMode.AllowOnlySelected, []);
        }

        await using var stream = File.OpenRead(policyPath);
        var snapshot = await JsonSerializer.DeserializeAsync<PolicySnapshot>(stream, cancellationToken: cancellationToken);
        return snapshot ?? new PolicySnapshot(FirewallPolicyMode.AllowOnlySelected, []);
    }
}

public sealed record PolicySnapshot(FirewallPolicyMode Mode, IReadOnlyList<FirewallRuleModel> Rules);

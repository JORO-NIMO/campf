using LocalFirewall.Core;

namespace LocalFirewall.Service;

public sealed class FirewallWorker : BackgroundService
{
    private readonly FirewallPolicyStore policyStore;
    private readonly WfpEngine wfpEngine;

    public FirewallWorker(FirewallPolicyStore policyStore, WfpEngine wfpEngine)
    {
        this.policyStore = policyStore;
        this.wfpEngine = wfpEngine;
    }

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            var snapshot = await policyStore.LoadAsync(stoppingToken);

            if (snapshot.Mode == FirewallPolicyMode.BlockAll)
            {
                foreach (var rule in snapshot.Rules)
                {
                    wfpEngine.ApplyRule(rule with { AllowInternet = false });
                }
            }
            else
            {
                foreach (var rule in snapshot.Rules)
                {
                    wfpEngine.ApplyRule(rule);
                }
            }

            await Task.Delay(TimeSpan.FromSeconds(3), stoppingToken);
        }
    }
}

using LocalFirewall.Core;
using LocalFirewall.Service;

var builder = Host.CreateApplicationBuilder(args);
builder.Services.AddWindowsService(options => options.ServiceName = "LocalAppFirewall");
builder.Services.AddSingleton(new FirewallPolicyStore(@"C:\ProgramData\LocalFirewall\policy.json"));
builder.Services.AddSingleton<WfpEngine>();
builder.Services.AddHostedService<FirewallWorker>();

await builder.Build().RunAsync();

/*
 * FILE:        EntryPoint.cs
 *                                                                      
 * DESCRIPTION: The file contains implementation for EntryPoint class.
 *
 * VERSION:     v0.1, updated Mar 2011.
 *
 * Copyright © 2011 Microsoft Corporation. All rights reserved.
 */

using System;
using System.Diagnostics;
using System.Net;
using System.Threading;
using Microsoft.WindowsAzure.Diagnostics;
using Microsoft.WindowsAzure.ServiceRuntime;
using Research.MapReduce.Core;

namespace Research.MapReduce.CloudHost.Slave
{
    /// <summary>
    /// Represents the entry point of a Slave node of a MapReduce deployment.
    /// </summary>
    public class EntryPoint : RoleEntryPoint
    {
        /// <summary>
        /// Main thread of execution of the Slave worker role instance.
        /// </summary>
        public override void Run()
        {
            // Initialize and run Slave.            
            string workingDirectory = RoleEnvironment.GetLocalResource("LocalStorage").RootPath;
            IPEndPoint slaveEndpoint = RoleEnvironment.CurrentRoleInstance.InstanceEndpoints
                ["Endpoint"].IPEndpoint;
            IPEndPoint masterEndpoint = RoleEnvironment.Roles["Research.MapReduce.CloudHost.Master"
                ].Instances[0].InstanceEndpoints["Endpoint"].IPEndpoint;
            int mapTaskSlotSize = int.Parse(RoleEnvironment.GetConfigurationSettingValue(
                "MapTaskSlotSize"));
            int reduceTaskSlotSize = int.Parse(RoleEnvironment.GetConfigurationSettingValue(
                "ReduceTaskSlotSize"));

            // Instantiate task tracker.
            TaskTracker slave = new TaskTracker(RoleEnvironment.CurrentRoleInstance.Id,
                workingDirectory, masterEndpoint, slaveEndpoint, mapTaskSlotSize,
                reduceTaskSlotSize, RoleEnvironment.GetConfigurationSettingValue(
                "StorageConnectionString"));
            slave.Start(); // Start the tracker.

            // Wait forever.
            while (true)
            {
                Thread.Sleep(30000);
            }
        }

        /// <summary>
        /// Initializes the Slave worker role instance.
        /// </summary>
        /// <returns>True if initialization succeeds, False if it fails.</returns>
        public override bool OnStart()
        {
            // Set the maximum number of concurrent connections 
            ServicePointManager.DefaultConnectionLimit = 12;

            // Initiaize azure diagnostics.
            DiagnosticMonitorConfiguration config = DiagnosticMonitor.GetDefaultInitialConfiguration();

            // The log level filtering is imposed at the trace source, so set it to the max
            // level here.
            config.Logs.ScheduledTransferLogLevelFilter = LogLevel.Verbose;
            config.Logs.ScheduledTransferPeriod = TimeSpan.FromMinutes(1);

            EnablePerfCounterCollection(config);
            DiagnosticMonitor.Start("DiagnosticsConnectionString", config);

            // For information on handling configuration changes
            // see the MSDN topic at http://go.microsoft.com/fwlink/?LinkId=166357.

            return base.OnStart();
        }

        [Conditional("DEBUG")]
        private void EnablePerfCounterCollection(DiagnosticMonitorConfiguration config)
        {
            config.PerformanceCounters.ScheduledTransferPeriod = TimeSpan.FromMinutes(1);
            // Performance counter path format:- PerfObject(ParentInstance/ObjectInstance#InstanceIndex)\Counter.
            // See the MSDN topic at http://msdn.microsoft.com/en-us/library/w8f5kw2e.aspx for more .NET performance counter
            // like '.NET CLR Exceptions', '.NET CLR Networking', '.NET CLR LocksAndThreads' etc.
            config.PerformanceCounters.DataSources.Add(new PerformanceCounterConfiguration()
            {
                CounterSpecifier = @"\.NET CLR Memory(_Global_)\*",
                SampleRate = TimeSpan.FromSeconds(5)
            });
        }
    }
}

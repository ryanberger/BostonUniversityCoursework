using System;
using System.Collections.Generic;
using System.Text;

namespace CS665Hw7
{
    class SimulationFacade
    {
        private static int _simLength, _avgServiceTime, _avgArrivalTime, _maxNumServers;
        private static SimulationFacade _uniqueInstance = new SimulationFacade();

        private SimulationFacade()
        { }

        public static SimulationFacade GetInstance(int simLength, int avgSeviceTime, int avgArrivalTime, int maxNumServers)
        {
            _simLength = simLength;
            _avgServiceTime = avgSeviceTime;
            _avgArrivalTime = avgArrivalTime;
            _maxNumServers = maxNumServers;
            return _uniqueInstance;
        }

        public void Run()
        {
            List<Server> serverList = new List<Server>();

            PrintHeader();

            for (int i = 1; i <= _maxNumServers; i++)
            {
                serverList.Add(new Server(_avgServiceTime));
                Statistics stats = Statistics.GetInstance();

                for (int currentTime = 1; currentTime <= _simLength; currentTime++)
                {
                    Entrance entrance = Entrance.GetInstance();
                    Queue queue = Queue.GetInstance();

                    // Check to see if new customer has arrived
                    if (entrance.CheckArrival(_avgArrivalTime))
                    {
                        queue.Enqueue(new Customer(currentTime));
                        stats.UpdateMaxQueueLength(queue.Size);

                        foreach (Server s in serverList)
                        {
                            // Check to see if server is available
                            if (s.IsAvailable(currentTime))
                            {
                                Customer removedCust = queue.Dequeue();

                                if (removedCust != null)
                                {
                                    // Serve customer and update statistics
                                    s.StartService(currentTime);
                                    stats.UpdateTotalWaitTime(removedCust.WaitTime(currentTime));
                                    stats.UpdateTotalNumServed();
                                }
                            }
                        }
                    }
                }
                stats.Print(i);
                stats.Clear();
            }
        }

        private void PrintHeader()
        {
            Console.WriteLine(string.Format("Simulation length: {0} sec", _simLength));
            Console.WriteLine(string.Format("Average service time: {0} sec", _avgServiceTime));
            Console.WriteLine(string.Format("Average arrival time: {0} sec", _avgArrivalTime));
        }
    }
}

using System;
using System.Collections.Generic;

namespace CS665Hw7
{
    public class Simulation
    {
        static void Main(string[] args)
        {
            SimulationFacade sim;

            sim = SimulationFacade.GetInstance(28800, 120, 10, 5);
            sim.Run();

            sim = SimulationFacade.GetInstance(28800, 120, 5, 5);
            sim.Run();

            sim = SimulationFacade.GetInstance(28800, 120, 6, 5);
            sim.Run();
        }

    }
}

using System;

namespace CS665Hw7
{
    public class Entrance
    {
        private static readonly Entrance _uniqueInstance = new Entrance();

        private Entrance()
        { }

        public static Entrance GetInstance()
        {
            return _uniqueInstance;
        }

        public bool CheckArrival(double interval)
        {
            double num = 0.0;
            double probability = 1 / interval;
            Random rand = new Random();

            // Returns a number between 0.0 and 1.0
            num = rand.NextDouble();

            if (num < probability)
            {
                return true;
            }
            return false;
        }
    }
}

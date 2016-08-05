using System;

namespace CS665Hw7
{
    public class Statistics
    {
        private int _totalNumServed = 0;
        private int _totalWaitTime = 0;
        private int _maxQueueLength = 0;
        private static readonly Statistics _uniqueInstance = new Statistics();

        private Statistics()
        { }

        public static Statistics GetInstance()
        {
            return _uniqueInstance;
        }

        public void UpdateTotalNumServed()
        {
            _totalNumServed++;
        }

        public void UpdateTotalWaitTime(int waitTime)
        {
            _totalWaitTime += waitTime;
        }

        public void UpdateMaxQueueLength(int queueLength)
        {
            if (queueLength > _maxQueueLength)
            {
                _maxQueueLength = queueLength;
            }
        }

        public void Print(int numServers)
        {
            if (_totalNumServed > 0)
            {
                string output = string.Format("Number of cashiers: {0}\n", numServers);
                output += string.Format("Served {0} customers\n", _totalNumServed);
                output += string.Format("Average wait time: {0} sec\n", _totalWaitTime / _totalNumServed);
                output += string.Format("Longest queue: {0} customers\n", _maxQueueLength);

                Console.Write(output);
            }
        }

        public void Clear()
        {
            _totalNumServed = 0; 
            _totalWaitTime = 0;
            _maxQueueLength = 0;
        }
    }
}

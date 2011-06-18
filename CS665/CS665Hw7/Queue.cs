using System.Collections.Generic;

namespace CS665Hw7
{
    public class Queue
    {
        private static readonly List<Customer> _customerList = new List<Customer>();
        private static readonly Queue _uniqueInstance = new Queue();

        private Queue()
        { }

        public static Queue GetInstance()
        {
            return _uniqueInstance;
        }

        public void Enqueue(Customer cust)
        {
            _customerList.Add(cust);
        }

        public Customer Dequeue()
        {
            Customer removedCust = null;

            if (_customerList.Count > 0)
            {
                removedCust = _customerList[0];
                _customerList.RemoveAt(0);
            }

            return removedCust;
        }

        public int Size
        {
            get { return _customerList.Count; }
        }
    }
}

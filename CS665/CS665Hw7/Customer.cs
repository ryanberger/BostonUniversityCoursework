
namespace CS665Hw7
{
    public class Customer
    {
        private readonly int _arrivalTime;

        public Customer(int arrivalTime)
        {
            _arrivalTime = arrivalTime;
        }

        public int WaitTime(int currentTime)
        {
            return currentTime - _arrivalTime;
        }

    }
}

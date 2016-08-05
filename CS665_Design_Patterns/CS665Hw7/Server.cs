
namespace CS665Hw7
{
    public class Server
    {
        private int _startTime = -1;
        private readonly int _serviceTime;

        public Server(int serviceTime)
        {
            _serviceTime = serviceTime;
        }

        public void StartService(int currentTime)
        {
            _startTime = currentTime;
        }

        public bool IsAvailable(int currentTime)
        {
            return _startTime + _serviceTime < currentTime;
        }

    }
}


namespace CS665Hw9
{
    public class OpenOrderState : State
    {
        private Order _order;

        public OpenOrderState(Order order)
        {
            _order = order;
        }

        public override string ToString()
        {
            return string.Format("{0}\t{1}\t{2}", _order.ItemNumber, _order.QuantityRequested, _order.QuantityRemaining);
        }

    }
}

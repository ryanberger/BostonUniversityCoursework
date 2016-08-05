
namespace CS665Hw9
{
    public class ClosedOrderState : State
    {
        private Order _order;

        public ClosedOrderState(Order order)
        {
            _order = order;
        }

        public override string ToString()
        {
            return string.Format("{0}\t{1}\tclosed", _order.ItemNumber, _order.QuantityRequested);
        }

    }
}


namespace CS665Hw9
{
    public class NewOrderState : State
    {
        private Order _order;

        public NewOrderState(Order order)
        {
            _order = order;
        }

        public override string ToString()
        {
            return string.Format("{0}\t{1}", _order.ItemNumber, _order.QuantityRequested);
        }
    }
}

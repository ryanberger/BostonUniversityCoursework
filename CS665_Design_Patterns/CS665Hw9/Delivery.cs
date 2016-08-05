
namespace CS665Hw9
{
    public class Delivery
    {
        private int _itemNumber, _quantityDelivered;

        public Delivery(int itemNumber, int quantityDelivered)
        {
            _itemNumber = itemNumber;
            _quantityDelivered = quantityDelivered;
        }

        public override string ToString()
        {
            return string.Format("{0}\t{1}", _itemNumber, _quantityDelivered);
        }

        public int ItemNumber
        {
            get { return _itemNumber; }
        }

        public int QuantityDelivered
        {
            get { return _quantityDelivered; }
        }

    }
}

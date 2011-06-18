
namespace CS665Hw9
{
    public class Item
    {
        private int _itemNumber, _quantityOnHand;
        private string _itemName;

        public Item(int itemNumber, string itemName, int quantityOnHand)
        {
            _itemNumber = itemNumber;
            _itemName = itemName;
            _quantityOnHand = quantityOnHand;
        }

        public override string ToString()
        {
            return string.Format("{0}\t{1}\t{2}", _itemNumber, _itemName, _quantityOnHand);
        }

        public int QuanityOnHand
        {
            get { return _quantityOnHand; }
            set { _quantityOnHand = value; }
        }

        public bool FindItem(int itemNumber)
        {
            return (_itemNumber == itemNumber);
        }

    }
}

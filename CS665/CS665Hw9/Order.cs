
namespace CS665Hw9
{
    public class Order
    {
        private State _state, _newOrderState, _openOrderState, _closedOrderState;
        private int _itemNumber, _quantityRequested, _newQuantityRequested;
        private int _quantityRemaining;

        public Order(int itemNumber, int quantityRequested)
        {
            _newOrderState = new NewOrderState(this);
            _openOrderState = new OpenOrderState(this);
            _closedOrderState = new ClosedOrderState(this);
            _state = _newOrderState;
            _itemNumber = itemNumber;
            _quantityRequested = quantityRequested;
            _newQuantityRequested = quantityRequested;
        }

        public override string ToString()
        {
            return _state.ToString();
        }

        public int ItemNumber
        {
            get { return _itemNumber; }
        }

        public int QuantityRequested
        {
            get { return _quantityRequested; }
        }

        public int QuantityRemaining
        {
            get { return _quantityRemaining; }
        }

        public int UpdateOrderStatus(int quantityOnHand)
        {
            // Don't process closed orders
            if (_state != _closedOrderState)
            {
                // Partial fulfillment
                if (_newQuantityRequested > quantityOnHand)
                {
                    _state = _openOrderState;
                    // Update quantity remaining for order
                    _quantityRemaining = _newQuantityRequested - quantityOnHand;
                    // Update new quantity requested
                    _newQuantityRequested -= quantityOnHand;
                    // Quantity on-hand has been exhausted
                    return 0;
                }
                else // full fulfillment
                {
                    _state = _closedOrderState;
                    // Update quantity remaining for order
                    _quantityRemaining = quantityOnHand - _newQuantityRequested;
                    // Quantity requested has been exhausted
                    _newQuantityRequested = 0;
                    return _quantityRemaining;
                }
            }
            return quantityOnHand;
        }

    }
}

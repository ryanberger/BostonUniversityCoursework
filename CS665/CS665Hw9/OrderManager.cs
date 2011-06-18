using System;
using System.Collections.Generic;

namespace CS665Hw9
{
    public class OrderManager
    {
        private List<Order> _orderList = new List<Order>();
        private List<Item> _itemList = new List<Item>();

        public OrderManager(List<Order> orderList, List<Item> itemList)
        {
            _orderList = orderList;
            _itemList = itemList;
        }

        public void ProcessOrders()
        {
            foreach (Order order in _orderList)
            {
                Item item = _itemList.Find(
                    delegate(Item i) 
                    { 
                        return i.FindItem(order.ItemNumber); 
                    }
                );

                item.QuanityOnHand = order.UpdateOrderStatus(item.QuanityOnHand);
            }
        }

        public void ProcessDeliveries(List<Delivery> deliveryList)
        {
            foreach (Delivery delivery in deliveryList)
            {
                Item item = _itemList.Find(
                    delegate(Item i)
                    {
                        return i.FindItem(delivery.ItemNumber);
                    }
                );

                item.QuanityOnHand += delivery.QuantityDelivered;
            }
        }

        public void PrintInventory(string caption)
        {
            Console.WriteLine(caption);

            foreach (Item item in _itemList)
            {
                Console.WriteLine(item.ToString());
            }
        }

        public void PrintOrders(string caption)
        {
            Console.WriteLine(caption);

            foreach (Order order in _orderList)
            {
                Console.WriteLine(order.ToString());
            }
        }

        public void PrintDeliveries(string caption, List<Delivery> deliveryList)
        {
            Console.WriteLine(caption);

            foreach (Delivery delivery in deliveryList)
            {
                Console.WriteLine(delivery.ToString());
            }
        }

    }
}

using System.Collections.Generic;

namespace CS665Hw9
{
    class OrderManagerDrive
    {
        static void Main(string[] args)
        {
            Item i1 = new Item(121, "Part1", 12);
            Item i2 = new Item(122, "Part2", 15);
            Item i3 = new Item(123, "Part3", 18);
            Item i4 = new Item(124, "Part4", 4);
            Item i5 = new Item(125, "Part5", 0);

            Order o1 = new Order(121, 6);
            Order o2 = new Order(122, 15);
            Order o3 = new Order(123, 28);
            Order o4 = new Order(124, 22);
            Order o5 = new Order(125, 2);
            Order o6 = new Order(125, 5);

            List<Item> itemList = new List<Item> { i1, i2, i3, i4, i5 };
            List<Order> orderList = new List<Order> { o1, o2, o3, o4, o5, o6 };
            
            OrderManager om = new OrderManager(orderList, itemList);

            om.PrintInventory("Initial Inventory");
            om.PrintOrders("Requested Items");

            om.ProcessOrders();
            om.PrintOrders("Closed and Partially Filled Orders");

            Delivery d1 = new Delivery(122, 4);
            Delivery d2 = new Delivery(123, 15);
            Delivery d3 = new Delivery(124, 5);
            Delivery d4 = new Delivery(125, 4);

            List<Delivery> deliveryList = new List<Delivery> { d1, d2, d3, d4 };

            om.PrintDeliveries("Item Delivery", deliveryList);
            om.ProcessDeliveries(deliveryList);
            om.ProcessOrders();
            om.PrintOrders("Orders After Delivery");
            om.PrintInventory("Final Inventory");

        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public abstract class Dessert : Dish
    {
    }

    public class CremeBrule : Dessert
    {
        public CremeBrule()
        {
            name = "Creme Brule";
            price = 1.20;
            calories = 200.0;
        }
    }

    public class IceCream : Dessert
    {
        public IceCream()
        {
            name = "Ice Cream";
            price = 1.50;
        }
    }
}

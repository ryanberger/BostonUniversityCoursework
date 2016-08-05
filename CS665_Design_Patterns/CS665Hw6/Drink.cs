using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public abstract class Drink : Dish
    {
    }

    public class VodkaAbsolute : Drink
    {
        public VodkaAbsolute()
        {
            name = "Vodka Absolute";
            price = 2.50;
            calories = 300.0;
        }
    }

    public class TapWater : Drink
    {
        public TapWater()
        {
            name = "Tap Water";
            price = 0.50;
        }
    }
}

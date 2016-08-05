using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public abstract class Dish
    {
        protected string name = "";
        protected double price = 0.0;
        protected double calories = 0.0;

        public string GetName()
        {
            return name;
        }

        public double GetPrice()
        {
            return price;
        }

        public double GetCalories()
        {
            return calories;
        }
    }
}

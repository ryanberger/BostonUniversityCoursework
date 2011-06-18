using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public abstract class IngredientFactory
    {
        protected Entree entree;

        protected Drink drink;

        protected Dessert dessert;

        protected double totalPrice = 0.0;

        public abstract void PlaceOrder();

        public abstract override string ToString();

        public double GetTotalPrice()
        {
            return totalPrice;
        }
    }
}

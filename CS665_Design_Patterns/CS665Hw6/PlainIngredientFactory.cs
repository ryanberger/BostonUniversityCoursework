using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public class PlainIngredientFactory : IngredientFactory
    {
        public override void PlaceOrder()
        {
            entree = new PlainEntree();
            drink = new TapWater();
            dessert = new IceCream();
            totalPrice += entree.GetPrice() + drink.GetPrice() + dessert.GetPrice();
        }

        public override string ToString()
        {
            string output = "---- Plain Style Dinner ----\n";
            output += string.Format("{0}: ${1:0.00}\n", entree.GetName(), entree.GetPrice());
            output += string.Format("{0}: ${1:0.00}\n", drink.GetName(), drink.GetPrice());
            output += string.Format("{0}: ${1:0.00}\n", dessert.GetName(), dessert.GetPrice());
            output += string.Format("Dinner price: ${0:0.00}\n",
                entree.GetPrice() + drink.GetPrice() + dessert.GetPrice());
            output += string.Format("Total price of plain dinners: ${0:0.00}\n", totalPrice);
            return output;
        }
    }
}

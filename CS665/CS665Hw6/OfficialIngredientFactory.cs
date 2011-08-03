using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public class OfficialIngredientFactory : IngredientFactory
    {
        public override void PlaceOrder()
        {
            entree = new OfficialEntree();
            drink = new VodkaAbsolute();
            dessert = new CremeBrule();
            totalPrice += entree.GetPrice() + drink.GetPrice() + dessert.GetPrice();
        }

        public override string ToString()
        {
            string output = "---- Official Style Dinner ----\n";
            output += string.Format("{0}: {1:0.0} calories, ${2:0.00}\n", entree.GetName(), entree.GetCalories(), entree.GetPrice());
            output += string.Format("{0}: {1:0.0} calories, ${2:0.00}\n", drink.GetName(), drink.GetCalories(), drink.GetPrice());
            output += string.Format("{0}: {1:0.0} calories, ${2:0.00}\n", dessert.GetName(), dessert.GetCalories(), dessert.GetPrice());
            output += string.Format("Dinner price: ${0:0.00}\t Total calories: {1:0.0}\n", 
                entree.GetPrice() + drink.GetPrice() + dessert.GetPrice(), 
                entree.GetCalories() + drink.GetCalories() + dessert.GetCalories());
            output += string.Format("Total price of official dinners: ${0:0.00}\n", totalPrice);
            return output;
        }
    }
}

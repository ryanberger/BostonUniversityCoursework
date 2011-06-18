using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    public abstract class Entree : Dish
    {
    }

    public class OfficialEntree : Entree
    {
        public OfficialEntree()
        {
            name = "Official Entry";
            price = 6.50;
            calories = 1500.0;
        }
    }

    public class PlainEntree : Entree
    {
        public PlainEntree()
        {
            name = "Plain Entry";
            price = 12.00;
        }
    }
}

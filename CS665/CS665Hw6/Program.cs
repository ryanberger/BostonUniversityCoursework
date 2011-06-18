using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CS665Hw6
{
    class Program
    {
        static void Main(string[] args)
        {
            int numOfficial, numPlain;

            Console.Write("Enter the number of official dinners to prepare: ");
            int.TryParse(Console.ReadLine(), out numOfficial);
            Console.Write("Enter the number of plain dinners to prepare: ");
            int.TryParse(Console.ReadLine(), out numPlain);

            IngredientFactory officialFactory = new OfficialIngredientFactory();
            IngredientFactory plainFactory = new PlainIngredientFactory();

            for (int i = 0; i < numOfficial; i++)
            {
                officialFactory.PlaceOrder();
            }
            for (int j = 0; j < numPlain; j++)
            {
                plainFactory.PlaceOrder();
            }

            Print(officialFactory, plainFactory, numOfficial, numPlain);
        }

        private static void Print(IngredientFactory officialFactory, 
            IngredientFactory plainFactory, int numOfficial, int numPlain)
        {
            Console.WriteLine();

            if (numOfficial > 0)
                Console.WriteLine(officialFactory.ToString());
            if (numPlain > 0)
                Console.WriteLine(plainFactory.ToString());

            Console.WriteLine(string.Format("Total order (no taxes): ${0:0.00}", 
                officialFactory.GetTotalPrice() + plainFactory.GetTotalPrice()));
        }
    }
}

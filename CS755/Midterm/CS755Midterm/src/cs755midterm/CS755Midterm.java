package cs755midterm;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.lang.Exception.*;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ryanberger
 */
public class CS755Midterm {
    
    static Double CROSSOVER_RATE = 0.7;
    static Double MUTATION_RATE = 0.001;
    static int POP_SIZE = 100;			//must be an even number
    static int CHROMO_LENGTH = 300;
    static int GENE_LENGTH = 4;
    static int MAX_ALLOWABLE_GENERATIONS = 400;

    static void Main(string[] args)
    {
        // overall champ;
        chromo_typ ultimatechamp = new chromo_typ(); 

        // just loop endlessly until user gets bored :0)
        while (true)
        {
            // storage for our population of chromosomes.
            //size: [POP_SIZE]
            List<chromo_typ> Population = new List<chromo_typ>();

            // get a target number from the user
            Double Target;
            Console.WriteLine("Input a target number: ");
            string sTarget= "15"; 
            bool success = Double.TryParse(sTarget, out Target);
            if (!success)
            {
                Console.WriteLine("Oopsie!");
                return;
            }

            // first create a random population, all with zero fitness.
            for (int i=0; i<POP_SIZE; i++)
            {
                chromo_typ ct = new chromo_typ(GetRandomBits(CHROMO_LENGTH, i), 0.0);
                Population.Add(ct); 
            }

            int GenerationsRequiredToFindASolution = 0;

            //we will set this flag if a solution has been found
            bool bFound = false;

            //enter the main GA loop
            while(!bFound)
            {
                    //this is used during roulette wheel sampling
                    double TotalFitness = 0.0f;

                    // test and update the fitness of every chromosome in the population
                foreach (chromo_typ ct in Population)
                {
                    ct.fitness = AssignFitness(ct.bits, Target);
                    TotalFitness += ct.fitness;
                }

                    // check to see if we have found any solutions (fitness will be 999)
                chromo_typ thechamp = new chromo_typ(); 
                    foreach (chromo_typ ct in Population)
                {
                    if (ct.fitness == 999.0f)
                    {
                        ultimatechamp.fitness = ct.fitness;
                        ultimatechamp.bits = ct.bits;

                        Console.WriteLine("=========================");
                        Console.WriteLine("Solution found in " + GenerationsRequiredToFindASolution.ToString() + " generations! Its genes are:");
                        PrintChromo(ct.bits);
                        Console.WriteLine("\nIt has phenotype {0}, and is the chromosome: {1}", Phenotype(ultimatechamp.bits), ultimatechamp.bits);
                        Console.WriteLine("=========================");
                        bFound = true;
                        Console.ReadLine(); 
                        return;
                    }
                    else
                    {
                        if (ct.fitness > thechamp.fitness)
                        {
                            thechamp.fitness = ct.fitness;
                            thechamp.bits = ct.bits; 
                        }
                        if (ct.fitness > ultimatechamp.fitness)
                        {
                            ultimatechamp.fitness = ct.fitness;
                            ultimatechamp.bits = ct.bits; 
                        }
                    }
                    }
                Console.WriteLine("\nBest chromosome detected in generation " + GenerationsRequiredToFindASolution.ToString() + " has fitness {0}, phenotype {1}, and genotype: {2}", thechamp.fitness, Phenotype(thechamp.bits), DisplayChromo(thechamp.bits));

                //
                    // create a new population by selecting two parents at a time and creating offspring
                // by applying crossover and mutation. Do this until the desired number of offspring
                // have been created
                //

                    //define some temporary storage for the new population we are about to create
                    //chromo_typ temp[POP_SIZE];
                List<chromo_typ> temp = new List<chromo_typ>();

                    //loop until we have created POP_SIZE new chromosomes
                int cPop = 0;
                    while (cPop < POP_SIZE)
                {
                    // we are going to create the new population by grabbing members of the old 
                    // population two at a time via roulette wheel selection
                    Random r = new Random(GenerationsRequiredToFindASolution);
                    string offspring1 = Roulette(TotalFitness, Population, r.Next(1000));
                    string offspring2 = Roulette(TotalFitness, Population, r.Next(1000));

                    // add crossover dependent on the crossover rate
                    string newoffspring1;
                    string newoffspring2;
                    Crossover(offspring1, offspring2, out newoffspring1, out newoffspring2, r.Next(1000));

                    // mutate dependent on the mutation rate
                    newoffspring1 = Mutate(newoffspring1, r.Next(1000));
                    newoffspring2 = Mutate(newoffspring2, r.Next(1000));

                    //add these offspring to the new population. (assigning zero as their fitness scores)
                    chromo_typ ct1 = new chromo_typ(newoffspring1, 0.0f);
                    chromo_typ ct2 = new chromo_typ(newoffspring2, 0.0f);
                    temp.Add(ct1); cPop++;
                    temp.Add(ct2); cPop++;
                }//end loop

                    //copy temp population into main population array
                Population.Clear();
                    for (int i=0; i<POP_SIZE; i++)
                {
                    Population.Add(temp[i]);
                }

                    ++GenerationsRequiredToFindASolution;

                    // exit app if no solution found within the maximum allowable number
                    // of generations
                    if (GenerationsRequiredToFindASolution > MAX_ALLOWABLE_GENERATIONS)
                    {
                    Console.WriteLine("=========================");
                    Console.WriteLine("No exact solutions found!");
                    Console.WriteLine("Best chromosome found has fitness {0}, phenotype {1}, genotype: {2}, and is the chromosome {3}", ultimatechamp.fitness, Phenotype(ultimatechamp.bits), DisplayChromo(ultimatechamp.bits), ultimatechamp.bits);
                    Console.WriteLine("=========================");
                            bFound = true;
                    }
            }
            Console.WriteLine("");
            Console.WriteLine("Next round:");
        }//end while
        Console.ReadLine(); 
    }

    //	This function returns a string of random 1s and 0s of the desired length.
    static string GetRandomBits(int length, int seed)
    {
        StringBuilder bits = new StringBuilder();
        Random r = new Random(seed);
        for (int i = 0; i < length; i++)
        {
            Double d = r.NextDouble();
            if (d > 0.5)
            {
                bits.Append("1");
            }
            else
            {
                bits.Append("0");
            }
        }
        return bits.ToString();
    }

    static int BinToDec(string bits)
    {
        int val = 0;
        int value_to_add = 1;

        for (int i = bits.Length; i > 0; i--)
        {
            if (bits[i - 1] == '1')
            {
                val += value_to_add;
            }
            value_to_add *= 2;
        }//next bit
        return val;
    }

    // Given a chromosome this function will step through the genes one at a time and insert 
    // the decimal values of each gene (which follow the operator -> number -> operator rule)
    // into a buffer. Returns the number of elements in the buffer, and the buffer as an out arg.
    //int ParseBits(string bits, ref List<int> buffer)
    static int ParseBits(string bits, ref int[] buffer)
    {
        // counter for buffer position
        int cBuff = 0;

        // step through bits a gene at a time until end and store decimal values
        // of valid operators and numbers. Don't forget we are looking for operator - 
        // number - operator - number and so on... 
        // We ignore the unused genes 1111 and 1110

        // flag to determine if we are looking for an operator or a number
        bool bOperator = true;

        // storage for decimal value of currently tested gene
        int this_gene = 0;

        for (int i = 0; i < CHROMO_LENGTH; i += GENE_LENGTH)
        {
            //convert the current gene to decimal
            this_gene = BinToDec(bits.Substring(i, GENE_LENGTH));

            //find a gene which represents an operator
            if (bOperator)
            {
                if ((this_gene < 10) || (this_gene > 13))
                {
                    continue;
                }
                else
                {
                    bOperator = false;
                    buffer[cBuff++] = this_gene;
                    //buffer.Add(this_gene);
                    continue;
                }
            }

            //find a gene which represents a number
            else
            {
                if (this_gene > 9)
                {
                    continue;
                }
                else
                {
                    bOperator = true;
                    buffer[cBuff++] = this_gene;
                    continue;
                }
            }

        }//next gene

        //	now we have to run through buffer to see if a possible divide by zero
        //	is included and delete it. (ie a '/' followed by a '0'). We take an easy
        //	way out here and just change the '/' to a '+'. This will not effect the 
        //	evolution of the solution
        for (int i = 0; i < cBuff; i++)
        {
            if ((buffer[i] == 13) && (buffer[i + 1] == 0))

                buffer[i] = 10;
        }

        // debugging
        string chromosome = DisplayChromo(buffer);

        return cBuff;
    }

    //	given a chromosome (string of bits) this function will calculate its  
    //  phenotype (representation)
    static Double Phenotype(string bits)
    {
        //holds decimal values of gene sequence
        int len = (int)(CHROMO_LENGTH / GENE_LENGTH);
        int[] buffer = new int[len];
        //List<int> buffer2 = new List<int>(); 
        int num_elements = ParseBits(bits, ref buffer);

        // ok, we have a buffer filled with valid values of: 
        // operator - number - operator - number..
        // now we calculate what this represents.
        Double result = 0.0f;
        for (int i = 0; i < num_elements - 1; i += 2)
        {
            switch (buffer[i])
            {
                case 10:
                    result += buffer[i + 1];
                    break;

                case 11:
                    result -= buffer[i + 1];
                    break;

                case 12:
                    result *= buffer[i + 1];
                    break;

                case 13:
                    result /= buffer[i + 1];
                    break;

            }//end switch
        }
        return result;
    }

    // given a phenotype (chromosome representation) and a target value, this function
    // will return a fitness score
    static Double AssignFitness(string bits, double target_value)
    {
        Double result = Phenotype(bits);

        // Now we calculate the fitness. First check to see if a solution has been found
        // and assign an arbitarily high fitness score if this is so.

        if (result == target_value)
        {
            return 999.0f;
        }
        else
        {
            return 1 / (double)Math.Abs((double)(target_value - result));
        }
    }

    // decodes and prints a chromo to screen
    static void PrintChromo(string bits)
    {	
            //holds decimal values of gene sequence
            //int buffer[(int)(CHROMO_LENGTH / GENE_LENGTH)];
        int len = (int)(CHROMO_LENGTH / GENE_LENGTH);
        int[] buffer = new int[len];
        //List<int> buffer2 = new List<int>(); 

            //parse the bit string
            int num_elements = ParseBits(bits, ref buffer);

            for (int i=0; i<num_elements; i++)
        {
                    PrintGeneSymbol(buffer[i]);
        }
            return;
    }
    static string DisplayChromo(string bits)
    {
        //holds decimal values of gene sequence
        //int buffer[(int)(CHROMO_LENGTH / GENE_LENGTH)];
        int len = (int)(CHROMO_LENGTH / GENE_LENGTH);
        int[] buffer = new int[len];
        //List<int> buffer2 = new List<int>(); 

        //parse the bit string
        int num_elements = ParseBits(bits, ref buffer);

        StringBuilder s = new StringBuilder(); 
        for (int i = 0; i < num_elements; i++)
        {
            s.Append(DisplayGeneSymbol(buffer[i]));
        }
        return s.ToString();
    }
    static void PrintChromo(int[] bits)
    {
        for (int i = 0; i < bits.Length; i++)
        {
            PrintGeneSymbol(bits[i]);
        }
        return;
    }
    static string DisplayChromo(int[] bits)
    {
        StringBuilder s = new StringBuilder(); 
        for (int i = 0; i < bits.Length; i++)
        {
            s.Append(DisplayGeneSymbol(bits[i]));
        }
        return s.ToString();
    }

    //	given an integer this function outputs its symbol to the screen 
    //----------------------------------------------------------------------------------
    static void PrintGeneSymbol(int val)
    {
        if (val < 10)
        {
            //cout << val << " ";
            Console.Write(val.ToString() + " ");  
        }
        else
        {
            switch (val)
            {
                case 10:
                    //cout << "+";
                    Console.Write("+");  
                    break;

                case 11:
                    //cout << "-";
                    Console.Write("-");  
                    break;

                case 12:
                    //cout << "*";
                    Console.Write("*");  
                    break;

                case 13:
                    //cout << "/";
                    Console.Write("/");  
                    break;
            }//end switch

            //cout << " ";
            Console.Write(" ");  
        }
        return;
    }
    static string DisplayGeneSymbol(int val)
    {
        StringBuilder s = new StringBuilder(); 
        if (val < 10)
        {
            //cout << val << " ";
            s.Append(val.ToString() + " "); 
        }
        else
        {
            switch (val)
            {
                case 10:
                    //cout << "+";
                    s.Append("+");
                    break;

                case 11:
                    //cout << "-";
                    s.Append("-");
                    break;

                case 12:
                    //cout << "*";
                    s.Append("*");
                    break;

                case 13:
                    //cout << "/";
                    s.Append("/");
                    break;
            }//end switch

            //cout << " ";
            s.Append(" ");
        }
        return s.ToString();
    }

    //	Mutates a chromosome's bits dependent on the MUTATION_RATE
    static string Mutate(string bits, int seed)
    {
        StringBuilder s = new StringBuilder();
        Random r = new Random(seed); 
            for (int i = 0; i < bits.Length; i++)
            {
            Double d = r.NextDouble();
            if (d < MUTATION_RATE)
            {
                if (bits[i] == '1')
                {
                    s.Append("0");
                    //bits[i] = '0';
                }
                else
                {
                    s.Append("1");
                    //bits[i] = '1';
                }
            }
            else
            {
                s.Append(bits[i]);
            }
            }
            return s.ToString();
    }

    //  Dependent on the CROSSOVER_RATE this function selects a random point along the 
    //  length of the chromosomes and swaps all the bits after that point.
    static void Crossover(string offspring1, string offspring2, 
        out string newoffspring1, out string newoffspring2, int seed)
    {
        newoffspring1 = String.Empty;
        newoffspring2 = String.Empty;	
        Random r = new Random(seed); 
        Double d = r.NextDouble(); 

        //dependent on the crossover rate
        if (d < CROSSOVER_RATE)
        {
            //create a random crossover point
            int crossover = (int)(d * CHROMO_LENGTH);

            string t1 = offspring1.Substring(0, crossover) + offspring2.Substring(crossover, CHROMO_LENGTH - crossover);
            string t2 = offspring2.Substring(0, crossover) + offspring1.Substring(crossover, CHROMO_LENGTH - crossover);

            newoffspring1 = t1;
            newoffspring2 = t2;
        }
        else
        {
            newoffspring1 = offspring1;
            newoffspring2 = offspring2;
        }
    }

    // selects a chromosome from the population via roulette wheel selection
    static string Roulette(double total_fitness, List<chromo_typ> Population, int seed)
    {
        //generate a random number between 0 & total fitness count
        Random r = new Random(seed); 
        Double d = r.NextDouble(); 
        double Slice = (double)(d * total_fitness);

        //go through the chromosones adding up the fitness so far
        double FitnessSoFar = 0.0f;

        for (int i=0; i<POP_SIZE; i++)
        {
                FitnessSoFar += Population[i].fitness;

                //if the fitness so far > random number return the chromo at this point
                if (FitnessSoFar >= Slice)
            {
                        return Population[i].bits;
            }
        }
        return "";
    }

    public class chromo_typ
    {
        public string bits;  
        public double fitness;
            public chromo_typ()
        {
            bits = String.Empty;
            fitness = 0.0;
        }
        public chromo_typ(string bts, double ftns)
        {
            bits = bts;
            fitness = ftns;
        }
    };
}

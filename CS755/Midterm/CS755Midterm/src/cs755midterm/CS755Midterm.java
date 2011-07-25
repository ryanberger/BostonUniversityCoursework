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
    
    static Scanner _console = new Scanner(System.in);
    static String _newoffspring1;
    static String _newoffspring2;
    static int[] _buffer;

    static Double CROSSOVER_RATE = 0.7;
    static Double MUTATION_RATE = 0.001;
    static int POP_SIZE = 100;			//must be an even number
    static int CHROMO_LENGTH = 300;
    static int GENE_LENGTH = 4;
    static int MAX_ALLOWABLE_GENERATIONS = 400;

    public static void main(String[] args)
    {
        // overall champ;
        chromo_typ ultimatechamp = new chromo_typ(); 

        // just loop endlessly until user gets bored :0)
        while (true)
        {
            Double Target;
            // storage for our population of chromosomes.
            //size: [POP_SIZE]
            List<chromo_typ> Population = new ArrayList<chromo_typ>();

            // get a target number from the user
            System.out.println("Input a target number: ");
            String sTarget = _console.nextLine();
            try
            {
                Target = Double.parseDouble(sTarget);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Oopsie!");
                return;
            }

            // first create a random population, all with zero fitness.
            for (int i=0; i<POP_SIZE; i++)
            {
                chromo_typ ct = new chromo_typ(GetRandomBits(CHROMO_LENGTH, i), 0.0);
                Population.add(ct); 
            }

            int GenerationsRequiredToFindASolution = 0;

            //we will set this flag if a solution has been found
            Boolean bFound = false;

            //enter the main GA loop
            while(!bFound)
            {
                //this is used during roulette wheel sampling
                double TotalFitness = 0.0f;

                    // test and update the fitness of every chromosome in the population
                for (chromo_typ ct: Population)
                {
                    ct.fitness = AssignFitness(ct.bits, Target);
                    TotalFitness += ct.fitness;
                }

                    // check to see if we have found any solutions (fitness will be 999)
                chromo_typ thechamp = new chromo_typ(); 
                    for (chromo_typ ct: Population)
                {
                    if (ct.fitness == 999.0f)
                    {
                        ultimatechamp.fitness = ct.fitness;
                        ultimatechamp.bits = ct.bits;

                        System.out.println("=========================");
                        System.out.println("Solution found in " + GenerationsRequiredToFindASolution + " generations! Its genes are:");
                        PrintChromo(ct.bits);
                        System.out.println("\nIt has phenotype " + Phenotype(ultimatechamp.bits) +", and is the chromosome: " + ultimatechamp.bits);
                        System.out.println("=========================");
                        bFound = true;
                        _console.nextLine(); 
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
                System.out.println("\nBest chromosome detected in generation " + 
                        GenerationsRequiredToFindASolution + " has fitness " + 
                        thechamp.fitness + ", phenotype " + Phenotype(thechamp.bits) + 
                        ", and genotype: " + DisplayChromo(thechamp.bits));

                //
                // create a new population by selecting two parents at a time and creating offspring
                // by applying crossover and mutation. Do this until the desired number of offspring
                // have been created
                //

                    //define some temporary storage for the new population we are about to create
                    //chromo_typ temp[POP_SIZE];
                List<chromo_typ> temp = new ArrayList<chromo_typ>();

                    //loop until we have created POP_SIZE new chromosomes
                int cPop = 0;
                    while (cPop < POP_SIZE)
                {
                    // we are going to create the new population by grabbing members of the old 
                    // population two at a time via roulette wheel selection
                    Random r = new Random(GenerationsRequiredToFindASolution);
                    String offspring1 = Roulette(TotalFitness, Population, r.nextInt(1000));
                    String offspring2 = Roulette(TotalFitness, Population, r.nextInt(1000));

                    // add crossover dependent on the crossover rate
                    String newoffspring1 = "";
                    String newoffspring2 = "";
                    Crossover(offspring1, offspring2, r.nextInt(1000));

                    // mutate dependent on the mutation rate
                    newoffspring1 = Mutate(_newoffspring1, r.nextInt(1000));
                    newoffspring2 = Mutate(_newoffspring2, r.nextInt(1000));

                    //add these offspring to the new population. (assigning zero as their fitness scores)
                    chromo_typ ct1 = new chromo_typ(newoffspring1, 0.0f);
                    chromo_typ ct2 = new chromo_typ(newoffspring2, 0.0f);
                    temp.add(ct1); cPop++;
                    temp.add(ct2); cPop++;
                }//end loop

                    //copy temp population into main population array
                Population.clear();
                    for (int i=0; i<POP_SIZE; i++)
                {
                    Population.add(temp.get(i));
                }

                    ++GenerationsRequiredToFindASolution;

                    // exit app if no solution found within the maximum allowable number
                    // of generations
                    if (GenerationsRequiredToFindASolution > MAX_ALLOWABLE_GENERATIONS)
                    {
                    System.out.println("=========================");
                    System.out.println("No exact solutions found!");
                    System.out.println("Best chromosome found has fitness " + 
                            ultimatechamp.fitness + ", phenotype " + Phenotype(ultimatechamp.bits) + 
                            ", genotype: " + DisplayChromo(ultimatechamp.bits) + 
                            ", and is the chromosome " + ultimatechamp.bits);
                    System.out.println("=========================");
                            bFound = true;
                    }
            }
            System.out.println("");
            System.out.println("Next round:");
        }//end while
        //_console.nextLine(); 
    }

    //	This function returns a String of random 1s and 0s of the desired length.
    static String GetRandomBits(int length, int seed)
    {
        StringBuilder bits = new StringBuilder();
        Random r = new Random(seed);
        for (int i = 0; i < length; i++)
        {
            Double d = r.nextDouble();
            if (d > 0.5)
            {
                bits.append("1");
            }
            else
            {
                bits.append("0");
            }
        }
        return bits.toString();
    }

    static int BinToDec(String bits)
    {
        int val = 0;
        int value_to_add = 1;

        for (int i = bits.length(); i > 0; i--)
        {
            if (bits.charAt(i - 1) == '1')
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
    //int ParseBits(String bits, ref List<int> buffer)
    static int ParseBits(String bits)
    {
        // counter for buffer position
        int cBuff = 0;

        // step through bits a gene at a time until end and store decimal values
        // of valid operators and numbers. Don't forget we are looking for operator - 
        // number - operator - number and so on... 
        // We ignore the unused genes 1111 and 1110

        // flag to determine if we are looking for an operator or a number
        Boolean bOperator = true;

        // storage for decimal value of currently tested gene
        int this_gene = 0;

        for (int i = 0; i < CHROMO_LENGTH; i += GENE_LENGTH)
        {
            //convert the current gene to decimal
            this_gene = BinToDec(bits.substring(i, i + GENE_LENGTH));

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
                    _buffer[cBuff++] = this_gene;
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
                    _buffer[cBuff++] = this_gene;
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
            if ((_buffer[i] == 13) && (_buffer[i + 1] == 0))

                _buffer[i] = 10;
        }

        // debugging
        String chromosome = DisplayChromo(_buffer);

        return cBuff;
    }

    //	given a chromosome (String of bits) this function will calculate its  
    //  phenotype (representation)
    static Double Phenotype(String bits)
    {
        //holds decimal values of gene sequence
        int len = (int)(CHROMO_LENGTH / GENE_LENGTH);
        _buffer = new int[len];
        //List<int> buffer2 = new List<int>(); 
        int num_elements = ParseBits(bits);

        // ok, we have a buffer filled with valid values of: 
        // operator - number - operator - number..
        // now we calculate what this represents.
        Double result = 0.0;
        for (int i = 0; i < num_elements - 1; i += 2)
        {
            switch (_buffer[i])
            {
                case 10:
                    result += _buffer[i + 1];
                    break;

                case 11:
                    result -= _buffer[i + 1];
                    break;

                case 12:
                    result *= _buffer[i + 1];
                    break;

                case 13:
                    result /= _buffer[i + 1];
                    break;

            }//end switch
        }
        return result;
    }

    // given a phenotype (chromosome representation) and a target value, this function
    // will return a fitness score
    static Double AssignFitness(String bits, double target_value)
    {
        Double result = Phenotype(bits);

        // Now we calculate the fitness. First check to see if a solution has been found
        // and assign an arbitarily high fitness score if this is so.

        if (result == target_value)
        {
            return 999.0;
        }
        else
        {
            return 1 / (double)Math.abs((double)(target_value - result));
        }
    }

    // decodes and prints a chromo to screen
    static void PrintChromo(String bits)
    {	
            //holds decimal values of gene sequence
            //int buffer[(int)(CHROMO_LENGTH / GENE_LENGTH)];
        int len = (int)(CHROMO_LENGTH / GENE_LENGTH);
        _buffer = new int[len];
        //List<int> buffer2 = new List<int>(); 

            //parse the bit String
            int num_elements = ParseBits(bits);

            for (int i=0; i<num_elements; i++)
        {
                    PrintGeneSymbol(_buffer[i]);
        }
            return;
    }
    static String DisplayChromo(String bits)
    {
        //holds decimal values of gene sequence
        //int buffer[(int)(CHROMO_LENGTH / GENE_LENGTH)];
        int len = (int)(CHROMO_LENGTH / GENE_LENGTH);
        _buffer = new int[len];
        //List<int> buffer2 = new List<int>(); 

        //parse the bit String
        int num_elements = ParseBits(bits);

        StringBuilder s = new StringBuilder(); 
        for (int i = 0; i < num_elements; i++)
        {
            s.append(DisplayGeneSymbol(_buffer[i]));
        }
        return s.toString();
    }
    static void PrintChromo(int[] bits)
    {
        for (int i = 0; i < bits.length; i++)
        {
            PrintGeneSymbol(bits[i]);
        }
        return;
    }
    static String DisplayChromo(int[] bits)
    {
        StringBuilder s = new StringBuilder(); 
        for (int i = 0; i < bits.length; i++)
        {
            s.append(DisplayGeneSymbol(bits[i]));
        }
        return s.toString();
    }

    //	given an integer this function outputs its symbol to the screen 
    //----------------------------------------------------------------------------------
    static void PrintGeneSymbol(int val)
    {
        if (val < 10)
        {
            //cout << val << " ";
            System.out.print(val + " ");  
        }
        else
        {
            switch (val)
            {
                case 10:
                    //cout << "+";
                    System.out.print("+");  
                    break;

                case 11:
                    //cout << "-";
                    System.out.print("-");  
                    break;

                case 12:
                    //cout << "*";
                    System.out.print("*");  
                    break;

                case 13:
                    //cout << "/";
                    System.out.print("/");  
                    break;
            }//end switch

            //cout << " ";
            System.out.print(" ");  
        }
        return;
    }
    static String DisplayGeneSymbol(int val)
    {
        StringBuilder s = new StringBuilder(); 
        if (val < 10)
        {
            //cout << val << " ";
            s.append(val + " "); 
        }
        else
        {
            switch (val)
            {
                case 10:
                    //cout << "+";
                    s.append("+");
                    break;

                case 11:
                    //cout << "-";
                    s.append("-");
                    break;

                case 12:
                    //cout << "*";
                    s.append("*");
                    break;

                case 13:
                    //cout << "/";
                    s.append("/");
                    break;
            }//end switch

            //cout << " ";
            s.append(" ");
        }
        return s.toString();
    }

    //	Mutates a chromosome's bits dependent on the MUTATION_RATE
    static String Mutate(String bits, int seed)
    {
        StringBuilder s = new StringBuilder();
        Random r = new Random(seed); 
            for (int i = 0; i < bits.length(); i++)
            {
            Double d = r.nextDouble();
            if (d < MUTATION_RATE)
            {
                if (bits.charAt(i) == '1')
                {
                    s.append("0");
                    //bits[i] = '0';
                }
                else
                {
                    s.append("1");
                    //bits[i] = '1';
                }
            }
            else
            {
                s.append(bits.charAt(i));
            }
            }
            return s.toString();
    }

    //  Dependent on the CROSSOVER_RATE this function selects a random point along the 
    //  length of the chromosomes and swaps all the bits after that point.
    static void Crossover(String offspring1, String offspring2, int seed)
    {
        _newoffspring1 = "";
        _newoffspring2 = "";	
        Random r = new Random(seed); 
        Double d = r.nextDouble(); 

        //dependent on the crossover rate
        if (d < CROSSOVER_RATE)
        {
            //create a random crossover point
            int crossover = (int)(d * CHROMO_LENGTH);

            String t1 = offspring1.substring(0, crossover) + offspring2.substring(crossover, crossover + (CHROMO_LENGTH - crossover));
            String t2 = offspring2.substring(0, crossover) + offspring1.substring(crossover, crossover + (CHROMO_LENGTH - crossover));

            _newoffspring1 = t1;
            _newoffspring2 = t2;
        }
        else
        {
            _newoffspring1 = offspring1;
            _newoffspring2 = offspring2;
        }
    }

    // selects a chromosome from the population via roulette wheel selection
    static String Roulette(double total_fitness, List<chromo_typ> Population, int seed)
    {
        //generate a random number between 0 & total fitness count
        Random r = new Random(seed); 
        Double d = r.nextDouble(); 
        double Slice = (double)(d * total_fitness);

        //go through the chromosones adding up the fitness so far
        double FitnessSoFar = 0.0f;

        for (int i=0; i<POP_SIZE; i++)
        {
                FitnessSoFar += Population.get(i).fitness;

                //if the fitness so far > random number return the chromo at this point
                if (FitnessSoFar >= Slice)
            {
                        return Population.get(i).bits;
            }
        }
        return "";
    }

    public static class chromo_typ
    {
        public String bits;  
        public double fitness;
            public chromo_typ()
        {
            bits = "";
            fitness = 0.0;
        }
        public chromo_typ(String bts, double ftns)
        {
            bits = bts;
            fitness = ftns;
        }
    }
}

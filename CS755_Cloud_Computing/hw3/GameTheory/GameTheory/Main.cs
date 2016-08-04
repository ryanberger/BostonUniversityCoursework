using System;
using System.Collections.Generic;
using System.Diagnostics;

using Meta.Numerics;
using Meta.Numerics.Matrices;

namespace GameTheory
{
	internal struct BoardSpace {

        public BoardSpace (string name, double rent, double price) {
            this.name = name;
            this.price = price;
            this.rent = rent;
        }

        private string name;
        private double price;
        private double rent;

        public string Name { get { return(name); } }

        public double Price { get { return (price); } }

        public double Rent { get { return (rent); } }

    }

    internal class Program {

        private static void Main (string[] args) {

            // define squares
            BoardSpace[] spaces = new BoardSpace[] {
                new BoardSpace("Go", 0.0, Double.PositiveInfinity),
                new BoardSpace("Mediterranean Avenue", 2.0, 60.0),
                new BoardSpace("Community Chest", 0.0, Double.PositiveInfinity),
                new BoardSpace("Baltic Avenue", 4.0, 60.0),
                new BoardSpace("Income Tax", 0.0, Double.PositiveInfinity),
                new BoardSpace("Reading Railroad", 25.0, 200.0),
                new BoardSpace("Oriental Avenue", 6.0, 100.0),
                new BoardSpace("Chance", 0.0, Double.PositiveInfinity),
                new BoardSpace("Vermont Avenue", 6.0, 100.0),
                new BoardSpace("Connecticut Avenue", 8.0, 120.0),
                new BoardSpace("Jail", 0.0, Double.PositiveInfinity),
                new BoardSpace("St. Charles Place", 10.0, 140.0),
                new BoardSpace("Electric Company", 4.0 * 6.0, 150.0),
                new BoardSpace("States Avenue", 10.0, 140.0),
                new BoardSpace("Virginia Avenue", 12.0, 160.0),
                new BoardSpace("Pennsylvania Railroad", 25.0, 200.0),
                new BoardSpace("St. James Place", 14.0, 180.0),
                new BoardSpace("Community Chest", 0.0, Double.PositiveInfinity),
                new BoardSpace("Tennessee Avenue", 14.0, 180.0),
                new BoardSpace("New York Avenue", 16.0, 200.0),
                new BoardSpace("Free Parking", 0.0, Double.PositiveInfinity),
                new BoardSpace("Kentucky Avenue", 18.0, 220.0),
                new BoardSpace("Chance", 0.0, Double.PositiveInfinity),
                new BoardSpace("Indiana Avenue", 18.0, 220.0),
                new BoardSpace("Illinois Avenue", 20.0, 240.0),
                new BoardSpace("B & O Railroad", 25.0, 200.0),
                new BoardSpace("Atlantic Avenue", 22.0, 260.0),
                new BoardSpace("Ventnor Avenue", 22.0, 260.0),
                new BoardSpace("Water Works", 4.0 * 6.0, 150.0),
                new BoardSpace("Marvin Gardens", 24.0, 280.0),
                new BoardSpace("Go To Jail", 0.0, Double.PositiveInfinity),
                new BoardSpace("Pacific Avenue", 26.0, 300.0),
                new BoardSpace("North Carolina Avenue", 26.0, 300.0),
                new BoardSpace("Community Chest", 0.0, Double.PositiveInfinity),
                new BoardSpace("Pennsylvania Avenue", 28.0, 320.0),
                new BoardSpace("Short Line", 25.0, 200.0),
                new BoardSpace("Chance", 0.0, Double.PositiveInfinity),
                new BoardSpace("Park Place", 35.0, 350.0),
                new BoardSpace("Luxury Tax", 0.0, Double.PositiveInfinity),
                new BoardSpace("Boardwalk", 50.0, 400.0)
            };

            // number of squares
            int n = spaces.Length;

            // compute the transition matrix which takes the dice roll
            // into account: Moves us ahead by two spaces with
            // probability 1/36 (“snake-eyes”), three spaces with 
            // probability 2/36, and so on up to twelve spaces with 
            // probability 1/36 (“double-sixes”), etc.
            SquareMatrix R = new SquareMatrix(n);
            for (int c = 0; c < n; c++) {
                R[(c + 2) % n, c] = 1.0 / 36.0;
                R[(c + 3) % n, c] = 2.0 / 36.0;
                R[(c + 4) % n, c] = 3.0 / 36.0;
                R[(c + 5) % n, c] = 4.0 / 36.0;
                R[(c + 6) % n, c] = 5.0 / 36.0;
                R[(c + 7) % n, c] = 6.0 / 36.0;
                R[(c + 8) % n, c] = 5.0 / 36.0;
                R[(c + 9) % n, c] = 4.0 / 36.0;
                R[(c + 10) % n, c] = 3.0 / 36.0;
                R[(c + 11) % n, c] = 2.0 / 36.0;
                R[(c + 12) % n, c] = 1.0 / 36.0;
            }

            // compute the special matrix, which takes board into account:
            // The column for space 30, “Go To Jail”, transitions with 
            // certainty to space 10, the jail.
            // A token that lands on a community chest space sometimes 
            // transitions to go and sometimes transitions to jail, but 
            // most often just stays put
            SquareMatrix S = new SquareMatrix(n);
            for (int c = 0; c < n; c++) {
                if (c == 30) {
                    // go to jail
                    S[10, 30] = 1.0;
                } else if ((c == 7) || (c == 22) || (c == 36)) {
                    // chance
                    // advance to go
                    S[0, c] = 1.0 / 16.0;
                    // advance to illinois avenue
                    S[24, c] = 1.0 / 16.0;
                    // take a walk on the boardwalk
                    S[39, c] = 1.0 / 16.0;
                    // go to jail
                    S[10, c] = 1.0 / 16.0;
                    // take a ride on the reading
                    S[5, c] = 1.0 / 16.0;
                    // advance to St. Charles place
                    S[11, c] = 1.0 / 16.0;
                    // go back 3 spaces
                    S[(c - 3) % S.Dimension, c] = 1.0 / 16.0;
                    // advance token to the nearest utility
                    if ((c < 12) || (c > 28)) {
                        S[12, c] = 1.0 / 16.0;
                    } else {
                        S[28, c] = 1.0 / 16.0;
                    }
                    // advance token to the nearest railroad
                    if (c < 5) {
                        S[5, c] += 1.0 / 16.0;
                    } else if (c < 15) {
                        S[15, c] = 1.0 / 16.0;
                    } else if (c < 25) {
                        S[25, c] = 1.0 / 16.0;
                    } else if (c < 35) {
                        S[35, c] = 1.0 / 16.0;
                    } else {
                        S[5, c] += 1.0 / 16.0;
                    }
                    // stay put
                    S[c, c] = 7.0 / 16.0;
                } else if ((c == 2) || (c == 17) || (c == 33)) {
                    // community chest
                    // advance to go
                    S[0, c] = 1.0 / 16.0;
                    // go to jail
                    S[10, c] = 1.0 / 16.0;
                    // stay put
                    S[c, c] = 14.0 / 16.0;
                } else {
                    // all other spaces are no-ops
                    S[c, c] = 1.0;
                }
            }

            // compute the complete transition matrix
            SquareMatrix P = S * R;

            // verify Markov conditions
            for (int c = 0; c < P.Dimension; c++) {
                double sr = 0.0;
                for (int r = 0; r < P.Dimension; r++) {
                    sr += P[r, c];
                }
                Debug.Assert(Math.Abs(sr - 1.0) < 1.0e-15);
            }

            // An alternative now is to begin with some initial state vector 
            // guess, and repeatedly apply P until the resultant vector
            // no longer changes. The resultant vector will be the dominant
            // eigenvector. That's how PageRank is evaluated by Google,
            // using MapReduce.

            // Another alternative is to compute the eigenvalues and 
            // eigenvectors of the transition matrix
            ComplexEigensystem E = P.Eigensystem();

            // get the dominant eigenvector: The one with the largest
            // eigenvalue
            Complex e = -1.0;
            IList<Complex> v = null;
            for (int i = 0; i < E.Dimension; i++) {
                Complex ei = E.Eigenvalue(i);
                if (ComplexMath.Abs(ei) > ComplexMath.Abs(e)) {
                    e = ei;
                    v = E.Eigenvector(i);
                }
            }

            // verify that it has eigenvalue 1 and is real
            Debug.Assert(Math.Abs(e.Re - 1.0) < 1.0e-15);
            Debug.Assert(e.Im == 0.0);
            for (int i = 0; i < n; i++) {
                Debug.Assert(v[i].Im == 0.0);
            }

            // normalize the probabilities
            double sv = 0.0;
            for (int i = 0; i < E.Dimension; i++) {
                sv += v[i].Re;
            }
            double[] p = new double[E.Dimension];
            for (int i = 0; i < E.Dimension; i++) {
                p[i] = v[i].Re / sv;
            }

            // Having found the dominant eigenvector, we can examine its components
            // to find the long-run relative probabilities of a token landing on each
            // space.
            // Print the probabilities

            // First column: Which rent-generating properties are most landed upon

            // Second column: A rent-seeking player would be willing to accept a slightly
            // less frequently landed upon space, if it generates a much higher rent when 
            // landed upon --> Multiply the landing probability per turn by the rent per landing

            // Third column: A clever investor cares not only about cash flow, but 
            // also about the return on his investment (ROI), that is the rent generated
            // per dollar paid --> Divide the rent per turn by the property price

            for (int i = 0; i < n; i++) {
                Console.WriteLine("{0,-24} {1:f4} {2:f4} {3:f4}", spaces[i].Name, p[i], p[i] * spaces[i].Rent, p[i] * spaces[i].Rent / spaces[i].Price);
            }

            // First column: Which rent-generating properties are most landed upon:
            // Illinois Avenue (3.2%) and New York Avenue (3.1%), followed by Tennessee, the Reading, and the B & O (2.9% each).

            // Second column: A rent-seeking player would be willing to accept a slightly less frequently landed upon space,
            // if it generates a much higher rent when landed upon. To know which spaces maximize rent per turn,
            // we need to multiply the landing probability per turn by the rent per landing.
            // Boardwalk ($1.32/turn) is far and away the most rent-generating property to own

            // Third column: Finally, a clever investor cares not only about cash flow, but also about the return on his 
            // investment (ROI), that is the rent generated per dollar paid. Dividing the rent per turn by 
            // the property price changes the picture yet again. By this metric, the best values on the board 
            // are the utilities, followed by the railways.

            Console.ReadLine(); 
        }

    }
}


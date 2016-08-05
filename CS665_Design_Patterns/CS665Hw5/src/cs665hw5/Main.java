
package cs665hw5;

import java.text.ParseException;

/**
 *
 * @author Ryan
 */
public class Main {

    private Movie movies[] = {
        new Movie("War and Peace", "Boston", "PG", 2,
            new String[] {"2:15 pm", "5:20 pm", "7:50 pm", "10:15 pm"}),
        new Movie("Splash", "Boston", "G", 3,
            new String[] {"1:55 pm", "4:05 pm", "7:35 pm", "10:10 pm"}),
        new Movie("Birds", "Waltham", "PG-13", 4,
            new String[] {"4:30 pm", "9:00 pm"}),
        new Movie("Gone with the Wind", "Cambridge", "NC-17", 3,
            new String[] {"11:10 am", "12:45 pm", "1:45 pm", "3:30 pm",
            "4:30 pm", "6:20 pm","7:20 pm", "9:20 pm", "10:20 pm","11:50 pm"}),
    };  // end of movies[]

    private static final String testCommands[][] = {
        {"town=cam, wal"}, // first test run
        {"earliest=4:30 pm", "latest=8:00 pm", "maxRating=pg-13"}, // second run
        }; // end of testCommands[]

    private static Filter filter;
    private static Movie[] finalList;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {

        Main main = new Main();
        for (String[] commands: testCommands)
        {
            main.makeFilters(commands);
            main.printList(finalList);
        }
    }

    private void makeFilters(String[] commands) throws ParseException
    {
        String[] input;
        String param;
        String value;

        // Initialize finalList of movies
        finalList = movies;

        for (String command: commands)
        {
            System.out.println("Processing command: " + command);
            input = command.split("=");
            param = input[0];
            value = input[1];

            if (param.equals("town"))
            {
                filter = new TownFilter(value);
            }
            else if (param.equals("maxRating"))
            {
                filter = new RatingFilter(value);
            }
            else if (param.equals("earliest"))
            {
                filter = new TimeFilter(value, true);
            }
            else if (param.equals("latest"))
            {
                filter = new TimeFilter(value, false);
            }
            finalList = filter.getMovies(finalList);
        }
    }

    private void printList(Movie[] movies)
    {
        if (movies.length > 0)
        {
            for (Movie m: movies)
            {
                System.out.println(m.toString());
            }
        }
        else
        {
            System.out.println("Sorry, no movies match the search criteria.");
        }
    }

}

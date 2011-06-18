
package cs665hw5;


/**
 *
 * @author Ryan
 */
public class TownFilter extends Filter {

    private String[] townNames;

    public TownFilter(String townNames)
    {
        this.townNames = townNames.split(",");
    }

    public Boolean acceptMovie(Movie movie)
    {
        String movieTown;
        String inputTown;

        for (String town: townNames)
        {
            movieTown = movie.getTown().toUpperCase();
            inputTown = town.trim().toUpperCase();

            if (movieTown.startsWith(inputTown))
            {
                return true;
            }
        }

        return false;
    }

}

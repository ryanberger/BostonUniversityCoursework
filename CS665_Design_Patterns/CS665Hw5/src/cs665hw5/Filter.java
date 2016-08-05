
package cs665hw5;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ryan
 */
public abstract class Filter {

    public Movie[] getMovies(Movie[] movies) throws ParseException
    {
        List<Movie> finalList = new ArrayList<Movie>();

        for (Movie movie: movies)
        {
            if (acceptMovie(movie))
            {
                finalList.add(movie);
            }
        }

        // Convert List back to Array
        movies = new Movie[finalList.size()];
        for (int i=0; i<finalList.size(); i++)
        {
            movies[i] = finalList.get(i);
        }

        return movies;
    }
    
    public abstract Boolean acceptMovie(Movie movie) throws ParseException;

}


package cs665hw5;

import java.util.Arrays;
import java.util.List;


/**
 *
 * @author rberger
 */
public class MovieList implements Component {
    //private Movie[] movieList;

    List<Movie> movieList;

    public MovieList(Movie[] movieList)
    {
        this.movieList.addAll(Arrays.asList(movieList));
    }

    public void add(Movie movie)
    {
//        Movie[] tempList = new Movie[movieList.length];
//        tempList = movieList;
//        movieList = new Movie[tempList.length + 1];
        movieList.add(movie);
        
    }

    public Movie[] getMovieList()
    {
        int size = movieList.size();

        Movie[] movies = new Movie[size];

        for (int i=0; i<size; i++)
        {
            movies[i] = movieList.get(i);
        }

        return movies;
    }
}

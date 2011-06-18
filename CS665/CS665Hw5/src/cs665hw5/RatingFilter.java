
package cs665hw5;


/**
 *
 * @author Ryan
 */
public class RatingFilter extends Filter {

    private String[] ratings = new String[] {"G", "PG", "PG-13", "R", "NC-17"};
    private String maxRating;

    public RatingFilter(String maxRating)
    {
        this.maxRating = maxRating;
    }

     public Boolean acceptMovie(Movie movie)
    {
        int movieRatingIndex = 0;
        int maxRatingIndex = 0;

        for(int i = 0; i < ratings.length; i++){
            // Get rating index of movie
            if (ratings[i].equals(movie.getRating()))
            {
                movieRatingIndex = i;
            }
            // Now, get max rating index
            if (ratings[i].equals(maxRating.toUpperCase()))
            {
                maxRatingIndex = i;
            }
        }

        if (movieRatingIndex <= maxRatingIndex)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}

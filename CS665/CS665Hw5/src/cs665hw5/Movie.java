
package cs665hw5;

/**
 *
 * @author Ryan
 */
public class Movie {

    private String title;
    private String town;
    private String rating;
    private int numStars;
    private String[] times;

    public Movie(String title, String town, String rating,
            int numStars, String[] times)
    {
        this.title = title;
        this.town = town;
        this.rating = rating;
        this.numStars = numStars;
        this.times = times;
    }

    @Override
    public String toString()
    {
        String desc;

        desc = title;

        switch (numStars)
        {
            case 1:
                desc += " *,";
                break;
            case 2:
                desc += " **,";
                break;
            case 3:
                desc += " ***,";
                break;
            case 4:
                desc += " ****,";
                break;
            case 0:
            default:
               break;
        }

        desc += " rated " + rating + " now playing in " + town + " at ";

        for (String time: times)
        {
            if (time != null)
            {
                desc += time + ", ";
            }
        }

        // Trim trailing comma
        desc = desc.substring(0, desc.length() - 2);

        return desc;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTown()
    {
        return town;
    }

    public String getRating()
    {
        return rating;
    }

    public int getNumStars()
    {
        return numStars;
    }

    public String[] getTimes()
    {
        return times;
    }

    public void setTimes(String[] times)
    {
        this.times = times;
    }

}

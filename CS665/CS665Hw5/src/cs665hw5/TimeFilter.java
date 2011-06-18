
package cs665hw5;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author Ryan
 */
public class TimeFilter extends Filter {
    
    private Date requestedTime;
    private Boolean isEarliest;

    public TimeFilter(String requestedTime,
            Boolean isEarliest) throws ParseException
    {
        if (requestedTime != null)
        {
            this.requestedTime = DateFormat
                .getTimeInstance(DateFormat.SHORT)
                .parse(requestedTime);
        }
        this.isEarliest = isEarliest;
    }

    public Boolean acceptMovie(Movie movie) throws ParseException
    {
        String[] times = movie.getTimes();
        String[] tempTimes = new String[times.length];
        Date tempTime;
        int index = 0;
        int result;

        for(String time: times)
        {
            if (time != null)
            {
                tempTime = DateFormat
                    .getTimeInstance(DateFormat.SHORT)
                    .parse(time);
                result = requestedTime.compareTo(tempTime);
            
                if (isEarliest)
                {
                    if (result <= 0)
                    {
                        tempTimes[index] = time;
                    }
                }
                else
                {
                    if (result >= 0)
                    {
                        tempTimes[index] = time;
                    }
                }
            }
            index++;
        }

        if (tempTimes.length > 0)
        {
            movie.setTimes(tempTimes);
            return true;
        }
        else
        {
            return false;
        }
    }

}

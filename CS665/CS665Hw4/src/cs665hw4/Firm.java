
package cs665hw4;

import java.util.HashMap;
import java.util.Observable;

/**
 *
 * @author Ryan
 */
public class Firm extends Observable {

    private HashMap firstQ;
    private double firstQuarter;
    private HashMap secondQ;
    private double secondQuarter;
    private HashMap thirdQ;
    private double thirdQuarter;
    private HashMap fourthQ;
    private double fourthQuarter;

    public void Firm()
    {   }

    public void firmChanged()
    {
        setChanged();
        notifyObservers();
    }

    public void setFirm(String year)
    {
        setFirm(firstQ, secondQ, thirdQ, fourthQ, year);
    }

    public void setFirm(HashMap firstQ, HashMap secondQ,
            HashMap thirdQ, HashMap fourthQ, String year)
    {
        this.firstQuarter = ((Double)firstQ.get(year)).doubleValue();
        this.secondQuarter = ((Double)secondQ.get(year)).doubleValue();
        this.thirdQuarter = ((Double)thirdQ.get(year)).doubleValue();
        this.fourthQuarter = ((Double)fourthQ.get(year)).doubleValue();
        firmChanged();
    }

    public void initialize()
    {
        firstQ = new HashMap();
        firstQ.put("2008", 1000.0);
        firstQ.put("2009", 2000.0);
        secondQ = new HashMap();
        secondQ.put("2008", 2300.0);
        secondQ.put("2009", 3000.0);
        thirdQ = new HashMap();
        thirdQ.put("2008", 1000.0);
        thirdQ.put("2009", 4500.0);
        fourthQ = new HashMap();
        fourthQ.put("2008", 1000.0);
        fourthQ.put("2009", 3000.0);

    }

    public String getReport()
    {
        return toString();
    }

    public double getFirstQuarter()
    {
        return firstQuarter;
    }

    public double getSecondQuarter()
    {
        return secondQuarter;
    }

    public double getThirdQuarter()
    {
        return thirdQuarter;
    }

    public double getFourthQuarter()
    {
        return fourthQuarter;
    }
    
    @Override
    public String toString()
    {
        return firstQuarter + " " + secondQuarter 
                + " " + thirdQuarter + " " + fourthQuarter;
    }

}

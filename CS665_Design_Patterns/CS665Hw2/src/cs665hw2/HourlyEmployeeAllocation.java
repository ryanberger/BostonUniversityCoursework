
package cs665hw2;

/**
 *
 * @author Ryan
 */
public class HourlyEmployeeAllocation extends Allocation {

    private double[] projectShares;
    private double totalHours;

    public HourlyEmployeeAllocation(double[] projectShares,
            double totalHours)
    {
        this.projectShares = projectShares;
        this.totalHours = totalHours;
    }

    public double[] getProjectShares()
    {
        return projectShares;
    }

    public double getHours()
    {
        return totalHours;
    }

    public String toString()
    {
        String output = "Project shares:";
        for (double s: projectShares)
        {
            output += " " + Double.toString(s);
        }

        output += " Total hours:";
        output += " " + totalHours;

        return output;
    }
}

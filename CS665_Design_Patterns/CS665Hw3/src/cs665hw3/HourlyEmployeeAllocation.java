
package cs665hw3;

/**
 *
 * @author Ryan
 */
public class HourlyEmployeeAllocation extends Allocation {

    private double[] projectShares;
    private double[] totalHours;

    public HourlyEmployeeAllocation(double[] projectShares,
            double[] totalHours)
    {
        this.projectShares = projectShares;
        this.totalHours = totalHours;
    }

    public double[] getProjectShares()
    {
        return projectShares;
    }

    public double[] getHours()
    {
        return totalHours;
    }

    @Override
    public String toString()
    {
        String output = "Project shares:";
        for (double s: projectShares)
        {
            output += " " + s;
        }

        output += " Total hours:";
        for (double h: totalHours)
        {
            output += " " + h;
        }

        return output;
    }
}

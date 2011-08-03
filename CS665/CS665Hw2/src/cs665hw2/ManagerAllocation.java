
package cs665hw2;

/**
 *
 * @author Ryan
 */
public class ManagerAllocation extends Allocation {

    private double[] projectShares;

    public ManagerAllocation(double[] projectShares)
    {
        this.projectShares = projectShares;
    }

    public double[] getProjectShares()
    {
        return projectShares;
    }

    public String toString()
    {
        String output = "Project shares:";
        for (double s: projectShares)
        {
            output += " " + s;
        }
        return output;
    }
}

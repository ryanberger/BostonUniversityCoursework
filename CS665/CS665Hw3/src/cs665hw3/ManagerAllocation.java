
package cs665hw3;

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

    @Override
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

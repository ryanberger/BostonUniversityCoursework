
package cs665hw2;

/**
 *
 * @author Ryan
 */
public class ExemptEmployeeAllocation extends Allocation {

    private double[] projectHours;

    public ExemptEmployeeAllocation(double[] projectHours)
    {
        this.projectHours = projectHours;
    }

    public double[] getHours()
    {
        return projectHours;
    }

    public String toString()
    {
        String output = "Project hours:";
        for (double h: projectHours)
        {
            output += " " + h;
        }
        return output;
    }

}

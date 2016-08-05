
package cs665hw2;

/**
 *
 * @author Ryan
 */
public class Manager extends Employee {

    private ManagerAllocation all;

    public Manager(String name,
                String status,
                double salary)
	{
        super(name, status, salary);
    }

    public void setMonthlyData(ManagerAllocation all)
    {
        this.all = all;
    }

    public double getProjectShare(int projectNumber)
    {
        return all.getProjectShares()[projectNumber];
    }

    public double getCost(int projectNumber)
    {
        double percentWorked = getProjectShare(projectNumber);
        return salary / 12 * percentWorked;
    }

    public String toString()
    {
        return name + " "
                + status
                + " Salary: " + salary
                + " " + all.toString();
    }

}

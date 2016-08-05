
package cs665hw3;

/**
 *
 * @author Ryan
 */
public class ManagerPay implements PayBehavior {

    private ManagerAllocation all;
    private String name;
    private String status;
    private double salary;

    public ManagerPay(String name,
                String status,
                double salary)
	{
        this.name = name;
        this.status = status;
        this.salary = salary;
    }

    public void setMonthlyData(Allocation all)
    {
        this.all = (ManagerAllocation)all;
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

    @Override
    public String toString()
    {
        return name + " "
                + status
                + " Salary: " + salary
                + " " + all.toString();
    }

}

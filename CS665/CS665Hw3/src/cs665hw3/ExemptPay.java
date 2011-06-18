
package cs665hw3;

/**
 *
 * @author Ryan
 */
public class ExemptPay implements PayBehavior {

    private ExemptEmployeeAllocation all;
    private String name;
    private String status;
    private double salary;

    public ExemptPay(String name,
                String status,
                double salary)
	{
        this.name = name;
        this.status = status;
        this.salary = salary;
    }

    public void setMonthlyData(Allocation all)
    {
        this.all = (ExemptEmployeeAllocation)all;
    }

    public double getHours(int projectNumber)
    {
        return all.getHours()[projectNumber];
    }

    public double getCost(int projectNumber)
    {
        double hoursWorked = getHours(projectNumber);
        return salary * hoursWorked / 12 / 160;
    }

    @Override
    public String toString()
    {
        return name + " " 
                + status + " Salary: " + salary
                + " " + all.toString();
    }

}

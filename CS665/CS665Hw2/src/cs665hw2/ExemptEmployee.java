
package cs665hw2;

/**
 *
 * @author Ryan
 */
public class ExemptEmployee extends Employee {

    private ExemptEmployeeAllocation all;

    public ExemptEmployee(String name,
                String status,
                double salary)
	{
        super(name, status, salary);
    }

    public void setMonthlyData(ExemptEmployeeAllocation all)
    {
        this.all = all;
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

    public String toString()
    {
        return name + " " 
                + status + " Salary: " + salary
                + " " + all.toString();
    }

}

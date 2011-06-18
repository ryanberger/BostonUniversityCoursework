
package cs665hw2;

/**
 *
 * @author Ryan
 */
public class HourlyEmployee extends Employee {

    private HourlyEmployeeAllocation all;

    public HourlyEmployee(String name,
                String status,
                double hourlyRate)
	{
        super(name, status, hourlyRate);
    }

    public void setMonthlyData(HourlyEmployeeAllocation all)
    {
        this.all = all;
    }

    public double getHours()
    {
        return all.getHours();
    }

    public double getProjectShare(int projectNumber)
    {
        return all.getProjectShares()[projectNumber];
    }

    public double getCost(int projectNumber)
    {
        double hoursWorked = getHours();
        double percentWorked = getProjectShare(projectNumber);
        double cost = 0.0;

        if (hoursWorked > 40)
        {
            cost = salary * 40 * percentWorked;
            cost += (salary * percentWorked * (hoursWorked - 40)) * 1.5;
        }
        else
        {
            cost = salary * percentWorked * hoursWorked;
        }

        return cost;
    }

    public String toString()
    {
        return name + " " 
                + status + " Salary: " + salary
                + " " + all.toString();
    }
 
}

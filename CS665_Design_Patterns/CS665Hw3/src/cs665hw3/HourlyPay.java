
package cs665hw3;

/**
 *
 * @author Ryan
 */
public class HourlyPay implements PayBehavior {

    private HourlyEmployeeAllocation all;
    private String name;
    private String status;
    private double hourlyRate;

    public HourlyPay(String name,
                String status,
                double hourlyRate)
	{
        this.name = name;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }

    public void setMonthlyData(Allocation all)
    {
        this.all = (HourlyEmployeeAllocation)all;
    }

    public double[] getHours()
    {
        return all.getHours();
    }

    public double getProjectShare(int projectNumber)
    {
        return all.getProjectShares()[projectNumber];
    }

    public double getCost(int projectNumber)
    {
        double hoursWorked = getHours()[0];
        double percentWorked = getProjectShare(projectNumber);
        double cost = 0.0;

        if (hoursWorked > 40)
        {
            cost = hourlyRate * 40 * percentWorked;
            cost += (hourlyRate * percentWorked * (hoursWorked - 40)) * 1.5;
        }
        else
        {
            cost = hourlyRate * percentWorked * hoursWorked;
        }

        return cost;
    }

    @Override
    public String toString()
    {
        return name + " " 
                + status + " Hourly Rate: " + hourlyRate
                + " " + all.toString();
    }
 
}

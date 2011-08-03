
package cs665hw3;

/**
 *
 * @author Ryan
 */
public class Employee {

    private Allocation all;
    private PayBehavior payBehavior;

    public Employee()
	{
	}

    public void setPayBehavior(PayBehavior pb)
    {
        payBehavior = pb;
    }

    public void setMonthlyData(Allocation all)
    {
        payBehavior.setMonthlyData(all);
    }

    public Allocation getMonthlyData()
    {
        return all;
    }

    public double getCost(int projectNumber)
    {
        return payBehavior.getCost(projectNumber);
    }

    @Override
    public String toString()
    {
        return payBehavior.toString();
    }
    
}

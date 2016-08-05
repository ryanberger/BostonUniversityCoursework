
package cs665hw2;

/**
 *
 * @author Ryan
 */
public abstract class Employee {

    protected String name;
	protected String status;
	protected double salary;
    protected Allocation all;

    public Employee(String name,
                String status,
                double salary)
	{
		this.name = name;
		this.status = status;
		this.salary = salary;
	}

    public void setMonthlyData(Allocation all)
    {}

    public String getName()
    {
        return name;
    }

    public String getStatus()
    {
        return status;
    }

    public double getSalary()
    {
        return salary;
    }

    public Allocation getMonthlyData()
    {
        return all;
    }

    public abstract double getCost(int projectNumber);

    @Override
    public abstract String toString();
    
}

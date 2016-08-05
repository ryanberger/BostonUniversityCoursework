import java.util.HashMap;

public class Employee {

	private String name;
	private String status;
	private double salary;
	private HashMap time = new HashMap();
	
	public Employee(String name, 
                String status,
                double salary)
	{
		this.name = name;
		this.status = status;
		this.salary = salary;
	}
	
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
	
	public double[] getTime(String name)
	{
		return (double[])time.get(name);
	}
	
	public void setMonthlyData(String projName,
                double[] time)
	{
		this.time.put(projName, time);
	}
	
}

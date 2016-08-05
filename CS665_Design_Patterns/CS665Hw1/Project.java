
public class Project {
	
	private String name;
	private double percentUtilities;
	private double percentRent;
	private double percentEquipment;
	private double cost;
	
	public Project(String name,
                double percentUtilities,
                double percentRent,
                double percentEquipment)
	{
        this.name = name;
        this.percentUtilities = percentUtilities;
        this.percentRent = percentRent;
        this.percentEquipment = percentEquipment;
	}
	
	public double setProjectCost(Employee emp)
	{
        String empStatus = emp.getStatus();
        double empSalary = emp.getSalary();
	    double charge = 0.0;
		
	    try
	    {
	    	double percentWorked = emp.getTime(name)[0];
            
            if (empStatus.equals("Manager"))
            {
                charge = empSalary / 12 * percentWorked;
            }
            else if (empStatus.equals("Hourly"))
            {
                double hoursWorked = emp.getTime(name)[1];
                if (hoursWorked > 40)
                {
                    charge = empSalary * 40 * percentWorked;
                    charge += empSalary * percentWorked * (hoursWorked - 40);
                }
                else
                {
                    charge = empSalary * percentWorked * hoursWorked;
                }
            }
            else if (empStatus.equals("Exempt"))
            {
                charge = empSalary * percentWorked / 12 / 160;
            }
	    }
	    catch (Exception ex)
	    {
	    	System.out.println("Unable to retrieve Employee data for "
                        + emp.getName() + "!");
	    }
	    return charge;
	}
	
	public double getCost()
	{
        return cost;
	}
	
	public String getName()
	{
        return name;
	}

    public double getPercentUtilities()
    {
        return percentUtilities;
    }

    public double getPercentRent()
    {
        return percentRent;
    }

    public double getPercentEquipment()
    {
        return percentEquipment;
    }
}

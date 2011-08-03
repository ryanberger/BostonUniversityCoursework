
public class Allocation {
    
	private double[] time;
	
	public Allocation(String projName,
                double[] time,
                Employee emp)
	{
		this.time = time;
		emp.setMonthlyData(projName, time);
	}

}

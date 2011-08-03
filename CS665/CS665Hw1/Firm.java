import java.util.ArrayList;
import java.util.List;


public class Firm {

    private String month;
	private double utilities;
	private double rent;
	private double equipment;
	
	public Firm(String month,
                double utilities,
                double rent,
                double equipment)
	{
        this.month = month;
        this.utilities = utilities;
        this.rent = rent;
        this.equipment = equipment;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Firm firm = new Firm("April", 2000.0, 10000.0, 3000.0);
		
		List<Project> projectList = new ArrayList();
		String projectData = "Rose:0.2:0.4:0.6;Jasmine:0.8:0.6:0.4";
		
		String[] projects = projectData.split(";");
		
		for (String p: projects)
		{
			String[] projectGroup = p.split(":");
			Project tempProj = new Project(projectGroup[0],
                                Double.parseDouble(projectGroup[1]),
                                Double.parseDouble(projectGroup[2]),
                                Double.parseDouble(projectGroup[3]));
			
			projectList.add(tempProj);
		}
		
		List<Employee> employeeList = new ArrayList();
		String employeeData = "Green:Manager:90000.0:0.2#0.8;"
                        + "Black:Hourly:20.0:0.5,50.0#0.5,50.0;"
                        + "Brown:Hourly:10.0:0.4,30.0#0.6,30.0;"
                        + "Jones:Exempt:60000.0:80.0#80.0;"
                        + "Smith:Exempt:48000.0:40.0#120.0";
		
		String[] employees = employeeData.split(";");
		
		for (String e: employees)
		{
                    int hoursCount = 0;
                    String[] employeeGroup = e.split(":");
                    Employee tempEmp = new Employee(employeeGroup[0],
                            employeeGroup[1],
                            Double.parseDouble(employeeGroup[2]));
                    employeeList.add(tempEmp);

                    String[] hoursGroup = (employeeGroup[3]).split("#");

                    for (String h: hoursGroup)
                    {
                        String[] tempHours = h.split(",");
                        double[] hours = new double[tempHours.length];

                        for (int i=0; i < tempHours.length; i++)
                        {
                            if (tempHours[i] != null ||
                                tempHours[i].trim() != "")
                            {
                                hours[i] = Double.parseDouble(tempHours[i]);
                            }
                        }
                        new Allocation(projectList.get(hoursCount)
                            .getName(), hours, tempEmp);
				hoursCount++;
                    }
		}
                printResults(firm, projectList, employeeList);
		
		System.exit(0);
	}

    private static void printResults(Firm firm,
            List<Project> projectList,
            List<Employee> employeeList) {
        Boolean printOnce = true;
        System.out.println("Utilities, rent, equipment: " + firm.getUtilities()
                + " " + firm.getRent() + " " + firm.getEquipment());
        for (Project proj : projectList) {
            double projectCost = 0.0;
            for (Employee emp : employeeList) {
                if (printOnce) {
                    // Not all data is printed out here since it would require
                    // an enormous if-then-else construct. Inheritance makes
                    // much more sense to use instead.
                    System.out.print(emp.getName() + " " + emp.getStatus()
                            + ": " + emp.getSalary() + "\n");
                }
                projectCost += proj.setProjectCost(emp);
            }
            printOnce = false;
            System.out.println("Month of " + firm.getMonth() + " Project "
                    + proj.getName() + ": " + proj.getPercentUtilities()
                    + " " + proj.getPercentRent() + " "
                    + proj.getPercentEquipment() + " " + projectCost);
        }
    }

    public String getMonth()
    {
        return month;
    }

    public double getUtilities()
    {
        return utilities;
    }

    public double getRent()
    {
        return rent;
    }

    public double getEquipment()
    {
        return equipment;
    }
}

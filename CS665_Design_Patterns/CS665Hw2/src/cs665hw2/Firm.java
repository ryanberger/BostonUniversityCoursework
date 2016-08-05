
package cs665hw2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ryan
 */
public class Firm {

    private double utilities;
    private double rent;
    private double equipment;

    public Firm(double utilities,
            double rent,
            double equipment)
    {
        this.utilities = utilities;
        this.rent = rent;
        this.equipment = equipment;
    }

    public static void main(String[] args) {

        List<Project> projectList = new ArrayList();
        List<Employee> employeeList = new ArrayList();
        String firmData = "2000.0:10000.0:3000.0";
        String projectData = "April:Rose:0.2:0.4:0.6;"
                        + "April:Jasmine:0.8:0.6:0.4";
		String employeeData = "Green:Manager:90000.0:0.2,0.8;"
                        + "Black:Hourly:20.0:0.5,0.5:50.0;"
                        + "Brown:Hourly:10.0:0.4,0.6:30.0;"
                        + "Jones:Exempt:60000.0:80.0,80.0;"
                        + "Smith:Exempt:48000.0:40.0,120.0";

        String[] firms = firmData.split(":");

        Firm firm = new Firm(Double.parseDouble(firms[0]),
                Double.parseDouble(firms[1]),
                Double.parseDouble(firms[2]));

		String[] employees = employeeData.split(";");

		for (String e: employees)
		{
            Employee emp = null;
            double[] projectShares = null;
            double[] totalHours = null;
			String[] employeeGroup = e.split(":");
            String employeeType = employeeGroup[1];

            projectShares = getProjectInfo(employeeGroup, projectShares, 3);
            totalHours = getProjectInfo(employeeGroup, projectShares, 4);

            if (employeeType.equals("Manager"))
            {
                emp = new Manager(employeeGroup[0],
                        employeeType,
                        Double.parseDouble(employeeGroup[2]));
                ((Manager)emp)
                        .setMonthlyData(new ManagerAllocation(projectShares));
            }
            else if(employeeType.equals("Hourly"))
            {
                emp = new HourlyEmployee(employeeGroup[0],
                        employeeType,
                        Double.parseDouble(employeeGroup[2]));
                ((HourlyEmployee)emp)
                        .setMonthlyData(new
                        HourlyEmployeeAllocation(projectShares, totalHours[0]));
            }
            else if(employeeType.equals("Exempt"))
            {
                emp = new ExemptEmployee(employeeGroup[0],
                        employeeType,
                        Double.parseDouble(employeeGroup[2]));
                ((ExemptEmployee)emp)
                        .setMonthlyData(new
                        ExemptEmployeeAllocation(projectShares));
            }
			employeeList.add(emp);
        }

        int projectNumber = 0;
        String[] projects = projectData.split(";");
		for (String p: projects)
		{
			String[] projectGroup = p.split(":");
			Project tempProj = new Project(projectGroup[0],
                                projectGroup[1],
                                Double.parseDouble(projectGroup[2]),
                                Double.parseDouble(projectGroup[3]),
                                Double.parseDouble(projectGroup[4]),
                                employeeList,
                                firm,
                                projectNumber);
            tempProj.setProjectCost();
            projectList.add(tempProj);
            projectNumber++;
		}

        printReport(employeeList, projectList, firm);

    }

    /**
     * Extracts projectInfo from input string
     * @param employeeGroup
     * @param projectShares
     * @param index
     * @return projectShares
     * @throws NumberFormatException
     */
    private static double[] getProjectInfo(String[] employeeGroup,
            double[] projectShares,
            int index) throws NumberFormatException {
        if (employeeGroup.length > index)
        {
            int i = 0;
            String[] projectTemp = (employeeGroup[index]).split(",");
            projectShares = new double[projectTemp.length];
            for (String h : projectTemp) {
                projectShares[i] = Double.parseDouble(h);
                i++;
            }
        }
        return projectShares;
    }

    /**
     * Prints report of employee and project data
     * @param empList
     * @param projList
     */
    private static void printReport(List<Employee> empList,
            List<Project> projList,
            Firm firm)
    {
        for (Employee emp: empList)
        {
            System.out.println(emp.toString());
        }
        System.out.println(firm.toString());
        
        for (Project proj: projList)
        {
            System.out.println(proj.toString());
        }
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
    
    @Override
    public String toString()
    {
        return "Utilities, Rent, Equipment: "
                + utilities + " " + rent + " " + equipment;
    }

}


package cs665hw2;

import java.util.List;

/**
 *
 * @author Ryan
 */
public class Project {

    private String month;
    private String name;
    private double percentUtilities;
    private double percentRent;
    private double percentEquipment;
    private List<Employee> employees;
    private Firm firm;
    private int projectNumber;
    private double cost = 0.0;

    public Project(String month,
            String name,
            double percentUtilities,
            double percentRent,
            double percentEquipment,
            List<Employee> employees,
            Firm firm,
            int projectNumber)
    {
        this.month = month;
        this.name = name;
        this.percentUtilities = percentUtilities;
        this.percentRent = percentRent;
        this.percentEquipment = percentEquipment;
        this.employees = employees;
        this.firm = firm;
        this.projectNumber = projectNumber;
    }

    public void setProjectCost()
	{
        for (Employee emp: employees)
        {
            cost += emp.getCost(projectNumber);
        }
	}

    public double getProjectCost()
    {
        return cost;
    }

    @Override
    public String toString()
    {
        double utilities = (firm.getUtilities() * percentUtilities);
        double rent = (firm.getRent() * percentRent);
        double equipment = (firm.getEquipment() * percentEquipment);
        double total = utilities + rent + equipment + cost;

        return "Month of "
                + month + " Project " + name + ": "
                + utilities + " "
                + rent + " "
                + equipment + " "
                + cost + " "
                + total;
    }

}

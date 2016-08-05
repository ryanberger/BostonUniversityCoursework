
package cs665hw8;

/**
 *
 * @author Ryan
 */
public class AnalyzeUpperCase extends Analyze {

    public AnalyzeUpperCase(String inputString)
    {
        this.inputString = inputString;
    }

    public void testString()
    {
        ContainerIterator iter = new ContainerIterator(inputString);

        while (iter.hasNext())
        {
            char a = iter.next();

            if (Character.isUpperCase(a))
            {
                counter++;
            }
        }
    }

    public void printReport()
    {
        String outputString = counter + " upper case character";

        if (counter > 1)
        {
            outputString += "s";
        }

        System.out.println(outputString);
    }
}

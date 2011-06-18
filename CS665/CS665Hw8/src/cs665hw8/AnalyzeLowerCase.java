
package cs665hw8;

/**
 *
 * @author Ryan
 */
public class AnalyzeLowerCase extends Analyze {

    public AnalyzeLowerCase(String inputString)
    {
        this.inputString = inputString;
    }

    public void testString()
    {
        ContainerIterator iter = new ContainerIterator(inputString);

        while (iter.hasNext())
        {
            char a = iter.next();

            if (Character.isLowerCase(a))
            {
                counter++;
            }
        }
    }

    public void printReport()
    {
        String outputString = counter + " lower case character";

        if (counter > 1)
        {
            outputString += "s";
        }

        System.out.println(outputString);
    }
}

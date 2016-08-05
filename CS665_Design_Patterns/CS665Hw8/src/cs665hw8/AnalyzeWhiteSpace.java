
package cs665hw8;

/**
 *
 * @author Ryan
 */
public class AnalyzeWhiteSpace extends Analyze {

    public AnalyzeWhiteSpace(String inputString)
    {
        this.inputString = inputString;
    }

    public void testString()
    {
        ContainerIterator iter = new ContainerIterator(inputString);

        while (iter.hasNext())
        {
            char a = iter.next();

            if (!Character.isSpaceChar(a))
            {
                counter++;
            }
        }
    }

    @Override
    public boolean isWhiteSpaceTest()
    {
        return true;
    }

    public void printReport()
    {
        String outputString = counter + " non-white symbol";

        if (counter > 1)
        {
            outputString += "s";
        }

        System.out.println(outputString);
    }
}

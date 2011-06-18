
package cs665hw8;

/**
 *
 * @author Ryan
 */
public abstract class Analyze {

    protected int counter = 0;
    protected String inputString = "";

    public void initializeCounter()
    {
        counter = 0;
    }

    public abstract void testString();

    public boolean isWhiteSpaceTest()
    {
        return false;
    }

    public void printInputString()
    {
        System.out.println("String: " + inputString);
    }

    public abstract void printReport();

    public final void performAnalysis()
    {
        initializeCounter();
        testString();
        if (isWhiteSpaceTest())
        {
            printInputString();
        }
        printReport();
    }
}

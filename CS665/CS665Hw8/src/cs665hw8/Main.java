
package cs665hw8;

/**
 *
 * @author Ryan
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String test1 = "\"TEST\"";
        String test2 = "A test";
        String test3 = "This Is Another Test";

        Analyze analyze = new AnalyzeWhiteSpace(test1);
        analyze.performAnalysis();
        analyze = new AnalyzeUpperCase(test1);
        analyze.performAnalysis();
        analyze = new AnalyzeLowerCase(test1);
        analyze.performAnalysis();

        System.out.println();

        analyze = new AnalyzeWhiteSpace(test2);
        analyze.performAnalysis();
        analyze = new AnalyzeUpperCase(test2);
        analyze.performAnalysis();
        analyze = new AnalyzeLowerCase(test2);
        analyze.performAnalysis();

        System.out.println();

        analyze = new AnalyzeWhiteSpace(test3);
        analyze.performAnalysis();
        analyze = new AnalyzeUpperCase(test3);
        analyze.performAnalysis();
        analyze = new AnalyzeLowerCase(test3);
        analyze.performAnalysis();

        System.out.println("\nContainer data in the upper case:");
        Request ruc = new RequestUpperCase();
        Container cont = new Container(test1);
        cont.internalIterator(ruc);
        cont = new Container(test2);
        cont.internalIterator(ruc);
        cont = new Container(test3);
        cont.internalIterator(ruc);

        System.out.println("\nContainer data in the upper case:");
        Request rlc = new RequestLowerCase();
        cont = new Container(test1);
        cont.internalIterator(rlc);
        cont = new Container(test2);
        cont.internalIterator(rlc);
        cont = new Container(test3);
        cont.internalIterator(rlc);

    }
}


package cs665hw8;

/**
 *
 * @author Ryan
 */
public class Container {

    private String inputString = "";
    private ContainerIterator cont;

    public Container(String inputString)
    {
        this.inputString = inputString;
    }

    public ContainerIterator iterator(String inputString)
    {
        if (cont == null)
        {
            cont = new ContainerIterator(inputString);
        }

        return cont;
    }

    public void internalIterator(Request request)
    {
        CharacterItem c;
        for (int i = 0; i < inputString.length(); i++)
        {
            c = new CharacterItem(iterator(inputString).next());
            request.apply(c);
        }
        System.out.println();
        
    }
}

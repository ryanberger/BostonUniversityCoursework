
package cs665hw8;

import java.util.Iterator;

/**
 *
 * @author Ryan
 */
public class ContainerIterator implements Iterator<Character> {

    private char[] items;
    private int position;

    public ContainerIterator(String items)
    {
        this.items = items.toCharArray();
    }

    public Character next()
    {
        return items[position++];
    }

    public boolean hasNext()
    {
        return (position + 1 <= items.length && items[position] != '\0');
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}

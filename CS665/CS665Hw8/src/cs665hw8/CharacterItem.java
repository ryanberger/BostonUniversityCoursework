
package cs665hw8;

/**
 *
 * @author rberger
 */
public class CharacterItem {

    private char c;

    public CharacterItem(char c)
    {
        this.c = c;
    }

    public void toLower()
    {
        c = Character.toLowerCase(c);
    }

    public void toUpper()
    {
        c = Character.toUpperCase(c);
    }

    public void printCharacterItem()
    {
        System.out.print(c);
    }
}

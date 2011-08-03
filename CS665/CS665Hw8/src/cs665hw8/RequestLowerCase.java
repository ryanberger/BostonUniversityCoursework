
package cs665hw8;

/**
 *
 * @author Ryan
 */
public class RequestLowerCase extends Request {

    public void apply(CharacterItem c)
    {
        c.toLower();
        c.printCharacterItem();
    }
}

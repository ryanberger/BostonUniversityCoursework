
package cs665hw8;

/**
 *
 * @author rberger
 */
public class RequestUpperCase extends Request {

    public void apply(CharacterItem c)
    {
        c.toUpper();
        c.printCharacterItem();
    }
}

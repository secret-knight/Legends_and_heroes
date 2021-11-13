/**
 * inaccessible cell
 */
public class Inaccessible extends Tile{
    private final static String    marker  = "I";
    public Inaccessible(Character character) {
        super(character, marker);
    }
    
    public Inaccessible()
    {
        super(marker);
    }

    @Override
    public String toString() {
        return "\u001b[41;1m   " + getSymbol() + "   \u001b[0m";
    }
}

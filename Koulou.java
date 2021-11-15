/**
 * Koulou cell that has 10% strength buff
 */
public class Koulou extends Tile{
    private final static Attribute attr    = Attribute.STRENGTH;
    private final static int       attPcnt = 10;
    private final static String    marker  = "K";
    
    public Koulou(Character character) {
        super(character, attr, attPcnt, marker);
    }
    public Koulou()
    {
        super(attr, attPcnt, marker);
    }
    @Override
    public String toString() {
        if(getSymbol().length() == 1)
        {
            return "\u001b[43;1m   " + getSymbol() + "   \u001b[0m";
        }
        else
        {
            return "\u001b[43;1m  " + getSymbol() + "  \u001b[0m";
        }
    }
}

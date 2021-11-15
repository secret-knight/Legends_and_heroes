/**
 * nexus cell 
 */
public class Nexus extends Tile{
    private final static String    marker  = "N";
    public Nexus(Character character) {
        super(character, marker);
    }
    
    public Nexus()
    {
        super(marker);
    }

    @Override
    public String toString() {
        if(getSymbol().length() == 1)
        {
            return "\u001b[44;1m   " + getSymbol() + "   \u001b[0m";
        }
        else
        {
            return "\u001b[44;1m  " + getSymbol() + "  \u001b[0m";
        }
    }
}

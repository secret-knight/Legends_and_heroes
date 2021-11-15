/**
 * plain cell that has no buff attr
 */
public class Plain extends Tile{
    private final static String marker = "P";
    public Plain(Character character) {
        super(character, marker);
    }
    
    public Plain()
    {
        super(marker);
    }

    @Override
    public String toString() {
        if(getSymbol().length() == 1)
        {
            return "\u001b[42;1m   " + getSymbol() + "   \u001b[0m";
        }
        else
        {
            return "\u001b[42;1m  " + getSymbol() + "  \u001b[0m";
        }
    }
}

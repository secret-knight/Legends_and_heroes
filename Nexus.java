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
        return "\u001b[44;1m   " + getSymbol() + "   \u001b[0m";
    }
}

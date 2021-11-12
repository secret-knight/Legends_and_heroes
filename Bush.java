public class Bush extends Tile{
    private final static Attribute attr    = Attribute.DEXTERITY;
    private final static int       attPcnt = 10;
    private final static String    marker  = "B";
    public Bush(Character character) {
        super(character, attr, attPcnt, marker);
    }
    
    public Bush()
    {
        super(attr, attPcnt, marker);
    }
    
    @Override
    public String toString() {
        return "\u001b[45;1m   " + getSymbol() + "   \u001b[0m";
    }
}

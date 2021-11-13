/**
 * Cave cell that has 10% agility buff
 */
public class Cave extends Tile{
    private final static Attribute attr    = Attribute.AGILITY;
    private final static int       attPcnt = 10;
    private final static String    marker  = "C";
    public Cave(Character character) {
        super(character, attr, attPcnt, marker);
    }
    
    public Cave()
    {
        super(attr, attPcnt, marker);
    }
    
    @Override
    public String toString() {
        return "\u001b[47;1m   " + getSymbol() +"   \u001b[0m";
    }
}

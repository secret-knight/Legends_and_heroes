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
        return "\u001b[43;1m   " + getSymbol() + "   \u001b[0m";
    }
}

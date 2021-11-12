public abstract class Tile {
    private Character character;
    private Attribute buffAttribute; 
    private String symbol;
    private int buffIncrPcnt;
    
    public Tile(String symbol)
    {
        this(null, null, 0, symbol);
    }
    public Tile(Character character, String symbol)
    {
        this(character, null, 0, symbol);
    }
    public Tile(Attribute buffAttribute, int buffIncrPcnt, String symbol)
    {
        this(null, buffAttribute, buffIncrPcnt, symbol);
    }
    public Tile(Character character, Attribute buffAttribute, int buffIncrPcnt, String symbol)
    {
        this.setCharacter(character);
        this.setBuffAttribute(buffAttribute);
        this.setBuffIncrPcnt(buffIncrPcnt);
        this.setSymbol(symbol);
    }
    
    /**
     * whether this Tile contains character
     * @return
     */
    public boolean isEmpty()
    {
        return this.getCharacter() == null;
    }
    
    public Character getCharacter()
    {
        return character;
    }

    public void moveCharacterFrom(Tile tile)
    {
        this.setCharacter(tile.getCharacter());
        tile.removeCharacter();
    }
    public void setCharacter(Character character)
    {
        // only hero can get buff
        if(character instanceof Hero && getBuffAttribute() != null)
        {
            character.applyBuff(getBuffAttribute(), getBuffIncrPcnt());
        }
        this.character = character;
    }
    
    public void removeCharacter()
    {
        if(this.buffAttribute != null)
        {
            this.buffAttribute = null;
            this.buffIncrPcnt  = 0;
        }
        this.character = null;
    }
    
    public Attribute getBuffAttribute()
    {
        return buffAttribute;
    }
    public void setBuffAttribute(Attribute buffAttribute)
    {
        this.buffAttribute = buffAttribute;
    }
    public int getBuffIncrPcnt()
    {
        return buffIncrPcnt;
    }
    public void setBuffIncrPcnt(int buffIncrPcnt)
    {
        this.buffIncrPcnt = buffIncrPcnt;
    }
    public String getSymbol()
    {
        if(getCharacter() == null)
        {
            return symbol;
        }
        else
        {
            return "H";
        }
    }
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
}

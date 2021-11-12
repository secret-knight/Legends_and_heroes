import java.util.ArrayList;
import java.util.List;

public abstract class Tile {
    private List<Character> characters = new ArrayList<Character>();;
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
        this.addCharacter(character);
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
        return this.getCharacters().isEmpty();
    }
    
    public List<Character> getCharacters()
    {
        return characters;
    }
    
    public void moveCharacterFrom(Tile from, Character character)
    {
        if(from != null)
        {
            from.removeCharacter(character);
        }
        this.addCharacter(character);
    }
    public void addCharacter(Character character)
    {
        if(character == null)
        {
            return;
        }
        // only hero can get buff
        if(character instanceof Hero && getBuffAttribute() != null)
        {
            character.applyBuff(getBuffAttribute(), getBuffIncrPcnt());
        }
        this.characters.add(character);
    }
    
    public void removeCharacter(Character character)
    {
        if(this.buffAttribute != null)
        {
            this.buffAttribute = null;
            this.buffIncrPcnt  = 0;
        }
        this.characters.remove(character);
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
        if(getCharacters().isEmpty())
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

import java.util.ArrayList;
import java.util.List;

/**
 * abstract class that for Tile
 * Contains max 2 and must be different characters 
 */
public abstract class Tile {
    private List<Character> characters    = new ArrayList<Character>();
    private Attribute       buffAttribute; 
    private int             buffIncrPcnt;
    private String          symbol;
    
    // constructors 
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
    
    /**
     * move character from another tile to current tile
     * 
     * @param from      Tile that come from
     * @param character character that moving
     * @return          move successful or not
     */
    public boolean moveCharacterFrom(Tile from, Character character)
    {
        // check if we can add character to current tile
        // - we can only has max 2 character and they must not belongs to different party
        // check if we can move the character
        // - we can not move heros and monsters pass through each other
        if(!canAddCharacter(character))
        {
            return false;
        }
        if(from != null)
        {
            from.removeCharacter(character);
        }
        this.addCharacter(character);
        return true;
    }
    
    /**
     * check if we can add another character to current tile
     * @param character
     * @return
     */
    public boolean canAddCharacter(Character character)
    {
        // if empty
        if(this.isEmpty())
        {
            return true;
        }
        
        // if not empty
        // should only be able to add another one 
        // is the one that different that existing one
        // using exclusive-or (^) operation to check whether they are same kind
        if(this.characters.size() < 2)
        {
            return (characters.get(0) instanceof Hero) ^ (character instanceof Hero);
        }
        return false;
    }
    
    /**
     * add character to current Tile
     * 
     * Hero Monster
     * or
     * Monster Hero
     * 
     * no same type of character in one Tile
     * @param character to be added
     */
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
    
    /**
     * remove character from current tile
     * remove their buff
     * @param character
     */
    public void removeCharacter(Character character)
    {
        if(character.getBuffedAttributes().containsKey(getBuffAttribute()))
        {
            character.removeBuff(getBuffAttribute(), getBuffIncrPcnt());
        }
        this.characters.remove(character);
    }
    
    // getters and setters
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
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    /**
     * characters getter
     * @return 
     */
    public List<Character> getCharacters()
    {
        return characters;
    }
    
    // toString
    public String getSymbol()
    {

        if(getCharacters().isEmpty())
        {
            return "    " + symbol + "    ";
        }
        else
        {
            // if contains only 1 character, return their symbol
            if(getCharacters().size() == 1)
            {
                if(getCharacters().get(0) instanceof Hero)
                {
                    return " H" + (Player.getPlayer().getHeroes().indexOf(getCharacters().get(0))+1) + " " + symbol + "    ";
                }
                else
                {
                    return " M  " + symbol + "    ";
                }
            }
            // if contains 2 return a combo
            else
            {
                if(getCharacters().get(0) instanceof Hero)
                {
                    return " H" + (Player.getPlayer().getHeroes().indexOf(getCharacters().get(0))+1) + " " + symbol + " M  ";
                }
                else
                {
                    return " M  " + symbol + " H" + (Player.getPlayer().getHeroes().indexOf(getCharacters().get(1))+1) + " ";
                }
            }
        }
    }
}

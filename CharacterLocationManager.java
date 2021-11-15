import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Map.Entry;

public class CharacterLocationManager <T extends Character>
{
    private HashMap<T, Coordinate> characters;
    private PriorityQueue<T>       locationsPQ;
    private int                    origin;
    
    /**
     * constructor 
     * @param origin
     */
    public CharacterLocationManager(int origin)
    {
        this.origin      = origin;
        this.characters  = new LinkedHashMap<T, Coordinate>();
        this.locationsPQ = new PriorityQueue<T>(new Comparator<T>() 
        {
            @Override
            public int compare(T o1, T o2)
            {
                return distanceToOrigin(characters.get(o1)) - distanceToOrigin(characters.get(o2));
            }
        });
    }
    
    // CRUD functions
    public void add(T character, Coordinate cord)
    {
        this.characters.put(character, cord);
        this.locationsPQ.offer(character);
    }
    
    public Coordinate getCharacterCoordinate(T character)
    {
        return this.characters.get(character);
    }
    
    public Coordinate getFrontCoordinate()
    {
        return characters.get(getFrontCharacter());
    }
    
    public T getFrontCharacter()
    {
        return locationsPQ.peek();
    }
    
    public void updateLocation(T character, Coordinate cord)
    {
        Coordinate localCord = this.characters.get(character);
        if(localCord == null)
        {
            this.add(character, cord);
        }
        else
        {
            localCord.setCol(cord.getCol());
            localCord.setRow(cord.getRow());
        }
    }
    
    public void remove(T character)
    {
        this.characters.remove(character);
        this.locationsPQ.remove(character);
    }
    
    public Iterator<T> iterator()
    {
        return this.characters.keySet().iterator();
    }
    
    // helper function
    private int distanceToOrigin(Coordinate cord)
    {
        return Math.abs(cord.getRow() - origin);
    }

    // getters and setters
    public List<T> getCharacters()
    {
        return new ArrayList<T>(this.characters.keySet());
    }
    
    public Set<Entry<T, Coordinate>> getEntrySet()
    {
        return this.characters.entrySet();
    }

    public int getOrigin()
    {
        return origin;
    }

    public void setOrigin(int origin)
    {
        this.origin = origin;
    }
}

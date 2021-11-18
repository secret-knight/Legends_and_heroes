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
    private Coordinate             origin;
    
//    /**
//     * constructor
//     * @param origin
//     */
    public CharacterLocationManager(int originRow, int originCol)
    {
        this(new Coordinate(originRow, originCol));
    }
    
    public CharacterLocationManager(Coordinate cord)
    {
        this.setOriginCoordinate(cord);
        this.characters  = new LinkedHashMap<T, Coordinate>();
        this.locationsPQ = new PriorityQueue<T>(new Comparator<T>() 
        {
            @Override
            public int compare(T o1, T o2)
            {
                return distanceToOrigin(characters.get(o2)) - distanceToOrigin(characters.get(o1));
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
    
    public int getFurthermostDistance()
    {
        int res;
        if(this.locationsPQ.isEmpty())
        {
            res = 0;
        }
        else
        {
            res = distanceToOrigin(this.characters.get(getFrontCharacter()));
        }
        return res;
    }

    public Coordinate getTeleportCoordinate() {
        if (locationsPQ.isEmpty())
            return origin;
        else if (locationsPQ.size() == 1)
            return new Coordinate(getFrontCoordinate().getRow(), -(getFrontCoordinate().getCol()-1));
        else {
            Iterator<T> iter = locationsPQ.iterator();
            iter.next();
            // if row has two heroes on it, one in each column
            if (distanceToOrigin(getFrontCoordinate()) == distanceToOrigin(getCharacterCoordinate(iter.next()))) {
                // check if full row is nexus. if so, return null so teleport is disabled
                if (distanceToOrigin(getFrontCoordinate()) == 0)
                    return null;
                // else return the row below the full row
                else
                    return new Coordinate(getFrontCoordinate().getRow() + 1, getFrontCoordinate().getCol());
            }
            // if front most row has only one hero, return the other column of the same row
            else
                return new Coordinate(getFrontCoordinate().getRow(), -(getFrontCoordinate().getCol()));
        }
    }
    
    public int size()
    {
        return getCharacters().size();
    }
    
    public boolean isEmpty()
    {
        return this.size() == 0;
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

    public int distanceToOrigin(Coordinate cord)
    {
        return Math.abs(cord.getRow() - this.getOriginRow());
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

    public int getCharacterDistance(T character) {return distanceToOrigin(getCharacterCoordinate(character));}

    public int getOriginRow()
    {
        return this.getOriginCoordinate().getRow();
    }

    public void setOriginRow(int originRow)
    {
        this.getOriginCoordinate().setRow(originRow);
    }

    public int getOriginCol()
    {
        return this.getOriginCoordinate().getCol();
    }

    public void setOriginCol(int originCol)
    {
        this.getOriginCoordinate().setCol(originCol);;
    }
    
    public Coordinate getOriginCoordinate()
    {
        return this.origin; 
    }
    
    public void setOriginCoordinate(Coordinate cord)
    {
        this.origin = cord;
    }
}

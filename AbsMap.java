import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AbsMap
{
    private int            rowNum;
    private int            colNum;
    private int            laneNum;
    private LaneCollection laneCollection;
    private Set<Hero>      actedHero;
    private HashMap<Character, LOVlane> recallingCharacters;

    
    public AbsMap(int rowNum, int colNum, int laneNum)
    {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.laneNum = laneNum;
        setLaneCollection(new LaneCollection());
        this.actedHero = new HashSet<>();
        this.recallingCharacters = new HashMap<>();
    }
    
    public int getRowNum()
    {
        return rowNum;
    }
    public void setRowNum(int rowNum)
    {
        this.rowNum = rowNum;
    }
    public int getColNum()
    {
        return colNum;
    }
    public void setColNum(int colNum)
    {
        this.colNum = colNum;
    }
    public int getLaneNum()
    {
        return laneNum;
    }
    public void setLaneNum(int laneNum)
    {
        this.laneNum = laneNum;
    }

    public LaneCollection getLaneCollection()
    {
        return laneCollection;
    }

    public void setLaneCollection(LaneCollection laneCollection)
    {
        this.laneCollection = laneCollection;
    }

    public Set<Hero> getActedHero()
    {
        return actedHero;
    }

    public void setActedHero(Set<Hero> actedHero)
    {
        this.actedHero = actedHero;
    }
    
    public HashMap<Character, LOVlane> getRecallingCharacters()
    {
        return recallingCharacters;
    }

    public void setRecallingCharacters(HashMap<Character, LOVlane> recallingCharacters)
    {
        this.recallingCharacters = recallingCharacters;
    }
}

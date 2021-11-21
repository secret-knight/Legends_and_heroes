import java.util.ArrayList;
import java.util.List;

/**
 * collection class for Lane instances, in charge of lane rotate 
 */
public class LaneCollection
{
    private List<LOVLane> lanes;
    private int        nextIdx;
    public LaneCollection()
    {
        this.lanes   = new ArrayList<LOVLane>();
        this.nextIdx = 0;
    }
    
    public void add(LOVLane lane)
    {
        this.lanes.add(lane);
    }
    
    public int size()
    {
        return this.lanes.size();
    }
    
    public LOVLane getNext()
    {
        LOVLane lane = this.lanes.get(nextIdx);
        nextIdx   = (nextIdx + 1) % this.lanes.size();
        
        return lane;
    }
    
    public boolean isEmpty()
    {
        return this.lanes.isEmpty();
    }

    public List<LOVLane> getLaneList()
    {
        return this.lanes;
    }
}

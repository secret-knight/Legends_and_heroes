import java.util.ArrayList;
import java.util.List;

public class LaneCollection
{
    private List<LOVlane> lanes;
    private int        nextIdx;
    public LaneCollection()
    {
        this.lanes   = new ArrayList<LOVlane>();
        this.nextIdx = 0;
    }
    
    public void add(LOVlane lane)
    {
        this.lanes.add(lane);
    }
    
    public int size()
    {
        return this.lanes.size();
    }
    
    public LOVlane getNext()
    {
        LOVlane lane = this.lanes.get(nextIdx);
        nextIdx   = (nextIdx + 1) % this.lanes.size();
        
        return lane;
    }
    
    public boolean isEmpty()
    {
        return this.lanes.isEmpty();
    }

    public List<LOVlane> getLaneList()
    {
        return this.lanes;
    }
}

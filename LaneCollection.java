import java.util.ArrayList;
import java.util.List;

public class LaneCollection
{
    private List<Lane> lanes;
    private int        nextIdx;
    public LaneCollection()
    {
        this.lanes   = new ArrayList<Lane>();
        this.nextIdx = 0;
    }
    
    public void add(Lane lane)
    {
        this.lanes.add(lane);
    }
    
    public int size()
    {
        return this.lanes.size();
    }
    
    public Lane getNext()
    {
        Lane lane = this.lanes.get(nextIdx);
        nextIdx   = (nextIdx + 1) % this.lanes.size();
        
        return lane;
    }
    
    public boolean isEmpty()
    {
        return this.lanes.isEmpty();
    }

    public List<Lane> getLaneList()
    {
        return this.lanes;
    }
}


public abstract class AbsLane
{
    private Tile[][] tiles;
    
    public AbsLane(int rowNum, int colNum)
    {
        this.tiles = new Tile[rowNum][colNum];
    }
    
    public Tile[][] getTiles()
    {
        return tiles;
    }
    
    public Tile getTile(int row, int col)
    {
        return getTiles()[row][col];
    }
    
    public void setTile(int row, int col, Tile tile)
    {
        this.getTiles()[row][col] = tile; 
    }

    public void setTiles(Tile[][] tiles)
    {
        this.tiles = tiles;
    }
}

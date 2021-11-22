
/**
 * util class for encap row + col combo
 */
public class Coordinate
{
    private int row;
    private int col;
    
    public Coordinate(int row, int col)
    {
        this.setRow(row);
        this.setCol(col);
    }
    
    public Coordinate(Coordinate another)
    {
        this(another.getRow(), another.getCol());
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public void setCol(int col)
    {
        this.col = col;
    }
}

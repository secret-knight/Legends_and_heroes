public abstract class AbsRPGGame implements IRPGGame
{
    private int rowNum;
    private int colNum;
    private int laneNum;

    public AbsRPGGame(int rowNum, int colNum, int laneNum)
    {
        this.setRowNum(rowNum);
        this.setColNum(colNum);
        this.setLaneNum(laneNum);
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
    
    
    
    
}

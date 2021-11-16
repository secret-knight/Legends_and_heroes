import java.util.*;
import java.util.Map.Entry;

// represents the map of the game, singleton instance
public class Map {
    private static       Map                      map;

    private              LaneCollection           laneCollection;
    private              Tile                     currentTile;
    private              HashMap<Character, Lane> recallingCharacters;
    private              int                      currentRow;
    private              int                      currentCol;
    private        final int                      rows                = 8;
    private        final int                      cols                = 8;
    private static final int                      NUMOFLANE           = 3;
    private Map() {
        laneCollection      = new LaneCollection();
        recallingCharacters = new LinkedHashMap<Character, Lane>();
        for (int i = 0; i < NUMOFLANE; i++)
            laneCollection.add(new Lane(Player.getPlayer().getHeroes().get(i)));
        System.out.println(this);
    }

//    public boolean playerPlaced(int i, int j) {
//        Tile t = tiles[i][j];
//        if (t != Tile.Inaccessible) {
//            if (currentTile != null) {
//                tiles[currentRow][currentCol] = currentTile;
//            }
//            currentTile = t;
//            currentRow = i;
//            currentCol = j;
//            if (t == Tile.Market) {
//                tiles[i][j] = Tile.OccupiedMarket;
//            } else {
//                tiles[i][j] = Tile.OccupiedCommon;
//            }
//            return true;
//        }
//        return false;
//    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

//    public Tile getSpecificTile(int i, int j) { return tiles[i][j];}

    public static Map getMap() {
        if (map == null) {
            map = new Map();
        }
        return map;
    }

    private String getRowDivider() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("   ");
        for (int j = 0; j < cols; j++) {
            stringBuilder.append("+-----");
        }
        return stringBuilder.append("+").toString();
    }

    public void move() {
        // since laneCollection.getNext() is infinite rotate, 
        // e.g: 0 -> 1 -> 2 -> 3 -> 0 -> 1
        // we need to count from 0 to collection.size();

        // hero move, attack a monster
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            this.laneCollection.getNext().move();
        }
        
        // monster turn
        // at every round each monster either attack a hero
        // or 
        // move one cell "forward"
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            this.laneCollection.getNext().moveMonster();
        }
        
        // move characters that recalling to base
        for(Entry<Character, Lane> recallEntry : getRecallingCharacters().entrySet())
        {
            Character character = recallEntry.getKey();
            Lane      lane      = recallEntry.getValue();
            teleport(character, 
                    lane.getCharacterLocation(character), 
                    lane, 
                    character.getOrgLane().getOrgCord(character), 
                    character.getOrgLane());
        }
    }

    private List<Iterator<String>> getLaneStrIters() {
        List<Iterator<String>> laneStrIters = new ArrayList<Iterator<String>>();

        for (Lane lane : laneCollection.getLaneList())
            laneStrIters.add(lane.getString().iterator());

        return laneStrIters;
    }

    @Override
    public String toString() {

        String[] controls = new String[]{"                  Controls & Info             |\n",
                                         "         +-------------------------------+    |\n",
                                         "         |w = Move up   | a = Move left  |    |\n",
                                         "         |s = Move down | d = Move right |    |\n",
                                         "         |e = Inventory | i = Info       |    |\n",
                                         "         |q = Quit game |                |    |\n",
                                         "         +-------------------------------+    |\n",
                                         "         |     You are currently on a    |    |\n",
                                         "         |                               |    |\n",
                                         "         |                               |    |\n",
                                         "         +-------------------------------+    |\n",
                                         "         |           C = Common          |    |\n",
                                         "         |           M = Market          |    |\n",
                                         "         |        I = Inaccessible       |    |\n",
                                         "         +-------------------------------+    |\n",
                                         "                                              |\n"};

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|                                                MAP                                                 |\n" +
                "|                                                                                                    |\n");

        List<Iterator<String>> lanesStrIter = getLaneStrIters();
        int count = 0;

        while (lanesStrIter.get(0).hasNext()) {
            String line = "";
            for (int i = 0; i < lanesStrIter.size(); i++) {
                line += lanesStrIter.get(i).next();
                if (i != lanesStrIter.size() - 1)
                    if (count % 2 == 0)
                        line += "-------";
                    else
                        line += new Inaccessible();
            }
            count ++;
            stringBuilder.append("|" + Utils.getStringWithNumChar(line, line.length()+35) + "|\n");
        }
        stringBuilder.append("|                                                                                                    |\n" +
                "+----------------------------------------------------------------------------------------------------+");

        return stringBuilder.toString();
    }

    public void recall(Character character, Lane currentLane)
    {
        this.recallingCharacters.put(character, currentLane);
    }
    
    public void teleportToOtherLane(Character character, Coordinate org, Lane orgLane)
    {
        //TODO 
        // print map lanes with indexed Tile, do not show other lane;
        // ask user input the index number
        // call teleport
    }
    
    public void teleport(Character character, Coordinate org, Lane orgLane, Coordinate dest, Lane destLane)
    {
        destLane.placeCharacter(org, dest, character);
    }

    public HashMap<Character, Lane> getRecallingCharacters()
    {
        return recallingCharacters;
    }
}

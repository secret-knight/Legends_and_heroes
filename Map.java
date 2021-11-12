import java.util.*;

// represents the map of the game, singleton instance
public class Map {
    private static Map map;

    private List<Lane> lanes;
    private Tile currentTile;
    private int currentRow;
    private int currentCol;
    private final int rows = 8;
    private final int cols = 8;

    private Map() {
        lanes = new ArrayList<Lane>();
        for (int i = 0; i < 3; i++)
            lanes.add(new Lane());
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

//    public void move() {
//        boolean moved = false;
//        boolean closed = false;
//
//        while (!closed && !Player.getPlayer().isGameOver()) {
//            System.out.println(this);
//            String action = Utils.getValidInputString(new String[]{"e", "k", "w", "s", "a", "d", "i", "q"});
//            switch (action) {
//                case "e":
//                    Inventory.enterInventoryScreen(null, true);
//                    break;
//                case "k":
//                    if (getCurrentTile() != Tile.Market)
//                        System.out.println("Can't enter market, you are not on a market tile.");
//                    else {
//                        Market market = new Market(Player.getPlayer().getMaxHeroLevel());
//                        market.enterMarket();
//                    }
//                    break;
//                case "w":
//                    moved = updateMapAfterMoveUp();
//                    closed = true;
//                    break;
//                case "s":
//                    moved = updateMapAfterMoveDown();
//                    closed = true;
//                    break;
//                case "a":
//                    moved = updateMapAfterMoveLeft();
//                    closed = true;
//                    break;
//                case "d":
//                    moved = updateMapAfterMoveRight();
//                    closed = true;
//                    break;
//                case "i":
//                    Player.getPlayer().enterInfoScreen();
//                    break;
//                default:
//                    System.out.println("Game ended");
//                    Player.getPlayer().setGameOver(true);
//                    break;
//            }
//        }
//        if (moved && getCurrentTile() == Tile.Common) {
//            if (Utils.rand.nextFloat() > 0.5) {
//                Fight fight = new Fight(Player.getPlayer().getHeroes());
//                fight.commenceFight();
//            }
//        }
//    }
//
//    public boolean updateMapAfterMoveUp() {
//        if (getCurrentRow() == 0) {
//            System.out.println("Can't move player up any further, please provide a valid action.");
//            return false;
//        }
//        else if (getSpecificTile(getCurrentRow()-1, getCurrentCol()) == Tile.Inaccessible) {
//            System.out.println("Can't move up, tile is inaccessable, please choose a different path.");
//            return false;
//        }
//        else {
//            playerPlaced(getCurrentRow() - 1, getCurrentCol());
//            return true;
//        }
//    }
//
//    public boolean updateMapAfterMoveDown() {
//        if (getCurrentRow() == getRows() - 1) {
//            System.out.println("Can't move player down any further, please provide a valid action.");
//            return false;
//        }
//        else if (getSpecificTile(getCurrentRow()+1, getCurrentCol()) == Tile.Inaccessible) {
//            System.out.println("Can't move down, tile is inaccessable, please choose a different path.");
//            return false;
//        }
//        else {
//            playerPlaced(getCurrentRow() + 1, getCurrentCol());
//            return true;
//        }
//    }
//
//    public boolean updateMapAfterMoveLeft() {
//        if (getCurrentCol() == 0) {
//            System.out.println("Can't move player left any further, please provide a valid action.");
//            return false;
//        }
//        else if (map.getSpecificTile(getCurrentRow(), getCurrentCol()-1) == Tile.Inaccessible) {
//            System.out.println("Can't move left, tile is inaccessable, please choose a different path.");
//            return false;
//        }
//        else {
//            map.playerPlaced(getCurrentRow(), getCurrentCol() - 1);
//            return true;
//        }
//    }
//
//    public boolean updateMapAfterMoveRight() {
//        if (getCurrentCol() == getCols() - 1) {
//            System.out.println("Can't move player right any further, please provide a valid action.");
//            return false;
//        }
//        else if (getSpecificTile(getCurrentRow(), getCurrentCol() + 1) == Tile.Inaccessible) {
//            System.out.println("Can't move right, tile is inaccessable, please choose a different path.");
//            return false;
//        }
//        else {
//            playerPlaced(getCurrentRow(), getCurrentCol() + 1);
//            return true;
//        }
//    }
//

    private List<Iterator<String>> getLaneStrIters() {
        List<Iterator<String>> laneStrIters = new ArrayList<Iterator<String>>();

        for (Lane lane : lanes)
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
}

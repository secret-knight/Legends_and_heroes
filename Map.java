import java.util.*;

// represents the map of the game, singleton instance
public class Map {
    private static Map map;

    private Tile[][] tiles;
    private Tile currentTile;
    private int currentRow;
    private int currentCol;
    private final int rows = 8;
    private final int cols = 8;

    private Map() {
        createMap();
        while (!checkMapIsFullyAccessible()) {
            createMap();
        }
    }

    private void createMap() {
        int numTiles = rows * cols;
        List<Integer> placement = new ArrayList<Integer>(Collections.nCopies(Math.round((float) (numTiles * 0.5)), 0));
        placement.addAll(Collections.nCopies(Math.round((float) (numTiles * 0.3)), 1));
        placement.addAll(Collections.nCopies(Math.round((float) (numTiles * 0.2)), 2));
        Collections.shuffle(placement);
        tiles = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                switch (placement.remove(0)) {
                    case 0:
                        tiles[i][j] = Tile.Common;
                        break;
                    case 1:
                        tiles[i][j] = Tile.Market;
                        break;
                    default:
                        tiles[i][j] = Tile.Inaccessible;
                        break;
                }
            }
        }
        while (true) {
            if (playerPlaced(Utils.rand.nextInt(rows), Utils.rand.nextInt(cols))) { break; };
        }
    }

    public boolean playerPlaced(int i, int j) {
        Tile t = tiles[i][j];
        if (t != Tile.Inaccessible) {
            if (currentTile != null) {
                tiles[currentRow][currentCol] = currentTile;
            }
            currentTile = t;
            currentRow = i;
            currentCol = j;
            if (t == Tile.Market) {
                tiles[i][j] = Tile.OccupiedMarket;
            } else {
                tiles[i][j] = Tile.OccupiedCommon;
            }
            return true;
        }
        return false;
    }

    public boolean checkMapIsFullyAccessible() {
        int[][] connected = new int[rows][cols];
        connected[currentRow][currentCol] = 1;
        connected = checkNeighborsConnected(connected, currentRow, currentCol);
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (connected[i][j] == 1) {
                    count ++;
                }
            }
        }
        return count == Math.round((float) rows*cols*0.8);
    }

    private int[][] checkNeighborsConnected(int[][] conn, int i, int j) {
        List<Integer> rowsToCheck = new ArrayList<Integer>();
        List<Integer> colsToCheck = new ArrayList<Integer>();
        rowsToCheck.add(i-1);
        rowsToCheck.add(i+1);
        colsToCheck.add(j-1);
        colsToCheck.add(j+1);
        if (i == 0) {
            rowsToCheck.remove(0);
        }
        if (j == 0) {
            colsToCheck.remove(0);
        }
        if (i == rows - 1) {
            rowsToCheck.remove(1);
        }
        if (j == cols - 1) {
            colsToCheck.remove(1);
        }
        for (int r : rowsToCheck) {
            if (tiles[r][j] != Tile.Inaccessible && conn[r][j] != 1) {
                conn[r][j] = 1;
                conn = checkNeighborsConnected(conn, r, j);
            }
        }
        for (int c : colsToCheck) {
            if (tiles[i][c] != Tile.Inaccessible && conn[i][c] != 1) {
                conn[i][c] = 1;
                conn = checkNeighborsConnected(conn, i, c);
            }
        }

        return conn;
    }

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

    public Tile getSpecificTile(int i, int j) { return tiles[i][j];}

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
        boolean moved = false;
        boolean closed = false;

        while (!closed && !Player.getPlayer().isGameOver()) {
            System.out.println(this);
            String action = Utils.getValidInputString(new String[]{"e", "k", "w", "s", "a", "d", "i", "q"});
            switch (action) {
                case "e":
                    Inventory.enterInventoryScreen(null, true);
                    break;
                case "k":
                    if (getCurrentTile() != Tile.Market)
                        System.out.println("Can't enter market, you are not on a market tile.");
                    else {
                        Market market = new Market(Player.getPlayer().getMaxHeroLevel());
                        market.enterMarket();
                    }
                    break;
                case "w":
                    moved = updateMapAfterMoveUp();
                    closed = true;
                    break;
                case "s":
                    moved = updateMapAfterMoveDown();
                    closed = true;
                    break;
                case "a":
                    moved = updateMapAfterMoveLeft();
                    closed = true;
                    break;
                case "d":
                    moved = updateMapAfterMoveRight();
                    closed = true;
                    break;
                case "i":
                    Player.getPlayer().enterInfoScreen();
                    break;
                default:
                    System.out.println("Game ended");
                    Player.getPlayer().setGameOver(true);
                    break;
            }
        }
        if (moved && getCurrentTile() == Tile.Common) {
            if (Utils.rand.nextFloat() > 0.5) {
                Fight fight = new Fight(Player.getPlayer().getHeroes());
                fight.commenceFight();
            }
        }
    }

    public boolean updateMapAfterMoveUp() {
        if (getCurrentRow() == 0) {
            System.out.println("Can't move player up any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(getCurrentRow()-1, getCurrentCol()) == Tile.Inaccessible) {
            System.out.println("Can't move up, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            playerPlaced(getCurrentRow() - 1, getCurrentCol());
            return true;
        }
    }

    public boolean updateMapAfterMoveDown() {
        if (getCurrentRow() == getRows() - 1) {
            System.out.println("Can't move player down any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(getCurrentRow()+1, getCurrentCol()) == Tile.Inaccessible) {
            System.out.println("Can't move down, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            playerPlaced(getCurrentRow() + 1, getCurrentCol());
            return true;
        }
    }

    public boolean updateMapAfterMoveLeft() {
        if (getCurrentCol() == 0) {
            System.out.println("Can't move player left any further, please provide a valid action.");
            return false;
        }
        else if (map.getSpecificTile(getCurrentRow(), getCurrentCol()-1) == Tile.Inaccessible) {
            System.out.println("Can't move left, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            map.playerPlaced(getCurrentRow(), getCurrentCol() - 1);
            return true;
        }
    }

    public boolean updateMapAfterMoveRight() {
        if (getCurrentCol() == getCols() - 1) {
            System.out.println("Can't move player right any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(getCurrentRow(), getCurrentCol() + 1) == Tile.Inaccessible) {
            System.out.println("Can't move right, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            playerPlaced(getCurrentRow(), getCurrentCol() + 1);
            return true;
        }
    }

    @Override
    public String toString() {
        String msg1 = "";
        String msg2 = "";
        if (currentTile == Tile.Common) {
            msg1 = "         |          a common tile        |    |\n";
            msg2 = "         |                               |    |\n";
        } else {
            msg1 = "         |          a market tile        |    |\n";
            msg2 = "         |  Press k to enter the market  |    |\n";
        }
        String[] controls = new String[]{"                  Controls & Info             |\n",
                                         "         +-------------------------------+    |\n",
                                         "         |w = Move up   | a = Move left  |    |\n",
                                         "         |s = Move down | d = Move right |    |\n",
                                         "         |e = Inventory | i = Info       |    |\n",
                                         "         |q = Quit game |                |    |\n",
                                         "         +-------------------------------+    |\n",
                                         "         |     You are currently on a    |    |\n",
                                         msg1,
                                         msg2,
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

        for (int i = 0, k = 0; i < rows; i++, k += 2) {
            stringBuilder.append("|  ").append(getRowDivider()).append(controls[k]).append("|     ");
            for (int j = 0; j < cols; j++) {
                stringBuilder.append("|");
                Tile tile = tiles[i][j];
                stringBuilder.append(tile.toString());
            }
            stringBuilder.append("|").append(controls[k+1]);
        }
        stringBuilder.append("|  ").append(getRowDivider()).append("                                              |\n" +
                "|                                                                                                    |\n" +
                "+----------------------------------------------------------------------------------------------------+");

        return stringBuilder.toString();
    }
}

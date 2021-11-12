import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lane {
    private Tile[][] tiles;
    private Tile currentTile;
    private int currentRow;
    private int currentCol;
    private final int rows = 8;
    private final int cols = 2;

    public Lane() {
        List<Integer> placement = new ArrayList<Integer>(Collections.nCopies(3, 0));
        placement.addAll(Collections.nCopies(2, 1));
        placement.addAll(Collections.nCopies(2, 2));
        placement.addAll(Collections.nCopies(5, 3));
        Collections.shuffle(placement);
        placement.add(0, 4);
        placement.add(0, 4);
        placement.add(4);
        placement.add(4);
        tiles = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                switch (placement.remove(0)) {
                    case 0:
                        tiles[i][j] = new Bush();
                        break;
                    case 1:
                        tiles[i][j] = new Koulou();
                        break;
                    case 2:
                        tiles[i][j] = new Cave();
                        break;
                    case 3:
                        tiles[i][j] = new Plain();
                        break;
                    default:
                        tiles[i][j] = new Nexus();
                        break;
                }
            }
        }
    }

    public boolean playerPlaced(int i, int j) {
        Tile t = tiles[i][j];
        if (!(t instanceof Inaccessible)) {
            if (currentTile != null) {
                tiles[currentRow][currentCol] = currentTile;
            }
            currentTile = t;
            currentRow = i;
            currentCol = j;
//            if (t instanceof Nexus) {
//                tiles[i][j] = Tile.OccupiedMarket;
//            } else {
//                tiles[i][j] = Tile.OccupiedCommon;
//            }
            return true;
        }
        return false;
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

//    public static Map getMap() {
//        if (map == null) {
//            map = new Map();
//        }
//        return map;
//    }

    public void move() {
        boolean moved = false;
        boolean closed = false;

        while (!closed && !Player.getPlayer().isGameOver()) {
            System.out.println(this); // change to view.showMap();
            String action = Utils.getValidInputString(new String[]{"e", "k", "w", "s", "a", "d", "i", "q"});
            switch (action) {
                case "e":
                    Inventory.enterInventoryScreen(null, true);
                    break;
                case "k":
                    if (!(getCurrentTile() instanceof Nexus))
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
        if (moved && (getCurrentTile() instanceof Plain)) {
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
        else if (getSpecificTile(getCurrentRow()-1, getCurrentCol()) instanceof Inaccessible) {
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
        else if (getSpecificTile(getCurrentRow()+1, getCurrentCol()) instanceof Inaccessible) {
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
        else if (getSpecificTile(getCurrentRow(), getCurrentCol()-1) instanceof Inaccessible) {
            System.out.println("Can't move left, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            playerPlaced(getCurrentRow(), getCurrentCol() - 1);
            return true;
        }
    }

    public boolean updateMapAfterMoveRight() {
        if (getCurrentCol() == getCols() - 1) {
            System.out.println("Can't move player right any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(getCurrentRow(), getCurrentCol() + 1) instanceof Inaccessible) {
            System.out.println("Can't move right, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            playerPlaced(getCurrentRow(), getCurrentCol() + 1);
            return true;
        }
    }

    public List<String> getString() {
        List<String> rowsStr = new ArrayList<String>();
        for (int i = 0, k = 0; i < rows; i++, k += 2) {
            rowsStr.add(getRowDivider());
            String s = "";
            for (int j = 0; j < cols; j++) {
                s += "|";
                Tile tile = tiles[i][j];
                s += tile.toString();
            }
            rowsStr.add(s + "|");
        }
        rowsStr.add(getRowDivider());

        return rowsStr;
    }

    private String getRowDivider() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < cols; j++) {
            stringBuilder.append("+-------");
        }
        return stringBuilder.append("+").toString();
    }
}

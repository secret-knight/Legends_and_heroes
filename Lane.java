import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.HashMap;

public class Lane {
    private Tile[][] tiles;
    private HashMap<Hero, Coordinate>    heros;
    private HashMap<Monster, Coordinate> monsters;
    private final int rows = 8;
    private final int cols = 2;

    public Lane(Hero hero) {
        List<Integer> placement = new ArrayList<Integer>(Collections.nCopies(3, 0));
        this.heros = new HashMap<>();
        this.monsters = new HashMap<>();
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
        heroPlaced(rows - 1, 0, hero);
        this.addHero(rows - 1, 0, hero);
    }

    // change playerPlaced to heroPlaced
    public boolean heroPlaced(int row, int col, Hero hero)
    {
        Coordinate dest = new Coordinate(row, col);
        return placeHero(dest, dest, hero);
    }
    
    
    /**
     * move hero from original Tile to another one
     * 
     * @param from Coordinate of original
     * @param to   destination Tile
     * @param hero hero that moving
     * @return     whether is move applicable or not
     */
    public boolean placeHero(Coordinate from, Coordinate to, Hero hero) {
        Tile t = tiles[to.getRow()][to.getCol()];
        // check if we can move the character
        // - can not move to inaccessible tile
        // - can not move heros and monsters pass through each other
        // - can not move out lane
        if (!(t instanceof Inaccessible) && canMoveCharacter(to, hero)) {
            boolean isMovable = t.moveCharacterFrom(tiles[from.getRow()][from.getCol()], hero);
            if(isMovable)
            {
                getHeros().put(hero, to);
            }
            return isMovable;
        }
        return false;
    }
    
    /**
     * check if we can move the character to current tile
     * @param character
     * @return
     */
    private boolean canMoveCharacter(Coordinate to, Character character)
    {
        // TODO Auto-generated method stub
        return true;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Tile getSpecificTile(int i, int j) { return tiles[i][j];}

    public void move() {
        // iterate each hero, move each of them in current lane
        for(Entry<Hero, Coordinate> entry : heros.entrySet())
        {
            boolean    moved       = false;
            boolean    closed      = false;
            Hero       hero        = entry.getKey();
            Coordinate cord        = entry.getValue();
            Tile       currentTile = getSpecificTile(cord.getRow(), cord.getCol());
            while (!closed && !Player.getPlayer().isGameOver()) {
                System.out.println(Map.getMap());
                String action = Utils.getValidInputString(new String[]{"e", "k", "w", "s", "a", "d", "i", "q"});
                switch (action) {
                case "e":
                    Inventory.enterInventoryScreen(null, true);
                    break;
                case "k":
                    if (!(currentTile instanceof Nexus))
                        System.out.println("Can't enter market, you are not on a market tile.");
                    else {
                        Market market = new Market(Player.getPlayer().getMaxHeroLevel());
                        market.enterMarket();
                    }
                    break;
                case "w":
                    moved = updateMapAfterMoveUp(cord.getRow(), cord.getCol(), hero);
                    closed = true;
                    break;
                case "s":
                    moved = updateMapAfterMoveDown(cord.getRow(), cord.getCol(), hero);
                    closed = true;
                    break;
                case "a":
                    moved = updateMapAfterMoveLeft(cord.getRow(), cord.getCol(), hero);
                    closed = true;
                    break;
                case "d":
                    moved = updateMapAfterMoveRight(cord.getRow(), cord.getCol(), hero);
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
            if (moved) {
                // update currentTile
                cord = getHeros().get(hero); 
                currentTile = getSpecificTile(cord.getRow(), cord.getCol());
                // check if plain and need to fight monster
                if ((currentTile instanceof Plain) && Utils.rand.nextFloat() > 0.5) {
                    Fight fight = new Fight(Player.getPlayer().getHeroes());
                    fight.commenceFight();
                }
            }
        }
    }
    
    /**
     * move monster
     */
    public void moveMonster()
    {
        moveMonsterForward();
    }
    
    public void moveMonsterForward()
    {
        //TODO
    }
    
    public boolean updateMapAfterMoveUp(int row, int col, Hero hero) {
        if (row == 0) {
            System.out.println("Can't move player up any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row-1, col) instanceof Inaccessible) {
            System.out.println("Can't move up, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeHero(new Coordinate(row, col), new Coordinate(row - 1, col), hero);
        }
    }

    public boolean updateMapAfterMoveDown(int row, int col, Hero hero) {
        if (row == getRows() - 1) {
            System.out.println("Can't move player down any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row + 1, col) instanceof Inaccessible) {
            System.out.println("Can't move down, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeHero(new Coordinate(row, col), new Coordinate(row + 1, col), hero);
        }
    }

    public boolean updateMapAfterMoveLeft(int row, int col, Hero hero) {
        if (col == 0) {
            System.out.println("Can't move player left any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row, col-1) instanceof Inaccessible) {
            System.out.println("Can't move left, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeHero(new Coordinate(row, col), new Coordinate(row, col - 1), hero);
        }
    }

    public boolean updateMapAfterMoveRight(int row, int col, Hero hero) {
        if (col == getCols() - 1) {
            System.out.println("Can't move player right any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row, col + 1) instanceof Inaccessible) {
            System.out.println("Can't move right, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeHero(new Coordinate(row, col), new Coordinate(row, col + 1), hero);
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
    
    public void addHero(int row, int col, Hero hero)
    {
        this.heros.put(hero, new Coordinate(row, col));
    }

    public HashMap<Hero, Coordinate> getHeros()
    {
        return heros;
    }

    public HashMap<Monster, Coordinate> getMonsters()
    {
        return monsters;
    }

    public void setHeros(HashMap<Hero, Coordinate> heros)
    {
        this.heros = heros;
    }

    public void setMonsters(HashMap<Monster, Coordinate> monsters)
    {
        this.monsters = monsters;
    }
}

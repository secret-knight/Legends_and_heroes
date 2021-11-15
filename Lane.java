import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Comparator;

public class Lane {
    private Tile[][] tiles;
    private CharacterLocationManager<Hero>    herosLocationManager;
    private CharacterLocationManager<Monster> monstersLocationManager;
    private final int rows = 8;
    private final int cols = 2;

    public Lane(Hero hero) {
        List<Integer> placement = new ArrayList<Integer>(Collections.nCopies(3, 0));
        this.setHerosLocationManager(new CharacterLocationManager<Hero>(rows));
        this.setMonstersLocationManager(new CharacterLocationManager<Monster>(0));
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
        getHerosLocationManager().add(hero, new Coordinate(rows - 1, 0));
    }

    // change playerPlaced to heroPlaced
    public boolean heroPlaced(int row, int col, Hero hero)
    {
        Coordinate dest = new Coordinate(row, col);
        return placeCharacter(dest, dest, hero);
    }
    
    
    /**
     * move hero from original Tile to another one
     * 
     * @param from      Coordinate of original
     * @param to        destination Tile
     * @param character hero that moving
     * @return          whether is move applicable or not
     */
    public boolean placeCharacter(Coordinate from, Coordinate to, Character character) {
        Tile t = tiles[to.getRow()][to.getCol()];
        // check if we can move the character
        // - can not move to inaccessible tile
        // - can not move heros and monsters pass through each other
        // - can not move out lane
        if (!(t instanceof Inaccessible) && canMoveCharacter(character, to)) {
            boolean isMovable = t.moveCharacterFrom(tiles[from.getRow()][from.getCol()], character);
            if(isMovable)
            {
                if(character instanceof Hero)
                {
                    getHerosLocationManager().updateLocation((Hero)character, to);
                }
                else
                {
                    getMonstersLocationManager().updateLocation((Monster)character, to);
                }
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
    private boolean canMoveCharacter(Character character, Coordinate to)
    {
        // check if pass through front monster
        int heroFurthestRow    = getHerosLocationManager().getFrontCoordinate().getRow();
        int monsterFurthestRow = getMonstersLocationManager().getFrontCoordinate().getRow();
        if(heroFurthestRow +  monsterFurthestRow > (this.rows + 1))
        {
            return false;
        }
        else
        {
            return true;
        }
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
        for(Entry<Hero, Coordinate> entry : getHerosLocationManager().getEntrySet())
        {
            boolean    moved       = false;
            boolean    closed      = false;
            Hero       hero        = (Hero)entry.getKey();
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
                cord = getHerosLocationManager().getCharacterCoordinate(hero); 
                currentTile = getSpecificTile(cord.getRow(), cord.getCol());
                // check if plain and need to fight monster
//                if ((currentTile instanceof Plain) && Utils.rand.nextFloat() > 0.5) {
//                    Fight fight = new Fight(Player.getPlayer().getHeroes());
//                    fight.commenceFight();
//                }
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
    
    /**
     * move monster downwards 
     */
    public void moveMonsterForward()
    {
        Iterator<Monster> ite = getMonstersLocationManager().iterator();
        while(ite.hasNext())
        {
            Monster    monster = ite.next();
            Coordinate cord    = getMonstersLocationManager().getCharacterCoordinate(monster);
            updateMapAfterMoveDown(cord.getRow(), cord.getCol(), monster);
        }
    }
    
    public boolean updateMapAfterMoveUp(int row, int col, Character character) {
        if (row == 0) {
            System.out.println("Can't move player up any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row-1, col) instanceof Inaccessible) {
            System.out.println("Can't move up, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeCharacter(new Coordinate(row, col), new Coordinate(row - 1, col), character);
        }
    }

    public boolean updateMapAfterMoveDown(int row, int col, Character character) {
        if (row == getRows() - 1) {
            System.out.println("Can't move player down any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row + 1, col) instanceof Inaccessible) {
            System.out.println("Can't move down, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeCharacter(new Coordinate(row, col), new Coordinate(row + 1, col), character);
        }
    }

    public boolean updateMapAfterMoveLeft(int row, int col, Character character) {
        if (col == 0) {
            System.out.println("Can't move player left any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row, col-1) instanceof Inaccessible) {
            System.out.println("Can't move left, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeCharacter(new Coordinate(row, col), new Coordinate(row, col - 1), character);
        }
    }

    public boolean updateMapAfterMoveRight(int row, int col, Character character) {
        if (col == getCols() - 1) {
            System.out.println("Can't move player right any further, please provide a valid action.");
            return false;
        }
        else if (getSpecificTile(row, col + 1) instanceof Inaccessible) {
            System.out.println("Can't move right, tile is inaccessable, please choose a different path.");
            return false;
        }
        else {
            return placeCharacter(new Coordinate(row, col), new Coordinate(row, col + 1), character);
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

    public CharacterLocationManager<Hero> getHerosLocationManager()
    {
        return herosLocationManager;
    }

    public void setHerosLocationManager(CharacterLocationManager<Hero> herosLocationManager)
    {
        this.herosLocationManager = herosLocationManager;
    }

    public CharacterLocationManager<Monster> getMonstersLocationManager()
    {
        return monstersLocationManager;
    }

    public void setMonstersLocationManager(CharacterLocationManager<Monster> monstersLocationManager)
    {
        this.monstersLocationManager = monstersLocationManager;
    }
}

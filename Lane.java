import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Comparator;

public class Lane {
    private Tile[][] tiles;
    private CharacterLocationManager<Hero>    herosLocationManager;
    private CharacterLocationManager<Monster> monstersLocationManager;
    private final int rows = 8;
    private final int cols = 2;

    public Lane(Hero hero) {
        this.setHerosLocationManager(new CharacterLocationManager<Hero>(rows-1, 0));
        this.setMonstersLocationManager(new CharacterLocationManager<Monster>(0, 0));
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
        characterPlaced(rows - 1, 0, hero);
        hero.setOrgLane(this);
        
        addNewMonster(hero.getLevel());
        
    }

    // change playerPlaced to heroPlaced
    public boolean characterPlaced(int row, int col, Character character)
    {
        Coordinate dest = new Coordinate(row, col);
        return placeCharacter(dest, dest, character);
    }


    public void addNewMonster(int level) {
        // copied from Fight.java
        Monster monster = (Monster) Utils.monsterFactory.createCharacter(level);
        characterPlaced(0, 0, monster);
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

    public boolean checkForWin() {

        if (getHerosLocationManager().getFurthermostDistance() == rows - 1) {
            System.out.println("The heroes have won!");
            System.out.println(AsciiArt.YOUWON);
            return true;
        }

        if (getMonstersLocationManager().getFurthermostDistance() == rows - 1) {
            System.out.println("The monsters have won!");
            System.out.println(AsciiArt.GAMEOVER);
            return true;
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
        int heroRow    = 0;
        int monsterRow = 0;
        if (character instanceof Hero) {
            heroRow    = getHerosLocationManager().distanceToOrigin(to);
            monsterRow = getMonstersLocationManager().getFurthermostDistance();
        } else {
            heroRow    = getHerosLocationManager().getFurthermostDistance();
            monsterRow = getMonstersLocationManager().distanceToOrigin(to);
        }
        // check if pass through front monster
        if(heroRow +  monsterRow > (this.rows - 1))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean canRecallCharacter(Character character)
    {
        // check whether nexus tiles are occupied by other hero
        for(Coordinate cord : getNexusCoordiantes())
        {
            if(this.getSpecificTile(cord).canAddCharacter(character))
            {
                return true;
            }
        }
        return false;
    }

    private boolean fight(Hero hero) {

        Coordinate closestMonsterCoord = getMonstersLocationManager().getFrontCoordinate();

        Coordinate currentHeroCoord = getHerosLocationManager().getCharacterCoordinate(hero);

        if (closestMonsterCoord == null || currentHeroCoord.getRow() - closestMonsterCoord.getRow() > 1) {
            System.out.println("Can't fight with current hero, no monster is close enough.");
            return false;
        }
        else {
            Monster monster = getMonstersLocationManager().getFrontCharacter();
            if (hero.act(monster)) {
                if (monster.getHp() <= 0) {
                    System.out.println(monster.getName() + " fainted!");
                    //TODO REWARD HERO HERE
                    getMonstersLocationManager().remove(monster);
                    Tile t = tiles[closestMonsterCoord.getRow()][closestMonsterCoord.getCol()];
                    t.removeCharacter(monster);
                }
                return true;
            }
            else
                return false;
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Tile[][] getTiles()
    {
        return tiles;
    }

    public void setTiles(Tile[][] tiles)
    {
        this.tiles = tiles;
    }

    public Tile getSpecificTile(int i, int j) { return tiles[i][j];}

    public Tile getSpecificTile(Coordinate cord) { return this.getSpecificTile(cord.getRow(), cord.getCol()); };
    
    public void move() {
        // iterate each hero, move each of them in current lane
        List<Entry<Hero, Coordinate>> herosEntries = new LinkedList<>(getHerosLocationManager().getEntrySet());
        for(Entry<Hero, Coordinate> entry : herosEntries)
        {
            boolean    closed      = false;
            Hero       hero        = entry.getKey();
            Coordinate cord        = entry.getValue();
            Tile       currentTile = getSpecificTile(cord.getRow(), cord.getCol());
            boolean    onNexus     = currentTile instanceof Nexus;
            if(Map.getMap().getActedHero().contains(hero)) 
            {
                continue;
            }
            else
            {
                Map.getMap().getActedHero().add(hero);
            }
            while (!closed && !Player.getPlayer().isGameOver()) {
                System.out.println(Map.getMap().getMapString(hero, onNexus));
                String action = Utils.getValidInputString(new String[]{"w", "s", "a", "d", "e", "i", "t", "b", "f", "q", "k"});
                switch (action) {
                case "w":
                    closed = updateMapAfterMoveUp(cord.getRow(), cord.getCol(), hero);
                    break;
                case "s":
                    closed = updateMapAfterMoveDown(cord.getRow(), cord.getCol(), hero);
                    break;
                case "a":
                    closed = updateMapAfterMoveLeft(cord.getRow(), cord.getCol(), hero);
                    break;
                case "d":
                    closed = updateMapAfterMoveRight(cord.getRow(), cord.getCol(), hero);
                    break;
                case "e":
                    Inventory.enterInventoryScreen(null, true);
                    break;
                case "i":
                    Player.getPlayer().enterInfoScreen();
                    break;
                case "t":
                    closed = teleport(hero, getHerosLocationManager().getCharacterCoordinate(hero));
                    break;
                case "b":
                    closed = sendHeroBackToOrigin(hero);
                    break;
                case "f":
                    closed = fight(hero);
                    break;
                case "q":
                    System.out.println("Game ended");
                    Player.getPlayer().setGameOver(true);
                    System.exit(0);// exit the game program
                    break;
                case "k":
                    if (!onNexus)
                        System.out.println("Can't enter market, you are not on a market tile.");
                    else {
                        Market market = new Market(Player.getPlayer().getMaxHeroLevel());
                        market.enterMarket();
                    }
                    break;
                default:
                    break;
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
            if (!updateMapAfterMoveDown(cord.getRow(), cord.getCol(), monster)) {
                Hero hero = getHerosLocationManager().getFrontCharacter();
                monster.act(hero);
                if (hero.getHp() <= 0) {
                    System.out.println(hero.getName() + " fainted!");
                    sendHeroBackToOrigin(hero);
                    hero.fullyRevive();
                }
            }
        }
    }

    private boolean sendHeroBackToOrigin(Hero hero) {
        Lane orgLane  = hero.getOrgLane();
        if (orgLane.canRecallCharacter(hero))
        {
            Map.getMap().recall(hero, this);
            return true;
        }
        else
        {
            System.out.println("Both origin spots are occupied by other heroes, can't go back to Nexus right now.");
            return false;
        }
//        Coordinate heroCoord = getHerosLocationManager().getCharacterCoordinate(hero);
//        Coordinate heroOrigin = getOrgCord(hero);
//        Tile to = tiles[heroOrigin.getRow()][heroOrigin.getCol()];
//        Tile from = tiles[heroCoord.getRow()][heroCoord.getCol()];
//        if (to.moveCharacterFrom(from, hero)) {
//            getHerosLocationManager().updateLocation(hero, heroOrigin);
//            return true;
//        } else {
//            // check if other origin spot is open
//            to = tiles[heroOrigin.getRow()][heroOrigin.getCol() + 1];
//            if (to.moveCharacterFrom(from, hero)) {
//                getHerosLocationManager().updateLocation(hero, new Coordinate(heroOrigin.getRow(), heroOrigin.getCol()+1));
//                return true;
//            } else {
//                System.out.println("Both origin spots are occupied by other heroes, can't go back to Nexus right now.");
//                return false;
//            }
//        }
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
    
    public boolean teleport(Character character, Coordinate from)
    {
        return Map.getMap().teleportToOtherLane(character, from, this);
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
            stringBuilder.append("+---------");
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
    
    public Coordinate getCharacterLocation(Character character)
    {
        if(character instanceof Hero)
        {
            return this.getHerosLocationManager().getCharacterCoordinate((Hero)character);
        }
        else 
        {
            return this.getMonstersLocationManager().getCharacterCoordinate((Monster)character);
        }
    }

    public Coordinate getOrgCord(Character character)
    {
        if(character instanceof Hero)
        {
            return this.getHerosLocationManager().getOriginCoordinate();
        }
        else
        {
            return this.getMonstersLocationManager().getOriginCoordinate();
        }
    }
    
    public List<Coordinate> getNexusCoordiantes()
    {
        List<Coordinate> res = new ArrayList<>();
        res.add(new Coordinate(this.getRows() - 1, 0));
        res.add(new Coordinate(this.getRows() - 1, 1));
        return res;
    }
    
    public boolean equals(Object another)
    {
        if(another instanceof Lane)
        {
            return this.getTiles().equals(((Lane)another).getTiles());
        }
        return false;
    }
}

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
    private              Set<Hero>                actedHero           = new HashSet<Hero>();
    private Map() {
        laneCollection      = new LaneCollection();
        recallingCharacters = new LinkedHashMap<Character, Lane>();
        for (int i = 0; i < NUMOFLANE; i++)
            laneCollection.add(new Lane(Player.getPlayer().getHeroes().get(i)));
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
            if(lane.canRecallCharacter(character)) 
            {
                for(Coordinate cord : lane.getNexusCoordiantes())
                {
                    if(lane.getSpecificTile(cord).canAddCharacter(character))
                    {
                        teleport(character, 
                                lane.getCharacterLocation(character), 
                                lane, 
                                cord, 
                                character.getOrgLane());
                        break;
                    }
                }
            }
        }
        
        // regain each character
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            for(Hero hero : this.laneCollection.getNext().getHerosLocationManager().getCharacters())
            {
                hero.regainAfterRound();
            }
        }
        
        getRecallingCharacters().clear();
        getActedHero().clear();
    }

    public void checkForWin() {
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            if(this.laneCollection.getNext().checkForWin()) {
                Player.getPlayer().setGameOver(true);
                System.out.println(Map.getMap().getMapString(Player.getPlayer().getHeroes().get(0), false));
            }
        }
    }

    public void createNewMonsters() {
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            this.laneCollection.getNext().addNewMonster(Player.getPlayer().getMaxHeroLevel());

        }
    }

    private List<Iterator<String>> getLaneStrIters() {
        List<Iterator<String>> laneStrIters = new ArrayList<Iterator<String>>();

        for (Lane lane : laneCollection.getLaneList())
            laneStrIters.add(lane.getString().iterator());

        return laneStrIters;
    }

    public String getMapString(Hero hero, boolean onNexus) {

        ArrayList<String> controls = new ArrayList<String>(Arrays.asList("|w = Move up   | a = Move left  |",
                                         "|s = Move down | d = Move right |",
                                         "|e = Inventory | i = Info       |",
                                         "|t = Teleport  | b = back       |",
                                         "|f = fight     | q = Quit game  |"));

        if (onNexus)
            controls.add("|On Nexus, hit k to Enter Market|");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+--------------------------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("MAP", 116) + "|\n" +
                Utils.getHeroAndControlsString(hero, controls, 116));

        List<Iterator<String>> lanesStrIter = getLaneStrIters();
        int count = 0;

        while (lanesStrIter.get(0).hasNext()) {
            String line = "";
            for (int i = 0; i < lanesStrIter.size(); i++) {
                line += lanesStrIter.get(i).next();
                if (i != lanesStrIter.size() - 1)
                    if (count % 2 == 0)
                        line += "---------";
                    else
                        line += new Inaccessible();
            }
            count ++;
            stringBuilder.append("|" + Utils.getStringWithNumChar(line, line.length()+35) + "|\n");
        }
        stringBuilder.append("|" + Utils.getStringWithNumChar("", 116) + "|\n" +
                "+--------------------------------------------------------------------------------------------------------------------+");

        return stringBuilder.toString();
    }

    public void recall(Character character, Lane currentLane)
    {
        this.recallingCharacters.put(character, currentLane);
    }
    
    public boolean teleportToOtherLane(Character character, Coordinate org, Lane orgLane)
    {
        boolean    closed      = false;
        Lane chosenLane        = null;
        List<Lane> possibleLanes = new ArrayList<Lane>();
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            Lane lane = this.laneCollection.getNext();
            if (lane != orgLane) {
                possibleLanes.add(lane);
            }
        }
        while (!closed) {
            System.out.println(getTeleportOptions((Hero) character, orgLane));
            String action = Utils.getValidInputString(new String[]{"1", "2", "c"});
            switch (action) {
                case "1":
                    chosenLane = possibleLanes.get(0);
                    closed = teleport(character, org, orgLane, chosenLane.getHerosLocationManager().getTeleportCoordinate(), chosenLane);
                    break;
                case "2":
                    chosenLane = possibleLanes.get(1);
                    closed = teleport(character, org, orgLane, chosenLane.getHerosLocationManager().getTeleportCoordinate(), chosenLane);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private String getTeleportOptions(Hero hero, Lane orgLane) {
        List<Iterator<String>> laneStrIters = new ArrayList<Iterator<String>>();
        for(int i = 0; i < this.laneCollection.size(); i++)
        {
            Lane lane = this.laneCollection.getNext();
            if (lane != orgLane) {
                laneStrIters.add(lane.getString().iterator());
            }
        }



        Iterator<String> strHero = hero.getVerticalString().iterator();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+--------------------------------------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Teleport", 128) + "|\n" +
                "|" + Utils.getStringWithNumChar("(Press 1 or 2 to pick a lane or c to close the screen)", 128) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 128) + "|\n");

        String line = Utils.getStringWithNumChar("Option 1", 25) + "         " +
                Utils.getStringWithNumChar("Option 2", 25) + "         " +
                Utils.getStringWithNumChar("Current Hero (H" + (Player.getPlayer().getHeroes().indexOf(hero) + 1) + ")", 25);

        stringBuilder.append("|" + Utils.getStringWithNumChar(line, line.length()+35) + "|\n");
        while (laneStrIters.get(0).hasNext()) {
            line = "";
            for (int i = 0; i < laneStrIters.size(); i++) {
                line += laneStrIters.get(i).next() + "             ";
            }

            if (strHero.hasNext())
                line += Utils.getStringWithNumChar(strHero.next(), 25);
            else
                line += Utils.getStringWithNumChar("", 25);

            stringBuilder.append("|" + Utils.getStringWithNumChar(line, line.length()+35) + "|\n");
        }

        stringBuilder.append("|" + Utils.getStringWithNumChar("", 128) + "|\n" +
                "+--------------------------------------------------------------------------------------------------------------------------------+");

        return stringBuilder.toString();
    }
    
    public boolean teleport(Character character, Coordinate org, Lane orgLane, Coordinate dest, Lane destLane)
    {
        if (dest == null) {
            System.out.println("Can't teleport, no open spots in selected lane.");
            return false;
        } else if (destLane.placeCharacter(org, dest, character)) 
        {
            // remove org lane character only if teleport to other lane
            if(!orgLane.equals(destLane)) 
            {
                orgLane.getSpecificTile(org.getRow(), org.getCol()).removeCharacter(character);
                orgLane.getHerosLocationManager().remove((Hero) character);
            }
            return true;
        }
        else
            return false;
    }

    public HashMap<Character, Lane> getRecallingCharacters()
    {
        return recallingCharacters;
    }

    public Set<Hero> getActedHero()
    {
        return actedHero;
    }

    public void setActedHero(Set<Hero> actedHero)
    {
        this.actedHero = actedHero;
    }
}

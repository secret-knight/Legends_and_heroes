import java.util.*;
import java.util.Map.Entry;

// represents the map of the game, singleton instance
public class LOVmap extends AbsMap{
    private static       LOVmap                   map;
    private              int                      currentRow;
    private              int                      currentCol;
    private static final int                      ROWNUM      = 8;
    private static final int                      COLNUM      = 8;
    private static final int                      LANENUM     = 3;
    private LOVmap(int rowNum, int colNum, int laneNum) {
        super(rowNum, colNum, laneNum);
        setRecallingCharacters(new LinkedHashMap<Character, LOVLane>());
        for (int i = 0; i < laneNum; i++)
            getLaneCollection().add(new LOVLane(Player.getPlayer().getHeroes().get(i)));
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

    public static LOVmap getMap(int rowNum, int colNum, int laneNum) {
        if (map == null) {
            map = new LOVmap(rowNum, colNum, laneNum);
        }
        return map;
    }
    
    public static LOVmap getMap() 
    {
        if (map == null) {
            map = new LOVmap(ROWNUM, COLNUM, LANENUM);
        }
        return map;
    }

    private String getRowDivider() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("   ");
        for (int j = 0; j < getColNum(); j++) {
            stringBuilder.append("+-----");
        }
        return stringBuilder.append("+").toString();
    }

    public void move() {
        // since laneCollection.getNext() is infinite rotate, 
        // e.g: 0 -> 1 -> 2 -> 3 -> 0 -> 1
        // we need to count from 0 to collection.size();

        // hero move, attack a monster
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            getLaneCollection().getNext().move();
        }
        
        // monster turn
        // at every round each monster either attack a hero
        // or 
        // move one cell "forward"
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            getLaneCollection().getNext().moveMonster();
        }
        
        // move characters that recalling to base
        for(Entry<Character, LOVLane> recallEntry : getRecallingCharacters().entrySet())
        {
            Character character = recallEntry.getKey();
            LOVLane      lane      = recallEntry.getValue();
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
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            for(Hero hero : getLaneCollection().getNext().getHerosLocationManager().getCharacters())
            {
                hero.regainAfterRound();
            }
        }
        
        getRecallingCharacters().clear();
        getActedHero().clear();
    }

    public void checkForWin() {
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            if(getLaneCollection().getNext().checkForWin()) {
                Player.getPlayer().setGameOver(true);
                System.out.println(getMapString(Player.getPlayer().getHeroes().get(0), false));
            }
        }
    }

    public void createNewMonsters() {
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            getLaneCollection().getNext().addNewMonster(Player.getPlayer().getMaxHeroLevel());

        }
    }

    private List<Iterator<String>> getLaneStrIters() {
        List<Iterator<String>> laneStrIters = new ArrayList<Iterator<String>>();

        for (LOVLane lane : getLaneCollection().getLaneList())
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

    public void recall(Character character, LOVLane currentLane)
    {
        getRecallingCharacters().put(character, currentLane);
    }
    
    public boolean teleportToOtherLane(Character character, Coordinate org, LOVLane orgLane)
    {
        boolean    closed      = false;
        LOVLane chosenLane        = null;
        List<LOVLane> possibleLanes = new ArrayList<LOVLane>();
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            LOVLane lane = getLaneCollection().getNext();
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

    private String getTeleportOptions(Hero hero, LOVLane orgLane) {
        List<Iterator<String>> laneStrIters = new ArrayList<Iterator<String>>();
        for(int i = 0; i < getLaneCollection().size(); i++)
        {
            LOVLane lane = getLaneCollection().getNext();
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
    
    public boolean teleport(Character character, Coordinate org, LOVLane orgLane, Coordinate dest, LOVLane destLane)
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
}

import java.util.*;

// class for the player of the game
public class Player {
    private static Player player;
    private List<Hero> heroes = new ArrayList<Hero>();
    private boolean gameOver = false;

    // create the player of the game by selecting heroes
    private Player() {
        boolean embark = false;
        // get heroes to select from
        HeroSelection hs = HeroSelection.getHeroSelection();

        while (!embark && !gameOver) {
            // print selection screen
            System.out.println(hs);

            // get action from user, update player's heroes appropriately and/or the selection screen
            String action = Utils.getValidInputString(new String[]{"e", "r", "w", "s", "a", "d", "c", "q"});
            switch (action) {
                case "e":
                    if (heroes.size() == 3)
                        System.out.println("Can't add another hero, you already have selected 3.\n" +
                                "Either embark now or remove one of your party's heroes to add the desired hero.");
                    else if (heroes.contains(hs.getCurrentHero()))
                        System.out.println("Can't add a hero that is already in the party.");
                    else
                        heroes.add(hs.addCurrentHero());
                    break;
                case "r":
                    if (heroes.size() == 0)
                        System.out.println("Can't remove a hero at all, none are added to your party yet.");
                    else if (!heroes.remove(hs.removeCurrentHero()))
                        System.out.println("Can't remove a hero that isn't in your party.");
                    else
                        System.out.println("Hero removed.");
                    break;
                case "w":
                    if (hs.getCurrentlySelected() < 3)
                        System.out.println("Can't move up in menu, please provide another action that is valid.");
                    else
                        hs.setCurrentlySelected(hs.getCurrentlySelected()-3);
                    break;
                case "s":
                    if (hs.getCurrentlySelected() > 14)
                        System.out.println("Can't move down in menu, please provide another action that is valid.");
                    else
                        hs.setCurrentlySelected(hs.getCurrentlySelected()+3);
                    break;
                case "a":
                    if (hs.getCurrentlySelected() % 3 == 0)
                        System.out.println("Can't move left in menu, please provide another action that is valid.");
                    else
                        hs.setCurrentlySelected(hs.getCurrentlySelected()-1);
                    break;
                case "d":
                    if (hs.getCurrentlySelected() % 3 == 2)
                        System.out.println("Can't move right in menu, please provide another action that is valid.");
                    else
                        hs.setCurrentlySelected(hs.getCurrentlySelected()+1);
                    break;
                case "c":
                    if (heroes.size() != 3)
                        System.out.println("Can't embark yet, must have 3 heroes.");
                    else
                        embark = true;
                    break;
                default:
                    System.out.println("Game ended");
                    setGameOver(true);
                    break;
            }
        }
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    // enter the player's info screen to see info on each of the heroes in the party
    public void enterInfoScreen() {
        System.out.println(getString());
        String action = Utils.getValidInputString(new String[]{"c", "q"});

        if (action == "q") {
            System.out.println("Game ended");
            setGameOver(true);
        }
    }


    public int getMaxHeroLevel() {

        Hero maxHero = Collections.max(heroes, Hero.heroLevelComparator);

        return maxHero.getLevel();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public static Player getPlayer() {
        if (player == null) {
            player = new Player();
        }
        return player;
    }

    // useful for creating screen such that they are dynamic
    // iterators are used to grow the menus as needed
    private List<Iterator<String>> getHeroesStringIterators() {

        List<Iterator<String>> its = new ArrayList<Iterator<String>>();

        for (Hero h : heroes)
            its.add(h.getVerticalString().iterator());

        return its;
    }

    // create the info screen string
    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Info (press c to close)", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n");

        List<Iterator<String>> its = getHeroesStringIterators();

        // loop through the list of iterators, each an iterator for a hero of the party's string representation
        while (its.get(0).hasNext()) {
            String line = "|";
            for (Iterator<String> it : its) {
                line += Utils.getStringWithNumChar(it.next(), 30) + "|";

            }
            stringBuilder.append("|" + Utils.getStringWithNumChar(line, 100) + "|\n");
        }

        stringBuilder.append("|                                                                                                    |\n" +
                "+----------------------------------------------------------------------------------------------------+");

        return stringBuilder.toString();
    }
}

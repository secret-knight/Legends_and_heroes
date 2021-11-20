import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// class that facilitates hero creation and selection by a player
public class HeroSelection {
    private static HeroSelection heroSelection;
    private List<Hero> heroes = new ArrayList<Hero>();
    private List<Hero> addedHeroes = new ArrayList<Hero>();;
    private int currentlySelected = 0;

    private HeroSelection() {

        String[] warriorNames = new String[]{"Gaerdal Ironhand", "Sehanine Monnbow", "Muamman Duathall",
                "Flandal Steelskin", "Undefeated Yoj", "Eunoia Cyn"};
        String[] sorcererNames = new String[]{"Rillifane Rallathil", "Segojan Earthcaller", "Reign Havoc",
                "Reverie Ashels", "Kalabar", "Skye Soar"};
        String[] paladinNames = new String[]{"Parzival", "Drako Delirious", "Skoraeus Stonebones",
                "Garl Glittergold ", "Amaryllis Astra", "Caliber Heist"};

        // create heroes to pick from using factories
        for (int i = 0; i < 6; i++) {
            heroes.add((Hero) Utils.heroFactory.createCharacter(0, warriorNames[i], i+1));
            heroes.add((Hero) Utils.heroFactory.createCharacter(1, sorcererNames[i], i+1));
            heroes.add((Hero) Utils.heroFactory.createCharacter(2, paladinNames[i], i+1));
        }
    }

    public static HeroSelection getHeroSelection() {
        if (heroSelection == null) {
            heroSelection = new HeroSelection();
        }
        return heroSelection;
    }

    public int getCurrentlySelected() {
        return currentlySelected;
    }

    public void setCurrentlySelected(int currentlySelected) {
        this.currentlySelected = currentlySelected;
    }

    public Hero getCurrentHero() {
        return heroes.get(currentlySelected);
    }

    public Hero addCurrentHero() {
        addedHeroes.add(getCurrentHero());
        return getCurrentHero();
    }

    public Hero removeCurrentHero() {
        addedHeroes.remove(getCurrentHero());

        return getCurrentHero();
    }

    private boolean isHeroAdded(Hero h) {
        return addedHeroes.contains(h);
    }

    // print hero selection screen which shows current heroes to pick from, the currently toggled hero, and heroes added
    // to the party
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> controls = Arrays.asList(new String[]{"|w = Move up   | a = Move left  |", "|s = Move down | d = Move right |",
                "|e = Add hero  | r = Remove hero|", "|c = Embark!   | q = Quit game  |"});

        // print top half of menu with directions, controls to choose from, and the current toggled hero's info
        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|                                       Character Selection                                          |\n" +
                "|   +-------------------------------------------------------------------------------------------+    |\n" +
                "|   | To select a hero, move cursor to the appropriate hero and press 'e' to add them to party  |    |\n" +
                "|   |" + Utils.getStringWithNumChar("", 91) + "|    |\n" +
                "|   | Added heroes have 'P' next to their name, press 'r' to remove them when selected          |    |\n" +
                "|   |" + Utils.getStringWithNumChar("", 91) + "|    |\n" +
                "|   | Select 3 heroes, press 'q' to quit at any point to exit the game                          |    |\n" +
                "|   +-------------------------------------------------------------------------------------------+    |\n" +
                Utils.getHeroAndControlsString(getCurrentHero(), controls, 100) +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                "|               Warriors                       Sorcerers                        Paladins             |\n");

        // print lists of heroes
        for (int i = 0; i < heroes.size(); i += 3) {
            stringBuilder.append("|       +---------------------+         +---------------------+         +---------------------+      |\n");
            stringBuilder.append("|      " + Utils.getMenuString(Utils.getStringWithNumChar(heroes.get(i).getName(), 21),
                    currentlySelected == i, isHeroAdded(heroes.get(i)), "P") + "        " +
                        Utils.getMenuString(Utils.getStringWithNumChar(heroes.get(i+1).getName(), 21),
                                currentlySelected == i + 1, isHeroAdded(heroes.get(i+1)), "P") + "        " +
                        Utils.getMenuString(Utils.getStringWithNumChar(heroes.get(i+2).getName(), 21),
                                currentlySelected == i + 2, isHeroAdded(heroes.get(i+2)), "P") + "      |\n");
        }

        stringBuilder.append("|       +---------------------+         +---------------------+         +---------------------+      |\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                "+----------------------------------------------------------------------------------------------------+");

        return stringBuilder.toString();
    }
}

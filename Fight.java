import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// class that conducts a fight between a set of heroes and monsters
public class Fight {

    private List<Hero> heroes;
    private List<Monster> monsters;
    private boolean playersWon;
    private List<String> log;

    public Fight(List<Hero> heroes) {
        this.heroes = heroes;
        playersWon = false;
        log = new ArrayList<String>(Arrays.asList(new String[]{"|"+Utils.getStringWithNumChar("",70)+"|",
                "|"+Utils.getStringWithNumChar("",70)+"|", "|"+Utils.getStringWithNumChar("",70)+"|",
                "|"+Utils.getStringWithNumChar("",70)+"|", "|"+Utils.getStringWithNumChar("",70)+"|",
                "|"+Utils.getStringWithNumChar("",70)+"|",}));
        MonsterFactory monsterFactory = new MonsterFactory();
        Iterator<Hero> it = heroes.iterator();
        monsters = new ArrayList<Monster>();
        while (it.hasNext())
            monsters.add((Monster) monsterFactory.createCharacter(it.next().getLevel()));
    }

    public void commenceFight() {

        while (!isFightOver() && !Player.getPlayer().isGameOver()) {
            Iterator<Hero> heroIter = heroes.iterator();
            Iterator<Monster> monsterIter = monsters.iterator();
            List<Hero> leftoverHeroes = new ArrayList<Hero>();
            List<Monster> leftoverMonsters = new ArrayList<Monster>();
            while (heroIter.hasNext() || monsterIter.hasNext()) {
                Hero curHero = heroIter.next();
                Monster curMonster = monsterIter.next();
                if (curHero.getHp() > 0 && curMonster.getHp() > 0)
                    fight(curHero, curMonster, true, true);
                else {
                    if (curHero.getHp() > 0)
                        leftoverHeroes.add(curHero);
                    if (curMonster.getHp() > 0)
                        leftoverMonsters.add(curMonster);
                }
            }
            Iterator<Hero> leftoverHeroIter = leftoverHeroes.iterator();
            while (leftoverHeroIter.hasNext()) {
                Monster m = (Monster) getFirstAlive(monsters);
                if (m != null)
                    fight(leftoverHeroIter.next(), m, true, false);
            }
            Iterator<Monster> leftoverMonsterIter = leftoverMonsters.iterator();
            while (leftoverMonsterIter.hasNext()) {
                Hero h = (Hero) getFirstAlive(heroes);
                if (h != null)
                    fight(h, leftoverMonsterIter.next(),false, true);
            }
        }
        if (playersWon)
            rewardHeroes();
        else {
            if (isFightOver()) {
                System.out.println("\n\n\n\n" + Utils.getStringWithNumChar("All heroes fainted, you lost the game.", 102) + "\n\n\n\n");
                Player.getPlayer().setGameOver(true);
            } else
                System.out.println("\n\n\n\n" + Utils.getStringWithNumChar("You quit the game.", 102) + "\n\n\n\n");
        }
    }

    private Character getFirstAlive(List<? extends Character> characters) {
        for (Character c : characters) {
            if (c.getHp() > 0)
                return c;
        }

        return null;
    }

    private void fight(Hero h, Monster m, boolean heroFights, boolean monsterFights) {

        int selectedAction = 0;
        int selectedRow = 0;
        boolean closed = false;

        if (heroFights) {
            while (!closed && !Player.getPlayer().isGameOver()) {
                fightScreen(h, m, selectedAction, selectedRow);
                String action = Utils.getValidInputString(new String[]{"w", "s", "a", "d", "e", "i", "q"});
                switch (action) {
                    case "e":
                        if (selectedAction == 1 && h.getInventory().getSpells().size() == 0)
                            System.out.println("Can't cast spell, no spells in inventory.");
                        else if (selectedAction == 2 && h.getInventory().getPotions().size() == 0)
                            System.out.println("Can't cast spell, no spells in inventory.");
                        else if (performAction(h, m, selectedAction, getSelectedItem(h, selectedAction, selectedRow)))
                            closed = true;
                        break;
                    case "i":
                        Player.getPlayer().enterInfoScreen();
                        break;
                    case "w":
                        if (selectedRow == 0)
                            System.out.println("Can't move up from the current spot.");
                        else
                            selectedRow--;
                        break;
                    case "s":
                        if (selectedAction == 0 || selectedAction == 3 ||
                                (selectedAction == 1 && h.getInventory().getSpells().size() == 0) ||
                                (selectedAction == 2 && h.getInventory().getPotions().size() == 0) ||
                                (selectedAction == 1 && selectedRow == h.getInventory().getSpells().size() - 1) ||
                                (selectedAction == 2 && selectedRow == h.getInventory().getPotions().size() - 1))
                            System.out.println("Can't move down from the current spot.");
                        else
                            selectedRow++;
                        break;
                    case "a":
                        if (selectedAction == 0)
                            System.out.println("Can't move left from the current spot.");
                        else {
                            selectedAction -= 1;
                            selectedRow = 0;
                        }
                        break;
                    case "d":
                        if (selectedAction == 3)
                            System.out.println("Can't move right from the current spot.");
                        else {
                            selectedAction += 1;
                            selectedRow = 0;
                        }
                        break;
                    default:
                        System.out.println("Game ended");
                        Player.getPlayer().setGameOver(true);
                        break;
                }
            }
        }

        if (m.getHp() > 0) {
            if (monsterFights) {
                m.attack(h, this);
                if (h.getHp() <= 0)
                    updateLog(h.getName() + " fainted!");
            }
        } else {
            updateLog(m.getName() + " fainted!");
        }

        if (h.getHp() > 0 && heroFights)
            h.regainAfterRound();
    }

    private Item getSelectedItem(Hero h, int action, int row) {
        if (action == 1)
            return h.getInventory().getSpells().get(row);
        else if (action == 2)
            return h.getInventory().getPotions().get(row);
        else
            return null;
    }

    private boolean performAction(Hero h, Monster m, int action, Item item) {
        switch (action) {
            case 0:
                h.attack(m, this);
                return true;
            case 1:
                return h.castItem(item, m, this);
            case 2:
                h.useItem(item, this);
                return true;
            default:
                return h.changeEquipment(this);
        }
    }

    private void fightScreen(Hero h, Monster m, int action, int row) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> controls = Arrays.asList(new String[]{"| w = Move up  | s = Move down  |", "| a = Move left| d = Move right |",
                "| i = Info     | q = Quit game  |", "|      e = Perform action       |"});
        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Fight!", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n" + Utils.getHeroAndControlsString(h, controls) +
                "|" + Utils.getStringWithNumChar("", 25) + Utils.getStringWithNumChar("Fight Log", 75) + "|\n");

        stringBuilder.append(Utils.getMonsterAndFightLogString(m, log) +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n");

        List<Iterator<String>> its;
        if (action == 1)
            its = Arrays.asList(h.getArmorStrIter(), h.getInventory().getSpellStrIter(row), h.getWeaponStrIter());
        else if (action == 2)
            its = Arrays.asList(h.getArmorStrIter(), h.getInventory().getPotionStrIter(row), h.getWeaponStrIter());
        else
            its = Arrays.asList(h.getArmorStrIter(), Utils.nullItemStringIterator, h.getWeaponStrIter());

        stringBuilder.append("|" + Utils.getStringWithNumChar("Equipped Armor", 33) +
                Utils.getStringWithNumChar("Current Selected Item", 33) +
                Utils.getStringWithNumChar("Equipped Weapon", 33) + " |\n");

        int notDone = 0;
        while(notDone < 3) {
            notDone = 0;
            String s = "";
            for (Iterator<String> it : its) {
                if (it.hasNext())
                    s += Utils.getStringWithNumChar(it.next(), 33);
                else {
                    s += Utils.getStringWithNumChar("", 33);
                    notDone++;
                }
            }
            stringBuilder.append("|" + s + " |\n");
        }

        stringBuilder.append("|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                "|  " + Utils.getStringWithNumChar("+------------------+", 24) + Utils.getStringWithNumChar("+------------------+", 24)
                + Utils.getStringWithNumChar("+------------------+", 24) + Utils.getStringWithNumChar("+------------------+", 24) + "  |\n" +
                "|  " + Utils.getStringWithNumChar(Utils.getMenuString("      Attack      ", action == 0, false, ""), 24) +
                Utils.getStringWithNumChar(Utils.getMenuString("    Cast Spell    ",
                            action == 1 && h.getInventory().getSpells().size() == 0, false, ""),24) +
                Utils.getStringWithNumChar(Utils.getMenuString("    Use Potion    ",
                        action == 2 && h.getInventory().getPotions().size() == 0, false, ""),24) +
                Utils.getStringWithNumChar(Utils.getMenuString(" Change Equipment ",action == 3, false, ""), 24) + "  |\n" +
                "|  " + Utils.getStringWithNumChar("+------------------+", 24) + Utils.getStringWithNumChar("+------------------+", 24)
                + Utils.getStringWithNumChar("+------------------+", 24) + Utils.getStringWithNumChar("+------------------+", 24) + "  |\n");

        Iterator<Item> spells = h.getInventory().getSpells().iterator();
        Iterator<Item> potions = h.getInventory().getPotions().iterator();

        while (spells.hasNext() || potions.hasNext()) {
            stringBuilder.append("|" + Utils.getStringWithNumChar("", 25));
            Item spell , potion;
            if (spells.hasNext()) {
            spell = spells.next();
            stringBuilder.append(Utils.getStringWithNumChar(Utils.getMenuString(
                    Utils.getStringWithNumChar(spell.getName(), 18), action == 1 &&
                            h.getInventory().getSpells().get(row) == spell, false, ""), 25));
            } else
                stringBuilder.append(Utils.getStringWithNumChar("", 25));
            if (potions.hasNext()) {
                potion = potions.next();
                stringBuilder.append(Utils.getStringWithNumChar(Utils.getMenuString(
                        Utils.getStringWithNumChar(potion.getName(), 18), action == 2 &&
                                h.getInventory().getPotions().get(row) == potion, false, ""), 25));
            } else
                stringBuilder.append(Utils.getStringWithNumChar("", 25));
            stringBuilder.append(Utils.getStringWithNumChar("", 25) + "|\n");
        }
        stringBuilder.append("|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                "+----------------------------------------------------------------------------------------------------+");

        System.out.println(stringBuilder.toString());
    }

    public void updateLog(String s) {
        log.remove(0);
        log.add("|"+Utils.getStringWithNumChar(s,70)+"|");
    }

    private void rewardHeroes() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Rewards (press c to close)", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n");

        Iterator<Hero> heroIter = heroes.iterator();
        Iterator<Monster> monsterIter = monsters.iterator();
        while (heroIter.hasNext() || monsterIter.hasNext()) {
            Hero curHero = heroIter.next();
            Monster curMonster = monsterIter.next();
            if (curHero.getHp() > 0) {
                curHero.setMoney(curHero.getMoney() + curMonster.getLevel() * 100);
                curHero.setExp(curHero.getExp() + 4);
                curHero.fullyRevive();
                stringBuilder.append(
                        "|" + Utils.getStringWithNumChar(curHero.getName() + " earned $" + curMonster.getLevel() * 100, 100) + "|\n" +
                        "|" + Utils.getStringWithNumChar(curHero.getName() + " earned " + curHero.getExp() + " exp", 100) + "|\n");
                if (curHero.getExp() >= curHero.getLevel()*10) {
                    curHero.levelUp();
                    stringBuilder.append("|" + Utils.getStringWithNumChar(curHero.getName() + " is now level" + curMonster.getLevel(), 100) + "|\n");
                }
            }
            else {
                curHero.fullyRevive();
                stringBuilder.append("|" + Utils.getStringWithNumChar(curHero.getName() + " had to be revived", 100) + "|\n");
            }
            stringBuilder.append("|" + Utils.getStringWithNumChar("", 100) + "|\n");
        }

        stringBuilder.append("+----------------------------------------------------------------------------------------------------+");

        System.out.println(stringBuilder);

        String action = Utils.getValidInputString(new String[]{"c", "q"});

        if (action == "q") {
            System.out.println("Game ended");
            Player.getPlayer().setGameOver(true);
        }
    }

    private boolean isFightOver() {
        int i = 0, j = 0;
        for (Monster m : monsters) {
            if (m.getHp() <= 0)
                i ++;
        }
        for (Hero h : heroes) {
            if (h.getHp() <= 0)
                j ++;
        }
        if (i == monsters.size()) {
            playersWon = true;
            return true;
        } else if (j == heroes.size())
            return true;
        else
            return false;
    }
}

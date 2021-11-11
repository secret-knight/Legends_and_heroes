import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// class that is used to represent the list of items that make up a hero's inventory
// many useful helper functions to parse through the inventory and print out an inventory screen
public class Inventory {
    private List<Item> inv;

    public Inventory() {
        inv = new ArrayList<Item>();
    }

    public void add(Item item) {
        inv.add(item);
    }

    public boolean remove(Item item) {
        return inv.remove(item);
    }

    public Item get(int index) {
        return inv.get(index);
    }

    public int indexOf(Item item) {
        return inv.indexOf(item);
    }

    public int size() {
        return inv.size();
    }

    public boolean contains(Item item) {
        return inv.contains(item);
    }

    public Iterator<Item> iterator(){
        return inv.iterator();
    }

    public List<Item> getSpells() {
        List<Item> spells = new ArrayList<Item>();
        for (Item item : inv) {
            if (item instanceof Spell)
                spells.add(item);
        }
        return spells;
    }

    public Iterator<String> getSpellStrIter(int index) {
        if (getSpells().size() == 0)
            return Utils.nullItemStringIterator;
        else
            return getSpells().get(index).getString().iterator();
    }

    public List<Item> getPotions() {
        List<Item> potions = new ArrayList<Item>();
        for (Item item : inv) {
            if (item instanceof Potion)
                potions.add(item);
        }
        return potions;
    }

    public Iterator<String> getPotionStrIter(int index) {
        if (getPotions().size() == 0)
            return Utils.nullItemStringIterator;
        else
            return getPotions().get(index).getString().iterator();
    }

    public static void enterInventoryScreen(Hero h, boolean cycle) {
        boolean closed = false;
        Iterator<Hero> heroIt = null;
        if (cycle) {
            heroIt = Player.getPlayer().getHeroes().iterator();
            h = heroIt.next();
        }
        int index = 0;

        while (!closed || Player.getPlayer().isGameOver()) {
            checkInventory(h, index, cycle);
            String action = Utils.getValidInputString(new String[]{"w", "s", "e", "r", "u", "n", "c", "q"});
            switch (action) {
                case "e":
                    if (h.getInventory().size() != 0)
                        h.equipItem(h.getInventory().get(index));
                    else
                        System.out.println("Can't equip anything, no items in inventory.");
                    break;
                case "r":
                    if (h.getInventory().size() != 0)
                        h.unequipItem(h.getInventory().get(index));
                    else
                        System.out.println("Can't unequip anything, no items in inventory.");
                    break;
                case "u":
                    if (h.getInventory().size() != 0)
                        h.useItem(h.getInventory().get(index), null);
                    else
                        System.out.println("Can't use anything, no items in inventory.");
                    break;
                case "n":
                    if (cycle) {
                        if (!heroIt.hasNext())
                            heroIt = Player.getPlayer().getHeroes().iterator();
                        h = heroIt.next();
                    } else
                        System.out.println("Can't cycle, in the middle of a battle. Change equipment or exit.");
                    break;
                case "w":
                    if (index == 0)
                        System.out.println("Can't move up in menu, please provide another action that is valid.");
                    else
                        index --;
                    break;
                case "s":
                    if (index == h.getInventory().size() - 1 || h.getInventory().size() == 0)
                        System.out.println("Can't move down in menu, please provide another action that is valid.");
                    else
                        index ++;
                    break;
                case "c":
                    closed = true;
                    break;
                default:
                    System.out.println("Game ended");
                    Player.getPlayer().setGameOver(true);
                    break;
            }
        }
    }

    public Iterator<String> getInvItemStrIter(int index) {
        if (size() == 0)
            return Utils.nullItemStringIterator;
        else
            return get(index).getString().iterator();
    }

    private static void checkInventory(Hero h, int index, boolean cycle) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> controls = new ArrayList<String>(Arrays.asList(new String[]{"| w = Move up  | s = Move down  |", "|   e = Equip Armor or Weapon   |",
                "|  r = Unequip Armor or Weapon  |", "|      u = Use Potion           |", "|      c = Exit Inventory       |",
                "|      q = Quit game            |",}));

        if (cycle)
            controls.add("|      n = Next Hero            |");

        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Inventory", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n" + Utils.getHeroAndControlsString(h, controls) +
                "|" + Utils.getStringWithNumChar("Note: For Potions' Stats Affected Category,", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("H = Health, S = Strength, A = Agility, D = Dexterity, M = Mana", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n");

        List<Iterator<String>> its = Arrays.asList(h.getArmorStrIter(), h.getInventory().getInvItemStrIter(index), h.getWeaponStrIter());

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

        stringBuilder.append("|" + Utils.getStringWithNumChar("Inventory", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("+-----------------------+", 100) + "|\n");

        if (h.getInventory().size() == 0)
            stringBuilder.append("|" + Utils.getStringWithNumChar("| No items in inventory |", 100) + "|\n" +
                    "|" + Utils.getStringWithNumChar("+-----------------------+", 100) + "|\n");
        else {
            Iterator<Item> inv = h.getInventory().iterator();
            while (inv.hasNext()) {
                Item item;
                item = inv.next();
                stringBuilder.append("|" + Utils.getStringWithNumChar(Utils.getMenuString(
                                Utils.getStringWithNumChar(item.getName(), 22), h.getInventory().get(index) == item,
                                item == h.getArmor() || item == h.getWeapon(), "E"), 100) + "|\n")
                        .append("|" + Utils.getStringWithNumChar("+-----------------------+", 100) + "|\n");
            }
        }
        stringBuilder.append("+----------------------------------------------------------------------------------------------------+");

        System.out.println(stringBuilder.toString());
    }
}

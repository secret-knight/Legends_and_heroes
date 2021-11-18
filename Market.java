import java.util.*;

// represents the market, newly generated every time a player enters a market
public class Market {

    private List<Item> marketItems = new ArrayList<Item>();
    private Item currentlySelected;
    private boolean inventorySelected;

    public Market() {
        this(3);
    }

    public Market(int level) {

        List<Integer> randomIndexes = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5});
        Collections.shuffle(randomIndexes);

        ArmorFactory armorFactory = new ArmorFactory();
        WeaponFactory weaponFactory = new WeaponFactory();
        PotionFactory potionFactory = new PotionFactory();
        SpellFactory spellFactory = new SpellFactory();

        for (int i = 0; i < 4; i++) {
            int index = randomIndexes.get(i);
            marketItems.add(armorFactory.createItem(level, index));
            marketItems.add(weaponFactory.createItem(level, index));
            marketItems.add(potionFactory.createItem(level, index));
            if (i < 2) {
                marketItems.add(spellFactory.createItem(level, index));
                marketItems.add(spellFactory.createItem(level, index + 6));
                marketItems.add(spellFactory.createItem(level, index + 12));
            }
        }

        currentlySelected = marketItems.get(0);
    }

    public Item getCurrentlySelected() {
        return currentlySelected;
    }

    public void setCurrentlySelected(Hero h, int index) {
        if (inventorySelected)
            this.currentlySelected = h.getInventory().get(index);
        else
            this.currentlySelected = marketItems.get(index);
    }

    public int getCurrentIndex(Hero h) {
        if (inventorySelected)
            return h.getInventory().indexOf(currentlySelected);
        else
            return marketItems.indexOf(currentlySelected);
    }

    public boolean isInventorySelected() {
        return inventorySelected;
    }

    public void setInventorySelected(boolean inventorySelected) {
        this.inventorySelected = inventorySelected;
    }

    public void enterMarket() {
        boolean closed = false;
        Iterator<Hero> heroIt = Player.getPlayer().getHeroes().iterator();
        Hero h = heroIt.next();

        while (!closed) {
            marketScreen(h);
            String action = Utils.getValidInputString(new String[]{"w", "s", "a", "d", "e", "r", "n", "c", "q"});
            switch (action) {
                case "e":
                    h.buyItem(getCurrentlySelected());
                    break;
                case "r":
                    if (h.sellItem(getCurrentlySelected())) {
                        updateMarketAfterSale(h);
                    } else
                        System.out.println("Can't remove item not in inventory.");
                    break;
                case "n":
                    if (!heroIt.hasNext())
                        heroIt = Player.getPlayer().getHeroes().iterator();
                    h = heroIt.next();
                    break;
                case "w":
                    updateMarketAfterMoveUp(h);
                    break;
                case "s":
                    updateMarketAfterMoveDown(h);
                    break;
                case "a":
                    updateMarketAfterMoveLeft(h);
                    break;
                case "d":
                    updateMarketAfterMoveRight(h);
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

    private void marketScreen(Hero h) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> controls = Arrays.asList(new String[]{"|w = Move up   | a = Move left  |", "|s = Move down | d = Move right |",
                "|     e = Buy Item for Sale     |", "|    r = Sell Inventory Item    |", "|      n = Next Hero            |",
                "|      c = Exit Market          |", "|      q = Quit game            |",});

        stringBuilder.append("+----------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Market", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                Utils.getHeroAndControlsString(h, controls, 100) +
                "|" + Utils.getStringWithNumChar("Note: For Potions' Stats Affected Category,", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("H = Health, S = Strength, A = Agility, D = Dexterity, M = Mana", 100) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 100) + "|\n");

        Iterator<String> stringIterator = currentlySelected.getString().iterator();
        while (stringIterator.hasNext()) {
            stringBuilder.append("|" + Utils.getStringWithNumChar(stringIterator.next(), 100) + "|\n");
        }

        stringBuilder.append("|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                "|    " + Utils.getStringWithNumChar("Inventory", 20) + "    " + Utils.getStringWithNumChar("Items for Sale", 68) + "    |\n" +
                "|    +------------------+    +------------------+    +------------------+    +------------------+    |\n");

        Iterator<Item> it = marketItems.iterator();
        Inventory heroInventory = h.getInventory();
        Iterator<Item> inv = heroInventory.iterator();

        while (it.hasNext() || inv.hasNext()) {
            Item item;
            String s = "";
            if (inv.hasNext()) {
                item = inv.next();
                s += Utils.getMenuString(Utils.getStringWithNumChar(item.getName(), 18),
                        currentlySelected == item && inventorySelected, false, "O");
            } else {
                s += Utils.getMenuString(Utils.getStringWithNumChar("", 18), false, false, "O");
            }
            for (int i = 0; i < 3; i++){
                if (it.hasNext()) {
                    item = it.next();
                    s += "   " + Utils.getMenuString(Utils.getStringWithNumChar(item.getName(), 18),
                    currentlySelected == item && !inventorySelected, heroInventory.contains(item), "O");
                } else {
                    s += "   " + Utils.getMenuString(Utils.getStringWithNumChar("", 18), false, false, "O");
                }
            }
            stringBuilder.append("|" + Utils.getStringWithNumChar(s, 100) + "|\n")
                    .append("|    +------------------+    +------------------+    +------------------+    +------------------+    |\n");
        }
        stringBuilder.append("|" + Utils.getStringWithNumChar("", 100) + "|\n" +
                "+----------------------------------------------------------------------------------------------------+");
        System.out.println(stringBuilder.toString());
    }

    private void updateMarketAfterSale(Hero h) {
        if (h.getInventory().size() == 0) {
            setInventorySelected(false);
            setCurrentlySelected(h, 0);
        }
        else if (getCurrentIndex(h) > 0) {
            setCurrentlySelected(h, getCurrentIndex(h) - 1);
        }
        else
            setCurrentlySelected(h, getCurrentIndex(h)+1);
    }

    private void updateMarketAfterMoveUp(Hero h) {
        if (isInventorySelected()){
            if (getCurrentIndex(h) == 0)
                System.out.println("Can't move up in menu, please provide another action that is valid.");
            else
                setCurrentlySelected(h,getCurrentIndex(h) - 1);
        }
        else {
            if (getCurrentIndex(h) < 3)
                System.out.println("Can't move up in menu, please provide another action that is valid.");
            else
                setCurrentlySelected(h,getCurrentIndex(h) - 3);
        }
    }

    private void updateMarketAfterMoveDown(Hero h) {
        if (isInventorySelected()){
            if (getCurrentIndex(h) == h.getInventory().size()-1)
                System.out.println("Can't move down in menu, please provide another action that is valid.");
            else
                setCurrentlySelected(h,getCurrentIndex(h) + 1);
        }
        else {
            if (getCurrentIndex(h) > 14)
                System.out.println("Can't move down in menu, please provide another action that is valid.");
            else
                setCurrentlySelected(h,getCurrentIndex(h) + 3);
        }
    }

    private void updateMarketAfterMoveLeft(Hero h) {
        if (isInventorySelected()){
            System.out.println("Can't move left in menu, please provide another action that is valid.");
        }
        else {
            if (getCurrentIndex(h) % 3 == 0) {
                if (h.getInventory().size() == 0)
                    System.out.println("Can't move into inventory, no items in it.");
                else if (getCurrentIndex(h) / 3 > h.getInventory().size()-1) {
                    setInventorySelected(true);
                    setCurrentlySelected(h, h.getInventory().size()-1);
                } else {
                    int temp = getCurrentIndex(h) / 3;
                    setInventorySelected(true);
                    setCurrentlySelected(h, temp);
                }
            }
            else
                setCurrentlySelected(h,getCurrentIndex(h) - 1);
        }
    }

    private void updateMarketAfterMoveRight(Hero h) {
        if (isInventorySelected()){
            setInventorySelected(false);
            if (h.getInventory().size() > 6)
                setCurrentlySelected(h,15);
            else
                setCurrentlySelected(h,h.getInventory().indexOf(getCurrentlySelected()) * 3);
        } else {
            if (getCurrentIndex(h) % 3 == 2)
                System.out.println("Can't move right in menu, please provide another action that is valid.");
            else
                setCurrentlySelected(h,getCurrentIndex(h) + 1);
        }
    }
}

import java.util.*;

// class that represents the heroes of the game
public abstract class Hero extends Character {
    private int mana;
    private int money;
    private int exp;
    private int strength;
    private int dexterity;
    private int agility;
    private Armor armor;
    private Weapon weapon;
    private Inventory inventory;
    
    public Hero(String heroName, int heroLvl, int heroStrength, int heroDexterity, int heroAgility) {
        super(heroName, heroLvl);
        mana = heroLvl*500;
        money = heroLvl*3000;
        exp = (heroLvl - 1 ) * 10 + 5;
        strength = heroStrength;
        dexterity = heroDexterity;
        agility = heroAgility;
        inventory = new Inventory();
    }

    public Hero(String heroName, int heroLvl, int baseSkill) {
        this(heroName, heroLvl, baseSkill, baseSkill, baseSkill);
    }

    public Hero(String heroName, int heroLvl) {
        this(heroName, heroLvl, heroLvl * 100 + 500);
    }

    public Hero() {
        this("Rillifane Rallathil", 1);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStrength() {
        if(getBuffedAttributes().containsKey(Attribute.STRENGTH))
        {
            return strength * (1 + getBuffedAttributes().get(Attribute.STRENGTH));
        }
        else
        {
            return strength;
        }
    }

    public int getDexterity() {
        if(getBuffedAttributes().containsKey(Attribute.DEXTERITY))
        {
            return dexterity * (1 + getBuffedAttributes().get(Attribute.DEXTERITY));
        }
        else
        {
            return dexterity;
        }
    }

    public int getAgility() {
        if(getBuffedAttributes().containsKey(Attribute.AGILITY))
        {
            return agility * (1 + getBuffedAttributes().get(Attribute.AGILITY));
        }
        else
        {
            return agility;
        }
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public abstract void setFavoredStats(float multiplier1, float multiplier2);

    public void regainAfterRound() {
        setHp((int) (getHp() * 1.1));
        setMana((int) (getMana() * 1.1));
    }

    public void fullyRevive() {
        setHp(getLevel() * 100);
        setMana((int) (getMana() * 1.1));
    }

    public abstract void levelUp();

    public void buyItem(Item item) {
        if (!inventory.contains(item)){
            boolean canBuy = true;
            if (item.getMinLevel() > getLevel()) {
                System.out.println("Can't buy item, the current hero doesn't meet the min level requirement.");
                canBuy = false;
            }
            if (item.getPrice() > money) {
                System.out.println("Can't buy item, the current hero doesn't have enough money.");
                canBuy = false;
            }
            if (canBuy) {
                money -= item.getPrice();
                inventory.add(item);
            }
        }
        else
            System.out.println("Can't add item already in inventory.");
    }

    public boolean sellItem(Item item) {
        if (inventory.remove(item)) {
            money += item.getPrice() / 2;
            return true;
        } else
            return false;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void equipItem(Item item) {
        if (item instanceof Equippable)
            ((Equippable) item).equip(this);
        else
            System.out.println("Can't equip potions and spells. They aren't equippable.");
    }

    public void unequipItem(Item item) {
        if (item instanceof Equippable)
            ((Equippable) item).unequip(this);
        else
            System.out.println("Can't unequip potions and spells. They aren't equippable.");
    }

    @Override
    public boolean act(Character opponent) {
        int selectedAction = 0;
        int selectedRow = 0;
        boolean closed = false;
        boolean acted = false;

        Monster monster = (Monster) opponent;

        while (!closed && !Player.getPlayer().isGameOver()) {
            actionScreen(monster, selectedAction, selectedRow);
            String action = Utils.getValidInputString(new String[]{"w", "s", "a", "d", "e", "i", "c", "q"});
            switch (action) {
                case "e":
                    if (selectedAction == 1 && getInventory().getSpells().size() == 0)
                        System.out.println("Can't cast spell, no spells in inventory.");
                    else if (selectedAction == 2 && getInventory().getPotions().size() == 0)
                        System.out.println("Can't cast spell, no spells in inventory.");
                    else {
                        closed = performAction(monster, selectedAction, getSelectedItem(selectedAction, selectedRow));
                        acted = true;
                    }
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
                            (selectedAction == 1 && getInventory().getSpells().size() == 0) ||
                            (selectedAction == 2 && getInventory().getPotions().size() == 0) ||
                            (selectedAction == 1 && selectedRow == getInventory().getSpells().size() - 1) ||
                            (selectedAction == 2 && selectedRow == getInventory().getPotions().size() - 1))
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
                case "c":
                    closed = true;
                    break;
                case "q":
                    System.out.println("Game ended");
                    Player.getPlayer().setGameOver(true);
                    break;
                default:
                    break;
            }
        }
        return acted;
    }

    private boolean performAction(Monster m, int action, Item item) {
        switch (action) {
            case 0:
                attack(m);
                return true;
            case 1:
                return castItem(item, m);
            case 2:
                useItem(item);
                return true;
            default:
                return changeEquipment();
        }
    }

    private Item getSelectedItem(int action, int row) {
        if (action == 1)
            return getInventory().getSpells().get(row);
        else if (action == 2)
            return getInventory().getPotions().get(row);
        else
            return null;
    }

    @Override
    public void attack(Character character) {

        Monster monster = (Monster) character;
        if (!monster.attemptDodge()) {
            int monsterDef = (int) (monster.getDefense() * 0.04);
            int attackDamage;
            if (weapon == null) {
                attackDamage = (int) (strength * 0.08);
                monster.setHp(Math.max(monster.getHp() + monsterDef - attackDamage, 0));
                System.out.println(getName() + " dealt " + (attackDamage - monsterDef) + " damage to " + monster.getName());
            } else {
                attackDamage = (int) ((strength + weapon.getDamage()) * 0.08);
                monster.setHp(Math.max(monster.getHp() + monsterDef - attackDamage, 0));
                System.out.println(getName() + " dealt " + (attackDamage - monsterDef)
                        + " damage to " + monster.getName());
            }
        } else
            System.out.println(monster.getName() + " dodged the attack of " + getName());
    }

    @Override
    public boolean attemptDodge() {
        if (Utils.rand.nextFloat() < agility * .002)
            return false;
        else
            return true;
    }

    public void useItem(Item item) {
        if (item instanceof Usable) {
            ((Usable) item).use(this);
            inventory.remove(item);
        }
        else
            System.out.println("Can't use anything besides potions.");
    }

    public boolean castItem(Item item, Monster monster) {
        if (item instanceof Castable) {
            if (((Spell) item).getRequiredMana() > getMana()) {
                System.out.println("Can't cast this spell, do not have enough mana.");
                return false;
            }
            else if (!monster.attemptDodge()) {
                 ((Castable) item).cast(this, monster);
                 return true;
            }
            else {
                System.out.println(monster.getName() + " dodged the attack of " + getName());
                return true;
            }
        }
        else {
            System.out.println("Can't cast anything besides spells.");
            return false;
        }
    }

    public boolean changeEquipment() {

        Armor oldArmor = armor;
        Weapon oldWeapon = weapon;
        Inventory.enterInventoryScreen(this, false);
        if (oldArmor != armor || oldWeapon != weapon) {
            System.out.println(getName() + " changed equipment");
            return true;
        }
        else
            return false;
    }
    
    @Override
    public void applyBuff(Attribute att, int pcnt)
    {
        if(!getBuffedAttributes().containsKey(att))
        {
            getBuffedAttributes().put(att, 0);
        }
        getBuffedAttributes().put(att, getBuffedAttributes().get(att) + pcnt);
    }
    
    public void removeBuff(Attribute att)
    {
        getBuffedAttributes().remove(att);
    }
    
    public void removeBuff(Attribute att, int pcnt)
    {
        int res = getBuffedAttributes().get(att) - pcnt;
        if(res <= 0)
        {
            removeBuff(att);
        }
        else
        {
            getBuffedAttributes().put(att, res);
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Iterator<String> getWeaponStrIter() {
        if (weapon == null)
            return Utils.nullItemStringIterator;
        else
            return weapon.getString().iterator();
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public Iterator<String> getArmorStrIter() {
        if (armor == null)
            return Utils.nullItemStringIterator;
        else
            return armor.getString().iterator();
    }

    // sort by level
    public static Comparator<Hero> heroLevelComparator = new Comparator<Hero>() {

        public int compare(Hero h1, Hero h2) {
            Integer thisLevel = h1.getLevel();
            Integer otherLevel = h2.getLevel();
            return  otherLevel.compareTo(thisLevel);
        }
    };

    private void actionScreen(Monster m, int action, int row) {

        StringBuilder stringBuilder = new StringBuilder();
        List<String> controls = Arrays.asList(new String[]{"| w = Move up  | s = Move down  |", "| a = Move left| d = Move right |",
                "| i = Info     | e = Act        |", "| c = Close    | q = Quit game  |"});
        stringBuilder.append("+------------------------------------------------------------------------------------------------------------------------+\n" +
                "|" + Utils.getStringWithNumChar("Fight!", 120) + "|\n" +
                "|" + Utils.getStringWithNumChar("", 120) + "|\n" +
                Utils.getFightInfoString(this, m, controls, 120) +
                "|" + Utils.getStringWithNumChar("", 120) + "|\n");

        List<Iterator<String>> its;
        if (action == 1)
            its = Arrays.asList(getArmorStrIter(), getInventory().getSpellStrIter(row), getWeaponStrIter());
        else if (action == 2)
            its = Arrays.asList(getArmorStrIter(), getInventory().getPotionStrIter(row), getWeaponStrIter());
        else
            its = Arrays.asList(getArmorStrIter(), Utils.nullItemStringIterator, getWeaponStrIter());

        stringBuilder.append("|" + Utils.getStringWithNumChar("Equipped Armor", 40) +
                Utils.getStringWithNumChar("Current Selected Item", 40) +
                Utils.getStringWithNumChar("Equipped Weapon", 40) + "|\n");

        int notDone = 0;
        while(notDone < 3) {
            notDone = 0;
            String s = "";
            for (Iterator<String> it : its) {
                if (it.hasNext())
                    s += Utils.getStringWithNumChar(it.next(), 40);
                else {
                    s += Utils.getStringWithNumChar("", 40);
                    notDone++;
                }
            }
            stringBuilder.append("|" + s + "|\n");
        }

        stringBuilder.append("|" + Utils.getStringWithNumChar("", 120) + "|\n" +
                "|" + Utils.getStringWithNumChar("+------------------+", 30) +
                Utils.getStringWithNumChar("+------------------+", 30) +
                Utils.getStringWithNumChar("+------------------+", 30) +
                Utils.getStringWithNumChar("+------------------+", 30) + "|\n" +
                "|" + Utils.getStringWithNumChar(Utils.getMenuString("      Attack      ", action == 0, false, ""), 30) +
                Utils.getStringWithNumChar(Utils.getMenuString("    Cast Spell    ",
                        action == 1 && getInventory().getSpells().size() == 0, false, ""),30) +
                Utils.getStringWithNumChar(Utils.getMenuString("    Use Potion    ",
                        action == 2 && getInventory().getPotions().size() == 0, false, ""),30) +
                Utils.getStringWithNumChar(Utils.getMenuString(" Change Equipment ",action == 3, false, ""), 24)
                + "|\n" + "|" + Utils.getStringWithNumChar("+------------------+", 30) +
                Utils.getStringWithNumChar("+------------------+", 30) +
                Utils.getStringWithNumChar("+------------------+", 30) +
                Utils.getStringWithNumChar("+------------------+", 30) + "|\n");

        Iterator<Item> spells = getInventory().getSpells().iterator();
        Iterator<Item> potions = getInventory().getPotions().iterator();

        while (spells.hasNext() || potions.hasNext()) {
            stringBuilder.append("|" + Utils.getStringWithNumChar("", 30));
            Item spell , potion;
            if (spells.hasNext()) {
                spell = spells.next();
                stringBuilder.append(Utils.getStringWithNumChar(Utils.getMenuString(
                        Utils.getStringWithNumChar(spell.getName(), 18), action == 1 &&
                                getInventory().getSpells().get(row) == spell, false, ""), 30));
            } else
                stringBuilder.append(Utils.getStringWithNumChar("", 30));
            if (potions.hasNext()) {
                potion = potions.next();
                stringBuilder.append(Utils.getStringWithNumChar(Utils.getMenuString(
                        Utils.getStringWithNumChar(potion.getName(), 18), action == 2 &&
                                getInventory().getPotions().get(row) == potion, false, ""), 30));
            } else
                stringBuilder.append(Utils.getStringWithNumChar("", 30));
            stringBuilder.append(Utils.getStringWithNumChar("", 30) + "|\n");
        }
        stringBuilder.append("|" + Utils.getStringWithNumChar("", 120) + "|\n" +
                "+------------------------------------------------------------------------------------------------------------------------+");

        System.out.println(stringBuilder.toString());
    }

    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Name: " + getName());
        s.add("Level : " + getLevel() + "       Strength : " + strength);
        s.add("HP : " + getHp() + "       Dexterity : " + dexterity);
        s.add("Exp : " + exp + "          Agility : " + agility);
        s.add("Money : " + money + "         Mana :  " + mana);
        return s;
    }

    public List<String> getVerticalString() {
        List<String> s = new ArrayList<String>();
        s.add("Name: " + getName());
        s.add("Level : " + getLevel());
        s.add("Strength : " + strength);
        s.add("HP : " + getHp());
        s.add("Dexterity : " + dexterity);
        s.add("Exp : " + exp);
        s.add("Agility : " + agility);
        s.add("Money : " + money);
        s.add("Mana :  " + mana);
        return s;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + super.getName() + '\'' +
                ", level=" + super.getLevel() +
                ", hp=" + super.getHp() +
                ", mana=" + mana +
                ", money=" + money +
                ", exp=" + exp +
                ", strength=" + strength +
                ", dexterity=" + dexterity +
                ", agility=" + agility +
                '}';
    }
}

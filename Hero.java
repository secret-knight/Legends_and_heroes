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
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getAgility() {
        return agility;
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
    public void attack(Character monster, Fight fight) {
        if (!monster.attemptDodge()) {
            int monsterDef = (int) (((Monster) monster).getDefense() * 0.04);
            int attackDamage;
            if (weapon == null) {
                attackDamage = (int) (strength * 0.08);
                monster.setHp(Math.max(monster.getHp() + monsterDef - attackDamage, 0));
                fight.updateLog(getName() + " dealt " + (attackDamage - monsterDef) + " damage to " + monster.getName());
            } else {
                attackDamage = (int) ((strength + weapon.getDamage()) * 0.08);
                monster.setHp(Math.max(monster.getHp() + monsterDef - attackDamage, 0));
                fight.updateLog(getName() + " dealt " + (attackDamage - monsterDef)
                        + " damage to " + monster.getName());
            }
        } else
            fight.updateLog(monster.getName() + " dodged the attack of " + getName());
    }

    @Override
    public boolean attemptDodge() {
        if (Utils.rand.nextFloat() < agility * .002)
            return false;
        else
            return true;
    }

    public void useItem(Item item, Fight fight) {
        if (item instanceof Usable) {
            ((Usable) item).use(this, fight);
            inventory.remove(item);
        }
        else
            System.out.println("Can't use anything besides potions.");
    }

    public boolean castItem(Item item, Monster monster, Fight fight) {
        if (item instanceof Castable) {
            if (((Spell) item).getRequiredMana() > getMana()) {
                System.out.println("Can't cast this spell, do not have enough mana.");
                return false;
            }
            else if (!monster.attemptDodge()) {
                 ((Castable) item).cast(this, monster, fight);
                 return true;
            }
            else {
                fight.updateLog(monster.getName() + " dodged the attack of " + getName());
                return true;
            }
        }
        else {
            System.out.println("Can't cast anything besides spells.");
            return false;
        }
    }

    public boolean changeEquipment(Fight fight) {

        Armor oldArmor = armor;
        Weapon oldWeapon = weapon;
        Inventory.enterInventoryScreen(this, false);
        if (oldArmor != armor || oldWeapon != weapon) {
            fight.updateLog(getName() + " changed equipment");
            return true;
        }
        else
            return false;
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

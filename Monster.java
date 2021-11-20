import java.util.ArrayList;
import java.util.List;

// class that represents Monsters of the game, much less functionality than heroes
public abstract class Monster extends Character {
    private int damage;
    private int defense;
    private int dodgeChance;

    public Monster(String monsterName, int monsterLvl, int monsterDamage, int monsterDefense, int monsterDodge) {
        super(monsterName, monsterLvl);
        damage = monsterDamage;
        defense = monsterDefense;
        dodgeChance = monsterDodge;
    }

    public Monster(String monsterName, int monsterLvl, int baseSkill) {
        this(monsterName, monsterLvl, baseSkill, baseSkill, baseSkill);
    }

    public Monster(String monsterName, int monsterLvl) {
        this(monsterName, monsterLvl, monsterLvl * 100 + 500);
    }

    public Monster() {
        this("Desghidorrah", 1);
    }

    @Override
    public boolean act(Character opponent) {
        Hero hero = (Hero) opponent;
        attack(hero);
        return true;
    }

    // attacks a hero and adds the attack to the log
    @Override
    public void attack(Character hero) {
        if (!hero.attemptDodge()) {
            int attackDamage = (int) (damage * 0.08);
            if (((Hero) hero).getArmor() == null) {
                hero.setHp(Math.max(hero.getHp() - attackDamage, 0));
                System.out.println(getName() + " dealt " + attackDamage + " damage to " + hero.getName());
            } else{
                int armorEffect = (int) (((Hero) hero).getArmor().getReducedDamage() * 0.03);
                hero.setHp(Math.max(hero.getHp() + armorEffect - attackDamage, 0));
                System.out.println(getName() + " dealt " + (attackDamage - armorEffect) + " damage to " + hero.getName());
            }
        } else
            System.out.println(hero.getName() + " dodged the attack of " + getName());
    }

    // attempts to dodge given the monster's stats
    @Override
    public boolean attemptDodge() {
        if (Utils.rand.nextFloat() < dodgeChance * .01)
            return false;
        else
            return true;
    }

    public abstract void setFavoredStat(float multiplier);

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public void setDodgeChance(int dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    public int getDodgeChance() {
        return dodgeChance;
    }
    
    @Override
    public void applyBuff(Attribute att, int pcnt)
    {
        // do nothing
    }

    public void removeBuff(Attribute att)
    {
        // do nothing
    }

    public void removeBuff(Attribute att, int pcnt)
    {
        // do nothing
    }

    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Name: " + getName());
        s.add("Level : " + getLevel());
        s.add("HP : " + getHp());
        s.add("Damage : " + damage);
        s.add("Defense : " + defense);
        s.add("Dodge % : " + dodgeChance);
        return s;
    }
}

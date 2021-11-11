// most general version of character in the game, each are fightable with current descriptions
public abstract class Character implements Fightable{
    private String name;
    private int level;
    private int hp;

    public Character(String heroName, int heroLvl) {
        name = heroName;
        level = heroLvl;
        hp = heroLvl * 100;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}

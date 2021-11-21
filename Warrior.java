public class Warrior extends Hero{

    public Warrior(String heroName, int heroLvl, float m1, float m2) {
        super(heroName, heroLvl);
        setFavoredStats(m1, m2);
    }

    public Warrior(String heroName, int heroLvl, int mana, int money, int exp, int heroStrength, int heroDexterity, int heroAgility, float m1, float m2) {
        super(heroName, heroLvl, heroStrength, heroDexterity, heroAgility);
        setFavoredStats(m1, m2);
    }
    
    public Warrior(float m1, float m2) {
        super();
        setFavoredStats(m1, m2);
    }

    @Override
    public void setFavoredStats(float multiplier1, float multiplier2) {
        super.setStrength((int) (super.getStrength()*multiplier1));
        super.setAgility((int) (super.getAgility()*multiplier2));
    }

    @Override
    public void levelUp() {
        setLevel(getLevel() + 1);
        setHp(getLevel() * 100);
        setStrength((int) (getStrength() * 1.1));
        setDexterity((int) (getDexterity() * 1.05));
        setAgility((int) (getAgility() * 1.1));
    }
}

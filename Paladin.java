public class Paladin extends Hero{

    public Paladin(String heroName, int heroLvl, float m1, float m2) {
        super(heroName, heroLvl);
        setFavoredStats(m1, m2);
    }

    public Paladin(float m1, float m2) {
        super();
        setFavoredStats(m1, m2);
    }

    @Override
    public void setFavoredStats(float multiplier1, float multiplier2) {
        super.setStrength((int) (super.getStrength()*multiplier1));
        super.setDexterity((int) (super.getDexterity()*multiplier2));
    }

    @Override
    public void levelUp() {
        setLevel(getLevel() + 1);
        setHp(getLevel() * 100);
        setStrength((int) (getStrength() * 1.1));
        setDexterity((int) (getDexterity() * 1.1));
        setAgility((int) (getAgility() * 1.05));
    }
}

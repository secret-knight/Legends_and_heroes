// an extension of the monster class with specific stats favored
public class Spirit extends Monster{

    public Spirit(String monsterName, int monsterLvl, float m) {
        super(monsterName, monsterLvl);
        setFavoredStat(m);
    }

    public Spirit(float m) {
        super();
        setFavoredStat(m);
    }

    public Spirit() {
        super();
        setFavoredStat(1);
    }

    @Override
    public void setFavoredStat(float multiplier) {
        super.setDodgeChance((int) (super.getDodgeChance()*multiplier));
    }

}

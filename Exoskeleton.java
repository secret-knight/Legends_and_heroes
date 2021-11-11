// an extension of the monster class with specific stats favored
public class Exoskeleton extends Monster{

    public Exoskeleton(String monsterName, int monsterLvl, float m) {
        super(monsterName, monsterLvl);
        setFavoredStat(m);
    }

    public Exoskeleton(float m) {
        super();
        setFavoredStat(m);
    }

    public Exoskeleton() {
        super();
        setFavoredStat(1);
    }

    @Override
    public void setFavoredStat(float multiplier) {
        super.setDefense((int) (super.getDefense()*multiplier));
    }

}

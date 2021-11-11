// an extension of the monster class with specific stats favored
public class Dragon extends Monster{

    public Dragon(String monsterName, int monsterLvl, float m) {
        super(monsterName, monsterLvl);
        setFavoredStat(m);
    }

    public Dragon(float m) {
        super();
        setFavoredStat(m);
    }

    public Dragon() {
        super();
        setFavoredStat(1);
    }

    @Override
    public void setFavoredStat(float multiplier) {
        super.setDamage((int) (super.getDamage()*multiplier));
    }

}

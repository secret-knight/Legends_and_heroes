import java.util.HashMap;
import java.util.Map;

// most general version of character in the game, each are fightable with current descriptions
public abstract class Character implements Fightable{
    private String name;
    private int level;
    private int hp;
    private Map<Attribute, Integer> buffedAtt; 
    private Lane orgLane;
    
    public Character(String heroName, int heroLvl) {
        name = heroName;
        level = heroLvl;
        hp = heroLvl * 100;
        this.setBuffedAtt(new HashMap<Attribute, Integer>());
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
    
    public abstract void applyBuff(Attribute att, int pcnt);
    public abstract void removeBuff(Attribute att);
    public abstract void removeBuff(Attribute att, int pcnt);

    public Map<Attribute, Integer> getBuffedAttributes()
    {
        return buffedAtt;
    }

    public void setBuffedAtt(Map<Attribute, Integer> buffedAtt)
    {
        this.buffedAtt = buffedAtt;
    }
    
    @Override
    public boolean equals(Object another)
    {
        boolean res = false;
        if(another instanceof Character)
        {
            res = this.getName().equals(((Character)another).getName()) && 
                    this.getHp() == ((Character)another).getHp() && 
                    this.getLevel() == ((Character)another).getLevel(); 
        }
        return res;
    }

    public Lane getOrgLane()
    {
        return this.orgLane;
    }
    
    public void setOrgLane(Lane lane)
    {
        this.orgLane = lane;
    }
}

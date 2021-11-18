import java.util.ArrayList;
import java.util.List;

public class Potion extends Item implements Usable {
    private int statIncrease;
    private StatType[] statTypes;

    public Potion(String potionName, int potionPrice, int potionMinLevel, int potionStatIncrease, StatType[] potionStatTypes) {
        super(potionName, potionPrice, potionMinLevel);
        statIncrease = potionStatIncrease;
        statTypes = potionStatTypes;
    }

    @Override
    public void use(Hero h) {
        for (StatType stat : statTypes) {
            switch (stat) {
                case Health:
                    h.setHp(h.getHp() + statIncrease);
                    break;
                case Strength:
                    h.setStrength(h.getStrength() + statIncrease);
                    break;
                case Dexterity:
                    h.setDexterity(h.getDexterity() + statIncrease);
                    break;
                case Agility:
                    h.setAgility(h.getAgility() + statIncrease);
                    break;
                case Mana:
                    h.setMana(h.getMana() + statIncrease);
                    break;
                default:
                    break;
            }
        }

        System.out.println(h.getName() + " increased " + getStatTypesString() + " by " + statIncrease);
    }

    @Override
    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Potion");
        s.add(getName());
        s.add("Price : " + getPrice());
        s.add("Min Level : " + getMinLevel());
        s.add("Stats Affected : " + getStatTypesString());
        s.add("Stat Increase : " + statIncrease);
        return s;
    }

    private String getStatTypesString() {
        String s = "[";
        for (StatType type : statTypes)
            s += type.name().charAt(0) + ",";
        return s + "]";
    }
}

import java.util.ArrayList;
import java.util.List;

// class represents the Armor item, can be equipped
public class Armor extends Item implements Equippable{
    private int reducedDamage;

    public Armor(String armorName, int armorPrice, int armorMinLevel, int armorReducedDamage) {
        super(armorName, armorPrice, armorMinLevel);
        reducedDamage = armorReducedDamage;
    }

    public int getReducedDamage() {
        return reducedDamage;
    }

    @Override
    public void equip(Hero h) {
        // dont equip armor if the currently equipped weapon is two handed, must unequip weapon first
        if (h.getWeapon() != null && h.getWeapon().isTwoHands())
            System.out.println("The hero has a two handed weapon currently equipped. The hero must unequip the weapon " +
                    "or change to a one handed weapon before adding on this armor.\n" +
                    "Can't have armor and a two handed weapon equipped at the same time.");
        else
            h.setArmor(this);
    }

    // unequips armor, sets hero's armor to null
    @Override
    public void unequip(Hero h) {
        if (h.getArmor() == this)
            h.setArmor(null);
        else
            System.out.println("Can't unequip item that is not currently equipped.");
    }

    @Override
    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Armor");
        s.add(getName());
        s.add("Price : " + getPrice());
        s.add("Min Level : " + getMinLevel());
        s.add("Reduced Damage : " + reducedDamage);
        return s;
    }
}

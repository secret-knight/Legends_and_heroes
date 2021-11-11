import java.util.ArrayList;
import java.util.List;

// class represents the Weapon item, can be equipped
public class Weapon extends Item implements Equippable {
    private int damage;
    private boolean twoHands;

    public Weapon(String weaponName, int weaponPrice, int weaponMinLevel, int weaponDamage, boolean weaponHands) {
        super(weaponName, weaponPrice, weaponMinLevel);
        damage = weaponDamage;
        twoHands = weaponHands;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isTwoHands() {
        return twoHands;
    }

    @Override
    public void equip(Hero h) {
        // dont equip weapon if armor is currently equipped and the weapon is two-handed, must unequip armor first
        if (h.getArmor() == null || !this.isTwoHands())
            h.setWeapon(this);
        else
            System.out.println("Desired weapon to equip requires two hands, the hero must remove their armor first.\n" +
                    "Can't have armor and a two handed weapon equipped at the same time.");
    }

    // unequips weapon, sets hero's weapon to null
    @Override
    public void unequip(Hero h) {
        if (h.getWeapon() == this)
            h.setWeapon(null);
        else
            System.out.println("Can't unequip item that is not currently equipped.");
    }

    @Override
    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Weapon");
        s.add(getName());
        s.add("Price : " + getPrice());
        s.add("Min Level : " + getMinLevel());
        s.add("Damage Inflicted : " + damage);
        if (twoHands)
            s.add("Requires 2 hands");
        else
            s.add("Requires 1 hand");
        return s;
    }
}

import java.util.ArrayList;
import java.util.List;

public class FireSpell extends Spell {
    public FireSpell(String spellName, int spellPrice, int spellMinLevel,int spellDamage,
                    int spellRequiredMana, int spellReduceStat) {
        super(spellName, spellPrice, spellMinLevel, spellDamage, spellRequiredMana, spellReduceStat);
    }

    @Override
    public void cast(Hero caster, Monster monster) {
        monster.setDefense(Math.max(monster.getDefense() - getReduceStat(), 0));
        caster.setMana(caster.getMana() - getRequiredMana());
        int damageWithDexterity = getDamage() + (caster.getDexterity()/10000) * getDamage();
        int defense = (int) (monster.getDefense() * 0.03);
        monster.setHp(Math.max(monster.getHp() + defense - damageWithDexterity, 0));
        System.out.println(caster.getName() + " dealt " + (defense - damageWithDexterity) + " damage to " + monster.getName());
    }

    @Override
    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Fire Spell");
        s.add(getName());
        s.add("Price : " + getPrice());
        s.add("Min Level : " + getMinLevel());
        s.add("Base Damage : " + getDamage());
        s.add("Reduced Monster Defense : " + getReduceStat());
        s.add("Required Mana : " + getRequiredMana());
        return s;
    }
}

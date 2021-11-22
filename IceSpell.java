import java.util.ArrayList;
import java.util.List;
/**
 * ice spell entity
 */
public class IceSpell extends Spell {

    public IceSpell(String spellName, int spellPrice, int spellMinLevel,int spellDamage,
                 int spellRequiredMana, int spellReduceStat) {
        super(spellName, spellPrice, spellMinLevel, spellDamage, spellRequiredMana, spellReduceStat);
    }

    @Override
    public void cast(Hero caster, Monster monster) {
        monster.setDamage(Math.max(monster.getDamage() - getReduceStat(), 0));
        caster.setMana(caster.getMana() - getRequiredMana());
        int damageWithDexterity = getDamage() + (caster.getDexterity()/10000) * getDamage();
        int defense = (int) (monster.getDefense() * 0.04);
        monster.setHp(Math.max(monster.getHp() + defense - damageWithDexterity, 0));
        System.out.println(caster.getName() + " dealt " + Math.abs(defense - damageWithDexterity) + " damage to " + monster.getName());
    }

    @Override
    public List<String> getString() {
        List<String> s = new ArrayList<String>();
        s.add("Ice Spell");
        s.add(getName());
        s.add("Price : " + getPrice());
        s.add("Min Level : " + getMinLevel());
        s.add("Base Damage : " + getDamage());
        s.add("Reduced Monster Damage : " + getReduceStat());
        s.add("Required Mana : " + getRequiredMana());
        return s;
    }
}

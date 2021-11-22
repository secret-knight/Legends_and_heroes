import java.util.List;

/**
 * spell class that abstract spell common basic member and function
 */
public abstract class Spell extends Item implements Castable {
    private int damage;
    private int requiredMana;
    private int reduceStat;

    public Spell(String spellName, int spellPrice, int spellMinLevel,int spellDamage,
                 int spellRequiredMana, int spellReduceStat) {
        super(spellName, spellPrice, spellMinLevel);
        damage = spellDamage;
        requiredMana = spellRequiredMana;
        reduceStat = spellReduceStat;
    }

    public int getDamage() {
        return damage;
    }

    public int getReduceStat() {
        return reduceStat;
    }

    public int getRequiredMana() {
        return requiredMana;
    }

    @Override
    public abstract List<String> getString();
}

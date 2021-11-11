// items that are castable can be casted by a hero onto a monster in a fight
public interface Castable {
    public void cast(Hero caster, Monster monster, Fight fight);
}

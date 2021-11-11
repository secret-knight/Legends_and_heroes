// characters that are fightable can attack and dodge other characters
public interface Fightable {
    public void attack(Character opponent, Fight fight);

    public boolean attemptDodge();
}

// characters that are fightable can attack and dodge other characters
public interface Fightable {

    public boolean act(Character opponent);

    public  void attack(Character opponent);

    public boolean attemptDodge();
}

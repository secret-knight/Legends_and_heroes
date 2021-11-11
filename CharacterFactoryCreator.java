// factory interface from which hero and monster factory classes will implement
public interface CharacterFactoryCreator {
    Character createCharacter(int i, String name, int level);

    Character createCharacter(int level);
}

// hero factory that creates heroes either purely random or with more inputs passed in
public class HeroFactory implements CharacterFactoryCreator{

    private static String[] names = PresetLoader.getInstance().getAllHerosNames();

    // creates hero with some input provided
    @Override
    public Character createCharacter(int i, String name, int level) {
        switch (i) {
            case 0:
                return new Warrior(name, level, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
            case 1:
                return new Sorcerer(name, level, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
            default:
                return new Paladin(name, level, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
        }
    }

    // creates hero completely at random
    @Override
    public Character createCharacter(int level) {
        return new Warrior(names[Utils.rand.nextInt(names.length)], level, (float) 1.1+Utils.rand.nextFloat()/10, (float) 1.1+Utils.rand.nextFloat()/10);
    }
}

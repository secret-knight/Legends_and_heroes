// monster factory that creates monsters either purely random or with more inputs passed in
public class MonsterFactory implements CharacterFactoryCreator{

    private static String[] dragonNames = PresetLoader.getInstance().getDragonNames();
    private static String[] exoskeletonNames = PresetLoader.getInstance().getExoskeletonNames();
    private static String[] spiritNames = PresetLoader.getInstance().getSpiritNames();

    // creates monster with some given input such as name and type
    @Override
    public Character createCharacter(int i, String name, int level) {
        switch (i) {
            case 0:
                return new Dragon(name, level, (float) 1.1+Utils.rand.nextFloat()/10);
            case 1:
                return new Exoskeleton(name, level, (float) 1.1+Utils.rand.nextFloat()/10);
            default:
                return new Spirit(name, level, (float) 1.1+Utils.rand.nextFloat()/10);
        }
    }

    // creates monster completely at random
    @Override
    public Character createCharacter(int level) {
        int val = Utils.rand.nextInt(3);
        switch(val) {
            case 0:
                return new Dragon(dragonNames[Utils.rand.nextInt(dragonNames.length)], level, (float) 1.1+Utils.rand.nextFloat()/10);
            case 1:
                return new Exoskeleton(exoskeletonNames[Utils.rand.nextInt(exoskeletonNames.length)], level, (float) 1.1+Utils.rand.nextFloat()/10);
            default:
                return new Spirit(spiritNames[Utils.rand.nextInt(spiritNames.length)], level, (float) 1.1+Utils.rand.nextFloat()/10);
        }
    }
}

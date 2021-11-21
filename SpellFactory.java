// given a set of names, spells are created with random attributes given a level
public class SpellFactory implements ItemFactoryCreator {
    private static String[] iceSpellNames = PresetLoader.getInstance().getIceSpellNames();
    private static String[] fireSpellNames = PresetLoader.getInstance().getFireSpellNames();
    private static String[] lightningSpellNames = PresetLoader.getInstance().getLightningSpellNames();

    @Override
    public Item createItem(int level, int i) {

        // this value is the level we randomly assign given the level of the hero shopping
        int val = Utils.rand.nextInt(level*(Utils.rand.nextInt(2)+2)) + 1;

        switch (i / 6) {
            case 0:
                return new IceSpell(iceSpellNames[i], val * (Utils.rand.nextInt(101) + 100), val,
                        val * (Utils.rand.nextInt(81) + 60), val * (Utils.rand.nextInt(101) + 100),
                        val * (Utils.rand.nextInt(101) + 100));
            case 1:
                return new FireSpell(fireSpellNames[i-6], val * (Utils.rand.nextInt(101) + 100), val,
                        val * (Utils.rand.nextInt(81) + 60), val * (Utils.rand.nextInt(101) + 100),
                        val * (Utils.rand.nextInt(101) + 100));
            default:
                return new LightningSpell(lightningSpellNames[i-12], val * (Utils.rand.nextInt(101) + 100), val,
                        val * (Utils.rand.nextInt(81) + 60), val * (Utils.rand.nextInt(101) + 100),
                        val * (Utils.rand.nextInt(101) + 100));
        }
    }
}

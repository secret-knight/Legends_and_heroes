// given a set of names, armor is created with random attributes given a level
public class ArmorFactory implements ItemFactoryCreator{
    private static String[] armorNames = PresetLoader.getInstance().getArmorNames();

    @Override
    public Item createItem(int level, int i) {

        // this value is the level we randomly assign given the level of the hero shopping
        int val = Utils.rand.nextInt(level*(Utils.rand.nextInt(2)+2)) + 1;

        return new Armor(armorNames[i], val*(Utils.rand.nextInt(101)+200), val,
                    val*(Utils.rand.nextInt(101)+200));
    }
}

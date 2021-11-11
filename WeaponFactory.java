// given a set of names, weapons are created with random attributes given a level
public class WeaponFactory implements ItemFactoryCreator {
    private static String[] weaponNames = new String[]{"Sword", "Bow", "Scythe",
            "Axe", "TSwords", "Dagger"};
    private static boolean[] weaponHands = new boolean[]{false, true, true, false, true, false};

    @Override
    public Item createItem(int level, int i) {

        // this value is the level we randomly assign given the level of the hero shopping
        int val = Utils.rand.nextInt(level*(Utils.rand.nextInt(2)+2)) + 1;

        return new Weapon(weaponNames[i], val*(Utils.rand.nextInt(101)+200), val,
                val*(Utils.rand.nextInt(101)+200), weaponHands[i]);
    }
}

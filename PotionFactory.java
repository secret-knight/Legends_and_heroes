// given a set of names, potions are created with random attributes given a level
public class PotionFactory implements ItemFactoryCreator {
    private static String[] potionNames = new String[]{"Healing Potion", "Strength Potion", "Magic Potion",
            "Luck Elixir", "Mermaid Tears", "Ambrosia"};
    private static StatType[][] potionAffectedStats = new StatType[][]{{StatType.Health}, {StatType.Strength},
            {StatType.Mana}, {StatType.Agility}, {StatType.Health, StatType.Mana, StatType.Strength, StatType.Agility},
            {StatType.Health, StatType.Mana, StatType.Strength, StatType.Dexterity, StatType.Agility}};

    @Override
    public Item createItem(int level, int i) {
        // this value is the level we randomly assign given the level of the hero shopping
        int val = Utils.rand.nextInt(level*(Utils.rand.nextInt(2)+2)) + 1;

        return new Potion(potionNames[i], val*(Utils.rand.nextInt(101)+100), val,
                val*(Utils.rand.nextInt(81)+60), potionAffectedStats[i]);
    }
}

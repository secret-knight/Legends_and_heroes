import java.util.ArrayList;
import java.util.List;

// different stats that could be increased by a potion
public enum StatType {
    Health, Mana, Strength, Dexterity, Agility;
    
    public static List<String> getAllAttrs()
    {
        List<String> res = new ArrayList<String>();
        StatType[] arr = StatType.values();
        for(StatType attr : arr)
        {
            res.add(attr.toString());
        }
        return res;
    }
}

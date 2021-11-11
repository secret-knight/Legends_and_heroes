import java.util.List;

// abstract class that represents items at a base level
public abstract class Item {
    private String name;
    private int price;
    private int minLevel;

    public Item(String n, int p, int l) {
        name = n;
        price = p;
        minLevel = l;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public abstract List<String> getString();
}

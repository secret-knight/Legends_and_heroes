// factory interface from which each item's factory classes will implement
public interface ItemFactoryCreator {
    Item createItem(int level, int index);
}

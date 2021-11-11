public class Main {

    public static void main(String[] args) {
        // creates player and map
        Player p = Player.getPlayer();
        Map m = Map.getMap();

        // play game
        while (!p.isGameOver()) {
            m.move();
        }
    }
}

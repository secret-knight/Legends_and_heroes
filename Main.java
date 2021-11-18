public class Main {

    public static void main(String[] args) {
        // creates player and map
        Player p = Player.getPlayer();
        Map m = Map.getMap();
        int roundsPlayed = 1;

        // play game
        while (!p.isGameOver()) {
            m.move();
            if (roundsPlayed % 8 == 0) {
                m.createNewMonsters();
            }
            m.checkForWin();
        }
    }
}

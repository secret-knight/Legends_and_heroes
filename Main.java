public class Main {

    public static void main(String[] args) {
        System.out.println(AsciiArt.LEGENDSOFVALOR);
        // creates player and map
        Player p = Player.getPlayer();
        Map m = Map.getMap();
        int roundsPlayed = 1;

        // play game
        while (!p.isGameOver()) {
            m.move();
            if (roundsPlayed % 2 == 0) {
                m.createNewMonsters();
            }
            roundsPlayed++;
            m.checkForWin();
        }
    }
}


public class LegendOfValor extends AbsRPGGame
{
    private static final int    ROWS                = 8;
    private static final int    COLS                = 8;
    private static final int    NUMOFLANE           = 3;
    public  static final String GAMENAME = "LegendsOfValor";
    private              LOVmap map;
    private              Player player;
    public LegendOfValor() {
        super(ROWS, COLS, NUMOFLANE);
        map = LOVmap.getMap(ROWS, COLS, NUMOFLANE);
        player = Player.getPlayer(); 
    }

    

    @Override
    public boolean startGame()
    {
        System.out.println(AsciiArt.LEGENDSOFVALOR);
        // creates player and map
        int roundsPlayed = 1;

        // play game
        while (!player.isGameOver()) {
            map.move();
            if (roundsPlayed % 8 == 0) {
                map.createNewMonsters();
            }
            roundsPlayed++;
            map.checkForWin();
        }
        return false;
    }

}

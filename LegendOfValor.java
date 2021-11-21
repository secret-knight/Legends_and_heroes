import java.io.File;

/**
 * main game class for LOV
 */
public class LegendOfValor extends AbsRPGGame
{
    private static final int    ROWS        = 8;
    private static final int    COLS        = 8;
    private static final int    NUMOFLANE   = 3;
    private static final int    MONSTERFREQ = 8;
    public  static final String GAMENAME    = "LegendsOfValor";
    public  static final File   soundFile   = new File(System.getProperty("user.dir") + "/Music/" + "music.wav");
    private              LOVmap map;
    private              Player player;
    
    public LegendOfValor() {
        super(ROWS, COLS, NUMOFLANE);
        System.out.println(AsciiArt.LEGENDSOFVALOR);
        BGM.playClip(soundFile);
        System.out.println("BGM\"Beat Thee\" Written by Alexander Nakarada. The music play in this game is for education purpose only");
        map = LOVmap.getMap(ROWS, COLS, NUMOFLANE);
        player = Player.getPlayer(); 
    }

    @Override
    public boolean startGame()
    {
        // creates player and map
        int roundsPlayed = 1;

        // play game
        while (!player.isGameOver()) {
            map.move();
            if (roundsPlayed % MONSTERFREQ == 0) {
                map.createNewMonsters();
            }
            roundsPlayed++;
            map.checkForWin();
        }
        return false;
    }

}

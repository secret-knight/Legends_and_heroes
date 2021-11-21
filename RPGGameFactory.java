import java.util.HashSet;
import java.util.Set;

public class RPGGameFactory{
    private Set<String> gameNames;
    private String[] perLoadGames = new String[] {LegendOfValor.GAMENAME};
    private static RPGGameFactory rpgGameFactory = null;
    
    private RPGGameFactory()
    {
        this.gameNames = new HashSet<String>();
        loadGameName();
    }
    
    public static RPGGameFactory getInstance()
    {
        if(rpgGameFactory == null)
        {
            rpgGameFactory = new RPGGameFactory();
        }
        return rpgGameFactory;
    }
    /**
     * @param       gameName  
     * @return      IGame
     * @description Game factory for BoardGames. Create Game From gamename
     */
    public AbsRPGGame createGame(String gameName)
    {
        AbsRPGGame game = null;
        if(this.gameNames.contains(gameName))
        {
            if(gameName.equals(LegendOfValor.GAMENAME))
            {
                return new LegendOfValor();
            }
        }
        return game;
    }
    
    private void loadGameName()
    {
        for(String game : perLoadGames)
        {
            this.addGameName(game);
        }
    }
    
    public Set<String> getGameNames()
    {
        return this.gameNames;
    }
    
    public void addGameName(String gameName)
    {
        this.gameNames.add(gameName);
    }
}

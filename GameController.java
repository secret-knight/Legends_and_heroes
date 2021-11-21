public class GameController {
    
    /**
     * start games
     */
    public void start()
    {
        //Create view
        MainView mainView = new MainView();
        mainView.displayWelcome();
        //GameType gameType = mainView.getGameType(); // choose board, card, etc...
        boolean keepPlay = true;
        while(keepPlay)
        {
            String gameName = mainView.getGameName();
            keepPlay = createAndRun(gameName);
        }
        mainView.displayGoodBye();
    }
    
    /**
     *  Create games and execute games, 
     * @param gameName the name of game
     * @return user wanna play other games or not
     */
    public boolean createAndRun(String gameName)
    {
        IRPGGame   game             = RPGGameFactory.getInstance().createGame(gameName);
        boolean    isPlayOtherGames = game.startGame();
        return isPlayOtherGames;
    }
}

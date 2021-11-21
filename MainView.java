import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Main entrence view of the game
 */
public class MainView {

    public void displayWelcome()
    {
        System.out.println("WELCOME!!!");
    }

    /**
     * ask user select game
     * 
     * @return GameName enum of the game user selected
     */
    public String getGameName()
    {
        Scanner instream = new Scanner(System.in);
        System.out.println("=============GAME LIBRARY=============");
        int n = 1;
        List<String> gameNames = new ArrayList<>();
        RPGGameFactory rpgFactory = RPGGameFactory.getInstance();
        for(String name : rpgFactory.getGameNames()) {
            System.out.println(n + ". " + name);
            gameNames.add(name);
            n++;
        }
        
        String idx = "";
        while(idx.length() == 0 || !idx.matches("-?\\d+") || Integer.valueOf(idx) >= n)
        {
            System.out.println("Please choose game by index:");
            idx = instream.nextLine();
        }
        
        String gameName = gameNames.get(Integer.valueOf(idx) - 1);
        return gameName;
    }

    /**
     * say good bye, gg 
     */
    public void displayGoodBye()
    {
        System.out.println("=============GOOD BYE!!!=============");
    }

}

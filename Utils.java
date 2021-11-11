import java.util.*;

public class Utils {

    // create a scanner object to be used for the entire application
    public static final Scanner scan = new Scanner( System.in );
    public static final Random rand = new Random();
    public static final HeroFactory heroFactory = new HeroFactory();
    public static final MonsterFactory monsterFactor = new MonsterFactory();
    public static final Iterator<String> nullItemStringIterator = Arrays.asList(new String[]{""}).iterator();

    // helper function that returns an input integer from the user given a lower and upper bound
    // a string is also taken in so that print messages are more descriptive to the user for what the desired input
    public static int getIntInput(String str, int lowerBound, int upperBound) {
        boolean validInput = false;
        System.out.println(String.format("Enter %s :", str));
        while (!validInput) {
            // make sure input from user is integer. if it is and within bounds return it. if it is an int and not within
            // bounds, ask for another input int
            try{
                int input = scan.nextInt();
                if (input >= lowerBound && input <= upperBound){
                    return input;
                }
                System.out.println(String.format("Please enter a valid integer for the %s, specifically an " +
                        "integer between %d and %d.", str, lowerBound, upperBound));
            }
            // catch error if user inputted something besides an int
            catch (java.util.InputMismatchException e){
                System.out.println(String.format("Please make sure to enter an integer between %d and %d! No other " +
                        "inputs are allowed!", lowerBound, upperBound));
                scan.nextLine();
            }
        }
        return 0;
    }

    // helper function that can be used to ask the user for either an X or an O and nothing else. this is used for assigning
    // players their symbol and for order and chaos, letting each player choose which symbol for each turn
    public static String getValidInputString(String[] possibleInputs){
        boolean validInput = false;
        String inputStr = "";
        while (!validInput){
            System.out.print("Input: ");
            inputStr = scan.next();
            if (Arrays.asList(possibleInputs).contains(inputStr)){
                break;
            }
            else {
                System.out.println(String.format("%s is not a valid input. Valid inputs are ", inputStr) + Arrays.toString(possibleInputs));
            }
            scan.nextLine();
        }
        return inputStr;
    }

    // helper function that gets a sting padded with spaces on both sides so that it is of size n
    public static String getStringWithNumChar(String s, int n) {
        char[] charArray = new char[(n - s.length()) / 2];
        Arrays.fill(charArray, ' ');
        String spacesBegin = new String(charArray);
        char[] charArray2 = new char[(n - s.length()) - charArray.length];
        Arrays.fill(charArray2, ' ');
        String spacesEnd = new String(charArray2);
        return spacesBegin + s + spacesEnd;
    }

    // helper function that prints side by side a hero's info and a list of controls passed in.
    // super useful since this is used multiple times across the codebase
    public static String getHeroAndControlsString(Hero h, List<String> controls) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|" + Utils.getStringWithNumChar("Current Selected Hero", 50) +
                Utils.getStringWithNumChar("+-------------------------------+", 50) + "|\n");
        Iterator<String> strHero = h.getString().iterator();
        Iterator<String> strControls = controls.iterator();
        boolean lastControl = true;

        while (strHero.hasNext() || lastControl) {
            if (strHero.hasNext()) {
                stringBuilder.append("|" + Utils.getStringWithNumChar(strHero.next(), 50));
            } else {
                stringBuilder.append("|" + Utils.getStringWithNumChar("", 50));
            }
            if (strControls.hasNext()) {
                stringBuilder.append(Utils.getStringWithNumChar(strControls.next(), 50) + "|\n");
            } else if (lastControl) {
                stringBuilder.append(Utils.getStringWithNumChar("+-------------------------------+", 50) + "|\n");
                lastControl = false;
            } else {
                stringBuilder.append(Utils.getStringWithNumChar("", 50) + "|\n");
            }
        }
        return stringBuilder.toString();
    }

    // helper function that prints side by side a monster's info and a fight log passed.
    // super useful since this is used multiple times and could be used elsewhere if project is extended
    public static String getMonsterAndFightLogString(Monster m, List<String> log) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|" + Utils.getStringWithNumChar("Current Monster", 25) +
                Utils.getStringWithNumChar("+----------------------------------------------------------------------+", 75) + "|\n");
        Iterator<String> strMonst = m.getString().iterator();
        Iterator<String> strLog = log.iterator();
        boolean lastControl = true;
        int i = 6;

        while (strMonst.hasNext() || lastControl) {
            if (strMonst.hasNext()) {
                stringBuilder.append("|" + Utils.getStringWithNumChar(strMonst.next(), 25));
            } else {
                stringBuilder.append("|" + Utils.getStringWithNumChar("", 25));
            }
            if (strLog.hasNext()) {
                stringBuilder.append(Utils.getStringWithNumChar(strLog.next(), 75) + "|\n");
            } else if (lastControl) {
                stringBuilder.append(Utils.getStringWithNumChar("+----------------------------------------------------------------------+", 75) + "|\n");
                lastControl = false;
            } else {
                stringBuilder.append(Utils.getStringWithNumChar("", 75) + "|\n");
            }
        }
        return stringBuilder.toString();
    }

    // helper function to label item as currently toggled or not
    // also labels if item is added or not by denoting with a char, such E if item in inventory is equipped
    public static String getMenuString(String s, boolean selected, boolean added, String addedChar) {
        if (selected)
            s = "=>" + s;
        else
            s = " +" + s;

        if (added)
            s += addedChar;
        else
            s += "+";

        return s;
    }
}

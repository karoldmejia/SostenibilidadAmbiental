package model;
import ui.Main;

/**
 * A utility class handling interactions between the system and the user.
 * It offers methods for input and output, allowing users to input strings and integers,
 * and display text output.
 */
public abstract class UserInteraction {
    /**
     * Method to retrieve a string input from the user.
     * @param input The message to display as input prompt.
     * @return The string entered by the user.
     */
    public static String getInputString(String input) {
        System.out.print(input);
        return Main.txt.nextLine();
    }

    /**
     * Method to retrieve an integer input from the user.
     * @param input The message to display as input prompt.
     * @return The integer entered by the user.
     */
    public static int getInputInt(String input) {
        System.out.print(input);
        return Integer.parseInt(Main.txt.nextLine());
    }

    /**
     * Method to display text output.
     * @param text The text to be displayed.
     */
    public static void showText(String text) {
        System.out.print(text);
    }
}

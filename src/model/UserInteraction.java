package model;
import ui.Main;

public abstract class UserInteraction {
    public static String getInputString(String input) {
        System.out.print(input);
        return Main.txt.nextLine();
    }
    public static int getInputInt(String input) {
        System.out.print(input);
        return Integer.parseInt(Main.txt.nextLine());
    }
    public static void showText(String text) {
        System.out.print(text);
    }
}

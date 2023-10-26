package ui;
import java.util.*;
import model.Controller;

public class Main {

    public static Scanner txt;
    static Controller controller;

    public static void main(String[] args) {
        txt = new Scanner(System.in);
        controller = new Controller();
    }



    public String getInputString(String input) {
        System.out.print(input);
        return Main.txt.nextLine();
    }

    public double getInputDouble(String input) {
        System.out.print(input);
        return Double.parseDouble(Main.txt.nextLine());
    }

    public int getInputInt(String input) {
        System.out.print(input);
        return Integer.parseInt(Main.txt.nextLine());
    }

    public void showText(String text) {
        System.out.print(text);
    }
}
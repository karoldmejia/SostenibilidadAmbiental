package ui;
import java.util.*;
import model.Controller;

public class Main {

    public static Scanner txt;
    static Controller controller;

    public static void main(String[] args) {
        txt = new Scanner(System.in);
        controller = new Controller();
        System.out.print("¡Bienvenido al departamento de Sostenibilidad ambiental ICESI!\n");
        credentialUser();
    }

    public static void credentialUser(){
        int optMenu = -1;
        System.out.print("Para acceder a nuestros servicios debe ingresar como usuario.");
        while (optMenu != 0) {
            System.out.print("\nDesea (0 to finish):\n1. Register user\n2. Log in\n");
            optMenu = Main.txt.nextInt();
            Main.txt.nextLine();
            switch (optMenu) {
                case 1:
                    controller.registerUser();
                    break;
                case 2:
                    controller.loginUser();
                    break;
                case 0:
                    System.out.print("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.print("Please enter a valid option");
                    break;
            }
        }
    }


    public void menuUser(int userType) {
        int optMenu = -1;
        while (optMenu != 0) {
            if (userType == 1 || userType == 2 || userType == 3) {
                showText("\nDesea (0 para ir a inicio):\n1. Visualizar mapa de puntos de interés\n");
            }
            if (userType == 2 || userType == 3) {
                showText("2. Crear una evidencia\n");
            }
            if (userType == 3) {
                showText("3. Registrar proyecto\n4. Consultar proyecto\n5. Modificar proyecto\n6. Eliminar proyecto\n7. Modificar evidencia\n8. Desactivar evidencia\n9. Revisar reseña\n10. Link data gatherers to project\n");
            }
            optMenu = Main.txt.nextInt();
            Main.txt.nextLine();
            if ((userType == 1 || userType == 2 || userType == 3) && optMenu == 1) {
                controller.visualizeMap();
            } else if ((userType == 2 || userType == 3) && optMenu == 2) {
                controller.createEvidence(userType);
            } else if (userType == 3 && optMenu >= 3 && optMenu <= 10) {
                switch (optMenu) {
                    case 3 -> controller.createProject();
                    case 4 -> controller.seeProject();
                    case 5 -> controller.updateProject();
                    case 6 -> controller.deleteProject();
                    case 7 -> controller.updateEvidence();
                    case 8 -> controller.deactivateEvidence();
                    case 9 -> controller.reviewReview();
                    case 10 -> controller.reviewReview();
                }
            } else if (optMenu == 0) {
                showText("Redirigiendo a inicio... ");
                credentialUser();
            } else {
                showText("Please, select a valid option\n");
            }
        }
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
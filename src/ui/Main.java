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
    public static void menuUser(int userType) {
        int optMenu = -1;
        while (optMenu != 0) {
            if (userType == 1 || userType == 2 || userType == 3) {
                System.out.println("\nDesea (0 para ir a inicio):\n1. Visualizar mapa de puntos de interés\n");
            }
            if (userType == 2 || userType == 3) {
                System.out.println("2. Crear una evidencia\n");
            }
            if (userType == 3) {
                System.out.println("3. Registrar proyecto\n4. Consultar proyecto\n5. Modificar proyecto\n6. Eliminar proyecto\n7. Modificar evidencia\n8. Desactivar evidencia\n9. Revisar reseña\n10. Link data gatherers to project\n");
            }
            optMenu = Main.txt.nextInt();
            Main.txt.nextLine();
            if ((userType == 1 || userType == 2 || userType == 3) && optMenu == 1) {
                controller.visualizeMap();
            } else if ((userType == 2 || userType == 3) && optMenu == 2) {
                controller.createEvidence(userType);
            } else if (userType == 3 && optMenu >= 3 && optMenu <= 10) {
                switch (optMenu) {
                    case 3:
                        controller.createProject();
                        break;
                    case 4:
                        controller.seeProject();
                        break;
                    case 5:
                        controller.updateProject();
                        break;
                    case 6:
                        controller.deleteProject();
                        break;
                    case 7:
                        controller.updateEvidence();
                        break;
                    case 8:
                        controller.deactivateEvidence();
                        break;
                    case 9:
                        controller.reviewEvidences();
                        break;
                    case 10:
                        controller.linkDataGatherersToProject();
                        break;
                }
            } else if (optMenu == 0) {
                System.out.println("Redirigiendo a inicio... ");
                credentialUser();
            } else {
                System.out.println("Please, select a valid option\n");
            }
        }
    }
}
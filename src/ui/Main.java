package ui;
import java.util.*;
import model.Controller;

public class Main {

    public static Scanner txt;
    static Controller controller;

    /**
     * Initializes the application and prompts user for credentials to access services.
     * Displays a menu to register or log in as a user.
     *
     * @param args The command line arguments provided to the program.
     * @pre None.
     * @post The user either registers, logs in, or exits the system.
     */
    public static void main(String[] args) {
        txt = new Scanner(System.in);
        controller = new Controller();
        System.out.print("Welcome to the ICESI Environmental Sustainability Department!\n");
        credentialUser();
    }

    /**
     * Manages the user authentication process by displaying a menu for user actions.
     * Allows the user to register, log in, or exit the system.
     *
     * @pre None.
     * @post User either registers, logs in, or exits the system.
     */
    public static void credentialUser(){
        int optMenu = -1;
        System.out.print("To access our services, you need to log in as a user.");
        while (optMenu != 0) {
            System.out.print("\nDo you wish to (0 to finish):\n1. Register user\n2. Log in\n");
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

    /**
     * Manages the user interface based on the user type by displaying different menu options.
     * Depending on the user type, enables access to various functionalities.
     *
     * @param userType An integer indicating the type of user (1, 2, or 3).
     * @pre `userType` must be a valid user type (1, 2, or 3).
     * @post User performs actions based on the displayed menu.
     */
    public static void menuUser(int userType) {
        int optMenu = -1;
        while (optMenu != 0) {
            if (userType == 1 || userType == 2 || userType == 3) {
                System.out.println("\nDo you wish to (0 to return to the beginning):\n1. View map of points of interest");
            }
            if (userType == 2 || userType == 3) {
                System.out.println("2. Create an evidence");
            }
            if (userType == 3) {
                System.out.println("3. Modify evidence\n4. Activate or deactivate evidence\n5. Register project\n6. Consult project\n7. Modify projecto\n8. Delete project\n9. Review review\n10. Link data gatherers to project\n11. Modify points of interest\n12. Delete points of interest");
            }
            optMenu = Main.txt.nextInt();
            Main.txt.nextLine();
            if ((userType == 1 || userType == 2 || userType == 3) && optMenu == 1) {
                controller.visualizeMap();
            } else if ((userType == 2 || userType == 3) && optMenu == 2) {
                controller.createEvidence(userType);
            } else if (userType == 3 && optMenu >= 3 && optMenu <= 12) {
                switch (optMenu) {
                    case 3:
                        controller.updateEvidence();
                        break;
                    case 4:
                        controller.deactivateEvidence();
                        break;
                    case 5:
                        controller.createProject();
                        break;
                    case 6:
                        controller.seeProject();
                        break;
                    case 7:
                        controller.updateProject();
                        break;
                     case 8:
                        controller.deleteProject();
                        break;
                    case 9:
                        controller.reviewEvidences();
                        break;
                    case 10:
                        controller.linkDataGatherersToProject();
                        break;
                    case 11:
                        controller.updateInterestPoint();
                        break;
                    case 12:
                        controller.deleteInterestPoint();
                        break;

                }
            } else if (optMenu == 0) {
                System.out.println("Redirecting to the homepage... ");
                credentialUser();
            } else {
                System.out.println("Please, select a valid option\n");
            }
        }
    }
}
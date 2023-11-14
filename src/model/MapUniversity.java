package model;
import java.util.ArrayList;
import java.util.Random;

/**
 * MapUniversity class manages the visualization and manipulation of the university map,
 * allowing users to filter and navigate through various points of interest represented
 * on the map, such as projects and their locations, and enabling interaction
 * through point selection, review, and update operations.
 */
public class MapUniversity {
    private static InterestPoint[][] map = new InterestPoint[20][20];

    // Main methods --------------------------------------------

    /**
     * Adds an interest point to the map grid.
     *
     * @param point The interest point to be added.
     * @pre The `point` should not have `x` and `y` values beyond the grid range [0, 20).
     * @post The interest point is added to the map.
     */
    public static void addInterestPoint(InterestPoint point) {
        int x = point.getxAxis();
        int y = point.getyAxis();
        if (x >= 0 && x < 20 && y >= 0 && y < 20) {
            if (map[x][y] == null) {
                map[x][y] = point;
            } else {
                InterestPoint existentPoint = map[x][y];
                for (EvidenceProject evidence : point.getEvidences()) {
                    existentPoint.addEvidence(evidence);
                }
            }
        } else {
            UserInteraction.showText("The values entered are outside map range");
        }
    }

    /**
     * Displays the map grid with project evidence information.
     *
     * @param projects The list of projects.
     * @pre The `projects` parameter should not be null.
     * @post The map is displayed with project evidence information.
     */
    public static void showMap(ArrayList<Project> projects) {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                InterestPoint point = map[row][col];
                if (point != null) {
                    StringBuilder projectEvidences = new StringBuilder();
                    for (Project project : projects) {
                        for (EvidenceProject pointEvidence : point.getEvidences()) {
                            if (pointEvidence != null && pointEvidence.getAvailability()) {
                                String projectName = ProjectManagementService.findEvidenceProject(pointEvidence);
                                if (projectName != null && projectName.equals(project.getNameProject())) {
                                    if (pointEvidence instanceof Review && ((Review) pointEvidence).evidenceStatus==true){
                                        projectEvidences.append(pointEvidence.getTypeEvidence()).append(" ");
                                    } else if (pointEvidence instanceof Evidence) {
                                        projectEvidences.append(pointEvidence.getTypeEvidence()).append(" ");
                                    }
                                }
                            }
                        }
                    }
                    String result = projectEvidences.toString().trim();
                    System.out.print("[" + result + "]");
                } else {
                    System.out.print("[   ]");
                }
            }
            System.out.println();
        }
    }

    /**
     * Deletes the interest point at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @pre The `x` and `y` coordinates should be within the grid range [0, 20).
     * @post The interest point is removed from the map grid.
     */
    public static void deletePoint(int x, int y) {
        InterestPoint locationPoint = getInterestPoint(x,y);
        if (locationPoint != null) {
            locationPoint=null;
            map[x][y]=null;
            UserInteraction.showText("The interest's point has been removed\n");
        }
    }

    /**
     * Updates information about the interest point at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @pre The `x` and `y` coordinates should be within the grid range [0, 20).
     * @post The information for the interest point at the specified coordinates is updated.
     */
    protected static void updatePoint(int x, int y) {
        InterestPoint point = getInterestPoint(x, y);
        if (point == null || !point.hasApprovedEvidences()) {
            return;
        }
        while (true) {
            int opcion = UserInteraction.getInputInt("Select the attribute to modify (0 to finish and apply changes):\n1. Name\n2. Coordinates\n3. QR code\n");
            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1:
                    updateNamePoint(point);
                    break;
                case 2:
                    updateCoordinates(point);
                    break;
                case 3:
                    updateQR(point);
                    break;
                default:
                    System.out.print("Please enter a valid option");
                    break;
            }
        }
        UserInteraction.showText("Changes applied successfully.");
    }

    // Support methods --------------------------------------------

    /**
     * Retrieves the interest point at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The interest point if found; otherwise, null.
     * @pre The `x` and `y` coordinates should be within the grid range [0, 20).
     * @post None.
     */
    protected static InterestPoint getInterestPoint(int x, int y) {
        InterestPoint point = null;
        if (x >= 0 && x < 20 && y >= 0 && y < 20) {
            point=map[x][y];
        }
        if (point!=null) {
            return point;
        }else{
            UserInteraction.showText("We couldn't find an interest's point there!\n");
            return null;
        }
    }

    /**
     * Generates a random QR code.
     *
     * @return The randomly generated QR code.
     * @pre None.
     * @post A QR code is generated.
     */
    public static String createQR() {
        String caracteres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder qr = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int indice = random.nextInt(caracteres.length());
            char caracterAleatorio = caracteres.charAt(indice);
            qr.append(caracterAleatorio);
        }
        return qr.toString();
    }

    /**
     * Validates the uniqueness of the generated QR code.
     *
     * @param qr The QR code to be validated.
     * @return True if the QR code is valid; otherwise, false.
     * @pre The `qr` should not be null.
     * @post None.
     */
    public static boolean isQRValid(String qr) {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                if (map[row][col] != null) {
                    String codeQr = map[row][col].getCodeQr();
                    if (codeQr != null && qr.equals(codeQr)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Updates the name of the interest point.
     *
     * @param point The interest point to be updated.
     * @pre The `point` should not be null.
     * @post The name of the interest point is updated.
     */
    private static void updateNamePoint(InterestPoint point){
        String namePoint=UserInteraction.getInputString("Enter the new point's name:");
        point.setNamePoint(namePoint);
    }

    /**
     * Updates the coordinates of the interest point.
     *
     * @param point The interest point to be updated.
     * @pre The `point` should not be null.
     * @post The coordinates of the interest point are updated.
     */
    private static void updateCoordinates(InterestPoint point){
        int x = UserInteraction.getInputInt("Enter the new x-coordinate: ");
        int y = UserInteraction.getInputInt("Enter the new y-coordinate: ");
        InterestPoint otherPoint = getInterestPoint(x,y);
        if (point!=null && otherPoint==null){
            map[point.getxAxis()][point.getyAxis()]=null;
            point.setxAxis(x);
            point.setyAxis(y);
            map[x][y]=point;
        } else {
            UserInteraction.showText("Already exists a point in this location\n");
        }
    }

    /**
     * Updates the QR code of the interest point.
     *
     * @param point The interest point to be updated.
     * @pre The `point` should not be null.
     * @post The QR code of the interest point is updated.
     */
    private static void updateQR(InterestPoint point){
        String codeQR = MapUniversity.createQR();
        while (!MapUniversity.isQRValid(codeQR)){
            codeQR = MapUniversity.createQR();
            MapUniversity.isQRValid(codeQR);
        }
        point.setCodeQr(codeQR);
        UserInteraction.showText("Your new code is: "+codeQR+"\n");
    }

    /**
     * Displays the information of the interest point at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @pre The `x` and `y` coordinates should be within the grid range [0, 20).
     * @post The information for the interest point at the specified coordinates is displayed.
     */
    public static void showPoint(int x, int y) {
        InterestPoint point = getInterestPoint(x, y);
        String addComment = "o";
        if (point != null && point.hasApprovedEvidences()) {
            UserInteraction.showText("- Interest point name: " + point.getNamePoint() + "\n");
            UserInteraction.showText("- Code QR: " + point.getCodeQr() + "\n\n");
            point.showEvidences();
            point.showComments(point);
            while (!(addComment.equalsIgnoreCase("y"))) {
                addComment = UserInteraction.getInputString("Do you want to add a comment? Enter 'y' to confirm, and any other letter to exit\n");
                if (addComment.equalsIgnoreCase("y")) {
                    point.addComment(point);
                } else {
                    break;
                }
            }
        }
    }




}

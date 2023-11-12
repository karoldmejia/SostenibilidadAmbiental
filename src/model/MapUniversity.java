package model;
import java.util.ArrayList;
import java.util.Random;

public class MapUniversity {
    private static InterestPoint[][] map = new InterestPoint[20][20];

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
            UserInteraction.showText("Los valores ingresados se encuentran fuera del rango del mapa");
        }
    }

    /**
     * Retrieves the interest point at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The interest point if found; otherwise, null.
     * @pre The `x` and `y` coordinates should be within the grid range [0, 20).
     * @post None.
     */
    public static InterestPoint getInterestPoint(int x, int y) {
        InterestPoint point = null;
        if (x >= 0 && x < 20 && y >= 0 && y < 20) {
            point=map[x][y];
        }
        if (point!=null) {
            return point;
        }else{
            UserInteraction.showText("No encontramos un punto de interés en esa ubicación!\n");
            return null;
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
            UserInteraction.showText("El punto de interés ha sido removido\n");
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
    public static void updatePoint(int x, int y) {
        InterestPoint point = getInterestPoint(x, y);
        if (point == null) {
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

    /**
     * Updates the name of the interest point.
     *
     * @param point The interest point to be updated.
     * @pre The `point` should not be null.
     * @post The name of the interest point is updated.
     */
    private static void updateNamePoint(InterestPoint point){
        String namePoint=UserInteraction.getInputString("Ingresa el nuevo nombre para el punto: ");
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
        int x = UserInteraction.getInputInt("Ingresa la nueva coordenada x: ");
        int y = UserInteraction.getInputInt("Ingresa la nueva coordenada y: ");
        InterestPoint otherPoint = getInterestPoint(x,y);
        if (point!=null && otherPoint==null){
            map[point.getxAxis()][point.getyAxis()]=null;
            point.setxAxis(x);
            point.setyAxis(y);
            map[x][y]=point;
        } else {
            UserInteraction.showText("Ya existe un punto en esta ubicación!\n");
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
        UserInteraction.showText("Tu nuevo código es: "+codeQR+"\n");
    }

    /**
     * Displays the information of the interest point at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @pre The `x` and `y` coordinates should be within the grid range [0, 20).
     * @post The information for the interest point at the specified coordinates is displayed.
     */
    public static void showPoint(int x, int y){
        InterestPoint point = getInterestPoint(x,y);
        if (point!=null){
            UserInteraction.showText("- Interest point name: "+point.getNamePoint()+"\n");
            UserInteraction.showText("- Code QR: "+point.getCodeQr()+"\n");
            point.showEvidences();
        }
    }


}

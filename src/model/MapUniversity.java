package model;
import java.util.Random;

public class MapUniversity {
    private static InterestPoint[][] map = new InterestPoint[20][20];

    protected static void addInterestPoint(InterestPoint point) {
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

    protected static InterestPoint getInterestPoint(int x, int y) {
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

    protected static void showMap(Project project) {
        ProjectManagementService projectService = new ProjectManagementService();
        boolean flag = false;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                InterestPoint point = map[row][col];
                if (point != null) {
                    StringBuilder projectEvidences = new StringBuilder();
                    for (EvidenceProject evidence : point.getEvidences()) {
                        if (evidence != null && evidence.getAvailability()) {
                            String projectName = projectService.findEvidenceProject(evidence);
                            if (projectName != null && projectName.equals(project.getNameProject())) {
                                projectEvidences.append(evidence.getTypeEvidence()).append(" ");
                                flag = true;
                            }
                        }
                    }
                    if (flag) {
                        String result = projectEvidences.toString().trim();
                        System.out.print("[" + result + "]");
                    } else {
                        System.out.print("[   ]");
                    }
                } else {
                    System.out.print("[   ]");
                }
            }
            System.out.println();
        }
    }

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

    protected static boolean isQRValid(String qr) {
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
    protected static void deletePoint(int x, int y) {
        InterestPoint locationPoint = getInterestPoint(x,y);
        if (locationPoint != null) {
            locationPoint=null;
            map[x][y]=null;
            UserInteraction.showText("El punto de interés ha sido removido\n");
        }
    }
    protected static void updatePoint(int x, int y) {
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

    private static void updateNamePoint(InterestPoint point){
        String namePoint=UserInteraction.getInputString("Ingresa el nuevo nombre para el punto: ");
        point.setNamePoint(namePoint);
    }
    private static void updateCoordinates(InterestPoint point){
        int x = UserInteraction.getInputInt("Ingresa la nueva coordenada x: ");
        int y = UserInteraction.getInputInt("Ingresa la nueva coordenada y: ");
        InterestPoint otherPoint = getInterestPoint(x,y);
        if (otherPoint==null){
            point=map[x][y];
            point.setxAxis(x);
            point.setyAxis(y);
        } else {
            UserInteraction.showText("Ya existe un punto en esta ubicación!\n");
        }
    }
    private static void updateQR(InterestPoint point){
        String codeQR = MapUniversity.createQR();
        while (!MapUniversity.isQRValid(codeQR)){
            codeQR = MapUniversity.createQR();
            MapUniversity.isQRValid(codeQR);
        }
        point.setCodeQr(codeQR);
        UserInteraction.showText("Tu nuevo código es: "+codeQR+"\n");
    }

    protected static void showPoint(int x, int y){
        InterestPoint point = getInterestPoint(x,y);
        if (point!=null){
            UserInteraction.showText("- Interest point name: "+point.getNamePoint()+"\n");
            UserInteraction.showText("- Code QR: "+point.getCodeQr()+"\n");
            point.showEvidences();
        }
    }


}

package model;

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
        if (x >= 0 && x < 20 && y >= 0 && y < 20) {
            return map[x][y];
        } else {
            return null;
        }
    }

    protected static void showMap() {
        boolean flag = false;
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                InterestPoint point = map[row][col];
                if (point != null) {
                    StringBuilder allEvidences = new StringBuilder();
                    for (EvidenceProject evidence : point.getEvidences()) {
                        if (evidence.getAvailability()) {
                            allEvidences.append(evidence.getTypeEvidence()).append(" ");
                            flag = true;
                        }
                    }
                    if (flag) {
                        String result = allEvidences.toString().trim();
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
    protected static String getAllPointsNames() {
        StringBuilder pointsNames = new StringBuilder();
        boolean first = true;
        for (InterestPoint[] pointRow : map) {
            for (InterestPoint point : pointRow) {
                if (point != null) {
                    if (!first) {
                        pointsNames.append(", ");
                    }
                    pointsNames.append(point.getNameInterestPoint());
                    first = false;
                }
            }
        }
        return pointsNames.toString();
    }

    protected static void deletePoint(String idPoint) {
        int[] locationPoint = searchPointLocation(idPoint);
        if (locationPoint != null) {
            map[locationPoint[0]][locationPoint[1]] = null;
            UserInteraction.showText("El punto de interés ha sido removido\n");
        } else {
            UserInteraction.showText("El punto de interés no fue encontrado :(\n");
        }
    }

    private static int[] searchPointLocation(String idPoint) {
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                if (map[row][col] != null && idPoint.equalsIgnoreCase(map[row][col].getNameInterestPoint())) {
                    int[] locationPoint = new int[2];
                    locationPoint[0] = row;
                    locationPoint[1] = col;
                    return locationPoint;
                }
            }
        }
        return null;
    }
}

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
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                InterestPoint point = map[row][col];
                if (point != null) {
                    System.out.print("[" + point.getNameInterestPoint() + "]");
                } else {
                    System.out.print("[   ]");
                }
            }
            System.out.println();
        }
    }
}

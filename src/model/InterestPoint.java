package model;

import java.util.ArrayList;

public class InterestPoint {
    String nameInterestPoint;
    int xAxis;
    int yAxis;
    String codeQr;
    private ArrayList<EvidenceProject> evidences;

    InterestPoint(String nameInterestPoint,int xAxis,int yAxis, String codeQr){
        this.nameInterestPoint=nameInterestPoint;
        this.xAxis=xAxis;
        this.yAxis=yAxis;
        this.codeQr=codeQr;
        this.evidences = new ArrayList<>();
    }

    // Getters ----------


    public String getNameInterestPoint() {
        return nameInterestPoint;
    }

    public int getxAxis() {
        return xAxis;
    }
    public int getyAxis() {
        return yAxis;
    }
    public ArrayList<EvidenceProject> getEvidences() {
        return evidences;
    }
    public void addEvidence(EvidenceProject evidence) {
        evidences.add(evidence);
    }


}

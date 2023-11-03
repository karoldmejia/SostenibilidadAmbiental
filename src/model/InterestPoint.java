package model;

import java.util.ArrayList;

public class InterestPoint {
    String namePoint;
    int xAxis;
    int yAxis;
    String codeQr;
    private ArrayList<EvidenceProject> evidences;
    private ArrayList<String> comments;
    private ArrayList<User> userComments;

    InterestPoint(String namePoint,int xAxis,int yAxis, String codeQr){
        this.namePoint=namePoint;
        this.xAxis=xAxis;
        this.yAxis=yAxis;
        this.codeQr=codeQr;
        this.evidences = new ArrayList<>();
    }

    // Getters ----------

    public String getNamePoint() {
        return namePoint;
    }

    public int getxAxis() {
        return xAxis;
    }
    public int getyAxis() {
        return yAxis;
    }

    public String getCodeQr() {
        return codeQr;
    }
    public ArrayList<EvidenceProject> getEvidences() {
        return evidences;
    }

    // Setters ----------

    public void setNamePoint(String namePoint) {
        this.namePoint = namePoint;
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public void setCodeQr(String codeQr) {
        this.codeQr = codeQr;
    }

    public void addEvidence(EvidenceProject evidence) {
        evidences.add(evidence);
    }
    protected void showEvidences(){
        int counter=1;
        String typeEvidence = null;
        for (EvidenceProject evidence : getEvidences()){
            UserInteraction.showText(counter+". Evidence: "+evidence.getNameEvidence()+"\n");
            UserInteraction.showText(" Registration date: "+evidence.getRegisterDate()+"\n");
            String project = ProjectManagementService.findEvidenceProject(evidence);
            UserInteraction.showText(" Proyecto perteneciente: "+project+"\n");
            if (evidence instanceof Evidence) {
                if (evidence.getTypeEvidence() == CharTypeEvidence.A) {
                    typeEvidence = "Audio";
                } else if (evidence.getTypeEvidence() == CharTypeEvidence.V) {
                    typeEvidence = "Video";
                } else if (evidence.getTypeEvidence() == CharTypeEvidence.P) {
                    typeEvidence = "Photo";
                } else if (evidence.getTypeEvidence() == CharTypeEvidence.T) {
                    typeEvidence = "Text";
                } else if (evidence.getTypeEvidence() == CharTypeEvidence.RR) {
                    typeEvidence = "Results report";
                }
                UserInteraction.showText(" Tipo de archivo: "+typeEvidence+"\n");
                UserInteraction.showText(" URL: "+((Evidence) evidence).getUrl()+"\n");
            } else if (evidence instanceof Review){
                UserInteraction.showText(" Tipo de archivo: Reseña\n");
                UserInteraction.showText(" URL's:\n");
                for (int i = 0; i < ((Review) evidence).getListUrl().length; i++) {
                    UserInteraction.showText(" "+(i+1) + ". " + ((Review) evidence).getListUrl()[i]+"\n");
                }
            }
            counter++;
        }
    }


}

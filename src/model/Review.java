package model;

public class Review extends EvidenceProject{

    boolean evidenceStatus;
    String[] listUrl;
    Review(String nameEvidence, String registerDate, boolean availability, CharTypeEvidence typeEvidence, boolean evidenceStatus, String[] listUrl) {
        super(nameEvidence, registerDate, availability, typeEvidence);
        this.evidenceStatus=evidenceStatus;
        this.listUrl=listUrl;
    }

    public boolean getEvidenceStatus(){
        return evidenceStatus;
    }
    public String[] getListUrl() {
        return listUrl;
    }

    public void setEvidenceStatus(boolean evidenceStatus) {
        this.evidenceStatus = evidenceStatus;
    }
}

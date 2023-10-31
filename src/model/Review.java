package model;

public class Review extends EvidenceProject{

    boolean evidenceStatus;
    String[] listUrl;
    Review(Project project, String nameEvidence, String registerDate, boolean availability, boolean evidenceStatus, String[] listUrl) {
        super(project, nameEvidence, registerDate, availability);
        this.evidenceStatus=evidenceStatus;
        this.listUrl=listUrl;
    }

    public boolean getEvidenceStatus(){
        return evidenceStatus;
    }
    public String[] getListUrl() {
        return listUrl;
    }
}

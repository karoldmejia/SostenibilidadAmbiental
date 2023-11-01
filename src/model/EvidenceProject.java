package model;

public abstract class EvidenceProject {
    int idEvidence=0;
    String nameEvidence;
    String registerDate;
    boolean availability;
    InterestPoint interestPoint;

    EvidenceProject(String nameEvidence,String registerDate, boolean availability){
        idEvidence+=1;
        this.nameEvidence=nameEvidence;
        this.registerDate=registerDate;
        this.availability=availability;
    }

    // Getters
    public String getNameEvidence() {
        return nameEvidence;
    }
    public boolean getAvailability(){
        return availability;
    }

    // Setters
    public void setNameEvidence(String nameEvidence) {
        this.nameEvidence = nameEvidence;
    }
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}

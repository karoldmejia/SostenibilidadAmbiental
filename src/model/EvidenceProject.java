package model;
import java.util.ArrayList;

public abstract class EvidenceProject {
    int idEvidence=0;
    String nameEvidence;
    String registerDate;
    boolean availability;
    CharTypeEvidence typeEvidence;

    EvidenceProject(String nameEvidence,String registerDate, boolean availability, CharTypeEvidence typeEvidence){
        idEvidence+=1;
        this.nameEvidence=nameEvidence;
        this.registerDate=registerDate;
        this.availability=availability;
        this.typeEvidence=typeEvidence;
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

package model;
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

    public int getIdEvidence() {
        return idEvidence;
    }

    public String getNameEvidence() {
        return nameEvidence;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public boolean getAvailability(){
        return availability;
    }
    public CharTypeEvidence getTypeEvidence() {
        return typeEvidence;
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

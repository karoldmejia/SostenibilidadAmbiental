package model;

public abstract class EvidenceProject {
    int idEvidence=0;
    Project project;
    String nameEvidence;
    String registerDate;
    boolean availability;

    EvidenceProject(Project project,String nameEvidence,String registerDate, boolean availability){
        idEvidence+=1;
        this.project=project;
        this.nameEvidence=nameEvidence;
        this.registerDate=registerDate;
        this.availability=availability;
    }

    // Getters
    public String getNameEvidence() {
        return nameEvidence;
    }

    public Project getProject() {
        return project;
    }

    public int getIdEvidence() {
        return idEvidence;
    }

    // Setters
    public void setNameEvidence(String nameEvidence) {
        this.nameEvidence = nameEvidence;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}

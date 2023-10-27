package model;

public abstract class EvidenceProject {
    int idEvidence=0;
    Project project;
    String nameEvidence;
    InterestPoint interestPoint;
    String registerDate;

    EvidenceProject(Project project,String nameEvidence,InterestPoint interestPoint,String registerDate){
        idEvidence+=1;
        this.project=project;
        this.nameEvidence=nameEvidence;
        this.interestPoint=interestPoint;
        this.registerDate=registerDate;
    }

    public String getNameEvidence() {
        return nameEvidence;
    }
}

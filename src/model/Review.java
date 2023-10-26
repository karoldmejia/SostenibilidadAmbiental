package model;

public class Review extends EvidenceProject{

    boolean evidenceStatus;
    String[] listUrl;
    Review(Project project, String nameEvidence, InterestPoint interestPoint, String registerDate,boolean evidenceStatus) {
        super(project, nameEvidence, interestPoint, registerDate);
        this.evidenceStatus=evidenceStatus;
        listUrl=new String[10];
    }
}

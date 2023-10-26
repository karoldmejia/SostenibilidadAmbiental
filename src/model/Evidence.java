package model;

public class Evidence extends EvidenceProject{
    String url;

    Evidence(Project project, String nameEvidence, InterestPoint interestPoint, String registerDate, String url) {
        super(project, nameEvidence, interestPoint, registerDate);
        this.url=url;
    }
}

package model;

public class Evidence extends EvidenceProject{
    String url;

    Evidence(Project project, String nameEvidence, String registerDate, boolean availability, String url) {
        super(project, nameEvidence, registerDate, availability);
        this.url=url;
    }

    // Setters

    public void setUrl(String url) {
        this.url = url;
    }
}

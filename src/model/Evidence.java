package model;

public class Evidence extends EvidenceProject{
    String url;

    Evidence(String nameEvidence, String registerDate, boolean availability, String url) {
        super(nameEvidence, registerDate, availability);
        this.url=url;
    }

    // Setters

    public void setUrl(String url) {
        this.url = url;
    }
}

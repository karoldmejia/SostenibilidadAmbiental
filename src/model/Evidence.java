package model;

public class Evidence extends EvidenceProject{
    String url;

    Evidence(String nameEvidence, String registerDate, boolean availability, CharTypeEvidence typeEvidence, String url) {
        super(nameEvidence, registerDate, availability, typeEvidence);
        this.url=url;
    }

    // Getters
    public String getUrl() {
        return url;
    }

    // Setters
    public void setUrl(String url) {
        this.url = url;
    }
}

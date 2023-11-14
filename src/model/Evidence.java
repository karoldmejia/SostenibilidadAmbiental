package model;

/**
 * Represents a common evidence with a single url
 */
public class Evidence extends EvidenceProject{
    String url;

    /**
     * Constructs an Evidence object with the specified attributes.
     * @param nameEvidence The name of the evidence.
     * @param registerDate The registration date of the evidence.
     * @param availability The availability status of the evidence.
     * @param typeEvidence The type of evidence.
     * @param url The URL associated with the evidence.
     */
    Evidence(String nameEvidence, String registerDate, boolean availability, CharTypeEvidence typeEvidence, String url) {
        super(nameEvidence, registerDate, availability, typeEvidence);
        this.url=url;
    }

    // Getters

    /**
     * Retrieves the URL of the evidence.
     * @return The URL of the evidence.
     */
    public String getUrl() {
        return url;
    }

    // Setters

    /**
     * Sets the URL for the evidence.
     * @param url The URL to be associated with the evidence.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}

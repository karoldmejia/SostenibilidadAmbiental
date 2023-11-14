package model;

/**
 * Represents a review, a specific type of evidence. Contains additional attributes like evidence status and a list of URLs.
 */
public class Review extends EvidenceProject{

    boolean evidenceStatus;
    String[] listUrl;

    /**
     * Constructs a Review object with specific attributes.
     * @param nameEvidence The name of the evidence.
     * @param registerDate The registration date of the evidence.
     * @param availability The availability status of the evidence.
     * @param typeEvidence The type of evidence.
     * @param evidenceStatus The status of the evidence.
     * @param listUrl List of URLs associated with the evidence.
     */
    Review(String nameEvidence, String registerDate, boolean availability, CharTypeEvidence typeEvidence, boolean evidenceStatus, String[] listUrl) {
        super(nameEvidence, registerDate, availability, typeEvidence);
        this.evidenceStatus=evidenceStatus;
        this.listUrl=listUrl;
    }

    // Getters

    /**
     * Retrieves the status of the evidence.
     * @return The status of the evidence.
     */
    public boolean getEvidenceStatus() {
        return evidenceStatus;
    }

    /**
     * Retrieves the list of URLs associated with the evidence.
     * @return The list of URLs associated with the evidence.
     */
    public String[] getListUrl() {
        return listUrl;
    }

    // Setters

    /**
     * Sets the status of the evidence.
     * @param evidenceStatus The status to be set for the evidence.
     */
    public void setEvidenceStatus(boolean evidenceStatus) {
        this.evidenceStatus = evidenceStatus;
    }
}

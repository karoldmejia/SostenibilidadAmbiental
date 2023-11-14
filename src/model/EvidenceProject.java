package model;

/**
 * Represents the base class for evidences. Contains attributes such as evidence ID, name, registration date, availability, and evidence type.
 */
public abstract class EvidenceProject {
    private static int nextId = 1;
    int idEvidence;
    String nameEvidence;
    String registerDate;
    boolean availability;
    CharTypeEvidence typeEvidence;

    /**
     * Constructs an EvidenceProject object with specific attributes.
     * @param nameEvidence The name of the evidence.
     * @param registerDate The registration date of the evidence.
     * @param availability The availability status of the evidence.
     * @param typeEvidence The type of evidence.
     */
    EvidenceProject(String nameEvidence,String registerDate, boolean availability, CharTypeEvidence typeEvidence){
        idEvidence=nextId++;
        this.nameEvidence=nameEvidence;
        this.registerDate=registerDate;
        this.availability=availability;
        this.typeEvidence=typeEvidence;
    }

    // Getters

    /**
     * Obtains the unique ID of the evidence.
     * @return The evidence ID.
     */
    public int getIdEvidence() {
        return idEvidence;
    }

    /**
     * Retrieves the name of the evidence.
     * @return The name of the evidence.
     */
    public String getNameEvidence() {
        return nameEvidence;
    }

    /**
     * Retrieves the registration date of the evidence.
     * @return The registration date of the evidence.
     */
    public String getRegisterDate() {
        return registerDate;
    }

    /**
     * Checks the availability status of the evidence.
     * @return The availability status of the evidence.
     */
    public boolean getAvailability(){
        return availability;
    }

    /**
     * Gets the type of evidence (audio, video, photo, text, results report).
     * @return The type of evidence.
     */
    public CharTypeEvidence getTypeEvidence() {
        return typeEvidence;
    }

    // Setters

    /**
     * Sets the name of the evidence.
     * @param nameEvidence The name of the evidence.
     */
    public void setNameEvidence(String nameEvidence) {
        this.nameEvidence = nameEvidence;
    }

    /**
     * Sets the registration date for the evidence.
     * @param registerDate The registration date of the evidence.
     */
    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * Sets the availability status of the evidence.
     * @param availability The availability status of the evidence.
     */
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }


}

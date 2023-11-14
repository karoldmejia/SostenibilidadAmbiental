package model;

/**
 * Represents a researcher, extending the DataGatherer class.
 */
public class Researcher extends DataGatherer{

    String universityArea;
    String position;

    /**
     * Constructs a Researcher with the provided username, password, personal details, university area, and position.
     * @param username The username for the Researcher.
     * @param password The password for the Researcher.
     * @param fullName The full name of the Researcher.
     * @param email The email of the Researcher.
     * @param phone The phone number of the Researcher.
     * @param universityArea The area in the university where the Researcher belongs.
     * @param position The position of the Researcher.
     */
    Researcher(String username, String password, String fullName, String email, String phone, String universityArea, String position) {
        super(username, password, fullName, email, phone);
        this.universityArea=universityArea;
        this.position=position;
    }


}

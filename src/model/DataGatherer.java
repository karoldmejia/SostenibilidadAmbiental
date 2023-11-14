package model;

/**
 * Represents a data gatherer, a type of user in the system who can post reviews.
 */
public class DataGatherer extends Visitor{

    String fullName;
    String email;
    String phone;

    /**
     * Constructs a data gatherer with the provided information.
     * @param username The username for the data gatherer.
     * @param password The password for the data gatherer.
     * @param fullName The full name of the data gatherer.
     * @param email The email address of the data gatherer.
     * @param phone The phone number of the data gatherer.
     */
    DataGatherer(String username, String password, String fullName, String email, String phone) {
        super(username, password);
        this.fullName=fullName;
        this.email=email;
        this.phone=phone;
    }
}

package model;

/**
 * Represents a visitor, a type of user in the system.
 */
public class Visitor extends User{

    /**
     * Constructs a visitor with the provided username and password.
     * @param username The username for the visitor.
     * @param password The password for the visitor.
     */
    Visitor(String username, String password){
        super(username,password);
    }


}

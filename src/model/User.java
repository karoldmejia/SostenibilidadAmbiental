package model;

/**
 * Represents a user, serving as the base class for various user types.
 */
public abstract class User {
    private static int nextId = 0;
    int idUser;
    String username;
    String password;

    /**
     * Initializes a user with a unique identifier, a username, and a password.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    User(String username, String password){
        this.idUser = nextId++;
        this.username=username;
        this.password=password;
    }

    /**
     * Retrieves the unique identifier of the user.
     * @return The user's unique identifier.
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * Retrieves the username of the user.
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }
}

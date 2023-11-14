package model;
import ui.Main;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The UserCredentialService class is responsible for managing user credentials and authentication.
 * It provides methods for user registration, login, and user-related operations.
 */
public class UserCredentialService{

    private static ArrayList<User> userList=new ArrayList<>();
    private static int loggedInUserId;
    private String username, password, fullname, email, phone, universityArea, position;

    /**
     * Initializes the user list with default instances of Visitor, DataGatherer, and Researcher.
     * @pre None.
     * @post The user list contains default instances of different user types.
     */
    public void initializeUsers() {
        userList.add(new Visitor("dd","monomono"));
        userList.add(new DataGatherer("oo", "holahola", "oo oo", "oo@oo.com", "2020202020"));
        userList.add(new Researcher("uu", "saposapo", "uu uu", "uu@uu.com", "2020202020","uu","uu"));
    }

    // Sign in methods --------------------------------------------

    /**
     * Registers a new Visitor account.
     * @param optUser - The user type identifier (1 for Visitor).
     * @pre The user type is selected as Visitor, and required information is validated for registration.
     * @post If successful, a new Visitor account is created and added to the user list.
     */
    protected void registerVisitor(int optUser) {
        if (optUser == 1 && registerInfo(1)) {
            userList.add(new Visitor(username, password));
            UserInteraction.showText("Visitor account successfully created! Now you can log in");
        }
    }

    /**
     * Registers a new Data Gatherer account.
     * @param optUser - The user type identifier (2 for Data Gatherer).
     * @pre The user type is selected as Data Gatherer, and all mandatory information is provided and validated for registration.
     * @post If successful, a new Data Gatherer account is created and added to the user list.
     */
    protected void registerDataGatherer(int optUser) {
        if (optUser == 2 && registerInfo(2)) {
            userList.add(new DataGatherer(username, password, fullname, email, phone));
            UserInteraction.showText("Data gatherer account successfully created! Now you can log in");
        }
    }

    /**
     * Registers a new Researcher account.
     * @param optUser - The user type identifier (3 for Researcher).
     * @pre The user type is selected as Researcher, and all necessary information is validated for registration.
     * @post If successful, a new Researcher account is created and added to the user list.
     */
    protected void registerResearcher(int optUser) {
        if (optUser == 3 && registerInfo(3)) {
            userList.add(new Researcher(username, password, fullname, email, phone, universityArea, position));
            UserInteraction.showText("Researcher account successfully created! Now you can log in");
        }
    }

    /**
     * Gathers and validates user information based on the specified user type for registration.
     * @param userType - The type of user (Visitor, DataGatherer, Researcher).
     * @return True if the registration process is successful; False otherwise.
     * @pre Prompts for user information relevant to the specified user type.
     * @post User information is validated and processed for registration based on user type criteria.
     */
    private boolean registerInfo(int userType) {
        boolean registrationSuccess = false;
        boolean flag = false;
        while (!registrationSuccess) {
            if (userType == 1 || userType == 2 || userType == 3) {
                username = UserInteraction.getInputString("Please, enter username: ");
                password = UserInteraction.getInputString("Now create a password (must be longer than 8 characters): ");
                flag = isUsernameValid() && isValidPassword(password);
            }
            if (!registrationSuccess && (userType == 2 || userType == 3) && flag) {
                String name = UserInteraction.getInputString("Enter your first name: ");
                String lastname = UserInteraction.getInputString("Enter your last name: ");
                fullname = name + " " + lastname;
                email = UserInteraction.getInputString("Enter your email: ");
                phone = UserInteraction.getInputString("Enter your phone: ");
                if (isValidEmail(email) && isValidPhone(phone)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
            if (!registrationSuccess && userType == 3 && flag) {
                universityArea = UserInteraction.getInputString("Enter your university area: ");
                position = UserInteraction.getInputString("Enter your position: ");
                flag = true;
            }
            if (flag) {
                return true;
            }
        }
        return false;
    }

    // Login methods --------------------------------------------

    /**
     * Authenticates user credentials and logs them into the system.
     * @param username - The provided username for login authentication.
     * @param password - The provided password for login authentication.
     * @pre The user list contains users with credentials for login.
     * @post Logs in the user if valid credentials are provided; Else, it prompts for correct login information.
     */
    public void loginUser(String username, String password) {
        boolean found = false;
        int userType = -1;
        for (User user : userList) {
            if (user != null && username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                found = true;
                loggedInUserId = user.getIdUser();
                if (user instanceof Visitor) {
                    userType = 1;
                }
                if (user instanceof DataGatherer) {
                    userType = 2;
                }
                if (user instanceof Researcher) {
                    userType = 3;
                }
                break;
            }
        }
        if (!found) {
            UserInteraction.showText("The username or password is incorrect\n");
            Main.credentialUser();
        } else {
            UserInteraction.showText("You have successfully logged in!\n");
            Main.menuUser(userType);
        }
    }

    // Support methods --------------------------------------------

    /**
     * Checks if the password meets the required criteria.
     * @param password - The password to validate.
     * @return boolean - Returns true if the password length is at least 8 characters, false otherwise.
     * @post A message is displayed if the password length requirement is not met.
     */
    private boolean isValidPassword(String password) {
        if (password.length() >= 8) {
            return true;
        } else {
            UserInteraction.showText("The password should be 8 characters or longer\n");
            return false;
        }
    }

    /**
     * Verifies if the email format is valid.
     * @param email - The email address to validate.
     * @return boolean - Returns true if the email matches the specified format, false otherwise.
     * @post An error message is shown if the email format doesn't meet the required criteria.
     */
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            UserInteraction.showText("The email format entered is not valid\n");
            return false;
        }
    }

    /**
     * Checks if the phone number is of a valid length.
     * @param phone - The phone number to validate.
     * @return boolean - Returns true if the phone number's length is at least 10 digits, false otherwise.
     * @post An error message is displayed if the phone number's length requirement is not satisfied.
     */
    private boolean isValidPhone(String phone) {
        if (phone.length() >= 10) {
            return true;
        } else {
            UserInteraction.showText("The phone number should be more than 10 digits\n");
            return false;
        }
    }

    /**
     * Verifies if the username is available for use.
     * @return boolean - Returns true if the username is unique, false if it already exists.
     * @post If the username is already in use, an error message is shown.
     */
    private boolean isUsernameValid() {
        for (User user : userList) {
            if (user != null && username.equals(user.getUsername())) {
                UserInteraction.showText("Sorry, the username you entered is already registered. Please choose a different username.\n");
                return false;
            }
        }
        return true;
    }

    /**
     * Finds a specific Data Gatherer by their username.
     * @param idDataGatherer - The username of the Data Gatherer to be searched.
     * @return DataGatherer - Returns the Data Gatherer object if found, otherwise, returns null.
     * @post If the Data Gatherer is not found, an error message is displayed.
     */
    public static DataGatherer searchDataGatherer(String idDataGatherer) {
        for (User user : userList) {
            if (user instanceof DataGatherer) {
                DataGatherer dataGatherer = (DataGatherer) user;
                if (dataGatherer.getUsername().equals(idDataGatherer)) {
                    return dataGatherer;
                }
            }
        }
        UserInteraction.showText("We couldn't find any data gatherer with that name :(\n");
        return null;
    }

    /**
     * Checks if the current user is associated with any Data Gatherers.
     * @param associatedDataGatherers - The list of Data Gatherers associated with the current user.
     * @return boolean - Returns true if the user is associated with any Data Gatherer, false otherwise.
     * @post If the user is not associated with any Data Gatherers, an error message is shown.
     */
    public static boolean searchDataGathererAssociated(ArrayList<DataGatherer> associatedDataGatherers){
        for (DataGatherer dataGatherer : associatedDataGatherers){
            if (dataGatherer.getIdUser()==loggedInUserId){
                return true;
            }
        }
        UserInteraction.showText("You cannot add evidence to this project since you are not linked to it :(\n");
        return false;
    }

    /**
     * Searches and retrieves a user based on the currently logged-in user's identification.
     * @return The user corresponding to the current logged-in user ID, if found; otherwise, returns null.
     */
    public static User searchUser() {
        for (User user : userList) {
            if (user.getIdUser()==loggedInUserId) {
                return user;
            }
        }
        return null;
    }
}


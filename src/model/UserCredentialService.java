package model;
import ui.Main;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UserCredentialService{

    private static ArrayList<User> userList=new ArrayList<>();
    private static int loggedInUserId;
    private String username, password, fullname, email, phone, universityArea, position;

    protected void initializeUsers() {
        userList.add(new DataGatherer("oo", "holahola", "oo oo", "oo@oo.com", "2020202020"));
        userList.add(new Researcher("uu", "saposapo", "uu uu", "uu@uu.com", "2020202020","uu","uu"));
    }

    // Sign in methods --------------------------------------------

    public void registerVisitor(int optUser) {
        if (optUser == 1 && registerInfo(1)) {
            userList.add(new Visitor(username, password));
            UserInteraction.showText("Visitor account succesfully created! Now you can log in");
        }
    }

    public void registerDataGatherer(int optUser) {
        if (optUser == 2 && registerInfo(2)) {
            userList.add(new DataGatherer(username, password, fullname, email, phone));
            UserInteraction.showText("Data gatherer account succesfully created! Now you can log in");
        }
    }

    public void registerResearcher(int optUser) {
        if (optUser == 3 && registerInfo(3)) {
            userList.add(new Researcher(username, password, fullname, email, phone, universityArea, position));
            UserInteraction.showText("Researcher account succesfully created! Now you can log in");
        }
    }

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
            UserInteraction.showText("El usuario o la contraseña son erróneos\n");
            Main.credentialUser();
        } else {
            UserInteraction.showText("Haz ingresado exitosamente!\n");
            Main.menuUser(userType);
        }
    }

    // Support methods --------------------------------------------

    private boolean isValidPassword(String password) {
        if (password.length() >= 8) {
            return true;
        } else {
            UserInteraction.showText("La contraseña debe ser mayor o igual a 8 dígitos\n");
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            UserInteraction.showText("El formato de correo ingresado no es vállido\n");
            return false;
        }
    }

    private boolean isValidPhone(String phone) {
        if (phone.length() >= 10) {
            return true;
        } else {
            UserInteraction.showText("El número de celular debe ser mayor a 10 dígitos\n");
            return false;
        }
    }

    private boolean isUsernameValid() {
        for (User user : userList) {
            if (user != null && username.equals(user.getUsername())) {
                UserInteraction.showText("Lo siento, el nombre de usuario que has ingresado ya está registrado. Por favor, elige un nombre de usuario diferente.\n");
                return false;
            }
        }
        return true;
    }
    public static DataGatherer searchDataGatherer(String idDataGatherer) {
        for (User user : userList) {
            if (user instanceof DataGatherer) {
                DataGatherer dataGatherer = (DataGatherer) user;
                if (dataGatherer.getUsername().equals(idDataGatherer)) {
                    return dataGatherer;
                }
            }
        }
        UserInteraction.showText("No se pudo encontrar ningún data gatherer con ese nombre :(");
        return null;
    }
    public static boolean searchDataGathererAssociated(ArrayList<DataGatherer> associatedDataGatherers){
        for (DataGatherer dataGatherer : associatedDataGatherers){
            if (dataGatherer.getIdUser()==loggedInUserId){
                return true;
            }
        }
        UserInteraction.showText("No puedes agregar evidencias a este proyecto ya que no estás vinculado a este :(\n");
        return false;
    }
}


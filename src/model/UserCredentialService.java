package model;
import java.util.*;
import ui.Main;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UserCredentialService {

    private Main main;
    User[] userList;

    int counterUsers = 0;
    private String username, password, fullname, email, phone, universityArea, position;
    public UserCredentialService() {
        main = new Main();
        userList = new User[100];
    }

    // Sign in methods --------------------------------------------

    public void registerVisitor(int optUser) {
        if (optUser == 1 && registerInfo(1)) {
            userList[counterUsers] = new Visitor(username, password);
            counterUsers++;
            main.showText("Visitor account succesfully created! Now you can log in");
        }
    }

    public void registerDataGatherer(int optUser) {
        if (optUser == 2 && registerInfo(2)) {
            userList[counterUsers] = new DataGatherer(username, password, fullname, email, phone);
            counterUsers++;
            main.showText("Data gatherer account succesfully created! Now you can log in");
        }
    }

    public void registerResearcher(int optUser) {
        if (optUser == 3 && registerInfo(3)) {
            userList[counterUsers] = new Researcher(username, password, fullname, email, phone, universityArea, position);
            counterUsers++;
            main.showText("Researcher account succesfully created! Now you can log in");
        }
    }

    private boolean registerInfo(int userType) {
        boolean registrationSuccess = false;
        boolean flag = false;
        while (!registrationSuccess) {
            if (userType == 1 || userType == 2 || userType == 3) {
                username = main.getInputString("Please, enter username: ");
                password = main.getInputString("Now create a password (must be longer than 8 characters): ");
                if (isUsernameValid() && isValidPassword(password)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
            if (!registrationSuccess && (userType == 2 || userType == 3) && flag) {
                String name = main.getInputString("Enter your first name: ");
                String lastname = main.getInputString("Enter your last name: ");
                fullname = name + " " + lastname;
                email = main.getInputString("Enter your email: ");
                phone = main.getInputString("Enter your phone: ");
                if (isValidEmail(email) && isValidPhone(phone)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
            if (!registrationSuccess && userType == 3 && flag) {
                universityArea = main.getInputString("Enter your university area: ");
                position = main.getInputString("Enter your position: ");
                flag = true;
            }
            if (flag) {
                registrationSuccess = true;
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
            main.showText("El usuario o la contraseña son erróneos\n");
            main.credentialUser();
        } else {
            main.showText("Haz ingresado exitosamente!\n");
            main.menuUser(userType);
        }
    }

    // Support methods --------------------------------------------

    private boolean isValidPassword(String password) {
        if (password.length() >= 8) {
            return true;
        } else {
            main.showText("La contraseña debe ser mayor o igual a 8 dígitos\n");
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
            main.showText("El formato de correo ingresado no es vállido\n");
            return false;
        }
    }

    private boolean isValidPhone(String phone) {
        if (phone.length() >= 10) {
            return true;
        } else {
            main.showText("El número de celular debe ser mayor a 10 dígitos\n");
            return false;
        }
    }

    private boolean isUsernameValid() {
        for (User user : userList) {
            if (user != null && username.equals(user.getUsername())) {
                main.showText("Lo siento, el nombre de usuario que has ingresado ya está registrado. Por favor, elige un nombre de usuario diferente.\n");
                return false;
            }
        }
        return true;
    }
}


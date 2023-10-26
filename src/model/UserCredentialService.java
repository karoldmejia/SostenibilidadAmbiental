package model;
import java.util.*;
import ui.Main;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UserCredentialService {

    private Main main;
    User[] userList;
    int counterUsers=0;
    private String username;
    private String password;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String universityArea;
    private String position;

    public UserCredentialService(){
        main= new Main();
        userList=new User[100];
    }

    public void registerVisitor(){
        registerInfo(1);
        userList[counterUsers]=new Visitor(username, password);
        counterUsers++;
        main.showText("Visitor profile succesfully created! Now you can log in");
    }

    public void registerDataGatherer(){
        if (registerInfo(2)) {
            userList[counterUsers] = new Visitor(username, password);
            counterUsers++;
            main.showText("Visitor profile succesfully created! Now you can log in");
        }
    }

    private boolean registerInfo(int userType){
        if (userType==1 || userType==2 || userType==3) {
            String username = main.getInputString("Please, enter username: ");
            String password = main.getInputString("Now create a password (must be longer than 8 characteres: ");
            if (validatePassword(password)){
                return true;
            }
        } else if (userType==2 || userType==3) {
            String name= main.getInputString("Enter your first name: ");
            String lastname=main.getInputString("Enter your last name: ");
            String fullname=name+" "+lastname;
            String email=main.getInputString("Enter your email: ");
            String phone=main.getInputString("Enter your phone: ");
        } else if (userType==3) {
            String universityArea=main.getInputString("Enter your university area");
            String position=main.getInputString("Enter your position");

        }
    }

    private boolean validatePassword(String password){
        if(password.length()>8){
            return true;
        } else {
            main.showText("La contraseña debe ser mayor a 8 dígitos");
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

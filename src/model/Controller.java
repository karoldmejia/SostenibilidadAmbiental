package model;
import java.util.*;
import ui.Main;

public class Controller {
    private Main main;
    private UserCredentialService userCredentialService;
    public Controller(){
        main=new Main();
        userCredentialService=new UserCredentialService();
    }

    public void registerUser() {
        int optUser = -1;
        while (optUser < 1 || optUser > 3) {
            optUser = main.getInputInt("Please, select which user you want to create: \n1. Visitor \n2. Data gatherer \n3. Researcher\n");
            if (optUser < 1 || optUser > 3) {
                main.showText("Por favor, selecciona una opción válida (1, 2 o 3).");
            }
        }
        if (optUser==1){
            userCredentialService.registerVisitor(optUser);
        } else if (optUser==2){
            userCredentialService.registerDataGatherer(optUser);
        } else if (optUser==3) {
            userCredentialService.registerResearcher(optUser);
        }
    }

    public void loginUser(){
        main.showText("Log in!\n");
        String username=main.getInputString("Please, enter your name: ");
        String password=main.getInputString("Now your password: ");
        userCredentialService.loginUser(username,password);
    }

    public void visualizeMap(){
    }
    public void createView(){
    }
    public void createProject(){
    }
    public void seeProject(){
    }
    public void modifyProject(){
    }
    public void deleteProject(){
    }
    public void createEvidence(){
    }
    public void modifyEvidence(){
    }
    public void deleteEvidence(){
    }
    public void reviewReview(){
    }


}

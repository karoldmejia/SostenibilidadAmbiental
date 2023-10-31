package model;
import java.util.*;
import ui.Main;

public class Controller {
    private Main main;
    private UserCredentialService userCredentialService;
    private ProjectManagementService projectManagementService;
    public Controller(){
        main=new Main();
        userCredentialService=new UserCredentialService();
        projectManagementService=new ProjectManagementService();

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

    private String showSelectProjects(){
        main.showText("These are the registered projects: " + projectManagementService.getAllProjectsNames());
        String idProject = main.getInputString("\nPlease insert the name of one of them: ");
        return idProject;
    }
    public void createProject(){
        projectManagementService.createProject();
    }
    public void seeProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            projectManagementService.queryProject(idProject);
        } else {
            main.showText("Projects have not been registered yet :(");
        }
    }
    public void updateProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            projectManagementService.updateProject(idProject);
        } else {
            main.showText("Projects have not been registered yet :(");
        }
    }
    public void deleteProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            String checkDecision=main.getInputString("¿Está usted seguro que desea eliminar el proyecto? Press 'si' or any other letter for otherwise\n");
            if (checkDecision.equalsIgnoreCase("si")){
                projectManagementService.deleteProject(idProject);
            } else {
                main.showText("Okey, let's get back to the menu!\n");
            }
        } else {
            main.showText("Projects have not been registered yet :(");
        }
    }
    public void createEvidence(int userType){
        projectManagementService.createEvidence(userType);
    }
    public void updateEvidence(){
        if (projectManagementService.evidencesProjects.size()>0) {
            main.showText("These are the registered evidences: " + projectManagementService.getAllEvidencesNames());
            String idEvidence = main.getInputString("\nPlease insert the name of one of them: ");
            projectManagementService.updateEvidence(idEvidence);
        } else {
            main.showText("Evidences have not been registered yet :(");
        }
    }
    public void deactivateEvidence(){
        if (projectManagementService.evidencesProjects.size()>0) {
            main.showText("These are the registered evidences: " + projectManagementService.getAllEvidencesNames());
            String idEvidence = main.getInputString("\nPlease insert the name of one of them: ");
            projectManagementService.deactivateEvidence(idEvidence);
        } else {
            main.showText("Evidences have not been registered yet :(");
        }
    }
    public void linkDataGatherersToProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            String idDataGatherer = main.getInputString("\nPlease insert data gatherer's name: ");
            projectManagementService.linkDataGatherersToProject(idProject,idDataGatherer);
        } else {
            main.showText("Projects have not been registered yet :(");
        }
    }
    public void reviewReview(){
    }


}

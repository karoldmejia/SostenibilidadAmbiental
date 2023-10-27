package model;
import ui.Main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class ProjectManagementService {
    private Main main;
    Project[] projects;
    EvidenceProject[] evidenceProjects;
    InterestPoint[] interestPoints;
    int counterProjects = 0, counterEvidences = 0, counterInterestPoints = 0;

    ProjectManagementService() {
        main = new Main();
        projects = new Project[100];
        evidenceProjects = new EvidenceProject[100];
        interestPoints = new InterestPoint[100];
    }

    public void createProject() {
        boolean flag = false;
        while (!flag) {
            String nameProject = main.getInputString("Enter project's name: ");
            String description = main.getInputString("Enter description: ");
            int pilarId = main.getInputInt("Please, select which pilar this project belongs to:\n1. Biodiversidad\n" +
                    "2. Gestión del recurso hídrico\n3. Gestión integral de residuos sólidos\n4. Energía\n");
            String initialDate = main.getInputString("Insert initial project's date (format dd/MM/yyyy): ");
            String finalDate = main.getInputString("Insert final project's date (format dd/MM/yyyy): ");
            String optStatus = main.getInputString("Please insert 'y' if this project is available, and any other letter for otherwise\n");
            if (isNameValid(1, nameProject) && isDescriptionValid(description) && isPilarValid(pilarId)!=null &&
                    isDateValid(initialDate) && isDateValid(finalDate) && isSecondDateGreaterThanFirst(initialDate,finalDate)){
                flag=true;
                Pilar pilar=isPilarValid(pilarId);
                boolean status=checkStatus(optStatus);
                projects[counterProjects]=new Project(nameProject,description,pilar,initialDate,finalDate,status);
                counterProjects++;
                main.showText("Proyecto agregado exitosamente!");
            }
        }
    }

    private boolean isNameValid(int typeValid, String nameProject) {
        boolean flag=false;
        if (typeValid == 1) {
            for (Project project : projects) {
                if (project != null && nameProject.equals(project.getNameProject())) {
                    flag=true;
                }
            }
        } else if (typeValid == 2) {
            for (EvidenceProject evidenceProject : evidenceProjects) {
                if (evidenceProject != null && evidenceProject.equals(evidenceProject.getNameEvidence())) {
                    flag=true;
                }
            }
        }
        if (flag){
            main.showText("Lo siento, el nombre ingresado ya está registrado. Por favor, elige un nombre de usuario diferente.\n");
            return false;
        } else {
            return true;
        }
    }

    private boolean isDescriptionValid(String description){
        if (description.length() >= 30) {
            return true;
        } else {
            main.showText("La descripción debe ser mayor a 30 carácteres\n");
            return false;
        }
    }
    private Pilar isPilarValid(int pilarId){
        Pilar pilar = null;
        if (pilarId==1) {
            pilar = Pilar.Biodiversidad;
        } else if (pilarId==2) {
            pilar = Pilar.Gestion_del_recurso_hidrico;
        } else if (pilarId==3) {
            pilar = Pilar.Gestion_integral_de_residuos_solidos;
        } else if (pilarId==4) {
            pilar = Pilar.Energia;
        } else {
            main.showText("La opción de pilar ingresada no es válida\n");
            return null;
        }
        return pilar;
    }
    private boolean isDateValid(String date) {
        boolean flag=false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(date);
            if (date.equals(dateFormat.format(parsedDate))) {
                flag=true;
            } else {
                main.showText("El formato de fecha ingresado no es válido");
                flag=false;
            }
        } catch (ParseException e) {
            flag=false;
        }
        if (flag){
            return true;
        } else {
            main.showText("El formato de fecha ingresado no es válido");
            return false;
        }
    }

    private boolean isSecondDateGreaterThanFirst(String firstDate, String secondDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(firstDate);
            Date date2 = dateFormat.parse(secondDate);
            return date2.after(date1);
        } catch (ParseException e) {
            main.showText("La fecha final es menor que la fecha inicial\n");
            return false;
        }
    }

    private boolean checkStatus(String optStatus){
        if (optStatus=="y"){
            return true;
        } else {
            return false;
        }
    }

}

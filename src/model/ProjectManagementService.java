package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

public class ProjectManagementService{
    static ArrayList<Project> projects = new ArrayList<>();

    protected void initializeProjects() {
        projects.add(new Project("uu", "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu", Pilar.Biodiversidad, "01/01/2001", "01/01/2002", true));
        projects.get(0).getEvidences().add(new Evidence("pp","01/01/2001",true,CharTypeEvidence.A,"ww"));
        String[] list=new String[2];
        list[0]="ww";
        list[1]="ww";
        projects.get(0).getEvidences().add(new Review("dd","01/01/2001",true,CharTypeEvidence.R,false,list));
        InterestPoint interestPoint=new InterestPoint("qq",12,12,"eoiroir");
        interestPoint.getEvidences().add(projects.get(0).getEvidences().get(0));
        interestPoint.getEvidences().add(projects.get(0).getEvidences().get(1));
        MapUniversity.addInterestPoint(interestPoint);
    }

    // Main methods --------------------------------------------

    String pilarOptions= "Please, select which pilar this project belongs to:\n1. Biodiversidad\n2. Gestión del recurso hídrico\n3. Gestión integral de residuos sólidos\n4. Energía\n";
    String statusOptions="Please insert 'y' if this project is available, and any other letter for otherwise\n";
    public void createProject() {
        boolean flag = false;
        while (!flag) {
            String nameProject = UserInteraction.getInputString("Enter project's name: ");
            String description = UserInteraction.getInputString("Enter description: ");
            int pilarId = UserInteraction.getInputInt(pilarOptions);
            String initialDate = UserInteraction.getInputString("Insert initial project's date (format dd/MM/yyyy): ");
            String finalDate = UserInteraction.getInputString("Insert final project's date (format dd/MM/yyyy): ");
            String optStatus = UserInteraction.getInputString(statusOptions);
            if (isNameValid(nameProject) && isDescriptionValid(description) && isPilarValid(pilarId)!=null &&
                    isDateValid(initialDate) && isDateValid(finalDate) && isSecondDateGreaterThanFirst(initialDate,finalDate)){
                flag=true;
                Pilar pilar=isPilarValid(pilarId);
                boolean status=checkStatus(optStatus);
                projects.add(new Project(nameProject,description,pilar,initialDate,finalDate,status));
                UserInteraction.showText("Proyecto agregado exitosamente!");
            }
        }
    }
    public void queryProject(String idProject) {
        Project project = searchProject(idProject);
        if (project != null) {
            UserInteraction.showText("\n- Project: " + project.getNameProject() + "\n Description: " +
                    project.getDescription() + "\n Pilar: " + project.getPilar() + "\n Initial date: " + project.getInitialDate()
                    + "\n Final date: " + project.getFinalDate() + "\n Status: " + (project.getStatus() ? "Available" : "Not Available"));

            int[] cantEvidences = project.countEvidences();
            UserInteraction.showText("\n- Reviews associated: " + cantEvidences[1]);
            UserInteraction.showText("\n- Other evidences associated: " + cantEvidences[0]);
        }
    }
    public void updateProject(String idProject) {
        Project project = searchProject(idProject);
        if (project == null) {
            return;
        }
        while (true) {
            UserInteraction.showText("Select the attribute to modify: \n1. Name\n2. Description\n3. Pilar\n4. Initial date\n5. Final date\n6. Status\n0. Finish and apply changes");
            int opcion = UserInteraction.getInputInt("\nEnter the number of the desired option (0 to finish): ");
            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1:
                    updateNameProject(project);
                    break;
                case 2:
                    updateDescriptionProject(project);
                    break;
                case 3:
                    updatePilarProject(project);
                    break;
                case 4:
                    updateInitialDateProject(project);
                    break;
                case 5:
                    updateFinalDateProject(project);
                    break;
                case 6:
                    updateStatusProject(project);
                    break;
                default:
                    UserInteraction.showText("Invalid option.");
                    break;
            }
        }
        UserInteraction.showText("Changes applied successfully.");
    }
    public void deleteProject(String idProject) {
        Project projectDelete=null;
        boolean flag=false;
        for (Project project : projects) {
            if (project != null && idProject.equals(project.getNameProject())) {
                projectDelete=project;
                flag=true;
            }
        }
        if (flag){
            projects.remove(projectDelete);
            UserInteraction.showText("Proyecto eliminado exitosamente.");
            return;
        }
        UserInteraction.showText("We couldn't find any project");
    }
    public void linkDataGatherersToProject(String idProject, String idDataGatherer){
        Project project=searchProject(idProject);
        if (project!=null){
            DataGatherer dataGatherer=UserCredentialService.searchDataGatherer(idDataGatherer);
            if(dataGatherer!=null){
                project.getAssociatedDataGatherers().add(dataGatherer);
                UserInteraction.showText("Data gatherer has been successfully linked!");
            }
        }
    }

    // Support methods --------------------------------------------
    private boolean isNameValid(String nameProject) {
        boolean flag=false;
        for (Project project : projects) {
            if (project != null && nameProject.equals(project.getNameProject())) {
                flag = true;
                break;
            }
        }
        if (flag){
            UserInteraction.showText("Sorry, entered name is already registered. Please, select a different username.\n");
            return false;
        } else {
            return true;
        }
    }
    private boolean isDescriptionValid(String description){
        if (description.length() >= 20) {
            return true;
        } else {
            UserInteraction.showText("Description length should be greater than 20 characters\n");
            return false;
        }
    }
    private Pilar isPilarValid(int pilarId){
        Pilar pilar;
        if (pilarId==1) {
            pilar = Pilar.Biodiversidad;
        } else if (pilarId==2) {
            pilar = Pilar.Gestion_del_recurso_hidrico;
        } else if (pilarId==3) {
            pilar = Pilar.Gestion_integral_de_residuos_solidos;
        } else if (pilarId==4) {
            pilar = Pilar.Energia;
        } else {
            UserInteraction.showText("La opción de pilar ingresada no es válida\n");
            return null;
        }
        return pilar;
    }
    private boolean isDateValid(String date) {
        boolean flag;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            Date parsedDate = dateFormat.parse(date);
            flag= date.equals(dateFormat.format(parsedDate));
        } catch (ParseException e) {
            flag=false;
        }
        if (flag){
            return true;
        } else {
            UserInteraction.showText("El formato de fecha ingresado no es válido\n");
            return false;
        }
    }
    private boolean isSecondDateGreaterThanFirst(String firstDate, String secondDate) {
        boolean flag;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = dateFormat.parse(firstDate);
            Date date2 = dateFormat.parse(secondDate);
            flag= date2.compareTo(date1) > 0;
        } catch (ParseException e) {
            flag=false;
        }
        if (flag){
            return true;
        } else {
            UserInteraction.showText("La fecha final es menor que la fecha inicial\n");
            return false;
        }
    }
    private boolean checkStatus(String optStatus){
        return optStatus.equalsIgnoreCase("y");
    }
    protected Project searchProject(String idProject) {
        for (Project project : projects) {
            if (project!=null && idProject.equals(project.getNameProject())) {
                return project;
            }
        }
        UserInteraction.showText("We couldn't find any project with that name :(\n");
        return null;
    }
    public String getAllProjectsNames() {
        StringBuilder projectNames = new StringBuilder();
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            String name = project.getNameProject();
            projectNames.append(name);
            if (i < projects.size() - 1) {
                projectNames.append(", ");
            }
        }
        return projectNames.toString();
    }

    private void updateNameProject(Project project){
        String nameProject = UserInteraction.getInputString("Enter the new name: ");
        if (isNameValid(nameProject)) {
            project.setNameProject(nameProject);
        }
    }
    private void updateDescriptionProject(Project project){
        String description = UserInteraction.getInputString("Enter the new description: ");
        if (isDescriptionValid(description)) {
            project.setDescription(description);
        }
    }
    private void updatePilarProject(Project project){
        int idPilar = UserInteraction.getInputInt(pilarOptions);
        if (isPilarValid(idPilar)!=null) {
            Pilar pilar = isPilarValid(idPilar);
            project.setPilar(pilar);
        }
    }
    private void updateInitialDateProject(Project project){
        String initialDate = UserInteraction.getInputString("Enter the new description: ");
        if (isDateValid(initialDate) && isSecondDateGreaterThanFirst(initialDate,project.getFinalDate())) {
            project.setInitialDate(initialDate);
        }
    }
    private void updateFinalDateProject(Project project){
        String finalDate = UserInteraction.getInputString("Enter the new description: ");
        if (isDateValid(finalDate) && isSecondDateGreaterThanFirst(project.getInitialDate(),finalDate)) {
            project.setFinalDate(finalDate);
        }
    }
    private void updateStatusProject(Project project){
        String idStatus = UserInteraction.getInputString(statusOptions);
        boolean status = checkStatus(idStatus);
        project.setStatus(status);
    }
    protected static String findEvidenceProject(EvidenceProject evidenceProject) {
        String nameProject=null;
        for (Project project : projects) {
            for (EvidenceProject evidence : project.getEvidences()) {
                if (evidence.getIdEvidence() == evidenceProject.getIdEvidence()) {
                    nameProject=project.getNameProject();
                    break;
                }
            }
        }

        if (nameProject!=null){
            return nameProject;
        } else {
            return null;
        }
    }


}

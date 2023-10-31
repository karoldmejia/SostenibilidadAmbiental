package model;
import ui.Main;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

public class ProjectManagementService {
    private final Main main;
    UserCredentialService userCredentialService;
    ArrayList<Project> projects;
    ArrayList<EvidenceProject> evidencesProjects;
    ArrayList<InterestPoint> interestPoints;

    ProjectManagementService() {
        main = new Main();
        projects = new ArrayList<>();
        evidencesProjects = new ArrayList<>();
        interestPoints = new ArrayList<>();
        userCredentialService=new UserCredentialService();
        projects.add(new Project("uu", "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu",Pilar.Biodiversidad,"01/01/2001","01/01/2002",true));
    }

    // Main methods --------------------------------------------

    String pilarOptions= """
            Please, select which pilar this project belongs to:
            1. Biodiversidad
            2. Gestión del recurso hídrico
            3. Gestión integral de residuos sólidos
            4. Energía
            """;
    String statusOptions="Please insert 'y' if this project is available, and any other letter for otherwise\n";
    public void createProject() {
        boolean flag = false;
        while (!flag) {
            String nameProject = main.getInputString("Enter project's name: ");
            String description = main.getInputString("Enter description: ");
            int pilarId = main.getInputInt(pilarOptions);
            String initialDate = main.getInputString("Insert initial project's date (format dd/MM/yyyy): ");
            String finalDate = main.getInputString("Insert final project's date (format dd/MM/yyyy): ");
            String optStatus = main.getInputString(statusOptions);
            if (isNameValid(1, nameProject) && isDescriptionValid(description) && isPilarValid(pilarId)!=null &&
                    isDateValid(initialDate) && isDateValid(finalDate) && isSecondDateGreaterThanFirst(initialDate,finalDate)){
                flag=true;
                Pilar pilar=isPilarValid(pilarId);
                boolean status=checkStatus(optStatus);
                projects.add(new Project(nameProject,description,pilar,initialDate,finalDate,status));
                main.showText("Proyecto agregado exitosamente!");
            }
        }
    }
    public void queryProject(String idProject) {
        Project project = searchProject(idProject);
        if (project != null) {
            main.showText("\n- Project: " + project.getNameProject() + "\n Description: " +
                    project.getDescription() + "\n Pilar: " + project.getPilar() + "\n Initial date: " + project.getInitialDate()
                    + "\n Final date: " + project.getFinalDate() + "\n Status: " + (project.getStatus() ? "Available" : "Not Available"));

            int[] cantEvidences = countEvidencesProject(project);
            main.showText("\n- Reviews associated: " + cantEvidences[1]);
            main.showText("\n- Other evidences associated: " + cantEvidences[0]);
        }
    }
    public void updateProject(String idProject) {
        Project project = searchProject(idProject);
        if (project == null) {
            return;
        }
        while (true) {
            main.showText("Select the attribute to modify: \n1. Name\n2. Description\n3. Pilar\n4. Initial date\n5. Final date\n6. Status\n0. Finish and apply changes");
            int opcion = main.getInputInt("\nEnter the number of the desired option (0 to finish): ");
            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1 -> updateNameProject(project);
                case 2 -> updateDescriptionProject(project);
                case 3 -> updatePilarProject(project);
                case 4 -> updateInitialDateProject(project);
                case 5 -> updateFinalDateProject(project);
                case 6 -> updateStatusProject(project);
                default -> main.showText("Invalid option.");
            }
        }
        main.showText("Changes applied successfully.");
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
            deleteEvidencesProject(projectDelete);
            projects.remove(projectDelete);
            main.showText("Proyecto eliminado exitosamente.");
            return;
        }
        main.showText("We couldn't find any project");
    }

    public void createEvidence(int userType){
        boolean flag=false;
        int optEvidence=0;
        String nameEvidence=null, registerDate=null;
        Project project=null;
        if (userType==3) {
            optEvidence = main.getInputInt("Desea añadir:\n1. Reseña\n2. Evidencia común\n");
        } else if (userType==2) {
            optEvidence=1;
            main.showText("Crearemos una reseña!\n");
        }
        while (!flag){
            main.showText("These are the registered projects: " + getAllProjectsNames());
            String idProject= main.getInputString("\nIngresa el proyecto asociado: ");
            project=searchProject(idProject);
            if (project!=null && userType==2 && !(userCredentialService.searchDataGathererAssociated(project.getAssociatedDataGatherers()))){
                return;
            }
            nameEvidence= main.getInputString("Ingresa el nombre: ");
            registerDate = main.getInputString("Enter the registration date: ");
            if (isNameValid(2,nameEvidence) && isDateValid(registerDate) && project!=null && project.getStatus()){
                flag=true;
            } else if (project!=null && !project.getStatus()) {
                main.showText("Lo siento, no puedes agregar evidencias a este proyecto ya que no se encuentra disponible\n");
                return;
            }
        }
        if (flag) {
            if ((userType == 2 || userType == 3) && optEvidence == 1) {
                createReview(nameEvidence, registerDate, project, userType);
            } else if (userType == 3 && optEvidence == 2) {
                createCommonEvidence(nameEvidence, registerDate, project);
            }
        }
    }

    public void updateEvidence(String idEvidence){
        EvidenceProject evidence=searchEvidence(idEvidence);
        if (evidence == null) {
            return;
        }
        while (true) {
            main.showText("Select the attribute to modify: \n1. Name\n2. Project\n3. Registration date\n");
            if (evidence instanceof Evidence){
                main.showText("4. Url\n");
            } else if (evidence instanceof Review) {
                main.showText("4. Url from list\n");
            }
            int opcion = main.getInputInt("\nEnter the number of the desired option (0 to finish and apply changes): ");
            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1 -> updateNameEvidence(evidence);
                case 2 -> updateProjectEvidence(evidence);
                case 3 -> updateDateEvidence(evidence);
            }
            if (evidence instanceof Evidence && opcion==4){
                updateSingleUrl(evidence);
            } else if (evidence instanceof Review && opcion==4) {
                updateMultipleUrl(evidence);
            } else {
                main.showText("Invalid option.");
            }
        }
        main.showText("Changes applied successfully.");
    }

    public void deactivateEvidence(String idEvidence){
        EvidenceProject evidenceDelete=null;
        boolean flag=false;
        for (EvidenceProject evidenceProject : evidencesProjects) {
            if (evidenceProject != null && idEvidence.equals(evidenceProject.getNameEvidence())) {
                evidenceDelete=evidenceProject;
                flag=true;
            }
        }
        if (flag){
            evidenceDelete.setAvailability(false);
            main.showText("Evidencia desactivada.");
            return;
        }
        main.showText("We couldn't find any project");
    }

    public void linkDataGatherersToProject(String idProject, String idDataGatherer){
        Project project=searchProject(idProject);
        if (project!=null){
            if(userCredentialService.searchDataGatherer(idDataGatherer)!=null){
                project.getAssociatedDataGatherers().add(userCredentialService.searchDataGatherer(idDataGatherer));
                main.showText("Data gatherer has been successfully linked!");
            }
        }
    }

    // Support methods --------------------------------------------
    private boolean isNameValid(int typeValid, String nameProject) {
        boolean flag=false;
        if (typeValid == 1) {
            for (Project project : projects) {
                if (project != null && nameProject.equals(project.getNameProject())) {
                    flag=true;
                }
            }
        } else if (typeValid == 2) {
            for (EvidenceProject evidenceProject : evidencesProjects) {
                if (evidenceProject != null && nameProject.equals(evidenceProject.getNameEvidence())) {
                    flag=true;
                }
            }
        }
        if (flag){
            main.showText("Sorry, entered name is already registered. Please, select a different username.\n");
            return false;
        } else {
            return true;
        }
    }
    private boolean isDescriptionValid(String description){
        if (description.length() >= 20) {
            return true;
        } else {
            main.showText("Description length should be greater than 20 characters\n");
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
            main.showText("La opción de pilar ingresada no es válida\n");
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
            main.showText("El formato de fecha ingresado no es válido\n");
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
            main.showText("La fecha final es menor que la fecha inicial\n");
            return false;
        }
    }
    private boolean checkStatus(String optStatus){
        return optStatus.equalsIgnoreCase("y");
    }
    private Project searchProject(String idProject) {
        for (Project project : projects) {
            if (project!=null && idProject.equals(project.getNameProject())) {
                return project;
            }
        }
        main.showText("We couldn't find any project with that name :(\n");
        return null;
    }
    private int[] countEvidencesProject(Project project){
        int[] cantEvidencesProject=new int[2];
        int cEvidences=0;
        int cReviews=0;
        for (EvidenceProject evidence: evidencesProjects) {
            if (evidence !=null){
                if (evidence instanceof Evidence && evidence.getProject().getIdProject()==project.getIdProject()){
                    cEvidences++;
                } else if (evidence instanceof Review && (((Review) evidence).getEvidenceStatus()) && evidence.getProject().getIdProject()==project.getIdProject()) {
                    cReviews++;
                }
            }
        }
        return cantEvidencesProject;
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
        String nameProject = main.getInputString("Enter the new name: ");
        if (isNameValid(1,nameProject)) {
            project.setNameProject(nameProject);
        }
    }
    private void updateDescriptionProject(Project project){
        String description = main.getInputString("Enter the new description: ");
        if (isDescriptionValid(description)) {
            project.setDescription(description);
        }
    }
    private void updatePilarProject(Project project){
        int idPilar = main.getInputInt(pilarOptions);
        if (isPilarValid(idPilar)!=null) {
            Pilar pilar = isPilarValid(idPilar);
            project.setPilar(pilar);
        }
    }
    private void updateInitialDateProject(Project project){
        String initialDate = main.getInputString("Enter the new description: ");
        if (isDateValid(initialDate) && isSecondDateGreaterThanFirst(initialDate,project.getFinalDate())) {
            project.setInitialDate(initialDate);
        }
    }
    private void updateFinalDateProject(Project project){
        String finalDate = main.getInputString("Enter the new description: ");
        if (isDateValid(finalDate) && isSecondDateGreaterThanFirst(project.getInitialDate(),finalDate)) {
            project.setFinalDate(finalDate);
        }
    }
    private void updateStatusProject(Project project){
        String idStatus = main.getInputString(statusOptions);
        boolean status = checkStatus(idStatus);
        project.setStatus(status);
    }
    private void deleteEvidencesProject(Project projectDelete){
        evidencesProjects.removeIf(evidenceProject -> evidenceProject != null && evidenceProject.getProject() == projectDelete);
    }
    private void createReview(String nameEvidence, String registerDate, Project project, int userType){
        boolean evidenceStatus=false;
        int cantUrl=main.getInputInt("¿Cuántos enlaces desea agregar?: ");
        String[] listUrl=new String[cantUrl];
        if (userType==3) {
            evidenceStatus=true;
        }
        for (int i=0;i<cantUrl;i++){
            listUrl[i]=main.getInputString("Evidence no. "+(i+1)+": ");
        }
        evidencesProjects.add(new Review(project,nameEvidence,registerDate,true,evidenceStatus,listUrl));
        if (!evidenceStatus){
            main.showText("Creación exitosa, ahora tu reseña se encuentra pendiente por aprobación!\n");
        } else {
            main.showText("Reseña creada exitosamente!\n");
        }
    }
    private void createCommonEvidence(String nameEvidence, String registerDate, Project project){
        String url= main.getInputString("Ingresa la url: ");
        if (url!=null){
            evidencesProjects.add(new Evidence(project,nameEvidence,registerDate,true,url));
            main.showText("Evidencia creada exitosamente!\n");
        } else{
            main.showText("Url no puede estar vacía!\n");
        }
    }
    private void updateNameEvidence(EvidenceProject evidenceProject){
        String name = main.getInputString("Enter the new name: ");
        if (isNameValid(2,name)) {
            evidenceProject.setNameEvidence(name);
        }
    }
    private void updateProjectEvidence(EvidenceProject evidenceProject){
        main.showText("These are the registered projects: " + getAllProjectsNames());
        String idProject = main.getInputString("\nPlease insert the name of one of them: ");
        Project project=searchProject(idProject);
        if (project!=null && !project.getStatus()) {
            evidenceProject.setProject(project);
        } else if (!project.getStatus()) {
            main.showText("Lo siento, no puedes agregar evidencias a este proyecto ya que no se encuentra disponible\n");
        }
    }
    private void updateDateEvidence(EvidenceProject evidenceProject){
        String date = main.getInputString("Enter the new registration date: ");
        if (isDateValid(date)) {
            evidenceProject.setRegisterDate(date);
        }
    }
    private void updateSingleUrl(EvidenceProject evidenceProject){
        String url = main.getInputString("Enter the new url: ");
        Evidence evidence = (Evidence) evidenceProject;
        evidence.setUrl(url);
    }
    private void updateMultipleUrl(EvidenceProject evidenceProject){
        Review review=(Review) evidenceProject;
        int optUrl=-1;
        while (optUrl!=0) {
            main.showText("Por favor, elija cuál url desea cambiar\n");
            for (int i = 0; i < review.getListUrl().length; i++) {
                main.showText((i+1) + ". " + review.getListUrl()[i]+"\n");
            }
            optUrl = main.getInputInt("Ingrese su elección o '0' para salir: ");
            if (optUrl>=1 && optUrl<=review.getListUrl().length){
                review.getListUrl()[(optUrl-1)] = main.getInputString("Enter new url: ");
            } else {
                main.showText("Ingresa una opción válida");
            }
        }
    }
    public String getAllEvidencesNames() {
        StringBuilder evidencesNames = new StringBuilder();
        for (int i = 0; i < evidencesProjects.size(); i++) {
            EvidenceProject evidenceProject = evidencesProjects.get(i);
            String name = evidenceProject.getNameEvidence();
            evidencesNames.append(name);
            if (i < evidencesProjects.size() - 1) {
                evidencesNames.append(", ");
            }
        }
        return evidencesNames.toString();
    }

    private EvidenceProject searchEvidence(String idEvidence) {
        for (EvidenceProject evidenceProject : evidencesProjects) {
            if (evidenceProject!=null && idEvidence.equals(evidenceProject.getNameEvidence())) {
                return evidenceProject;
            }
        }
        main.showText("We couldn't find any evidence with that name :(\n");
        return null;
    }
}

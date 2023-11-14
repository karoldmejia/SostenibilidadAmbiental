package model;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;

/**
 * The ProjectManagementService class manages operations related to projects within the system.
 * It handles the creation, update, deletion, and querying of projects, as well as
 * managing associated data gatherers and their contributions to specific projects.
 */
public class ProjectManagementService{
    static ArrayList<Project> projects = new ArrayList<>();

    /**
     * Initializes and populates the projects list with default projects and their associated data.
     * @pre None.
     * @post The projects list is populated with default projects and their associated data.
     */
    public void initializeProjects() {
        Project project1 = new Project("uu", "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu", Pilar.Biodiversidad, "01/01/2001", "01/01/2002", true);
        Project project2 = new Project("ii", "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", Pilar.Energia, "01/01/2001", "01/01/2002", true);
        projects.add(project1);
        projects.add(project2);

        String[] list = new String[2];
        list[0] = "ww";
        list[1] = "ww";
        EvidenceProject evidence1=new Evidence("pp", "01/01/2001", true, CharTypeEvidence.A, "ww");
        EvidenceProject evidence2=new Review("dd", "01/01/2001", true, CharTypeEvidence.R, false, list);
        EvidenceProject evidence3=new Evidence("qq", "01/01/2001", true, CharTypeEvidence.T, "ww");
        project1.getEvidences().add(evidence1);
        project1.getEvidences().add(evidence2);

        InterestPoint interestPoint1 = new InterestPoint("qq", 12, 12, "eoiroir");
        InterestPoint interestPoint2 = new InterestPoint("oo", 3, 3, "wV3g5H");
        MapUniversity.addInterestPoint(interestPoint1);
        MapUniversity.addInterestPoint(interestPoint2);
        interestPoint1.getEvidences().add(evidence1);
        interestPoint2.getEvidences().add(evidence2);

    }

    // Main methods --------------------------------------------

    String pilarOptions= "Please, select which pilar this project belongs to:\n1. Biodiversidad\n2. Gestión del recurso hídrico\n3. Gestión integral de residuos sólidos\n4. Energía\n";
    String statusOptions="Please insert 'y' if this project is available, and any other letter for otherwise\n";

    /**
     * Creates a new project.
     * @pre None.
     * @post A new project is created if all inputs are valid.
     */
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
                UserInteraction.showText("Project added successfully!");
            }
        }
    }

    /**
     * Queries and displays details of a project based on its ID.
     * @param idProject The ID of the project to be queried.
     * @pre The `idProject` should not be null.
     * @post Details of the queried project are displayed.
     */
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

    /**
     * Updates the details of a specific project based on its ID.
     * @param idProject The ID of the project to be updated.
     * @pre The `idProject` should not be null.
     * @post The details of the project are updated if changes are applied.
     */
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

    /**
     * Deletes a project based on its ID.
     * @param idProject The ID of the project to be deleted.
     * @pre The `idProject` should not be null.
     * @post The project is deleted if found in the projects list.
     */
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
            UserInteraction.showText("Project successfully deleted.");
            return;
        }
        UserInteraction.showText("We couldn't find any project");
    }

    /**
     * Links a data gatherer to a specific project.
     * @param idProject The ID of the project.
     * @param idDataGatherer The ID of the data gatherer.
     * @pre The `idProject` and `idDataGatherer` should not be null.
     * @post The data gatherer is linked to the project if found.
     */
    public void linkDataGatherersToProject(String idProject, String idDataGatherer){
        Project project=searchProject(idProject);
        if (project!=null){
            DataGatherer dataGatherer=UserCredentialService.searchDataGatherer(idDataGatherer);
            if(dataGatherer!=null){
                project.getAssociatedDataGatherers().add(dataGatherer);
                UserInteraction.showText("Data gatherer has been successfully linked!\n");
            }
        }
    }

    // Support methods --------------------------------------------

    /**
     * Validates whether the entered project name is unique.
     * @param nameProject The name of the project to validate.
     * @pre The `nameProject` should not be null.
     * @post Returns true if the name is unique; otherwise, returns false and displays a message.
     */
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

    /**
     * Validates the length of the project description.
     * @param description The description of the project.
     * @pre The `description` should not be null.
     * @post Returns true if the description length is valid; otherwise, returns false and displays a message.
     */
    private boolean isDescriptionValid(String description){
        if (description.length() >= 20) {
            return true;
        } else {
            UserInteraction.showText("Description length should be greater than 20 characters\n");
            return false;
        }
    }

    /**
     * Validates and returns the type of Pilar based on the ID.
     * @param pilarId The ID representing the Pilar.
     * @pre None.
     * @post Returns the Pilar type if valid; otherwise, displays an error message and returns null.
     */
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
            UserInteraction.showText("The pillar option entered is not valid\n");
            return null;
        }
        return pilar;
    }

    /**
     * Validates the entered date format.
     * @param date The date to validate.
     * @pre The `date` should be in the format "dd/MM/yyyy".
     * @post Returns true if the date format is valid; otherwise, returns false and displays a message.
     */
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
            UserInteraction.showText("The date format entered is not valid\n");
            return false;
        }
    }

    /**
     * Validates if the second date is after the first date.
     * @param firstDate The first date.
     * @param secondDate The second date.
     * @pre The `firstDate` and `secondDate` should be in the format "dd/MM/yyyy".
     * @post Returns true if the second date is after the first date; otherwise, returns false and displays a message.
     */
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
            UserInteraction.showText("The end date is before the start date\n");
            return false;
        }
    }

    /**
     * Checks the status of the project.
     * @param optStatus The status string to check.
     * @pre None.
     * @post Returns true if the status is 'y'; otherwise, returns false.
     */
    private boolean checkStatus(String optStatus){
        return optStatus.equalsIgnoreCase("y");
    }

    /**
     * Searches for a project by its ID.
     * @param idProject The ID of the project to search.
     * @pre The `idProject` should not be null.
     * @post Returns the project if found; otherwise, displays a message and returns null.
     */
    protected Project searchProject(String idProject) {
        for (Project project : projects) {
            if (project!=null && idProject.equals(project.getNameProject())) {
                return project;
            }
        }
        UserInteraction.showText("We couldn't find any project with that name :(\n");
        return null;
    }

    /**
     * Retrieves the names of all available projects.
     * @pre None.
     * @post Returns a string containing the names of all projects.
     */
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

    /**
     * Updates the name of the given project.
     * @param project The project to update.
     * @pre None.
     * @post The project's name will be updated if the new name is valid.
     */
    private void updateNameProject(Project project){
        String nameProject = UserInteraction.getInputString("Enter the new name: ");
        if (isNameValid(nameProject)) {
            project.setNameProject(nameProject);
        }
    }

    /**
     * Updates the description of the given project.
     * @param project The project to update.
     * @pre None.
     * @post The project's description will be updated if the new description is valid.
     */
    private void updateDescriptionProject(Project project){
        String description = UserInteraction.getInputString("Enter the new description: ");
        if (isDescriptionValid(description)) {
            project.setDescription(description);
        }
    }

    /**
     * Updates the Pilar of the given project.
     * @param project The project to update.
     * @pre None.
     * @post The project's Pilar will be updated if the entered ID is valid.
     */
    private void updatePilarProject(Project project){
        int idPilar = UserInteraction.getInputInt(pilarOptions);
        if (isPilarValid(idPilar)!=null) {
            Pilar pilar = isPilarValid(idPilar);
            project.setPilar(pilar);
        }
    }

    /**
     * Updates the initial date of the given project.
     * @param project The project to update.
     * @pre None.
     * @post The project's initial date will be updated if the new date is valid and after the final date.
     */
    private void updateInitialDateProject(Project project){
        String initialDate = UserInteraction.getInputString("Enter the new description: ");
        if (isDateValid(initialDate) && isSecondDateGreaterThanFirst(initialDate,project.getFinalDate())) {
            project.setInitialDate(initialDate);
        }
    }

    /**
     * Updates the final date of the given project.
     * @param project The project to update.
     * @pre None.
     * @post The project's final date will be updated if the new date is valid and after the initial date.
     */
    private void updateFinalDateProject(Project project){
        String finalDate = UserInteraction.getInputString("Enter the new description: ");
        if (isDateValid(finalDate) && isSecondDateGreaterThanFirst(project.getInitialDate(),finalDate)) {
            project.setFinalDate(finalDate);
        }
    }

    /**
     * Updates the status of the given project.
     * @param project The project to update.
     * @pre None.
     * @post The project's status will be updated based on the provided input.
     */
    private void updateStatusProject(Project project){
        String idStatus = UserInteraction.getInputString(statusOptions);
        boolean status = checkStatus(idStatus);
        project.setStatus(status);
    }

    /**
     * Finds the project associated with the given evidence.
     * @param evidenceProject The evidence to search for.
     * @pre None.
     * @post Returns the name of the project associated with the evidence if found; otherwise, returns null.
     */
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

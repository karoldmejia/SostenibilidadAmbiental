package model;
import java.util.ArrayList;

/**
 * Controller class handles the main logic and user interactions of the system.
 */
public class Controller{
    private UserCredentialService userCredentialService;
    private ProjectManagementService projectManagementService;

    /**
     * Constructor initializing UserCredentialService and ProjectManagementService.
     * Initializes users and projects.
     * @pre None.
     * @post UserCredentialService and ProjectManagementService are initialized.
     */
    public Controller(){
        this.userCredentialService=new UserCredentialService();
        this.projectManagementService=new ProjectManagementService();
        userCredentialService.initializeUsers();
        projectManagementService.initializeProjects();
    }

    /**
     * Allows user registration as a Visitor, Data Gatherer, or Researcher.
     * @pre None.
     * @post A new user of the selected type is registered.
     */
    public void registerUser() {
        int optUser = -1;
        while (optUser < 1 || optUser > 3) {
            optUser = UserInteraction.getInputInt("Please, select which user you want to create: \n1. Visitor \n2. Data gatherer \n3. Researcher\n");
            if (optUser < 1 || optUser > 3) {
                UserInteraction.showText("Please select a valid option (1, 2, or 3).");
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

    /**
     * Facilitates the login process for users.
     * @pre None.
     * @post User is logged in if credentials are valid.
     */
    public void loginUser(){
        UserInteraction.showText("Log in!\n");
        String username=UserInteraction.getInputString("Please, enter your name: ");
        String password=UserInteraction.getInputString("Now your password: ");
        userCredentialService.loginUser(username,password);
    }

    /**
     * Provides options to filter by Pillar or Project and visualizes the map accordingly.
     * @pre None.
     * @post Displays filtered map data based on user choice.
     */
    public void visualizeMap() {
        int filterOption = UserInteraction.getInputInt("Do you wish to filter... \n1. Pillars\n2. Projects\n");

        if (filterOption == 1) {
            filterByPillar();
        } else if (filterOption == 2) {
            filterByProject();
        } else {
            UserInteraction.showText("Invalid option :(\n");
        }

        String checkDecision = "o";
        while (!checkDecision.equals("y")) {
            checkDecision = UserInteraction.getInputString("Would you like to inquire about any point of interest? Press 'y' or any other letter for otherwise\n");
            if (checkDecision.equalsIgnoreCase("y")) {
                int x = UserInteraction.getInputInt("Insert coordinate x: ");
                int y = UserInteraction.getInputInt("Insert coordinate y: ");
                MapUniversity.showPoint(x, y);
            } else {
                break;
            }
        }
    }

    /**
     * Filters projects based on the selected pillar and displays the map with related projects.
     * @pre None.
     * @post The map displays the projects related to the selected pillar.
     */
    private void filterByPillar() {
        int pillarOpt = UserInteraction.getInputInt("Select a pillar: \n1. Biodiversidad\n2. Gestión del recurso hídrico\n3. Gestión integral de residuos sólidos\n4. Energía\n");
        Pilar pillar;

        switch (pillarOpt) {
            case 1:
                pillar = Pilar.Biodiversidad;
                break;
            case 2:
                pillar = Pilar.Gestion_del_recurso_hidrico;
                break;
            case 3:
                pillar = Pilar.Gestion_integral_de_residuos_solidos;
                break;
            case 4:
                pillar = Pilar.Energia;
                break;
            default:
                UserInteraction.showText("Invalid option :(\n");
                return;
        }

        ArrayList<Project> relatedProjects = new ArrayList<>();
        for (Project project : projectManagementService.projects) {
            if (project.getPilar() == pillar) {
                relatedProjects.add(project);
            }
        }
        if (!relatedProjects.isEmpty()) {
            MapUniversity.showMap(relatedProjects);
        } else {
            UserInteraction.showText("No projects found for the selected pillar :(\n");
        }
    }

    /**
     * Filters and displays the map for a selected project.
     * @pre None.
     * @post The map shows the selected project if available.
     */
    private void filterByProject() {
        if (projectManagementService.projects.isEmpty()) {
            UserInteraction.showText("Projects have not been registered yet :(\n");
            return;
        }
        String idProject = showSelectProjects();
        Project project = projectManagementService.searchProject(idProject);
        if (project != null) {
            ArrayList<Project> projectsToDisplay = new ArrayList<>();
            projectsToDisplay.add(project);
            MapUniversity.showMap(projectsToDisplay);
        }
    }

    /**
     * Displays the list of registered projects and prompts the user to select one.
     * @pre None.
     * @post The selected project's name is returned for filtering.
     * @return The name of the selected project.
     */
    private String showSelectProjects(){
        UserInteraction.showText("These are the registered projects: " + projectManagementService.getAllProjectsNames());
        String idProject = UserInteraction.getInputString("\nPlease insert the name of one of them: ");
        return idProject;
    }

    /**
     * Facilitates the creation of a project.
     * @pre None.
     * @post A new project is created and added to the project collection.
     */
    public void createProject(){
        projectManagementService.createProject();
    }

    /**
     * Displays the details of a specific project if available.
     * @pre None.
     * @post Displays project details.
     */
    public void seeProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            projectManagementService.queryProject(idProject);
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Updates an existing project's details if available.
     * @pre None.
     * @post Updates project details if applicable.
     */
    public void updateProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            projectManagementService.updateProject(idProject);
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Deletes a specific project if available.
     * @pre None.
     * @post Deletes the specified project if confirmed.
     */
    public void deleteProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            String checkDecision=UserInteraction.getInputString("Are you sure you want to delete the project? Press 'yes' or any other letter for otherwise\n");
            if (checkDecision.equalsIgnoreCase("yes")){
                projectManagementService.deleteProject(idProject);
            } else {
                UserInteraction.showText("Okey, let's get back to the menu!\n");
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Creates evidence associated with a selected project according to the user type.
     * @param userType - Type of the user creating the evidence.
     * @pre A project is selected to add evidence.
     * @post New evidence is created and linked to the project.
     */
    public void createEvidence(int userType){
        if (!projectManagementService.projects.isEmpty()) {
            String idProject=showSelectProjects();
            Project project=projectManagementService.searchProject(idProject);
            if (project!=null){
                project.createEvidence(userType, project);
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Allows updating existing evidence linked to a project.
     * @pre A project with evidence exists for updating.
     * @post The selected evidence is updated if available.
     */
    public void updateEvidence() {
        if (!projectManagementService.projects.isEmpty()) {
            String idProject = showSelectProjects();
            Project project = projectManagementService.searchProject(idProject);
            if (project != null) {
                if (!project.evidences.isEmpty()) {
                    UserInteraction.showText("These are the registered evidences: " + project.getAllEvidencesNames());
                    String idEvidence = UserInteraction.getInputString("\nPlease insert the name of one of them: ");
                    project.updateEvidence(idEvidence);
                } else {
                    UserInteraction.showText("Evidences have not been registered yet :(");
                }
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Deactivates or activates a piece of evidence linked to a project.
     * @pre A project and its evidence exist for deactivation.
     * @post The selected evidence is deactivated or activated as per the user's choice.
     */
    public void deactivateEvidence(){
        if (!projectManagementService.projects.isEmpty()) {
            String idProject = showSelectProjects();
            Project project = projectManagementService.searchProject(idProject);
            if (project != null) {
                if (!project.evidences.isEmpty()) {
                    UserInteraction.showText("These are the registered evidences: " + project.getAllEvidencesNames());
                    String idEvidence = UserInteraction.getInputString("\nPlease insert the name of one of them: ");
                    project.ActivateDeactivateEvidence(idEvidence);
                } else {
                    UserInteraction.showText("Evidences have not been registered yet :(");
                }
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Links a data gatherer to a specific project.
     * @pre A project exists for data gatherer linking.
     * @post Data gatherer is successfully linked to the project.
     */
    public void linkDataGatherersToProject(){
        if (!projectManagementService.projects.isEmpty()) {
            String idProject=showSelectProjects();
            String idDataGatherer = UserInteraction.getInputString("Please insert data gatherer's name: ");
            projectManagementService.linkDataGatherersToProject(idProject,idDataGatherer);
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Initiates the reviewing process for the reviews of a selected project.
     * @pre A project is selected to review its reviews.
     * @post Reviews for the project are accessed for further action.
     */
    public void reviewEvidences(){
        if (!projectManagementService.projects.isEmpty()) {
            UserInteraction.showText("Select a project to review its reviews\n");
            String idProject=showSelectProjects();
            Project project=projectManagementService.searchProject(idProject);
            if (project!=null){
                project.reviewReviews(project);
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    /**
     * Allows updating an interest point's location on the map.
     * @pre Coordinates are given for an existing interest point.
     * @post The interest point's location is updated.
     */
    public void updateInterestPoint() {
        UserInteraction.showText("Please insert point's location\n");
        int x=UserInteraction.getInputInt("Insert coordinate x: ");
        int y=UserInteraction.getInputInt("Insert coordinate y: ");
        MapUniversity.updatePoint(x,y);
    }

    /**
     * Allows the deletion of an interest point from the map.
     * @pre Coordinates are given for an existing interest point.
     * @post The interest point is deleted if confirmed.
     */
    public void deleteInterestPoint() {
        UserInteraction.showText("Please insert point's location\n");
        int x=UserInteraction.getInputInt("Insert coordinate x: ");
        int y=UserInteraction.getInputInt("Insert coordinate y: ");
        String checkDecision = UserInteraction.getInputString("Are you sure you want to delete the point of interest? Type 'yes' or any other character to cancel\n");
        if (checkDecision.equalsIgnoreCase("yes")) {
            MapUniversity.deletePoint(x,y);
        } else {
            UserInteraction.showText("Okey, let's get back to the menu!\n");
        }
    }


}

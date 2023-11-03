package model;
public class Controller{
    private UserCredentialService userCredentialService;
    private ProjectManagementService projectManagementService;
    public Controller(){
        this.userCredentialService=new UserCredentialService();
        this.projectManagementService=new ProjectManagementService();
        userCredentialService.initializeUsers();
        projectManagementService.initializeProjects();
    }


    public void registerUser() {
        int optUser = -1;
        while (optUser < 1 || optUser > 3) {
            optUser = UserInteraction.getInputInt("Please, select which user you want to create: \n1. Visitor \n2. Data gatherer \n3. Researcher\n");
            if (optUser < 1 || optUser > 3) {
                UserInteraction.showText("Por favor, selecciona una opción válida (1, 2 o 3).");
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
        UserInteraction.showText("Log in!\n");
        String username=UserInteraction.getInputString("Please, enter your name: ");
        String password=UserInteraction.getInputString("Now your password: ");
        userCredentialService.loginUser(username,password);
    }

    public void visualizeMap() {
        int filterOption = UserInteraction.getInputInt("You wish to filter... \n1. Pillars\n2. Projects\n");

        if (filterOption == 1) {
            filterByPillar();
        } else if (filterOption == 2) {
            filterByProject();
        } else {
            UserInteraction.showText("Invalid option :(\n");
        }

        String checkDecision = "o";
        while (checkDecision != "y") {
            checkDecision = UserInteraction.getInputString("¿Desea consultar algún punto de interés? Press 'y' or any other letter for otherwise\n");
            if (checkDecision.equalsIgnoreCase("y")) {
                int x = UserInteraction.getInputInt("Insert coordinate x: ");
                int y = UserInteraction.getInputInt("Insert coordinate y: ");
                MapUniversity.showPoint(x, y);
            } else {
                break;
            }
        }
    }

    private void filterByPillar() {
        int pillarOpt = UserInteraction.getInputInt("Select a pillar: \n1. Biodiversidad\n2. Gestión del recurso hídrico\n3. Gestión integral de residuos sólidos\n4. Energía\n");
        Pilar pillar = null;

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
        boolean projectFound = false;
        for (Project project : projectManagementService.projects) {
            if (project.getPilar() == pillar) {
                MapUniversity.showMap(project);
                projectFound = true;
            }
        }
        if (!projectFound) {
            UserInteraction.showText("No projects found for the selected pillar :(\n");
        }
    }

    private void filterByProject() {
        if (projectManagementService.projects.isEmpty()) {
            UserInteraction.showText("Projects have not been registered yet :(\n");
            return;
        }
        String idProject = showSelectProjects();
        Project project = projectManagementService.searchProject(idProject);
        if (project != null) {
            MapUniversity.showMap(project);
        }
    }


    private String showSelectProjects(){
        UserInteraction.showText("These are the registered projects: " + projectManagementService.getAllProjectsNames());
        String idProject = UserInteraction.getInputString("\nPlease insert the name of one of them: ");
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
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }
    public void updateProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            projectManagementService.updateProject(idProject);
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }
    public void deleteProject(){
        if (projectManagementService.projects.size()>0) {
            String idProject=showSelectProjects();
            String checkDecision=UserInteraction.getInputString("¿Está usted seguro que desea eliminar el proyecto? Press 'si' or any other letter for otherwise\n");
            if (checkDecision.equalsIgnoreCase("si")){
                projectManagementService.deleteProject(idProject);
            } else {
                UserInteraction.showText("Okey, let's get back to the menu!\n");
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }
    public void createEvidence(int userType){
        if (!projectManagementService.projects.isEmpty()) {
            String idProject=showSelectProjects();
            Project project=projectManagementService.searchProject(idProject);
            if (project!=null){
                project.createEvidence(userType);
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }
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
    public void linkDataGatherersToProject(){
        if (!projectManagementService.projects.isEmpty()) {
            String idProject=showSelectProjects();
            String idDataGatherer = UserInteraction.getInputString("Please insert data gatherer's name: ");
            projectManagementService.linkDataGatherersToProject(idProject,idDataGatherer);
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }
    public void reviewEvidences(){
        if (!projectManagementService.projects.isEmpty()) {
            UserInteraction.showText("Elige un proyecto para revisar sus reviews\n");
            String idProject=showSelectProjects();
            Project project=projectManagementService.searchProject(idProject);
            if (project!=null){
                project.reviewReviews();
            }
        } else {
            UserInteraction.showText("Projects have not been registered yet :(");
        }
    }

    public void updateInterestPoint() {
        UserInteraction.showText("Please insert point's location\n");
        int x=UserInteraction.getInputInt("Insert coordinate x: ");
        int y=UserInteraction.getInputInt("Insert coordinate y: ");
        MapUniversity.updatePoint(x,y);
    }

    public void deleteInterestPoint() {
        UserInteraction.showText("Please insert point's location\n");
        int x=UserInteraction.getInputInt("Insert coordinate x: ");
        int y=UserInteraction.getInputInt("Insert coordinate y: ");
        String checkDecision = UserInteraction.getInputString("¿Está usted seguro que desea eliminar el punto de interés? Press 'si' or any other letter for otherwise\n");
        if (checkDecision.equalsIgnoreCase("si")) {
            MapUniversity.deletePoint(x,y);
        } else {
            UserInteraction.showText("Okey, let's get back to the menu!\n");
        }
    }


}

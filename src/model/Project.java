package model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Project{
    private static int nextId = 1;
    int idProject;
    String nameProject, description, initialDate, finalDate;
    ArrayList<DataGatherer> associatedDataGatherers;
    Pilar pilar;
    boolean status;
    ArrayList<EvidenceProject> evidences;

    /**
     * Constructor de la clase Project.
     *
     * @param nameProject       El nombre del proyecto.
     * @param description       La descripción del proyecto.
     * @param pilar             El pilar al que pertenece el proyecto.
     * @param initialDate       La fecha de inicio del proyecto.
     * @param finalDate         La fecha de finalización del proyecto.
     * @param status            El estado del proyecto (activo o inactivo).
     * @pre Ninguna.
     * @post Se crea una instancia válida de la clase Project con los parámetros proporcionados.
     */
    Project(String nameProject,String description,Pilar pilar,String initialDate,String finalDate,boolean status){
        idProject=nextId++;
        this.nameProject=nameProject;
        this.description=description;
        this.associatedDataGatherers = new ArrayList<>();
        this.evidences = new ArrayList<>();
        this.pilar=pilar;
        this.initialDate=initialDate;
        this.finalDate=finalDate;
        this.status=status;
    }

    // Getters
    public String getNameProject() {
        return nameProject;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<DataGatherer> getAssociatedDataGatherers() {
        return associatedDataGatherers;
    }

    public Pilar getPilar() {
        return pilar;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }
    public boolean getStatus() {
        return status;
    }
    public ArrayList<EvidenceProject> getEvidences() {
        return evidences;
    }
    // Setters


    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPilar(Pilar pilar) {
        this.pilar = pilar;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Evidences methods
    // Main methods ---------------------------------------------------

    String typeEvidenceOpt= "Please, select evidence's type:\n1. Audio (A)\n2. Video (V)\n3. Photo (P)\n4. Text (T)\n5. Results report (RR)\n";

    /**
     * Creates an evidence based on user input and associates it with the project, adhering to specific user privileges.
     *
     * @param userType   Type of the user creating the evidence.
     * @param project    The project to associate the evidence with.
     * @pre The user must have appropriate privileges. The evidence name and registration date must be valid.
     * @post If requirements are met, an evidence is created and linked to the project.
     */
    public void createEvidence(int userType, Project project){
        boolean flag=false;
        int optEvidence=0;
        String nameEvidence=null, registerDate=null;
        if (userType==3) {
            optEvidence = UserInteraction.getInputInt("Do you wish to add:\n1. Review\n2. Other evidence\n");
        } else if (userType==2) {
            optEvidence=1;
            if (!(UserCredentialService.searchDataGathererAssociated(getAssociatedDataGatherers()))){
                return;
            }
        }
        while (!flag){
            nameEvidence= UserInteraction.getInputString("Enter the name: ");
            registerDate = UserInteraction.getInputString("Enter the registration date: ");
            if (isNameValid(nameEvidence) && isDateValid(registerDate) && getStatus()){
                flag=true;
            } else if (!getStatus()) {
                UserInteraction.showText("I'm sorry, you can't add evidences to this project because it isn't available anymore\n");
                return;
            }
        }
        if (flag) {
            if ((userType == 2 || userType == 3) && optEvidence == 1) {
                createReview(nameEvidence, registerDate, userType, project);
            } else if (userType == 3 && optEvidence == 2) {
                createCommonEvidence(nameEvidence, registerDate, project);
            }
        }
    }

    /**
     * Creates a review for the project, based on the given information.
     *
     * @param nameEvidence   The name of the review.
     * @param registerDate   The registration date of the review.
     * @param userType       Type of the user creating the review.
     * @param project        The project to associate the review with.
     * @pre Valid user and project status, adequate review information.
     * @post A new review is added to the project if requirements are met.
     */
    public void createReview(String nameEvidence, String registerDate, int userType, Project project){
        boolean evidenceStatus=false;
        int cantUrl=UserInteraction.getInputInt("How many URLs you with to add?: ");
        String[] listUrl=new String[cantUrl];
        if (userType==3) {
            evidenceStatus=true;
        }
        for (int i=0;i<cantUrl;i++){
            listUrl[i]=UserInteraction.getInputString("Evidence no. "+(i+1)+": ");
        }
        CharTypeEvidence typeEvidence=CharTypeEvidence.R;
        Review review=new Review(nameEvidence, registerDate, true, typeEvidence, evidenceStatus, listUrl);
        project.getEvidences().add(review);
        linkInterestPoint(review);
        if (!evidenceStatus){
            UserInteraction.showText("Successful creation, now your review is pending approval!\n");
        } else {
            UserInteraction.showText("Review successfully created!\n");
        }
    }

    /**
     * Creates common evidence based on the provided details and associates it with the project.
     *
     * @param nameEvidence   The name of the evidence.
     * @param registerDate   The registration date of the evidence.
     * @param project        The project to associate the evidence with.
     * @pre Valid user input and project status, adequate evidence information.
     * @post A new common evidence is added to the project if requirements are met.
     */
    private void createCommonEvidence(String nameEvidence, String registerDate, Project project){
        String url= UserInteraction.getInputString("Enter the URL: ");
        int charTypeEvidence=UserInteraction.getInputInt(typeEvidenceOpt);
        CharTypeEvidence typeEvidence=selectTypeEvidence(charTypeEvidence);
        if (typeEvidence!=null){
            Evidence evidence = new Evidence(nameEvidence,registerDate,true,typeEvidence,url);
            project.getEvidences().add(evidence);
            linkInterestPoint(evidence);
            UserInteraction.showText("Review successfully created!\n");
        }
    }

    /**
     * Updates the details of an existing evidence in the project.
     *
     * @param idEvidence   The identifier of the evidence to be updated.
     * @pre The evidence should exist within the project.
     * @post The attributes of the selected evidence are modified if the evidence is found.
     */
    public void updateEvidence(String idEvidence){
        EvidenceProject evidence=searchEvidence(idEvidence);
        if (evidence == null) {
            return;
        }
        while (true) {
            UserInteraction.showText("Select the attribute to modify: \n1. Name\n2. Registration date\n");
            if (evidence instanceof Evidence){
                UserInteraction.showText("3. Url\n");
            } else if (evidence instanceof Review) {
                UserInteraction.showText("3. Url from list\n");
            }
            int opcion = UserInteraction.getInputInt("\nEnter the number of the desired option (0 to finish and apply changes): ");
            if (opcion == 0) {
                break;
            }
            switch (opcion) {
                case 1:
                    updateNameEvidence(evidence);
                    break;
                case 2:
                    updateDateEvidence(evidence);
                    break;
            }
            if (evidence instanceof Evidence && opcion==3){
                updateSingleUrl(evidence);
            } else if (evidence instanceof Review && opcion==3) {
                updateMultipleUrl(evidence);
            } else {
                UserInteraction.showText("Invalid option.");
            }
        }
        UserInteraction.showText("Changes applied successfully.");
    }

    /**
     * Toggles the availability status of an evidence in the project.
     *
     * @param idEvidence   The identifier of the evidence to activate/deactivate.
     * @pre The evidence should exist within the project.
     * @post The availability of the evidence is altered based on the current state.
     */
    public void ActivateDeactivateEvidence(String idEvidence){
        EvidenceProject evidence=searchEvidence(idEvidence);
        if (evidence!=null) {
            if (evidence.getAvailability()){
                evidence.setAvailability(false);
                UserInteraction.showText("Evidence deactivated.");
                return;
            } else{
                evidence.setAvailability(true);
                UserInteraction.showText("Evidence activated.");
                return;
            }

        }
        UserInteraction.showText("We couldn't find any evidence");
    }

    /**
     * Reviews pending unreviewed reviews associated with the project.
     *
     * @param project   The project to review pending unreviewed reviews.
     * @pre The project must contain pending reviews.
     * @post The selected reviews are either approved or disapproved based on the reviewer's choice.
     */
    public void reviewReviews(Project project) {
        ArrayList<Review> unreviewedReviews = new ArrayList<>();
        for (EvidenceProject evidence : project.getEvidences()) {
            if (evidence instanceof Review && !((Review) evidence).getEvidenceStatus()) {
                unreviewedReviews.add((Review) evidence);
            }
        }
        if (unreviewedReviews.isEmpty()) {
            UserInteraction.showText("No pending review found for review :)\n");
            return;
        }
        UserInteraction.showText("Please select the review you'd like to check, or press 0 to exit:\n");
        int optReview = -1;
        while (optReview != 0) {
            for (int i = 0; i < unreviewedReviews.size(); i++) {
                UserInteraction.showText((i + 1) + ". " + unreviewedReviews.get(i).getNameEvidence()+"\n");
            }
            optReview = UserInteraction.getInputInt("Please enter your choice: ");
            if (optReview == 0) {
                break;
            }
            if (optReview < 1 || optReview > unreviewedReviews.size()) {
                UserInteraction.showText("Invalid option. Please choose a valid option.\n");
            } else {
                Review selectedReview = unreviewedReviews.get(optReview - 1);
                int markReview = UserInteraction.getInputInt("Would you like to?\n1. Approve\n2. Disapprove\nEnter your choice: ");
                if (markReview == 1) {
                    selectedReview.setEvidenceStatus(true);
                    unreviewedReviews.remove(selectedReview);
                    UserInteraction.showText("Review approved!\n");
                } else if (markReview == 2) {
                    project.getEvidences().remove(selectedReview);
                    unreviewedReviews.remove(selectedReview);
                    selectedReview=null;
                    UserInteraction.showText("Review disapproved!\n");
                } else {
                    UserInteraction.showText("Invalid option. Please, select 1 or 2.\n");
                }
            }
        }
    }

    // Main methods ---------------------------------------------------

    /**
     * Searches for an evidence in the collection based on the provided evidence name.
     *
     * @param idEvidence The name of the evidence to search for.
     * @return The found evidence; otherwise, null if not found.
     */
    private EvidenceProject searchEvidence(String idEvidence) {
        for (EvidenceProject evidenceProject : evidences) {
            if (evidenceProject!=null && idEvidence.equals(evidenceProject.getNameEvidence())) {
                return evidenceProject;
            }
        }
        UserInteraction.showText("We couldn't find any evidence with that name :(\n");
        return null;
    }

    /**
     * Checks the validity of a provided evidence name against the existing collection.
     *
     * @param nameProject The name of the evidence to be validated.
     * @return True if the name is unique; otherwise, false.
     */
    private boolean isNameValid(String nameProject) {
        boolean flag=false;
        for (EvidenceProject evidenceProject : evidences) {
            if (evidenceProject != null && nameProject.equals(evidenceProject.getNameEvidence())) {
                flag=true;
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
     * Validates the date format to ensure its correctness.
     *
     * @param date The date to be validated.
     * @return True if the date is in a valid format; otherwise, false.
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
            UserInteraction.showText("The date format entered isn't valid\n");
            return false;
        }
    }

    /**
     * Selects the evidence type based on the given type ID.
     *
     * @param typeEvidenceId The ID representing the type of evidence.
     * @return The evidence type.
     */
    private CharTypeEvidence selectTypeEvidence(int typeEvidenceId){
        CharTypeEvidence typeEvidence;
        if (typeEvidenceId==1) {
            typeEvidence = CharTypeEvidence.A;
        } else if (typeEvidenceId==2) {
            typeEvidence = CharTypeEvidence.V;
        } else if (typeEvidenceId==3) {
            typeEvidence = CharTypeEvidence.P;
        } else if (typeEvidenceId==4) {
            typeEvidence = CharTypeEvidence.T;
        } else if (typeEvidenceId==5) {
        typeEvidence = CharTypeEvidence.RR;
        } else {
            UserInteraction.showText("La opción ingresada no es válida\n");
            return null;
        }
        return typeEvidence;
    }

    /**
     * Updates the name of the evidence.
     *
     * @param evidenceProject The evidence to update.
     */
    private void updateNameEvidence(EvidenceProject evidenceProject){
        String name = UserInteraction.getInputString("Enter the new name: ");
        if (isNameValid(name)) {
            evidenceProject.setNameEvidence(name);
        }
    }

    /**
     * Updates the registration date of the evidence.
     *
     * @param evidenceProject The evidence to update.
     */
    private void updateDateEvidence(EvidenceProject evidenceProject){
        String date = UserInteraction.getInputString("Enter the new registration date: ");
        if (isDateValid(date)) {
            evidenceProject.setRegisterDate(date);
        }
    }

    /**
     * Updates the URL of a single URL-based evidence.
     *
     * @param evidenceProject The single URL evidence to update.
     */
    private void updateSingleUrl(EvidenceProject evidenceProject){
        String url = UserInteraction.getInputString("Enter the new url: ");
        Evidence evidence = (Evidence) evidenceProject;
        evidence.setUrl(url);
    }

    /**
     * Updates multiple URLs of a review-based evidence.
     *
     * @param evidenceProject The review-based evidence to update.
     */
    private void updateMultipleUrl(EvidenceProject evidenceProject){
        Review review=(Review) evidenceProject;
        int optUrl=-1;
        while (optUrl!=0) {
            UserInteraction.showText("Please choose which URL you'd like to change\n");
            for (int i = 0; i < review.getListUrl().length; i++) {
                UserInteraction.showText((i+1) + ". " + review.getListUrl()[i]+"\n");
            }
            optUrl = UserInteraction.getInputInt("Enter your choice or '0' to exit: ");
            if (optUrl>=1 && optUrl<=review.getListUrl().length){
                review.getListUrl()[(optUrl-1)] = UserInteraction.getInputString("Enter new url: ");
            } else {
                UserInteraction.showText("Please input a valid option");
            }
        }
    }

    /**
     * Counts the number of evidences and reviews in the project.
     *
     * @return An array with the count of evidences and reviews respectively.
     */
    protected int[] countEvidences(){
        int[] cantEvidencesProject=new int[2];
        int cEvidences=0;
        int cReviews=0;
        for (EvidenceProject evidence: evidences) {
            if (evidence !=null && evidence.getAvailability() ){
                if (evidence instanceof Evidence){
                    cEvidences++;
                } else if (evidence instanceof Review && ((Review) evidence).getEvidenceStatus()) {
                    cReviews++;
                }
            }
        }
        cantEvidencesProject[0]=cEvidences;
        cantEvidencesProject[1]=cReviews;
        return cantEvidencesProject;
    }

    /**
     * Retrieves the names of all evidences associated with the project.
     *
     * @return A string containing all evidence names.
     */
    protected String getAllEvidencesNames() {
        StringBuilder evidencesNames = new StringBuilder();
        for (int i = 0; i < evidences.size(); i++) {
            EvidenceProject evidenceProject = evidences.get(i);
            String name = evidenceProject.getNameEvidence();
            evidencesNames.append(name);
            if (i < evidences.size() - 1) {
                evidencesNames.append(", ");
            }
        }
        return evidencesNames.toString();
    }

    /**
     * Links a point of interest to the evidence.
     *
     * @param evidence The evidence to link to a point of interest.
     */
    private void linkInterestPoint(EvidenceProject evidence){
        String addInterestPoint = UserInteraction.getInputString("Do you want to link a point of interest to the evidence? Enter 'y' to confirm, and any other letter to cancel\n");
        if (addInterestPoint.equalsIgnoreCase("y")) {
            int axisX = UserInteraction.getInputInt("Enter the x-coordinate: ");
            int axisY = UserInteraction.getInputInt("Enter the y-coordinate: ");
            InterestPoint point = MapUniversity.getInterestPoint(axisX,axisY);
            if (point == null) {
                String namePoint = UserInteraction.getInputString("Enter the name of the point of interest: ");
                String codeQR = MapUniversity.createQR();
                while (!MapUniversity.isQRValid(codeQR)){
                    codeQR = MapUniversity.createQR();
                    MapUniversity.isQRValid(codeQR);
                }
                point = new InterestPoint(namePoint, axisX, axisY, codeQR);
                MapUniversity.addInterestPoint(point);
            }
            point.addEvidence(evidence);
            UserInteraction.showText("The evidence associated with the point of interest was successful!\n");
        }
    }

}

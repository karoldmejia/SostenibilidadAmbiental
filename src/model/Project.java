package model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * The Project class represents a project entity in the system.
 * It contains information essential for managing evidences within the system.
 */
public class Project{
    private static int nextId = 1;
    int idProject;
    String nameProject, description, initialDate, finalDate;
    ArrayList<DataGatherer> associatedDataGatherers;
    Pilar pilar;
    boolean status;
    ArrayList<EvidenceProject> evidences;

    /**
     * Constructor for the Project class.
     *
     * @param nameProject   The project's name.
     * @param description   The project's description.
     * @param pilar         The pillar to which the project belongs.
     * @param initialDate   The start date of the project.
     * @param finalDate     The end date of the project.
     * @param status        The status of the project (active or inactive).
     * @pre None.
     * @post A valid instance of the Project class is created with the provided parameters.
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

    /**
     * Retrieves the name of the project.
     * @return The name of the project.
     */
    public String getNameProject() {
        return nameProject;
    }

    /**
     * Retrieves the description of the project.
     * @return The description of the project.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the associated data gatherers of the project.
     * @return The list of associated data gatherers.
     */
    public ArrayList<DataGatherer> getAssociatedDataGatherers() {
        return associatedDataGatherers;
    }

    /**
     * Retrieves the category pillar of the project.
     * @return The category pillar.
     */
    public Pilar getPilar() {
        return pilar;
    }

    /**
     * Retrieves the initial date of the project.
     * @return The initial date of the project.
     */
    public String getInitialDate() {
        return initialDate;
    }

    /**
     * Retrieves the final date of the project.
     * @return The final date of the project.
     */
    public String getFinalDate() {
        return finalDate;
    }

    /**
     * Retrieves the status of the project.
     * @return The status of the project.
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Retrieves the list of evidences associated with the project.
     * @return The list of evidences.
     */
    public ArrayList<EvidenceProject> getEvidences() {
        return evidences;
    }

    /**
     * Retrieves unreviewed reviews associated with the project.
     * @return The list of unreviewed reviews.
     */
    public ArrayList<Review> getUnreviewedReviews() {
        ArrayList<Review> unreviewedReviews = new ArrayList<>();
        for (EvidenceProject evidence : evidences) {
            if (evidence instanceof Review && !((Review) evidence).getEvidenceStatus()) {
                unreviewedReviews.add((Review) evidence);
            }
        }
        return unreviewedReviews;
    }

    // Setters

    /**
     * Sets the project's name.
     * @param nameProject The new name for the project.
     */
    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    /**
     * Sets the project's description.
     * @param description The new description for the project.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the project's category pillar.
     * @param pilar The category pillar to assign to the project.
     */
    public void setPilar(Pilar pilar) {
        this.pilar = pilar;
    }

    /**
     * Sets the project's initial date.
     * @param initialDate The new initial date for the project.
     */
    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * Sets the project's final date.
     * @param finalDate The new final date for the project.
     */
    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    /**
     * Sets the project's status.
     * @param status The new status for the project.
     */
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
        int cantUrl=UserInteraction.getInputInt("How many URLs you wish to add?: ");
        String[] listUrl=new String[cantUrl];
        if (userType==3) {
            evidenceStatus=true;
        }
        for (int i=0;i<cantUrl;i++){
            listUrl[i]=UserInteraction.getInputString("URL no. "+(i+1)+": ");
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
    public void updateEvidence(String idEvidence) {
        EvidenceProject evidence = searchEvidence(idEvidence);
        if (evidence == null) {
            return;
        } else if (evidence instanceof Review && !((Review) evidence).evidenceStatus || !evidence.getAvailability()) {
            UserInteraction.showText("We couldn't find any evidence with that name :(\n");
            return;
        }
        while (true) {
            UserInteraction.showText("Select the attribute to modify: \n1. Name\n2. Registration date\n");
            if (evidence instanceof Evidence) {
                UserInteraction.showText("3. Url\n");
            } else if (evidence instanceof Review) {
                UserInteraction.showText("3. Url from list\n");
            }
            int option = UserInteraction.getInputInt("\nEnter the number of the desired option (0 to finish and apply changes): ");
            if (option == 0) {
                break;
            }
            switch (option) {
                case 1:
                    updateNameEvidence(evidence);
                    break;
                case 2:
                    updateDateEvidence(evidence);
                    break;
                case 3:
                    if (evidence instanceof Evidence) {
                        updateSingleUrl(evidence);
                    } else if (evidence instanceof Review) {
                        updateMultipleUrl(evidence);
                    }
                    break;
                default:
                    UserInteraction.showText("Invalid option.");
                    break;
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
        ArrayList<Review> unreviewedReviews = project.getUnreviewedReviews();

        if (unreviewedReviews.isEmpty()) {
            UserInteraction.showText("No pending reviews found :)\n");
            return;
        }

        while (true) {
            if (unreviewedReviews.size()==0){
                break;
            }
            for (int i = 0; i < unreviewedReviews.size(); i++) {
                UserInteraction.showText((i + 1) + ". " + unreviewedReviews.get(i).getNameEvidence() + "\n");
            }
            int optReview = UserInteraction.getInputInt("Select the review to check (0 to exit): ");
            if (optReview == 0 || optReview > unreviewedReviews.size()) {
                break;
            }
            Review selectedReview = unreviewedReviews.get(optReview - 1);
            int markReview = UserInteraction.getInputInt("Would you like to?\n1. Approve\n2. Disapprove\nEnter your choice: ");
            if (markReview == 1) {
                selectedReview.setEvidenceStatus(true);
                unreviewedReviews.remove(selectedReview);
                UserInteraction.showText("Review approved!\n");
            } else if (markReview == 2) {
                project.getEvidences().remove(selectedReview);
                UserInteraction.showText("Review disapproved!\n");
            } else {
                UserInteraction.showText("Invalid option. Please select 1 or 2.\n");
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
            UserInteraction.showText("The option entered isn't valid\n");
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
            UserInteraction.showText("Choose which URL you'd like to change\n");
            for (int i = 0; i < review.getListUrl().length; i++) {
                UserInteraction.showText((i+1) + ". " + review.getListUrl()[i]+"\n");
            }
            optUrl = UserInteraction.getInputInt("Enter your choice (0 to exit): ");
            if (optUrl>=1 && optUrl<=review.getListUrl().length){
                review.getListUrl()[(optUrl-1)] = UserInteraction.getInputString("Enter new url: ");
            } else {
                UserInteraction.showText("Please input a valid option\n");
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
        int addedNames = 0;

        for (int i = 0; i < evidences.size(); i++) {
            EvidenceProject evidenceProject = evidences.get(i);
            if (evidenceProject.getAvailability() && (evidenceProject instanceof Review && ((Review) evidenceProject).evidenceStatus) || evidenceProject instanceof Evidence) {
                String name = evidenceProject.getNameEvidence();
                evidencesNames.append(name);
                addedNames++;

                if (addedNames < countRelevantEvidences()) {
                    evidencesNames.append(", ");
                }
            }
        }
        return evidencesNames.toString();
    }

    /**
     * Counts the number of relevant evidences associated with the project.
     * Relevant evidences are either reviews or standard evidence items.
     *
     * @return The count of relevant evidences associated with the project.
     */
    private int countRelevantEvidences() {
        int count = 0;
        for (EvidenceProject evidence : evidences) {
            if ((evidence instanceof Review && ((Review) evidence).evidenceStatus) || evidence instanceof Evidence) {
                count++;
            }
        }
        return count;
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

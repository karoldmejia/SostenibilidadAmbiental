package model;
import java.util.ArrayList;

/**
 * Represents an interest point within a map, identified by its name, coordinates, and a QR code.
 * It encompasses the evidence and comments associated with this point.
 */
public class InterestPoint {
    String namePoint;
    int xAxis;
    int yAxis;
    String codeQr;
    private ArrayList<EvidenceProject> evidences;
    private ArrayList<Comment> comments;

    /**
     * Creates an interest point with specific coordinates and details.
     * @param namePoint The name of the interest point.
     * @param xAxis The x-axis coordinate of the interest point.
     * @param yAxis The y-axis coordinate of the interest point.
     * @param codeQr The QR code related to the interest point.
     * @post Initializes a new interest point with the provided details.
     */
    InterestPoint(String namePoint, int xAxis, int yAxis, String codeQr) {
        this.namePoint = namePoint;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.codeQr = codeQr;
        this.evidences = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // Getters ----------

    /**
     * Retrieves the name of the interest point.
     * @return The name of the interest point.
     * @pre The interest point must have a name set.
     */
    public String getNamePoint() {
        return namePoint;
    }

    /**
     * Retrieves the x-axis coordinate of the interest point.
     * @return The x-axis coordinate.
     * @pre The interest point must have an x-axis coordinate set.
     */
    public int getxAxis() {
        return xAxis;
    }

    /**
     * Retrieves the y-axis coordinate of the interest point.
     * @return The y-axis coordinate.
     * @pre The interest point must have a y-axis coordinate set.
     */
    public int getyAxis() {
        return yAxis;
    }

    /**
     * Retrieves the QR code of the interest point.
     * @return The QR code.
     * @pre The interest point must have a QR code set.
     */
    public String getCodeQr() {
        return codeQr;
    }

    /**
     * Retrieves the list of evidences associated with the interest point.
     * @return The list of evidences.
     * @pre The interest point must have evidence data available.
     */
    public ArrayList<EvidenceProject> getEvidences() {
        return evidences;
    }

    /**
     * Retrieves the comments associated with the interest point.
     * @return The list of comments.
     * @pre The interest point must have comments available.
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    // Setters ----------

    /**
     * Sets the name of the interest point.
     *
     * @param namePoint The name to be set.
     * @post The name of the interest point is updated.
     */
    public void setNamePoint(String namePoint) {
        this.namePoint = namePoint;
    }

    /**
     * Sets the x-axis coordinate of the interest point.
     * @param xAxis The x-axis coordinate to be set.
     * @post The x-axis coordinate of the interest point is updated.
     */
    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    /**
     * Sets the y-axis coordinate of the interest point.
     * @param yAxis The y-axis coordinate to be set.
     * @post The y-axis coordinate of the interest point is updated.
     */
    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    /**
     * Sets the QR code of the interest point.
     * @param codeQr The QR code to be set.
     * @post The QR code of the interest point is updated.
     */
    public void setCodeQr(String codeQr) {
        this.codeQr = codeQr;
    }

    // Evidences methods
    // Main methods --------------------------------------------

    /**
     * Displays the available evidence associated with the current point of interest.
     */
    protected void showEvidences(){
        int counter=1;
        String typeEvidence = null;
        for (EvidenceProject evidence : getEvidences()) {
            if (checkEvidences(evidence)) {
                UserInteraction.showText(counter + ". Evidence: " + evidence.getNameEvidence() + "\n");
                UserInteraction.showText(" Registration date: " + evidence.getRegisterDate() + "\n");
                String project = ProjectManagementService.findEvidenceProject(evidence);
                UserInteraction.showText(" Related project: " + project + "\n");
                if (evidence instanceof Evidence) {
                    if (evidence.getTypeEvidence() == CharTypeEvidence.A) {
                        typeEvidence = "Audio";
                    } else if (evidence.getTypeEvidence() == CharTypeEvidence.V) {
                        typeEvidence = "Video";
                    } else if (evidence.getTypeEvidence() == CharTypeEvidence.P) {
                        typeEvidence = "Photo";
                    } else if (evidence.getTypeEvidence() == CharTypeEvidence.T) {
                        typeEvidence = "Text";
                    } else if (evidence.getTypeEvidence() == CharTypeEvidence.RR) {
                        typeEvidence = "Results report";
                    }
                    UserInteraction.showText(" Tipo de archivo: " + typeEvidence + "\n");
                    UserInteraction.showText(" URL: " + ((Evidence) evidence).getUrl() + "\n");
                } else if (evidence instanceof Review) {
                    UserInteraction.showText(" Tipo de archivo: Reseña\n");
                    UserInteraction.showText(" URL's:\n");
                    for (int i = 0; i < ((Review) evidence).getListUrl().length; i++) {
                        UserInteraction.showText(" " + (i + 1) + ". " + ((Review) evidence).getListUrl()[i] + "\n");
                    }
                }
                counter++;
            }
        }
    }

    /**
     * Adds a comment to a specified point of interest.
     * @param point The point to add the comment to.
     */
    public void addComment(InterestPoint point) {
        boolean flag = false;
        User user = UserCredentialService.searchUser();
        String commentText = null;
        if (user != null) {
            while (!flag) {
                commentText = UserInteraction.getInputString("Please, enter the comment: ");
                if (commentText != null) {
                    flag = true;
                } else {
                    UserInteraction.showText("Comment can't be null!");
                }
            }
        }
        if (flag) {
            point.getComments().add(new Comment(user, commentText));
            UserInteraction.showText("Comment added successfully!\n");
        }
    }

    /**
     * Displays comments associated with a specific point of interest.
     * @param point The point of interest to display comments for.
     */
    public void showComments(InterestPoint point){
        if (point!=null && point.getComments().size()>0) {
            UserInteraction.showText("\nInterest's points comments:\n");
            for (Comment comment : point.getComments()) {
                if (comment != null) {
                    UserInteraction.showText("- " + (comment.getUserName()) + ": " + comment.getCommentText() + "\n");
                }
            }
        } else {
            UserInteraction.showText("\nThis interest's point don't have any comments\n");
        }
    }

    // Support methods --------------------------------------------

    /**
     * Adds an evidence to the collection of evidences associated with the interest point.
     * @param evidence The evidence to add.
     * @post The provided evidence is added to the collection.
     */
    protected void addEvidence(EvidenceProject evidence) {
        evidences.add(evidence);
    }

    /**
     * Checks if the provided evidence is available or approved for display.
     * @param evidence The evidence to check.
     * @return `true` if the evidence is available, `false` otherwise.
     * @post The availability status of the evidence is determined.
     */
    private boolean checkEvidences(EvidenceProject evidence){
        if ((evidence instanceof Review && ((Review) evidence).evidenceStatus==false) || !evidence.getAvailability()){
            return false;
        }
        return true;
    }

    /**
     * Checks if there are approved evidences associated with the interest point.
     * @return `true` if there are approved evidences, `false` otherwise.
     * @post The presence of approved evidences is determined.
     */
    protected boolean hasApprovedEvidences() {
        for (EvidenceProject evidence : evidences) {
            if (checkEvidences(evidence)) {
                return true;
            }
        }
        UserInteraction.showText("We couldn't find an interest's point there!\n");
        return false;
    }
}

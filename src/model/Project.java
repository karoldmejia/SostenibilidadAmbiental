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
    protected void createEvidence(int userType){
        boolean flag=false;
        int optEvidence=0;
        String nameEvidence=null, registerDate=null;
        if (userType==3) {
            optEvidence = UserInteraction.getInputInt("Desea añadir:\n1. Reseña\n2. Evidencia común\n");
        } else if (userType==2) {
            optEvidence=1;
            if (!(UserCredentialService.searchDataGathererAssociated(getAssociatedDataGatherers()))){
                return;
            }
        }
        while (!flag){
            nameEvidence= UserInteraction.getInputString("Ingresa el nombre: ");
            registerDate = UserInteraction.getInputString("Enter the registration date: ");
            if (isNameValid(nameEvidence) && isDateValid(registerDate) && getStatus()){
                flag=true;
            } else if (!getStatus()) {
                UserInteraction.showText("Lo siento, no puedes agregar evidencias a este proyecto ya que no se encuentra disponible\n");
                return;
            }
        }
        if (flag) {
            if ((userType == 2 || userType == 3) && optEvidence == 1) {
                createReview(nameEvidence, registerDate, userType);
            } else if (userType == 3 && optEvidence == 2) {
                createCommonEvidence(nameEvidence, registerDate);
            }
        }
    }

    private void createReview(String nameEvidence, String registerDate, int userType){
        boolean evidenceStatus=false;
        int cantUrl=UserInteraction.getInputInt("¿Cuántos enlaces desea agregar?: ");
        String[] listUrl=new String[cantUrl];
        if (userType==3) {
            evidenceStatus=true;
        }
        for (int i=0;i<cantUrl;i++){
            listUrl[i]=UserInteraction.getInputString("Evidence no. "+(i+1)+": ");
        }
        CharTypeEvidence typeEvidence=CharTypeEvidence.R;
        evidences.add(new Review(nameEvidence, registerDate, true, typeEvidence, evidenceStatus, listUrl));
        int lastIndex = evidences.size() - 1;
        EvidenceProject lastEvidence = evidences.get(lastIndex);
        linkInterestPoint(lastEvidence);
        if (!evidenceStatus){
            UserInteraction.showText("Creación exitosa, ahora tu reseña se encuentra pendiente por aprobación!\n");
        } else {
            UserInteraction.showText("Reseña creada exitosamente!\n");
        }
    }
    private void createCommonEvidence(String nameEvidence, String registerDate){
        String url= UserInteraction.getInputString("Ingresa la url: ");
        int charTypeEvidence=UserInteraction.getInputInt(typeEvidenceOpt);
        CharTypeEvidence typeEvidence=selectTypeEvidence(charTypeEvidence);
        if (typeEvidence!=null){
            evidences.add(new Evidence(nameEvidence,registerDate,true,typeEvidence,url));
            int lastIndex = evidences.size() - 1;
            EvidenceProject lastEvidence = evidences.get(lastIndex);
            linkInterestPoint(lastEvidence);
            UserInteraction.showText("Evidencia creada exitosamente!\n");
        }
    }

    protected void updateEvidence(String idEvidence){
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
    protected void ActivateDeactivateEvidence(String idEvidence){
        EvidenceProject evidence=searchEvidence(idEvidence);
        if (evidence!=null) {
            if (evidence.getAvailability()){
                evidence.setAvailability(false);
                UserInteraction.showText("Evidencia desactivada.");
                return;
            } else{
                evidence.setAvailability(true);
                UserInteraction.showText("Evidencia desactivada.");
                return;
            }

        }
        UserInteraction.showText("We couldn't find any evidence");
    }
    protected void reviewReviews() {
        ArrayList<Review> unreviewedReviews = new ArrayList<>();
        for (EvidenceProject evidence : evidences) {
            if (evidence instanceof Review && !((Review) evidence).getEvidenceStatus()) {
                unreviewedReviews.add((Review) evidence);
            }
        }
        if (unreviewedReviews.isEmpty()) {
            UserInteraction.showText("No se encontró ninguna review pendiente por revisión :)\n");
            return;
        }
        UserInteraction.showText("Por favor, elige la review que desees revisar, o 0 para salir:\n");
        int optReview = -1;
        while (optReview != 0) {
            for (int i = 0; i < unreviewedReviews.size(); i++) {
                UserInteraction.showText((i + 1) + ". " + unreviewedReviews.get(i).getNameEvidence()+"\n");
            }
            optReview = UserInteraction.getInputInt("Ingresa tu opción: ");
            if (optReview == 0) {
                break;
            }
            if (optReview < 1 || optReview > unreviewedReviews.size()) {
                UserInteraction.showText("Opción inválida. Por favor, elige una opción válida.\n");
            } else {
                Review selectedReview = unreviewedReviews.get(optReview - 1);
                int markReview = UserInteraction.getInputInt("¿Desea?\n1. Aprobar\n2. Desaprobar\nIngresa tu opción: ");
                if (markReview == 1) {
                    selectedReview.setEvidenceStatus(true);
                    unreviewedReviews.remove(selectedReview);
                    UserInteraction.showText("Review aprobada!\n");
                } else if (markReview == 2) {
                    evidences.remove(selectedReview);
                    unreviewedReviews.remove(selectedReview);
                    UserInteraction.showText("Review desaprobada!\n");
                } else {
                    UserInteraction.showText("Opción inválida. Por favor, elige 1 o 2.\n");
                }
            }
        }
    }

    // Main methods ---------------------------------------------------
    private EvidenceProject searchEvidence(String idEvidence) {
        for (EvidenceProject evidenceProject : evidences) {
            if (evidenceProject!=null && idEvidence.equals(evidenceProject.getNameEvidence())) {
                return evidenceProject;
            }
        }
        UserInteraction.showText("We couldn't find any evidence with that name :(\n");
        return null;
    }
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
    private void updateNameEvidence(EvidenceProject evidenceProject){
        String name = UserInteraction.getInputString("Enter the new name: ");
        if (isNameValid(name)) {
            evidenceProject.setNameEvidence(name);
        }
    }
    private void updateDateEvidence(EvidenceProject evidenceProject){
        String date = UserInteraction.getInputString("Enter the new registration date: ");
        if (isDateValid(date)) {
            evidenceProject.setRegisterDate(date);
        }
    }
    private void updateSingleUrl(EvidenceProject evidenceProject){
        String url = UserInteraction.getInputString("Enter the new url: ");
        Evidence evidence = (Evidence) evidenceProject;
        evidence.setUrl(url);
    }
    private void updateMultipleUrl(EvidenceProject evidenceProject){
        Review review=(Review) evidenceProject;
        int optUrl=-1;
        while (optUrl!=0) {
            UserInteraction.showText("Por favor, elija cuál url desea cambiar\n");
            for (int i = 0; i < review.getListUrl().length; i++) {
                UserInteraction.showText((i+1) + ". " + review.getListUrl()[i]+"\n");
            }
            optUrl = UserInteraction.getInputInt("Ingrese su elección o '0' para salir: ");
            if (optUrl>=1 && optUrl<=review.getListUrl().length){
                review.getListUrl()[(optUrl-1)] = UserInteraction.getInputString("Enter new url: ");
            } else {
                UserInteraction.showText("Ingresa una opción válida");
            }
        }
    }
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

    private void linkInterestPoint(EvidenceProject evidence){
        String addInterestPoint = UserInteraction.getInputString("¿Deseas vincular un punto de interés a la evidencia? Ingresa 'y' para afirmar, y cualquier otra letra para cancelar\n");
        if (addInterestPoint.equalsIgnoreCase("y")) {
            int axisX = UserInteraction.getInputInt("Ingresa la coordenada x: ");
            int axisY = UserInteraction.getInputInt("Ingresa la coordenada y: ");
            InterestPoint point = MapUniversity.getInterestPoint(axisX,axisY);
            if (point == null) {
                String namePoint = UserInteraction.getInputString("Ingresa el nombre del punto de interés: ");
                String codeQR = MapUniversity.createQR();
                while (!MapUniversity.isQRValid(codeQR)){
                    codeQR = MapUniversity.createQR();
                    MapUniversity.isQRValid(codeQR);
                }
                point = new InterestPoint(namePoint, axisX, axisY, codeQR);
                MapUniversity.addInterestPoint(point);
            }
            point.addEvidence(evidence);
            UserInteraction.showText("Evidencia asociada al punto de interés exitosamente!\n");
        }
    }

}

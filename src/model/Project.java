package model;

import java.util.ArrayList;

public class Project {
    int idProject=0;
    String nameProject;
    String description;
    ArrayList<DataGatherer> associatedDataGatherers;
    int dataGatherersCounter;
    Pilar pilar;
    String initialDate;
    String finalDate;
    boolean status;

    Project(String nameProject,String description,Pilar pilar,String initialDate,String finalDate,boolean status){
        idProject+=1;
        this.nameProject=nameProject;
        this.description=description;
        associatedDataGatherers = new ArrayList<>();
        this.dataGatherersCounter=0;
        this.pilar=pilar;
        this.initialDate=initialDate;
        this.finalDate=finalDate;
        this.status=status;
    }

    // Getters
    public int getIdProject() {
        return idProject;
    }

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
}

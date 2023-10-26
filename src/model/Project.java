package model;

public class Project {
    int idProject=0;
    String nameProject;
    String description;
    DataGatherer[] associatedDataGatherers;
    Pilar pilar;
    String initialDate;
    String finalDate;
    boolean status;

    Project(String nameProject,String description,Pilar pilar,String initialDate,String finalDate,boolean status){
        idProject+=1;
        this.nameProject=nameProject;
        this.description=description;
        associatedDataGatherers=new DataGatherer[10];
        this.pilar=pilar;
        this.initialDate=initialDate;
        this.finalDate=finalDate;
        this.status=status;
    }

}

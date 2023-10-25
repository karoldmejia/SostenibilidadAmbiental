package model;

public class Researcher extends DataGatherer{

    String universityArea;
    String position;

    Researcher(String nameUser, String password, String fullName, String email, String phone, String universityArea, String position) {
        super(nameUser, password, fullName, email, phone);
        this.universityArea=universityArea;
        this.position=position;

    }
}

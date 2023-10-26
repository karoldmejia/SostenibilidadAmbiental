package model;

public class Researcher extends DataGatherer{

    String universityArea;
    String position;

    Researcher(String username, String password, String fullName, String email, String phone, String universityArea, String position) {
        super(username, password, fullName, email, phone);
        this.universityArea=universityArea;
        this.position=position;

    }


}

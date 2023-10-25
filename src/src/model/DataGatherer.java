package model;

public class DataGatherer extends Visitor{

    String fullName;
    String email;
    String phone;

    DataGatherer(String nameUser, String password, String fullName, String email, String phone) {
        super(nameUser, password);
        this.fullName=fullName;
        this.email=email;
        this.phone=phone;
    }
}

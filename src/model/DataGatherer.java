package model;

public class DataGatherer extends Visitor{

    String fullName;
    String email;
    String phone;

    DataGatherer(String username, String password, String fullName, String email, String phone) {
        super(username, password);
        this.fullName=fullName;
        this.email=email;
        this.phone=phone;
    }
}

package model;

public abstract class User {
    int idUser=0;
    String nameUser;
    String password;

    User(String nameUser, String password){
        idUser+=1;
        this.nameUser=nameUser;
        this.password=password;
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getPassword() {
        return password;
    }
}

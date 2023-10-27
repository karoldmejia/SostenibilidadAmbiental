package model;

public abstract class User {
    int idUser=0;
    String username;
    String password;

    User(String username, String password){
        idUser+=1;
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

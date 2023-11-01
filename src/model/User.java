package model;

public abstract class User {
    private static int nextId = 0;
    int idUser;
    String username;
    String password;

    User(String username, String password){
        this.idUser = nextId++;
        this.username=username;
        this.password=password;
    }

    public int getIdUser() {
        return idUser;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}

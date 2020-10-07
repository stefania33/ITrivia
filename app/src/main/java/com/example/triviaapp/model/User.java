package com.example.triviaapp.model;

public class User {
    private String password;
    private String email;
    private String user;

    public User() {
    }

    public User(String email, String user, String password) {
        this.password = password;
        this.email = email;
        this.user = user;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

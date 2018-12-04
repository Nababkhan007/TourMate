package com.example.nabab.tourmate;

public class UsersPojo {
    private String email;
    private String password;

    public UsersPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

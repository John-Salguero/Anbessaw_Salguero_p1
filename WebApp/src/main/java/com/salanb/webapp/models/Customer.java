package com.salanb.webapp.models;

public class Customer {

    private Integer id;
    private String username;
    private String password;

    public Customer(int id) {
        this.id = id;
    }

    public Customer() {
        id = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


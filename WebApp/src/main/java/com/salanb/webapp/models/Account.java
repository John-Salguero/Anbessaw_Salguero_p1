package com.salanb.webapp.models;

import com.salanb.orm.utillities.HashGenerator;

public class Account {
    String username;
    String password;

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

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + HashGenerator.getInstance().getMessageDigestString(password) + '\'' +
                '}';
    }
}

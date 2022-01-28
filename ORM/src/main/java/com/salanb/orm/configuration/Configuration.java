package com.salanb.orm.configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * This Class is used to tell the Session Factory how to configure each Session
 * The Configuration Factory makes instances of these classes to be used in case
 * multiple databases or seperate users need to access a database within the
 * app using the ORM framework
 */
public class Configuration {
    String driver;
    String url;
    String username;
    String password;
    List<String> mapResources;

    public Configuration(String driver, String url, String username, String password, List<String> mapResources) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.mapResources = mapResources;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<String> getMapResources() {
        return mapResources;
    }

    public void setMapResources(List<String> mapResources) {
        this.mapResources = mapResources;
    }
}

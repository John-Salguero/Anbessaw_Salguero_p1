package com.salanb.orm.configuration;

import java.util.List;

/**
 * This Class is used to tell the Session Factory how to configure each Session
 * The Configuration Factory makes instances of these classes to be used in case
 * multiple databases or separate users need to access a database within the
 * app using the ORM framework
 */
public class Configuration {
    private final String driver;
    private final String url;
    private final String username;
    private final String password;
    private final List<String> mapResources;

    /**
     * Constructor for the configuration object
     * @param driver - Tells the session factory which driver to use
     * @param url - Tells the session factory which URL to use to get the endpoint
     * @param username - Tells the session factory which username to use
     * @param password - Tells the session factory which password to use
     * @param mapResources - Tells the session factory how to map the objects to the DB
     */
    public Configuration(String driver, String url, String username, String password, List<String> mapResources) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.mapResources = mapResources;
    }

    /**
     * Gets the Driver class for the session factory
     * @return the Driver class for the session factory
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Gets the URL for the session factory
     * @return the URL for the session factory
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the username for the session factory
     * @return The username for the session factory
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the Password for the session factory
     * @return - The Password for the session factory
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the list of files used to map objects
     * @return - The list of files used to map objects
     */
    public List<String> getMapResources() {
        return mapResources;
    }

}

package com.salanb.orm.utillities;

import com.salanb.orm.logging.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A connection class using JDBC
 */
public class JDBCConnection {

    private final String driver;
    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    /**
     * Constructor for the connection class
     * @param driver - the driver type we are to use with JDBC, custom drivers are welcome
     * @param url - the url in JDBC format to the database
     * @param username - username to the database
     * @param password - password to the database
     */
    public JDBCConnection(String driver, String url, String username, String password){
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            String msg = "Could not load in the driver " + driver;
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }
    }

    public Connection getConnection() {

        try {
        if(connection == null || !connection.isValid(60)){

            connection = DriverManager
                    .getConnection(url,
                            username, password);
        }
        }catch (SQLException e) {
            MyLogger.logger.error("Could not connect to the Database");
            return null; // can't establish a connection
        }

        return connection;
    }
}

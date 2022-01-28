package com.salanb.orm.session;

import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.JDBCConnection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation for the Session which allows user to get transactions
 */
public class SessionImplementation implements Session{

    // contains the connection to the Database
    JDBCConnection connection;
    // The parent Session Factory who connects to a database
    private final SessionFactory parent;
    // contains a list of data affected by the session for caching reasons
    private final Map<String, List<Identifier>> dirtyFlags;
    // has the transaction class that is keeping the session open
    private final Transaction transaction;

    /**
     * Constructor for the Session object which allows users to get transactions
     * @param driver - Driver used to connect to the database
     * @param url - The URL used to connect to the database
     * @param username - the username used to connect to the Database
     * @param password - the password used to connect to the Database
     */
    SessionImplementation(SessionFactory parent, String driver, String url,
                          String username, String password) {
        this.parent = parent;
        dirtyFlags = new HashMap<>();
        connection = new JDBCConnection(driver, url, username, password);

        transaction = new Transaction(this);
    }

    /**
     * Sets a dirty flag for information that needs to be written to the database upon close
     * @param tableName - the name of the table the data belongs to
     * @param key - the key of the data in the table
     */
    @Override
    public void setDirtyFlag(String tableName, Identifier key) {
        if(!dirtyFlags.containsKey(tableName))
            dirtyFlags.put(tableName, new LinkedList<>());
        dirtyFlags.get(tableName).add(key);
    }

    /**
     * Gets the JDBC connection object for the Database
     * @return The JDBC Connection object
     */
    @Override
    public JDBCConnection getConnection() {
        return connection;
    }

    /**
     * Gets the Session factory of this session which contains the cache all sessions from
     * the specific configuration share
     * @return The session factory that created this session
     */
    @Override
    public SessionFactory getParent(){
        return parent;
    }

    /**
     * Gets the Transaction class responsible for communicating to the database
     * @return The transaction class that sends SQL Queries
     */
    @Override
    public Transaction getTransaction(){
        return transaction;
    }

    /**
     * Writes the cached data associated with this session upon close
     */
    @Override
    public void close() {
/// TODO Write the dirty flags upon close
    }
}

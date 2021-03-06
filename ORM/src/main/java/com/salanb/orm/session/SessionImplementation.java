package com.salanb.orm.session;

import com.salanb.orm.App;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.orm.utillities.ResourceNotFoundException;
import com.salanb.orm.utillities.Pair;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation for the Session which allows user to get transactions
 */
public class SessionImplementation implements Session {

    // contains the connection to the Database
    JDBCConnection connection;
    // The parent Session Factory who connects to a database
    private final SessionFactory parent;
    // contains a list of data affected by the session for caching reasons
    private final List<Identifier> dirtyFlags;
    // has the transaction class that is keeping the session open
    private final Transaction transaction;
    // Whether the session is valid or not
    private boolean isValid;

    /**
     * Constructor for the Session object which allows users to get transactions
     * @param driver - Driver used to connect to the database
     * @param url - The URL used to connect to the database
     * @param username - the username used to connect to the Database
     * @param password - the password used to connect to the Database
     */
    protected SessionImplementation(SessionFactory parent, String driver, String url,
                          String username, String password) {
        this.parent = parent;
        dirtyFlags = new LinkedList<>();
        connection = new JDBCConnection(driver, url, username, password);
        isValid = true;

        transaction = new Transaction(this);
    }

    /**
     * Sets a dirty flag for information that needs to be written to the database upon close
     * @param key - the key of the data in the table
     */
    @Override
   synchronized public void setDirtyFlag(Identifier key) {
        dirtyFlags.add(key);
    }

    /**
     * Removes a dirty flag for information that needs to be written to the database upon close
     * @param key - the key of the data in the table
     */
    @Override
    public void removeDirtyFlag(Identifier key) {
        dirtyFlags.remove(key);
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
     * Tells whether the session is valid or not.
     * @return True if the session is valid
     */
    @Override
    public boolean isInvalid(){
        try {
        Connection con = connection.getConnection();
            if(con != null && con.isValid(30))
               return !isValid;
        } catch (SQLException e) {
            throw new RuntimeException("An unknown fatal error occurred", e);
        }

        return true;
    }

    /**
     * given a mapped pojo, save the object in cache and to the database
     * @param pojo The object to save
     * @return The object that was saved
     */
    protected Object save(Object pojo) {

        // The Class of the object to update
        Class<?> clazz = pojo.getClass();
        // The table name of the object in the database
        String tableName = parent.getTableMaps().get(clazz);

        // Get the SQL Query
        String query = getSaveQuery(tableName, clazz);
        // Prepare the Statement and execute it
        return executeSQLINSERTStatement(pojo, query);
    }

    /**
     * Given a table name and an id, update the data of the object in the id
     * @param tableName - Table in the database that holds the data in the object
     * @param id - Framework specified identifier for the data in the database
     * @return - The object that was updated in the database
     * @throws ResourceNotFoundException - Thrown when the data is not in the cache to update
     */
    private Object update(String tableName, Identifier id) throws ResourceNotFoundException {

        Object updatePOJO = ((SessionFactoryImplementation)parent).getFromCachedData(tableName, id);
        if(updatePOJO == null)
            throw new ResourceNotFoundException("The object is not in the cache");

        // The Class of the object to update
        Class<?> clazz = updatePOJO.getClass();


        // Get the SQL Query
        String query = getUpdateQuery(tableName, clazz);
        // Prepare the Statement
        AtomicInteger placementPos = new AtomicInteger(0);
        PreparedStatement ps = prepareUpdateSQLStatement(updatePOJO, id, query, placementPos);
        // execute it
        return executeSQLStatement(clazz, id, ps, placementPos.get());

    }

    /**
     * Given a class, and an id - delete that information from the database
     * @param clazz - The class of the object associated with the data of the database
     * @param id - The framework assigned id which represents the primary key
     * @return - The object that was deleted
     * @throws RuntimeException - is thrown when connection to the database cannot be established
     */
    Object delete(Class<?> clazz, Identifier id) {

        Object deletePOJO = getObjectFromRepo(clazz, id);
        if(deletePOJO == null)
            return null;

        // Get the name of the table
        String tableName = parent.getTableMaps().get(clazz);


        // Get the SQL Query
        String query = getDeleteQuery(tableName, clazz);
        // Prepare the Statement
        PreparedStatement ps;
        try {
            ps = connection.getConnection().prepareStatement(query);
        } catch (SQLException e) {
            String msg = "Could not connect to the database.";
            MyLogger.logger.error(msg);
            throw new RuntimeException(msg);
        }
        // Execute the statement
        return  executeSQLStatement(clazz, id, ps, 0);
    }


    /**
     * Method that obtains an object from the repo, first it tries the cache, if
     * it isn't there then it makes a query to the database - returns null if
     * it isn't found
     * @param clazz The class of the object to get
     * @param id The framework supplied ID for the object
     * @return The object associated with the ID from the repo
     * @throws RuntimeException - is thrown when connection to the database cannot be established
     */
    protected Object getObjectFromRepo(Class<?> clazz, Identifier id) {

        // if the id isn't supplied, there is nothing to get
        if(id == null) {
            MyLogger.logger.error("id for object is null");
            return null;
        }

        // using the default methods of the parent requires a cast
        SessionFactoryImplementation sf = (SessionFactoryImplementation) parent;

        // Name of the table the object is related to
        String tableName = sf.getTableMaps().get(clazz);

        // Try to get the object from cache first, if it fails then get from database
        Object retVal = sf.getFromCachedData(tableName, id);
        if(retVal != null)
            return retVal;

        // start query to use for the database
        String query = getSelectQuery(clazz, tableName);
        // Prepare the Statement
        PreparedStatement ps;
        try {
            ps = connection.getConnection().prepareStatement(query);
        } catch (SQLException e) {
            String msg = "Could not connect to the database.";
            MyLogger.logger.error(msg);
            throw new RuntimeException(msg);
        }
        // Execute the statement
        Object fromDatabase = executeSQLStatement(clazz, id, ps, 0);
        if(fromDatabase != null)
            sf.addToCachedData(fromDatabase);
        return fromDatabase;
    }

    /**
     * Given a class, get that classes data in the form of a list
     * @param clazz - The class to get all the tables from
     * @return A list of all the objects of the mapped class
     */
    @Override
    public <T> List<T> getTableFromRepo(Class<T> clazz) {

        List<T> retVal = new ArrayList<>();
        SessionFactoryImplementation sf = (SessionFactoryImplementation) getParent();
        Map<String, String> fieldToNamesMap = sf.getFieldMaps().get(clazz);

        String tableName = parent.getTableMaps().get(clazz);
        if(tableName == null) {
            String msg = "This class is not mapped in the configuration: " + clazz;
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg);
        }

        try {
            String query = "SELECT * FROM \"" + tableName +"\"";
            PreparedStatement ps = connection.getConnection().prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                T retrievedPojo = buildObject(clazz, rs, fieldToNamesMap);
                if (retrievedPojo != null) {
                    Object cachedPojo = sf.getFromCachedData(retrievedPojo);
                    if (cachedPojo == null) { // if it's not cached, add it to the cache
                        ((SessionFactoryImplementation) getParent()).
                                addToCachedData(retrievedPojo);
                        retVal.add(retrievedPojo);
                    }
                    else // if it's already cached, return the cached version
                        retVal.add((T)cachedPojo);
                }
            }
        }catch (SQLException e) {
            String msg = "could not retrieve data from table " + tableName;
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }


        return retVal;
    }

    /**
     * Helper function which obtains the SQL Query to select the object from the DB
     * @param clazz - The class of the object being obtained
     * @param tableName - Which table the class belongs
     * @return The query used to select objects from the DB
     */
    private String getSelectQuery(Class<?> clazz, String tableName) {

        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz);
        // The list of fields that make up the primary/composite key of the object
        List<String> primaryKey = parent.getPrimaryKeys().get(clazz);


        // The string of the query to execute
        StringBuilder query = new StringBuilder();
        query.append("SELECT \"");

        // add the names
        Field[] fields= clazz.getDeclaredFields();
        for(int i = 0; i < fields.length; ){
            String name = fieldToNamesMap.get(fields[i++].getName());
            if(name != null) // if the field has been mapped to the table
                query.append(name).append("\"");
            else if(i == fields.length)
                query.setLength(query.length() - 3);
            if(i < fields.length)
                query.append(", \"");
        }

        // add the "From Clause"
        query.append(" FROM \"").append(tableName);

        // add the Where Clause
        query.append("\" WHERE \"");
        Iterator<String> it = primaryKey.iterator();
        query.append(fieldToNamesMap.get(it.next())).append("\"=?");
        while(it.hasNext()){
            query.append(" AND \"").
                    append(fieldToNamesMap.get(it.next())).append("\"=?");
        }
        return query.toString();
    }

    /**
     * Helper function which obtains the SQL Query to delete the object from the DB
     * @param tableName The table that the object belongs
     * @param clazz The class of the object that holds the info to update
     */
    private String getDeleteQuery(String tableName, Class<?> clazz) {
        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz);
        // The list of fields that make up the primary/composite key of the object
        List<String> primaryKey = parent.getPrimaryKeys().get(clazz);

        // The string of the query to execute
        StringBuilder query = new StringBuilder();
        query.append( "DELETE FROM \"").append(tableName).append("\" WHERE \"");

        // Get the Where Clause
        Iterator<String> it = primaryKey.iterator();
        query.append(fieldToNamesMap.get(it.next())).append("\"=?");
        while(it.hasNext()){
            query.append(" AND \"").
                    append(fieldToNamesMap.get(it.next())).append("\"=?");
        }
        query.append(" RETURNING *");

        return query.toString();
    }

    /**
     * Helper function which obtains the SQL Query to save an object to the DB from cache
     * @param tableName The table that the object belongs
     * @param clazz The class of the object that holds the info to save
     */
    private String getSaveQuery(String tableName, Class<?> clazz) {
        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz);
        // The list of fields that make up the primary/composite key of the object
        List<String> primaryKey = parent.getPrimaryKeys().get(clazz);
        // iterator for the primary keys to determine new ids for the data
        Iterator<String> primIt = primaryKey.iterator();
        // Gets a list of the generator types for the ids
        List<SessionFactory.GeneratorType> generatorTypeList = parent.getGenerationType().get(clazz);
        // iterator for the generator types for the different IDs
        Iterator<SessionFactory.GeneratorType> genIt = generatorTypeList.iterator();
        // start iterating by getting the next ID we are looking for
        String nextId = fieldToNamesMap.get(primIt.next());

        // The string of the query to execute
        StringBuilder query = new StringBuilder();
        query.append( "INSERT INTO \"").append(tableName).append("\"  (\"");

        // Get the column names
        for(Field field : clazz.getDeclaredFields()){
            String name = fieldToNamesMap.get(field.getName());
            if(name != null) // if the field has been mapped to the table
            {
                query.append(name).append("\", \"");
            }
        }
        query.setLength(query.length() - 3);
        query.append(") VALUES (");

        // Fill in the Placeholders
        for(Field field : clazz.getDeclaredFields()){
            String name = fieldToNamesMap.get(field.getName());
            if(name != null) // if the field has been mapped to the table
            {
                // if the field is an id and naturally generated, add the default keyword
                if (nextId.equals(name) &&
                        genIt.next() == SessionFactory.GeneratorType.NATURAL) {
                     // let the Database determine the id
                     query.append("DEFAULT, ");
                } else { // no special id generation, just add a placeholder
                    query.append("?, ");
                }
            }

        }
        query.setLength(query.length() - 2);
        query.append(") RETURNING *");

        return query.toString();
    }

    /**
     * Helper function which obtains the SQL Query to update the object to the DB from cache
     * @param tableName The table that the object belongs
     * @param clazz The class of the object that holds the info to update
     */
    private String getUpdateQuery(String tableName, Class<?> clazz) {
        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz);
        // The list of fields that make up the primary/composite key of the object
        List<String> primaryKey = parent.getPrimaryKeys().get(clazz);

        // The string of the query to execute
        StringBuilder query = new StringBuilder();
        query.append( "UPDATE \"").append(tableName).append("\" SET \"");

        boolean isPurePrimaryKey = true;
        // Get the column names
        for(Field field : clazz.getDeclaredFields()){
            String name = fieldToNamesMap.get(field.getName());
            // if the field has been mapped to the table and is not a primary key
            if(name != null && !primaryKey.contains(name)) {
                query.append(name).append("\"=?, \"");
                isPurePrimaryKey = false;
            }
        }
        if(isPurePrimaryKey) {
            MyLogger.logger.error("Cannot update a pure primary key, make sure to save instead");
            throw new InputMismatchException("Cannot Update primary keys");
        }
        if(clazz.getDeclaredFields().length > 0)
            query.setLength(query.length() - 3);

        // Get the Where Clause
        query.append(" WHERE \"");
        Iterator<String> it = primaryKey.iterator();
        query.append(fieldToNamesMap.get(it.next())).append("\"=?");
        while(it.hasNext()){
            query.append(" AND \"").
                    append(fieldToNamesMap.get(it.next())).append("\"=?");
        }
        query.append(" RETURNING *");

        return query.toString();
    }

    /**
     * Given an object, id, map of field names to column names, and an SQL query - execute the query and
     * build a pojo representing the data retrieved from the Database
     * @param pojo - The object to save
     * @param query - The SQL Query to execute
     * @return - Returns the object retrieved if the query retrieved any data
     */
    private Object executeSQLINSERTStatement(Object pojo, String query) {
        // The object to return
        Object retVal;

        Class<?> clazz = pojo.getClass();
        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz);
        // The name of the table that holds the object
        String tableName = parent.getTableMaps().get(clazz);
        // The Map of column names to their types
        Map<String, String> columnToTypeMap = parent.getTableTypeMaps().get(tableName);
        // The list of fields that make up the primary/composite key of the object
        List<String> primaryKey = parent.getPrimaryKeys().get(clazz);
        // Gets a list of the generator types for the ids
        List<SessionFactory.GeneratorType> generatorTypeList = parent.getGenerationType().get(clazz);

        // iterator for the primary keys to determine new ids for the data
        Iterator<String> primIt = primaryKey.iterator();
        // iterator for the generator types for the different IDs
        Iterator<SessionFactory.GeneratorType> genIt = generatorTypeList.iterator();

        PreparedStatement ps;
        try {
            ps = getConnection().getConnection().prepareStatement(query);

            // iterate throw all the placeholders and put in the correct values
            // generate the id's based on the specified way
            // start iterating by getting the next ID we are looking for
            String nextId = fieldToNamesMap.get(primIt.next());
            // number of placeholders we have defined
            int i = 0;
            for(Field field : clazz.getDeclaredFields()){
                String columnName = fieldToNamesMap.get(field.getName());
                if(columnName != null) { // if the field has been mapped to the table
                    // The value being placed into the placeholder
                    Object value;
                    // The type of the column name
                    String type = columnToTypeMap.get(columnName);

                    // if the field is an id generate the id
                    if(nextId.equals(columnName)){
                        // if the generation type is natural, let the database take care of it
                        if(genIt.next() == SessionFactory.GeneratorType.NATURAL) {
                            continue;
                        } else { // if we defined the id, use is as a placeholder
                            field.setAccessible(true);
                            value = field.get(pojo);
                            // set the placeholder
                            setPlaceholder(type, value, ps, ++i);
                        }
                        // if we have more id's to check for get the next one
                        if(primIt.hasNext()) {
                            nextId = primIt.next();
                        }
                    } else { // The field is not an ID just save it
                        field.setAccessible(true);
                        value = field.get(pojo);
                        // set the placeholder
                        setPlaceholder(type, value, ps, ++i);
                    }
                }
            }

            // Execute the Statement
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                retVal = buildObject(clazz, rs, fieldToNamesMap);
                if(retVal != null) {
                    ((SessionFactoryImplementation) getParent()).
                            addToCachedData(retVal);
                }
                return retVal;
            }

        }catch (SQLException e) {
            StringBuilder msg = new StringBuilder();
            msg.append("Could not save object to Database\n");
            msg.append(clazz);
            msg.append("\n" + e.getMessage());
            String out = msg.toString();
            MyLogger.logger.error(out);
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        // nothing was found return null
        return null;

    }

    /**
     * Given a class, id, map of field names to column names, and an SQL query in a String -
     * prepare the statement query and return it to prepped tp be executed
     * @param updatePOJO - The object representing the data in the DB we want to update
     * @param id - The framework ID for the object
     * @param query - The SQL Query to execute
     * @return - Returns the object retrieved if the query retrieved any data
     */
    private PreparedStatement prepareUpdateSQLStatement(Object updatePOJO, Identifier id,
                                                        String query, AtomicInteger placementPos) {

        PreparedStatement ps; // the prepared statement
        Object placeholder;   // the value as an object of the next placeholder
        Class<?> clazz = updatePOJO.getClass(); // the class of the object
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz); // map of fields to column names
        List<String> primaryKey = parent.getPrimaryKeys().get(clazz); // a list of fields used as primary keys

        try {
            ps = getConnection().getConnection().prepareStatement(query);
            // Iterate through all the Fields and set the placeholders if they are not primary keys
            int i = 0;
            for(Field field : clazz.getDeclaredFields()){
                String name = fieldToNamesMap.get(field.getName());
                // if the field has been mapped to the table and is not a primary key
                if(name != null && !primaryKey.contains(name)) {
                    field.setAccessible(true);
                    placeholder = field.get(updatePOJO);
                }else // if the field is either not mapped, or is a primary key, continue to the other fields
                    continue;
                placementPos.set(placementPos.get() + 1);
                if(placeholder == null){
                    ps.setNull(++i, Types.NULL);
                    continue;
                }
                Class<?> placeholderClass = placeholder.getClass();
                if (placeholderClass == Integer.class) {
                    ps.setInt(++i, (Integer) placeholder);
                } else if (placeholderClass == Long.class) {
                    ps.setLong(++i, (Long) placeholder);
                } else if (placeholderClass == Short.class) {
                    ps.setShort(++i, (Short) placeholder);
                } else if (placeholderClass == BigDecimal.class) {
                    ps.setBigDecimal(++i, (BigDecimal) placeholder);
                } else if (placeholderClass == Character.class) {
                    ps.setString(++i, ((Character) placeholder).toString());
                } else if (placeholderClass == String.class) {
                    ps.setString(++i, (String) placeholder);
                } else if (placeholderClass == Boolean.class) {
                    ps.setBoolean(++i, (Boolean) placeholder);
                } else if (placeholderClass == Double.class) {
                    ps.setDouble(++i, (Double) placeholder);
                } else {
                    MyLogger.logger.fatal("an undefined class was used as a primary/composite key");
                    throw new RuntimeException("an undefined class was used as a primary/composite key");
                }
            }
        } catch (SQLException | IllegalAccessException e) {
            String msg = "Could not access a prepared statement for " + clazz + " " + id;
            MyLogger.logger.error(msg);
            throw new RuntimeException(msg, e);
        }

        return  ps;
    }

    /**
     * Given a class, id, map of field names to column names, and an SQL query - execute the query and
     * build a pojo representing the data retrieved from the Database
     * @param clazz - The class of the object
     * @param id - The framework ID for the object
     * @param ps - The SQL Query to execute
     * @param placementPos - The position of where to start inserting the primary key
     * @return - Returns the object retrieved if the query retrieved any data
     */
    private Object executeSQLStatement(Class<?> clazz, Identifier id, PreparedStatement ps, int placementPos) {
        Object retVal;

        // A map of the object's field names to the database's table column names
        Map<String, String> fieldToNamesMap = parent.getFieldMaps().get(clazz);

        try {

            // Iterate through all the primary keys and set the placeholders
            Iterator<Object> idIt = id.iterator();
            int i = placementPos;
            while(idIt.hasNext()){
                Object placeholder = idIt.next();
                Class<?> placeholderClass = placeholder.getClass();
                if (placeholderClass == Integer.class) {
                    ps.setInt(++i, (Integer) placeholder);
                }else if(placeholderClass == Long.class){
                    ps.setLong(++i, (Long) placeholder);
                }else if(placeholderClass == Short.class){
                    ps.setShort(++i, (Short) placeholder);
                }else if(placeholderClass == BigDecimal.class){
                    ps.setBigDecimal(++i, (BigDecimal) placeholder);
                }else if(placeholderClass == Character.class){
                    ps.setString(++i, ((Character) placeholder).toString());
                }else if(placeholderClass == String.class){
                    ps.setString(++i, (String)placeholder);
                }else if(placeholderClass == Boolean.class) {
                    ps.setBoolean(++i, (Boolean) placeholder);
                }else if(placeholderClass == Double.class){
                    ps.setDouble(++i, (Double) placeholder);
                }else {
                    MyLogger.logger.fatal("an undefined class was used as a primary/composite key");
                    throw new RuntimeException("an undefined class was used as a primary/composite key");
                }
            }

            // Execute the Statement
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                retVal = buildObject(clazz, rs, fieldToNamesMap);
                return retVal;
            }

        }catch (SQLException e) {
            String msg = "Could not update object on Database" + clazz + " " + id;
            MyLogger.logger.error(msg);
            throw new RuntimeException(msg, e);
        }
        // nothing was found return null
        return null;
    }

    /**
     * Helper function for inserting placeholders into the save query
     * @param type The type of value to be inserted
     * @param value The value to be inserted
     * @param ps The prepared statement to insert the value
     * @param place The place the value belongs
     */
    private void setPlaceholder(String type, Object value, PreparedStatement ps, int place) {

        try {
            if(value == null)
                ps.setNull(place, Types.NULL);
            else
            switch (type) {
                case "integer":
                    ps.setInt(place, (Integer) value);
                    break;
                case "long":
                    ps.setLong(place, (Long) value);
                    break;
                case "short":
                    ps.setShort(place, (Short) value);
                    break;
                case "big_decimal":
                    ps.setBigDecimal(place, (BigDecimal) value);
                    break;
                case "character":
                    ps.setString(place, value.toString());
                    break;
                case "string":
                    ps.setString(place, (String) value);
                    break;
                case "boolean":
                    ps.setBoolean(place, (Boolean) value);
                    break;
                case "double":
                    ps.setDouble(place, (Double) value);
                    break;
                default:
                    String msg = "Mapped field has malformed type attribute";
                    MyLogger.logger.fatal(msg);
                    throw new InputMismatchException(msg);
            }
        } catch (SQLException e) {
            String msg = "Set placeholders to save object";
            msg += "\n" + e.getMessage();
            MyLogger.logger.error(msg);
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Given a class, a ResultSet of a query, and the map of class's fields to table names
     * construct an object and return it to caller
     * @param clazz - The class of the object to construct
     * @param rs - the Result set from the Query
     * @param fieldToNamesMap - A Map of a class's fields to its table names
     * @return The new object constructed with the information
     */
    private <T> T buildObject(Class<T> clazz, ResultSet rs, Map<String, String> fieldToNamesMap) {

        // construct the object and populate it with info
        T retVal;
        try {
            retVal = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg ="Mapped class was not a public POJO";
            MyLogger.logger.error(msg);
            return null;
        }

        try {
            for (Field field : clazz.getDeclaredFields()) {
                String type = field.getGenericType().getTypeName();
                String name = fieldToNamesMap.get(field.getName());
                switch (type){
                    case "int":
                        rs.getInt(name);
                        if(rs.wasNull())
                        {
                            String msg = "Cannot Assign a null value to a primitive Type";
                            MyLogger.logger.fatal(msg);
                            throw new IllegalArgumentException(msg);
                        }
                    case "java.lang.Integer":{
                        rs.getInt(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            Integer val = rs.getInt(name);
                            field.set(retVal, val);
                        }
                        break;}
                    case "long":
                        rs.getLong(name);
                        if(rs.wasNull())
                        {
                            String msg = "Cannot Assign a null value to a primitive Type";
                            MyLogger.logger.fatal(msg);
                            throw new IllegalArgumentException(msg);
                        }
                    case "java.lang.Long":{
                        rs.getLong(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            Long val = rs.getLong(name);
                            field.set(retVal, val);
                        }
                        break;}
                    case "short":
                        rs.getShort(name);
                        if(rs.wasNull())
                        {
                            String msg = "Cannot Assign a null value to a primitive Type";
                            MyLogger.logger.fatal(msg);
                            throw new IllegalArgumentException(msg);
                        }
                    case "java.lang.Short":{
                        rs.getShort(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            Short val = rs.getShort(name);
                            field.set(retVal, val);
                        }
                        break;}
                    case "double":
                        rs.getDouble(name);
                        if(rs.wasNull())
                        {
                            String msg = "Cannot Assign a null value to a primitive Type";
                            MyLogger.logger.fatal(msg);
                            throw new IllegalArgumentException(msg);
                        }
                    case "java.lang.Double":{
                        rs.getDouble(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            Double val = rs.getDouble(name);
                            field.set(retVal, val);
                        }
                        break;}
                    case "java.math.BigDecimal":{
                        rs.getBigDecimal(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            BigDecimal val = rs.getBigDecimal(name);
                            field.set(retVal, val);
                        }
                        break;}
                    case "char":
                        rs.getString(name);
                        if(rs.wasNull())
                        {
                            String msg = "Cannot Assign a null value to a primitive Type";
                            MyLogger.logger.fatal(msg);
                            throw new IllegalArgumentException(msg);
                        }
                    case "java.lang.Character":{
                        rs.getString(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            String val = rs.getString(name);
                            field.set(retVal, val.charAt(0));
                        }
                        break;}
                    case "java.lang.String":{
                        rs.getString(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            String val = rs.getString(name);
                            field.set(retVal, val);
                        }
                        break;}
                    case "boolean":
                        rs.getBoolean(name);
                        if(rs.wasNull())
                        {
                            String msg = "Cannot Assign a null value to a primitive Type";
                            MyLogger.logger.fatal(msg);
                            throw new IllegalArgumentException(msg);
                        }
                    case "java.lang.Boolean":{
                        rs.getBoolean(name);
                        field.setAccessible(true);
                        if(rs.wasNull())
                            field.set(retVal, null);
                        else {
                            Boolean val = rs.getBoolean(name);
                            field.set(retVal, val);
                        }
                        break;}
                    default:
                        throw new SQLException();
                }

            }
        } catch (IllegalAccessException | NullPointerException | SQLException e) {
            MyLogger.logger.fatal("Could not extract info from the Result Set Properly, Make sure you mapped out the POJO Correctly");
            throw new RuntimeException("Could not extract info from the Result Set Properly, Make sure you mapped out the POJO Correctly", e);
        }

        return retVal;
    }

    /**
     * Writes the cached data associated with this session upon close
     */
    @Override
    public void close() {
        isValid = false;

        final SessionFactoryImplementation sf = (SessionFactoryImplementation) parent;
        Map<String, Map<Identifier, Object>> cachedData = sf.getCachedData();

        // update All the new Objects using for each loops in conjunction with streams
        for(Map.Entry<String, Map<Identifier, Object>> tableNameToIdObjMap : cachedData.entrySet()) {
            String tableName = tableNameToIdObjMap.getKey(); // Extract out the table name for the current map
            // Filter out the objects this session did not touch
            tableNameToIdObjMap.getValue().entrySet().stream().filter( idToObj -> dirtyFlags.contains(idToObj.getKey()))
                .forEach(idToObj-> {
                    try {
                        update(tableName, idToObj.getKey());
                    } catch (ResourceNotFoundException e) {
                        MyLogger.logger.error("Tried updating an object that doesn't exist");
                    }

                });
        }

    }

    @Override
    public void writeAllCache(
            Map<String, Map<Identifier, Object>> cachedData) {

        // the session factory used to add and remove things from cache marked as final to use in lambdas
        final SessionFactoryImplementation sf = (SessionFactoryImplementation) parent;


        // Update all the objects cached using lambdas
        // extract out the table name
        // for each of the table-names to maps of ids to objects
        cachedData.forEach((tableName, idObjMap) -> { // take the key/value pair as arguments
            idObjMap.forEach((id, value) -> {         // take the key/value pair as arguments
                try {                                 // try catch block must be in lambda
                    update(tableName, id);            //  update the database
                } catch (ResourceNotFoundException e) {
                    MyLogger.logger.error("Tried updating an object that doesn't exist");
                } catch (InputMismatchException e) {
                    MyLogger.logger.info("Skipping pure primary key");
                }
            });
        });
    }
}

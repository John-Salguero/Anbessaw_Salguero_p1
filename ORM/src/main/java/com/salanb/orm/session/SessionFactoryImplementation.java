package com.salanb.orm.session;

import com.salanb.orm.App;
import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.IdentifierImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionFactoryImplementation implements SessionFactory {

    // All the maps used for relational mapping
    private final Map<Class, Map<String, String>> fieldMaps;
    private final Map<Class, String> tableMaps;
    private final Map<String, Map<String, String>> tableTypeMaps;
    private final Map<Class, List<String>> primaryKeys;
    private final Map<String, Map<Identifier, Object>> cachedData;
    private final Map<String, Set<Identifier>> cacheToDelete;

    // information used for connecting to the database
    private final String driver;
    private final String url;
    private final String username;
    private final String password;
    /**
     * Given a configuration object, construct the Session factory configured for
     * the ORM defined in the files
     * @param config - the configuration object that defines the behavior of the session factory
     */
    public SessionFactoryImplementation(Configuration config) {

        // get the info for connecting to the database
        driver = config.getDriver();
        url = config.getUrl();
        username = config.getUsername();
        password = config.getPassword();

        // Instantiate the maps of the SessionFactory
        fieldMaps =  new HashMap<>();
        tableMaps = new HashMap<>();
        tableTypeMaps = new HashMap<>();
        primaryKeys = new HashMap<>();
        // used a concurrent map since  the cache to delete needs to be thread-safe
        // the first layer of cached data does not, second layer does
        cachedData = new HashMap<>();
        cacheToDelete = new ConcurrentHashMap<>();


        // Instantiate the DocumentBuilderFactory Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            MyLogger.logger.info("Started Loading in the Session Configuration");
            // parse XML file(s)
            DocumentBuilder db = dbf.newDocumentBuilder();

            List<String> mapResources = config.getMapResources();
            for (String resource : mapResources) {
                Document doc = db.parse(App.getFileFromResource(resource));

                // optional, but recommended
                // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                doc.getDocumentElement().normalize();

                Element root = doc.getDocumentElement();
                MyLogger.logger.info("Root Element :" + root.getNodeName());

                // get a list of all the classes being mapped
                NodeList classList = root.getElementsByTagName("class");

                // iterate through all the class mappings and
                // make model the map for them
                for (int i = 0; i < classList.getLength(); ++i) {

                    // Get the node of classes
                    Element classNode = (Element) classList.item(i);
                    String className = classNode.getAttribute("name");
                    String tableName = classNode.getAttribute("table");
                    // If the table name is not provided, assume the name of the table
                    if(tableName.equals(""))
                        tableName = Class.forName(className).getSimpleName();
                    Class clazz = Class.forName(className);

                    // initialize the maps for class and table data
                    fieldMaps.put(clazz, new HashMap<>());
                    tableMaps.put(clazz, tableName);
                    tableTypeMaps.put(tableName, new HashMap<>());
                    primaryKeys.put(clazz, new LinkedList<>());
                    // the second layer of the cached data needs to be thread safe
                    cachedData.put(tableName, new ConcurrentHashMap<>());
                    cacheToDelete.put(tableName, new HashSet<>());



                    // get the primary key(s) of the class object
                    NodeList idList = classNode.getElementsByTagName("id");
                    // Get the rest of the fields of the class
                    NodeList propertyList = classNode.getElementsByTagName("property");

                    // iterate through the id's and populate the mapping data
                    for (int j = 0; j < idList.getLength(); ++j) {
                        Element id = (Element) idList.item(j);
                        String fieldName = id.getAttribute("name");
                        String columnName = id.getAttribute("column");
                        String type = id.getAttribute("type");

                        tableTypeMaps.get(tableName).put(columnName, type);
                        fieldMaps.get(clazz).put(fieldName, columnName);
                        primaryKeys.get(clazz).add(fieldName);
                    }

                    // iterate through the properties and populate the mapping data
                    for (int j = 0; j < propertyList.getLength(); ++j) {
                        // Get the next property settings
                        Element property = (Element) propertyList.item(j);
                        String fieldName = property.getAttribute("name");
                        String columnName = property.getAttribute("column");
                        String type = property.getAttribute("type");

                        tableTypeMaps.get(tableName).put(columnName, type);
                        fieldMaps.get(clazz).put(fieldName, columnName);

                    }
                    MyLogger.logger.info("Successfully mapped out " + className);
                }
            }
        } catch (ClassNotFoundException e) {
            MyLogger.logger.fatal("Failed to read in the mapping configuration");
            throw new RuntimeException("The Specified Class does not exist!", e);
        } catch (ParserConfigurationException | SAXException | IOException e)
        {
            MyLogger.logger.fatal("Failed to read in the mapping configuration");
            throw new RuntimeException("The Mapping data could not be loaded!", e);
        }

    }

    /**
     * Get a new session from the factory
     * @return A session represents a temporary connection to the Database
     */
    @Override
    public SessionImplementation getSession() {
        return new SessionImplementation(this, driver, url, username, password);
    }

    /**
     * Make sure to write all the cached data before closing the session
     */
    @Override
    public void close() {
        // TODO write the cached data and deleted data upon close
    }

    /**
     * Gets the Map of class fields to table names
     * @return a map where the class can be used to get to a map where the fields of the class
     * key to the names of columns on the table
     */
    @Override
    public Map<Class, Map<String, String>> getFieldMaps() {
        return fieldMaps;
    }

    /**
     * Returns a map where the class keys for the table names
     * @return a map where the class keys for the table names
     */
    @Override
    public Map<Class, String> getTableMaps() {
        return tableMaps;
    }

    /**
     * Returns a map where the tableName keys for a map of the name of columns and their typs
     * @return a map where the tableName keys for a map of the name of columns and their type
     */
    @Override
    public Map<String, Map<String, String>> getTableTypeMaps() {
        return tableTypeMaps;
    }

    /**
     * Returns a map where the class keys for a list of fields used as a primary/composite key
     * @return a map where the class keys for a list of fields used as a primary/composite key
     */
    @Override
    public Map<Class, List<String>> getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * Given an object returns the identifier
     * @param pojo The object which needs to get the id
     * @return the primary/composite key of the object
     */
    @Override
    public Identifier getId(Object pojo) {

        Class clazz = pojo.getClass();
        List<String> fieldList = primaryKeys.get(clazz);
        Identifier retVal = new IdentifierImplementation();
        try {
            for(String field : fieldList){
                Field accessField = clazz.getDeclaredField(field);
                accessField.setAccessible(true);
                if(!retVal.add(accessField.get(pojo)))
                    return null;
            }
        } catch (NoSuchFieldException e) {
            String msg = "Class was incorrectly mapped, no such field";
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }catch (IllegalAccessException e) {
            String msg = "Could not access the mapped field of the class" + clazz;
            MyLogger.logger.fatal(msg);
            throw new RuntimeException(msg, e);
        }

        return retVal;
    }

    /**
     * Given a tableName, id, and an object - adds that object to the cache of data saved
     * from them Database to reduce making calls to the database
     * @param tableName - the name of the table the data is coming from
     * @param id - primary or composite key
     * @param pojo - the object being stored
     * @return If successful returns the object being stored
     */
    Object addToCachedData(String tableName, Identifier id, Object pojo) {
        cachedData.get(tableName).put(id, pojo);
        return cachedData.get(tableName).get(id);
    }

    /**
     * Given a well formed object, add the object to the cached data
     * @param pojo - the object being stored
     * @return If successful returns the object being stored
     */
     Object addToCachedData(Object pojo) {
        String tableName = tableMaps.get(pojo.getClass());
        Identifier id = getId(pojo);

        cachedData.get(tableName).put(id, pojo);
        return cachedData.get(tableName).get(id);
    }

    /**
     * Given a tableName and id, removes the object from cached data
     * @param tableName - the table where the object belongs
     * @param id - the primary or composite key
     * @return The object that was successfully removed from cached data
     */
    Object removeFromCachedData(String tableName, Identifier id) {
        return cachedData.get(tableName).remove(id);
    }

    /**
     * Given a tableName and id fetches data from the cache
     * @param tableName - the table where the object belongs
     * @param id - the primary or composite key
     * @return - the object, if it's in the cache
     */
    Object getFromCachedData(String tableName, Identifier id) {
        return cachedData.get(tableName).get(id);
    }

    /**
     * returns the entire list set to be deleted
     * @return a map where the tableName keys for a list of id's set to be deleted from the cache
     */
    Map<String, Set<Identifier>> getCacheToDelete() {
        return cacheToDelete;
    }

    /**
     * given the tableName, gets all the ids to be deleted from the cache
     * @param tableName - the name of the table to get the objects to be deleted
     * @return - the list of id's (primary or composite) of objects to be deleted
     */
    Set<Identifier> getCacheToDelete(String tableName) {
        return cacheToDelete.get(tableName);
    }

    /**
     * given a tablename and id, returns true if the row is set to be deleted
     * @param tableName - the tableName of the object
     * @param id - the id of the object
     * @return true if the object is set to be deleted
     */
    boolean hasCachedToDelete(String tableName, Identifier id) {
        return cacheToDelete.containsKey(id);
    }

    /**
     * given a tableName and an id, sets that object to be deleted
     * @param tableName - the table name where the object belongs
     * @param id - the object's primary/composite key
     * @return The object that was successfully added to be deleted
     */
    synchronized Object addCacheToDelete(String tableName, Identifier id) {

        Object ret = cacheToDelete.get(tableName).add(id);
        if(cachedData.get(tableName).containsKey(getId(ret)))
            return ret;
        else
            return null;
    }

}

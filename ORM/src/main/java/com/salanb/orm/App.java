package com.salanb.orm;

import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.configuration.ConfigurationFactoryImplementation;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionFactoryImplementation;
import com.salanb.orm.utillities.Identifier;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the Session information and Cached information used throughout
 * the framework
 */
public class App {

    /**
     * This ensures the singleton Patter is used
     */
    private static final App instance = new App();
    /**
     * Making the default constructor private ensures the singleton pattern is used
     */
    private App() {}
    /**
     * The method used to retrieve the instance
     * @return - The instance of the App
     */
    public static App getInstance() {return instance;}

    static private final String FILENAME;
    static private final ConfigurationFactory factory;
    static private  final Map<String, Configuration> configs;
    static private final Map<String, SessionFactory> sessionFactories;
    public static final Map<String, Class<?>> typeMaps;


    // Static block that executes when the app is loaded into memory
    static {
        FILENAME = "SalAnb.cfg.xml";
        factory = ConfigurationFactoryImplementation.getInstance();
        configs = factory.getConfigurations(FILENAME);
        sessionFactories = new HashMap<>();
        for(Map.Entry<String, Configuration> config : configs.entrySet()) {
            sessionFactories.put(config.getKey(), new SessionFactoryImplementation(config.getValue()));
        }

        typeMaps = new HashMap<>();
        typeMaps.put("integer", Integer.class);
        typeMaps.put("long", Long.class);
        typeMaps.put("short", Short.class);
        typeMaps.put("big_decimal", BigDecimal.class);
        typeMaps.put("character", Character.class);
        typeMaps.put("string", String.class);
        typeMaps.put("boolean", Bool.class);
        typeMaps.put("double", Double.class);
    }

    /**
     * Returns a new session to be used to communicate with the database
     * @return - A Session object that can be used for transactions
     */
    public Session getNewSession() throws ParserConfigurationException {
        if(sessionFactories.isEmpty()) {
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new ParserConfigurationException(
                    "Trying to get a session from an unconfigured factory");
        }

        if(sessionFactories.containsKey(""))
            return sessionFactories.get("").getSession();
        return sessionFactories.entrySet().iterator().next().getValue().getSession();
    }

    /**
     * Returns a new session to be used to communicate with the database
     * @param name - the name of the session factory if one is not provided it defaults to the nameless one
     * @return - A Session object that can be used for transactions
     */
    public Session getNewSession(String name) {
        if(!sessionFactories.containsKey("")){
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new RuntimeException("Trying to get a session from an unconfigured factory");
        }

        return sessionFactories.get(name).getSession();
    }

    /**
     * Given an object returns the identifier of the default factory
     * @param pojo The object which needs to get the id
     * @return the primary/composite key of the object
     */
    public Identifier getId(Object pojo) {
        if(!sessionFactories.containsKey("")){
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new RuntimeException("Trying to get a session from an unconfigured factory");
        }

        return sessionFactories.get("").getId(pojo);
    }

    /**
     * Given an object and factory name returns the identifier of the named factory
     * @param factoryName The name of the factory to retrieve the id from
     * @param pojo The object which needs to get the id
     * @return the primary/composite key of the object
     */
    public Identifier getId(String factoryName, Object pojo) {
        if(!sessionFactories.containsKey(factoryName)){
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new RuntimeException("Trying to get a session from an unconfigured factory");
        }

        return sessionFactories.get(factoryName).getId(pojo);
    }

    /**
     * Given a filename, retrieves a file using the class loader
     * @param fileName - The name of the file the class loader is to load
     * @return - The file that gets loaded in
     */
    public static File getFileFromResource(String fileName) {
        ClassLoader classLoader = ConfigurationFactoryImplementation.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            MyLogger.logger.error("file not found! " + fileName);
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException e) {
                MyLogger.logger.error("Syntax Error while retrieving configuration file path");
                throw new RuntimeException(
                        "Syntax Error while retrieving configuration file path", e);
            }
        }

    }
}

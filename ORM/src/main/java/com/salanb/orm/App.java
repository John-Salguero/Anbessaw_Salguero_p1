package com.salanb.orm;

import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.configuration.ConfigurationFactoryImplementation;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionFactoryImplementation;
import com.salanb.orm.utillities.Identifier;

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
    private static App instance;

    private final String FILENAME;
    private final ConfigurationFactory factory;
    private  final Map<String, Configuration> configs;
    private final Map<String, SessionFactory> sessionFactories;
    public final Map<String, Class<?>> typeMaps;

    /**
     * Making the default constructor private ensures the singleton pattern is used
     */
    private App() {
        FILENAME = "SalAnb.cfg.xml";
        factory = ConfigurationFactoryImplementation.getInstance();
        configs = factory.getConfigurations(FILENAME);
        sessionFactories = new HashMap<>();
        for(Map.Entry<String, Configuration> config : configs.entrySet()) {
            SessionFactory sf = new SessionFactoryImplementation(config.getValue());
            sessionFactories.put(config.getKey(), sf);
            // register close as shutdown hook to write the cached data to the database
            Runtime.getRuntime().addShutdownHook(new Thread(sf::close));
        }

        typeMaps = new HashMap<>();
        typeMaps.put("integer", Integer.class);
        typeMaps.put("long", Long.class);
        typeMaps.put("short", Short.class);
        typeMaps.put("big_decimal", BigDecimal.class);
        typeMaps.put("character", Character.class);
        typeMaps.put("string", String.class);
        typeMaps.put("boolean", Boolean.class);
        typeMaps.put("double", Double.class);
    }
    /**
     * The method used to retrieve the instance
     * @return - The instance of the App
     */
    public static App getInstance() {
        if(instance == null)
             instance = new App();
        return instance;}



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
                throw new RuntimeException("Syntax Error while retrieving configuration file path", e);
            }
        }

    }

    /**
     * Used to close down the app in an expected manner - writes the cached data to the database
     */
    public void close() {

        for(Map.Entry<String, SessionFactory> factorySet: sessionFactories.entrySet()) {
            factorySet.getValue().close();
        }

    }
}

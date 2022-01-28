package com.salanb.orm;

import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.configuration.ConfigurationFactoryImplementation;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.models.Movie;
import com.salanb.orm.models.UserAccounts;
import com.salanb.orm.models.UserContent;
import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionFactoryImplementation;
import com.salanb.orm.session.Transaction;
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
    static private Map<String, SessionFactory> sessionFactories;
    public static final Map<String, Class> typeMaps;


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
    public Session getNewSession(String name) throws ParserConfigurationException {
        if(!sessionFactories.containsKey("")){
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new ParserConfigurationException("Trying to get a session from an unconfigured factory");
        }

        return sessionFactories.get(name).getSession();
    }



    /**
     * Test main please ignore
     *
     * @param args - What??? Why would you pass in args to this?
     */
    public static void main(String[] args) {
        App thisInstance = App.getInstance();
        Session session = null;
        try {
            session = thisInstance.getNewSession();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Transaction transaction = session.getTransaction();
        Movie m = new Movie();
        m.setId(32);
        m = (Movie)transaction.get(m);
        System.out.println(m);

        UserContent u = new UserContent();
        u.setUsername("humongous.situation3362");
        u = (UserContent) transaction.get(u);
        System.out.println(u);

        UserAccounts ua = new UserAccounts();
        ua.setUsername(null);
        ua.setAccountId("ae1d459bac7266fdc36001e6ff446bf563b7d4d861aeed75a642654e49eb2bae");
        ua = (UserAccounts) transaction.get(ua);
        System.out.println(ua);
        ua = (UserAccounts) transaction.get(ua);
        System.out.println(ua);

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

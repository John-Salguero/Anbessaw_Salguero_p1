package com.salanb.orm;

import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.logging.MyLogger;
import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactory;

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

    // Static block that executes when the app is loaded into memory
    static {
        FILENAME = "SalAnb.cfg.xml";
        factory = ConfigurationFactory.getInstance();
        configs = factory.getConfigurations(FILENAME);
        sessionFactories = new HashMap<>();
        for(Map.Entry<String, Configuration> config : configs.entrySet()) {
            sessionFactories.put(config.getKey(), new SessionFactory(config.getValue()));
        }
    }

    /**
     * Returns a new session to be used to communicate with the database
     * @return - A Session object that can be used for transactions
     */
    public Session getNewSession(){
        if(sessionFactories.isEmpty()) {
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new RuntimeException("Trying to get a session from an unconfigured factory");
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
    public Session getNewSession(String name){
        if(!sessionFactories.containsKey("")){
            MyLogger.logger.error("Trying to get a session from an unconfigured factory");
            throw new RuntimeException("Trying to get a session from an unconfigured factory");
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
        Session session = thisInstance.getNewSession();


    }
}

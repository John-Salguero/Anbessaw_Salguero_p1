package com.salanb.orm.configuration;

import com.salanb.orm.logging.MyLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


public class ConfigurationFactoryImplementation implements ConfigurationFactory {

    /**
     * This ensures the singleton Patter is used
     */
    private static final ConfigurationFactoryImplementation instance = new ConfigurationFactoryImplementation();
    /**
     * Making the default constructor private ensures the singleton pattern is used
     */
    private ConfigurationFactoryImplementation() {}
    /**
     * The method used to retrieve the instance
     * @return - The instance of the ConfigurationFactory
     */
    public static ConfigurationFactory getInstance() {return instance;}
    static private final String DRIVERCLASS = "salanb.connection.driver_class";
    static private final String CONNECTIONURL = "salanb.connection.url";
    static private final String CONNECTIONUSER = "salanb.connection.username";
    static private final String CONNECTIONPASS = "salanb.connection.password";

    /**
     * Given a filename, produces a map of names to configurations in the xml file
     * @param filename The file to parse
     * @return - a map of names to configurations to be used to init SessionFactories
     */
    @Override
    public Map<String, Configuration> getConfigurations(String filename){
        // Instantiate the return value
        Map<String, Configuration> retVal = new HashMap<>();
        // Instantiate the DocumentBuilderFactory Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            MyLogger.logger.info("Started Loading in the Configuration");
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(getFileFromResource(filename));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            MyLogger.logger.info("Root Element :" + root.getNodeName());

            // get a list of all the Session Factories
            NodeList factoryList = root.getElementsByTagName("session-factory");

            // iterate through all the factory configurations and
            // make a configuration for them
            for (int i = 0; i < factoryList.getLength(); ++i) {

                String name = null;
                String driver = null;
                String url = null;
                String username = null;
                String password = null;
                List<String> mapResources = new LinkedList<>();

                // Get the next factory settings
                Element factoryConfig = (Element) factoryList.item(i);
                name = factoryConfig.getAttribute("name");
                NodeList propertyList = factoryConfig.getElementsByTagName("property");
                for(int j = 0; j < propertyList.getLength(); ++j) {
                    // Get the next property settings
                    Element property = (Element) propertyList.item(j);
                    switch(property.getAttribute("name")){
                        case (DRIVERCLASS):
                            driver = property.getTextContent().trim();
                            break;
                        case CONNECTIONURL:
                            url = property.getTextContent().trim();
                            break;
                        case CONNECTIONUSER:
                            username = property.getTextContent().trim();
                            break;
                        case CONNECTIONPASS:
                            password = property.getTextContent().trim();
                            break;
                        default:
                            MyLogger.logger.error("Configurations File is in the wrong format");
                            throw new ParserConfigurationException("Wrong format!");
                    }
                }

                // Get the mapping resources
                NodeList mappingList = factoryConfig.getElementsByTagName("mapping");
                for(int j = 0; j < mappingList.getLength(); ++j) {
                    // Get the next mapping resource files
                    Element mapResource = (Element) mappingList.item(j);
                    mapResources.add(mapResource.getAttribute("resource"));
                }

                Configuration newConfiguration =
                        new Configuration(driver, url, username, password, mapResources);
                retVal.put(name, newConfiguration);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            MyLogger.logger.error("The configuration could not be loaded!");
            throw new RuntimeException("The configuration could not be loaded!", e);
        }

        return retVal;
    }

    /**
     * Given a filename, retrieves a file using the class loader
     * @param fileName - The name of the file the class loader is to load
     * @return - The file that gets loaded in
     */
    private static  File getFileFromResource(String fileName) {
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

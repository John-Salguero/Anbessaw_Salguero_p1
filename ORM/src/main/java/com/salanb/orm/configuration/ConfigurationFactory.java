package com.salanb.orm.configuration;

import com.salanb.orm.logging.MyLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ConfigurationFactory {

    /**
     * This ensures the singleton Patter is used
     */
    private static final ConfigurationFactory instance = new ConfigurationFactory();
    /**
     * Making the default constructor private ensures the singleton pattern is used
     */
    private ConfigurationFactory() {}
    /**
     * The method used to retrieve the instance
     * @return
     */
    public static ConfigurationFactory getInstance() {return instance;}
    static private final String FILENAME = "SalAnb.cfg.xml";

    /**
     * Static block gets executed as soon as class is loaded into memory
     */
    {
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(getFileFromResource(FILENAME));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            // get <session-factory>
            NodeList list = doc.getElementsByTagName("session-factory");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
            }

            //// LOAD IN THE XML FILE

        } catch (ParserConfigurationException | SAXException | IOException e) {
            MyLogger.logger.error("The configuration could not be loaded!");
            throw new RuntimeException("The configuration could not be loaded!", e);
        }
    }

    /**
     * Given a filename, retrieves a file using the class loader
     * @param fileName - The name of the file the class loader is to load
     * @return - The file that gets loaded in
     */
    private static  File getFileFromResource(String fileName) {
        ClassLoader classLoader = ConfigurationFactory.class.getClassLoader();
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

package com.salanb.orm.configuration;

import java.util.Map;

/**
 * A public interface to the ConfigurationFactoryClass
 */
public interface ConfigurationFactory {

    /**
     * Given a filename, produces a map of names to configurations in the xml file
     * @param filename The file to parse
     * @return - a map of names to configurations to be used to init SessionFactories
     */
    Map<String, Configuration> getConfigurations(String filename);
}

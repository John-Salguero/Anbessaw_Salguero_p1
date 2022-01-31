package test.configuration;

import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.configuration.ConfigurationFactoryImplementation;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


class ConfigurationFactoryImplementationTest {

    @Test
    void getInstance() {
       ConfigurationFactory obj = ConfigurationFactoryImplementation.getInstance();
       assertNotNull(obj);
    }

    @Test
    void getConfigurations() {
        ConfigurationFactoryImplementation.getInstance().getConfigurations("SalAnb.cfg.xml");
    }
}
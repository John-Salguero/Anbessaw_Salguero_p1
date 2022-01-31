package test.configuration;

import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.configuration.ConfigurationFactoryImplementation;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ConfigurationFactoryImplementationTest {

    @Test
    public void getInstance() {
       ConfigurationFactory obj = ConfigurationFactoryImplementation.getInstance();
       assertNotNull(obj);
    }

    @Test
    public void getConfigurations() {
        ConfigurationFactoryImplementation.getInstance().getConfigurations("SalAnb.cfg.xml");
    }
}
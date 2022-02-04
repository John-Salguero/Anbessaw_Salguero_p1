package test.configuration;

import com.salanb.orm.App;
import com.salanb.orm.configuration.ConfigurationFactory;
import com.salanb.orm.configuration.ConfigurationFactoryImplementation;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ConfigurationFactoryImplementationTest {


    @Test
    public void getInstance_Happy() {
       ConfigurationFactory cf = ConfigurationFactoryImplementation.getInstance();
       assertNotNull(cf);
    }


    @Test
    public void getConfigurations_Happy() {

        // App calls the method upon being loaded into memory by the class loader
        // Mockito will not mock App since it both is a static call AND a singleton
        // I'm using this as a way to test the appropriate code correctly
        App.getInstance();
    }

    @Test
    public void getConfigurations_Sad_WrongFormat() {

        // App calls the method upon being loaded into memory by the class loader
        // Mockito will not mock App since it both is a static call AND a singleton
        // I'm using this as a way to test the appropriate code correctly
        ConfigurationFactory cf = ConfigurationFactoryImplementation.getInstance();
        assertThrows(RuntimeException.class, () ->
                cf.getConfigurations("SalAnb.cfg.sad.format.xml"));
    }
}
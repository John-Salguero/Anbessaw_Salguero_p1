package test.configuration;

import com.salanb.orm.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConfigurationTest {

    /**
     * Tests the constructor of the Configuration class
     */
    @Test
    public void ConfigurationTest_Happy(){
        List<String> mapSources = null;
        Configuration config = new Configuration("","","","", mapSources);
        assertNotNull(config);
    }

    /**
     * Tests getting the driver class of the configuration class
     */
    @Test
    public void getDriver_Happy() {
        String test = "match";

        Configuration config = Whitebox.newInstance(Configuration.class);
        Whitebox.setInternalState(config, "driver", test);

        String receivedDriver = config.getDriver();

        assertEquals(test, receivedDriver);

    }

    /**
     * Tests getting the url from the configuration class
     */
    @Test
    public void getUrl_Happy() {
        String test = "match";

        Configuration config = Whitebox.newInstance(Configuration.class);
        Whitebox.setInternalState(config, "url", test);

        String receivedURL = config.getUrl();

        assertEquals(test, receivedURL);
    }

    /**
     * Tests getting the username from the configuration class
     */
    @Test
    public void getUsername_Happy() {
        String test = "match";

        Configuration config = Whitebox.newInstance(Configuration.class);
        Whitebox.setInternalState(config, "username", test);

        String receivedUsername = config.getUsername();

        assertEquals(test, receivedUsername);
    }

    /**
     * Tests getting the password from the configuration class
     */
    @Test
    public void getPassword_Happy() {
        String test = "match";

        Configuration config = Whitebox.newInstance(Configuration.class);
        Whitebox.setInternalState(config, "password", test);

        String receivedPassword = config.getPassword();

        assertEquals(test, receivedPassword);
    }

    /**
     * Tests getting the list of map files from the configuration class
     */
    @Test
    public void getMapResources_Happy() {
        String test = "match";
        List<String> testMaps = new ArrayList<>();
        testMaps.add(test);

        Configuration config = Whitebox.newInstance(Configuration.class);
        Whitebox.setInternalState(config, "mapResources", testMaps);

        List<String> receivedMaps = config.getMapResources();

        assertEquals(testMaps, receivedMaps);
    }
}
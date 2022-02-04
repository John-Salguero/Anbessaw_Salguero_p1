package test;

import com.salanb.orm.App;
import com.salanb.orm.session.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import testmodels.Movie;

import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class AppTest {

    @Before
    public void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = App.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void getInstance() {
       App obj = App.getInstance();
       assertNotNull(obj);
    }

    @Test
    public void getNewSession_Happy() {
        App.getInstance().getNewSession("");
    }

    @Test
    public void getNewSession_Sad_Unconfigured() throws NoSuchFieldException, IllegalAccessException {
        App app = Whitebox.newInstance(App.class);
        Field instance = App.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, app);
        Field factories = App.class.getDeclaredField("sessionFactories");
        factories.setAccessible(true);
        factories.set(app, new HashMap<>());
        assertThrows(RuntimeException.class, ()-> app.getNewSession(""));
    }

    @Test
    public void get_Id_Sad_Unconfigured() throws NoSuchFieldException, IllegalAccessException {
        App app = Whitebox.newInstance(App.class);
        Field instance = App.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, app);
        Field factories = App.class.getDeclaredField("sessionFactories");
        factories.setAccessible(true);
        factories.set(app, new HashMap<>());
        assertThrows(RuntimeException.class, ()-> app.getId(""));
    }


    @Test
    public void getFileFromResource_FileNotFound() {
        assertThrows(IllegalArgumentException.class,
                ()->App.getFileFromResource("ElonTusk.txt"));
    }

    @Test
    public void getId_Sad_Unconfigured_Named(){
        assertThrows(RuntimeException.class, ()->
                App.getInstance().getId("ElonTusk", new Movie()));
    }

    @Test
    public void getId_Happy_Named(){
        App.getInstance().getId("PK", new Movie());
    }

    @Test
    public void getId_Named(){
        App.getInstance().getId(new Movie());
    }

    @Test
    public void close_Happy() {
        App.getInstance().close();
    }
}
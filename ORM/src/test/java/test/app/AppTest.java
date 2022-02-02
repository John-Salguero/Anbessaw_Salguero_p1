package test.app;

import com.salanb.orm.App;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;

public class AppTest {


    @Test
    public void getInstance() {
       App obj = App.getInstance();
       assertNotNull(obj);
    }

    @Test
    public void getNewSession() {
        App.getInstance().getNewSession("")
    }

    @Test
    public void testGetNewSession() {

    }


    @Test
    public void getFileFromResource() {
    }
}
package test.session;

import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactoryImplementation;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertNotNull;


public class SessionFactoryImplementationTest {

    @Test
    public void getSession() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Session session = sessionFactory.getSession();
        assertNotNull(session);
    }

    @Test
    public void testClose() {
    }

    @Test
    public void getFieldMaps() {

    }

    @Test
    public void getTableMaps() {
    }

    @Test
    public void getTableTypeMaps() {
    }

    @Test
    public void getPrimaryKeys() {
    }

    @Test
    public void getId() {
    }
}
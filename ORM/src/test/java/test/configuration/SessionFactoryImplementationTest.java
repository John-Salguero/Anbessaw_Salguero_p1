package test.configuration;

import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactoryImplementation;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertNotNull;

public class SessionFactoryImplementationTest {

    @Test
    public void getSession() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "driver", "org.postgresql.Driver");
        Whitebox.setInternalState(sessionFactory, "url", "jdbc:postgresql://tomasdb1.cqyrfsmtmxo2.us-east-1.rds.amazonaws.com:5432/postgres");
        Whitebox.setInternalState(sessionFactory, "username", "postgres");
        Whitebox.setInternalState(sessionFactory, "password", "Ea093003$");
        Session session = sessionFactory.getSession();
        assertNotNull(session);
    }

    @Test
    void close() {
    }

    @Test
    public void getFieldMaps() {

    }

    @Test
    void getTableMaps() {
    }

    @Test
    void getTableTypeMaps() {
    }

    @Test
    void getPrimaryKeys() {
    }

    @Test
    void getId() {
    }
}
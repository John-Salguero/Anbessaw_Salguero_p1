package test.configuration;

import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionFactoryImplementation;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

class SessionFactoryImplementationTest {

    @Test
    void getSession() {
      SessionFactory obj = SessionFactoryImplementation.getSession();
        assertNotNull(obj);
    }

    @Test
    void close() {
    }

    @Test
    void getFieldMaps() {
        SessionFactoryImplementation.getSession().getFieldMaps();
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
package test;

import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionFactoryImplementation;
import com.salanb.orm.session.SessionImplementation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionFactoryImplementationTest {

    @Test
    void getSession() {
      SessionFactory (obj) = SessionFactoryImplementation.getSession();
        assertNotNull(obj);
    }

    @Test
    void close() {
        assertNull(
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
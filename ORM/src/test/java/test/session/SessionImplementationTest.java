package test.session;


import com.salanb.orm.session.SessionImplementation;
import com.salanb.orm.utillities.JDBCConnection;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertNotNull;

public class SessionImplementationTest {
    @Mock
    private JDBCConnection getConnection;

    @Test
    public void setDirtyFlag() {
    }

    @Test
    public void getConnection() {
        SessionImplementation session = Whitebox.newInstance(SessionImplementation.class);
        Whitebox.setInternalState(session, "connection", getConnection);
        JDBCConnection sessionConnection = session.getConnection();
        assertNotNull(session);

    }

    @Test
    public void getParent() {
    }

    @Test
    public void getTransaction() {
    }

    @Test
    public void close() {
    }
}
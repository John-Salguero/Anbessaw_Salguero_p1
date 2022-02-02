package test.session;


import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionImplementation;
import com.salanb.orm.session.Transaction;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.IdentifierImplementation;
import com.salanb.orm.utillities.JDBCConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
@RunWith(MockitoJUnitRunner.class)
public class SessionImplementationTest {
    @Mock
    private JDBCConnection getConnection;

    @Mock
    private SessionFactory getParent;

    @Mock
    private Transaction getTransaction;


    @Mock
    private List<Identifier> dirtyFlags;

    @Test
    public void setDirtyFlag_Happy() {
        SessionImplementation session = Whitebox.newInstance(SessionImplementation.class);
        List<Identifier> textList = new LinkedList<>();

        Whitebox.setInternalState(session, "dirtyFlags", textList);
        Identifier i = new IdentifierImplementation();
        session.setDirtyFlag(i);



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
        SessionImplementation session = Whitebox.newInstance(SessionImplementation.class);
        Whitebox.setInternalState(session, "parent", getParent);
        SessionFactory sessionFactory = session.getParent();
        assertNotNull(session);

    }

    @Test
    public void getTransaction() {
        SessionImplementation session = Whitebox.newInstance(SessionImplementation.class);
        Whitebox.setInternalState(session, "transaction", getTransaction);
        Transaction sessionFactory = session.getTransaction();
        assertNotNull(session);
    }

    @Test
    public void close() {
    }
}
package test.session;


import com.salanb.orm.App;
import com.salanb.orm.configuration.Configuration;
import com.salanb.orm.session.*;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.IdentifierImplementation;
import com.salanb.orm.utillities.JDBCConnection;
import com.salanb.orm.utillities.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import testmodels.Movie;

import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SessionImplementationTest {
    @Mock
    private JDBCConnection jdbcConnection;
    @Mock
    private TestSessionFactoryImplementation parent;
    @Mock
    private Transaction getTransaction;
    @Mock
    private List<Identifier> dirtyFlags;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement ps;
    @Mock
    private ResultSet rs;

    private class TestSessionFactoryImplementation extends SessionFactoryImplementation {

        public TestSessionFactoryImplementation(Configuration config) {
            super(config);
        }

        protected Object getFromCachedData(String tableName, Identifier id) {
            return super.getFromCachedData(tableName, id);
        }
    }

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
        Whitebox.setInternalState(session, "connection", jdbcConnection);
        JDBCConnection sessionConnection = session.getConnection();
        assertNotNull(session);

    }

    @Test
    public void getParent() {
        SessionImplementation session = Whitebox.newInstance(SessionImplementation.class);
        Whitebox.setInternalState(session, "parent", parent);
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
    public void getTableFromRepo_Happy () throws ParserConfigurationException {
        Session session = App.getInstance().getNewSession();
        List<Movie> movies = session.getTransaction().getTable(Movie.class);

        assertNotNull(movies);
    }

    @Test
    public void isInvalid_Sad() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Session session = Whitebox.newInstance(SessionImplementation.class);
        Whitebox.setInternalState(session, "connection", jdbcConnection);

        Mockito.when(jdbcConnection.getConnection()).thenReturn(connection);
        Mockito.when(connection.isValid(30)).thenReturn(false);

        session.isInvalid();

        Mockito.verify(jdbcConnection, Mockito.times(1)).getConnection();
        Mockito.verify(connection, Mockito.times(1)).isValid(30);
    }

    @Test
    public void delete_sad() throws ParserConfigurationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Session session = App.getInstance().getNewSession();
        Method delete = SessionImplementation.class.getDeclaredMethod("delete",
                Class.forName("java.lang.Class"), Identifier.class);
        delete.setAccessible(true);
        assertNull(delete.invoke(session, App.class, null));
    }

    @Test
    public void delete_Disconnect() throws ParserConfigurationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, SQLException {
        // Get the session and transaction to set up
        Session session = App.getInstance().getNewSession();
        Transaction transaction = session.getTransaction();
        // insert a valid entry to delete
        Movie movie = new Movie("Iron Man", BigDecimal.TEN, true, 0);
        Movie savedMovie = transaction.save(movie);
        // simulate a disconnection
        Whitebox.setInternalState(session, "connection", jdbcConnection);
        Mockito.when(jdbcConnection.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenThrow(SQLException.class);
        // call delete once disconnected
        Method delete = SessionImplementation.class.getDeclaredMethod("delete",
                Class.forName("java.lang.Class"), Identifier.class);
        delete.setAccessible(true);
        assertThrows(InvocationTargetException.class, () ->
                delete.invoke(session, Movie.class, session.getParent().getId(savedMovie)));
        // verify things went as expected
        Mockito.verify(jdbcConnection, Mockito.times(1)).getConnection();
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
    }

    @Test
    public void executeSQLINSERTStatement_Sad() throws ParserConfigurationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Session session = App.getInstance().getNewSession();
        Movie movie = new Movie("Iron Man", BigDecimal.TEN, true, 0);

        Method execQuery = SessionImplementation.class.getDeclaredMethod("executeSQLINSERTStatement",
                Object.class, String.class);
        execQuery.setAccessible(true);
        assertNull(execQuery.invoke(session, movie, "? ? ? ? ? ? ? ? ? ? ? ? ? ?"));
    }

    @Test
    public void close() throws SQLException {

        // Set up the dependencies
        Map<String, Map<Identifier, Object>> cachedData = new HashMap<>();
        Map<String, Set<Pair<Class<?>, Identifier>>> cacheToDelete = new HashMap<>();
        Map<Identifier, Object> objectsToUpdate =  new HashMap<>();
        List<Identifier> dirtyFlags = new ArrayList<>();
        Movie pojo = new Movie(1, "Iron Man", BigDecimal.TEN, true, 0);
        Identifier id =new IdentifierImplementation();
        Map<Class<?>, List<String>> primaryKeys = new HashMap<>();
        List<String> primaryKey = new ArrayList<>();
        Map<Class<?>, Map<String, String>> fieldMaps = new HashMap<>();
        Map<String, String> fieldToNamesMap = new HashMap<>();
        fieldToNamesMap.put("id", "m_id");
        fieldToNamesMap.put("title", "title");
        fieldToNamesMap.put("price", "price");
        fieldToNamesMap.put("available", "available");
        fieldToNamesMap.put("returnDate", "return_date");
        fieldMaps.put(Movie.class, fieldToNamesMap);
        primaryKey.add("id");
        id.add(1);
        String tableName = "movies";
        primaryKeys.put(pojo.getClass(), primaryKey);
        objectsToUpdate.put(id, pojo);
        dirtyFlags.add(id);
        cachedData.put(tableName, objectsToUpdate);

        // Set up the mocked objects
        Mockito.when(parent.getFromCachedData(tableName, id)).thenReturn(pojo);
        Mockito.when(parent.getPrimaryKeys()).thenReturn(primaryKeys);

        // Setup the dependencies
        SessionImplementation session = Whitebox.newInstance(SessionImplementation.class);
        JDBCConnection jConnection = Whitebox.newInstance(JDBCConnection.class);
        Whitebox.setInternalState(session, "parent", parent);
        Whitebox.setInternalState(session, "dirtyFlags", dirtyFlags);
        Whitebox.setInternalState(session, "connection", jConnection);
        Whitebox.setInternalState(jConnection, "connection", connection);

        // Set up the mocked objects
        Mockito.when(parent.getCachedData()).thenReturn(cachedData);
        Mockito.when(parent.getFieldMaps()).thenReturn(fieldMaps);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(ps);
        Mockito.when(connection.isValid(60)).thenReturn(true);
        Mockito.when(ps.executeQuery()).thenReturn(rs);

        // call the method
        session.close();

        // verify the state
        Mockito.verify(parent, Mockito.times(1)).getCachedData();
        Mockito.verify(parent, Mockito.times(3)).getFieldMaps();
        Mockito.verify(connection, Mockito.times(1)).prepareStatement(Mockito.anyString());
    }
}
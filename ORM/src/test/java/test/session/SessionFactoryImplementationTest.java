package test.session;

import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactory;
import com.salanb.orm.session.SessionFactoryImplementation;
import com.salanb.orm.session.SessionImplementation;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import testmodels.Movie;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SessionFactoryImplementationTest {
    @Mock
    private Map<Class<?>, Map<String, String>> getFieldMaps;
    @Mock
    private Map<Class<?>, String> getTableMaps;
    @Mock
    private Map<String, Map<String, String>> getTableTypeMaps;
    @Mock
    private Map<Class<?>, List<String>> getPrimaryKeys;
    @Mock
    private Map<Class<?>, List<SessionFactory.GeneratorType>> getGenerationType;
    @Mock
    private Map<Class<?>, List<String>> primaryKeys;
    @Mock
    private Map<String, Map<Identifier, Object>> getCachedData;
    @Mock
    private Map<String, Set<Pair<Class<?>, Identifier>>> getCacheToDelete;
    @Mock
    private SessionImplementation session;

    @Test
    public void getSession_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "driver", "org.postgresql.Driver");
        Whitebox.setInternalState(sessionFactory, "url", "jdbc:postgresql://tomasdb1.cqyrfsmtmxo2.us-east-1.rds.amazonaws.com:5432/postgres");
        Whitebox.setInternalState(sessionFactory, "username", "postgres");
        Whitebox.setInternalState(sessionFactory, "password","");
        Session session = sessionFactory.getSession();
        assertNotNull(session);
    }

    @Test
    public void testClose_Happy() {
        // set up the field dependencies
        Map<String, Map<Identifier, Object>> cachedData = new HashMap<>();
        Map<String, Set<Pair<Class<?>, Identifier>>> cachedDelte = new HashMap<>();
        // Setting up the factory
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "cachedData", cachedData);
        Whitebox.setInternalState(sessionFactory, "cacheToDelete", cachedDelte);
        Whitebox.setInternalState(sessionFactory, "session", session);

        // test close
        sessionFactory.close();

        // verify proper state
        Mockito.verify(session, Mockito.times(1)).writeAllCache(cachedData, cachedDelte);
    }

    @Test
    public void getFieldMaps_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory,"fieldMaps", getFieldMaps);
        Map<Class<?>, Map<String, String>> session = sessionFactory.getFieldMaps();
        assertNotNull(session);
    }

    @Test
    public void getTableMaps_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "tableMaps", getTableMaps);
        Map<Class<?>, String> session = sessionFactory.getTableMaps();
        assertNotNull(session);

    }

    @Test
    public void getTableTypeMaps_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "tableTypeMaps", getTableTypeMaps);
        Map<String, Map<String, String>> session = sessionFactory.getTableTypeMaps();
        assertNotNull(session);
    }

    @Test
    public void getPrimaryKeys_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "primaryKeys", getPrimaryKeys);
        Map<Class<?>, List<String>> session = sessionFactory.getPrimaryKeys();
        assertNotNull(session);


    }
    @Test
    public void getGenerationType_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "generationType", getGenerationType);
        Map<Class<?>, List<SessionFactory.GeneratorType>> session = sessionFactory.getGenerationType();
        assertNotNull(session);
    }

    @Test
    public void getId_Happy() {
        //Setting up dependencies
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        List<String> fieldlist = new LinkedList<>();
        fieldlist.add("id");
        Movie m= new Movie();
        Whitebox.setInternalState(sessionFactory, "primaryKeys", this.primaryKeys);

        //set up mocked objects
        Mockito.when(primaryKeys.get(Movie.class)).thenReturn(fieldlist);

      Identifier id = sessionFactory.getId(m);

      //verifying standard behavior

      assertNotNull(id);
      Mockito.verify(primaryKeys, Mockito.times(1)).get(Movie.class);


    }
    @Test
    public void getId_Sad_NullPrimaryKey() {
        //Setting up dependencies
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        List<String> fieldlist = new LinkedList<>();
        fieldlist.add("id");
        fieldlist.add("price");
        Movie m= new Movie();
        Whitebox.setInternalState(sessionFactory, "primaryKeys", this.primaryKeys);

        //set up mocked objects
        Mockito.when(primaryKeys.get(Movie.class)).thenReturn(fieldlist);

        assertThrows(InputMismatchException.class, () -> sessionFactory.getId(m));

        //verifying standard behavior
        Mockito.verify(primaryKeys, Mockito.times(1)).get(Movie.class);


    }
    @Test
    public void getCachedData() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory,"cachedData", getCachedData);
        Map<String, Map<Identifier, Object>> session = sessionFactory.getCachedData();
        assertNotNull(session);
    }
    @Test
    public void getCacheToDelete() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "cacheToDelete", getCacheToDelete);
        Map<String, Set<Pair<Class<?>, Identifier>>> session = sessionFactory.getCacheToDelete();
        assertNotNull(session);
    }
}
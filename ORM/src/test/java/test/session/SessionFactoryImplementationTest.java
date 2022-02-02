package test.session;

import com.salanb.orm.models.Movie;
import com.salanb.orm.session.Session;
import com.salanb.orm.session.SessionFactoryImplementation;
import com.salanb.orm.utillities.Identifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SessionFactoryImplementationTest {
    @Mock
    private Movie testMovie;


    @Mock
    private Map<Class, List<String>> primaryKeys;

    @Test
    public void getSession_Happy() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        Whitebox.setInternalState(sessionFactory, "driver", "org.postgresql.Driver");
        Whitebox.setInternalState(sessionFactory, "url", "jdbc:postgresql://tomasdb1.cqyrfsmtmxo2.us-east-1.rds.amazonaws.com:5432/postgres");
        Whitebox.setInternalState(sessionFactory, "username", "postgres");
        Whitebox.setInternalState(sessionFactory, "password","Ea093003$");
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
    public void getId_Happy() {
        //Setting up dependencies
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        List<String> fieldlist = new LinkedList<String>();
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
        List<String> fieldlist = new LinkedList<String>();
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
    public void getID_Sad_NoSuchField() {
        SessionFactoryImplementation sessionFactory = Whitebox.newInstance(SessionFactoryImplementation.class);
        List<String> fieldlist = new LinkedList<String>();
        fieldlist.add("id");
        fieldlist.add("number");
        Movie m= new Movie();
        Whitebox.setInternalState(sessionFactory, "primaryKeys", this.primaryKeys);

        //set up mocked objects
        //Mockito.when(primaryKeys.get(Movie.class)).thenReturn(fieldlist);

        //Identifier id = sessionFactory.getId(m);

        //verifying standard behavior

        //assertNotNull(id);
        //Mockito.verify(primaryKeys, Mockito.times(1)).get(Movie.class);

    }
}
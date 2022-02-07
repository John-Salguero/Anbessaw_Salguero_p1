package test.session;

import com.salanb.orm.App;
import com.salanb.orm.session.*;
import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.IdentifierImplementation;
import com.salanb.orm.utillities.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import testmodels.Movie;
import testmodels.TestPOJO;

import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionTest {

    private static class TestSessionImplementation extends SessionImplementation {
        TestSessionImplementation(SessionFactory parent, String driver, String url,
                                  String username, String password) {
            super(parent, driver, url, username, password);
        }
        @Override
        protected Object getObjectFromRepo(Class<?> clazz, Identifier id) {
            return super.getObjectFromRepo(clazz, id);
        }

        @Override
        protected Object save(Object pojo) {
            return super.save(pojo);
        }
    }


    @Mock
    private TestSessionImplementation parent;
    @Mock
    private Object pojo;
    @Mock
    private IdentifierImplementation id;
    @Mock
    private SessionFactoryImplementation sf;
    @Mock
    private Map<Class<?>, Map<String, String>> fieldMapList;
    @Mock
    private Map<String, String> fieldMap;
    @Mock
    private Map<Class<?>, String> tableMaps;
    @Mock
    private Map<String, Map<String, String>> tableTypeMaps;
    @Mock
    private Map<Class<?>, List<String>> primaryKeys;
    @Mock
    private List<String> primaryKey;
    @Mock
    private Map<Class<?>, List<SessionFactory.GeneratorType>> generatorMapTypeList;
    @Mock
    private List<SessionFactory.GeneratorType> generatorTypeList;

    @Before
    public void resetSingleton() throws NoSuchFieldException, IllegalAccessException {
        Field instance = App.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void save_Happy() {

        // Create the Transaction and inject the dependency
        Transaction transaction = Whitebox.newInstance(Transaction.class);
        Whitebox.setInternalState(transaction, "parent", parent);
        // Create dependencies
        List<SessionFactory.GeneratorType> generatorTypeList = new ArrayList<>();
        List<String> primaryKey = new ArrayList<>();
        generatorTypeList.add(SessionFactory.GeneratorType.NATURAL);
        primaryKey.add("FieldName");

        // Set up Mocked objects
        Mockito.when(parent.isInvalid()).thenReturn(false);
        Mockito.when(parent.getParent()).thenReturn(sf);
        Mockito.when(parent.getObjectFromRepo(pojo.getClass(), id)).thenReturn(null);
        Mockito.when(parent.save(pojo)).thenReturn(pojo);
        Mockito.when(sf.getFieldMaps()).thenReturn(fieldMapList);
        Mockito.when(sf.getTableMaps()).thenReturn(tableMaps);
        Mockito.when(sf.getTableTypeMaps()).thenReturn(tableTypeMaps);
        Mockito.when(sf.getPrimaryKeys()).thenReturn(primaryKeys);
        Mockito.when(sf.getGenerationType()).thenReturn(generatorMapTypeList);
        Mockito.when(sf.getId(pojo)).thenReturn(id);
        Mockito.when(tableMaps.get(pojo.getClass())).thenReturn("Tables");
        Mockito.when(primaryKeys.get(pojo.getClass())).thenReturn(primaryKey);
        Mockito.when(generatorMapTypeList.get(pojo.getClass())).thenReturn(generatorTypeList);
        Mockito.when(fieldMapList.get(pojo.getClass())).thenReturn(fieldMap);
        Mockito.when(fieldMap.get("FieldName")).thenReturn("ColumnName");

        // Call the method
        transaction.save(pojo);

        Mockito.verify(parent, Mockito.times(2)).isInvalid();
        Mockito.verify(parent, Mockito.times(3)).getParent();
        Mockito.verify(parent, Mockito.times(1)).save(pojo);
        Mockito.verify(sf, Mockito.times(1)).getId(pojo);
        Mockito.verify(sf, Mockito.times(1)).getTableMaps();
        Mockito.verify(sf, Mockito.times(1)).getFieldMaps();
        Mockito.verify(sf, Mockito.times(1)).getTableTypeMaps();
        Mockito.verify(sf, Mockito.times(1)).getPrimaryKeys();
        Mockito.verify(sf, Mockito.times(1)).getGenerationType();
        Mockito.verify(tableMaps, Mockito.times(1)).get(pojo.getClass());
        Mockito.verify(primaryKeys, Mockito.times(1)).get(pojo.getClass());
        Mockito.verify(generatorMapTypeList, Mockito.times(1)).get(pojo.getClass());
        Mockito.verify(fieldMap, Mockito.times(1)).get("FieldName");


    }

    @Test
    public void CrudOperations() throws ParserConfigurationException, ResourceNotFoundException {
        Transaction transaction = App.getInstance().getNewSession().getTransaction();
        TestPOJO testPOJO = new TestPOJO((short)1, 2, 500L, .64, "Fails", '\n');
        TestPOJO savedPOJO = transaction.save(testPOJO);
        TestPOJO updatePOJO = transaction.get(savedPOJO);
        assertNotNull(updatePOJO);
        updatePOJO.setString("Passes");
        updatePOJO = transaction.update(updatePOJO);
        assertNotNull(updatePOJO);

        assertNotEquals(updatePOJO.hashCode(), testPOJO.hashCode());
        assertNotEquals(updatePOJO, savedPOJO);
        assertNotEquals(testPOJO, savedPOJO);

        assertNotNull(transaction.delete(updatePOJO));
        assertNull(transaction.get(updatePOJO.getClass(), App.getInstance().getId(updatePOJO)));
        transaction.close();
    }

    @Test
    public void CrudOperations_PK() throws ParserConfigurationException, ResourceNotFoundException {
        Transaction transaction = App.getInstance().getNewSession("PK").getTransaction();
        TestPOJO testPOJO = new TestPOJO((short)1, 2, 500L, .64, "Fails", '\n');
        TestPOJO savedPOJO = transaction.save(testPOJO);

        assertNotEquals(testPOJO, savedPOJO);

        assertNotNull(transaction.delete(savedPOJO));
        App.getInstance().close();
    }

    @Test
    public void ObjectCanBeSave_Pure_Defined() {
        Transaction transaction = App.getInstance().getNewSession("Defined").getTransaction();
        TestPOJO testPOJO = new TestPOJO((short)1001, 2, 500L, Math.random(), "Fails", '\n');
        testPOJO.setId((int)(Math.random() * 100));
        TestPOJO savedPOJO = transaction.save(testPOJO);

        assertThrows(IllegalStateException.class, () -> transaction.save(savedPOJO));

        assertNotNull(transaction.delete(savedPOJO));
        App.getInstance().close();

    }

    @Test
    public void delete() throws ParserConfigurationException {
        Transaction transaction = App.getInstance().getNewSession().getTransaction();
        Movie movie = new Movie(90, "Iron Man", BigDecimal.TEN, true, 0);
        Movie deletedMovie = transaction.save(movie);
        transaction.delete(movie);

        assertNotNull(deletedMovie);
        assertNotEquals(deletedMovie.hashCode(), movie.hashCode());
        assertNotEquals(deletedMovie, movie);
    }

    @Test
    public void get() {
    }

    @Test
    public void getTable() {
    }

    @Test
    public void testGetTable() {
    }

    @Test
    public void testGet() {
    }

    @Test
    public void close() {
    }
}
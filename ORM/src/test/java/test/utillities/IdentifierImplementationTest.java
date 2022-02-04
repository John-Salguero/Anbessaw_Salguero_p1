package test.utillities;

import com.salanb.orm.utillities.Identifier;
import com.salanb.orm.utillities.IdentifierImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IdentifierImplementationTest {

    @Mock
    private Identifier mId;
    @Mock
    private Iterator<Object> mockIT;

    @Test
    public void testHashCode() {
        Identifier id1 = new IdentifierImplementation();
        Identifier id2 = new IdentifierImplementation();

        BigDecimal val1 = BigDecimal.valueOf(2.5);
        BigDecimal val2 = BigDecimal.valueOf(2.5);

        assertTrue(id1.add(val1));
        assertTrue(id2.add(val2));

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void testEquals_Equals() {
        Identifier id1 = new IdentifierImplementation();
        Identifier id2 = new IdentifierImplementation();

        BigDecimal val1 = BigDecimal.valueOf(2.5);
        BigDecimal val2 = BigDecimal.valueOf(2.5);

        assertTrue(id1.add(val1));
        assertTrue(id2.add(val2));

        assertEquals(id1, id2);
    }

    @Test
    public void testEquals_Null() {
        Identifier id1 = new IdentifierImplementation();
        Identifier id2 = new IdentifierImplementation();

        id1.add(1);
        id2.add(1);
        id2.set(0, null);


        assertThrows(RuntimeException.class, ()-> id1.equals(id2));

    }
}
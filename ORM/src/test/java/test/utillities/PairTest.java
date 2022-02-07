package test.utillities;

import com.salanb.orm.utillities.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class PairTest {

    @Test
    public void testEquals() {
        Pair<Integer, Integer> pair1 = new Pair<>(1,1);
        Pair<Integer, Integer> pair2 = new Pair<>(1,1);

        assertEquals(pair1, pair2);
    }

    @Test
    public void testNotEquals() {
        Pair<Integer, Integer> pair1 = new Pair<>(1,2);
        Pair<Integer, Integer> pair2 = new Pair<>(4,1);

        assertNotEquals(pair1, pair2);
    }

    @Test
    public void testHash_Equals() {
        Pair<Integer, Integer> pair1 = new Pair<>(1,1);
        Pair<Integer, Integer> pair2 = new Pair<>(1,1);

        assertEquals(pair1.hashCode(), pair2.hashCode());
    }

    @Test
    public void testHash_NotEquals() {
        Pair<Integer, Integer> pair1 = new Pair<>(1,2);
        Pair<Integer, Integer> pair2 = new Pair<>(1,6);

        assertNotEquals(pair1.hashCode(), pair2.hashCode());
    }


    @Test
    public void getKey() {
        Pair<Integer, Integer> pair = new Pair<>(1,2);

        Integer expect = 1;
        assertEquals(expect, pair.getKey());
    }

    @Test
    public void getValue() {
        Pair<Integer, Integer> pair = new Pair<>(1,2);

        Integer expect = 2;
        assertEquals(expect, pair.getValue());
    }
}
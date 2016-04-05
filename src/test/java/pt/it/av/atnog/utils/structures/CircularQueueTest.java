package pt.it.av.atnog.utils.structures;

import org.junit.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for CircularQueue.
 *
 * @author Mário Antunes
 * @version 1.0
 */
public class CircularQueueTest {

    @Test(expected=NoSuchElementException.class)
    public void test_middle_empty() {
        CircularQueue<String> q = new CircularQueue(3);
        q.middle();
    }

    @Test(expected=NoSuchElementException.class)
    public void test_middle_array_empty() {
        CircularQueue<String> q = new CircularQueue(3);
        String array[] = new String[1];
        q.middle(array);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_middle_array_parity() {
        CircularQueue<String> q = new CircularQueue(3);
        q.add("A");
        q.add("B");
        q.add("C");
        String array[] = new String[2];
        q.middle(array);
    }

    @Test
    public void test_middle_odd() {
        CircularQueue<String> q = new CircularQueue(3);
        q.add("A");
        assertTrue(q.middle().equals("A"));
        q.add("B");
        q.add("C");
        assertTrue(q.middle().equals("B"));
    }

    @Test
    public void test_middle_even() {
        CircularQueue<String> q = new CircularQueue(3);
        q.add("A");
        q.add("B");
        assertTrue(q.middle().equals("B"));
    }

    @Test
    public void test_middle_array_odd() {
        CircularQueue<String> q = new CircularQueue(5);
        q.add("A");
        String array[] = new String[1];
        q.middle(array);
        String r1[] = {"A"};
        assertTrue(Arrays.equals(array,r1));
        q.add("B");
        q.add("C");
        array = new String[3];
        q.middle(array);
        String r2[] = {"A","B","C"};
        assertTrue(Arrays.equals(array,r2));
    }

    @Test
    public void test_middle_array_even() {
        CircularQueue<String> q = new CircularQueue(5);
        q.add("A");
        q.add("B");
        String array[] = new String[2];
        q.middle(array);
        String r1[] = {"A","B"};
        assertTrue(Arrays.equals(array,r1));
        q.add("C");
        q.add("D");
        array = new String[4];
        q.middle(array);
        String r2[] = {"A","B","C","D"};
        assertTrue(Arrays.equals(array,r2));
    }


}

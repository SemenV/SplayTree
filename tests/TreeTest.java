import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TreeTest {
    public SplayTree<Integer> splayTree = new SplayTree<>();

    @Test
    public void addTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12, 11, 4, 6, 7);
        assertEquals(8, splayTree.size());
    }

    @Test
    public void rootTest() {
        splayTree.clear();
        splayTree.add(6);
        splayTree.add(4);
        assertEquals(4, splayTree.getRoot().value);
        splayTree.add(0);
        assertEquals(0, splayTree.getRoot().value);
        splayTree.add(10);
        assertEquals(10, splayTree.getRoot().value);
    }

    @Test
    public void searchTest() {
        splayTree.clear();
        splayTree.addAllElements(13, 12, 17, 123, 6);
        assertTrue(splayTree.containsAll(Arrays.asList(13, 12, 17, 123, 6)));
    }

    @Test
    public void lastTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12);
        assertEquals(12, (int) splayTree.last());
        splayTree.addAllElements(1, 0, 25, 18, 100);
        assertEquals(100, (int) splayTree.last());
    }

    @Test
    public void firstTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12);
        assertEquals(8, (int) splayTree.first());
        splayTree.addAllElements(1, 0, 25, 18, 100);
        assertEquals(0, (int) splayTree.first());
    }

    @Test
    public void removeTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12, 14, 17, 0, 3, 11, 2);
        splayTree.remove(14);
        assertTrue(splayTree.containsAll(Arrays.asList(10, 9, 8, 12, 17, 0, 3, 11, 2)));
        assertEquals(9, splayTree.size());
        splayTree.removeAll(Arrays.asList(2, 10));
        assertEquals(7, splayTree.size());
        assertFalse(splayTree.contains(10));
        assertFalse(splayTree.contains(2));
    }

    @Test
    public void iteratorTest() {
        splayTree.clear();
        List list = Arrays.asList(10, 9, 8, 12, 11, 4, 6, 7, 421, 0, 25, 48, 59, 74, 25, 86);
        splayTree.addAll(list);
        list.sort(Comparator.comparingInt(x -> (int) x));
        Iterator listIter = list.iterator();
        Iterator spIter = splayTree.iterator();

        assertEquals(list.size(), splayTree.size());
        while (spIter.hasNext()) {
            assertEquals(listIter.next(), spIter.next());
        }
    }

    @Test
    public void clearIsEmptyTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12, 11, 4, 6, 7, 421, 0, 2);
        assertEquals(11, splayTree.size());
        splayTree.clear();
        assertEquals(0, splayTree.size());
        assertTrue(splayTree.isEmpty());
    }

    @Test
    public void retainTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12, 11);
        List<Integer> elements = Arrays.asList(10,8,2);
        splayTree.retainAll(elements);
        assertTrue(splayTree.containsAll(Arrays.asList(10,8)));
        assertFalse(splayTree.contains(9));
        assertFalse(splayTree.contains(12));
        assertFalse(splayTree.contains(11));
    }






}

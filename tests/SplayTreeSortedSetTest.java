import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SplayTreeSortedSetTest {
    public SplayTreeSortedSet<Integer> splayTree = new SplayTreeSortedSet<>();

    @Test
    public void addTest() {
        splayTree.clear();
        splayTree.addAllElements(10, 9, 8, 12, 11, 4, 6, 7);
        assertEquals(8, splayTree.size());
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
        List list = new ArrayList(Arrays.asList(10, 9, 8, 12, 11, 4, 6, 7, 421, 0, 48, 59, 74, 86, 25, 25));
        splayTree.addAll(list);
        list.remove(new Integer(25));
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

    @Test
    public void iteratorRemoveTest() {
        splayTree.clear();
        List list = new ArrayList(Arrays.asList(10, 9, 8, 12, 11, 4, 6, 7, 421, 0, 48, 59, 74, 86, 25, 25));
        splayTree.addAll(list);
        Iterator spIter = splayTree.iterator();
        while (spIter.hasNext()) {
            Integer k = (Integer) spIter.next();
            if (k.compareTo(48) < 0) splayTree.remove(new Integer(k));
        }
    }

    @Test
    protected void doSubSetTest() {
        splayTree.clear();
        for (int i = 50; i >= 30; i--) {
            splayTree.add(i);
        }
        for (int i = 10; i <= 30; i++) {
            splayTree.add(i);
        }
        for (int i = 10; i >=0 ; i--) {
            splayTree.add(i);
        }
        SortedSet<Integer> set = splayTree.subSet(15,50);
        assertTrue(set.contains(17));
        assertTrue(set.contains(40));
        assertFalse(set.contains(0));
        assertEquals(splayTree.size(),51);
        assertEquals(set.size(),36);
    }

    @Test
    protected void doTailSetTest() {
        splayTree.clear();
        for (int i = 30; i <= 200; i++) {
            splayTree.add(i);
        }
        SortedSet set = splayTree.tailSet(200);
        splayTree.add(290);
        splayTree.add(280);
        assertTrue(set.contains(280));
        assertFalse(set.contains(30));
        assertEquals(3,set.size());
    }

    @Test
    protected void doHeadSetTest() {
        splayTree.clear();
        for (int i = 200; i >= 30; i--) {
            splayTree.add(i);
        }
        SortedSet set = splayTree.headSet(30);
        splayTree.add(29);
        splayTree.add(28);
        assertTrue(set.contains(28));
        assertFalse(set.contains(150));
        assertEquals(2,set.size());
    }



}






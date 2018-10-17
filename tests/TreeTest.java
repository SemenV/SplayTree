import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TreeTest {
    public SplayTree<Integer> splayTree = new SplayTree<>();

    @Test
    public void addTest() {
        splayTree.clear();
        splayTree.addAllElements(10,9,8,12,11,4,6,7);
        assertTrue(splayTree.size() == 8);
    }

    @Test
    public void  searchTest() {
        splayTree.clear();
        splayTree.addAllElements(13,12,17,123,6);
        assertTrue(splayTree.containsAll(Arrays.asList(13,12,17,123,6)));
    }

    @Test
    public void lastTest() {
        splayTree.clear();
        splayTree.addAllElements(10,9,8,12);
        assertTrue(splayTree.last() == 12);

    }

    @Test
    public void firstTest() {
        splayTree.clear();
        splayTree.addAllElements(10,9,8,12);
        assertTrue(splayTree.first() == 8);
    }

    @Test
    public void removeTest() {
        splayTree.clear();
        splayTree.addAllElements(10,9,8,12);
        splayTree.remove(9);
        assertTrue(splayTree.containsAll(Arrays.asList(10,8,12)));
        assertTrue(splayTree.size() == 3);
    }





}

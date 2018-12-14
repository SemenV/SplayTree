import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SplayTreeSortedMapTest {
    public SplayTreeSortedMap<Integer, String> splayTreeSortedMap = new SplayTreeSortedMap<>();

    @Test
    public void addTest() {
        splayTreeSortedMap.clear();
        splayTreeSortedMap.putAll(Map.of(3, "c", 1, "a", 2, "b"));
        assertEquals(splayTreeSortedMap.size(), 3);
        assertEquals(splayTreeSortedMap.put(5, "e"), null);
        assertEquals(splayTreeSortedMap.size(), 4);
        assertEquals(splayTreeSortedMap.put(5, "k"), "e");
        assertEquals(splayTreeSortedMap.size(), 4);
        splayTreeSortedMap.clear();
        assertEquals(splayTreeSortedMap.size(), 0);
    }


    @Test
    public void rootTest() {
        splayTreeSortedMap.clear();
        splayTreeSortedMap.put(6, "f");
        splayTreeSortedMap.put(4, "d");
        assertEquals(splayTreeSortedMap.getRoot().getValue(), "d");
        splayTreeSortedMap.put(0, "qwert");
        assertEquals(splayTreeSortedMap.getRoot().getValue(), "qwert");
        splayTreeSortedMap.put(0, "zero");
        assertEquals(splayTreeSortedMap.getRoot().getValue(), "zero");
        splayTreeSortedMap.put(6, "six");
        assertEquals(splayTreeSortedMap.getRoot().getValue(), "six");
    }

    @Test
    public void lastTest() {
        splayTreeSortedMap.clear();
        splayTreeSortedMap.putAll(Map.of(3, "c", 1, "a", 2, "b"));
        assertEquals(3, (int) splayTreeSortedMap.lastKey());
        splayTreeSortedMap.putAll(Map.of(1, "a", 0, "s", 25, "r", 18, "n", 100, "zxc"));
        assertEquals(100, (int) splayTreeSortedMap.lastKey());
    }

    @Test
    public void firstTest() {
        splayTreeSortedMap.clear();
        splayTreeSortedMap.putAll(Map.of(3, "c", 1, "a", 2, "b"));
        assertEquals(1, (int) splayTreeSortedMap.firstKey());
        splayTreeSortedMap.putAll(Map.of(1, "a", 0, "s", 25, "r", 18, "n", 100, "zxc"));
        assertEquals(0, (int) splayTreeSortedMap.firstKey());
    }

    @Test
    public void containsTest() {
        splayTreeSortedMap.clear();
        Map map = new TreeMap();
        map.putAll(Map.of(3, "c", 1, "a", 2, "b", 0, "s",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        assertTrue(splayTreeSortedMap.containsKey(3));
        assertTrue(splayTreeSortedMap.containsValue("c"));
        map.forEach((k, v) -> {
            assertTrue(splayTreeSortedMap.containsKey(k));
            assertTrue(splayTreeSortedMap.containsValue(v));
        });
    }

    @Test
    public void removeTest() {
        splayTreeSortedMap.clear();
        Map map = new TreeMap();
        map.putAll(Map.of(3, "c", 1, "a", 2, "b", 0, "s",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        splayTreeSortedMap.remove(2);
        splayTreeSortedMap.remove(0);
        splayTreeSortedMap.remove(100);
        map.remove(2);
        map.remove(0);
        map.remove(100);
        assertTrue(splayTreeSortedMap.containsKey(3));
        assertTrue(splayTreeSortedMap.containsValue("c"));
        map.forEach((k, v) -> {
            assertTrue(splayTreeSortedMap.containsKey(k));
            assertTrue(splayTreeSortedMap.containsValue(v));
        });
    }

    @Test
    public void entrySetTest() {
        splayTreeSortedMap.clear();
        Map map = new TreeMap();
        map.putAll(Map.of(3, "c", 1, "a", 2, "b", 0, "s",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        Set<Map.Entry<Integer, String>> set = splayTreeSortedMap.entrySet();
        assertEquals(7, set.size());

        set.forEach(k -> {
            SplayTreeSortedMap.Node val = (SplayTreeSortedMap.Node) k;
            assertTrue(map.containsKey(val.getKey()));
            assertTrue(map.containsValue(val.getValue()));
        });

        for (Map.Entry<Integer, String> entry : set) {
            assertTrue(map.containsKey(entry.getKey()));
            assertTrue(map.containsValue(entry.getValue()));
        }

        Set setStartMap = map.entrySet();
        set.forEach(k -> assertTrue(setStartMap.contains(k)));
        set.removeIf(k -> k.getKey().compareTo(2) < 0);
        assertEquals(5, set.size());
        set.clear();
        assertTrue(set.isEmpty());
    }

    @Test
    public void keySetTest() {
        splayTreeSortedMap.clear();
        Map<Integer,String> map = new TreeMap();
        map.putAll(Map.of(3, "c", 1, "a", 2, "b", 0, "s",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        Set<Integer> set = splayTreeSortedMap.keySet();
        assertEquals(7, set.size());
        assertEquals(map.size(),set.size());

        set.forEach(k -> assertTrue(map.containsKey(k)));

        for (Integer entry : set) {
            assertTrue(map.containsKey(entry));
        }

        set.removeIf(k -> k.compareTo(2) < 0);
        assertEquals(5, set.size());
        map.remove(new Integer(1));
        map.remove(new Integer(0));

        Iterator k = set.iterator();
        while (k.hasNext()) {
            Integer v = (Integer) k.next();
            if (v == 3) k.remove();
        }
        map.remove(new Integer(3));

        set.forEach(x -> assertTrue(map.containsKey(x)));
        assertEquals(map.size(),set.size());
        assertEquals(4,set.size());
        set.clear();
        assertTrue(set.isEmpty());
    }

    @Test
    public void subSetTest() {
        splayTreeSortedMap.clear();
        Map<Integer,String> map = new TreeMap();
        map.putAll(Map.of( 0, "s", 1, "a", 2, "b", 3, "c",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        Map subMap = splayTreeSortedMap.subMap(10,50);
        assertEquals(2,subMap.size());
        assertTrue(subMap.containsKey(25));
        assertTrue(subMap.containsKey(18));
        assertFalse(subMap.containsKey(100));
        assertFalse(subMap.containsKey(3));
        splayTreeSortedMap.put(20,"asdf");
        assertTrue(subMap.containsKey(20));
        assertTrue(subMap.containsValue("asdf"));
        assertEquals(3,subMap.size());
    }

    @Test
    public void tailSetTest() {
        splayTreeSortedMap.clear();
        Map<Integer,String> map = new TreeMap();
        map.putAll(Map.of( 0, "s", 1, "a", 2, "b", 3, "c",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        Map tailMap = splayTreeSortedMap.tailMap(10);
        assertEquals(3,tailMap.size());
        assertTrue(tailMap.containsKey(25));
        assertTrue(tailMap.containsKey(18));
        assertTrue(tailMap.containsKey(100));
        assertFalse(tailMap.containsKey(3));
        splayTreeSortedMap.put(20,"asdf");
        assertTrue(tailMap.containsKey(20));
        assertTrue(tailMap.containsValue("asdf"));
        splayTreeSortedMap.put(4,"asdf");
        assertEquals(4,tailMap.size());
    }

    @Test
    public void headSetTest() {
        splayTreeSortedMap.clear();
        Map<Integer,String> map = new TreeMap();
        map.putAll(Map.of( 0, "s", 1, "a", 2, "b", 3, "c",
                25, "r", 18, "n", 100, "zxc"));
        splayTreeSortedMap.putAll(map);
        Map headMap = splayTreeSortedMap.headMap(50);
        assertEquals(6,headMap.size());
        assertTrue(headMap.containsKey(25));
        assertTrue(headMap.containsKey(18));
        assertFalse(headMap.containsKey(100));
        assertTrue(headMap.containsKey(3));
        splayTreeSortedMap.put(20,"asdf");
        assertTrue(headMap.containsKey(20));
        assertTrue(headMap.containsValue("asdf"));
        splayTreeSortedMap.put(4,"asdf");
        splayTreeSortedMap.put(102,"asdf");
        assertEquals(8,headMap.size());
    }
}

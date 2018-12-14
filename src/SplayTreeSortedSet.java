import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SplayTreeSortedSet<T extends Comparable<T>> implements SortedSet<T> {
    SplayTreeSortedMap tree = new SplayTreeSortedMap();

    public SplayTreeSortedMap.Node getRoot() {
        return tree.getRoot();
    }


    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return tree.containsKey(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SplayTreeSortedSetIter();
    }

    @SuppressWarnings("unchecked")
    public class SplayTreeSortedSetIter implements Iterator<T> {
        SplayTreeSortedMap.SplayTreeIteratorPseudo t = tree.new SplayTreeIteratorPseudo();

        @Override
        public boolean hasNext() {
            return t.hasNext();
        }

        @Override
        public T next() {
            return (T) t.nextNode().getKey();
        }

        public void remove() {
            t.remove();
        }
    }
    public final void addAllElements(T... c) {
        addAll(Arrays.asList(c));
    }

    @Override
    public boolean add(T t) {
        tree.put(t,null);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return tree.removeNode(o);
    }

    @Override
    public boolean containsAll(Collection<?> e) {
        for (Object element : e) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T element : c) {
            if (!add(element)) return false;
        }
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        SortedSet<T> set = this;
        Set<T> retained = new HashSet<>();
        for (Object o : c) {
            if (contains(o)) retained.add((T) o);
        }
        clear();
        addAll(retained);
        return !equals(set);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        for (Object e : c) {
            remove((T) e);
        }
        return true;
    }

    @Override
    public void clear() {
        tree.clear();
    }


    @Override
    public T first() {
        return (T) tree.firstKey();
    }

    @Override
    public T last() {
        return (T) tree.lastKey();
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SplayTreeSubSortedSet<>(this, fromElement, toElement, true);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SplayTreeSubSortedSet<>(this, null, toElement, false);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SplayTreeSubSortedSet<>(this, fromElement, null, true);
    }

    @NotNull
    @Override
    public Object[] toArray() {
        Object[] array = new Object[tree.size()];
        Iterator<T> iterator = this.iterator();
        for (int k = 0; k < tree.size() - 1; k++) {
            array[k] = iterator.next();
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        Object[] array = new Object[tree.size()];
        Iterator<T> iterator = this.iterator();
        for (int k = 0; k < a.length - 1; k++) {
            if (iterator.hasNext())
                array[k] = iterator.next();
        }
        return (T1[]) Arrays.copyOf(array, array.length, a.getClass());
    }

    class SplayTreeSubSortedSet<V extends Comparable<V>> extends SplayTreeSortedSet<V> {
        private SplayTreeSortedSet<V> tree;
        private V fromElement, toElement;
        private boolean containsEdge;

        SplayTreeSubSortedSet(SplayTreeSortedSet<V> tree, V fromElement, V toElement, boolean containsEdge) {
            this.tree = tree;
            this.fromElement = fromElement;
            this.toElement = toElement;
            this.containsEdge = containsEdge;
        }

        private boolean isInside(V v) {
            return (fromElement == null || v.compareTo(fromElement) > 0 || containsEdge && v.compareTo(fromElement) == 0) &&
                    (toElement == null || v.compareTo(toElement) < 0 || containsEdge && v.compareTo(toElement) == 0);
        }

        @Override
        public boolean add(V v) {
            if (isInside(v)) {
                return tree.add(v);
            }
            throw new IllegalArgumentException("IllegalArgumentException");
        }


        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(Object o) {
            return isInside((V) o) && tree.contains(o);
        }

        private int countSize(SplayTreeSortedMap.Node root) {
            int size = 0;
            if (root != null) {
                if (isInside((V) root.getKey())) {
                    ++size;
                }
                size += countSize(root.getLeft());
                size += countSize(root.getRight());
            }
            return size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public int size() {
            return countSize(tree.getRoot());
        }
    }

}

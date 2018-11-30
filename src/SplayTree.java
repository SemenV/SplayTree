import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SplayTree<T extends Comparable<T>> implements SortedSet<T> {

    private Node root = null;

    private int size = 0;

    protected class Node<T> {
        final T value;
        Node<T> parent = null, left = null, right = null;

        Node(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("Value: " + value);
            if (left != null) str.append(" Left: ").append(left.value);
            if (right != null) str.append(" Right: ").append(right.value);
            if (parent != null) str.append(" Parent: ").append(parent.value);
            return str.toString();
        }
    }

    public Node getRoot() {
        return root;
    }

    @Override
    public boolean add(T value) {
        //searching parent with "null"
        Node<T> currentNode = root;
        Node<T> parentForCurrNode = null;

        while (currentNode != null) {
            parentForCurrNode = currentNode;
            if (value.compareTo(currentNode.value) < 0)
                currentNode = currentNode.left;
            else currentNode = currentNode.right;
        }

        //create new node
        Node<T> newNode = new Node(value);
        newNode.parent = parentForCurrNode;

        //paste new node
        if (parentForCurrNode == null)
            root = newNode;
        else if (newNode.value.compareTo(parentForCurrNode.value) < 0)
            parentForCurrNode.left = newNode;
        else parentForCurrNode.right = newNode;

        splay(newNode);
        size++;
        return true;
    }

    private void splay(Node<T> node) {
        while (node.parent != null) {
            if (node.parent == null) return;

            Node<T> parent = node.parent;
            Node<T> gparent = parent.parent;

            if (gparent == null) {
                zig(parent, node);
                return;
            } else {
                if (gparent.left == parent && parent.left == node ||
                        gparent.right == parent && parent.right == node) {
                    zigZig(gparent, parent, node);

                } else if (gparent.left == parent && parent.right == node ||
                        gparent.right == parent && parent.left == node) {
                    zigZag(gparent, parent, node);
                }
            }
        }
    }

    private void nodeUp(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> gparent = parent.parent;
        Node<T> nodeRight = node.right;
        Node<T> nodeLeft = node.left;
        boolean rigth = false;
        if (gparent != null) rigth = gparent.right == parent;

        if (parent.left == node) {
            node.right = parent;
            parent.left = nodeRight;
            if (nodeRight != null)
                nodeRight.parent = parent;
        } else {
            node.left = parent;
            parent.right = nodeLeft;
            if (nodeLeft != null)
                nodeLeft.parent = parent;
        }
        node.parent = gparent;
        parent.parent = node;

        if (gparent == null) root = node;
        else {
            if (rigth)
                gparent.right = node;
            else
                gparent.left = node;
        }
    }

    private void zig(Node<T> parent, Node<T> node) {
        nodeUp(node);
    }

    private void zigZig(Node<T> gparent, Node<T> parent, Node<T> node) {
        nodeUp(parent);
        nodeUp(node);
    }

    private void zigZag(Node<T> gparent, Node<T> parent, Node<T> node) {
        nodeUp(node);
        nodeUp(node);
    }

    private Node<T> search(T node) {
        Node<T> currentNode = root;
        while (currentNode != null) {
            if (node.compareTo(currentNode.value) == 0) {
                splay(currentNode);
                return currentNode;
            } else if (node.compareTo(currentNode.value) < 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> e) {
        for (Object element : e) {
            if (!contains(element)) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object e) {
        T element = (T) e;
        Node<T> elemTree = search(element);
        return element != null && elemTree != null && elemTree.value.compareTo(element) == 0;
    }

    @Override
    public T last() {
        if (isEmpty()) throw new NoSuchElementException("NoSuchElementException");
        Node<T> currentNode = root;
        while (currentNode.right != null) {
            currentNode = currentNode.right;
        }
        splay(currentNode);
        return currentNode.value;
    }

    @Override
    public T first() {
        if (isEmpty()) throw new NoSuchElementException("NoSuchElementException");
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.value;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T element : c) {
            if (!add(element)) return false;
        }
        return true;
    }

    @SafeVarargs
    public final void addAllElements(T... c) {
        addAll(Arrays.asList(c));
    }

    private Node<T> merge(Node<T> tree1, Node<T> tree2) {
        if (tree1 == null) return tree2;
        if (tree2 == null) return tree1;
        tree1 = search((maxNode(tree1)).value);
        if (tree1.value.compareTo(tree2.value) > 0) throw new IllegalArgumentException();
        tree1.right = tree2;
        tree2.parent = tree1;
        return tree1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if (o == null || root == null) return false;
        Node<T> element = search((T) o);
        element.left.parent = null;
        element.right.parent = null;
        root = merge(element.left, element.right);
        size--;
        return true;
    }

    private Node<T> maxNode(Node<T> node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

    @SuppressWarnings("unchecked")
    public class SplayTreeIterator implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }


    @NotNull
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<T> iterator = this.iterator();
        for (int k = 0; k < size - 1; k++) {
            array[k] = iterator.next();
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        Object[] array = new Object[size];
        Iterator<T> iterator = this.iterator();
        for (int k = 0; k < a.length - 1; k++) {
            if (iterator.hasNext())
                array[k] = iterator.next();
        }
        return (T1[]) Arrays.copyOf(array, array.length, a.getClass());
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        for (Object e : c) {
            if (!remove((T) e)) return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }


    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SplaySubTree<>(this, fromElement, toElement, true);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SplaySubTree<>(this, null, toElement, false);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SplaySubTree<>(this, fromElement, null, true);
    }
}


class SplaySubTree<V extends Comparable<V>> extends SplayTree<V> {
    private SplayTree<V> tree;
    private V fromElement, toElement;
    private boolean containsEdge;

    SplaySubTree(SplayTree<V> tree, V fromElement, V toElement, boolean containsEdge) {
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

    private int countSize(Node<V> root) {
        int size = 0;
        if (root != null) {
            if (isInside(root.value)) {
                ++size;
            }
            size += countSize(root.right);
            size += countSize(root.left);
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int size() {
        return countSize(tree.getRoot());
    }
}


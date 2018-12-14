import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class SplayTreeSortedMap<T extends Comparable<T>, V> implements SortedMap<T, V> {

    private Node root = null;

    private int size = 0;

    protected final class Node<T, V> implements Map.Entry<T, V> {
        private final T key;
        private V value;
        private Node<T, V> parent = null, left = null, right = null;

        Node(T key, V value) {
            this.value = value;
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V last = this.value;
            this.value = value;
            return last;
        }

        public T getKey() {
            return key;
        }

        public Node<T, V> getLeft() {
            return left;
        }

        public Node<T, V> getRight() {
            return right;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry))
                return false;
            Map.Entry o = (Map.Entry) obj;
            return Objects.equals(key, o.getKey()) &&
                    Objects.equals(value, o.getValue());
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("Key: " + key);
            if (left != null) str.append(" Left: ").append(left.key);
            if (right != null) str.append(" Right: ").append(right.key);
            if (parent != null) str.append(" Parent: ").append(parent.key);
            if (value != null) str.append(" Value: ").append(value);
            return str.toString();
        }
    }

    public Node getRoot() {
        return root;
    }

    private void splay(Node<T, V> node) {
        while (node.parent != null) {
            if (node.parent == null) return;

            Node<T, V> parent = node.parent;
            Node<T, V> gparent = parent.parent;

            if (gparent == null) {
                zig(node);
                return;
            } else {
                if (gparent.left == parent && parent.left == node ||
                        gparent.right == parent && parent.right == node) {
                    zigZig(node);

                } else if (gparent.left == parent && parent.right == node ||
                        gparent.right == parent && parent.left == node) {
                    zigZag(node);
                }
            }
        }
    }

    private void nodeUp(Node<T, V> node) {
        Node<T, V> parent = node.parent;
        Node<T, V> gparent = parent.parent;
        Node<T, V> nodeRight = node.right;
        Node<T, V> nodeLeft = node.left;
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

    private void zig(Node<T, V> node) {
        nodeUp(node);
    }

    private void zigZig(Node<T, V> node) {
        nodeUp(node.parent);
        nodeUp(node);
    }

    private void zigZag(Node<T, V> node) {
        nodeUp(node);
        nodeUp(node);
    }

    private Node<T, V> search(T node) {
        Node<T, V> currentNode = root;
        while (currentNode != null) {
            if (node.compareTo(currentNode.key) == 0) {
                splay(currentNode);
                return currentNode;
            } else if (node.compareTo(currentNode.key) < 0) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
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
    public V get(Object key) {
        return search((T) key).value;
    }

    @Nullable
    @Override
    public V put(T key, V value) {
        //searching parent with "null"
        Node<T, V> currentNode = root;
        Node<T, V> parentForCurrNode = null;

        while (currentNode != null) {
            parentForCurrNode = currentNode;
            if (key.compareTo(currentNode.key) == 0) {
                splay(currentNode);
                return currentNode.setValue(value);
            }
            if (key.compareTo(currentNode.key) < 0)
                currentNode = currentNode.left;
            else currentNode = currentNode.right;
        }

        //create new node
        Node<T, V> newNode = new Node(key, value);
        newNode.parent = parentForCurrNode;

        //paste new node
        if (parentForCurrNode == null)
            root = newNode;
        else if (newNode.key.compareTo(parentForCurrNode.key) < 0)
            parentForCurrNode.left = newNode;
        else parentForCurrNode.right = newNode;

        splay(newNode);
        size++;
        return null;
    }

    private Node<T, V> merge(Node<T, V> tree1, Node<T, V> tree2) {
        if (tree1 == null) return tree2;
        if (tree2 == null) return tree1;
        tree1 = search((maxNode(tree1)).key);
        if (tree1.key.compareTo(tree2.key) > 0) throw new IllegalArgumentException();
        tree1.right = tree2;
        tree2.parent = tree1;
        return tree1;
    }

    public boolean removeNode(Object o) {
        return remove(o) != null;
    }


    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object o) {
        if (o == null || root == null) return null;
        Node<T, V> element = search((T) o);
        if (element == null) return null;
        V value = element.value;
        Node<T, V> left = element.left;
        Node<T, V> right = element.right;
        size--;
        if (right == null && left == null) {
            root = null;
            return null;
        } else if (right == null) {
            left.parent = null;
            root = left;
            return value;
        } else if (left == null) {
            right.parent = null;
            root = right;
            return value;
        }
        left.parent = null;
        right.parent = null;
        root = merge(element.left, element.right);
        return value;
    }

    @Override
    public void putAll(@NotNull Map<? extends T, ? extends V> m) {
        m.forEach((k, v) -> put(k, v));
    }

    private Node<T, V> maxNode(Node<T, V> node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }


    @SuppressWarnings("unchecked")
    public class SplayTreeIteratorPseudo {

        private Node<T, V> current = null;
        private Node<T, V> nextAfterRemove = null;

        private Node<T, V> findNext() {
            if (nextAfterRemove != null) return nextAfterRemove;
            Node cur = current;

            if (cur == null) {
                return first();
            }

            if (cur.right != null) {
                cur = cur.right;
                while (cur.left != null) cur = cur.left;
                return cur;
            }

            while (cur.parent != null) {
                if (cur.parent.left == cur) {
                    cur = cur.parent;
                    return cur;
                }
                cur = cur.parent;
            }

            return null;
        }

        private Node<T, V> first() {
            if (root == null) throw new NoSuchElementException();
            Node<T, V> cur = root;
            while (cur.left != null) {
                cur = cur.left;
            }
            return cur;
        }

        public boolean hasNext() {
            return findNext() != null;
        }

        public Node<T, V> nextNode() {
            current = findNext();
            if (nextAfterRemove != null) nextAfterRemove = null;
            if (current == null) throw new NoSuchElementException();
            return current;
        }

        public void remove() {
            nextAfterRemove = findNext();
            SplayTreeSortedMap.this.remove(current.key);
        }
    }


    @Override
    public Collection<V> values() {
        List array = new LinkedList();
        SplayTreeIteratorPseudo iter = new SplayTreeIteratorPseudo();
        while (iter.hasNext()) array.add(iter.nextNode());
        return array;
    }


    @Override
    public boolean containsValue(Object value) {
        SplayTreeIteratorPseudo iter = new SplayTreeIteratorPseudo();
        while (iter.hasNext()) if (iter.nextNode().value == value) return true;
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return search((T) key) != null;
    }

    @Override
    public T firstKey() {
        if (isEmpty()) throw new NoSuchElementException("NoSuchElementException");
        Node<T, V> current = root;
        while (current.left != null) {
            current = current.left;
        }
        splay(current);
        return current.key;
    }

    @Override
    public T lastKey() {
        if (isEmpty()) throw new NoSuchElementException("NoSuchElementException");
        Node<T, V> currentNode = root;
        while (currentNode.right != null) {
            currentNode = currentNode.right;
        }
        splay(currentNode);
        return currentNode.key;
    }

    final class EntryIterator extends SplayTreeIteratorPseudo
            implements Iterator<Map.Entry<T, V>> {
        public final Map.Entry<T, V> next() {
            return nextNode();
        }
    }

    @Override
    public Set<Entry<T, V>> entrySet() {
        return new EntrySet();
    }

    final class EntrySet extends AbstractSet<Map.Entry<T, V>> {
        public final int size() {
            return size;
        }

        public final void clear() {
            root = null;
            size = 0;
        }

        public final Iterator<Map.Entry<T, V>> iterator() {
            return new EntryIterator();
        }

        public final boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

            T key = (T) e.getKey();
            Node<T, V> candidate = search(key);
            return candidate != null && candidate.equals(e);
        }

        public final boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                Object key = e.getKey();
                return removeNode(key);
            }
            return false;
        }

        public final void forEach(Consumer<? super Entry<T, V>> action) {
            if (action == null)
                throw new NullPointerException();
            SplayTreeIteratorPseudo k = new SplayTreeIteratorPseudo();
            while (k.hasNext()) {
                action.accept(k.nextNode());
            }
        }
    }

    final class KeyIterator extends SplayTreeIteratorPseudo
            implements Iterator<T> {
        public final T next() {
            return nextNode().key;
        }
    }

    @Override
    public Set<T> keySet() {
        return new keySet();
    }

    final class keySet extends AbstractSet<T> {
        public final int size() {
            return size;
        }

        public final void clear() {
            root = null;
            size = 0;
        }

        public final Iterator<T> iterator() {
            return new KeyIterator();
        }

        public final boolean contains(Object o) {
            return containsKey(o);
        }

        public final boolean remove(Object o) {
            return SplayTreeSortedMap.this.remove(o) != null;

        }

        public final void forEach(Consumer<? super T> action) {
            if (action == null)
                throw new NullPointerException();
            SplayTreeIteratorPseudo k = new SplayTreeIteratorPseudo();
            while (k.hasNext()) {
                action.accept(k.nextNode().key);
            }
        }
    }


    @NotNull
    @Override
    public SortedMap<T, V> subMap(T fromKey, T toKey) {
        return new SplaySubSortedMap<>(this, fromKey, toKey, true);
    }

    @NotNull
    @Override
    public SortedMap<T, V> headMap(T toKey) {

        return new SplaySubSortedMap<>(this, null, toKey, false);
    }

    @NotNull
    @Override
    public SortedMap<T, V> tailMap(T fromKey) {
        return new SplaySubSortedMap<>(this, fromKey, null, false);
    }

    class SplaySubSortedMap<T extends Comparable<T>, V> extends SplayTreeSortedMap<T, V> {
        private SplayTreeSortedMap<T, V> tree;
        private T fromElement, toElement;
        private boolean containsEdge;

        SplaySubSortedMap(SplayTreeSortedMap<T, V> tree, T fromElement, T toElement, boolean containsEdge) {
            this.tree = tree;
            this.fromElement = fromElement;
            this.toElement = toElement;
            this.containsEdge = containsEdge;
        }

        private boolean isInside(T v) {
            return (fromElement == null || v.compareTo(fromElement) > 0 || containsEdge && v.compareTo(fromElement) == 0) &&
                    (toElement == null || v.compareTo(toElement) < 0 || containsEdge && v.compareTo(toElement) == 0);
        }

        @Override
        public @Nullable V put(T t, V v) {
            if (isInside(t)) {
                return tree.put(t, v);
            }
            throw new IllegalArgumentException("IllegalArgumentException");
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsKey(Object o) {
            return isInside((T) o) && tree.containsKey(o);
        }

        @Override
        public boolean containsValue(Object o) {
            Iterator iter = tree.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                if (e.getValue() instanceof Comparable) {
                    if (((Comparable) e.getValue()).compareTo(o) == 0) return true;
                }
            }
            return false;
        }

        private int countSize(Node<T, V> root) {
            int size = 0;
            if (root != null) {
                if (isInside(root.key)) {
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


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

}

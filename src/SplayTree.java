import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SplayTree<T extends Comparable<T>> implements SortedSet<T> {

    private Node root = null;

    private int size = 0;

    public class Node<T> {
        private final T value;
        private Node<T> parent = null, left = null, right = null;

        Node(T value) {
            this.value = value;
        }
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

    private void changeParent(Node<T> parent, Node<T> node) {
        Node<T> right = node.right;
        Node<T> left = node.left;
        Node<T> parentParent = parent.parent;

        if (parent.left == node) {
            node.right = parent;
            parent.left = right;
            if (right != null)
                right.parent = parent;
        } else {
            node.left = parent;
            parent.right = left;
            if (left != null)
                left.parent = parent;
        }
        node.parent = parentParent;
        parent.parent = node;

        if (node.parent == null) root = node;
        else {
            if (parentParent.left != null && parentParent.left == parent)
                parentParent.left = node;
            if (parentParent.right != null && parentParent.right == parent)
                parentParent.right = node;
        }
    }

    private void zig(Node<T> parent, Node<T> node) {
        changeParent(parent, node);
        root = node;
    }

    private void zigZig(Node<T> gparent, Node<T> parent, Node<T> node) {
        changeParent(gparent, parent);
        changeParent(parent, node);
    }

    private void zigZag(Node<T> gparent, Node<T> parent, Node<T> node) {
        changeParent(parent, node);
        changeParent(gparent, node);
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

    @Override
    public boolean contains(Object e) {
        T element = (T) e;
        Node<T> elemTree = search(element);
        return element != null && elemTree.value.compareTo(element) == 0;
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

    public boolean addAllElements(T... c) {
        addAll(Arrays.asList(c));
        return true;
    }

    public Node<T> merge(Node<T> tree1, Node<T> tree2) {
        if (tree1 == null) return tree2;
        if (tree2 == null) return tree1;

        tree1 = search((maxNode(tree1)).value);
        tree1.right = tree2;
        return tree1;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || root == null) return false;
        Node<T> element = search((T) o);
        root = merge(element.left, element.right);
        size--;
        return true;
    }

    private Node<T> maxNode(Node<T> node) {
        if (node.right != null) {
            return maxNode(node.right);
        } else return node;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

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
        return new Object[0];
    }

    @NotNull
    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return null;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return null;
    }

}
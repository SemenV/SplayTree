import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public class SplayTree<T extends Comparable<T>> implements SortedSet<T> {

    private Node root = null;

    public int count = 0;

    private class Node<E> {
        private final E value;
        private Node<E> parent, left, right;

        Node(E value) {
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
        count++;
        return true;
    }


    @Nullable
    private void splay(Node<T> node) {
        if (node.parent == null) return;
        while (node != root) {

            Node<T> parent = node.parent;
            Node<T> gparent = parent.parent;

            if (gparent == null) {
                if (parent.left == node) {
                    zig(parent, node);
                    return;
                } else {
                    zig(parent, node);
                    return;
                }

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

        if (parent.left == node) {
            node.right = parent;
            parent.left = node.right;
            if (right != null)
                right.parent = parent;

        } else {
            node.left = parent;
            parent.right = node.left;
            if (left != null)
                left.parent = parent;
        }
        parent.parent = node;
    }

    private void zig(Node<T> parent, Node<T> node) {
        changeParent(parent, node);
        node.parent = null;
    }

    private void zigZig(Node<T> gparent, Node<T> parent, Node<T> node) {
        Node<T> startParent = gparent.parent;
        changeParent(gparent, parent);
        parent.parent = startParent;
        changeParent(parent, node);
        node.parent = startParent;
    }

    private void zigZag(Node<T> gparent, Node<T> parent, Node<T> node) {
        Node<T> startParent = parent.parent;
        changeParent(parent, node);
        node.parent = startParent;
        changeParent(gparent, node);
        node.parent = startParent;
    }

    private Node<T> search(T value) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return true;
    }

    @Override
    public boolean contains(Object e) {
        return false;
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

    @Override
    public T first() {
        return null;
    }

    @Override
    public T last() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return null;
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
    public boolean remove(Object o) {
        return false;
    }


    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }


}
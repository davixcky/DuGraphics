package DuGraphics.services.data.LinkedList;

import java.util.Collection;
import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {
    ListNode<T> head, tail;
    int length;

    public LinkedList() {
        head = tail = null;
        length = 0;
    }

    public void insert(T value) {
        ListNode<T> newNode = new ListNode<>(value);
        length++;
        if (head == null) {
            head = tail = newNode;
            return;
        }

        tail.setNext(newNode);
        tail = newNode;
    }

    public void add(T value) {
        insert(value);
    }

    public void clear() {
        reset();
    }

    public void reset() {
        head = tail = null;
        length = 0;
    }

    public void addAll(Collection<? extends T> c) {
        clear();
        for (T el: c) {
            insert(el);
        }
    }

    public void remove(Object o) {
        if (o == null || head == null) return;

        if (head.getValue().equals(o)) { // The target node is the head
            head = head.next;

            if (tail.getValue().equals(o)) {
                tail = head;
            }

            length--;
            return;
        }

        ListNode<T> current = head;
        while (current.next != null && !current.next.getValue().equals(o)) {
            current = current.next;
        }

        if (current.next == null || !current.next.getValue().equals(o)) {
            return;
        }

        length--;
        current.next = current.next.next;

        if (current.next == null) { // The deleted node, was the last one in the list
            tail = current;
        }

    }

    public T get(int index) {
        ListNode<T> node = getNode(index);
        if (node == null) return null;

        return node.getValue();
    }

    private ListNode<T> getNode(int index) {
        if (index >= length) return null;

        ListNode<T> current = head;
        int i = 0;
        while (i < index) {
            i++;
            current = current.next;
        }

        return current;
    }

    public void add(int index, T value) {
        ListNode<T> node = getNode(index);
        if (node == null) {
            insert(value);
            return;
        }

        node.setValue(value);
    }

    public boolean contains(Object o) {
        for (T node: this) {
            if (node.equals(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder listNodes = new StringBuilder();
        for (T node : this) {
            listNodes.append(node).append(" ");
        }

        return "LinkedList{" +
                "head=" + head +
                "nodes=" + listNodes +
                '}';
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<>() {
            private ListNode<T> currentNode = head;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public T next() {
                ListNode<T> tmp = currentNode;
                currentNode = currentNode.getNext();
                return tmp.getValue();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

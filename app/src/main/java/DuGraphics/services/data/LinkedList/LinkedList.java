package DuGraphics.services.data.LinkedList;

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

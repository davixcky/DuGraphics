package DuGraphics.services.data.LinkedList;

import DuGraphics.services.data.common.Node;

public class ListNode<T> extends Node<T> {
    ListNode<T> next;

    public ListNode(T value) {
        super(value);
        this.value = value;
        this.next = null;
    }

    public ListNode<T> getNext() {
        return next;
    }

    public void setNext(ListNode<T> next) {
        this.next = next;
    }
}

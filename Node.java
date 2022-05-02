public class Node<T> {
    private Node<T> next;
    private Node<T> down;
    private Node<T> up;
    private T payload;

    //default constructor
    public Node() {
        this.next = null;
        this.down = null;
        this.payload = null;
        this.up = null;
    }
    // overloaded constructor
    public Node(Node<T> nextNode, Node<T> downNode, Node<T> upNode,T temp) {
        this.next = nextNode;
        this.down = downNode;
        this.up = upNode;
        this.payload = temp;
    }

    // mutators
    public void setDown(Node<T> down) {
        this.down = down;
    }
    public void setNext(Node<T> next) {
        this.next = next;
    }
    public void setPayload(T payload) {
        this.payload = payload;
    }
    public void setUp(Node<T> up) { this.up = up; }

    // accessors
    public Node<T> getDown() {
        return down;
    }
    public Node<T> getNext() {
        return next;
    }
    public T getPayload() {
        return payload;
    }

}

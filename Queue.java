
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Queue<V> implements Iterable<V> {
    private Node<V> first;    // beginning of queue
    private Node<V> last;     // end of queue
    private int length;               // number of elements on queue

  
    public Queue() {
        first = null;
        last  = null;
        length = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }


    public int size() {
        return length;
    }

    public void add(Node <V> newNode) {
        Node<V> oldLast = last;
        last = newNode;
        last.next = null;
        last.previous = oldLast;
        if (isEmpty()) first = last;
        else           oldLast.next = last;
        length++;
    }

    // Remove a specific node in the linked list in O(1) time
    public void delete(Node<V> trashNode) {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
       trashNode.previous.next = trashNode.next;
       trashNode = null;
       length--;
      
    }

    public Iterator<V> iterator()  {
        return new ListIterator<V>(first);  
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<V> implements Iterator<V> {
        private Node<V> current;

        public ListIterator(Node<V> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public V next() {
            if (!hasNext()) throw new NoSuchElementException();
            V data = current.data;
            current = current.next; 
            return item;
        }
    }






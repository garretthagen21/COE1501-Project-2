import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PHPArray<V>implements Iterable<V>{
	private int itemCount=0;
	private Node<V> [] hashTable;
	private Node<V> first;
	private Node<V> last;
	private PHPArrayIterator<V> internalIterator;

	@SuppressWarnings("unchecked")
	public PHPArray(int capacity){
		this.hashTable =  (Node<V>[])new Node[capacity];
	}
	
	/**Integer override functions**/
	public void put(int key,V value) { put(Integer.toString(key),value);	}
	public V get(int key) { return get(Integer.toString(key)); }
	public void unset(int key) {unset(Integer.toString(key)); }
	
	/**Getters and Setters**/
	public boolean isEmpty() { return (itemCount==0);}
	public int length(){ return itemCount; }
	
	/**Implementation**/
	public void put(String key,V value) {
		Node <V> newNode = getNode(key);	
		
		//The node exists so just change the value
		if(newNode != null) {
			newNode.pair.value = value;
			return;
		}
		//else create the new node and place it in the hashtable, then connect it to the linked list
		checkSize();
		
		newNode = new Node<V>(key,value);
		putNode(newNode);
		
		if(isEmpty()) {
			first = newNode;
			reset();
		}
		else {
			last.next = newNode;
			newNode.previous = last;
		}
		last = newNode;
		itemCount++;	
	}
	public void putNode(Node<V> currentNode) {
		int hashCode = currentNode.pair.key.hashCode() & 0x7fffffff;
		//System.out.println("Placing key: "+currentNode.pair.key+" with hashcode "+hashCode);
		for(int i = 0; i<hashTable.length; i++) {
	
			int probe = (hashCode+i) % hashTable.length;
			
			if(hashTable[probe] == null || hashTable[probe].isDeleted) {
				hashTable[probe] = currentNode;
				return;		
			}
		}
	}

	public V get(String key) {
		Node<V> foundNode = getNode(key);
		if(foundNode != null)
			return foundNode.pair.value;
		return null;
	}
	private Node<V> getNode(String key){
		if(isEmpty()) return null;
	
		int hashCode = key.hashCode() & 0x7fffffff;
		
		for(int i = 0; i<hashTable.length; i++) {
			int probe = (hashCode+i) % hashTable.length;
			//We have found a null location so quit searching
			if(hashTable[probe] == null) {
				break;
			}
			//We have found the node and it has not been deleted
			if(hashTable[probe].pair.key.equals(key) && !hashTable[probe].isDeleted) {
				return hashTable[probe];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void checkSize() {
		if(itemCount < (hashTable.length / 2)) return;
		
		System.out.println("\t\tSize: "+length()+" ---resizing hash table from "+hashTable.length+" to "+2*hashTable.length);
		
		//Double table size and make a temp copy to rehash into the new table
		Node<V>[] tempTable = hashTable;
		hashTable = (Node<V>[])new Node[2*hashTable.length];
		
		for(int i = 0;i<tempTable.length;i++) {
			if(tempTable[i] != null && !tempTable[i].isDeleted)
				putNode(tempTable[i]);	
		}
		
	}
	
	public void unset(String key) {
		Node<V> foundNode = getNode(key);
	
		if(foundNode == null) 
			return;
		
			
		Node<V> prevNode = foundNode.previous;
		Node<V> nextNode = foundNode.next;
		
		
		if(nextNode != null)
			nextNode.previous = prevNode;
		else
			last = prevNode;
		
		if(prevNode != null)
			prevNode.next = nextNode;
		else
			first = nextNode;
		
		foundNode.isDeleted = true;
		itemCount--;
	}
	
	
	

	
	public void showTable() {
		System.out.println("Raw Hash Table Contents:\n");
		for(int i = 0;i<hashTable.length;i++) {
			System.out.print(i+": ");
			Node<V> currentNode = hashTable[i];
			if(currentNode !=null)
				System.out.println("Key: "+currentNode.pair.key+" Value: "+currentNode.pair.value);
			else
				System.out.println("null");
		}
		
	}
	
	public ArrayList<String> keys(){
		ArrayList<String> theKeys = new ArrayList<String>();
		PHPArrayIterator<V> tempIterator = new PHPArrayIterator(first);
		while(tempIterator.hasNext()) {
			theKeys.add(tempIterator.nextPair().key);
		}
		return theKeys;
	}
	public ArrayList<V> values(){
		ArrayList<V> theValues = new ArrayList<V>();
		PHPArrayIterator<V> tempIterator = new PHPArrayIterator(first);
		while(tempIterator.hasNext()) {
			theValues.add(tempIterator.nextPair().value);
		}
		return theValues;
		
	}
	public Pair<V> each(){
		if(internalIterator == null) {return null;}
		return internalIterator.nextPair();
	}
	public void reset() {
		internalIterator = new PHPArrayIterator(first);
	}
	
	public Iterator<V> iterator() {
		return new PHPArrayIterator(first);
	}
	
	public static class Pair<V>{
		public String key;
		public V value;
		public Pair(String key,V value) {
			this.key = key;
			this.value = value;
		}
	}
	private class Node<V>{
		private Pair<V> pair;
		private boolean isDeleted = false;
		private Node next;
		private Node previous;
		
		private Node(String key,V value) {
			this.pair = new Pair(key,value);
		}	
	}
	private class PHPArrayIterator<V> implements Iterator<V> {
        private Node<V> current;
        
        public PHPArrayIterator(Node<V> first) {
            current = first;
        }
        public boolean hasNext()  { return current != null; }

        public V next() {
            return nextPair().value;
        }
        public Pair<V> nextPair(){
        	  if (!hasNext()) return null;
              Pair<V> pair = current.pair;
              current = current.next; 
              return pair;
        }
    }
	public PHPArray<String> array_flip() {
		// TODO Auto-generated method stub
		return null;
	}



	public void sort() {
		// TODO Auto-generated method stub
		
	}

	public void asort() {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
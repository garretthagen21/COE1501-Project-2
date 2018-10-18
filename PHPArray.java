import java.util.*;

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
			
			if(hashTable[probe] == null) {
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
			if(hashTable[probe].pair.key.equals(key)) {
				return hashTable[probe];
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void checkSize() {
		if(itemCount < (hashTable.length / 2)) return;
		
		System.out.println("\t\tSize: "+length()+" -- resizing array from "+hashTable.length+" to "+2*hashTable.length);
		
		//Double table size 
		hashTable = (Node<V>[])new Node[2*hashTable.length];
		
		//Iterate through the linked list and rehash each node
		Node<V> currentNode = first;
		while(currentNode != null) {
			putNode(currentNode);
			currentNode = currentNode.next;
		}
		
	}
	
	public void unset(String key) {
	
		if(isEmpty()) return;
		
		Node<V> deleteNode = null;
		
		int hashCode = key.hashCode() & 0x7fffffff;
		for(int i = 0; i<hashTable.length; i++) {
			int probe = (hashCode+i) % hashTable.length;
			//We have found a null location so quit searching
			if(hashTable[probe] == null) {
				return;
			}
			//We have found the node and it has not been deleted
			if(hashTable[probe].pair.key.equals(key)) {
				deleteNode = hashTable[probe];
				
				if(deleteNode.next != null) { deleteNode.next.previous = deleteNode.previous; }
				else { last = deleteNode.previous; }
				
				if(deleteNode.previous != null) { deleteNode.previous.next = deleteNode.next; }
				else { first = deleteNode.next; }
				
				hashTable[probe] = null;
				itemCount--;
				rehash(hashCode+i,1); 				//Rehash the remainder of the cluster
				return;
		}	
	  }
	}
	//Recursive method to rehash cluster nodes
	private void rehash(int deleteIndex,int i) {
		int probe = (deleteIndex + i) % hashTable.length;
		Node<V> clusterNode = hashTable[probe];
		
		if(clusterNode == null)
			return;
		
		hashTable[probe] = null;
		System.out.println("\t\tKey "+clusterNode.pair.key+" rehashed...\n");
		putNode(clusterNode);
		rehash(deleteIndex,i+1);
		
	}
	
	public void showTable() {
		System.out.println("\tRaw Hash Table Contents:");
		for(int i = 0;i<hashTable.length;i++) {
			System.out.print(i+": ");
			Node<V> currentNode = hashTable[i];
			if(currentNode != null)
				System.out.println("Key: "+currentNode.pair.key+" Value: "+currentNode.pair.value);
			else
				System.out.println("null");
		}
		
	}
	public PHPArray<String> array_flip() throws ClassCastException{
		this.reset();
		PHPArray<String> tableFlip = new PHPArray<String>(this.hashTable.length);
		Pair<V> currentPair;
		while((currentPair = this.each()) != null) {
			tableFlip.put((String)currentPair.value, currentPair.key);
		}
		return tableFlip;
	}
	//EXTRA CREDIT IMPLEMENTATION OF array_reverse()
	public PHPArray<V> array_reverse() throws ClassCastException{
		PHPArray<V> tableReverse = new PHPArray<V>(this.hashTable.length);
		Node<V> currentNode = last;
		while(currentNode != null) {
			tableReverse.put(currentNode.pair.key, currentNode.pair.value);
			currentNode = currentNode.previous;
		}
		return tableReverse;
	}
	
	//EXTRA CREDIT IMPLEMENTATION OF shuffle()
	public void shuffle() throws ClassCastException{
			ArrayList<Pair<V>> pairs = (ArrayList<Pair<V>>)pairs();
			Collections.shuffle(pairs);
			clearTable();
			//Rebuild the hash table and linked list
			for(int i = 0;i<pairs.size();i++) {
				this.put(pairs.get(i).key,pairs.get(i).value);
			}
		}
		
	private void clearTable() {
		hashTable =  (Node<V>[])new Node[hashTable.length];
		itemCount = 0;
		first = last = null;
	}
	
	public void sort() throws ClassCastException {
		ArrayList<Pair<V>> pairs = (ArrayList<Pair<V>>)pairs();
		Collections.sort(pairs);
		clearTable();
		//Rebuild the hash table and linked list
		for(int i = 0;i<pairs.size();i++) {
				this.put(i,pairs.get(i).value);
			}
	  	}
	

	public void asort() {
		ArrayList<Pair<V>> pairs = (ArrayList<Pair<V>>)pairs();
		Collections.sort(pairs);
		clearTable();
		//Rebuild the hash table and linked list
		for(int i = 0;i<pairs.size();i++) {
			this.put(pairs.get(i).key,pairs.get(i).value);
		}
		
	}
	public ArrayList<Pair<V>> pairs(){
		ArrayList<Pair<V>> thePairs = new ArrayList<Pair<V>>();
		PHPArrayIterator<V> tempIterator = new PHPArrayIterator(first);
		while(tempIterator.hasNext()) {
			thePairs.add(tempIterator.nextPair());
		}
		return thePairs;
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
	
	public static class Pair<V> implements Comparable<Pair<V>>{
		public String key;
		public V value;
		public Pair(String key,V value) {
			this.key = key;
			this.value = value;
		}
		@Override
		public int compareTo(Pair<V> p) throws ClassCastException{
			 return ((Comparable) this.value).compareTo((Comparable) p.value);
		}
	}
	private class Node<V>{
		private Pair<V> pair;
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


	
	
	
	
}
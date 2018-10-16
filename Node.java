
public class Node<V>{
		public V key;
		public V value;
		public boolean isDeleted = false;
		public Node next;
		public Node previous;
		
		public Node(V key,V value) {
			this.key = key;
			this.value = value;	
		}
		
	}
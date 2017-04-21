
/**
 * @author denisavataksi
 * Structures a list of Customers, each node contains unique customer data collected from
 * the customersfile.txt and a reference to the next customer
 * Customers are served on a FIFO basis.
 */

public class QueueLinkedList implements Queue<Customer>{

	//first and last elements
	private Customer first; private Customer last;
	
	//default empty list
	public QueueLinkedList(){
		first = null;
		last = null;
	}
	public Customer getFirst(){
		return first;
	}
	
	public Customer getLast(){
		return last;
	}

	/*
	 * Linked List properties
	 */
	public boolean isEmpty(){
		return (first == null);
	}
	
	public void addFirst(Customer data){
		data.setNext(first); 
		first = data;
	}
	public void addLast(Customer data){
		if(isEmpty()){
			data.setNext(first);
			first = data;
			last = data;
		}else{
			last.setNext(data);
			last = data;
			last.setNext(null);
		}
	}
	
	public void removeFirst(){
		Customer temp = first;
		if(first.getNext() == null)
			last = null;
		first = first.getNext();
	}
	
	public void displayList(){
		Customer current = first;
		while(current != null){
			current.displayCustomer();
			current = current.getNext();
		}
	}
	/*
	 * Queue Properties
	 */
	@Override
	public void enqueue(Customer data) {
       this.addLast(data);
   }

	@Override
    public Customer dequeue() {
        if(!this.isEmpty()){
        	Customer temp = first;
            this.removeFirst();
            return temp;
        }return null;		
    }
}

package utilities.eq_classes;

/**
 * Generic LinkedList implementation. Contains a private generic Node class.
 * @author James, Ian, Hanna
 * @date 9/23
 */
public class LinkedList<T>
{
	private class Node<T>
	{
	    T _data;
	    Node<T> _next;

	    /**
	     * Node constructor
	     * calls overloaded (T, Node) constructor
	     */
	    public Node()
	    {
	        this(null, null);
	    }

	    /**
	     * initializes _data and _next
	     * @param T d data of node being created
	     * @param Node n next Node from Node being created
	     */
	    public Node(T d, Node<T> n)
	    {
	        _data = d;
	        _next = n;
	    }
	}
	
    Node<T> _head;
    Node<T> _tail;
    int _size;

    /**
     * LinkedList constructor
     * sets _head and _tail data to null and connects head to tail
     * sets _size to 0
     */
    public LinkedList()
    {
        _head = new Node<T>(null, null);
        _tail = new Node<T>(null, null);
        _head._next = _tail;
        _size = 0;
    }    

    /**
     * @return boolean _size is 0
     */
    public boolean isEmpty() { return _size == 0;}

    /**
     * sets _size to 0
     * removes nodes by omission
     */
    public void clear()
    {
        _size = 0;
        _head._next = _tail;
    }

    /**
     * @return int _size instance variable
     */
    public int size() { return _size; }

    /**
     * creates and inserts new Node containing element after _head
     * increments _size
     * @param T element. data to add to LinkedList
     */
    public void addToFront(T element)
    {
        Node<T> nodeToAdd = new Node<T>(element, _head._next);
        _head._next = nodeToAdd;
        _size++;
    }

    /**
     * public-facing front for recursive contains
     * @param T target to look for
     * @return false if target == null or not found (reaches _tail), true if found
     */
    public boolean contains(T target)
    {
    	if (target == null) return false;
    	return contains(target, _head._next);   	
    }
   
    /**
     * recursively look for a target in the LinkedList
     * target will not be null (filtered out in public contains)
     * @param T target to look for
     * @param Node n current Node
     * @return boolean true if found, false if not found (reaches tail)
     */
    public boolean contains(T target, Node<T> n)
    {
    	if (n == _tail) return false;
    	if(target.equals(n._data)) return true;
    	n = n._next;
    	return(contains(target,n));
    }

    /**
     * public-facing for recursive call
     * finds the node before the Node containing target
     * @param T target data of Node you want the preceding Node of
     * @return Node<T> preceding node
     */
    private Node<T> previous(T target)
    {
    	return previous(target, _head);
    }
    
    /**
     * recursively search for Node containing target and return next Node
     * @param T target data of Node you want preceding Node of
     * @param Node n current Node
     * @return Node<T> preceding Node
     */
    private Node<T> previous(T target, Node<T> n)
    {
    	if (n == _tail) return null;
    	if (n._next._data.equals(target)) return n;
    	return previous(target, n._next);
    }
    
    /**
     * removes a Node from the LinkedList by omission
     * decrements _size
     * @param T target data of Node to remove
     * @return boolean true if Node found and removed, false if not found 
     */
    public boolean remove(T target)
    {
    	if (!this.contains(target)) return false;
        Node<T> prev = this.previous(target);
        prev._next = prev._next._next;
        _size--;
        return true;
    }
    
    /**
     * private helper for addToBack
     * @return Node before _tail
     */
    private Node<T> last()
    {
    	return last(_head);
    }
    
    /**
     * recursively search for Node before _tail
     * @param Node n current Node
     * @return Node before _tail
     */
    private Node<T> last(Node<T> n)
    {
    	if (n._next == _tail) return n;
    	return last(n._next);
    }

    /**
     * create and insert a Node containing element before _tail
     * increments _size
     * @param T element data to be added
     */
    public void addToBack(T element)
    {
    	Node<T> last = last();
    	last._next = new Node<T>(element, _tail);
    	_size++;
    }

    /**
     * format: "null, x, y, z, null"
     * @return String representation of LinkedList object
     */
    public String toString()
    {
    	String s = "null";
    	return toString(_head._next, s);
    }
    
    /**
     * recursively create a String representation of the LinkedList
     * @param Node n current Node
     * @param String s String so far
     * @return String representation of LinkedList
     * format: "null, x, y, s, null"
     */
    private String toString(Node<T> n, String s)
    {
    	if (n.equals(_tail)) {return s + ", null";}
    	s = s + ", " + n._data;
    	n = n._next;
    	return toString(n, s);
    }

    /**
     * public facing front for recursive reverse
     * reverses the contents of the LinkedList
     */
    public void reverse()
    {	
    	reverse(_head._next);
    }
    
    /*
     * reverses the nodes recursively
     */
    private void reverse(Node<T> n)
    {
    	if (n._next == _tail || n == _tail) {
    		_head._next = n;
    		return;
    	}
    	reverse(n._next);
    	Node<T> n2 = n._next;
    	n2._next = n;
    	n._next = _tail;
    }
    
    /**
     * get data of a Node for testing purposes
     * @param Node t
     * @return _data of t
     */
    public T getData(Node<T> t) {
		return t._data;
	}
	
    /**
     * get next Node for testing purposes
     * @param Node t
     * @return _next of t
     */
	public Node<T> getNext(Node<T> t) {
		return t._next;
	}
}
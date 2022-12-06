package utilities.eq_classes;

/**
 * Equivalence Class using a Linked List to store values equivalent to the canonical value
 * @author James, Ian, Hanna
 * @date 9/23
 */
import java.util.Comparator;

public class LinkedEquivalenceClass<T> extends LinkedList<T>
{	
	 T _canonical;
	 Comparator<T> _comparator;
	 LinkedList<T> _rest;

	 /**
	  * constructor, initializes all attributes
	  * @param Comparator<T> c
	  */
	public LinkedEquivalenceClass(Comparator<T> c)
	{
		_canonical = null;
		_comparator = c;
		_rest = new LinkedList<T>();
	}
	
	/**
	 * @return T _canonical
	 */
	public T canonical() { return _canonical; }
	
	/**
	 * @return true if _rest is empty, false if not
	 * does not consider canonical value
	 */
	public boolean isEmpty() { return _rest.isEmpty(); }
	
	/**
	 * clears _rest and resets _canonical to null
	 */
	public void clear()
	{
		_rest.clear();
		_canonical = null;
	}
	
	/**
	 * clears _rest
	 */
	public void clearNonCanonical()	{ _rest.clear(); }
	
	/**
	 * @return int the number of elements in _rest
	 */
	public int size() { return _rest._size;	}
	
	/**
	 * if canonical is null, adds element as the canonical and returns true
	 * otherwise, 
	 *    if element belongs adds element to _rest and returns true.
	 *    if element does not belong, returns false.
	 * @param T element value to add
	 * @return false if element does not belong, true if element is successfully added as canonical or in _rest
	 */
	public boolean add(T element)
	{
		if (_canonical == null)
		{
			demoteAndSetCanonical(element);
			return true;
		}
		if (belongs(element))
		{
			_rest.addToFront(element);
			return true;
		}
		return false;
	}

	public void addWithoutCheck(T element) {
		_rest.addToFront(element);
	}
	
	/**
	 * check if value is in _rest or canonical
	 * @param T target value to look for in LinkedList
	 * @return true if canonical is the target or if _rest contains the target, false otherwise
	 */
	public boolean contains(T target)
	{
		if (target == null) return _canonical == target || _rest.contains(target);
		if (_canonical == null) return _rest.contains(target);
		return _canonical.equals(target) || _rest.contains(target);
	}
	
	/**
	 * determine if target meets requirements to be in the equivalence class
	 * @param T target value to evaluate in comparator to canonical
	 * @return false if target is null, already in _rest, or not equivalent
	 */
	public boolean belongs(T target)
	{
		if (target == null) return false;
		return (contains(target) || _comparator.compare(_canonical, target) == 0);
	}
	
	/**
	 * remove target value from _rest, does not include canonical
	 * calls removeCanonical if target is the canonical value
	 * @param T target value to remove
	 * @return boolean true if remove was successful, false otherwise
	 */
	public boolean remove(T target)
	{	
		if (target.equals(_canonical)) return removeCanonical();
		return _rest.remove(target);
	}
	
	/**
	 * reset canonical to null
	 * @return boolean true if remove successful
	 */
	public boolean removeCanonical()
	{
		_canonical = null;
		return true;
	}
	
	/**
	 * adds old canonical value to _rest, then sets _canonical to element
	 * will not add null to _rest, but still sets new _canonical
	 * @param T element new value for canonical
	 * @return true if a value is demoted to _rest, false if only _canonical changes
	 */
	public boolean demoteAndSetCanonical(T element)
	{
		if (_canonical == null)
		{
			_canonical = element;
			return false;
		}
		_rest.addToFront(_canonical);
		_canonical = element;
		return true;		
	}
	
	/**
	 * returns String representation of LinkedEquivalenceList
	 * formatted link: 2 | null, 8, 6, 4, null
	 * @return String representation of LinkedEquivalenceClass
	 */
	public String toString()
	{
		if (_canonical == null) return "null | " + _rest.toString();
		return _canonical + " | " + _rest.toString();
	}
}

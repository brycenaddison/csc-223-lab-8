package utilities.eq_classes;

/**
 * ArrayList of LinkedEquivalenceClasses, all with the same comparator.
 * LEC = LinkedEquivalenceClass
 * 
 * @author James, Ian, Hanna
 * @date 9/23
 */
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;

public class EquivalenceClasses<T>
{
	Comparator<T> _comparator;
	List<LinkedEquivalenceClass<T>> _rest;
	
	/**
	 * constructor, initializes all instance variables
	 * _rest set to new ArrayList
	 * @param Comparator<T> c
	 */
	public EquivalenceClasses(Comparator<T> c)
	{
		_comparator = c;
		_rest = new ArrayList<LinkedEquivalenceClass<T>>();
	}
	
	/**
	 * adds element to the proper LEC
	 * null elements cannot be added
	 * if no LEC exists for element, a new one is created and add is called again
	 *    element will become the canonical of this new LEC
	 * @param T element value to add to a LEC
	 * @return boolean false if value is not added (null), true otherwise
	 */
	public boolean add(T element)
	{
		if (element == null) return false;
		for (LinkedEquivalenceClass<T> c : _rest)
		{
			if (c.add(element)) return true;
		}
		_rest.add(new LinkedEquivalenceClass<T>(_comparator));
		return add(element);
	}

	public List<LinkedEquivalenceClass<T>> classes() {
		return _rest;
	}

	/**
	 * looks in the LEC (_rest and canonical) for target
	 * @param T target value to search for
	 * @return boolean true if target is found in any LEC, false otherwise
	 */
	public boolean contains(T target) {
		for (LinkedEquivalenceClass<T> c : _rest)
		{
			if (c.contains(target)) return true;
		}
		return false;
	}
	
	/**
	 * sum the sizes of every value each LEC's _rest, and add one for non-null canonicals
	 * @return int total number of elements in all of the LECs, including canonicals
	 */
	public int size()
	{
		int sum = 0;
		for (LinkedEquivalenceClass<T> lec : _rest)
		{
			sum = sum + lec.size();
			if (lec.canonical() != null) sum += 1;
		}
		return sum;
	}
	
	/**
	 * @return the size of the underlying List
	 */
	public int numClasses() { return _rest.size(); }
	
	/**
	 * loop through the List looking for the LEC containing element
	 * @param T element
	 * @return int index of LEC which element is a member of, -1 if target is not present
	 */
	protected int indexOfClass(T element)
	{
		for (int i = 0; i < this.size(); i++)
		{
			if (_rest.get(i).contains(element)) return i;
		}
		return -1;
	}
	
	/**
	 * format: ""1 | null, 4, 2, null : 7 | null, 13, null"
	 * @return String representation of EquivalenceClasses object
	 */
	public String toString()
	{
		if (_rest.isEmpty()) return "null | null, null";
		String str = "";
		for (LinkedEquivalenceClass<T> c : _rest)
		{
			str = str + c.toString() + " : \n";
		}
		return str.substring(0, str.length() - 3);
	}
}
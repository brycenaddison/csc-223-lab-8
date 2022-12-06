package geometry_objects.angle;

/**
 * This implementation requires greater knowledge of the implementing Comparator.
 * 
 * According to our specifications for the AngleStructureComparator, we have
 * the following cases:
 *
 *    Consider Angles A and B
 *    * Integer.MAX_VALUE -- indicates that A and B are completely incomparable
                             STRUCTURALLY (have different measure, don't share sides, etc. )
 *    * 0 -- The result is indeterminate:
 *           A and B are structurally the same, but it is not clear one is structurally
 *           smaller (or larger) than another
 *    * 1 -- A > B structurally
 *    * -1 -- A < B structurally
 *    
 *    We want the 'smallest' angle structurally to be the canonical element of an
 *    equivalence class.
 * 
 * @author Hanna King
 */

import geometry_objects.angle.comparators.AngleStructureComparator;
import utilities.eq_classes.LinkedEquivalenceClass;

import utilities.eq_classes.LinkedList;

public class AngleLinkedEquivalenceClass extends LinkedEquivalenceClass<Angle>
{

    public static final int STRUCTURALLY_INCOMPARABLE = Integer.MAX_VALUE;

    Angle _canonical;
	AngleStructureComparator _comparator;
	LinkedList<Angle> _rest;

    public AngleLinkedEquivalenceClass()
    {
        super (new AngleStructureComparator());
        _rest = new LinkedList<Angle>();
        _comparator = new AngleStructureComparator();
    }
    public AngleLinkedEquivalenceClass(AngleStructureComparator c)
    {
        super (c);
        _rest = new LinkedList<Angle>();
        _comparator = c;
    }

    // add new element, maintaining canonical as smallest
    // unclear (0) added to _rest
    @Override
    public boolean add(Angle element)
	{
        // null in
        if (element == null) { return false; }

        //empty canonical, just set it
		if (_canonical == null)
		{
			_canonical = element;
			return true;
		}

        // not equivalent angle
        if (!belongs(element)) { return false; }

        // element > canonical. do not replace canonical
        // add element to _rest
        if (_comparator.compare(element, _canonical) >= 0)
        {
            _rest.addToFront(element);
            return true;
        }

        // element < canonical. demote and set.
		_rest.addToFront(_canonical);
		_canonical = element;
		return true;		
	}

    // override to prevent canonical rule being broken
    @Override
    public boolean demoteAndSetCanonical(Angle element) { return false; }

    //
    @Override
    public boolean belongs(Angle element)
    {
        if (element == null) return false;
		return (contains(element) || _comparator.compare(_canonical, element) != STRUCTURALLY_INCOMPARABLE);
    }
}
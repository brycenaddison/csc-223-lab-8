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

public class AngleLinkedEquivalenceClass extends LinkedEquivalenceClass<Angle>
{

    public static final int STRUCTURALLY_INCOMPARABLE = Integer.MAX_VALUE;
    private static final AngleStructureComparator comparator = new AngleStructureComparator();

    public AngleLinkedEquivalenceClass()
    {
        super (comparator);

    }

    /**add new element, maintaining canonical as smallest
     * unclear (0 result) added to _rest
     *
     * null value in is not added, returns false
     * empty canonical is replaced with element and returns true
     * does not belong returns false, no addition done anywhere
     * if the element is >= than the canonical, element is added the _rest and canonical remains. return true
     * lastly (element < canonical), canonical moves to _rest, element becomes the new canonical, and returns true
     * 
	 * @param Angle element value to add
	 * @return false if element does not belong, true if element is successfully added as canonical or in _rest
     */
    @Override
    public boolean add(Angle element)
	{
        // null in
        if (element == null) { return false; }

        //empty canonical, just set it
		if (canonical() == null)
		{
            demoteAndSetCanonical(element);
			return true;
		}

        // not equivalent angle
        if (!belongs(element)) { return false; }


        // element > canonical. do not replace canonical
        // add element to _rest
        if (comparator.compare(element, canonical()) >= 0)
        {
            addWithoutCheck(element);
            return true;
        }

        // element < canonical. demote and set.
		return demoteAndSetCanonical(element);
	}


    // adjust value to check (becomes !STRUCTURALLY_INCOMPARABLE instead of 0)
    @Override
    public boolean belongs(Angle element)
    {
        if (element == null) return false;
		return (contains(element) || comparator.compare(canonical(), element) != STRUCTURALLY_INCOMPARABLE);
    }
}
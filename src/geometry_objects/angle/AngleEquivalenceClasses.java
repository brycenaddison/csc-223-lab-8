package geometry_objects.angle;

import geometry_objects.angle.comparators.AngleStructureComparator;
import utilities.eq_classes.EquivalenceClasses;

import java.util.ArrayList;

/**
 * Given the figure below:
 * 
 *    A-------B----C-----------D
 *     \
 *      \
 *       \
 *        E
 *         \
 *          \
 *           F
 * 
 * Equivalence classes structure we want:
 * 
 *   canonical = BAE
 *   rest = BAF, CAE, DAE, CAF, DAF
 */

import java.util.List;

public class AngleEquivalenceClasses extends EquivalenceClasses<Angle>
{
	//please fix this
	AngleStructureComparator _comparator;
	List<AngleLinkedEquivalenceClass> _rest;

	public AngleEquivalenceClasses()
	{
		super(new AngleStructureComparator());
		_rest = new ArrayList<AngleLinkedEquivalenceClass>();
		_comparator = new AngleStructureComparator();
	}

	/**
	 * identical add function except datatypes changed to match.
	 * 
	 * adds element to the proper LEC
	 * null elements cannot be added
	 * if no LEC exists for element, a new one is created and add is called again
	 *    element will become the canonical of this new LEC
	 * @param T element value to add to a LEC
	 * @return boolean false if value is not added (null), true otherwise
	 */
	@Override
	public boolean add(Angle element)
	{
		if (element == null) return false;
		for (AngleLinkedEquivalenceClass c : _rest)
		{
			if (c.add(element)) return true;
		}
		_rest.add(new AngleLinkedEquivalenceClass(_comparator));
		return add(element);
	}
}
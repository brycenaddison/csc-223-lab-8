package geometry_objects.angle;

import geometry_objects.angle.comparators.AngleStructureComparator;
import utilities.eq_classes.EquivalenceClasses;

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
	AngleStructureComparator _comparator;
	List<AngleLinkedEquivalenceClass> _rest;

	public AngleEquivalenceClasses()
	{
		super(new AngleStructureComparator());
	}

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
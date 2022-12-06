package geometry_objects.angle;

import geometry_objects.angle.comparators.AngleStructureComparator;
import utilities.eq_classes.EquivalenceClasses;
import utilities.eq_classes.LinkedEquivalenceClass;

import java.util.List;

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


public class AngleEquivalenceClasses extends EquivalenceClasses<Angle>
{
	public AngleEquivalenceClasses()
	{
		super(new AngleStructureComparator());
	}

	public boolean add(Angle element)
	{
		if (element == null) return false;
		List<LinkedEquivalenceClass<Angle>> angles = classes();

		for (LinkedEquivalenceClass<Angle> c : angles)
		{
			if (c.add(element)) return true;
		}

		angles.add(new AngleLinkedEquivalenceClass());
		return add(element);
	}

}
/**
 * Write a succinct, meaningful description of the class here. You should avoid wordiness    
 * and redundancy. If necessary, additional paragraphs should be preceded by <p>,
 * the html tag for a new paragraph.
 *
 * <p>Bugs: (a list of bugs and / or other problems)
 *
 * @author <your name>
 * @date   <date of completion>
 */

package geometry_objects.angle.comparators;

import java.util.Comparator;

import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import geometry_objects.points.Point;
import utilities.math.MathUtilities;
import utilities.math.analytic_geometry.GeometryUtilities;

public class AngleStructureComparator implements Comparator<Angle>
{
	public static final int STRUCTURALLY_INCOMPARABLE = Integer.MAX_VALUE;
	
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
	 * What we care about is the fact that angle BAE is the smallest angle (structurally)
	 * and DAF is the largest angle (structurally). 
	 * 
	 * If one angle X has both rays (segments) that are subsegments of an angle Y, then X < Y.
	 * 
	 * If only one segment of an angle is a subsegment, then no conclusion can be made.
	 * 
	 * So:
	 *     BAE < CAE
   	 *     BAE < DAF
   	 *     CAF < DAF

   	 *     CAE inconclusive BAF
	 * 
	 * @param left -- an angle
	 * @param right -- an angle
	 * @return -- according to the algorithm above:
 	 *            Integer.MAX_VALUE will refer to our error result
 	 *            0 indicates an inconclusive result
	 */
	@Override
	public int compare(Angle left, Angle right)
	{
        if (left.equals(right)) return 0;

		if (!left.getVertex().equals(right.getVertex())) return STRUCTURALLY_INCOMPARABLE;

		double left1, left2, right1, right2;

		if (!Segment.overlaysAsRay(left.getRay1(), right.getRay2())) {
			if (!Segment.overlaysAsRay(left.getRay1(), right.getRay1())) return STRUCTURALLY_INCOMPARABLE;

			if (!Segment.overlaysAsRay(left.getRay2(), right.getRay2())) return STRUCTURALLY_INCOMPARABLE;

			// Same rays are collinear

			left1 = left.getRay1().length();
			left2 = left.getRay2().length();
			right1 = right.getRay1().length();
			right2 = right.getRay2().length();
		} else {
			if (!Segment.overlaysAsRay(left.getRay2(), right.getRay1())) return STRUCTURALLY_INCOMPARABLE;

			// Opposite rays are collinear

			left1 = left.getRay1().length();
			left2 = left.getRay2().length();
			right1 = right.getRay2().length();
			right2 = right.getRay1().length();
		}

		if (MathUtilities.doubleGEQ(left1, right1) && MathUtilities.doubleGEQ(left2, right2)) return 1;
		if (MathUtilities.doubleLEQ(left1, right1) && MathUtilities.doubleLEQ(left2, right2)) return -1;

		return 0;
	}
}

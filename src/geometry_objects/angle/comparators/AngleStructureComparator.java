/**
 * Write a succinct, meaningful description of the class here. You should avoid wordiness    
 * and redundancy. If necessary, additional paragraphs should be preceded by <p>,
 * the html tag for a new paragraph.
 *
 * @author Brycen Addison, Hanna King
 * @date   12/6/2022
 */

package geometry_objects.angle.comparators;

import java.util.Comparator;

import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import utilities.math.MathUtilities;

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
	 *            1 indicates left is greater
	 *            -1 indicates left is less than
	 */
	@Override
	public int compare(Angle left, Angle right)
	{
		// null
		if (left == null || right == null)
		{
			return STRUCTURALLY_INCOMPARABLE;
		}

		// same points
        if (left.equals(right)) return 0;

		// rays do not overlap at a vertex
		if (!left.getVertex().equals(right.getVertex())) return STRUCTURALLY_INCOMPARABLE;

		// create variables to hold ray lengths
		double left1, left2, right1, right2;

		// if statement because we need to consider ABC matches or CBA matches
		// if/else needed to consider both normal and flipped
		if (!Segment.overlaysAsRay(left.getRay1(), right.getRay2()))
		{
			// first rays do not share a point
			if (!Segment.overlaysAsRay(left.getRay1(), right.getRay1())) return STRUCTURALLY_INCOMPARABLE;
			// second rays do not share a point
			if (!Segment.overlaysAsRay(left.getRay2(), right.getRay2())) return STRUCTURALLY_INCOMPARABLE;

			// Same rays are collinear
			left1 = left.getRay1().length();
			left2 = left.getRay2().length();
			right1 = right.getRay1().length();
			right2 = right.getRay2().length();
		}
		else
		{
			// flipped angles do not share a point
			if (!Segment.overlaysAsRay(left.getRay2(), right.getRay1())) return STRUCTURALLY_INCOMPARABLE;

			// opposite rays are collinear
			left1 = left.getRay1().length();
			left2 = left.getRay2().length();
			right1 = right.getRay2().length();
			right2 = right.getRay1().length();
		}

		// both rays in left angle are greater than or equal in length to the corresponding rays in the right angle
		if (MathUtilities.doubleGEQ(left1, right1) && MathUtilities.doubleGEQ(left2, right2)) return 1;

		// both rays in left angle are shorter than the corresponding rays in the right angle
		if (MathUtilities.doubleLEQ(left1, right1) && MathUtilities.doubleLEQ(left2, right2)) return -1;

		// cannot clearly state that one angle is structurally ‘smaller’ or larger’
		// aka one ray in the left angle is larger than its corresponding ray in the right angle while
		// the left ray is smaller than its corresponding ray in the right angle or vice versa
		return 0;
	}
}

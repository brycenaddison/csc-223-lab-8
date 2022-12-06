package preprocessor.delegates;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

public class ImplicitPointPreprocessor
{
	/**
	 * It is possible that some of the defined segments intersect at points
	 * that are not named; we need to capture those points and name them.
	 * 
	 * if you pass in either param as null, a new, empty one will be created and used in the method
	 * 
	 * Algorithm:
	 * For every Segment in givenSegments, look at every remaining Segment in givenSegment and
	 * get the Point where the two segments intersect. If it exists (not null) and not yet in givenPoints,
	 * add the point to givenPoints and add the point to implicitPoints
	 * 
	 * @param givenPoints PointDatabase
	 * @param givenSegments List of Segments
	 * @return set of implicit points
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		if (givenPoints == null) givenPoints = new PointDatabase();
		if (givenSegments == null) givenSegments = new ArrayList<Segment>();
		
		Set<Point> implicitPoints = new LinkedHashSet<Point>();

        for (int i = 0; i < givenSegments.size() - 1; i++)
		{
			for (int j = i + 1; j < givenSegments.size(); j++)
			{
				Point intersection = givenSegments.get(i).segmentIntersection(givenSegments.get(j));

				if (intersection != null && givenPoints.getPoint(intersection) == null)
				{
					givenPoints.put(intersection.getX(), intersection.getY());
					implicitPoints.add(givenPoints.getPoint(intersection));
					System.out.println("Added " + givenPoints.getPoint(intersection));
				}
			}
		}
		System.out.println(implicitPoints);

		return implicitPoints;
	}
}

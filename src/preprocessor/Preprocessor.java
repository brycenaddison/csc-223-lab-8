package preprocessor;

import java.util.*;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import preprocessor.delegates.ImplicitPointPreprocessor;
import geometry_objects.Segment;

public class Preprocessor
{
	// The explicit points provided to us by the user.
	// This database will also be modified to include the implicit
	// points (i.e., all points in the figure).
	protected PointDatabase _pointDatabase;

	// Minimal ('Base') segments provided by the user
	protected Set<Segment> _givenSegments;

	// The set of implicitly defined points caused by segments
	// at implicit points.
	protected Set<Point> _implicitPoints;

	// The set of implicitly defined segments resulting from implicit points.
	protected Set<Segment> _implicitSegments;

	// Given all explicit and implicit points, we have a set of
	// segments that contain no other subsegments; these are minimal ('base') segments
	// That is, minimal segments uniquely define the figure.
	protected Set<Segment> _allMinimalSegments;

	// A collection of non-basic segments
	protected Set<Segment> _nonMinimalSegments;

	// A collection of all possible segments: maximal, minimal, and everything in between
	// For lookup capability, we use a map; each <key, value> has the same segment object
	// That is, key == value. 
	protected Map<Segment, Segment> _segmentDatabase;
	public Map<Segment, Segment> getAllSegments() { return _segmentDatabase; }

	public Preprocessor(PointDatabase points, Set<Segment> segments)
	{
		_pointDatabase  = points;
		_givenSegments = segments;
		
		_segmentDatabase = new HashMap<Segment, Segment>();
		
		analyze();
	}

	/**
	 * Invoke the precomputation procedure.
	 */
	public void analyze()
	{
		//
		// Implicit Points
		//
		_implicitPoints = ImplicitPointPreprocessor.compute(_pointDatabase, _givenSegments.stream().toList());

		//
		// Implicit Segments attributed to implicit points
		//
		_implicitSegments = computeImplicitBaseSegments(_implicitPoints);

		//
		// Combine the given minimal segments and implicit segments into a true set of minimal segments
		//     *givenSegments may not be minimal
		//     *implicitSegment
		//
		_allMinimalSegments = identifyAllMinimalSegments(_implicitPoints, _givenSegments, _implicitSegments);
		//
		// Construct all segments inductively from the base segments
		//
		_nonMinimalSegments = constructAllNonMinimalSegments(_allMinimalSegments);
		//
		// Combine minimal and non-minimal into one package: our database
		//
		_allMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
		_nonMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
	}

	/**
	 * Returns a set of all segments that can be created from the given base segments
	 * @param allMinimalSegments a list of base segments
	 * @return the list of all segments that can be created from the base segments
	 */
	public Set<Segment> constructAllNonMinimalSegments(Set<Segment> allMinimalSegments)
	{
		Set<Segment> nonMinimalSegments = new HashSet<>();

		List<Segment> segmentList = new ArrayList<>(allMinimalSegments);
		List<int[]> combinations = new ArrayList<>();

		// collect every combination of segments
		for (int i = 0; i < segmentList.size() - 1; i++)
		{
			for (int j = i + 1; j < segmentList.size(); j++)
			{
				combinations.add(new int[]{i, j});
			}
		}

		// For every combination
		for (int i = 0; i < combinations.size(); i++) {
			// Fetch the segments
			Segment s1 = segmentList.get(combinations.get(i)[0]);
			Segment s2 = segmentList.get(combinations.get(i)[1]);
			// and find if one "extends" the other, i.e. are connected and parallel
			// and return the combined segment
			Segment superSegment = extend(s1, s2);

			// If a combined segment was found and was not already in the segment list
			if (superSegment != null && !segmentList.contains(superSegment))
			{
				// Add it to the list of non minimal segments
				nonMinimalSegments.add(superSegment);
				// And add to the list of combinations to check all combinations
				// with the new combined segment
				addCombinations(combinations, segmentList.size());
				segmentList.add(superSegment);
			}
		}

		return nonMinimalSegments;
	}

	/**
	 * Add every combination for a new index to a list
	 * @param combinations List of int lists
	 * @param index exclusive ending index
	 */
	private void addCombinations(List<int[]> combinations, int index)
	{
		for (int i = 0; i < index; i++)
		{
			combinations.add(new int[]{i, index});
		}
	}

	/**
	 * 	If Segment s1 and s2 are collinear and connected, return the combination
	 * 	of them. Otherwise, return null.
 	 */
	private Segment extend(Segment s1, Segment s2)
	{
		Point v = s1.sharedVertex(s2);
		if (v == null || !s1.isCollinearWith(s2)) return null;

		int[] pointIndexes = new int[2];

		Point[] allPoints = new Point[]{
				s1.getPoint1(),
				s1.getPoint2(),
				s2.getPoint1(),
				s2.getPoint2()
		};

		int slot = 0;

		for (int index = 0; index < allPoints.length; index++) {
			if (!allPoints[index].equals(v)) {
				pointIndexes[slot] = index;
				slot = 1;
			}
		}

		Point p1 = allPoints[pointIndexes[0]];
		Point p2 = allPoints[pointIndexes[1]];

		return new Segment(p1, p2);
	}

	/**
	 * Return a set of all base segments from given segments.
	 * @param implicitPoints Set of implicit points to use to break apart segments
	 * @param givenSegments Set of segments to get base segments from
	 * @param implicitSegments Not used, may pass null
	 * @return Set of all base segments
	 */
	public Set<Segment> identifyAllMinimalSegments(Set<Point> implicitPoints, Set<Segment> givenSegments, Set<Segment> implicitSegments)
	{
		Set<Segment> allBaseSegments = new HashSet<>();

		// Create a TreeSet of every point (implicit and given)
		Set<Point> allPoints = new TreeSet<>(implicitPoints);
		allPoints.addAll(getPointsFromSegments(givenSegments));

		// If possible, break down every segment into base segments and add them to the set.
		for (Segment segment: givenSegments)
		{
			Set<Segment> baseSegments = getBaseSegmentsFromSegment(segment, allPoints);
			allBaseSegments.addAll(baseSegments);
		}

		return allBaseSegments;
	}

	/**
	 * Return set of all points found in a given set of segments
	 */
	private Set<Point> getPointsFromSegments(Set<Segment> segments)
	{
		Set<Point> points = new HashSet<>();

		for (Segment segment: segments) {
			points.add(segment.getPoint1());
			points.add(segment.getPoint2());
		}

		return points;
	}

	/**
	 * Given a list of all points, break apart a segment into base segments if points lie on
	 * the given segment.
	 * @param segment to break apart
	 * @param points to break apart with
	 * @return a list of all base segments
	 */
	private Set<Segment> getBaseSegmentsFromSegment(Segment segment, Set<Point> points)
	{
		Set<Segment> baseSegments = new HashSet<>();
		TreeSet<Point> linearPoints = new TreeSet<>();

		// get points on segment out of points
		// will be linear (no overlap)
		for (Point point: points)
		{
			if (segment.pointLiesOn(point)) { linearPoints.add(point); }
		}

		// rebuild base segments from linear points
		while (linearPoints.size() > 1)
		{
			// returns and removes
			Point p1 = linearPoints.pollFirst();
			// only returns
			Point p2 = linearPoints.first();
			baseSegments.add(new Segment(p1, p2));
		}

		return baseSegments;
	}

	/**
	 * finds all minimal segments with an implicit point at an endpoint
	 * @param implicitPoints that must be contained in a returned segment
	 * @return a base segment with at least one implicit point as an endpoint
	 */
	public Set<Segment> computeImplicitBaseSegments(Set<Point> implicitPoints) {
		Set<Segment> allBaseSegments = identifyAllMinimalSegments(implicitPoints, _givenSegments, null);

		return onlyContaining(allBaseSegments, implicitPoints);
	}

	/**
	 * @param segments Collection of Segments
	 * @param points Set of Points
	 * @return Set of Segments which contain any Point of points as an endpoint
	 */
	private Set<Segment> onlyContaining(Collection<Segment> segments, Set<Point> points)
	{
		Set<Segment> containing = new HashSet<>();

		for (Segment segment: segments)
		{
			if (segmentHasAnyOf(segment, points)) containing.add(segment);
		}

		return containing;
	}

	/**
	 * @param segment Segment
	 * @param points set of Points
	 * @return true if any Point in points is an endpoint of segment, false otherwise
	 */
	private boolean segmentHasAnyOf(Segment segment, Set<Point> points)
	{
		for (Point point: points)
		{	
			if (segment.has(point)) return true;
		}
		return false;
	}
}

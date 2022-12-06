package preprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.InputFacade;
import preprocessor.delegates.ImplicitPointPreprocessor;

class PreprocessorTest
{
	/*
	 * star
	 * crossed box
	 * no crossing (simple triangle)                      
	 */
	@Test
	void test_implicit_crossings()
	{
		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(
				"jsonfiles.fully_connected_irregular_polygon.json"
		);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		// 5 new implied points inside the pentagon
		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(5, iPoints.size());


		//
		//
		//		               D(3, 7)
		//
		//
		//   E(-2,4)       D*      E*
		//		         C*          A*       C(6, 3)
		//                      B*
		//		       A(2,0)        B(4, 0)
		//
		//		    An irregular pentagon with 5 C 2 = 10 segments

		Point a_star = new Point(56.0 / 15, 28.0 / 15);
		Point b_star = new Point(16.0 / 7, 8.0 / 7);
		Point c_star = new Point(8.0 / 9, 56.0 / 27);
		Point d_star = new Point(90.0 / 59, 210.0 / 59);
		Point e_star = new Point(194.0 / 55, 182.0 / 55);

		assertTrue(iPoints.contains(a_star));
		assertTrue(iPoints.contains(b_star));
		assertTrue(iPoints.contains(c_star));
		assertTrue(iPoints.contains(d_star));
		assertTrue(iPoints.contains(e_star));

		//
		// There are 15 implied segments inside the pentagon; see figure above
		//
		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(15, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("A"), c_star));
		expectedISegments.add(new Segment(points.getPoint("A"), b_star));

		expectedISegments.add(new Segment(points.getPoint("B"), b_star));
		expectedISegments.add(new Segment(points.getPoint("B"), a_star));

		expectedISegments.add(new Segment(points.getPoint("C"), a_star));
		expectedISegments.add(new Segment(points.getPoint("C"), e_star));

		expectedISegments.add(new Segment(points.getPoint("D"), d_star));
		expectedISegments.add(new Segment(points.getPoint("D"), e_star));

		expectedISegments.add(new Segment(points.getPoint("E"), c_star));
		expectedISegments.add(new Segment(points.getPoint("E"), d_star));

		expectedISegments.add(new Segment(c_star, b_star));
		expectedISegments.add(new Segment(b_star, a_star));
		expectedISegments.add(new Segment(a_star, e_star));
		expectedISegments.add(new Segment(e_star, d_star));
		expectedISegments.add(new Segment(d_star, c_star));

		for (Segment iSegment : iSegments)
		{
			assertTrue(expectedISegments.contains(iSegment));
		}

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));
		expectedMinimalSegments.add(new Segment(points.getPoint("E"), points.getPoint("A")));
		
		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}
		
		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);
		
		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		assertEquals(15, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("D")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("E")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), e_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));		

		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), a_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), e_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), a_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
		
		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

	@Test
	void test_implicit_crossed_square()
	{
		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(
				"jsonfiles.crossed_square.json"
		);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		// 1 new implied points inside the pentagon
		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(1, iPoints.size());


		// D            C
		//
		//       A* (implicit)
		//
		// A            B

		Point a_star = new Point(2, 2);

		assertTrue(iPoints.contains(a_star));

		//
		// There are 4 implied segments inside the pentagon; see figure above
		//
		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(4, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("A"), a_star));
		expectedISegments.add(new Segment(points.getPoint("B"), a_star));
		expectedISegments.add(new Segment(points.getPoint("C"), a_star));
		expectedISegments.add(new Segment(points.getPoint("D"), a_star));

		for (Segment iSegment : iSegments)
		{
			assertTrue(iSegments.contains(iSegment));
		}

		//
		// Ensure we have ALL minimal segments: 4 in this figure.
		//
		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("A")));
		
		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}
		
		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);
		
		//
		// All Segments will consist of the new 2 non-minimal segments.
		//
		assertEquals(2, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 2 in this figure.
		//
		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
		
		//
		// Check size and content equality
		//
		//assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

	@Test
	void test_no_implicit()
	{
		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(
				"jsonfiles.simple_triangle.json"
		);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		// 0 new implied points inside the triangle
		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(0, iPoints.size());


		// B
		//
		//       
		//
		// A            C

		//
		// There are 0 implied segments inside the pentagon; see figure above
		//
		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
		assertEquals(0, iSegments.size());

		//
		// Ensure we have ALL minimal segments: 3 in this figure.
		//
		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("A")));
		
		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}
		
		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);
		
		//
		// All Segments will consist of the new 0 non-minimal segments.
		//
		assertEquals(0, computedNonMinimalSegments.size());

		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		
		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

	/*
	 * +constructAllNonMinimalSegments
	 * -addCombinations
	 * -extend
	 * +identifyAllMinimalSegments
	 * -getPointsFromSegments
	 * -getBaseSegmentsFromSegments
	 * +computeImplicitBaseSegments
	 * -onlyContaining
	 * -segmentHasAnyOf
	 */

	/*
	 * index is 0
	 * normal
	 */
	@Test
	void testAddCombinations_zeroindex()
	{
		ArrayList<int[]> listExpected = new ArrayList<int[]>();

		ArrayList<int[]> list = new ArrayList<int[]>();
		addCombinations(list, 0);

		assertEquals(listExpected, list);
	} @Test
	void testAddCombinations_normal()
	{
		ArrayList<int[]> listExpected = new ArrayList<int[]>();
		listExpected.add(new int[] {0, 4});
		listExpected.add(new int[] {1, 4});
		listExpected.add(new int[] {2, 4});
		listExpected.add(new int[] {3, 4});

		ArrayList<int[]> list = new ArrayList<int[]>();
		addCombinations(list, 4);

		assertEquals(listExpected.size(), list.size());
		for (int i = 0; i < list.size(); i++)
		{
			assertEquals(listExpected.get(i)[0], list.get(i)[0]);
			assertEquals(listExpected.get(i)[1], list.get(i)[1]);
		}
	}

	/*
	 * no shared vertex
	 * not collinear
	 * collinear and shared vertex
	 */
	@Test
	void testExtend_noshared()
	{
		Segment s1 = new Segment(new Point("A", 0, 1), new Point("B", 0, 2));
		Segment s2 = new Segment (new Point("C", 0, 3), new Point("D", 0, 5));
		assertEquals(null, extend(s1, s2));
	}
	@Test
	void testExtend_notcollinear()
	{
		Segment s1 = new Segment(new Point("A", 0, 1), new Point("B", 0, 2));
		Segment s2 = new Segment (new Point("C", 0, 3), new Point("D", 4, 5));
		assertEquals(null, extend(s1, s2));
	}
	@Test
	void testExtend_good()
	{
		Segment s1 = new Segment(new Point("A", 0, 1), new Point("B", 0, 2));
		Segment s2 = new Segment (new Point("C", 0, 2), new Point("D", 0, 5));
		assertEquals(new Segment(new Point("A", 0, 1), new Point("D", 0, 5)), extend(s1, s2));
	}

	// /*
	//  * contains only minimal segments
	//  * shape contains minimal and nonminimal segments
	//  */
	// @Test
	// void testConstructAllNonMinimalSegments_triangle()
	// {
	// 	Point p1 = new Point("A", 0, 0);
	// 	Point p2 = new Point("B", 2, 2);
	// 	Point p3 = new Point("C", 0, 4);
	// 	Segment s1 = new Segment(p1, p2);
	// 	Segment s2 = new Segment(p2, p3);
	// 	Segment s3 = new Segment(p3, p1);

	// 	HashSet<Segment> segments = new HashSet<Segment>();
	// 	segments.add(s1);
	// 	segments.add(s2);
	// 	segments.add(s3);

		
	// }
	// @Test
	// void testConstructAllNonMinimalSegments_crossedbox()
	// {
	// 	Point p1 = new Point("A", 0, 0);
	// 	Point p2 = new Point("B", 0, 4);
	// 	Point p3 = new Point("C", 4, 4);
	// 	Point p4 = new Point("D", 4, 0);
	// 	Segment s1 = new Segment(p1, p2);
	// 	Segment s2 = new Segment(p2, p3);
	// 	Segment s3 = new Segment(p3, p4);
	// 	Segment s4 = new Segment(p1, p4);
	// 	Segment s5 = new Segment(p3, p1);
	// 	Segment s6 = new Segment(p2, p4);

	// 	HashSet<Segment> segments = new HashSet<Segment>();
	// 	segments.add(s1);
	// 	segments.add(s2);
	// 	segments.add(s3);
	// 	segments.add(s4);
	// 	segments.add(s5);
	// 	segments.add(s6);

	// 	HashSet<Segment> expected = new HashSet<Segment>();
	// 	expected.add(s1);
	// 	expected.add(s2);
	// 	expected.add(s3);
	// 	expected.add(s4);

	// 	assertEquals(expected, constructAllNonMinimalSegments(segments));
	// }

	/*
	 * empty
	 * one segment
	 * many segments
	 * segments with shared point(s)
	 */
	@Test
	void testGetPointsFromSegments_empty()
	{
		assertEquals(new HashSet<Point>(), getPointsFromSegments(new HashSet<Segment>()));
	}
	@Test
	void testGetPointsFromSegments_one()
	{
		Point p1 = new Point("A", 3, 4);
		Point p2 = new Point("B", 2, 6);
		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(new Segment(p1, p2));

		HashSet<Point> expected = new HashSet<Point>();
		expected.add(p1);
		expected.add(p2);

		assertEquals(2, getPointsFromSegments(segments).size());
		assertEquals(expected, getPointsFromSegments(segments));
	}
	@Test
	void testGetPointsFromSegments_many()
	{
		Point p1 = new Point("A", 3, 4);
		Point p2 = new Point("B", 2, 6);
		Point p3 = new Point("A", 1, 3);
		Point p4 = new Point("B", 5, 7);
		Point p5 = new Point("A", 7, 1);
		Point p6 = new Point("B", 0, 2);
		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(new Segment(p1, p2));
		segments.add(new Segment(p3, p4));
		segments.add(new Segment(p5, p6));

		HashSet<Point> expected = new HashSet<Point>();
		expected.add(p1);
		expected.add(p2);
		expected.add(p3);
		expected.add(p4);
		expected.add(p5);
		expected.add(p6);

		assertEquals(6, getPointsFromSegments(segments).size());
		assertEquals(expected, getPointsFromSegments(segments));
	}
	@Test
	void testGetPointsFromSegments_shared()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 2, 6);
		Point p3 = new Point("A", 1, 3);
		Point p4 = new Point("B", 5, 7);
		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(new Segment(p1, p2));
		segments.add(new Segment(p1, p3));
		segments.add(new Segment(p1, p4));

		HashSet<Point> expected = new HashSet<Point>();
		expected.add(p1);
		expected.add(p2);
		expected.add(p3);
		expected.add(p4);

		assertEquals(4, getPointsFromSegments(segments).size());
		assertEquals(expected, getPointsFromSegments(segments));
	}

	/*
	 * base segment in matching the points in
	 * extra points in
	 * more than one segment
	 */
	@Test
	void testGetBaseSegmentsFromSegments_basein()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 1, 1);
		Segment seg = new Segment(p1, p2);

		TreeSet<Point> pointSet = new TreeSet<Point>();
		pointSet.add(p1);
		pointSet.add(p2);

		HashSet<Segment> expected = new HashSet<Segment>();
		expected.add(seg);

		assertEquals(expected, getBaseSegmentsFromSegment(seg, pointSet));
	}
	@Test
	void testGetBaseSegmentsFromSegments_extrapoints()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 1, 1);
		Point p3 = new Point("C", 1, 2);
		Point p4 = new Point("D", 7, 8);
		Segment seg = new Segment(p1, p2);

		TreeSet<Point> pointSet = new TreeSet<Point>();
		pointSet.add(p1);
		pointSet.add(p2);
		pointSet.add(p3);
		pointSet.add(p4);

		HashSet<Segment> expected = new HashSet<Segment>();
		expected.add(seg);

		assertEquals(expected, getBaseSegmentsFromSegment(seg, pointSet));
	}
	@Test
	void testGetBaseSegmentsFromSegments_multiplesegments()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 1, 1);
		Point p3 = new Point("C", 2, 2);
		Point p4 = new Point("D", 8, 8);
		Segment seg1 = new Segment(p1, p4);
		Segment seg2 = new Segment(p1, p2);
		Segment seg3 = new Segment(p2, p3);
		Segment seg4 = new Segment(p3, p4);

		TreeSet<Point> pointSet = new TreeSet<Point>();
		pointSet.add(p1);
		pointSet.add(p2);
		pointSet.add(p3);
		pointSet.add(p4);

		HashSet<Segment> expected = new HashSet<Segment>();
		expected.add(seg2);
		expected.add(seg3);
		expected.add(seg4);

		assertEquals(expected, getBaseSegmentsFromSegment(seg1, pointSet));
	}

	// /*
	//  * implicitSegments parameter not used in method, so will be passing null in every test
	//  * 
	//  * no implicit points in shape
	//  * null implicitPoints
	//  * null givenSegments
	//  * no given segments
	//  * crossed box (normal with implicit)
	//  * triangle (normal without implicit)
	//  */
	// @Test
	// void testIdentifyAllMinimalSegments_noimplicitpoints()
	// {

	// }
	// @Test
	// void testIdentifyAllMinimalSegments_nullimplicitpoints()
	// {

	// }
	// @Test
	// void testIdentifyAllMinimalSegments_nullgivensegs()
	// {

	// }
	// @Test
	// void testIdentifyAllMinimalSegments_nogivensegs()
	// {

	// }
	// @Test
	// void testIdentifyAllMinimalSegments_crossedbox()
	// {

	// }
	// @Test
	// void testIdentifyAllMinimalSegments_triangle()
	// {

	// }

	/*
	 * empty segments
	 * empty points
	 * no overlap between segments and points
	 * some overlap (normal)
	 * full overlap
	 */
	@Test
	void testOnlyContaining_emptysegments()
	{
		HashSet<Point> points = new HashSet<Point>();
		points.add(new Point("A", 0, 0));

		HashSet<Segment> expected = new HashSet<Segment>();

		assertEquals(expected, onlyContaining(new HashSet<Segment>(), points));
	}
	@Test
	void testOnlyContaining_emptypoints()
	{
		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(new Segment(new Point("A", 0, 0), new Point("B", 1, 1)));

		HashSet<Segment> expected = new HashSet<Segment>();

		assertEquals(expected, onlyContaining(segments, new HashSet<Point>()));
	}
	@Test
	void testOnlyContaining_nooverlap()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 1, 1);
		Point p3 = new Point("C", 2, 2);
		Point p4 = new Point("D", 8, 8);
		Point p5 = new Point("E", 5, 8);
		Segment seg1 = new Segment(p1, p2);
		Segment seg2 = new Segment(p1, p3);

		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(seg1);
		segments.add(seg2);
		HashSet<Point> points = new HashSet<Point>();
		points.add(p4);
		points.add(p5);

		HashSet<Segment> expected = new HashSet<Segment>();

		assertEquals(expected, onlyContaining(segments, points));	
	}
	@Test
	void testOnlyContaining_normal()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 1, 1);
		Point p3 = new Point("C", 2, 2);
		Point p4 = new Point("D", 8, 8);
		Point p5 = new Point("E", 5, 8);
		Segment seg1 = new Segment(p1, p2);
		Segment seg2 = new Segment(p1, p3);

		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(seg1);
		segments.add(seg2);
		HashSet<Point> points = new HashSet<Point>();
		points.add(p2);
		points.add(p4);
		points.add(p5);

		HashSet<Segment> expected = new HashSet<Segment>();
		expected.add(seg1);

		assertEquals(expected, onlyContaining(segments, points));
	}
	@Test
	void testOnlyContaining_allsegmentscontain()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 1, 1);
		Point p3 = new Point("C", 2, 2);
		Segment seg1 = new Segment(p1, p2);
		Segment seg2 = new Segment(p1, p3);

		HashSet<Segment> segments = new HashSet<Segment>();
		segments.add(seg1);
		segments.add(seg2);
		HashSet<Point> points = new HashSet<Point>();
		points.add(p1);
		points.add(p2);
		points.add(p3);

		assertEquals(segments, onlyContaining(segments, points));
	}

	/*
	 * points contains an endpoint
	 * points does not contain an endpoint
	 * points empty
	 */
	@Test
	void testSegmentHasAnyOf_endpointin()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 2, 6);
		Point p3 = new Point("A", 1, 3);
		Point p4 = new Point("B", 5, 7);
		Point p5 = new Point("A", 7, 1);
		Point p6 = new Point("B", 0, 2);
		Segment seg = new Segment(p5, p6);

		TreeSet<Point> points = new TreeSet<Point>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);

		assertTrue(segmentHasAnyOf(seg, points));
	}
	@Test
	void testSegmentHasAnyOf_endpointnotin()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 2, 6);
		Point p3 = new Point("A", 1, 3);
		Point p4 = new Point("B", 5, 7);
		Point p5 = new Point("A", 7, 1);
		Point p6 = new Point("B", 0, 2);
		Segment seg = new Segment(p5, p6);

		TreeSet<Point> points = new TreeSet<Point>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);

		assertFalse(segmentHasAnyOf(seg, points));
	}
	@Test
	void testSegmentHasAnyOf_empty()
	{
		Point p1 = new Point("A", 0, 0);
		Point p2 = new Point("B", 2, 6);
		Segment seg = new Segment(p1, p2);

		TreeSet<Point> points = new TreeSet<Point>();

		assertFalse(segmentHasAnyOf(seg, points));
	}

	/*
	 * built in (uses instance variables, and the constructor calls analyze which calls this) 
	 */
	// @Test
	// void testComputeImplicitBaseSegments()
	// {

	// }












	/**
	 * get the index combinations for one index
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
	 * 
	 * 
	 * @param s1
	 * @param s2
	 * @return
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
	 * collect all endpoints of Segments in segments into a set
	 * @param segments
	 * @return
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
	 * 
	 * @param segment
	 * @param points
	 * @return
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
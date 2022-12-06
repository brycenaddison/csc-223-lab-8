package preprocessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.Triangle;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.components.FigureNode;
import input.InputFacade;

class TriangleIdentifierTest
{
	protected PointDatabase _points;
	protected Preprocessor _pp;
	protected Map<Segment, Segment> _segments;
	
	protected void init(String filename)
	{
		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(filename);

		_points = pair.getKey();

		_pp = new Preprocessor(_points, pair.getValue());

		_pp.analyze();

		_segments = _pp.getAllSegments();
	}
	
	//      A                                 
	//     / \                                
	//    B___C                               
	//   / \ / \                              
	//  /   X   \  X is not a specified point (it is implied) 
	// D_/_____\_E
	//
	// This figure contains 12 triangles
	//
	@Test
	void test_crossing_symmetric_triangle()
	{
		init("jsonfiles/crossing_symmetric_triangle.json");

		TriangleIdentifier triIdentifier = new TriangleIdentifier(_segments);

		Set<Triangle> computedTriangles = triIdentifier.getTriangles();

		assertEquals(12, computedTriangles.size());

		//
		// ALL original segments: 8 in this figure.
		//
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));
		Segment ac = new Segment(_points.getPoint("A"), _points.getPoint("C"));
		Segment bc = new Segment(_points.getPoint("B"), _points.getPoint("C"));

		Segment bd = new Segment(_points.getPoint("B"), _points.getPoint("D"));
		Segment ce = new Segment(_points.getPoint("C"), _points.getPoint("E"));
		Segment de = new Segment(_points.getPoint("D"), _points.getPoint("E"));

		Segment be = new Segment(_points.getPoint("B"), _points.getPoint("E"));
		Segment cd = new Segment(_points.getPoint("C"), _points.getPoint("D"));

		//
		// Implied minimal segments: 4 in this figure.
		//
		Point a_star = _points.getPoint(3,3);

		Segment a_star_b = new Segment(a_star, _points.getPoint("B"));
		Segment a_star_c = new Segment(a_star, _points.getPoint("C"));
		Segment a_star_d = new Segment(a_star, _points.getPoint("D"));
		Segment a_star_e = new Segment(a_star, _points.getPoint("E"));

		//
		// Non-minimal, computed segments: 2 in this figure.
		//
		Segment ad = new Segment(_points.getPoint("A"), _points.getPoint("D"));
		Segment ae = new Segment(_points.getPoint("A"), _points.getPoint("E"));

		//
		// Triangles we expect to find
		//
		List<Triangle> expectedTriangles = new ArrayList<Triangle>();
		try {
			expectedTriangles.add(new Triangle(Arrays.asList(ab, bc, ac)));
			expectedTriangles.add(new Triangle(Arrays.asList(bd, a_star_d, a_star_b)));
			expectedTriangles.add(new Triangle(Arrays.asList(bc, a_star_b, a_star_c)));
			expectedTriangles.add(new Triangle(Arrays.asList(ce, a_star_c, a_star_e)));
			expectedTriangles.add(new Triangle(Arrays.asList(de, a_star_d, a_star_e)));

			expectedTriangles.add(new Triangle(Arrays.asList(bd, cd, bc)));
			expectedTriangles.add(new Triangle(Arrays.asList(ce, be, bc)));

			expectedTriangles.add(new Triangle(Arrays.asList(bd, be, de)));
			expectedTriangles.add(new Triangle(Arrays.asList(ce, cd, de)));

			expectedTriangles.add(new Triangle(Arrays.asList(ab, be, ae)));
			expectedTriangles.add(new Triangle(Arrays.asList(ac, cd, ad)));

			expectedTriangles.add(new Triangle(Arrays.asList(ad, de, ae)));
		}
		catch (FactException te) { System.err.println("Invalid triangles in triangle test."); }

		assertEquals(expectedTriangles.size(), computedTriangles.size());
		
		for (Triangle computedTriangle : computedTriangles)
		{
			assertTrue(expectedTriangles.contains(computedTriangle));
		}
	}


	// B                                 
	// | \                                
	// |  \                               
	// |   \                              
	// |    \  
	// A_____C
	//
	// This figure contains 12 triangles
	//
	@Test
	void test_single_triangle()
	{
		init("jsonfiles/simple_triangle.json");

		TriangleIdentifier triIdentifier = new TriangleIdentifier(_segments);

		Set<Triangle> computedTriangles = triIdentifier.getTriangles();

		assertEquals(1, computedTriangles.size());

		//
		// ALL original segments: 3 in this figure.
		//
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));
		Segment ac = new Segment(_points.getPoint("A"), _points.getPoint("C"));
		Segment bc = new Segment(_points.getPoint("B"), _points.getPoint("C"));

		//
		// Implied minimal segments: 0 in this figure.
		//

		//
		// Non-minimal, computed segments: 0 in this figure.
		//

		//
		// Triangles we expect to find
		//
		List<Triangle> expectedTriangles = new ArrayList<Triangle>();
		try {
			expectedTriangles.add(new Triangle(Arrays.asList(ab, bc, ac)));
		}
		catch (FactException te) { System.err.println("Invalid triangles in triangle test."); }

		assertEquals(expectedTriangles.size(), computedTriangles.size());
		
		for (Triangle computedTriangle : computedTriangles)
		{
			assertTrue(expectedTriangles.contains(computedTriangle));
		}
	}



	// D-----------C
	// |  \     /  |
	// |     x (implicit)
	// |  /     \  |
	// A-----------B
	//
	@Test
	void test_crossed_square()
	{
		init("jsonfiles/crossed_square.json");

		TriangleIdentifier triIdentifier = new TriangleIdentifier(_segments);

		Set<Triangle> computedTriangles = triIdentifier.getTriangles();

		assertEquals(8, computedTriangles.size());

		//
		// ALL original segments: 4 in this figure.
		//
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));
		Segment ad = new Segment(_points.getPoint("A"), _points.getPoint("D"));
		Segment cb = new Segment(_points.getPoint("C"), _points.getPoint("B"));
		Segment cd = new Segment(_points.getPoint("C"), _points.getPoint("D"));

		//
		// Implied minimal segments: 4 in this figure.
		//
		Point x = _points.getPoint(2,2);

		Segment xa = new Segment(x, _points.getPoint("A"));
		Segment xb = new Segment(x, _points.getPoint("B"));
		Segment xc = new Segment(x, _points.getPoint("C"));
		Segment xd = new Segment(x, _points.getPoint("D"));

		//
		// Non-minimal, computed segments: 2 in this figure.
		//
		Segment ac = new Segment(_points.getPoint("A"), _points.getPoint("C"));
		Segment bd = new Segment(_points.getPoint("B"), _points.getPoint("D"));

		//
		// Triangles we expect to find
		//
		List<Triangle> expectedTriangles = new ArrayList<Triangle>();
		try {
			expectedTriangles.add(new Triangle(Arrays.asList(ad, xd, xa)));
			expectedTriangles.add(new Triangle(Arrays.asList(cd, xd, xc)));
			expectedTriangles.add(new Triangle(Arrays.asList(cb, xc, xb)));
			expectedTriangles.add(new Triangle(Arrays.asList(ab, xa, xb)));

			expectedTriangles.add(new Triangle(Arrays.asList(ad, bd, ab)));
			expectedTriangles.add(new Triangle(Arrays.asList(ad, cd, ac)));
			expectedTriangles.add(new Triangle(Arrays.asList(cb, ac, ab)));
			expectedTriangles.add(new Triangle(Arrays.asList(cb, bd, cd)));

		}
		catch (FactException te) { System.err.println("Invalid triangles in triangle test."); }

		assertEquals(expectedTriangles.size(), computedTriangles.size());

		for (Triangle computedTriangle : computedTriangles)
		{
			assertTrue(expectedTriangles.contains(computedTriangle));
		}
	}



	// A-------B
	//
	@Test
	void test_line_seg()
	{
		init("jsonfiles/line_seg.json");

		TriangleIdentifier triIdentifier = new TriangleIdentifier(_segments);

		Set<Triangle> computedTriangles = triIdentifier.getTriangles();

		assertEquals(0, computedTriangles.size());

		//
		// ALL original segments: 1 in this figure.
		//
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));

		//
		// Implied minimal segments: 0 in this figure.
		//

		//
		// Non-minimal, computed segments: 0 in this figure.
		//

		//
		// Triangles we expect to find
		//
		List<Triangle> expectedTriangles = new ArrayList<Triangle>();

		assertEquals(expectedTriangles.size(), computedTriangles.size());
		
		for (Triangle computedTriangle : computedTriangles)
		{
			assertTrue(expectedTriangles.contains(computedTriangle));
		}
	}




	//               D(2, 3)
	//             
	//           
	//   E(1, 2)   
	//                        C(3, 2)
	//	
	//       A(1, 0)        B(3, 0)
	//
	//    star with 5 points. they are connected like you would draw one, it just doesn't translate to character art well
	//
	// this shape has 10 triangles
	//
	@Test
	void test_star()
	{
		init("jsonfiles/star.json");

		TriangleIdentifier triIdentifier = new TriangleIdentifier(_segments);

		Set<Triangle> computedTriangles = triIdentifier.getTriangles();

		assertEquals(10, computedTriangles.size());

		//
		// ALL original segments: 5 in this figure.
		//
		Segment ac = new Segment(_points.getPoint("A"), _points.getPoint("C"));
		Segment ad = new Segment(_points.getPoint("A"), _points.getPoint("D"));

		Segment bd = new Segment(_points.getPoint("B"), _points.getPoint("D"));
		Segment be = new Segment(_points.getPoint("B"), _points.getPoint("E"));

		Segment ce = new Segment(_points.getPoint("C"), _points.getPoint("E"));

		//
		// Implied minimal segments: 15 in this figure.
		//
		Point a_star = _points.getPoint(1.5, 1.5);
		Point b_star = _points.getPoint(2, 1);
		Point c_star = _points.getPoint(2.5, 1.5);
		Point d_star = _points.getPoint((7.0/3), 2);
		Point e_star = _points.getPoint((5.0/3), 2);

		Segment a_star_a = new Segment(a_star, _points.getPoint("A"));
		Segment a_star_e = new Segment(a_star, _points.getPoint("E"));
		Segment a_star_e_star = new Segment(a_star, e_star);
		Segment a_star_b_star = new Segment(a_star, b_star);

		Segment b_star_a = new Segment(b_star, _points.getPoint("A"));
		Segment b_star_b = new Segment(b_star, _points.getPoint("B"));
		Segment b_star_c_star = new Segment(b_star, c_star);

		Segment c_star_b = new Segment(c_star, _points.getPoint("B"));
		Segment c_star_c = new Segment(c_star, _points.getPoint("C"));

		Segment c_star_d_star = new Segment(c_star, d_star);

		Segment d_star_c = new Segment(d_star, _points.getPoint("C"));
		Segment d_star_d = new Segment(d_star, _points.getPoint("D"));
		Segment d_star_e_star = new Segment(d_star, e_star);

		Segment e_star_d = new Segment(e_star, _points.getPoint("D"));
		Segment e_star_e = new Segment(e_star, _points.getPoint("E"));

		//
		// Non-minimal, computed segments: 10 in this figure.
		//

		Segment a_e_star = new Segment(_points.getPoint("A"), e_star);
		Segment a_star_d = new Segment(a_star, _points.getPoint("D"));

		Segment a_c_star = new Segment(_points.getPoint("A"), c_star);
		Segment b_star_c = new Segment(b_star, _points.getPoint("C"));

		Segment b_a_star = new Segment(_points.getPoint("B"), a_star);
		Segment b_star_e = new Segment(b_star, _points.getPoint("E"));

		Segment b_d_star = new Segment(_points.getPoint("B"), d_star);
		Segment c_star_d = new Segment(c_star, _points.getPoint("D"));

		Segment c_e_star = new Segment(_points.getPoint("C"), e_star);
		Segment d_star_e = new Segment(d_star, _points.getPoint("E"));

		//
		// Triangles we expect to find
		//
		List<Triangle> expectedTriangles = new ArrayList<Triangle>();
		try {
			expectedTriangles.add(new Triangle(Arrays.asList(a_star_a, b_star_a, a_star_b_star)));
			expectedTriangles.add(new Triangle(Arrays.asList(b_star_b, c_star_b, b_star_c_star)));
			expectedTriangles.add(new Triangle(Arrays.asList(c_star_c, d_star_c, c_star_d_star)));
			expectedTriangles.add(new Triangle(Arrays.asList(d_star_d, e_star_d, d_star_e_star)));
			expectedTriangles.add(new Triangle(Arrays.asList(a_star_e, e_star_e, a_star_e_star)));

			expectedTriangles.add(new Triangle(Arrays.asList(ad, c_star_d, a_c_star)));
			expectedTriangles.add(new Triangle(Arrays.asList(ac, a_e_star, c_e_star)));
			expectedTriangles.add(new Triangle(Arrays.asList(be, b_d_star, d_star_e)));
			expectedTriangles.add(new Triangle(Arrays.asList(bd, b_a_star, a_star_d)));
			expectedTriangles.add(new Triangle(Arrays.asList(ce, b_star_e, b_star_c)));
		}
		catch (FactException te) { System.err.println("Invalid triangles in triangle test."); }

		assertEquals(expectedTriangles.size(), computedTriangles.size());
		
		for (Triangle computedTriangle : computedTriangles)
		{
			assertTrue(expectedTriangles.contains(computedTriangle));
		}
	}
}

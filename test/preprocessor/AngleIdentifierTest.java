package preprocessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.angle.Angle;
import geometry_objects.angle.AngleEquivalenceClasses;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.components.FigureNode;
import input.InputFacade;

class AngleIdentifierTest
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
	// D_________E
	//
	// This figure contains 44 angles
	//
	@Test
	void test_crossing_symmetric_triangle()
	{
		init("jsonfiles/crossing_symmetric_triangle.json");

		AngleIdentifier angleIdentifier = new AngleIdentifier(_segments);

//		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();
		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();

		// The number of classes should equate to the number of 'minimal' angles
		assertEquals(25, computedAngles.numClasses());
		
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
		// Angles we expect to find
		//
		List<Angle> expectedAngles = new ArrayList<Angle>();
		try {
			//
			//
			// Angles broken down by equivalence class
			//
			//

			// Straight angles
			expectedAngles.add(new Angle(a_star_b, a_star_e));
			
			expectedAngles.add(new Angle(ac, ce));
			
			expectedAngles.add(new Angle(a_star_d, a_star_c));
			
			expectedAngles.add(new Angle(ab, bd));
			
			// right angles
			expectedAngles.add(new Angle(a_star_b, a_star_d));
			
			expectedAngles.add(new Angle(a_star_b, a_star_c));
			
			expectedAngles.add(new Angle(a_star_d, a_star_e));

			expectedAngles.add(new Angle(a_star_c, a_star_e));
			
			//
			//
			expectedAngles.add(new Angle(a_star_b, ab));
			expectedAngles.add(new Angle(be, ab));

			expectedAngles.add(new Angle(a_star_b, bc));
			expectedAngles.add(new Angle(be, bc));

			expectedAngles.add(new Angle(a_star_b, bd));
			expectedAngles.add(new Angle(be, bd));
			
			expectedAngles.add(new Angle(ac, a_star_c));
			expectedAngles.add(new Angle(ac, cd));

			expectedAngles.add(new Angle(bc, a_star_c));
			expectedAngles.add(new Angle(cd, bd));
			
			expectedAngles.add(new Angle(a_star_c, ce));
			expectedAngles.add(new Angle(cd, ce));
			
			expectedAngles.add(new Angle(a_star_d, de));
			expectedAngles.add(new Angle(cd, de));
			
			expectedAngles.add(new Angle(bd, de));
			expectedAngles.add(new Angle(ad, de));
			
			expectedAngles.add(new Angle(a_star_e, de));
			expectedAngles.add(new Angle(be, de));

			expectedAngles.add(new Angle(ce, de));
			expectedAngles.add(new Angle(ae, de));
			
			// Larger equivalence classes
			//
			expectedAngles.add(new Angle(ac, ab));
			expectedAngles.add(new Angle(ab, ae));
			expectedAngles.add(new Angle(ad, ae));
			expectedAngles.add(new Angle(ac, ad));

			expectedAngles.add(new Angle(a_star_d, bd));
			expectedAngles.add(new Angle(cd, bd));
			expectedAngles.add(new Angle(ad, a_star_d));
			expectedAngles.add(new Angle(cd, ad));

			expectedAngles.add(new Angle(a_star_e, ce));
			expectedAngles.add(new Angle(be, ae));
			expectedAngles.add(new Angle(be, ce));
			expectedAngles.add(new Angle(a_star_e, ce));

			
			// More singletons
			//
			expectedAngles.add(new Angle(ac, bc));

			expectedAngles.add(new Angle(ab, bc));
			
			expectedAngles.add(new Angle(bc, bd));

			expectedAngles.add(new Angle(bc, ce));			
		}
		catch (FactException te) { System.err.println("Invalid Angles in Angle test."); }

		assertEquals(expectedAngles.size(), computedAngles.size());
		
		//
		// Equality
		//
		for (Angle expected : expectedAngles)
		{
			assertTrue(computedAngles.contains(expected));
		}
	}



	// B                                 
	// | \                                
	// |  \                               
	// |   \                              
	// |    \  
	// A_____C
	//
	// This figure contains 3 angles
	//
	@Test
	void test_single_triangle()
	{
		init("jsonfiles/simple_triangle.json");

		AngleIdentifier angleIdentifier = new AngleIdentifier(_segments);

		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();

		// The number of classes should equate to the number of 'minimal' angles
		assertEquals(3, computedAngles.numClasses());

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
		// Angles we expect to find
		//
		List<Angle> expectedAngles = new ArrayList<Angle>();
		try {
			//
			//
			// Angles broken down by equivalence class
			//
			//

			// Straight angles
			
			// right angles
			expectedAngles.add(new Angle(ab, ac));
			
			//
			//
			expectedAngles.add(new Angle(ab, bc));
			expectedAngles.add(new Angle(ac, bc));		
		}
		catch (FactException te) { System.err.println("Invalid Angles in Angle test."); }

		assertEquals(expectedAngles.size(), computedAngles.size());
		
		//
		// Equality
		//
		for (Angle expected : expectedAngles)
		{
			assertTrue(computedAngles.contains(expected));
		}
	}



	// D-----------C
	// |  \     /  |
	// |     x (implicit)
	// |  /     \  |
	// A-----------B
	//
	// this figure contains 22 angles
	@Test
	void test_crossed_square()
	{
		init("jsonfiles/crossed_square.json");

		AngleIdentifier angleIdentifier = new AngleIdentifier(_segments);

		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();

		// The number of classes should equate to the number of 'minimal' angles
		assertEquals(18, computedAngles.numClasses());
		
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
		// Angles we expect to find
		//
		List<Angle> expectedAngles = new ArrayList<Angle>();
		try {
			//
			//
			// Angles broken down by equivalence class
			//
			//

			// Straight angles
			expectedAngles.add(new Angle(xa, xc));
			expectedAngles.add(new Angle(xd, xb));
			
			// right angles
			expectedAngles.add(new Angle(ad, ab));
			expectedAngles.add(new Angle(ab, cb));
			expectedAngles.add(new Angle(cb, cd));
			expectedAngles.add(new Angle(ad, cd));
			
			//
			//
			

			// Larger equivalence classes
			//
			expectedAngles.add(new Angle(ad, ac));
			expectedAngles.add(new Angle(ad, xa));

			expectedAngles.add(new Angle(bd, ab));
			expectedAngles.add(new Angle(ab, xb));

			expectedAngles.add(new Angle(xc, cb));
			expectedAngles.add(new Angle(ac, cb));
			
			expectedAngles.add(new Angle(xd, cd));
			expectedAngles.add(new Angle(cd, bd));

			expectedAngles.add(new Angle(ab, ac));
			expectedAngles.add(new Angle(ab, xa));

			expectedAngles.add(new Angle(bd, cb));
			expectedAngles.add(new Angle(cb, xb));

			expectedAngles.add(new Angle(xc, cd));
			expectedAngles.add(new Angle(ac, cd));
			
			expectedAngles.add(new Angle(xd, ad));
			expectedAngles.add(new Angle(ad, bd));
			
			// More singletons
			//
			expectedAngles.add(new Angle(xc, xb));
			
			expectedAngles.add(new Angle(xd, xa));

			expectedAngles.add(new Angle(xa, xb));

			expectedAngles.add(new Angle(xd, xc));
			
		}
		catch (FactException te) { System.err.println("Invalid Angles in Angle test."); }

		assertEquals(expectedAngles.size(), computedAngles.size());
		
		//
		// Equality
		//
		for (Angle expected : expectedAngles)
		{
			assertTrue(computedAngles.contains(expected));
		}
	}



	// A-------------B
	// this figure contains 0 angles
	@Test
	void test_line_seg()
	{
		init("jsonfiles/line_seg.json");

		AngleIdentifier angleIdentifier = new AngleIdentifier(_segments);

		AngleEquivalenceClasses computedAngles = angleIdentifier.getAngles();

		// The number of classes should equate to the number of 'minimal' angles
		assertEquals(0, computedAngles.numClasses());
		
		//
		// ALL original segments: 4 in this figure.
		//
		Segment ab = new Segment(_points.getPoint("A"), _points.getPoint("B"));

		//
		// Implied minimal segments: 0 in this figure.
		//

		//
		// Non-minimal, computed segments: 0 in this figure.
		//

		//
		// Angles we expect to find
		//
		List<Angle> expectedAngles = new ArrayList<Angle>();

		assertEquals(expectedAngles.size(), computedAngles.size());
		
		//
		// Equality
		//
		for (Angle expected : expectedAngles)
		{
			assertTrue(computedAngles.contains(expected));
		}
	}
}

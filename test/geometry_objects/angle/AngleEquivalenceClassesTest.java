package geometry_objects.angle;

import org.junit.jupiter.api.Test;

import exceptions.FactException;
import geometry_objects.Segment;
import geometry_objects.points.Point;

import static org.junit.jupiter.api.Assertions.*;

public class AngleEquivalenceClassesTest
{
    /*
     * constructor
     */
    @Test
    void testAngleEquivalenceClasses()
    {
        AngleEquivalenceClasses aec = new AngleEquivalenceClasses();
        assertEquals(0, aec._rest.size());
    }

    /*
     * to empty
     * another
     * new angle type
     * another of new angle type
     * null in
     */
    @Test
    void testAdd_empty() throws FactException
    {
        AngleEquivalenceClasses aec = new AngleEquivalenceClasses();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        assertTrue(aec.add(a));
        assertEquals(1, aec._rest.size());
    }
    @Test
    void testAdd_another() throws FactException
    {
        AngleEquivalenceClasses aec = new AngleEquivalenceClasses();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        assertTrue(aec.add(a));
        assertTrue(aec.add(b));
        assertEquals(1, aec._rest.size());
    }
    @Test
    void testAdd_new() throws FactException
    {
        AngleEquivalenceClasses aec = new AngleEquivalenceClasses();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle c = new Angle(new Segment(new Point(1, 1), new Point(2,  -1)),
                            new Segment(new Point(1, 1), new Point(1, -1)));
        aec.add(a);
        aec.add(b);
        assertTrue(aec.add(c));
        assertEquals(2, aec._rest.size());
    }
    @Test
    void testAdd_anothernew() throws FactException
    {
        AngleEquivalenceClasses aec = new AngleEquivalenceClasses();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle c = new Angle(new Segment(new Point(1, 1), new Point(2,  -1)),
                            new Segment(new Point(1, 1), new Point(1, -1)));
        Angle d = new Angle(new Segment(new Point(1, 1), new Point(2,  -1)),
                            new Segment(new Point(1, 1), new Point(1, -4)));
        aec.add(a);
        aec.add(b);
        aec.add(c);
        assertTrue(aec.add(d));
        assertEquals(2, aec._rest.size());
    }
    @Test
    void testAdd_null()
    {
        AngleEquivalenceClasses aec = new AngleEquivalenceClasses();
        assertFalse(aec.add(null));
        assertEquals(0, aec._rest.size());
    }
}
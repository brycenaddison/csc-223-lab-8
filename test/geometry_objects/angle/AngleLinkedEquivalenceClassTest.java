package geometry_objects.angle;

import org.junit.jupiter.api.Test;

import exceptions.FactException;

import static org.junit.jupiter.api.Assertions.*;
import utilities.eq_classes.LinkedList;
import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.angle.Angle;

class AngleLinkedEquivalenceClassTest
{
    /*
     * constructor - normal
     */
    @Test
    void testAngleLinkedEquivalenceClass()
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        assertNull(alec.canonical());
        assertEquals(0, alec.size());
    }

    /*
     * add to empty
     * add an equal, larger
     * add an equal, smaller (should replace canonical)
     * add an equal, returns 0 when compared
     * add a non-equal
     * null in
     */
    @Test
    void testAdd_empty() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        assertTrue(alec.add(a));
        assertEquals(a, alec.canonical());
        assertEquals(0, alec.size());
    }
    @Test
    void testAdd_larger() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 2)),
                            new Segment(new Point(0, 0), new Point(2, 0)));
        alec.add(a);
        assertTrue(alec.add(b));
        assertEquals(a, alec.canonical());
        assertEquals(1, alec.size());
        assertTrue(alec.contains(b));
    }
    @Test
    void testAdd_smaller() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)),
                            new Segment(new Point(0, 0), new Point(2, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        alec.add(a);
        assertTrue(alec.add(b));
        assertEquals(b, alec.canonical());
        assertEquals(1, alec.size());
        assertTrue(alec.contains(a));
    }
    @Test
    void testAdd_unclear() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)),
                            new Segment(new Point(0,0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(2, 0)));
        alec.add(a);
        assertTrue(alec.add(b));
        assertEquals(a, alec.canonical());
        assertEquals(1, alec.size());
    }
    @Test
    void testAdd_notcomparable() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(2, 2)),
                            new Segment(new Point(0,0), new Point(2, -1)));
        Angle b = new Angle(new Segment(new Point(2, -1), new Point(4, 0)),
                            new Segment(new Point(2, -1), new Point(4, -2)));
        alec.add(a);
        assertFalse(alec.add(b));
        assertEquals(a, alec.canonical());
        assertEquals(0, alec.size());
    }
    @Test
    void testAdd_null()
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        assertFalse(alec.add(null));
    }

    /*
     * demote and set canonical - allways false
     */
    @Test
    void testDemoteAndSetCanonical() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        assertFalse(alec.demoteAndSetCanonical(new Angle(new Segment(new Point(0, 0), new Point(9, 0)),
                                               new Segment(new Point(0,0), new Point(0,9)))));
    }

    /*
     * null in (f)
     * contains (t)
     * does not contain, but is structurally comparable (t)
     * does not contain and is not structurally comparable (f)
     */
    @Test
    void testBelongs_null()
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        assertFalse(alec.belongs(null));
    }
    @Test
    void testBelongs_contains() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 2)),
                            new Segment(new Point(0, 0), new Point(2, 0)));
        alec.add(a);
        alec.add(b);
        assertTrue(alec.belongs(b));
    }
    @Test
    void testBelongs_comparable() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 2)),
                            new Segment(new Point(0, 0), new Point(2, 0)));
        Angle c = new Angle(new Segment(new Point(0, 0), new Point(0, 9)),
                            new Segment(new Point(0, 0), new Point(9, 0)));
        alec.add(a);
        alec.add(b);
        assertTrue(alec.belongs(c));
    }
    @Test
    void testBelongs_notcomparable() throws FactException
    {
        AngleLinkedEquivalenceClass alec = new AngleLinkedEquivalenceClass();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)),
                            new Segment(new Point(0, 0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 2)),
                            new Segment(new Point(0, 0), new Point(2, 0)));
        Angle c = new Angle(new Segment(new Point(9, 8), new Point(6, -8)),
                            new Segment(new Point(9, 8), new Point(3, 3)));
        alec.add(a);
        alec.add(b);
        assertFalse(alec.belongs(c));
    }
}
package geometry_objects.angle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import exceptions.FactException;
import geometry_objects.angle.comparators.AngleStructureComparator;
import geometry_objects.points.Point;
import geometry_objects.Segment;
import geometry_objects.angle.Angle;

class AngleStructureComparatorTest
{
    public static final int STRUCTURALLY_INCOMPARABLE = Integer.MAX_VALUE;
    /*
     * same points
     * rays do not overlap but vertex does
     * totally seperate angles (no vertex overlap)
     * first larger
     * first smaller
     * undecided (0) (left larger, right larger)
     * first rays do not share a point <_
     *                                  \
     * second rays do not share a point /_
     *                                 <
     */
    @Test
    void compare_same()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0,0), new Point(2, 2)));
        assertEquals(0, comparator.compare(a, a));
    }
    @Test
    void compare_vertexoverlaponly()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0,0), new Point(2, 2)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(-1, 1)), new Segment(new Point(0,0), new Point(-1, -5)));
        assertEquals(STRUCTURALLY_INCOMPARABLE, comparator.compare(a, b));
    }
    @Test
    void compare_completelyseperate()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0,0), new Point(2, 2)));
        Angle b = new Angle(new Segment(new Point(8, 8), new Point(10, 10)), new Segment(new Point(8, 8), new Point(8, 9)));
        assertEquals(STRUCTURALLY_INCOMPARABLE, comparator.compare(a, b));
    }
    @Test
    void compare_firstlarger()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0,0), new Point(2, 2)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)), new Segment(new Point(0, 0), new Point(1, 1)));
        assertEquals(1, comparator.compare(a, b));
    }
    @Test
    void compare_firstsmaller()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0,0), new Point(2, 2)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)), new Segment(new Point(0, 0), new Point(1, 1)));
        assertEquals(-1, comparator.compare(b, a));
    }
    @Test
    void compare_undecidedfirstsegmentlarger() throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0,0), new Point(1, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 1)), new Segment(new Point(0, 0), new Point(2, 0)));
        assertEquals(0, comparator.compare(a, b));
    }
    @Test
    void compare_undecidedsecondsegmentlarger()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(0, 1)), new Segment(new Point(0,0), new Point(2, 0)));
        Angle b = new Angle(new Segment(new Point(0, 0), new Point(0, 2)), new Segment(new Point(0, 0), new Point(1, 0)));
        assertEquals(0, comparator.compare(a, b));
    }
    @Test
    void compare_firstraysdonotshare()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(2, 2)), new Segment(new Point(0,0), new Point(2, -1)));
        Angle b = new Angle(new Segment(new Point(2, -1), new Point(4, 0)), new Segment(new Point(2, -1), new Point(4, -2)));
        assertEquals(STRUCTURALLY_INCOMPARABLE, comparator.compare(a, b));
    }
    @Test
    void compare_secondraysdonotshare()  throws FactException
    {
        AngleStructureComparator comparator = new AngleStructureComparator();
        Angle a = new Angle(new Segment(new Point(0, 0), new Point(2, 2)), new Segment(new Point(0,0), new Point(2, -1)));
        Angle b = new Angle(new Segment(new Point(2, 2), new Point(4, 3)), new Segment(new Point(2, 2), new Point(4, 1)));
        assertEquals(STRUCTURALLY_INCOMPARABLE, comparator.compare(a, b));
    }
}
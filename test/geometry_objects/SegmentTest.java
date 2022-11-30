package geometry_objects;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import geometry_objects.points.Point;
import java.util.TreeSet;

class SegmentTest
{
    //hasSubSegment
    //coincideWithoutOverlap
    //collectOrderedPointsOnSegment

    /*
     * share endpoint 1
     * share endpoint 2
     * same segment in (full overlap)
     * half overlap (first end in segment)
     * half overlap (last end in segment)
     * no overlap
     * inside segment (full overlap)
     * null in (throw NullPointer)
     */
    @Test
    void testHasSubSegment_share_end_one()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 4, 0), new Point("D", 5, 1));

        assertFalse(mainseg.hasSubSegment(thatseg));
    }
    @Test
    void testHasSubSegment_share_end_two()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 4, 7), new Point("D", 4, 0));

        assertFalse(mainseg.hasSubSegment(thatseg));
    }
    @Test
    void testHasSubSegment_same_segment()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));

        assertTrue(mainseg.hasSubSegment(mainseg));
    }
    @Test
    void testHasSubSegment_first_point_in()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 2, 0), new Point("D", 6, 0));

        assertFalse(mainseg.hasSubSegment(thatseg));
    }
    @Test
    void testHasSubSegment_last_point_in()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", -2, 0), new Point("D", 2, 0));

        assertFalse(mainseg.hasSubSegment(thatseg));
    }
    @Test
    void testHasSubSegment_no_overlap()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 2, 3), new Point("D", 6, 9));

        assertFalse(mainseg.hasSubSegment(thatseg));
    }
    @Test
    void testHasSubSegment_inside()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 2, 0), new Point("D", 3, 0));

        assertTrue(mainseg.hasSubSegment(thatseg));
    }
    @Test
    void testHasSubSegment_null()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));

        assertThrows(NullPointerException.class, () -> {
            mainseg.hasSubSegment(null); });
    }

    /*
     * may share an endpoint
     * 
     * share endpoint 1
     * share endpoint 2
     * same segment
     * half overlap (first end in segment)
     * half overlap (last end in segment)
     * no overlap no coincide
     * inside segment (full overlap)
     * null in (throw NullPointer)
     * coincide without overlap true
     */
    @Test
    void testCoincideWithoutOverlap_end_one()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("A", -4, 0), new Point("B", 0, 0));

        assertTrue(mainseg.coincideWithoutOverlap(thatseg));
    }
    @Test
    void testCoincideWithoutOverlap_end_two()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 4, 0), new Point("D", 8, 0));

        assertTrue(mainseg.coincideWithoutOverlap(thatseg));
    }
    @Test
    void testCoincideWithoutOverlap_same_segment()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));

        assertFalse(mainseg.coincideWithoutOverlap(mainseg));
    }
    @Test
    void testCoincideWithoutOverlap_first_in()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 2, 0), new Point("D", 5, 0));

        assertFalse(mainseg.coincideWithoutOverlap(thatseg));
    }
    @Test
    void testCoincideWithoutOverlap_last_in()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 5, 1), new Point("D", 3, 0));

        assertFalse(mainseg.coincideWithoutOverlap(thatseg));
    }
    @Test
    void testCoincideWithoutOverlap_no_overlap_no_coincide()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 5, 0), new Point("D", 7, 2));

        assertFalse(mainseg.coincideWithoutOverlap(thatseg));
    }
    @Test
    void testCoincideWithoutOverlap_inside()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 2, 0), new Point("D", 3, 0));

        assertFalse(mainseg.coincideWithoutOverlap(thatseg));
    }
    @Test
    void testCoincideWithoutOverlap_null()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));

        assertThrows(NullPointerException.class, () -> { mainseg.coincideWithoutOverlap(null); } );
    }
    @Test
    void testCoincideWithoutOverlap_true()
    {
        Segment mainseg = new Segment(new Point("A", 0, 0), new Point("B", 4, 0));
        Segment thatseg = new Segment(new Point("C", 7, 0), new Point("D", 5, 0));

        assertFalse(mainseg.coincideWithoutOverlap(thatseg));
    }

    /*
     * empty set
     * set with one point which is on
     * set with one which is not on
     * set with many which are all on
     * set with many, which are not all on
     * null set
     */
    @Test
    void testCollectOrderedPointsOnSegment_empty()
    {
        Segment seg = new Segment(new Point("A", 0, 0), new Point("B", 5, 5));
        TreeSet<Point> set = new TreeSet<Point>();

        assertEquals(set, seg.collectOrderedPointsOnSegment(set));
    }
    @Test
    void testCollectOrderedPointsOnSegment_one_point_on()
    {
        Segment seg = new Segment(new Point("A", 0, 0), new Point("B", 5, 5));
        TreeSet<Point> set = new TreeSet<Point>();
        set.add(new Point("C", 1, 1));

        assertEquals(set, seg.collectOrderedPointsOnSegment(set));
    }
    @Test
    void testCollectOrderedPointsOnSegment_one_point_not_on()
    {
        Segment seg = new Segment(new Point("A", 0, 0), new Point("B", 5, 5));
        TreeSet<Point> set = new TreeSet<Point>();
        set.add(new Point("C", 9, 5));

        assertEquals(new TreeSet<Point>(), seg.collectOrderedPointsOnSegment(set));
    }
    @Test
    void testCollectOrderedPointsOnSegment_many_all_on()
    {
        Segment seg = new Segment(new Point("A", 0, 0), new Point("B", 5, 5));
        TreeSet<Point> set = new TreeSet<Point>();
        set.add(new Point("C", 1, 1));
        set.add(new Point("D", 2, 2));
        set.add(new Point("", 3, 3));

        assertEquals(set, seg.collectOrderedPointsOnSegment(set));
    }
    @Test
    void testCollectOrderedPointsOnSegment_many_some_on()
    {
        Segment seg = new Segment(new Point("A", 0, 0), new Point("B", 5, 5));
        TreeSet<Point> set = new TreeSet<Point>();
        set.add(new Point("C", 1, 1));
        Point pt = new Point("D", 70, -101);
        set.add(pt);
        set.add(new Point("", 3, 3));

        set.remove(pt);

        assertEquals(set, seg.collectOrderedPointsOnSegment(set));
    }
    @Test
    void testCollectOrderedPointsOnSegment_null()
    {
        Segment seg = new Segment(new Point("A", 0, 0), new Point("B", 5, 5));

        assertThrows(NullPointerException.class, () -> { seg.collectOrderedPointsOnSegment(null); });
    }
}
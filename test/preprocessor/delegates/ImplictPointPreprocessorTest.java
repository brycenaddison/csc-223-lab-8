package preprocessor.delegates;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

class ImplicitPointProcessorTest
{
    /*
     * null in
     * both empty
     * one empty
     * other empty
     * only explicit points (simple triangle)
     * mix of implicit and explicit (normal)
     * no intersections (disjointed segments)
     */
    @Test
    void testCompute_null()
    {
        /*
         *   A               B
         *           C
         *   D               E
         */
        Point p1 = new Point("A", 0, 2);
        Point p2 = new Point("B", 2, 2);
        Point p3 = new Point("C", 1, 1);
        Point p4 = new Point("D", 0, 0);
        Point p5 = new Point("E", 2, 0);
        
        ArrayList<Segment> segList = new ArrayList<Segment>();
        segList.add(new Segment(p1, p2));
        segList.add(new Segment(p1, p5));
        segList.add(new Segment(p1, p4));
        segList.add(new Segment(p2, p5));
        segList.add(new Segment(p2, p4));
        segList.add(new Segment(p4, p5));

        // implicit points only
        LinkedHashSet<Point> pExpected = new LinkedHashSet<Point>();
        pExpected.add(p3);

        assertEquals(pExpected, ImplicitPointPreprocessor.compute(null, segList));
    }
    @Test
    void testCompute_bothempty()
    {
        PointDatabase pd = new PointDatabase();
        ArrayList<Segment> segmentList = new ArrayList<Segment>();

        assertEquals(new LinkedHashSet<Point>(), ImplicitPointPreprocessor.compute(pd, segmentList));
    }
    @Test
    void testCompute_emptypoints()
    {
        Point p1 = new Point("A", 0, 0);
        Point p2 = new Point("B", 4, 0);
        Point p3 = new Point("C", 2, 2);

        ArrayList<Segment> segList = new ArrayList<Segment>();
        segList.add(new Segment(p1, p2));
        segList.add(new Segment(p1, p3));
        segList.add(new Segment(p2, p3));

        assertEquals(0, ImplicitPointPreprocessor.compute(new PointDatabase(), segList).size());
    }
    @Test
    void testCompute_emptysegments()
    {
        ArrayList<Segment> segList = new ArrayList<Segment>();

        PointDatabase pd = new PointDatabase();
        pd.put("A", 0, 0);
        pd.put("B", 4, 0);
        pd.put("C", 2, 2);

        // triangle, so no implicit points
        LinkedHashSet<Point> pExpected = new LinkedHashSet<Point>();

        assertEquals(pExpected, ImplicitPointPreprocessor.compute(pd, segList));
    }
    @Test
    void testCompute_allpointsexplicit()
    {
        Point p1 = new Point("A", 0, 0);
        Point p2 = new Point("B", 4, 0);
        Point p3 = new Point("C", 2, 2);

        ArrayList<Segment> segList = new ArrayList<Segment>();
        segList.add(new Segment(p1, p2));
        segList.add(new Segment(p2, p3));
        segList.add(new Segment(p1, p3));

        PointDatabase pd = new PointDatabase();
        pd.put("A", 0, 0);
        pd.put("B", 4, 0);
        pd.put("C", 2, 2);

        LinkedHashSet<Point> pExpected = new LinkedHashSet<Point>();

        assertEquals(pExpected, ImplicitPointPreprocessor.compute(pd, segList));
    }
    @Test
    void testCompute_normal()
    {
        /*
         *   A               B
         *           C
         *   D               E
         */
        Point p1 = new Point("A", 0, 2);
        Point p2 = new Point("B", 2, 2);
        Point p3 = new Point("C", 1, 1);
        Point p4 = new Point("D", 0, 0);
        Point p5 = new Point("E", 2, 0);
        
        ArrayList<Segment> segList = new ArrayList<Segment>();
        segList.add(new Segment(p1, p2));
        segList.add(new Segment(p1, p5));
        segList.add(new Segment(p1, p4));
        segList.add(new Segment(p2, p5));
        segList.add(new Segment(p2, p4));
        segList.add(new Segment(p4, p5));

        PointDatabase pd = new PointDatabase();
        pd.put("A", 0, 2);
        pd.put("B", 2, 2);
        pd.put("D", 0, 0);
        pd.put("E", 2, 0);

        //only one implicit point
        LinkedHashSet<Point> pExpected = new LinkedHashSet<Point>();
        pExpected.add(p3);

        assertEquals(pExpected, ImplicitPointPreprocessor.compute(pd, segList));
   }
    @Test
    void testCompute_nointersections()
    {
        Point p1 = new Point("A", 0, 0);
        Point p2 = new Point("B", 2, 0);
        Point p3 = new Point("C", 4, 4);
        Point p4 = new Point("D", 4, 0);
        Point p5 = new Point("E", -7, -9);
        Point p6 = new Point("F", -8, -10);
        
        Segment s1 = new Segment(p1, p2);
        Segment s2 = new Segment(p3, p4);
        Segment s3 = new Segment(p5, p6);

        ArrayList<Segment> segmentList = new ArrayList<Segment>();
        segmentList.add(s1);
        segmentList.add(s2);
        segmentList.add(s3);

        PointDatabase pd = new PointDatabase();
        pd.put("A", 0, 0);
        pd.put("B", 2, 0);
        pd.put("C", 4, 4);
        pd.put("D", 4, 0);
        pd.put("E", -7, -9);
        pd.put("F", -8, -10);

        // no intersections expected
        LinkedHashSet<Point> pExpected = new LinkedHashSet<Point>();

        assertEquals(pExpected, ImplicitPointPreprocessor.compute(pd, segmentList));
    }
}
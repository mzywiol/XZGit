/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author eXistenZ
 */
public class PointTest
{
   private static final double DELTA = 0.0001;
   public PointTest()
   {
   }
   
   @BeforeClass
   public static void setUpClass()
   {
   }
   
   @AfterClass
   public static void tearDownClass()
   {
   }
   
   @Before
   public void setUp()
   {
   }
   
   @After
   public void tearDown()
   {
   }

   @Test
   public void testDirection()
   {
      Point a = new Point(5.0, 2.0);
      Point b = new Point(9.0, 4.0);
      Point c = new Point(3.0, 6.0);
      Point d = new Point(7.0, 6.0);
      double atan = a.direction(b).deg();
      assertEquals(90.0 - atan, a.direction(d).deg(), DELTA);
      assertEquals(atan + 90.0, c.direction(a).deg(), DELTA);
      assertEquals(atan - 180.0, b.direction(a).deg(), DELTA);
      assertEquals(atan - 90.0, a.direction(c).deg(), DELTA);
      assertEquals(0.0, a.direction(a).deg(), DELTA);

      // corner cases and signum documentation
      Point zero = new Point(0.0, 0.0);
      assertEquals(45.0, zero.direction(new Point(1.0, 1.0)).deg(), DELTA);
      assertEquals(-45.0, zero.direction(new Point(-1.0, 1.0)).deg(), DELTA);
      assertEquals(-135.0, zero.direction(new Point(-1.0, -1.0)).deg(), DELTA);
      assertEquals(135.0, zero.direction(new Point(1.0, -1.0)).deg(), DELTA);
      assertEquals(0.0, zero.direction(new Point(0.0, 0.0)).deg(), DELTA);
      assertEquals(0.0, zero.direction(new Point(0.0, 1.0)).deg(), DELTA);
      assertEquals(90.0, zero.direction(new Point(1.0, 0.0)).deg(), DELTA);
      assertEquals(-90.0, zero.direction(new Point(-1.0, 0.0)).deg(), DELTA);
      assertEquals(180.0, zero.direction(new Point(0.0, -1.0)).deg(), DELTA);
   }

   @Test
   public void testDistance()
   {
      Point a = new Point(3.0, 2.0);
      Point b = new Point(7.0, 5.0);
      Point c = new Point(6.0, 6.0);
      Point d = new Point(0.0, -2.0);
      Point e = new Point(6.0, -2.0);
      assertEquals(5.0, a.distance(b), DELTA);
      assertEquals(5.0, a.distance(c), DELTA);
      assertEquals(5.0, a.distance(d), DELTA);
      assertEquals(5.0, a.distance(e), DELTA);
      assertEquals(10.0, c.distance(d), DELTA);
      assertEquals(8.0, c.distance(e), DELTA);
      assertEquals(6.0, d.distance(e), DELTA);
   }
   
   @Test
   public void testCrossProduct()
   {
      Point a = new Point(0.0, 1.0);
      Point b = new Point(3.0, 0.0);
      
      // cross product is < 0 => b is to the left of a, a is to the right of b
      assertTrue(a.crossProduct(b) < 0.0);
      assertTrue(a.isToRightOf(b));
      // cross product is > 0 => b is to the left of a, a is to the right of b
      assertTrue(b.crossProduct(a) > 0.0);
      assertTrue(b.isToLeftOf(a));
      // cross product == 0 => vectors are parallel
      assertTrue(a.crossProduct(a) == 0.0);
      assertTrue(a.crossProduct(new Point(0.0, 1000.0)) == 0.0);
      assertTrue(a.isParallelTo(new Point(0.0, 1000.0)));
      assertTrue(a.crossProduct(new Point(0.0, -1000.0)) == 0.0);
      assertFalse(b.isParallelTo(new Point(0.0, 1000.0)));
      
      // in relation to zero vector
      Point p0 = new Point(0.0, 0.0);
      assertTrue(a.crossProduct(p0) == 0.0);
      assertTrue(b.crossProduct(p0) == 0.0);
      assertTrue(p0.crossProduct(a) == 0.0);
      assertTrue(p0.crossProduct(b) == 0.0);
      assertTrue(p0.crossProduct(p0) == 0.0);
   }
   
   @Test
   public void testIsWithinTriangle()
   {
      Point a = new Point(0.0, 1.0);
      Point b = new Point(3.0, 0.0);
      Point c = new Point(5.0, 1.0);
      
      // vertex is not within triangle
      assertFalse(a.isWithinTriangle(a, b, c));
      assertFalse(b.isWithinTriangle(a, b, c));
      assertFalse(c.isWithinTriangle(a, b, c));
      
      // edge is not within triangle
      assertFalse(a.midpoint(b).isWithinTriangle(a, b, c));
      assertFalse(b.midpoint(c).isWithinTriangle(a, b, c));
      assertFalse(c.midpoint(a).isWithinTriangle(a, b, c));
      
      // points within triangle
      assertTrue(new Point(2.0, 0.5).isWithinTriangle(a, b, c));
      assertTrue(new Point(3.0, 0.001).isWithinTriangle(c, b, a));
      assertTrue(new Point(4.0, 0.75).isWithinTriangle(b, a, c));
      
      // points outside triangle
      assertFalse(new Point(1.0, 0.5).isWithinTriangle(a, b, c));
      assertFalse(new Point(3.0, 1.001).isWithinTriangle(c, a, b));
      assertFalse(new Point(0.0, 0.0).isWithinTriangle(c, b, a));
   }
   
   @Test
   public void testXAxisComparator()
   {
      assertEquals(0, Point.X_AXIS_COMPARATOR.compare(new Point(1.0, 1.0), new Point(1.0, 1.0)));
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(0.0, 1.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(0.0, 0.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(0.9, -1.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(2.0, 1.0), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(2.0, 0.0), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(1.000001, -1.0), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(1.0, -1.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(1.0, 0.99999), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(1.0, 1.000001), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.X_AXIS_COMPARATOR.compare(new Point(1.0, 2.0), new Point(1.0, 1.0)) > 0);
   }
   
   @Test
   public void testYAxisComparator()
   {
      assertEquals(0, Point.Y_AXIS_COMPARATOR.compare(new Point(1.0, 1.0), new Point(1.0, 1.0)));
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(1.0, 0.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(0.0, 0.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(-1.0, 0.9), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(1.0, 2.0), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(0.0, 2.0), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(-1.0, 1.000001), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(-1.0, 1.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(0.99999, 1.0), new Point(1.0, 1.0)) < 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(1.000001, 1.0), new Point(1.0, 1.0)) > 0);
      assertTrue(Point.Y_AXIS_COMPARATOR.compare(new Point(2.0, 1.0), new Point(1.0, 1.0)) > 0);
   }
}

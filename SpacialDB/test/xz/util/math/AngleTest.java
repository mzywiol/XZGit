/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.util.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static xz.util.math.XZMathTest.DELTA;

/**
 *
 * @author eXistenZ
 */
public class AngleTest
{
   @Test
   public void testDeg()
   {
      Angle._clearCache();
      
      Angle angle0 = Angle.deg(0.0);
      assertEquals(0.0, angle0.deg(), 0.0);
      assertSame(angle0, Angle.deg(360.0));
      assertSame(angle0, Angle.deg(-360.0));
      Angle angle180 = Angle.deg(180.0);
      assertEquals(180.0, angle180.deg(), 0.0);
      assertSame(angle180, Angle.deg(-180.0));
      assertSame(angle180, Angle.deg(540.0));
      assertNotSame(angle0, angle180);
      
      // precision tests
      assertSame(angle0, Angle.deg(0.00001));
      assertSame(angle0, Angle.deg(360.00001));
      assertSame(angle0, Angle.deg(359.9999));
      
      Angle angle = Angle.deg(42.19787667);
      assertTrue(angle.equals(Angle.deg(42.198)));
      
      Angle._clearCache();
   }

   @Test
   public void testNormalize()
   {
      assertEquals(0.0, Angle.normalize(0.0), DELTA);
      assertEquals(90.0, Angle.normalize(90.0), DELTA);
      assertEquals(179.99, Angle.normalize(179.99), DELTA);
      assertEquals(180.0, Angle.normalize(180.0), DELTA);
      assertEquals(-90.0, Angle.normalize(-90.0), DELTA);
      assertEquals(-179.99, Angle.normalize(-179.99), DELTA);
      assertEquals(-179.0, Angle.normalize(181.0), DELTA);
      assertEquals(-90.0, Angle.normalize(270.0), DELTA);
      assertEquals(0.0, Angle.normalize(360.0), DELTA);
      assertEquals(179.0, Angle.normalize(539.0), DELTA);
      assertEquals(180.0, Angle.normalize(540.0), DELTA);
      assertEquals(-179.0, Angle.normalize(541.0), DELTA);
      assertEquals(90.0, Angle.normalize(-270.0), DELTA);
      assertEquals(0.0, Angle.normalize(-360.0), DELTA);
      assertEquals(-179.0, Angle.normalize(-539.0), DELTA);
      assertEquals(180.0, Angle.normalize(-540.0), DELTA);
      assertEquals(179.0, Angle.normalize(-541.0), DELTA);
      assertEquals(90.0, Angle.normalize(-630.0), DELTA);
      assertEquals(0.0, Angle.normalize(-720.0), DELTA);
      assertEquals(-179.0, Angle.normalize(-899.0), DELTA);
      assertEquals(180.0, Angle.normalize(-900.0), DELTA);
      assertEquals(179.0, Angle.normalize(-901.0), DELTA);
   }
   
   @Test
   public void testDiffDeg()
   {
      Angle angle0 = Angle.deg(0.0);
      Angle angle60 = Angle.deg(60.0);
      Angle angle180 = Angle.deg(180.0);
      
      assertEquals(0.0, angle60.diffDeg(angle60), 0.0);
      assertEquals(0.0, angle60.diffDeg(Angle.deg(-300.0)), 0.0);
      assertEquals(0.0, angle60.diffDeg(Angle.deg(420.0)), 0.0);
      assertEquals(60.0, angle0.diffDeg(angle60), 0.0);
      assertEquals(-60.0, angle60.diffDeg(angle0), 0.0);
      assertEquals(180.0, angle0.diffDeg(angle180), 0.0);
      assertEquals(180.0, angle0.diffDeg(Angle.deg(-180.0)), 0.0);
      assertEquals(120.0, angle60.diffDeg(Angle.deg(-180.0)), 0.0);
      assertEquals(120.0, angle60.diffDeg(Angle.deg(180.0)), 0.0);
      assertEquals(120.0, angle180.diffDeg(Angle.deg(-60.0)), 0.0);
      assertEquals(-120.0, angle180.diffDeg(Angle.deg(60.0)), 0.0);
      assertEquals(120.0, Angle.deg(150.0).diffDeg(Angle.deg(-90.0)), 0.0);
      assertEquals(-120.0, Angle.deg(-135.0).diffDeg(Angle.deg(105.0)), 0.0);
   }
   
   @Test
   public void testIsBetween()
   {
      assertTrue(Angle.deg(60.0).isBetween(Angle.deg(59.0), Angle.deg(61.0)));
      assertFalse(Angle.deg(60.0).isBetween(Angle.deg(60.0), Angle.deg(61.0)));
      assertFalse(Angle.deg(60.0).isBetween(Angle.deg(59.0), Angle.deg(60.0)));
      assertTrue(Angle.deg(0.0).isBetween(Angle.deg(-90.0), Angle.deg(90.0)));
      assertTrue(Angle.deg(0.0).isBetween(Angle.deg(-361.0), Angle.deg(361.0)));
      assertTrue(Angle.deg(180.0).isBetween(Angle.deg(179.0), Angle.deg(-179.0)));
      assertFalse(Angle.deg(180.0).isBetween(Angle.deg(-179.0), Angle.deg(179.0)));
      assertTrue(Angle.deg(180.0).isBetween(Angle.deg(-179.0), Angle.deg(181.0)));
   }
   
   @Test
   public void testIsToLeftOf()
   {
      Angle angle45 = Angle.deg(45.0);
      assertTrue(Angle.deg(30.0).isToLeftOf(angle45));
      assertTrue(Angle.deg(0.0).isToLeftOf(angle45));
      assertTrue(Angle.deg(-90.0).isToLeftOf(angle45));
      assertTrue(Angle.deg(-134.9).isToLeftOf(angle45));
      assertFalse(Angle.deg(-135.0).isToLeftOf(angle45));
      assertFalse(Angle.deg(-180.0).isToLeftOf(angle45));
      assertFalse(Angle.deg(180.0).isToLeftOf(angle45));
      assertFalse(Angle.deg(90.0).isToLeftOf(angle45));
      assertFalse(angle45.isToLeftOf(angle45));
   }
   
   @Test
   public void testIsToRightOf()
   {
      Angle angle45 = Angle.deg(45.0);
      assertFalse(Angle.deg(30.0).isToRightOf(angle45));
      assertFalse(Angle.deg(0.0).isToRightOf(angle45));
      assertFalse(Angle.deg(-90.0).isToRightOf(angle45));
      assertFalse(Angle.deg(-134.9).isToRightOf(angle45));
      assertFalse(Angle.deg(-135.0).isToRightOf(angle45));
      assertTrue(Angle.deg(-180.0).isToRightOf(angle45));
      assertTrue(Angle.deg(180.0).isToRightOf(angle45));
      assertTrue(Angle.deg(90.0).isToRightOf(angle45));
      assertFalse(angle45.isToRightOf(angle45));
   }
}

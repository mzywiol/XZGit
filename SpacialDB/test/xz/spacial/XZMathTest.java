/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xz.spacial;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author eXistenZ
 */
public class XZMathTest
{
   static final double DELTA = 0.0001;

   @Test
   public void testNormalize()
   {
      // range 0.0
      try
      {
         assertEquals(0.0, XZMath.normalize(0.0, 180.0, 0.0), DELTA);
         fail("Should throw ArithmeticException due to range 0.0");
      } catch (ArithmeticException e)
      { /* expected */ }

      assertEquals(0.0, XZMath.normalize(0.0, 0.0, 360.0), DELTA);
      assertEquals(90.0, XZMath.normalize(90.0, 0.0, 360.0), DELTA);
      assertEquals(179.99, XZMath.normalize(179.99, 0.0, 360.0), DELTA);
      assertEquals(180.0, XZMath.normalize(180.0, 0.0, 360.0), DELTA);
      assertEquals(270.0, XZMath.normalize(-90.0, 0.0, 360.0), DELTA);
      assertEquals(180.01, XZMath.normalize(-179.99, 0.0, 360.0), DELTA);
      assertEquals(270.0, XZMath.normalize(270.0, 0.0, 360.0), DELTA);
      assertEquals(0.0, XZMath.normalize(360.0, 0.0, 360.0), DELTA);
      assertEquals(179.0, XZMath.normalize(539.0, 0.0, 360.0), DELTA);
      assertEquals(180.0, XZMath.normalize(540.0, 0.0, 360.0), DELTA);
      assertEquals(181.0, XZMath.normalize(541.0, 0.0, 360.0), DELTA);
      assertEquals(90.0, XZMath.normalize(-270.0, 0.0, 360.0), DELTA);
      assertEquals(0.0, XZMath.normalize(-360.0, 0.0, 360.0), DELTA);
      assertEquals(181.0, XZMath.normalize(-539.0, 0.0, 360.0), DELTA);
      assertEquals(180.0, XZMath.normalize(-540.0, 0.0, 360.0), DELTA);
      assertEquals(179.0, XZMath.normalize(-541.0, 0.0, 360.0), DELTA);
      assertEquals(90.0, XZMath.normalize(-630.0, 0.0, 360.0), DELTA);
      assertEquals(0.0, XZMath.normalize(-720.0, 0.0, 360.0), DELTA);
      assertEquals(181.0, XZMath.normalize(-899.0, 0.0, 360.0), DELTA);
      assertEquals(180.0, XZMath.normalize(-900.0, 0.0, 360.0), DELTA);
      assertEquals(179.0, XZMath.normalize(-901.0, 0.0, 360.0), DELTA);
   }

   @Test
   public void testCompareToRange()
   {
      assertEquals(0, XZMath.compareToRange(5.0, 2.0, true, 10.0, true));
      assertEquals(0, XZMath.compareToRange(2.0, 2.0, true, 10.0, true));
      assertEquals(-1, XZMath.compareToRange(2.0, 2.0, false, 10.0, true));
      assertEquals(0, XZMath.compareToRange(2.0001, 2.0, true, 10.0, true));
      assertEquals(-1, XZMath.compareToRange(1.9999, 2.0, true, 10.0, true));
      assertEquals(0, XZMath.compareToRange(9.9999, 2.0, true, 10.0, true));
      assertEquals(0, XZMath.compareToRange(10.0, 2.0, true, 10.0, true));
      assertEquals(1, XZMath.compareToRange(10.0, 2.0, true, 10.0, false));
      assertEquals(1, XZMath.compareToRange(10.0001, 2.0, true, 10.0, true));

      assertEquals(0, XZMath.compareToRange(5.0, 10.0, true, 2.0, true));
      assertEquals(0, XZMath.compareToRange(2.0, 10.0, true, 2.0, true));
      assertEquals(-1, XZMath.compareToRange(2.0, 10.0, true, 2.0, false));
      assertEquals(0, XZMath.compareToRange(2.0001, 10.0, true, 2.0, true));
      assertEquals(-1, XZMath.compareToRange(1.9999, 10.0, true, 2.0, true));
      assertEquals(0, XZMath.compareToRange(9.9999, 10.0, true, 2.0, true));
      assertEquals(0, XZMath.compareToRange(10.0, 10.0, true, 2.0, true));
      assertEquals(1, XZMath.compareToRange(10.0, 10.0, false, 2.0, true));
      assertEquals(1, XZMath.compareToRange(10.0001, 10.0, true, 2.0, true));
   }

}

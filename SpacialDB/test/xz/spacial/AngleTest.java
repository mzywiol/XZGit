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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static xz.spacial.XZMathTest.DELTA;

/**
 *
 * @author eXistenZ
 */
public class AngleTest
{
   
   public AngleTest()
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
   
}

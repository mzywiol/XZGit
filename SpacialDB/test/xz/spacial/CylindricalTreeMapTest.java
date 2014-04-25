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

/**
 *
 * @author eXistenZ
 */
public class CylindricalTreeMapTest
{
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
   public void testPutGet()
   {
      CylindricalTreeMap<String> map = new CylindricalTreeMap<>();
      Angle angle0 = Angle.deg(0.0);
      Angle angle180 = Angle.deg(180.0);
      Angle angle180n = Angle.deg(-180.0);
      Angle angle360 = Angle.deg(360.0);
      Angle angle90 = Angle.deg(90.0);

      map.put(angle0, "A");
      assertEquals("A", map.get(angle0));
      assertEquals("A", map.get(0.0));
      assertEquals("A", map.get(360.0));
      map.put(angle180, "B");
      assertEquals("B", map.get(angle180));
      assertEquals("B", map.get(180.0));
      assertEquals("B", map.get(540.0));
      assertEquals("B", map.get(-180.0));
      assertEquals("B", map.get(-540.0));
      map.put(angle180n, "C");
      assertEquals("C", map.get(angle180));
      assertEquals("C", map.get(angle180n));
      assertEquals("C", map.get(180.0));
      assertEquals("C", map.get(-180.0));
      map.put(angle360, "D");
      assertEquals("D", map.get(angle360));
      assertEquals("D", map.get(angle0));
      assertEquals("D", map.get(360.0));
      assertEquals("D", map.get(0.0));
      map.put(angle90, "E");
      assertEquals("E", map.get(angle90));
      assertEquals("E", map.get(90.0));
      assertEquals("E", map.get(450.0));
      assertEquals("E", map.get(-270.0));
      
   }
   
}

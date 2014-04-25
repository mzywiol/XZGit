/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Iterator;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author eXistenZ
 */
public class ContinuousTreeMapTest
{
   ContinuousTreeMap<Double, String> map;
   static Double zero = 0.0, one = 1.0, two = 2.0, three = 3.0;
   
   public ContinuousTreeMapTest()
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
      map = new ContinuousTreeMap<>();
   }
   
   @After
   public void tearDown()
   {
   }

   /**
    * Test of higherKey method, of class CircularTreeMap.
    */
   @Test
   public void testHigherKey()
   {
      assertTrue(map.isEmpty());
      assertNull(map.higherKey(zero));
      
      map.put(one, "");
      assertEquals(one, map.higherKey(zero));
      assertNull(map.higherKey(one));
      assertEquals(one, map.higherKey(two));
      
      map.put(two, "");
      assertEquals(one, map.higherKey(zero));
      assertEquals(two, map.higherKey(one));
      assertEquals(one, map.higherKey(two));
   }

   /**
    * Test of higherEntry method, of class CircularTreeMap.
    */
   @Test
   public void testHigherEntry()
   {
      assertTrue(map.isEmpty());
      assertNull(map.higherEntry(zero));

      map.put(one, "");
      assertEquals(one, map.higherEntry(zero).getKey());
      assertNull(map.higherEntry(one));
      assertEquals(one, map.higherEntry(two).getKey());

      map.put(two, "");
      assertEquals(one, map.higherEntry(zero).getKey());
      assertEquals(two, map.higherEntry(one).getKey());
      assertEquals(one, map.higherEntry(two).getKey());
   }

   /**
    * Test of ceilingKey method, of class CircularTreeMap.
    */
   @Test
   public void testCeilingKey()
   {
      assertTrue(map.isEmpty());
      assertNull(map.ceilingKey(zero));

      map.put(one, "");
      assertEquals(one, map.ceilingKey(zero));
      assertEquals(one, map.ceilingKey(one));
      assertEquals(one, map.ceilingKey(two));

      map.put(two, "");
      assertEquals(one, map.ceilingKey(zero));
      assertEquals(one, map.ceilingKey(one));
      assertEquals(two, map.ceilingKey(two));
      assertEquals(one, map.ceilingKey(three));
   }

   /**
    * Test of ceilingEntry method, of class CircularTreeMap.
    */
   @Test
   public void testCeilingEntry()
   {
      assertTrue(map.isEmpty());
      assertNull(map.ceilingEntry(zero));

      map.put(one, "");
      assertEquals(one, map.ceilingEntry(zero).getKey());
      assertEquals(one, map.ceilingEntry(one).getKey());
      assertEquals(one, map.ceilingEntry(two).getKey());

      map.put(two, "");
      assertEquals(one, map.ceilingEntry(zero).getKey());
      assertEquals(one, map.ceilingEntry(one).getKey());
      assertEquals(two, map.ceilingEntry(two).getKey());
      assertEquals(one, map.ceilingEntry(three).getKey());
   }

   /**
    * Test of floorKey method, of class CircularTreeMap.
    */
   @Test
   public void testFloorKey()
   {
      assertTrue(map.isEmpty());
      assertNull(map.floorKey(zero));

      map.put(one, "");
      assertEquals(one, map.floorKey(zero));
      assertEquals(one, map.floorKey(one));
      assertEquals(one, map.floorKey(two));

      map.put(two, "");
      assertEquals(two, map.floorKey(zero));
      assertEquals(one, map.floorKey(one));
      assertEquals(two, map.floorKey(two));
      assertEquals(two, map.floorKey(three));
   }

   /**
    * Test of floorEntry method, of class CircularTreeMap.
    */
   @Test
   public void testFloorEntry()
   {
      assertTrue(map.isEmpty());
      assertNull(map.floorEntry(zero));

      map.put(one, "");
      assertEquals(one, map.floorEntry(zero).getKey());
      assertEquals(one, map.floorEntry(one).getKey());
      assertEquals(one, map.floorEntry(two).getKey());

      map.put(two, "");
      assertEquals(two, map.floorEntry(zero).getKey());
      assertEquals(one, map.floorEntry(one).getKey());
      assertEquals(two, map.floorEntry(two).getKey());
      assertEquals(two, map.floorEntry(three).getKey());
   }

   /**
    * Test of lowerKey method, of class CircularTreeMap.
    */
   @Test
   public void testLowerKey()
   {
      assertTrue(map.isEmpty());
      assertNull(map.lowerKey(zero));

      map.put(one, "");
      assertEquals(one, map.lowerKey(zero));
      assertNull(map.lowerKey(one));
      assertEquals(one, map.lowerKey(two));

      map.put(two, "");
      assertEquals(two, map.lowerKey(zero));
      assertEquals(two, map.lowerKey(one));
      assertEquals(one, map.lowerKey(two));
   }

   /**
    * Test of lowerEntry method, of class CircularTreeMap.
    */
   @Test
   public void testLowerEntry()
   {
      assertTrue(map.isEmpty());
      assertNull(map.lowerEntry(zero));

      map.put(one, "");
      assertEquals(one, map.lowerEntry(zero).getKey());
      assertNull(map.lowerEntry(one));
      assertEquals(one, map.lowerEntry(two).getKey());

      map.put(two, "");
      assertEquals(two, map.lowerEntry(zero).getKey());
      assertEquals(two, map.lowerEntry(one).getKey());
      assertEquals(one, map.lowerEntry(two).getKey());
   }
   
   @Test
   public void testHigherIterator()
   {
      assertTrue(map.isEmpty());
      assertFalse(map.higherIterator(zero).hasNext());
      
      map.put(one, "");
      map.put(three, "");
      map.put(zero, "");
      map.put(two, "");
      
      Iterator<Map.Entry<Double, String>> iterator = map.higherIterator(two);
      assertEquals(three, iterator.next().getKey());
      assertEquals(zero, iterator.next().getKey());
      assertEquals(one, iterator.next().getKey());
      assertFalse(iterator.hasNext());
      
      iterator = map.higherIterator(2.5);
      assertEquals(three, iterator.next().getKey());
      assertEquals(zero, iterator.next().getKey());
      assertEquals(one, iterator.next().getKey());
      assertEquals(two, iterator.next().getKey());
      assertFalse(iterator.hasNext());
   }
   
   @Test
   public void testLowerIterator()
   {
      assertTrue(map.isEmpty());
      assertFalse(map.lowerIterator(zero).hasNext());
      
      map.put(one, "");
      map.put(three, "");
      map.put(zero, "");
      map.put(two, "");
      
      Iterator<Map.Entry<Double, String>> iterator = map.lowerIterator(two);
      assertEquals(one, iterator.next().getKey());
      assertEquals(zero, iterator.next().getKey());
      assertEquals(three, iterator.next().getKey());
      assertFalse(iterator.hasNext());
      
      iterator = map.lowerIterator(1.5);
      assertEquals(one, iterator.next().getKey());
      assertEquals(zero, iterator.next().getKey());
      assertEquals(three, iterator.next().getKey());
      assertEquals(two, iterator.next().getKey());
      assertFalse(iterator.hasNext());
   }
   
}

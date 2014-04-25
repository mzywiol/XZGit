/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import xz.util.test.XZTestHelper;
import xz.util.math.Point;
import xz.util.math.Angle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author eXistenZ
 */
public class NodeTest
{
   
   public NodeTest()
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
   public void testConnect()
   {
      Node<String> node1 = newNode(0.0, 0.0);
      
      XZTestHelper.assertNPE(() -> { node1.connect(null); }); 
      node1.connect(node1);
      assertTrue(node1.edges.isEmpty());
      
      Node<String> node2 = newNode(0.0, 10.0);
      Node<String> node3 = newNode(10.0, 0.0);

      assertTrue(node1.edges.isEmpty());
      assertTrue(node2.edges.isEmpty());
      assertTrue(node3.edges.isEmpty());

      node1.connect(node2);
      assertEquals(1, node1.edges.size());
      assertSame(node2, node1.edges.get(0.0));
      assertEquals(1, node2.edges.size());
      assertSame(node1, node2.edges.get(180.0));
      assertEquals(0, node3.edges.size());

      node3.connect(node2);
      assertEquals(1, node1.edges.size());
      assertEquals(2, node2.edges.size());
      assertSame(node3, node2.edges.get(135.0));
      assertEquals(1, node3.edges.size());
      assertSame(node2, node3.edges.get(-45.0));
      
      node1.connect(node3);
      assertEquals(2, node1.edges.size());
      assertSame(node3, node1.edges.get(90.0));
      assertEquals(2, node2.edges.size());
      assertEquals(2, node3.edges.size());
      assertSame(node1, node3.edges.get(-90.0));
   }
   
   @Test
   public void testConnect_withAngle()
   {
      Node<String> node1 = newNode(0.0, 0.0);
      
      XZTestHelper.assertNPE(() -> {node1.connect(null, Angle.deg(123.0));});
      node1.connect(node1, Angle.deg(123.0));
      assertTrue(node1.edges.isEmpty());

      Node<String> node2 = newNode(0.0, 1.0);
      
      XZTestHelper.assertNPE(() -> {node1.connect(node2, null);});
      
      Node<String> node3 = newNode(0.0, 2.0);
      
      assertTrue(node1.edges.isEmpty());
      assertTrue(node2.edges.isEmpty());
      assertTrue(node3.edges.isEmpty());
      
      node1.connect(node2, Angle.deg(30.0));
      assertEquals(1, node1.edges.size());
      assertSame(node2, node1.edges.get(30.0));
      assertEquals(1, node2.edges.size());
      assertSame(node1, node2.edges.get(-150.0));
      assertEquals(0, node3.edges.size());
      
      node3.connect(node2, Angle.deg(-179.0));
      assertEquals(1, node1.edges.size());
      assertEquals(2, node2.edges.size());
      assertSame(node3, node2.edges.get(1.0));
      assertEquals(1, node3.edges.size());
      assertSame(node2, node3.edges.get(-179.0));
   }
   
   @Test
   public void testIsConnectedTo()
   {
      Node<String> node = newNode(0.0, 0.0);
      Node<String> nodeAt0 = new Node<>(new Point(1.0, 0.0), null);
      Node<String> nodeAt45 = new Node<>(new Point(1.0, 1.0), null);
      
      XZTestHelper.assertNPE(() -> {node.isConnectedTo(null);});
      
      assertFalse(node.isConnectedTo(node));
      assertFalse(node.isConnectedTo(nodeAt0));
      assertFalse(node.isConnectedTo(nodeAt45));

      node.connect(nodeAt0);
      assertTrue(node.isConnectedTo(nodeAt0));
      assertTrue(nodeAt0.isConnectedTo(node));
      assertFalse(node.isConnectedTo(nodeAt45));
      
      node.connect(nodeAt45);
      assertTrue(node.isConnectedTo(nodeAt0));
      assertTrue(nodeAt0.isConnectedTo(node));
      assertTrue(node.isConnectedTo(nodeAt45));
      assertTrue(nodeAt45.isConnectedTo(node));
      assertFalse(nodeAt0.isConnectedTo(nodeAt45));
   }
   
   @Test
   public void testConnectionAngle()
   {
      Node<String> node = newNode(0.0, 0.0);
      
      XZTestHelper.assertNPE(() -> {node.connectionAngle(null);});
      
      Node<String> nodeAt0 = new Node<>(new Point(0.0, 1.0), null);
      Node<String> nodeAt45 = new Node<>(new Point(1.0, 1.0), null);

      assertNull(node.connectionAngle(node));
      assertNull(node.connectionAngle(nodeAt0));
      assertNull(node.connectionAngle(nodeAt45));
      assertNull(nodeAt0.connectionAngle(nodeAt45));

      node.connect(nodeAt0);
      assertEquals(0.0, node.connectionAngle(nodeAt0).deg(), 0.0);
      assertEquals(180.0, nodeAt0.connectionAngle(node).deg(), 0.0);
      assertNull(node.connectionAngle(nodeAt45));

      node.connect(nodeAt45);
      assertEquals(45.0, node.connectionAngle(nodeAt45).deg(), 0.0);
      assertEquals(-135.0, nodeAt45.connectionAngle(node).deg(), 0.0);
      assertNull(nodeAt0.connectionAngle(nodeAt45));
   }
   
   @Test
   public void testGetEdgeLeft()
   {
      Node<String> node = newNode(0.0, 0.0);

      XZTestHelper.assertNPE(() -> { node.getEdgeLeft(null); });

      Node<String> nodeAt0 = new Node<>(new Point(0.0, 1.0), null);
      Node<String> nodeAt45 = new Node<>(new Point(1.0, 1.0), null);
      Angle angle0 = Angle.deg(0.0);
      Angle angle45 = Angle.deg(45.0);
      
      assertNull(node.getEdgeLeft(angle0));
      assertNull(node.getEdgeLeft(Angle.deg(180.0)));
      
      node.connect(nodeAt0, angle0);
      assertNull(node.getEdgeLeft(angle0));
      assertNull(node.getEdgeLeft(Angle.deg(-0.1)));
      assertSame(nodeAt0, node.getEdgeLeft(Angle.deg(0.1)));
      assertSame(nodeAt0, node.getEdgeLeft(Angle.deg(179.99)));
      assertNull(node.getEdgeLeft(Angle.deg(180.0)));
      assertNull(node.getEdgeLeft(Angle.deg(-180.0)));
      
      node.connect(nodeAt45, angle45);
      assertNull(node.getEdgeLeft(angle0));
      assertSame(nodeAt0, node.getEdgeLeft(Angle.deg(0.1)));
      assertSame(nodeAt0, node.getEdgeLeft(angle45));
      assertSame(nodeAt45, node.getEdgeLeft(Angle.deg(45.1)));
      assertSame(nodeAt45, node.getEdgeLeft(Angle.deg(224.9)));
      assertSame(nodeAt45, node.getEdgeLeft(Angle.deg(-135.1)));
   }
   
   @Test
   public void testGetEdgeRight()
   {
      Node<String> node = newNode(0.0, 0.0);
      
      XZTestHelper.assertNPE(() -> {node.getEdgeRight(null);});
      
      Node<String> nodeAt0 = newNode(0.0, 1.0);
      Node<String> nodeAt45 = newNode(1.0, 1.0);
      Angle angle0 = Angle.deg(0.0);
      Angle angle45 = Angle.deg(45.0);

      assertNull(node.getEdgeRight(angle0));

      node.connect(nodeAt0, angle0);
      assertNull(node.getEdgeRight(angle0));
      assertNull(node.getEdgeRight(Angle.deg(0.1)));
      assertSame(nodeAt0, node.getEdgeRight(Angle.deg(-0.1)));
      assertSame(nodeAt0, node.getEdgeRight(Angle.deg(-179.99)));
      assertNull(node.getEdgeRight(Angle.deg(180.0)));
      assertNull(node.getEdgeRight(Angle.deg(-180.0)));

      node.connect(nodeAt45, angle45);
      assertNull(node.getEdgeRight(angle45));
      assertSame(nodeAt0, node.getEdgeRight(Angle.deg(-0.1)));
      assertSame(nodeAt45, node.getEdgeRight(angle0));
      assertSame(nodeAt45, node.getEdgeRight(Angle.deg(44.9)));
      assertSame(nodeAt0, node.getEdgeRight(Angle.deg(-179.9)));
      assertSame(nodeAt0, node.getEdgeRight(Angle.deg(180.1)));
   }

   private static Node<String> newNode(double x, double y)
   {
      return new Node<>(new Point(x, y), null);
   }
   
   @Test
   public void testGetAngleLeft()
   {
      Node<String> node = newNode(0.0, 0.0);
      
      XZTestHelper.assertNPE(() -> {node.getAngleLeft(null);});
      
      Node<String> nodeAt0 = newNode(0.0, 1.0);
      Node<String> nodeAt45 = newNode(0.0, 2.0);
      Angle angle0 = Angle.deg(0.0);
      Angle deg45 = Angle.deg(45.0);

      assertNull(node.getEdgeLeft(angle0));

      node.connect(nodeAt0, angle0);
      assertNull(node.getAngleLeft(angle0));
      assertEquals(angle0, node.getAngleLeft(Angle.deg(-0.1)));
      assertEquals(angle0, node.getAngleLeft(Angle.deg(0.1)));
      assertEquals(angle0, node.getAngleLeft(Angle.deg(179.99)));
      assertEquals(angle0, node.getAngleLeft(Angle.deg(-180.0)));
      assertEquals(angle0, node.getAngleLeft(Angle.deg(180.0)));

      node.connect(nodeAt45, deg45);
      assertEquals(deg45, node.getAngleLeft(angle0));
      assertEquals(angle0, node.getAngleLeft(Angle.deg(0.1)));
      assertEquals(angle0, node.getAngleLeft(deg45));
      assertEquals(deg45, node.getAngleLeft(Angle.deg(45.1)));
      assertEquals(deg45, node.getAngleLeft(Angle.deg(224.9)));
      assertEquals(deg45, node.getAngleLeft(Angle.deg(-135.1)));
   }
   
   @Test
   public void testGetAngleRight()
   {
      Node<String> node = newNode(0.0, 0.0);

      XZTestHelper.assertNPE(() -> { node.getAngleRight(null); });

      Node<String> nodeAt0 = new Node<>(new Point(0.0, 1.0), null);
      Node<String> nodeAt45 = new Node<>(new Point(1.0, 1.0), null);
      Angle angle0 = Angle.deg(0.0);
      Angle angle45 = Angle.deg(45.0);

      assertNull(node.getAngleRight(angle0));

      node.connect(nodeAt0, angle0);
      assertNull(node.getAngleRight(angle0));
      assertEquals(angle0, node.getAngleRight(Angle.deg(0.1)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(-0.1)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(-179.99)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(180.0)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(-180.0)));

      node.connect(nodeAt45, angle45);
      assertEquals(angle0, node.getAngleRight(angle45));
      assertEquals(angle0, node.getAngleRight(Angle.deg(45.1)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(90.0)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(-0.1)));
      assertEquals(angle45, node.getAngleRight(angle0));
      assertEquals(angle45, node.getAngleRight(Angle.deg(44.9)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(-179.9)));
      assertEquals(angle0, node.getAngleRight(Angle.deg(180.1)));
   }
   
}

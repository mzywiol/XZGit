/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
/**
 *
 * @author eXistenZ
 */
public class FlatMapTest
{
   private static final double DELTA = 0.01;
   
   FlatMap<String> map;
   
   @Before
   public void setUp()
   {
      map = new FlatMap<>();
   }
   
   @Test
   public void testAddNode()
   {
      assertEquals(0, map.getNodeCount());
      
      // test: add first node
      Node<String> nodeA = map.addNode(2.0, 0.0, "A");
      assertEquals(1, map.getNodeCount());
      
      // test: node on the same coords replaces previous node
      nodeA = map.addNode(2.0, 0.0, "newA");
      assertEquals(1, map.getNodeCount());
      assertEquals("newA", map.xAxis.firstEntry().getValue().getData());
      
      // test: add second node
      Node<String> nodeB = map.addNode(0.0, 1.0, "B");
      assertEquals(2, map.getNodeCount());
      assertConnected(nodeB, nodeA);
      
      // test: add node on existing edge
      Node<String> nodeAB = map.addNode(1.0, 0.5, "AB");
      assertEquals(3, map.getNodeCount());
      assertConnected(nodeA, nodeAB);
      assertConnected(nodeB, nodeAB);
      assertNotConnected(nodeA, nodeB);
      
      // test: add third node
      Node<String> nodeC = map.addNode(3.0, 1.0, "C");
      assertEquals(4, map.getNodeCount());
      assertConnected(nodeA, nodeC);
      assertConnected(nodeB, nodeC);
      assertConnected(nodeAB, nodeC);
      
      // test: add fourth node
      Node<String> nodeD = map.addNode(4.0, 2.0, "D");
      assertEquals(5, map.getNodeCount());
      assertConnected(nodeC, nodeD);
      assertConnected(nodeB, nodeD);
      assertNotConnected(nodeA, nodeD);
      
      // test: add node just outside node B
      Node<String> nodeB1 = map.addNode(-0.25, 1.0, "B1");
      assertEquals(6, map.getNodeCount());
      assertConnected(nodeB1, nodeD);
      assertConnected(nodeB1, nodeB);
      assertNotConnected(nodeB1, nodeC);
      assertConnected(nodeB1, nodeAB);
      assertConnected(nodeB1, nodeA);
      
      // test: add node just outside node A
      Node<String> nodeA1 = map.addNode(2.0, -0.25, "A1");
      assertEquals(7, map.getNodeCount());
      assertConnected(nodeA1, nodeB1);
      assertConnected(nodeA1, nodeA);
      assertConnected(nodeA1, nodeC);
      assertConnected(nodeA1, nodeD);
      assertNotConnected(nodeA, nodeB1);
      assertConnected(nodeA1, nodeAB);
      
      // test: add node just outside node AB
      Node<String> nodeAB1 = map.addNode(0.75, 0.25, "AB1");
      assertEquals(8, map.getNodeCount());
      assertConnected(nodeAB1, nodeB1);
      assertConnected(nodeAB1, nodeA1);
      assertConnected(nodeAB, nodeAB1);
      assertNotConnected(nodeB1, nodeA1);
      
      // test: add a node inside a triangle
      Node<String> nodeE = map.addNode(1.75, 0.75, "E");
      assertEquals(9, map.getNodeCount());
      assertConnected(nodeE, nodeB);
      assertConnected(nodeE, nodeAB);
      assertConnected(nodeE, nodeC);
      assertConnected(nodeE, nodeA);
      assertNotConnected(nodeC, nodeAB);
      assertConnected(nodeE, nodeD);
      assertNotConnected(nodeB, nodeC);
      
      // test: replace a vertex of a triangle
      assertEquals(4, nodeC.getConnectedNodes().size());
      nodeC = map.addNode(nodeC.getCoords().getX(), nodeC.getCoords().getY(), "newC");
      assertEquals(9, map.getNodeCount());
      assertEquals(4, nodeC.getConnectedNodes().size());
      assertConnected(nodeC, nodeE);
      assertConnected(nodeC, nodeA);
      assertConnected(nodeC, nodeA1);
      assertConnected(nodeC, nodeD);
      
      // test: new node within existing edge
      Node<String> nodeBD = map.addNode(2.0, 1.5, "BD");
      assertEquals(10, map.getNodeCount());
      assertNotConnected(nodeB, nodeD);
      assertConnected(nodeBD, nodeB);
      assertConnected(nodeBD, nodeD);
      assertConnected(nodeBD, nodeB1);
      assertConnected(nodeBD, nodeE);
      assertConnected(nodeBD, nodeC);
      assertNotConnected(nodeE, nodeD);
      assertConnected(nodeBD, nodeAB);
      assertNotConnected(nodeB, nodeE);
   }

   private void assertConnected(Node<String> n1, Node<String> n2)
   {
      assertTrue(n1.isConnectedTo(n2));
      assertTrue(n2.isConnectedTo(n1));
   }
   
   private void assertNotConnected(Node<String> n1, Node<String> n2)
   {
      assertFalse(n1.isConnectedTo(n2));
      assertFalse(n2.isConnectedTo(n1));
   }

   @Test
   public void testSelectNode()
   {
      map.setSelectionTolerance(1.0);
      assertNull(map.selectNode(0.0, 0.0));
      
      map.addNode(0.0, 0.0, "A");
      assertEquals("A", map.selectNode(0.0, 0.0).getData());
      assertEquals("A", map.selectNode(0.1, 0.1).getData());
      assertEquals("A", map.selectNode(0.5, -0.8).getData());
      assertEquals("A", map.selectNode(-1.0, 0.8).getData());
      assertEquals("A", map.selectNode(1.0, -1.0).getData());
      assertNull(map.selectNode(1.1, 0.0));
      assertNull(map.selectNode(0.2, -1.000001));
      
      map.addNode(0.5, 0.25, "B");
      assertEquals("A", map.selectNode(0.0, 0.0).getData());
      assertEquals("A", map.selectNode(0.1, 0.1).getData());
      assertEquals("A", map.selectNode(0.25, 0.0).getData());
      assertEquals("A", map.selectNode(-0.5, 0.0).getData());
      assertEquals("A", map.selectNode(-0.5, 0.75).getData());
      assertEquals("A", map.selectNode(1.0, -1.0).getData());
      assertEquals("B", map.selectNode(0.75, -0.5).getData());
      assertEquals("B", map.selectNode(0.25, 0.25).getData());
      assertEquals("B", map.selectNode(0.25, 0.5).getData());
      assertEquals("B", map.selectNode(1.0, 1.0).getData());
      assertEquals("B", map.selectNode(0.0, 1.0).getData());
      assertEquals("B", map.selectNode(1.2, -0.5).getData());
      assertNull(map.selectNode(0.0, 1.3));
      assertNull(map.selectNode(1.5000001, 0.0));
   }
   
   @Test
   public void testFindNearestNode()
   {
      assertNull(map.findNearestNode(0.0, 0.0, false));

      map.addNode(0.0, 0.0, "A");
      assertNull(map.findNearestNode(0.0, 0.0, false));
      assertEquals("A", map.findNearestNode(0.0, 0.0, true).getData());
      assertEquals("A", map.findNearestNode(0.1, 0.1, false).getData());
      assertEquals("A", map.findNearestNode(-1.5, -12.8, false).getData());
      assertEquals("A", map.findNearestNode(-1000000000.0, 1000000000.0, false).getData());
      assertEquals("A", map.findNearestNode(1000000000.0, -1.0, false).getData());

      map.addNode(1.0, 1.0, "B");
      assertEquals("A", map.findNearestNode(0.0, 0.0, true).getData());
      assertEquals("B", map.findNearestNode(0.0, 0.0, false).getData());
      assertEquals("A", map.findNearestNode(1.0, 1.0, false).getData());
      assertEquals("B", map.findNearestNode(1.0, 1.0, true).getData());
      assertEquals("A", map.findNearestNode(0.1, 0.1, false).getData());
      assertEquals("A", map.findNearestNode(0.9, 0.0, false).getData());
      assertEquals("A", map.findNearestNode(-0.5, 0.0, false).getData());
      assertEquals("A", map.findNearestNode(-0.5, 1.0, false).getData());
      assertEquals("A", map.findNearestNode(1.0, -100.0, false).getData());
      assertEquals("B", map.findNearestNode(1.5, 0.0, false).getData());
      assertEquals("B", map.findNearestNode(0.6, 0.6, false).getData());
      assertEquals("B", map.findNearestNode(0.25, 1000, false).getData());
      
      map.addNode(100.0, 0.5, "C");
      map.addNode(-1.0, -100.0, "D");
      assertEquals("A", map.findNearestNode(-2.0, 0.75, false).getData());
   }

   private void checkNumberOfNodes(int num)
   {
      assertEquals(num, map.xAxis.size());
      assertEquals(num, map.yAxis.size());
   }
   
}

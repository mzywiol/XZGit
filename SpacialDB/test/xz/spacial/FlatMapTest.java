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
   public void testGetNodeLocation()
   {
      assertTrue(map.xAxis.isEmpty());
      assertTrue(map.yAxis.isEmpty());
      
      Node<String> nodeA = new Node(new Point(2.0, 0.0), "A");
      // test: no other nodes
      assertNull(map.getNodeLocation(nodeA.getCoords()));
      
      unofficialAdd(nodeA);
      // test: on the only node
      checkRelativeLocation(map.getNodeLocation(nodeA.getCoords()), true, nodeA);
      
      //test: not on the only node
      checkRelativeLocation(map.getNodeLocation(new Point(1000000.0, -1000000.0)), false, nodeA);
      
      Node<String> nodeB = new Node(new Point(0.0, 1.0), "B");
      checkRelativeLocation(map.getNodeLocation(nodeB.getCoords()), false, nodeA);
      unofficialAdd(nodeB);
      nodeA.connect(nodeB);
      // test: on an existing node
      checkRelativeLocation(map.getNodeLocation(nodeB.getCoords()), true, nodeB);
      
      // test: on the edge between only two nodes
      checkRelativeLocation(map.getNodeLocation(new Point(1.0, 0.5)), true, nodeB, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(1.5, 0.25)), true, nodeA, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(0.1, 0.95)), true, nodeB, nodeA);
      
      // test: only one node visible
      checkRelativeLocation(map.getNodeLocation(new Point(-0.5, 1.25)), false, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(3.0, -0.5)), false, nodeA);
      
      // test: only single edge visible
      checkRelativeLocation(map.getNodeLocation(new Point(0.0, 0.0)), false, nodeB, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(1.0, 0.0)), false, nodeA, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(0.0, 0.5)), false, nodeB, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(2.0, 1.0)), false, nodeA, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(5.0, 0.0)), false, nodeA, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(0.0, 100.0)), false, nodeB, nodeA);
      
      // creating only triangle
      Node<String> nodeC = new Node(new Point(3.0, 1.0), "C");
      checkRelativeLocation(map.getNodeLocation(nodeC.getCoords()), false, nodeA, nodeB);
      unofficialAdd(nodeC);
      nodeA.connect(nodeC);
      nodeB.connect(nodeC);
      
      // test: on the triangle vertex
      checkRelativeLocation(map.getNodeLocation(nodeA.getCoords()), true, nodeA);
      checkRelativeLocation(map.getNodeLocation(nodeB.getCoords()), true, nodeB);
      checkRelativeLocation(map.getNodeLocation(nodeC.getCoords()), true, nodeC);
      
      // test: on the triangle edge
      checkRelativeLocation(map.getNodeLocation(new Point(2.25, 0.25)), true, nodeA, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(2.75, 0.75)), true, nodeC, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(1.5, 0.25)), true, nodeA, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(0.5, 0.75)), true, nodeB, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(1.25, 1.0)), true, nodeB, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(2.0, 1.0)), true, nodeC, nodeB);
      
      // test: outside the triangle, one edge visible
      checkRelativeLocation(map.getNodeLocation(new Point(2.5, 0.0)), false, nodeA, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(2.75, 0.5)), false, nodeC, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(1.0, 0.0)), false, nodeA, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(-0.25, 1.0)), false, nodeB, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(0.0, 1.1)), false, nodeB, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(1.0, 1.01)), false, nodeB, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(2.0, 1.01)), false, nodeC, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(1.55, 1.01)), false, nodeC, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(1.51, 1.48)), false, nodeC, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(1.45, 1.01)), false, nodeB, nodeC);

      // test: outside the triangle, two edges visible
      checkRelativeLocation(map.getNodeLocation(new Point(2.0, -0.1)), false, nodeB, nodeA, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(3.5, 1.25)), false, nodeA, nodeC, nodeB);
      checkRelativeLocation(map.getNodeLocation(new Point(-0.5, 1.20)), false, nodeC, nodeB, nodeA);
      
      // test: inside the triangle
      checkRelativeLocation(map.getNodeLocation(new Point(1.0, 0.75)), true, nodeB, nodeC, nodeA);
      checkRelativeLocation(map.getNodeLocation(new Point(2.0, 0.25)), true, nodeA, nodeB, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(2.5, 0.75)), true, nodeC, nodeA, nodeB);
      
      // two triangles
      Node<String> nodeD = new Node(new Point(4.0, 2.5), "D");
      checkRelativeLocation(map.getNodeLocation(nodeD.getCoords()), false, nodeC, nodeB);
      unofficialAdd(nodeD);
      nodeC.connect(nodeD);
      nodeB.connect(nodeD);
      
      // test: inside the triangle
      checkRelativeLocation(map.getNodeLocation(new Point(1.51, 1.48)), true, nodeC, nodeB, nodeD);
      
      // test: on the triangles shared edge
      checkRelativeLocation(map.getNodeLocation(new Point(1.25, 1.0)), true, nodeB, nodeC);
      checkRelativeLocation(map.getNodeLocation(new Point(2.0, 1.0)), true, nodeC, nodeB);
      
      // test: on the triangles edge
      checkRelativeLocation(map.getNodeLocation(new Point(1.0, 1.375)), true, nodeB, nodeD);
      checkRelativeLocation(map.getNodeLocation(new Point(3.0, 2.125)), true, nodeD, nodeB);
      
      Node<String> nodeE = new Node(new Point(3.25, 1.5), "E");
      checkRelativeLocation(map.getNodeLocation(nodeE.getCoords()), true, nodeC, nodeB, nodeD);
      unofficialAdd(nodeE);
      nodeC.connect(nodeE);
      nodeB.connect(nodeE);
      nodeD.connect(nodeE);
      // test: inside the triangle
      checkRelativeLocation(map.getNodeLocation(new Point(1.51, 1.48)), true, nodeB, nodeD, nodeE);
      
      Node<String> nodeF = new Node(new Point(3.0, 1.25), "F");
      checkRelativeLocation(map.getNodeLocation(nodeF.getCoords()), true, nodeC, nodeB, nodeE);
      unofficialAdd(nodeF);
      nodeC.connect(nodeF);
      nodeB.connect(nodeF);
      nodeE.connect(nodeF);
      // test: inside the triangle
      checkRelativeLocation(map.getNodeLocation(new Point(1.51, 1.48)), true, nodeB, nodeD, nodeE);
      
      // test: on a shared edge
      checkRelativeLocation(map.getNodeLocation(new Point(1.5, 1.125)), true, nodeB, nodeF);
      
      Node<String> nodeG = new Node(new Point(1.0, 0.75), "G");
      checkRelativeLocation(map.getNodeLocation(nodeG.getCoords()), true, nodeB, nodeC, nodeA);
      unofficialAdd(nodeG);
      nodeC.connect(nodeG);
      nodeB.connect(nodeG);
      nodeA.connect(nodeG);
      
      Node<String> nodeH = new Node(new Point(0.75, 0.0), "H");
      checkRelativeLocation(map.getNodeLocation(nodeH.getCoords()), false, nodeA, nodeB);
      
      Node<String> nodeI = new Node(new Point(-0.25, 1.0), "I");
      checkRelativeLocation(map.getNodeLocation(nodeI.getCoords()), false, nodeD, nodeB, nodeA);
      unofficialAdd(nodeI);
      nodeA.connect(nodeI);
      nodeB.connect(nodeI);
      nodeD.connect(nodeI);
      
      checkRelativeLocation(map.getNodeLocation(nodeH.getCoords()), false, nodeA, nodeI);
      
      Node<String> nodeJ = new Node(new Point(2.25, -0.25), "J");
      checkRelativeLocation(map.getNodeLocation(nodeJ.getCoords()), false, nodeI, nodeA, nodeC);
      unofficialAdd(nodeJ);
      nodeI.connect(nodeJ);
      nodeA.connect(nodeJ);
      nodeC.connect(nodeJ);

      checkRelativeLocation(map.getNodeLocation(nodeH.getCoords()), false, nodeI, nodeJ);

   }

   private void unofficialAdd(Node<String> nodeA)
   {
      map.xAxis.put(nodeA.getCoords(), nodeA);
      map.yAxis.put(nodeA.getCoords(), nodeA);
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
      assertConnected(nodeB1, nodeAB);
      assertConnected(nodeB1, nodeA);
      
      // test: add node just outside node A
      Node<String> nodeA1 = map.addNode(2.0, -0.25, "A1");
      assertEquals(7, map.getNodeCount());
      assertConnected(nodeA1, nodeB1);
      assertConnected(nodeA1, nodeA);
      assertConnected(nodeA1, nodeC);
      assertConnected(nodeA1, nodeD);
      
      // test: add node just outside node AB
      Node<String> nodeAB1 = map.addNode(0.75, 0.25, "AB1");
      assertEquals(8, map.getNodeCount());
      assertConnected(nodeAB1, nodeB1);
      assertConnected(nodeAB1, nodeA1);
      assertNotConnected(nodeAB1, nodeAB1);
      
      // test: add a node inside a triangle
      Node<String> nodeE = map.addNode(1.0, 0.75, "E");
      assertEquals(9, map.getNodeCount());
      assertConnected(nodeE, nodeB);
      assertConnected(nodeE, nodeAB);
      assertConnected(nodeE, nodeC);
      
      // test: replace a vertex of a triangle
      assertEquals(6, nodeC.getConnectedNodes().size());
      nodeC = map.addNode(nodeC.getCoords().getX(), nodeC.getCoords().getY(), "newC");
      assertEquals(9, map.getNodeCount());
      assertEquals(6, nodeC.getConnectedNodes().size());
      assertConnected(nodeC, nodeB);
      assertConnected(nodeC, nodeE);
      assertConnected(nodeC, nodeAB);
      assertConnected(nodeC, nodeA);
      assertConnected(nodeC, nodeA1);
      assertConnected(nodeC, nodeD);
      
      // test: new node within existing edge
      Node<String> nodeBC = map.addNode(1.5, 1.0, "BC");
      assertEquals(10, map.getNodeCount());
      assertNotConnected(nodeB, nodeC);
      assertConnected(nodeBC, nodeB);
      assertConnected(nodeBC, nodeC);
      assertConnected(nodeBC, nodeD);
      assertConnected(nodeBC, nodeE);
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

   
//   @Test
//   public void testSelectNode()
//   {
//      map.setSelectionTolerance(1.0);
//      assertNull(map.selectNode(0.0, 0.0));
//      
//      map.addNode(0.0, 0.0, "A");
//      assertEquals("A", map.selectNode(0.0, 0.0).getData());
//      assertEquals("A", map.selectNode(0.1, 0.1).getData());
//      assertEquals("A", map.selectNode(0.5, -0.8).getData());
//      assertEquals("A", map.selectNode(-1.0, 0.8).getData());
//      assertEquals("A", map.selectNode(1.0, -1.0).getData());
//      assertNull(map.selectNode(1.1, 0.0));
//      assertNull(map.selectNode(0.2, -1.000001));
//      
//      map.addNode(0.5, 0.25, "B");
//      assertEquals("A", map.selectNode(0.0, 0.0).getData());
//      assertEquals("A", map.selectNode(0.1, 0.1).getData());
//      assertEquals("A", map.selectNode(0.25, 0.0).getData());
//      assertEquals("A", map.selectNode(-0.5, 0.0).getData());
//      assertEquals("A", map.selectNode(-0.5, 0.75).getData());
//      assertEquals("A", map.selectNode(1.0, -1.0).getData());
//      assertEquals("B", map.selectNode(0.75, -0.5).getData());
//      assertEquals("B", map.selectNode(0.25, 0.25).getData());
//      assertEquals("B", map.selectNode(0.25, 0.5).getData());
//      assertEquals("B", map.selectNode(1.0, 1.0).getData());
//      assertEquals("B", map.selectNode(0.0, 1.0).getData());
//      assertEquals("B", map.selectNode(1.2, -0.5).getData());
//      assertNull(map.selectNode(0.0, 1.3));
//      assertNull(map.selectNode(1.5000001, 0.0));
//   }
   
//   @Test
//   public void testFindNearestNode()
//   {
//      assertNull(map.findNearestNode(0.0, 0.0, false));
//
//      map.addNode(0.0, 0.0, "A");
//      assertNull(map.findNearestNode(0.0, 0.0, false));
//      assertEquals("A", map.findNearestNode(0.0, 0.0, true).getData());
//      assertEquals("A", map.findNearestNode(0.1, 0.1, false).getData());
//      assertEquals("A", map.findNearestNode(-1.5, -12.8, false).getData());
//      assertEquals("A", map.findNearestNode(-1000000000.0, 1000000000.0, false).getData());
//      assertEquals("A", map.findNearestNode(1000000000.0, -1.0, false).getData());
//
//      map.addNode(1.0, 1.0, "B");
//      assertEquals("A", map.findNearestNode(0.0, 0.0, true).getData());
//      assertEquals("B", map.findNearestNode(0.0, 0.0, false).getData());
//      assertEquals("A", map.findNearestNode(1.0, 1.0, false).getData());
//      assertEquals("B", map.findNearestNode(1.0, 1.0, true).getData());
//      assertEquals("A", map.findNearestNode(0.1, 0.1, false).getData());
//      assertEquals("A", map.findNearestNode(0.9, 0.0, false).getData());
//      assertEquals("A", map.findNearestNode(-0.5, 0.0, false).getData());
//      assertEquals("A", map.findNearestNode(-0.5, 1.0, false).getData());
//      assertEquals("A", map.findNearestNode(1.0, -100.0, false).getData());
//      assertEquals("B", map.findNearestNode(1.5, 0.0, false).getData());
//      assertEquals("B", map.findNearestNode(0.6, 0.6, false).getData());
//      assertEquals("B", map.findNearestNode(0.25, 1000, false).getData());
//      
//      map.addNode(100.0, 0.5, "C");
//      map.addNode(-1.0, -100.0, "D");
//      assertEquals("A", map.findNearestNode(-2.0, 0.75, false).getData());
//   }

   private void checkNumberOfNodes(int num)
   {
      assertEquals(num, map.xAxis.size());
      assertEquals(num, map.yAxis.size());
   }

   private void checkTriangle(Node<String> nodeA, Node<String> nodeB, Node<String> nodeC)
   {
      assertTrue(nodeA.isConnectedTo(nodeB));
      assertTrue(nodeA.isConnectedTo(nodeC));
      assertTrue(nodeB.isConnectedTo(nodeA));
      assertTrue(nodeB.isConnectedTo(nodeC));
      assertTrue(nodeC.isConnectedTo(nodeA));
      assertTrue(nodeC.isConnectedTo(nodeB));
   }

   private void checkRelativeLocation(FlatMap.RelativeLocation actual, boolean within, Node... expected) //todoxz move to XZTestUtils
   {
      assertEquals(within, actual.within);
      assertEquals(expected.length, actual.nodes.length);
      for (int i = 0; i < expected.length; i++)
         assertEquals(expected[i], actual.nodes[i]);
   }
   
}

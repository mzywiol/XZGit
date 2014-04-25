/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Comparator;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author eXistenZ
 */
public class FlatMap<T>
{
   final TreeMap<Point, Node<T>> xAxis, yAxis;
   private double selectionTolerance = 0.01;
   MapEdgeListener<T> edgeListener;
   MapNodeListener<T> nodeListener;

   public FlatMap()
   {
      this.xAxis = new TreeMap<>(Point.X_AXIS_COMPARATOR);
      this.yAxis = new TreeMap<>(Point.Y_AXIS_COMPARATOR);
   }
   
   FlatMap.RelativeLocation<T> getNodeLocation(Point p)
   {
      if (xAxis.size() == 0)
         return null;
      
      if (xAxis.size() == 1)
      {
         Node<T> onlyNode = xAxis.firstEntry().getValue();
         return new RelativeLocation<>(onlyNode.getCoords().equals(p), onlyNode);
      }
      
      Node<T> nearest = findNearestNode(p.getX(), p.getY(), true);
      Point pNearest = nearest.getCoords();
      
      // are we at an existing node?
      if (pNearest.equals(p))
         return new RelativeLocation<>(true, nearest);// return this existing node
      
      return findTriangleWereIn(p, nearest);      
   }

   private RelativeLocation<T> findTriangleWereIn(Point p, Node<T> nearest)
   {
      // are we between existing nodes?
      Point pNearest = nearest.getCoords();
      Angle dNearestThis = direction(pNearest, p);
      Node<T> opposite = nearest.getEdgeAt(dNearestThis);
      if (opposite != null)
         return new RelativeLocation<>(true, nearest, opposite); // return both ends of the edge, starting with the nearest
      
      // find edges to left and right
      Node<T> left = nearest.getEdgeLeft(dNearestThis);
      Node<T> right = nearest.getEdgeRight(dNearestThis);

      if (left == null)
      {
         // is this the only node in sight?
         if (right == null)
            return new RelativeLocation<> (false, nearest); // return that node
         else
            return new RelativeLocation<> (false, nearest, right); // return both ends of the edge, starting with the nearest
      }
      else
      {
         if (right == null)
            return new RelativeLocation<>(false, nearest, left); // return both ends of the edge, starting with the nearest
         else
         {  // left and right are not null
            Point pLeft = left.getCoords();
            Point pRight = right.getCoords();

            // are we within a triangle?
            if (p.isWithinTriangle(pNearest, pLeft, pRight))
            {
               return new RelativeLocation<>(true, nearest, left, right);
            }
            else
            {
               // triangle is on the same side as point
               if (nearest.connectionAngle(right).isToRightOf(nearest.connectionAngle(left)))
               {
                  nearest = (distance(left, p) <= distance(right, p)) ? left : right;
                  return findTriangleWereIn(p, nearest);
               }
               else
               { // triangle is on the other side, we are outside of the whole graph
                  return new RelativeLocation<>(false, right, nearest, left);
               }
            }
         }
      }
   }
   
   public Node<T> addNode(double x, double y, T data)
   {
      Point coords = new Point(x, y);
      
      RelativeLocation<T> loc = getNodeLocation(coords);
      
      Node<T> node = new Node<>(coords, data);
      
      if (loc != null)
      {
         switch(loc.nodes.length)
         {
            case 1:
               if (loc.within) // existing node
               {
                  loc.nodes[0].setData(data);
                  fireNodeEvent(MapNodeEvent.EventType.UPDATED, loc.nodes[0]);
                  return loc.nodes[0];
               } 
               else // only one other node visible
                  connectNodeToNodes(node, loc.nodes);
               break;
            case 2:
               connectNodeToNodes(node, loc.nodes); 
               if (loc.within) // existing edge
               {
                  Angle connectionAngle = loc.nodes[0].connectionAngle(node);
                  Node<T> left = loc.nodes[0].getEdgeLeft(connectionAngle);
                  if (left != null && left.isConnectedTo(loc.nodes[1]))
                  {
                     connectNodes(node, left);
                     fixEdge(loc.nodes[0], left);
                     fixEdge(loc.nodes[1], left);
                  }
                  Node<T> right = loc.nodes[0].getEdgeRight(connectionAngle);
                  if (right != null && right.isConnectedTo(loc.nodes[1]))
                  {
                     connectNodes(node, right);
                     fixEdge(loc.nodes[0], right);
                     fixEdge(loc.nodes[1], right);
                  }
               }
               else // only one closest edge, but may be chained with other visible edges
                  connectToOtherVisibleNodes(loc.nodes[0], loc.nodes[1], node);
               break;
            case 3:
               connectNodeToNodes(node, loc.nodes);
               if (loc.within) // inside a triangle
               {
                  fixEdge(loc.nodes[0], loc.nodes[1]);
                  fixEdge(loc.nodes[1], loc.nodes[2]);
                  fixEdge(loc.nodes[2], loc.nodes[0]);
               }
               else // outside a triangle, two closest edges in both sides, but may be chained with other visible
               {
                  connectToOtherVisibleNodes(loc.nodes[1], loc.nodes[0], node);
                  connectToOtherVisibleNodes(loc.nodes[1], loc.nodes[2], node);
               }
               break;
         }
      }
      
      doAddNode(node);
      return node;
   }

   private void doAddNode(Node<T> node)
   {
      xAxis.put(node.getCoords(), node);
      yAxis.put(node.getCoords(), node);
      fireNodeEvent(MapNodeEvent.EventType.ADDED, node);
   }

   private void connectNodes(Node<T> node, Node<T> other)
   {
      node.connect(other);
      fireEdgeEvent(MapEdgeEvent.EventType.ADDED, node, other);
   }
   
   private void fixEdge(Node<T> from, Node<T> to)
   {
      try
      {
         Angle edge = from.connectionAngle(to);
         Node<T> left = from.getEdgeLeft(edge);
         Node<T> right = from.getEdgeRight(edge);
         if (left.isConnectedTo(to) && left.isConnectedTo(to))
         {
            double fromAngle = from.connectionAngle(left).diffDeg(from.connectionAngle(right));
            double toAngle = to.connectionAngle(right).diffDeg(to.connectionAngle(left));
            if (from.connectionAngle(right).isToRightOf(from.connectionAngle(left)) 
                  && to.connectionAngle(left).isToRightOf(to.connectionAngle(right))
                  && distance(from, to.getCoords()) > distance(left, right.getCoords()))
            {
               assert !left.isConnectedTo(right) : "fixEdge: Crossed edges.";
               disconnectNodes(from, to);
               connectNodes(left, right);
            }
         }
      }
      catch (NullPointerException e)
      {
      }      
   }

   private void disconnectNodes(Node<T> from, Node<T> to)
   {
      from.disconnect(to);
      fireEdgeEvent(MapEdgeEvent.EventType.REMOVED, from, to);
   }

   private void connectToOtherVisibleNodes(Node<T> previous, Node<T> current, Node<T> source)
   {
      boolean left = direction(previous, current).isToLeftOf(previous.connectionAngle(source));
      while(true)
      {
         fixEdge(previous, current);
         Node<T> nextNode = findNextVisibleNode(current, source, left);
         if (nextNode == null)
            break;
         connectNodes(source, nextNode);
         previous = current;
         current = nextNode;
      }
   }

   private Node<T> findNextVisibleNode(Node<T> current, Node<T> source, boolean left)
   {
      Angle dirCurrentToSource = current.connectionAngle(source);
      Angle angleToNext = left ? current.getAngleLeft(dirCurrentToSource)
                               : current.getAngleRight(dirCurrentToSource);
      double diffDeg = angleToNext.diffDeg(dirCurrentToSource);
      return (left && diffDeg > 0.0 || !left && diffDeg < 0.0) ? current.getEdgeAt(angleToNext) : null;
   }

   private void connectNodeToNodes(Node<T> node, Node<T>[] nodes)
   {
      for (Node<T> n : nodes)
         connectNodes(node, n);
   }
   
   public int getNodeCount()
   {
      return xAxis.size();
   }

   private Angle direction(Node<T> from, Node<T> to)
   {
      Angle dir = from.connectionAngle(to);
      if (dir == null)
         dir = direction(from.getCoords(), to.getCoords());
      return dir;
   }
   
   private Angle direction(Point from, Point to)
   {
      return from.direction(to);
   }
   
   private double distance(Node<T> from, Point to)
   {
      return from.getCoords().distance(to);
   }
   
   public Node<T> selectNode(double x, double y)
   {
      return findNearestInRange(new Point(x, y), selectionTolerance, true);
   }
   
   public Node<T> findNearestNode(double x, double y, boolean inclusive)
   {
      if (xAxis.isEmpty())
         return null;
      
      Point target = new Point(x, y);

      if (xAxis.size() == 1)
      {
         Node<T> only = xAxis.firstEntry().getValue();
         return (!inclusive && only.getCoords().equals(target)) ? null : only;
      }
      
      double minDistance = Double.MAX_VALUE;
      Point point;
      if ((point = xAxis.higherKey(target)) != null)
         minDistance = Math.min(minDistance, point.distance(target));
      if ((point = xAxis.lowerKey(target)) != null)
         minDistance = Math.min(minDistance, point.distance(target));
      if ((point = yAxis.higherKey(target)) != null)
         minDistance = Math.min(minDistance, point.distance(target));
      if ((point = yAxis.lowerKey(target)) != null)
         minDistance = Math.min(minDistance, point.distance(target));
      
      return findNearestInRange(target, minDistance, inclusive);
   }
   
   public Node<T> findNearestNode(Node<T> to)
   {
      return findNearestNode(to.getX(), to.getY(), false);
   }

   private Node<T> findNearestInRange(Point center, double range, boolean includeCenter)
   {
      Point topLeft = center.translate(-range, -range);
      Point bottomRight = center.translate(range, range);
      NavigableMap<Point, Node<T>> xNeighbours = xAxis.subMap(topLeft, true, bottomRight, true);
      NavigableMap<Point, Node<T>> yNeighbours = yAxis.subMap(topLeft, true, bottomRight, true);

      Set<Node<T>> intersection = new HashSet<>(xNeighbours.values());
      intersection.retainAll(yNeighbours.values());
      if (!includeCenter)
         intersection.removeIf(node -> node.getCoords().equals(center));
      if (intersection.isEmpty())
         return null;

      return intersection.stream().min(new ByDistanceToTarget(center)).get();
   }

   public double getSelectionTolerance()
   {
      return selectionTolerance;
   }

   public void setSelectionTolerance(double selectionTolerance)
   {
      this.selectionTolerance = selectionTolerance;
   }

   class ByDistanceToTarget implements Comparator<Node<T>>
   {
      final Node<T> target;

      public ByDistanceToTarget(Point coords)
      {
         this.target = new Node<>(coords, null);
      }
      
      @Override
      public int compare(Node<T> o1, Node<T> o2)
      {
         return Double.compare(o1.getCoords().distance(target.getCoords()), 
                               o2.getCoords().distance(target.getCoords()));
      }      
   }
   
   /******* LISTENERS LOGIC *******/
   
   public void addEdgeListener(MapEdgeListener<T> edgeListener)
   {
      this.edgeListener = edgeListener;
   }
   
   private void fireEdgeEvent(MapEdgeEvent.EventType type, Node<T> node1, Node<T> node2)
   {
      if (edgeListener != null)
      {
         MapEdgeEvent<T> event = new MapEdgeEvent<>(node1, node2, type);
         switch(type)
         {
            case ADDED:
               edgeListener.edgeAdded(event);
               break;
            case REMOVED:
               edgeListener.edgeRemoved(event);
               break;
         }
      }
   }

   public void removeEdgeListener()
   {
      this.edgeListener = null;
   }

   public void addNodeListener(MapNodeListener<T> nodeListener)
   {
      this.nodeListener = nodeListener;
   }

   private void fireNodeEvent(MapNodeEvent.EventType type, Node<T> node)
   {
      if (nodeListener != null)
      {
         MapNodeEvent<T> event = new MapNodeEvent<>(node, type);
         switch (type)
         {
            case ADDED:
               nodeListener.nodeAdded(event);
               break;
            case UPDATED:
               nodeListener.nodeUpdated(event);
               break;
            case REMOVED:
               nodeListener.nodeRemoved(event);
               break;
         }
      }
   }

   public void removeNodeListener()
   {
      this.nodeListener = null;
   }
   
   static class RelativeLocation<T>
   {
      Node<T>[] nodes;
      boolean within;

      public RelativeLocation(boolean within, Node<T>... nodes)
      {
         this.nodes = nodes;
         this.within = within;
      }
   }
}

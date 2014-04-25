/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author eXistenZ
 */
public class FlatMap<T>
{
   TreeMap<Point, Node<T>> xAxis, yAxis;
   private double selectionTolerance = 0.01;

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
      Node<T> node = new Node<>(coords, data);
      
      RelativeLocation<T> loc = getNodeLocation(coords);
      
      if (loc != null)
      {
         switch(loc.nodes.length)
         {
            case 1:
               if (!loc.within)
                  connectNodeToNodes(node, loc.nodes);
               else
               {
                  loc.nodes[0].setData(data);
                  return loc.nodes[0];
               }
               break;
            case 2:
               connectNodeToNodes(node, loc.nodes);
               if (loc.within)
               {
                  Angle connectionAngle = loc.nodes[0].connectionAngle(node);
                  Node<T> left = loc.nodes[0].getEdgeLeft(connectionAngle);
                  if (left != null && left.isConnectedTo(loc.nodes[1]))
                     connectNodes(node, left);
                  Node<T> right = loc.nodes[0].getEdgeRight(connectionAngle);
                  if (right != null && right.isConnectedTo(loc.nodes[1]))
                     connectNodes(node, right);
               }
               else
                  connectToOtherVisibleNodes(loc.nodes[0], loc.nodes[1], node);
               break;
            case 3:
               connectNodeToNodes(node, loc.nodes);
               if (!loc.within)
               {
                  connectToOtherVisibleNodes(loc.nodes[1], loc.nodes[0], node);
                  connectToOtherVisibleNodes(loc.nodes[1], loc.nodes[2], node);
               }
               break;
         }
      }
      
      xAxis.put(node.getCoords(), node);
      yAxis.put(node.getCoords(), node);
      return node;
   }

   private void connectNodes(Node<T> node, Node<T> other)
   {
      node.connect(other);
   }

   private void connectToOtherVisibleNodes(Node<T> startingNode, Node<T> firstVisible, Node<T> inRelationTo)
   {
      boolean left = startingNode.connectionAngle(firstVisible)
            .isToLeftOf(startingNode.connectionAngle(inRelationTo));
      while(true)
      {
         Node<T> nextNode = findNextVisibleNode(startingNode, firstVisible, inRelationTo, left);
         if (nextNode == null)
            break;
         connectNodes(inRelationTo, nextNode);
         startingNode = firstVisible;
         firstVisible = nextNode;
      }
   }

   private Node<T> findNextVisibleNode(Node<T> startingNode, Node<T> visible, Node<T> inRelationTo, boolean left)
   {
      Angle angleToNext = left ? visible.getAngleLeft(visible.connectionAngle(inRelationTo))
                               : visible.getAngleRight(visible.connectionAngle(inRelationTo));
      Angle relationAngle = inRelationTo.connectionAngle(visible);
      double diffDeg = relationAngle.diffDeg(angleToNext);
      return (left && diffDeg > 0.0 || !left && diffDeg < 0.0) ? visible.getEdgeAt(angleToNext) : null;
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
   
   private void addIfNotNull(Set<Node<T>> set, Map.Entry<Point, Node<T>> entry)
   {
      if (entry != null)
         set.add(entry.getValue());
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

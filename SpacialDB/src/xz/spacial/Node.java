/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xz.spacial;

import java.util.Collection;
import java.util.Objects;

/**
 * A map graph node with given data.
 * 
 * @author eXistenZ
 */
public class Node<T>
{
   private final Point coords;
   private T data;
   CylindricalTreeMap<Node<T>> edges = new CylindricalTreeMap<>();

   public Node(Point coords, T data)
   {
      this.coords = coords;
      this.data = data;
   }

   public double getX()
   {
      return coords.getX();
   }

   public double getY()
   {
      return coords.getY();
   }

   public Point getCoords()
   {
      return coords;
   }

   public T getData()
   {
      return data;
   }

   void setData(T data)
   {
      this.data = data;
   }

   /**
    * Returns Node connected to this one at given <code>at</code> Angle or <code>null</code> if there is no such Node.
    *
    * @param from Angle of connection
    * @return a Node connected to this one at given <code>at</code> Angle or <code>null</code> if there is no such Node
    * @throws NullPointerException if <code>at</code> Angle is null.
    */
   public Node<T> getEdgeAt(Angle at)
   {
      if (at == null)
         throw new NullPointerException("null Angle");

      return edges.get(at);
   }

   /**
    * Returns first Angle connecting to another Node to the left of given <code>from</code> Angle.
    *
    * @param from starting Angle
    * @return first Angle connecting to another Node to the left of given <code>from</code> Angle, or null if this Node
    * is not connected to any other Node on an angle different from given.
    * @throws NullPointerException if <code>from</code> Angle is null.
    */
   public Angle getAngleLeft(Angle from)
   {
      if (from == null)
      {
         throw new NullPointerException("null Angle");
      }

      return edges.lowerKey(from);
   }

   /**
    * Return the first Node this Node is connected to within a 180 degrees arc to the left of given <code>from</code>
    * Angle (exclusive) and ending on the angle opposite to given (exclusive), or <code>null</code> if there is no such
    * Node.
    *
    * @param from Angle to start from
    * @return first Node connected to the left of given Angle.
    * @throws NullPointerException if <code>from</code> Angle is null.
    */
   public Node<T> getEdgeLeft(Angle from)
   {
      Angle edgeAngle = getAngleLeft(from);
      if (edgeAngle == null || !edgeAngle.isToLeftOf(from))
         return null;

      return edges.get(edgeAngle);
   }

   /**
    * Returns first Angle connecting to another Node to the right of given <code>from</code> Angle.
    *
    * @param from starting Angle
    * @return first Angle connecting to another Node to the right of given <code>from</code> Angle, or null if this Node
    * is not connected to any other Node on an angle different from given.
    * @throws NullPointerException if <code>from</code> Angle is null.
    */
   public Angle getAngleRight(Angle from)
   {
      if (from == null)
         throw new NullPointerException("null Angle");

      return edges.higherKey(from);
   }

   /**
    * Return the first Node this Node is connected to within a 180 degrees arc to the right of given <code>from</code>
    * Angle (exclusive) and ending on the angle opposite to given (exclusive), or <code>null</code> if there is no such
    * Node.
    *
    * @param from Angle to start from
    * @return first Node connected to the right of given Angle.
    * @throws NullPointerException if <code>from</code> Angle is null.
    */
   public Node<T> getEdgeRight(Angle from)
   {
      Angle edgeAngle = getAngleRight(from);
      if (edgeAngle == null || !edgeAngle.isToRightOf(from))
         return null;

      return edges.get(edgeAngle);
   }

   /**
    * Connects this Node to the <code>other</code> Node at <code>angle</code> Angle.
    * @param other Node to connect to
    * @param angle Angle of connection
    * @throws NullPointerException if either <code>other</code> Node or <code>angle</code> Angle is null.
    */
   void connect(Node<T> other, Angle angle)
   {
      if (this.coords.equals(other.getCoords()))
         return;
      
      other.edges.put(angle.opposite(), this);
      edges.put(angle, other);
   }

   /**
    * Connects this Node to the <code>other</code> Node. Does nothing if both Nodes are at the same coords.
    *
    * @param other Node to connect to
    * @throws NullPointerException if <code>other</code> Node is null.
    */
   void connect(Node<T> other)
   {
      connect(other, coords.direction(other.getCoords()));
   }

   /**
    * Checks if this Node is connected to the <code>other</code> Node.
    * @param other Node to test connection with.
    * @return true if this Node is connected to the <code>other</code> Node, false otherwise.
    * @throws NullPointerException if <code>other</code> Node is null.
    */
   boolean isConnectedTo(Node<T> other)
   {
      if (other == null)
         throw new NullPointerException("Other Node is null.");

      return edges.containsValue(other);
   }

   /**
    * Returns the angle at which this Node is connected to <code>other</code> node or <code>null</code> if nodes are not
    * connected.
    *
    * @param other Node this Node is connected to
    * @return direction from this Node to the <code>other</code>
    * @throws NullPointerException if <code>other</code> Node is null.
    */
   Angle connectionAngle(Node<T> other)
   {
      if (other == null)
         throw new NullPointerException("Other Node is null.");
      
      if (!isConnectedTo(other))
         return null;

      return edges.entrySet().stream().filter(e -> e.getValue().equals(other)).findFirst().get().getKey();
   }

   /**
    * todoxz javadoc
    * @param angle
    * @return 
    */
   boolean disconnect(Angle angle)
   {
      Node<T> other = edges.remove(angle);
      if (other != null)
      {
         other.edges.remove(angle.opposite());
         return true;
      }
      return false;
   }

   /**
    * todoxz javadoc
    * @param other
    * @return 
    */
   boolean disconnect(Node<T> other)
   {
      return disconnect(connectionAngle(other));
   }

   /**
    * todoxz javadoc
    * @return 
    */
   Collection<Node<T>> getConnectedNodes()
   {
      return edges.values();
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 47 * hash + Objects.hashCode(this.coords);
      hash = 47 * hash + Objects.hashCode(this.data);
      return hash;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final Node<?> other = (Node<?>) obj;
      if (!Objects.equals(this.coords, other.coords))
      {
         return false;
      }
      return Objects.equals(this.data, other.data);
   }

   @Override
   public String toString()
   {
      return data.toString();
   }
}

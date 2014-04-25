/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Collection;
import java.util.Objects;

/**
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
   
   public Node<T> getEdgeAt(Angle at)
   {
      return edges.get(at);
   }
   
   public Angle getAngleLeft(Angle from)
   {
      return edges.lowerKey(from);
   }
      
   public Node<T> getEdgeLeft(Angle from)
   {
      Angle edgeAngle = getAngleLeft(from);
      if (edgeAngle == null || !edgeAngle.isToLeftOf(from))
         return null;
      
      return edges.get(edgeAngle);
   }
   
   public Angle getAngleRight(Angle from)
   {
      return edges.higherKey(from);
   }

   public Node<T> getEdgeRight(Angle from)
   {
      Angle edgeAngle = getAngleRight(from);
      if (edgeAngle == null || !edgeAngle.isToRightOf(from))
         return null;

      return edges.get(edgeAngle);
   }
   
   void connect(Node<T> other, Angle angle)
   {
      edges.put(angle, other);
      other.edges.put(angle.opposite(), this);
   }
   
   void connect(Node<T> other)
   {
      connect(other, coords.direction(other.getCoords()));
   }

   boolean isConnectedTo(Node<T> other)
   {
      return edges.containsValue(other);
   }

   Angle connectionAngle(Node<T> other)
   {
      if (!isConnectedTo(other))
         return null;
      
      return edges.entrySet().stream().filter(e -> e.getValue().equals(other)).findFirst().get().getKey();
   }
   
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
   
   boolean disconnect(Node<T> other)
   {
      return disconnect(connectionAngle(other));
   }

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

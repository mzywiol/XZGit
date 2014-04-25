/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

/**
 *
 * @author eXistenZ
 */
public class MapEdgeEvent<T>
{
   private final Node<T> node1, node2;
   private final EventType eventType;

   public MapEdgeEvent(Node<T> node1, Node<T> node2, EventType eventType)
   {
      this.node1 = node1;
      this.node2 = node2;
      this.eventType = eventType;
   }

   public Node<T> getNode1()
   {
      return node1;
   }

   public Node<T> getNode2()
   {
      return node2;
   }

   public EventType getEventType()
   {
      return eventType;
   }
   
   public static enum EventType { ADDED, REMOVED }
}

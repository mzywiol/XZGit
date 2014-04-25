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
class MapNodeEvent<T>
{
   final Node<T> node;
   final EventType eventType;

   public MapNodeEvent(Node<T> node, EventType eventType)
   {
      this.node = node;
      this.eventType = eventType;
   }

   public Node<T> getNode()
   {
      return node;
   }

   public EventType getEventType()
   {
      return eventType;
   }
   
   public static enum EventType { ADDED, REMOVED, UPDATED }
}

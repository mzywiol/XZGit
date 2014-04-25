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
public interface MapNodeListener<T>
{
   void nodeAdded(MapNodeEvent<T> e);
   
   void nodeRemoved(MapNodeEvent<T> e);
   
   void nodeUpdated(MapNodeEvent<T> e);
}

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
public interface MapEdgeListener<T>
{
   void edgeAdded(MapEdgeEvent<T> e);
   
   void edgeRemoved(MapEdgeEvent<T> e);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 *
 * @author eXistenZ
 */
public class ContinuousTreeMap<K, V> extends TreeMap<K, V>
{
  
   @Override
   public K higherKey(K key)
   {
      K higher = super.higherKey(key);
      if (higher == null && !isEmpty())
      {
         K first = firstKey();
         if (!first.equals(key))
            higher = first;
      }
      return higher;
   }

   @Override
   public Map.Entry<K, V> higherEntry(K key)
   {
      Map.Entry<K, V> higher = super.higherEntry(key);
      if (higher == null && !isEmpty())
      {
         Map.Entry<K, V> first = firstEntry();
         if (!first.getKey().equals(key))
            higher = first;
      }
      return higher;
   }

   @Override
   public K ceilingKey(K key)
   {
      K ceiling = super.ceilingKey(key);
      return ceiling == null ? (isEmpty() ? null : firstKey()) : ceiling;
   }

   @Override
   public Map.Entry<K, V> ceilingEntry(K key)
   {
      Map.Entry<K, V> ceiling = super.ceilingEntry(key);
      return ceiling == null ? firstEntry() : ceiling;
   }

   @Override
   public K floorKey(K key)
   {
      K floor = super.floorKey(key);
      return floor == null ? (isEmpty() ? null : lastKey()) : floor;
   }

   @Override
   public Map.Entry<K, V> floorEntry(K key)
   {
      Map.Entry<K, V> floor = super.floorEntry(key);
      return floor == null ? lastEntry() : floor;
   }

   @Override
   public K lowerKey(K key)
   {
      K lower = super.lowerKey(key);
      if (lower == null && !isEmpty())
      {
         K last = lastKey();
         if (!last.equals(key))
            lower = last;
      }
      return lower;
   }

   @Override
   public Map.Entry<K, V> lowerEntry(K key)
   {
      Map.Entry<K, V> lower = super.lowerEntry(key);
      if (lower == null && !isEmpty())
      {
         Map.Entry<K, V> last = lastEntry();
         if (!last.getKey().equals(key))
            lower = last;
      }
      return lower;
   }
   
   /**
    * Returns iterator that iterates circularly over all elements of the
    * map, starting with one with key immediately lower than given and in 
    * descending order. When it reaches first key, it circles around
    * to last key and continues iterating down to the key immediately higher
    * than given.
    * 
    * @param key Key below which to start iterating from
    */
   public Iterator<Map.Entry<K, V>> lowerIterator(K key)
   {
      NavigableMap<K, V> reversed = this.descendingMap();
      return new SequentialIterator<>(reversed.tailMap(key, false).entrySet(), reversed.headMap(key).entrySet());
   }
   
   /**
    * Returns iterator that iterates circularly over all elements of the
    * map, starting with one with key immediately higher than given and in
    * ascending order. When it reaches last key, it circles around to first
    * key and continues iterating up to the key immediately lower than
    * given.
    *
    * @param key Key above which to start iterating from
    */
   public Iterator<Map.Entry<K, V>> higherIterator(K key)
   {
      return new SequentialIterator<>(this.tailMap(key, false).entrySet(), this.headMap(key).entrySet());
   }
    
}

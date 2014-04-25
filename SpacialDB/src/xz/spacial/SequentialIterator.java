/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author eXistenZ
 */
public class SequentialIterator<E> implements Iterator<E>
{
   private final List<Iterator<E>> iterators;
   private final Iterator<Iterator<E>> outerIterator;
   private Iterator<E> innerIterator;

   public SequentialIterator(Iterator<E>... iterators)
   {
      this.iterators = Arrays.asList(iterators);
      this.outerIterator = this.iterators.iterator();
      innerIterator = outerIterator.next();
   }
   
   public SequentialIterator(Iterable<E>... collections)
   {
      this(Arrays.stream(collections).map(c -> c.iterator()).toArray(Iterator[]::new));      
   }

   @Override
   public boolean hasNext()
   {
      boolean currentHasNext = innerIterator.hasNext();
      while (!currentHasNext)
      {
         if (!outerIterator.hasNext())
            break;
         
         innerIterator = outerIterator.next();
         currentHasNext = innerIterator.hasNext();
      }
      
      return currentHasNext;
   }

   @Override
   public E next()
   {
      while (!innerIterator.hasNext())
      {
         if (!outerIterator.hasNext())
            break;

         innerIterator = outerIterator.next();
      }
      
      return innerIterator.next();
   }
}

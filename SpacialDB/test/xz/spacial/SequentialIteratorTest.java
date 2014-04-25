/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author eXistenZ
 */
public class SequentialIteratorTest
{
   
   public SequentialIteratorTest()
   {
   }
   
   @Test
   public void testNext()
   {
      List<String> numbers = Arrays.asList("1", "2", "3");
      List<String> letters = Arrays.asList("A", "B");
      List<String> empty = new ArrayList<String>();
      SequentialIterator<String> seq = new SequentialIterator<>(numbers.iterator());
      checkIteration(seq, "1", "2", "3");
      
      seq = new SequentialIterator<>(letters.iterator(), numbers.iterator());
      checkIteration(seq, "A", "B", "1", "2", "3");
      
      seq = new SequentialIterator<>(numbers, empty, letters);
      checkIteration(seq, "1", "2", "3", "A", "B");
      
      seq = new SequentialIterator<>(empty, letters, empty, empty, letters, empty);
      checkIteration(seq, "A", "B", "A", "B");
      
      seq = new SequentialIterator<>(empty, empty, empty);
      checkIteration(seq);
   }

   private void checkIteration(SequentialIterator<String> seq, String... elements)
   {
      for (String string : elements)
      {
         assertTrue("hasNext false for " + string, seq.hasNext());
         assertEquals("not equal for " + string, string, seq.next());
      }
      assertFalse("has more elements at the end", seq.hasNext());
      try
      {
         seq.next();
         fail("Should throw NoSuchElementException");
      } catch (NoSuchElementException ex) {}
   }
   
}

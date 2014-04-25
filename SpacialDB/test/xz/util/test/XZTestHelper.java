/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.util.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author eXistenZ
 */
public class XZTestHelper
{
   public static void assertNPE(VoidAction action)
   {
      try
      {
         action.perform();
         fail("Should throw NullPointerException.");
      } catch (NullPointerException e) { /* expected */ }
   }
   
   public static void assertException(VoidAction action, Class<? extends Exception> exceptionClass)
   {
      try
      {
         action.perform();
         fail("Should throw " + exceptionClass.getName() + ".");
      } catch (Exception e)
      { 
         assertTrue(exceptionClass.isAssignableFrom(e.getClass())); 
      }
   }
}

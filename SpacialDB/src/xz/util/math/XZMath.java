/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.util.math;

/**
 *
 * @author eXistenZ
 */
public class XZMath
{
   /**
    * Normalizes the given <code>number</code> to be within given <code>range</code>, starting inclusively at given 
    * <code>start</code> value. If range is positive, <code>number</code> will be normalized to be within 
    * [start, start+range). If negative, <code>number</code> will end up in (start+range, start]. <code>range</code> 
    * cannot be 0.0.
    * @param number value to be normalized
    * @param start starting value included in normalization range
    * @param range range of normalization
    * @return normalized <code>number</code> 
    * @throws ArithmeticException if <code>range</code> is 0.0
    */
   public static double normalize(double number, double start, double range)
   {
      if (range == 0.0)
         throw new ArithmeticException("Division by zero: normalize range cannot be 0.0");

      boolean up = range > 0.0;
      double end = start + range;
      range = Math.abs(range);
      int relation = compareToRange(number, start, true, end, false);

      if (relation == 0) // nothing to normalize here, move along
         return number;

      double distance = Math.abs((up ^ relation > 0) ? (end - number) : (start - number));
      double periodsAway = Math.floor(distance / range) * -relation;

      number += periodsAway * range;

      if (number == end)
         number = start;

      return number;
   }
      
   /**
    * Returns -1, 0 or 1, depending on given <code>number</code>'s relation to given range, between <code>left</code> and <code>right</code>.
    * Boolean parameters <code>leftInclusive</code> and <code>rightInclusive</code> define if range's ends should be included in range.
    * Value 0 is returned if <code>number</code> is within this range, -1 if it's below and 1 if above.
    * 
    * @param number tested number 
    * @param left left end of range
    * @param leftInclusive whether left end of range is included in range
    * @param right right end of range
    * @param rightInclusive whether right end of range is included in range
    * @return -1, 1 or 0, depending on <code>number</code> being less than <code>left</code>, greater than <code>right</code> 
    * or between those values
    */
   public static int compareToRange(double number, double left, boolean leftInclusive, double right, boolean rightInclusive)
   {
      if (left > right)
         return compareToRange(number, right, rightInclusive, left, leftInclusive);
      
      if (number < left)
         return -1;
      
      if (number > right)
         return 1;

      if (number == left)
         return leftInclusive ? 0 : -1;

      if (number == right)
         return rightInclusive ? 0 : 1;

      return 0;
   }
   
   /**
    * Returns true if given <code>number</code> is within given range, between <code>left</code>
    * and <code>right</code>. Boolean parameters <code>leftInclusive</code> and <code>rightInclusive</code> define if
    * range's ends should be included in range. 
    *
    * @param number tested number
    * @param left left end of range
    * @param leftInclusive whether left end of range is included in range
    * @param right right end of range
    * @param rightInclusive whether right end of range is included in range
    * @return true if <code>number</code> is between <code>left</code> and <code>right</code>
    */
   public static boolean within(double number, double left, boolean leftInclusive, double right, boolean rightInclusive)
   {
      return compareToRange(number, left, leftInclusive, right, rightInclusive) == 0;
   }
   
   /**
    * Returns true if given <code>number</code> is between <code>left</code> and <code>right</code>, exclusive.
    *
    * @param number tested number
    * @param left left end of range
    * @param right right end of range
    * @return true if <code>number</code> is between <code>left</code> and <code>right</code>
    */
   public static boolean between(double number, double left, double right)
   {
      return compareToRange(number, left, false, right, false) == 0;
   }   

   /**
    * Returns <code>i</code> raised to the <code>pow</code> power, or 1 if pow is negative.
    * @param i value
    * @param pow power
    * @return i raised to the pow power
    */
   public static long pow(int i, int pow)
   {
      long res = 1;
      for (int j = 0; j < pow; j++)
         res *= i;
      return res;
   }
}

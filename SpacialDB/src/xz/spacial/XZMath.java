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
public class XZMath
{
   public static double normalize(double angle, double start, double range)
   {
      if (range == 0.0)
         throw new ArithmeticException("Division by zero: normalize range cannot be 0.0");

      boolean up = range > 0.0;
      double end = start + range;
      range = Math.abs(range);
      int relation = compareToRange(angle, start, true, end, false);

      if (relation == 0) // nothing to normalize here, move along
         return angle;

      double distance = Math.abs((up ^ relation > 0) ? (end - angle) : (start - angle));
      double periodsAway = Math.floor(distance / range) * -relation;

      angle += periodsAway * range;

      if (angle == end)
      {
         angle = start;
      }

      return angle;
   }
      
   public static int compareToRange(double number, double left, boolean leftInclusive, double right, boolean rightInclusive)
   {
      if (left > right)
         return compareToRange(number, right, rightInclusive, left, leftInclusive);
      
      if (number == left)
         return leftInclusive ? 0 : -1;
      
      if (number == right)
         return rightInclusive ? 0 : 1;
      
      if (number < left)
         return -1;
      
      if (number > right)
         return 1;
      
      return 0;
   }
   
   public static boolean within(double number, double left, boolean leftInclusive, double right, boolean rightInclusive)
   {
      return compareToRange(number, left, leftInclusive, right, rightInclusive) == 0;
   }
   
   public static boolean between(double number, double left, double right)
   {
      return compareToRange(number, left, false, right, false) == 0;
   }   

   public static long pow(int i, int pow)
   {
      long res = 1;
      for (int j = 0; j < pow; j++)
         res *= i;
      return res;
   }
}

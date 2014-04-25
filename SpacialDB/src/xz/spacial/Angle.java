/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.TreeMap;

/**
 *
 * @author eXistenZ
 */
public final class Angle implements Comparable<Angle>
{
   private static final int PRECISION = 10; // 2 ^ 10 ~= 10 ^ 3
   
   public static final double MAX_VALUE = 180.0;
   public static final double MIN_VALUE = -180.0;
   public static final double HALF_ANGLE = 180.0;
   private static final TreeMap<Long, Angle> cachedAngles = new TreeMap<>();

   private final double degrees;
   private final Long cacheKey;
   
   public static Angle deg(double value)
   {
      double angle = normalize(value);
      Long cacheKey = toCacheKey(angle);
      Angle a = cachedAngles.get(cacheKey);
      if (a == null)
         cachedAngles.put(cacheKey, a = new Angle(cacheKey));
      
      return a;
   }
   
   private static Long toCacheKey(double normalizedAngle)
   {
      return Math.round(Math.scalb(normalizedAngle, PRECISION));
   }

   public static double normalize(double angle)
   {
      return XZMath.normalize(angle, 180.0, -360.0);
   }
   
   private Angle(Long cacheKey)
   {
      this.degrees = Math.scalb(cacheKey.doubleValue(), -PRECISION);
      this.cacheKey = cacheKey;
   }

   public double deg()
   {
      return degrees;
   }
   
   public Angle opposite()
   {
      return Angle.deg(degrees + HALF_ANGLE);
   }

   public boolean isBetween(Angle from, Angle to)
   {
      double vFrom = from.deg();
      double vTo = to.deg();
      
      if (vTo > vFrom)
      {
         return (vFrom < degrees) && (degrees < vTo);
      } else
      {
         return (vFrom < degrees && degrees <= Angle.MAX_VALUE) || (Angle.MIN_VALUE < degrees && degrees < vTo);
      }
   }
   
   public double diffDeg(Angle to)
   {
      return normalize(to.deg() - degrees);
   }
   
   public boolean isToLeftOf(Angle of)
   {
      double diff = diffDeg(of);
      return diff > 0.0 && diff < 180.0;
   }
   
   public boolean isToRightOf(Angle of)
   {
      return diffDeg(of) < 0.0;
   }
   
   @Override
   public int hashCode()
   {
      return Long.hashCode(cacheKey);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final Angle other = (Angle) obj;
      return cacheKey == other.cacheKey;
   }

   @Override
   public int compareTo(Angle o)
   {
      return Double.compare(degrees, o.deg());
   }

   @Override
   public String toString()
   {
      return Double.toString(degrees);
   }
   
   /*
    * Should only be used in tests.
    */
   static void _clearCache()
   {
      cachedAngles.clear();
   }
}
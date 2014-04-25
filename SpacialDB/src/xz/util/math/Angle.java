/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.util.math;

import java.util.TreeMap;

/**
 * Class representing an Angle on Cartesian plane, in degrees range (-180.0, 180.0].
 * 
 * @author eXistenZ
 */
public final class Angle implements Comparable<Angle>
{
   private static final int PRECISION = 10; // 2 ^ 10 ~= 10 ^ 3
   
   public static final double MAX_VALUE = 180.0;
   public static final double MIN_VALUE = -180.0;
   public static final double HALF_ANGLE = 180.0;
   public static final double RIGHT_ANGLE = 90.0;
   private static final TreeMap<Long, Angle> cachedAngles = new TreeMap<>();

   private final double degrees;
   private final Long cacheKey;
   
   //todoxz: add support for radians
   
   /**
    * Returns an angle equal to given <code>value</code> in degrees.
    * 
    * @param value degrees of the angle
    * @return the angle
    */
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

   /**
    * Normalizes the given <code>degrees</code> value, i.e. returns the degrees value within (-180, 180] bounds
    * that is equal to given degrees.
    * @param degrees given value
    * @return normalized degrees value
    */
   public static double normalize(double degrees)
   {
      return XZMath.normalize(degrees, 180.0, -360.0);
   }
   
   private Angle(Long cacheKey)
   {
      this.degrees = Math.scalb(cacheKey.doubleValue(), -PRECISION);
      this.cacheKey = cacheKey;
   }

   /**
    * Returns value of this Angle in degrees.
    * @return value of this Angle in degrees
    */
   public double deg()
   {
      return degrees;
   }
   
   /**
    * Returns an Angle opposite to this.
    * @return an Angle opposite to this
    */
   public Angle opposite()
   {
      return Angle.deg(degrees + HALF_ANGLE);
   }

   /**
    * Returns true if this angle's value is within an arc starting from <code>from</code> Angle and ending at <code>to</code> Angle.
    * 
    * @param from starting angle of the arc
    * @param to ending angle of the arc
    * @return true if this angle is within the arc
    */
   public boolean isBetween(Angle from, Angle to)
   {
      double vFrom = from.deg();
      double vTo = to.deg();
      
      if (vTo > vFrom)
      {
         return (vFrom < degrees) && (degrees < vTo);
      } 
      else
      {
         return (vFrom < degrees && degrees <= Angle.MAX_VALUE) || (Angle.MIN_VALUE < degrees && degrees < vTo);
      }
   }
   
   /**
    * Returns the difference between this and <code>to</code> Angles, in degrees. Specifically, it returns number of degrees
    * this angle needs to rotate to be equal to <code>to</code> Angle. The sign of return value signifies the direction 
    * of that rotation - if it's positive, <code>to</code> Angle is to the right of this, if negative - it's to the left.
    * @param to Angle to count the difference to
    * @return difference between two Angles
    * @throws NullPointerException if <code>to</code> Angle is null
    */
   public double diffDeg(Angle to)
   {
      return normalize(to.deg() - degrees);
   }
   
   /**
    * Returns true if this Angle is in left arc of the <code>of</code> Angle, i.e. if difference between them 
    * is positive and under 180 degrees.
    * @param of the other Angle
    * @return true if this Angle is in left arc of the <code>of</code> Angle
    * @throws NullPointerException if <code>to</code> Angle is null
    */
   public boolean isToLeftOf(Angle of)
   {
      double diff = diffDeg(of);
      return diff > 0.0 && diff < 180.0;
   }
   
   /**
    * Returns true if this Angle is in right arc of the <code>of</code> Angle, i.e. if difference between them is
    * negative and above -180 degrees.
    *
    * @param of the other Angle
    * @return true if this Angle is in right arc of the <code>of</code> Angle
    * @throws NullPointerException if <code>to</code> Angle is null
    */
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

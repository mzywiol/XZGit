/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.util.math;

import java.util.Comparator;

/**
 *
 * @author eXistenZ
 */
public class Point
{
   //todoxz: add subclass Vector for vector operations, move some of the methods there
   
   private final double x, y;

   public Point(double x, double y)
   {
      this.x = x;
      this.y = y;
   }

   public double getX()
   {
      return x;
   }

   public double getY()
   {
      return y;
   }
   
   /**
    * Returns this Point translated by given coordinates.
    * @param x x translation coordinate
    * @param y y translation coordinate
    * @return this Point translated by given coordinates
    */
   public Point translate(double x, double y)
   {
      return new Point(this.x + x, this.y + y);
   }
   
   /**
    * Returns difference between this point and <code>to</code>.
    * @param to vector target
    * @return difference between this and target Points
    */
   public Point vector(Point to)
   {
      return new Point(to.x - this.x, to.y - this.y);
   }
   
   /**
    * Returns third coordinate of vector that is a cross product of this and <code>other</code> Points.
    * @param other other point for cross product
    * @return third coordinate of vector that is a cross product of this and <code>other</code> Points
    */
   public double crossProduct(Point other)
   {
      return this.x * other.y - other.x * this.y;
   }
   
   /**
    * Return a Point that is in the middle between this and <code>other</code> Points.
    * @param other other Point
    * @return midpoint between this and <code>other</code> Points
    */
   public Point midpoint(Point other)
   {
      return new Point((this.x + other.x) / 2, (this.y + other.y) / 2);
   }
   
   /**
    * Returns true if this Point lies on a line between Points <code>a</code> and <code>b</code>.
    * @param a point
    * @param b point
    * @return true if this Point lies on a line between Points <code>a</code> and <code>b</code>
    */
   public boolean isBetween(Point a, Point b)
   {
      return (a.vector(b).isParallelTo(a.vector(this)) && XZMath.between(this.x, a.x, b.x));
   }
   
   /**
    * Returns true if this Point lies strictly within (i.e. not on edge) of a triangle with <code>a</code>, 
    * <code>b</code> and <code>c</code> vertices.
    * @param a vertex of a triangle
    * @param b vertex of a triangle
    * @param c vertex of a triangle
    * @return true if this Point lies strictly within the triangle
    */
   public boolean isWithinTriangle(Point a, Point b, Point c)
   {
      Point vAB = a.vector(b);
      Point vBC = b.vector(c);
      Point vCA = c.vector(a);
      Point vAP = a.vector(this);
      Point vBP = b.vector(this);
      Point vCP = c.vector(this);
      double signumB = Math.signum(vBC.crossProduct(vBP));
      return (signumB == Math.signum(vAB.crossProduct(vAP)) && signumB == Math.signum(vCA.crossProduct(vCP)));
   }
   
   //todoxz move to Vector subclass
   public boolean isToLeftOf(Point v)
   {
      return v.crossProduct(this) < 0.0;
   }
   
   //todoxz move to Vector subclass
   public boolean isToRightOf(Point v)
   {
      return v.crossProduct(this) > 0.0;
   }
   
   //todoxz move to Vector subclass
   public boolean isParallelTo(Point v)
   {
      return v.crossProduct(this) == 0.0;
   }
   
   /**
    * Returns an angle of the line between this and <code>to</code> Points.
    * @param to target Point
    * @return angle of the line between this and <code>to</code> Points
    */
   public Angle direction(Point to)
   {
      return Angle.deg(Math.toDegrees(Math.atan2(to.getX() - x, to.getY() - y)));
   }

   /**
    * Returns distance on Cartesian plane between this and <code>to</code> Points.
    * @param to target point
    * @return distance on Cartesian plane between this and <code>to</code> Points
    */
   public double distance(Point to)
   {
      return Math.hypot(to.getX() - x, to.getY() - y);
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 47 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
      hash = 47 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
      return hash;
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
      final Point other = (Point) obj;
      if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
      {
         return false;
      }
      return Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y);
   }

   @Override
   public String toString()
   {
      return "(" + x + ", " + y + ")";
   }
   
   public static final Comparator<Point> X_AXIS_COMPARATOR = (o1, o2) ->
   {
      int compareX = Double.compare(o1.x, o2.x);
      if (compareX == 0)
      {
         return Double.compare(o1.y, o2.y);
      }

      return compareX;
   };

   public static final Comparator<Point> Y_AXIS_COMPARATOR = (o1, o2) ->
   {
      int compareY = Double.compare(o1.y, o2.y);
      if (compareY == 0)
      {
         return Double.compare(o1.x, o2.x);
      }

      return compareY;
   };


}

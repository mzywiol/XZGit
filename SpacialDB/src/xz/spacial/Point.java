/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import java.util.Comparator;

/**
 *
 * @author eXistenZ
 */
public class Point
{
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
   
   public Point translate(double x, double y)
   {
      return new Point(this.x + x, this.y + y);
   }
   
   public Point vector(Point to)
   {
      return new Point(to.x - this.x, to.y - this.y);
   }
   
   public double crossProduct(Point other)
   {
      return this.x * other.y - other.x * this.y;
   }
   
   public Point midpoint(Point other)
   {
      return new Point((this.x + other.x) / 2, (this.y + other.y) / 2);
   }
   
   public boolean isBetween(Point a, Point b)
   {
      return (a.vector(b).isParallelTo(a.vector(this)) && XZMath.between(this.x, a.x, b.x));
   }
   
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
   
   public boolean isToLeftOf(Point v)
   {
      return v.crossProduct(this) < 0.0;
   }
   
   public boolean isToRightOf(Point v)
   {
      return v.crossProduct(this) > 0.0;
   }
   
   public boolean isParallelTo(Point v)
   {
      return v.crossProduct(this) == 0.0;
   }
   
   public Angle direction(Point to)
   {
      return Angle.deg(Math.toDegrees(Math.atan2(to.getX() - x, to.getY() - y)));
   }

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

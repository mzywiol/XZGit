/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xz.spacial;

import xz.util.math.Angle;


public class CylindricalTreeMap<T> extends ContinuousTreeMap<Angle, T>
{
   @Override
   public T get(Object key)
   {
      return (key instanceof Double) ? super.get(Angle.deg((Double)key)) : super.get(key);
   }
   
}

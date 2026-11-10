package com.larpclient;

import net.minecraft.class_243;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface gq {
   void blend(ti.d var1);

   default void solid(boolean var1) {
   }

   default Vector3f viewer(Matrix4f var1, Vector3f var2) {
      return var2.set(0.0F, -2.5F, -3.0F);
   }

   void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5);

   void vertex(double var1, double var3, double var5, int var7);

   void ribbon(class_243 var1, class_243 var2, double var3, int var5, int var6);
}

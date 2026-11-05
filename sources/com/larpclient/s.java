package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public interface s {
   void blend(pf.x var1);

   default void solid(boolean var1) {
   }

   void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5);

   void vertex(double var1, double var3, double var5, int var7);

   void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6);
}

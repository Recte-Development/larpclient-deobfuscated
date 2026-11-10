package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class vp {
   public static Matrix4f D(Matrix4f var0, b2.m var1, float var2, float var3, float var4, float var5, float var6, float[] var7, float var8) {
      var0.identity().translate(var2, var3, var4).rotateY(-var5);
      if (var8 > 0.0F) {
         b2.F(var0, var7, var8);
      }

      return var0.scale(var6 * 1.8F / var1.height());
   }

   public static void K(PoseStack var0, b2.m var1, Vector3f var2, boolean var3, float var4, boolean var5, float var6, float[] var7, float var8) {
      float var9 = 1.8F / var1.height() * var4;
      float var10 = var6 * 0.9375F;
      Vector3f var11 = new Vector3f(0.0F, var5 ? var6 * 0.125F : 0.0F, 0.0F);
      if (var5 && var8 > 0.0F) {
         b2.F(new Matrix4f(), var7, var8).invert().transformDirection(var11);
      }

      float var12 = var2.x * var9 + var11.x;
      float var13 = var2.y * var9 + var11.y;
      float var14 = var2.z * var9 + var11.z;
      var0.translate(var12 / var10, 1.501 - var13 / var10, -var14 / var10);
      var0.translate((var3 ? 1 : -1) / 16.0F, -0.625F, 0.125F);
   }

   private vp() {
   }
}

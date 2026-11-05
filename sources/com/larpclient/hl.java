package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class hl {
   public static Matrix4f Q(Matrix4f var0, _y.w var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      float var8 = 1.8F / var1.height() * var6;
      return var0.identity()
         .translate(var2, var3, var4)
         .rotateY(-var5)
         .scale(var8, var8 * (var7 ? 0.8333334F : 1.0F), var8)
         .translate(0.0F, -var1.minimumY(), 0.0F);
   }

   public static Vector3f G(boolean var0) {
      return var0 ? new Vector3f(-0.035F, -0.1025F, -0.03F) : new Vector3f(0.066F, -0.105F, -0.053F);
   }

   public static Vector3f G(_y.w var0, Vector3f var1, float var2, boolean var3) {
      float var4 = 1.8F / var0.height() * var2;
      return new Vector3f(var1.x * var4, (var1.y - var0.minimumY()) * var4 * (var3 ? 0.8333334F : 1.0F), var1.z * var4);
   }

   public static Vector3f y(_y.w var0, boolean var1, float var2, boolean var3) {
      return G(var0, G(var1), var2, var3);
   }

   public static void S(PoseStack var0, _y.w var1, boolean var2, float var3, boolean var4, float var5) {
      R(var0, var1, var2, G(var2), var3, var4, var5);
   }

   public static void R(PoseStack var0, _y.w var1, boolean var2, Vector3f var3, float var4, boolean var5, float var6) {
      Vector3f var7 = G(var1, var3, var4, var5);
      float var8 = var6 * 0.9375F;
      var0.translate(var7.x / var8, 1.501 - (var7.y + (var5 ? var6 * 0.125F : 0.0F)) / var8, -var7.z / var8);
      var0.translate((var2 ? 1 : -1) / 16.0F, -0.625F, 0.125F);
   }

   private hl() {
   }
}

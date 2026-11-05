package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class c_ {
   public static final double v = 0.08;
   private c1 Y;
   private int p;
   private int r;
   private float b;
   private double R = Double.NaN;
   private double c = Double.NEGATIVE_INFINITY;
   private Matrix4f[] h;
   private Matrix4f[] w;

   public void T() {
      this.Y = null;
      this.h = this.w = null;
      this.R = Double.NaN;
      this.c = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] u(c1 var1, z5 var2, tt.x var3, int var4, float var5, double var6) {
      int var8 = this.Y == var1
            && this.p == var4
            && this.b == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.R)
            && !(var6 < this.R)
            && !(var6 - this.R > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.T();
         this.Y = var1;
         this.p = var4;
         this.b = var5;
         this.h = X(var1.parts().size() + 2);
         this.w = X(this.h.length);
      } else if (var3.r || var6 - this.c < 0.08 && var3.U != this.r) {
         for (int var9 = 0; var9 < this.h.length; var9++) {
            this.w[var9].set(this.h[var9]);
         }

         this.c = var6;
      }

      wp var15 = wp.sample(var1, var2, var3.U, var3.K, var4, var5);
      this.h[0].set(var15.weapon());
      this.h[1].set(var15.arm());
      PoseStack var10 = new PoseStack();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.pushPose();
         var1.parts().get(var11).transform(var10, var3.U, var3.K, var4);
         this.h[var11 + 2].set(var10.last().pose());
         var10.popPose();
      }

      float var16 = (float)Math.clamp((var6 - this.c) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.h[0]).invert().mul(this.h[1]);
         Matrix4f var13 = new Matrix4f(this.w[0]).invert().mul(this.w[1]);
         G(var13, var12, var16);
         G(this.w[0], this.h[0], var16);
         this.h[1].set(this.h[0]).mul(var12);

         for (int var14 = 2; var14 < this.h.length; var14++) {
            G(this.w[var14], this.h[var14], var16);
         }
      }

      this.R = var6;
      this.r = var3.U;
      return this.h;
   }

   private static Matrix4f[] X(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }

   private static void G(Matrix4f var0, Matrix4f var1, float var2) {
      Vector3f var3 = var0.getTranslation(new Vector3f()).lerp(var1.getTranslation(new Vector3f()), var2);
      Quaternionf var4 = var0.getUnnormalizedRotation(new Quaternionf()).normalize().slerp(var1.getUnnormalizedRotation(new Quaternionf()).normalize(), var2);
      Vector3f var5 = var0.getScale(new Vector3f()).lerp(var1.getScale(new Vector3f()), var2);
      var1.translationRotateScale(var3, var4, var5);
   }
}

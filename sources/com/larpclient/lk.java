package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class lk {
   public static final double K = 0.08;
   private _r L;
   private int H;
   private int m;
   private float j;
   private double o = Double.NaN;
   private double a = Double.NEGATIVE_INFINITY;
   private Matrix4f[] D;
   private Matrix4f[] T;

   public void r() {
      this.L = null;
      this.D = this.T = null;
      this.o = Double.NaN;
      this.a = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] X(_r var1, yi var2, xh.n var3, int var4, float var5, double var6) {
      int var8 = this.L == var1
            && this.H == var4
            && this.j == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.o)
            && !(var6 < this.o)
            && !(var6 - this.o > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.r();
         this.L = var1;
         this.H = var4;
         this.j = var5;
         this.D = E(var1.parts().size() + 2);
         this.T = E(this.D.length);
      } else if (var3.K || var6 - this.a < 0.08 && var3.k != this.m) {
         for (int var9 = 0; var9 < this.D.length; var9++) {
            this.T[var9].set(this.D[var9]);
         }

         this.a = var6;
      }

      e2 var15 = e2.sample(var1, var2, var3.k, var3.r, var4, var5);
      this.D[0].set(var15.weapon());
      this.D[1].set(var15.arm());
      PoseStack var10 = new PoseStack();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.pushPose();
         var1.parts().get(var11).transform(var10, var3.k, var3.r, var4);
         this.D[var11 + 2].set(var10.last().pose());
         var10.popPose();
      }

      float var16 = (float)Math.clamp((var6 - this.a) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.D[0]).invert().mul(this.D[1]);
         Matrix4f var13 = new Matrix4f(this.T[0]).invert().mul(this.T[1]);
         e(var13, var12, var16);
         e(this.T[0], this.D[0], var16);
         this.D[1].set(this.D[0]).mul(var12);

         for (int var14 = 2; var14 < this.D.length; var14++) {
            e(this.T[var14], this.D[var14], var16);
         }
      }

      this.o = var6;
      this.m = var3.k;
      return this.D;
   }

   private static Matrix4f[] E(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }

   private static void e(Matrix4f var0, Matrix4f var1, float var2) {
      Vector3f var3 = var0.getTranslation(new Vector3f()).lerp(var1.getTranslation(new Vector3f()), var2);
      Quaternionf var4 = var0.getUnnormalizedRotation(new Quaternionf()).normalize().slerp(var1.getUnnormalizedRotation(new Quaternionf()).normalize(), var2);
      Vector3f var5 = var0.getScale(new Vector3f()).lerp(var1.getScale(new Vector3f()), var2);
      var1.translationRotateScale(var3, var4, var5);
   }
}

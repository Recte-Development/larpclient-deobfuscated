package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class z1 {
   public static final double l = 0.08;
   private m8 w;
   private int d;
   private int Z;
   private float H;
   private double h = Double.NaN;
   private double v = Double.NEGATIVE_INFINITY;
   private Matrix4f[] f;
   private Matrix4f[] L;

   public void Y() {
      this.w = null;
      this.f = this.L = null;
      this.h = Double.NaN;
      this.v = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] I(m8 var1, tc var2, gx.f var3, int var4, float var5, double var6) {
      int var8 = this.w == var1
            && this.d == var4
            && this.H == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.h)
            && !(var6 < this.h)
            && !(var6 - this.h > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.Y();
         this.w = var1;
         this.d = var4;
         this.H = var5;
         this.f = B(var1.parts().size() + 2);
         this.L = B(this.f.length);
      } else if (var3.H || var6 - this.v < 0.08 && var3.C != this.Z) {
         for (int var9 = 0; var9 < this.f.length; var9++) {
            this.L[var9].set(this.f[var9]);
         }

         this.v = var6;
      }

      li var15 = li.sample(var1, var2, var3.C, var3.t, var4, var5);
      this.f[0].set(var15.weapon());
      this.f[1].set(var15.arm());
      PoseStack var10 = new PoseStack();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.pushPose();
         var1.parts().get(var11).transform(var10, var3.C, var3.t, var4);
         this.f[var11 + 2].set(var10.last().pose());
         var10.popPose();
      }

      float var16 = (float)Math.clamp((var6 - this.v) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.f[0]).invert().mul(this.f[1]);
         Matrix4f var13 = new Matrix4f(this.L[0]).invert().mul(this.L[1]);
         G(var13, var12, var16);
         G(this.L[0], this.f[0], var16);
         this.f[1].set(this.f[0]).mul(var12);

         for (int var14 = 2; var14 < this.f.length; var14++) {
            G(this.L[var14], this.f[var14], var16);
         }
      }

      this.h = var6;
      this.Z = var3.C;
      return this.f;
   }

   private static Matrix4f[] B(int var0) {
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

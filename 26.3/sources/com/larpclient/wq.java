package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class wq {
   public static final double Q = 0.08;
   private w9 U;
   private int D;
   private int z;
   private float v;
   private double B = Double.NaN;
   private double Y = Double.NEGATIVE_INFINITY;
   private Matrix4f[] h;
   private Matrix4f[] w;

   public void w() {
      this.U = null;
      this.h = this.w = null;
      this.B = Double.NaN;
      this.Y = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] h(w9 var1, s_ var2, i7.g var3, int var4, float var5, double var6) {
      int var8 = this.U == var1
            && this.D == var4
            && this.v == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.B)
            && !(var6 < this.B)
            && !(var6 - this.B > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.w();
         this.U = var1;
         this.D = var4;
         this.v = var5;
         this.h = y(var1.parts().size() + 2);
         this.w = y(this.h.length);
      } else if (var3.S || var6 - this.Y < 0.08 && var3.B != this.z) {
         for (int var9 = 0; var9 < this.h.length; var9++) {
            this.w[var9].set(this.h[var9]);
         }

         this.Y = var6;
      }

      b_ var15 = b_.sample(var1, var2, var3.B, var3.v, var4, var5);
      this.h[0].set(var15.weapon());
      this.h[1].set(var15.arm());
      PoseStack var10 = new PoseStack();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.pushPose();
         var1.parts().get(var11).transform(var10, var3.B, var3.v, var4);
         this.h[var11 + 2].set(var10.last().pose());
         var10.popPose();
      }

      float var16 = (float)Math.clamp((var6 - this.Y) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.h[0]).invert().mul(this.h[1]);
         Matrix4f var13 = new Matrix4f(this.w[0]).invert().mul(this.w[1]);
         j(var13, var12, var16);
         j(this.w[0], this.h[0], var16);
         this.h[1].set(this.h[0]).mul(var12);

         for (int var14 = 2; var14 < this.h.length; var14++) {
            j(this.w[var14], this.h[var14], var16);
         }
      }

      this.B = var6;
      this.z = var3.B;
      return this.h;
   }

   private static Matrix4f[] y(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }

   private static void j(Matrix4f var0, Matrix4f var1, float var2) {
      Vector3f var3 = var0.getTranslation(new Vector3f()).lerp(var1.getTranslation(new Vector3f()), var2);
      Quaternionf var4 = var0.getUnnormalizedRotation(new Quaternionf()).normalize().slerp(var1.getUnnormalizedRotation(new Quaternionf()).normalize(), var2);
      Vector3f var5 = var0.getScale(new Vector3f()).lerp(var1.getScale(new Vector3f()), var2);
      var1.translationRotateScale(var3, var4, var5);
   }
}

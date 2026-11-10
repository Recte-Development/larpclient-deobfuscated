package com.larpclient;

import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class un {
   public static final double F = 0.08;
   private g_ m;
   private int S;
   private int e;
   private float E;
   private double t = Double.NaN;
   private double o = Double.NEGATIVE_INFINITY;
   private Matrix4f[] O;
   private Matrix4f[] v;

   public void J() {
      this.m = null;
      this.O = this.v = null;
      this.t = Double.NaN;
      this.o = Double.NEGATIVE_INFINITY;
   }

   public Matrix4f[] n(g_ var1, co var2, sl.t var3, int var4, float var5, double var6) {
      int var8 = this.m == var1
            && this.S == var4
            && this.E == var5
            && Double.isFinite(var6)
            && Double.isFinite(this.t)
            && !(var6 < this.t)
            && !(var6 - this.t > 0.5)
         ? 0
         : 1;
      if (var8 != 0) {
         this.J();
         this.m = var1;
         this.S = var4;
         this.E = var5;
         this.O = p(var1.parts().size() + 2);
         this.v = p(this.O.length);
      } else if (var3.P || var6 - this.o < 0.08 && var3.L != this.e) {
         for (int var9 = 0; var9 < this.O.length; var9++) {
            this.v[var9].set(this.O[var9]);
         }

         this.o = var6;
      }

      tz var15 = tz.sample(var1, var2, var3.L, var3.J, var4, var5);
      this.O[0].set(var15.weapon());
      this.O[1].set(var15.arm());
      class_4587 var10 = new class_4587();

      for (int var11 = 0; var11 < var1.parts().size(); var11++) {
         var10.method_22903();
         var1.parts().get(var11).transform(var10, var3.L, var3.J, var4);
         this.O[var11 + 2].set(var10.method_23760().method_23761());
         var10.method_22909();
      }

      float var16 = (float)Math.clamp((var6 - this.o) / 0.08, 0.0, 1.0);
      if (var16 < 1.0F) {
         var16 = 1.0F - (1.0F - var16) * (1.0F - var16);
         Matrix4f var12 = new Matrix4f(this.O[0]).invert().mul(this.O[1]);
         Matrix4f var13 = new Matrix4f(this.v[0]).invert().mul(this.v[1]);
         u(var13, var12, var16);
         u(this.v[0], this.O[0], var16);
         this.O[1].set(this.O[0]).mul(var12);

         for (int var14 = 2; var14 < this.O.length; var14++) {
            u(this.v[var14], this.O[var14], var16);
         }
      }

      this.t = var6;
      this.e = var3.L;
      return this.O;
   }

   private static Matrix4f[] p(int var0) {
      Matrix4f[] var1 = new Matrix4f[var0];

      for (int var2 = 0; var2 < var0; var2++) {
         var1[var2] = new Matrix4f();
      }

      return var1;
   }

   private static void u(Matrix4f var0, Matrix4f var1, float var2) {
      Vector3f var3 = var0.getTranslation(new Vector3f()).lerp(var1.getTranslation(new Vector3f()), var2);
      Quaternionf var4 = var0.getUnnormalizedRotation(new Quaternionf()).normalize().slerp(var1.getUnnormalizedRotation(new Quaternionf()).normalize(), var2);
      Vector3f var5 = var0.getScale(new Vector3f()).lerp(var1.getScale(new Vector3f()), var2);
      var1.translationRotateScale(var3, var4, var5);
   }
}

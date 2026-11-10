package com.larpclient;

import com.larpclient.module.ViewModelModule;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1041;
import net.minecraft.class_10444;
import net.minecraft.class_1268;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_4587;
import net.minecraft.class_630;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class bw {
   private static final long n = 250000000L;
   private static final bw.e[] B = new bw.e[]{new bw.e(), new bw.e()};
   private static float l;
   private static float G;
   private static int F;
   private static int x;
   private static long c;

   public static boolean f() {
      class_310 var0 = class_310.method_1551();
      ViewModelModule var1 = ViewModelModule.B();
      return var1 != null && var1.isRuntimeActive() && var0.field_1724 != null && var0.field_1755 != null && var0.field_1755.getClass() == class_408.class;
   }

   public static void y() {
      if (f()) {
         class_310 var0 = class_310.method_1551();
         class_1041 var1 = var0.method_22683();
         float var2 = b9.V();
         if (!(var2 <= 0.0F) && var1.method_4506() > 0) {
            float var3 = (float)Math.tan(Math.toRadians(var2) / 2.0);
            G = 1.0F / var3;
            l = G * var1.method_4506() / var1.method_4489();
            F = var1.method_4486();
            x = var1.method_4502();
            c++;
         }
      }
   }

   public static void o(class_1268 var0, boolean var1, class_4587 var2, class_10444 var3) {
      if (f() && G != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.method_23760().method_23761());
         bw.e var5 = p(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.method_67991(var3x -> var5.k(var4.transformPosition(var3x, var6)));
      }
   }

   public static void f(class_1268 var0, boolean var1, class_4587 var2, class_630 var3) {
      if (f() && G != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         bw.e var5 = p(var0, var1);
         class_4587 var6 = new class_4587();
         em.L(var6, var2.method_23760().method_23761());
         Vector3f var7 = new Vector3f();
         var3.method_72152(var6, var3x -> var5.k(var4.transformPosition(var3x, var7)));
      }
   }

   public static void k(class_1268 var0, boolean var1, class_4587 var2, float[] var3, int var4) {
      if (f() && G != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.method_23760().method_23761());
         bw.e var6 = p(var0, var1);
         Vector3f var7 = new Vector3f();

         for (byte var8 = 0; var8 < var3.length; var8 += 8) {
            var6.k(var5.transformPosition(var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static bw.s n(class_1268 var0) {
      bw.e var1 = B[var0.ordinal()];
      if (var1.G != 0 && System.nanoTime() - var1.N <= 250000000L) {
         double var2 = var1.w / var1.G;
         int var4 = (int)Math.floor(var1.P);
         int var5 = (int)Math.floor(var1.z);
         return new bw.s(
            var0,
            var1.r,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.B) - var4),
            Math.max(1, (int)Math.ceil(var1.m) - var5),
            2.0 * var2 / (l * F),
            2.0 * var2 / (G * x)
         );
      } else {
         return null;
      }
   }

   private static bw.e p(class_1268 var0, boolean var1) {
      bw.e var2 = B[var0.ordinal()];
      if (var2.M != c) {
         var2.v(c, var1);
      }

      var2.N = System.nanoTime();
      return var2;
   }

   private bw() {
   }

   private static final class e {
      long M = -1L;
      long N;
      boolean r;
      int G;
      double P;
      double z;
      double B;
      double m;
      double w;

      void v(long var1, boolean var3) {
         this.M = var1;
         this.r = var3;
         this.G = 0;
         this.w = 0.0;
         this.P = this.z = Double.MAX_VALUE;
         this.B = this.m = -Double.MAX_VALUE;
      }

      void k(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (bw.l * var1.x() / var2 + 1.0F) / 2.0F * bw.F;
            double var5 = (1.0F - bw.G * var1.y() / var2) / 2.0F * bw.x;
            this.P = Math.min(this.P, var3);
            this.B = Math.max(this.B, var3);
            this.z = Math.min(this.z, var5);
            this.m = Math.max(this.m, var5);
            this.w += var2;
            this.G++;
         }
      }
   }

   public record s(class_1268 hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      public boolean contains(double var1, double var3) {
         return var1 >= this.x && var3 >= this.y && var1 < this.x + this.width && var3 < this.y + this.height;
      }
   }
}

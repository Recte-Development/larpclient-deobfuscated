package com.larpclient;

import com.larpclient.module.d5;
import com.larpclient.module.dj;
import java.util.Locale;
import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class h_ {
   public static final double j = 180.0;
   public static final float l = 0.55F;
   public static final float H = 0.8F;
   public static final float Q = 1.0F;
   public static final float r = 30.0F;
   public static final float h = 0.5F;
   public static final float C = 0.15F;
   private static final double K = 0.3;
   private static final float v = 6.0F;
   private static final int w = 16;
   private final SplittableRandom D;
   private final com.larpclient.module.d8 o;
   private final com.larpclient.module.a p = new com.larpclient.module.a();
   private float Z;
   private float G;
   private float T;
   private boolean I;
   private final StringBuilder m = new StringBuilder();
   private int N;
   private float S;
   private float q;

   public h_() {
      this(new SplittableRandom());
   }

   public h_(SplittableRandom var1) {
      this.D = var1;
      this.o = new com.larpclient.module.d8(com.larpclient.module.d8.D, var1.split());
   }

   public float[] V(float var1, float var2, float var3, float var4, double var5, boolean var7) {
      return this.j(var1, var2, var3, var4, var5, var7, dj.t());
   }

   public float[] j(float var1, float var2, float var3, float var4, double var5, boolean var7, float var8) {
      float var9 = Mth.clamp(var4, -90.0F, 90.0F);
      float[] var10 = var7 ? this.U(var1, var2, var3, var9, (float)Math.clamp(var5, 1.0, 180.0), var8) : z(var1, var2, var3, var9, var5);
      this.V(var1, var2, var10);
      return var10;
   }

   public static float[] z(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.clamp(var3, -90.0F, 90.0F);
      return var4 >= 180.0 ? new float[]{var0 + Mth.wrapDegrees(var2 - var0), var6} : hq.A(var0, var1, var2, var6, var4);
   }

   private float[] U(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (var9 < 1.0E-4F) {
         this.Z = 0.0F;
         this.I = (boolean)0;
         return new float[]{var1, var2};
      }

      if (this.Z == 0.0F && !this.I && var9 > 30.0F && this.D.nextDouble() < 0.3) {
         float var10 = (float)(0.02 + this.D.nextDouble() * 0.04);
         this.G = var7 * var10;
         this.T = var8 * var10;
         this.I = (boolean)1;
      }

      if (this.I) {
         float var18 = var7 + this.G;
         float var11 = Mth.clamp(var4 + this.T, -90.0F, 90.0F) - var2;
         float var12 = (float)Math.hypot(var18, var11);
         if (var12 <= 1.5F) {
            this.I = (boolean)0;
         } else {
            var7 = var18;
            var8 = var11;
            var9 = var12;
         }
      }

      int var19 = var9 < 6.0F ? 1 : 0;
      this.o.q();
      float var20 = this.o.g(var9, (boolean)var19);
      float var21 = this.o.O(com.larpclient.module.d8.a.G, (boolean)var19) + this.o.c(com.larpclient.module.d8.a.G) * 0.05F;
      float var13 = this.o.O(com.larpclient.module.d8.a.L, (boolean)var19) + this.o.c(com.larpclient.module.d8.a.L) * 0.05F;
      float var14;
      float var15;
      if (var9 <= 1.0F) {
         var14 = var7;
         var15 = var8;
      } else {
         float var16 = Math.min(var5, this.Z + var5 * 0.55F);
         var16 = Math.min(var16, Math.max(1.0F, var9 * 0.8F)) * var20;
         var14 = var7 / var9 * var16 * var21;
         var15 = var8 / var9 * var16 * var13;
         if (var19 == 0) {
            var15 += this.o.Q(var14);
         }

         var14 = k(var14, var7);
         var15 = k(var15, var8);
         float var17 = (float)Math.hypot(var14, var15);
         if (var17 > var5) {
            var14 *= var5 / var17;
            var15 *= var5 / var17;
         }
      }

      d5 var25 = this.p.H(new d5(var1, var2), new d5(var1 + var14, Mth.clamp(var2 + var15, -90.0F, 90.0F)), var6);
      this.Z = (float)Math.hypot(var25.M - var1, var25.G - var2);
      return new float[]{var25.M, var25.G};
   }

   private static float k(float var0, float var1) {
      return var1 >= 0.0F ? Mth.clamp(var0, -0.5F, var1) : Mth.clamp(var0, var1, 0.5F);
   }

   public static boolean k(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Math.max(0.15F, var4);
      return Math.abs(Mth.wrapDegrees(var2 - var0)) <= var5 && Math.abs(var3 - var1) <= var5;
   }

   public static float r(boolean var0, float var1, float var2) {
      return var0 ? Math.max(var1, var2 * 0.6F) : var1;
   }

   public void f() {
      this.o.F();
      this.p.P();
      this.Z = this.G = this.T = 0.0F;
      this.I = (boolean)0;
   }

   public void b() {
      this.m.setLength(0);
      this.N = 0;
      this.S = this.q = 0.0F;
   }

   public int t() {
      return this.N;
   }

   public String H() {
      return String.format(Locale.ROOT, "%d ticks, %.1f degrees, fastest %.1f/tick: %s", this.N, this.S, this.q, this.m.length() == 0 ? "no turn" : this.m);
   }

   private void V(float var1, float var2, float[] var3) {
      float var4 = (float)Math.hypot(Mth.wrapDegrees(var3[0] - var1), var3[1] - var2);
      if (!(var4 <= 0.0F)) {
         this.N++;
         this.S += var4;
         this.q = Math.max(this.q, var4);
         if (this.N <= 16) {
            if (this.m.length() > 0) {
               this.m.append(" > ");
            }

            this.m.append(String.format(Locale.ROOT, "%.1f/%.1f", Mth.wrapDegrees(var3[0]), var3[1]));
         } else if (this.N == 17) {
            this.m.append(" > ...");
         }
      }
   }
}

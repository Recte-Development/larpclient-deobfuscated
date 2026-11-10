package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.class_238;
import net.minecraft.class_243;

public final class iw {
   public static final long x = 1500L;
   public static final long B = 150L;
   public static final double P = 0.5;
   public static final double F = 1.3;
   public static final double q = 0.05;

   public static boolean z(double var0, double var2, double var4, double var6) {
      return Double.isFinite(var0) && Double.isFinite(var2) && var0 >= 0.0 && var2 >= 0.0 && var0 <= var4 && var2 <= var6;
   }

   public static <T> T L(List<iw.k<T>> var0, T var1, long var2, T var4, double var5, double var7) {
      double var9 = var7 / 2.0;

      for (iw.k var12 : var0) {
         if (var12.entity == var1 && var2 >= 0L && var2 <= 1500L && z(var12.distance, var12.angle, var5, var9)) {
            return (T)var1;
         }
      }

      for (iw.k var18 : var0) {
         if (var18.entity == var4 && z(var18.distance, var18.angle, var5 + 0.5, var9 * 1.3)) {
            return (T)var4;
         }
      }

      Object var17 = null;
      double var19 = var9;

      for (iw.k var15 : var0) {
         if (z(var15.distance, var15.angle, var5, var19)) {
            var17 = var15.entity;
            var19 = var15.angle;
         }
      }

      return (T)var17;
   }

   public static class_238 C(class_238 var0) {
      return var0.method_35580(var0.method_17939() * 0.25, var0.method_17940() * 0.1, var0.method_17939() * 0.25);
   }

   public static class_243 y(class_243 var0, class_238 var1) {
      return new class_243(
         Math.clamp(var0.field_1352, var1.field_1323, var1.field_1320),
         Math.clamp(var0.field_1351, var1.field_1322, var1.field_1325),
         Math.clamp(var0.field_1350, var1.field_1321, var1.field_1324)
      );
   }

   public static float[] Q(float var0, float var1, double var2, double var4) {
      return new float[]{var0 + (float)var2 * 0.15F, Math.clamp(var1 + (float)var4 * 0.15F, -90.0F, 90.0F)};
   }

   public static class_243 t(class_243 var0, class_243 var1, class_238 var2, double var3) {
      if (var2.method_1006(var0)) {
         return var0;
      }

      Optional var5 = var2.method_992(var0, var0.method_1019(var1.method_1021(var3 + 1.0)));
      return var5.orElseGet(() -> y(var0.method_1019(var1.method_1021(var0.method_1022(var2.method_1005()))), var2));
   }

   public static float[] x(class_243 var0, class_243 var1, class_238 var2, double var3) {
      if (!var2.method_1006(var0) && !var2.method_992(var0, var0.method_1019(var1.method_1021(var3 + 1.0))).isPresent()) {
         class_243 var5 = t(var0, var1, var2, var3).method_1020(var0);
         return new float[]{
            (float)w4.X(Math.toDegrees(Math.atan2(var5.field_1350, var5.field_1352)) - 90.0),
            (float)Math.clamp(-Math.toDegrees(Math.atan2(var5.field_1351, Math.hypot(var5.field_1352, var5.field_1350))), -90.0, 90.0)
         };
      } else {
         return null;
      }
   }

   public static double b(float var0, float var1, float[] var2) {
      return var2 == null ? 0.0 : Math.hypot(w4.X(var2[0] - var0), var2[1] - var1);
   }

   public static double M(double var0, boolean var2, boolean var3) {
      double var4 = var0 * 0.6F + 0.2F;
      return var4 * var4 * var4 * (var2 && var3 ? 1 : 8);
   }

   private iw() {
   }

   public static final class c {
      private double r;
      private double j;
      private double b;
      private double z;
      private long d;
      private long e;
      private long o;
      private boolean S;

      public iw.u Q(
         double var1, double var3, double var5, double var7, boolean var9, boolean var10, double var11, double var13, double var15, double var17, long var19
      ) {
         if (Double.isFinite(var1)
            && Double.isFinite(var3)
            && Double.isFinite(var11)
            && !(var11 < 0.0)
            && Double.isFinite(var17)
            && !(var17 <= 0.0)
            && Double.isFinite(var5)
            && Double.isFinite(var7)
            && Double.isFinite(var13)
            && !(var13 <= 0.0)
            && Double.isFinite(var15)
            && !(var15 <= 0.0)) {
            if (var1 != 0.0 || var3 != 0.0) {
               this.d = var19;
               this.S = (boolean)1;
            }

            if (var1 != 0.0) {
               this.b = Math.signum(var1);
               this.e = var19;
            } else if (this.b != 0.0 && (var19 < this.e || var19 - this.e > 150L)) {
               this.b = 0.0;
               this.r = 0.0;
            }

            if (var3 != 0.0) {
               this.z = Math.signum(var3);
               this.o = var19;
            } else if (this.z != 0.0 && (var19 < this.o || var19 - this.o > 150L)) {
               this.z = 0.0;
               this.j = 0.0;
            }

            if (var9 && this.S && var19 >= this.d && var19 - this.d <= 150L) {
               double var21 = Math.min(var11, 0.05);
               double var23 = var13 * var15 * var21;
               double var25 = -Math.expm1(-12.0 * var21);
               double var27 = (float)var17 * 0.15F;
               double var29 = w4.X(var5);
               if (this.b * var29 < 0.0) {
                  var29 = 0.0;
               }

               double var31 = var10 && this.z * var7 >= 0.0 ? var7 : 0.0;
               if (var29 == 0.0 || this.r * var29 < 0.0) {
                  this.r = 0.0;
               }

               if (var31 == 0.0 || this.j * var31 < 0.0) {
                  this.j = 0.0;
               }

               this.r = this.r + Math.clamp(var29 * var25, -var23, var23) / var27;
               this.j = this.j + Math.clamp(var31 * var25 * 0.6, -var23, var23) / var27;
               long var33 = (long)this.r;
               long var35 = (long)this.j;
               this.r -= var33;
               this.j -= var35;
               return new iw.u(var33 * var17, var35 * var17);
            } else {
               this.V();
               this.b = this.z = 0.0;
               return iw.u.STILL;
            }
         } else {
            this.u();
            return iw.u.STILL;
         }
      }

      public void V() {
         this.r = this.j = 0.0;
      }

      public void u() {
         this.V();
         this.b = this.z = 0.0;
         this.d = 0L;
         this.e = this.o = 0L;
         this.S = (boolean)0;
      }

      public double a() {
         return this.r;
      }

      public double g() {
         return this.j;
      }

      public boolean T(long var1) {
         return this.S && var1 >= this.d && var1 - this.d <= 150L;
      }

      public boolean r(double var1, double var3, boolean var5) {
         int var6 = var1 != 0.0 ? 1 : 0;
         int var7 = var5 && var3 != 0.0 ? 1 : 0;
         return (var6 != 0 || var7 != 0) && (var6 == 0 || !(this.b * var1 >= 0.0)) && (var7 == 0 || !(this.z * var3 >= 0.0));
      }
   }

   public record k<T>(T entity, double distance, double angle) {
   }

   public record u(double yaw, double pitch) {
      public static final iw.u STILL = new iw.u(0.0, 0.0);
   }
}

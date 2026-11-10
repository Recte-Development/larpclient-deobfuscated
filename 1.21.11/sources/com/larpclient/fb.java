package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_243;

public final class fb {
   public static final int J = 64;
   private final fb.d[] Y = new fb.d[64];
   private int k;
   private int e;
   private int S;
   private long b;
   private boolean o;
   private double j = Double.NaN;
   private float X;
   private Object u;

   public int A() {
      return this.e;
   }

   public void p() {
      Arrays.fill(this.Y, null);
      this.k = this.e = this.S = 0;
      this.o = (boolean)0;
      this.j = Double.NaN;
      this.u = null;
   }

   public void x(double var1, double var3) {
      while (this.e > 0 && var1 - this.Y[this.k].time() > var3) {
         this.Y[this.k] = null;
         this.k = (this.k + 1) % 64;
         this.e--;
      }
   }

   public boolean r(Object var1, class_243 var2, class_243 var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (e(var2) && e(var3) && Double.isFinite(var4)) {
         if (this.u != var1 || Double.isFinite(this.j) && (var4 < this.j || var4 - this.j > 0.25)) {
            this.p();
            this.u = var1;
         }

         this.j = var4;
         this.x(var4, var8);

         while (this.e > Math.clamp(var10, 4, 64)) {
            this.Y[this.k] = null;
            this.k = (this.k + 1) % 64;
            this.e--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.o && !(var6 + 0.05F < this.X) ? 0 : 1;
            if (var11 != 0) {
               this.S++;
            }

            this.o = (boolean)1;
            this.X = var6;
            fb.d var12 = this.e > 0 ? this.G(this.e - 1) : null;
            if (var12 != null && var12.stroke() == this.S) {
               double var13 = Math.max(var2.method_1022(var12.root()), var3.method_1022(var12.tip()));
               if (!(var13 > 2.5)) {
                  double var15 = var4 - var12.time();
                  if (!(var15 <= 1.0E-7) && (!(var13 < 0.012) || !(var15 < 0.045))) {
                     if (var13 < 0.001) {
                        return false;
                     }

                     int var17 = Math.clamp((int)Math.ceil(var13 / 0.09), 1, 6);

                     for (int var18 = 1; var18 <= var17; var18++) {
                        double var19 = (double)var18 / var17;
                        this.V(
                           new fb.d(
                              var12.root().method_35590(var2, var19), var12.tip().method_35590(var3, var19), var12.time() + var15 * var19, this.S, ++this.b
                           ),
                           var10
                        );
                     }

                     return true;
                  }

                  return false;
               }

               this.p();
               this.u = var1;
               this.S++;
               this.o = (boolean)1;
            }

            this.V(new fb.d(var2, var3, var4, this.S, ++this.b), var10);
            return true;
         } else {
            this.o = (boolean)0;
            return false;
         }
      } else {
         this.p();
         return false;
      }
   }

   private void V(fb.d var1, int var2) {
      int var3 = Math.clamp(var2, 4, 64);

      while (this.e >= var3) {
         this.Y[this.k] = null;
         this.k = (this.k + 1) % 64;
         this.e--;
      }

      this.Y[(this.k + this.e++) % 64] = var1;
   }

   public fb.d G(int var1) {
      return this.Y[(this.k + var1) % 64];
   }

   public List<fb.d> K() {
      ArrayList var1 = new ArrayList(this.e);

      for (int var2 = 0; var2 < this.e; var2++) {
         var1.add(this.G(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean e(class_243 var0) {
      return var0 != null && Double.isFinite(var0.field_1352 + var0.field_1351 + var0.field_1350);
   }

   public record d(class_243 root, class_243 tip, double time, int stroke, long serial) {
   }
}

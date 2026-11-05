package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class t8 {
   public static final int M = 64;
   private final t8.m[] u = new t8.m[64];
   private int F;
   private int q;
   private int o;
   private long K;
   private boolean I;
   private double A = Double.NaN;
   private float N;
   private Object H;

   public int W() {
      return this.q;
   }

   public void h() {
      Arrays.fill(this.u, null);
      this.F = this.q = this.o = 0;
      this.I = (boolean)0;
      this.A = Double.NaN;
      this.H = null;
   }

   public void i(double var1, double var3) {
      while (this.q > 0 && var1 - this.u[this.F].time() > var3) {
         this.u[this.F] = null;
         this.F = (this.F + 1) % 64;
         this.q--;
      }
   }

   public boolean G(Object var1, Vec3 var2, Vec3 var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (J(var2) && J(var3) && Double.isFinite(var4)) {
         if (this.H != var1 || Double.isFinite(this.A) && (var4 < this.A || var4 - this.A > 0.25)) {
            this.h();
            this.H = var1;
         }

         this.A = var4;
         this.i(var4, var8);

         while (this.q > Math.clamp(var10, 4, 64)) {
            this.u[this.F] = null;
            this.F = (this.F + 1) % 64;
            this.q--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.I && !(var6 + 0.05F < this.N) ? 0 : 1;
            if (var11 != 0) {
               this.o++;
            }

            this.I = (boolean)1;
            this.N = var6;
            t8.m var12 = this.q > 0 ? this.h(this.q - 1) : null;
            if (var12 != null && var12.stroke() == this.o) {
               double var13 = Math.max(var2.distanceTo(var12.root()), var3.distanceTo(var12.tip()));
               if (!(var13 > 2.5)) {
                  double var15 = var4 - var12.time();
                  if (!(var15 <= 1.0E-7) && (!(var13 < 0.012) || !(var15 < 0.045))) {
                     if (var13 < 0.001) {
                        return false;
                     }

                     int var17 = Math.clamp((int)Math.ceil(var13 / 0.09), 1, 6);

                     for (int var18 = 1; var18 <= var17; var18++) {
                        double var19 = (double)var18 / var17;
                        this.F(new t8.m(var12.root().lerp(var2, var19), var12.tip().lerp(var3, var19), var12.time() + var15 * var19, this.o, ++this.K), var10);
                     }

                     return true;
                  }

                  return false;
               }

               this.h();
               this.H = var1;
               this.o++;
               this.I = (boolean)1;
            }

            this.F(new t8.m(var2, var3, var4, this.o, ++this.K), var10);
            return true;
         } else {
            this.I = (boolean)0;
            return false;
         }
      } else {
         this.h();
         return false;
      }
   }

   private void F(t8.m var1, int var2) {
      int var3 = Math.clamp(var2, 4, 64);

      while (this.q >= var3) {
         this.u[this.F] = null;
         this.F = (this.F + 1) % 64;
         this.q--;
      }

      this.u[(this.F + this.q++) % 64] = var1;
   }

   public t8.m h(int var1) {
      return this.u[(this.F + var1) % 64];
   }

   public List<t8.m> C() {
      ArrayList var1 = new ArrayList(this.q);

      for (int var2 = 0; var2 < this.q; var2++) {
         var1.add(this.h(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean J(Vec3 var0) {
      return var0 != null && Double.isFinite(var0.x + var0.y + var0.z);
   }

   public record m(Vec3 root, Vec3 tip, double time, int stroke, long serial) {
   }
}

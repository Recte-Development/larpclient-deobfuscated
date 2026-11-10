package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class sk {
   public static final int h = 64;
   private final sk.u[] Q = new sk.u[64];
   private int S;
   private int i;
   private int a;
   private long X;
   private boolean P;
   private double r = Double.NaN;
   private float Y;
   private Object F;

   public int h() {
      return this.i;
   }

   public void i() {
      Arrays.fill(this.Q, null);
      this.S = this.i = this.a = 0;
      this.P = (boolean)0;
      this.r = Double.NaN;
      this.F = null;
   }

   public void l(double var1, double var3) {
      while (this.i > 0 && var1 - this.Q[this.S].time() > var3) {
         this.Q[this.S] = null;
         this.S = (this.S + 1) % 64;
         this.i--;
      }
   }

   public boolean n(Object var1, Vec3 var2, Vec3 var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (g(var2) && g(var3) && Double.isFinite(var4)) {
         if (this.F != var1 || Double.isFinite(this.r) && (var4 < this.r || var4 - this.r > 0.25)) {
            this.i();
            this.F = var1;
         }

         this.r = var4;
         this.l(var4, var8);

         while (this.i > Math.clamp(var10, 4, 64)) {
            this.Q[this.S] = null;
            this.S = (this.S + 1) % 64;
            this.i--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.P && !(var6 + 0.05F < this.Y) ? 0 : 1;
            if (var11 != 0) {
               this.a++;
            }

            this.P = (boolean)1;
            this.Y = var6;
            sk.u var12 = this.i > 0 ? this.l(this.i - 1) : null;
            if (var12 != null && var12.stroke() == this.a) {
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
                        this.k(new sk.u(var12.root().lerp(var2, var19), var12.tip().lerp(var3, var19), var12.time() + var15 * var19, this.a, ++this.X), var10);
                     }

                     return true;
                  }

                  return false;
               }

               this.i();
               this.F = var1;
               this.a++;
               this.P = (boolean)1;
            }

            this.k(new sk.u(var2, var3, var4, this.a, ++this.X), var10);
            return true;
         } else {
            this.P = (boolean)0;
            return false;
         }
      } else {
         this.i();
         return false;
      }
   }

   private void k(sk.u var1, int var2) {
      int var3 = Math.clamp(var2, 4, 64);

      while (this.i >= var3) {
         this.Q[this.S] = null;
         this.S = (this.S + 1) % 64;
         this.i--;
      }

      this.Q[(this.S + this.i++) % 64] = var1;
   }

   public sk.u l(int var1) {
      return this.Q[(this.S + var1) % 64];
   }

   public List<sk.u> e() {
      ArrayList var1 = new ArrayList(this.i);

      for (int var2 = 0; var2 < this.i; var2++) {
         var1.add(this.l(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean g(Vec3 var0) {
      return var0 != null && Double.isFinite(var0.x + var0.y + var0.z);
   }

   public record u(Vec3 root, Vec3 tip, double time, int stroke, long serial) {
   }
}

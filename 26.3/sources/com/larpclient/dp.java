package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class dp {
   public static final int R = 64;
   private final dp.r[] n = new dp.r[64];
   private int Q;
   private int r;
   private int h;
   private long k;
   private boolean D;
   private double S = Double.NaN;
   private float t;
   private Object x;

   public int Z() {
      return this.r;
   }

   public void j() {
      Arrays.fill(this.n, null);
      this.Q = this.r = this.h = 0;
      this.D = (boolean)0;
      this.S = Double.NaN;
      this.x = null;
   }

   public void o(double var1, double var3) {
      while (this.r > 0 && var1 - this.n[this.Q].time() > var3) {
         this.n[this.Q] = null;
         this.Q = (this.Q + 1) % 64;
         this.r--;
      }
   }

   public boolean C(Object var1, Vec3 var2, Vec3 var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (H(var2) && H(var3) && Double.isFinite(var4)) {
         if (this.x != var1 || Double.isFinite(this.S) && (var4 < this.S || var4 - this.S > 0.25)) {
            this.j();
            this.x = var1;
         }

         this.S = var4;
         this.o(var4, var8);

         while (this.r > Math.clamp(var10, 4, 64)) {
            this.n[this.Q] = null;
            this.Q = (this.Q + 1) % 64;
            this.r--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.D && !(var6 + 0.05F < this.t) ? 0 : 1;
            if (var11 != 0) {
               this.h++;
            }

            this.D = (boolean)1;
            this.t = var6;
            dp.r var12 = this.r > 0 ? this.z(this.r - 1) : null;
            if (var12 != null && var12.stroke() == this.h) {
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
                        this.W(new dp.r(var12.root().lerp(var2, var19), var12.tip().lerp(var3, var19), var12.time() + var15 * var19, this.h, ++this.k), var10);
                     }

                     return true;
                  }

                  return false;
               }

               this.j();
               this.x = var1;
               this.h++;
               this.D = (boolean)1;
            }

            this.W(new dp.r(var2, var3, var4, this.h, ++this.k), var10);
            return true;
         } else {
            this.D = (boolean)0;
            return false;
         }
      } else {
         this.j();
         return false;
      }
   }

   private void W(dp.r var1, int var2) {
      int var3 = Math.clamp(var2, 4, 64);

      while (this.r >= var3) {
         this.n[this.Q] = null;
         this.Q = (this.Q + 1) % 64;
         this.r--;
      }

      this.n[(this.Q + this.r++) % 64] = var1;
   }

   public dp.r z(int var1) {
      return this.n[(this.Q + var1) % 64];
   }

   public List<dp.r> q() {
      ArrayList var1 = new ArrayList(this.r);

      for (int var2 = 0; var2 < this.r; var2++) {
         var1.add(this.z(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean H(Vec3 var0) {
      return var0 != null && Double.isFinite(var0.x + var0.y + var0.z);
   }

   public record r(Vec3 root, Vec3 tip, double time, int stroke, long serial) {
   }
}

package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.phys.Vec3;

public final class _j {
   public static final int m = 64;
   private final _j.i[] J = new _j.i[64];
   private int r;
   private int G;
   private int U;
   private long v;
   private boolean k;
   private double y = Double.NaN;
   private float R;
   private Object Y;

   public int v() {
      return this.G;
   }

   public void X() {
      Arrays.fill(this.J, null);
      this.r = this.G = this.U = 0;
      this.k = (boolean)0;
      this.y = Double.NaN;
      this.Y = null;
   }

   public void a(double var1, double var3) {
      while (this.G > 0 && var1 - this.J[this.r].time() > var3) {
         this.J[this.r] = null;
         this.r = (this.r + 1) % 64;
         this.G--;
      }
   }

   public boolean a(Object var1, Vec3 var2, Vec3 var3, double var4, float var6, boolean var7, double var8, int var10) {
      if (s(var2) && s(var3) && Double.isFinite(var4)) {
         if (this.Y != var1 || Double.isFinite(this.y) && (var4 < this.y || var4 - this.y > 0.25)) {
            this.X();
            this.Y = var1;
         }

         this.y = var4;
         this.a(var4, var8);

         while (this.G > Math.clamp(var10, 4, 64)) {
            this.J[this.r] = null;
            this.r = (this.r + 1) % 64;
            this.G--;
         }

         if (var7 && !(var6 <= 0.0F) && !(var6 >= 1.0F)) {
            int var11 = this.k && !(var6 + 0.05F < this.R) ? 0 : 1;
            if (var11 != 0) {
               this.U++;
            }

            this.k = (boolean)1;
            this.R = var6;
            _j.i var12 = this.G > 0 ? this.j(this.G - 1) : null;
            if (var12 != null && var12.stroke() == this.U) {
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
                        this.R(new _j.i(var12.root().lerp(var2, var19), var12.tip().lerp(var3, var19), var12.time() + var15 * var19, this.U, ++this.v), var10);
                     }

                     return true;
                  }

                  return false;
               }

               this.X();
               this.Y = var1;
               this.U++;
               this.k = (boolean)1;
            }

            this.R(new _j.i(var2, var3, var4, this.U, ++this.v), var10);
            return true;
         } else {
            this.k = (boolean)0;
            return false;
         }
      } else {
         this.X();
         return false;
      }
   }

   private void R(_j.i var1, int var2) {
      int var3 = Math.clamp(var2, 4, 64);

      while (this.G >= var3) {
         this.J[this.r] = null;
         this.r = (this.r + 1) % 64;
         this.G--;
      }

      this.J[(this.r + this.G++) % 64] = var1;
   }

   public _j.i j(int var1) {
      return this.J[(this.r + var1) % 64];
   }

   public List<_j.i> e() {
      ArrayList var1 = new ArrayList(this.G);

      for (int var2 = 0; var2 < this.G; var2++) {
         var1.add(this.j(var2));
      }

      return List.copyOf(var1);
   }

   private static boolean s(Vec3 var0) {
      return var0 != null && Double.isFinite(var0.x + var0.y + var0.z);
   }

   public record i(Vec3 root, Vec3 tip, double time, int stroke, long serial) {
   }
}

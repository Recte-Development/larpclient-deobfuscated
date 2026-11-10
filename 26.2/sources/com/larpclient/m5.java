package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class m5 {
   public static void s(kj var0, boolean var1, su var2, AABB var3, double var4, int var6, double var7, double var9, double var11, double var13) {
      var0.blend(gl.c.E);
      AABB var15 = var3;

      for (su.y var17 : var2.edges()) {
         double var18 = T(var17.ax(), var15.minX, var15.maxX);
         double var20 = T(var17.ay(), var15.minY, var15.maxY);
         double var22 = T(var17.az(), var15.minZ, var15.maxZ);
         double var24 = T(var17.bx(), var15.minX, var15.maxX);
         double var26 = T(var17.by(), var15.minY, var15.maxY);
         double var28 = T(var17.bz(), var15.minZ, var15.maxZ);
         double var30 = var4 * var7;
         if (var13 > 0.0) {
            int var32 = uh.F(var6, var30 * var13 * (var1 ? 0.25 : 0.09));
            var0.k(var18, var20, var22, var24, var26, var28, var9 * 3.0, var9 * 3.0, var32, var32);
            if (!var1) {
               var0.k(var18, var20, var22, var24, var26, var28, var9 * 5.0, var9 * 5.0, uh.F(var32, 0.35), uh.F(var32, 0.35));
            }
         }

         if (!var1) {
            int var35 = uh.F(var6, var30);
            var0.k(var18, var20, var22, var24, var26, var28, var9, var9, var35, var35);
         }
      }

      var0.blend(gl.c.K);
      if (!var1 && var11 > 0.0) {
         for (AABB var34 : var2.boxes()) {
            m(
               var0,
               new AABB(
                  T(var34.minX, var15.minX, var15.maxX),
                  T(var34.minY, var15.minY, var15.maxY),
                  T(var34.minZ, var15.minZ, var15.maxZ),
                  T(var34.maxX, var15.minX, var15.maxX),
                  T(var34.maxY, var15.minY, var15.maxY),
                  T(var34.maxZ, var15.minZ, var15.maxZ)
               ),
               uh.F(var6, var4 * var11)
            );
         }
      }
   }

   private static double T(double var0, double var2, double var4) {
      return su.map(var0, var2, var4);
   }

   private static void m(kj var0, AABB var1, int var2) {
      double var3 = var1.minX;
      double var5 = var1.minY;
      double var7 = var1.minZ;
      double var9 = var1.maxX;
      double var11 = var1.maxY;
      double var13 = var1.maxZ;
      u(var0, var2, var3, var5, var7, var9, var5, var7, var9, var11, var7, var3, var11, var7);
      u(var0, var2, var3, var5, var13, var3, var11, var13, var9, var11, var13, var9, var5, var13);
      u(var0, var2, var3, var5, var7, var3, var11, var7, var3, var11, var13, var3, var5, var13);
      u(var0, var2, var9, var5, var7, var9, var5, var13, var9, var11, var13, var9, var11, var7);
      u(var0, var2, var3, var5, var7, var3, var5, var13, var9, var5, var13, var9, var5, var7);
      u(var0, var2, var3, var11, var7, var9, var11, var7, var9, var11, var13, var3, var11, var13);
   }

   private static void u(
      kj var0,
      int var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24
   ) {
      var0.vertex(var2, var4, var6, var1);
      var0.vertex(var8, var10, var12, var1);
      var0.vertex(var14, var16, var18, var1);
      var0.vertex(var20, var22, var24, var1);
   }

   private m5() {
   }
}

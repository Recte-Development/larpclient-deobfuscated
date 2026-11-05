package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class md {
   public static void C(r6 var0, boolean var1, d4 var2, AABB var3, double var4, int var6, double var7, double var9, double var11, double var13) {
      var0.blend(fk.h.u);
      AABB var15 = var3;

      for (d4.n var17 : var2.edges()) {
         double var18 = i(var17.ax(), var15.minX, var15.maxX);
         double var20 = i(var17.ay(), var15.minY, var15.maxY);
         double var22 = i(var17.az(), var15.minZ, var15.maxZ);
         double var24 = i(var17.bx(), var15.minX, var15.maxX);
         double var26 = i(var17.by(), var15.minY, var15.maxY);
         double var28 = i(var17.bz(), var15.minZ, var15.maxZ);
         double var30 = var4 * var7;
         if (var13 > 0.0) {
            int var32 = oh.S(var6, var30 * var13 * (var1 ? 0.25 : 0.09));
            var0.p(var18, var20, var22, var24, var26, var28, var9 * 3.0, var9 * 3.0, var32, var32);
            if (!var1) {
               var0.p(var18, var20, var22, var24, var26, var28, var9 * 5.0, var9 * 5.0, oh.S(var32, 0.35), oh.S(var32, 0.35));
            }
         }

         if (!var1) {
            int var35 = oh.S(var6, var30);
            var0.p(var18, var20, var22, var24, var26, var28, var9, var9, var35, var35);
         }
      }

      var0.blend(fk.h.z);
      if (!var1 && var11 > 0.0) {
         for (AABB var34 : var2.boxes()) {
            c(
               var0,
               new AABB(
                  i(var34.minX, var15.minX, var15.maxX),
                  i(var34.minY, var15.minY, var15.maxY),
                  i(var34.minZ, var15.minZ, var15.maxZ),
                  i(var34.maxX, var15.minX, var15.maxX),
                  i(var34.maxY, var15.minY, var15.maxY),
                  i(var34.maxZ, var15.minZ, var15.maxZ)
               ),
               oh.S(var6, var4 * var11)
            );
         }
      }
   }

   private static double i(double var0, double var2, double var4) {
      return d4.map(var0, var2, var4);
   }

   private static void c(r6 var0, AABB var1, int var2) {
      double var3 = var1.minX;
      double var5 = var1.minY;
      double var7 = var1.minZ;
      double var9 = var1.maxX;
      double var11 = var1.maxY;
      double var13 = var1.maxZ;
      s(var0, var2, var3, var5, var7, var9, var5, var7, var9, var11, var7, var3, var11, var7);
      s(var0, var2, var3, var5, var13, var3, var11, var13, var9, var11, var13, var9, var5, var13);
      s(var0, var2, var3, var5, var7, var3, var11, var7, var3, var11, var13, var3, var5, var13);
      s(var0, var2, var9, var5, var7, var9, var5, var13, var9, var11, var13, var9, var11, var7);
      s(var0, var2, var3, var5, var7, var3, var5, var13, var9, var5, var13, var9, var5, var7);
      s(var0, var2, var3, var11, var7, var9, var11, var7, var9, var11, var13, var3, var11, var13);
   }

   private static void s(
      r6 var0,
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

   private md() {
   }
}

package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class mx {
   public static void N(ki var0, boolean var1, hf var2, AABB var3, double var4, int var6, double var7, double var9, double var11, double var13) {
      var0.blend(pf.x.E);
      AABB var15 = var3;

      for (hf.v var17 : var2.edges()) {
         double var18 = a(var17.ax(), var15.minX, var15.maxX);
         double var20 = a(var17.ay(), var15.minY, var15.maxY);
         double var22 = a(var17.az(), var15.minZ, var15.maxZ);
         double var24 = a(var17.bx(), var15.minX, var15.maxX);
         double var26 = a(var17.by(), var15.minY, var15.maxY);
         double var28 = a(var17.bz(), var15.minZ, var15.maxZ);
         double var30 = var4 * var7;
         if (var13 > 0.0) {
            int var32 = _3.U(var6, var30 * var13 * (var1 ? 0.25 : 0.09));
            var0.b(var18, var20, var22, var24, var26, var28, var9 * 3.0, var9 * 3.0, var32, var32);
            if (!var1) {
               var0.b(var18, var20, var22, var24, var26, var28, var9 * 5.0, var9 * 5.0, _3.U(var32, 0.35), _3.U(var32, 0.35));
            }
         }

         if (!var1) {
            int var35 = _3.U(var6, var30);
            var0.b(var18, var20, var22, var24, var26, var28, var9, var9, var35, var35);
         }
      }

      var0.blend(pf.x.S);
      if (!var1 && var11 > 0.0) {
         for (AABB var34 : var2.boxes()) {
            D(
               var0,
               new AABB(
                  a(var34.minX, var15.minX, var15.maxX),
                  a(var34.minY, var15.minY, var15.maxY),
                  a(var34.minZ, var15.minZ, var15.maxZ),
                  a(var34.maxX, var15.minX, var15.maxX),
                  a(var34.maxY, var15.minY, var15.maxY),
                  a(var34.maxZ, var15.minZ, var15.maxZ)
               ),
               _3.U(var6, var4 * var11)
            );
         }
      }
   }

   private static double a(double var0, double var2, double var4) {
      return hf.map(var0, var2, var4);
   }

   private static void D(ki var0, AABB var1, int var2) {
      double var3 = var1.minX;
      double var5 = var1.minY;
      double var7 = var1.minZ;
      double var9 = var1.maxX;
      double var11 = var1.maxY;
      double var13 = var1.maxZ;
      V(var0, var2, var3, var5, var7, var9, var5, var7, var9, var11, var7, var3, var11, var7);
      V(var0, var2, var3, var5, var13, var3, var11, var13, var9, var11, var13, var9, var5, var13);
      V(var0, var2, var3, var5, var7, var3, var11, var7, var3, var11, var13, var3, var5, var13);
      V(var0, var2, var9, var5, var7, var9, var5, var13, var9, var11, var13, var9, var11, var7);
      V(var0, var2, var3, var5, var7, var3, var5, var13, var9, var5, var13, var9, var5, var7);
      V(var0, var2, var3, var11, var7, var9, var11, var7, var9, var11, var13, var3, var11, var13);
   }

   private static void V(
      ki var0,
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

   private mx() {
   }
}

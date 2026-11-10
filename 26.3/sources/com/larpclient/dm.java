package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class dm {
   public static final double k = 0.25;
   public static final double J = 0.012;

   public static dm.x b(AABB var0, double var1, double var3, dm.k var5, boolean var6) {
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      if (var1 > 0.0 && Double.isFinite(var1)) {
         double var9 = Math.min(8.0, var1 + Math.max(var0.getXsize(), var0.getZsize()) / 2.0 + 0.25);
         Vec3 var11 = var0.getCenter();
         int var12 = (int)Math.floor((var11.x - var9) / 0.25);
         int var13 = (int)Math.ceil((var11.x + var9) / 0.25);
         int var14 = (int)Math.floor((var11.z - var9) / 0.25);
         int var15 = (int)Math.ceil((var11.z + var9) / 0.25);

         for (int var16 = var12; var16 < var13; var16++) {
            for (int var17 = var14; var17 < var15; var17++) {
               double var18 = var16 * 0.25;
               double var20 = var17 * 0.25;
               dm.h var22 = var5.D(var18 + 0.125, var20 + 0.125);
               if (var22 != null) {
                  double var23 = Math.max(var18, var22.minX);
                  double var25 = Math.min(var18 + 0.25, var22.maxX);
                  double var27 = Math.max(var20, var22.minZ);
                  double var29 = Math.min(var20 + 0.25, var22.maxZ);
                  if (!(var23 >= var25)
                     && !(var27 >= var29)
                     && D(var5, var23 + 1.0E-5, var27 + 1.0E-5, var22.y)
                     && D(var5, var25 - 1.0E-5, var27 + 1.0E-5, var22.y)
                     && D(var5, var25 - 1.0E-5, var29 - 1.0E-5, var22.y)
                     && D(var5, var23 + 1.0E-5, var29 - 1.0E-5, var22.y)) {
                     Vec3 var31 = new Vec3(var23, var22.y + 0.012, var27);
                     Vec3 var32 = new Vec3(var25, var22.y + 0.012, var27);
                     Vec3 var33 = new Vec3(var25, var22.y + 0.012, var29);
                     Vec3 var34 = new Vec3(var23, var22.y + 0.012, var29);
                     R(var0, var1, var3, var31, var32, var33, var7, var6 ? var8 : null);
                     R(var0, var1, var3, var31, var33, var34, var7, var6 ? var8 : null);
                     z(var0, var1, var3, var5, var31, var32, 0.0, -0.003, var22.y, var7);
                     z(var0, var1, var3, var5, var32, var33, 0.003, 0.0, var22.y, var7);
                     z(var0, var1, var3, var5, var33, var34, 0.0, 0.003, var22.y, var7);
                     z(var0, var1, var3, var5, var34, var31, -0.003, 0.0, var22.y, var7);
                  }
               }
            }
         }

         return new dm.x(List.copyOf(var7), List.copyOf(var8));
      } else {
         return new dm.x(List.of(), List.of());
      }
   }

   private static boolean D(dm.k var0, double var1, double var3, double var5) {
      dm.h var7 = var0.D(var1, var3);
      return var7 != null && Math.abs(var7.y - var5) < 0.001;
   }

   private static void z(AABB var0, double var1, double var3, dm.k var5, Vec3 var6, Vec3 var7, double var8, double var10, double var12, List<dm.b> var14) {
      dm.h var15 = var5.D((var6.x + var7.x) / 2.0 + var8, (var6.z + var7.z) / 2.0 + var10);
      if (var15 != null && !(var12 - var15.y < 0.001) && !(var12 - var15.y > 1.05)) {
         Vec3 var16 = var6.add(var8, 0.0, var10);
         Vec3 var17 = var7.add(var8, 0.0, var10);
         Vec3 var18 = new Vec3(var17.x, var15.y + 0.012, var17.z);
         Vec3 var19 = new Vec3(var16.x, var15.y + 0.012, var16.z);
         R(var0, var1, var3, var16, var17, var18, var14, null);
         R(var0, var1, var3, var16, var18, var19, var14, null);
      }
   }

   public static double C(AABB var0, Vec3 var1, double var2, double var4) {
      return jz.j(var1.add(0.0, var2 - 0.012, 0.0), var0) - var4;
   }

   private static void R(AABB var0, double var1, double var3, Vec3 var5, Vec3 var6, Vec3 var7, List<dm.b> var8, List<dm.r> var9) {
      Vec3[] var10 = new Vec3[]{var5, var6, var7};
      double[] var11 = new double[]{C(var0, var5, var3, var1), C(var0, var6, var3, var1), C(var0, var7, var3, var1)};
      if (!(var11[0] > 0.0) || !(var11[1] > 0.0) || !(var11[2] > 0.0)) {
         if (var9 != null || !(var11[0] <= 0.0) || !(var11[1] <= 0.0) || !(var11[2] <= 0.0)) {
            ArrayList var12 = new ArrayList(4);
            ArrayList var13 = new ArrayList(2);

            for (int var14 = 0; var14 < 3; var14++) {
               int var15 = (var14 + 1) % 3;
               if (var11[var14] <= 0.0) {
                  var12.add(var10[var14]);
               }

               if ((var11[var14] <= 0.0 ? 1 : 0) != (var11[var15] <= 0.0 ? 1 : 0)) {
                  Vec3 var16 = u(var0, var1, var3, var10[var14], var10[var15], var11[var14] <= 0.0);
                  var12.add(var16);
                  var13.add(var16);
               }
            }

            if (var13.size() == 2 && ((Vec3)var13.get(0)).distanceToSqr((Vec3)var13.get(1)) > 1.0E-12) {
               var8.add(new dm.b((Vec3)var13.get(0), (Vec3)var13.get(1)));
            }

            if (var9 != null) {
               for (int var17 = 1; var17 + 1 < var12.size(); var17++) {
                  var9.add(new dm.r((Vec3)var12.get(0), (Vec3)var12.get(var17), (Vec3)var12.get(var17 + 1)));
               }
            }
         }
      }
   }

   private static Vec3 u(AABB var0, double var1, double var3, Vec3 var5, Vec3 var6, boolean var7) {
      double var8 = 0.0;
      double var10 = 1.0;

      for (int var12 = 0; var12 < 12; var12++) {
         double var13 = (var8 + var10) / 2.0;
         if ((C(var0, var5.lerp(var6, var13), var3, var1) <= 0.0 ? 1 : 0) == var7) {
            var8 = var13;
         } else {
            var10 = var13;
         }
      }

      return var5.lerp(var6, (var8 + var10) / 2.0);
   }

   public static dm.h I(List<AABB> var0, double var1, double var3, double var5) {
      AABB var7 = null;

      for (AABB var9 : var0) {
         if (!(var1 < var9.minX)
            && !(var1 >= var9.maxX)
            && !(var3 < var9.minZ)
            && !(var3 >= var9.maxZ)
            && !(var9.maxY < var5 - 1.5)
            && !(var9.maxY > var5 + 1.05)) {
            byte var10 = 0;

            for (AABB var12 : var0) {
               if (var1 >= var12.minX
                  && var1 < var12.maxX
                  && var3 >= var12.minZ
                  && var3 < var12.maxZ
                  && var12.minY <= var9.maxY + 0.001
                  && var12.maxY > var9.maxY + 0.001) {
                  var10 = 1;
                  break;
               }
            }

            if (var10 == 0
               && (
                  var7 == null
                     || Math.abs(var9.maxY - var5) < Math.abs(var7.maxY - var5)
                     || Math.abs(var9.maxY - var5) == Math.abs(var7.maxY - var5) && var9.maxY > var7.maxY
               )) {
               var7 = var9;
            }
         }
      }

      return var7 == null ? null : new dm.h(var7.minX, var7.minZ, var7.maxX, var7.maxZ, var7.maxY);
   }

   private dm() {
   }

   public record b(Vec3 from, Vec3 to) {
   }

   public record h(double minX, double minZ, double maxX, double maxZ, double y) {
   }

   @FunctionalInterface
   public interface k {
      dm.h D(double var1, double var3);
   }

   public record r(Vec3 a, Vec3 b, Vec3 c) {
   }

   public record x(List<dm.b> outline, List<dm.r> fill) {
   }
}

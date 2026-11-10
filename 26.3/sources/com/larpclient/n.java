package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class n {
   public static final double A = 8.0;
   public static final double b = 0.012;
   public static final int G = 361;
   public static final int S = 32;
   public static final int f = 128;
   public static final int M = 4096;

   public static List<n.p> N(Vec3 var0, double var1, n.f var3) {
      double var4 = Math.clamp(var1, 0.5, 8.0);
      ArrayList var6 = new ArrayList();

      for (int var7 = (int)Math.floor(var0.x - var4); var7 <= Math.floor(var0.x + var4); var7++) {
         for (int var8 = (int)Math.floor(var0.z - var4); var8 <= Math.floor(var0.z + var4); var8++) {
            double var9 = Math.max(Math.max(var7 - var0.x, var0.x - (var7 + 1)), 0.0);
            double var11 = Math.max(Math.max(var8 - var0.z, var0.z - (var8 + 1)), 0.0);
            if (var9 * var9 + var11 * var11 <= var4 * var4) {
               var6.add(new int[]{var7, var8});
            }
         }
      }

      var6.sort(Comparator.comparingDouble(var1x -> Math.hypot(var1x[0] + 0.5 - var0.x, var1x[1] + 0.5 - var0.z)));
      LinkedHashMap var17 = new LinkedHashMap();
      int var18 = 0;

      for (int[] var10 : var6) {
         if (var18++ >= 361 || var17.size() >= 4096) {
            break;
         }

         int var20 = var10[0];
         int var12 = var10[1];
         List var13 = var3.C(var20, var12);
         if (var13.size() <= 32) {
            List var14 = N(var13, var20, var12, var0.y);

            for (n.o var16 : var14) {
               w(var17, var16, i(var14, var16, 0), 0, var0, var4);
               w(var17, var16, i(var14, var16, 1), 1, var0, var4);
               w(var17, var16, i(var14, var16, 2), 2, var0, var4);
               w(var17, var16, i(var14, var16, 3), 3, var0, var4);
               if (var17.size() >= 4096) {
                  break;
               }
            }
         }
      }

      return List.copyOf(var17.values());
   }

   private static List<n.o> N(List<AABB> var0, int var1, int var2, double var3) {
      TreeSet var5 = new TreeSet<>(List.of((double)var1, var1 + 1.0));
      TreeSet var6 = new TreeSet<>(List.of((double)var2, var2 + 1.0));

      for (AABB var8 : var0) {
         var5.add(Math.clamp(var8.minX, var1, var1 + 1));
         var5.add(Math.clamp(var8.maxX, var1, var1 + 1));
         var6.add(Math.clamp(var8.minZ, var2, var2 + 1));
         var6.add(Math.clamp(var8.maxZ, var2, var2 + 1));
      }

      if ((var5.size() - 1) * (var6.size() - 1) > 128) {
         return List.of();
      }

      Double[] var14 = var5.toArray(Double[]::new);
      Double[] var15 = var6.toArray(Double[]::new);
      ArrayList var9 = new ArrayList();

      for (int var10 = 0; var10 + 1 < var14.length; var10++) {
         for (int var11 = 0; var11 + 1 < var15.length; var11++) {
            double var12 = Q(var0, (var14[var10] + var14[var10 + 1]) / 2.0, (var15[var11] + var15[var11 + 1]) / 2.0, var3);
            if (Double.isFinite(var12)) {
               var9.add(new n.o(var14[var10], var15[var11], var14[var10 + 1], var15[var11 + 1], var12));
            }
         }
      }

      return var9;
   }

   public static double Q(List<AABB> var0, double var1, double var3, double var5) {
      ArrayList var7 = new ArrayList();

      for (AABB var9 : var0) {
         if (y(var9, var1, var3)) {
            var7.add(var9);
         }
      }

      var7.sort(Comparator.comparingDouble(var0x -> var0x.minY));
      double var15 = Double.NaN;
      int var10 = 0;

      while (var10 < var7.size()) {
         double var11 = ((AABB)var7.get(var10)).minY;
         double var13 = ((AABB)var7.get(var10++)).maxY;

         while (var10 < var7.size() && ((AABB)var7.get(var10)).minY <= var13 + 0.001) {
            var13 = Math.max(var13, ((AABB)var7.get(var10++)).maxY);
         }

         if (!(var13 < var5 - 1.5)
            && !(var13 > var5 + 1.05)
            && (!(var13 > var5 + 0.05) || !(var11 > var5 + 0.05))
            && (!Double.isFinite(var15) || Math.abs(var13 - var5) < Math.abs(var15 - var5))) {
            var15 = var13;
         }
      }

      return var15;
   }

   private static boolean y(AABB var0, double var1, double var3) {
      return var1 >= var0.minX && var1 < var0.maxX && var3 >= var0.minZ && var3 < var0.maxZ;
   }

   private static double i(List<n.o> var0, n.o var1, int var2) {
      double var3 = (var1.x0 + var1.x1) / 2.0;
      double var5 = (var1.z0 + var1.z1) / 2.0;
      if (var2 == 0) {
         var5 = var1.z0 - 1.0E-4;
      }

      if (var2 == 1) {
         var3 = var1.x1 + 1.0E-4;
      }

      if (var2 == 2) {
         var5 = var1.z1 + 1.0E-4;
      }

      if (var2 == 3) {
         var3 = var1.x0 - 1.0E-4;
      }

      for (n.o var8 : var0) {
         if (var3 >= var8.x0 && var3 < var8.x1 && var5 >= var8.z0 && var5 < var8.z1) {
            return var8.y;
         }
      }

      return Double.NaN;
   }

   private static void w(Map<String, n.p> var0, n.o var1, double var2, int var4, Vec3 var5, double var6) {
      if (!(Math.abs(var2 - var1.y) < 0.001) && var0.size() < 4096) {
         double var10 = var1.y + 0.012;
         Vec3 var8;
         Vec3 var9;
         if (var4 == 0) {
            var8 = new Vec3(var1.x0, var10, var1.z0);
            var9 = new Vec3(var1.x1, var10, var1.z0);
         } else if (var4 == 1) {
            var8 = new Vec3(var1.x1, var10, var1.z0);
            var9 = new Vec3(var1.x1, var10, var1.z1);
         } else if (var4 == 2) {
            var8 = new Vec3(var1.x0, var10, var1.z1);
            var9 = new Vec3(var1.x1, var10, var1.z1);
         } else {
            var8 = new Vec3(var1.x0, var10, var1.z0);
            var9 = new Vec3(var1.x0, var10, var1.z1);
         }

         double var12 = Math.hypot((var8.x + var9.x) / 2.0 - var5.x, (var8.z + var9.z) / 2.0 - var5.z);
         if (!(var12 > var6)) {
            String var14 = var8.toString() + "/" + var9;
            var0.putIfAbsent(var14, new n.p(var8, var9, var12));
         }
      }
   }

   private n() {
   }

   public interface f {
      List<AABB> C(int var1, int var2);
   }

   private record o(double x0, double z0, double x1, double z1, double y) {
   }

   public record p(Vec3 from, Vec3 to, double distance) {
   }
}

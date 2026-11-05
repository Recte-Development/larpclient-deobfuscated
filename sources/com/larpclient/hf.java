package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public record hf(AABB bounds, List<hf.v> edges, List<AABB> boxes) {
   public static final int MAX_EDGES = 128;
   public static final int MAX_BOXES = 32;

   public static hf build(VoxelShape var0) {
      if (var0 != null && !var0.isEmpty()) {
         AABB var1 = var0.bounds();
         ArrayList var2 = new ArrayList();
         boolean[] var3 = new boolean[]{false};
         var0.forAllEdges(
            (var3x, var5, var7, var9, var11, var13) -> {
               if (var2.size() >= 128) {
                  var3[0] = (boolean)1;
               } else {
                  var2.add(
                     new hf.v(
                        normal(var3x, var1.minX, var1.maxX),
                        normal(var5, var1.minY, var1.maxY),
                        normal(var7, var1.minZ, var1.maxZ),
                        normal(var9, var1.minX, var1.maxX),
                        normal(var11, var1.minY, var1.maxY),
                        normal(var13, var1.minZ, var1.maxZ)
                     )
                  );
               }
            }
         );
         ArrayList var4 = new ArrayList();
         var0.forAllBoxes(
            (var3x, var5, var7, var9, var11, var13) -> {
               if (var4.size() >= 32) {
                  var3[0] = (boolean)1;
               } else {
                  var4.add(
                     new AABB(
                        normal(var3x, var1.minX, var1.maxX),
                        normal(var5, var1.minY, var1.maxY),
                        normal(var7, var1.minZ, var1.maxZ),
                        normal(var9, var1.minX, var1.maxX),
                        normal(var11, var1.minY, var1.maxY),
                        normal(var13, var1.minZ, var1.maxZ)
                     )
                  );
               }
            }
         );
         return !var3[0] && !var2.isEmpty() ? new hf(var1, List.copyOf(var2), List.copyOf(var4)) : null;
      } else {
         return null;
      }
   }

   private static double normal(double var0, double var2, double var4) {
      return var4 - var2 < 1.0E-8 ? 0.5 : (var0 - var2) / (var4 - var2);
   }

   public static double map(double var0, double var2, double var4) {
      return var2 + var0 * (var4 - var2);
   }

   public record v(double ax, double ay, double az, double bx, double by, double bz) {
   }
}

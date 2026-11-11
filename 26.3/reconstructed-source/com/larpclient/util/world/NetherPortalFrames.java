package com.larpclient.util.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public final class NetherPortalFrames {
   public static final int portalHeight = 4;

   public static boolean isFrameValid(BlockPos var0, int var1, NetherPortalFrames.CellClassifier var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;
      int var6 = 0;

      while (var6 < var5) {
         BlockPos var7 = var0.offset(var3 * var6, 0, var4 * var6);
         if (var2.classify(var7) != NetherPortalFrames.CellType.EMPTY) {
            return false;
         }

         if (var1 == 2 || var2.classify(var7.north()) == NetherPortalFrames.CellType.SOLID && var2.classify(var7.south()) == NetherPortalFrames.CellType.SOLID) {
            if (var1 == 1 || var2.classify(var7.east()) == NetherPortalFrames.CellType.SOLID && var2.classify(var7.west()) == NetherPortalFrames.CellType.SOLID
               )
             {
               var6++;
               continue;
            }

            return false;
         }

         return false;
      }

      return var1 == 0
         || var2.classify(var0.offset(-var3, 0, -var4)) == NetherPortalFrames.CellType.SOLID
            && var2.classify(var0.offset(var3 * 3, 0, var4 * 3)) == NetherPortalFrames.CellType.SOLID;
   }

   public static int findFrameAxis(BlockPos var0, int var1, NetherPortalFrames.CellClassifier var2) {
      if (var0.getY() <= var1 - 3 && var2.classify(var0) == NetherPortalFrames.CellType.EMPTY) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!isFrameValid(var0.above(var5), var3, var2)) {
                  var4 = 0;
                  break;
               }
            }

            if (var4 != 0) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static AABB frameBox(BlockPos var0, int var1) {
      return new AABB(var0.getX(), var0.getY(), var0.getZ(), var0.getX() + (var1 == 1 ? 3 : 1), var0.getY() + 4, var0.getZ() + (var1 == 2 ? 3 : 1));
   }

   private NetherPortalFrames() {
   }

   @FunctionalInterface
   public interface CellClassifier {
      NetherPortalFrames.CellType classify(BlockPos var1);
   }

   public enum CellType {
      EMPTY,
      SOLID,
      OTHER;
   }
}

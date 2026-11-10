package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class lx {
   public static final double k = 5.0;

   public static float C(LivingEntity var0, BlockPos var1, BlockPos var2) {
      return var0 instanceof Player var3 && var3.getAbilities().invulnerable ? 0.0F : B(var0, var1, T(var0, var1, var2));
   }

   public static float B(LivingEntity var0, BlockPos var1, float var2) {
      if (var0 instanceof Player var3 && var3.getAbilities().invulnerable) {
         return 0.0F;
      } else {
         double var5 = var0.position().distanceTo(Vec3.atCenterOf(var1));
         return var5 > 10.0 ? 0.0F : zu.E(var0, zu.z(var5, var2, 5.0));
      }
   }

   public static float T(LivingEntity var0, BlockPos var1, BlockPos var2) {
      Vec3 var3 = Vec3.atCenterOf(var1);
      if (var0.position().distanceTo(var3) > 10.0) {
         return 0.0F;
      }

      Level var4 = var0.level();
      return A(var3, var0.getBoundingBox(), var1, var2, (var2x, var3x) -> var4.clip(new ClipContext(var2x, var3x, Block.COLLIDER, Fluid.NONE, var0)));
   }

   public static List<BlockPos> E(BlockPos var0, Vec3 var1) {
      Vec3 var2 = var1.subtract(Vec3.atCenterOf(var0));
      ArrayList var3 = new ArrayList(26);

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            for (int var6 = -1; var6 <= 1; var6++) {
               if ((var4 != 0 || var5 != 0 || var6 != 0) && var4 * var2.x + var5 * var2.y + var6 * var2.z > 0.0) {
                  var3.add(var0.offset(var4, var5, var6));
               }
            }
         }
      }

      return var3;
   }

   public static float A(Vec3 var0, AABB var1, BlockPos var2, BlockPos var3, BiFunction<Vec3, Vec3, BlockHitResult> var4) {
      double var5 = 1.0 / ((var1.maxX - var1.minX) * 2.0 + 1.0);
      double var7 = 1.0 / ((var1.maxY - var1.minY) * 2.0 + 1.0);
      double var9 = 1.0 / ((var1.maxZ - var1.minZ) * 2.0 + 1.0);
      if (!(var5 < 0.0) && !(var7 < 0.0) && !(var9 < 0.0)) {
         double var11 = (1.0 - Math.floor(1.0 / var5) * var5) / 2.0;
         double var13 = (1.0 - Math.floor(1.0 / var9) * var9) / 2.0;
         AABB var15 = var3 == null ? null : new AABB(var3);
         int var16 = 0;
         int var17 = 0;

         for (double var18 = 0.0; var18 <= 1.0; var18 += var5) {
            for (double var20 = 0.0; var20 <= 1.0; var20 += var7) {
               for (double var22 = 0.0; var22 <= 1.0; var22 += var9) {
                  Vec3 var24 = new Vec3(
                     Mth.lerp(var18, var1.minX, var1.maxX) + var11, Mth.lerp(var20, var1.minY, var1.maxY), Mth.lerp(var22, var1.minZ, var1.maxZ) + var13
                  );
                  BlockHitResult var25 = (BlockHitResult)var4.apply(var24, var0);
                  int var26 = var25.getType() == Type.MISS ? 1 : 0;
                  if ((var26 != 0 || var25.getBlockPos().equals(var2))
                     && (var15 == null || !var15.contains(var24) && var15.clip(var24, var26 != 0 ? var0 : var25.getLocation()).isEmpty())) {
                     var16++;
                  }

                  var17++;
               }
            }
         }

         return (float)var16 / var17;
      } else {
         return 0.0F;
      }
   }

   private lx() {
   }
}

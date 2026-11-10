package com.larpclient.module;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class v {
   private v() {
   }

   public static double E(LocalPlayer var0, AABB var1) {
      Vec3 var2 = var0.getEyePosition();
      Vec3 var3 = new Vec3(Mth.clamp(var2.x, var1.minX, var1.maxX), Mth.clamp(var2.y, var1.minY, var1.maxY), Mth.clamp(var2.z, var1.minZ, var1.maxZ));
      return var3.subtract(var2).length();
   }

   public static boolean n(LocalPlayer var0) {
      return var0 != null && var0.level() != null
         ? !var0.hasEffect(MobEffects.LEVITATION)
            && !var0.hasEffect(MobEffects.BLINDNESS)
            && !var0.isInWater()
            && !var0.isInLava()
            && !var0.getAbilities().flying
            && !var0.isFallFlying()
            && !var0.onClimbable()
            && !var0.isPassenger()
         : false;
   }

   public static boolean N(LocalPlayer var0) {
      if (!n(var0)) {
         return false;
      } else {
         return r() ? !var0.onGround() : var0.fallDistance > 0.0 && !var0.onGround();
      }
   }

   public static boolean B(LocalPlayer var0, AABB var1, float var2, float var3, double var4, boolean var6) {
      Vec3 var7 = var0.getEyePosition();
      Vec3 var8 = w.Y(var2, var3);
      Vec3 var9 = var7.add(var8.scale(var4 + 1.0));
      double var10 = var4;
      if (!var6) {
         BlockHitResult var12 = var0.level().clip(new ClipContext(var7, var9, Block.COLLIDER, Fluid.NONE, var0));
         if (var12.getType() != Type.MISS) {
            var10 = var7.distanceTo(var12.getLocation());
         }
      }

      Vec3 var13 = var7.add(var8.scale(var10));
      return var1.clip(var7, var13).isPresent() || var1.contains(var7);
   }

   public static EntityHitResult F(LocalPlayer var0, float var1, float var2, double var3, boolean var5) {
      if (var0 != null && var0.level() != null && !(var3 <= 0.0)) {
         Vec3 var6 = var0.getEyePosition();
         Vec3 var7 = w.Y(var1, var2);
         Vec3 var8 = var6.add(var7.scale(var3));
         double var9 = var3 * var3;
         if (!var5) {
            BlockHitResult var11 = var0.level().clip(new ClipContext(var6, var8, Block.COLLIDER, Fluid.NONE, var0));
            if (var11.getType() != Type.MISS) {
               var9 = var6.distanceToSqr(var11.getLocation());
            }
         }

         AABB var12 = var0.getBoundingBox().expandTowards(var7.scale(var3)).inflate(1.0);
         return ProjectileUtil.getEntityHitResult(
            var0, var6, var8, var12, var1x -> var1x != var0 && var1x.isPickable() && var1x.isAlive() && !var1x.isSpectator(), var9
         );
      } else {
         return null;
      }
   }

   public static boolean Z(Vec3 var0, double var1, AABB var3) {
      LocalPlayer var4 = Minecraft.getInstance().player;
      if (var4 == null) {
         return false;
      }

      Vec3 var5 = var4.getEyePosition();
      if (var3.contains(var5)) {
         return true;
      }

      Optional var6 = var3.clip(var5, var5.add(var0.scale(var1)));
      return var6.isPresent();
   }

   public static boolean d() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null && var0.isUsingItem()) {
         ItemStack var1 = var0.getUseItem();
         if (var1.isEmpty()) {
            return false;
         }

         if (var1.has(DataComponents.FOOD)) {
            return true;
         }

         ItemUseAnimation var2 = var1.getUseAnimation();
         return var2 == ItemUseAnimation.EAT || var2 == ItemUseAnimation.DRINK;
      } else {
         return false;
      }
   }

   public static boolean r() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null && var0.level != null) {
         BlockPos var1 = var0.player.blockPosition();
         if (!var0.level.getBlockState(var1).is(Blocks.COBWEB)
            && !var0.level.getBlockState(var1.above()).is(Blocks.COBWEB)
            && !var0.level.getBlockState(BlockPos.containing(var0.player.getX(), var0.player.getY() + 0.5, var0.player.getZ())).is(Blocks.COBWEB)
            && !var0.level.getBlockState(BlockPos.containing(var0.player.getX(), var0.player.getY() + 1.2F, var0.player.getZ())).is(Blocks.COBWEB)) {
            AABB var2 = var0.player.getBoundingBox();

            for (int var3 = var1.getX() - 2; var3 <= var1.getX() + 2; var3++) {
               for (int var4 = var1.getY() - 1; var4 <= var1.getY() + 4; var4++) {
                  for (int var5 = var1.getZ() - 2; var5 <= var1.getZ() + 2; var5++) {
                     BlockPos var6 = new BlockPos(var3, var4, var5);
                     if (var2.intersects(new AABB(var6)) && var0.level.getBlockState(var6).is(Blocks.COBWEB)) {
                        return true;
                     }
                  }
               }
            }

            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static Vec3 Q(Entity var0) {
      Vec3 var1 = Minecraft.getInstance().player.getEyePosition();
      AABB var2 = var0.getBoundingBox();
      return new Vec3(Mth.clamp(var1.x, var2.minX, var2.maxX), Mth.clamp(var1.y, var2.minY, var2.maxY), Mth.clamp(var1.z, var2.minZ, var2.maxZ));
   }
}

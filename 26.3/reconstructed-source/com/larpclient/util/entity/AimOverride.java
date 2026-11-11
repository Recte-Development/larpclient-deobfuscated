package com.larpclient.util.entity;

import com.larpclient.core.RotationManager;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class AimOverride {
   private static LocalPlayer player;
   private static BlockHitResult target;
   private static BooleanSupplier readyCondition;
   private static int startTick;
   private static boolean rotationApplied;
   private static int releaseTick = -1;

   public static void setTarget(Minecraft var0, BlockHitResult var1, BooleanSupplier var2) {
      if (player != var0.player || target == null || var0.player.tickCount - startTick > 5) {
         player = var0.player;
         target = var1;
         readyCondition = var2;
         startTick = player.tickCount;
         rotationApplied = (boolean)0;
         releaseTick = -1;
      }
   }

   private static boolean isActive(LocalPlayer var0) {
      return var0 == player
         && target != null
         && var0.tickCount - startTick <= 5
         && Minecraft.getInstance().player == var0
         && !var0.isPassenger()
         && !RotationManager.hasRequest();
   }

   public static float getYaw(LocalPlayer var0) {
      if (!isActive(var0)) {
         return var0.getYRot();
      }

      rotationApplied = (boolean)1;
      Vec3 var1 = target.getLocation().subtract(var0.getEyePosition());
      return (float)Math.toDegrees(Math.atan2(-var1.x, var1.z));
   }

   public static float getPitch(LocalPlayer var0) {
      if (!isActive(var0)) {
         return var0.getXRot();
      }

      Vec3 var1 = target.getLocation().subtract(var0.getEyePosition());
      return (float)(-Math.toDegrees(Math.atan2(var1.y, Math.hypot(var1.x, var1.z))));
   }

   public static void tick(LocalPlayer var0) {
      if (isActive(var0) && rotationApplied) {
         if (releaseTick < 0) {
            releaseTick = var0.tickCount;
            return;
         }

         if (var0.tickCount <= releaseTick) {
            return;
         }

         if (readyCondition != null) {
            if (!readyCondition.getAsBoolean()) {
               return;
            }

            readyCondition = null;
            releaseTick = var0.tickCount - 1;
         }

         if (var0.tickCount <= releaseTick + 2) {
            return;
         }
      }

      player = null;
      target = null;
      readyCondition = null;
      rotationApplied = (boolean)0;
      releaseTick = -1;
   }
}

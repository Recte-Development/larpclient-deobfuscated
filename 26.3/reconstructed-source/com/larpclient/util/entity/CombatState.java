package com.larpclient.util.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class CombatState {
   private static double lastY;
   private static double fallDistance;
   private static boolean hasPosition;
   private static boolean onGround = (boolean)1;
   private static boolean sprinting;
   private static float yaw;
   private static float pitch;
   private static boolean hasRotation;

   public static synchronized void onPacket(Packet<?> var0) {
      if (var0 instanceof ServerboundMovePlayerPacket var1) {
         if (var1.hasRotation()) {
            yaw = var1.getYRot(yaw);
            pitch = var1.getXRot(pitch);
            hasRotation = (boolean)1;
         }

         double var3 = var1.getY(lastY);
         if (var1.isOnGround()) {
            fallDistance = 0.0;
         } else if (var1.hasPosition() && hasPosition) {
            double var5 = lastY - var3;
            fallDistance = var5 < 0.0 ? 0.0 : fallDistance + var5;
         }

         if (var1.hasPosition()) {
            lastY = var3;
            hasPosition = (boolean)1;
         }

         onGround = var1.isOnGround();
      } else if (var0 instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING) {
            sprinting = (boolean)1;
         } else if (var2.getAction() == Action.STOP_SPRINTING) {
            sprinting = (boolean)0;
         }
      }
   }

   public static synchronized void reset() {
      hasPosition = (boolean)0;
      fallDistance = 0.0;
      lastY = 0.0;
      onGround = (boolean)1;
      sprinting = (boolean)0;
      hasRotation = (boolean)0;
   }

   public static synchronized boolean isSprinting(boolean var0) {
      return sprinting == var0;
   }

   public static synchronized Vec3 getLookVector(Vec3 var0) {
      return hasRotation ? Vec3.directionFromRotation(pitch, yaw) : var0;
   }

   public static boolean canCritical(Player var0, Entity var1) {
      if (!canCriticalClient(var0, var1)) {
         return false;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var2.getSingleplayerServer() != null && PlayerInputUtil.isModuleActive("NoFall")
         ? false
         : isSprinting(var0.getY(), var0.getDeltaMovement().y, var0.getAttackStrengthScale(0.0F));
   }

   public static boolean isSprinting(Player var0, Entity var1) {
      return var0 != null
         && canCriticalClient(
            var0.getDeltaMovement().y,
            var0.getAttackStrengthScale(1.0F),
            var0.onGround(),
            var0.onClimbable(),
            var0.isInWater() || var0.isInLava(),
            var0.isMobilityRestricted(),
            var0.isPassenger(),
            var0.getAbilities().flying || var0.isFallFlying(),
            var1 instanceof LivingEntity
         );
   }

   public static boolean canCriticalClient(
      double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9
   ) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean isSprinting(double var0, double var2, float var4) {
      return canCriticalFall(hasPosition, lastY, fallDistance, onGround, sprinting, var0, var2, var4);
   }

   public static boolean canCriticalFall(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean canCriticalClient(Player var0, Entity var1) {
      return var0 != null
         && canCriticalFall(
            var0.fallDistance,
            var0.getAttackStrengthScale(0.5F),
            var0.onGround(),
            var0.onClimbable(),
            var0.isInWater(),
            var0.isMobilityRestricted(),
            var0.isPassenger(),
            var0.isSprinting(),
            var1 instanceof LivingEntity
         );
   }

   public static boolean canCriticalFall(
      double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9
   ) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private CombatState() {
   }
}

package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class pl {
   private static double b;
   private static double J;
   private static boolean Q;
   private static boolean D = (boolean)1;
   private static boolean o;
   private static float u;
   private static float I;
   private static boolean F;

   public static synchronized void l(Packet<?> var0) {
      if (var0 instanceof ServerboundMovePlayerPacket var1) {
         if (var1.hasRotation()) {
            u = var1.getYRot(u);
            I = var1.getXRot(I);
            F = (boolean)1;
         }

         double var3 = var1.getY(b);
         if (var1.isOnGround()) {
            J = 0.0;
         } else if (var1.hasPosition() && Q) {
            double var5 = b - var3;
            J = var5 < 0.0 ? 0.0 : J + var5;
         }

         if (var1.hasPosition()) {
            b = var3;
            Q = (boolean)1;
         }

         D = var1.isOnGround();
      } else if (var0 instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING) {
            o = (boolean)1;
         } else if (var2.getAction() == Action.STOP_SPRINTING) {
            o = (boolean)0;
         }
      }
   }

   public static synchronized void l() {
      Q = (boolean)0;
      J = 0.0;
      b = 0.0;
      D = (boolean)1;
      o = (boolean)0;
      F = (boolean)0;
   }

   public static synchronized boolean I(boolean var0) {
      return o == var0;
   }

   public static synchronized Vec3 b(Vec3 var0) {
      return F ? Vec3.directionFromRotation(I, u) : var0;
   }

   public static boolean B(Player var0, Entity var1) {
      if (!A(var0, var1)) {
         return false;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var2.getSingleplayerServer() != null && te.i("NoFall") ? false : H(var0.getY(), var0.getDeltaMovement().y, var0.getAttackStrengthScale(0.0F));
   }

   public static boolean E(Player var0, Entity var1) {
      return var0 != null
         && W(
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

   public static boolean W(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean H(double var0, double var2, float var4) {
      return Z(Q, b, J, D, o, var0, var2, var4);
   }

   public static boolean Z(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean A(Player var0, Entity var1) {
      return var0 != null
         && Y(
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

   public static boolean Y(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private pl() {
   }
}

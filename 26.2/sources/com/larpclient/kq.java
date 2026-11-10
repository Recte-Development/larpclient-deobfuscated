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

public final class kq {
   private static double B;
   private static double G;
   private static boolean N;
   private static boolean r = (boolean)1;
   private static boolean x;
   private static float X;
   private static float T;
   private static boolean M;

   public static synchronized void X(Packet<?> var0) {
      if (var0 instanceof ServerboundMovePlayerPacket var1) {
         if (var1.hasRotation()) {
            X = var1.getYRot(X);
            T = var1.getXRot(T);
            M = (boolean)1;
         }

         double var3 = var1.getY(B);
         if (var1.isOnGround()) {
            G = 0.0;
         } else if (var1.hasPosition() && N) {
            double var5 = B - var3;
            G = var5 < 0.0 ? 0.0 : G + var5;
         }

         if (var1.hasPosition()) {
            B = var3;
            N = (boolean)1;
         }

         r = var1.isOnGround();
      } else if (var0 instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING) {
            x = (boolean)1;
         } else if (var2.getAction() == Action.STOP_SPRINTING) {
            x = (boolean)0;
         }
      }
   }

   public static synchronized void Q() {
      N = (boolean)0;
      G = 0.0;
      B = 0.0;
      r = (boolean)1;
      x = (boolean)0;
      M = (boolean)0;
   }

   public static synchronized boolean M(boolean var0) {
      return x == var0;
   }

   public static synchronized Vec3 r(Vec3 var0) {
      return M ? Vec3.directionFromRotation(T, X) : var0;
   }

   public static boolean A(Player var0, Entity var1) {
      if (!V(var0, var1)) {
         return false;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var2.getSingleplayerServer() != null && c5.t("NoFall") ? false : o(var0.getY(), var0.getDeltaMovement().y, var0.getAttackStrengthScale(0.0F));
   }

   public static boolean k(Player var0, Entity var1) {
      return var0 != null
         && j(
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

   public static boolean j(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean o(double var0, double var2, float var4) {
      return h(N, B, G, r, x, var0, var2, var4);
   }

   public static boolean h(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean V(Player var0, Entity var1) {
      return var0 != null
         && C(
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

   public static boolean C(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private kq() {
   }
}

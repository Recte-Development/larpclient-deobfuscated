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

public final class xq {
   private static double c;
   private static double t;
   private static boolean S;
   private static boolean i = (boolean)1;
   private static boolean O;
   private static float k;
   private static float B;
   private static boolean Z;

   public static synchronized void V(Packet<?> var0) {
      if (var0 instanceof ServerboundMovePlayerPacket var1) {
         if (var1.hasRotation()) {
            k = var1.getYRot(k);
            B = var1.getXRot(B);
            Z = (boolean)1;
         }

         double var3 = var1.getY(c);
         if (var1.isOnGround()) {
            t = 0.0;
         } else if (var1.hasPosition() && S) {
            double var5 = c - var3;
            t = var5 < 0.0 ? 0.0 : t + var5;
         }

         if (var1.hasPosition()) {
            c = var3;
            S = (boolean)1;
         }

         i = var1.isOnGround();
      } else if (var0 instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING) {
            O = (boolean)1;
         } else if (var2.getAction() == Action.STOP_SPRINTING) {
            O = (boolean)0;
         }
      }
   }

   public static synchronized void y() {
      S = (boolean)0;
      t = 0.0;
      c = 0.0;
      i = (boolean)1;
      O = (boolean)0;
      Z = (boolean)0;
   }

   public static synchronized boolean E(boolean var0) {
      return O == var0;
   }

   public static synchronized Vec3 K(Vec3 var0) {
      return Z ? Vec3.directionFromRotation(B, k) : var0;
   }

   public static boolean k(Player var0, Entity var1) {
      if (!M(var0, var1)) {
         return false;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var2.getSingleplayerServer() != null && yu.G("NoFall") ? false : J(var0.getY(), var0.getDeltaMovement().y, var0.getAttackStrengthScale(0.0F));
   }

   public static boolean l(Player var0, Entity var1) {
      return var0 != null
         && p(
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

   public static boolean p(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean J(double var0, double var2, float var4) {
      return e(S, c, t, i, O, var0, var2, var4);
   }

   public static boolean e(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean M(Player var0, Entity var1) {
      return var0 != null
         && v(
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

   public static boolean v(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private xq() {
   }
}

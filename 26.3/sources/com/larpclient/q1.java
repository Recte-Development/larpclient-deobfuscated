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

public final class q1 {
   private static double y;
   private static double V;
   private static boolean g;
   private static boolean f = (boolean)1;
   private static boolean c;
   private static float w;
   private static float H;
   private static boolean p;

   public static synchronized void S(Packet<?> var0) {
      if (var0 instanceof ServerboundMovePlayerPacket var1) {
         if (var1.hasRotation()) {
            w = var1.getYRot(w);
            H = var1.getXRot(H);
            p = (boolean)1;
         }

         double var3 = var1.getY(y);
         if (var1.isOnGround()) {
            V = 0.0;
         } else if (var1.hasPosition() && g) {
            double var5 = y - var3;
            V = var5 < 0.0 ? 0.0 : V + var5;
         }

         if (var1.hasPosition()) {
            y = var3;
            g = (boolean)1;
         }

         f = var1.isOnGround();
      } else if (var0 instanceof ServerboundPlayerCommandPacket var2) {
         if (var2.getAction() == Action.START_SPRINTING) {
            c = (boolean)1;
         } else if (var2.getAction() == Action.STOP_SPRINTING) {
            c = (boolean)0;
         }
      }
   }

   public static synchronized void r() {
      g = (boolean)0;
      V = 0.0;
      y = 0.0;
      f = (boolean)1;
      c = (boolean)0;
      p = (boolean)0;
   }

   public static synchronized boolean h(boolean var0) {
      return c == var0;
   }

   public static synchronized Vec3 Q(Vec3 var0) {
      return p ? Vec3.directionFromRotation(H, w) : var0;
   }

   public static boolean B(Player var0, Entity var1) {
      if (!t(var0, var1)) {
         return false;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var2.getSingleplayerServer() != null && nz.X("NoFall") ? false : h(var0.getY(), var0.getDeltaMovement().y, var0.getAttackStrengthScale(0.0F));
   }

   public static boolean h(Player var0, Entity var1) {
      return var0 != null
         && t(
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

   public static boolean t(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var0 < 0.0 && var2 >= 1.0F && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   public static synchronized boolean h(double var0, double var2, float var4) {
      return H(g, y, V, f, c, var0, var2, var4);
   }

   public static boolean H(boolean var0, double var1, double var3, boolean var5, boolean var6, double var7, double var9, float var11) {
      return var0 && !var5 && !var6 && var3 > 0.0 && Math.abs(var7 - var1) <= 2.0E-4 && var9 < 0.0 && var11 >= 1.0F;
   }

   public static boolean t(Player var0, Entity var1) {
      return var0 != null
         && H(
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

   public static boolean H(double var0, float var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7, boolean var8, boolean var9) {
      return var2 > 0.9F && var0 > 0.0 && !var3 && !var4 && !var5 && !var6 && !var7 && !var8 && var9;
   }

   private q1() {
   }
}

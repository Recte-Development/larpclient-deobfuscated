package com.larpclient.util.math;

import com.larpclient.LarpClient;
import com.larpclient.module.combat.AntiKnockbackModule;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.world.phys.Vec3;

public final class KnockbackScaler {
   static final long hIT_WINDOW_NANOS = 250000000L;
   private static long hitTimeNanos;
   private static boolean hitPending;
   private static Vec3 scaledAccumulated = Vec3.ZERO;

   public static Vec3 scale(Vec3 var0, double var1, double var3) {
      return new Vec3(var0.x * var1, var0.y * var3, var0.z * var1);
   }

   public static Vec3 lerpScaled(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(scale(var1.subtract(var0), var2, var4));
   }

   public static Vec3 scaleVelocity(Vec3 var0, boolean var1) {
      AntiKnockbackModule var2 = activeModule();
      if (var2 == null) {
         return var0;
      }

      Vec3 var3 = scale(var0, var2.getHorizontalMultiplier(), var2.getVerticalMultiplier());
      if (var1 && hitPending) {
         scaledAccumulated = scaledAccumulated.add(var3);
      }

      return var3;
   }

   public static void markHit() {
      hitPending = (boolean)1;
      hitTimeNanos = System.nanoTime();
      scaledAccumulated = Vec3.ZERO;
   }

   public static Vec3 applyHitKnockback(Vec3 var0, Vec3 var1) {
      if (!hitPending) {
         return var1;
      }

      hitPending = (boolean)0;
      long var2 = System.nanoTime() - hitTimeNanos;
      Vec3 var4 = scaledAccumulated;
      scaledAccumulated = Vec3.ZERO;
      if (var2 > 250000000L) {
         return var1;
      }

      AntiKnockbackModule var5 = activeModule();
      if (var5 == null) {
         return var1;
      }

      double var6 = var5.getHorizontalMultiplier();
      double var8 = var5.getVerticalMultiplier();
      Vec3 var10 = lerpScaled(var0.subtract(var4), var1, var6, var8);
      LarpLog.info(
         "AntiKnockback",
         "Hit knockback scaled after {} ms: server ({}, {}, {}) -> ({}, {}, {}) at {}/{}",
         var2 / 1000000L,
         round3(var1.x),
         round3(var1.y),
         round3(var1.z),
         round3(var10.x),
         round3(var10.y),
         round3(var10.z),
         var6,
         var8
      );
      return var10;
   }

   private static AntiKnockbackModule activeModule() {
      LarpClient var0 = LarpClient.getInstance();
      if (var0 != null && var0.getModuleManager() != null) {
         AntiKnockbackModule var1 = var0.getModuleManager().getModule(AntiKnockbackModule.class);
         return var1 != null && var1.isRuntimeActive() ? var1 : null;
      } else {
         return null;
      }
   }

   private static double round3(double var0) {
      return Math.round(var0 * 1000.0) / 1000.0;
   }

   private KnockbackScaler() {
   }
}

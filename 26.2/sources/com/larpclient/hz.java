package com.larpclient;

import com.larpclient.module.AntiKnockbackModule;
import net.minecraft.world.phys.Vec3;

public final class hz {
   static final long b = 250000000L;
   private static long J;
   private static boolean F;
   private static Vec3 y = Vec3.ZERO;

   public static Vec3 d(Vec3 var0, double var1, double var3) {
      return new Vec3(var0.x * var1, var0.y * var3, var0.z * var1);
   }

   public static Vec3 n(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(d(var1.subtract(var0), var2, var4));
   }

   public static Vec3 i(Vec3 var0, boolean var1) {
      AntiKnockbackModule var2 = z();
      if (var2 == null) {
         return var0;
      }

      Vec3 var3 = d(var0, var2.Y(), var2.z());
      if (var1 && F) {
         y = y.add(var3);
      }

      return var3;
   }

   public static void a() {
      F = (boolean)1;
      J = System.nanoTime();
      y = Vec3.ZERO;
   }

   public static Vec3 V(Vec3 var0, Vec3 var1) {
      if (!F) {
         return var1;
      }

      F = (boolean)0;
      long var2 = System.nanoTime() - J;
      Vec3 var4 = y;
      y = Vec3.ZERO;
      if (var2 > 250000000L) {
         return var1;
      }

      AntiKnockbackModule var5 = z();
      if (var5 == null) {
         return var1;
      }

      double var6 = var5.Y();
      double var8 = var5.z();
      Vec3 var10 = n(var0.subtract(var4), var1, var6, var8);
      h.A(
         "AntiKnockback",
         "Hit knockback scaled after {} ms: server ({}, {}, {}) -> ({}, {}, {}) at {}/{}",
         var2 / 1000000L,
         M(var1.x),
         M(var1.y),
         M(var1.z),
         M(var10.x),
         M(var10.y),
         M(var10.z),
         var6,
         var8
      );
      return var10;
   }

   private static AntiKnockbackModule z() {
      LarpClient var0 = LarpClient.R();
      if (var0 != null && var0.z() != null) {
         AntiKnockbackModule var1 = var0.z().p(AntiKnockbackModule.class);
         return var1 != null && var1.isRuntimeActive() ? var1 : null;
      } else {
         return null;
      }
   }

   private static double M(double var0) {
      return Math.round(var0 * 1000.0) / 1000.0;
   }

   private hz() {
   }
}

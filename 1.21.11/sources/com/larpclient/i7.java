package com.larpclient;

import com.larpclient.module.AntiKnockbackModule;
import net.minecraft.class_243;

public final class i7 {
   static final long c = 250000000L;
   private static long e;
   private static boolean h;
   private static class_243 n = class_243.field_1353;

   public static class_243 S(class_243 var0, double var1, double var3) {
      return new class_243(var0.field_1352 * var1, var0.field_1351 * var3, var0.field_1350 * var1);
   }

   public static class_243 u(class_243 var0, class_243 var1, double var2, double var4) {
      return var0.method_1019(S(var1.method_1020(var0), var2, var4));
   }

   public static class_243 D(class_243 var0, boolean var1) {
      AntiKnockbackModule var2 = n();
      if (var2 == null) {
         return var0;
      }

      class_243 var3 = S(var0, var2.x(), var2.f());
      if (var1 && h) {
         n = n.method_1019(var3);
      }

      return var3;
   }

   public static void K() {
      h = (boolean)1;
      e = System.nanoTime();
      n = class_243.field_1353;
   }

   public static class_243 O(class_243 var0, class_243 var1) {
      if (!h) {
         return var1;
      }

      h = (boolean)0;
      long var2 = System.nanoTime() - e;
      class_243 var4 = n;
      n = class_243.field_1353;
      if (var2 > 250000000L) {
         return var1;
      }

      AntiKnockbackModule var5 = n();
      if (var5 == null) {
         return var1;
      }

      double var6 = var5.x();
      double var8 = var5.f();
      class_243 var10 = u(var0.method_1020(var4), var1, var6, var8);
      q8.G(
         "AntiKnockback",
         "Hit knockback scaled after {} ms: server ({}, {}, {}) -> ({}, {}, {}) at {}/{}",
         var2 / 1000000L,
         J(var1.field_1352),
         J(var1.field_1351),
         J(var1.field_1350),
         J(var10.field_1352),
         J(var10.field_1351),
         J(var10.field_1350),
         var6,
         var8
      );
      return var10;
   }

   private static AntiKnockbackModule n() {
      LarpClient var0 = LarpClient.b();
      if (var0 != null && var0.G() != null) {
         AntiKnockbackModule var1 = var0.G().b(AntiKnockbackModule.class);
         return var1 != null && var1.isRuntimeActive() ? var1 : null;
      } else {
         return null;
      }
   }

   private static double J(double var0) {
      return Math.round(var0 * 1000.0) / 1000.0;
   }

   private i7() {
   }
}

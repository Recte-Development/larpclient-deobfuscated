package com.larpclient;

import com.larpclient.module.AntiKnockbackModule;
import net.minecraft.world.phys.Vec3;

public final class j_ {
   static final long p = 250000000L;
   private static long B;
   private static boolean P;
   private static Vec3 I = Vec3.ZERO;

   public static Vec3 A(Vec3 var0, double var1, double var3) {
      return new Vec3(var0.x * var1, var0.y * var3, var0.z * var1);
   }

   public static Vec3 r(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(A(var1.subtract(var0), var2, var4));
   }

   public static Vec3 a(Vec3 var0, boolean var1) {
      AntiKnockbackModule var2 = H();
      if (var2 == null) {
         return var0;
      }

      Vec3 var3 = A(var0, var2.A(), var2.j());
      if (var1 && P) {
         I = I.add(var3);
      }

      return var3;
   }

   public static void K() {
      P = (boolean)1;
      B = System.nanoTime();
      I = Vec3.ZERO;
   }

   public static Vec3 o(Vec3 var0, Vec3 var1) {
      if (!P) {
         return var1;
      }

      P = (boolean)0;
      long var2 = System.nanoTime() - B;
      Vec3 var4 = I;
      I = Vec3.ZERO;
      if (var2 > 250000000L) {
         return var1;
      }

      AntiKnockbackModule var5 = H();
      if (var5 == null) {
         return var1;
      }

      double var6 = var5.A();
      double var8 = var5.j();
      Vec3 var10 = r(var0.subtract(var4), var1, var6, var8);
      _q.z(
         "AntiKnockback",
         "Hit knockback scaled after {} ms: server ({}, {}, {}) -> ({}, {}, {}) at {}/{}",
         var2 / 1000000L,
         b(var1.x),
         b(var1.y),
         b(var1.z),
         b(var10.x),
         b(var10.y),
         b(var10.z),
         var6,
         var8
      );
      return var10;
   }

   private static AntiKnockbackModule H() {
      LarpClient var0 = LarpClient.N();
      if (var0 != null && var0.j() != null) {
         AntiKnockbackModule var1 = var0.j().y(AntiKnockbackModule.class);
         return var1 != null && var1.isRuntimeActive() ? var1 : null;
      } else {
         return null;
      }
   }

   private static double b(double var0) {
      return Math.round(var0 * 1000.0) / 1000.0;
   }

   private j_() {
   }
}

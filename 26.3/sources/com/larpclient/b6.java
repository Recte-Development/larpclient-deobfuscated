package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class b6 {
   private static b6.w V;
   private static Module n;
   private static float F;
   private static float C;
   private static int r;
   private static long H;
   private static Object W;
   private static int p = Integer.MIN_VALUE;
   private static float w;
   private static float O;
   private static float l;
   private static float Z;

   public static void b() {
      V = null;
      n = null;
   }

   public static boolean c(b6.w var0) {
      return V == var0;
   }

   public static boolean x() {
      return V != null;
   }

   public static boolean T(b6.w var0, b6.w var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void f(b6.w var0, float var1, float var2, double var3) {
      LocalPlayer var5 = Minecraft.getInstance().player;
      if (var5 != null) {
         E(var0, var1, var2, var3, var5.getYRot(), var5.getXRot());
      }
   }

   public static void E(b6.w var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == V || T(var0, V)) {
         V = var0;
         n = null;
         float[] var7 = U(var5, var6, var1, var2, var3);
         F = var7[0];
         C = var7[1];
      }
   }

   public static void P(Module var0, b6.w var1, float var2, float var3, double var4) {
      f(var1, var2, var3, var4);
      if (V == var1) {
         n = var0;
      }
   }

   public static float[] U(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + Mth.wrapDegrees(var2 - var0) * (float)var6, Mth.clamp(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void e() {
      Minecraft var0 = Minecraft.getInstance();
      if (V != null && _p.D(n) && (n == null || n.isRuntimeActive())) {
         if (_p.A()) {
            E();
         } else {
            float var1 = var0.player.getYRot();
            float var2 = var0.player.getXRot();
            var0.player.setYRot(F);
            var0.player.setXRot(C);
            if (V == b6.w.x) {
               W = var0.player;
               p = var0.player.tickCount;
               w = var1;
               O = var2;
               l = F;
               Z = C;
            }
         }
      }
   }

   public static float O(LocalPlayer var0, float var1) {
      return !i(var0) ? Float.NaN : w + Mth.wrapDegrees(l - w) * Mth.clamp(var1, 0.0F, 1.0F);
   }

   public static float G(LocalPlayer var0, float var1) {
      return !i(var0) ? Float.NaN : Mth.lerp(Mth.clamp(var1, 0.0F, 1.0F), O, Z);
   }

   private static boolean i(LocalPlayer var0) {
      return var0 == W && var0.tickCount == p && !var0.isPassenger() && var0.getYRot() == l && var0.getXRot() == Z && !ij.y();
   }

   private static void E() {
      r++;
      long var0 = System.nanoTime();
      if (H == 0L || var0 - H >= 5000000000L) {
         _q.z("Rotation", "Held {} turn for your own right-click so the use and movement facing match ({} held since last note)", V, r);
         H = var0;
         r = 0;
      }
   }

   public static float[] R(Vec3 var0, Vec3 var1) {
      Vec3 var2 = var1.subtract(var0);
      return new float[]{(float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z))))};
   }

   private b6() {
   }

   public enum w {
      b,
      p,
      d,
      x,
      y,
      F,
      P,
      q,
      S,
      W,
      X,
      G,
      c;
   }
}

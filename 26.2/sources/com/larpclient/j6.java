package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class j6 {
   private static j6.l u;
   private static Module P;
   private static float J;
   private static float i;
   private static int j;
   private static long w;
   private static Object y;
   private static int F = Integer.MIN_VALUE;
   private static float V;
   private static float X;
   private static float o;
   private static float c;

   public static void R() {
      u = null;
      P = null;
   }

   public static boolean g(j6.l var0) {
      return u == var0;
   }

   public static boolean j() {
      return u != null;
   }

   public static boolean d(j6.l var0, j6.l var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void C(j6.l var0, float var1, float var2, double var3) {
      LocalPlayer var5 = Minecraft.getInstance().player;
      if (var5 != null) {
         k(var0, var1, var2, var3, var5.getYRot(), var5.getXRot());
      }
   }

   public static void k(j6.l var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == u || d(var0, u)) {
         u = var0;
         P = null;
         float[] var7 = s(var5, var6, var1, var2, var3);
         J = var7[0];
         i = var7[1];
      }
   }

   public static void U(Module var0, j6.l var1, float var2, float var3, double var4) {
      C(var1, var2, var3, var4);
      if (u == var1) {
         P = var0;
      }
   }

   public static float[] s(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + Mth.wrapDegrees(var2 - var0) * (float)var6, Mth.clamp(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void n() {
      Minecraft var0 = Minecraft.getInstance();
      if (u != null && ta.S(P) && (P == null || P.isRuntimeActive())) {
         if (ta.i()) {
            d();
         } else {
            float var1 = var0.player.getYRot();
            float var2 = var0.player.getXRot();
            var0.player.setYRot(J);
            var0.player.setXRot(i);
            if (u == j6.l.o) {
               y = var0.player;
               F = var0.player.tickCount;
               V = var1;
               X = var2;
               o = J;
               c = i;
            }
         }
      }
   }

   public static float D(LocalPlayer var0, float var1) {
      return !P(var0) ? Float.NaN : V + Mth.wrapDegrees(o - V) * Mth.clamp(var1, 0.0F, 1.0F);
   }

   public static float B(LocalPlayer var0, float var1) {
      return !P(var0) ? Float.NaN : Mth.lerp(Mth.clamp(var1, 0.0F, 1.0F), X, c);
   }

   private static boolean P(LocalPlayer var0) {
      return var0 == y && var0.tickCount == F && !var0.isPassenger() && var0.getYRot() == o && var0.getXRot() == c && !p_.V();
   }

   private static void d() {
      j++;
      long var0 = System.nanoTime();
      if (w == 0L || var0 - w >= 5000000000L) {
         h.A("Rotation", "Held {} turn for your own right-click so the use and movement facing match ({} held since last note)", u, j);
         w = var0;
         j = 0;
      }
   }

   public static float[] s(Vec3 var0, Vec3 var1) {
      Vec3 var2 = var1.subtract(var0);
      return new float[]{(float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z))))};
   }

   private j6() {
   }

   public enum l {
      F,
      E,
      s,
      o,
      w,
      p,
      J,
      D,
      T,
      N,
      m,
      b,
      P;
   }
}

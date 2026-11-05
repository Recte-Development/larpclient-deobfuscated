package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class ep {
   private static ep.z I;
   private static Module G;
   private static float B;
   private static float z;
   private static int J;
   private static long u;

   public static void U() {
      I = null;
      G = null;
   }

   public static boolean W(ep.z var0) {
      return I == var0;
   }

   public static boolean f() {
      return I != null;
   }

   public static boolean I(ep.z var0, ep.z var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void C(ep.z var0, float var1, float var2, double var3) {
      LocalPlayer var5 = Minecraft.getInstance().player;
      if (var5 != null) {
         q(var0, var1, var2, var3, var5.getYRot(), var5.getXRot());
      }
   }

   public static void q(ep.z var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == I || I(var0, I)) {
         I = var0;
         G = null;
         float[] var7 = x(var5, var6, var1, var2, var3);
         B = var7[0];
         z = var7[1];
      }
   }

   public static void t(Module var0, ep.z var1, float var2, float var3, double var4) {
      C(var1, var2, var3, var4);
      if (I == var1) {
         G = var0;
      }
   }

   public static float[] x(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + Mth.wrapDegrees(var2 - var0) * (float)var6, Mth.clamp(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void u() {
      Minecraft var0 = Minecraft.getInstance();
      if (I != null && d8.r(G) && (G == null || G.isRuntimeActive())) {
         if (d8.p()) {
            v();
         } else {
            var0.player.setYRot(B);
            var0.player.setXRot(z);
         }
      }
   }

   private static void v() {
      J++;
      long var0 = System.nanoTime();
      if (u == 0L || var0 - u >= 5000000000L) {
         r4.V("Rotation", "Held {} turn for your own right-click so the use and movement facing match ({} held since last note)", I, J);
         u = var0;
         J = 0;
      }
   }

   public static float[] g(Vec3 var0, Vec3 var1) {
      Vec3 var2 = var1.subtract(var0);
      return new float[]{(float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z))))};
   }

   private ep() {
   }

   public enum z {
      Y,
      W,
      R,
      z,
      d,
      n,
      J,
      D,
      O,
      F,
      q,
      v;
   }
}

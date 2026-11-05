package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class m9 {
   private static m9.s d;
   private static Module K;
   private static float q;
   private static float y;
   private static int x;
   private static long e;

   public static void S() {
      d = null;
      K = null;
   }

   public static boolean F(m9.s var0) {
      return d == var0;
   }

   public static boolean R() {
      return d != null;
   }

   public static boolean Z(m9.s var0, m9.s var1) {
      return var1 == null || var0.ordinal() > var1.ordinal();
   }

   public static void c(m9.s var0, float var1, float var2, double var3) {
      LocalPlayer var5 = Minecraft.getInstance().player;
      if (var5 != null) {
         t(var0, var1, var2, var3, var5.getYRot(), var5.getXRot());
      }
   }

   public static void t(m9.s var0, float var1, float var2, double var3, float var5, float var6) {
      if (var0 == d || Z(var0, d)) {
         d = var0;
         K = null;
         float[] var7 = W(var5, var6, var1, var2, var3);
         q = var7[0];
         y = var7[1];
      }
   }

   public static void k(Module var0, m9.s var1, float var2, float var3, double var4) {
      c(var1, var2, var3, var4);
      if (d == var1) {
         K = var0;
      }
   }

   public static float[] W(float var0, float var1, float var2, float var3, double var4) {
      double var6 = var4 <= 0.0 ? 1.0 : Math.clamp(var4, 0.0, 1.0);
      return new float[]{var0 + Mth.wrapDegrees(var2 - var0) * (float)var6, Mth.clamp(var1 + (var3 - var1) * (float)var6, -90.0F, 90.0F)};
   }

   public static void j() {
      Minecraft var0 = Minecraft.getInstance();
      if (d != null && _m.n(K) && (K == null || K.isRuntimeActive())) {
         if (_m.A()) {
            q();
         } else {
            var0.player.setYRot(q);
            var0.player.setXRot(y);
         }
      }
   }

   private static void q() {
      x++;
      long var0 = System.nanoTime();
      if (e == 0L || var0 - e >= 5000000000L) {
         mt.K("Rotation", "Held {} turn for your own right-click so the use and movement facing match ({} held since last note)", d, x);
         e = var0;
         x = 0;
      }
   }

   public static float[] Z(Vec3 var0, Vec3 var1) {
      Vec3 var2 = var1.subtract(var0);
      return new float[]{(float)Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(var2.y, Math.hypot(var2.x, var2.z))))};
   }

   private m9() {
   }

   public enum s {
      D,
      F,
      w,
      u,
      p,
      t,
      X,
      o,
      x,
      Z,
      q,
      H;
   }
}

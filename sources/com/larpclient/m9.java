package com.larpclient;

import java.util.Locale;
import java.util.function.BiPredicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class m9 {
   public static final float i = 1.15F;
   public static final double s = 0.35;
   public static final double R = -0.3;
   public static final double X = 0.1;
   public static final double N = 16.0;
   public static final double P = 1.8;
   public static final double j = 0.3;

   public static int q(m9.e[] var0, boolean var1, boolean var2) {
      int var3 = -1;
      int var4 = 0;
      float var5 = 0.0F;

      for (m9.e var9 : var0) {
         if (var9 != null && var9.mace() && !var9.breaksNextUse() && (var1 || var9.breach() != 0)) {
            int var10 = var1 && var2 && var9.windBurst() > 0 ? 1 : 0;
            if (var3 < 0 || (var4 == 0 || var10 != 0) && (var4 != var10 || !(var9.damage() <= var5))) {
               var3 = var9.slot();
               var4 = var10;
               var5 = var9.damage();
            }
         }
      }

      return var3;
   }

   public static boolean b(boolean var0, float var1, float var2) {
      return var0 || !(var1 < var2 * 1.15F);
   }

   public static double s(double var0) {
      if (var0 <= 3.0) {
         return 4.0 * var0;
      } else {
         return var0 <= 8.0 ? 12.0 + 2.0 * (var0 - 3.0) : 22.0 + var0 - 8.0;
      }
   }

   public static float c(float var0, float var1, float var2, int var3) {
      float var4 = Math.clamp(var1 - var0 / (2.0F + var2 / 4.0F), var1 * 0.2F, 20.0F) / 25.0F;
      return var0 * (1.0F - Math.clamp(var4 - 0.15F * var3, 0.0F, 1.0F));
   }

   public static float x(
      double var0, float var2, double var3, boolean var5, boolean var6, int var7, boolean var8, int var9, float var10, float var11, int var12
   ) {
      float var13 = (float)(1.0 + var0) * (0.2F + 0.8F * var2 * var2);
      if (var5 && var6) {
         var13 += (float)(s(var3) + 0.5 * var7 * var3);
      }

      if (var8) {
         var13 *= 1.5F;
      }

      if (var9 > 0) {
         var13 += var2 * (0.5F * var9 + 0.5F);
      }

      return c(var13, var10, var11, var12);
   }

   public static boolean G(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, boolean var8, boolean var9) {
      return var0 && !var1 && var2 && var3 >= var5 && (var7 || var8 && var9);
   }

   public static boolean r(boolean var0, boolean var1, float var2, float var3, int var4, boolean var5) {
      int var6 = var2 <= 0.9F && var3 > 0.9F ? 1 : 0;
      return var0 && (var1 || var6 != 0 || var4 > 0 || var5);
   }

   public static m9.a y(String var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      if (!var1 || var2 || !var3) {
         return m9.a.c;
      } else if (var4 && !"Off".equals(var0)) {
         return "Next Tick".equals(var0) ? m9.a.G : m9.a.j;
      } else {
         return m9.a.t;
      }
   }

   public static String K(
      boolean var0,
      boolean var1,
      boolean var2,
      boolean var3,
      boolean var4,
      boolean var5,
      boolean var6,
      double var7,
      double var9,
      boolean var11,
      boolean var12,
      boolean var13,
      boolean var14
   ) {
      if (var0) {
         return "another action this tick";
      } else if (var1) {
         return "your own click this tick";
      } else if (var2) {
         return "an item use this tick";
      } else if (var3) {
         return "another rotation owner";
      } else if (!var4) {
         return "busy hands";
      } else if (!var5) {
         return "not falling";
      } else if (!var6) {
         return "fall of 1.5 blocks not reached";
      } else if (var7 < var9) {
         return String.format(Locale.ROOT, "fall %.1f under Min Fall %.1f", var7, var9);
      } else if (var11) {
         return "smash already landed this fall";
      } else if (var12) {
         return "waiting for the server to confirm the last smash";
      } else if (!var13) {
         return "no usable mace";
      } else {
         return !var14 ? "facing not on the target yet" : null;
      }
   }

   public static boolean q(double var0, double var2, int var4, int var5) {
      return var0 < -0.3 && var2 > -0.1 || var5 > var4;
   }

   public static int K(int var0) {
      return Math.max(0, var0) + 3;
   }

   public static double T(double var0, boolean var2, double var3, int var5, double var6) {
      return var2 && !(var3 <= 0.0) ? Math.max(var0, Math.min(16.0, var6 + var3 * (Math.max(0, var5) + 1))) : var0;
   }

   public static double g(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var2 - (var4 + 1.8));
      return !(var0 <= 0.0) && !(var6 >= var0) ? Math.sqrt(var0 * var0 - var6 * var6) + 0.3 : 0.0;
   }

   public static boolean H(boolean var0, double var1, Vec3 var3, Vec3 var4, AABB var5, Vec3 var6, float var7, Vec3 var8, BiPredicate<Vec3, Vec3> var9) {
      double var10 = var1 - 0.35;
      if (var0 && !(var10 <= 0.0)) {
         Vec3 var12 = var3.add(var4);
         AABB var13 = var5.move(var6).inflate(var7);
         return var13.contains(var12) ? true : var13.clip(var12, var12.add(var8.scale(var10))).filter(var2 -> var9.test(var12, var2)).isPresent();
      } else {
         return false;
      }
   }

   private m9() {
   }

   public enum a {
      c,
      j,
      G,
      t;
   }

   public record e(int slot, boolean mace, boolean breaksNextUse, int breach, int windBurst, float damage) {
   }
}

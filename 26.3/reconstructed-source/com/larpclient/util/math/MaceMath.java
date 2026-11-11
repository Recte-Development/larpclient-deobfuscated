package com.larpclient.util.math;

import java.util.Locale;
import java.util.function.BiPredicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class MaceMath {
   public static final float dAMAGE_MARGIN = 1.15F;
   public static final double rEACH_PADDING = 0.35;
   public static final double mIN_FALL_VELOCITY = -0.3;
   public static final double vELOCITY_EPSILON = 0.1;
   public static final double mAX_REACH = 16.0;
   public static final double hITBOX_OFFSET = 1.8;
   public static final double rEACH_BONUS = 0.3;

   public static int selectMaceSlot(MaceMath.Anonymous[] var0, boolean var1, boolean var2) {
      int var3 = -1;
      int var4 = 0;
      float var5 = 0.0F;

      for (MaceMath.Anonymous var9 : var0) {
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

   public static boolean hasEnoughDamage(boolean var0, float var1, float var2) {
      return var0 || !(var1 < var2 * 1.15F);
   }

   public static double fallBonusDamage(double var0) {
      if (var0 <= 3.0) {
         return 4.0 * var0;
      } else {
         return var0 <= 8.0 ? 12.0 + 2.0 * (var0 - 3.0) : 22.0 + var0 - 8.0;
      }
   }

   public static float applyArmorReduction(float var0, float var1, float var2, int var3) {
      float var4 = Math.clamp(var1 - var0 / (2.0F + var2 / 4.0F), var1 * 0.2F, 20.0F) / 25.0F;
      return var0 * (1.0F - Math.clamp(var4 - 0.15F * var3, 0.0F, 1.0F));
   }

   public static float calculateDamage(
      double var0, float var2, double var3, boolean var5, boolean var6, int var7, boolean var8, int var9, float var10, float var11, int var12
   ) {
      float var13 = (float)(1.0 + var0) * (0.2F + 0.8F * var2 * var2);
      if (var5 && var6) {
         var13 += (float)(fallBonusDamage(var3) + 0.5 * var7 * var3);
      }

      if (var8) {
         var13 *= 1.5F;
      }

      if (var9 > 0) {
         var13 += var2 * (0.5F * var9 + 0.5F);
      }

      return applyArmorReduction(var13, var10, var11, var12);
   }

   public static boolean canSmash(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, boolean var8, boolean var9) {
      return var0 && !var1 && var2 && var3 >= var5 && (var7 || var8 && var9);
   }

   public static boolean shouldAttack(boolean var0, boolean var1, float var2, float var3, int var4, boolean var5) {
      int var6 = var2 <= 0.9F && var3 > 0.9F ? 1 : 0;
      return var0 && (var1 || var6 != 0 || var4 > 0 || var5);
   }

   public static MaceMath.SmashMode resolveSmashMode(String var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      if (!var1 || var2 || !var3) {
         return MaceMath.SmashMode.NONE;
      } else if (var4 && !"Off".equals(var0)) {
         return "Next Tick".equals(var0) ? MaceMath.SmashMode.NEXT_TICK : MaceMath.SmashMode.SAME_TICK;
      } else {
         return MaceMath.SmashMode.NORMAL;
      }
   }

   public static String blockReason(
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

   public static boolean shouldAbortFall(double var0, double var2, int var4, int var5) {
      return var0 < -0.3 && var2 > -0.1 || var5 > var4;
   }

   public static int smashTicks(int var0) {
      return Math.max(0, var0) + 3;
   }

   public static double extendedReach(double var0, boolean var2, double var3, int var5, double var6) {
      return var2 && !(var3 <= 0.0) ? Math.max(var0, Math.min(16.0, var6 + var3 * (Math.max(0, var5) + 1))) : var0;
   }

   public static double horizontalReachAtHeight(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var2 - (var4 + 1.8));
      return !(var0 <= 0.0) && !(var6 >= var0) ? Math.sqrt(var0 * var0 - var6 * var6) + 0.3 : 0.0;
   }

   public static boolean isPathClear(boolean var0, double var1, Vec3 var3, Vec3 var4, AABB var5, Vec3 var6, float var7, Vec3 var8, BiPredicate<Vec3, Vec3> var9) {
      double var10 = var1 - 0.35;
      if (var0 && !(var10 <= 0.0)) {
         Vec3 var12 = var3.add(var4);
         AABB var13 = var5.move(var6).inflate(var7);
         return var13.contains(var12) ? true : var13.clip(var12, var12.add(var8.scale(var10))).filter(var2 -> var9.test(var12, var2)).isPresent();
      } else {
         return false;
      }
   }

   private MaceMath() {
   }

   public record Anonymous(int slot, boolean mace, boolean breaksNextUse, int breach, int windBurst, float damage) {
   }

   public enum SmashMode {
      NONE,
      SAME_TICK,
      NEXT_TICK,
      NORMAL;
   }
}

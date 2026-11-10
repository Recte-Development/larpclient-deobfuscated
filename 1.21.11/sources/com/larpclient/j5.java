package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.class_11515;
import net.minecraft.class_1922;
import net.minecraft.class_2302;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2756;
import net.minecraft.class_3481;
import net.minecraft.class_3486;
import net.minecraft.class_3609;
import net.minecraft.class_3610;
import net.minecraft.class_4696;
import net.minecraft.class_777;
import net.minecraft.class_7923;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2350.class_2351;

public final class j5 {
   private static final int B = 1;
   private static final int k = 2;
   private static final int T = 3;
   private static final int w = 4;
   private static final Set<String> u = Set.of(
      "short_grass",
      "tall_grass",
      "short_dry_grass",
      "tall_dry_grass",
      "fern",
      "large_fern",
      "dead_bush",
      "sweet_berry_bush",
      "azalea",
      "flowering_azalea",
      "pink_petals",
      "leaf_litter",
      "wildflowers",
      "bush",
      "firefly_bush",
      "cactus_flower",
      "torchflower_crop",
      "pitcher_crop",
      "pitcher_plant"
   );
   private static final ThreadLocal<j5.r> y = new ThreadLocal<>();
   private static final ThreadLocal<Integer> M = new ThreadLocal<>();
   private static final ThreadLocal<j5.h> v = new ThreadLocal<>();
   private static volatile boolean f;
   private static volatile int b = 3;
   public static final int H = 252;
   public static final int o = 251;
   public static final int Y = 249;
   private static volatile boolean s;
   private static volatile boolean V;
   private static volatile boolean W;
   private static volatile boolean G;
   private static volatile boolean a;

   public static void e() {
      SnowGroundModule var0 = SnowGroundModule.R();
      f = (boolean)(var0 != null && var0.isRuntimeActive() ? 1 : 0);
      b = var0 != null && var0.w() >= 2 ? 3 : 2;
      WavingFoliageModule var1 = WavingFoliageModule.y();
      s = (boolean)(var1 != null && var1.isRuntimeActive() ? 1 : 0);
      V = (boolean)(s && var1.S.getValue() ? 1 : 0);
      W = (boolean)(V && var1.K.getValue() ? 1 : 0);
      G = (boolean)(s && var1.v.getValue() ? 1 : 0);
      RealisticWaterModule var2 = RealisticWaterModule.C();
      a = (boolean)(var2 != null && var2.isRuntimeActive() ? 1 : 0);
   }

   public static boolean f(class_2680 var0, float var1, class_1922 var2, class_2338 var3) {
      return E(var0, 0.0F, var1, 0.0F, var2, var3);
   }

   public static boolean E(class_2680 var0, float var1, float var2, float var3, class_1922 var4, class_2338 var5) {
      if (!f && !s) {
         return false;
      }

      y.remove();
      v.remove();
      if (f && qs.Y() && var0.method_26227().method_15769()) {
         class_265 var6 = var0.method_26220(var4, var5);
         if (!var6.method_1110()) {
            float var7 = (float)var6.method_1105(class_2351.field_11052);
            boolean var8 = var4.method_8320(var5.method_10084()).method_26206(var4, var5.method_10084(), class_2350.field_11033);
            ArrayList var9 = new ArrayList();
            if (!var8) {
               for (int var10 = 0; var10 <= 1; var10++) {
                  for (int var11 = -1; var11 <= 1; var11++) {
                     for (int var12 = -1; var12 <= 1; var12++) {
                        if (var11 != 0 || var10 != 0 || var12 != 0) {
                           class_2338 var13 = var5.method_10069(var11, var10, var12);
                           class_2680 var14 = var4.method_8320(var13);
                           if (!var14.method_26215()) {
                              for (class_238 var16 : var14.method_26220(var4, var13).method_1090()) {
                                 if (!(var10 + var16.field_1322 > var7 + 0.55F) && !(var10 + var16.field_1325 <= var7 + 0.001)) {
                                    var9.add(var16.method_989(var11, var10, var12));
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            v.set(
               new j5.h(
                  var1, var2, var3, var8 ? Float.POSITIVE_INFINITY : var2 + var7, class_4696.method_23679(var0) == class_11515.field_60923, List.copyOf(var9)
               )
            );
         }
      }

      if (s && !qs.k()) {
         int var17 = f(var0);
         if ((var17 != 1 && var17 != 2 || V) && (var17 != 3 || G) && (var17 != 4 || W) && var17 != 0) {
            float var18 = !W || var17 != 4 && var17 != 1 ? -1.0F : V(var0, var17, var4, var5);
            if (var17 == 4 && var18 < 0.0F) {
               return true;
            }

            int var19 = var0.method_28498(class_2741.field_12533) && var0.method_11654(class_2741.field_12533) == class_2756.field_12609 ? 1 : 0;
            y.set(new j5.r(var17, var2, var18, (boolean)var19));
            return true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public static void O() {
      y.remove();
      v.remove();
   }

   static boolean N() {
      return f && qs.Y();
   }

   public static void b(class_2680 var0, class_2338 var1, class_1922 var2, class_2338 var3) {
      if (f) {
         if (var2 != null && class_4696.method_23679(var0) != class_11515.field_60926) {
            E(var0, var1.method_10263(), var1.method_10264(), var1.method_10260(), var2, var3);
         } else {
            O();
         }
      }
   }

   public static void n() {
      if (f) {
         O();
      }
   }

   static boolean K() {
      return f && v.get() != null;
   }

   public static boolean J() {
      j5.h var0 = v.get();
      return f && var0 != null && Float.isFinite(var0.top) && y.get() == null;
   }

   public static int P(class_777 var0, int[] var1) {
      j5.h var2 = v.get();
      if (f && var2 != null && Float.isFinite(var2.top) && var2.solid && y.get() == null && var0.comp_3723() != class_2350.field_11033) {
         int var3 = 0;
         int var4 = 0;

         for (int var5 = 0; var5 < 4; var5++) {
            if (Math.abs(var0.method_76648(var5).y() + var2.y - var2.top) < 0.001F) {
               var3++;
            }

            var4 = Math.max(var4, var1[var5] >>> 16 & 65535);
         }

         return var3 >= 2 && var4 >= 224 ? b : 0;
      } else {
         return 0;
      }
   }

   public static int k(int var0, float var1) {
      return y(var0, Float.NaN, var1, Float.NaN);
   }

   public static int y(int var0, float var1, float var2, float var3) {
      if (!f && !s) {
         return var0;
      }

      j5.r var4 = y.get();
      if (var4 == null) {
         j5.h var11 = v.get();
         if (var11 == null) {
            return var0;
         }

         short var12 = 251;
         if (var2 >= var11.top - 0.001F) {
            var12 = 252;
            float var14 = var1 - var11.x;
            float var8 = var3 - var11.z;

            for (class_238 var10 : var11.blockers) {
               if (var14 >= var10.field_1323 - 0.001
                  && var14 <= var10.field_1320 + 0.001
                  && var8 >= var10.field_1321 - 0.001
                  && var8 <= var10.field_1324 + 0.001) {
                  var12 = 249;
                  break;
               }
            }
         }

         return var0 & 16777215 | var12 << 24;
      } else {
         float var5 = Math.clamp(var2 - var4.blockY, 0.0F, 1.0F);
         int var6;
         if (var4.height >= 0.0F) {
            float var7 = var4.height + var5;
            var6 = var4.kind == 4 ? 100 + Math.clamp(Math.round(var7 * 2.0F), 0, 49) : 2 + Math.clamp(Math.round(var7 * 4.0F), 0, 97);
         } else if (var4.kind == 1) {
            var6 = 254;
         } else if (var4.kind == 2) {
            var6 = 253;
         } else {
            float var13 = var4.upper ? 1.0F + var5 * 0.8F : var5;
            if (var13 < 0.001F) {
               return var0;
            }

            var6 = 150 + Math.clamp(Math.round(var13 * 50.0F), 0, 90);
         }

         return var0 & 16777215 | var6 << 24;
      }
   }

   public static boolean w(class_1922 var0, class_2338 var1, class_3610 var2) {
      if (!a) {
         return false;
      }

      M.remove();
      if (!qs.k() && var2.method_15767(class_3486.field_15517)) {
         short var3;
         if (ex.P(var1)) {
            var3 = 150;
         } else if (!var2.method_15771() && var2.method_28498(class_3609.field_15902) && (Boolean)var2.method_11654(class_3609.field_15902)) {
            var3 = 160;
         } else {
            class_243 var4 = var2.method_15758(var0, var1);
            var3 = (short)(var4.field_1352 == 0.0 && var4.field_1350 == 0.0
               ? 224
               : 192 + ((int)Math.round(Math.atan2(var4.field_1350, var4.field_1352) * 32.0 / (Math.PI * 2)) & 31));
         }

         M.set(Integer.valueOf(var3));
         return true;
      } else {
         return true;
      }
   }

   public static int G(int var0) {
      if (!a) {
         return var0;
      }

      Integer var1 = M.get();
      return var1 == null ? var0 : var0 & 16777215 | var1 << 24;
   }

   public static void d() {
      M.remove();
   }

   private static int f(class_2680 var0) {
      String var1 = class_7923.field_41175.method_10221(var0.method_26204()).method_12832();
      if (var1.endsWith("_leaves")) {
         return 1;
      } else if (var1.equals("vine")) {
         return 2;
      } else if ((var1.endsWith("_log") || var1.endsWith("_stem")) && !var1.startsWith("stripped_")) {
         return 4;
      } else {
         return !u.contains(var1)
               && !var1.endsWith("_sapling")
               && !var0.method_26164(class_3481.field_20339)
               && !var0.method_26164(class_3481.field_20341)
               && !(var0.method_26204() instanceof class_2302)
            ? 0
            : 3;
      }
   }

   private static boolean e(class_2680 var0) {
      return f(var0) == 1 && (!var0.method_28498(class_2741.field_12514) || !(Boolean)var0.method_11654(class_2741.field_12514));
   }

   private static float V(class_2680 var0, int var1, class_1922 var2, class_2338 var3) {
      if (var1 == 1 && !e(var0)) {
         return -1.0F;
      }

      class_2339 var4 = var3.method_25503();
      int var5 = 0;
      if (var1 == 1) {
         byte var6 = 0;

         while (var5 < 24) {
            var4.method_10100(0, -1, 0);
            class_2680 var7 = var2.method_8320(var4);
            if (f(var7) == 4) {
               var5++;
               var6 = 1;
               break;
            }

            if (!var7.method_26215() && f(var7) != 1) {
               return -1.0F;
            }

            var5++;
         }

         if (var6 == 0) {
            return -1.0F;
         }
      } else {
         class_2339 var12 = var3.method_25503();

         for (int var14 = 0; var14 < 24 && f(var2.method_8320(var12.method_10084())) == 4; var14++) {
            var12.method_10100(0, 1, 0);
         }

         byte var15 = 0;

         for (class_2350 var11 : class_2350.values()) {
            if (var11 != class_2350.field_11033 && e(var2.method_8320(var12.method_10093(var11)))) {
               var15 = 1;
               break;
            }
         }

         if (var15 == 0) {
            return -1.0F;
         }
      }

      int var13;
      for (var13 = 0; var13 < 24 && f(var2.method_8320(var4.method_10074())) == 4; var13++) {
         var4.method_10100(0, -1, 0);
      }

      return var5 + var13;
   }

   private j5() {
   }

   private record h(float x, float y, float z, float top, boolean solid, List<class_238> blockers) {
   }

   private record r(int kind, float blockY, float height, boolean upper) {
   }
}

package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.yq;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_465;
import net.minecraft.class_490;
import net.minecraft.class_239.class_240;

public final class at {
   private static final long A = 30000000000L;
   private static final long u = 1200000000L;
   private static final long j = 1500000000L;
   private static final int l = -13180806;
   private static final int T = -2074034;
   private static at.h O;
   private static long v;
   private static long x;
   private static class_2338 R;
   private static long J;
   private static class_1703 W;
   private static int y = -1;
   private static long c;
   private static boolean k;

   private at() {
   }

   public static void Q(String var0, Predicate<class_2680> var1, String var2, Function<class_2338, String> var3) {
      class_310 var4 = class_310.method_1551();
      if (var4.field_1724 != null && var4.field_1687 != null) {
         J();
         O = new at.j(var0, var1, var2, var3);
         g(var4);
         var4.method_1507(null);
      } else {
         q8.G("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static void o(String var0, Predicate<class_1799> var1, String var2, Function<class_1799, String> var3) {
      class_310 var4 = class_310.method_1551();
      if (var4.field_1724 != null && var4.field_1687 != null) {
         J();
         O = new at.r(var0, var1, var2, var3);
         g(var4);
         var4.method_1507(new class_490(var4.field_1724));
      } else {
         q8.G("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static boolean z() {
      return O != null;
   }

   private static void g(class_310 var0) {
      long var1 = System.nanoTime();
      v = var1 + 30000000000L;
      x = var1;
      G(var0, O.prompt());
      q8.G("Rig", "Selection started: {}", O.prompt());
   }

   public static void L(String var0) {
      if (O != null) {
         O = null;
         G(class_310.method_1551(), "Selection " + var0);
         q8.G("Rig", "Selection {}", var0);
      }
   }

   private static void G(class_310 var0, String var1) {
      if (var0.field_1724 != null) {
         var0.field_1724.method_7353(class_2561.method_43470(var1), true);
      }
   }

   private static void J() {
      if (!k) {
         k = (boolean)1;
         ClientTickEvents.END_CLIENT_TICK.register(at::H);
         WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var0 -> g());
      }
   }

   private static void H(class_310 var0) {
      if (O != null) {
         if (var0.field_1724 != null && var0.field_1687 != null) {
            long var1 = System.nanoTime();
            if (var1 > v) {
               L("timed out");
            } else if (O instanceof at.j && var0.field_1755 != null) {
               L("cancelled");
            } else if (O instanceof at.r && !(var0.field_1755 instanceof class_465)) {
               L("cancelled");
            } else {
               if (O instanceof at.j && var1 - x > 1500000000L) {
                  x = var1;
                  G(var0, O.prompt());
               }
            }
         } else {
            O = null;
         }
      }
   }

   private static class_238 s(class_2338 var0) {
      return new class_238(
         var0.method_10263() - 0.002,
         var0.method_10264() - 0.002,
         var0.method_10260() - 0.002,
         var0.method_10263() + 1.002,
         var0.method_10264() + 1.002,
         var0.method_10260() + 1.002
      );
   }

   private static void g() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1687 != null) {
         if (O instanceof at.j var1 && var0.field_1765 instanceof class_3965 var2 && var2.method_17783() == class_240.field_1332) {
            boolean var6 = var1.valid().test(var0.field_1687.method_8320(var2.method_17777()));
            class_12180.method_75541(s(var2.method_17777()), class_12179.method_75536(var6 ? -13180806 : -2074034, 2.5F)).method_75533();
         }

         long var4 = System.nanoTime();
         if (R != null && var4 < J) {
            int var7 = (int)(110L * (J - var4) / 1.2E9);
            class_12180.method_75541(s(R), class_12179.method_75537(-13180806, 2.5F, var7 << 24 | 3596410)).method_75533();
         } else {
            R = null;
         }
      }
   }

   public static boolean i(class_310 var0) {
      if (!(O instanceof at.j var1 && var0.field_1687 != null)) {
         return false;
      } else if (var0.field_1765 instanceof class_3965 var5 && var5.method_17783() == class_240.field_1332) {
         class_2338 var6 = var5.method_17777().method_10062();
         if (!var1.valid().test(var0.field_1687.method_8320(var6))) {
            G(var0, var1.invalid());
            return true;
         } else {
            O = null;
            String var4 = var1.apply().apply(var6);
            R = var6;
            J = System.nanoTime() + 1200000000L;
            G(var0, var4);
            yq.E("Casino", var4);
            return true;
         }
      } else {
         G(var0, var1.prompt());
         return true;
      }
   }

   public static boolean o(class_1735 var0, class_1703 var1) {
      if (O instanceof at.r var2 && var0 != null) {
         class_310 var6 = class_310.method_1551();
         class_1799 var4 = var0.method_7677();
         if (!var4.method_7960() && var2.valid().test(var4)) {
            O = null;
            String var5 = var2.apply().apply(var4.method_7972());
            W = var1;
            y = var0.field_7874;
            c = System.nanoTime() + 1200000000L;
            G(var6, var5);
            yq.E("Casino", var5);
            return true;
         } else {
            G(var6, var2.invalid());
            return true;
         }
      } else {
         return false;
      }
   }

   public static int e(class_1735 var0, boolean var1, class_1703 var2) {
      long var3 = System.nanoTime();
      if (y >= 0 && var3 >= c) {
         y = -1;
         W = null;
      }

      if (y >= 0 && var2 == W && var0.field_7874 == y) {
         int var7 = (int)(150L * (c - var3) / 1.2E9);
         return var7 << 24 | 3596410;
      }

      if (O instanceof at.r var5 && !var0.method_7677().method_7960()) {
         boolean var8 = var5.valid().test(var0.method_7677());
         if (var1) {
            return var8 ? -2143887238 : -2132780466;
         } else {
            return var8 ? 808902778 : 0;
         }
      } else {
         return 0;
      }
   }

   private sealed interface h permits at.j, at.r {
      String prompt();
   }

   private record j(String prompt, Predicate<class_2680> valid, String invalid, Function<class_2338, String> apply) implements at.h {
   }

   private record r(String prompt, Predicate<class_1799> valid, String invalid, Function<class_1799, String> apply) implements at.h {
   }
}

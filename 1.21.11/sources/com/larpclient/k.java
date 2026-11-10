package com.larpclient;

import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.FakeFPSModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.IslandLyricsModule;
import com.larpclient.module.Module;
import com.larpclient.module.StaffListModule;
import com.larpclient.module.TargetHUDModule;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_10799;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_742;
import net.minecraft.class_7532;
import net.minecraft.class_8030;
import net.minecraft.class_897;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class k {
   private static final Map<k.l, List<cv.z>> Q = new LinkedHashMap<>();
   private static Object d;
   private static Object n;
   private static int B;
   private static int V;
   private static long m;
   private static List<k.i> z = List.of();
   private static long R;
   private static boolean v;
   public static final int U = 8;
   public static final int j = 220;
   private static final qb H = new qb(0.0);
   private static long x = Long.MIN_VALUE;
   private static k.l W;
   private static boolean a;
   private static final int L = 224;
   private static final int K = 52;
   private static final int o = 36;
   private static final int E = 12;
   private static final int b = 79;
   private static final List<k.c> S = List.of(
      new k.c("miner", "Auto Area Miner", sh::a, new tc.g("Layer 2 of 3", "64%", "Mining toward 120 64 -45", "4m 12s", 0.64)),
      new k.c("builder", "Auto Builder", s0::u, new tc.g("house", "64%", "Walking to 120 64 -45", "412 placed, 230 missing", 0.64)),
      new k.c(
         "pilot", "Auto Elytra Pilot", sb::m, new tc.g("12,340 blocks left, about 3m 10s", "64%", "Flying to 1000, -2500", "32 rockets, elytra 412/432", 0.64)
      ),
      new k.c("hunter", "Anchor Hunter", sz::Y, new tc.g("4 anchors, 24/h", "4 of 10", "Going to an anchor", "/rtp in 42s", 0.4))
   );
   public static final List<k.l> u = N(
      List.of(
         new k.l("watermark", 170, 22, 0.015, 0.02),
         new k.l("info", 178, 27, 0.015, 0.85),
         new k.l("modules", 130, 100, 0.98, 0.06),
         new k.l("target", 224, 52, 0.55, 0.62),
         new k.l("effects", 150, 58, 0.015, 0.7),
         new k.l("keybinds", 120, 58, 0.015, 0.35),
         new k.l("armor", 84, 22, 0.5, 0.9),
         new k.l("notifications", 260, 178, 0.98, 0.08),
         new k.l("crosshair", 84, 84, 0.5, 0.5),
         new k.l("speed", 172, 72, 0.015, 0.55),
         new k.l("cooldowns", 168, 48, 0.78, 0.08),
         new k.l("staffs", 168, 48, 0.35, 0.65),
         new k.l("inventory", 207, 94, 0.04, 0.48),
         new k.l("keystrokes", 81, 98, 0.985, 0.93),
         new k.l("island", 300, 20, 0.5, 0.015),
         new k.l("casino", 168, 140, 0.98, 0.5)
      )
   );
   private static final qb k = new qb(1.0);
   private static final qb h = new qb(0.0);
   private static final qb q = new qb(1.0);
   private static double I = 1.0;
   private static long D;
   private static final class_2960 i = class_2960.method_60656("hud/heart/container");
   private static final class_2960 c = class_2960.method_60656("hud/heart/full");
   private static class_1309 p;
   private static final ji w = new ji();
   private static float f;
   private static long Z;
   private static final hz.s l = new hz.s(1.0, false);
   private static final class_1304[] y = new class_1304[]{
      class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166, class_1304.field_6173, class_1304.field_6171
   };
   private static final Set<String> s = Set.of("target", "staffs", "casino", "miner", "builder", "pilot", "hunter");
   private static final List<k.l> G = u.stream().filter(var0 -> !s.contains(var0.s)).toList();
   private static final cq g = new cq();
   private static final up A = new up();
   private static List<class_1799> C;

   public static boolean k() {
      class_310 var0 = class_310.method_1551();
      return var0.field_1724 != null
         && var0.field_1687 != null
         && var0.method_1569()
         && var0.field_1755 != null
         && var0.field_1755.getClass() == class_408.class;
   }

   public static void Y() {
      z = List.of();
      Q.clear();
      d = null;
      n = null;
      w5.b();
      ud.V();
   }

   public static void U(class_332 var0) {
      if (d != var0) {
         Q.clear();
         d = var0;
         class_310 var1 = class_310.method_1551();
         n = var1.field_1687;
         B = var0.method_51421();
         V = var0.method_51443();
         m = hz.o;
      }
   }

   public static void d(k.l var0, cv.z var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         Q.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static String W(String var0) {
      return "info".equals(var0) ? "watermark" : null;
   }

   public static boolean k(k.l var0) {
      String var1 = W(var0.s);
      return var1 != null && var1.equals(var0.m());
   }

   public static void J(k.l var0) {
      W = var0;
   }

   public static k.l e() {
      return W;
   }

   public static boolean J(k.l var0, int var1, int var2) {
      String var3 = W(var0.s);
      if (var3 != null && a) {
         cv.z var4 = m(var3).k(var1, var2);
         cv.z var5 = var0.I(var1, var2);
         return U(var4, var5.x() + var5.width() / 2.0, var5.y() + var5.height() / 2.0);
      } else {
         return false;
      }
   }

   public static boolean U(cv.z var0, double var1, double var3) {
      return var1 >= var0.x() - 8 && var1 <= var0.x() + var0.width() + 8 && var3 >= var0.y() - 8 && var3 <= var0.y() + var0.height() + 8;
   }

   private static boolean V(k.l var0, boolean var1, boolean var2, k.l var3) {
      boolean var4 = B(var0);
      return var1 ? cv.C(var4, var2, var3 == var0) : var4;
   }

   public static List<k.i> y() {
      class_310 var0 = class_310.method_1551();
      if (n == var0.field_1687
         && m == hz.o
         && var0.field_1724 != null
         && B == var0.method_22683().method_4486()
         && V == var0.method_22683().method_4502()
         && !var0.field_1690.field_1842) {
         ArrayList var1 = new ArrayList();
         Q.forEach((var1x, var2) -> {
            if (B(var1x)) {
               for (cv.z var4 : var2) {
                  var1.add(new k.i(var1x, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<cv.z> o(List<k.i> var0, k.l var1) {
      return var0.stream()
         .filter(var1x -> var1x.element() == var1)
         .map(k.i::bounds)
         .reduce(
            (var0x, var1x) -> new cv.z(
               Math.min(var0x.x(), var1x.x()),
               Math.min(var0x.y(), var1x.y()),
               Math.max(var0x.x() + var0x.width(), var1x.x() + var1x.width()) - Math.min(var0x.x(), var1x.x()),
               Math.max(var0x.y() + var0x.height(), var1x.y() + var1x.height()) - Math.min(var0x.y(), var1x.y())
            )
         );
   }

   public static List<com.larpclient.module.lb<?>> J(k.l var0) {
      if (var0.s.equals("crosshair")) {
         com.larpclient.module.lr var5 = LarpClient.b().G();
         Module var7 = var5.j("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      }

      HUDModule var1 = HUDModule.y();
      if (var0.s.equals("island")) {
         IslandLyricsModule var6 = IslandLyricsModule.K();
         return var6 == null ? List.of() : var6.getSettings();
      }

      if (!var0.s.equals("target") && !var0.s.equals("staffs")) {
         return var1 == null ? List.of() : var1.k(var0.s);
      }

      com.larpclient.module.lr var2 = LarpClient.b().G();
      Module var3 = var0.s.equals("target") ? var2.b(TargetHUDModule.class) : var2.b(StaffListModule.class);
      ArrayList var4 = new ArrayList();
      if (var3 != null) {
         var4.addAll(var3.getSettings());
      }

      if (var1 != null) {
         var4.addAll(var1.k(var0.s));
      }

      return List.copyOf(var4);
   }

   public static boolean B(List<class_1799> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.method_7960());
   }

   public static String L(k.l var0) {
      if (var0.q()) {
         return "Hidden";
      }

      if (!B(var0)) {
         return "Disabled";
      }

      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 == null) {
         return "Waiting for world";
      }

      int var2 = switch (var0.s) {
         case "notifications" -> wt.T().h();
         case "target" -> wc.X() == null && (p == null || !(h.u() > 0.01)) ? 0 : 1;
         case "effects" -> !var1.field_1724.method_6026().isEmpty() ? 1 : 0;
         case "keybinds" -> 1;
         case "modules" -> LarpClient.b().G().M().stream().anyMatch(var0x -> var0x.isOperational());
         case "armor" -> {
            HUDModule var5 = HUDModule.y();
            yield var5 != null
                  && Arrays.stream(y)
                     .filter(var1x -> var5.Y.getValue() || var1x != class_1304.field_6173 && var1x != class_1304.field_6171)
                     .anyMatch(var1x -> !var1.field_1724.method_6118(var1x).method_7960())
               ? 1
               : 0;
         }
         case "cooldowns", "staffs" -> y4.p(var0.s, y4.L(var0.s, false));
         case "crosshair" -> var1.field_1690.method_31044().method_31034();
         case "island" -> IslandLyricsModule.S();
         default -> 1;
      };
      return var2 != 0 ? "Visible" : "Waiting for content";
   }

   private static k.c r(String var0) {
      return S.stream().filter(var1 -> var1.A.equals(var0)).findFirst().orElseThrow();
   }

   private static List<k.l> N(List<k.l> var0) {
      ArrayList var1 = new ArrayList(var0);

      for (k.c var3 : S) {
         var1.add(new k.l(var3.A, 196, 80, 0.5, 0.1));
      }

      return List.copyOf(var1);
   }

   public static List<k.l> A() {
      return com.larpclient.module.lf.l() ? G : u;
   }

   public static k.l m(String var0) {
      return u.stream().filter(var1 -> var1.s.equals(var0)).findFirst().orElseThrow();
   }

   public static String L(String var0) {
      return switch (var0) {
         case "watermark" -> "Watermark / server / FPS";
         case "speed" -> "Speed graph";
         case "info" -> "Coordinates";
         case "modules" -> "Active modules";
         case "target" -> "TargetHUD";
         case "effects" -> "Potions";
         case "keybinds" -> "Keybinds";
         case "armor" -> "Armor";
         case "notifications" -> "Notifications";
         case "crosshair" -> "Custom crosshair";
         case "cooldowns" -> "Cooldowns";
         case "staffs" -> "Staff List";
         case "inventory" -> "Inventory";
         case "keystrokes" -> "Keystrokes";
         case "miner" -> "Auto Area Miner";
         case "builder" -> "Auto Builder";
         case "island" -> "Island Lyrics";
         case "pilot" -> "Auto Elytra Pilot";
         case "hunter" -> "Anchor Hunter";
         case "casino" -> "Casino games";
         default -> var0;
      };
   }

   public static boolean B(k.l var0) {
      if (var0.q()) {
         return false;
      }

      HUDModule var1 = HUDModule.y();

      return (boolean)(switch (var0.s) {
         case "crosshair" -> CustomCrosshairModule.y();
         case "notifications" -> 1;
         case "target" -> {
            TargetHUDModule var4 = LarpClient.b().G().b(TargetHUDModule.class);
            yield var4 != null && var4.isOperational() ? 1 : 0;
         }
         case "watermark" -> var1 != null && var1.j() ? 1 : 0;
         case "info" -> var1 != null && var1.M() ? 1 : 0;
         case "modules" -> var1 != null && var1.T() ? 1 : 0;
         case "armor" -> var1 != null && var1.I() ? 1 : 0;
         case "keybinds" -> var1 != null && var1.S() ? 1 : 0;
         case "effects" -> var1 != null && var1.b() ? 1 : 0;
         case "speed" -> var1 != null && var1.x() ? 1 : 0;
         case "miner", "builder", "pilot", "hunter" -> r(var0.s).P() != null ? 1 : 0;
         case "staffs" -> StaffListModule.a();
         case "island" -> IslandLyricsModule.t();
         case "casino" -> com.larpclient.module.r.m() != null ? 1 : 0;
         case "cooldowns", "inventory", "keystrokes" -> var1 != null && var1.u(var0.s) ? 1 : 0;
         default -> var1 != null && var1.isOperational() ? 1 : 0;
      });
   }

   public static void V() {
      z(var0 -> true);
   }

   public static void z(Predicate<k.l> var0) {
      for (k.l var2 : u) {
         if (var0.test(var2)) {
            hz.J.remove(var2.s);
            hz.s var3 = hz.S.get(var2.s);
            if (var3 != null) {
               hz.S.put(var2.s, new hz.s(var2.H(), var3.hidden()));
            }
         }
      }
   }

   public static void F(k.l var0, boolean var1) {
      K(var0, var1, var0x -> LarpClient.b().G().j(var0x));
   }

   public static void K(k.l var0, boolean var1, Function<String, Module> var2) {
      var0.a(!var1);
      if (var1 && !var0.s.equals("notifications")) {
         Module var3 = (Module)var2.apply(
            var0.s.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.s.equals("target") ? "TargetHUD" : (var0.s.equals("staffs") ? "Staff List" : (var0.s.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var4 = switch (var0.s) {
               case "watermark" -> "Watermark";
               case "info" -> "Info Panel";
               case "modules" -> "Module List";
               case "armor" -> "Armor";
               case "keybinds" -> "Keybinds";
               case "effects" -> "Potions";
               case "speed" -> "Speed";
               case "cooldowns" -> "Cooldowns";
               case "inventory" -> "Inventory";
               case "keystrokes" -> "Keystrokes";
               default -> "";
            };

            for (com.larpclient.module.lb var9 : var3.getSettings()) {
               if (var9.R().equals(var4) && var9 instanceof com.larpclient.module.l3 var7) {
                  var7.setValue(true);
               }
            }

            var3.setEnabled(true);
         }
      }
   }

   public void J() {
      HudElementRegistry.addLast(class_2960.method_60655("larp-client", "hudoverlay"), (var0, var1) -> {
         class_310 var2 = class_310.method_1551();
         if (var2.field_1724 != null && !var2.field_1690.field_1842 && !tm.K(var2.field_1755) && !(var2.field_1755 instanceof tj)) {
            class_332 var3 = wi.p(var0, false, false);
            if (var3 != null) {
               U(var3);
               s(var3, false);
            }
         }
      });
      q8.G("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", u.size());
   }

   public static void c(class_310 var0) {
      HUDModule var1 = HUDModule.y();
      if (var0.field_1687 == null || var0.field_1724 == null || var1 == null || !var1.isOperational()) {
         g.H();
      } else if (!var0.method_1493() && var0.field_1687.method_54719().method_54751()) {
         g.n(var0.field_1687, var0.field_1724, var0.field_1724.method_23317(), var0.field_1724.method_23321(), var0.field_1687.method_54719().method_54748());
      } else {
         g.e();
      }
   }

   private static List<class_1799> T() {
      if (C == null) {
         C = List.of(
            new class_1799(class_1802.field_8805),
            new class_1799(class_1802.field_8058),
            new class_1799(class_1802.field_8348),
            new class_1799(class_1802.field_8285)
         );
      }

      return C;
   }

   public static void s(class_332 var0, boolean var1) {
      N(var0, var1, null, false);
   }

   public static void d(class_332 var0, k.l var1, boolean var2) {
      N(var0, true, var1, var2);
   }

   public static List<k.i> D() {
      return R == hz.o ? z : List.of();
   }

   public static void O() {
      z = List.of();
   }

   public static void C(class_332 var0, k.l var1, boolean var2, boolean var3) {
      if (var3) {
         U(var0);
         s(var0, false);
         z = y();
         R = hz.o;
      } else {
         d(var0, var1, var2);
      }
   }

   private static void N(class_332 var0, boolean var1, k.l var2, boolean var3) {
      class_310 var4 = class_310.method_1551();
      if (var4.field_1724 != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            z = List.of();
         }

         long var6 = c5.X();
         HUDModule var8 = HUDModule.y();
         int var9 = var4.method_22683().method_4486();
         int var10 = var4.method_22683().method_4502();
         int var11 = !var1 && k() ? 1 : 0;
         if (!var1 && var11 != v) {
            v = (boolean)var11;
            q8.v("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List var12 = A.b(ChooseASkyEffectAndAdjustItsAppearanceModule.C(LarpClient.b().G().Y(), LarpClient.b().e().F()), var4.field_1772, uw.E(), t_.g, w5::I);
         int var13 = (var1 || var11 != 0) && var12.isEmpty() ? 1 : 0;
         if (var13 != 0) {
            var12 = List.of(new up.d("Sprint", w5.I("Sprint")), new up.d("Fullbright", w5.I("Fullbright")));
         }

         k.l var14 = m("modules");
         double var15 = var14.S();
         int var17 = Math.max(1, (int)((var10 - 24) / var15 / 17.0));
         int var18 = Math.max(1, (var12.size() + var17 - 1) / var17);
         int var19 = Math.max(20, var12.stream().mapToInt(var0x -> ur.G(var0x.width())).max().orElse(0));
         ud.a var20 = var8 != null && !var8.w() ? null : ud.i(var12, var1 || var13 != 0, ud.Q(var10, var15));
         if (var20 != null) {
            var14.L = var20.width();
            var14.F = var20.height();
         } else {
            var14.L = var18 * var19 + (var18 - 1) * 8;
            var14.F = Math.max(20, Math.min(var17, var12.size()) * 17);
         }

         var14.B = Math.max(0.1, Math.min(4.0, Math.min((var9 - 16.0) / var14.L, (var10 - 16.0) / var14.F)));
         ArrayList var21 = new ArrayList();
         if (var4.field_1724 != null && var8 != null) {
            for (class_1304 var25 : y) {
               if (var25 != class_1304.field_6173 && var25 != class_1304.field_6171 || var8.Y.getValue()) {
                  class_1799 var26 = var4.field_1724.method_6118(var25);
                  if (!var26.method_7960() || var8.Q.getValue()) {
                     var21.add(var26);
                  }
               }
            }

            if (var8.o.getValue()) {
               Collections.reverse(var21);
            }
         }

         if ((var1 || var11 != 0) && var21.stream().allMatch(class_1799::method_7960)) {
            var21.clear();
            var21.addAll(T());
            if (var8 != null && var8.o.getValue()) {
               Collections.reverse(var21);
            }
         }

         int var69 = var8 != null && var8.F.G("Remaining") ? 46 : 26;
         int var70 = var8 != null && !var8.F.G("Off") ? 37 : 27;
         int var71 = var8 == null ? 5 : var8.K.getValue().intValue();
         int var72 = var8 != null && var8.p.G("Vertical") ? 1 : 0;
         if (var72 != 0 && var8 != null && !var8.F.G("Off")) {
            var69 += 38;
            var70 = 26;
         }

         int var73 = Math.max(1, var21.size());
         m("armor").L = 6 + (var72 != 0 ? var69 : var73 * (var69 + var71) - var71);
         m("armor").F = 6 + (var72 != 0 ? var73 * (var70 + var71) - var71 : var70);
         String var27 = FakeFPSModule.l(var4.method_47599()) + " fps";
         String var28 = var4.field_1724 == null ? "Player" : he.n(var4.field_1724.method_5667(), var4.field_1724.method_5477().getString());
         var28 = w5.O(var28, 130.0, 13.0);
         String var29 = !var4.method_1496() && var4.method_1558() != null ? var4.method_1558().field_3761 : "singleplayer";
         var29 = w5.O(var29, 154.0, 13.0);
         String var30 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         String var31 = var4.field_1724 == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.field_1724.method_23317(), var4.field_1724.method_23318(), var4.field_1724.method_23321());
         m("info").L = ur.m(var31);
         k.l var32 = m("watermark");
         k.l var33 = m("info");
         boolean var34 = V(var32, var1, var3, var2);
         int var35 = !var34 || !V(var33, var1, var3, var2) || !k(var33) && W != var33 ? 0 : 1;
         if (x != hz.o) {
            x = hz.o;
            H.w(var35 != 0 ? 1.0 : 0.0);
         }

         double var36 = Math.clamp(H.a(var35 != 0 ? 1.0 : 0.0, 220), 0.0, 1.0);
         a = var34;
         int var38 = var8 != null && var8.I.G("Right") ? 1 : 0;
         cv.z var39 = null;
         var32.L = ur.U(ur.f(var28, var29, var27, var30), var31, var36);
         var32.F = ur.r(var36);
         int var40 = var11 == 0 || wc.X() != null || p != null && !(h.u() < 0.01) ? 0 : 1;
         HashMap var41 = new HashMap();

         for (String var43 : List.of("cooldowns", "staffs", "inventory")) {
            y4.l var44 = y4.L(var43, var1);
            var41.put(var43, var44);
            m(var43).L = var44.width();
            m(var43).F = var44.height();
         }

         if (var8 != null) {
            m("keystrokes").L = g9.T(var8);
            m("keystrokes").F = g9.I(var8);
         }

         w5.w var76 = w5.O(var1);
         w5.w var77 = w5.F(var1);
         w5.w var78 = w5.l(var1);
         m("casino").L = var78.width();
         m("casino").F = var78.height();

         for (k.c var46 : S) {
            var46.F(var1);
         }

         int var79 = var11 == 0 || var4.field_1724 != null && !var4.field_1724.method_6026().isEmpty() ? 0 : 1;
         if (var79 != 0) {
            var77 = w5.F(true);
         }

         m("keybinds").L = var76.width();
         m("keybinds").F = var76.height();
         m("effects").L = var77.width();
         m("effects").F = var77.height();

         for (k.l var47 : A()) {
            var47.O(var9, var10);
            int var48 = var47.l(var9);
            int var49 = var47.M(var10);
            boolean var50 = B(var47);
            if ((var1 ? cv.C(var50, var3, var2 == var47) : var50)
               && (var1 || (!var47.s.equals("modules") || (var20 == null ? !var12.isEmpty() : !var20.empty())) && (!var47.s.equals("armor") || B(var21)))
               && (var1 || var11 != 0 || !var41.containsKey(var47.s) || y4.p(var47.s, (y4.l)var41.get(var47.s)))
               && (var1 || var79 != 0 || !var47.s.equals("effects") || var4.field_1724 != null && !var4.field_1724.method_6026().isEmpty())) {
               int var51 = !var1 && (var11 == 0 || IslandLyricsModule.S()) ? 0 : 1;
               if (var1 || !var47.s.equals("island") || var51 != 0 || IslandLyricsModule.S()) {
                  int var52 = !var47.s.equals("notifications") && !var47.s.equals("crosshair") ? 0 : 1;
                  if (var52 == 0 || var1) {
                     if (var47 != var33 || var35 == 0) {
                        var47.e(var0, var48, var49);

                        try {
                           switch (var47.s) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                                 y4.E(var0, var47.s, (y4.l)var41.get(var47.s), var48, var49, var1);
                                 break;
                              case "island":
                                 IslandLyricsModule var93 = IslandLyricsModule.K();
                                 if (var93 == null) {
                                    continue;
                                 }

                                 var93.I(var0, var48, var49, var47.L, (boolean)var51);
                                 break;
                              case "keystrokes":
                                 if (var8 == null) {
                                    continue;
                                 }

                                 if (var8.K() && !(var4.field_1755 instanceof tk)) {
                                    J(var0, var48, var49, var47.L, var47.F);
                                 }

                                 g9.N(var0, var8, var48, var49, var1);
                                 break;
                              case "notifications":
                                 wt.f(var0, var48, var49);
                                 break;
                              case "crosshair":
                                 CustomCrosshairModule.J(var0, var48 + var47.L / 2, var49 + var47.F / 2);
                                 break;
                              case "watermark":
                                 if (!var1 && !var8.j()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.K() && !(var4.field_1755 instanceof tk)) {
                                    J(var0, var48, var49, var47.L, var47.F);
                                 }

                                 ur.B(var0, var48, var49, var47.L, var28, var29, var27, var30, var31, var36);
                                 if (var35 != 0) {
                                    cv.z var92 = ur.Y(var48, var49, var47.L, (boolean)var38, var31);
                                    var39 = new cv.z(
                                       var48 + (int)Math.floor((var92.x() - var48) * var47.o()),
                                       var49 + (int)Math.floor((var92.y() - var49) * var47.o()),
                                       (int)Math.ceil(var92.width() * var47.o()),
                                       (int)Math.ceil(var92.height() * var47.o())
                                    );
                                 }
                                 break;
                              case "speed":
                                 ur.y(var0, var48, var49, g, var1);
                                 break;
                              case "miner":
                              case "builder":
                              case "pilot":
                              case "hunter":
                                 k.c var91 = r(var47.s);
                                 if (var91.r == null) {
                                    continue;
                                 }

                                 if (var8 != null && var8.K() && !(var4.field_1755 instanceof tk)) {
                                    J(var0, var48, var49, var47.L, var47.F);
                                 }

                                 w5.e(
                                    var0,
                                    var48,
                                    var49,
                                    var47.L,
                                    var91.M,
                                    "scan",
                                    var91.r.headline(),
                                    var91.r.value(),
                                    var91.r.action(),
                                    var91.r.time(),
                                    var91.E,
                                    var91.r.alert()
                                 );
                                 break;
                              case "info":
                                 if (!var1 && !var8.M()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.K() && !(var4.field_1755 instanceof tk)) {
                                    J(var0, var48, var49, var47.L, var47.F);
                                 }

                                 ur.y(var0, var48, var49, var47.L, var31);
                                 break;
                              case "modules":
                                 if (!var1 && !var8.T()) {
                                    continue;
                                 }

                                 int var90 = var8 != null && var8.K() && !(var4.field_1755 instanceof tk) ? 1 : 0;
                                 if (var20 != null) {
                                    if (var90 != 0) {
                                       J(var0, var48, var49, var47.L, var47.F);
                                    }

                                    ud.c(var0, var20, var48, var49);
                                    if (!var1) {
                                       d(var47, var47.k(var9, var10));
                                    }
                                 } else {
                                    for (int var95 = 0; var95 < var12.size(); var95++) {
                                       String var98 = ((up.d)var12.get(var95)).name();
                                       int var101 = var95 / var17;
                                       int var102 = var95 % var17;
                                       int var103 = var48 + var47.N() / 2 > var9 / 2 ? 1 : 0;
                                       int var104 = var103 != 0
                                          ? var48 + var47.L - var101 * (var19 + 8)
                                          : var48 + var101 * (var19 + 8) + ur.G(((up.d)var12.get(var95)).width());
                                       int var62 = ((up.d)var12.get(var95)).width();
                                       int var105 = ur.G(var62);
                                       int var106 = var104 - var105;
                                       int var107 = var49 + var102 * 17;
                                       if (var90 != 0) {
                                          J(var0, var106, var107, var105, 17);
                                       }

                                       ur.p(var0, var98, var62, var104, var107, (boolean)var103);
                                       if (!var1) {
                                          d(
                                             var47,
                                             new cv.z(
                                                var48 + (int)Math.floor((var106 - var48) * var47.o()),
                                                var49 + (int)Math.floor((var107 - var49) * var47.o()),
                                                (int)Math.ceil(var105 * var47.o()),
                                                (int)Math.ceil(17.0 * var47.o())
                                             )
                                          );
                                       }
                                    }
                                 }

                                 if (var1 && var12.isEmpty() && var20 == null) {
                                    uw.z(var0, "No active modules", var48 + 4, var49 + 5, ez.PH);
                                 }
                                 break;
                              case "target":
                                 if (!var1 && var40 == 0) {
                                    M(var0, var48, var49);
                                 } else {
                                    q(var0, var48, var49);
                                 }
                                 break;
                              case "keybinds":
                              case "effects":
                              case "casino":
                                 w5.w var89 = var47.s.equals("keybinds") ? var76 : (var47.s.equals("casino") ? var78 : var77);
                                 int var94 = var48 + var47.N() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.K() && !(var4.field_1755 instanceof tk)) {
                                    for (cv.z var99 : w5.P(var89, var48, var49, (boolean)var94)) {
                                       J(var0, var99.x(), var99.y(), var99.width(), var99.height());
                                    }
                                 }

                                 w5.J(var0, var89, var48, var49, (boolean)var94);
                                 if (!var1) {
                                    for (cv.z var100 : w5.P(var89, var48, var49, (boolean)var94)) {
                                       d(
                                          var47,
                                          new cv.z(
                                             var48 + (int)Math.floor((var100.x() - var48) * var47.o()),
                                             var49 + (int)Math.floor((var100.y() - var49) * var47.o()),
                                             (int)Math.ceil(var100.width() * var47.o()),
                                             (int)Math.ceil(var100.height() * var47.o())
                                          )
                                       );
                                    }
                                 }
                                 break;
                              case "armor":
                                 if (!var1 && !var8.I()) {
                                    continue;
                                 }

                                 if (var8.c.getValue() && B(var21)) {
                                    gu.N(var0, var48, var49, var47.L, var47.F, 6.0F, ur.i());
                                    ur.D(var0, var48, var49, var47.L, var47.F, 6.0F);
                                    ur.x(var0, var48, var49, var47.L, var47.F, 6);
                                 }

                                 for (int var55 = 0; var55 < var21.size(); var55++) {
                                    class_1799 var56 = (class_1799)var21.get(var55);
                                    int var57 = var48 + 3 + (var72 != 0 ? 0 : var55 * (var69 + var71));
                                    int var58 = var49 + 3 + (var72 != 0 ? var55 * (var70 + var71) : 0);
                                    if (!var56.method_7960()) {
                                       int var59 = var72 != 0 ? var57 : var57 + (var69 - 21) / 2;
                                       var0.method_51448().pushMatrix();
                                       var0.method_51448().translate(var59, var58).scale(1.3125F);
                                       var0.method_51427(var56, 0, 0);
                                       var0.method_51448().popMatrix();
                                       if (var56.method_7963()) {
                                          int var60 = Math.max(0, var56.method_7936() - var56.method_7919());
                                          double var61 = (double)var60 / Math.max(1, var56.method_7936());
                                          int var63 = ei.W(-43434, -8790368, var61);
                                          if (var8.u.getValue()) {
                                             gu.Z(var0, var57 + 2, var58 + 23, var69 - 4, 2, 1879048192);
                                             gu.Z(var0, var57 + 2, var58 + 23, (int)Math.round((var69 - 4) * var61), 2, var63);
                                          }

                                          if (!var8.F.G("Off")) {
                                             String var64 = var8.F.G("Percent") ? (int)Math.ceil(var61 * 100.0) + "%" : Integer.toString(var60);
                                             int var65 = var72 != 0 ? var57 + 28 : var57 + (var69 - w5.A(var64, 10.4)) / 2;
                                             w5.y(var0, var64, var65, var58 + (var72 != 0 ? 7 : 28), 10.4, var63);
                                          }
                                       }
                                    }
                                 }
                           }
                        } finally {
                           var47.o(var0);
                        }

                        if (!var1
                           && (
                              var47.s.equals("target")
                                 ? var40 != 0
                                 : !var47.s.equals("modules") && !var47.s.equals("keybinds") && !var47.s.equals("effects") && !var47.s.equals("casino")
                           )) {
                           d(var47, var47.k(var9, var10));
                        }

                        if (var1) {
                           var5.add(new k.i(var47, var47.k(var9, var10)));
                        }

                        if (var1 && !var50) {
                           cv.z var88 = var47.k(var9, var10);
                           gu.Z(var0, var88.x(), var88.y(), var88.width(), var88.height(), 1882207290);
                        }
                     } else if (var39 != null) {
                        if (var1) {
                           var5.add(new k.i(var47, var39));
                        } else {
                           d(var47, var39);
                        }
                     }
                  }
               }
            }
         }

         if (var1) {
            z = List.copyOf(var5);
            R = hz.o;
         }

         if (!var1 && Boolean.getBoolean("larp.debug")) {
            int var81 = var10 - 15;

            for (Entry var84 : c5.G().entrySet()) {
               String var86 = (String)var84.getKey() + " " + var84.getValue();
               uw.z(var0, var86, var9 - uw.i(var86) - 5, var81, ez.PH);
               var81 -= 11;
            }

            for (Entry var85 : c5.M().entrySet()) {
               String var87 = String.format(Locale.ROOT, "%s %.2f ms CPU", var85.getKey(), var85.getValue());
               uw.z(var0, var87, var9 - uw.i(var87) - 5, var81, ez.PH);
               var81 -= 11;
            }
         }

         c5.w("HUD", var6);
      }
   }

   private static void J(class_332 var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.method_51448().transformPosition(var1, var2, new Vector2f());
      t_.A(Math.round(var5.x), Math.round(var5.y), Math.round(var3 * var0.method_51448().m00()), Math.round(var4 * var0.method_51448().m11()));
   }

   public static void q() {
      p = null;
      k.w(1.0);
      q.w(1.0);
      I = 1.0;
      D = 0L;
      h.w(0.0);
      w.s();
      Z = 0L;
      f = 0.0F;
   }

   public static void q(class_332 var0, int var1, int var2) {
      h(var0, var1, var2, class_310.method_1551().field_1724, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0);
   }

   public static void h(class_332 var0, int var1, int var2, class_1309 var3, String var4, float var5, float var6, float var7, double var8) {
      double var10 = Math.clamp(var5 / Math.max(1.0F, var6), 0.0F, 1.0F);
      z(var0, var1, var2, var3, var4, var5, var6, var7, var8, 1.0, var10, var10, true);
   }

   private static void M(class_332 var0, int var1, int var2) {
      long var3 = c5.X();

      try {
         class_310 var5 = class_310.method_1551();
         if (p != null && p.method_73183() != var5.field_1687) {
            q();
         }

         class_1309 var6 = wc.X();
         if (var6 != null && var6 != p) {
            p = var6;
            q8.v(
               "TargetHUD",
               "Target {}: {} x {} blocks, portrait scale {} before HUD transform",
               var6.method_5864(),
               var6.method_17681(),
               var6.method_17682(),
               l(var6.method_17681(), var6.method_17682())
            );
            w.s();
            f = j2.T(var6);
            Z = System.nanoTime();
            double var7 = j2.z(var6);
            k.w(var7);
            q.w(var7);
            I = var7;
            D = 0L;
         }

         long var22 = System.nanoTime();
         if (Z != 0L) {
            w.T((var22 - Z) / 1.0E9);
         }

         Z = var22;
         float var9 = p == null ? 0.0F : j2.T(p);
         if (p != null) {
            w.U(f - var9, ThreadLocalRandom.current());
            f = var9;
         }

         double var10 = h.a(var6 == null ? 0.0 : 1.0, 200);
         if (p == null || var10 < 0.01) {
            if (var6 == null) {
               p = null;
               w.s();
            }

            return;
         }

         k.l var12 = m("target");
         double var13 = 0.92 + 0.08 * var10;
         d(var12, b(var1, var2, var12.o(), var13, new cv.z(0, 0, 224, 52)));
         double var15 = Math.clamp(var9 / Math.max(1.0F, p.method_6063()), 0.0F, 1.0F);
         double var17 = k.a(var15, 220);
         z(
            var0,
            var1,
            var2,
            p,
            he.n(p.method_5667(), p.method_5477().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, p.method_6063()),
            Math.max(0.0F, p.method_6067()),
            var5.field_1724 == null ? 0.0 : var5.field_1724.method_5739(p),
            var10,
            var17,
            c(var15, var17, var22),
            false
         );
      } finally {
         c5.w("TargetHUD", var3);
      }
   }

   private static double c(double var0, double var2, long var4) {
      if (var0 < I) {
         q.w(Math.max(q.u(), var2));
         D = var4 + 400000000L;
      }

      I = var0;
      double var6 = var4 < D ? q.u() : q.G(var0, 450, qb.y.z);
      return Math.max(var6, var2);
   }

   public static cv.z b(int var0, int var1, double var2, double var4, cv.z var6) {
      double var7 = 112.0;
      double var9 = 26.0;
      double var11 = var0 + (var7 + (var6.x() - var7) * var4) * var2;
      double var13 = var1 + (var9 + (var6.y() - var9) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new cv.z(var15, var16, (int)Math.ceil(var11 + var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + var6.height() * var4 * var2) - var16);
   }

   public static float l(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void L(class_332 var0, int var1, int var2, class_1309 var3) {
      class_897 var4 = class_310.method_1551().method_1561().method_3953(var3);
      class_10017 var5 = var4.method_62425(var3, 1.0F);
      float var6 = l(var5.field_53329, var5.field_53330);
      if (var6 <= 0.0F) {
         q8.O("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         w5.C(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
      } else {
         var5.field_61823.clear();
         var5.field_61821 = 0;
         if (var5 instanceof class_10042 var7) {
            var7.field_53446 = 180.0F;
            var7.field_53447 = 0.0F;
            var7.field_53448 = 0.0F;
         }

         class_8030 var11 = new class_8030(var1, var2, 50, 50).method_65185(var0.method_51448());
         var0.method_44379(var1, var2, var1 + 50, var2 + 50);

         try {
            var0.method_70856(
               var5,
               var6 * var0.method_51448().m00(),
               new Vector3f(0.0F, var5.field_53330 / 2.0F, 0.0F),
               new Quaternionf().rotateZ((float) Math.PI),
               new Quaternionf(),
               var11.method_49620(),
               var11.method_49618(),
               var11.method_49621(),
               var11.method_49619()
            );
         } finally {
            var0.method_44380();
         }
      }
   }

   public static List<k.z> z(boolean var0, boolean var1) {
      if (var1) {
         return var0 ? List.of(k.z.S, k.z.w, k.z.J) : List.of(k.z.w, k.z.J, k.z.S);
      } else {
         return var0 ? List.of(k.z.J, k.z.S, k.z.w) : List.of(k.z.J, k.z.w, k.z.S);
      }
   }

   private static String F(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static void z(
      class_332 var0,
      int var1,
      int var2,
      class_1309 var3,
      String var4,
      float var5,
      float var6,
      float var7,
      double var8,
      double var10,
      double var12,
      double var14,
      boolean var16
   ) {
      class_310 var17 = class_310.method_1551();
      TargetHUDModule var18 = LarpClient.b().G().b(TargetHUDModule.class);
      short var19 = 224;
      byte var20 = 52;
      double var21 = 0.92 + 0.08 * var10;
      var0.method_51448().pushMatrix();
      var0.method_51448().translate(var1 + var19 / 2.0F, var2 + var20 / 2.0F).scale((float)var21).translate(-var1 - var19 / 2.0F, -var2 - var20 / 2.0F);

      try {
         HUDModule var23 = HUDModule.y();
         if (var23 != null && var23.K() && !(var17.field_1755 instanceof tk)) {
            J(var0, var1, var2, var19, var20);
         }

         gu.N(var0, var1, var2, var19, var20, 10.0F, ei.R(-468906478, var10));
         int var24 = var1 + 8;
         int var25 = var2 + 8;
         int var26 = var24 + 18;
         int var27 = var25 + 18;
         if (!var16 && !w.N().isEmpty()) {
            var0.method_44379(var1, var2, var1 + var19, var2 + var20);

            try {
               for (ji.t var29 : w.N()) {
                  double var30 = Math.clamp(var29.r / var29.t, 0.0, 1.0);
                  double var32 = var10 * (1.0 - var30) * (var29.n ? 1.0 : 0.72);
                  int var34 = Math.max(1, (int)Math.round(var29.J * (1.0 - var30 * 0.35)));
                  int var35 = var26 + (int)Math.round(var29.Q);
                  int var36 = var27 + (int)Math.round(var29.G);
                  int var37 = var29.n ? ez.a : -419430401;
                  gu.Q(var0, var35, var36, (int)Math.ceil(var34 * 1.125), ei.R(var37, var32 * 0.22));
                  gu.Q(var0, var35, var36, Math.max(1, var34 / 2), ei.R(var37, var32));
               }
            } finally {
               var0.method_44380();
            }
         }

         gu.N(var0, var24 - 1, var25 - 1, 38.0F, 38.0F, 3.0F, ei.R(822083583, var10));
         if (var3 instanceof class_742 var55) {
            int var57 = Math.min(140, var55.field_6235 * 16);
            var0.method_51448().pushMatrix();
            float var59 = (float)Math.sin(var55.field_6235 / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.method_51448().translate(var26, var27).rotate(var59).translate(-var26, -var27);
            class_7532.method_44443(var0, var55.method_52814(), var24, var25, 36, ei.R(-65536 | 255 - var57 << 8 | 255 - var57, var10));
            var0.method_51448().popMatrix();
         } else if (var3 != null) {
            var0.method_51448().pushMatrix();
            var0.method_51448().translate(var24, var25).scale(0.72F).translate(-var24, -var25);

            try {
               L(var0, var24, var25, var3);
            } finally {
               var0.method_51448().popMatrix();
            }
         } else {
            w5.C(var0, "user", var24 + 7, var25 + 7, 22, ei.R(-5592406, var10));
         }

         int var56 = var18 != null && !var18.p.G("Above") ? 0 : 1;
         int var58 = var18 != null && var18.J.G("Above") ? 1 : 0;
         int var60 = var18 != null && var18.C.G("Left") ? 1 : 0;
         int var31 = var1 + 53;
         int var61 = var1 + var19 - 9;
         int var33 = ei.R(-1, var10);
         int var62 = ei.R(-6645086, var10);
         double var63 = var25;

         for (k.z var38 : z((boolean)var56, (boolean)var58)) {
            int var39 = (int)Math.round(var63);
            switch (var38) {
               case J:
                  String var65 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
                  int var41 = var61 - w5.A(var65, 10.0);
                  w5.y(var0, var65, var41, var39 + 1.5, 10.0, var62);
                  w5.y(var0, w5.O(var4, Math.max(0, var41 - 6 - var31), 13.0), var31, var39, 13.0, var33);
                  break;
               case S:
                  var0.method_52707(class_10799.field_56883, i, var31, var39 + 2, 9, 9, var33);
                  var0.method_52707(class_10799.field_56883, c, var31, var39 + 2, 9, 9, var33);
                  String var40 = F(var5);
                  w5.y(var0, var40, var31 + 12, var39 + 1, 12.0, var33);
                  if (var7 > 0.0F) {
                     w5.y(var0, "+" + F(var7), var31 + 16 + w5.A(var40, 12.0), var39 + 2.5, 10.0, ei.R(-14003, var10));
                  }

                  if (var3 instanceof class_742) {
                     Q(var0, var3, var61 - 79, var39, (boolean)var60, var10);
                  }
                  break;
               case w:
                  S(var0, var31, var39, var61 - var31, var12, var14, var6, var7, var10);
            }

            var63 += var38.s + 1.5;
         }
      } finally {
         var0.method_51448().popMatrix();
      }
   }

   private static void S(class_332 var0, int var1, int var2, int var3, double var4, double var6, float var8, float var9, double var10) {
      double var12 = var8 / (var8 + Math.max(0.0F, var9));
      gu.N(var0, var1, var2, var3, 5.0F, 2.0F, ei.R(620756991, var10));
      int var14 = (int)Math.round(var3 * Math.clamp(var4, 0.0, 1.0) * var12);
      int var15 = (int)Math.round(var3 * Math.clamp(var6, 0.0, 1.0) * var12);
      if (var15 > var14) {
         gu.N(var0, var1, var2, var15, 5.0F, 2.0F, ei.R(-1862270977, var10));
      }

      if (var14 > 0) {
         ws.u(var0, var1, var2, var14, 5.0F, 2.0F, ei.R(-4776932, var10), ei.R(-42418, var10));
      }

      if (var9 > 0.0F) {
         int var16 = Math.min(var3 - var14, Math.round(var3 * var9 / (var8 + var9)));
         if (var16 > 0) {
            gu.N(var0, var1 + var14, var2, var16, 5.0F, 2.0F, ei.R(-14003, var10));
         }
      }
   }

   private static void Q(class_332 var0, class_1309 var1, int var2, int var3, boolean var4, double var5) {
      int var7 = var2;

      for (int var8 = 0; var8 < 2; var8++) {
         int var9 = var4 == (var8 == 0 ? 1 : 0) ? 1 : 0;
         int var10 = var9 != 0 ? 4 : 0;
         int var11 = var9 != 0 ? 6 : 4;

         for (int var12 = var10; var12 < var11; var12++) {
            class_1799 var13 = var1.method_6118(y[var12]);
            if (var13.method_7960()) {
               gu.N(var0, var7 + 3, var3 + 3, 6.0F, 6.0F, 2.0F, ei.R(486539263, var5));
            } else {
               if (var5 > 0.5) {
                  var0.method_51448().pushMatrix();
                  var0.method_51448().translate(var7, var3).scale(0.75F);

                  try {
                     var0.method_51427(var13, 0, 0);
                  } finally {
                     var0.method_51448().popMatrix();
                  }
               }

               if (var13.method_7963() && var13.method_7919() > 0) {
                  double var14 = Math.clamp((double)(var13.method_7936() - var13.method_7919()) / Math.max(1, var13.method_7936()), 0.0, 1.0);
                  gu.N(var0, var7 + 1, var3 + 12, 10.0F, 1.5F, 0.75F, ei.R(-1610612736, var5));
                  gu.N(var0, var7 + 1, var3 + 12, (float)Math.max(1.0, 10.0 * var14), 1.5F, 0.75F, ei.R(ei.W(-43434, -10755203, var14), var5));
               }
            }

            var7 += 13;
         }

         var7 += 2;
      }
   }

   private static final class c {
      final String A;
      final String M;
      final Supplier<? extends tc> s;
      final tc.g H;
      final qb K = new qb(0.0);
      boolean m;
      tc.g r;
      double E;

      c(String var1, String var2, Supplier<? extends tc> var3, tc.g var4) {
         this.A = var1;
         this.M = var2;
         this.s = var3;
         this.H = var4;
      }

      tc.g P() {
         tc var1 = this.s.get();
         return var1 == null ? null : var1.hudCard();
      }

      void F(boolean var1) {
         tc.g var2 = this.P();
         this.r = var2 != null ? var2 : (var1 ? this.H : null);
         this.E = this.r == null ? 0.0 : this.r.progress();
         if (var2 != null && !var1) {
            if (!this.m) {
               this.K.w(Math.max(0.0, this.E));
            }

            this.E = this.K.a(Math.max(0.0, this.E), 450);
         }

         if (!var1) {
            this.m = (boolean)(var2 != null ? 1 : 0);
         }

         if (this.r != null) {
            com.larpclient.k.m(this.A).L = w5.k(this.M, this.r.headline(), this.r.value(), this.r.action(), this.r.time());
         }
      }
   }

   public record i(k.l element, cv.z bounds) {
   }

   public static final class l {
      public final String s;
      public int L;
      public int F;
      private double B = 4.0;
      private final hz.v M;

      l(String var1, int var2, int var3, double var4, double var6) {
         this.s = var1;
         this.L = var2;
         this.F = var3;
         this.M = new hz.v(var4, var6);
      }

      public double H() {
         return switch (this.s) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "miner", "builder", "pilot", "hunter" -> 0.8;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double S() {
         hz.s var1 = hz.S.get(this.s);
         return var1 == null ? this.H() : var1.scale();
      }

      public double o() {
         return Math.min(this.B, this.S());
      }

      public void O(int var1, int var2) {
         this.B = Math.max(0.1, Math.min(4.0, Math.min((double)Math.max(1, var1) / this.L, (double)Math.max(1, var2) / this.F)));
      }

      public cv.z k(int var1, int var2) {
         if (this.s.equals("crosshair")) {
            int var3 = CustomCrosshairModule.X();
            return new cv.z(
               this.l(var1) + (int)Math.round((this.L / 2.0 - var3) * this.o()),
               this.M(var2) + (int)Math.round((this.F / 2.0 - var3) * this.o()),
               (int)Math.ceil(var3 * 2 * this.o()),
               (int)Math.ceil(var3 * 2 * this.o())
            );
         } else {
            return new cv.z(this.l(var1), this.M(var2), this.N(), this.s.equals("notifications") ? (int)Math.round(30.0 * this.o()) : this.E());
         }
      }

      public cv.z I(int var1, int var2) {
         return new cv.z(this.l(var1), this.M(var2), this.N(), this.E());
      }

      public void x() {
         hz.J.remove(this.s);
         hz.S.put(this.s, new hz.s(this.H(), this.q()));
      }

      public void c() {
         hz.J.remove(this.s);
         hz.S.remove(this.s);
      }

      public void v() {
         hz.J.put(this.s, new hz.v(0.5, 0.5));
      }

      public boolean q() {
         return hz.S.getOrDefault(this.s, com.larpclient.k.l).hidden();
      }

      public int N() {
         return (int)Math.round(this.L * this.o());
      }

      public int E() {
         return (int)Math.round(this.F * this.o());
      }

      public void a(boolean var1) {
         hz.S.put(this.s, new hz.s(this.S(), var1, this.m()));
      }

      public void V(double var1) {
         hz.S.put(this.s, new hz.s(var1, this.q(), this.m()));
      }

      public String m() {
         hz.s var1 = hz.S.get(this.s);
         return var1 == null ? null : var1.dock();
      }

      public void w(String var1) {
         hz.S.put(this.s, new hz.s(this.S(), this.q(), var1));
      }

      public void e(class_332 var1, int var2, int var3) {
         var1.method_51448().pushMatrix();
         var1.method_51448().translate(var2, var3).scale((float)this.o()).translate(-var2, -var3);
      }

      public void o(class_332 var1) {
         var1.method_51448().popMatrix();
      }

      public int l(int var1) {
         return this.s.equals("crosshair") && !hz.J.containsKey(this.s)
            ? Math.max(0, var1 / 2 - this.N() / 2)
            : (int)Math.round(hz.J.getOrDefault(this.s, this.M).x() * Math.max(0, var1 - this.N()));
      }

      public int M(int var1) {
         return this.s.equals("crosshair") && !hz.J.containsKey(this.s)
            ? Math.max(0, var1 / 2 - this.E() / 2)
            : (int)Math.round(hz.J.getOrDefault(this.s, this.M).y() * Math.max(0, var1 - this.E()));
      }

      public void e(int var1, int var2, int var3, int var4) {
         hz.J.put(this.s, new hz.v((double)var1 / Math.max(1, var3 - this.N()), (double)var2 / Math.max(1, var4 - this.E())));
      }
   }

   public enum z {
      J(14),
      S(14),
      w(5);

      public final int s;

      z(int var3) {
         this.s = var3;
      }
   }
}

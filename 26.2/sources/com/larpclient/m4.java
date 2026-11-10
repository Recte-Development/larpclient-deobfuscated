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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class m4 {
   private static final Map<m4.p, List<hw.l>> U = new LinkedHashMap<>();
   private static Object N;
   private static Object W;
   private static int i;
   private static int A;
   private static long y;
   private static List<m4.k> I = List.of();
   private static long h;
   private static boolean x;
   public static final int B = 8;
   public static final int C = 220;
   private static final jp Y = new jp(0.0);
   private static long a = Long.MIN_VALUE;
   private static m4.p T;
   private static boolean u;
   private static final int w = 224;
   private static final int n = 52;
   private static final int d = 36;
   private static final int b = 12;
   private static final int p = 79;
   private static final List<m4.i> k = List.of(
      new m4.i("miner", "Auto Area Miner", ae::m, new s1.u("Layer 2 of 3", "64%", "Mining toward 120 64 -45", "4m 12s", 0.64)),
      new m4.i("builder", "Auto Builder", ak::g, new s1.u("house", "64%", "Walking to 120 64 -45", "412 placed, 230 missing", 0.64)),
      new m4.i(
         "pilot", "Auto Elytra Pilot", ac::y, new s1.u("12,340 blocks left, about 3m 10s", "64%", "Flying to 1000, -2500", "32 rockets, elytra 412/432", 0.64)
      ),
      new m4.i("hunter", "Anchor Hunter", a4::f, new s1.u("4 anchors, 24/h", "4 of 10", "Going to an anchor", "/rtp in 42s", 0.4))
   );
   public static final List<m4.p> c = S(
      List.of(
         new m4.p("watermark", 170, 22, 0.015, 0.02),
         new m4.p("info", 178, 27, 0.015, 0.85),
         new m4.p("modules", 130, 100, 0.98, 0.06),
         new m4.p("target", 224, 52, 0.55, 0.62),
         new m4.p("effects", 150, 58, 0.015, 0.7),
         new m4.p("keybinds", 120, 58, 0.015, 0.35),
         new m4.p("armor", 84, 22, 0.5, 0.9),
         new m4.p("notifications", 260, 178, 0.98, 0.08),
         new m4.p("crosshair", 84, 84, 0.5, 0.5),
         new m4.p("speed", 172, 72, 0.015, 0.55),
         new m4.p("cooldowns", 168, 48, 0.78, 0.08),
         new m4.p("staffs", 168, 48, 0.35, 0.65),
         new m4.p("inventory", 207, 94, 0.04, 0.48),
         new m4.p("keystrokes", 81, 98, 0.985, 0.93),
         new m4.p("island", 300, 20, 0.5, 0.015),
         new m4.p("casino", 168, 140, 0.98, 0.5)
      )
   );
   private static final jp E = new jp(1.0);
   private static final jp Z = new jp(0.0);
   private static final jp s = new jp(1.0);
   private static double t = 1.0;
   private static long l;
   private static final Identifier X = Identifier.withDefaultNamespace("hud/heart/container");
   private static final Identifier Q = Identifier.withDefaultNamespace("hud/heart/full");
   private static LivingEntity F;
   private static final w8 j = new w8();
   private static float P;
   private static long L;
   private static final c7.l V = new c7.l(1.0, false);
   private static final EquipmentSlot[] f = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private static final Set<String> K = Set.of("target", "staffs", "casino", "miner", "builder", "pilot", "hunter");
   private static final List<m4.p> O = c.stream().filter(var0 -> !K.contains(var0.r)).toList();
   private static final pd z = new pd();
   private static final k8 m = new k8();
   private static List<ItemStack> D;

   public static boolean t() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null
         && var0.level != null
         && var0.isWindowActive()
         && var0.gui.screen() != null
         && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void i() {
      I = List.of();
      U.clear();
      N = null;
      W = null;
      gw.d();
      hv.p();
   }

   public static void Z(GuiGraphicsExtractor var0) {
      if (N != var0) {
         U.clear();
         N = var0;
         Minecraft var1 = Minecraft.getInstance();
         W = var1.level;
         i = var0.guiWidth();
         A = var0.guiHeight();
         y = c7.w;
      }
   }

   public static void k(m4.p var0, hw.l var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         U.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static String J(String var0) {
      return "info".equals(var0) ? "watermark" : null;
   }

   public static boolean J(m4.p var0) {
      String var1 = J(var0.r);
      return var1 != null && var1.equals(var0.w());
   }

   public static void A(m4.p var0) {
      T = var0;
   }

   public static m4.p C() {
      return T;
   }

   public static boolean m(m4.p var0, int var1, int var2) {
      String var3 = J(var0.r);
      if (var3 != null && u) {
         hw.l var4 = S(var3).X(var1, var2);
         hw.l var5 = var0.S(var1, var2);
         return D(var4, var5.x() + var5.width() / 2.0, var5.y() + var5.height() / 2.0);
      } else {
         return false;
      }
   }

   public static boolean D(hw.l var0, double var1, double var3) {
      return var1 >= var0.x() - 8 && var1 <= var0.x() + var0.width() + 8 && var3 >= var0.y() - 8 && var3 <= var0.y() + var0.height() + 8;
   }

   private static boolean D(m4.p var0, boolean var1, boolean var2, m4.p var3) {
      boolean var4 = B(var0);
      return var1 ? hw.H(var4, var2, var3 == var0) : var4;
   }

   public static List<m4.k> I() {
      Minecraft var0 = Minecraft.getInstance();
      if (W == var0.level
         && y == c7.w
         && var0.player != null
         && i == var0.getWindow().getGuiScaledWidth()
         && A == var0.getWindow().getGuiScaledHeight()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         ArrayList var1 = new ArrayList();
         U.forEach((var1x, var2) -> {
            if (B(var1x)) {
               for (hw.l var4 : var2) {
                  var1.add(new m4.k(var1x, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<hw.l> J(List<m4.k> var0, m4.p var1) {
      return var0.stream()
         .filter(var1x -> var1x.element() == var1)
         .map(m4.k::bounds)
         .reduce(
            (var0x, var1x) -> new hw.l(
               Math.min(var0x.x(), var1x.x()),
               Math.min(var0x.y(), var1x.y()),
               Math.max(var0x.x() + var0x.width(), var1x.x() + var1x.width()) - Math.min(var0x.x(), var1x.x()),
               Math.max(var0x.y() + var0x.height(), var1x.y() + var1x.height()) - Math.min(var0x.y(), var1x.y())
            )
         );
   }

   public static List<com.larpclient.module.q<?>> y(m4.p var0) {
      if (var0.r.equals("crosshair")) {
         com.larpclient.module.f var5 = LarpClient.R().z();
         Module var7 = var5.d("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      }

      HUDModule var1 = HUDModule.K();
      if (var0.r.equals("island")) {
         IslandLyricsModule var6 = IslandLyricsModule.K();
         return var6 == null ? List.of() : var6.getSettings();
      }

      if (!var0.r.equals("target") && !var0.r.equals("staffs")) {
         return var1 == null ? List.of() : var1.w(var0.r);
      }

      com.larpclient.module.f var2 = LarpClient.R().z();
      Module var3 = var0.r.equals("target") ? var2.p(TargetHUDModule.class) : var2.p(StaffListModule.class);
      ArrayList var4 = new ArrayList();
      if (var3 != null) {
         var4.addAll(var3.getSettings());
      }

      if (var1 != null) {
         var4.addAll(var1.w(var0.r));
      }

      return List.copyOf(var4);
   }

   public static boolean e(List<ItemStack> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.isEmpty());
   }

   public static String s(m4.p var0) {
      if (var0.k()) {
         return "Hidden";
      }

      if (!B(var0)) {
         return "Disabled";
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null) {
         return "Waiting for world";
      }

      int var2 = switch (var0.r) {
         case "notifications" -> s0.s().o();
         case "target" -> kz.p() == null && (F == null || !(Z.B() > 0.01)) ? 0 : 1;
         case "effects" -> !var1.player.getActiveEffects().isEmpty() ? 1 : 0;
         case "keybinds" -> 1;
         case "modules" -> LarpClient.R().z().h().stream().anyMatch(var0x -> var0x.isOperational());
         case "armor" -> {
            HUDModule var5 = HUDModule.K();
            yield var5 != null
                  && Arrays.stream(f)
                     .filter(var1x -> var5.a.getValue() || var1x != EquipmentSlot.MAINHAND && var1x != EquipmentSlot.OFFHAND)
                     .anyMatch(var1x -> !var1.player.getItemBySlot(var1x).isEmpty())
               ? 1
               : 0;
         }
         case "cooldowns", "staffs" -> cl.e(var0.r, cl.e(var0.r, false));
         case "crosshair" -> var1.options.getCameraType().isFirstPerson();
         case "island" -> IslandLyricsModule.W();
         default -> 1;
      };
      return var2 != 0 ? "Visible" : "Waiting for content";
   }

   private static m4.i x(String var0) {
      return k.stream().filter(var1 -> var1.k.equals(var0)).findFirst().orElseThrow();
   }

   private static List<m4.p> S(List<m4.p> var0) {
      ArrayList var1 = new ArrayList(var0);

      for (m4.i var3 : k) {
         var1.add(new m4.p(var3.k, 196, 80, 0.5, 0.1));
      }

      return List.copyOf(var1);
   }

   public static List<m4.p> P() {
      return com.larpclient.module.c.p() ? O : c;
   }

   public static m4.p S(String var0) {
      return c.stream().filter(var1 -> var1.r.equals(var0)).findFirst().orElseThrow();
   }

   public static String W(String var0) {
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

   public static boolean B(m4.p var0) {
      if (var0.k()) {
         return false;
      }

      HUDModule var1 = HUDModule.K();

      return (boolean)(switch (var0.r) {
         case "crosshair" -> CustomCrosshairModule.p();
         case "notifications" -> 1;
         case "target" -> {
            TargetHUDModule var4 = LarpClient.R().z().p(TargetHUDModule.class);
            yield var4 != null && var4.isOperational() ? 1 : 0;
         }
         case "watermark" -> var1 != null && var1.L() ? 1 : 0;
         case "info" -> var1 != null && var1.x() ? 1 : 0;
         case "modules" -> var1 != null && var1.A() ? 1 : 0;
         case "armor" -> var1 != null && var1.d() ? 1 : 0;
         case "keybinds" -> var1 != null && var1.O() ? 1 : 0;
         case "effects" -> var1 != null && var1.m() ? 1 : 0;
         case "speed" -> var1 != null && var1.f() ? 1 : 0;
         case "miner", "builder", "pilot", "hunter" -> x(var0.r).t() != null ? 1 : 0;
         case "staffs" -> StaffListModule.v();
         case "island" -> IslandLyricsModule.Z();
         case "casino" -> com.larpclient.module.to.A() != null ? 1 : 0;
         case "cooldowns", "inventory", "keystrokes" -> var1 != null && var1.g(var0.r) ? 1 : 0;
         default -> var1 != null && var1.isOperational() ? 1 : 0;
      });
   }

   public static void D() {
      F(var0 -> true);
   }

   public static void F(Predicate<m4.p> var0) {
      for (m4.p var2 : c) {
         if (var0.test(var2)) {
            c7.v.remove(var2.r);
            c7.l var3 = c7.y.get(var2.r);
            if (var3 != null) {
               c7.y.put(var2.r, new c7.l(var2.E(), var3.hidden()));
            }
         }
      }
   }

   public static void Z(m4.p var0, boolean var1) {
      U(var0, var1, var0x -> LarpClient.R().z().d(var0x));
   }

   public static void U(m4.p var0, boolean var1, Function<String, Module> var2) {
      var0.z(!var1);
      if (var1 && !var0.r.equals("notifications")) {
         Module var3 = (Module)var2.apply(
            var0.r.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.r.equals("target") ? "TargetHUD" : (var0.r.equals("staffs") ? "Staff List" : (var0.r.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var4 = switch (var0.r) {
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

            for (com.larpclient.module.q var9 : var3.getSettings()) {
               if (var9.e().equals(var4) && var9 instanceof com.larpclient.module.s var7) {
                  var7.setValue(true);
               }
            }

            var3.setEnabled(true);
         }
      }
   }

   public void S() {
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "hudoverlay"),
         (var0, var1) -> {
            Minecraft var2 = Minecraft.getInstance();
            if (var2.player != null
               && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && !hi.X(var2.gui.screen())
               && !(var2.gui.screen() instanceof h7)) {
               GuiGraphicsExtractor var3 = p2.x(var0, false, false);
               if (var3 != null) {
                  Z(var3);
                  G(var3, false);
               }
            }
         }
      );
      com.larpclient.h.A("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", c.size());
   }

   public static void B(Minecraft var0) {
      HUDModule var1 = HUDModule.K();
      if (var0.level == null || var0.player == null || var1 == null || !var1.isOperational()) {
         z.b();
      } else if (!var0.isPaused() && var0.level.tickRateManager().runsNormally()) {
         z.W(var0.level, var0.player, var0.player.getX(), var0.player.getZ(), var0.level.tickRateManager().tickrate());
      } else {
         z.E();
      }
   }

   private static List<ItemStack> o() {
      if (D == null) {
         D = List.of(
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         );
      }

      return D;
   }

   public static void G(GuiGraphicsExtractor var0, boolean var1) {
      r(var0, var1, null, false);
   }

   public static void Y(GuiGraphicsExtractor var0, m4.p var1, boolean var2) {
      r(var0, true, var1, var2);
   }

   public static List<m4.k> J() {
      return h == c7.w ? I : List.of();
   }

   public static void a() {
      I = List.of();
   }

   public static void N(GuiGraphicsExtractor var0, m4.p var1, boolean var2, boolean var3) {
      if (var3) {
         Z(var0);
         G(var0, false);
         I = I();
         h = c7.w;
      } else {
         Y(var0, var1, var2);
      }
   }

   private static void r(GuiGraphicsExtractor var0, boolean var1, m4.p var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            I = List.of();
         }

         long var6 = zj.m();
         HUDModule var8 = HUDModule.K();
         int var9 = var4.getWindow().getGuiScaledWidth();
         int var10 = var4.getWindow().getGuiScaledHeight();
         int var11 = !var1 && t() ? 1 : 0;
         if (!var1 && var11 != x) {
            x = (boolean)var11;
            com.larpclient.h.U("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List var12 = m.K(ChooseASkyEffectAndAdjustItsAppearanceModule.Z(LarpClient.R().z().a(), LarpClient.R().o().g()), var4.font, bj.X(), wa.s, gw::S);
         int var13 = (var1 || var11 != 0) && var12.isEmpty() ? 1 : 0;
         if (var13 != 0) {
            var12 = List.of(new k8.l("Sprint", gw.S("Sprint")), new k8.l("Fullbright", gw.S("Fullbright")));
         }

         m4.p var14 = S("modules");
         double var15 = var14.k();
         int var17 = Math.max(1, (int)((var10 - 24) / var15 / 17.0));
         int var18 = Math.max(1, (var12.size() + var17 - 1) / var17);
         int var19 = Math.max(20, var12.stream().mapToInt(var0x -> u9.s(var0x.width())).max().orElse(0));
         hv.h var20 = var8 != null && !var8.o() ? null : hv.n(var12, var1 || var13 != 0, hv.A(var10, var15));
         if (var20 != null) {
            var14.d = var20.width();
            var14.T = var20.height();
         } else {
            var14.d = var18 * var19 + (var18 - 1) * 8;
            var14.T = Math.max(20, Math.min(var17, var12.size()) * 17);
         }

         var14.m = Math.max(0.1, Math.min(4.0, Math.min((var9 - 16.0) / var14.d, (var10 - 16.0) / var14.T)));
         ArrayList var21 = new ArrayList();
         if (var4.player != null && var8 != null) {
            for (EquipmentSlot var25 : f) {
               if (var25 != EquipmentSlot.MAINHAND && var25 != EquipmentSlot.OFFHAND || var8.a.getValue()) {
                  ItemStack var26 = var4.player.getItemBySlot(var25);
                  if (!var26.isEmpty() || var8.B.getValue()) {
                     var21.add(var26);
                  }
               }
            }

            if (var8.k.getValue()) {
               Collections.reverse(var21);
            }
         }

         if ((var1 || var11 != 0) && var21.stream().allMatch(ItemStack::isEmpty)) {
            var21.clear();
            var21.addAll(o());
            if (var8 != null && var8.k.getValue()) {
               Collections.reverse(var21);
            }
         }

         int var69 = var8 != null && var8.O.O("Remaining") ? 46 : 26;
         int var70 = var8 != null && !var8.O.O("Off") ? 37 : 27;
         int var71 = var8 == null ? 5 : var8.z.getValue().intValue();
         int var72 = var8 != null && var8.J.O("Vertical") ? 1 : 0;
         if (var72 != 0 && var8 != null && !var8.O.O("Off")) {
            var69 += 38;
            var70 = 26;
         }

         int var73 = Math.max(1, var21.size());
         S("armor").d = 6 + (var72 != 0 ? var69 : var73 * (var69 + var71) - var71);
         S("armor").T = 6 + (var72 != 0 ? var73 * (var70 + var71) - var71 : var70);
         String var27 = FakeFPSModule.p(var4.getFps()) + " fps";
         String var28 = var4.player == null ? "Player" : ci.S(var4.player.getUUID(), var4.player.getName().getString());
         var28 = gw.J(var28, 110.0, 11.0);
         String var29 = !var4.hasSingleplayerServer() && var4.getCurrentServer() != null ? var4.getCurrentServer().ip : "singleplayer";
         var29 = gw.J(var29, 130.0, 11.0);
         String var30 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         String var31 = var4.player == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.player.getX(), var4.player.getY(), var4.player.getZ());
         S("info").d = u9.V(var31);
         m4.p var32 = S("watermark");
         m4.p var33 = S("info");
         boolean var34 = D(var32, var1, var3, var2);
         int var35 = !var34 || !D(var33, var1, var3, var2) || !J(var33) && T != var33 ? 0 : 1;
         if (a != c7.w) {
            a = c7.w;
            Y.F(var35 != 0 ? 1.0 : 0.0);
         }

         double var36 = Math.clamp(Y.k(var35 != 0 ? 1.0 : 0.0, 220), 0.0, 1.0);
         u = var34;
         int var38 = var8 != null && var8.o.O("Right") ? 1 : 0;
         hw.l var39 = null;
         var32.d = u9.g(u9.R(var28, var29, var27, var30), var31, var36);
         var32.T = u9.L(var36);
         int var40 = var11 == 0 || kz.p() != null || F != null && !(Z.B() < 0.01) ? 0 : 1;
         HashMap var41 = new HashMap();

         for (String var43 : List.of("cooldowns", "staffs", "inventory")) {
            cl.w var44 = cl.e(var43, var1);
            var41.put(var43, var44);
            S(var43).d = var44.width();
            S(var43).T = var44.height();
         }

         if (var8 != null) {
            S("keystrokes").d = to.q(var8);
            S("keystrokes").T = to.v(var8);
         }

         gw.e var76 = gw.I(var1);
         gw.e var77 = gw.K(var1);
         gw.e var78 = gw.o(var1);
         S("casino").d = var78.width();
         S("casino").T = var78.height();

         for (m4.i var46 : k) {
            var46.N(var1);
         }

         int var79 = var11 == 0 || var4.player != null && !var4.player.getActiveEffects().isEmpty() ? 0 : 1;
         if (var79 != 0) {
            var77 = gw.K(true);
         }

         S("keybinds").d = var76.width();
         S("keybinds").T = var76.height();
         S("effects").d = var77.width();
         S("effects").T = var77.height();

         for (m4.p var47 : P()) {
            var47.b(var9, var10);
            int var48 = var47.T(var9);
            int var49 = var47.P(var10);
            boolean var50 = B(var47);
            if ((var1 ? hw.H(var50, var3, var2 == var47) : var50)
               && (var1 || (!var47.r.equals("modules") || (var20 == null ? !var12.isEmpty() : !var20.empty())) && (!var47.r.equals("armor") || e(var21)))
               && (var1 || var11 != 0 || !var41.containsKey(var47.r) || cl.e(var47.r, (cl.w)var41.get(var47.r)))
               && (var1 || var79 != 0 || !var47.r.equals("effects") || var4.player != null && !var4.player.getActiveEffects().isEmpty())) {
               int var51 = !var1 && (var11 == 0 || IslandLyricsModule.W()) ? 0 : 1;
               if (var1 || !var47.r.equals("island") || var51 != 0 || IslandLyricsModule.W()) {
                  int var52 = !var47.r.equals("notifications") && !var47.r.equals("crosshair") ? 0 : 1;
                  if (var52 == 0 || var1) {
                     if (var47 != var33 || var35 == 0) {
                        var47.N(var0, var48, var49);

                        try {
                           switch (var47.r) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                                 cl.R(var0, var47.r, (cl.w)var41.get(var47.r), var48, var49, var1);
                                 break;
                              case "island":
                                 IslandLyricsModule var93 = IslandLyricsModule.K();
                                 if (var93 == null) {
                                    continue;
                                 }

                                 var93.N(var0, var48, var49, var47.d, (boolean)var51);
                                 break;
                              case "keystrokes":
                                 if (var8 == null) {
                                    continue;
                                 }

                                 if (var8.p() && !(var4.gui.screen() instanceof hf)) {
                                    T(var0, var48, var49, var47.d, var47.T);
                                 }

                                 to.Y(var0, var8, var48, var49, var1);
                                 break;
                              case "notifications":
                                 s0.z(var0, var48, var49);
                                 break;
                              case "crosshair":
                                 CustomCrosshairModule.b(var0, var48 + var47.d / 2, var49 + var47.T / 2);
                                 break;
                              case "watermark":
                                 if (!var1 && !var8.L()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.p() && !(var4.gui.screen() instanceof hf)) {
                                    T(var0, var48, var49, var47.d, var47.T);
                                 }

                                 u9.y(var0, var48, var49, var47.d, var28, var29, var27, var30, var31, var36);
                                 if (var35 != 0) {
                                    hw.l var92 = u9.s(var48, var49, var47.d, (boolean)var38, var31);
                                    var39 = new hw.l(
                                       var48 + (int)Math.floor((var92.x() - var48) * var47.s()),
                                       var49 + (int)Math.floor((var92.y() - var49) * var47.s()),
                                       (int)Math.ceil(var92.width() * var47.s()),
                                       (int)Math.ceil(var92.height() * var47.s())
                                    );
                                 }
                                 break;
                              case "speed":
                                 u9.u(var0, var48, var49, z, var1);
                                 break;
                              case "miner":
                              case "builder":
                              case "pilot":
                              case "hunter":
                                 m4.i var91 = x(var47.r);
                                 if (var91.Q == null) {
                                    continue;
                                 }

                                 if (var8 != null && var8.p() && !(var4.gui.screen() instanceof hf)) {
                                    T(var0, var48, var49, var47.d, var47.T);
                                 }

                                 gw.I(
                                    var0,
                                    var48,
                                    var49,
                                    var47.d,
                                    var91.s,
                                    "scan",
                                    var91.Q.headline(),
                                    var91.Q.value(),
                                    var91.Q.action(),
                                    var91.Q.time(),
                                    var91.v,
                                    var91.Q.alert()
                                 );
                                 break;
                              case "info":
                                 if (!var1 && !var8.x()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.p() && !(var4.gui.screen() instanceof hf)) {
                                    T(var0, var48, var49, var47.d, var47.T);
                                 }

                                 u9.r(var0, var48, var49, var47.d, var31);
                                 break;
                              case "modules":
                                 if (!var1 && !var8.A()) {
                                    continue;
                                 }

                                 int var90 = var8 != null && var8.p() && !(var4.gui.screen() instanceof hf) ? 1 : 0;
                                 if (var20 != null) {
                                    if (var90 != 0) {
                                       T(var0, var48, var49, var47.d, var47.T);
                                    }

                                    hv.j(var0, var20, var48, var49);
                                    if (!var1) {
                                       k(var47, var47.X(var9, var10));
                                    }
                                 } else {
                                    for (int var95 = 0; var95 < var12.size(); var95++) {
                                       String var98 = ((k8.l)var12.get(var95)).name();
                                       int var101 = var95 / var17;
                                       int var102 = var95 % var17;
                                       int var103 = var48 + var47.A() / 2 > var9 / 2 ? 1 : 0;
                                       int var104 = var103 != 0
                                          ? var48 + var47.d - var101 * (var19 + 8)
                                          : var48 + var101 * (var19 + 8) + u9.s(((k8.l)var12.get(var95)).width());
                                       int var62 = ((k8.l)var12.get(var95)).width();
                                       int var105 = u9.s(var62);
                                       int var106 = var104 - var105;
                                       int var107 = var49 + var102 * 17;
                                       if (var90 != 0) {
                                          T(var0, var106, var107, var105, 17);
                                       }

                                       u9.I(var0, var98, var62, var104, var107, (boolean)var103);
                                       if (!var1) {
                                          k(
                                             var47,
                                             new hw.l(
                                                var48 + (int)Math.floor((var106 - var48) * var47.s()),
                                                var49 + (int)Math.floor((var107 - var49) * var47.s()),
                                                (int)Math.ceil(var105 * var47.s()),
                                                (int)Math.ceil(17.0 * var47.s())
                                             )
                                          );
                                       }
                                    }
                                 }

                                 if (var1 && var12.isEmpty() && var20 == null) {
                                    bj.h(var0, "No active modules", var48 + 4, var49 + 5, ho.Z);
                                 }
                                 break;
                              case "target":
                                 if (!var1 && var40 == 0) {
                                    f(var0, var48, var49);
                                 } else {
                                    U(var0, var48, var49);
                                 }
                                 break;
                              case "keybinds":
                              case "effects":
                              case "casino":
                                 gw.e var89 = var47.r.equals("keybinds") ? var76 : (var47.r.equals("casino") ? var78 : var77);
                                 int var94 = var48 + var47.A() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.p() && !(var4.gui.screen() instanceof hf)) {
                                    for (hw.l var99 : gw.m(var89, var48, var49, (boolean)var94)) {
                                       T(var0, var99.x(), var99.y(), var99.width(), var99.height());
                                    }
                                 }

                                 gw.g(var0, var89, var48, var49, (boolean)var94);
                                 if (!var1) {
                                    for (hw.l var100 : gw.m(var89, var48, var49, (boolean)var94)) {
                                       k(
                                          var47,
                                          new hw.l(
                                             var48 + (int)Math.floor((var100.x() - var48) * var47.s()),
                                             var49 + (int)Math.floor((var100.y() - var49) * var47.s()),
                                             (int)Math.ceil(var100.width() * var47.s()),
                                             (int)Math.ceil(var100.height() * var47.s())
                                          )
                                       );
                                    }
                                 }
                                 break;
                              case "armor":
                                 if (!var1 && !var8.d()) {
                                    continue;
                                 }

                                 if (var8.m.getValue() && e(var21)) {
                                    kv.f(var0, var48, var49, var47.d, var47.T, 6.0F, u9.w());
                                    u9.n(var0, var48, var49, var47.d, var47.T, 6.0F);
                                    u9.M(var0, var48, var49, var47.d, var47.T, 6);
                                 }

                                 for (int var55 = 0; var55 < var21.size(); var55++) {
                                    ItemStack var56 = (ItemStack)var21.get(var55);
                                    int var57 = var48 + 3 + (var72 != 0 ? 0 : var55 * (var69 + var71));
                                    int var58 = var49 + 3 + (var72 != 0 ? var55 * (var70 + var71) : 0);
                                    if (!var56.isEmpty()) {
                                       int var59 = var72 != 0 ? var57 : var57 + (var69 - 21) / 2;
                                       var0.pose().pushMatrix();
                                       var0.pose().translate(var59, var58).scale(1.3125F);
                                       var0.item(var56, 0, 0);
                                       var0.pose().popMatrix();
                                       if (var56.isDamageableItem()) {
                                          int var60 = Math.max(0, var56.getMaxDamage() - var56.getDamageValue());
                                          double var61 = (double)var60 / Math.max(1, var56.getMaxDamage());
                                          int var63 = uh.q(-43434, -8790368, var61);
                                          if (var8.N.getValue()) {
                                             kv.d(var0, var57 + 2, var58 + 23, var69 - 4, 2, 1879048192);
                                             kv.d(var0, var57 + 2, var58 + 23, (int)Math.round((var69 - 4) * var61), 2, var63);
                                          }

                                          if (!var8.O.O("Off")) {
                                             String var64 = var8.O.O("Percent") ? (int)Math.ceil(var61 * 100.0) + "%" : Integer.toString(var60);
                                             int var65 = var72 != 0 ? var57 + 28 : var57 + (var69 - gw.m(var64, 10.4)) / 2;
                                             gw.S(var0, var64, var65, var58 + (var72 != 0 ? 7 : 28), 10.4, var63);
                                          }
                                       }
                                    }
                                 }
                           }
                        } finally {
                           var47.w(var0);
                        }

                        if (!var1
                           && (
                              var47.r.equals("target")
                                 ? var40 != 0
                                 : !var47.r.equals("modules") && !var47.r.equals("keybinds") && !var47.r.equals("effects") && !var47.r.equals("casino")
                           )) {
                           k(var47, var47.X(var9, var10));
                        }

                        if (var1) {
                           var5.add(new m4.k(var47, var47.X(var9, var10)));
                        }

                        if (var1 && !var50) {
                           hw.l var88 = var47.X(var9, var10);
                           kv.d(var0, var88.x(), var88.y(), var88.width(), var88.height(), 1882207290);
                        }
                     } else if (var39 != null) {
                        if (var1) {
                           var5.add(new m4.k(var47, var39));
                        } else {
                           k(var47, var39);
                        }
                     }
                  }
               }
            }
         }

         if (var1) {
            I = List.copyOf(var5);
            h = c7.w;
         }

         if (!var1 && Boolean.getBoolean("larp.debug")) {
            int var81 = var10 - 15;

            for (Entry var84 : zj.R().entrySet()) {
               String var86 = (String)var84.getKey() + " " + var84.getValue();
               bj.h(var0, var86, var9 - bj.q(var86) - 5, var81, ho.Z);
               var81 -= 11;
            }

            for (Entry var85 : zj.J().entrySet()) {
               String var87 = String.format(Locale.ROOT, "%s %.2f ms CPU", var85.getKey(), var85.getValue());
               bj.h(var0, var87, var9 - bj.q(var87) - 5, var81, ho.Z);
               var81 -= 11;
            }
         }

         zj.l("HUD", var6);
      }
   }

   private static void T(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.pose().transformPosition(var1, var2, new Vector2f());
      wa.T(Math.round(var5.x), Math.round(var5.y), Math.round(var3 * var0.pose().m00()), Math.round(var4 * var0.pose().m11()));
   }

   public static void s() {
      F = null;
      E.F(1.0);
      s.F(1.0);
      t = 1.0;
      l = 0L;
      Z.F(0.0);
      j.z();
      L = 0L;
      P = 0.0F;
   }

   public static void U(GuiGraphicsExtractor var0, int var1, int var2) {
      U(var0, var1, var2, Minecraft.getInstance().player, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0);
   }

   public static void U(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3, String var4, float var5, float var6, float var7, double var8) {
      double var10 = Math.clamp(var5 / Math.max(1.0F, var6), 0.0F, 1.0F);
      D(var0, var1, var2, var3, var4, var5, var6, var7, var8, 1.0, var10, var10, true);
   }

   private static void f(GuiGraphicsExtractor var0, int var1, int var2) {
      long var3 = zj.m();

      try {
         Minecraft var5 = Minecraft.getInstance();
         if (F != null && F.level() != var5.level) {
            s();
         }

         LivingEntity var6 = kz.p();
         if (var6 != null && var6 != F) {
            F = var6;
            com.larpclient.h.U(
               "TargetHUD",
               "Target {}: {} x {} blocks, portrait scale {} before HUD transform",
               var6.getType(),
               var6.getBbWidth(),
               var6.getBbHeight(),
               p(var6.getBbWidth(), var6.getBbHeight())
            );
            j.z();
            P = zy.f(var6);
            L = System.nanoTime();
            double var7 = zy.N(var6);
            E.F(var7);
            s.F(var7);
            t = var7;
            l = 0L;
         }

         long var22 = System.nanoTime();
         if (L != 0L) {
            j.d((var22 - L) / 1.0E9);
         }

         L = var22;
         float var9 = F == null ? 0.0F : zy.f(F);
         if (F != null) {
            j.S(P - var9, ThreadLocalRandom.current());
            P = var9;
         }

         double var10 = Z.k(var6 == null ? 0.0 : 1.0, 200);
         if (F == null || var10 < 0.01) {
            if (var6 == null) {
               F = null;
               j.z();
            }

            return;
         }

         m4.p var12 = S("target");
         double var13 = 0.92 + 0.08 * var10;
         k(var12, v(var1, var2, var12.s(), var13, new hw.l(0, 0, 224, 52)));
         double var15 = Math.clamp(var9 / Math.max(1.0F, F.getMaxHealth()), 0.0F, 1.0F);
         double var17 = E.k(var15, 220);
         D(
            var0,
            var1,
            var2,
            F,
            ci.S(F.getUUID(), F.getName().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, F.getMaxHealth()),
            Math.max(0.0F, F.getAbsorptionAmount()),
            var5.player == null ? 0.0 : var5.player.distanceTo(F),
            var10,
            var17,
            O(var15, var17, var22),
            false
         );
      } finally {
         zj.l("TargetHUD", var3);
      }
   }

   private static double O(double var0, double var2, long var4) {
      if (var0 < t) {
         s.F(Math.max(s.B(), var2));
         l = var4 + 400000000L;
      }

      t = var0;
      double var6 = var4 < l ? s.B() : s.y(var0, 450, jp.m.X);
      return Math.max(var6, var2);
   }

   public static hw.l v(int var0, int var1, double var2, double var4, hw.l var6) {
      double var7 = 112.0;
      double var9 = 26.0;
      double var11 = var0 + (var7 + (var6.x() - var7) * var4) * var2;
      double var13 = var1 + (var9 + (var6.y() - var9) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new hw.l(var15, var16, (int)Math.ceil(var11 + var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + var6.height() * var4 * var2) - var16);
   }

   public static float p(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void K(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3) {
      EntityRenderer var4 = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var3);
      EntityRenderState var5 = var4.createRenderState(var3, 1.0F);
      float var6 = p(var5.boundingBoxWidth, var5.boundingBoxHeight);
      if (var6 <= 0.0F) {
         com.larpclient.h.w("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         gw.i(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
      } else {
         var5.shadowPieces.clear();
         var5.outlineColor = 0;
         if (var5 instanceof LivingEntityRenderState var7) {
            var7.bodyRot = 180.0F;
            var7.yRot = 0.0F;
            var7.xRot = 0.0F;
         }

         ScreenRectangle var11 = new ScreenRectangle(var1, var2, 50, 50).transformAxisAligned(var0.pose());
         var0.enableScissor(var1, var2, var1 + 50, var2 + 50);

         try {
            var0.entity(
               var5,
               var6 * var0.pose().m00(),
               new Vector3f(0.0F, var5.boundingBoxHeight / 2.0F, 0.0F),
               new Quaternionf().rotateZ((float) Math.PI),
               new Quaternionf(),
               var11.left(),
               var11.top(),
               var11.right(),
               var11.bottom()
            );
         } finally {
            var0.disableScissor();
         }
      }
   }

   public static List<m4.q> G(boolean var0, boolean var1) {
      if (var1) {
         return var0 ? List.of(m4.q.j, m4.q.z, m4.q.Z) : List.of(m4.q.z, m4.q.Z, m4.q.j);
      } else {
         return var0 ? List.of(m4.q.Z, m4.q.j, m4.q.z) : List.of(m4.q.Z, m4.q.z, m4.q.j);
      }
   }

   private static String V(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static void D(
      GuiGraphicsExtractor var0,
      int var1,
      int var2,
      LivingEntity var3,
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
      Minecraft var17 = Minecraft.getInstance();
      TargetHUDModule var18 = LarpClient.R().z().p(TargetHUDModule.class);
      short var19 = 224;
      byte var20 = 52;
      double var21 = 0.92 + 0.08 * var10;
      var0.pose().pushMatrix();
      var0.pose().translate(var1 + var19 / 2.0F, var2 + var20 / 2.0F).scale((float)var21).translate(-var1 - var19 / 2.0F, -var2 - var20 / 2.0F);

      try {
         HUDModule var23 = HUDModule.K();
         if (var23 != null && var23.p() && !(var17.gui.screen() instanceof hf)) {
            T(var0, var1, var2, var19, var20);
         }

         kv.f(var0, var1, var2, var19, var20, 10.0F, uh.F(-468906478, var10));
         int var24 = var1 + 8;
         int var25 = var2 + 8;
         int var26 = var24 + 18;
         int var27 = var25 + 18;
         if (!var16 && !j.D().isEmpty()) {
            var0.enableScissor(var1, var2, var1 + var19, var2 + var20);

            try {
               for (w8.k var29 : j.D()) {
                  double var30 = Math.clamp(var29.p / var29.W, 0.0, 1.0);
                  double var32 = var10 * (1.0 - var30) * (var29.I ? 1.0 : 0.72);
                  int var34 = Math.max(1, (int)Math.round(var29.J * (1.0 - var30 * 0.35)));
                  int var35 = var26 + (int)Math.round(var29.y);
                  int var36 = var27 + (int)Math.round(var29.Z);
                  int var37 = var29.I ? ho.J : -419430401;
                  kv.Q(var0, var35, var36, (int)Math.ceil(var34 * 1.125), uh.F(var37, var32 * 0.22));
                  kv.Q(var0, var35, var36, Math.max(1, var34 / 2), uh.F(var37, var32));
               }
            } finally {
               var0.disableScissor();
            }
         }

         kv.f(var0, var24 - 1, var25 - 1, 38.0F, 38.0F, 3.0F, uh.F(822083583, var10));
         if (var3 instanceof AbstractClientPlayer var55) {
            int var57 = Math.min(140, var55.hurtTime * 16);
            var0.pose().pushMatrix();
            float var59 = (float)Math.sin(var55.hurtTime / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.pose().translate(var26, var27).rotate(var59).translate(-var26, -var27);
            PlayerFaceExtractor.extractRenderState(var0, var55.getSkin(), var24, var25, 36, uh.F(-65536 | 255 - var57 << 8 | 255 - var57, var10));
            var0.pose().popMatrix();
         } else if (var3 != null) {
            var0.pose().pushMatrix();
            var0.pose().translate(var24, var25).scale(0.72F).translate(-var24, -var25);

            try {
               K(var0, var24, var25, var3);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            gw.i(var0, "user", var24 + 7, var25 + 7, 22, uh.F(-5592406, var10));
         }

         int var56 = var18 != null && !var18.i.O("Above") ? 0 : 1;
         int var58 = var18 != null && var18.X.O("Above") ? 1 : 0;
         int var60 = var18 != null && var18.F.O("Left") ? 1 : 0;
         int var31 = var1 + 53;
         int var61 = var1 + var19 - 9;
         int var33 = uh.F(-1, var10);
         int var62 = uh.F(-6645086, var10);
         double var63 = var25;

         for (m4.q var38 : G((boolean)var56, (boolean)var58)) {
            int var39 = (int)Math.round(var63);
            switch (var38) {
               case Z:
                  String var65 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
                  int var41 = var61 - gw.m(var65, 10.0);
                  gw.S(var0, var65, var41, var39 + 1.5, 10.0, var62);
                  gw.S(var0, gw.J(var4, Math.max(0, var41 - 6 - var31), 13.0), var31, var39, 13.0, var33);
                  break;
               case j:
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, X, var31, var39 + 2, 9, 9, var33);
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, Q, var31, var39 + 2, 9, 9, var33);
                  String var40 = V(var5);
                  gw.S(var0, var40, var31 + 12, var39 + 1, 12.0, var33);
                  if (var7 > 0.0F) {
                     gw.S(var0, "+" + V(var7), var31 + 16 + gw.m(var40, 12.0), var39 + 2.5, 10.0, uh.F(-14003, var10));
                  }

                  if (var3 instanceof AbstractClientPlayer) {
                     C(var0, var3, var61 - 79, var39, (boolean)var60, var10);
                  }
                  break;
               case z:
                  s(var0, var31, var39, var61 - var31, var12, var14, var6, var7, var10);
            }

            var63 += var38.Y + 1.5;
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   private static void s(GuiGraphicsExtractor var0, int var1, int var2, int var3, double var4, double var6, float var8, float var9, double var10) {
      double var12 = var8 / (var8 + Math.max(0.0F, var9));
      kv.f(var0, var1, var2, var3, 5.0F, 2.0F, uh.F(620756991, var10));
      int var14 = (int)Math.round(var3 * Math.clamp(var4, 0.0, 1.0) * var12);
      int var15 = (int)Math.round(var3 * Math.clamp(var6, 0.0, 1.0) * var12);
      if (var15 > var14) {
         kv.f(var0, var1, var2, var15, 5.0F, 2.0F, uh.F(-1862270977, var10));
      }

      if (var14 > 0) {
         tb.d(var0, var1, var2, var14, 5.0F, 2.0F, uh.F(-4776932, var10), uh.F(-42418, var10));
      }

      if (var9 > 0.0F) {
         int var16 = Math.min(var3 - var14, Math.round(var3 * var9 / (var8 + var9)));
         if (var16 > 0) {
            kv.f(var0, var1 + var14, var2, var16, 5.0F, 2.0F, uh.F(-14003, var10));
         }
      }
   }

   private static void C(GuiGraphicsExtractor var0, LivingEntity var1, int var2, int var3, boolean var4, double var5) {
      int var7 = var2;

      for (int var8 = 0; var8 < 2; var8++) {
         int var9 = var4 == (var8 == 0 ? 1 : 0) ? 1 : 0;
         int var10 = var9 != 0 ? 4 : 0;
         int var11 = var9 != 0 ? 6 : 4;

         for (int var12 = var10; var12 < var11; var12++) {
            ItemStack var13 = var1.getItemBySlot(f[var12]);
            if (var13.isEmpty()) {
               kv.f(var0, var7 + 3, var3 + 3, 6.0F, 6.0F, 2.0F, uh.F(486539263, var5));
            } else {
               if (var5 > 0.5) {
                  var0.pose().pushMatrix();
                  var0.pose().translate(var7, var3).scale(0.75F);

                  try {
                     var0.item(var13, 0, 0);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }

               if (var13.isDamageableItem() && var13.getDamageValue() > 0) {
                  double var14 = Math.clamp((double)(var13.getMaxDamage() - var13.getDamageValue()) / Math.max(1, var13.getMaxDamage()), 0.0, 1.0);
                  kv.f(var0, var7 + 1, var3 + 12, 10.0F, 1.5F, 0.75F, uh.F(-1610612736, var5));
                  kv.f(var0, var7 + 1, var3 + 12, (float)Math.max(1.0, 10.0 * var14), 1.5F, 0.75F, uh.F(uh.q(-43434, -10755203, var14), var5));
               }
            }

            var7 += 13;
         }

         var7 += 2;
      }
   }

   private static final class i {
      final String k;
      final String s;
      final Supplier<? extends s1> W;
      final s1.u w;
      final jp d = new jp(0.0);
      boolean x;
      s1.u Q;
      double v;

      i(String var1, String var2, Supplier<? extends s1> var3, s1.u var4) {
         this.k = var1;
         this.s = var2;
         this.W = var3;
         this.w = var4;
      }

      s1.u t() {
         s1 var1 = this.W.get();
         return var1 == null ? null : var1.hudCard();
      }

      void N(boolean var1) {
         s1.u var2 = this.t();
         this.Q = var2 != null ? var2 : (var1 ? this.w : null);
         this.v = this.Q == null ? 0.0 : this.Q.progress();
         if (var2 != null && !var1) {
            if (!this.x) {
               this.d.F(Math.max(0.0, this.v));
            }

            this.v = this.d.k(Math.max(0.0, this.v), 450);
         }

         if (!var1) {
            this.x = (boolean)(var2 != null ? 1 : 0);
         }

         if (this.Q != null) {
            m4.S(this.k).d = gw.t(this.s, this.Q.headline(), this.Q.value(), this.Q.action(), this.Q.time());
         }
      }
   }

   public record k(m4.p element, hw.l bounds) {
   }

   public static final class p {
      public final String r;
      public int d;
      public int T;
      private double m = 4.0;
      private final c7.q y;

      p(String var1, int var2, int var3, double var4, double var6) {
         this.r = var1;
         this.d = var2;
         this.T = var3;
         this.y = new c7.q(var4, var6);
      }

      public double E() {
         return switch (this.r) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "miner", "builder", "pilot", "hunter" -> 0.8;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double k() {
         c7.l var1 = c7.y.get(this.r);
         return var1 == null ? this.E() : var1.scale();
      }

      public double s() {
         return Math.min(this.m, this.k());
      }

      public void b(int var1, int var2) {
         this.m = Math.max(0.1, Math.min(4.0, Math.min((double)Math.max(1, var1) / this.d, (double)Math.max(1, var2) / this.T)));
      }

      public hw.l X(int var1, int var2) {
         if (this.r.equals("crosshair")) {
            int var3 = CustomCrosshairModule.q();
            return new hw.l(
               this.T(var1) + (int)Math.round((this.d / 2.0 - var3) * this.s()),
               this.P(var2) + (int)Math.round((this.T / 2.0 - var3) * this.s()),
               (int)Math.ceil(var3 * 2 * this.s()),
               (int)Math.ceil(var3 * 2 * this.s())
            );
         } else {
            return new hw.l(this.T(var1), this.P(var2), this.A(), this.r.equals("notifications") ? (int)Math.round(30.0 * this.s()) : this.m());
         }
      }

      public hw.l S(int var1, int var2) {
         return new hw.l(this.T(var1), this.P(var2), this.A(), this.m());
      }

      public void g() {
         c7.v.remove(this.r);
         c7.y.put(this.r, new c7.l(this.E(), this.k()));
      }

      public void I() {
         c7.v.remove(this.r);
         c7.y.remove(this.r);
      }

      public void U() {
         c7.v.put(this.r, new c7.q(0.5, 0.5));
      }

      public boolean k() {
         return c7.y.getOrDefault(this.r, m4.V).hidden();
      }

      public int A() {
         return (int)Math.round(this.d * this.s());
      }

      public int m() {
         return (int)Math.round(this.T * this.s());
      }

      public void z(boolean var1) {
         c7.y.put(this.r, new c7.l(this.k(), var1, this.w()));
      }

      public void f(double var1) {
         c7.y.put(this.r, new c7.l(var1, this.k(), this.w()));
      }

      public String w() {
         c7.l var1 = c7.y.get(this.r);
         return var1 == null ? null : var1.dock();
      }

      public void t(String var1) {
         c7.y.put(this.r, new c7.l(this.k(), this.k(), var1));
      }

      public void N(GuiGraphicsExtractor var1, int var2, int var3) {
         var1.pose().pushMatrix();
         var1.pose().translate(var2, var3).scale((float)this.s()).translate(-var2, -var3);
      }

      public void w(GuiGraphicsExtractor var1) {
         var1.pose().popMatrix();
      }

      public int T(int var1) {
         return this.r.equals("crosshair") && !c7.v.containsKey(this.r)
            ? Math.max(0, var1 / 2 - this.A() / 2)
            : (int)Math.round(c7.v.getOrDefault(this.r, this.y).x() * Math.max(0, var1 - this.A()));
      }

      public int P(int var1) {
         return this.r.equals("crosshair") && !c7.v.containsKey(this.r)
            ? Math.max(0, var1 / 2 - this.m() / 2)
            : (int)Math.round(c7.v.getOrDefault(this.r, this.y).y() * Math.max(0, var1 - this.m()));
      }

      public void E(int var1, int var2, int var3, int var4) {
         c7.v.put(this.r, new c7.q((double)var1 / Math.max(1, var3 - this.A()), (double)var2 / Math.max(1, var4 - this.m())));
      }
   }

   public enum q {
      Z(14),
      j(14),
      z(5);

      public final int Y;

      q(int var3) {
         this.Y = var3;
      }
   }
}

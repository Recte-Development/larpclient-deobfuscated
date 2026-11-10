package com.larpclient;

import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.FakeFPSModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.IslandLyricsModule;
import com.larpclient.module.Module;
import com.larpclient.module.StaffListModule;
import com.larpclient.module.TargetHUDModule;
import com.larpclient.module.kj;
import com.larpclient.module.kl;
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

public final class dk {
   private static final Map<dk.q, List<qy.i>> D = new LinkedHashMap<>();
   private static Object I;
   private static Object W;
   private static int O;
   private static int F;
   private static long g;
   private static List<dk.i> A = List.of();
   private static long e;
   private static boolean x;
   public static final int p = 8;
   public static final int X = 220;
   private static final fq y = new fq(0.0);
   private static long i = Long.MIN_VALUE;
   private static dk.q S;
   private static boolean m;
   private static final int N = 224;
   private static final int L = 52;
   private static final int v = 36;
   private static final int s = 12;
   private static final int o = 79;
   private static final List<dk.n> C = List.of(
      new dk.n("miner", "Auto Area Miner", _6::N, new ac.g("Layer 2 of 3", "64%", "Mining toward 120 64 -45", "4m 12s", 0.64)),
      new dk.n("builder", "Auto Builder", _d::Z, new ac.g("house", "64%", "Walking to 120 64 -45", "412 placed, 230 missing", 0.64)),
      new dk.n(
         "pilot", "Auto Elytra Pilot", _7::E, new ac.g("12,340 blocks left, about 3m 10s", "64%", "Flying to 1000, -2500", "32 rockets, elytra 412/432", 0.64)
      ),
      new dk.n("hunter", "Anchor Hunter", _z::f, new ac.g("4 anchors, 24/h", "4 of 10", "Going to an anchor", "/rtp in 42s", 0.4))
   );
   public static final List<dk.q> c = b(
      List.of(
         new dk.q("watermark", 170, 22, 0.015, 0.02),
         new dk.q("info", 178, 27, 0.015, 0.85),
         new dk.q("modules", 130, 100, 0.98, 0.06),
         new dk.q("target", 224, 52, 0.55, 0.62),
         new dk.q("effects", 150, 58, 0.015, 0.7),
         new dk.q("keybinds", 120, 58, 0.015, 0.35),
         new dk.q("armor", 84, 22, 0.5, 0.9),
         new dk.q("notifications", 260, 178, 0.98, 0.08),
         new dk.q("crosshair", 84, 84, 0.5, 0.5),
         new dk.q("speed", 172, 72, 0.015, 0.55),
         new dk.q("cooldowns", 168, 48, 0.78, 0.08),
         new dk.q("staffs", 168, 48, 0.35, 0.65),
         new dk.q("inventory", 207, 94, 0.04, 0.48),
         new dk.q("keystrokes", 81, 98, 0.985, 0.93),
         new dk.q("island", 300, 20, 0.5, 0.015),
         new dk.q("casino", 168, 140, 0.98, 0.5)
      )
   );
   private static final fq r = new fq(1.0);
   private static final fq T = new fq(0.0);
   private static final fq H = new fq(1.0);
   private static double E = 1.0;
   private static long z;
   private static final Identifier a = Identifier.withDefaultNamespace("hud/heart/container");
   private static final Identifier M = Identifier.withDefaultNamespace("hud/heart/full");
   private static LivingEntity V;
   private static final hq n = new hq();
   private static float l;
   private static long d;
   private static final bh.b q = new bh.b(1.0, false);
   private static final EquipmentSlot[] h = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private static final Set<String> Y = Set.of("target", "staffs", "casino", "miner", "builder", "pilot", "hunter");
   private static final List<dk.q> t = c.stream().filter(var0 -> !Y.contains(var0.d)).toList();
   private static final q9 u = new q9();
   private static final n7 G = new n7();
   private static List<ItemStack> w;

   public static boolean w() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null
         && var0.level != null
         && var0.isWindowActive()
         && var0.gui.screen() != null
         && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void C() {
      A = List.of();
      D.clear();
      I = null;
      W = null;
      i5.O();
      wx.j();
   }

   public static void n(GuiGraphicsExtractor var0) {
      if (I != var0) {
         D.clear();
         I = var0;
         Minecraft var1 = Minecraft.getInstance();
         W = var1.level;
         O = var0.guiWidth();
         F = var0.guiHeight();
         g = bh.k;
      }
   }

   public static void X(dk.q var0, qy.i var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         D.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static String M(String var0) {
      return "info".equals(var0) ? "watermark" : null;
   }

   public static boolean R(dk.q var0) {
      String var1 = M(var0.d);
      return var1 != null && var1.equals(var0.D());
   }

   public static void z(dk.q var0) {
      S = var0;
   }

   public static dk.q u() {
      return S;
   }

   public static boolean Z(dk.q var0, int var1, int var2) {
      String var3 = M(var0.d);
      if (var3 != null && m) {
         qy.i var4 = T(var3).W(var1, var2);
         qy.i var5 = var0.o(var1, var2);
         return q(var4, var5.x() + var5.width() / 2.0, var5.y() + var5.height() / 2.0);
      } else {
         return false;
      }
   }

   public static boolean q(qy.i var0, double var1, double var3) {
      return var1 >= var0.x() - 8 && var1 <= var0.x() + var0.width() + 8 && var3 >= var0.y() - 8 && var3 <= var0.y() + var0.height() + 8;
   }

   private static boolean j(dk.q var0, boolean var1, boolean var2, dk.q var3) {
      boolean var4 = k(var0);
      return var1 ? qy.V(var4, var2, var3 == var0) : var4;
   }

   public static List<dk.i> q() {
      Minecraft var0 = Minecraft.getInstance();
      if (W == var0.level
         && g == bh.k
         && var0.player != null
         && O == var0.getWindow().getGuiScaledWidth()
         && F == var0.getWindow().getGuiScaledHeight()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         ArrayList var1 = new ArrayList();
         D.forEach((var1x, var2) -> {
            if (k(var1x)) {
               for (qy.i var4 : var2) {
                  var1.add(new dk.i(var1x, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<qy.i> O(List<dk.i> var0, dk.q var1) {
      return var0.stream()
         .filter(var1x -> var1x.element() == var1)
         .map(dk.i::bounds)
         .reduce(
            (var0x, var1x) -> new qy.i(
               Math.min(var0x.x(), var1x.x()),
               Math.min(var0x.y(), var1x.y()),
               Math.max(var0x.x() + var0x.width(), var1x.x() + var1x.width()) - Math.min(var0x.x(), var1x.x()),
               Math.max(var0x.y() + var0x.height(), var1x.y() + var1x.height()) - Math.min(var0x.y(), var1x.y())
            )
         );
   }

   public static List<com.larpclient.module.r<?>> K(dk.q var0) {
      if (var0.d.equals("crosshair")) {
         kl var5 = LarpClient.N().j();
         Module var7 = var5.U("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      }

      HUDModule var1 = HUDModule.w();
      if (var0.d.equals("island")) {
         IslandLyricsModule var6 = IslandLyricsModule.r();
         return var6 == null ? List.of() : var6.getSettings();
      }

      if (!var0.d.equals("target") && !var0.d.equals("staffs")) {
         return var1 == null ? List.of() : var1.L(var0.d);
      }

      kl var2 = LarpClient.N().j();
      Module var3 = var0.d.equals("target") ? var2.y(TargetHUDModule.class) : var2.y(StaffListModule.class);
      ArrayList var4 = new ArrayList();
      if (var3 != null) {
         var4.addAll(var3.getSettings());
      }

      if (var1 != null) {
         var4.addAll(var1.L(var0.d));
      }

      return List.copyOf(var4);
   }

   public static boolean w(List<ItemStack> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.isEmpty());
   }

   public static String x(dk.q var0) {
      if (var0.X()) {
         return "Hidden";
      }

      if (!k(var0)) {
         return "Disabled";
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null) {
         return "Waiting for world";
      }

      int var2 = switch (var0.d) {
         case "notifications" -> ib.L().r();
         case "target" -> wp.b() == null && (V == null || !(T.K() > 0.01)) ? 0 : 1;
         case "effects" -> !var1.player.getActiveEffects().isEmpty() ? 1 : 0;
         case "keybinds" -> 1;
         case "modules" -> LarpClient.N().j().d().stream().anyMatch(var0x -> var0x.isOperational());
         case "armor" -> {
            HUDModule var5 = HUDModule.w();
            yield var5 != null
                  && Arrays.stream(h)
                     .filter(var1x -> var5.s.getValue() || var1x != EquipmentSlot.MAINHAND && var1x != EquipmentSlot.OFFHAND)
                     .anyMatch(var1x -> !var1.player.getItemBySlot(var1x).isEmpty())
               ? 1
               : 0;
         }
         case "cooldowns", "staffs" -> nv.f(var0.d, nv.E(var0.d, false));
         case "crosshair" -> var1.options.getCameraType().isFirstPerson();
         case "island" -> IslandLyricsModule.U();
         default -> 1;
      };
      return var2 != 0 ? "Visible" : "Waiting for content";
   }

   private static dk.n A(String var0) {
      return C.stream().filter(var1 -> var1.N.equals(var0)).findFirst().orElseThrow();
   }

   private static List<dk.q> b(List<dk.q> var0) {
      ArrayList var1 = new ArrayList(var0);

      for (dk.n var3 : C) {
         var1.add(new dk.q(var3.N, 196, 80, 0.5, 0.1));
      }

      return List.copyOf(var1);
   }

   public static List<dk.q> X() {
      return com.larpclient.module.fj.r() ? t : c;
   }

   public static dk.q T(String var0) {
      return c.stream().filter(var1 -> var1.d.equals(var0)).findFirst().orElseThrow();
   }

   public static String m(String var0) {
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

   public static boolean k(dk.q var0) {
      if (var0.X()) {
         return false;
      }

      HUDModule var1 = HUDModule.w();

      return (boolean)(switch (var0.d) {
         case "crosshair" -> CustomCrosshairModule.K();
         case "notifications" -> 1;
         case "target" -> {
            TargetHUDModule var4 = LarpClient.N().j().y(TargetHUDModule.class);
            yield var4 != null && var4.isOperational() ? 1 : 0;
         }
         case "watermark" -> var1 != null && var1.V() ? 1 : 0;
         case "info" -> var1 != null && var1.w() ? 1 : 0;
         case "modules" -> var1 != null && var1.D() ? 1 : 0;
         case "armor" -> var1 != null && var1.j() ? 1 : 0;
         case "keybinds" -> var1 != null && var1.n() ? 1 : 0;
         case "effects" -> var1 != null && var1.L() ? 1 : 0;
         case "speed" -> var1 != null && var1.h() ? 1 : 0;
         case "miner", "builder", "pilot", "hunter" -> A(var0.d).x() != null ? 1 : 0;
         case "staffs" -> StaffListModule.G();
         case "island" -> IslandLyricsModule.J();
         case "casino" -> kj.O() != null ? 1 : 0;
         case "cooldowns", "inventory", "keystrokes" -> var1 != null && var1.z(var0.d) ? 1 : 0;
         default -> var1 != null && var1.isOperational() ? 1 : 0;
      });
   }

   public static void w() {
      N(var0 -> true);
   }

   public static void N(Predicate<dk.q> var0) {
      for (dk.q var2 : c) {
         if (var0.test(var2)) {
            bh.Z.remove(var2.d);
            bh.b var3 = bh.F.get(var2.d);
            if (var3 != null) {
               bh.F.put(var2.d, new bh.b(var2.D(), var3.hidden()));
            }
         }
      }
   }

   public static void I(dk.q var0, boolean var1) {
      V(var0, var1, var0x -> LarpClient.N().j().U(var0x));
   }

   public static void V(dk.q var0, boolean var1, Function<String, Module> var2) {
      var0.a(!var1);
      if (var1 && !var0.d.equals("notifications")) {
         Module var3 = (Module)var2.apply(
            var0.d.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.d.equals("target") ? "TargetHUD" : (var0.d.equals("staffs") ? "Staff List" : (var0.d.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var4 = switch (var0.d) {
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

            for (com.larpclient.module.r var9 : var3.getSettings()) {
               if (var9.X().equals(var4) && var9 instanceof com.larpclient.module.m var7) {
                  var7.setValue(true);
               }
            }

            var3.setEnabled(true);
         }
      }
   }

   public void G() {
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "hudoverlay"),
         (var0, var1) -> {
            Minecraft var2 = Minecraft.getInstance();
            if (var2.player != null
               && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && !fj.X(var2.gui.screen())
               && !(var2.gui.screen() instanceof fv)) {
               GuiGraphicsExtractor var3 = qc.h(var0, false, false);
               if (var3 != null) {
                  n(var3);
                  M(var3, false);
               }
            }
         }
      );
      _q.z("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", c.size());
   }

   public static void B(Minecraft var0) {
      HUDModule var1 = HUDModule.w();
      if (var0.level == null || var0.player == null || var1 == null || !var1.isOperational()) {
         u.Q();
      } else if (!var0.isPaused() && var0.level.tickRateManager().runsNormally()) {
         u.U(var0.level, var0.player, var0.player.getX(), var0.player.getZ(), var0.level.tickRateManager().tickrate());
      } else {
         u.R();
      }
   }

   private static List<ItemStack> W() {
      if (w == null) {
         w = List.of(
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         );
      }

      return w;
   }

   public static void M(GuiGraphicsExtractor var0, boolean var1) {
      A(var0, var1, null, false);
   }

   public static void a(GuiGraphicsExtractor var0, dk.q var1, boolean var2) {
      A(var0, true, var1, var2);
   }

   public static List<dk.i> G() {
      return e == bh.k ? A : List.of();
   }

   public static void l() {
      A = List.of();
   }

   public static void a(GuiGraphicsExtractor var0, dk.q var1, boolean var2, boolean var3) {
      if (var3) {
         n(var0);
         M(var0, false);
         A = q();
         e = bh.k;
      } else {
         a(var0, var1, var2);
      }
   }

   private static void A(GuiGraphicsExtractor var0, boolean var1, dk.q var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            A = List.of();
         }

         long var6 = he.s();
         HUDModule var8 = HUDModule.w();
         int var9 = var4.getWindow().getGuiScaledWidth();
         int var10 = var4.getWindow().getGuiScaledHeight();
         int var11 = !var1 && w() ? 1 : 0;
         if (!var1 && var11 != x) {
            x = (boolean)var11;
            _q.c("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List var12 = G.K(ChooseASkyEffectAndAdjustItsAppearanceModule.Z(LarpClient.N().j().a(), LarpClient.N().h().k()), var4.font, ic.l(), nl.q, i5::k);
         int var13 = (var1 || var11 != 0) && var12.isEmpty() ? 1 : 0;
         if (var13 != 0) {
            var12 = List.of(new n7.p("Sprint", i5.k("Sprint")), new n7.p("Fullbright", i5.k("Fullbright")));
         }

         dk.q var14 = T("modules");
         double var15 = var14.Z();
         int var17 = Math.max(1, (int)((var10 - 24) / var15 / 17.0));
         int var18 = Math.max(1, (var12.size() + var17 - 1) / var17);
         int var19 = Math.max(20, var12.stream().mapToInt(var0x -> nc.U(var0x.width())).max().orElse(0));
         wx.z var20 = var8 != null && !var8.S() ? null : wx.b(var12, var1 || var13 != 0, wx.s(var10, var15));
         if (var20 != null) {
            var14.w = var20.width();
            var14.Z = var20.height();
         } else {
            var14.w = var18 * var19 + (var18 - 1) * 8;
            var14.Z = Math.max(20, Math.min(var17, var12.size()) * 17);
         }

         var14.U = Math.max(0.1, Math.min(4.0, Math.min((var9 - 16.0) / var14.w, (var10 - 16.0) / var14.Z)));
         ArrayList var21 = new ArrayList();
         if (var4.player != null && var8 != null) {
            for (EquipmentSlot var25 : h) {
               if (var25 != EquipmentSlot.MAINHAND && var25 != EquipmentSlot.OFFHAND || var8.s.getValue()) {
                  ItemStack var26 = var4.player.getItemBySlot(var25);
                  if (!var26.isEmpty() || var8.Y.getValue()) {
                     var21.add(var26);
                  }
               }
            }

            if (var8.D.getValue()) {
               Collections.reverse(var21);
            }
         }

         if ((var1 || var11 != 0) && var21.stream().allMatch(ItemStack::isEmpty)) {
            var21.clear();
            var21.addAll(W());
            if (var8 != null && var8.D.getValue()) {
               Collections.reverse(var21);
            }
         }

         int var69 = var8 != null && var8.l.w("Remaining") ? 46 : 26;
         int var70 = var8 != null && !var8.l.w("Off") ? 37 : 27;
         int var71 = var8 == null ? 5 : var8.B.getValue().intValue();
         int var72 = var8 != null && var8.N.w("Vertical") ? 1 : 0;
         if (var72 != 0 && var8 != null && !var8.l.w("Off")) {
            var69 += 38;
            var70 = 26;
         }

         int var73 = Math.max(1, var21.size());
         T("armor").w = 6 + (var72 != 0 ? var69 : var73 * (var69 + var71) - var71);
         T("armor").Z = 6 + (var72 != 0 ? var73 * (var70 + var71) - var71 : var70);
         String var27 = FakeFPSModule.E(var4.getFps()) + " fps";
         String var28 = var4.player == null ? "Player" : p3.J(var4.player.getUUID(), var4.player.getName().getString());
         var28 = i5.I(var28, 110.0, 11.0);
         String var29 = !var4.hasSingleplayerServer() && var4.getCurrentServer() != null ? var4.getCurrentServer().ip : "singleplayer";
         var29 = i5.I(var29, 130.0, 11.0);
         String var30 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         String var31 = var4.player == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.player.getX(), var4.player.getY(), var4.player.getZ());
         T("info").w = nc.q(var31);
         dk.q var32 = T("watermark");
         dk.q var33 = T("info");
         boolean var34 = j(var32, var1, var3, var2);
         int var35 = !var34 || !j(var33, var1, var3, var2) || !R(var33) && S != var33 ? 0 : 1;
         if (i != bh.k) {
            i = bh.k;
            y.p(var35 != 0 ? 1.0 : 0.0);
         }

         double var36 = Math.clamp(y.n(var35 != 0 ? 1.0 : 0.0, 220), 0.0, 1.0);
         m = var34;
         int var38 = var8 != null && var8.T.w("Right") ? 1 : 0;
         qy.i var39 = null;
         var32.w = nc.q(nc.w(var28, var29, var27, var30), var31, var36);
         var32.Z = nc.p(var36);
         int var40 = var11 == 0 || wp.b() != null || V != null && !(T.K() < 0.01) ? 0 : 1;
         HashMap var41 = new HashMap();

         for (String var43 : List.of("cooldowns", "staffs", "inventory")) {
            nv.b var44 = nv.E(var43, var1);
            var41.put(var43, var44);
            T(var43).w = var44.width();
            T(var43).Z = var44.height();
         }

         if (var8 != null) {
            T("keystrokes").w = _k.h(var8);
            T("keystrokes").Z = _k.b(var8);
         }

         i5.a var76 = i5.p(var1);
         i5.a var77 = i5.W(var1);
         i5.a var78 = i5.h(var1);
         T("casino").w = var78.width();
         T("casino").Z = var78.height();

         for (dk.n var46 : C) {
            var46.l(var1);
         }

         int var79 = var11 == 0 || var4.player != null && !var4.player.getActiveEffects().isEmpty() ? 0 : 1;
         if (var79 != 0) {
            var77 = i5.W(true);
         }

         T("keybinds").w = var76.width();
         T("keybinds").Z = var76.height();
         T("effects").w = var77.width();
         T("effects").Z = var77.height();

         for (dk.q var47 : X()) {
            var47.s(var9, var10);
            int var48 = var47.f(var9);
            int var49 = var47.p(var10);
            boolean var50 = k(var47);
            if ((var1 ? qy.V(var50, var3, var2 == var47) : var50)
               && (var1 || (!var47.d.equals("modules") || (var20 == null ? !var12.isEmpty() : !var20.empty())) && (!var47.d.equals("armor") || w(var21)))
               && (var1 || var11 != 0 || !var41.containsKey(var47.d) || nv.f(var47.d, (nv.b)var41.get(var47.d)))
               && (var1 || var79 != 0 || !var47.d.equals("effects") || var4.player != null && !var4.player.getActiveEffects().isEmpty())) {
               int var51 = !var1 && (var11 == 0 || IslandLyricsModule.U()) ? 0 : 1;
               if (var1 || !var47.d.equals("island") || var51 != 0 || IslandLyricsModule.U()) {
                  int var52 = !var47.d.equals("notifications") && !var47.d.equals("crosshair") ? 0 : 1;
                  if (var52 == 0 || var1) {
                     if (var47 != var33 || var35 == 0) {
                        var47.p(var0, var48, var49);

                        try {
                           switch (var47.d) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                                 nv.K(var0, var47.d, (nv.b)var41.get(var47.d), var48, var49, var1);
                                 break;
                              case "island":
                                 IslandLyricsModule var93 = IslandLyricsModule.r();
                                 if (var93 == null) {
                                    continue;
                                 }

                                 var93.B(var0, var48, var49, var47.w, (boolean)var51);
                                 break;
                              case "keystrokes":
                                 if (var8 == null) {
                                    continue;
                                 }

                                 if (var8.u() && !(var4.gui.screen() instanceof f8)) {
                                    G(var0, var48, var49, var47.w, var47.Z);
                                 }

                                 _k.j(var0, var8, var48, var49, var1);
                                 break;
                              case "notifications":
                                 ib.c(var0, var48, var49);
                                 break;
                              case "crosshair":
                                 CustomCrosshairModule.U(var0, var48 + var47.w / 2, var49 + var47.Z / 2);
                                 break;
                              case "watermark":
                                 if (!var1 && !var8.V()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.u() && !(var4.gui.screen() instanceof f8)) {
                                    G(var0, var48, var49, var47.w, var47.Z);
                                 }

                                 nc.k(var0, var48, var49, var47.w, var28, var29, var27, var30, var31, var36);
                                 if (var35 != 0) {
                                    qy.i var92 = nc.y(var48, var49, var47.w, (boolean)var38, var31);
                                    var39 = new qy.i(
                                       var48 + (int)Math.floor((var92.x() - var48) * var47.F()),
                                       var49 + (int)Math.floor((var92.y() - var49) * var47.F()),
                                       (int)Math.ceil(var92.width() * var47.F()),
                                       (int)Math.ceil(var92.height() * var47.F())
                                    );
                                 }
                                 break;
                              case "speed":
                                 nc.s(var0, var48, var49, u, var1);
                                 break;
                              case "miner":
                              case "builder":
                              case "pilot":
                              case "hunter":
                                 dk.n var91 = A(var47.d);
                                 if (var91.w == null) {
                                    continue;
                                 }

                                 if (var8 != null && var8.u() && !(var4.gui.screen() instanceof f8)) {
                                    G(var0, var48, var49, var47.w, var47.Z);
                                 }

                                 i5.E(
                                    var0,
                                    var48,
                                    var49,
                                    var47.w,
                                    var91.R,
                                    "scan",
                                    var91.w.headline(),
                                    var91.w.value(),
                                    var91.w.action(),
                                    var91.w.time(),
                                    var91.P,
                                    var91.w.alert()
                                 );
                                 break;
                              case "info":
                                 if (!var1 && !var8.w()) {
                                    continue;
                                 }

                                 if (var8 != null && var8.u() && !(var4.gui.screen() instanceof f8)) {
                                    G(var0, var48, var49, var47.w, var47.Z);
                                 }

                                 nc.Q(var0, var48, var49, var47.w, var31);
                                 break;
                              case "modules":
                                 if (!var1 && !var8.D()) {
                                    continue;
                                 }

                                 int var90 = var8 != null && var8.u() && !(var4.gui.screen() instanceof f8) ? 1 : 0;
                                 if (var20 != null) {
                                    if (var90 != 0) {
                                       G(var0, var48, var49, var47.w, var47.Z);
                                    }

                                    wx.B(var0, var20, var48, var49);
                                    if (!var1) {
                                       X(var47, var47.W(var9, var10));
                                    }
                                 } else {
                                    for (int var95 = 0; var95 < var12.size(); var95++) {
                                       String var98 = ((n7.p)var12.get(var95)).name();
                                       int var101 = var95 / var17;
                                       int var102 = var95 % var17;
                                       int var103 = var48 + var47.v() / 2 > var9 / 2 ? 1 : 0;
                                       int var104 = var103 != 0
                                          ? var48 + var47.w - var101 * (var19 + 8)
                                          : var48 + var101 * (var19 + 8) + nc.U(((n7.p)var12.get(var95)).width());
                                       int var62 = ((n7.p)var12.get(var95)).width();
                                       int var105 = nc.U(var62);
                                       int var106 = var104 - var105;
                                       int var107 = var49 + var102 * 17;
                                       if (var90 != 0) {
                                          G(var0, var106, var107, var105, 17);
                                       }

                                       nc.i(var0, var98, var62, var104, var107, (boolean)var103);
                                       if (!var1) {
                                          X(
                                             var47,
                                             new qy.i(
                                                var48 + (int)Math.floor((var106 - var48) * var47.F()),
                                                var49 + (int)Math.floor((var107 - var49) * var47.F()),
                                                (int)Math.ceil(var105 * var47.F()),
                                                (int)Math.ceil(17.0 * var47.F())
                                             )
                                          );
                                       }
                                    }
                                 }

                                 if (var1 && var12.isEmpty() && var20 == null) {
                                    ic.H(var0, "No active modules", var48 + 4, var49 + 5, com.larpclient.c.W);
                                 }
                                 break;
                              case "target":
                                 if (!var1 && var40 == 0) {
                                    z(var0, var48, var49);
                                 } else {
                                    y(var0, var48, var49);
                                 }
                                 break;
                              case "keybinds":
                              case "effects":
                              case "casino":
                                 i5.a var89 = var47.d.equals("keybinds") ? var76 : (var47.d.equals("casino") ? var78 : var77);
                                 int var94 = var48 + var47.v() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.u() && !(var4.gui.screen() instanceof f8)) {
                                    for (qy.i var99 : i5.h(var89, var48, var49, (boolean)var94)) {
                                       G(var0, var99.x(), var99.y(), var99.width(), var99.height());
                                    }
                                 }

                                 i5.j(var0, var89, var48, var49, (boolean)var94);
                                 if (!var1) {
                                    for (qy.i var100 : i5.h(var89, var48, var49, (boolean)var94)) {
                                       X(
                                          var47,
                                          new qy.i(
                                             var48 + (int)Math.floor((var100.x() - var48) * var47.F()),
                                             var49 + (int)Math.floor((var100.y() - var49) * var47.F()),
                                             (int)Math.ceil(var100.width() * var47.F()),
                                             (int)Math.ceil(var100.height() * var47.F())
                                          )
                                       );
                                    }
                                 }
                                 break;
                              case "armor":
                                 if (!var1 && !var8.j()) {
                                    continue;
                                 }

                                 if (var8.X.getValue() && w(var21)) {
                                    h5.q(var0, var48, var49, var47.w, var47.Z, 6.0F, nc.U());
                                    nc.X(var0, var48, var49, var47.w, var47.Z, 6.0F);
                                    nc.e(var0, var48, var49, var47.w, var47.Z, 6);
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
                                          int var63 = vv.Q(-43434, -8790368, var61);
                                          if (var8.C.getValue()) {
                                             h5.N(var0, var57 + 2, var58 + 23, var69 - 4, 2, 1879048192);
                                             h5.N(var0, var57 + 2, var58 + 23, (int)Math.round((var69 - 4) * var61), 2, var63);
                                          }

                                          if (!var8.l.w("Off")) {
                                             String var64 = var8.l.w("Percent") ? (int)Math.ceil(var61 * 100.0) + "%" : Integer.toString(var60);
                                             int var65 = var72 != 0 ? var57 + 28 : var57 + (var69 - i5.l(var64, 10.4)) / 2;
                                             i5.i(var0, var64, var65, var58 + (var72 != 0 ? 7 : 28), 10.4, var63);
                                          }
                                       }
                                    }
                                 }
                           }
                        } finally {
                           var47.I(var0);
                        }

                        if (!var1
                           && (
                              var47.d.equals("target")
                                 ? var40 != 0
                                 : !var47.d.equals("modules") && !var47.d.equals("keybinds") && !var47.d.equals("effects") && !var47.d.equals("casino")
                           )) {
                           X(var47, var47.W(var9, var10));
                        }

                        if (var1) {
                           var5.add(new dk.i(var47, var47.W(var9, var10)));
                        }

                        if (var1 && !var50) {
                           qy.i var88 = var47.W(var9, var10);
                           h5.N(var0, var88.x(), var88.y(), var88.width(), var88.height(), 1882207290);
                        }
                     } else if (var39 != null) {
                        if (var1) {
                           var5.add(new dk.i(var47, var39));
                        } else {
                           X(var47, var39);
                        }
                     }
                  }
               }
            }
         }

         if (var1) {
            A = List.copyOf(var5);
            e = bh.k;
         }

         if (!var1 && Boolean.getBoolean("larp.debug")) {
            int var81 = var10 - 15;

            for (Entry var84 : he.V().entrySet()) {
               String var86 = (String)var84.getKey() + " " + var84.getValue();
               ic.H(var0, var86, var9 - ic.E(var86) - 5, var81, com.larpclient.c.W);
               var81 -= 11;
            }

            for (Entry var85 : he.R().entrySet()) {
               String var87 = String.format(Locale.ROOT, "%s %.2f ms CPU", var85.getKey(), var85.getValue());
               ic.H(var0, var87, var9 - ic.E(var87) - 5, var81, com.larpclient.c.W);
               var81 -= 11;
            }
         }

         he.T("HUD", var6);
      }
   }

   private static void G(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.pose().transformPosition(var1, var2, new Vector2f());
      nl.N(Math.round(var5.x), Math.round(var5.y), Math.round(var3 * var0.pose().m00()), Math.round(var4 * var0.pose().m11()));
   }

   public static void X() {
      V = null;
      r.p(1.0);
      H.p(1.0);
      E = 1.0;
      z = 0L;
      T.p(0.0);
      n.o();
      d = 0L;
      l = 0.0F;
   }

   public static void y(GuiGraphicsExtractor var0, int var1, int var2) {
      h(var0, var1, var2, Minecraft.getInstance().player, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0);
   }

   public static void h(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3, String var4, float var5, float var6, float var7, double var8) {
      double var10 = Math.clamp(var5 / Math.max(1.0F, var6), 0.0F, 1.0F);
      H(var0, var1, var2, var3, var4, var5, var6, var7, var8, 1.0, var10, var10, true);
   }

   private static void z(GuiGraphicsExtractor var0, int var1, int var2) {
      long var3 = he.s();

      try {
         Minecraft var5 = Minecraft.getInstance();
         if (V != null && V.level() != var5.level) {
            X();
         }

         LivingEntity var6 = wp.b();
         if (var6 != null && var6 != V) {
            V = var6;
            _q.c(
               "TargetHUD",
               "Target {}: {} x {} blocks, portrait scale {} before HUD transform",
               var6.getType(),
               var6.getBbWidth(),
               var6.getBbHeight(),
               h(var6.getBbWidth(), var6.getBbHeight())
            );
            n.o();
            l = wu.W(var6);
            d = System.nanoTime();
            double var7 = wu.D(var6);
            r.p(var7);
            H.p(var7);
            E = var7;
            z = 0L;
         }

         long var22 = System.nanoTime();
         if (d != 0L) {
            n.N((var22 - d) / 1.0E9);
         }

         d = var22;
         float var9 = V == null ? 0.0F : wu.W(V);
         if (V != null) {
            n.h(l - var9, ThreadLocalRandom.current());
            l = var9;
         }

         double var10 = T.n(var6 == null ? 0.0 : 1.0, 200);
         if (V == null || var10 < 0.01) {
            if (var6 == null) {
               V = null;
               n.o();
            }

            return;
         }

         dk.q var12 = T("target");
         double var13 = 0.92 + 0.08 * var10;
         X(var12, M(var1, var2, var12.F(), var13, new qy.i(0, 0, 224, 52)));
         double var15 = Math.clamp(var9 / Math.max(1.0F, V.getMaxHealth()), 0.0F, 1.0F);
         double var17 = r.n(var15, 220);
         H(
            var0,
            var1,
            var2,
            V,
            p3.J(V.getUUID(), V.getName().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, V.getMaxHealth()),
            Math.max(0.0F, V.getAbsorptionAmount()),
            var5.player == null ? 0.0 : var5.player.distanceTo(V),
            var10,
            var17,
            c(var15, var17, var22),
            false
         );
      } finally {
         he.T("TargetHUD", var3);
      }
   }

   private static double c(double var0, double var2, long var4) {
      if (var0 < E) {
         H.p(Math.max(H.K(), var2));
         z = var4 + 400000000L;
      }

      E = var0;
      double var6 = var4 < z ? H.K() : H.r(var0, 450, fq.y.X);
      return Math.max(var6, var2);
   }

   public static qy.i M(int var0, int var1, double var2, double var4, qy.i var6) {
      double var7 = 112.0;
      double var9 = 26.0;
      double var11 = var0 + (var7 + (var6.x() - var7) * var4) * var2;
      double var13 = var1 + (var9 + (var6.y() - var9) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new qy.i(var15, var16, (int)Math.ceil(var11 + var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + var6.height() * var4 * var2) - var16);
   }

   public static float h(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void w(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3) {
      EntityRenderer var4 = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var3);
      EntityRenderState var5 = var4.createRenderState(var3, 1.0F);
      float var6 = h(var5.boundingBoxWidth, var5.boundingBoxHeight);
      if (var6 <= 0.0F) {
         _q.g("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         i5.t(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
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

   public static List<dk.w> w(boolean var0, boolean var1) {
      if (var1) {
         return var0 ? List.of(dk.w.B, dk.w.p, dk.w.Y) : List.of(dk.w.p, dk.w.Y, dk.w.B);
      } else {
         return var0 ? List.of(dk.w.Y, dk.w.B, dk.w.p) : List.of(dk.w.Y, dk.w.p, dk.w.B);
      }
   }

   private static String h(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static void H(
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
      TargetHUDModule var18 = LarpClient.N().j().y(TargetHUDModule.class);
      short var19 = 224;
      byte var20 = 52;
      double var21 = 0.92 + 0.08 * var10;
      var0.pose().pushMatrix();
      var0.pose().translate(var1 + var19 / 2.0F, var2 + var20 / 2.0F).scale((float)var21).translate(-var1 - var19 / 2.0F, -var2 - var20 / 2.0F);

      try {
         HUDModule var23 = HUDModule.w();
         if (var23 != null && var23.u() && !(var17.gui.screen() instanceof f8)) {
            G(var0, var1, var2, var19, var20);
         }

         h5.q(var0, var1, var2, var19, var20, 10.0F, vv.O(-468906478, var10));
         int var24 = var1 + 8;
         int var25 = var2 + 8;
         int var26 = var24 + 18;
         int var27 = var25 + 18;
         if (!var16 && !n.n().isEmpty()) {
            var0.enableScissor(var1, var2, var1 + var19, var2 + var20);

            try {
               for (hq.n var29 : n.n()) {
                  double var30 = Math.clamp(var29.d / var29.P, 0.0, 1.0);
                  double var32 = var10 * (1.0 - var30) * (var29.Y ? 1.0 : 0.72);
                  int var34 = Math.max(1, (int)Math.round(var29.M * (1.0 - var30 * 0.35)));
                  int var35 = var26 + (int)Math.round(var29.v);
                  int var36 = var27 + (int)Math.round(var29.I);
                  int var37 = var29.Y ? com.larpclient.c.y : -419430401;
                  h5.M(var0, var35, var36, (int)Math.ceil(var34 * 1.125), vv.O(var37, var32 * 0.22));
                  h5.M(var0, var35, var36, Math.max(1, var34 / 2), vv.O(var37, var32));
               }
            } finally {
               var0.disableScissor();
            }
         }

         h5.q(var0, var24 - 1, var25 - 1, 38.0F, 38.0F, 3.0F, vv.O(822083583, var10));
         if (var3 instanceof AbstractClientPlayer var55) {
            int var57 = Math.min(140, var55.hurtTime * 16);
            var0.pose().pushMatrix();
            float var59 = (float)Math.sin(var55.hurtTime / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.pose().translate(var26, var27).rotate(var59).translate(-var26, -var27);
            PlayerFaceExtractor.extractRenderState(var0, var55.getSkin(), var24, var25, 36, vv.O(-65536 | 255 - var57 << 8 | 255 - var57, var10));
            var0.pose().popMatrix();
         } else if (var3 != null) {
            var0.pose().pushMatrix();
            var0.pose().translate(var24, var25).scale(0.72F).translate(-var24, -var25);

            try {
               w(var0, var24, var25, var3);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            i5.t(var0, "user", var24 + 7, var25 + 7, 22, vv.O(-5592406, var10));
         }

         int var56 = var18 != null && !var18.Q.w("Above") ? 0 : 1;
         int var58 = var18 != null && var18.R.w("Above") ? 1 : 0;
         int var60 = var18 != null && var18.q.w("Left") ? 1 : 0;
         int var31 = var1 + 53;
         int var61 = var1 + var19 - 9;
         int var33 = vv.O(-1, var10);
         int var62 = vv.O(-6645086, var10);
         double var63 = var25;

         for (dk.w var38 : w((boolean)var56, (boolean)var58)) {
            int var39 = (int)Math.round(var63);
            switch (var38) {
               case Y:
                  String var65 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
                  int var41 = var61 - i5.l(var65, 10.0);
                  i5.i(var0, var65, var41, var39 + 1.5, 10.0, var62);
                  i5.i(var0, i5.I(var4, Math.max(0, var41 - 6 - var31), 13.0), var31, var39, 13.0, var33);
                  break;
               case B:
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, a, var31, var39 + 2, 9, 9, var33);
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, M, var31, var39 + 2, 9, 9, var33);
                  String var40 = h(var5);
                  i5.i(var0, var40, var31 + 12, var39 + 1, 12.0, var33);
                  if (var7 > 0.0F) {
                     i5.i(var0, "+" + h(var7), var31 + 16 + i5.l(var40, 12.0), var39 + 2.5, 10.0, vv.O(-14003, var10));
                  }

                  if (var3 instanceof AbstractClientPlayer) {
                     M(var0, var3, var61 - 79, var39, (boolean)var60, var10);
                  }
                  break;
               case p:
                  a(var0, var31, var39, var61 - var31, var12, var14, var6, var7, var10);
            }

            var63 += var38.c + 1.5;
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   private static void a(GuiGraphicsExtractor var0, int var1, int var2, int var3, double var4, double var6, float var8, float var9, double var10) {
      double var12 = var8 / (var8 + Math.max(0.0F, var9));
      h5.q(var0, var1, var2, var3, 5.0F, 2.0F, vv.O(620756991, var10));
      int var14 = (int)Math.round(var3 * Math.clamp(var4, 0.0, 1.0) * var12);
      int var15 = (int)Math.round(var3 * Math.clamp(var6, 0.0, 1.0) * var12);
      if (var15 > var14) {
         h5.q(var0, var1, var2, var15, 5.0F, 2.0F, vv.O(-1862270977, var10));
      }

      if (var14 > 0) {
         w1.c(var0, var1, var2, var14, 5.0F, 2.0F, vv.O(-4776932, var10), vv.O(-42418, var10));
      }

      if (var9 > 0.0F) {
         int var16 = Math.min(var3 - var14, Math.round(var3 * var9 / (var8 + var9)));
         if (var16 > 0) {
            h5.q(var0, var1 + var14, var2, var16, 5.0F, 2.0F, vv.O(-14003, var10));
         }
      }
   }

   private static void M(GuiGraphicsExtractor var0, LivingEntity var1, int var2, int var3, boolean var4, double var5) {
      int var7 = var2;

      for (int var8 = 0; var8 < 2; var8++) {
         int var9 = var4 == (var8 == 0 ? 1 : 0) ? 1 : 0;
         int var10 = var9 != 0 ? 4 : 0;
         int var11 = var9 != 0 ? 6 : 4;

         for (int var12 = var10; var12 < var11; var12++) {
            ItemStack var13 = var1.getItemBySlot(h[var12]);
            if (var13.isEmpty()) {
               h5.q(var0, var7 + 3, var3 + 3, 6.0F, 6.0F, 2.0F, vv.O(486539263, var5));
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
                  h5.q(var0, var7 + 1, var3 + 12, 10.0F, 1.5F, 0.75F, vv.O(-1610612736, var5));
                  h5.q(var0, var7 + 1, var3 + 12, (float)Math.max(1.0, 10.0 * var14), 1.5F, 0.75F, vv.O(vv.Q(-43434, -10755203, var14), var5));
               }
            }

            var7 += 13;
         }

         var7 += 2;
      }
   }

   public record i(dk.q element, qy.i bounds) {
   }

   private static final class n {
      final String N;
      final String R;
      final Supplier<? extends ac> t;
      final ac.g O;
      final fq A = new fq(0.0);
      boolean T;
      ac.g w;
      double P;

      n(String var1, String var2, Supplier<? extends ac> var3, ac.g var4) {
         this.N = var1;
         this.R = var2;
         this.t = var3;
         this.O = var4;
      }

      ac.g x() {
         ac var1 = this.t.get();
         return var1 == null ? null : var1.hudCard();
      }

      void l(boolean var1) {
         ac.g var2 = this.x();
         this.w = var2 != null ? var2 : (var1 ? this.O : null);
         this.P = this.w == null ? 0.0 : this.w.progress();
         if (var2 != null && !var1) {
            if (!this.T) {
               this.A.p(Math.max(0.0, this.P));
            }

            this.P = this.A.n(Math.max(0.0, this.P), 450);
         }

         if (!var1) {
            this.T = (boolean)(var2 != null ? 1 : 0);
         }

         if (this.w != null) {
            dk.T(this.N).w = i5.Y(this.R, this.w.headline(), this.w.value(), this.w.action(), this.w.time());
         }
      }
   }

   public static final class q {
      public final String d;
      public int w;
      public int Z;
      private double U = 4.0;
      private final bh.u Q;

      q(String var1, int var2, int var3, double var4, double var6) {
         this.d = var1;
         this.w = var2;
         this.Z = var3;
         this.Q = new bh.u(var4, var6);
      }

      public double D() {
         return switch (this.d) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "miner", "builder", "pilot", "hunter" -> 0.8;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double Z() {
         bh.b var1 = bh.F.get(this.d);
         return var1 == null ? this.D() : var1.scale();
      }

      public double F() {
         return Math.min(this.U, this.Z());
      }

      public void s(int var1, int var2) {
         this.U = Math.max(0.1, Math.min(4.0, Math.min((double)Math.max(1, var1) / this.w, (double)Math.max(1, var2) / this.Z)));
      }

      public qy.i W(int var1, int var2) {
         if (this.d.equals("crosshair")) {
            int var3 = CustomCrosshairModule.X();
            return new qy.i(
               this.f(var1) + (int)Math.round((this.w / 2.0 - var3) * this.F()),
               this.p(var2) + (int)Math.round((this.Z / 2.0 - var3) * this.F()),
               (int)Math.ceil(var3 * 2 * this.F()),
               (int)Math.ceil(var3 * 2 * this.F())
            );
         } else {
            return new qy.i(this.f(var1), this.p(var2), this.v(), this.d.equals("notifications") ? (int)Math.round(30.0 * this.F()) : this.u());
         }
      }

      public qy.i o(int var1, int var2) {
         return new qy.i(this.f(var1), this.p(var2), this.v(), this.u());
      }

      public void O() {
         bh.Z.remove(this.d);
         bh.F.put(this.d, new bh.b(this.D(), this.X()));
      }

      public void e() {
         bh.Z.remove(this.d);
         bh.F.remove(this.d);
      }

      public void m() {
         bh.Z.put(this.d, new bh.u(0.5, 0.5));
      }

      public boolean X() {
         return bh.F.getOrDefault(this.d, dk.q).hidden();
      }

      public int v() {
         return (int)Math.round(this.w * this.F());
      }

      public int u() {
         return (int)Math.round(this.Z * this.F());
      }

      public void a(boolean var1) {
         bh.F.put(this.d, new bh.b(this.Z(), var1, this.D()));
      }

      public void m(double var1) {
         bh.F.put(this.d, new bh.b(var1, this.X(), this.D()));
      }

      public String D() {
         bh.b var1 = bh.F.get(this.d);
         return var1 == null ? null : var1.dock();
      }

      public void I(String var1) {
         bh.F.put(this.d, new bh.b(this.Z(), this.X(), var1));
      }

      public void p(GuiGraphicsExtractor var1, int var2, int var3) {
         var1.pose().pushMatrix();
         var1.pose().translate(var2, var3).scale((float)this.F()).translate(-var2, -var3);
      }

      public void I(GuiGraphicsExtractor var1) {
         var1.pose().popMatrix();
      }

      public int f(int var1) {
         return this.d.equals("crosshair") && !bh.Z.containsKey(this.d)
            ? Math.max(0, var1 / 2 - this.v() / 2)
            : (int)Math.round(bh.Z.getOrDefault(this.d, this.Q).x() * Math.max(0, var1 - this.v()));
      }

      public int p(int var1) {
         return this.d.equals("crosshair") && !bh.Z.containsKey(this.d)
            ? Math.max(0, var1 / 2 - this.u() / 2)
            : (int)Math.round(bh.Z.getOrDefault(this.d, this.Q).y() * Math.max(0, var1 - this.u()));
      }

      public void o(int var1, int var2, int var3, int var4) {
         bh.Z.put(this.d, new bh.u((double)var1 / Math.max(1, var3 - this.v()), (double)var2 / Math.max(1, var4 - this.u())));
      }
   }

   public enum w {
      Y(14),
      B(14),
      p(5);

      public final int c;

      w(int var3) {
         this.c = var3;
      }
   }
}

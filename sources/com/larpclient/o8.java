package com.larpclient;

import com.larpclient.module.CustomCrosshairModule;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
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

public final class o8 {
   private static final Map<o8.a, List<_h.p>> D = new LinkedHashMap<>();
   private static Object K;
   private static Object e;
   private static int J;
   private static int H;
   private static long O;
   private static List<o8.r> p = List.of();
   private static long G;
   private static boolean x;
   private static final int E = 224;
   private static final int L = 52;
   private static final int Z = 36;
   private static final int l = 12;
   private static final int f = 79;
   public static final List<o8.a> d = List.of(
      new o8.a("watermark", 170, 22, 0.015, 0.02),
      new o8.a("info", 178, 27, 0.015, 0.85),
      new o8.a("modules", 130, 100, 0.98, 0.06),
      new o8.a("target", 224, 52, 0.55, 0.62),
      new o8.a("effects", 150, 58, 0.015, 0.7),
      new o8.a("keybinds", 120, 58, 0.015, 0.35),
      new o8.a("armor", 84, 22, 0.5, 0.9),
      new o8.a("notifications", 260, 178, 0.98, 0.08),
      new o8.a("crosshair", 84, 84, 0.5, 0.5),
      new o8.a("speed", 172, 72, 0.015, 0.55),
      new o8.a("cooldowns", 168, 48, 0.78, 0.08),
      new o8.a("staffs", 168, 48, 0.35, 0.65),
      new o8.a("inventory", 250, 120, 0.04, 0.48),
      new o8.a("radar", 84, 84, 0.34, 0.45),
      new o8.a("keystrokes", 81, 98, 0.985, 0.93),
      new o8.a("island", 300, 20, 0.5, 0.015),
      new o8.a("casino", 168, 140, 0.98, 0.5)
   );
   private static final u7 A = new u7(1.0);
   private static final u7 Q = new u7(0.0);
   private static final u7 N = new u7(1.0);
   private static double i = 1.0;
   private static long R;
   private static final Identifier u = Identifier.withDefaultNamespace("hud/heart/container");
   private static final Identifier m = Identifier.withDefaultNamespace("hud/heart/full");
   private static LivingEntity z;
   private static final ht j = new ht();
   private static float g;
   private static long t;
   private static final fc.n V = new fc.n(1.0, false);
   private static final EquipmentSlot[] X = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private static final fi T = new fi();
   private static final _u I = new _u();
   private static List<ItemStack> r;

   public static boolean u() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null
         && var0.level != null
         && var0.isWindowActive()
         && var0.gui.screen() != null
         && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void N() {
      p = List.of();
      D.clear();
      K = null;
      e = null;
      rd.g();
      yt.L();
   }

   public static void e(GuiGraphicsExtractor var0) {
      if (K != var0) {
         D.clear();
         K = var0;
         Minecraft var1 = Minecraft.getInstance();
         e = var1.level;
         J = var0.guiWidth();
         H = var0.guiHeight();
         O = fc.S;
      }
   }

   public static void D(o8.a var0, _h.p var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         D.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static List<o8.r> d() {
      Minecraft var0 = Minecraft.getInstance();
      if (e == var0.level
         && O == fc.S
         && var0.player != null
         && J == var0.getWindow().getGuiScaledWidth()
         && H == var0.getWindow().getGuiScaledHeight()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         ArrayList var1 = new ArrayList();
         D.forEach((var1x, var2) -> {
            if (r(var1x)) {
               for (_h.p var4 : var2) {
                  var1.add(new o8.r(var1x, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<_h.p> B(List<o8.r> var0, o8.a var1) {
      return var0.stream()
         .filter(var1x -> var1x.element() == var1)
         .map(o8.r::bounds)
         .reduce(
            (var0x, var1x) -> new _h.p(
               Math.min(var0x.x(), var1x.x()),
               Math.min(var0x.y(), var1x.y()),
               Math.max(var0x.x() + var0x.width(), var1x.x() + var1x.width()) - Math.min(var0x.x(), var1x.x()),
               Math.max(var0x.y() + var0x.height(), var1x.y() + var1x.height()) - Math.min(var0x.y(), var1x.y())
            )
         );
   }

   public static List<com.larpclient.module.l6<?>> z(o8.a var0) {
      if (var0.M.equals("crosshair")) {
         com.larpclient.module.ly var5 = LarpClient.y().e();
         Module var7 = var5.O("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      }

      HUDModule var1 = HUDModule.f();
      if (var0.M.equals("island")) {
         IslandLyricsModule var6 = IslandLyricsModule.g();
         return var6 == null ? List.of() : var6.getSettings();
      }

      if (!var0.M.equals("target") && !var0.M.equals("staffs")) {
         return var1 == null ? List.of() : var1.t(var0.M);
      }

      com.larpclient.module.ly var2 = LarpClient.y().e();
      Module var3 = var0.M.equals("target") ? var2.y(TargetHUDModule.class) : var2.y(StaffListModule.class);
      ArrayList var4 = new ArrayList();
      if (var3 != null) {
         var4.addAll(var3.getSettings());
      }

      if (var1 != null) {
         var4.addAll(var1.t(var0.M));
      }

      return List.copyOf(var4);
   }

   public static boolean X(List<ItemStack> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.isEmpty());
   }

   public static String Z(o8.a var0) {
      if (var0.z()) {
         return "Hidden";
      }

      if (!r(var0)) {
         return "Disabled";
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null) {
         return "Waiting for world";
      }

      int var2 = switch (var0.M) {
         case "notifications" -> hg.B().K();
         case "target" -> f8.m() == null && (z == null || !(Q.V() > 0.01)) ? 0 : 1;
         case "effects" -> !var1.player.getActiveEffects().isEmpty() ? 1 : 0;
         case "keybinds" -> 1;
         case "modules" -> LarpClient.y().e().X().stream().anyMatch(var0x -> var0x.isOperational());
         case "armor" -> {
            HUDModule var5 = HUDModule.f();
            yield var5 != null
                  && Arrays.stream(X)
                     .filter(var1x -> var5.r.getValue() || var1x != EquipmentSlot.MAINHAND && var1x != EquipmentSlot.OFFHAND)
                     .anyMatch(var1x -> !var1.player.getItemBySlot(var1x).isEmpty())
               ? 1
               : 0;
         }
         case "cooldowns", "staffs" -> p3.V(var0.M, p3.L(var0.M, false));
         case "crosshair" -> var1.options.getCameraType().isFirstPerson();
         case "island" -> IslandLyricsModule.e();
         default -> 1;
      };
      return var2 != 0 ? "Visible" : "Waiting for content";
   }

   public static o8.a q(String var0) {
      return d.stream().filter(var1 -> var1.M.equals(var0)).findFirst().orElseThrow();
   }

   public static String F(String var0) {
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
         case "radar" -> "Radar";
         case "keystrokes" -> "Keystrokes";
         case "island" -> "Island Lyrics";
         case "casino" -> "Casino games";
         default -> var0;
      };
   }

   public static boolean r(o8.a var0) {
      if (var0.z()) {
         return false;
      }

      HUDModule var1 = HUDModule.f();

      return (boolean)(switch (var0.M) {
         case "crosshair" -> CustomCrosshairModule.S();
         case "notifications" -> 1;
         case "target" -> {
            TargetHUDModule var4 = LarpClient.y().e().y(TargetHUDModule.class);
            yield var4 != null && var4.isOperational() ? 1 : 0;
         }
         case "watermark" -> var1 != null && var1.H() ? 1 : 0;
         case "info" -> var1 != null && var1.n() ? 1 : 0;
         case "modules" -> var1 != null && var1.E() ? 1 : 0;
         case "armor" -> var1 != null && var1.l() ? 1 : 0;
         case "keybinds" -> var1 != null && var1.M() ? 1 : 0;
         case "effects" -> var1 != null && var1.B() ? 1 : 0;
         case "speed" -> var1 != null && var1.w() ? 1 : 0;
         case "staffs" -> StaffListModule.p();
         case "island" -> IslandLyricsModule.n();
         case "casino" -> com.larpclient.module.a.x() != null ? 1 : 0;
         case "cooldowns", "inventory", "radar", "keystrokes" -> var1 != null && var1.D(var0.M) ? 1 : 0;
         default -> var1 != null && var1.isOperational() ? 1 : 0;
      });
   }

   public static void I() {
      K(var0 -> true);
   }

   public static void K(Predicate<o8.a> var0) {
      for (o8.a var2 : d) {
         if (var0.test(var2)) {
            fc.m.remove(var2.M);
            fc.n var3 = fc.Q.get(var2.M);
            if (var3 != null) {
               fc.Q.put(var2.M, new fc.n(var2.o(), var3.hidden()));
            }
         }
      }
   }

   public static void a(o8.a var0, boolean var1) {
      L(var0, var1, var0x -> LarpClient.y().e().O(var0x));
   }

   public static void L(o8.a var0, boolean var1, Function<String, Module> var2) {
      var0.W(!var1);
      if (var1 && !var0.M.equals("notifications")) {
         Module var3 = (Module)var2.apply(
            var0.M.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.M.equals("target") ? "TargetHUD" : (var0.M.equals("staffs") ? "Staff List" : (var0.M.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var4 = switch (var0.M) {
               case "watermark" -> "Watermark";
               case "info" -> "Info Panel";
               case "modules" -> "Module List";
               case "armor" -> "Armor";
               case "keybinds" -> "Keybinds";
               case "effects" -> "Potions";
               case "speed" -> "Speed";
               case "cooldowns" -> "Cooldowns";
               case "inventory" -> "Inventory";
               case "radar" -> "Radar";
               case "keystrokes" -> "Keystrokes";
               default -> "";
            };

            for (com.larpclient.module.l6 var9 : var3.getSettings()) {
               if (var9.k().equals(var4) && var9 instanceof com.larpclient.module.ln var7) {
                  var7.setValue(true);
               }
            }

            var3.setEnabled(true);
         }
      }
   }

   public void K() {
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "hudoverlay"),
         (var0, var1) -> {
            Minecraft var2 = Minecraft.getInstance();
            if (var2.player != null
               && !var2.gameRenderer.gameRenderState().guiRenderState.isHudHidden
               && !pc.s(var2.gui.screen())
               && !(var2.gui.screen() instanceof ps)) {
               GuiGraphicsExtractor var3 = y8.U(var0, false, false);
               if (var3 != null) {
                  e(var3);
                  V(var3, false);
               }
            }
         }
      );
      r4.V("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", d.size());
   }

   public static void C(Minecraft var0) {
      HUDModule var1 = HUDModule.f();
      if (var0.level == null || var0.player == null || var1 == null || !var1.isOperational()) {
         T.F();
      } else if (!var0.isPaused() && var0.level.tickRateManager().runsNormally()) {
         T.A(var0.level, var0.player, var0.player.getX(), var0.player.getZ(), var0.level.tickRateManager().tickrate());
      } else {
         T.P();
      }
   }

   private static List<ItemStack> t() {
      if (r == null) {
         r = List.of(
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         );
      }

      return r;
   }

   public static void V(GuiGraphicsExtractor var0, boolean var1) {
      e(var0, var1, null, false);
   }

   public static void V(GuiGraphicsExtractor var0, o8.a var1, boolean var2) {
      e(var0, true, var1, var2);
   }

   public static List<o8.r> z() {
      return G == fc.S ? p : List.of();
   }

   public static void d() {
      p = List.of();
   }

   public static void w(GuiGraphicsExtractor var0, o8.a var1, boolean var2, boolean var3) {
      if (var3) {
         e(var0);
         V(var0, false);
         p = d();
         G = fc.S;
      } else {
         V(var0, var1, var2);
      }
   }

   private static void e(GuiGraphicsExtractor var0, boolean var1, o8.a var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            p = List.of();
         }

         long var6 = h_.U();
         HUDModule var8 = HUDModule.f();
         int var9 = var4.getWindow().getGuiScaledWidth();
         int var10 = var4.getWindow().getGuiScaledHeight();
         int var11 = !var1 && u() ? 1 : 0;
         if (!var1 && var11 != x) {
            x = (boolean)var11;
            r4.g("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List var12 = I.n(ChooseASkyEffectAndAdjustItsAppearanceModule.O(LarpClient.y().e().X(), LarpClient.y().v().E()), var4.font, j_.g(), jk.v, rd::D);
         int var13 = (var1 || var11 != 0) && var12.isEmpty() ? 1 : 0;
         if (var13 != 0) {
            var12 = List.of(new _u.d("Sprint", rd.D("Sprint")), new _u.d("Fullbright", rd.D("Fullbright")));
         }

         o8.a var14 = q("modules");
         double var15 = var14.H();
         int var17 = Math.max(1, (int)((var10 - 24) / var15 / 17.0));
         int var18 = Math.max(1, (var12.size() + var17 - 1) / var17);
         int var19 = Math.max(20, var12.stream().mapToInt(var0x -> m0.H(var0x.width())).max().orElse(0));
         yt.d var20 = var8 != null && !var8.p() ? null : yt.D(var12, var1 || var13 != 0, yt.O(var10, var15));
         if (var20 != null) {
            var14.N = var20.width();
            var14.F = var20.height();
         } else {
            var14.N = var18 * var19 + (var18 - 1) * 8;
            var14.F = Math.max(20, Math.min(var17, var12.size()) * 17);
         }

         var14.u = Math.max(0.1, Math.min(2.5, Math.min((var9 - 16.0) / var14.N, (var10 - 16.0) / var14.F)));
         ArrayList var21 = new ArrayList();
         if (var4.player != null && var8 != null) {
            for (EquipmentSlot var25 : X) {
               if (var25 != EquipmentSlot.MAINHAND && var25 != EquipmentSlot.OFFHAND || var8.r.getValue()) {
                  ItemStack var26 = var4.player.getItemBySlot(var25);
                  if (!var26.isEmpty() || var8.n.getValue()) {
                     var21.add(var26);
                  }
               }
            }

            if (var8.L.getValue()) {
               Collections.reverse(var21);
            }
         }

         if ((var1 || var11 != 0) && var21.stream().allMatch(ItemStack::isEmpty)) {
            var21.clear();
            var21.addAll(t());
            if (var8 != null && var8.L.getValue()) {
               Collections.reverse(var21);
            }
         }

         int var61 = var8 != null && var8.z.i("Remaining") ? 46 : 26;
         int var62 = var8 != null && !var8.z.i("Off") ? 37 : 27;
         int var63 = var8 == null ? 5 : var8.B.getValue().intValue();
         int var64 = var8 != null && var8.c.i("Vertical") ? 1 : 0;
         if (var64 != 0 && var8 != null && !var8.z.i("Off")) {
            var61 += 38;
            var62 = 26;
         }

         int var65 = Math.max(1, var21.size());
         q("armor").N = 6 + (var64 != 0 ? var61 : var65 * (var61 + var63) - var63);
         q("armor").F = 6 + (var64 != 0 ? var65 * (var62 + var63) - var63 : var62);
         String var27 = var4.getFps() + " fps";
         String var28 = var4.player == null ? "Player" : x2.b(var4.player.getUUID(), var4.player.getName().getString());
         var28 = rd.R(var28, 110.0, 11.0);
         String var29 = !var4.hasSingleplayerServer() && var4.getCurrentServer() != null ? var4.getCurrentServer().ip : "singleplayer";
         var29 = rd.R(var29, 130.0, 11.0);
         String var30 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         q("watermark").N = m0.R(var28, var29, var27, var30);
         q("watermark").F = 42;
         String var31 = var4.player == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.player.getX(), var4.player.getY(), var4.player.getZ());
         q("info").N = m0.j(var31);
         int var32 = var11 == 0 || f8.m() != null || z != null && !(Q.V() < 0.01) ? 0 : 1;
         HashMap var33 = new HashMap();

         for (String var35 : List.of("cooldowns", "staffs", "inventory", "radar")) {
            p3.i var36 = p3.L(var35, var1);
            var33.put(var35, var36);
            q(var35).N = var36.width();
            q(var35).F = var36.height();
         }

         if (var8 != null) {
            q("keystrokes").N = xc.E(var8);
            q("keystrokes").F = xc.y(var8);
         }

         rd.w var68 = rd.w(var1);
         rd.w var69 = rd.h(var1);
         rd.w var70 = rd.K(var1);
         q("casino").N = var70.width();
         q("casino").F = var70.height();
         int var37 = var11 == 0 || var4.player != null && !var4.player.getActiveEffects().isEmpty() ? 0 : 1;
         if (var37 != 0) {
            var69 = rd.h(true);
         }

         q("keybinds").N = var68.width();
         q("keybinds").F = var68.height();
         q("effects").N = var69.width();
         q("effects").F = var69.height();
         Iterator var38 = d.iterator();

         while (true) {
            o8.a var39;
            boolean var42;
            label1024:
            while (true) {
               if (!var38.hasNext()) {
                  if (var1) {
                     p = List.copyOf(var5);
                     G = fc.S;
                  }

                  if (!var1 && Boolean.getBoolean("larp.debug")) {
                     int var71 = var10 - 15;

                     for (Entry var74 : h_.D().entrySet()) {
                        String var76 = (String)var74.getKey() + " " + var74.getValue();
                        j_.c(var0, var76, var9 - j_.L(var76) - 5, var71, rv.G0);
                        var71 -= 11;
                     }

                     for (Entry var75 : h_.b().entrySet()) {
                        String var77 = String.format(Locale.ROOT, "%s %.2f ms CPU", var75.getKey(), var75.getValue());
                        j_.c(var0, var77, var9 - j_.L(var77) - 5, var71, rv.G0);
                        var71 -= 11;
                     }
                  }

                  h_.n("HUD", var6);
                  return;
               }

               var39 = (o8.a)var38.next();
               var39.q(var9, var10);
               int var40 = var39.b(var9);
               int var41 = var39.M(var10);
               var42 = r(var39);
               if ((var1 ? _h.q(var42, var3, var2 == var39) : var42)
                  && (var1 || (!var39.M.equals("modules") || (var20 == null ? !var12.isEmpty() : !var20.empty())) && (!var39.M.equals("armor") || X(var21)))
                  && (var1 || var11 != 0 || !var33.containsKey(var39.M) || p3.V(var39.M, (p3.i)var33.get(var39.M)))
                  && (var1 || var37 != 0 || !var39.M.equals("effects") || var4.player != null && !var4.player.getActiveEffects().isEmpty())) {
                  int var43 = !var1 && (var11 == 0 || IslandLyricsModule.e()) ? 0 : 1;
                  if (var1 || !var39.M.equals("island") || var43 != 0 || IslandLyricsModule.e()) {
                     int var44 = !var39.M.equals("notifications") && !var39.M.equals("crosshair") ? 0 : 1;
                     if (var44 == 0 || var1) {
                        var39.J(var0, var40, var41);

                        try {
                           switch (var39.M) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                              case "radar":
                                 p3.i(var0, var39.M, (p3.i)var33.get(var39.M), var40, var41, var1);
                                 break label1024;
                              case "island":
                                 IslandLyricsModule var81 = IslandLyricsModule.g();
                                 if (var81 == null) {
                                    break;
                                 }

                                 var81.w(var0, var40, var41, var39.N, (boolean)var43);
                                 break label1024;
                              case "keystrokes":
                                 if (var8 == null) {
                                    break;
                                 }

                                 if (var8.L() && !(var4.gui.screen() instanceof p0)) {
                                    q(var0, var40, var41, var39.N, var39.F);
                                 }

                                 xc.n(var0, var8, var40, var41, var1);
                                 break label1024;
                              case "notifications":
                                 hg.x(var0, var40, var41);
                                 break label1024;
                              case "crosshair":
                                 CustomCrosshairModule.N(var0, var40 + var39.N / 2, var41 + var39.F / 2);
                                 break label1024;
                              case "watermark":
                                 if (!var1 && !var8.H()) {
                                    break;
                                 }

                                 if (var8 != null && var8.L() && !(var4.gui.screen() instanceof p0)) {
                                    q(var0, var40, var41, var39.N, var39.F);
                                 }

                                 m0.j(var0, var40, var41, var39.N, var28, var29, var27, var30);
                                 break label1024;
                              case "speed":
                                 m0.E(var0, var40, var41, T, var1);
                                 break label1024;
                              case "info":
                                 if (!var1 && !var8.n()) {
                                    break;
                                 }

                                 if (var8 != null && var8.L() && !(var4.gui.screen() instanceof p0)) {
                                    q(var0, var40, var41, var39.N, var39.F);
                                 }

                                 m0.d(var0, var40, var41, var39.N, var31);
                                 break label1024;
                              case "modules":
                                 if (!var1 && !var8.E()) {
                                    break;
                                 }

                                 int var80 = var8 != null && var8.L() && !(var4.gui.screen() instanceof p0) ? 1 : 0;
                                 if (var20 != null) {
                                    if (var80 != 0) {
                                       q(var0, var40, var41, var39.N, var39.F);
                                    }

                                    yt.B(var0, var20, var40, var41);
                                    if (!var1) {
                                       D(var39, var39.v(var9, var10));
                                    }
                                 } else {
                                    for (int var83 = 0; var83 < var12.size(); var83++) {
                                       String var86 = ((_u.d)var12.get(var83)).name();
                                       int var89 = var83 / var17;
                                       int var90 = var83 % var17;
                                       int var91 = var40 + var39.c() / 2 > var9 / 2 ? 1 : 0;
                                       int var92 = var91 != 0
                                          ? var40 + var39.N - var89 * (var19 + 8)
                                          : var40 + var89 * (var19 + 8) + m0.H(((_u.d)var12.get(var83)).width());
                                       int var54 = ((_u.d)var12.get(var83)).width();
                                       int var93 = m0.H(var54);
                                       int var94 = var92 - var93;
                                       int var95 = var41 + var90 * 17;
                                       if (var80 != 0) {
                                          q(var0, var94, var95, var93, 17);
                                       }

                                       m0.f(var0, var86, var54, var92, var95, (boolean)var91);
                                       if (!var1) {
                                          D(
                                             var39,
                                             new _h.p(
                                                var40 + (int)Math.floor((var94 - var40) * var39.p()),
                                                var41 + (int)Math.floor((var95 - var41) * var39.p()),
                                                (int)Math.ceil(var93 * var39.p()),
                                                (int)Math.ceil(17.0 * var39.p())
                                             )
                                          );
                                       }
                                    }
                                 }

                                 if (var1 && var12.isEmpty() && var20 == null) {
                                    j_.c(var0, "No active modules", var40 + 4, var41 + 5, rv.G0);
                                 }
                                 break label1024;
                              case "target":
                                 if (!var1 && var32 == 0) {
                                    S(var0, var40, var41);
                                    break label1024;
                                 }

                                 J(var0, var40, var41);
                                 break label1024;
                              case "keybinds":
                              case "effects":
                              case "casino":
                                 rd.w var79 = var39.M.equals("keybinds") ? var68 : (var39.M.equals("casino") ? var70 : var69);
                                 int var82 = var40 + var39.c() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.L() && !(var4.gui.screen() instanceof p0)) {
                                    for (_h.p var87 : rd.R(var79, var40, var41, (boolean)var82)) {
                                       q(var0, var87.x(), var87.y(), var87.width(), var87.height());
                                    }
                                 }

                                 rd.N(var0, var79, var40, var41, (boolean)var82);
                                 if (var1) {
                                    break label1024;
                                 }

                                 Iterator var85 = rd.R(var79, var40, var41, (boolean)var82).iterator();

                                 while (true) {
                                    if (!var85.hasNext()) {
                                       break label1024;
                                    }

                                    _h.p var88 = (_h.p)var85.next();
                                    D(
                                       var39,
                                       new _h.p(
                                          var40 + (int)Math.floor((var88.x() - var40) * var39.p()),
                                          var41 + (int)Math.floor((var88.y() - var41) * var39.p()),
                                          (int)Math.ceil(var88.width() * var39.p()),
                                          (int)Math.ceil(var88.height() * var39.p())
                                       )
                                    );
                                 }
                              case "armor":
                                 if (!var1 && !var8.l()) {
                                    break;
                                 }

                                 if (var8.Y.getValue() && X(var21)) {
                                    fr.S(var0, var40, var41, var39.N, var39.F, 6.0F, m0.z());
                                    m0.V(var0, var40, var41, var39.N, var39.F, 6.0F);
                                    m0.g(var0, var40, var41, var39.N, var39.F, 6);
                                 }

                                 for (int var47 = 0; var47 < var21.size(); var47++) {
                                    ItemStack var48 = (ItemStack)var21.get(var47);
                                    int var49 = var40 + 3 + (var64 != 0 ? 0 : var47 * (var61 + var63));
                                    int var50 = var41 + 3 + (var64 != 0 ? var47 * (var62 + var63) : 0);
                                    if (!var48.isEmpty()) {
                                       int var51 = var64 != 0 ? var49 : var49 + (var61 - 21) / 2;
                                       var0.pose().pushMatrix();
                                       var0.pose().translate(var51, var50).scale(1.3125F);
                                       var0.item(var48, 0, 0);
                                       var0.pose().popMatrix();
                                       if (var48.isDamageableItem()) {
                                          int var52 = Math.max(0, var48.getMaxDamage() - var48.getDamageValue());
                                          double var53 = (double)var52 / Math.max(1, var48.getMaxDamage());
                                          int var55 = oh.j(-43434, -8790368, var53);
                                          if (var8.b.getValue()) {
                                             fr.k(var0, var49 + 2, var50 + 23, var61 - 4, 2, 1879048192);
                                             fr.k(var0, var49 + 2, var50 + 23, (int)Math.round((var61 - 4) * var53), 2, var55);
                                          }

                                          if (!var8.z.i("Off")) {
                                             String var56 = var8.z.i("Percent") ? (int)Math.ceil(var53 * 100.0) + "%" : Integer.toString(var52);
                                             int var57 = var64 != 0 ? var49 + 28 : var49 + (var61 - rd.H(var56, 10.4)) / 2;
                                             rd.v(var0, var56, var57, var50 + (var64 != 0 ? 7 : 28), 10.4, var55);
                                          }
                                       }
                                    }
                                 }
                              default:
                                 break label1024;
                           }
                        } finally {
                           var39.z(var0);
                        }
                     }
                  }
               }
            }

            if (!var1
               && (
                  var39.M.equals("target")
                     ? var32 != 0
                     : !var39.M.equals("modules") && !var39.M.equals("keybinds") && !var39.M.equals("effects") && !var39.M.equals("casino")
               )) {
               D(var39, var39.v(var9, var10));
            }

            if (var1) {
               var5.add(new o8.r(var39, var39.v(var9, var10)));
            }

            if (var1 && !var42) {
               _h.p var78 = var39.v(var9, var10);
               fr.k(var0, var78.x(), var78.y(), var78.width(), var78.height(), 1882207290);
            }
         }
      }
   }

   private static void q(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.pose().transformPosition(var1, var2, new Vector2f());
      jk.Z(Math.round(var5.x), Math.round(var5.y), Math.round(var3 * var0.pose().m00()), Math.round(var4 * var0.pose().m11()));
   }

   public static void A() {
      z = null;
      A.c(1.0);
      N.c(1.0);
      i = 1.0;
      R = 0L;
      Q.c(0.0);
      j.j();
      t = 0L;
      g = 0.0F;
   }

   public static void J(GuiGraphicsExtractor var0, int var1, int var2) {
      b(var0, var1, var2, Minecraft.getInstance().player, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0);
   }

   public static void b(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3, String var4, float var5, float var6, float var7, double var8) {
      double var10 = Math.clamp(var5 / Math.max(1.0F, var6), 0.0F, 1.0F);
      e(var0, var1, var2, var3, var4, var5, var6, var7, var8, 1.0, var10, var10, true);
   }

   private static void S(GuiGraphicsExtractor var0, int var1, int var2) {
      long var3 = h_.U();

      try {
         Minecraft var5 = Minecraft.getInstance();
         if (z != null && z.level() != var5.level) {
            A();
         }

         LivingEntity var6 = f8.m();
         if (var6 != null && var6 != z) {
            z = var6;
            r4.g(
               "TargetHUD",
               "Target {}: {} x {} blocks, portrait scale {} before HUD transform",
               var6.getType(),
               var6.getBbWidth(),
               var6.getBbHeight(),
               s(var6.getBbWidth(), var6.getBbHeight())
            );
            j.j();
            g = u_.B(var6);
            t = System.nanoTime();
            double var7 = u_.g(var6);
            A.c(var7);
            N.c(var7);
            i = var7;
            R = 0L;
         }

         long var22 = System.nanoTime();
         if (t != 0L) {
            j.F((var22 - t) / 1.0E9);
         }

         t = var22;
         float var9 = z == null ? 0.0F : u_.B(z);
         if (z != null) {
            j.q(g - var9, ThreadLocalRandom.current());
            g = var9;
         }

         double var10 = Q.y(var6 == null ? 0.0 : 1.0, 200);
         if (z == null || var10 < 0.01) {
            if (var6 == null) {
               z = null;
               j.j();
            }

            return;
         }

         o8.a var12 = q("target");
         double var13 = 0.92 + 0.08 * var10;
         D(var12, o(var1, var2, var12.p(), var13, new _h.p(0, 0, 224, 52)));
         double var15 = Math.clamp(var9 / Math.max(1.0F, z.getMaxHealth()), 0.0F, 1.0F);
         double var17 = A.y(var15, 220);
         e(
            var0,
            var1,
            var2,
            z,
            x2.b(z.getUUID(), z.getName().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, z.getMaxHealth()),
            Math.max(0.0F, z.getAbsorptionAmount()),
            var5.player == null ? 0.0 : var5.player.distanceTo(z),
            var10,
            var17,
            b(var15, var17, var22),
            false
         );
      } finally {
         h_.n("TargetHUD", var3);
      }
   }

   private static double b(double var0, double var2, long var4) {
      if (var0 < i) {
         N.c(Math.max(N.V(), var2));
         R = var4 + 400000000L;
      }

      i = var0;
      double var6 = var4 < R ? N.V() : N.c(var0, 450, u7.p.Z);
      return Math.max(var6, var2);
   }

   public static _h.p o(int var0, int var1, double var2, double var4, _h.p var6) {
      double var7 = 112.0;
      double var9 = 26.0;
      double var11 = var0 + (var7 + (var6.x() - var7) * var4) * var2;
      double var13 = var1 + (var9 + (var6.y() - var9) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new _h.p(var15, var16, (int)Math.ceil(var11 + var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + var6.height() * var4 * var2) - var16);
   }

   public static float s(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void C(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3) {
      EntityRenderer var4 = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var3);
      EntityRenderState var5 = var4.createRenderState(var3, 1.0F);
      float var6 = s(var5.boundingBoxWidth, var5.boundingBoxHeight);
      if (var6 <= 0.0F) {
         r4.T("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         rd.Z(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
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

   public static List<o8.g> Q(boolean var0, boolean var1) {
      if (var1) {
         return var0 ? List.of(o8.g.u, o8.g.G, o8.g.l) : List.of(o8.g.G, o8.g.l, o8.g.u);
      } else {
         return var0 ? List.of(o8.g.l, o8.g.u, o8.g.G) : List.of(o8.g.l, o8.g.G, o8.g.u);
      }
   }

   private static String M(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static void e(
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
      TargetHUDModule var18 = LarpClient.y().e().y(TargetHUDModule.class);
      short var19 = 224;
      byte var20 = 52;
      double var21 = 0.92 + 0.08 * var10;
      var0.pose().pushMatrix();
      var0.pose().translate(var1 + var19 / 2.0F, var2 + var20 / 2.0F).scale((float)var21).translate(-var1 - var19 / 2.0F, -var2 - var20 / 2.0F);

      try {
         HUDModule var23 = HUDModule.f();
         if (var23 != null && var23.L() && !(var17.gui.screen() instanceof p0)) {
            q(var0, var1, var2, var19, var20);
         }

         fr.S(var0, var1, var2, var19, var20, 10.0F, oh.S(-468906478, var10));
         int var24 = var1 + 8;
         int var25 = var2 + 8;
         int var26 = var24 + 18;
         int var27 = var25 + 18;
         if (!var16 && !j.j().isEmpty()) {
            var0.enableScissor(var1, var2, var1 + var19, var2 + var20);

            try {
               for (ht.t var29 : j.j()) {
                  double var30 = Math.clamp(var29.g / var29.L, 0.0, 1.0);
                  double var32 = var10 * (1.0 - var30) * (var29.V ? 1.0 : 0.72);
                  int var34 = Math.max(1, (int)Math.round(var29.n * (1.0 - var30 * 0.35)));
                  int var35 = var26 + (int)Math.round(var29.u);
                  int var36 = var27 + (int)Math.round(var29.e);
                  int var37 = var29.V ? rv.e : -419430401;
                  fr.J(var0, var35, var36, (int)Math.ceil(var34 * 1.125), oh.S(var37, var32 * 0.22));
                  fr.J(var0, var35, var36, Math.max(1, var34 / 2), oh.S(var37, var32));
               }
            } finally {
               var0.disableScissor();
            }
         }

         fr.S(var0, var24 - 1, var25 - 1, 38.0F, 38.0F, 3.0F, oh.S(822083583, var10));
         if (var3 instanceof AbstractClientPlayer var55) {
            int var57 = Math.min(140, var55.hurtTime * 16);
            var0.pose().pushMatrix();
            float var59 = (float)Math.sin(var55.hurtTime / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.pose().translate(var26, var27).rotate(var59).translate(-var26, -var27);
            PlayerFaceExtractor.extractRenderState(var0, var55.getSkin(), var24, var25, 36, oh.S(-65536 | 255 - var57 << 8 | 255 - var57, var10));
            var0.pose().popMatrix();
         } else if (var3 != null) {
            var0.pose().pushMatrix();
            var0.pose().translate(var24, var25).scale(0.72F).translate(-var24, -var25);

            try {
               C(var0, var24, var25, var3);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            rd.Z(var0, "user", var24 + 7, var25 + 7, 22, oh.S(-5592406, var10));
         }

         int var56 = var18 != null && !var18.L.i("Above") ? 0 : 1;
         int var58 = var18 != null && var18.P.i("Above") ? 1 : 0;
         int var60 = var18 != null && var18.I.i("Left") ? 1 : 0;
         int var31 = var1 + 53;
         int var61 = var1 + var19 - 9;
         int var33 = oh.S(-1, var10);
         int var62 = oh.S(-6645086, var10);
         double var63 = var25;

         for (o8.g var38 : Q((boolean)var56, (boolean)var58)) {
            int var39 = (int)Math.round(var63);
            switch (var38) {
               case l:
                  String var65 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
                  int var41 = var61 - rd.H(var65, 10.0);
                  rd.v(var0, var65, var41, var39 + 1.5, 10.0, var62);
                  rd.v(var0, rd.R(var4, Math.max(0, var41 - 6 - var31), 13.0), var31, var39, 13.0, var33);
                  break;
               case u:
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, u, var31, var39 + 2, 9, 9, var33);
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, m, var31, var39 + 2, 9, 9, var33);
                  String var40 = M(var5);
                  rd.v(var0, var40, var31 + 12, var39 + 1, 12.0, var33);
                  if (var7 > 0.0F) {
                     rd.v(var0, "+" + M(var7), var31 + 16 + rd.H(var40, 12.0), var39 + 2.5, 10.0, oh.S(-14003, var10));
                  }

                  if (var3 instanceof AbstractClientPlayer) {
                     O(var0, var3, var61 - 79, var39, (boolean)var60, var10);
                  }
                  break;
               case G:
                  W(var0, var31, var39, var61 - var31, var12, var14, var6, var7, var10);
            }

            var63 += var38.m + 1.5;
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   private static void W(GuiGraphicsExtractor var0, int var1, int var2, int var3, double var4, double var6, float var8, float var9, double var10) {
      double var12 = var8 / (var8 + Math.max(0.0F, var9));
      fr.S(var0, var1, var2, var3, 5.0F, 2.0F, oh.S(620756991, var10));
      int var14 = (int)Math.round(var3 * Math.clamp(var4, 0.0, 1.0) * var12);
      int var15 = (int)Math.round(var3 * Math.clamp(var6, 0.0, 1.0) * var12);
      if (var15 > var14) {
         fr.S(var0, var1, var2, var15, 5.0F, 2.0F, oh.S(-1862270977, var10));
      }

      if (var14 > 0) {
         ow.Z(var0, var1, var2, var14, 5.0F, 2.0F, oh.S(-4776932, var10), oh.S(-42418, var10));
      }

      if (var9 > 0.0F) {
         int var16 = Math.min(var3 - var14, Math.round(var3 * var9 / (var8 + var9)));
         if (var16 > 0) {
            fr.S(var0, var1 + var14, var2, var16, 5.0F, 2.0F, oh.S(-14003, var10));
         }
      }
   }

   private static void O(GuiGraphicsExtractor var0, LivingEntity var1, int var2, int var3, boolean var4, double var5) {
      int var7 = var2;

      for (int var8 = 0; var8 < 2; var8++) {
         int var9 = var4 == (var8 == 0 ? 1 : 0) ? 1 : 0;
         int var10 = var9 != 0 ? 4 : 0;
         int var11 = var9 != 0 ? 6 : 4;

         for (int var12 = var10; var12 < var11; var12++) {
            ItemStack var13 = var1.getItemBySlot(X[var12]);
            if (var13.isEmpty()) {
               fr.S(var0, var7 + 3, var3 + 3, 6.0F, 6.0F, 2.0F, oh.S(486539263, var5));
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
                  fr.S(var0, var7 + 1, var3 + 12, 10.0F, 1.5F, 0.75F, oh.S(-1610612736, var5));
                  fr.S(var0, var7 + 1, var3 + 12, (float)Math.max(1.0, 10.0 * var14), 1.5F, 0.75F, oh.S(oh.j(-43434, -10755203, var14), var5));
               }
            }

            var7 += 13;
         }

         var7 += 2;
      }
   }

   public static final class a {
      public final String M;
      public int N;
      public int F;
      private double u = 2.5;
      private final fc.g w;

      a(String var1, int var2, int var3, double var4, double var6) {
         this.M = var1;
         this.N = var2;
         this.F = var3;
         this.w = new fc.g(var4, var6);
      }

      public double o() {
         return switch (this.M) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double H() {
         fc.n var1 = fc.Q.get(this.M);
         return var1 == null ? this.o() : var1.scale();
      }

      public double p() {
         return Math.min(this.u, this.H());
      }

      public void q(int var1, int var2) {
         this.u = Math.max(0.1, Math.min(2.5, Math.min((double)Math.max(1, var1) / this.N, (double)Math.max(1, var2) / this.F)));
      }

      public _h.p v(int var1, int var2) {
         if (this.M.equals("crosshair")) {
            int var3 = CustomCrosshairModule.d();
            return new _h.p(
               this.b(var1) + (int)Math.round((this.N / 2.0 - var3) * this.p()),
               this.M(var2) + (int)Math.round((this.F / 2.0 - var3) * this.p()),
               (int)Math.ceil(var3 * 2 * this.p()),
               (int)Math.ceil(var3 * 2 * this.p())
            );
         } else {
            return new _h.p(this.b(var1), this.M(var2), this.c(), this.M.equals("notifications") ? (int)Math.round(30.0 * this.p()) : this.p());
         }
      }

      public _h.p O(int var1, int var2) {
         return new _h.p(this.b(var1), this.M(var2), this.c(), this.p());
      }

      public void j() {
         fc.m.remove(this.M);
         this.m(this.o());
      }

      public void O() {
         fc.m.remove(this.M);
         fc.Q.remove(this.M);
      }

      public void W() {
         fc.m.put(this.M, new fc.g(0.5, 0.5));
      }

      public boolean z() {
         return fc.Q.getOrDefault(this.M, o8.V).hidden();
      }

      public int c() {
         return (int)Math.round(this.N * this.p());
      }

      public int p() {
         return (int)Math.round(this.F * this.p());
      }

      public void W(boolean var1) {
         fc.Q.put(this.M, new fc.n(this.H(), var1));
      }

      public void m(double var1) {
         fc.Q.put(this.M, new fc.n(var1, this.z()));
      }

      public void J(GuiGraphicsExtractor var1, int var2, int var3) {
         var1.pose().pushMatrix();
         var1.pose().translate(var2, var3).scale((float)this.p()).translate(-var2, -var3);
      }

      public void z(GuiGraphicsExtractor var1) {
         var1.pose().popMatrix();
      }

      public int b(int var1) {
         return this.M.equals("crosshair") && !fc.m.containsKey(this.M)
            ? Math.max(0, var1 / 2 - this.c() / 2)
            : (int)Math.round(fc.m.getOrDefault(this.M, this.w).x() * Math.max(0, var1 - this.c()));
      }

      public int M(int var1) {
         return this.M.equals("crosshair") && !fc.m.containsKey(this.M)
            ? Math.max(0, var1 / 2 - this.p() / 2)
            : (int)Math.round(fc.m.getOrDefault(this.M, this.w).y() * Math.max(0, var1 - this.p()));
      }

      public void i(int var1, int var2, int var3, int var4) {
         fc.m.put(this.M, new fc.g((double)var1 / Math.max(1, var3 - this.c()), (double)var2 / Math.max(1, var4 - this.p())));
      }
   }

   public enum g {
      l(14),
      u(14),
      G(5);

      public final int m;

      g(int var3) {
         this.m = var3;
      }
   }

   public record r(o8.a element, _h.p bounds) {
   }
}

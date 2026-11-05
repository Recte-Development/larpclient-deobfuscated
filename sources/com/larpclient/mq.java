package com.larpclient;

import com.larpclient.module.CustomCrosshairModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.IslandLyricsModule;
import com.larpclient.module.Module;
import com.larpclient.module.StaffListModule;
import com.larpclient.module.TargetHUDModule;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dz;
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

public final class mq {
   private static final Map<mq.s, List<ez.h>> w = new LinkedHashMap<>();
   private static Object e;
   private static Object G;
   private static int q;
   private static int s;
   private static long T;
   private static List<mq.l> V = List.of();
   private static long R;
   private static boolean x;
   private static final int k = 224;
   private static final int b = 52;
   private static final int n = 36;
   private static final int B = 12;
   private static final int t = 79;
   public static final List<mq.s> Y = List.of(
      new mq.s("watermark", 170, 22, 0.015, 0.02),
      new mq.s("info", 178, 27, 0.015, 0.85),
      new mq.s("modules", 130, 100, 0.98, 0.06),
      new mq.s("target", 224, 52, 0.55, 0.62),
      new mq.s("effects", 150, 58, 0.015, 0.7),
      new mq.s("keybinds", 120, 58, 0.015, 0.35),
      new mq.s("armor", 84, 22, 0.5, 0.9),
      new mq.s("notifications", 260, 178, 0.98, 0.08),
      new mq.s("crosshair", 84, 84, 0.5, 0.5),
      new mq.s("speed", 172, 72, 0.015, 0.55),
      new mq.s("cooldowns", 168, 48, 0.78, 0.08),
      new mq.s("staffs", 168, 48, 0.35, 0.65),
      new mq.s("inventory", 250, 120, 0.04, 0.48),
      new mq.s("radar", 84, 84, 0.34, 0.45),
      new mq.s("keystrokes", 81, 98, 0.985, 0.93),
      new mq.s("island", 300, 20, 0.5, 0.015),
      new mq.s("casino", 168, 140, 0.98, 0.5)
   );
   private static final ef S = new ef(1.0);
   private static final ef F = new ef(0.0);
   private static final ef o = new ef(1.0);
   private static double v = 1.0;
   private static long E;
   private static final Identifier Q = Identifier.withDefaultNamespace("hud/heart/container");
   private static final Identifier y = Identifier.withDefaultNamespace("hud/heart/full");
   private static LivingEntity L;
   private static final mc I = new mc();
   private static float l;
   private static long h;
   private static final cx.z f = new cx.z(1.0, false);
   private static final EquipmentSlot[] A = new EquipmentSlot[]{
      EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND
   };
   private static final zk P = new zk();
   private static final wo O = new wo();
   private static List<ItemStack> a;

   public static boolean L() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null
         && var0.level != null
         && var0.isWindowActive()
         && var0.gui.screen() != null
         && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void N() {
      V = List.of();
      w.clear();
      e = null;
      G = null;
      we.E();
      tk.v();
   }

   public static void x(GuiGraphicsExtractor var0) {
      if (e != var0) {
         w.clear();
         e = var0;
         Minecraft var1 = Minecraft.getInstance();
         G = var1.level;
         q = var0.guiWidth();
         s = var0.guiHeight();
         T = cx.U;
      }
   }

   public static void H(mq.s var0, ez.h var1) {
      if (var1.width() > 0 && var1.height() > 0) {
         w.computeIfAbsent(var0, var0x -> new ArrayList<>()).add(var1);
      }
   }

   public static List<mq.l> q() {
      Minecraft var0 = Minecraft.getInstance();
      if (G == var0.level
         && T == cx.U
         && var0.player != null
         && q == var0.getWindow().getGuiScaledWidth()
         && s == var0.getWindow().getGuiScaledHeight()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         ArrayList var1 = new ArrayList();
         w.forEach((var1x, var2) -> {
            if (M(var1x)) {
               for (ez.h var4 : var2) {
                  var1.add(new mq.l(var1x, var4));
               }
            }
         });
         return List.copyOf(var1);
      } else {
         return List.of();
      }
   }

   public static Optional<ez.h> e(List<mq.l> var0, mq.s var1) {
      return var0.stream()
         .filter(var1x -> var1x.element() == var1)
         .map(mq.l::bounds)
         .reduce(
            (var0x, var1x) -> new ez.h(
               Math.min(var0x.x(), var1x.x()),
               Math.min(var0x.y(), var1x.y()),
               Math.max(var0x.x() + var0x.width(), var1x.x() + var1x.width()) - Math.min(var0x.x(), var1x.x()),
               Math.max(var0x.y() + var0x.height(), var1x.y() + var1x.height()) - Math.min(var0x.y(), var1x.y())
            )
         );
   }

   public static List<df<?>> E(mq.s var0) {
      if (var0.g.equals("crosshair")) {
         dz var5 = LarpClient.F().x();
         Module var7 = var5.k("CustomCrosshair");
         return var7 == null ? List.of() : var7.getSettings();
      }

      HUDModule var1 = HUDModule.U();
      if (var0.g.equals("island")) {
         IslandLyricsModule var6 = IslandLyricsModule.u();
         return var6 == null ? List.of() : var6.getSettings();
      }

      if (!var0.g.equals("target") && !var0.g.equals("staffs")) {
         return var1 == null ? List.of() : var1.Y(var0.g);
      }

      dz var2 = LarpClient.F().x();
      Module var3 = var0.g.equals("target") ? var2.V(TargetHUDModule.class) : var2.V(StaffListModule.class);
      ArrayList var4 = new ArrayList();
      if (var3 != null) {
         var4.addAll(var3.getSettings());
      }

      if (var1 != null) {
         var4.addAll(var1.Y(var0.g));
      }

      return List.copyOf(var4);
   }

   public static boolean Y(List<ItemStack> var0) {
      return var0.stream().anyMatch(var0x -> !var0x.isEmpty());
   }

   public static String m(mq.s var0) {
      if (var0.N()) {
         return "Hidden";
      }

      if (!M(var0)) {
         return "Disabled";
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null) {
         return "Waiting for world";
      }

      int var2 = switch (var0.g) {
         case "notifications" -> oj.K().F();
         case "target" -> com.larpclient.h.o() == null && (L == null || !(F.G() > 0.01)) ? 0 : 1;
         case "effects" -> !var1.player.getActiveEffects().isEmpty() ? 1 : 0;
         case "keybinds" -> 1;
         case "modules" -> LarpClient.F().x().C().stream().anyMatch(var0x -> var0x.isOperational());
         case "armor" -> {
            HUDModule var5 = HUDModule.U();
            yield var5 != null
                  && Arrays.stream(A)
                     .filter(var1x -> var5.k.P() || var1x != EquipmentSlot.MAINHAND && var1x != EquipmentSlot.OFFHAND)
                     .anyMatch(var1x -> !var1.player.getItemBySlot(var1x).isEmpty())
               ? 1
               : 0;
         }
         case "cooldowns", "staffs" -> e0.v(var0.g, e0.t(var0.g, false));
         case "crosshair" -> var1.options.getCameraType().isFirstPerson();
         case "island" -> IslandLyricsModule.p();
         default -> 1;
      };
      return var2 != 0 ? "Visible" : "Waiting for content";
   }

   public static mq.s B(String var0) {
      return Y.stream().filter(var1 -> var1.g.equals(var0)).findFirst().orElseThrow();
   }

   public static String w(String var0) {
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

   public static boolean M(mq.s var0) {
      if (var0.N()) {
         return false;
      }

      HUDModule var1 = HUDModule.U();

      return (boolean)(switch (var0.g) {
         case "crosshair" -> CustomCrosshairModule.I();
         case "notifications" -> 1;
         case "target" -> {
            TargetHUDModule var4 = LarpClient.F().x().V(TargetHUDModule.class);
            yield var4 != null && var4.isOperational() ? 1 : 0;
         }
         case "watermark" -> var1 != null && var1.f() ? 1 : 0;
         case "info" -> var1 != null && var1.M() ? 1 : 0;
         case "modules" -> var1 != null && var1.E() ? 1 : 0;
         case "armor" -> var1 != null && var1.C() ? 1 : 0;
         case "keybinds" -> var1 != null && var1.G() ? 1 : 0;
         case "effects" -> var1 != null && var1.Z() ? 1 : 0;
         case "speed" -> var1 != null && var1.c() ? 1 : 0;
         case "staffs" -> StaffListModule.L();
         case "island" -> IslandLyricsModule.l();
         case "casino" -> com.larpclient.module.v.v() != null ? 1 : 0;
         case "cooldowns", "inventory", "radar", "keystrokes" -> var1 != null && var1.e(var0.g) ? 1 : 0;
         default -> var1 != null && var1.isOperational() ? 1 : 0;
      });
   }

   public static void h() {
      P(var0 -> true);
   }

   public static void P(Predicate<mq.s> var0) {
      for (mq.s var2 : Y) {
         if (var0.test(var2)) {
            cx.O.remove(var2.g);
            cx.z var3 = cx.D.get(var2.g);
            if (var3 != null) {
               cx.D.put(var2.g, new cx.z(var2.T(), var3.hidden()));
            }
         }
      }
   }

   public static void m(mq.s var0, boolean var1) {
      W(var0, var1, var0x -> LarpClient.F().x().k(var0x));
   }

   public static void W(mq.s var0, boolean var1, Function<String, Module> var2) {
      var0.r(!var1);
      if (var1 && !var0.g.equals("notifications")) {
         Module var3 = (Module)var2.apply(
            var0.g.equals("crosshair")
               ? "CustomCrosshair"
               : (var0.g.equals("target") ? "TargetHUD" : (var0.g.equals("staffs") ? "Staff List" : (var0.g.equals("island") ? "Island Lyrics" : "HUD")))
         );
         if (var3 != null) {
            String var4 = switch (var0.g) {
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

            for (df var9 : var3.getSettings()) {
               if (var9.D().equals(var4) && var9 instanceof dh var7) {
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
               && !h1.I(var2.gui.screen())
               && !(var2.gui.screen() instanceof hu)) {
               GuiGraphicsExtractor var3 = zo.R(var0, false, false);
               if (var3 != null) {
                  x(var3);
                  h(var3, false);
               }
            }
         }
      );
      mt.K("HUD", "Registered {} Larp-styled movable elements; native fonts, saved placement and chat controls retained", Y.size());
   }

   public static void Z(Minecraft var0) {
      HUDModule var1 = HUDModule.U();
      if (var0.level == null || var0.player == null || var1 == null || !var1.isOperational()) {
         P.g();
      } else if (!var0.isPaused() && var0.level.tickRateManager().runsNormally()) {
         P.O(var0.level, var0.player, var0.player.getX(), var0.player.getZ(), var0.level.tickRateManager().tickrate());
      } else {
         P.c();
      }
   }

   private static List<ItemStack> z() {
      if (a == null) {
         a = List.of(
            new ItemStack(Items.DIAMOND_HELMET),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_BOOTS)
         );
      }

      return a;
   }

   public static void h(GuiGraphicsExtractor var0, boolean var1) {
      B(var0, var1, null, false);
   }

   public static void P(GuiGraphicsExtractor var0, mq.s var1, boolean var2) {
      B(var0, true, var1, var2);
   }

   public static List<mq.l> K() {
      return R == cx.U ? V : List.of();
   }

   public static void v() {
      V = List.of();
   }

   public static void K(GuiGraphicsExtractor var0, mq.s var1, boolean var2, boolean var3) {
      if (var3) {
         x(var0);
         h(var0, false);
         V = q();
         R = cx.U;
      } else {
         P(var0, var1, var2);
      }
   }

   private static void B(GuiGraphicsExtractor var0, boolean var1, mq.s var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null || var1) {
         ArrayList var5 = var1 ? new ArrayList() : null;
         if (var1) {
            V = List.of();
         }

         long var6 = kx.G();
         HUDModule var8 = HUDModule.U();
         int var9 = var4.getWindow().getGuiScaledWidth();
         int var10 = var4.getWindow().getGuiScaledHeight();
         int var11 = !var1 && L() ? 1 : 0;
         if (!var1 && var11 != x) {
            x = (boolean)var11;
            mt.U("HUD", var11 != 0 ? "Chat open; enabled overlays without content show samples" : "Chat closed; overlay samples removed");
         }

         List var12 = O.O(ChooseASkyEffectAndAdjustItsAppearanceModule.C(LarpClient.F().x().C(), LarpClient.F().f().t()), var4.font, k6.O(), m5.k, we::L);
         int var13 = (var1 || var11 != 0) && var12.isEmpty() ? 1 : 0;
         if (var13 != 0) {
            var12 = List.of(new wo.i("Sprint", we.L("Sprint")), new wo.i("Fullbright", we.L("Fullbright")));
         }

         mq.s var14 = B("modules");
         double var15 = var14.X();
         int var17 = Math.max(1, (int)((var10 - 24) / var15 / 17.0));
         int var18 = Math.max(1, (var12.size() + var17 - 1) / var17);
         int var19 = Math.max(20, var12.stream().mapToInt(var0x -> mn.T(var0x.width())).max().orElse(0));
         tk.z var20 = var8 != null && !var8.A() ? null : tk.J(var12, var1 || var13 != 0, tk.u(var10, var15));
         if (var20 != null) {
            var14.W = var20.width();
            var14.n = var20.height();
         } else {
            var14.W = var18 * var19 + (var18 - 1) * 8;
            var14.n = Math.max(20, Math.min(var17, var12.size()) * 17);
         }

         var14.H = Math.max(0.1, Math.min(2.5, Math.min((var9 - 16.0) / var14.W, (var10 - 16.0) / var14.n)));
         ArrayList var21 = new ArrayList();
         if (var4.player != null && var8 != null) {
            for (EquipmentSlot var25 : A) {
               if (var25 != EquipmentSlot.MAINHAND && var25 != EquipmentSlot.OFFHAND || var8.k.P()) {
                  ItemStack var26 = var4.player.getItemBySlot(var25);
                  if (!var26.isEmpty() || var8.W.P()) {
                     var21.add(var26);
                  }
               }
            }

            if (var8.s.P()) {
               Collections.reverse(var21);
            }
         }

         if ((var1 || var11 != 0) && var21.stream().allMatch(ItemStack::isEmpty)) {
            var21.clear();
            var21.addAll(z());
            if (var8 != null && var8.s.P()) {
               Collections.reverse(var21);
            }
         }

         int var61 = var8 != null && var8.v.S("Remaining") ? 46 : 26;
         int var62 = var8 != null && !var8.v.S("Off") ? 37 : 27;
         int var63 = var8 == null ? 5 : var8.A.P().intValue();
         int var64 = var8 != null && var8.V.S("Vertical") ? 1 : 0;
         if (var64 != 0 && var8 != null && !var8.v.S("Off")) {
            var61 += 38;
            var62 = 26;
         }

         int var65 = Math.max(1, var21.size());
         B("armor").W = 6 + (var64 != 0 ? var61 : var65 * (var61 + var63) - var63);
         B("armor").n = 6 + (var64 != 0 ? var65 * (var62 + var63) - var63 : var62);
         String var27 = var4.getFps() + " fps";
         String var28 = var4.player == null ? "Player" : t7.A(var4.player.getUUID(), var4.player.getName().getString());
         var28 = we.t(var28, 110.0, 11.0);
         String var29 = !var4.hasSingleplayerServer() && var4.getCurrentServer() != null ? var4.getCurrentServer().ip : "singleplayer";
         var29 = we.t(var29, 130.0, 11.0);
         String var30 = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
         B("watermark").W = mn.X(var28, var29, var27, var30);
         B("watermark").n = 42;
         String var31 = var4.player == null
            ? "128.0, 64.0, -32.0"
            : String.format(Locale.ROOT, "%.1f, %.1f, %.1f", var4.player.getX(), var4.player.getY(), var4.player.getZ());
         B("info").W = mn.R(var31);
         int var32 = var11 == 0 || com.larpclient.h.o() != null || L != null && !(F.G() < 0.01) ? 0 : 1;
         HashMap var33 = new HashMap();

         for (String var35 : List.of("cooldowns", "staffs", "inventory", "radar")) {
            e0.o var36 = e0.t(var35, var1);
            var33.put(var35, var36);
            B(var35).W = var36.width();
            B(var35).n = var36.height();
         }

         if (var8 != null) {
            B("keystrokes").W = kp.Y(var8);
            B("keystrokes").n = kp.h(var8);
         }

         we.t var68 = we.r(var1);
         we.t var69 = we.E(var1);
         we.t var70 = we.z(var1);
         B("casino").W = var70.width();
         B("casino").n = var70.height();
         int var37 = var11 == 0 || var4.player != null && !var4.player.getActiveEffects().isEmpty() ? 0 : 1;
         if (var37 != 0) {
            var69 = we.E(true);
         }

         B("keybinds").W = var68.width();
         B("keybinds").n = var68.height();
         B("effects").W = var69.width();
         B("effects").n = var69.height();
         Iterator var38 = Y.iterator();

         while (true) {
            mq.s var39;
            boolean var42;
            label1024:
            while (true) {
               if (!var38.hasNext()) {
                  if (var1) {
                     V = List.copyOf(var5);
                     R = cx.U;
                  }

                  if (!var1 && Boolean.getBoolean("larp.debug")) {
                     int var71 = var10 - 15;

                     for (Entry var74 : kx.G().entrySet()) {
                        String var76 = (String)var74.getKey() + " " + var74.getValue();
                        k6.G(var0, var76, var9 - k6.x(var76) - 5, var71, com.larpclient.f.s);
                        var71 -= 11;
                     }

                     for (Entry var75 : kx.M().entrySet()) {
                        String var77 = String.format(Locale.ROOT, "%s %.2f ms CPU", var75.getKey(), var75.getValue());
                        k6.G(var0, var77, var9 - k6.x(var77) - 5, var71, com.larpclient.f.s);
                        var71 -= 11;
                     }
                  }

                  kx.u("HUD", var6);
                  return;
               }

               var39 = (mq.s)var38.next();
               var39.P(var9, var10);
               int var40 = var39.W(var9);
               int var41 = var39.i(var10);
               var42 = M(var39);
               if ((var1 ? ez.k(var42, var3, var2 == var39) : var42)
                  && (var1 || (!var39.g.equals("modules") || (var20 == null ? !var12.isEmpty() : !var20.empty())) && (!var39.g.equals("armor") || Y(var21)))
                  && (var1 || var11 != 0 || !var33.containsKey(var39.g) || e0.v(var39.g, (e0.o)var33.get(var39.g)))
                  && (var1 || var37 != 0 || !var39.g.equals("effects") || var4.player != null && !var4.player.getActiveEffects().isEmpty())) {
                  int var43 = !var1 && (var11 == 0 || IslandLyricsModule.p()) ? 0 : 1;
                  if (var1 || !var39.g.equals("island") || var43 != 0 || IslandLyricsModule.p()) {
                     int var44 = !var39.g.equals("notifications") && !var39.g.equals("crosshair") ? 0 : 1;
                     if (var44 == 0 || var1) {
                        var39.v(var0, var40, var41);

                        try {
                           switch (var39.g) {
                              case "cooldowns":
                              case "staffs":
                              case "inventory":
                              case "radar":
                                 e0.W(var0, var39.g, (e0.o)var33.get(var39.g), var40, var41, var1);
                                 break label1024;
                              case "island":
                                 IslandLyricsModule var81 = IslandLyricsModule.u();
                                 if (var81 == null) {
                                    break;
                                 }

                                 var81.b(var0, var40, var41, var39.W, (boolean)var43);
                                 break label1024;
                              case "keystrokes":
                                 if (var8 == null) {
                                    break;
                                 }

                                 if (var8.X() && !(var4.gui.screen() instanceof hw)) {
                                    F(var0, var40, var41, var39.W, var39.n);
                                 }

                                 kp.J(var0, var8, var40, var41, var1);
                                 break label1024;
                              case "notifications":
                                 oj.N(var0, var40, var41);
                                 break label1024;
                              case "crosshair":
                                 CustomCrosshairModule.l(var0, var40 + var39.W / 2, var41 + var39.n / 2);
                                 break label1024;
                              case "watermark":
                                 if (!var1 && !var8.f()) {
                                    break;
                                 }

                                 if (var8 != null && var8.X() && !(var4.gui.screen() instanceof hw)) {
                                    F(var0, var40, var41, var39.W, var39.n);
                                 }

                                 mn.E(var0, var40, var41, var39.W, var28, var29, var27, var30);
                                 break label1024;
                              case "speed":
                                 mn.W(var0, var40, var41, P, var1);
                                 break label1024;
                              case "info":
                                 if (!var1 && !var8.M()) {
                                    break;
                                 }

                                 if (var8 != null && var8.X() && !(var4.gui.screen() instanceof hw)) {
                                    F(var0, var40, var41, var39.W, var39.n);
                                 }

                                 mn.u(var0, var40, var41, var39.W, var31);
                                 break label1024;
                              case "modules":
                                 if (!var1 && !var8.E()) {
                                    break;
                                 }

                                 int var80 = var8 != null && var8.X() && !(var4.gui.screen() instanceof hw) ? 1 : 0;
                                 if (var20 != null) {
                                    if (var80 != 0) {
                                       F(var0, var40, var41, var39.W, var39.n);
                                    }

                                    tk.L(var0, var20, var40, var41);
                                    if (!var1) {
                                       H(var39, var39.K(var9, var10));
                                    }
                                 } else {
                                    for (int var83 = 0; var83 < var12.size(); var83++) {
                                       String var86 = ((wo.i)var12.get(var83)).name();
                                       int var89 = var83 / var17;
                                       int var90 = var83 % var17;
                                       int var91 = var40 + var39.Y() / 2 > var9 / 2 ? 1 : 0;
                                       int var92 = var91 != 0
                                          ? var40 + var39.W - var89 * (var19 + 8)
                                          : var40 + var89 * (var19 + 8) + mn.T(((wo.i)var12.get(var83)).width());
                                       int var54 = ((wo.i)var12.get(var83)).width();
                                       int var93 = mn.T(var54);
                                       int var94 = var92 - var93;
                                       int var95 = var41 + var90 * 17;
                                       if (var80 != 0) {
                                          F(var0, var94, var95, var93, 17);
                                       }

                                       mn.C(var0, var86, var54, var92, var95, (boolean)var91);
                                       if (!var1) {
                                          H(
                                             var39,
                                             new ez.h(
                                                var40 + (int)Math.floor((var94 - var40) * var39.b()),
                                                var41 + (int)Math.floor((var95 - var41) * var39.b()),
                                                (int)Math.ceil(var93 * var39.b()),
                                                (int)Math.ceil(17.0 * var39.b())
                                             )
                                          );
                                       }
                                    }
                                 }

                                 if (var1 && var12.isEmpty() && var20 == null) {
                                    k6.G(var0, "No active modules", var40 + 4, var41 + 5, com.larpclient.f.s);
                                 }
                                 break label1024;
                              case "target":
                                 if (!var1 && var32 == 0) {
                                    F(var0, var40, var41);
                                    break label1024;
                                 }

                                 g(var0, var40, var41);
                                 break label1024;
                              case "keybinds":
                              case "effects":
                              case "casino":
                                 we.t var79 = var39.g.equals("keybinds") ? var68 : (var39.g.equals("casino") ? var70 : var69);
                                 int var82 = var40 + var39.Y() / 2 > var9 / 2 ? 1 : 0;
                                 if (var8 != null && var8.X() && !(var4.gui.screen() instanceof hw)) {
                                    for (ez.h var87 : we.e(var79, var40, var41, (boolean)var82)) {
                                       F(var0, var87.x(), var87.y(), var87.width(), var87.height());
                                    }
                                 }

                                 we.V(var0, var79, var40, var41, (boolean)var82);
                                 if (var1) {
                                    break label1024;
                                 }

                                 Iterator var85 = we.e(var79, var40, var41, (boolean)var82).iterator();

                                 while (true) {
                                    if (!var85.hasNext()) {
                                       break label1024;
                                    }

                                    ez.h var88 = (ez.h)var85.next();
                                    H(
                                       var39,
                                       new ez.h(
                                          var40 + (int)Math.floor((var88.x() - var40) * var39.b()),
                                          var41 + (int)Math.floor((var88.y() - var41) * var39.b()),
                                          (int)Math.ceil(var88.width() * var39.b()),
                                          (int)Math.ceil(var88.height() * var39.b())
                                       )
                                    );
                                 }
                              case "armor":
                                 if (!var1 && !var8.C()) {
                                    break;
                                 }

                                 if (var8.B.P() && Y(var21)) {
                                    c7.n(var0, var40, var41, var39.W, var39.n, 6.0F, mn.W());
                                    mn.b(var0, var40, var41, var39.W, var39.n, 6.0F);
                                    mn.X(var0, var40, var41, var39.W, var39.n, 6);
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
                                          int var55 = _3.D(-43434, -8790368, var53);
                                          if (var8.D.P()) {
                                             c7.s(var0, var49 + 2, var50 + 23, var61 - 4, 2, 1879048192);
                                             c7.s(var0, var49 + 2, var50 + 23, (int)Math.round((var61 - 4) * var53), 2, var55);
                                          }

                                          if (!var8.v.S("Off")) {
                                             String var56 = var8.v.S("Percent") ? (int)Math.ceil(var53 * 100.0) + "%" : Integer.toString(var52);
                                             int var57 = var64 != 0 ? var49 + 28 : var49 + (var61 - we.V(var56, 10.4)) / 2;
                                             we.F(var0, var56, var57, var50 + (var64 != 0 ? 7 : 28), 10.4, var55);
                                          }
                                       }
                                    }
                                 }
                              default:
                                 break label1024;
                           }
                        } finally {
                           var39.i(var0);
                        }
                     }
                  }
               }
            }

            if (!var1
               && (
                  var39.g.equals("target")
                     ? var32 != 0
                     : !var39.g.equals("modules") && !var39.g.equals("keybinds") && !var39.g.equals("effects") && !var39.g.equals("casino")
               )) {
               H(var39, var39.K(var9, var10));
            }

            if (var1) {
               var5.add(new mq.l(var39, var39.K(var9, var10)));
            }

            if (var1 && !var42) {
               ez.h var78 = var39.K(var9, var10);
               c7.s(var0, var78.x(), var78.y(), var78.width(), var78.height(), 1882207290);
            }
         }
      }
   }

   private static void F(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      Vector2f var5 = var0.pose().transformPosition(var1, var2, new Vector2f());
      m5.y(Math.round(var5.x), Math.round(var5.y), Math.round(var3 * var0.pose().m00()), Math.round(var4 * var0.pose().m11()));
   }

   public static void M() {
      L = null;
      S.Q(1.0);
      o.Q(1.0);
      v = 1.0;
      E = 0L;
      F.Q(0.0);
      I.N();
      h = 0L;
      l = 0.0F;
   }

   public static void g(GuiGraphicsExtractor var0, int var1, int var2) {
      Q(var0, var1, var2, Minecraft.getInstance().player, "Sample opponent", 14.0F, 20.0F, 2.0F, 6.0);
   }

   public static void Q(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3, String var4, float var5, float var6, float var7, double var8) {
      double var10 = Math.clamp(var5 / Math.max(1.0F, var6), 0.0F, 1.0F);
      L(var0, var1, var2, var3, var4, var5, var6, var7, var8, 1.0, var10, var10, true);
   }

   private static void F(GuiGraphicsExtractor var0, int var1, int var2) {
      long var3 = kx.G();

      try {
         Minecraft var5 = Minecraft.getInstance();
         if (L != null && L.level() != var5.level) {
            M();
         }

         LivingEntity var6 = com.larpclient.h.o();
         if (var6 != null && var6 != L) {
            L = var6;
            mt.U(
               "TargetHUD",
               "Target {}: {} x {} blocks, portrait scale {} before HUD transform",
               var6.getType(),
               var6.getBbWidth(),
               var6.getBbHeight(),
               J(var6.getBbWidth(), var6.getBbHeight())
            );
            I.N();
            l = t3.Z(var6);
            h = System.nanoTime();
            double var7 = t3.f(var6);
            S.Q(var7);
            o.Q(var7);
            v = var7;
            E = 0L;
         }

         long var22 = System.nanoTime();
         if (h != 0L) {
            I.S((var22 - h) / 1.0E9);
         }

         h = var22;
         float var9 = L == null ? 0.0F : t3.Z(L);
         if (L != null) {
            I.U(l - var9, ThreadLocalRandom.current());
            l = var9;
         }

         double var10 = F.m(var6 == null ? 0.0 : 1.0, 200);
         if (L == null || var10 < 0.01) {
            if (var6 == null) {
               L = null;
               I.N();
            }

            return;
         }

         mq.s var12 = B("target");
         double var13 = 0.92 + 0.08 * var10;
         H(var12, i(var1, var2, var12.b(), var13, new ez.h(0, 0, 224, 52)));
         double var15 = Math.clamp(var9 / Math.max(1.0F, L.getMaxHealth()), 0.0F, 1.0F);
         double var17 = S.m(var15, 220);
         L(
            var0,
            var1,
            var2,
            L,
            t7.A(L.getUUID(), L.getName().getString()),
            Math.max(0.0F, var9),
            Math.max(1.0F, L.getMaxHealth()),
            Math.max(0.0F, L.getAbsorptionAmount()),
            var5.player == null ? 0.0 : var5.player.distanceTo(L),
            var10,
            var17,
            s(var15, var17, var22),
            false
         );
      } finally {
         kx.u("TargetHUD", var3);
      }
   }

   private static double s(double var0, double var2, long var4) {
      if (var0 < v) {
         o.Q(Math.max(o.G(), var2));
         E = var4 + 400000000L;
      }

      v = var0;
      double var6 = var4 < E ? o.G() : o.y(var0, 450, ef.q.w);
      return Math.max(var6, var2);
   }

   public static ez.h i(int var0, int var1, double var2, double var4, ez.h var6) {
      double var7 = 112.0;
      double var9 = 26.0;
      double var11 = var0 + (var7 + (var6.x() - var7) * var4) * var2;
      double var13 = var1 + (var9 + (var6.y() - var9) * var4) * var2;
      int var15 = (int)Math.floor(var11);
      int var16 = (int)Math.floor(var13);
      return new ez.h(var15, var16, (int)Math.ceil(var11 + var6.width() * var4 * var2) - var15, (int)Math.ceil(var13 + var6.height() * var4 * var2) - var16);
   }

   public static float J(float var0, float var1) {
      return Float.isFinite(var0) && Float.isFinite(var1) && !(var0 <= 0.0F) && !(var1 <= 0.0F) ? Math.min(32.0F, 42.0F / Math.max(var0, var1)) : 0.0F;
   }

   private static void T(GuiGraphicsExtractor var0, int var1, int var2, LivingEntity var3) {
      EntityRenderer var4 = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(var3);
      EntityRenderState var5 = var4.createRenderState(var3, 1.0F);
      float var6 = J(var5.boundingBoxWidth, var5.boundingBoxHeight);
      if (var6 <= 0.0F) {
         mt.B("TargetHUD", "Invalid portrait dimensions; using an icon until target state recovers", null);
         we.C(var0, "user", var1 + 10, var2 + 10, 30, -5592406);
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

   public static List<mq.g> S(boolean var0, boolean var1) {
      if (var1) {
         return var0 ? List.of(mq.g.b, mq.g.p, mq.g.O) : List.of(mq.g.p, mq.g.O, mq.g.b);
      } else {
         return var0 ? List.of(mq.g.O, mq.g.b, mq.g.p) : List.of(mq.g.O, mq.g.p, mq.g.b);
      }
   }

   private static String D(float var0) {
      return var0 == Math.rint(var0) ? String.valueOf((int)var0) : String.format(Locale.ROOT, "%.1f", var0);
   }

   private static void L(
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
      TargetHUDModule var18 = LarpClient.F().x().V(TargetHUDModule.class);
      short var19 = 224;
      byte var20 = 52;
      double var21 = 0.92 + 0.08 * var10;
      var0.pose().pushMatrix();
      var0.pose().translate(var1 + var19 / 2.0F, var2 + var20 / 2.0F).scale((float)var21).translate(-var1 - var19 / 2.0F, -var2 - var20 / 2.0F);

      try {
         HUDModule var23 = HUDModule.U();
         if (var23 != null && var23.X() && !(var17.gui.screen() instanceof hw)) {
            F(var0, var1, var2, var19, var20);
         }

         c7.n(var0, var1, var2, var19, var20, 10.0F, _3.U(-468906478, var10));
         int var24 = var1 + 8;
         int var25 = var2 + 8;
         int var26 = var24 + 18;
         int var27 = var25 + 18;
         if (!var16 && !I.L().isEmpty()) {
            var0.enableScissor(var1, var2, var1 + var19, var2 + var20);

            try {
               for (mc.a var29 : I.L()) {
                  double var30 = Math.clamp(var29.J / var29.l, 0.0, 1.0);
                  double var32 = var10 * (1.0 - var30) * (var29.U ? 1.0 : 0.72);
                  int var34 = Math.max(1, (int)Math.round(var29.G * (1.0 - var30 * 0.35)));
                  int var35 = var26 + (int)Math.round(var29.H);
                  int var36 = var27 + (int)Math.round(var29.h);
                  int var37 = var29.U ? com.larpclient.f.C : -419430401;
                  c7.K(var0, var35, var36, (int)Math.ceil(var34 * 1.125), _3.U(var37, var32 * 0.22));
                  c7.K(var0, var35, var36, Math.max(1, var34 / 2), _3.U(var37, var32));
               }
            } finally {
               var0.disableScissor();
            }
         }

         c7.n(var0, var24 - 1, var25 - 1, 38.0F, 38.0F, 3.0F, _3.U(822083583, var10));
         if (var3 instanceof AbstractClientPlayer var55) {
            int var57 = Math.min(140, var55.hurtTime * 16);
            var0.pose().pushMatrix();
            float var59 = (float)Math.sin(var55.hurtTime / 10.0 * Math.PI * 2.0) * 0.07F;
            var0.pose().translate(var26, var27).rotate(var59).translate(-var26, -var27);
            PlayerFaceExtractor.extractRenderState(var0, var55.getSkin(), var24, var25, 36, _3.U(-65536 | 255 - var57 << 8 | 255 - var57, var10));
            var0.pose().popMatrix();
         } else if (var3 != null) {
            var0.pose().pushMatrix();
            var0.pose().translate(var24, var25).scale(0.72F).translate(-var24, -var25);

            try {
               T(var0, var24, var25, var3);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            we.C(var0, "user", var24 + 7, var25 + 7, 22, _3.U(-5592406, var10));
         }

         int var56 = var18 != null && !var18.E.S("Above") ? 0 : 1;
         int var58 = var18 != null && var18.m.S("Above") ? 1 : 0;
         int var60 = var18 != null && var18.L.S("Left") ? 1 : 0;
         int var31 = var1 + 53;
         int var61 = var1 + var19 - 9;
         int var33 = _3.U(-1, var10);
         int var62 = _3.U(-6645086, var10);
         double var63 = var25;

         for (mq.g var38 : S((boolean)var56, (boolean)var58)) {
            int var39 = (int)Math.round(var63);
            switch (var38) {
               case O:
                  String var65 = String.format(Locale.ROOT, var8 >= 100.0 ? "%.0fm" : "%.1fm", var8);
                  int var41 = var61 - we.V(var65, 10.0);
                  we.F(var0, var65, var41, var39 + 1.5, 10.0, var62);
                  we.F(var0, we.t(var4, Math.max(0, var41 - 6 - var31), 13.0), var31, var39, 13.0, var33);
                  break;
               case b:
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, Q, var31, var39 + 2, 9, 9, var33);
                  var0.blitSprite(RenderPipelines.GUI_TEXTURED, y, var31, var39 + 2, 9, 9, var33);
                  String var40 = D(var5);
                  we.F(var0, var40, var31 + 12, var39 + 1, 12.0, var33);
                  if (var7 > 0.0F) {
                     we.F(var0, "+" + D(var7), var31 + 16 + we.V(var40, 12.0), var39 + 2.5, 10.0, _3.U(-14003, var10));
                  }

                  if (var3 instanceof AbstractClientPlayer) {
                     o(var0, var3, var61 - 79, var39, (boolean)var60, var10);
                  }
                  break;
               case p:
                  P(var0, var31, var39, var61 - var31, var12, var14, var6, var7, var10);
            }

            var63 += var38.h + 1.5;
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   private static void P(GuiGraphicsExtractor var0, int var1, int var2, int var3, double var4, double var6, float var8, float var9, double var10) {
      double var12 = var8 / (var8 + Math.max(0.0F, var9));
      c7.n(var0, var1, var2, var3, 5.0F, 2.0F, _3.U(620756991, var10));
      int var14 = (int)Math.round(var3 * Math.clamp(var4, 0.0, 1.0) * var12);
      int var15 = (int)Math.round(var3 * Math.clamp(var6, 0.0, 1.0) * var12);
      if (var15 > var14) {
         c7.n(var0, var1, var2, var15, 5.0F, 2.0F, _3.U(-1862270977, var10));
      }

      if (var14 > 0) {
         tl.A(var0, var1, var2, var14, 5.0F, 2.0F, _3.U(-4776932, var10), _3.U(-42418, var10));
      }

      if (var9 > 0.0F) {
         int var16 = Math.min(var3 - var14, Math.round(var3 * var9 / (var8 + var9)));
         if (var16 > 0) {
            c7.n(var0, var1 + var14, var2, var16, 5.0F, 2.0F, _3.U(-14003, var10));
         }
      }
   }

   private static void o(GuiGraphicsExtractor var0, LivingEntity var1, int var2, int var3, boolean var4, double var5) {
      int var7 = var2;

      for (int var8 = 0; var8 < 2; var8++) {
         int var9 = var4 == (var8 == 0 ? 1 : 0) ? 1 : 0;
         int var10 = var9 != 0 ? 4 : 0;
         int var11 = var9 != 0 ? 6 : 4;

         for (int var12 = var10; var12 < var11; var12++) {
            ItemStack var13 = var1.getItemBySlot(A[var12]);
            if (var13.isEmpty()) {
               c7.n(var0, var7 + 3, var3 + 3, 6.0F, 6.0F, 2.0F, _3.U(486539263, var5));
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
                  c7.n(var0, var7 + 1, var3 + 12, 10.0F, 1.5F, 0.75F, _3.U(-1610612736, var5));
                  c7.n(var0, var7 + 1, var3 + 12, (float)Math.max(1.0, 10.0 * var14), 1.5F, 0.75F, _3.U(_3.D(-43434, -10755203, var14), var5));
               }
            }

            var7 += 13;
         }

         var7 += 2;
      }
   }

   public enum g {
      O(14),
      b(14),
      p(5);

      public final int h;

      g(int var3) {
         this.h = var3;
      }
   }

   public record l(mq.s element, ez.h bounds) {
   }

   public static final class s {
      public final String g;
      public int W;
      public int n;
      private double H = 2.5;
      private final cx.m U;

      s(String var1, int var2, int var3, double var4, double var6) {
         this.g = var1;
         this.W = var2;
         this.n = var3;
         this.U = new cx.m(var4, var6);
      }

      public double T() {
         return switch (this.g) {
            case "watermark" -> 0.55;
            case "keybinds", "modules", "info" -> 0.7;
            case "inventory" -> 0.7;
            default -> 1.0;
         };
      }

      public double X() {
         cx.z var1 = cx.D.get(this.g);
         return var1 == null ? this.T() : var1.scale();
      }

      public double b() {
         return Math.min(this.H, this.X());
      }

      public void P(int var1, int var2) {
         this.H = Math.max(0.1, Math.min(2.5, Math.min((double)Math.max(1, var1) / this.W, (double)Math.max(1, var2) / this.n)));
      }

      public ez.h K(int var1, int var2) {
         if (this.g.equals("crosshair")) {
            int var3 = CustomCrosshairModule.C();
            return new ez.h(
               this.W(var1) + (int)Math.round((this.W / 2.0 - var3) * this.b()),
               this.i(var2) + (int)Math.round((this.n / 2.0 - var3) * this.b()),
               (int)Math.ceil(var3 * 2 * this.b()),
               (int)Math.ceil(var3 * 2 * this.b())
            );
         } else {
            return new ez.h(this.W(var1), this.i(var2), this.Y(), this.g.equals("notifications") ? (int)Math.round(30.0 * this.b()) : this.K());
         }
      }

      public ez.h H(int var1, int var2) {
         return new ez.h(this.W(var1), this.i(var2), this.Y(), this.K());
      }

      public void f() {
         cx.O.remove(this.g);
         this.W(this.T());
      }

      public void I() {
         cx.O.remove(this.g);
         cx.D.remove(this.g);
      }

      public void o() {
         cx.O.put(this.g, new cx.m(0.5, 0.5));
      }

      public boolean N() {
         return cx.D.getOrDefault(this.g, mq.f).hidden();
      }

      public int Y() {
         return (int)Math.round(this.W * this.b());
      }

      public int K() {
         return (int)Math.round(this.n * this.b());
      }

      public void r(boolean var1) {
         cx.D.put(this.g, new cx.z(this.X(), var1));
      }

      public void W(double var1) {
         cx.D.put(this.g, new cx.z(var1, this.N()));
      }

      public void v(GuiGraphicsExtractor var1, int var2, int var3) {
         var1.pose().pushMatrix();
         var1.pose().translate(var2, var3).scale((float)this.b()).translate(-var2, -var3);
      }

      public void i(GuiGraphicsExtractor var1) {
         var1.pose().popMatrix();
      }

      public int W(int var1) {
         return this.g.equals("crosshair") && !cx.O.containsKey(this.g)
            ? Math.max(0, var1 / 2 - this.Y() / 2)
            : (int)Math.round(cx.O.getOrDefault(this.g, this.U).x() * Math.max(0, var1 - this.Y()));
      }

      public int i(int var1) {
         return this.g.equals("crosshair") && !cx.O.containsKey(this.g)
            ? Math.max(0, var1 / 2 - this.K() / 2)
            : (int)Math.round(cx.O.getOrDefault(this.g, this.U).y() * Math.max(0, var1 - this.K()));
      }

      public void s(int var1, int var2, int var3, int var4) {
         cx.O.put(this.g, new cx.m((double)var1 / Math.max(1, var3 - this.Y()), (double)var2 / Math.max(1, var4 - this.K())));
      }
   }
}

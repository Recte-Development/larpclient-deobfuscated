package com.larpclient.module;

import java.util.ArrayList;
import java.util.List;

public class HUDModule extends Module {
   private final m h = this.addSetting(new m("Watermark", "Show LARP watermark", true));
   private final m c = this.addSetting(new m("Module List", "Show active modules", true));
   private final m V = this.addSetting(new m("Info Panel", "Show coords/fps/ping etc", true));
   private final m z = this.addSetting(new m("Keybinds", "Show active module keybinds", true));
   private final m S = this.addSetting(new m("Potions", "Show active potion effects", false));
   private final m Q = this.addSetting(new m("Armor", "Show armor display", true));
   public final m I = this.addSetting(new m("Speed", "Show the speed graph", false));
   public final m u = this.addSetting(new m("Inventory", "Show the Larp inventory widget", false));
   public final m G = this.addSetting(new m("Cooldowns", "Show item cooldowns", false));
   public final m y = this.addSetting(new m("Radar", "Retired radar-widget preference", false));
   public final m A = this.addSetting(new m("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final m i = this.addSetting(new m("Music Bar", "Retired music-widget preference", false));
   public final m t = this.addSetting(new m("Watermark User", "Show player name", true));
   public final m g = this.addSetting(new m("Watermark IP", "Show server address", true));
   public final m d = this.addSetting(new m("Watermark FPS", "Show frame rate", true));
   public final m J = this.addSetting(new m("Watermark Time", "Show local time", true));
   public final o T = this.addSetting(new o("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final o E = this.addSetting(new o("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only"));
   public final o P = this.addSetting(new o("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final m R = this.addSetting(new m("Inventory Header", "Show inventory title", true));
   public final b e = this.addSetting(new b("Radar Range", "Retired radar-widget preference", 47.0, 20.0, 80.0, 5.0));
   public final m q = this.addSetting(new m("Radar Mobs", "Retired radar-widget preference", false));
   public final m k = this.addSetting(new m("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final m b = this.addSetting(new m("Keystrokes Space", "Show the jump key", true));
   public final m o = this.addSetting(new m("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final m w = this.addSetting(new m("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false));
   public final b L = this.addSetting(new b("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0));
   public final b M = this.addSetting(new b("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0));
   public final m U = this.addSetting(new m("Keystrokes Shadow", "Soft shadow under the card", true));
   public final o p = this.addSetting(new o("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final m v = this.addSetting(new m("Music Progress", "Show track progress", true));
   public final m W = this.addSetting(new m("Music Time", "Show track position and duration", true));
   public final o N = this.addSetting(new o("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final b B = this.addSetting(new b("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final o l = this.addSetting(new o("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining"));
   public final m C = this.addSetting(new m("Armor Bars", "Color-coded durability bars", true));
   public final m X = this.addSetting(new m("Armor Background", "Draw equipment panel", false));
   public final m Y = this.addSetting(new m("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true));
   public final m D = this.addSetting(new m("Armor Reverse", "Reverse equipment order", false));
   public final m s = this.addSetting(new m("Armor Held Items", "Include main hand and offhand", false));
   public final h x = this.addSetting(new h("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final h m = this.addSetting(new h("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final h Z = this.addSetting(new h("Speed Line", "Speed graph curve color", -1841171));
   public final m F = this.addSetting(new m("Speed Card Background", "Use the same dark background as the other HUD cards", true));
   private final m O = this.addSetting(new m("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final m f = this.addSetting(new m("Light Sweep", "Faint light band crossing every HUD panel", true));
   private final m K = this.addSetting(new m("Panel Glow", "Accent gradient and edge light inside HUD panels", true));
   public final o j = this.addSetting(
      new o("Module List Style", "Card lists active modules in a titled panel; Rows keeps the stepped list", "Card", "Card", "Rows")
   );
   private static HUDModule H;

   public List<r<?>> L() {
      return List.of(this.F, this.x, this.m, this.Z);
   }

   public List<r<?>> J() {
      return List.of(this.k, this.b, this.o, this.w, this.L, this.M);
   }

   public List<r<?>> f() {
      return List.of(this.h, this.c, this.V, this.z, this.S, this.Q, this.I, this.u, this.G, this.A);
   }

   public List<r<?>> T() {
      ArrayList var1 = new ArrayList<>(List.of(this.i, this.p, this.v, this.W));
      var1.addAll(List.of(this.y, this.e, this.q));
      var1.add(this.U);
      return List.copyOf(var1);
   }

   public List<r<?>> z() {
      return List.of(this.O, this.f, this.K);
   }

   public List<r<?>> L(String var1) {
      return switch (var1) {
         case "speed" -> this.L();
         case "keybinds" -> List.of(this.E, this.O);
         case "inventory" -> List.of(this.R, this.O);
         case "cooldowns" -> List.of(this.P, this.O);
         case "staffs" -> List.of(this.O);
         case "keystrokes" -> {
            ArrayList var4 = new ArrayList<>(this.J());
            var4.add(this.O);
            yield List.copyOf(var4);
         }
         case "armor" -> List.of(this.N, this.B, this.l, this.C, this.X, this.Y, this.D, this.s, this.O);
         case "watermark" -> List.of(this.t, this.g, this.d, this.J, this.T, this.O);
         case "modules" -> List.of(this.j, this.O);
         case "info", "effects", "target", "miner", "builder", "pilot", "hunter" -> List.of(this.O);
         default -> List.of();
      };
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.isOperational() && this.I.getValue();
   }

   public boolean z(String var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational()) {
         switch (var1) {
            case "inventory":
               if (this.u.getValue()) {
                  return true;
               }
               break;
            case "cooldowns":
               if (this.G.getValue()) {
                  return true;
               }
               break;
            case "keystrokes":
               if (this.A.getValue()) {
                  return true;
               }
         }
      }

      return false;
   }

   public boolean u() {
      return this.isOperational() && this.O.getValue();
   }

   public boolean f() {
      return this.f.getValue();
   }

   public boolean T() {
      return this.K.getValue();
   }

   public boolean S() {
      return this.j.w("Card");
   }

   public HUDModule() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      H = this;

      for (r var2 : List.of(this.t, this.g, this.d, this.J, this.T)) {
         var2.a(this.h::getValue);
      }

      this.E.a(this.z::getValue);
      this.j.a(this.c::getValue);
      this.P.a(this.G::getValue);
      this.R.a(this.u::getValue);

      for (r var9 : this.J()) {
         var9.a(this.A::getValue);
      }

      this.o.a(() -> this.A.getValue() && this.k.getValue());

      for (r var10 : this.T()) {
         var10.a(() -> false);
      }

      this.F.a(this.I::getValue);
      this.Z.a(this.I::getValue);

      for (h var11 : List.of(this.x, this.m)) {
         var11.a(() -> this.I.getValue() && !this.F.getValue());
      }

      for (r var4 : new r[]{this.N, this.B, this.l, this.C, this.X, this.Y, this.D, this.s}) {
         var4.a(this.Q::getValue);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUDModule w() {
      return H;
   }

   public boolean V() {
      return !this.isEnabled() ? false : this.isOperational() && this.h.getValue();
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.isOperational() && this.c.getValue();
   }

   public boolean w() {
      return !this.isEnabled() ? false : this.isOperational() && this.V.getValue();
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.isOperational() && this.z.getValue();
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isOperational() && this.S.getValue();
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.isOperational() && this.Q.getValue();
   }
}

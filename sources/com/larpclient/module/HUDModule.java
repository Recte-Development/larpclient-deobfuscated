package com.larpclient.module;

import java.util.ArrayList;
import java.util.List;

public class HUDModule extends Module {
   private final dh x = this.addSetting(new dh("Watermark", "Show LARP watermark", true));
   private final dh Q = this.addSetting(new dh("Module List", "Show active modules", true));
   private final dh K = this.addSetting(new dh("Info Panel", "Show coords/fps/ping etc", true));
   private final dh E = this.addSetting(new dh("Keybinds", "Show active module keybinds", true));
   private final dh f = this.addSetting(new dh("Potions", "Show active potion effects", false));
   private final dh G = this.addSetting(new dh("Armor", "Show armor display", true));
   public final dh X = this.addSetting(new dh("Speed", "Show the speed graph", false));
   public final dh L = this.addSetting(new dh("Inventory", "Show the Larp inventory widget", false));
   public final dh J = this.addSetting(new dh("Cooldowns", "Show item cooldowns", false));
   public final dh R = this.addSetting(new dh("Radar", "Show nearby players", false));
   public final dh a = this.addSetting(new dh("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final dh w = this.addSetting(new dh("Music Bar", "Retired music-widget preference", false));
   public final dh U = this.addSetting(new dh("Watermark User", "Show player name", true));
   public final dh y = this.addSetting(new dh("Watermark IP", "Show server address", true));
   public final dh n = this.addSetting(new dh("Watermark FPS", "Show frame rate", true));
   public final dh t = this.addSetting(new dh("Watermark Time", "Show local time", true));
   public final d6 I = this.addSetting(new d6("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final d6 g = this.addSetting(new d6("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only"));
   public final d6 b = this.addSetting(new d6("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final dh M = this.addSetting(new dh("Inventory Header", "Show inventory title", true));
   public final d9 u = this.addSetting(new d9("Radar Range", "Nearby radar range in blocks", 47.0, 20.0, 80.0, 5.0));
   public final dh H = this.addSetting(new dh("Radar Mobs", "Show nearby mobs", false));
   public final dh e = this.addSetting(new dh("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final dh z = this.addSetting(new dh("Keystrokes Space", "Show the jump key", true));
   public final dh Y = this.addSetting(new dh("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final dh c = this.addSetting(new dh("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false));
   public final d9 o = this.addSetting(new d9("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0));
   public final d9 i = this.addSetting(new d9("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0));
   public final dh P = this.addSetting(new dh("Keystrokes Shadow", "Soft shadow under the card", true));
   public final d6 d = this.addSetting(new d6("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final dh N = this.addSetting(new dh("Music Progress", "Show track progress", true));
   public final dh C = this.addSetting(new dh("Music Time", "Show track position and duration", true));
   public final d6 V = this.addSetting(new d6("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final d9 A = this.addSetting(new d9("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final d6 v = this.addSetting(new d6("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining"));
   public final dh D = this.addSetting(new dh("Armor Bars", "Color-coded durability bars", true));
   public final dh B = this.addSetting(new dh("Armor Background", "Draw equipment panel", false));
   public final dh W = this.addSetting(new dh("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true));
   public final dh s = this.addSetting(new dh("Armor Reverse", "Reverse equipment order", false));
   public final dh k = this.addSetting(new dh("Armor Held Items", "Include main hand and offhand", false));
   public final dk T = this.addSetting(new dk("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final dk r = this.addSetting(new dk("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final dk j = this.addSetting(new dk("Speed Line", "Speed graph curve color", -1841171));
   private final dh m = this.addSetting(new dh("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final dh q = this.addSetting(new dh("Light Sweep", "Faint light band crossing every HUD panel", true));
   private final dh Z = this.addSetting(new dh("Panel Glow", "Accent gradient and edge light inside HUD panels", true));
   public final d6 h = this.addSetting(
      new d6("Module List Style", "Card lists active modules in a titled panel; Rows keeps the stepped list", "Card", "Card", "Rows")
   );
   private static HUDModule p;

   public List<df<?>> n() {
      return List.of(this.T, this.r, this.j);
   }

   public List<df<?>> G() {
      return List.of(this.e, this.z, this.Y, this.c, this.o, this.i);
   }

   public List<df<?>> d() {
      return List.of(this.x, this.Q, this.K, this.E, this.f, this.G, this.X, this.L, this.J, this.R, this.a);
   }

   public List<df<?>> y() {
      return List.of(this.w, this.d, this.N, this.C, this.P);
   }

   public List<df<?>> T() {
      return List.of(this.m, this.q, this.Z);
   }

   public List<df<?>> Y(String var1) {
      return switch (var1) {
         case "speed" -> this.n();
         case "keybinds" -> List.of(this.g, this.m);
         case "inventory" -> List.of(this.M, this.m);
         case "cooldowns" -> List.of(this.b, this.m);
         case "staffs" -> List.of(this.m);
         case "keystrokes" -> {
            ArrayList var4 = new ArrayList<>(this.G());
            var4.add(this.m);
            yield List.copyOf(var4);
         }
         case "radar" -> List.of(this.u, this.H, this.m);
         case "armor" -> List.of(this.V, this.A, this.v, this.D, this.B, this.W, this.s, this.k, this.m);
         case "watermark" -> List.of(this.U, this.y, this.n, this.t, this.I, this.m);
         case "modules" -> List.of(this.h, this.m);
         case "info", "effects", "target" -> List.of(this.m);
         default -> List.of();
      };
   }

   public boolean c() {
      return !this.isEnabled() ? false : this.isOperational() && this.X.P();
   }

   public boolean e(String var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational()) {
         switch (var1) {
            case "inventory":
               if (this.L.P()) {
                  return true;
               }
               break;
            case "cooldowns":
               if (this.J.P()) {
                  return true;
               }
               break;
            case "radar":
               if (this.R.P()) {
                  return true;
               }
               break;
            case "keystrokes":
               if (this.a.P()) {
                  return true;
               }
         }
      }

      return false;
   }

   public boolean X() {
      return this.isOperational() && this.m.P();
   }

   public boolean L() {
      return this.q.P();
   }

   public boolean F() {
      return this.Z.P();
   }

   public boolean A() {
      return this.h.S("Card");
   }

   public HUDModule() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      p = this;

      for (df var2 : List.of(this.U, this.y, this.n, this.t, this.I)) {
         var2.H(this.x::P);
      }

      this.g.H(this.E::P);
      this.h.H(this.Q::P);
      this.b.H(this.J::P);
      this.M.H(this.L::P);
      this.u.H(this.R::P);
      this.H.H(this.R::P);

      for (df var9 : this.G()) {
         var9.H(this.a::P);
      }

      this.Y.H(() -> this.a.P() && this.e.P());

      for (df var10 : this.y()) {
         var10.H(() -> false);
      }

      for (df var11 : this.n()) {
         var11.H(this.X::P);
      }

      for (df var4 : new df[]{this.V, this.A, this.v, this.D, this.B, this.W, this.s, this.k}) {
         var4.H(this.G::P);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUDModule U() {
      return p;
   }

   public boolean f() {
      return !this.isEnabled() ? false : this.isOperational() && this.x.P();
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.isOperational() && this.Q.P();
   }

   public boolean M() {
      return !this.isEnabled() ? false : this.isOperational() && this.K.P();
   }

   public boolean G() {
      return !this.isEnabled() ? false : this.isOperational() && this.E.P();
   }

   public boolean Z() {
      return !this.isEnabled() ? false : this.isOperational() && this.f.P();
   }

   public boolean C() {
      return !this.isEnabled() ? false : this.isOperational() && this.G.P();
   }
}

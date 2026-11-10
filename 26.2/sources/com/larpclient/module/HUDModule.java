package com.larpclient.module;

import java.util.ArrayList;
import java.util.List;

public class HUDModule extends Module {
   private final s i = this.addSetting(new s("Watermark", "Show LARP watermark", true));
   private final s Z = this.addSetting(new s("Module List", "Show active modules", true));
   private final s U = this.addSetting(new s("Info Panel", "Show coords/fps/ping etc", true));
   private final s V = this.addSetting(new s("Keybinds", "Show active module keybinds", true));
   private final s e = this.addSetting(new s("Potions", "Show active potion effects", false));
   private final s u = this.addSetting(new s("Armor", "Show armor display", true));
   public final s I = this.addSetting(new s("Speed", "Show the speed graph", false));
   public final s v = this.addSetting(new s("Inventory", "Show the Larp inventory widget", false));
   public final s C = this.addSetting(new s("Cooldowns", "Show item cooldowns", false));
   public final s w = this.addSetting(new s("Radar", "Retired radar-widget preference", false));
   public final s y = this.addSetting(new s("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final s t = this.addSetting(new s("Music Bar", "Retired music-widget preference", false));
   public final s l = this.addSetting(new s("Watermark User", "Show player name", true));
   public final s g = this.addSetting(new s("Watermark IP", "Show server address", true));
   public final s q = this.addSetting(new s("Watermark FPS", "Show frame rate", true));
   public final s W = this.addSetting(new s("Watermark Time", "Show local time", true));
   public final l o = this.addSetting(new l("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final l Y = this.addSetting(new l("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only"));
   public final l X = this.addSetting(new l("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final s R = this.addSetting(new s("Inventory Header", "Show inventory title", true));
   public final e Q = this.addSetting(new e("Radar Range", "Retired radar-widget preference", 47.0, 20.0, 80.0, 5.0));
   public final s P = this.addSetting(new s("Radar Mobs", "Retired radar-widget preference", false));
   public final s p = this.addSetting(new s("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final s L = this.addSetting(new s("Keystrokes Space", "Show the jump key", true));
   public final s b = this.addSetting(new s("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final s x = this.addSetting(new s("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false));
   public final e K = this.addSetting(new e("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0));
   public final e T = this.addSetting(new e("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0));
   public final s c = this.addSetting(new s("Keystrokes Shadow", "Soft shadow under the card", true));
   public final l d = this.addSetting(new l("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final s F = this.addSetting(new s("Music Progress", "Show track progress", true));
   public final s f = this.addSetting(new s("Music Time", "Show track position and duration", true));
   public final l J = this.addSetting(new l("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final e z = this.addSetting(new e("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final l O = this.addSetting(new l("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining"));
   public final s N = this.addSetting(new s("Armor Bars", "Color-coded durability bars", true));
   public final s m = this.addSetting(new s("Armor Background", "Draw equipment panel", false));
   public final s B = this.addSetting(new s("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true));
   public final s k = this.addSetting(new s("Armor Reverse", "Reverse equipment order", false));
   public final s a = this.addSetting(new s("Armor Held Items", "Include main hand and offhand", false));
   public final i G = this.addSetting(new i("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final i j = this.addSetting(new i("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final i D = this.addSetting(new i("Speed Line", "Speed graph curve color", -1841171));
   public final s r = this.addSetting(new s("Speed Card Background", "Use the same dark background as the other HUD cards", true));
   private final s E = this.addSetting(new s("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final s h = this.addSetting(new s("Light Sweep", "Faint light band crossing every HUD panel", true));
   private final s s = this.addSetting(new s("Panel Glow", "Accent gradient and edge light inside HUD panels", true));
   public final l A = this.addSetting(
      new l("Module List Style", "Card lists active modules in a titled panel; Rows keeps the stepped list", "Card", "Card", "Rows")
   );
   private static HUDModule M;

   public List<q<?>> R() {
      return List.of(this.r, this.G, this.j, this.D);
   }

   public List<q<?>> E() {
      return List.of(this.p, this.L, this.b, this.x, this.K, this.T);
   }

   public List<q<?>> z() {
      return List.of(this.i, this.Z, this.U, this.V, this.e, this.u, this.I, this.v, this.C, this.y);
   }

   public List<q<?>> Q() {
      ArrayList var1 = new ArrayList<>(List.of(this.t, this.d, this.F, this.f));
      var1.addAll(List.of(this.w, this.Q, this.P));
      var1.add(this.c);
      return List.copyOf(var1);
   }

   public List<q<?>> C() {
      return List.of(this.E, this.h, this.s);
   }

   public List<q<?>> w(String var1) {
      return switch (var1) {
         case "speed" -> this.R();
         case "keybinds" -> List.of(this.Y, this.E);
         case "inventory" -> List.of(this.R, this.E);
         case "cooldowns" -> List.of(this.X, this.E);
         case "staffs" -> List.of(this.E);
         case "keystrokes" -> {
            ArrayList var4 = new ArrayList<>(this.E());
            var4.add(this.E);
            yield List.copyOf(var4);
         }
         case "armor" -> List.of(this.J, this.z, this.O, this.N, this.m, this.B, this.k, this.a, this.E);
         case "watermark" -> List.of(this.l, this.g, this.q, this.W, this.o, this.E);
         case "modules" -> List.of(this.A, this.E);
         case "info", "effects", "target", "miner", "builder", "pilot", "hunter" -> List.of(this.E);
         default -> List.of();
      };
   }

   public boolean f() {
      return !this.isEnabled() ? false : this.isOperational() && this.I.getValue();
   }

   public boolean g(String var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational()) {
         switch (var1) {
            case "inventory":
               if (this.v.getValue()) {
                  return true;
               }
               break;
            case "cooldowns":
               if (this.C.getValue()) {
                  return true;
               }
               break;
            case "keystrokes":
               if (this.y.getValue()) {
                  return true;
               }
         }
      }

      return false;
   }

   public boolean p() {
      return this.isOperational() && this.E.getValue();
   }

   public boolean t() {
      return this.h.getValue();
   }

   public boolean g() {
      return this.s.getValue();
   }

   public boolean o() {
      return this.A.O("Card");
   }

   public HUDModule() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      M = this;

      for (q var2 : List.of(this.l, this.g, this.q, this.W, this.o)) {
         var2.P(this.i::getValue);
      }

      this.Y.P(this.V::getValue);
      this.A.P(this.Z::getValue);
      this.X.P(this.C::getValue);
      this.R.P(this.v::getValue);

      for (q var9 : this.E()) {
         var9.P(this.y::getValue);
      }

      this.b.P(() -> this.y.getValue() && this.p.getValue());

      for (q var10 : this.Q()) {
         var10.P(() -> false);
      }

      this.r.P(this.I::getValue);
      this.D.P(this.I::getValue);

      for (i var11 : List.of(this.G, this.j)) {
         var11.P(() -> this.I.getValue() && !this.r.getValue());
      }

      for (q var4 : new q[]{this.J, this.z, this.O, this.N, this.m, this.B, this.k, this.a}) {
         var4.P(this.u::getValue);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUDModule K() {
      return M;
   }

   public boolean L() {
      return !this.isEnabled() ? false : this.isOperational() && this.i.getValue();
   }

   public boolean A() {
      return !this.isEnabled() ? false : this.isOperational() && this.Z.getValue();
   }

   public boolean x() {
      return !this.isEnabled() ? false : this.isOperational() && this.U.getValue();
   }

   public boolean O() {
      return !this.isEnabled() ? false : this.isOperational() && this.V.getValue();
   }

   public boolean m() {
      return !this.isEnabled() ? false : this.isOperational() && this.e.getValue();
   }

   public boolean d() {
      return !this.isEnabled() ? false : this.isOperational() && this.u.getValue();
   }
}

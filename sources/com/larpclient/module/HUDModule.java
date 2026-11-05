package com.larpclient.module;

import java.util.ArrayList;
import java.util.List;

public class HUDModule extends Module {
   private final ln h = this.addSetting(new ln("Watermark", "Show LARP watermark", true));
   private final ln I = this.addSetting(new ln("Module List", "Show active modules", true));
   private final ln i = this.addSetting(new ln("Info Panel", "Show coords/fps/ping etc", true));
   private final ln j = this.addSetting(new ln("Keybinds", "Show active module keybinds", true));
   private final ln t = this.addSetting(new ln("Potions", "Show active potion effects", false));
   private final ln A = this.addSetting(new ln("Armor", "Show armor display", true));
   public final ln o = this.addSetting(new ln("Speed", "Show the speed graph", false));
   public final ln N = this.addSetting(new ln("Inventory", "Show the Larp inventory widget", false));
   public final ln R = this.addSetting(new ln("Cooldowns", "Show item cooldowns", false));
   public final ln f = this.addSetting(new ln("Radar", "Show nearby players", false));
   public final ln G = this.addSetting(new ln("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final ln q = this.addSetting(new ln("Music Bar", "Retired music-widget preference", false));
   public final ln d = this.addSetting(new ln("Watermark User", "Show player name", true));
   public final ln K = this.addSetting(new ln("Watermark IP", "Show server address", true));
   public final ln s = this.addSetting(new ln("Watermark FPS", "Show frame rate", true));
   public final ln U = this.addSetting(new ln("Watermark Time", "Show local time", true));
   public final lp D = this.addSetting(new lp("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final lp a = this.addSetting(new lp("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only"));
   public final lp H = this.addSetting(new lp("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final ln e = this.addSetting(new ln("Inventory Header", "Show inventory title", true));
   public final lc J = this.addSetting(new lc("Radar Range", "Nearby radar range in blocks", 47.0, 20.0, 80.0, 5.0));
   public final ln X = this.addSetting(new ln("Radar Mobs", "Show nearby mobs", false));
   public final ln O = this.addSetting(new ln("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final ln W = this.addSetting(new ln("Keystrokes Space", "Show the jump key", true));
   public final ln m = this.addSetting(new ln("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final ln g = this.addSetting(new ln("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false));
   public final lc Z = this.addSetting(new lc("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0));
   public final lc P = this.addSetting(new lc("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0));
   public final ln S = this.addSetting(new ln("Keystrokes Shadow", "Soft shadow under the card", true));
   public final lp Q = this.addSetting(new lp("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final ln v = this.addSetting(new ln("Music Progress", "Show track progress", true));
   public final ln F = this.addSetting(new ln("Music Time", "Show track position and duration", true));
   public final lp c = this.addSetting(new lp("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final lc B = this.addSetting(new lc("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final lp z = this.addSetting(new lp("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining"));
   public final ln b = this.addSetting(new ln("Armor Bars", "Color-coded durability bars", true));
   public final ln Y = this.addSetting(new ln("Armor Background", "Draw equipment panel", false));
   public final ln n = this.addSetting(new ln("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true));
   public final ln L = this.addSetting(new ln("Armor Reverse", "Reverse equipment order", false));
   public final ln r = this.addSetting(new ln("Armor Held Items", "Include main hand and offhand", false));
   public final lv x = this.addSetting(new lv("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final lv l = this.addSetting(new lv("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final lv E = this.addSetting(new lv("Speed Line", "Speed graph curve color", -1841171));
   private final ln C = this.addSetting(new ln("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final ln k = this.addSetting(new ln("Light Sweep", "Faint light band crossing every HUD panel", true));
   private final ln M = this.addSetting(new ln("Panel Glow", "Accent gradient and edge light inside HUD panels", true));
   public final lp V = this.addSetting(
      new lp("Module List Style", "Card lists active modules in a titled panel; Rows keeps the stepped list", "Card", "Card", "Rows")
   );
   private static HUDModule p;

   public List<l6<?>> F() {
      return List.of(this.x, this.l, this.E);
   }

   public List<l6<?>> h() {
      return List.of(this.O, this.W, this.m, this.g, this.Z, this.P);
   }

   public List<l6<?>> c() {
      return List.of(this.h, this.I, this.i, this.j, this.t, this.A, this.o, this.N, this.R, this.f, this.G);
   }

   public List<l6<?>> B() {
      return List.of(this.q, this.Q, this.v, this.F, this.S);
   }

   public List<l6<?>> I() {
      return List.of(this.C, this.k, this.M);
   }

   public List<l6<?>> t(String var1) {
      return switch (var1) {
         case "speed" -> this.F();
         case "keybinds" -> List.of(this.a, this.C);
         case "inventory" -> List.of(this.e, this.C);
         case "cooldowns" -> List.of(this.H, this.C);
         case "staffs" -> List.of(this.C);
         case "keystrokes" -> {
            ArrayList var4 = new ArrayList<>(this.h());
            var4.add(this.C);
            yield List.copyOf(var4);
         }
         case "radar" -> List.of(this.J, this.X, this.C);
         case "armor" -> List.of(this.c, this.B, this.z, this.b, this.Y, this.n, this.L, this.r, this.C);
         case "watermark" -> List.of(this.d, this.K, this.s, this.U, this.D, this.C);
         case "modules" -> List.of(this.V, this.C);
         case "info", "effects", "target" -> List.of(this.C);
         default -> List.of();
      };
   }

   public boolean w() {
      return !this.isEnabled() ? false : this.isOperational() && this.o.getValue();
   }

   public boolean D(String var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational()) {
         switch (var1) {
            case "inventory":
               if (!this.N.getValue()) {
                  return false;
               }
               break;
            case "cooldowns":
               if (!this.R.getValue()) {
                  return false;
               }
               break;
            case "radar":
               if (!this.f.getValue()) {
                  return false;
               }
               break;
            case "keystrokes":
               if (!this.G.getValue()) {
                  return false;
               }
               break;
            default:
               return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean L() {
      return this.isOperational() && this.C.getValue();
   }

   public boolean o() {
      return this.k.getValue();
   }

   public boolean g() {
      return this.M.getValue();
   }

   public boolean p() {
      return this.V.i("Card");
   }

   public HUDModule() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      p = this;

      for (l6 var2 : List.of(this.d, this.K, this.s, this.U, this.D)) {
         var2.x(this.h::getValue);
      }

      this.a.x(this.j::getValue);
      this.V.x(this.I::getValue);
      this.H.x(this.R::getValue);
      this.e.x(this.N::getValue);
      this.J.x(this.f::getValue);
      this.X.x(this.f::getValue);

      for (l6 var9 : this.h()) {
         var9.x(this.G::getValue);
      }

      this.m.x(() -> this.G.getValue() && this.O.getValue());

      for (l6 var10 : this.B()) {
         var10.x(() -> false);
      }

      for (l6 var11 : this.F()) {
         var11.x(this.o::getValue);
      }

      for (l6 var4 : new l6[]{this.c, this.B, this.z, this.b, this.Y, this.n, this.L, this.r}) {
         var4.x(this.A::getValue);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUDModule f() {
      return p;
   }

   public boolean H() {
      return !this.isEnabled() ? false : this.isOperational() && this.h.getValue();
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.isOperational() && this.I.getValue();
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.isOperational() && this.i.getValue();
   }

   public boolean M() {
      return !this.isEnabled() ? false : this.isOperational() && this.j.getValue();
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.isOperational() && this.t.getValue();
   }

   public boolean l() {
      return !this.isEnabled() ? false : this.isOperational() && this.A.getValue();
   }
}

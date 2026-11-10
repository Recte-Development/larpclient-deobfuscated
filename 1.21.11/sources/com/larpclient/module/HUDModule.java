package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.uw;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HUDModule extends Module {
   public final li a = this.addSetting(new li("HUD Font", "Font used by every HUD overlay", "Inter", "Inter", "SF Pro", "Minecraft"));
   public final aj B = this.addSetting(new aj("Staff Names", "Donut account names, comma separated; blank uses the reference list", ""));
   private final l3 h = this.addSetting(new l3("Watermark", "Show LARP watermark", true));
   private final l3 H = this.addSetting(new l3("Module List", "Show active modules", true));
   private final l3 A = this.addSetting(new l3("Info Panel", "Show coords/fps/ping etc", true));
   private final l3 g = this.addSetting(new l3("Keybinds", "Show active module keybinds", true));
   private final l3 x = this.addSetting(new l3("Potions", "Show active potion effects", false));
   private final l3 T = this.addSetting(new l3("Armor", "Show armor display", true));
   public final l3 q = this.addSetting(new l3("Speed", "Show the speed graph", false));
   public final l3 O = this.addSetting(new l3("Inventory", "Show the Larp inventory widget", false));
   public final l3 k = this.addSetting(new l3("Cooldowns", "Show item cooldowns", false));
   public final l3 d = this.addSetting(new l3("Radar", "Retired radar-widget preference", false));
   public final l3 R = this.addSetting(new l3("Keystrokes", "Show movement keys and mouse buttons as they are pressed", false));
   public final l3 i = this.addSetting(new l3("Music Bar", "Retired music-widget preference", false));
   public final l3 r = this.addSetting(new l3("Watermark User", "Show player name", true));
   public final l3 U = this.addSetting(new l3("Watermark IP", "Show server address", true));
   public final l3 D = this.addSetting(new l3("Watermark FPS", "Show frame rate", true));
   public final l3 f = this.addSetting(new l3("Watermark Time", "Show local time", true));
   public final li I = this.addSetting(new li("Watermark Logo Side", "Logo tile position", "Left", "Left", "Right"));
   public final li C = this.addSetting(new li("Keybind Display", "Show every bound module or only enabled modules", "Always", "Always", "Active Only"));
   public final li w = this.addSetting(new li("Cooldown Items", "Which cooldown items to display", "Combat", "Combat", "All"));
   public final l3 j = this.addSetting(new l3("Inventory Header", "Show inventory title", true));
   public final lo J = this.addSetting(new lo("Radar Range", "Retired radar-widget preference", 47.0, 20.0, 80.0, 5.0));
   public final l3 e = this.addSetting(new l3("Radar Mobs", "Retired radar-widget preference", false));
   public final l3 v = this.addSetting(new l3("Keystrokes Mouse", "Show the left and right mouse buttons", true));
   public final l3 G = this.addSetting(new l3("Keystrokes Space", "Show the jump key", true));
   public final l3 n = this.addSetting(new l3("Keystrokes CPS", "Clicks per second under each mouse button", true));
   public final l3 s = this.addSetting(new l3("Keystrokes Arrows", "Label the movement keys with arrows instead of WASD", false));
   public final lo m = this.addSetting(new lo("Keystrokes Padding", "Space between the keys and the card edge", 5.0, 1.0, 10.0, 1.0));
   public final lo L = this.addSetting(new lo("Keystrokes BG Alpha", "Card background opacity, from 0 to 255", 150.0, 0.0, 255.0, 5.0));
   public final l3 t = this.addSetting(new l3("Keystrokes Shadow", "Soft shadow under the card", true));
   public final li z = this.addSetting(new li("Music Layout", "Music card layout", "Bottom", "Bottom", "Compact"));
   public final l3 y = this.addSetting(new l3("Music Progress", "Show track progress", true));
   public final l3 W = this.addSetting(new l3("Music Time", "Show track position and duration", true));
   public final li p = this.addSetting(new li("Armor Layout", "Equipment arrangement", "Horizontal", "Horizontal", "Vertical"));
   public final lo K = this.addSetting(new lo("Armor Spacing", "Space between equipment slots", 5.0, 0.0, 20.0, 1.0));
   public final li F = this.addSetting(new li("Armor Durability", "Durability labels", "Percent", "Off", "Percent", "Remaining"));
   public final l3 u = this.addSetting(new l3("Armor Bars", "Color-coded durability bars", true));
   public final l3 c = this.addSetting(new l3("Armor Background", "Draw equipment panel", false));
   public final l3 Q = this.addSetting(new l3("Armor Empty Slots", "Reserve spacing for empty slots without drawing placeholders", true));
   public final l3 o = this.addSetting(new l3("Armor Reverse", "Reverse equipment order", false));
   public final l3 Y = this.addSetting(new l3("Armor Held Items", "Include main hand and offhand", false));
   public final l0 Z = this.addSetting(new l0("Speed Header", "Speed graph header color and opacity", -1291845632));
   public final l0 V = this.addSetting(new l0("Speed Background", "Speed graph body color and opacity", -1291845632));
   public final l0 X = this.addSetting(new l0("Speed Line", "Speed graph curve color", -1841171));
   public final l3 M = this.addSetting(new l3("Speed Card Background", "Use the same dark background as the other HUD cards", true));
   private final l3 l = this.addSetting(new l3("Blur Panels", "Reuse one scene blur beneath HUD panels", false));
   private final l3 S = this.addSetting(new l3("Light Sweep", "Faint light band crossing every HUD panel", true));
   private final l3 N = this.addSetting(new l3("Panel Glow", "Accent gradient and edge light inside HUD panels", true));
   public final li b = this.addSetting(
      new li("Module List Style", "Card lists active modules in a titled panel; Rows keeps the stepped list", "Card", "Card", "Rows")
   );
   private static HUDModule E;

   public List<lb<?>> a() {
      return List.of(this.M, this.Z, this.V, this.X);
   }

   public List<lb<?>> c() {
      return List.of(this.v, this.G, this.n, this.s, this.m, this.L);
   }

   public List<lb<?>> l() {
      return List.of(this.h, this.H, this.A, this.g, this.x, this.T, this.q, this.O, this.k, this.R);
   }

   public List<lb<?>> n() {
      ArrayList var1 = new ArrayList<>(List.of(this.i, this.z, this.y, this.W));
      var1.addAll(List.of(this.d, this.J, this.e));
      var1.add(this.B);
      var1.add(this.t);
      return List.copyOf(var1);
   }

   public List<lb<?>> U() {
      return List.of(this.a, this.l, this.S, this.N);
   }

   public List<lb<?>> k(String var1) {
      List var2 = switch (var1) {
         case "speed" -> this.a();
         case "keybinds" -> List.of(this.C, this.l);
         case "inventory" -> List.of(this.j, this.l);
         case "cooldowns" -> List.of(this.w, this.l);
         case "staffs" -> List.of(this.l);
         case "keystrokes" -> {
            ArrayList var5 = new ArrayList<>(this.c());
            var5.add(this.l);
            yield List.copyOf(var5);
         }
         case "armor" -> List.of(this.p, this.K, this.F, this.u, this.c, this.Q, this.o, this.Y, this.l);
         case "watermark" -> List.of(this.r, this.U, this.D, this.f, this.I, this.l);
         case "modules" -> List.of(this.b, this.l);
         case "info", "effects", "target", "miner", "builder", "pilot", "hunter" -> List.of(this.l);
         default -> List.of();
      };
      return Stream.concat(Stream.of(this.a), var2.stream()).distinct().toList();
   }

   public boolean x() {
      return !this.isEnabled() ? false : this.isOperational() && this.q.getValue();
   }

   public boolean u(String var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isOperational()) {
         switch (var1) {
            case "inventory":
               if (!this.O.getValue()) {
                  return false;
               }
               break;
            case "cooldowns":
               if (!this.k.getValue()) {
                  return false;
               }
               break;
            case "keystrokes":
               if (!this.R.getValue()) {
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

   public boolean K() {
      return this.isOperational() && this.l.getValue();
   }

   public boolean G() {
      return this.S.getValue();
   }

   public boolean h() {
      return this.N.getValue();
   }

   public boolean w() {
      return this.b.G("Card");
   }

   public HUDModule() {
      super("HUD", "Choose on-screen information", Category.RENDER, -1);
      E = this;
      this.a.a(var0 -> {
         uw.W();
         q8.G("HUD", "HUD font changed to {}", var0);
      });

      for (lb var2 : List.of(this.r, this.U, this.D, this.f, this.I)) {
         var2.O(this.h::getValue);
      }

      this.C.O(this.g::getValue);
      this.b.O(this.H::getValue);
      this.w.O(this.k::getValue);
      this.j.O(this.O::getValue);

      for (lb var9 : this.c()) {
         var9.O(this.R::getValue);
      }

      this.n.O(() -> this.R.getValue() && this.v.getValue());

      for (lb var10 : this.n()) {
         var10.O(() -> false);
      }

      this.M.O(this.q::getValue);
      this.X.O(this.q::getValue);

      for (l0 var11 : List.of(this.Z, this.V)) {
         var11.O(() -> this.q.getValue() && !this.M.getValue());
      }

      for (lb var4 : new lb[]{this.p, this.K, this.F, this.u, this.c, this.Q, this.o, this.Y}) {
         var4.O(this.T::getValue);
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static HUDModule y() {
      return E;
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.isOperational() && this.h.getValue();
   }

   public boolean T() {
      return !this.isEnabled() ? false : this.isOperational() && this.H.getValue();
   }

   public boolean M() {
      return !this.isEnabled() ? false : this.isOperational() && this.A.getValue();
   }

   public boolean S() {
      return !this.isEnabled() ? false : this.isOperational() && this.g.getValue();
   }

   public boolean b() {
      return !this.isEnabled() ? false : this.isOperational() && this.x.getValue();
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.isOperational() && this.T.getValue();
   }
}

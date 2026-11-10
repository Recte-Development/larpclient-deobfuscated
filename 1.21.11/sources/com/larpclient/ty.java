package com.larpclient;

import com.larpclient.update.UpdateConsentKeys;
import com.larpclient.update.UpdateRuntime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_332;
import net.minecraft.class_3675;

final class ty extends t6 {
   private String u = "Interface";
   private boolean o;
   private final Map<lc, BooleanSupplier> U = new IdentityHashMap<>();
   private ls V;
   private ls J;
   private ls Y;
   private final UpdateConsentKeys Q = new UpdateConsentKeys();
   private final List<ty.u> q = new ArrayList<>();
   private final qb c = new qb(0.0);
   private k.l k;
   private final Map<ls, k.l> L = new LinkedHashMap<>();
   private com.larpclient.module.lo M;
   private com.larpclient.module.lo g;
   private com.larpclient.module.lo d;
   private com.larpclient.module.l3 t;
   private com.larpclient.module.li E;

   void A(k.l var1) {
      this.k = var1;
   }

   ty() {
      super("Interface");
   }

   boolean V() {
      return this.u.equals("HUD");
   }

   void C(String var1) {
      this.u = var1;
   }

   @Override
   protected void build() {
      this.o();
      this.q.clear();
      this.L.clear();
      this.U.clear();
      this.M = this.g = this.d = null;
      this.t = null;
      this.E = null;
      this.V = this.J = this.Y = null;
      this.v = this.u.equals("Interface") ? "GUI Settings" : this.u;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.S(var4.equals("Interface") ? "GUI Settings" : var4, this.u.equals(var4), () -> {
            if (!this.u.equals(var4)) {
               this.u = var4;
               this.f = 0;
               this.W();
               q8.G("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      switch (this.u) {
         case "Interface":
            this.m();
            break;
         case "HUD":
            this.v();
            break;
         case "Account":
            this.P();
            break;
         case "Updates":
            this.J();
            break;
         default:
            throw new IllegalStateException("Unknown settings section");
      }
   }

   private void P() {
      this.d("Minecraft", () -> this.X.c().method_1548().method_1676());
      this.d("Larp account", () -> "".isBlank() ? "Signed in" : "");
      this.u(new l5("Discord"));
      qk var1 = LarpClient.b().e();
      com.larpclient.module.l3 var2 = new com.larpclient.module.l3(
         "Discord activity", "Show Larp Client, your version and server on Discord. Needs the Discord desktop app", var1.F().G4
      );
      var2.a(var2x -> {
         if (!var1.M(var2x)) {
            var2.setValue(var1.F().G4);
         }
      });
      this.u(new l9(var2));
   }

   private void J() {
      this.Q.begin(this::Y);
      this.d("Version", () -> sc.v);
      this.d("New version", UpdateRuntime::offeredVersion);
      this.U.put(this.F.getLast(), () -> !UpdateRuntime.offeredVersion().isEmpty());
      this.d("Status", UpdateRuntime::status);
      y0 var1 = LarpClient.b().e().F();
      com.larpclient.module.l3 var2 = new com.larpclient.module.l3("Automatic checks", "Check for a new version after startup", var1.N);
      var2.a(var2x -> {
         if (!UpdateRuntime.automatic(var2x)) {
            var2.setValue(var1.N);
         }
      });
      this.u(new l9(var2));
      Object var3 = new Object();
      this.V = this.E(var3, UpdateRuntime::primary);
      this.J = this.E(var3, UpdateRuntime::secondary);
      this.Y = this.E(var3, UpdateRuntime::skip);
      this.Y.O = "Skip this release";
      this.U.put(this.Y, UpdateRuntime::skippable);
      this.C();
      this.o(UpdateRuntime::statusDetail);
      this.U.put(this.F.getLast(), () -> !UpdateRuntime.statusDetail().isEmpty());
      this.u(new l5("What's new", () -> !UpdateRuntime.releaseNotes().isEmpty()));
      this.o(UpdateRuntime::releaseNotes);
      this.U.put(this.F.getLast(), () -> !UpdateRuntime.releaseNotes().isEmpty());
   }

   private ls E(Object var1, Runnable var2) {
      ls var3 = new ls("", var2);
      var3.x = (boolean)1;
      var3.U = var1;
      var3.w = 24;
      this.u(var3);
      return var3;
   }

   private void C() {
      if (this.V != null) {
         this.V.O = UpdateRuntime.primaryLabel();
         this.V.N = UpdateRuntime.primaryEnabled();
         this.J.O = UpdateRuntime.secondaryLabel();
         this.J.N = UpdateRuntime.secondaryEnabled();
      }
   }

   private boolean Y(int var1) {
      return class_3675.method_15987(this.X.c().method_22683(), var1);
   }

   @Override
   protected boolean componentShown(lc var1) {
      BooleanSupplier var2 = this.U.get(var1);
      return (var2 == null || var2.getAsBoolean()) && super.componentShown(var1);
   }

   private void r(Runnable var1) {
      var1.run();
      LarpClient.b().e().g();
   }

   private void T(String var1, String var2, lc... var3) {
      l_ var4 = new l_(var1, var2);
      this.u(var4);

      for (lc var8 : var3) {
         this.u(var8);
      }

      this.q.add(new ty.u(var4, List.of(var3)));
   }

   private void m() {
      y0 var1 = LarpClient.b().e().F();
      this.T("Main color", "Choose the main GUI theme.", new lg(() -> var1.Gr, var2x -> this.r(() -> var1.Gr = var2x)));
      com.larpclient.module.l0 var2 = new com.larpclient.module.l0("Primary", "Primary theme color", ez.a);
      var2.a(var0 -> ez.a = var0);
      com.larpclient.module.l0 var3 = new com.larpclient.module.l0("Secondary", "Secondary theme color", ez.f);
      var3.a(var0 -> ez.f = var0);
      ls var4 = new ls("Reset palette", () -> {
         ez.a = -1381654;
         ez.f = -6250336;
         this.build();
      });
      var4.r = (boolean)1;
      com.larpclient.module.l3 var5 = new com.larpclient.module.l3("Animation gradient", "Animate the GUI accent between primary and secondary", var1.O);
      var5.a(var2x -> this.r(() -> var1.O = var2x));
      this.T("Accent color", "Accent colors of the GUI.", new l3(var1x -> {
         ez.a = var1x;
         var2.setValue(var1x);
      }), new l9(var2), new l9(var3), new l9(var5), var4);
      com.larpclient.module.li var6 = new com.larpclient.module.li("Open", "How the GUI appears", var1.K, sd.I.toArray(String[]::new));
      var6.a(var2x -> this.r(() -> var1.K = var2x));
      com.larpclient.module.li var7 = new com.larpclient.module.li(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.R,
         y0.h.toArray(String[]::new)
      );
      var7.a(var2x -> this.r(() -> var1.R = var2x));
      com.larpclient.module.lo var8 = new com.larpclient.module.lo(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", var1.G, 100.0, 1000.0, 10.0
      );
      var8.a(var2x -> this.r(() -> var1.G = var2x.intValue()));
      this.T("Animation", "GUI open/close animations.", new l9(var6), new l9(var7), new l9(var8));
      com.larpclient.module.lo var9 = new com.larpclient.module.lo("Opacity", "Window and navigation opacity", var1.G7 * 100.0, 0.0, 100.0, 1.0);
      var9.a(var2x -> this.r(() -> var1.G7 = var2x / 100.0));
      this.T("Transparent", "Make your GUI transparent.", new l9(var9));
      com.larpclient.module.li var10 = new com.larpclient.module.li(
         "Style", "What the GUI draws behind its window. Dim darkens the world without light", var1.L, y0.Y.toArray(String[]::new)
      );
      var10.a(var2x -> this.r(() -> var1.L = var2x));
      com.larpclient.module.lo var11 = new com.larpclient.module.lo(
         "Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.X * 100.0, 0.0, 90.0, 1.0
      );
      var11.a(var2x -> this.r(() -> var1.X = var2x / 100.0));
      com.larpclient.module.lo var12 = new com.larpclient.module.lo(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         var1.p,
         0.0,
         4.0,
         1.0
      );
      var12.a(var2x -> this.r(() -> var1.p = var2x.intValue()));
      com.larpclient.module.li var13 = new com.larpclient.module.li(
         "Blur area", "Blur only behind the window, or the whole world while the GUI is open", var1.T, y0.C.toArray(String[]::new)
      );
      var13.a(var2x -> this.r(() -> var1.T = var2x));
      this.T("Backdrop", "Shown behind the GUI.", new l9(var10), new l9(var11), new l9(var12), new lt(var13));
      com.larpclient.module.li var14 = new com.larpclient.module.li(
         "Cards", "Module cards: Filled, an Outline only, or Flat without a contour", var1.F, y0.v.toArray(String[]::new)
      );
      var14.a(var2x -> this.r(() -> var1.F = var2x));
      com.larpclient.module.li var15 = new com.larpclient.module.li(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.J,
         y0.b.toArray(String[]::new)
      );
      var15.a(var2x -> this.r(() -> var1.J = var2x));
      com.larpclient.module.li var16 = new com.larpclient.module.li("Switches", "Pill, Square or Minimal switches", var1.y, y0.V.toArray(String[]::new));
      var16.a(var2x -> this.r(() -> var1.y = var2x));
      com.larpclient.module.li var17 = new com.larpclient.module.li("Sliders", "Pill, Minimal or Classic sliders", var1.GE, y0.D.toArray(String[]::new));
      var17.a(var2x -> this.r(() -> var1.GE = var2x));
      com.larpclient.module.lo var18 = new com.larpclient.module.lo(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.P * 100.0, 0.0, 200.0, 5.0
      );
      var18.a(var2x -> this.r(() -> var1.P = var2x / 100.0));
      com.larpclient.module.l3 var19 = new com.larpclient.module.l3("Accent glow", "A soft accent glow breathing around the window", var1.S);
      var19.a(var2x -> this.r(() -> var1.S = var2x));
      com.larpclient.module.l3 var20 = new com.larpclient.module.l3("Ripple", "A ripple spreads from the pointer when you click a module card", var1.i);
      var20.a(var2x -> this.r(() -> var1.i = var2x));
      com.larpclient.module.l3 var21 = new com.larpclient.module.l3("Hover lift", "Module cards rise a little under the pointer", var1.g);
      var21.a(var2x -> this.r(() -> var1.g = var2x));
      this.T(
         "Style",
         "Look of cards, controls and effects.",
         new l9(var14),
         new l9(var15),
         new l9(var16),
         new l9(var17),
         new l9(var18),
         new l9(var19),
         new l9(var20),
         new l9(var21)
      );
      com.larpclient.module.lo var22 = new com.larpclient.module.lo("Intensity", "Glow strength", t_.V, 0.0, 2.0, 0.05);
      var22.a(var0 -> t_.V = var0.floatValue());
      com.larpclient.module.lo var23 = new com.larpclient.module.lo("Radius", "Halo spread", t_.f, 0.5, 3.0, 0.1);
      var23.a(var0 -> t_.f = var0.floatValue());
      this.T("Bloom", "Shared world glow.", new l9(var22), new l9(var23));
      LarpClient var24 = LarpClient.b();
      com.larpclient.module.lw var25 = new com.larpclient.module.lw(
            "ClickUI keybind",
            "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
            344
         )
         .P();
      var25.J(var24.V());
      var25.a(var24::n);
      com.larpclient.module.lo var26 = new com.larpclient.module.lo(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.f * 100.0, 50.0, 200.0, 5.0
      );
      var26.a(var2x -> this.r(() -> var1.f = var2x / 100.0));
      ls var27 = new ls("Reset scale", () -> var26.q(100.0));
      var27.r = (boolean)1;
      this.T("ClickUI", "Shortcut and size of the client interface.", new l9(var25), new l9(var26), var27);
      com.larpclient.module.l3 var28 = new com.larpclient.module.l3(
         "Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.A
      );
      var28.a(var2x -> {
         this.r(() -> var1.A = var2x);
         q8.G("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.T("Main menu", "Client title screen outside worlds.", new l9(var28));
      com.larpclient.module.l3 var29 = new com.larpclient.module.l3(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.M.Z
      );
      var29.a(var2x -> {
         this.r(() -> var1.M.Z = var2x);
         q8.G("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      com.larpclient.module.l0 var30 = new com.larpclient.module.l0(
         "Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.M.b
      );
      var30.a(var2x -> this.r(() -> var1.M.b = var2x));
      ls var31 = new ls("Reset panel layout", () -> this.r(var1.M::k));
      var31.r = (boolean)1;
      this.T("Legacy ClickGUI", "Classic panels with drop-down settings.", new l9(var29), new l9(var30), var31);
      if (!com.larpclient.module.lf.l()) {
         com.larpclient.module.l3 var32 = new com.larpclient.module.l3(
            "Legit mode", "Stop and hide everything the Legit edition does not have. Your modules come back when you switch it off with the key below", false
         );
         var32.a(var1x -> {
            if (var1x) {
               var24.V(true);
            }
         });
         com.larpclient.module.lw var33 = new com.larpclient.module.lw(
               "Switch key", "Press this key three times quickly to switch Legit mode on or off. Keyboard keys only; clearing it restores /", var1.e
            )
            .P();
         var33.a(var3x -> {
            if (s5.D(var3x)) {
               this.r(() -> var1.e = var3x);
            } else {
               var33.J(47);
            }
         });
         this.T("Legit mode", "Play as the Legit edition without changing clients.", new l9(var32), new l9(var33));
      }
   }

   private static int f(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      }

      return switch (var0) {
         case 0, 5, 8 -> 0;
         case 1, 4, 6, 9 -> 1;
         default -> 2;
      };
   }

   private static int q(int[] var0) {
      int var1 = 0;

      for (int var2 = 1; var2 < var0.length; var2++) {
         if (var0[var2] < var0[var1]) {
            var1 = var2;
         }
      }

      return var1;
   }

   @Override
   protected boolean layoutCustomContent() {
      if (this.u.equals("Interface") && !this.q.isEmpty()) {
         int var1 = this.A - this.C - 16;
         int var2 = var1 >= 470 ? 3 : (var1 >= 320 ? 2 : 1);
         int[] var3 = new int[var2];
         int[] var4 = new int[var2];
         if (var2 == 3) {
            var4[0] = (var1 - 16) * 31 / 100;
            var4[1] = (var1 - 16) * 38 / 100;
            var4[2] = var1 - 16 - var4[0] - var4[1];
         } else {
            Arrays.fill(var4, Math.max(1, (var1 - 8 * (var2 - 1)) / var2));
         }

         this.R = 0;
         int[] var5 = new int[this.q.size()];

         for (int var6 = 0; var6 < this.q.size(); var6++) {
            ty.u var7 = this.q.get(var6);
            int var8 = var2 == 3 ? f(var6, var2) : q(var3);
            var5[var6] = var8;
            var7.panel.w = 40 + var7.controls.stream().mapToInt(var0 -> var0.w + 4).sum();
            var3[var8] += var7.panel.w + 8;
            this.R = Math.max(this.R, var3[var8] - 8);
         }

         this.f = Math.clamp(this.f, 0, Math.max(0, this.R - (this.l - this.i)));
         int var16 = (int)Math.round(this.c.a(this.f, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.q.size(); var17++) {
            ty.u var18 = this.q.get(var17);
            int var9 = var5[var17];
            int var10 = this.C + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.i + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (lc var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof l9 var15) {
                  var15.Z(this.r.top() + 12, this.l);
               }

               var12 += var14.w + 4;
            }

            var3[var9] += var18.panel.w + 8;
         }

         return true;
      } else {
         return false;
      }
   }

   private void v() {
      this.v = this.k == null ? "HUD overlays" : "HUD / " + com.larpclient.k.L(this.k.s);
      if (this.k == null) {
         this.s("Open HUD editor", () -> this.X.R());
         this.text("Use Editor for placement and previews. Chat dragging remains available.");

         for (k.l var8 : com.larpclient.k.A()) {
            ls var11 = this.s(com.larpclient.k.L(var8.s), () -> {
               this.k = var8;
               this.f = 0;
               this.W();
               this.build();
            });
            var11.T = com.larpclient.k.L(var8);
            var11.j = (boolean)1;
            var11.w = 34;
            var11.W = com.larpclient.k.B(var8);
            var11.Y = () -> {
               com.larpclient.k.F(var8, !com.larpclient.k.B(var8));
               q8.v("HUD editor", "{} visibility={}", var8.s, com.larpclient.k.B(var8));
            };
            this.L.put(var11, var8);
         }

         this.u(new l5("Placement guides"));
         com.larpclient.module.l3 var6 = new com.larpclient.module.l3("Snap", "Snap to viewport edges and center while dragging", hz.l.snapping());
         var6.a(var0 -> hz.l = new hz.j(hz.l.panel(), hz.l.collapsed(), var0, hz.l.grid()));
         this.u(new l9(var6));
         com.larpclient.module.l3 var9 = new com.larpclient.module.l3("8px Grid", "Optional grid; Alt bypasses grid and snapping", hz.l.grid());
         var9.a(var0 -> hz.l = new hz.j(hz.l.panel(), hz.l.collapsed(), hz.l.snapping(), var0));
         this.u(new l9(var9));
      } else {
         k.l var1 = this.k;
         var1.O(this.field_22789, this.field_22790);
         this.s("All HUD overlays", () -> {
            this.k = null;
            this.f = 0;
            this.W();
            this.build();
         });
         this.text(com.larpclient.k.L(var1) + ". Visibility restores the required module and content toggle.");
         this.t = new com.larpclient.module.l3("Visible", "Hide only this overlay; restoring enables its content", com.larpclient.k.B(var1));
         this.t.a(var2 -> {
            if (!this.o) {
               com.larpclient.k.F(var1, var2);
            }
         });
         this.M = new com.larpclient.module.lo("X", "Layout origin in GUI pixels", var1.l(this.field_22789), 0.0, this.field_22789, 1.0);
         this.g = new com.larpclient.module.lo("Y", "Layout origin in GUI pixels", var1.M(this.field_22790), 0.0, this.field_22790, 1.0);
         this.M.a(var2 -> {
            if (!this.o) {
               var1.e(var2.intValue(), var1.M(this.field_22790), this.field_22789, this.field_22790);
            }
         });
         this.g.a(var2 -> {
            if (!this.o) {
               var1.e(var1.l(this.field_22789), var2.intValue(), this.field_22789, this.field_22790);
            }
         });
         this.d = new com.larpclient.module.lo("Scale", "Uniform content scale", var1.S(), 0.5, 2.5, 0.05);
         this.d.a(var2 -> {
            if (!this.o) {
               var1.V(var2);
            }
         });
         this.E = new com.larpclient.module.li(
            "Alignment",
            "Normalized screen anchor",
            G(var1),
            "Free",
            "Top left",
            "Top center",
            "Top right",
            "Center left",
            "Center",
            "Center right",
            "Bottom left",
            "Bottom center",
            "Bottom right"
         );
         this.E.a(var2 -> {
            if (!this.o && !var2.equals("Free")) {
               int var3x = this.E.e().indexOf(var2) - 1;
               hz.J.put(var1.s, new hz.v(var3x % 3 * 0.5, var3x / 3 * 0.5));
            }
         });
         this.u(new l5("Placement"));

         for (com.larpclient.module.lb var3 : List.of(this.t, this.M, this.g, this.d, this.E)) {
            this.u(new l9(var3));
         }

         List var7 = com.larpclient.k.J(var1);
         if (!var7.isEmpty()) {
            this.u(new l5("Appearance"));
         }

         for (com.larpclient.module.lb var4 : var7) {
            this.u(new l9(var4, true));
         }

         if (var1.s.equals("crosshair")) {
            this.s("Center crosshair", () -> {
               var1.v();
               this.build();
            });
         }

         this.s("Reset placement", () -> {
            var1.x();
            this.build();
         });
      }
   }

   private static String G(k.l var0) {
      hz.v var1 = hz.J.get(var0.s);
      if (var1 == null) {
         return var0.s.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? List.of("Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right")
            .get(var3 * 3 + var2)
         : "Free";
   }

   @Override
   protected void renderExtra(class_332 var1, int var2, int var3) {
      if (this.u.equals("Updates")) {
         this.Q.reconcile(this::Y);
      }

      for (Entry var5 : this.L.entrySet()) {
         ((ls)var5.getKey()).W = com.larpclient.k.B((k.l)var5.getValue());
         ((ls)var5.getKey()).T = com.larpclient.k.L((k.l)var5.getValue());
      }

      if (this.k != null && this.M != null) {
         this.o = (boolean)1;

         try {
            this.M.q((double)this.k.l(this.field_22789));
            this.g.q((double)this.k.M(this.field_22790));
            this.d.q(this.k.S());
            this.t.setValue(com.larpclient.k.B(this.k));
            this.E.setValue(G(this.k));
         } finally {
            this.o = (boolean)0;
         }
      }
   }

   @Override
   protected void layout() {
      this.C();
      super.layout();
      this.A = this.u.equals("Interface") ? this.r.right() : Math.min(this.r.right(), this.C + 440);
      this.q();
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      return !var2
            || this.V == null
            || !this.V.contains(var1.comp_4798(), var1.comp_4799())
               && !this.J.contains(var1.comp_4798(), var1.comp_4799())
               && !this.Y.contains(var1.comp_4798(), var1.comp_4799())
         ? super.method_25402(var1, var2)
         : true;
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      return this.u.equals("Updates") && !this.Q.press(var1.comp_4795()) ? true : super.method_25404(var1);
   }

   private record u(l_ panel, List<lc> controls) {
   }
}

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
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

final class fg extends fp {
   private String t = "Interface";
   private boolean Y;
   private final Map<j0, BooleanSupplier> S = new IdentityHashMap<>();
   private s8 l;
   private s8 m;
   private s8 Q;
   private final UpdateConsentKeys D = new UpdateConsentKeys();
   private final List<fg.v> H = new ArrayList<>();
   private final fq u = new fq(0.0);
   private dk.q W;
   private final Map<s8, dk.q> O = new LinkedHashMap<>();
   private com.larpclient.module.b a;
   private com.larpclient.module.b w;
   private com.larpclient.module.b x;
   private com.larpclient.module.m e;
   private com.larpclient.module.o h;

   void f(dk.q var1) {
      this.W = var1;
   }

   fg() {
      super("Interface");
   }

   boolean E() {
      return this.t.equals("HUD");
   }

   void R(String var1) {
      this.t = var1;
   }

   @Override
   protected void build() {
      this.D();
      this.H.clear();
      this.O.clear();
      this.S.clear();
      this.a = this.w = this.x = null;
      this.e = null;
      this.h = null;
      this.l = this.m = this.Q = null;
      this.j = this.t.equals("Interface") ? "GUI Settings" : this.t;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.f(var4.equals("Interface") ? "GUI Settings" : var4, this.t.equals(var4), () -> {
            if (!this.t.equals(var4)) {
               this.t = var4;
               this.L = 0;
               this.C();
               _q.z("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      switch (this.t) {
         case "Interface":
            this.K();
            break;
         case "HUD":
            this.A();
            break;
         case "Account":
            this.X();
            break;
         case "Updates":
            this.B();
            break;
         default:
            throw new IllegalStateException("Unknown settings section");
      }
   }

   private void X() {
      this.i("Minecraft", () -> this.T.a().getUser().getName());
      this.i("Larp account", () -> "".isBlank() ? "Signed in" : "");
      this.f(new sj("Discord"));
      aq var1 = LarpClient.N().h();
      com.larpclient.module.m var2 = new com.larpclient.module.m(
         "Discord activity", "Show Larp Client, your version and server on Discord. Needs the Discord desktop app", var1.k().f
      );
      var2.w(var2x -> {
         if (!var1.l(var2x)) {
            var2.setValue(var1.k().f);
         }
      });
      this.f(new su(var2));
   }

   private void B() {
      this.D.begin(this::b);
      this.i("Version", () -> a0.r);
      this.i("New version", UpdateRuntime::offeredVersion);
      this.S.put(this.V.getLast(), () -> !UpdateRuntime.offeredVersion().isEmpty());
      this.i("Status", UpdateRuntime::status);
      i3 var1 = LarpClient.N().h().k();
      com.larpclient.module.m var2 = new com.larpclient.module.m("Automatic checks", "Check for a new version after startup", var1.N);
      var2.w(var2x -> {
         if (!UpdateRuntime.automatic(var2x)) {
            var2.setValue(var1.N);
         }
      });
      this.f(new su(var2));
      Object var3 = new Object();
      this.l = this.G(var3, UpdateRuntime::primary);
      this.m = this.G(var3, UpdateRuntime::secondary);
      this.Q = this.G(var3, UpdateRuntime::skip);
      this.Q.W = "Skip this release";
      this.S.put(this.Q, UpdateRuntime::skippable);
      this.R();
      this.n(UpdateRuntime::statusDetail);
      this.S.put(this.V.getLast(), () -> !UpdateRuntime.statusDetail().isEmpty());
      this.f(new sj("What's new", () -> !UpdateRuntime.releaseNotes().isEmpty()));
      this.n(UpdateRuntime::releaseNotes);
      this.S.put(this.V.getLast(), () -> !UpdateRuntime.releaseNotes().isEmpty());
   }

   private s8 G(Object var1, Runnable var2) {
      s8 var3 = new s8("", var2);
      var3.Z = (boolean)1;
      var3.l = var1;
      var3.O = 24;
      this.f(var3);
      return var3;
   }

   private void R() {
      if (this.l != null) {
         this.l.W = UpdateRuntime.primaryLabel();
         this.l.V = UpdateRuntime.primaryEnabled();
         this.m.W = UpdateRuntime.secondaryLabel();
         this.m.V = UpdateRuntime.secondaryEnabled();
      }
   }

   private boolean b(int var1) {
      return j2.U(this.T.a().getWindow(), var1);
   }

   @Override
   protected boolean componentShown(j0 var1) {
      BooleanSupplier var2 = this.S.get(var1);
      return (var2 == null || var2.getAsBoolean()) && super.componentShown(var1);
   }

   private void P(Runnable var1) {
      var1.run();
      LarpClient.N().h().c();
   }

   private void T(String var1, String var2, j0... var3) {
      ss var4 = new ss(var1, var2);
      this.f(var4);

      for (j0 var8 : var3) {
         this.f(var8);
      }

      this.H.add(new fg.v(var4, List.of(var3)));
   }

   private void K() {
      i3 var1 = LarpClient.N().h().k();
      this.T("Main color", "Choose the main GUI theme.", new sg(() -> var1.z, var2x -> this.P(() -> var1.z = var2x)));
      com.larpclient.module.h var2 = new com.larpclient.module.h("Primary", "Primary theme color", com.larpclient.c.y);
      var2.w(var0 -> com.larpclient.c.y = var0);
      com.larpclient.module.h var3 = new com.larpclient.module.h("Secondary", "Secondary theme color", com.larpclient.c.t);
      var3.w(var0 -> com.larpclient.c.t = var0);
      s8 var4 = new s8("Reset palette", () -> {
         com.larpclient.c.y = -1381654;
         com.larpclient.c.t = -6250336;
         this.build();
      });
      var4.D = (boolean)1;
      com.larpclient.module.m var5 = new com.larpclient.module.m("Animation gradient", "Animate the GUI accent between primary and secondary", var1.b);
      var5.w(var2x -> this.P(() -> var1.b = var2x));
      this.T("Accent color", "Accent colors of the GUI.", new s4(var1x -> {
         com.larpclient.c.y = var1x;
         var2.setValue(var1x);
      }), new su(var2), new su(var3), new su(var5), var4);
      com.larpclient.module.o var6 = new com.larpclient.module.o("Open", "How the GUI appears", var1.p, wv.D.toArray(String[]::new));
      var6.w(var2x -> this.P(() -> var1.p = var2x));
      com.larpclient.module.o var7 = new com.larpclient.module.o(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.q,
         i3.y.toArray(String[]::new)
      );
      var7.w(var2x -> this.P(() -> var1.q = var2x));
      com.larpclient.module.b var8 = new com.larpclient.module.b(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", var1.n, 100.0, 1000.0, 10.0
      );
      var8.w(var2x -> this.P(() -> var1.n = var2x.intValue()));
      this.T("Animation", "GUI open/close animations.", new su(var6), new su(var7), new su(var8));
      com.larpclient.module.b var9 = new com.larpclient.module.b("Opacity", "Window and navigation opacity", var1.j * 100.0, 0.0, 100.0, 1.0);
      var9.w(var2x -> this.P(() -> var1.j = var2x / 100.0));
      this.T("Transparent", "Make your GUI transparent.", new su(var9));
      com.larpclient.module.o var10 = new com.larpclient.module.o(
         "Style", "What the GUI draws behind its window. Dim darkens the world without light", var1.Ju, i3.J0.toArray(String[]::new)
      );
      var10.w(var2x -> this.P(() -> var1.Ju = var2x));
      com.larpclient.module.b var11 = new com.larpclient.module.b(
         "Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.Z * 100.0, 0.0, 90.0, 1.0
      );
      var11.w(var2x -> this.P(() -> var1.Z = var2x / 100.0));
      com.larpclient.module.b var12 = new com.larpclient.module.b(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         var1.F,
         0.0,
         4.0,
         1.0
      );
      var12.w(var2x -> this.P(() -> var1.F = var2x.intValue()));
      com.larpclient.module.o var13 = new com.larpclient.module.o(
         "Blur area", "Blur only behind the window, or the whole world while the GUI is open", var1.i, i3.X.toArray(String[]::new)
      );
      var13.w(var2x -> this.P(() -> var1.i = var2x));
      this.T("Backdrop", "Shown behind the GUI.", new su(var10), new su(var11), new su(var12), new sp(var13));
      com.larpclient.module.o var14 = new com.larpclient.module.o(
         "Cards", "Module cards: Filled, an Outline only, or Flat without a contour", var1.R, i3.g.toArray(String[]::new)
      );
      var14.w(var2x -> this.P(() -> var1.R = var2x));
      com.larpclient.module.o var15 = new com.larpclient.module.o(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.J,
         i3.o.toArray(String[]::new)
      );
      var15.w(var2x -> this.P(() -> var1.J = var2x));
      com.larpclient.module.o var16 = new com.larpclient.module.o("Switches", "Pill, Square or Minimal switches", var1.S, i3.H.toArray(String[]::new));
      var16.w(var2x -> this.P(() -> var1.S = var2x));
      com.larpclient.module.o var17 = new com.larpclient.module.o("Sliders", "Pill, Minimal or Classic sliders", var1.a, i3.JO.toArray(String[]::new));
      var17.w(var2x -> this.P(() -> var1.a = var2x));
      com.larpclient.module.b var18 = new com.larpclient.module.b(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.K * 100.0, 0.0, 200.0, 5.0
      );
      var18.w(var2x -> this.P(() -> var1.K = var2x / 100.0));
      com.larpclient.module.m var19 = new com.larpclient.module.m("Accent glow", "A soft accent glow breathing around the window", var1.JH);
      var19.w(var2x -> this.P(() -> var1.JH = var2x));
      com.larpclient.module.m var20 = new com.larpclient.module.m("Ripple", "A ripple spreads from the pointer when you click a module card", var1.d);
      var20.w(var2x -> this.P(() -> var1.d = var2x));
      com.larpclient.module.m var21 = new com.larpclient.module.m("Hover lift", "Module cards rise a little under the pointer", var1.E);
      var21.w(var2x -> this.P(() -> var1.E = var2x));
      this.T(
         "Style",
         "Look of cards, controls and effects.",
         new su(var14),
         new su(var15),
         new su(var16),
         new su(var17),
         new su(var18),
         new su(var19),
         new su(var20),
         new su(var21)
      );
      com.larpclient.module.b var22 = new com.larpclient.module.b("Intensity", "Glow strength", nl.b, 0.0, 2.0, 0.05);
      var22.w(var0 -> nl.b = var0.floatValue());
      com.larpclient.module.b var23 = new com.larpclient.module.b("Radius", "Halo spread", nl.C, 0.5, 3.0, 0.1);
      var23.w(var0 -> nl.C = var0.floatValue());
      this.T("Bloom", "Shared world glow.", new su(var22), new su(var23));
      LarpClient var24 = LarpClient.N();
      com.larpclient.module.x var25 = new com.larpclient.module.x(
            "ClickUI keybind",
            "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
            344
         )
         .t();
      var25.b(var24.t());
      var25.w(var24::M);
      com.larpclient.module.b var26 = new com.larpclient.module.b(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.U * 100.0, 50.0, 200.0, 5.0
      );
      var26.w(var2x -> this.P(() -> var1.U = var2x / 100.0));
      s8 var27 = new s8("Reset scale", () -> var26.O(100.0));
      var27.D = (boolean)1;
      this.T("ClickUI", "Shortcut and size of the client interface.", new su(var25), new su(var26), var27);
      com.larpclient.module.m var28 = new com.larpclient.module.m(
         "Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.M
      );
      var28.w(var2x -> {
         this.P(() -> var1.M = var2x);
         _q.z("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.T("Main menu", "Client title screen outside worlds.", new su(var28));
      com.larpclient.module.m var29 = new com.larpclient.module.m(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.u.U
      );
      var29.w(var2x -> {
         this.P(() -> var1.u.U = var2x);
         _q.z("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      com.larpclient.module.h var30 = new com.larpclient.module.h("Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.u.h);
      var30.w(var2x -> this.P(() -> var1.u.h = var2x));
      s8 var31 = new s8("Reset panel layout", () -> this.P(var1.u::l));
      var31.D = (boolean)1;
      this.T("Legacy ClickGUI", "Classic panels with drop-down settings.", new su(var29), new su(var30), var31);
      if (!com.larpclient.module.fj.r()) {
         com.larpclient.module.m var32 = new com.larpclient.module.m(
            "Legit mode", "Stop and hide everything the Legit edition does not have. Your modules come back when you switch it off with the key below", false
         );
         var32.w(var1x -> {
            if (var1x) {
               var24.c(true);
            }
         });
         com.larpclient.module.x var33 = new com.larpclient.module.x(
               "Switch key", "Press this key three times quickly to switch Legit mode on or off. Keyboard keys only; clearing it restores /", var1.D
            )
            .t();
         var33.w(var3x -> {
            if (p4.z(var3x)) {
               this.P(() -> var1.D = var3x);
            } else {
               var33.b(47);
            }
         });
         this.T("Legit mode", "Play as the Legit edition without changing clients.", new su(var32), new su(var33));
      }
   }

   private static int W(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      }

      return switch (var0) {
         case 0, 5, 8 -> 0;
         case 1, 4, 6, 9 -> 1;
         default -> 2;
      };
   }

   private static int M(int[] var0) {
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
      if (this.t.equals("Interface") && !this.H.isEmpty()) {
         int var1 = this.p - this.E - 16;
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

         this.k = 0;
         int[] var5 = new int[this.H.size()];

         for (int var6 = 0; var6 < this.H.size(); var6++) {
            fg.v var7 = this.H.get(var6);
            int var8 = var2 == 3 ? W(var6, var2) : M(var3);
            var5[var6] = var8;
            var7.panel.O = 40 + var7.controls.stream().mapToInt(var0 -> var0.O + 4).sum();
            var3[var8] += var7.panel.O + 8;
            this.k = Math.max(this.k, var3[var8] - 8);
         }

         this.L = Math.clamp(this.L, 0, Math.max(0, this.k - (this.r - this.c)));
         int var16 = (int)Math.round(this.u.n(this.L, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.H.size(); var17++) {
            fg.v var18 = this.H.get(var17);
            int var9 = var5[var17];
            int var10 = this.E + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.c + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (j0 var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof su var15) {
                  var15.B(this.G.top() + 12, this.r);
               }

               var12 += var14.O + 4;
            }

            var3[var9] += var18.panel.O + 8;
         }

         return true;
      } else {
         return false;
      }
   }

   private void A() {
      this.j = this.W == null ? "HUD overlays" : "HUD / " + dk.m(this.W.d);
      if (this.W == null) {
         this.t("Open HUD editor", () -> this.T.j());
         this.text("Use Editor for placement and previews. Chat dragging remains available.");

         for (dk.q var8 : dk.X()) {
            s8 var11 = this.t(dk.m(var8.d), () -> {
               this.W = var8;
               this.L = 0;
               this.C();
               this.build();
            });
            var11.z = dk.x(var8);
            var11.b = (boolean)1;
            var11.O = 34;
            var11.G = dk.k(var8);
            var11.o = () -> {
               dk.I(var8, !dk.k(var8));
               _q.c("HUD editor", "{} visibility={}", var8.d, dk.k(var8));
            };
            this.O.put(var11, var8);
         }

         this.f(new sj("Placement guides"));
         com.larpclient.module.m var6 = new com.larpclient.module.m("Snap", "Snap to viewport edges and center while dragging", bh.z.snapping());
         var6.w(var0 -> bh.z = new bh.f(bh.z.panel(), bh.z.collapsed(), var0, bh.z.grid()));
         this.f(new su(var6));
         com.larpclient.module.m var9 = new com.larpclient.module.m("8px Grid", "Optional grid; Alt bypasses grid and snapping", bh.z.grid());
         var9.w(var0 -> bh.z = new bh.f(bh.z.panel(), bh.z.collapsed(), bh.z.snapping(), var0));
         this.f(new su(var9));
      } else {
         dk.q var1 = this.W;
         var1.s(this.width, this.height);
         this.t("All HUD overlays", () -> {
            this.W = null;
            this.L = 0;
            this.C();
            this.build();
         });
         this.text(dk.x(var1) + ". Visibility restores the required module and content toggle.");
         this.e = new com.larpclient.module.m("Visible", "Hide only this overlay; restoring enables its content", dk.k(var1));
         this.e.w(var2 -> {
            if (!this.Y) {
               dk.I(var1, var2);
            }
         });
         this.a = new com.larpclient.module.b("X", "Layout origin in GUI pixels", var1.f(this.width), 0.0, this.width, 1.0);
         this.w = new com.larpclient.module.b("Y", "Layout origin in GUI pixels", var1.p(this.height), 0.0, this.height, 1.0);
         this.a.w(var2 -> {
            if (!this.Y) {
               var1.o(var2.intValue(), var1.p(this.height), this.width, this.height);
            }
         });
         this.w.w(var2 -> {
            if (!this.Y) {
               var1.o(var1.f(this.width), var2.intValue(), this.width, this.height);
            }
         });
         this.x = new com.larpclient.module.b("Scale", "Uniform content scale", var1.Z(), 0.5, 2.5, 0.05);
         this.x.w(var2 -> {
            if (!this.Y) {
               var1.m(var2);
            }
         });
         this.h = new com.larpclient.module.o(
            "Alignment",
            "Normalized screen anchor",
            P(var1),
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
         this.h.w(var2 -> {
            if (!this.Y && !var2.equals("Free")) {
               int var3x = this.h.j().indexOf(var2) - 1;
               bh.Z.put(var1.d, new bh.u(var3x % 3 * 0.5, var3x / 3 * 0.5));
            }
         });
         this.f(new sj("Placement"));

         for (com.larpclient.module.r var3 : List.of(this.e, this.a, this.w, this.x, this.h)) {
            this.f(new su(var3));
         }

         List var7 = dk.K(var1);
         if (!var7.isEmpty()) {
            this.f(new sj("Appearance"));
         }

         for (com.larpclient.module.r var4 : var7) {
            this.f(new su(var4, true));
         }

         if (var1.d.equals("crosshair")) {
            this.t("Center crosshair", () -> {
               var1.m();
               this.build();
            });
         }

         this.t("Reset placement", () -> {
            var1.O();
            this.build();
         });
      }
   }

   private static String P(dk.q var0) {
      bh.u var1 = bh.Z.get(var0.d);
      if (var1 == null) {
         return var0.d.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.x() * 2.0);
      int var3 = (int)Math.round(var1.y() * 2.0);
      return Math.abs(var1.x() - var2 * 0.5) < 0.001 && Math.abs(var1.y() - var3 * 0.5) < 0.001
         ? List.of("Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right")
            .get(var3 * 3 + var2)
         : "Free";
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.t.equals("Updates")) {
         this.D.reconcile(this::b);
      }

      for (Entry var5 : this.O.entrySet()) {
         ((s8)var5.getKey()).G = dk.k((dk.q)var5.getValue());
         ((s8)var5.getKey()).z = dk.x((dk.q)var5.getValue());
      }

      if (this.W != null && this.a != null) {
         this.Y = (boolean)1;

         try {
            this.a.O((double)this.W.f(this.width));
            this.w.O((double)this.W.p(this.height));
            this.x.O(this.W.Z());
            this.e.setValue(dk.k(this.W));
            this.h.setValue(P(this.W));
         } finally {
            this.Y = (boolean)0;
         }
      }
   }

   @Override
   protected void layout() {
      this.R();
      super.layout();
      this.p = this.t.equals("Interface") ? this.G.right() : Math.min(this.G.right(), this.E + 440);
      this.T();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !var2 || this.l == null || !this.l.contains(var1.x(), var1.y()) && !this.m.contains(var1.x(), var1.y()) && !this.Q.contains(var1.x(), var1.y())
         ? super.mouseClicked(var1, var2)
         : true;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return this.t.equals("Updates") && !this.D.press(j2.P(var1)) ? true : super.keyPressed(var1);
   }

   private record v(ss panel, List<j0> controls) {
   }
}

package com.larpclient;

import com.larpclient.update.UpdateConsentKeys;
import com.larpclient.update.UpdateRuntime;
import com.mojang.blaze3d.platform.InputConstants;
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

final class hy extends h6 {
   private String X = "Interface";
   private boolean f;
   private final Map<jd, BooleanSupplier> K = new IdentityHashMap<>();
   private jx c;
   private jx v;
   private jx P;
   private final UpdateConsentKeys B = new UpdateConsentKeys();
   private final List<hy.e> t = new ArrayList<>();
   private final jp Q = new jp(0.0);
   private m4.p e;
   private final Map<jx, m4.p> T = new LinkedHashMap<>();
   private com.larpclient.module.e A;
   private com.larpclient.module.e Z;
   private com.larpclient.module.e n;
   private com.larpclient.module.s D;
   private com.larpclient.module.l i;

   void y(m4.p var1) {
      this.e = var1;
   }

   hy() {
      super("Interface");
   }

   boolean h() {
      return this.X.equals("HUD");
   }

   void p(String var1) {
      this.X = var1;
   }

   @Override
   protected void build() {
      this.v();
      this.t.clear();
      this.T.clear();
      this.K.clear();
      this.A = this.Z = this.n = null;
      this.D = null;
      this.i = null;
      this.c = this.v = this.P = null;
      this.m = this.X.equals("Interface") ? "GUI Settings" : this.X;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.O(var4.equals("Interface") ? "GUI Settings" : var4, this.X.equals(var4), () -> {
            if (!this.X.equals(var4)) {
               this.X = var4;
               this.Y = 0;
               this.C();
               com.larpclient.h.A("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      switch (this.X) {
         case "Interface":
            this.o();
            break;
         case "HUD":
            this.n();
            break;
         case "Account":
            this.Q();
            break;
         case "Updates":
            this.z();
            break;
         default:
            throw new IllegalStateException("Unknown settings section");
      }
   }

   private void Q() {
      this.T("Minecraft", () -> this.O.H().getUser().getName());
      this.T("Larp account", () -> "".isBlank() ? "Signed in" : "");
      this.h(new j1("Discord"));
      gg var1 = LarpClient.R().o();
      com.larpclient.module.s var2 = new com.larpclient.module.s(
         "Discord activity", "Show Larp Client, your version and server on Discord. Needs the Discord desktop app", var1.g().w
      );
      var2.o(var2x -> {
         if (!var1.A(var2x)) {
            var2.setValue(var1.g().w);
         }
      });
      this.h(new ju(var2));
   }

   private void z() {
      this.B.begin(this::s);
      this.T("Version", () -> z0.f);
      this.T("New version", UpdateRuntime::offeredVersion);
      this.K.put(this.x.getLast(), () -> !UpdateRuntime.offeredVersion().isEmpty());
      this.T("Status", UpdateRuntime::status);
      hb var1 = LarpClient.R().o().g();
      com.larpclient.module.s var2 = new com.larpclient.module.s("Automatic checks", "Check for a new version after startup", var1.C);
      var2.o(var2x -> {
         if (!UpdateRuntime.automatic(var2x)) {
            var2.setValue(var1.C);
         }
      });
      this.h(new ju(var2));
      Object var3 = new Object();
      this.c = this.x(var3, UpdateRuntime::primary);
      this.v = this.x(var3, UpdateRuntime::secondary);
      this.P = this.x(var3, UpdateRuntime::skip);
      this.P.K = "Skip this release";
      this.K.put(this.P, UpdateRuntime::skippable);
      this.h();
      this.Y(UpdateRuntime::statusDetail);
      this.K.put(this.x.getLast(), () -> !UpdateRuntime.statusDetail().isEmpty());
      this.h(new j1("What's new", () -> !UpdateRuntime.releaseNotes().isEmpty()));
      this.Y(UpdateRuntime::releaseNotes);
      this.K.put(this.x.getLast(), () -> !UpdateRuntime.releaseNotes().isEmpty());
   }

   private jx x(Object var1, Runnable var2) {
      jx var3 = new jx("", var2);
      var3.G = (boolean)1;
      var3.r = var1;
      var3.J = 24;
      this.h(var3);
      return var3;
   }

   private void h() {
      if (this.c != null) {
         this.c.K = UpdateRuntime.primaryLabel();
         this.c.p = UpdateRuntime.primaryEnabled();
         this.v.K = UpdateRuntime.secondaryLabel();
         this.v.p = UpdateRuntime.secondaryEnabled();
      }
   }

   private boolean s(int var1) {
      return InputConstants.isKeyDown(this.O.H().getWindow(), var1);
   }

   @Override
   protected boolean componentShown(jd var1) {
      BooleanSupplier var2 = this.K.get(var1);
      return (var2 == null || var2.getAsBoolean()) && super.componentShown(var1);
   }

   private void j(Runnable var1) {
      var1.run();
      LarpClient.R().o().W();
   }

   private void f(String var1, String var2, jd... var3) {
      jj var4 = new jj(var1, var2);
      this.h(var4);

      for (jd var8 : var3) {
         this.h(var8);
      }

      this.t.add(new hy.e(var4, List.of(var3)));
   }

   private void o() {
      hb var1 = LarpClient.R().o().g();
      this.f("Main color", "Choose the main GUI theme.", new jn(() -> var1.f, var2x -> this.j(() -> var1.f = var2x)));
      com.larpclient.module.i var2 = new com.larpclient.module.i("Primary", "Primary theme color", ho.J);
      var2.o(var0 -> ho.J = var0);
      com.larpclient.module.i var3 = new com.larpclient.module.i("Secondary", "Secondary theme color", ho.ds);
      var3.o(var0 -> ho.ds = var0);
      jx var4 = new jx("Reset palette", () -> {
         ho.J = -1381654;
         ho.ds = -6250336;
         this.build();
      });
      var4.y = (boolean)1;
      com.larpclient.module.s var5 = new com.larpclient.module.s("Animation gradient", "Animate the GUI accent between primary and secondary", var1.W);
      var5.o(var2x -> this.j(() -> var1.W = var2x));
      this.f("Accent color", "Accent colors of the GUI.", new jw(var1x -> {
         ho.J = var1x;
         var2.setValue(var1x);
      }), new ju(var2), new ju(var3), new ju(var5), var4);
      com.larpclient.module.l var6 = new com.larpclient.module.l("Open", "How the GUI appears", var1.E, by.q.toArray(String[]::new));
      var6.o(var2x -> this.j(() -> var1.E = var2x));
      com.larpclient.module.l var7 = new com.larpclient.module.l(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.l,
         hb.i.toArray(String[]::new)
      );
      var7.o(var2x -> this.j(() -> var1.l = var2x));
      com.larpclient.module.e var8 = new com.larpclient.module.e(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", var1.c, 100.0, 1000.0, 10.0
      );
      var8.o(var2x -> this.j(() -> var1.c = var2x.intValue()));
      this.f("Animation", "GUI open/close animations.", new ju(var6), new ju(var7), new ju(var8));
      com.larpclient.module.e var9 = new com.larpclient.module.e("Opacity", "Window and navigation opacity", var1.r * 100.0, 0.0, 100.0, 1.0);
      var9.o(var2x -> this.j(() -> var1.r = var2x / 100.0));
      this.f("Transparent", "Make your GUI transparent.", new ju(var9));
      com.larpclient.module.l var10 = new com.larpclient.module.l(
         "Style", "What the GUI draws behind its window. Dim darkens the world without light", var1.FF, hb.v.toArray(String[]::new)
      );
      var10.o(var2x -> this.j(() -> var1.FF = var2x));
      com.larpclient.module.e var11 = new com.larpclient.module.e(
         "Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.Z * 100.0, 0.0, 90.0, 1.0
      );
      var11.o(var2x -> this.j(() -> var1.Z = var2x / 100.0));
      com.larpclient.module.e var12 = new com.larpclient.module.e(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         var1.g,
         0.0,
         4.0,
         1.0
      );
      var12.o(var2x -> this.j(() -> var1.g = var2x.intValue()));
      com.larpclient.module.l var13 = new com.larpclient.module.l(
         "Blur area", "Blur only behind the window, or the whole world while the GUI is open", var1.H, hb.j.toArray(String[]::new)
      );
      var13.o(var2x -> this.j(() -> var1.H = var2x));
      this.f("Backdrop", "Shown behind the GUI.", new ju(var10), new ju(var11), new ju(var12), new jm(var13));
      com.larpclient.module.l var14 = new com.larpclient.module.l(
         "Cards", "Module cards: Filled, an Outline only, or Flat without a contour", var1.I, hb.B.toArray(String[]::new)
      );
      var14.o(var2x -> this.j(() -> var1.I = var2x));
      com.larpclient.module.l var15 = new com.larpclient.module.l(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.Fw,
         hb.n.toArray(String[]::new)
      );
      var15.o(var2x -> this.j(() -> var1.Fw = var2x));
      com.larpclient.module.l var16 = new com.larpclient.module.l("Switches", "Pill, Square or Minimal switches", var1.S, hb.A.toArray(String[]::new));
      var16.o(var2x -> this.j(() -> var1.S = var2x));
      com.larpclient.module.l var17 = new com.larpclient.module.l("Sliders", "Pill, Minimal or Classic sliders", var1.T, hb.b.toArray(String[]::new));
      var17.o(var2x -> this.j(() -> var1.T = var2x));
      com.larpclient.module.e var18 = new com.larpclient.module.e(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.O * 100.0, 0.0, 200.0, 5.0
      );
      var18.o(var2x -> this.j(() -> var1.O = var2x / 100.0));
      com.larpclient.module.s var19 = new com.larpclient.module.s("Accent glow", "A soft accent glow breathing around the window", var1.N);
      var19.o(var2x -> this.j(() -> var1.N = var2x));
      com.larpclient.module.s var20 = new com.larpclient.module.s("Ripple", "A ripple spreads from the pointer when you click a module card", var1.s);
      var20.o(var2x -> this.j(() -> var1.s = var2x));
      com.larpclient.module.s var21 = new com.larpclient.module.s("Hover lift", "Module cards rise a little under the pointer", var1.U);
      var21.o(var2x -> this.j(() -> var1.U = var2x));
      this.f(
         "Style",
         "Look of cards, controls and effects.",
         new ju(var14),
         new ju(var15),
         new ju(var16),
         new ju(var17),
         new ju(var18),
         new ju(var19),
         new ju(var20),
         new ju(var21)
      );
      com.larpclient.module.e var22 = new com.larpclient.module.e("Intensity", "Glow strength", wa.w, 0.0, 2.0, 0.05);
      var22.o(var0 -> wa.w = var0.floatValue());
      com.larpclient.module.e var23 = new com.larpclient.module.e("Radius", "Halo spread", wa.z, 0.5, 3.0, 0.1);
      var23.o(var0 -> wa.z = var0.floatValue());
      this.f("Bloom", "Shared world glow.", new ju(var22), new ju(var23));
      LarpClient var24 = LarpClient.R();
      com.larpclient.module.v var25 = new com.larpclient.module.v(
            "ClickUI keybind",
            "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
            344
         )
         .Q();
      var25.u(var24.m());
      var25.o(var24::C);
      com.larpclient.module.e var26 = new com.larpclient.module.e(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.q * 100.0, 50.0, 200.0, 5.0
      );
      var26.o(var2x -> this.j(() -> var1.q = var2x / 100.0));
      jx var27 = new jx("Reset scale", () -> var26.X(100.0));
      var27.y = (boolean)1;
      this.f("ClickUI", "Shortcut and size of the client interface.", new ju(var25), new ju(var26), var27);
      com.larpclient.module.s var28 = new com.larpclient.module.s(
         "Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.t
      );
      var28.o(var2x -> {
         this.j(() -> var1.t = var2x);
         com.larpclient.h.A("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.f("Main menu", "Client title screen outside worlds.", new ju(var28));
      com.larpclient.module.s var29 = new com.larpclient.module.s(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.P.G
      );
      var29.o(var2x -> {
         this.j(() -> var1.P.G = var2x);
         com.larpclient.h.A("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      com.larpclient.module.i var30 = new com.larpclient.module.i("Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.P.Z);
      var30.o(var2x -> this.j(() -> var1.P.Z = var2x));
      jx var31 = new jx("Reset panel layout", () -> this.j(var1.P::z));
      var31.y = (boolean)1;
      this.f("Legacy ClickGUI", "Classic panels with drop-down settings.", new ju(var29), new ju(var30), var31);
      if (!com.larpclient.module.c.p()) {
         com.larpclient.module.s var32 = new com.larpclient.module.s(
            "Legit mode", "Stop and hide everything the Legit edition does not have. Your modules come back when you switch it off with the key below", false
         );
         var32.o(var1x -> {
            if (var1x) {
               var24.o(true);
            }
         });
         com.larpclient.module.v var33 = new com.larpclient.module.v(
               "Switch key", "Press this key three times quickly to switch Legit mode on or off. Keyboard keys only; clearing it restores /", var1.m
            )
            .Q();
         var33.o(var3x -> {
            if (bc.I(var3x)) {
               this.j(() -> var1.m = var3x);
            } else {
               var33.u(47);
            }
         });
         this.f("Legit mode", "Play as the Legit edition without changing clients.", new ju(var32), new ju(var33));
      }
   }

   private static int V(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      }

      return switch (var0) {
         case 0, 5, 8 -> 0;
         case 1, 4, 6, 9 -> 1;
         default -> 2;
      };
   }

   private static int J(int[] var0) {
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
      if (this.X.equals("Interface") && !this.t.isEmpty()) {
         int var1 = this.l - this.L - 16;
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

         this.g = 0;
         int[] var5 = new int[this.t.size()];

         for (int var6 = 0; var6 < this.t.size(); var6++) {
            hy.e var7 = this.t.get(var6);
            int var8 = var2 == 3 ? V(var6, var2) : J(var3);
            var5[var6] = var8;
            var7.panel.J = 40 + var7.controls.stream().mapToInt(var0 -> var0.J + 4).sum();
            var3[var8] += var7.panel.J + 8;
            this.g = Math.max(this.g, var3[var8] - 8);
         }

         this.Y = Math.clamp(this.Y, 0, Math.max(0, this.g - (this.h - this.R)));
         int var16 = (int)Math.round(this.Q.k(this.Y, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.t.size(); var17++) {
            hy.e var18 = this.t.get(var17);
            int var9 = var5[var17];
            int var10 = this.L + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.R + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (jd var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof ju var15) {
                  var15.q(this.y.top() + 12, this.h);
               }

               var12 += var14.J + 4;
            }

            var3[var9] += var18.panel.J + 8;
         }

         return true;
      } else {
         return false;
      }
   }

   private void n() {
      this.m = this.e == null ? "HUD overlays" : "HUD / " + m4.W(this.e.r);
      if (this.e == null) {
         this.a("Open HUD editor", () -> this.O.T());
         this.text("Use Editor for placement and previews. Chat dragging remains available.");

         for (m4.p var8 : m4.P()) {
            jx var11 = this.a(m4.W(var8.r), () -> {
               this.e = var8;
               this.Y = 0;
               this.C();
               this.build();
            });
            var11.X = m4.s(var8);
            var11.I = (boolean)1;
            var11.J = 34;
            var11.M = m4.B(var8);
            var11.Q = () -> {
               m4.Z(var8, !m4.B(var8));
               com.larpclient.h.U("HUD editor", "{} visibility={}", var8.r, m4.B(var8));
            };
            this.T.put(var11, var8);
         }

         this.h(new j1("Placement guides"));
         com.larpclient.module.s var6 = new com.larpclient.module.s("Snap", "Snap to viewport edges and center while dragging", c7.B.snapping());
         var6.o(var0 -> c7.B = new c7.d(c7.B.panel(), c7.B.collapsed(), var0, c7.B.grid()));
         this.h(new ju(var6));
         com.larpclient.module.s var9 = new com.larpclient.module.s("8px Grid", "Optional grid; Alt bypasses grid and snapping", c7.B.grid());
         var9.o(var0 -> c7.B = new c7.d(c7.B.panel(), c7.B.collapsed(), c7.B.snapping(), var0));
         this.h(new ju(var9));
      } else {
         m4.p var1 = this.e;
         var1.b(this.width, this.height);
         this.a("All HUD overlays", () -> {
            this.e = null;
            this.Y = 0;
            this.C();
            this.build();
         });
         this.text(m4.s(var1) + ". Visibility restores the required module and content toggle.");
         this.D = new com.larpclient.module.s("Visible", "Hide only this overlay; restoring enables its content", m4.B(var1));
         this.D.o(var2 -> {
            if (!this.f) {
               m4.Z(var1, var2);
            }
         });
         this.A = new com.larpclient.module.e("X", "Layout origin in GUI pixels", var1.T(this.width), 0.0, this.width, 1.0);
         this.Z = new com.larpclient.module.e("Y", "Layout origin in GUI pixels", var1.P(this.height), 0.0, this.height, 1.0);
         this.A.o(var2 -> {
            if (!this.f) {
               var1.E(var2.intValue(), var1.P(this.height), this.width, this.height);
            }
         });
         this.Z.o(var2 -> {
            if (!this.f) {
               var1.E(var1.T(this.width), var2.intValue(), this.width, this.height);
            }
         });
         this.n = new com.larpclient.module.e("Scale", "Uniform content scale", var1.k(), 0.5, 2.5, 0.05);
         this.n.o(var2 -> {
            if (!this.f) {
               var1.f(var2);
            }
         });
         this.i = new com.larpclient.module.l(
            "Alignment",
            "Normalized screen anchor",
            L(var1),
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
         this.i.o(var2 -> {
            if (!this.f && !var2.equals("Free")) {
               int var3x = this.i.a().indexOf(var2) - 1;
               c7.v.put(var1.r, new c7.q(var3x % 3 * 0.5, var3x / 3 * 0.5));
            }
         });
         this.h(new j1("Placement"));

         for (com.larpclient.module.q var3 : List.of(this.D, this.A, this.Z, this.n, this.i)) {
            this.h(new ju(var3));
         }

         List var7 = m4.y(var1);
         if (!var7.isEmpty()) {
            this.h(new j1("Appearance"));
         }

         for (com.larpclient.module.q var4 : var7) {
            this.h(new ju(var4, true));
         }

         if (var1.r.equals("crosshair")) {
            this.a("Center crosshair", () -> {
               var1.U();
               this.build();
            });
         }

         this.a("Reset placement", () -> {
            var1.g();
            this.build();
         });
      }
   }

   private static String L(m4.p var0) {
      c7.q var1 = c7.v.get(var0.r);
      if (var1 == null) {
         return var0.r.equals("crosshair") ? "Center" : "Free";
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
      if (this.X.equals("Updates")) {
         this.B.reconcile(this::s);
      }

      for (Entry var5 : this.T.entrySet()) {
         ((jx)var5.getKey()).M = m4.B((m4.p)var5.getValue());
         ((jx)var5.getKey()).X = m4.s((m4.p)var5.getValue());
      }

      if (this.e != null && this.A != null) {
         this.f = (boolean)1;

         try {
            this.A.X((double)this.e.T(this.width));
            this.Z.X((double)this.e.P(this.height));
            this.n.X(this.e.k());
            this.D.setValue(m4.B(this.e));
            this.i.setValue(L(this.e));
         } finally {
            this.f = (boolean)0;
         }
      }
   }

   @Override
   protected void layout() {
      this.h();
      super.layout();
      this.l = this.X.equals("Interface") ? this.y.right() : Math.min(this.y.right(), this.L + 440);
      this.N();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !var2 || this.c == null || !this.c.contains(var1.x(), var1.y()) && !this.v.contains(var1.x(), var1.y()) && !this.P.contains(var1.x(), var1.y())
         ? super.mouseClicked(var1, var2)
         : true;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return this.X.equals("Updates") && !this.B.press(var1.key()) ? true : super.keyPressed(var1);
   }

   private record e(jj panel, List<jd> controls) {
   }
}

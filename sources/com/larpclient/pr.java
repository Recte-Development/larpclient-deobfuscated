package com.larpclient;

import com.larpclient.update.UpdateRuntime;
import com.larpclient.update.UpdateService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;

final class pr extends pb {
   private String S = "Interface";
   private boolean V;
   private final List<pr.e> f = new ArrayList<>();
   private final u7 y = new u7(0.0);
   private o8.a X;
   private final Map<p7, o8.a> a = new LinkedHashMap<>();
   private com.larpclient.module.lc i;
   private com.larpclient.module.lc K;
   private com.larpclient.module.lc q;
   private com.larpclient.module.ln r;
   private com.larpclient.module.lp w;

   void a(o8.a var1) {
      this.X = var1;
   }

   pr() {
      super("Interface");
   }

   boolean b() {
      return this.S.equals("HUD");
   }

   void h(String var1) {
      this.S = var1;
   }

   @Override
   protected void build() {
      this.f();
      this.f.clear();
      this.a.clear();
      this.i = this.K = this.q = null;
      this.r = null;
      this.w = null;
      this.h = false;
      this.Y = this.S.equals("Interface") ? "GUI Settings" : this.S;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.o(var4.equals("Interface") ? "GUI Settings" : var4, this.S.equals(var4), () -> {
            if (!this.S.equals(var4)) {
               this.S = var4;
               this.e = 0;
               this.D();
               r4.V("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      switch (this.S) {
         case "Interface":
            this.w();
            break;
         case "HUD":
            this.h();
            break;
         case "Account":
            this.text("Minecraft: " + this.x.M().getUser().getName());
            this.text("Ready");
            this.e(new p8("Discord"));
            mo var8 = LarpClient.y().v();
            com.larpclient.module.ln var10 = new com.larpclient.module.ln("Discord activity", "Show Playing with larpclient.com on Discord", var8.E().L);
            var10.W(var2 -> {
               if (!var8.I(var2)) {
                  var10.setValue(var8.E().L);
               }
            });
            this.e(new p9(var10));
            this.text("Requires the Discord desktop app and activity sharing enabled.");
            break;
         case "Updates":
            this.e(new p8("Client updates"));
            fs var7 = LarpClient.y().v().E();
            com.larpclient.module.ln var9 = new com.larpclient.module.ln("Automatic checks", "Check signed public release information after startup", var7.h);
            var9.W(var2 -> {
               if (!UpdateRuntime.automatic(var2)) {
                  var9.setValue(var7.h);
               }
            });
            this.e(new p9(var9));
            this.L("View updates / check now", () -> {
               UpdateService var1 = UpdateRuntime.service();
               if (var1 != null) {
                  var1.check();
               }

               UpdateRuntime.open(this.x);
            });
            this.text("Press Update on a new release and it installs when you close Minecraft. No launcher setup is needed.");
            this.text("On macOS, or when the client JAR is not directly in your mods folder, use the official downloads page.");
            break;
         default:
            throw new IllegalStateException("Unknown settings section");
      }
   }

   private void Y(Runnable var1) {
      var1.run();
      LarpClient.y().v().a();
   }

   private void k(String var1, String var2, oq... var3) {
      py var4 = new py(var1, var2);
      this.e(var4);

      for (oq var8 : var3) {
         this.e(var8);
      }

      this.f.add(new pr.e(var4, List.of(var3)));
   }

   private void w() {
      fs var1 = LarpClient.y().v().E();
      this.k("Main color", "Choose the main GUI theme.", new po(() -> var1.Nw, var2x -> this.Y(() -> var1.Nw = var2x)));
      com.larpclient.module.lv var2 = new com.larpclient.module.lv("Primary", "Primary theme color", rv.e);
      var2.W(var0 -> rv.e = var0);
      com.larpclient.module.lv var3 = new com.larpclient.module.lv("Secondary", "Secondary theme color", rv.GQ);
      var3.W(var0 -> rv.GQ = var0);
      p7 var4 = new p7("Reset palette", () -> {
         rv.e = -1381654;
         rv.GQ = -6250336;
         this.build();
      });
      var4.Q = (boolean)1;
      com.larpclient.module.ln var5 = new com.larpclient.module.ln("Animation gradient", "Animate the GUI accent between primary and secondary", var1.b);
      var5.W(var2x -> this.Y(() -> var1.b = var2x));
      this.k("Accent color", "Accent colors of the GUI.", new pa(var1x -> {
         rv.e = var1x;
         var2.setValue(var1x);
      }), new p9(var2), new p9(var3), new p9(var5), var4);
      com.larpclient.module.lp var6 = new com.larpclient.module.lp("Open", "How the GUI appears", var1.s, fm.N.toArray(String[]::new));
      var6.W(var2x -> this.Y(() -> var1.s = var2x));
      com.larpclient.module.lp var7 = new com.larpclient.module.lp(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.V,
         fs.K.toArray(String[]::new)
      );
      var7.W(var2x -> this.Y(() -> var1.V = var2x));
      com.larpclient.module.lc var8 = new com.larpclient.module.lc(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", var1.U, 100.0, 1000.0, 10.0
      );
      var8.W(var2x -> this.Y(() -> var1.U = var2x.intValue()));
      this.k("Animation", "GUI open/close animations.", new p9(var6), new p9(var7), new p9(var8));
      com.larpclient.module.lc var9 = new com.larpclient.module.lc("Opacity", "Window and navigation opacity", var1.w * 100.0, 0.0, 100.0, 1.0);
      var9.W(var2x -> this.Y(() -> var1.w = var2x / 100.0));
      this.k("Transparent", "Make your GUI transparent.", new p9(var9));
      com.larpclient.module.lp var10 = new com.larpclient.module.lp(
         "Style", "What the GUI draws behind its window. Dim darkens the world without light", var1.z, fs.g.toArray(String[]::new)
      );
      var10.W(var2x -> this.Y(() -> var1.z = var2x));
      com.larpclient.module.lc var11 = new com.larpclient.module.lc(
         "Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.E * 100.0, 0.0, 90.0, 1.0
      );
      var11.W(var2x -> this.Y(() -> var1.E = var2x / 100.0));
      com.larpclient.module.lc var12 = new com.larpclient.module.lc(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         var1.Na,
         0.0,
         4.0,
         1.0
      );
      var12.W(var2x -> this.Y(() -> var1.Na = var2x.intValue()));
      com.larpclient.module.lp var13 = new com.larpclient.module.lp(
         "Blur area", "Blur only behind the window, or the whole world while the GUI is open", var1.C, fs.D.toArray(String[]::new)
      );
      var13.W(var2x -> this.Y(() -> var1.C = var2x));
      this.k("Backdrop", "Shown behind the GUI.", new p9(var10), new p9(var11), new p9(var12), new p4(var13));
      com.larpclient.module.lp var14 = new com.larpclient.module.lp(
         "Cards", "Module cards: Filled, an Outline only, or Flat without a contour", var1.F, fs.f.toArray(String[]::new)
      );
      var14.W(var2x -> this.Y(() -> var1.F = var2x));
      com.larpclient.module.lp var15 = new com.larpclient.module.lp(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.j,
         fs.T.toArray(String[]::new)
      );
      var15.W(var2x -> this.Y(() -> var1.j = var2x));
      com.larpclient.module.lp var16 = new com.larpclient.module.lp("Switches", "Pill, Square or Minimal switches", var1.H, fs.R.toArray(String[]::new));
      var16.W(var2x -> this.Y(() -> var1.H = var2x));
      com.larpclient.module.lp var17 = new com.larpclient.module.lp("Sliders", "Pill, Minimal or Classic sliders", var1.v, fs.y.toArray(String[]::new));
      var17.W(var2x -> this.Y(() -> var1.v = var2x));
      com.larpclient.module.lc var18 = new com.larpclient.module.lc(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.q * 100.0, 0.0, 200.0, 5.0
      );
      var18.W(var2x -> this.Y(() -> var1.q = var2x / 100.0));
      com.larpclient.module.ln var19 = new com.larpclient.module.ln("Accent glow", "A soft accent glow breathing around the window", var1.n);
      var19.W(var2x -> this.Y(() -> var1.n = var2x));
      com.larpclient.module.ln var20 = new com.larpclient.module.ln("Ripple", "A ripple spreads from the pointer when you click a module card", var1.Z);
      var20.W(var2x -> this.Y(() -> var1.Z = var2x));
      com.larpclient.module.ln var21 = new com.larpclient.module.ln("Hover lift", "Module cards rise a little under the pointer", var1.u);
      var21.W(var2x -> this.Y(() -> var1.u = var2x));
      this.k(
         "Style",
         "Look of cards, controls and effects.",
         new p9(var14),
         new p9(var15),
         new p9(var16),
         new p9(var17),
         new p9(var18),
         new p9(var19),
         new p9(var20),
         new p9(var21)
      );
      com.larpclient.module.lc var22 = new com.larpclient.module.lc("Intensity", "Glow strength", jk.L, 0.0, 2.0, 0.05);
      var22.W(var0 -> jk.L = var0.floatValue());
      com.larpclient.module.lc var23 = new com.larpclient.module.lc("Radius", "Halo spread", jk.s, 0.5, 3.0, 0.1);
      var23.W(var0 -> jk.s = var0.floatValue());
      this.k("Bloom", "Shared world glow.", new p9(var22), new p9(var23));
      LarpClient var24 = LarpClient.y();
      com.larpclient.module.la var25 = new com.larpclient.module.la(
            "ClickUI keybind",
            "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
            344
         )
         .p();
      var25.k(var24.b());
      var25.W(var24::X);
      com.larpclient.module.lc var26 = new com.larpclient.module.lc(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.Y * 100.0, 50.0, 200.0, 5.0
      );
      var26.W(var2x -> this.Y(() -> var1.Y = var2x / 100.0));
      p7 var27 = new p7("Reset scale", () -> var26.C(100.0));
      var27.Q = (boolean)1;
      this.k("ClickUI", "Shortcut and size of the client interface.", new p9(var25), new p9(var26), var27);
      com.larpclient.module.ln var28 = new com.larpclient.module.ln(
         "Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.NB
      );
      var28.W(var2x -> {
         this.Y(() -> var1.NB = var2x);
         r4.V("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.k("Main menu", "Client title screen outside worlds.", new p9(var28));
      com.larpclient.module.ln var29 = new com.larpclient.module.ln(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.p.h
      );
      var29.W(var2x -> {
         this.Y(() -> var1.p.h = var2x);
         r4.V("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      com.larpclient.module.lv var30 = new com.larpclient.module.lv(
         "Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.p.p
      );
      var30.W(var2x -> this.Y(() -> var1.p.p = var2x));
      p7 var31 = new p7("Reset panel layout", () -> this.Y(var1.p::S));
      var31.Q = (boolean)1;
      this.k("Legacy ClickGUI", "Classic panels with drop-down settings.", new p9(var29), new p9(var30), var31);
   }

   private static int L(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      }

      return switch (var0) {
         case 0, 5, 8 -> 0;
         case 1, 4, 6, 9 -> 1;
         default -> 2;
      };
   }

   private static int y(int[] var0) {
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
      if (!this.h && this.S.equals("Interface") && !this.f.isEmpty()) {
         int var1 = this.A - this.O - 16;
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

         this.H = 0;
         int[] var5 = new int[this.f.size()];

         for (int var6 = 0; var6 < this.f.size(); var6++) {
            pr.e var7 = this.f.get(var6);
            int var8 = var2 == 3 ? L(var6, var2) : y(var3);
            var5[var6] = var8;
            var7.panel.O = 40 + var7.controls.stream().mapToInt(var0 -> var0.O + 4).sum();
            var3[var8] += var7.panel.O + 8;
            this.H = Math.max(this.H, var3[var8] - 8);
         }

         this.e = Math.clamp(this.e, 0, Math.max(0, this.H - (this.n - this.t)));
         int var16 = (int)Math.round(this.y.y(this.e, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.f.size(); var17++) {
            pr.e var18 = this.f.get(var17);
            int var9 = var5[var17];
            int var10 = this.O + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.t + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (oq var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof p9 var15) {
                  var15.Y(this.M.top() + 12, this.n);
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

   private void h() {
      this.Y = this.X == null ? "HUD overlays" : "HUD / " + o8.F(this.X.M);
      if (this.X == null) {
         this.L("Open HUD editor", () -> this.x.N());
         this.text("Use Editor for placement and previews. Chat dragging remains available.");

         for (o8.a var8 : o8.d) {
            p7 var11 = this.L(o8.F(var8.M), () -> {
               this.X = var8;
               this.e = 0;
               this.D();
               this.build();
            });
            var11.y = o8.Z(var8);
            var11.n = (boolean)1;
            var11.O = 34;
            var11.I = o8.r(var8);
            var11.H = () -> {
               o8.a(var8, !o8.r(var8));
               r4.g("HUD editor", "{} visibility={}", var8.M, o8.r(var8));
            };
            this.a.put(var11, var8);
         }

         this.e(new p8("Placement guides"));
         com.larpclient.module.ln var6 = new com.larpclient.module.ln("Snap", "Snap to viewport edges and center while dragging", fc.h.snapping());
         var6.W(var0 -> fc.h = new fc.p(fc.h.panel(), fc.h.collapsed(), var0, fc.h.grid()));
         this.e(new p9(var6));
         com.larpclient.module.ln var9 = new com.larpclient.module.ln("8px Grid", "Optional grid; Alt bypasses grid and snapping", fc.h.grid());
         var9.W(var0 -> fc.h = new fc.p(fc.h.panel(), fc.h.collapsed(), fc.h.snapping(), var0));
         this.e(new p9(var9));
      } else {
         o8.a var1 = this.X;
         var1.q(this.width, this.height);
         this.L("All HUD overlays", () -> {
            this.X = null;
            this.e = 0;
            this.D();
            this.build();
         });
         this.text(o8.Z(var1) + ". Visibility restores the required module and content toggle.");
         this.r = new com.larpclient.module.ln("Visible", "Hide only this overlay; restoring enables its content", o8.r(var1));
         this.r.W(var2 -> {
            if (!this.V) {
               o8.a(var1, var2);
            }
         });
         this.i = new com.larpclient.module.lc("X", "Layout origin in GUI pixels", var1.b(this.width), 0.0, this.width, 1.0);
         this.K = new com.larpclient.module.lc("Y", "Layout origin in GUI pixels", var1.M(this.height), 0.0, this.height, 1.0);
         this.i.W(var2 -> {
            if (!this.V) {
               var1.i(var2.intValue(), var1.M(this.height), this.width, this.height);
            }
         });
         this.K.W(var2 -> {
            if (!this.V) {
               var1.i(var1.b(this.width), var2.intValue(), this.width, this.height);
            }
         });
         this.q = new com.larpclient.module.lc("Scale", "Uniform content scale", var1.H(), 0.5, 2.5, 0.05);
         this.q.W(var2 -> {
            if (!this.V) {
               var1.m(var2);
            }
         });
         this.w = new com.larpclient.module.lp(
            "Alignment",
            "Normalized screen anchor",
            b(var1),
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
         this.w.W(var2 -> {
            if (!this.V && !var2.equals("Free")) {
               int var3x = this.w.X().indexOf(var2) - 1;
               fc.m.put(var1.M, new fc.g(var3x % 3 * 0.5, var3x / 3 * 0.5));
            }
         });
         this.e(new p8("Placement"));

         for (com.larpclient.module.l6 var3 : List.of(this.r, this.i, this.K, this.q, this.w)) {
            this.e(new p9(var3));
         }

         List var7 = o8.z(var1);
         if (!var7.isEmpty()) {
            this.e(new p8("Appearance"));
         }

         for (com.larpclient.module.l6 var4 : var7) {
            this.e(new p9(var4, true));
         }

         if (var1.M.equals("crosshair")) {
            this.L("Center crosshair", () -> {
               var1.W();
               this.build();
            });
         }

         this.L("Reset placement", () -> {
            var1.j();
            this.build();
         });
      }
   }

   private static String b(o8.a var0) {
      fc.g var1 = fc.m.get(var0.M);
      if (var1 == null) {
         return var0.M.equals("crosshair") ? "Center" : "Free";
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
      for (Entry var5 : this.a.entrySet()) {
         ((p7)var5.getKey()).I = o8.r((o8.a)var5.getValue());
         ((p7)var5.getKey()).y = o8.Z((o8.a)var5.getValue());
      }

      if (this.X != null && this.i != null) {
         this.V = (boolean)1;

         try {
            this.i.C((double)this.X.b(this.width));
            this.K.C((double)this.X.M(this.height));
            this.q.C(this.X.H());
            this.r.setValue(o8.r(this.X));
            this.w.setValue(b(this.X));
         } finally {
            this.V = (boolean)0;
         }
      }
   }

   @Override
   protected void layout() {
      super.layout();
      if (!this.h) {
         this.A = this.S.equals("Interface") ? this.M.right() : Math.min(this.M.right(), this.O + 440);
         this.d();
      }
   }

   @Override
   protected void dismissDialog() {
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return super.keyPressed(var1);
   }

   private record e(py panel, List<oq> controls) {
   }
}

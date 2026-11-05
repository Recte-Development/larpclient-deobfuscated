package com.larpclient;

import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dk;
import com.larpclient.module.dv;
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

final class hd extends hb {
   private String x = "Interface";
   private boolean B;
   private final List<hd.t> J = new ArrayList<>();
   private final ef h = new ef(0.0);
   private mq.s O;
   private final Map<u7, mq.s> o = new LinkedHashMap<>();
   private com.larpclient.module.d9 l;
   private com.larpclient.module.d9 w;
   private com.larpclient.module.d9 S;
   private dh P;
   private com.larpclient.module.d6 A;

   void K(mq.s var1) {
      this.O = var1;
   }

   hd() {
      super("Interface");
   }

   boolean z() {
      return this.x.equals("HUD");
   }

   void N(String var1) {
      this.x = var1;
   }

   @Override
   protected void build() {
      this.d();
      this.J.clear();
      this.o.clear();
      this.l = this.w = this.S = null;
      this.P = null;
      this.A = null;
      this.W = false;
      this.F = this.x.equals("Interface") ? "GUI Settings" : this.x;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.f(var4.equals("Interface") ? "GUI Settings" : var4, this.x.equals(var4), () -> {
            if (!this.x.equals(var4)) {
               this.x = var4;
               this.Z = 0;
               this.P();
               mt.K("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      switch (this.x) {
         case "Interface":
            this.Z();
            break;
         case "HUD":
            this.t();
            break;
         case "Account":
            this.text("Minecraft: " + this.M.F().getUser().getName());
            this.text("");
            this.w(new us("Discord"));
            w9 var8 = LarpClient.F().f();
            dh var10 = new dh("Discord activity", "Show Playing with larpclient.com on Discord", var8.t().d);
            var10.h(var2 -> {
               if (!var8.X(var2)) {
                  var10.setValue(var8.t().d);
               }
            });
            this.w(new un(var10));
            this.text("Requires the Discord desktop app and activity sharing enabled.");
            break;
         case "Updates":
            this.w(new us("Client updates"));
            xv var7 = LarpClient.F().f().t();
            dh var9 = new dh("Automatic checks", "Check signed public release information after startup", var7.o);
            var9.h(var2 -> {
               if (!UpdateRuntime.automatic(var2)) {
                  var9.setValue(var7.o);
               }
            });
            this.w(new un(var9));
            this.U("View updates / check now", () -> {
               UpdateService var1 = UpdateRuntime.service();
               if (var1 != null) {
                  var1.check();
               }

               UpdateRuntime.open(this.M);
            });
            this.text("Press Update on a new release and it installs when you close Minecraft. No launcher setup is needed.");
            this.text("On macOS, or when the client JAR is not directly in your mods folder, use the official downloads page.");
            break;
         default:
            throw new IllegalStateException("Unknown settings section");
      }
   }

   private void I(Runnable var1) {
      var1.run();
      LarpClient.F().f().P();
   }

   private void S(String var1, String var2, b... var3) {
      uq var4 = new uq(var1, var2);
      this.w(var4);

      for (b var8 : var3) {
         this.w(var8);
      }

      this.J.add(new hd.t(var4, List.of(var3)));
   }

   private void Z() {
      xv var1 = LarpClient.F().f().t();
      this.S("Main color", "Choose the main GUI theme.", new up(() -> var1.c, var2x -> this.I(() -> var1.c = var2x)));
      dk var2 = new dk("Primary", "Primary theme color", com.larpclient.f.C);
      var2.h(var0 -> com.larpclient.f.C = var0);
      dk var3 = new dk("Secondary", "Secondary theme color", com.larpclient.f.O);
      var3.h(var0 -> com.larpclient.f.O = var0);
      u7 var4 = new u7("Reset palette", () -> {
         com.larpclient.f.C = -1381654;
         com.larpclient.f.O = -6250336;
         this.build();
      });
      var4.g = (boolean)1;
      dh var5 = new dh("Animation gradient", "Animate the GUI accent between primary and secondary", var1.i);
      var5.h(var2x -> this.I(() -> var1.i = var2x));
      this.S("Accent color", "Accent colors of the GUI.", new u9(var1x -> {
         com.larpclient.f.C = var1x;
         var2.setValue(var1x);
      }), new un(var2), new un(var3), new un(var5), var4);
      com.larpclient.module.d6 var6 = new com.larpclient.module.d6("Open", "How the GUI appears", var1.sb, p2.i.toArray(String[]::new));
      var6.h(var2x -> this.I(() -> var1.sb = var2x));
      com.larpclient.module.d6 var7 = new com.larpclient.module.d6(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.Y,
         xv.W.toArray(String[]::new)
      );
      var7.h(var2x -> this.I(() -> var1.Y = var2x));
      com.larpclient.module.d9 var8 = new com.larpclient.module.d9(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", var1.S, 100.0, 1000.0, 10.0
      );
      var8.h(var2x -> this.I(() -> var1.S = var2x.intValue()));
      this.S("Animation", "GUI open/close animations.", new un(var6), new un(var7), new un(var8));
      com.larpclient.module.d9 var9 = new com.larpclient.module.d9("Opacity", "Window and navigation opacity", var1.T * 100.0, 0.0, 100.0, 1.0);
      var9.h(var2x -> this.I(() -> var1.T = var2x / 100.0));
      this.S("Transparent", "Make your GUI transparent.", new un(var9));
      com.larpclient.module.d6 var10 = new com.larpclient.module.d6(
         "Style", "What the GUI draws behind its window. Dim darkens the world without light", var1.F, xv.Q.toArray(String[]::new)
      );
      var10.h(var2x -> this.I(() -> var1.F = var2x));
      com.larpclient.module.d9 var11 = new com.larpclient.module.d9(
         "Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.X * 100.0, 0.0, 90.0, 1.0
      );
      var11.h(var2x -> this.I(() -> var1.X = var2x / 100.0));
      com.larpclient.module.d9 var12 = new com.larpclient.module.d9(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         var1.q,
         0.0,
         4.0,
         1.0
      );
      var12.h(var2x -> this.I(() -> var1.q = var2x.intValue()));
      com.larpclient.module.d6 var13 = new com.larpclient.module.d6(
         "Blur area", "Blur only behind the window, or the whole world while the GUI is open", var1.R, xv.h.toArray(String[]::new)
      );
      var13.h(var2x -> this.I(() -> var1.R = var2x));
      this.S("Backdrop", "Shown behind the GUI.", new un(var10), new un(var11), new un(var12), new ug(var13));
      com.larpclient.module.d6 var14 = new com.larpclient.module.d6(
         "Cards", "Module cards: Filled, an Outline only, or Flat without a contour", var1.g, xv.f.toArray(String[]::new)
      );
      var14.h(var2x -> this.I(() -> var1.g = var2x));
      com.larpclient.module.d6 var15 = new com.larpclient.module.d6(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.sS,
         xv.v.toArray(String[]::new)
      );
      var15.h(var2x -> this.I(() -> var1.sS = var2x));
      com.larpclient.module.d6 var16 = new com.larpclient.module.d6("Switches", "Pill, Square or Minimal switches", var1.M, xv.u.toArray(String[]::new));
      var16.h(var2x -> this.I(() -> var1.M = var2x));
      com.larpclient.module.d6 var17 = new com.larpclient.module.d6("Sliders", "Pill, Minimal or Classic sliders", var1.G, xv.D.toArray(String[]::new));
      var17.h(var2x -> this.I(() -> var1.G = var2x));
      com.larpclient.module.d9 var18 = new com.larpclient.module.d9(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.m * 100.0, 0.0, 200.0, 5.0
      );
      var18.h(var2x -> this.I(() -> var1.m = var2x / 100.0));
      dh var19 = new dh("Accent glow", "A soft accent glow breathing around the window", var1.y);
      var19.h(var2x -> this.I(() -> var1.y = var2x));
      dh var20 = new dh("Ripple", "A ripple spreads from the pointer when you click a module card", var1.sz);
      var20.h(var2x -> this.I(() -> var1.sz = var2x));
      dh var21 = new dh("Hover lift", "Module cards rise a little under the pointer", var1.P);
      var21.h(var2x -> this.I(() -> var1.P = var2x));
      this.S(
         "Style",
         "Look of cards, controls and effects.",
         new un(var14),
         new un(var15),
         new un(var16),
         new un(var17),
         new un(var18),
         new un(var19),
         new un(var20),
         new un(var21)
      );
      com.larpclient.module.d9 var22 = new com.larpclient.module.d9("Intensity", "Glow strength", m5.v, 0.0, 2.0, 0.05);
      var22.h(var0 -> m5.v = var0.floatValue());
      com.larpclient.module.d9 var23 = new com.larpclient.module.d9("Radius", "Halo spread", m5.a, 0.5, 3.0, 0.1);
      var23.h(var0 -> m5.a = var0.floatValue());
      this.S("Bloom", "Shared world glow.", new un(var22), new un(var23));
      LarpClient var24 = LarpClient.F();
      dv var25 = new dv(
            "ClickUI keybind",
            "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
            344
         )
         .b();
      var25.T(var24.R());
      var25.h(var24::g);
      com.larpclient.module.d9 var26 = new com.larpclient.module.d9(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.s8 * 100.0, 50.0, 200.0, 5.0
      );
      var26.h(var2x -> this.I(() -> var1.s8 = var2x / 100.0));
      u7 var27 = new u7("Reset scale", () -> var26.o(100.0));
      var27.g = (boolean)1;
      this.S("ClickUI", "Shortcut and size of the client interface.", new un(var25), new un(var26), var27);
      dh var28 = new dh("Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.U);
      var28.h(var2x -> {
         this.I(() -> var1.U = var2x);
         mt.K("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.S("Main menu", "Client title screen outside worlds.", new un(var28));
      dh var29 = new dh(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.O.e
      );
      var29.h(var2x -> {
         this.I(() -> var1.O.e = var2x);
         mt.K("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      dk var30 = new dk("Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.O.A);
      var30.h(var2x -> this.I(() -> var1.O.A = var2x));
      u7 var31 = new u7("Reset panel layout", () -> this.I(var1.O::R));
      var31.g = (boolean)1;
      this.S("Legacy ClickGUI", "Classic panels with drop-down settings.", new un(var29), new un(var30), var31);
   }

   private static int R(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      }

      return switch (var0) {
         case 0, 5, 8 -> 0;
         case 1, 4, 6, 9 -> 1;
         default -> 2;
      };
   }

   private static int T(int[] var0) {
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
      if (!this.W && this.x.equals("Interface") && !this.J.isEmpty()) {
         int var1 = this.X - this.V - 16;
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

         this.r = 0;
         int[] var5 = new int[this.J.size()];

         for (int var6 = 0; var6 < this.J.size(); var6++) {
            hd.t var7 = this.J.get(var6);
            int var8 = var2 == 3 ? R(var6, var2) : T(var3);
            var5[var6] = var8;
            var7.panel.W = 40 + var7.controls.stream().mapToInt(var0 -> var0.W + 4).sum();
            var3[var8] += var7.panel.W + 8;
            this.r = Math.max(this.r, var3[var8] - 8);
         }

         this.Z = Math.clamp(this.Z, 0, Math.max(0, this.r - (this.L - this.y)));
         int var16 = (int)Math.round(this.h.m(this.Z, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.J.size(); var17++) {
            hd.t var18 = this.J.get(var17);
            int var9 = var5[var17];
            int var10 = this.V + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.y + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (b var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof un var15) {
                  var15.N(this.j.top() + 12, this.L);
               }

               var12 += var14.W + 4;
            }

            var3[var9] += var18.panel.W + 8;
         }

         return true;
      } else {
         return false;
      }
   }

   private void t() {
      this.F = this.O == null ? "HUD overlays" : "HUD / " + mq.w(this.O.g);
      if (this.O == null) {
         this.U("Open HUD editor", () -> this.M.Z());
         this.text("Use Editor for placement and previews. Chat dragging remains available.");

         for (mq.s var8 : mq.Y) {
            u7 var11 = this.U(mq.w(var8.g), () -> {
               this.O = var8;
               this.Z = 0;
               this.P();
               this.build();
            });
            var11.u = mq.m(var8);
            var11.Q = (boolean)1;
            var11.W = 34;
            var11.v = mq.M(var8);
            var11.E = () -> {
               mq.m(var8, !mq.M(var8));
               mt.U("HUD editor", "{} visibility={}", var8.g, mq.M(var8));
            };
            this.o.put(var11, var8);
         }

         this.w(new us("Placement guides"));
         dh var6 = new dh("Snap", "Snap to viewport edges and center while dragging", cx.l.snapping());
         var6.h(var0 -> cx.l = new cx.h(cx.l.panel(), cx.l.collapsed(), var0, cx.l.grid()));
         this.w(new un(var6));
         dh var9 = new dh("8px Grid", "Optional grid; Alt bypasses grid and snapping", cx.l.grid());
         var9.h(var0 -> cx.l = new cx.h(cx.l.panel(), cx.l.collapsed(), cx.l.snapping(), var0));
         this.w(new un(var9));
      } else {
         mq.s var1 = this.O;
         var1.P(this.width, this.height);
         this.U("All HUD overlays", () -> {
            this.O = null;
            this.Z = 0;
            this.P();
            this.build();
         });
         this.text(mq.m(var1) + ". Visibility restores the required module and content toggle.");
         this.P = new dh("Visible", "Hide only this overlay; restoring enables its content", mq.M(var1));
         this.P.h(var2 -> {
            if (!this.B) {
               mq.m(var1, var2);
            }
         });
         this.l = new com.larpclient.module.d9("X", "Layout origin in GUI pixels", var1.W(this.width), 0.0, this.width, 1.0);
         this.w = new com.larpclient.module.d9("Y", "Layout origin in GUI pixels", var1.i(this.height), 0.0, this.height, 1.0);
         this.l.h(var2 -> {
            if (!this.B) {
               var1.s(var2.intValue(), var1.i(this.height), this.width, this.height);
            }
         });
         this.w.h(var2 -> {
            if (!this.B) {
               var1.s(var1.W(this.width), var2.intValue(), this.width, this.height);
            }
         });
         this.S = new com.larpclient.module.d9("Scale", "Uniform content scale", var1.X(), 0.5, 2.5, 0.05);
         this.S.h(var2 -> {
            if (!this.B) {
               var1.W(var2);
            }
         });
         this.A = new com.larpclient.module.d6(
            "Alignment",
            "Normalized screen anchor",
            B(var1),
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
         this.A.h(var2 -> {
            if (!this.B && !var2.equals("Free")) {
               int var3x = this.A.N().indexOf(var2) - 1;
               cx.O.put(var1.g, new cx.m(var3x % 3 * 0.5, var3x / 3 * 0.5));
            }
         });
         this.w(new us("Placement"));

         for (df var3 : List.of(this.P, this.l, this.w, this.S, this.A)) {
            this.w(new un(var3));
         }

         List var7 = mq.E(var1);
         if (!var7.isEmpty()) {
            this.w(new us("Appearance"));
         }

         for (df var4 : var7) {
            this.w(new un(var4, true));
         }

         if (var1.g.equals("crosshair")) {
            this.U("Center crosshair", () -> {
               var1.o();
               this.build();
            });
         }

         this.U("Reset placement", () -> {
            var1.f();
            this.build();
         });
      }
   }

   private static String B(mq.s var0) {
      cx.m var1 = cx.O.get(var0.g);
      if (var1 == null) {
         return var0.g.equals("crosshair") ? "Center" : "Free";
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
      for (Entry var5 : this.o.entrySet()) {
         ((u7)var5.getKey()).v = mq.M((mq.s)var5.getValue());
         ((u7)var5.getKey()).u = mq.m((mq.s)var5.getValue());
      }

      if (this.O != null && this.l != null) {
         this.B = (boolean)1;

         try {
            this.l.o((double)this.O.W(this.width));
            this.w.o((double)this.O.i(this.height));
            this.S.o(this.O.X());
            this.P.setValue(mq.M(this.O));
            this.A.setValue(B(this.O));
         } finally {
            this.B = (boolean)0;
         }
      }
   }

   @Override
   protected void layout() {
      super.layout();
      if (!this.W) {
         this.X = this.x.equals("Interface") ? this.j.right() : Math.min(this.j.right(), this.V + 440);
         this.a();
      }
   }

   @Override
   protected void dismissDialog() {
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return super.keyPressed(var1);
   }

   private record t(uq panel, List<b> controls) {
   }
}

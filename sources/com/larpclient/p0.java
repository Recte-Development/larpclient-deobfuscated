package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.WorldMenuModule;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class p0 extends pw {
   private final Screen X;
   private final List<p7> J = new ArrayList<>();
   private final List<p7> n = new ArrayList<>();
   private final Map<p7, Module> v = new IdentityHashMap<>();
   private final Map<p7, pp> B = new IdentityHashMap<>();
   private final Map<p7, p8> e = new IdentityHashMap<>();
   private int r;
   private boolean T;
   private boolean f;
   private long i;
   private final p2 I = new p2();
   private final pr t = new pr();
   private pz Q;
   private pb P;
   private mj F;
   private ry s;
   private lw D;
   private Module x;
   private String R = "Modules";
   private String S = "";
   private String w = "";
   private boolean C;
   private boolean N;
   private boolean j;
   private static p0 E;
   private Object K;
   private _8 G;
   private int o;
   private int O;
   private double d;
   private double U = 1.0;
   private boolean b;
   private boolean H;
   private boolean p;
   private final u7 q = new u7(0.0);
   private final u7 u = new u7(1.0);
   private p_ M;
   private p7 m;
   private p7 y;
   private p7 l;
   private p7 h;
   private final yd wD = new yd();
   private static final rs Z = new rs();
   private final u7 g = new u7(0.0);
   private final u7 z = new u7(0.0);
   private final u7 a = new u7(0.0);
   private final u7 V = new u7(0.0);
   private boolean Y;
   private y0 A;
   private final Matrix3x2f c = new Matrix3x2f();

   public p0() {
      this(null);
   }

   public p0(Screen var1) {
      super("Larp client");
      this.X = var1;
      this.D = LarpClient.y().v().E().t ? lw.l : lw.z(fc.K);
   }

   public void I() {
      this.A = new y0();
   }

   public static p0 X(Screen var0, String var1, boolean var2) {
      p0 var3 = new p0(var0);
      var3.b = (boolean)1;
      var3.R = "Configs";
      var3.Q = new pz(var3);
      if (var1 != null) {
         var3.Q.dn.e = var1;
      }

      var3.Q.da = var2;
      return var3;
   }

   public static p0 j(Screen var0) {
      p0 var1 = X(var0, null, false);
      var1.C = (boolean)1;
      return var1;
   }

   public static p0 x(Screen var0, String var1) {
      p0 var2 = new p0(var0);
      var2.b = (boolean)1;
      var2.R = "Settings";
      var2.t.h(var1);
      return var2;
   }

   public static p0 T(Screen var0, o8.a var1) {
      p0 var2 = x(var0, "HUD");
      var2.t.a(var1);
      return var2;
   }

   Minecraft M() {
      return this.minecraft;
   }

   boolean H(pz var1) {
      return this.P == var1;
   }

   public void onClose() {
      if (!this.H) {
         if (this.P != null) {
            this.P.suspend();
         }

         this.I.M();
         hx.w(hx.t.I);
         this.H = (boolean)1;
         this.g.y(0.0, this.Z());
         if (!WorldMenuModule.O(this)) {
            this.K = this.minecraft.level;
            E = this;
            this.G = this.W();
            if (this.X instanceof rf && !LarpClient.y().v().E().NB) {
               r4.V("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.minecraft.gui.setScreen(new TitleScreen());
            } else {
               this.minecraft.gui.setScreen(this.X);
            }
         }
      }
   }

   private int Z() {
      return LarpClient.y().v().E().U;
   }

   private _8 W() {
      fs var1 = LarpClient.y().v().E();
      String var2 = var1.V;
      if (!var2.equals("Reverse") && var1.o) {
         ArrayList var3 = new ArrayList();
         float var4 = this.c.m00();
         Vector2f var5 = this.c.transformPosition(this.F.left(), this.F.top(), new Vector2f());
         var3.add(new _8.q(var5.x, var5.y, this.F.width() * var4, this.F.height() * var4, rv.T * var4));
         if (this.x != null && this.P == null && this.F.detachedInspector() && this.O() > 0.5) {
            mj var6 = this.F.moduleInspector(this.d);
            Vector2f var7 = this.c.transformPosition(var6.inspectorX(), var6.top(), new Vector2f());
            var3.add(new _8.q(var7.x, var7.y, (var6.right() - var6.inspectorX()) * var4, var6.height() * var4, rv.T * var4));
         }

         return _8.p(var2, var3, this.Z());
      } else {
         return null;
      }
   }

   public static boolean H() {
      return E != null;
   }

   public static void j(GuiGraphicsExtractor var0, float var1) {
      p0 var2 = E;
      if (var2 != null) {
         Minecraft var3 = Minecraft.getInstance();
         _8 var4 = var2.G;
         if (var3.gui.screen() == var2.X
            && var3.level == var2.K
            && var3.gui.overlay() == null
            && var2.width == var0.guiWidth()
            && var2.height == var0.guiHeight()
            && var2.s.equals(D(var3))
            && (var4 == null ? !(var2.g.V() <= 0.0) : !var4.r())) {
            try {
               var0.nextStratum();
               if (var4 != null) {
                  fs var5 = LarpClient.y().v().E();

                  try (rv.s var6 = rv.z(var5.Nw, var5.w, var5.b)) {
                     V(var0, var0.guiWidth(), var0.guiHeight(), 1.0 - var4.H(), var5);
                  }

                  var4.B(var0);
               } else {
                  var2.extractRenderState(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var11) {
               p();
               r4.r("GUI", "Window exit rendering failed; input remains released", var11);
            }
         } else {
            if (var4 != null) {
               r4.g("GUI", "Close effect {}", var4.r() ? "finished" : "stopped by a screen, world or window size change");
            }

            p();
         }
      }
   }

   private static void p() {
      if (E != null && E.G != null) {
         E.G = null;
         _8.Q();
      }

      E = null;
   }

   private static ry D(Minecraft var0) {
      return ry.paired(var0.getWindow(), LarpClient.y().v().E().Y);
   }

   protected void init() {
      this.s = D(this.minecraft);
      this.c.set(this.s.transform());
      p();
      if (!this.b) {
         fc.t var1 = fc.J;
         this.R = var1.page();
         this.S = var1.query();
         this.o = var1.moduleScroll();
         this.x = LarpClient.y().e().O(var1.module());
         this.x = ChooseASkyEffectAndAdjustItsAppearanceModule.R(this.x, LarpClient.y().e().X(), LarpClient.y().v().E());
         if (this.x == null && !var1.module().isEmpty()) {
            r4.V("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.x != null && this.S.isBlank() && this.D != lw.l) {
            this.D = lw.k(this.x);
         }

         this.b = (boolean)1;
      }

      this.F = this.R.equals("Modules")
         ? mj.calculateModules(this.s.width(), this.s.height(), this.x == null ? 0.0 : 1.0)
         : mj.calculate(this.s.width(), this.s.height());
      this.H = (boolean)0;
      this.g.c(this.A == null ? 0.0 : 1.0);
      this.g.y(1.0, this.Z());
      if (!this.N) {
         this.N = (boolean)1;
         hx.w(hx.t.O);
      }

      this.p = (boolean)0;
      this.z.c(this.x == null ? 0.0 : 1.0);
      this.q.c(this.o);
      this.f();
      this.q.c(this.o);
      if (this.x != null) {
         int var2 = this.I.M == null ? fc.J.settingScroll() : this.I.e;
         this.I.p(this, this.F);
         this.I.K(this.x);
         this.I.e = var2;
         this.z.c(1.0);
      }

      this.r();
      r4.g(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.width,
         this.height,
         this.minecraft.getWindow().getGuiScale(),
         this.s.width(),
         this.s.height(),
         this.s.scale(),
         this.R
      );
   }

   private void r() {
      this.P = switch (this.R) {
         case "Configs" -> {
            if (this.Q == null) {
               this.Q = new pz(this);
            }

            yield this.Q;
         }
         case "Settings" -> this.t;
         default -> null;
      };
      if (this.P != null) {
         this.P.p(this, this.F);
      }

      if (this.C && this.P == this.Q) {
         this.C = (boolean)0;
         this.Q.D("Discover");
      }
   }

   private void r(String var1) {
      if (!this.R.equals(var1)) {
         hx.w(hx.t.Y);
      }

      if (this.p) {
         this.x = null;
         this.p = (boolean)0;
         this.z.c(0.0);
      }

      if (this.P != null) {
         this.P.suspend();
      }

      this.I.suspend();
      this.R = var1;
      this.f();
      this.r();
      if (this.P == null && this.x != null) {
         this.I.p(this, this.F);
         this.I.K(this.x);
      }

      this.i();
      r4.g("GUI", "Destination switched to {}", var1);
   }

   void Y() {
      this.t.h("HUD");
      this.t.a(null);
      this.t.e = 0;
      this.r("Settings");
   }

   void N() {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         this.i();
         this.minecraft.gui.setScreen(new ps(this));
      } else {
         this.w = "Join a world to edit the HUD.";
      }
   }

   void b() {
      if (this.x != null && !this.p) {
         hx.w(hx.t.S);
         this.p = (boolean)1;
         this.I.M();
         this.z.y(0.0, 270);
      }
   }

   private void M() {
      if (this.p && this.z.V() == 0.0) {
         this.x = null;
         this.p = (boolean)0;
         this.I.suspend();
      }
   }

   private void z(lw var1) {
      boolean var2 = var1.n(this.D, () -> {
         if (this.R.equals("Modules")) {
            hx.w(hx.t.Y);
         }

         this.u.P(220);
         fs var2x = LarpClient.y().v().E();
         if (var2x.t != (var1 == lw.l ? 1 : 0)) {
            var2x.t = (boolean)(var1 == lw.l ? 1 : 0);
            this.f = (boolean)1;
            this.F();
         }

         this.D = var1;
         this.o = 0;
         this.q.c(0.0);
         this.x = null;
         this.p = (boolean)0;
         this.S = "";
         this.r("Modules");
      });
      if (!var2 && !this.R.equals("Modules")) {
         this.r("Modules");
      }
   }

   private void f() {
      this.u();
      this.J.clear();
      this.n.clear();
      this.v.clear();
      this.B.clear();

      for (lw var4 : lw.values()) {
         p7 var5 = new p7(var4.C, () -> this.z(var4));
         var5.s = null;
         var5.e = var4.A;
         var5.c = (boolean)1;
         var5.W = (boolean)1;
         this.J.add(var5);
         this.k.add(var5);
      }

      for (String var9 : new String[]{"Editor", "Configs", "Settings"}) {
         p7 var10 = new p7(var9, () -> {
            if (var9.equals("Editor")) {
               this.N();
            } else {
               this.r(var9);
            }
         });
         var10.s = null;
         var10.e = var9.equals("Editor") ? "panels-top-left" : (var9.equals("Configs") ? "folder" : "settings");
         var10.c = (boolean)1;
         var10.W = (boolean)1;
         this.J.add(var10);
         this.k.add(var10);
      }

      this.h = new p7("Account", () -> {
         this.t.h("Account");
         this.r("Settings");
      });
      this.k.add(this.h);
      this.y = new p7("Israel mode", this::w);
      this.y.c = (boolean)1;
      this.k.add(this.y);
      this.l = new p7("Larp Preset", this::z);
      this.l.c = (boolean)1;
      this.k.add(this.l);
      this.M = new p_("Search modules...", var1 -> {
         this.S = var1;
         this.o = 0;
         this.q.c(0.0);
         this.s();
      }, 256);
      this.M.Y(var0 -> d3.E(ChooseASkyEffectAndAdjustItsAppearanceModule.O(LarpClient.y().e().X(), LarpClient.y().v().E()), var0));
      this.M.V = this.S;
      this.M.S = "search";
      this.k.add(this.M);
      this.m = new p7("", this::b);
      this.m.e = "x";
      this.m.c = (boolean)1;
      this.k.add(this.m);
      this.m.s = null;
      this.s();
   }

   private void s() {
      this.T = (boolean)0;
      int var1 = -1;
      int var2 = this.W != null && this.W.h ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.n.size(); var5++) {
         if (this.W == this.n.get(var5) || this.W == this.B.get(this.n.get(var5))) {
            var1 = var5;
            var4 = this.v.get(this.n.get(var5));
            var3 = (byte)(this.W == this.B.get(this.n.get(var5)) ? 1 : 0);
         }
      }

      if (this.n.contains(this.W) || this.B.containsValue(this.W)) {
         this.W.blur();
         this.W = null;
      }

      this.k.removeAll(this.n);
      this.k.removeAll(this.B.values());
      this.n.clear();
      this.v.clear();
      this.B.clear();
      this.e.clear();
      fs var13 = LarpClient.y().v().E();
      String var6 = "";

      for (Module var8 : this.D.f(LarpClient.y().e().X(), this.S, var13)) {
         String var9 = this.D.s(this.S) ? lw.t(var8) : "";
         int var10 = !var9.equals(var6) ? 1 : 0;
         var6 = var9;
         p7 var11 = new p7(var8.getDisplayName(), () -> {
            int var2x = this.x == null ? 1 : 0;
            int var3x = this.x != var8 ? 1 : 0;
            if (var3x != 0) {
               hx.w(hx.t.h);
            }

            this.p = (boolean)0;
            this.x = var8;
            if (var2x != 0) {
               this.I.p(this, this.F.moduleInspector(this.d));
            }

            if (var3x != 0) {
               this.I.K(var8);
            }

            this.z.y(1.0, 270);
            this.e();
         });
         var11.s = null;
         var11.n = var11.X = (boolean)1;
         var11.H = () -> {
            var8.toggle();
            hx.w(var8.isEnabled() ? hx.t.N : hx.t.F);
         };
         var11.I = var8.isEnabled();
         this.n.add(var11);
         this.v.put(var11, var8);
         if (var10 != 0 && !var6.isEmpty()) {
            this.e.put(var11, p8.Y(var6));
         }

         pp var12 = new pp(() -> var13.s(var8.getName()), () -> {
            var13.V(var8.getName(), !var13.s(var8.getName()));
            this.f = (boolean)1;
            this.F();
            this.T = (boolean)1;
            this.i = System.nanoTime() + 160000000L;
         });
         this.B.put(var11, var12);
         this.k.add(var12);
         this.k.add(var11);
      }

      this.e();
      if (var1 >= 0 && var2 != 0 && !this.n.isEmpty()) {
         p7 var14 = this.n.get(Math.min(var1, this.n.size() - 1));

         for (p7 var16 : this.n) {
            if (this.v.get(var16) == var4) {
               var14 = var16;
               break;
            }
         }

         this.W = var3 != 0 ? this.B.get(var14) : var14;
         this.W.A = this.W.h = (boolean)1;
         this.focusChanged(this.W);
      } else if (var1 >= 0 && var2 != 0) {
         this.W = this.M;
         this.W.A = this.W.h = (boolean)1;
      }
   }

   private boolean F() {
      LarpClient.y().v().a();
      this.f = (boolean)0;
      return true;
   }

   private void w() {
      LarpClient var1 = LarpClient.y();
      mo var2 = var1.v();
      rq var3 = var2.E().e;
      boolean var4 = var2.f(!var3.S(), var1.e().X());
      if (!var4) {
         this.w = "Israel mode could not be changed. Click to retry; see the client log.";
         r4.m("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.w.startsWith("Israel mode")) {
         this.w = "";
      }

      if (this.x != null && this.P == null) {
         this.I.P();
      }
   }

   private void z() {
      LarpClient var1 = LarpClient.y();
      mo var2 = var1.v();
      if (!var2.q(!var2.E().X.r(), var1.e().X())) {
         this.w = "Larp Preset could not be changed. Click to retry; see the client log.";
         r4.m("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.w.startsWith("Larp Preset")) {
         this.w = "";
      }

      if (this.x != null && this.P == null) {
         this.I.P();
      }
   }

   private Vector2f T(double var1, double var3) {
      return new Matrix3x2f(this.c).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private MouseButtonEvent z(MouseButtonEvent var1) {
      Vector2f var2 = this.T(var1.x(), var1.y());
      return new MouseButtonEvent(var2.x, var2.y, var1.buttonInfo());
   }

   private pb F() {
      return this.P != null ? this.P : (this.x != null ? this.I : null);
   }

   private boolean R() {
      return this.P == null && this.x != null && !this.p && !this.F.columns();
   }

   private int B() {
      return this.F.moduleListRight(this.d);
   }

   private double O() {
      return this.F.detachedInspector() ? Math.clamp(this.d * 2.0 - 1.0, 0.0, 1.0) : this.d;
   }

   private int w() {
      return this.F.detachedInspector() ? this.F.top() : this.F.bodyTop();
   }

   private int i() {
      return this.F.detachedInspector() ? this.F.top() + this.F.height() : this.F.bottom();
   }

   private void e() {
      fs var1 = LarpClient.y().v().E();
      if (!this.j) {
         ry var2 = D(this.minecraft);
         if (!var2.equals(this.s)) {
            this.s = var2;
            r4.g("GUI", "ClickGUI size {}%: workspace {}x{} / factor {}", Math.round(var1.Y * 100.0), this.s.width(), this.s.height(), this.s.scale());
         }
      }

      double var14 = this.g.y(this.H ? 0.0 : 1.0, var1.U);
      this.d = this.z.y(this.R.equals("Modules") && this.x != null && !this.p ? 1.0 : 0.0, 270);
      this.F = (this.R.equals("Modules") ? mj.calculateModules(this.s.width(), this.s.height(), this.d) : mj.calculate(this.s.width(), this.s.height()))
         .offset(0, var1.s.equals("Slide") ? (int)Math.round((1.0 - var14) * 8.0) : 0);
      int var4 = this.F.navigationPitch(this.J.size());
      this.O = Math.clamp(this.O, 0, this.F.navigationScrollLimit(this.J.size()));
      int var5 = this.F.navigationTop() - this.O;

      for (int var6 = 0; var6 < this.J.size(); var6++) {
         if (var6 == lw.values().length) {
            var5 += this.F.navigationGap();
         }

         p7 var7 = this.J.get(var6);
         var7.bounds(this.F.left() + 4, var5, this.F.navigationRight() - this.F.left() - 4);
         var7.t = 1.0;
         var7.O = var4 - 1;
         var5 += var4;
         var7.R = this.R.equals("Modules") ? var7.J.equals(this.D.C) : var7.J.equals(this.R);
      }

      this.h.bounds(this.F.left() + 4, this.F.accountY(), this.F.navigationRight() - this.F.left() - 4);
      this.h.O = 30;
      this.y.bounds(this.F.presetLeft(), this.F.themeSwitchY(), this.F.presetWidth());
      this.y.O = this.F.themeSwitchHeight();
      this.y.I = LarpClient.y().v().E().e.S();
      this.l.bounds(this.F.larpPresetLeft(), this.F.themeSwitchY(), this.F.presetWidth());
      this.l.O = this.F.themeSwitchHeight();
      this.l.I = LarpClient.y().v().E().X.r();
      this.M.F = this.R.equals("Modules");
      this.M.bounds(this.F.contentX(), this.F.top() + 12, this.F.right() - this.F.contentX());
      this.M.O = 26;
      this.U = this.u.V();
      int var16 = Math.max(1, this.F.bottom() - this.F.bodyTop());
      int var17 = this.F.moduleGridColumns();
      int var8 = 0;
      int var9 = 0;
      int var10 = var17 * this.F.moduleCardWidth() + (var17 - 1) * 6;

      for (p7 var12 : this.n) {
         p8 var13 = this.e.get(var12);
         if (var13 != null) {
            if (var8 > 0) {
               var8 = 0;
               var9 += 38;
            }

            var13.bounds(this.F.moduleCardX(0, this.U), var9, var10);
            var9 += var13.O;
         }

         var12.bounds(this.F.moduleCardX(var8, this.U), var9, this.F.moduleCardWidth());
         if (++var8 == var17) {
            var8 = 0;
            var9 += 38;
         }
      }

      this.r = var9 + (var8 > 0 ? 38 : 0);
      this.o = Math.clamp(this.o, 0, Math.max(0, this.r - var16));
      var5 = this.F.bodyTop() - (int)Math.round(this.q.y(this.o, 150));

      for (p7 var20 : this.n) {
         var20.F = (boolean)(this.R.equals("Modules") && !this.R() ? 1 : 0);
         var20.L += var5;
         var20.O = 34;
         this.B.get(var20).j(var20);
         p8 var22 = this.e.get(var20);
         if (var22 != null) {
            var22.F = var20.F;
            var22.L += var5;
         }
      }

      this.m.F = (boolean)(this.R.equals("Modules") && this.x != null ? 1 : 0);
      mj var19 = this.F.moduleInspector(this.d);
      this.m.bounds(var19.right() - 30, var19.bodyTop() + 1, 22);
      this.m.O = 22;
      if (this.P != null) {
         this.P.i(this.F);
      } else if (this.x != null) {
         mj var21 = this.F.moduleInspector(this.d);
         this.I.i(var21, this.F.moduleInspectorClipLeft(this.d), var21.right());
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = h_.U();
      this.M();
      this.e();
      double var7 = this.g.V();
      fs var9 = LarpClient.y().v().E();
      boolean var10 = WorldMenuModule.f(this);
      jk.W(var9.Na);
      if (!var10 && this.minecraft.level != null && var9.C.equals("Everywhere")) {
         jk.F();
      }

      if (!var10) {
         V(var1, this.width, this.height, var7, var9);
      }

      this.c.set(this.s.transform()).mul(fm.q(var9.s, var7, this.F.left() + this.F.width() / 2.0, this.F.top() + this.F.height() / 2.0));
      if (this.A != null) {
         r3.h var11 = this.H ? this.A.Z() : this.A.p(System.nanoTime(), this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive(), ys.O());
         this.c
            .set(this.s.transform())
            .mul(var11.matrix(this.s.width() / 2.0, this.s.height() / 2.0))
            .mul(fm.q(var9.s, var7, this.F.left() + this.F.width() / 2.0, this.F.top() + this.F.height() / 2.0));
      }

      Vector2f var117 = this.T(var2, var3);
      var2 = Math.round(var117.x);
      var3 = Math.round(var117.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.c);

      try (
         rv.s var12 = rv.v(var9.Nw, var10 ? 1.0 : var9.w, var9.b, var9.e());
         j_.j var13 = j_.v();
         r var14 = com.larpclient.r.p(fm.j(var9.s, var7));
      ) {
         if (!var10 && rv.D().accentGlow()) {
            ll.C(var1, this.F.left(), this.F.top(), this.F.width(), this.F.height());
         }

         ll.h(var1, this.F.left(), this.F.top(), this.F.width(), this.F.height());
         ll.d(var1, this.F.sunkenX(), this.F.sunkenY(), this.F.sunkenWidth(), this.F.sunkenHeight());
         this.t(var1, this.F() != null && this.F().m() ? -1 : var2, var3);
         ll.v(var1, this.F.left(), this.F.top(), this.F.width(), this.F.height());
         this.y.render(var1, this.F() != null && this.F().m() ? -1 : var2, var3);
         this.l.render(var1, this.F() != null && this.F().m() ? -1 : var2, var3);
         if (this.P != null) {
            this.P.extractRenderState(var1, var2, var3, var4);
         } else {
            this.M.render(var1, var2, var3);
            var1.enableScissor(this.F.contentX(), this.F.bodyTop(), this.B(), this.F.bottom());

            try (r var15 = com.larpclient.r.p(0.35 + 0.65 * this.U)) {
               for (p8 var17 : this.e.values()) {
                  if (var17.L + var17.O > this.F.bodyTop() && var17.L < this.F.bottom()) {
                     var17.render(var1, -1, var3);
                  }
               }

               for (p7 var127 : this.n) {
                  Module var18 = this.v.get(var127);
                  var127.R = (boolean)(var18 == this.x ? 1 : 0);
                  var127.I = var18.isEnabled();
                  var127.y = this.S.isBlank() ? var18.getDescription() : lw.k(var18).C + " · " + var18.getDescription();
                  if (var127.L + var127.O > this.F.bodyTop() && var127.L < this.F.bottom()) {
                     int var19 = var3 >= this.F.bodyTop() && var3 < this.F.bottom() && var2 >= this.F.contentX() && var2 < this.B() ? var2 : -1;
                     var127.render(var1, var19, var3);
                     this.B.get(var127).render(var1, var19, var3);
                  }
               }

               if (this.n.isEmpty() && !this.R()) {
                  String var123 = this.D == lw.l && this.S.isBlank() ? "Star a module to add it here." : "No matches";
                  j_.E(var1, j_.S(var123, this.B() - this.F.contentX() - 16), this.F.moduleListLeft(this.U) + 8, this.F.bodyTop() + 12, rv.G0);
               }
            } finally {
               var1.disableScissor();
            }

            if (!this.R()) {
               ll.g(
                  var1, this.F.moduleScrollbarX(this.d), this.F.bodyTop(), Math.max(1, this.F.bottom() - this.F.bodyTop()), this.r, (int)Math.round(this.q.V())
               );
            }

            if (this.x != null) {
               mj var118 = this.F.moduleInspector(this.d);
               int var124 = this.F.columns() ? var118.inspectorX() : var118.contentX();
               double var128 = this.O();
               if (this.F.detachedInspector()) {
                  try (r var133 = com.larpclient.r.p(var128)) {
                     ll.z(var1, var124, var118.top(), var118.right() - var124, var118.height());
                  }
               }

               var1.enableScissor(this.F.moduleInspectorClipLeft(this.d), this.w(), var118.right(), this.i());

               try (r var134 = com.larpclient.r.p(var128)) {
                  if (!this.F.detachedInspector()) {
                     ll.w(var1, var124, this.F.bodyTop(), var118.right() - var124, this.F.bottom() - this.F.bodyTop());
                  }

                  this.I.extractRenderState(var1, var2, var3, var4);
                  this.m.render(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }

               var1.enableScissor(this.F.moduleInspectorClipLeft(this.d), var118.top() + 12, var118.right(), this.i());

               try (r var135 = com.larpclient.r.p(var128)) {
                  this.I.S(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }
            }

            for (p7 var125 : this.n) {
               pp var129 = this.B.get(var125);
               boolean var131 = this.accepts(var129, var2, var3);
               if ((this.F() == null || !this.F().m())
                  && var129.F
                  && (var131 || var129.A && var129.h)
                  && var129.L >= this.F.bodyTop()
                  && var129.L + var129.O <= this.F.bottom()) {
                  var129.A(var1, this.s.width(), this.s.height());
                  break;
               }
            }
         }

         pb var120 = this.F();
         String var126 = s(var120, var120 == null ? "" : var120.E(var2, var3));
         String var130 = LarpClient.y().v().m();
         if (!var130.isBlank()) {
            var126 = var130;
         }

         if (!this.w.isBlank()) {
            var126 = this.w;
         }

         if (var126.isBlank() && (this.y.contains(var2, var3) || this.y.A && this.y.h)) {
            var126 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var126.isBlank() && (this.l.contains(var2, var3) || this.l.A && this.l.h)) {
            var126 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var126.isBlank()) {
            var126 = "Enter opens settings · Space toggles · Tab navigates · Right click resets a setting";
         }

         int var132 = this.F.profileLeft();
         String var136 = "Profile: " + LarpClient.y().v().o();
         j_.L(var1, j_.d(var136, this.F.profileWidth(), j_.m.U), var132, this.F.profileY(), rv.G0, j_.m.U);
         j_.L(var1, j_.d(var126, this.F.helpWidth(), j_.m.U), this.F.helpLeft(), this.F.helpY(), this.w.isBlank() ? rv.G0 : -24915, j_.m.U);
         double var121 = fm.E(var9.s, var7);
         if (var121 > 0.01) {
            ow.P(var1, this.F.left(), this.F.top(), this.F.width(), this.F.height(), rv.T, oh.i(-1, var121));
         }

         if (var10) {
            WorldMenuModule.A(var1, this, this.F, var2, var3);
         }
      } finally {
         var1.pose().popMatrix();
      }

      h_.n("ClickGUI", var5);
   }

   public double[] a() {
      float var1 = this.s.scale();
      return new double[]{this.F.left() * var1, this.F.top() * var1, (this.F.left() + this.F.width()) * var1, (this.F.top() + this.F.height()) * var1};
   }

   public Screen Y() {
      return this.X;
   }

   public boolean y() {
      return this.H && this.g.V() <= 0.0;
   }

   public boolean h(double var1, double var3) {
      Vector2f var5 = this.T(var1, var3);
      if (var5.x >= this.F.left() && var5.x < this.F.left() + this.F.width() && var5.y >= this.F.top() && var5.y < this.F.top() + this.F.height()) {
         return true;
      }

      if (this.x != null && this.F.detachedInspector() && this.O() > 0.2) {
         mj var6 = this.F.moduleInspector(this.d);
         if (var5.x >= var6.inspectorX() && var5.x < var6.right() && var5.y >= var6.top() && var5.y < var6.top() + var6.height()) {
            return true;
         }
      }

      return WorldMenuModule.B(this.F, var5.x, var5.y);
   }

   public boolean M() {
      pb var1 = this.F();
      return Y(this.W) || var1 != null && Y(var1.y());
   }

   private static boolean Y(oq var0) {
      return var0 instanceof p_ var2 && var2.A || var0 instanceof p9 var1 && var1.C();
   }

   public void v() {
      pb var1 = this.F();
      oq var2 = var1 == null ? null : var1.y();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.W != null && this.W != var2) {
         this.W.key(256);
      }

      this.E();
   }

   public void E() {
      pb var1 = this.F();
      if (var1 != null) {
         var1.M();
      }

      if (this.W != null) {
         this.W.blur();
      }

      this.W = this.L = null;
   }

   private static void V(GuiGraphicsExtractor var0, int var1, int var2, double var3, fs var5) {
      int var6 = oh.i(-16777216, var5.E);
      switch (var5.z) {
         case "None":
            break;
         case "Gradient":
            int var11 = oh.S(var6, var3);
            int var10 = oh.S(oh.j(var6, oh.i(rv.e, 0.5), 0.35), var3);
            fr.Y(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            y4.a var9 = y4.a.valueOf(var5.z.toUpperCase(Locale.ROOT));
            if (!y4.x(var0, var1, var2, var3, rv.e, rv.GQ, var5.Nw, var5.o, var9, var5.E)) {
               fr.k(var0, 0, 0, var1, var2, oh.S(var6, var3));
            }
            break;
         default:
            fr.k(var0, 0, 0, var1, var2, oh.S(var6, var3));
      }
   }

   static String s(pb var0, String var1) {
      return var0 instanceof pz var2 && !var2.V.isBlank() ? var2.V : var1;
   }

   private void t(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = this.F.contentX() - this.F.left() - 10;
      var1.enableScissor(this.F.left(), this.F.top(), this.F.contentX() - 10, this.F.navigationTop());

      try {
         var1.blit(RenderPipelines.GUI_TEXTURED, dm.Q(), this.F.left() + 7, this.F.top() + 10, 0.0F, 0.0F, 22, 22, 64, 64, 64, 64, com.larpclient.r.D(-1));

         try (r var5 = com.larpclient.r.p(1.0)) {
            int var6 = this.F.left() + 35;
            int var7 = var4 - 39;
            j_.L(var1, j_.d("Larp Client", var7, j_.m.g), var6, this.F.top() + 10, rv.P, j_.m.g);
            String var8 = "Version ";
            int var9 = var6 + j_.o(var8, j_.m.U);
            j_.L(var1, var8, var6, this.F.top() + 24, rv.r, j_.m.U);
            j_.L(var1, j_.d(LarpClient.L, Math.max(1, var6 + var7 - var9), j_.m.U), var9, this.F.top() + 24, rv.a(var9, rv.w), j_.m.U);
            P(var1, "FEATURES", this.F.left() + 12, this.F.navigationTop() - 12, this.F.navigationRight());
         }
      } finally {
         var1.disableScissor();
      }

      var1.enableScissor(this.F.left(), this.F.navigationTop(), this.F.contentX() - 10, this.F.navigationBottom());

      try {
         try (r var49 = com.larpclient.r.p(1.0)) {
            P(var1, "OTHERS", this.F.left() + 12, this.J.get(lw.values().length).L - 12, this.F.navigationRight());
         }

         this.X(var1);

         for (p7 var52 : this.J) {
            var52.render(var1, var3 >= this.F.navigationTop() && var3 < this.F.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.disableScissor();
      }

      if (this.F.navigationScrollLimit(this.J.size()) > 0) {
         ll.g(
            var1,
            this.F.contentX() - 12,
            this.F.navigationTop(),
            this.F.navigationBottom() - this.F.navigationTop(),
            this.J.size() * this.F.navigationPitch(this.J.size()) + this.F.navigationGap(),
            this.O
         );
      }

      var1.enableScissor(this.F.left(), this.F.accountY(), this.F.contentX() - 10, this.F.accountY() + this.h.O);

      try {
         double var51 = this.h.a(var2, var3);
         if (var51 > 0.001) {
            fr.S(var1, this.h.B, this.h.L, this.h.v, this.h.O, rv.Gj, oh.i(rv.P, var51 * 0.045));
         }

         o var53 = com.larpclient.o.current();
         if (!Z.F(var1, this.F.left() + 8, this.h.L + 5, 20)) {
            this.wD.n(var1, this.F.left() + 8, this.h.L + 5, 20);
         }

         fr.J(var1, this.F.left() + 25, this.h.L + 24, 4, rv.w);
         fr.J(var1, this.F.left() + 25, this.h.L + 24, 3, var53.state().x());

         try (r var54 = com.larpclient.r.p(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.minecraft.getUser().getName();
            j_.x(var1, j_.V(var10, var55, j_.w.Q), this.F.left() + 34, this.h.L + 6, rv.P, j_.w.Q);
            j_.L(var1, j_.d(var53.state().u(), var55, j_.m.U), this.F.left() + 34, this.h.L + 18, rv.G0, j_.m.U);
         }

         if (this.h.A && this.h.h) {
            ll.w(var1, this.h.B, this.h.L, this.h.v, this.h.O, rv.Gj);
         }
      } finally {
         var1.disableScissor();
      }
   }

   private static void P(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      j_.L(var0, var1, var2, var3, oh.i(rv.G0, 0.8), j_.m.u);
   }

   private void X(GuiGraphicsExtractor var1) {
      p7 var2 = null;

      for (p7 var4 : this.J) {
         if (var4.R) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.V.y(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.L - this.F.navigationTop() + this.O;
         if (!this.Y) {
            this.a.c(var5);
            this.Y = (boolean)1;
         }

         this.a.y(var5, 200);
      }

      if (this.Y && !(var9 < 0.01)) {
         p7 var10 = this.J.getFirst();
         float var6 = this.F.navigationTop() - this.O + (float)this.a.V();
         int var7 = rv.a(var10.B, rv.w);
         int var8 = rv.Gj;
         ll.S(var1, var10.B, var6, var10.v, var10.O, var8, 9.0F, oh.i(var7, 0.22 * var9));
         ow.Z(var1, var10.B, var6, var10.v, var10.O, var8, oh.i(var7, 0.26 * var9), oh.i(rv.GQ, 0.08 * var9));
         ow.y(var1, var10.B, var6, var10.v, var10.O, var8, oh.i(var7, 0.35 * var9), oh.i(var7, 0.35 * var9));
      }
   }

   @Override
   protected boolean accepts(oq var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.J.contains(var1)) {
         return var4 >= this.F.navigationTop() && var4 < this.F.navigationBottom();
      } else {
         return !this.n.contains(var1) && !this.B.containsValue(var1)
            ? true
            : !this.R() && var4 >= this.F.bodyTop() && var4 < this.F.bottom() && var2 >= this.F.contentX() && var2 < this.B();
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.H) {
         return true;
      }

      this.j = (boolean)1;
      var1 = this.z(var1);
      this.e();
      pb var3 = this.F();
      oq var4 = var3 == null ? null : var3.y();
      if (var4 != null && var4.captureMouse(r7.y(var1))) {
         return true;
      }

      if (r7.y(var1) > 1 && LarpClient.y().b() == pe.h(r7.y(var1))) {
         this.onClose();
         return true;
      }

      if (var3 != null && var3.m()) {
         return var3.mouseClicked(var1, var2);
      }

      if (WorldMenuModule.H(this, var1.x(), var1.y(), r7.y(var1), this.F)) {
         if (var3 != null) {
            var3.M();
         }

         return true;
      } else {
         for (p7 var6 : this.J) {
            if (this.accepts(var6, var1.x(), var1.y())) {
               if (var6.R && this.R.equals("Modules")) {
                  return var6.click(var1.x(), var1.y(), r7.y(var1));
               }

               return super.mouseClicked(var1, var2);
            }
         }

         if (this.O() > 0.2 && this.m.contains(var1.x(), var1.y()) && var1.x() < this.F.moduleInspector(this.d).right()) {
            return super.mouseClicked(var1, var2);
         }

         int var8 = this.P != null
            ? this.F.containsWorkspace(var1.x(), var1.y())
            : (
               this.O() > 0.2
                     && (!this.p || this.F.columns())
                     && var1.x() >= this.F.moduleInspectorClipLeft(this.d)
                     && var1.x() < this.F.moduleInspector(this.d).right()
                     && var1.y() >= this.w()
                     && var1.y() < this.i()
                  ? 1
                  : 0
            );
         if (var3 != null && var8 != 0) {
            if (this.W != null) {
               this.W.blur();
            }

            this.W = this.L = null;
            return this.p && this.P == null ? true : var3.mouseClicked(var1, var2);
         } else {
            if (var3 != null) {
               var3.M();
            }

            return super.mouseClicked(var1, var2);
         }
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.H) {
         return true;
      } else {
         Vector2f var9 = this.T(var1, var3);
         var1 = var9.x;
         var3 = var9.y;
         this.e();
         pb var10 = this.F();
         if ((var10 == null || !var10.m()) && this.F.containsNavigation(var1, var3) && var3 >= this.F.navigationTop() && var3 < this.F.navigationBottom()) {
            this.O -= (int)(var7 * 28.0);
            this.e();
            return true;
         } else if (var10 != null && !this.p && (!this.F.detachedInspector() || this.O() > 0.2) && var10.mouseScrolled(var1, var3, var5, var7)) {
            return true;
         } else if (this.R.equals("Modules") && !this.R() && var1 >= this.F.contentX() && var1 < this.B() && var3 >= this.F.bodyTop() && var3 < this.F.bottom()
            )
          {
            this.o -= (int)(var7 * 27.0);
            this.e();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(oq var1) {
      if (this.J.contains(var1)) {
         if (var1.L < this.F.navigationTop()) {
            this.O = this.O - (this.F.navigationTop() - var1.L);
         } else if (var1.L + var1.O > this.F.navigationBottom()) {
            this.O = this.O + (var1.L + var1.O - this.F.navigationBottom());
         }

         this.e();
      }

      if (this.n.contains(var1) || this.B.containsValue(var1)) {
         for (Entry var3 : this.B.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (oq)var3.getKey();
               break;
            }
         }

         int var4 = this.e.containsKey(var1) ? this.e.get(var1).L : var1.L;
         if (var4 < this.F.bodyTop()) {
            this.o = this.o - (this.F.bodyTop() - var4);
         } else if (var1.L + var1.O > this.F.bottom()) {
            this.o = this.o + (var1.L + var1.O - this.F.bottom());
         }

         this.q.c(this.o);
         this.e();
      }
   }

   static boolean z(int var0, oq var1, oq var2, Runnable var3) {
      if (var0 != 256) {
         return false;
      }

      if (var1 != null) {
         var1.key(var0);
      }

      if (var2 != null && var2 != var1) {
         var2.key(var0);
      }

      var3.run();
      return true;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.H) {
         return true;
      }

      pb var2 = this.F();
      if (z(r7.g(var1), var2 == null ? null : var2.y(), this.W, this::onClose)) {
         return true;
      }

      if (this.M != null
         && this.R.equals("Modules")
         && this.W == this.M
         && (var2 == null || !var2.m())
         && r7.g(var1) == 258
         && (var1.modifiers() & 1) == 0
         && this.M.q()) {
         return true;
      }

      if (r7.g(var1) != 258 || var2 != null && var2.m()) {
         return var2 == null || var2.y() == null && !var2.m() ? super.keyPressed(var1) : var2.keyPressed(var1);
      }

      ArrayList var3 = new ArrayList();
      this.k.stream().filter(var0 -> var0.F && var0.x).forEach(var3::add);
      if (var2 != null && !this.p && (!this.F.detachedInspector() || this.O() > 0.2)) {
         var3.addAll(var2.S());
      }

      oq var4 = var2 != null && var2.y() != null ? var2.y() : this.W;
      int var5 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
      if (this.W != null) {
         this.W.blur();
      }

      this.W = null;
      if (var2 != null) {
         var2.M();
      }

      if (!var3.isEmpty()) {
         oq var7 = (oq)var3.get(Math.floorMod(var6 + var5, var3.size()));
         if (this.k.contains(var7)) {
            this.W = var7;
            var7.A = var7.h = (boolean)1;
            this.focusChanged(var7);
         } else {
            var2.x(var7);
         }
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.H) {
         return true;
      }

      pb var2 = this.F();
      return var2 != null && var2.y() != null ? var2.charTyped(var1) : super.charTyped(var1);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (!this.H && !this.p) {
         Vector2f var6 = this.T(var1.x(), var1.y());
         Vector2f var7 = this.T(var1.x() - var2, var1.y() - var4);
         var1 = this.z(var1);
         var2 = var6.x - var7.x;
         var4 = var6.y - var7.y;
         pb var8 = this.F();
         return var8 != null && var8.mouseDragged(var1, var2, var4) || super.mouseDragged(var1, var2, var4);
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      this.j = (boolean)0;
      var1 = this.z(var1);
      if (this.F() != null) {
         this.F().mouseReleased(var1);
      }

      return super.mouseReleased(var1);
   }

   public void tick() {
      this.M();
      if (this.T && System.nanoTime() >= this.i) {
         this.T = (boolean)0;
         this.s();
      }

      if (!this.H && this.F() != null) {
         this.F().tick();
      }
   }

   void i() {
      if (this.D != lw.l) {
         fc.K = this.D.name();
      }

      fc.J = new fc.t(this.R, this.x == null ? "" : this.x.getName(), this.S, this.o, this.I.e);
   }

   public void removed() {
      if (this.A != null && !this.H) {
         this.A.z();
      }

      if (this.P != null) {
         this.P.suspend();
      }

      this.I.suspend();
      this.i();
      mo var1 = LarpClient.y().v();
      if (this.f) {
         this.F();
      }

      var1.A();
      r4.g("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}

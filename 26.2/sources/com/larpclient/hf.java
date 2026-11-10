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

public final class hf extends h3 {
   private final Screen j;
   private final List<jx> m = new ArrayList<>();
   private final List<jx> t = new ArrayList<>();
   private final Map<jx, Module> P = new IdentityHashMap<>();
   private final Map<jx, j_> B = new IdentityHashMap<>();
   private final Map<jx, j1> vu = new IdentityHashMap<>();
   private int K;
   private boolean f;
   private boolean A;
   private long x;
   private final hs d = new hs();
   private final hy V = new hy();
   private hd E;
   private h6 T;
   private uc vW;
   private tq n;
   private pz Y;
   private int k;
   private Module R;
   private String I = "Modules";
   private String L = "";
   private String c = "";
   private boolean z;
   private boolean i;
   private boolean w;
   private static hf h;
   private Object S;
   private lg O;
   private int C;
   private int U;
   private double v;
   private double s = 1.0;
   private boolean N;
   private boolean G;
   private boolean Q;
   private final jp M = new jp(0.0);
   private final jp g = new jp(1.0);
   private j9 a;
   private jx y;
   private jx q;
   private jx e;
   private jx u;
   private final b6 r = new b6();
   private static final wl l = new wl();
   private final jp W = new jp(0.0);
   private final jp X = new jp(0.0);
   private final jp J = new jp(0.0);
   private final jp b = new jp(0.0);
   private boolean H;
   private kt Z;
   private final Matrix3x2f D = new Matrix3x2f();

   public hf() {
      this(null);
   }

   public hf(Screen var1) {
      super("Larp client");
      this.j = var1;
      this.Y = LarpClient.R().o().g().x ? pz.s : pz.m(c7.c);
   }

   public void x() {
      this.Z = new kt();
   }

   public static hf X(Screen var0, String var1, boolean var2) {
      hf var3 = new hf(var0);
      var3.N = (boolean)1;
      var3.I = "Configs";
      var3.E = new hd(var3);
      if (var1 != null) {
         var3.E.Q.j = var1;
      }

      var3.E.c = var2;
      return var3;
   }

   public static hf v(Screen var0) {
      hf var1 = X(var0, null, false);
      var1.z = (boolean)1;
      return var1;
   }

   public static hf Z(Screen var0, String var1) {
      hf var2 = new hf(var0);
      var2.N = (boolean)1;
      var2.I = "Settings";
      var2.V.p(var1);
      return var2;
   }

   public static hf o(Screen var0, m4.p var1) {
      hf var2 = Z(var0, "HUD");
      var2.V.y(var1);
      return var2;
   }

   Minecraft H() {
      return this.minecraft;
   }

   boolean m(hd var1) {
      return this.T == var1;
   }

   public void onClose() {
      if (!this.G) {
         if (this.T != null) {
            this.T.suspend();
         }

         this.d.a();
         sr.v(sr.e.s);
         this.G = (boolean)1;
         this.W.k(0.0, this.m());
         if (!WorldMenuModule.F(this)) {
            this.S = this.minecraft.level;
            h = this;
            this.O = this.Q();
            if (this.j instanceof mh && !LarpClient.R().o().g().t) {
               com.larpclient.h.A("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.minecraft.gui.setScreen(new TitleScreen());
            } else {
               this.minecraft.gui.setScreen(this.j);
            }
         }
      }
   }

   private int m() {
      return LarpClient.R().o().g().c;
   }

   private lg Q() {
      hb var1 = LarpClient.R().o().g();
      String var2 = var1.l;
      if (!var2.equals("Reverse") && var1.L) {
         ArrayList var3 = new ArrayList();
         float var4 = this.D.m00();
         Vector2f var5 = this.D.transformPosition(this.vW.left(), this.vW.top(), new Vector2f());
         var3.add(new lg.s(var5.x, var5.y, this.vW.width() * var4, this.vW.height() * var4, ho.x * var4));
         if (this.R != null && this.T == null && this.vW.detachedInspector() && this.C() > 0.5) {
            uc var6 = this.vW.moduleInspector(this.v);
            Vector2f var7 = this.D.transformPosition(var6.inspectorX(), var6.top(), new Vector2f());
            var3.add(new lg.s(var7.x, var7.y, (var6.right() - var6.inspectorX()) * var4, var6.height() * var4, ho.x * var4));
         }

         return lg.M(var2, var3, this.m());
      } else {
         return null;
      }
   }

   public static boolean m() {
      return h != null;
   }

   public static void V(GuiGraphicsExtractor var0, float var1) {
      hf var2 = h;
      if (var2 != null) {
         Minecraft var3 = Minecraft.getInstance();
         lg var4 = var2.O;
         if (var3.gui.screen() == var2.j
            && var3.level == var2.S
            && var3.gui.overlay() == null
            && var2.width == var0.guiWidth()
            && var2.height == var0.guiHeight()
            && var2.n.equals(t(var3))
            && (var4 == null ? !(var2.W.B() <= 0.0) : !var4.d())) {
            try {
               var0.nextStratum();
               if (var4 != null) {
                  hb var5 = LarpClient.R().o().g();

                  try (ho.y var6 = ho.K(var5.f, var5.r, var5.W)) {
                     W(var0, var0.guiWidth(), var0.guiHeight(), 1.0 - var4.T(), var5);
                  }

                  var4.C(var0);
               } else {
                  var2.extractRenderState(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var11) {
               A();
               com.larpclient.h.m("GUI", "Window exit rendering failed; input remains released", var11);
            }
         } else {
            if (var4 != null) {
               com.larpclient.h.U("GUI", "Close effect {}", var4.d() ? "finished" : "stopped by a screen, world or window size change");
            }

            A();
         }
      }
   }

   private static void A() {
      if (h != null && h.O != null) {
         h.O = null;
         lg.S();
      }

      h = null;
   }

   private static tq t(Minecraft var0) {
      return tq.paired(var0.getWindow(), LarpClient.R().o().g().q);
   }

   protected void init() {
      this.n = t(this.minecraft);
      this.D.set(this.n.transform());
      A();
      if (!this.N) {
         c7.x var1 = c7.D;
         this.I = var1.page();
         this.L = var1.query();
         this.C = var1.moduleScroll();
         this.R = LarpClient.R().z().d(var1.module());
         this.R = ChooseASkyEffectAndAdjustItsAppearanceModule.p(this.R, LarpClient.R().z().h(), LarpClient.R().o().g());
         if (this.R == null && !var1.module().isEmpty()) {
            com.larpclient.h.A("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.R != null && this.L.isBlank() && this.Y != pz.s) {
            this.Y = pz.B(this.R);
         }

         this.N = (boolean)1;
      }

      if (!pz.Y().contains(this.Y)) {
         this.Y = pz.F;
      }

      if (this.R != null && !(this.R instanceof ChooseASkyEffectAndAdjustItsAppearanceModule) && com.larpclient.module.c.W(this.R)) {
         this.R = null;
      }

      this.vW = this.I.equals("Modules")
         ? uc.calculateModules(this.n.width(), this.n.height(), this.R == null ? 0.0 : 1.0)
         : uc.calculate(this.n.width(), this.n.height());
      this.G = (boolean)0;
      this.W.F(this.Z == null ? 0.0 : 1.0);
      this.W.k(1.0, this.m());
      if (!this.i) {
         this.i = (boolean)1;
         sr.v(sr.e.X);
      }

      this.Q = (boolean)0;
      this.X.F(this.R == null ? 0.0 : 1.0);
      this.M.F(this.C);
      this.z();
      this.M.F(this.C);
      if (this.R != null) {
         int var2 = this.d.y == null ? c7.D.settingScroll() : this.d.Y;
         this.d.P(this, this.vW);
         this.d.v(this.R);
         this.d.Y = var2;
         this.X.F(1.0);
      }

      this.L();
      com.larpclient.h.U(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.width,
         this.height,
         this.minecraft.getWindow().getGuiScale(),
         this.n.width(),
         this.n.height(),
         this.n.scale(),
         this.I
      );
   }

   private void L() {
      this.T = switch (this.I) {
         case "Configs" -> {
            if (this.E == null) {
               this.E = new hd(this);
            }

            yield this.E;
         }
         case "Settings" -> this.V;
         default -> null;
      };
      if (this.T != null) {
         this.T.P(this, this.vW);
      }

      if (this.z && this.T == this.E) {
         this.z = (boolean)0;
         this.E.i("Discover");
      }
   }

   private void o(String var1) {
      if (!this.I.equals(var1)) {
         sr.v(sr.e.T);
      }

      if (this.Q) {
         this.R = null;
         this.Q = (boolean)0;
         this.X.F(0.0);
      }

      if (this.T != null) {
         this.T.suspend();
      }

      this.d.suspend();
      this.I = var1;
      this.z();
      this.L();
      if (this.T == null && this.R != null) {
         this.d.P(this, this.vW);
         this.d.v(this.R);
      }

      this.r();
      com.larpclient.h.U("GUI", "Destination switched to {}", var1);
   }

   void N() {
      this.V.p("HUD");
      this.V.y(null);
      this.V.Y = 0;
      this.o("Settings");
   }

   void T() {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         this.r();
         this.minecraft.gui.setScreen(new h7(this));
      } else {
         this.c = "Join a world to edit the HUD.";
      }
   }

   void d() {
      if (this.R != null && !this.Q) {
         sr.v(sr.e.d);
         this.Q = (boolean)1;
         this.d.a();
         this.X.k(0.0, 270);
      }
   }

   private void U() {
      if (this.Q && this.X.B() == 0.0) {
         this.R = null;
         this.Q = (boolean)0;
         this.d.suspend();
      }
   }

   private void z(pz var1) {
      boolean var2 = var1.M(this.Y, () -> {
         if (this.I.equals("Modules")) {
            sr.v(sr.e.T);
         }

         this.g.Y(220);
         hb var2x = LarpClient.R().o().g();
         if (var2x.x != (var1 == pz.s ? 1 : 0)) {
            var2x.x = (boolean)(var1 == pz.s ? 1 : 0);
            this.A = (boolean)1;
            this.V();
         }

         this.Y = var1;
         this.C = 0;
         this.M.F(0.0);
         this.R = null;
         this.Q = (boolean)0;
         this.L = "";
         this.o("Modules");
      });
      if (!var2 && !this.I.equals("Modules")) {
         this.o("Modules");
      }
   }

   private void z() {
      this.u();
      this.m.clear();
      this.t.clear();
      this.P.clear();
      this.B.clear();
      List var1 = pz.Y();
      this.k = var1.size();

      for (pz var3 : var1) {
         jx var4 = new jx(var3.u, () -> this.z(var3));
         var4.q = null;
         var4.l = var3.D;
         var4.g = (boolean)1;
         var4.f = (boolean)1;
         this.m.add(var4);
         this.p.add(var4);
      }

      for (String var5 : new String[]{"Editor", "Configs", "Settings"}) {
         jx var6 = new jx(var5, () -> {
            if (var5.equals("Editor")) {
               this.T();
            } else {
               this.o(var5);
            }
         });
         var6.q = null;
         var6.l = var5.equals("Editor") ? "panels-top-left" : (var5.equals("Configs") ? "folder" : "settings");
         var6.g = (boolean)1;
         var6.f = (boolean)1;
         this.m.add(var6);
         this.p.add(var6);
      }

      this.u = new jx("Account", () -> {
         this.V.p("Account");
         this.o("Settings");
      });
      this.p.add(this.u);
      this.q = new jx("Israel mode", this::Y);
      this.q.g = (boolean)1;
      this.p.add(this.q);
      this.e = new jx("Larp Preset", this::E);
      this.e.g = (boolean)1;
      this.p.add(this.e);
      this.a = new j9("Search modules...", var1x -> {
         this.L = var1x;
         this.C = 0;
         this.M.F(0.0);
         this.a();
      }, 256);
      this.a.b(var0 -> sy.n(ChooseASkyEffectAndAdjustItsAppearanceModule.Z(LarpClient.R().z().a(), LarpClient.R().o().g()), var0));
      this.a.H = this.L;
      this.a.X = "search";
      this.p.add(this.a);
      this.y = new jx("", this::d);
      this.y.l = "x";
      this.y.g = (boolean)1;
      this.p.add(this.y);
      this.y.q = null;
      this.a();
   }

   private void a() {
      this.f = (boolean)0;
      int var1 = -1;
      int var2 = this.o != null && this.o.W ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.t.size(); var5++) {
         if (this.o == this.t.get(var5) || this.o == this.B.get(this.t.get(var5))) {
            var1 = var5;
            var4 = this.P.get(this.t.get(var5));
            var3 = (byte)(this.o == this.B.get(this.t.get(var5)) ? 1 : 0);
         }
      }

      if (this.t.contains(this.o) || this.B.containsValue(this.o)) {
         this.o.blur();
         this.o = null;
      }

      this.p.removeAll(this.t);
      this.p.removeAll(this.B.values());
      this.t.clear();
      this.P.clear();
      this.B.clear();
      this.vu.clear();
      hb var13 = LarpClient.R().o().g();
      String var6 = "";

      for (Module var8 : this.Y.P(LarpClient.R().z().a(), this.L, var13)) {
         String var9 = this.Y.Y(this.L) ? pz.W(var8) : "";
         int var10 = !var9.equals(var6) ? 1 : 0;
         var6 = var9;
         jx var11 = new jx(var8.getDisplayName(), () -> {
            int var2x = this.R == null ? 1 : 0;
            int var3x = this.R != var8 ? 1 : 0;
            if (var3x != 0) {
               sr.v(sr.e.S);
            }

            this.Q = (boolean)0;
            this.R = var8;
            if (var2x != 0) {
               this.d.P(this, this.vW.moduleInspector(this.v));
            }

            if (var3x != 0) {
               this.d.v(var8);
            }

            this.X.k(1.0, 270);
            this.s();
         });
         var11.q = null;
         var11.I = var11.h = (boolean)1;
         var11.Q = () -> {
            var8.toggle();
            sr.v(var8.isEnabled() ? sr.e.J : sr.e.v);
         };
         var11.M = var8.isEnabled();
         this.t.add(var11);
         this.P.put(var11, var8);
         if (var10 != 0 && !var6.isEmpty()) {
            this.vu.put(var11, j1.n(var6));
         }

         j_ var12 = new j_(() -> var13.M(var8.getName()), () -> {
            var13.e(var8.getName(), !var13.M(var8.getName()));
            this.A = (boolean)1;
            this.V();
            this.f = (boolean)1;
            this.x = System.nanoTime() + 160000000L;
         });
         this.B.put(var11, var12);
         this.p.add(var12);
         this.p.add(var11);
      }

      this.s();
      if (var1 >= 0 && var2 != 0 && !this.t.isEmpty()) {
         jx var14 = this.t.get(Math.min(var1, this.t.size() - 1));

         for (jx var16 : this.t) {
            if (this.P.get(var16) == var4) {
               var14 = var16;
               break;
            }
         }

         this.o = var3 != 0 ? this.B.get(var14) : var14;
         this.o.T = this.o.W = (boolean)1;
         this.focusChanged(this.o);
      } else if (var1 >= 0 && var2 != 0) {
         this.o = this.a;
         this.o.T = this.o.W = (boolean)1;
      }
   }

   private boolean V() {
      LarpClient.R().o().W();
      this.A = (boolean)0;
      return true;
   }

   private void Y() {
      LarpClient var1 = LarpClient.R();
      gg var2 = var1.o();
      u3 var3 = var2.g().d;
      boolean var4 = var2.j(!var3.X(), var1.z().h());
      if (!var4) {
         this.c = "Israel mode could not be changed. Click to retry; see the client log.";
         com.larpclient.h.g("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.c.startsWith("Israel mode")) {
         this.c = "";
      }

      if (this.R != null && this.T == null) {
         this.d.j();
      }
   }

   private void E() {
      LarpClient var1 = LarpClient.R();
      gg var2 = var1.o();
      if (!var2.g(!var2.g().p.s(), var1.z().h())) {
         this.c = "Larp Preset could not be changed. Click to retry; see the client log.";
         com.larpclient.h.g("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.c.startsWith("Larp Preset")) {
         this.c = "";
      }

      if (this.R != null && this.T == null) {
         this.d.j();
      }
   }

   private Vector2f K(double var1, double var3) {
      return new Matrix3x2f(this.D).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private MouseButtonEvent A(MouseButtonEvent var1) {
      Vector2f var2 = this.K(var1.x(), var1.y());
      return new MouseButtonEvent(var2.x, var2.y, var1.buttonInfo());
   }

   private h6 y() {
      return this.T != null ? this.T : (this.R != null ? this.d : null);
   }

   private boolean t() {
      return this.T == null && this.R != null && !this.Q && !this.vW.columns();
   }

   private int z() {
      return this.vW.moduleListRight(this.v);
   }

   private double C() {
      return this.vW.detachedInspector() ? Math.clamp(this.v * 2.0 - 1.0, 0.0, 1.0) : this.v;
   }

   private int i() {
      return this.vW.detachedInspector() ? this.vW.top() : this.vW.bodyTop();
   }

   private int y() {
      return this.vW.detachedInspector() ? this.vW.top() + this.vW.height() : this.vW.bottom();
   }

   private void s() {
      hb var1 = LarpClient.R().o().g();
      if (!this.w) {
         tq var2 = t(this.minecraft);
         if (!var2.equals(this.n)) {
            this.n = var2;
            com.larpclient.h.U(
               "GUI", "ClickGUI size {}%: workspace {}x{} / factor {}", Math.round(var1.q * 100.0), this.n.width(), this.n.height(), this.n.scale()
            );
         }
      }

      double var14 = this.W.k(this.G ? 0.0 : 1.0, var1.c);
      this.v = this.X.k(this.I.equals("Modules") && this.R != null && !this.Q ? 1.0 : 0.0, 270);
      this.vW = (this.I.equals("Modules") ? uc.calculateModules(this.n.width(), this.n.height(), this.v) : uc.calculate(this.n.width(), this.n.height()))
         .offset(0, var1.E.equals("Slide") ? (int)Math.round((1.0 - var14) * 8.0) : 0);
      int var4 = this.vW.navigationPitch(this.m.size());
      this.U = Math.clamp(this.U, 0, this.vW.navigationScrollLimit(this.m.size()));
      int var5 = this.vW.navigationTop() - this.U;

      for (int var6 = 0; var6 < this.m.size(); var6++) {
         if (var6 == this.k) {
            var5 += this.vW.navigationGap();
         }

         jx var7 = this.m.get(var6);
         var7.bounds(this.vW.left() + 4, var5, this.vW.navigationRight() - this.vW.left() - 4);
         var7.C = 1.0;
         var7.J = var4 - 1;
         var5 += var4;
         var7.z = this.I.equals("Modules") ? var7.K.equals(this.Y.u) : var7.K.equals(this.I);
      }

      this.u.bounds(this.vW.left() + 4, this.vW.accountY(), this.vW.navigationRight() - this.vW.left() - 4);
      this.u.J = 30;
      this.q.bounds(this.vW.presetLeft(), this.vW.themeSwitchY(), this.vW.presetWidth());
      this.q.J = this.vW.themeSwitchHeight();
      this.q.M = LarpClient.R().o().g().d.X();
      this.e.bounds(this.vW.larpPresetLeft(), this.vW.themeSwitchY(), this.vW.presetWidth());
      this.e.J = this.vW.themeSwitchHeight();
      this.e.M = LarpClient.R().o().g().p.s();
      this.a.i = this.I.equals("Modules");
      this.a.bounds(this.vW.contentX(), this.vW.top() + 12, this.vW.right() - this.vW.contentX());
      this.a.J = 26;
      this.s = this.g.B();
      int var16 = Math.max(1, this.vW.bottom() - this.vW.bodyTop());
      int var17 = this.vW.moduleGridColumns();
      int var8 = 0;
      int var9 = 0;
      int var10 = var17 * this.vW.moduleCardWidth() + (var17 - 1) * 6;

      for (jx var12 : this.t) {
         j1 var13 = this.vu.get(var12);
         if (var13 != null) {
            if (var8 > 0) {
               var8 = 0;
               var9 += 38;
            }

            var13.bounds(this.vW.moduleCardX(0, this.s), var9, var10);
            var9 += var13.J;
         }

         var12.bounds(this.vW.moduleCardX(var8, this.s), var9, this.vW.moduleCardWidth());
         if (++var8 == var17) {
            var8 = 0;
            var9 += 38;
         }
      }

      this.K = var9 + (var8 > 0 ? 38 : 0);
      this.C = Math.clamp(this.C, 0, Math.max(0, this.K - var16));
      var5 = this.vW.bodyTop() - (int)Math.round(this.M.k(this.C, 150));

      for (jx var20 : this.t) {
         var20.i = (boolean)(this.I.equals("Modules") && !this.t() ? 1 : 0);
         var20.u += var5;
         var20.J = 34;
         this.B.get(var20).H(var20);
         j1 var22 = this.vu.get(var20);
         if (var22 != null) {
            var22.i = var20.i;
            var22.u += var5;
         }
      }

      this.y.i = (boolean)(this.I.equals("Modules") && this.R != null ? 1 : 0);
      uc var19 = this.vW.moduleInspector(this.v);
      this.y.bounds(var19.right() - 30, var19.bodyTop() + 1, 22);
      this.y.J = 22;
      if (this.T != null) {
         this.T.x(this.vW);
      } else if (this.R != null) {
         uc var21 = this.vW.moduleInspector(this.v);
         this.d.O(var21, this.vW.moduleInspectorClipLeft(this.v), var21.right());
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = zj.m();
      this.U();
      this.s();
      double var7 = this.W.B();
      hb var9 = LarpClient.R().o().g();
      boolean var10 = WorldMenuModule.h(this);
      wa.b(var9.g);
      if (!var10 && this.minecraft.level != null && var9.H.equals("Everywhere")) {
         wa.P();
      }

      if (!var10) {
         W(var1, this.width, this.height, var7, var9);
      }

      this.D.set(this.n.transform()).mul(by.K(var9.E, var7, this.vW.left() + this.vW.width() / 2.0, this.vW.top() + this.vW.height() / 2.0));
      if (this.Z != null) {
         mb.d var11 = this.G ? this.Z.V() : this.Z.h(System.nanoTime(), this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive(), tw.D());
         this.D
            .set(this.n.transform())
            .mul(var11.matrix(this.n.width() / 2.0, this.n.height() / 2.0))
            .mul(by.K(var9.E, var7, this.vW.left() + this.vW.width() / 2.0, this.vW.top() + this.vW.height() / 2.0));
      }

      Vector2f var117 = this.K(var2, var3);
      var2 = Math.round(var117.x);
      var3 = Math.round(var117.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.D);

      try (
         ho.y var12 = ho.V(var9.f, var10 ? 1.0 : var9.r, var9.W, var9.b());
         bj.a var13 = bj.y();
         ua var14 = ua.K(by.X(var9.E, var7));
      ) {
         if (!var10 && ho.m().accentGlow()) {
            wp.v(var1, this.vW.left(), this.vW.top(), this.vW.width(), this.vW.height());
         }

         wp.Q(var1, this.vW.left(), this.vW.top(), this.vW.width(), this.vW.height());
         this.z(var1, this.y() != null && this.y().q() ? -1 : var2, var3);
         wp.s(var1, this.vW.left(), this.vW.top(), this.vW.width(), this.vW.height());
         this.q.render(var1, this.y() != null && this.y().q() ? -1 : var2, var3);
         this.e.render(var1, this.y() != null && this.y().q() ? -1 : var2, var3);
         if (this.T != null) {
            this.T.extractRenderState(var1, var2, var3, var4);
         } else {
            this.a.render(var1, var2, var3);
            var1.enableScissor(this.vW.moduleClipLeft(), this.vW.bodyTop(), this.z(), this.vW.bottom());

            try (ua var15 = ua.K(0.35 + 0.65 * this.s)) {
               for (j1 var17 : this.vu.values()) {
                  if (var17.u + var17.J > this.vW.bodyTop() && var17.u < this.vW.bottom()) {
                     var17.render(var1, -1, var3);
                  }
               }

               for (jx var127 : this.t) {
                  Module var18 = this.P.get(var127);
                  var127.z = (boolean)(var18 == this.R ? 1 : 0);
                  var127.M = var18.isEnabled();
                  var127.X = this.L.isBlank() ? var18.getDescription() : pz.B(var18).u + " · " + var18.getDescription();
                  if (var127.u + var127.J > this.vW.bodyTop() && var127.u < this.vW.bottom()) {
                     int var19 = var3 >= this.vW.bodyTop() && var3 < this.vW.bottom() && var2 >= this.vW.contentX() && var2 < this.z() ? var2 : -1;
                     var127.render(var1, var19, var3);
                     this.B.get(var127).render(var1, var19, var3);
                  }
               }

               if (this.t.isEmpty() && !this.t()) {
                  String var123 = this.Y == pz.s && this.L.isBlank() ? "Star a module to add it here." : "No matches";
                  bj.E(var1, bj.T(var123, this.z() - this.vW.contentX() - 16), this.vW.moduleListLeft(this.s) + 8, this.vW.bodyTop() + 12, ho.Z);
               }
            } finally {
               var1.disableScissor();
            }

            if (!this.t()) {
               wp.p(
                  var1,
                  this.vW.moduleScrollbarX(this.v),
                  this.vW.bodyTop(),
                  Math.max(1, this.vW.bottom() - this.vW.bodyTop()),
                  this.K,
                  (int)Math.round(this.M.B())
               );
            }

            if (this.R != null) {
               uc var118 = this.vW.moduleInspector(this.v);
               int var124 = this.vW.columns() ? var118.inspectorX() : var118.contentX();
               double var128 = this.C();
               if (this.vW.detachedInspector()) {
                  try (ua var133 = ua.K(var128)) {
                     wp.g(var1, var124, var118.top(), var118.right() - var124, var118.height());
                  }
               }

               var1.enableScissor(this.vW.moduleInspectorClipLeft(this.v), this.i(), var118.right(), this.y());

               try (ua var134 = ua.K(var128)) {
                  if (!this.vW.detachedInspector()) {
                     wp.G(var1, var124, this.vW.bodyTop(), var118.right() - var124, this.vW.bottom() - this.vW.bodyTop());
                  }

                  this.d.extractRenderState(var1, var2, var3, var4);
                  this.y.render(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }

               var1.enableScissor(this.vW.moduleInspectorClipLeft(this.v), var118.top() + 12, var118.right(), this.y());

               try (ua var135 = ua.K(var128)) {
                  this.d.M(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }
            }

            for (jx var125 : this.t) {
               j_ var129 = this.B.get(var125);
               boolean var131 = this.accepts(var129, var2, var3);
               if ((this.y() == null || !this.y().q())
                  && var129.i
                  && (var131 || var129.T && var129.W)
                  && var129.u >= this.vW.bodyTop()
                  && var129.u + var129.J <= this.vW.bottom()) {
                  var129.r(var1, this.n.width(), this.n.height());
                  break;
               }
            }
         }

         h6 var120 = this.y();
         String var126 = i(var120, var120 == null ? "" : var120.A(var2, var3));
         String var130 = LarpClient.R().o().H();
         if (!var130.isBlank()) {
            var126 = var130;
         }

         if (!this.c.isBlank()) {
            var126 = this.c;
         }

         if (var126.isBlank() && (this.q.contains(var2, var3) || this.q.T && this.q.W)) {
            var126 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var126.isBlank() && (this.e.contains(var2, var3) || this.e.T && this.e.W)) {
            var126 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var126.isBlank()) {
            var126 = "Enter opens settings · Space toggles · Tab navigates · Right click resets a setting";
         }

         int var132 = this.vW.profileLeft();
         String var136 = "Profile: " + LarpClient.R().o().M();
         bj.k(var1, bj.i(var136, this.vW.profileWidth(), bj.u.M), var132, this.vW.profileY(), ho.Z, bj.u.M);
         bj.k(var1, bj.i(var126, this.vW.helpWidth(), bj.u.M), this.vW.helpLeft(), this.vW.helpY(), this.c.isBlank() ? ho.Z : -24915, bj.u.M);
         double var121 = by.s(var9.E, var7);
         if (var121 > 0.01) {
            tb.E(var1, this.vW.left(), this.vW.top(), this.vW.width(), this.vW.height(), ho.x, uh.H(-1, var121));
         }

         if (var10) {
            WorldMenuModule.O(var1, this, this.vW, var2, var3);
         }
      } finally {
         var1.pose().popMatrix();
      }

      zj.l("ClickGUI", var5);
   }

   public double[] c() {
      float var1 = this.n.scale();
      return new double[]{this.vW.left() * var1, this.vW.top() * var1, (this.vW.left() + this.vW.width()) * var1, (this.vW.top() + this.vW.height()) * var1};
   }

   public Screen l() {
      return this.j;
   }

   public boolean D() {
      return this.G && this.W.B() <= 0.0;
   }

   public boolean Y(double var1, double var3) {
      Vector2f var5 = this.K(var1, var3);
      if (var5.x >= this.vW.left() && var5.x < this.vW.left() + this.vW.width() && var5.y >= this.vW.top() && var5.y < this.vW.top() + this.vW.height()) {
         return true;
      }

      if (this.R != null && this.vW.detachedInspector() && this.C() > 0.2) {
         uc var6 = this.vW.moduleInspector(this.v);
         if (var5.x >= var6.inspectorX() && var5.x < var6.right() && var5.y >= var6.top() && var5.y < var6.top() + var6.height()) {
            return true;
         }
      }

      return WorldMenuModule.V(this.vW, var5.x, var5.y);
   }

   public boolean P() {
      h6 var1 = this.y();
      return Z(this.o) || var1 != null && Z(var1.q());
   }

   static boolean Z(jd var0) {
      return var0 instanceof j9 var2 && var2.T || var0 instanceof ju var1 && var1.k();
   }

   public void Z() {
      h6 var1 = this.y();
      jd var2 = var1 == null ? null : var1.q();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.o != null && this.o != var2) {
         this.o.key(256);
      }

      this.V();
   }

   public void V() {
      h6 var1 = this.y();
      if (var1 != null) {
         var1.a();
      }

      if (this.o != null) {
         this.o.blur();
      }

      this.o = this.F = null;
   }

   private static void W(GuiGraphicsExtractor var0, int var1, int var2, double var3, hb var5) {
      int var6 = uh.H(-16777216, var5.Z);
      switch (var5.FF) {
         case "None":
            break;
         case "Gradient":
            int var11 = uh.F(var6, var3);
            int var10 = uh.F(uh.q(var6, uh.H(ho.J, 0.5), 0.35), var3);
            kv.d(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            sx.a var9 = sx.a.valueOf(var5.FF.toUpperCase(Locale.ROOT));
            if (!sx.L(var0, var1, var2, var3, ho.J, ho.ds, var5.f, var5.L, var9, var5.Z)) {
               kv.d(var0, 0, 0, var1, var2, uh.F(var6, var3));
            }
            break;
         default:
            kv.d(var0, 0, 0, var1, var2, uh.F(var6, var3));
      }
   }

   static String i(h6 var0, String var1) {
      return var0 instanceof hd var2 && !var2.Gr.isBlank() ? var2.Gr : var1;
   }

   private void z(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = this.vW.contentX() - this.vW.left() - 10;
      var1.enableScissor(this.vW.left(), this.vW.top(), this.vW.contentX() - 10, this.vW.navigationTop());

      try {
         var1.blit(RenderPipelines.GUI_TEXTURED, gr.V(), this.vW.left() + 7, this.vW.top() + 10, 0.0F, 0.0F, 22, 22, 64, 64, 64, 64, ua.I(-1));

         try (ua var5 = ua.K(1.0)) {
            int var6 = this.vW.left() + 35;
            int var7 = var4 - 39;
            bj.k(var1, bj.i("Larp Client", var7, bj.u.S), var6, this.vW.top() + 10, ho.u, bj.u.S);
            String var8 = "Version ";
            int var9 = var6 + bj.e(var8, bj.u.M);
            bj.k(var1, var8, var6, this.vW.top() + 24, ho.j, bj.u.M);
            bj.k(var1, bj.i(LarpClient.X, Math.max(1, var6 + var7 - var9), bj.u.M), var9, this.vW.top() + 24, ho.S(var9, ho.A), bj.u.M);
            S(var1, "FEATURES", this.vW.left() + 12, this.vW.navigationTop() - 12, this.vW.navigationRight());
         }
      } finally {
         var1.disableScissor();
      }

      var1.enableScissor(this.vW.left(), this.vW.navigationTop(), this.vW.contentX() - 10, this.vW.navigationBottom());

      try {
         try (ua var49 = ua.K(1.0)) {
            S(var1, "OTHERS", this.vW.left() + 12, this.m.get(this.k).u - 12, this.vW.navigationRight());
         }

         this.F(var1);

         for (jx var52 : this.m) {
            var52.render(var1, var3 >= this.vW.navigationTop() && var3 < this.vW.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.disableScissor();
      }

      if (this.vW.navigationScrollLimit(this.m.size()) > 0) {
         wp.p(
            var1,
            this.vW.contentX() - 12,
            this.vW.navigationTop(),
            this.vW.navigationBottom() - this.vW.navigationTop(),
            this.m.size() * this.vW.navigationPitch(this.m.size()) + this.vW.navigationGap(),
            this.U
         );
      }

      var1.enableScissor(this.vW.left(), this.vW.accountY(), this.vW.contentX() - 10, this.vW.accountY() + this.u.J);

      try {
         double var51 = this.u.S(var2, var3);
         if (var51 > 0.001) {
            kv.f(var1, this.u.S, this.u.u, this.u.A, this.u.J, ho.k, uh.H(ho.u, var51 * 0.045));
         }

         bq var53 = bq.current();
         if (!l.D(var1, this.vW.left() + 8, this.u.u + 5, 20)) {
            this.r.c(var1, this.vW.left() + 8, this.u.u + 5, 20);
         }

         kv.Q(var1, this.vW.left() + 25, this.u.u + 24, 4, ho.A);
         kv.Q(var1, this.vW.left() + 25, this.u.u + 24, 3, var53.state().C());

         try (ua var54 = ua.K(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.minecraft.getUser().getName();
            bj.J(var1, bj.o(var10, var55, bj.j.T), this.vW.left() + 34, this.u.u + 6, ho.u, bj.j.T);
            bj.k(var1, bj.i(var53.state().Z(), var55, bj.u.M), this.vW.left() + 34, this.u.u + 18, ho.Z, bj.u.M);
         }

         if (this.u.T && this.u.W) {
            wp.t(var1, this.u.S, this.u.u, this.u.A, this.u.J, ho.k);
         }
      } finally {
         var1.disableScissor();
      }
   }

   private static void S(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      bj.k(var0, var1, var2, var3, uh.H(ho.Z, 0.8), bj.u.m);
   }

   private void F(GuiGraphicsExtractor var1) {
      jx var2 = null;

      for (jx var4 : this.m) {
         if (var4.z) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.b.k(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.u - this.vW.navigationTop() + this.U;
         if (!this.H) {
            this.J.F(var5);
            this.H = (boolean)1;
         }

         this.J.k(var5, 200);
      }

      if (this.H && !(var9 < 0.01)) {
         jx var10 = this.m.getFirst();
         float var6 = this.vW.navigationTop() - this.U + (float)this.J.B();
         int var7 = ho.S(var10.S, ho.A);
         int var8 = ho.k;
         wp.e(var1, var10.S, var6, var10.A, var10.J, var8, 9.0F, uh.H(var7, 0.22 * var9));
         tb.d(var1, var10.S, var6, var10.A, var10.J, var8, uh.H(var7, 0.26 * var9), uh.H(ho.ds, 0.08 * var9));
         tb.s(var1, var10.S, var6, var10.A, var10.J, var8, uh.H(var7, 0.35 * var9), uh.H(var7, 0.35 * var9));
      }
   }

   @Override
   protected boolean accepts(jd var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.m.contains(var1)) {
         return var4 >= this.vW.navigationTop() && var4 < this.vW.navigationBottom();
      } else {
         return !this.t.contains(var1) && !this.B.containsValue(var1)
            ? true
            : !this.t() && var4 >= this.vW.bodyTop() && var4 < this.vW.bottom() && var2 >= this.vW.contentX() && var2 < this.z();
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.G) {
         return true;
      }

      this.w = (boolean)1;
      var1 = this.A(var1);
      this.s();
      h6 var3 = this.y();
      jd var4 = var3 == null ? null : var3.q();
      if (var4 != null && var4.captureMouse(k2.w(var1))) {
         return true;
      }

      if (k2.w(var1) > 1 && LarpClient.R().m() == bc.Z(k2.w(var1))) {
         this.onClose();
         return true;
      }

      if (var3 != null && var3.q()) {
         return var3.mouseClicked(var1, var2);
      }

      if (WorldMenuModule.Z(this, var1.x(), var1.y(), k2.w(var1), this.vW)) {
         if (var3 != null) {
            var3.a();
         }

         return true;
      } else {
         for (jx var6 : this.m) {
            if (this.accepts(var6, var1.x(), var1.y())) {
               if (var6.z && this.I.equals("Modules")) {
                  return var6.click(var1.x(), var1.y(), k2.w(var1));
               }

               return super.mouseClicked(var1, var2);
            }
         }

         if (this.C() > 0.2 && this.y.contains(var1.x(), var1.y()) && var1.x() < this.vW.moduleInspector(this.v).right()) {
            return super.mouseClicked(var1, var2);
         }

         int var8 = this.T != null
            ? this.vW.containsWorkspace(var1.x(), var1.y())
            : (
               this.C() > 0.2
                     && (!this.Q || this.vW.columns())
                     && var1.x() >= this.vW.moduleInspectorClipLeft(this.v)
                     && var1.x() < this.vW.moduleInspector(this.v).right()
                     && var1.y() >= this.i()
                     && var1.y() < this.y()
                  ? 1
                  : 0
            );
         if (var3 != null && var8 != 0) {
            if (this.o != null) {
               this.o.blur();
            }

            this.o = this.F = null;
            return this.Q && this.T == null ? true : var3.mouseClicked(var1, var2);
         } else {
            if (var3 != null) {
               var3.a();
            }

            return super.mouseClicked(var1, var2);
         }
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.G) {
         return true;
      } else {
         Vector2f var9 = this.K(var1, var3);
         var1 = var9.x;
         var3 = var9.y;
         this.s();
         h6 var10 = this.y();
         if ((var10 == null || !var10.q()) && this.vW.containsNavigation(var1, var3) && var3 >= this.vW.navigationTop() && var3 < this.vW.navigationBottom()) {
            this.U -= (int)(var7 * 28.0);
            this.s();
            return true;
         } else if (var10 != null && !this.Q && (!this.vW.detachedInspector() || this.C() > 0.2) && var10.mouseScrolled(var1, var3, var5, var7)) {
            return true;
         } else if (this.I.equals("Modules")
            && !this.t()
            && var1 >= this.vW.contentX()
            && var1 < this.z()
            && var3 >= this.vW.bodyTop()
            && var3 < this.vW.bottom()) {
            this.C -= (int)(var7 * 27.0);
            this.s();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(jd var1) {
      if (this.m.contains(var1)) {
         if (var1.u < this.vW.navigationTop()) {
            this.U = this.U - (this.vW.navigationTop() - var1.u);
         } else if (var1.u + var1.J > this.vW.navigationBottom()) {
            this.U = this.U + (var1.u + var1.J - this.vW.navigationBottom());
         }

         this.s();
      }

      if (this.t.contains(var1) || this.B.containsValue(var1)) {
         for (Entry var3 : this.B.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (jd)var3.getKey();
               break;
            }
         }

         int var4 = this.vu.containsKey(var1) ? this.vu.get(var1).u : var1.u;
         if (var4 < this.vW.bodyTop()) {
            this.C = this.C - (this.vW.bodyTop() - var4);
         } else if (var1.u + var1.J > this.vW.bottom()) {
            this.C = this.C + (var1.u + var1.J - this.vW.bottom());
         }

         this.M.F(this.C);
         this.s();
      }
   }

   static boolean m(int var0, jd var1, jd var2, Runnable var3) {
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
      if (this.G) {
         return true;
      }

      h6 var2 = this.y();
      if (m(k2.a(var1), var2 == null ? null : var2.q(), this.o, this::onClose)) {
         return true;
      }

      if (this.a != null
         && this.I.equals("Modules")
         && this.o == this.a
         && (var2 == null || !var2.q())
         && k2.a(var1) == 258
         && (var1.modifiers() & 1) == 0
         && this.a.l()) {
         return true;
      }

      if (k2.a(var1) != 258 || var2 != null && var2.q()) {
         return var2 == null || var2.q() == null && !var2.q() ? super.keyPressed(var1) : var2.keyPressed(var1);
      }

      ArrayList var3 = new ArrayList();
      this.p.stream().filter(var0 -> var0.i && var0.p).forEach(var3::add);
      if (var2 != null && !this.Q && (!this.vW.detachedInspector() || this.C() > 0.2)) {
         var3.addAll(var2.n());
      }

      jd var4 = var2 != null && var2.q() != null ? var2.q() : this.o;
      int var5 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
      if (this.o != null) {
         this.o.blur();
      }

      this.o = null;
      if (var2 != null) {
         var2.a();
      }

      if (!var3.isEmpty()) {
         jd var7 = (jd)var3.get(Math.floorMod(var6 + var5, var3.size()));
         if (this.p.contains(var7)) {
            this.o = var7;
            var7.T = var7.W = (boolean)1;
            this.focusChanged(var7);
         } else {
            var2.q(var7);
         }
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.G) {
         return true;
      }

      h6 var2 = this.y();
      return var2 != null && var2.q() != null ? var2.charTyped(var1) : super.charTyped(var1);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (!this.G && !this.Q) {
         Vector2f var6 = this.K(var1.x(), var1.y());
         Vector2f var7 = this.K(var1.x() - var2, var1.y() - var4);
         var1 = this.A(var1);
         var2 = var6.x - var7.x;
         var4 = var6.y - var7.y;
         h6 var8 = this.y();
         return var8 != null && var8.mouseDragged(var1, var2, var4) || super.mouseDragged(var1, var2, var4);
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      this.w = (boolean)0;
      var1 = this.A(var1);
      if (this.y() != null) {
         this.y().mouseReleased(var1);
      }

      return super.mouseReleased(var1);
   }

   public void tick() {
      this.U();
      if (this.f && System.nanoTime() >= this.x) {
         this.f = (boolean)0;
         this.a();
      }

      if (!this.G && this.y() != null) {
         this.y().tick();
      }
   }

   void r() {
      if (this.Y != pz.s) {
         c7.c = this.Y.name();
      }

      c7.D = new c7.x(this.I, this.R == null ? "" : this.R.getName(), this.L, this.C, this.d.Y);
   }

   public void removed() {
      if (this.Z != null && !this.G) {
         this.Z.O();
      }

      if (this.T != null) {
         this.T.suspend();
      }

      this.d.suspend();
      this.r();
      gg var1 = LarpClient.R().o();
      if (this.A) {
         this.V();
      }

      var1.C();
      com.larpclient.h.U("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}

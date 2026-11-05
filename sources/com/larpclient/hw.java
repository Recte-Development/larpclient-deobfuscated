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

public final class hw extends h8 {
   private final Screen u;
   private final List<u7> z = new ArrayList<>();
   private final List<u7> F = new ArrayList<>();
   private final Map<u7, Module> S = new IdentityHashMap<>();
   private final Map<u7, u_> K = new IdentityHashMap<>();
   private final Map<u7, us> O = new IdentityHashMap<>();
   private int T;
   private boolean N;
   private boolean k;
   private long X;
   private final h4 v = new h4();
   private final hd L = new hd();
   private hx P;
   private hb x;
   private mp Yk;
   private m4 M;
   private om l;
   private Module m;
   private String Q = "Modules";
   private String Y = "";
   private String W = "";
   private boolean w;
   private boolean y;
   private boolean a;
   private static hw A;
   private Object e;
   private oa i;
   private int r;
   private int b;
   private double c;
   private double t = 1.0;
   private boolean d;
   private boolean Z;
   private boolean h;
   private final ef C = new ef(0.0);
   private final ef E = new ef(1.0);
   private ur V;
   private u7 g;
   private u7 s;
   private u7 o;
   private u7 j;
   private final cn p = new cn();
   private static final h9 D = new h9();
   private final ef n = new ef(0.0);
   private final ef U = new ef(0.0);
   private final ef B = new ef(0.0);
   private final ef I = new ef(0.0);
   private boolean R;
   private xt J;
   private final Matrix3x2f H = new Matrix3x2f();

   public hw() {
      this(null);
   }

   public hw(Screen var1) {
      super("Larp client");
      this.u = var1;
      this.l = LarpClient.F().f().t().p ? om.L : om.D(cx.K);
   }

   public void A() {
      this.J = new xt();
   }

   public static hw N(Screen var0, String var1, boolean var2) {
      hw var3 = new hw(var0);
      var3.d = (boolean)1;
      var3.Q = "Configs";
      var3.P = new hx(var3);
      if (var1 != null) {
         var3.P.v6.E = var1;
      }

      var3.P.S = var2;
      return var3;
   }

   public static hw N(Screen var0) {
      hw var1 = N(var0, null, false);
      var1.w = (boolean)1;
      return var1;
   }

   public static hw P(Screen var0, String var1) {
      hw var2 = new hw(var0);
      var2.d = (boolean)1;
      var2.Q = "Settings";
      var2.L.N(var1);
      return var2;
   }

   public static hw t(Screen var0, mq.s var1) {
      hw var2 = P(var0, "HUD");
      var2.L.K(var1);
      return var2;
   }

   Minecraft F() {
      return this.minecraft;
   }

   boolean J(hx var1) {
      return this.x == var1;
   }

   public void onClose() {
      if (!this.Z) {
         if (this.x != null) {
            this.x.suspend();
         }

         this.v.o();
         kb.d(kb.n.G);
         this.Z = (boolean)1;
         this.n.m(0.0, this.S());
         if (!WorldMenuModule.s(this)) {
            this.e = this.minecraft.level;
            A = this;
            this.i = this.P();
            if (this.u instanceof uj && !LarpClient.F().f().t().U) {
               mt.K("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.minecraft.gui.setScreen(new TitleScreen());
            } else {
               this.minecraft.gui.setScreen(this.u);
            }
         }
      }
   }

   private int S() {
      return LarpClient.F().f().t().S;
   }

   private oa P() {
      xv var1 = LarpClient.F().f().t();
      String var2 = var1.Y;
      if (!var2.equals("Reverse") && var1.V) {
         ArrayList var3 = new ArrayList();
         float var4 = this.H.m00();
         Vector2f var5 = this.H.transformPosition(this.Yk.left(), this.Yk.top(), new Vector2f());
         var3.add(new oa.v(var5.x, var5.y, this.Yk.width() * var4, this.Yk.height() * var4, com.larpclient.f.r * var4));
         if (this.m != null && this.x == null && this.Yk.detachedInspector() && this.R() > 0.5) {
            mp var6 = this.Yk.moduleInspector(this.c);
            Vector2f var7 = this.H.transformPosition(var6.inspectorX(), var6.top(), new Vector2f());
            var3.add(new oa.v(var7.x, var7.y, (var6.right() - var6.inspectorX()) * var4, var6.height() * var4, com.larpclient.f.r * var4));
         }

         return oa.x(var2, var3, this.S());
      } else {
         return null;
      }
   }

   public static boolean n() {
      return A != null;
   }

   public static void X(GuiGraphicsExtractor var0, float var1) {
      hw var2 = A;
      if (var2 != null) {
         Minecraft var3 = Minecraft.getInstance();
         oa var4 = var2.i;
         if (var3.gui.screen() == var2.u
            && var3.level == var2.e
            && var3.gui.overlay() == null
            && var2.width == var0.guiWidth()
            && var2.height == var0.guiHeight()
            && var2.M.equals(u(var3))
            && (var4 == null ? !(var2.n.G() <= 0.0) : !var4.I())) {
            try {
               var0.nextStratum();
               if (var4 != null) {
                  xv var5 = LarpClient.F().f().t();

                  try (f.s var6 = com.larpclient.f.W(var5.c, var5.T, var5.i)) {
                     g(var0, var0.guiWidth(), var0.guiHeight(), 1.0 - var4.D(), var5);
                  }

                  var4.M(var0);
               } else {
                  var2.extractRenderState(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var11) {
               O();
               mt.P("GUI", "Window exit rendering failed; input remains released", var11);
            }
         } else {
            if (var4 != null) {
               mt.U("GUI", "Close effect {}", var4.I() ? "finished" : "stopped by a screen, world or window size change");
            }

            O();
         }
      }
   }

   private static void O() {
      if (A != null && A.i != null) {
         A.i = null;
         oa.U();
      }

      A = null;
   }

   private static m4 u(Minecraft var0) {
      return m4.paired(var0.getWindow(), LarpClient.F().f().t().s8);
   }

   protected void init() {
      this.M = u(this.minecraft);
      this.H.set(this.M.transform());
      O();
      if (!this.d) {
         cx.p var1 = cx.P;
         this.Q = var1.page();
         this.Y = var1.query();
         this.r = var1.moduleScroll();
         this.m = LarpClient.F().x().k(var1.module());
         this.m = ChooseASkyEffectAndAdjustItsAppearanceModule.r(this.m, LarpClient.F().x().C(), LarpClient.F().f().t());
         if (this.m == null && !var1.module().isEmpty()) {
            mt.K("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.m != null && this.Y.isBlank() && this.l != om.L) {
            this.l = om.d(this.m);
         }

         this.d = (boolean)1;
      }

      this.Yk = this.Q.equals("Modules")
         ? mp.calculateModules(this.M.width(), this.M.height(), this.m == null ? 0.0 : 1.0)
         : mp.calculate(this.M.width(), this.M.height());
      this.Z = (boolean)0;
      this.n.Q(this.J == null ? 0.0 : 1.0);
      this.n.m(1.0, this.S());
      if (!this.y) {
         this.y = (boolean)1;
         kb.d(kb.n.K);
      }

      this.h = (boolean)0;
      this.U.Q(this.m == null ? 0.0 : 1.0);
      this.C.Q(this.r);
      this.m();
      this.C.Q(this.r);
      if (this.m != null) {
         int var2 = this.v.j == null ? cx.P.settingScroll() : this.v.Z;
         this.v.W(this, this.Yk);
         this.v.l(this.m);
         this.v.Z = var2;
         this.U.Q(1.0);
      }

      this.k();
      mt.U(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.width,
         this.height,
         this.minecraft.getWindow().getGuiScale(),
         this.M.width(),
         this.M.height(),
         this.M.scale(),
         this.Q
      );
   }

   private void k() {
      this.x = switch (this.Q) {
         case "Configs" -> {
            if (this.P == null) {
               this.P = new hx(this);
            }

            yield this.P;
         }
         case "Settings" -> this.L;
         default -> null;
      };
      if (this.x != null) {
         this.x.W(this, this.Yk);
      }

      if (this.w && this.x == this.P) {
         this.w = (boolean)0;
         this.P.V("Discover");
      }
   }

   private void n(String var1) {
      if (!this.Q.equals(var1)) {
         kb.d(kb.n.Q);
      }

      if (this.h) {
         this.m = null;
         this.h = (boolean)0;
         this.U.Q(0.0);
      }

      if (this.x != null) {
         this.x.suspend();
      }

      this.v.suspend();
      this.Q = var1;
      this.m();
      this.k();
      if (this.x == null && this.m != null) {
         this.v.W(this, this.Yk);
         this.v.l(this.m);
      }

      this.e();
      mt.U("GUI", "Destination switched to {}", var1);
   }

   void i() {
      this.L.N("HUD");
      this.L.K(null);
      this.L.Z = 0;
      this.n("Settings");
   }

   void Z() {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         this.e();
         this.minecraft.gui.setScreen(new hu(this));
      } else {
         this.W = "Join a world to edit the HUD.";
      }
   }

   void Q() {
      if (this.m != null && !this.h) {
         kb.d(kb.n.z);
         this.h = (boolean)1;
         this.v.o();
         this.U.m(0.0, 270);
      }
   }

   private void I() {
      if (this.h && this.U.G() == 0.0) {
         this.m = null;
         this.h = (boolean)0;
         this.v.suspend();
      }
   }

   private void M(om var1) {
      boolean var2 = var1.m(this.l, () -> {
         if (this.Q.equals("Modules")) {
            kb.d(kb.n.Q);
         }

         this.E.s(220);
         xv var2x = LarpClient.F().f().t();
         if (var2x.p != (var1 == om.L ? 1 : 0)) {
            var2x.p = (boolean)(var1 == om.L ? 1 : 0);
            this.k = (boolean)1;
            this.M();
         }

         this.l = var1;
         this.r = 0;
         this.C.Q(0.0);
         this.m = null;
         this.h = (boolean)0;
         this.Y = "";
         this.n("Modules");
      });
      if (!var2 && !this.Q.equals("Modules")) {
         this.n("Modules");
      }
   }

   private void m() {
      this.z();
      this.z.clear();
      this.F.clear();
      this.S.clear();
      this.K.clear();

      for (om var4 : om.values()) {
         u7 var5 = new u7(var4.D, () -> this.M(var4));
         var5.s = null;
         var5.j = var4.u;
         var5.r = (boolean)1;
         var5.z = (boolean)1;
         this.z.add(var5);
         this.G.add(var5);
      }

      for (String var9 : new String[]{"Editor", "Configs", "Settings"}) {
         u7 var10 = new u7(var9, () -> {
            if (var9.equals("Editor")) {
               this.Z();
            } else {
               this.n(var9);
            }
         });
         var10.s = null;
         var10.j = var9.equals("Editor") ? "panels-top-left" : (var9.equals("Configs") ? "folder" : "settings");
         var10.r = (boolean)1;
         var10.z = (boolean)1;
         this.z.add(var10);
         this.G.add(var10);
      }

      this.j = new u7("Account", () -> {
         this.L.N("Account");
         this.n("Settings");
      });
      this.G.add(this.j);
      this.s = new u7("Israel mode", this::r);
      this.s.r = (boolean)1;
      this.G.add(this.s);
      this.o = new u7("Larp Preset", this::S);
      this.o.r = (boolean)1;
      this.G.add(this.o);
      this.V = new ur("Search modules...", var1 -> {
         this.Y = var1;
         this.r = 0;
         this.C.Q(0.0);
         this.d();
      }, 256);
      this.V.B(var0 -> py.p(ChooseASkyEffectAndAdjustItsAppearanceModule.C(LarpClient.F().x().C(), LarpClient.F().f().t()), var0));
      this.V.d = this.Y;
      this.V.u = "search";
      this.G.add(this.V);
      this.g = new u7("", this::Q);
      this.g.j = "x";
      this.g.r = (boolean)1;
      this.G.add(this.g);
      this.g.s = null;
      this.d();
   }

   private void d() {
      this.N = (boolean)0;
      int var1 = -1;
      int var2 = this.q != null && this.q.R ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.F.size(); var5++) {
         if (this.q == this.F.get(var5) || this.q == this.K.get(this.F.get(var5))) {
            var1 = var5;
            var4 = this.S.get(this.F.get(var5));
            var3 = (byte)(this.q == this.K.get(this.F.get(var5)) ? 1 : 0);
         }
      }

      if (this.F.contains(this.q) || this.K.containsValue(this.q)) {
         this.q.blur();
         this.q = null;
      }

      this.G.removeAll(this.F);
      this.G.removeAll(this.K.values());
      this.F.clear();
      this.S.clear();
      this.K.clear();
      this.O.clear();
      xv var13 = LarpClient.F().f().t();
      String var6 = "";

      for (Module var8 : this.l.k(LarpClient.F().x().C(), this.Y, var13)) {
         String var9 = this.l.y(this.Y) ? om.i(var8) : "";
         int var10 = !var9.equals(var6) ? 1 : 0;
         var6 = var9;
         u7 var11 = new u7(var8.getDisplayName(), () -> {
            int var2x = this.m == null ? 1 : 0;
            int var3x = this.m != var8 ? 1 : 0;
            if (var3x != 0) {
               kb.d(kb.n.T);
            }

            this.h = (boolean)0;
            this.m = var8;
            if (var2x != 0) {
               this.v.W(this, this.Yk.moduleInspector(this.c));
            }

            if (var3x != 0) {
               this.v.l(var8);
            }

            this.U.m(1.0, 270);
            this.c();
         });
         var11.s = null;
         var11.Q = var11.Y = (boolean)1;
         var11.E = () -> {
            var8.toggle();
            kb.d(var8.isEnabled() ? kb.n.l : kb.n.O);
         };
         var11.v = var8.isEnabled();
         this.F.add(var11);
         this.S.put(var11, var8);
         if (var10 != 0 && !var6.isEmpty()) {
            this.O.put(var11, us.R(var6));
         }

         u_ var12 = new u_(() -> var13.J(var8.getName()), () -> {
            var13.W(var8.getName(), !var13.J(var8.getName()));
            this.k = (boolean)1;
            this.M();
            this.N = (boolean)1;
            this.X = System.nanoTime() + 160000000L;
         });
         this.K.put(var11, var12);
         this.G.add(var12);
         this.G.add(var11);
      }

      this.c();
      if (var1 >= 0 && var2 != 0 && !this.F.isEmpty()) {
         u7 var14 = this.F.get(Math.min(var1, this.F.size() - 1));

         for (u7 var16 : this.F) {
            if (this.S.get(var16) == var4) {
               var14 = var16;
               break;
            }
         }

         this.q = var3 != 0 ? this.K.get(var14) : var14;
         this.q.l = this.q.R = (boolean)1;
         this.focusChanged(this.q);
      } else if (var1 >= 0 && var2 != 0) {
         this.q = this.V;
         this.q.l = this.q.R = (boolean)1;
      }
   }

   private boolean M() {
      LarpClient.F().f().P();
      this.k = (boolean)0;
      return true;
   }

   private void r() {
      LarpClient var1 = LarpClient.F();
      w9 var2 = var1.f();
      kr var3 = var2.t().K;
      boolean var4 = var2.v(!var3.X(), var1.x().C());
      if (!var4) {
         this.W = "Israel mode could not be changed. Click to retry; see the client log.";
         mt.T("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.W.startsWith("Israel mode")) {
         this.W = "";
      }

      if (this.m != null && this.x == null) {
         this.v.G();
      }
   }

   private void S() {
      LarpClient var1 = LarpClient.F();
      w9 var2 = var1.f();
      if (!var2.N(!var2.t().C.Y(), var1.x().C())) {
         this.W = "Larp Preset could not be changed. Click to retry; see the client log.";
         mt.T("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.W.startsWith("Larp Preset")) {
         this.W = "";
      }

      if (this.m != null && this.x == null) {
         this.v.G();
      }
   }

   private Vector2f x(double var1, double var3) {
      return new Matrix3x2f(this.H).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private MouseButtonEvent x(MouseButtonEvent var1) {
      Vector2f var2 = this.x(var1.x(), var1.y());
      return new MouseButtonEvent(var2.x, var2.y, var1.buttonInfo());
   }

   private hb o() {
      return this.x != null ? this.x : (this.m != null ? this.v : null);
   }

   private boolean B() {
      return this.x == null && this.m != null && !this.h && !this.Yk.columns();
   }

   private int M() {
      return this.Yk.moduleListRight(this.c);
   }

   private double R() {
      return this.Yk.detachedInspector() ? Math.clamp(this.c * 2.0 - 1.0, 0.0, 1.0) : this.c;
   }

   private int H() {
      return this.Yk.detachedInspector() ? this.Yk.top() : this.Yk.bodyTop();
   }

   private int j() {
      return this.Yk.detachedInspector() ? this.Yk.top() + this.Yk.height() : this.Yk.bottom();
   }

   private void c() {
      xv var1 = LarpClient.F().f().t();
      if (!this.a) {
         m4 var2 = u(this.minecraft);
         if (!var2.equals(this.M)) {
            this.M = var2;
            mt.U("GUI", "ClickGUI size {}%: workspace {}x{} / factor {}", Math.round(var1.s8 * 100.0), this.M.width(), this.M.height(), this.M.scale());
         }
      }

      double var14 = this.n.m(this.Z ? 0.0 : 1.0, var1.S);
      this.c = this.U.m(this.Q.equals("Modules") && this.m != null && !this.h ? 1.0 : 0.0, 270);
      this.Yk = (this.Q.equals("Modules") ? mp.calculateModules(this.M.width(), this.M.height(), this.c) : mp.calculate(this.M.width(), this.M.height()))
         .offset(0, var1.sb.equals("Slide") ? (int)Math.round((1.0 - var14) * 8.0) : 0);
      int var4 = this.Yk.navigationPitch(this.z.size());
      this.b = Math.clamp(this.b, 0, this.Yk.navigationScrollLimit(this.z.size()));
      int var5 = this.Yk.navigationTop() - this.b;

      for (int var6 = 0; var6 < this.z.size(); var6++) {
         if (var6 == om.values().length) {
            var5 += this.Yk.navigationGap();
         }

         u7 var7 = this.z.get(var6);
         var7.bounds(this.Yk.left() + 4, var5, this.Yk.navigationRight() - this.Yk.left() - 4);
         var7.T = 1.0;
         var7.W = var4 - 1;
         var5 += var4;
         var7.U = this.Q.equals("Modules") ? var7.S.equals(this.l.D) : var7.S.equals(this.Q);
      }

      this.j.bounds(this.Yk.left() + 4, this.Yk.accountY(), this.Yk.navigationRight() - this.Yk.left() - 4);
      this.j.W = 30;
      this.s.bounds(this.Yk.presetLeft(), this.Yk.themeSwitchY(), this.Yk.presetWidth());
      this.s.W = this.Yk.themeSwitchHeight();
      this.s.v = LarpClient.F().f().t().K.X();
      this.o.bounds(this.Yk.larpPresetLeft(), this.Yk.themeSwitchY(), this.Yk.presetWidth());
      this.o.W = this.Yk.themeSwitchHeight();
      this.o.v = LarpClient.F().f().t().C.Y();
      this.V.D = this.Q.equals("Modules");
      this.V.bounds(this.Yk.contentX(), this.Yk.top() + 12, this.Yk.right() - this.Yk.contentX());
      this.V.W = 26;
      this.t = this.E.G();
      int var16 = Math.max(1, this.Yk.bottom() - this.Yk.bodyTop());
      int var17 = this.Yk.moduleGridColumns();
      int var8 = 0;
      int var9 = 0;
      int var10 = var17 * this.Yk.moduleCardWidth() + (var17 - 1) * 6;

      for (u7 var12 : this.F) {
         us var13 = this.O.get(var12);
         if (var13 != null) {
            if (var8 > 0) {
               var8 = 0;
               var9 += 38;
            }

            var13.bounds(this.Yk.moduleCardX(0, this.t), var9, var10);
            var9 += var13.W;
         }

         var12.bounds(this.Yk.moduleCardX(var8, this.t), var9, this.Yk.moduleCardWidth());
         if (++var8 == var17) {
            var8 = 0;
            var9 += 38;
         }
      }

      this.T = var9 + (var8 > 0 ? 38 : 0);
      this.r = Math.clamp(this.r, 0, Math.max(0, this.T - var16));
      var5 = this.Yk.bodyTop() - (int)Math.round(this.C.m(this.r, 150));

      for (u7 var20 : this.F) {
         var20.D = (boolean)(this.Q.equals("Modules") && !this.B() ? 1 : 0);
         var20.J += var5;
         var20.W = 34;
         this.K.get(var20).r(var20);
         us var22 = this.O.get(var20);
         if (var22 != null) {
            var22.D = var20.D;
            var22.J += var5;
         }
      }

      this.g.D = (boolean)(this.Q.equals("Modules") && this.m != null ? 1 : 0);
      mp var19 = this.Yk.moduleInspector(this.c);
      this.g.bounds(var19.right() - 30, var19.bodyTop() + 1, 22);
      this.g.W = 22;
      if (this.x != null) {
         this.x.w(this.Yk);
      } else if (this.m != null) {
         mp var21 = this.Yk.moduleInspector(this.c);
         this.v.J(var21, this.Yk.moduleInspectorClipLeft(this.c), var21.right());
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = kx.G();
      this.I();
      this.c();
      double var7 = this.n.G();
      xv var9 = LarpClient.F().f().t();
      boolean var10 = WorldMenuModule.c(this);
      m5.M(var9.q);
      if (!var10 && this.minecraft.level != null && var9.R.equals("Everywhere")) {
         m5.o();
      }

      if (!var10) {
         g(var1, this.width, this.height, var7, var9);
      }

      this.H.set(this.M.transform()).mul(p2.q(var9.sb, var7, this.Yk.left() + this.Yk.width() / 2.0, this.Yk.top() + this.Yk.height() / 2.0));
      if (this.J != null) {
         m8.w var11 = this.Z ? this.J.j() : this.J.s(System.nanoTime(), this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive(), wv.X());
         this.H
            .set(this.M.transform())
            .mul(var11.matrix(this.M.width() / 2.0, this.M.height() / 2.0))
            .mul(p2.q(var9.sb, var7, this.Yk.left() + this.Yk.width() / 2.0, this.Yk.top() + this.Yk.height() / 2.0));
      }

      Vector2f var117 = this.x(var2, var3);
      var2 = Math.round(var117.x);
      var3 = Math.round(var117.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.H);

      try (
         f.s var12 = com.larpclient.f.F(var9.c, var10 ? 1.0 : var9.T, var9.i, var9.O());
         k6.l var13 = k6.F();
         de var14 = de.l(p2.Z(var9.sb, var7));
      ) {
         if (!var10 && com.larpclient.f.O().accentGlow()) {
            pn.F(var1, this.Yk.left(), this.Yk.top(), this.Yk.width(), this.Yk.height());
         }

         pn.C(var1, this.Yk.left(), this.Yk.top(), this.Yk.width(), this.Yk.height());
         pn.B(var1, this.Yk.sunkenX(), this.Yk.sunkenY(), this.Yk.sunkenWidth(), this.Yk.sunkenHeight());
         this.t(var1, this.o() != null && this.o().B() ? -1 : var2, var3);
         pn.b(var1, this.Yk.left(), this.Yk.top(), this.Yk.width(), this.Yk.height());
         this.s.render(var1, this.o() != null && this.o().B() ? -1 : var2, var3);
         this.o.render(var1, this.o() != null && this.o().B() ? -1 : var2, var3);
         if (this.x != null) {
            this.x.extractRenderState(var1, var2, var3, var4);
         } else {
            this.V.render(var1, var2, var3);
            var1.enableScissor(this.Yk.contentX(), this.Yk.bodyTop(), this.M(), this.Yk.bottom());

            try (de var15 = de.l(0.35 + 0.65 * this.t)) {
               for (us var17 : this.O.values()) {
                  if (var17.J + var17.W > this.Yk.bodyTop() && var17.J < this.Yk.bottom()) {
                     var17.render(var1, -1, var3);
                  }
               }

               for (u7 var127 : this.F) {
                  Module var18 = this.S.get(var127);
                  var127.U = (boolean)(var18 == this.m ? 1 : 0);
                  var127.v = var18.isEnabled();
                  var127.u = this.Y.isBlank() ? var18.getDescription() : om.d(var18).D + " · " + var18.getDescription();
                  if (var127.J + var127.W > this.Yk.bodyTop() && var127.J < this.Yk.bottom()) {
                     int var19 = var3 >= this.Yk.bodyTop() && var3 < this.Yk.bottom() && var2 >= this.Yk.contentX() && var2 < this.M() ? var2 : -1;
                     var127.render(var1, var19, var3);
                     this.K.get(var127).render(var1, var19, var3);
                  }
               }

               if (this.F.isEmpty() && !this.B()) {
                  String var123 = this.l == om.L && this.Y.isBlank() ? "Star a module to add it here." : "No matches";
                  k6.Z(var1, k6.j(var123, this.M() - this.Yk.contentX() - 16), this.Yk.moduleListLeft(this.t) + 8, this.Yk.bodyTop() + 12, com.larpclient.f.s);
               }
            } finally {
               var1.disableScissor();
            }

            if (!this.B()) {
               pn.u(
                  var1,
                  this.Yk.moduleScrollbarX(this.c),
                  this.Yk.bodyTop(),
                  Math.max(1, this.Yk.bottom() - this.Yk.bodyTop()),
                  this.T,
                  (int)Math.round(this.C.G())
               );
            }

            if (this.m != null) {
               mp var118 = this.Yk.moduleInspector(this.c);
               int var124 = this.Yk.columns() ? var118.inspectorX() : var118.contentX();
               double var128 = this.R();
               if (this.Yk.detachedInspector()) {
                  try (de var133 = de.l(var128)) {
                     pn.a(var1, var124, var118.top(), var118.right() - var124, var118.height());
                  }
               }

               var1.enableScissor(this.Yk.moduleInspectorClipLeft(this.c), this.H(), var118.right(), this.j());

               try (de var134 = de.l(var128)) {
                  if (!this.Yk.detachedInspector()) {
                     pn.t(var1, var124, this.Yk.bodyTop(), var118.right() - var124, this.Yk.bottom() - this.Yk.bodyTop());
                  }

                  this.v.extractRenderState(var1, var2, var3, var4);
                  this.g.render(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }

               var1.enableScissor(this.Yk.moduleInspectorClipLeft(this.c), var118.top() + 12, var118.right(), this.j());

               try (de var135 = de.l(var128)) {
                  this.v.J(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }
            }

            for (u7 var125 : this.F) {
               u_ var129 = this.K.get(var125);
               boolean var131 = this.accepts(var129, var2, var3);
               if ((this.o() == null || !this.o().B())
                  && var129.D
                  && (var131 || var129.l && var129.R)
                  && var129.J >= this.Yk.bodyTop()
                  && var129.J + var129.W <= this.Yk.bottom()) {
                  var129.J(var1, this.M.width(), this.M.height());
                  break;
               }
            }
         }

         hb var120 = this.o();
         String var126 = e(var120, var120 == null ? "" : var120.B(var2, var3));
         String var130 = LarpClient.F().f().u();
         if (!var130.isBlank()) {
            var126 = var130;
         }

         if (!this.W.isBlank()) {
            var126 = this.W;
         }

         if (var126.isBlank() && (this.s.contains(var2, var3) || this.s.l && this.s.R)) {
            var126 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var126.isBlank() && (this.o.contains(var2, var3) || this.o.l && this.o.R)) {
            var126 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var126.isBlank()) {
            var126 = "Enter opens settings · Space toggles · Tab navigates · Right click resets a setting";
         }

         int var132 = this.Yk.profileLeft();
         String var136 = "Profile: " + LarpClient.F().f().w();
         k6.T(var1, k6.o(var136, this.Yk.profileWidth(), k6.d.K), var132, this.Yk.profileY(), com.larpclient.f.s, k6.d.K);
         k6.T(var1, k6.o(var126, this.Yk.helpWidth(), k6.d.K), this.Yk.helpLeft(), this.Yk.helpY(), this.W.isBlank() ? com.larpclient.f.s : -24915, k6.d.K);
         double var121 = p2.H(var9.sb, var7);
         if (var121 > 0.01) {
            tl.g(var1, this.Yk.left(), this.Yk.top(), this.Yk.width(), this.Yk.height(), com.larpclient.f.r, _3.A(-1, var121));
         }

         if (var10) {
            WorldMenuModule.h(var1, this, this.Yk, var2, var3);
         }
      } finally {
         var1.pose().popMatrix();
      }

      kx.u("ClickGUI", var5);
   }

   public double[] C() {
      float var1 = this.M.scale();
      return new double[]{this.Yk.left() * var1, this.Yk.top() * var1, (this.Yk.left() + this.Yk.width()) * var1, (this.Yk.top() + this.Yk.height()) * var1};
   }

   public Screen S() {
      return this.u;
   }

   public boolean G() {
      return this.Z && this.n.G() <= 0.0;
   }

   public boolean J(double var1, double var3) {
      Vector2f var5 = this.x(var1, var3);
      if (var5.x >= this.Yk.left() && var5.x < this.Yk.left() + this.Yk.width() && var5.y >= this.Yk.top() && var5.y < this.Yk.top() + this.Yk.height()) {
         return true;
      }

      if (this.m != null && this.Yk.detachedInspector() && this.R() > 0.2) {
         mp var6 = this.Yk.moduleInspector(this.c);
         if (var5.x >= var6.inspectorX() && var5.x < var6.right() && var5.y >= var6.top() && var5.y < var6.top() + var6.height()) {
            return true;
         }
      }

      return WorldMenuModule.c(this.Yk, var5.x, var5.y);
   }

   public boolean m() {
      hb var1 = this.o();
      return a(this.q) || var1 != null && a(var1.c());
   }

   private static boolean a(b var0) {
      return var0 instanceof ur var2 && var2.l || var0 instanceof un var1 && var1.s();
   }

   public void H() {
      hb var1 = this.o();
      b var2 = var1 == null ? null : var1.c();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.q != null && this.q != var2) {
         this.q.key(256);
      }

      this.F();
   }

   public void F() {
      hb var1 = this.o();
      if (var1 != null) {
         var1.o();
      }

      if (this.q != null) {
         this.q.blur();
      }

      this.q = this.f = null;
   }

   private static void g(GuiGraphicsExtractor var0, int var1, int var2, double var3, xv var5) {
      int var6 = _3.A(-16777216, var5.X);
      switch (var5.F) {
         case "None":
            break;
         case "Gradient":
            int var11 = _3.U(var6, var3);
            int var10 = _3.U(_3.D(var6, _3.A(com.larpclient.f.C, 0.5), 0.35), var3);
            c7.l(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            xs.j var9 = xs.j.valueOf(var5.F.toUpperCase(Locale.ROOT));
            if (!xs.G(var0, var1, var2, var3, com.larpclient.f.C, com.larpclient.f.O, var5.c, var5.V, var9, var5.X)) {
               c7.s(var0, 0, 0, var1, var2, _3.U(var6, var3));
            }
            break;
         default:
            c7.s(var0, 0, 0, var1, var2, _3.U(var6, var3));
      }
   }

   static String e(hb var0, String var1) {
      return var0 instanceof hx var2 && !var2.A.isBlank() ? var2.A : var1;
   }

   private void t(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = this.Yk.contentX() - this.Yk.left() - 10;
      var1.enableScissor(this.Yk.left(), this.Yk.top(), this.Yk.contentX() - 10, this.Yk.navigationTop());

      try {
         var1.blit(RenderPipelines.GUI_TEXTURED, pw.k(), this.Yk.left() + 7, this.Yk.top() + 10, 0.0F, 0.0F, 22, 22, 64, 64, 64, 64, de.h(-1));

         try (de var5 = de.l(1.0)) {
            int var6 = this.Yk.left() + 35;
            int var7 = var4 - 39;
            k6.T(var1, k6.o("Larp Client", var7, k6.d.y), var6, this.Yk.top() + 10, com.larpclient.f.J, k6.d.y);
            String var8 = "Version ";
            int var9 = var6 + k6.a(var8, k6.d.K);
            k6.T(var1, var8, var6, this.Yk.top() + 24, com.larpclient.f.G, k6.d.K);
            k6.T(
               var1,
               k6.o(LarpClient.t, Math.max(1, var6 + var7 - var9), k6.d.K),
               var9,
               this.Yk.top() + 24,
               com.larpclient.f.Q(var9, com.larpclient.f.u),
               k6.d.K
            );
            r(var1, "FEATURES", this.Yk.left() + 12, this.Yk.navigationTop() - 12, this.Yk.navigationRight());
         }
      } finally {
         var1.disableScissor();
      }

      var1.enableScissor(this.Yk.left(), this.Yk.navigationTop(), this.Yk.contentX() - 10, this.Yk.navigationBottom());

      try {
         try (de var49 = de.l(1.0)) {
            r(var1, "OTHERS", this.Yk.left() + 12, this.z.get(om.values().length).J - 12, this.Yk.navigationRight());
         }

         this.f(var1);

         for (u7 var52 : this.z) {
            var52.render(var1, var3 >= this.Yk.navigationTop() && var3 < this.Yk.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.disableScissor();
      }

      if (this.Yk.navigationScrollLimit(this.z.size()) > 0) {
         pn.u(
            var1,
            this.Yk.contentX() - 12,
            this.Yk.navigationTop(),
            this.Yk.navigationBottom() - this.Yk.navigationTop(),
            this.z.size() * this.Yk.navigationPitch(this.z.size()) + this.Yk.navigationGap(),
            this.b
         );
      }

      var1.enableScissor(this.Yk.left(), this.Yk.accountY(), this.Yk.contentX() - 10, this.Yk.accountY() + this.j.W);

      try {
         double var51 = this.j.k(var2, var3);
         if (var51 > 0.001) {
            c7.n(var1, this.j.q, this.j.J, this.j.A, this.j.W, com.larpclient.f.y, _3.A(com.larpclient.f.J, var51 * 0.045));
         }

         hm var53 = hm.current();
         if (!D.b(var1, this.Yk.left() + 8, this.j.J + 5, 20)) {
            this.p.W(var1, this.Yk.left() + 8, this.j.J + 5, 20);
         }

         c7.K(var1, this.Yk.left() + 25, this.j.J + 24, 4, com.larpclient.f.u);
         c7.K(var1, this.Yk.left() + 25, this.j.J + 24, 3, var53.state().o());

         try (de var54 = de.l(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.minecraft.getUser().getName();
            k6.M(var1, k6.q(var10, var55, k6.v.B), this.Yk.left() + 34, this.j.J + 6, com.larpclient.f.J, k6.v.B);
            k6.T(var1, k6.o(var53.state().H(), var55, k6.d.K), this.Yk.left() + 34, this.j.J + 18, com.larpclient.f.s, k6.d.K);
         }

         if (this.j.l && this.j.R) {
            pn.y(var1, this.j.q, this.j.J, this.j.A, this.j.W, com.larpclient.f.y);
         }
      } finally {
         var1.disableScissor();
      }
   }

   private static void r(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      k6.T(var0, var1, var2, var3, _3.A(com.larpclient.f.s, 0.8), k6.d.q);
   }

   private void f(GuiGraphicsExtractor var1) {
      u7 var2 = null;

      for (u7 var4 : this.z) {
         if (var4.U) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.I.m(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.J - this.Yk.navigationTop() + this.b;
         if (!this.R) {
            this.B.Q(var5);
            this.R = (boolean)1;
         }

         this.B.m(var5, 200);
      }

      if (this.R && !(var9 < 0.01)) {
         u7 var10 = this.z.getFirst();
         float var6 = this.Yk.navigationTop() - this.b + (float)this.B.G();
         int var7 = com.larpclient.f.Q(var10.q, com.larpclient.f.u);
         int var8 = com.larpclient.f.y;
         pn.Z(var1, var10.q, var6, var10.A, var10.W, var8, 9.0F, _3.A(var7, 0.22 * var9));
         tl.A(var1, var10.q, var6, var10.A, var10.W, var8, _3.A(var7, 0.26 * var9), _3.A(com.larpclient.f.O, 0.08 * var9));
         tl.V(var1, var10.q, var6, var10.A, var10.W, var8, _3.A(var7, 0.35 * var9), _3.A(var7, 0.35 * var9));
      }
   }

   @Override
   protected boolean accepts(b var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.z.contains(var1)) {
         return var4 >= this.Yk.navigationTop() && var4 < this.Yk.navigationBottom();
      } else {
         return !this.F.contains(var1) && !this.K.containsValue(var1)
            ? true
            : !this.B() && var4 >= this.Yk.bodyTop() && var4 < this.Yk.bottom() && var2 >= this.Yk.contentX() && var2 < this.M();
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.Z) {
         return true;
      }

      this.a = (boolean)1;
      var1 = this.x(var1);
      this.c();
      hb var3 = this.o();
      b var4 = var3 == null ? null : var3.c();
      if (var4 != null && var4.captureMouse(mo.Z(var1))) {
         return true;
      }

      if (mo.Z(var1) > 1 && LarpClient.F().R() == cc.M(mo.Z(var1))) {
         this.onClose();
         return true;
      }

      if (var3 != null && var3.B()) {
         return var3.mouseClicked(var1, var2);
      }

      if (WorldMenuModule.p(this, var1.x(), var1.y(), mo.Z(var1), this.Yk)) {
         if (var3 != null) {
            var3.o();
         }

         return true;
      } else {
         for (u7 var6 : this.z) {
            if (this.accepts(var6, var1.x(), var1.y())) {
               if (var6.U && this.Q.equals("Modules")) {
                  return var6.click(var1.x(), var1.y(), mo.Z(var1));
               }

               return super.mouseClicked(var1, var2);
            }
         }

         if (this.R() > 0.2 && this.g.contains(var1.x(), var1.y()) && var1.x() < this.Yk.moduleInspector(this.c).right()) {
            return super.mouseClicked(var1, var2);
         }

         int var8 = this.x != null
            ? this.Yk.containsWorkspace(var1.x(), var1.y())
            : (
               this.R() > 0.2
                     && (!this.h || this.Yk.columns())
                     && var1.x() >= this.Yk.moduleInspectorClipLeft(this.c)
                     && var1.x() < this.Yk.moduleInspector(this.c).right()
                     && var1.y() >= this.H()
                     && var1.y() < this.j()
                  ? 1
                  : 0
            );
         if (var3 != null && var8 != 0) {
            if (this.q != null) {
               this.q.blur();
            }

            this.q = this.f = null;
            return this.h && this.x == null ? true : var3.mouseClicked(var1, var2);
         } else {
            if (var3 != null) {
               var3.o();
            }

            return super.mouseClicked(var1, var2);
         }
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.Z) {
         return true;
      } else {
         Vector2f var9 = this.x(var1, var3);
         var1 = var9.x;
         var3 = var9.y;
         this.c();
         hb var10 = this.o();
         if ((var10 == null || !var10.B()) && this.Yk.containsNavigation(var1, var3) && var3 >= this.Yk.navigationTop() && var3 < this.Yk.navigationBottom()) {
            this.b -= (int)(var7 * 28.0);
            this.c();
            return true;
         } else if (var10 != null && !this.h && (!this.Yk.detachedInspector() || this.R() > 0.2) && var10.mouseScrolled(var1, var3, var5, var7)) {
            return true;
         } else if (this.Q.equals("Modules")
            && !this.B()
            && var1 >= this.Yk.contentX()
            && var1 < this.M()
            && var3 >= this.Yk.bodyTop()
            && var3 < this.Yk.bottom()) {
            this.r -= (int)(var7 * 27.0);
            this.c();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(b var1) {
      if (this.z.contains(var1)) {
         if (var1.J < this.Yk.navigationTop()) {
            this.b = this.b - (this.Yk.navigationTop() - var1.J);
         } else if (var1.J + var1.W > this.Yk.navigationBottom()) {
            this.b = this.b + (var1.J + var1.W - this.Yk.navigationBottom());
         }

         this.c();
      }

      if (this.F.contains(var1) || this.K.containsValue(var1)) {
         for (Entry var3 : this.K.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (b)var3.getKey();
               break;
            }
         }

         int var4 = this.O.containsKey(var1) ? this.O.get(var1).J : var1.J;
         if (var4 < this.Yk.bodyTop()) {
            this.r = this.r - (this.Yk.bodyTop() - var4);
         } else if (var1.J + var1.W > this.Yk.bottom()) {
            this.r = this.r + (var1.J + var1.W - this.Yk.bottom());
         }

         this.C.Q(this.r);
         this.c();
      }
   }

   static boolean q(int var0, b var1, b var2, Runnable var3) {
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
      if (this.Z) {
         return true;
      }

      hb var2 = this.o();
      if (q(mo.O(var1), var2 == null ? null : var2.c(), this.q, this::onClose)) {
         return true;
      }

      if (this.V != null
         && this.Q.equals("Modules")
         && this.q == this.V
         && (var2 == null || !var2.B())
         && mo.O(var1) == 258
         && (var1.modifiers() & 1) == 0
         && this.V.N()) {
         return true;
      }

      if (mo.O(var1) != 258 || var2 != null && var2.B()) {
         return var2 == null || var2.c() == null && !var2.B() ? super.keyPressed(var1) : var2.keyPressed(var1);
      }

      ArrayList var3 = new ArrayList();
      this.G.stream().filter(var0 -> var0.D && var0.X).forEach(var3::add);
      if (var2 != null && !this.h && (!this.Yk.detachedInspector() || this.R() > 0.2)) {
         var3.addAll(var2.r());
      }

      b var4 = var2 != null && var2.c() != null ? var2.c() : this.q;
      int var5 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
      if (this.q != null) {
         this.q.blur();
      }

      this.q = null;
      if (var2 != null) {
         var2.o();
      }

      if (!var3.isEmpty()) {
         b var7 = (b)var3.get(Math.floorMod(var6 + var5, var3.size()));
         if (this.G.contains(var7)) {
            this.q = var7;
            var7.l = var7.R = (boolean)1;
            this.focusChanged(var7);
         } else {
            var2.M(var7);
         }
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.Z) {
         return true;
      }

      hb var2 = this.o();
      return var2 != null && var2.c() != null ? var2.charTyped(var1) : super.charTyped(var1);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (!this.Z && !this.h) {
         Vector2f var6 = this.x(var1.x(), var1.y());
         Vector2f var7 = this.x(var1.x() - var2, var1.y() - var4);
         var1 = this.x(var1);
         var2 = var6.x - var7.x;
         var4 = var6.y - var7.y;
         hb var8 = this.o();
         return var8 != null && var8.mouseDragged(var1, var2, var4) || super.mouseDragged(var1, var2, var4);
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      this.a = (boolean)0;
      var1 = this.x(var1);
      if (this.o() != null) {
         this.o().mouseReleased(var1);
      }

      return super.mouseReleased(var1);
   }

   public void tick() {
      this.I();
      if (this.N && System.nanoTime() >= this.X) {
         this.N = (boolean)0;
         this.d();
      }

      if (!this.Z && this.o() != null) {
         this.o().tick();
      }
   }

   void e() {
      if (this.l != om.L) {
         cx.K = this.l.name();
      }

      cx.P = new cx.p(this.Q, this.m == null ? "" : this.m.getName(), this.Y, this.r, this.v.Z);
   }

   public void removed() {
      if (this.J != null && !this.Z) {
         this.J.r();
      }

      if (this.x != null) {
         this.x.suspend();
      }

      this.v.suspend();
      this.e();
      w9 var1 = LarpClient.F().f();
      if (this.k) {
         this.M();
      }

      var1.f();
      mt.U("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}

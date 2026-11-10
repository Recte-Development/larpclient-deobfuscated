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

public final class f8 extends fs {
   private final Screen W;
   private final List<s8> E = new ArrayList<>();
   private final List<s8> o = new ArrayList<>();
   private final Map<s8, Module> k = new IdentityHashMap<>();
   private final Map<s8, sv> S = new IdentityHashMap<>();
   private final Map<s8, sj> B = new IdentityHashMap<>();
   private int j;
   private boolean R;
   private boolean Y;
   private long V;
   private final fu Q = new fu();
   private final fg G = new fg();
   private fk z;
   private fp t9;
   private h_ X;
   private az e;
   private q_ d;
   private int T;
   private Module n;
   private String N = "Modules";
   private String t = "";
   private String Z = "";
   private boolean K;
   private boolean v;
   private boolean M;
   private static f8 w;
   private Object y;
   private _9 s;
   private int x;
   private int O;
   private double I;
   private double u = 1.0;
   private boolean H;
   private boolean i;
   private boolean f;
   private final fq F = new fq(0.0);
   private final fq r = new fq(1.0);
   private sk P;
   private s8 A;
   private s8 a;
   private s8 l;
   private s8 c;
   private final bd U = new bd();
   private static final oo p = new oo();
   private final fq C = new fq(0.0);
   private final fq b = new fq(0.0);
   private final fq D = new fq(0.0);
   private final fq m = new fq(0.0);
   private boolean L;
   private dc h;
   private final Matrix3x2f tl = new Matrix3x2f();

   public f8() {
      this(null);
   }

   public f8(Screen var1) {
      super("Larp client");
      this.W = var1;
      this.d = LarpClient.N().h().k().v ? q_.N : q_.j(bh.C);
   }

   public void x() {
      this.h = new dc();
   }

   public static f8 t(Screen var0, String var1, boolean var2) {
      f8 var3 = new f8(var0);
      var3.H = (boolean)1;
      var3.N = "Configs";
      var3.z = new fk(var3);
      if (var1 != null) {
         var3.z.t.f = var1;
      }

      var3.z.W = var2;
      return var3;
   }

   public static f8 x(Screen var0) {
      f8 var1 = t(var0, null, false);
      var1.K = (boolean)1;
      return var1;
   }

   public static f8 i(Screen var0, String var1) {
      f8 var2 = new f8(var0);
      var2.H = (boolean)1;
      var2.N = "Settings";
      var2.G.R(var1);
      return var2;
   }

   public static f8 K(Screen var0, dk.q var1) {
      f8 var2 = i(var0, "HUD");
      var2.G.f(var1);
      return var2;
   }

   Minecraft a() {
      return this.minecraft;
   }

   boolean d(fk var1) {
      return this.t9 == var1;
   }

   public void onClose() {
      if (!this.i) {
         if (this.t9 != null) {
            this.t9.suspend();
         }

         this.Q.y();
         ft.h(ft.e.n);
         this.i = (boolean)1;
         this.C.n(0.0, this.U());
         if (!WorldMenuModule.d(this)) {
            this.y = this.minecraft.level;
            w = this;
            this.s = this.i();
            if (this.W instanceof de && !LarpClient.N().h().k().M) {
               _q.z("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.minecraft.gui.setScreen(new TitleScreen());
            } else {
               this.minecraft.gui.setScreen(this.W);
            }
         }
      }
   }

   private int U() {
      return LarpClient.N().h().k().n;
   }

   private _9 i() {
      i3 var1 = LarpClient.N().h().k();
      String var2 = var1.q;
      if (!var2.equals("Reverse") && var1.w) {
         ArrayList var3 = new ArrayList();
         float var4 = this.tl.m00();
         Vector2f var5 = this.tl.transformPosition(this.X.left(), this.X.top(), new Vector2f());
         var3.add(new _9.e(var5.x, var5.y, this.X.width() * var4, this.X.height() * var4, com.larpclient.c.O * var4));
         if (this.n != null && this.t9 == null && this.X.detachedInspector() && this.y() > 0.5) {
            h_ var6 = this.X.moduleInspector(this.I);
            Vector2f var7 = this.tl.transformPosition(var6.inspectorX(), var6.top(), new Vector2f());
            var3.add(new _9.e(var7.x, var7.y, (var6.right() - var6.inspectorX()) * var4, var6.height() * var4, com.larpclient.c.O * var4));
         }

         return _9.V(var2, var3, this.U());
      } else {
         return null;
      }
   }

   public static boolean H() {
      return w != null;
   }

   public static void E(GuiGraphicsExtractor var0, float var1) {
      f8 var2 = w;
      if (var2 != null) {
         Minecraft var3 = Minecraft.getInstance();
         _9 var4 = var2.s;
         if (var3.gui.screen() == var2.W
            && var3.level == var2.y
            && var3.gui.overlay() == null
            && var2.width == var0.guiWidth()
            && var2.height == var0.guiHeight()
            && var2.e.equals(W(var3))
            && (var4 == null ? !(var2.C.K() <= 0.0) : !var4.g())) {
            try {
               var0.nextStratum();
               if (var4 != null) {
                  i3 var5 = LarpClient.N().h().k();

                  try (c.z var6 = com.larpclient.c.C(var5.z, var5.j, var5.b)) {
                     z(var0, var0.guiWidth(), var0.guiHeight(), 1.0 - var4.b(), var5);
                  }

                  var4.Z(var0);
               } else {
                  var2.extractRenderState(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var11) {
               r();
               _q.o("GUI", "Window exit rendering failed; input remains released", var11);
            }
         } else {
            if (var4 != null) {
               _q.c("GUI", "Close effect {}", var4.g() ? "finished" : "stopped by a screen, world or window size change");
            }

            r();
         }
      }
   }

   private static void r() {
      if (w != null && w.s != null) {
         w.s = null;
         _9.u();
      }

      w = null;
   }

   private static az W(Minecraft var0) {
      return az.paired(var0.getWindow(), LarpClient.N().h().k().U);
   }

   protected void init() {
      this.e = W(this.minecraft);
      this.tl.set(this.e.transform());
      r();
      if (!this.H) {
         bh.v var1 = bh.c;
         this.N = var1.page();
         this.t = var1.query();
         this.x = var1.moduleScroll();
         this.n = LarpClient.N().j().U(var1.module());
         this.n = ChooseASkyEffectAndAdjustItsAppearanceModule.N(this.n, LarpClient.N().j().d(), LarpClient.N().h().k());
         if (this.n == null && !var1.module().isEmpty()) {
            _q.z("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.n != null && this.t.isBlank() && this.d != q_.N) {
            this.d = q_.U(this.n);
         }

         this.H = (boolean)1;
      }

      if (!q_.H().contains(this.d)) {
         this.d = q_.n;
      }

      if (this.n != null && !(this.n instanceof ChooseASkyEffectAndAdjustItsAppearanceModule) && com.larpclient.module.fj.r(this.n)) {
         this.n = null;
      }

      this.X = this.N.equals("Modules")
         ? h_.calculateModules(this.e.width(), this.e.height(), this.n == null ? 0.0 : 1.0)
         : h_.calculate(this.e.width(), this.e.height());
      this.i = (boolean)0;
      this.C.p(this.h == null ? 0.0 : 1.0);
      this.C.n(1.0, this.U());
      if (!this.v) {
         this.v = (boolean)1;
         ft.h(ft.e.D);
      }

      this.f = (boolean)0;
      this.b.p(this.n == null ? 0.0 : 1.0);
      this.F.p(this.x);
      this.z();
      this.F.p(this.x);
      if (this.n != null) {
         int var2 = this.Q.G == null ? bh.c.settingScroll() : this.Q.L;
         this.Q.u(this, this.X);
         this.Q.o(this.n);
         this.Q.L = var2;
         this.b.p(1.0);
      }

      this.A();
      _q.c(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.width,
         this.height,
         this.minecraft.getWindow().getGuiScale(),
         this.e.width(),
         this.e.height(),
         this.e.scale(),
         this.N
      );
   }

   private void A() {
      this.t9 = switch (this.N) {
         case "Configs" -> {
            if (this.z == null) {
               this.z = new fk(this);
            }

            yield this.z;
         }
         case "Settings" -> this.G;
         default -> null;
      };
      if (this.t9 != null) {
         this.t9.u(this, this.X);
      }

      if (this.K && this.t9 == this.z) {
         this.K = (boolean)0;
         this.z.U("Discover");
      }
   }

   private void z(String var1) {
      if (!this.N.equals(var1)) {
         ft.h(ft.e.E);
      }

      if (this.f) {
         this.n = null;
         this.f = (boolean)0;
         this.b.p(0.0);
      }

      if (this.t9 != null) {
         this.t9.suspend();
      }

      this.Q.suspend();
      this.N = var1;
      this.z();
      this.A();
      if (this.t9 == null && this.n != null) {
         this.Q.u(this, this.X);
         this.Q.o(this.n);
      }

      this.b();
      _q.c("GUI", "Destination switched to {}", var1);
   }

   void c() {
      this.G.R("HUD");
      this.G.f(null);
      this.G.L = 0;
      this.z("Settings");
   }

   void j() {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         this.b();
         this.minecraft.gui.setScreen(new fv(this));
      } else {
         this.Z = "Join a world to edit the HUD.";
      }
   }

   void E() {
      if (this.n != null && !this.f) {
         ft.h(ft.e.X);
         this.f = (boolean)1;
         this.Q.y();
         this.b.n(0.0, 270);
      }
   }

   private void g() {
      if (this.f && this.b.K() == 0.0) {
         this.n = null;
         this.f = (boolean)0;
         this.Q.suspend();
      }
   }

   private void j(q_ var1) {
      boolean var2 = var1.A(this.d, () -> {
         if (this.N.equals("Modules")) {
            ft.h(ft.e.E);
         }

         this.r.m(220);
         i3 var2x = LarpClient.N().h().k();
         if (var2x.v != (var1 == q_.N ? 1 : 0)) {
            var2x.v = (boolean)(var1 == q_.N ? 1 : 0);
            this.Y = (boolean)1;
            this.P();
         }

         this.d = var1;
         this.x = 0;
         this.F.p(0.0);
         this.n = null;
         this.f = (boolean)0;
         this.t = "";
         this.z("Modules");
      });
      if (!var2 && !this.N.equals("Modules")) {
         this.z("Modules");
      }
   }

   private void z() {
      this.W();
      this.E.clear();
      this.o.clear();
      this.k.clear();
      this.S.clear();
      List var1 = q_.H();
      this.T = var1.size();

      for (q_ var3 : var1) {
         s8 var4 = new s8(var3.f, () -> this.j(var3));
         var4.K = null;
         var4.x = var3.F;
         var4.N = (boolean)1;
         var4.w = (boolean)1;
         this.E.add(var4);
         this.J.add(var4);
      }

      for (String var5 : new String[]{"Editor", "Configs", "Settings"}) {
         s8 var6 = new s8(var5, () -> {
            if (var5.equals("Editor")) {
               this.j();
            } else {
               this.z(var5);
            }
         });
         var6.K = null;
         var6.x = var5.equals("Editor") ? "panels-top-left" : (var5.equals("Configs") ? "folder" : "settings");
         var6.N = (boolean)1;
         var6.w = (boolean)1;
         this.E.add(var6);
         this.J.add(var6);
      }

      this.c = new s8("Account", () -> {
         this.G.R("Account");
         this.z("Settings");
      });
      this.J.add(this.c);
      this.a = new s8("Israel mode", this::t);
      this.a.N = (boolean)1;
      this.J.add(this.a);
      this.l = new s8("Larp Preset", this::k);
      this.l.N = (boolean)1;
      this.J.add(this.l);
      this.P = new sk("Search modules...", var1x -> {
         this.t = var1x;
         this.x = 0;
         this.F.p(0.0);
         this.L();
      }, 256);
      this.P.z(var0 -> com.larpclient.q.j(ChooseASkyEffectAndAdjustItsAppearanceModule.Z(LarpClient.N().j().a(), LarpClient.N().h().k()), var0));
      this.P.j = this.t;
      this.P.o = "search";
      this.J.add(this.P);
      this.A = new s8("", this::E);
      this.A.x = "x";
      this.A.N = (boolean)1;
      this.J.add(this.A);
      this.A.K = null;
      this.L();
   }

   private void L() {
      this.R = (boolean)0;
      int var1 = -1;
      int var2 = this.q != null && this.q.X ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.o.size(); var5++) {
         if (this.q == this.o.get(var5) || this.q == this.S.get(this.o.get(var5))) {
            var1 = var5;
            var4 = this.k.get(this.o.get(var5));
            var3 = (byte)(this.q == this.S.get(this.o.get(var5)) ? 1 : 0);
         }
      }

      if (this.o.contains(this.q) || this.S.containsValue(this.q)) {
         this.q.blur();
         this.q = null;
      }

      this.J.removeAll(this.o);
      this.J.removeAll(this.S.values());
      this.o.clear();
      this.k.clear();
      this.S.clear();
      this.B.clear();
      i3 var13 = LarpClient.N().h().k();
      String var6 = "";

      for (Module var8 : this.d.l(LarpClient.N().j().a(), this.t, var13)) {
         String var9 = this.d.D(this.t) ? q_.C(var8) : "";
         int var10 = !var9.equals(var6) ? 1 : 0;
         var6 = var9;
         s8 var11 = new s8(var8.getDisplayName(), () -> {
            int var2x = this.n == null ? 1 : 0;
            int var3x = this.n != var8 ? 1 : 0;
            if (var3x != 0) {
               ft.h(ft.e.Q);
            }

            this.f = (boolean)0;
            this.n = var8;
            if (var2x != 0) {
               this.Q.u(this, this.X.moduleInspector(this.I));
            }

            if (var3x != 0) {
               this.Q.o(var8);
            }

            this.b.n(1.0, 270);
            this.o();
         });
         var11.K = null;
         var11.b = var11.H = (boolean)1;
         var11.o = () -> {
            var8.toggle();
            ft.h(var8.isEnabled() ? ft.e.y : ft.e.J);
         };
         var11.G = var8.isEnabled();
         this.o.add(var11);
         this.k.put(var11, var8);
         if (var10 != 0 && !var6.isEmpty()) {
            this.B.put(var11, sj.B(var6));
         }

         sv var12 = new sv(() -> var13.m(var8.getName()), () -> {
            var13.h(var8.getName(), !var13.m(var8.getName()));
            this.Y = (boolean)1;
            this.P();
            this.R = (boolean)1;
            this.V = System.nanoTime() + 160000000L;
         });
         this.S.put(var11, var12);
         this.J.add(var12);
         this.J.add(var11);
      }

      this.o();
      if (var1 >= 0 && var2 != 0 && !this.o.isEmpty()) {
         s8 var14 = this.o.get(Math.min(var1, this.o.size() - 1));

         for (s8 var16 : this.o) {
            if (this.k.get(var16) == var4) {
               var14 = var16;
               break;
            }
         }

         this.q = var3 != 0 ? this.S.get(var14) : var14;
         this.q.F = this.q.X = (boolean)1;
         this.focusChanged(this.q);
      } else if (var1 >= 0 && var2 != 0) {
         this.q = this.P;
         this.q.F = this.q.X = (boolean)1;
      }
   }

   private boolean P() {
      LarpClient.N().h().c();
      this.Y = (boolean)0;
      return true;
   }

   private void t() {
      LarpClient var1 = LarpClient.N();
      aq var2 = var1.h();
      hi var3 = var2.k().Y;
      boolean var4 = var2.s(!var3.w(), var1.j().d());
      if (!var4) {
         this.Z = "Israel mode could not be changed. Click to retry; see the client log.";
         _q.R("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.Z.startsWith("Israel mode")) {
         this.Z = "";
      }

      if (this.n != null && this.t9 == null) {
         this.Q.z();
      }
   }

   private void k() {
      LarpClient var1 = LarpClient.N();
      aq var2 = var1.h();
      if (!var2.y(!var2.k().c.w(), var1.j().d())) {
         this.Z = "Larp Preset could not be changed. Click to retry; see the client log.";
         _q.R("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.Z.startsWith("Larp Preset")) {
         this.Z = "";
      }

      if (this.n != null && this.t9 == null) {
         this.Q.z();
      }
   }

   private Vector2f l(double var1, double var3) {
      return new Matrix3x2f(this.tl).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private MouseButtonEvent M(MouseButtonEvent var1) {
      Vector2f var2 = this.l(var1.x(), var1.y());
      return new MouseButtonEvent(var2.x, var2.y, var1.buttonInfo());
   }

   private fp U() {
      return this.t9 != null ? this.t9 : (this.n != null ? this.Q : null);
   }

   private boolean Z() {
      return this.t9 == null && this.n != null && !this.f && !this.X.columns();
   }

   private int l() {
      return this.X.moduleListRight(this.I);
   }

   private double y() {
      return this.X.detachedInspector() ? Math.clamp(this.I * 2.0 - 1.0, 0.0, 1.0) : this.I;
   }

   private int y() {
      return this.X.detachedInspector() ? this.X.top() : this.X.bodyTop();
   }

   private int q() {
      return this.X.detachedInspector() ? this.X.top() + this.X.height() : this.X.bottom();
   }

   private void o() {
      i3 var1 = LarpClient.N().h().k();
      if (!this.M) {
         az var2 = W(this.minecraft);
         if (!var2.equals(this.e)) {
            this.e = var2;
            _q.c("GUI", "ClickGUI size {}%: workspace {}x{} / factor {}", Math.round(var1.U * 100.0), this.e.width(), this.e.height(), this.e.scale());
         }
      }

      double var14 = this.C.n(this.i ? 0.0 : 1.0, var1.n);
      this.I = this.b.n(this.N.equals("Modules") && this.n != null && !this.f ? 1.0 : 0.0, 270);
      this.X = (this.N.equals("Modules") ? h_.calculateModules(this.e.width(), this.e.height(), this.I) : h_.calculate(this.e.width(), this.e.height()))
         .offset(0, var1.p.equals("Slide") ? (int)Math.round((1.0 - var14) * 8.0) : 0);
      int var4 = this.X.navigationPitch(this.E.size());
      this.O = Math.clamp(this.O, 0, this.X.navigationScrollLimit(this.E.size()));
      int var5 = this.X.navigationTop() - this.O;

      for (int var6 = 0; var6 < this.E.size(); var6++) {
         if (var6 == this.T) {
            var5 += this.X.navigationGap();
         }

         s8 var7 = this.E.get(var6);
         var7.bounds(this.X.left() + 4, var5, this.X.navigationRight() - this.X.left() - 4);
         var7.p = 1.0;
         var7.O = var4 - 1;
         var5 += var4;
         var7.a = this.N.equals("Modules") ? var7.W.equals(this.d.f) : var7.W.equals(this.N);
      }

      this.c.bounds(this.X.left() + 4, this.X.accountY(), this.X.navigationRight() - this.X.left() - 4);
      this.c.O = 30;
      this.a.bounds(this.X.presetLeft(), this.X.themeSwitchY(), this.X.presetWidth());
      this.a.O = this.X.themeSwitchHeight();
      this.a.G = LarpClient.N().h().k().Y.w();
      this.l.bounds(this.X.larpPresetLeft(), this.X.themeSwitchY(), this.X.presetWidth());
      this.l.O = this.X.themeSwitchHeight();
      this.l.G = LarpClient.N().h().k().c.w();
      this.P.R = this.N.equals("Modules");
      this.P.bounds(this.X.contentX(), this.X.top() + 12, this.X.right() - this.X.contentX());
      this.P.O = 26;
      this.u = this.r.K();
      int var16 = Math.max(1, this.X.bottom() - this.X.bodyTop());
      int var17 = this.X.moduleGridColumns();
      int var8 = 0;
      int var9 = 0;
      int var10 = var17 * this.X.moduleCardWidth() + (var17 - 1) * 6;

      for (s8 var12 : this.o) {
         sj var13 = this.B.get(var12);
         if (var13 != null) {
            if (var8 > 0) {
               var8 = 0;
               var9 += 38;
            }

            var13.bounds(this.X.moduleCardX(0, this.u), var9, var10);
            var9 += var13.O;
         }

         var12.bounds(this.X.moduleCardX(var8, this.u), var9, this.X.moduleCardWidth());
         if (++var8 == var17) {
            var8 = 0;
            var9 += 38;
         }
      }

      this.j = var9 + (var8 > 0 ? 38 : 0);
      this.x = Math.clamp(this.x, 0, Math.max(0, this.j - var16));
      var5 = this.X.bodyTop() - (int)Math.round(this.F.n(this.x, 150));

      for (s8 var20 : this.o) {
         var20.R = (boolean)(this.N.equals("Modules") && !this.Z() ? 1 : 0);
         var20.T += var5;
         var20.O = 34;
         this.S.get(var20).o(var20);
         sj var22 = this.B.get(var20);
         if (var22 != null) {
            var22.R = var20.R;
            var22.T += var5;
         }
      }

      this.A.R = (boolean)(this.N.equals("Modules") && this.n != null ? 1 : 0);
      h_ var19 = this.X.moduleInspector(this.I);
      this.A.bounds(var19.right() - 30, var19.bodyTop() + 1, 22);
      this.A.O = 22;
      if (this.t9 != null) {
         this.t9.u(this.X);
      } else if (this.n != null) {
         h_ var21 = this.X.moduleInspector(this.I);
         this.Q.M(var21, this.X.moduleInspectorClipLeft(this.I), var21.right());
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = he.s();
      this.g();
      this.o();
      double var7 = this.C.K();
      i3 var9 = LarpClient.N().h().k();
      boolean var10 = WorldMenuModule.s(this);
      nl.p(var9.F);
      if (!var10 && this.minecraft.level != null && var9.i.equals("Everywhere")) {
         nl.K();
      }

      if (!var10) {
         z(var1, this.width, this.height, var7, var9);
      }

      this.tl.set(this.e.transform()).mul(wv.d(var9.p, var7, this.X.left() + this.X.width() / 2.0, this.X.top() + this.X.height() / 2.0));
      if (this.h != null) {
         fr.z var11 = this.i ? this.h.h() : this.h.A(System.nanoTime(), this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive(), sd.Z());
         this.tl
            .set(this.e.transform())
            .mul(var11.matrix(this.e.width() / 2.0, this.e.height() / 2.0))
            .mul(wv.d(var9.p, var7, this.X.left() + this.X.width() / 2.0, this.X.top() + this.X.height() / 2.0));
      }

      Vector2f var117 = this.l(var2, var3);
      var2 = Math.round(var117.x);
      var3 = Math.round(var117.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.tl);

      try (
         c.z var12 = com.larpclient.c.N(var9.z, var10 ? 1.0 : var9.j, var9.b, var9.m());
         ic.m var13 = ic.Z();
         oa var14 = oa.j(wv.T(var9.p, var7));
      ) {
         if (!var10 && com.larpclient.c.t().accentGlow()) {
            wc.q(var1, this.X.left(), this.X.top(), this.X.width(), this.X.height());
         }

         wc.A(var1, this.X.left(), this.X.top(), this.X.width(), this.X.height());
         this.Y(var1, this.U() != null && this.U().c() ? -1 : var2, var3);
         wc.M(var1, this.X.left(), this.X.top(), this.X.width(), this.X.height());
         this.a.render(var1, this.U() != null && this.U().c() ? -1 : var2, var3);
         this.l.render(var1, this.U() != null && this.U().c() ? -1 : var2, var3);
         if (this.t9 != null) {
            this.t9.extractRenderState(var1, var2, var3, var4);
         } else {
            this.P.render(var1, var2, var3);
            var1.enableScissor(this.X.moduleClipLeft(), this.X.bodyTop(), this.l(), this.X.bottom());

            try (oa var15 = oa.j(0.35 + 0.65 * this.u)) {
               for (sj var17 : this.B.values()) {
                  if (var17.T + var17.O > this.X.bodyTop() && var17.T < this.X.bottom()) {
                     var17.render(var1, -1, var3);
                  }
               }

               for (s8 var127 : this.o) {
                  Module var18 = this.k.get(var127);
                  var127.a = (boolean)(var18 == this.n ? 1 : 0);
                  var127.G = var18.isEnabled();
                  var127.z = this.t.isBlank() ? var18.getDescription() : q_.U(var18).f + " · " + var18.getDescription();
                  if (var127.T + var127.O > this.X.bodyTop() && var127.T < this.X.bottom()) {
                     int var19 = var3 >= this.X.bodyTop() && var3 < this.X.bottom() && var2 >= this.X.contentX() && var2 < this.l() ? var2 : -1;
                     var127.render(var1, var19, var3);
                     this.S.get(var127).render(var1, var19, var3);
                  }
               }

               if (this.o.isEmpty() && !this.Z()) {
                  String var123 = this.d == q_.N && this.t.isBlank() ? "Star a module to add it here." : "No matches";
                  ic.N(var1, ic.U(var123, this.l() - this.X.contentX() - 16), this.X.moduleListLeft(this.u) + 8, this.X.bodyTop() + 12, com.larpclient.c.W);
               }
            } finally {
               var1.disableScissor();
            }

            if (!this.Z()) {
               wc.V(
                  var1, this.X.moduleScrollbarX(this.I), this.X.bodyTop(), Math.max(1, this.X.bottom() - this.X.bodyTop()), this.j, (int)Math.round(this.F.K())
               );
            }

            if (this.n != null) {
               h_ var118 = this.X.moduleInspector(this.I);
               int var124 = this.X.columns() ? var118.inspectorX() : var118.contentX();
               double var128 = this.y();
               if (this.X.detachedInspector()) {
                  try (oa var133 = oa.j(var128)) {
                     wc.Q(var1, var124, var118.top(), var118.right() - var124, var118.height());
                  }
               }

               var1.enableScissor(this.X.moduleInspectorClipLeft(this.I), this.y(), var118.right(), this.q());

               try (oa var134 = oa.j(var128)) {
                  if (!this.X.detachedInspector()) {
                     wc.L(var1, var124, this.X.bodyTop(), var118.right() - var124, this.X.bottom() - this.X.bodyTop());
                  }

                  this.Q.extractRenderState(var1, var2, var3, var4);
                  this.A.render(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }

               var1.enableScissor(this.X.moduleInspectorClipLeft(this.I), var118.top() + 12, var118.right(), this.q());

               try (oa var135 = oa.j(var128)) {
                  this.Q.X(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }
            }

            for (s8 var125 : this.o) {
               sv var129 = this.S.get(var125);
               boolean var131 = this.accepts(var129, var2, var3);
               if ((this.U() == null || !this.U().c())
                  && var129.R
                  && (var131 || var129.F && var129.X)
                  && var129.T >= this.X.bodyTop()
                  && var129.T + var129.O <= this.X.bottom()) {
                  var129.x(var1, this.e.width(), this.e.height());
                  break;
               }
            }
         }

         fp var120 = this.U();
         String var126 = k(var120, var120 == null ? "" : var120.d(var2, var3));
         String var130 = LarpClient.N().h().u();
         if (!var130.isBlank()) {
            var126 = var130;
         }

         if (!this.Z.isBlank()) {
            var126 = this.Z;
         }

         if (var126.isBlank() && (this.a.contains(var2, var3) || this.a.F && this.a.X)) {
            var126 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var126.isBlank() && (this.l.contains(var2, var3) || this.l.F && this.l.X)) {
            var126 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var126.isBlank()) {
            var126 = "Enter opens settings · Space toggles · Tab navigates · Right click resets a setting";
         }

         int var132 = this.X.profileLeft();
         String var136 = "Profile: " + LarpClient.N().h().S();
         ic.N(var1, ic.o(var136, this.X.profileWidth(), ic.n.I), var132, this.X.profileY(), com.larpclient.c.W, ic.n.I);
         ic.N(var1, ic.o(var126, this.X.helpWidth(), ic.n.I), this.X.helpLeft(), this.X.helpY(), this.Z.isBlank() ? com.larpclient.c.W : -24915, ic.n.I);
         double var121 = wv.m(var9.p, var7);
         if (var121 > 0.01) {
            w1.a(var1, this.X.left(), this.X.top(), this.X.width(), this.X.height(), com.larpclient.c.O, vv.N(-1, var121));
         }

         if (var10) {
            WorldMenuModule.w(var1, this, this.X, var2, var3);
         }
      } finally {
         var1.pose().popMatrix();
      }

      he.T("ClickGUI", var5);
   }

   public double[] u() {
      float var1 = this.e.scale();
      return new double[]{this.X.left() * var1, this.X.top() * var1, (this.X.left() + this.X.width()) * var1, (this.X.top() + this.X.height()) * var1};
   }

   public Screen E() {
      return this.W;
   }

   public boolean K() {
      return this.i && this.C.K() <= 0.0;
   }

   public boolean d(double var1, double var3) {
      Vector2f var5 = this.l(var1, var3);
      if (var5.x >= this.X.left() && var5.x < this.X.left() + this.X.width() && var5.y >= this.X.top() && var5.y < this.X.top() + this.X.height()) {
         return true;
      }

      if (this.n != null && this.X.detachedInspector() && this.y() > 0.2) {
         h_ var6 = this.X.moduleInspector(this.I);
         if (var5.x >= var6.inspectorX() && var5.x < var6.right() && var5.y >= var6.top() && var5.y < var6.top() + var6.height()) {
            return true;
         }
      }

      return WorldMenuModule.M(this.X, var5.x, var5.y);
   }

   public boolean g() {
      fp var1 = this.U();
      return q(this.q) || var1 != null && q(var1.t());
   }

   static boolean q(j0 var0) {
      return var0 instanceof sk var2 && var2.F || var0 instanceof su var1 && var1.T();
   }

   public void J() {
      fp var1 = this.U();
      j0 var2 = var1 == null ? null : var1.t();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.q != null && this.q != var2) {
         this.q.key(256);
      }

      this.T();
   }

   public void T() {
      fp var1 = this.U();
      if (var1 != null) {
         var1.y();
      }

      if (this.q != null) {
         this.q.blur();
      }

      this.q = this.g = null;
   }

   private static void z(GuiGraphicsExtractor var0, int var1, int var2, double var3, i3 var5) {
      int var6 = vv.N(-16777216, var5.Z);
      switch (var5.Ju) {
         case "None":
            break;
         case "Gradient":
            int var11 = vv.O(var6, var3);
            int var10 = vv.O(vv.Q(var6, vv.N(com.larpclient.c.y, 0.5), 0.35), var3);
            h5.f(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            v1.i var9 = v1.i.valueOf(var5.Ju.toUpperCase(Locale.ROOT));
            if (!v1.f(var0, var1, var2, var3, com.larpclient.c.y, com.larpclient.c.t, var5.z, var5.w, var9, var5.Z)) {
               h5.N(var0, 0, 0, var1, var2, vv.O(var6, var3));
            }
            break;
         default:
            h5.N(var0, 0, 0, var1, var2, vv.O(var6, var3));
      }
   }

   static String k(fp var0, String var1) {
      return var0 instanceof fk var2 && !var2.w.isBlank() ? var2.w : var1;
   }

   private void Y(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = this.X.contentX() - this.X.left() - 10;
      var1.enableScissor(this.X.left(), this.X.top(), this.X.contentX() - 10, this.X.navigationTop());

      try {
         var1.blit(RenderPipelines.GUI_TEXTURED, com.larpclient.h.b(), this.X.left() + 7, this.X.top() + 10, 0.0F, 0.0F, 22, 22, 64, 64, 64, 64, oa.e(-1));

         try (oa var5 = oa.j(1.0)) {
            int var6 = this.X.left() + 35;
            int var7 = var4 - 39;
            ic.N(var1, ic.o("Larp Client", var7, ic.n.t), var6, this.X.top() + 10, com.larpclient.c.H, ic.n.t);
            String var8 = "Version ";
            int var9 = var6 + ic.f(var8, ic.n.I);
            ic.N(var1, var8, var6, this.X.top() + 24, com.larpclient.c.f, ic.n.I);
            ic.N(
               var1, ic.o(LarpClient.G, Math.max(1, var6 + var7 - var9), ic.n.I), var9, this.X.top() + 24, com.larpclient.c.e(var9, com.larpclient.c.D), ic.n.I
            );
            h(var1, "FEATURES", this.X.left() + 12, this.X.navigationTop() - 12, this.X.navigationRight());
         }
      } finally {
         var1.disableScissor();
      }

      var1.enableScissor(this.X.left(), this.X.navigationTop(), this.X.contentX() - 10, this.X.navigationBottom());

      try {
         try (oa var49 = oa.j(1.0)) {
            h(var1, "OTHERS", this.X.left() + 12, this.E.get(this.T).T - 12, this.X.navigationRight());
         }

         this.R(var1);

         for (s8 var52 : this.E) {
            var52.render(var1, var3 >= this.X.navigationTop() && var3 < this.X.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.disableScissor();
      }

      if (this.X.navigationScrollLimit(this.E.size()) > 0) {
         wc.V(
            var1,
            this.X.contentX() - 12,
            this.X.navigationTop(),
            this.X.navigationBottom() - this.X.navigationTop(),
            this.E.size() * this.X.navigationPitch(this.E.size()) + this.X.navigationGap(),
            this.O
         );
      }

      var1.enableScissor(this.X.left(), this.X.accountY(), this.X.contentX() - 10, this.X.accountY() + this.c.O);

      try {
         double var51 = this.c.b(var2, var3);
         if (var51 > 0.001) {
            h5.q(var1, this.c.P, this.c.T, this.c.L, this.c.O, com.larpclient.c.i, vv.N(com.larpclient.c.H, var51 * 0.045));
         }

         b3 var53 = b3.current();
         if (!p.A(var1, this.X.left() + 8, this.c.T + 5, 20)) {
            this.U.K(var1, this.X.left() + 8, this.c.T + 5, 20);
         }

         h5.M(var1, this.X.left() + 25, this.c.T + 24, 4, com.larpclient.c.D);
         h5.M(var1, this.X.left() + 25, this.c.T + 24, 3, var53.state().G());

         try (oa var54 = oa.j(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.minecraft.getUser().getName();
            ic.W(var1, ic.N(var10, var55, ic.b.R), this.X.left() + 34, this.c.T + 6, com.larpclient.c.H, ic.b.R);
            ic.N(var1, ic.o(var53.state().R(), var55, ic.n.I), this.X.left() + 34, this.c.T + 18, com.larpclient.c.W, ic.n.I);
         }

         if (this.c.F && this.c.X) {
            wc.U(var1, this.c.P, this.c.T, this.c.L, this.c.O, com.larpclient.c.i);
         }
      } finally {
         var1.disableScissor();
      }
   }

   private static void h(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      ic.N(var0, var1, var2, var3, vv.N(com.larpclient.c.W, 0.8), ic.n.L);
   }

   private void R(GuiGraphicsExtractor var1) {
      s8 var2 = null;

      for (s8 var4 : this.E) {
         if (var4.a) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.m.n(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.T - this.X.navigationTop() + this.O;
         if (!this.L) {
            this.D.p(var5);
            this.L = (boolean)1;
         }

         this.D.n(var5, 200);
      }

      if (this.L && !(var9 < 0.01)) {
         s8 var10 = this.E.getFirst();
         float var6 = this.X.navigationTop() - this.O + (float)this.D.K();
         int var7 = com.larpclient.c.e(var10.P, com.larpclient.c.D);
         int var8 = com.larpclient.c.i;
         wc.b(var1, var10.P, var6, var10.L, var10.O, var8, 9.0F, vv.N(var7, 0.22 * var9));
         w1.c(var1, var10.P, var6, var10.L, var10.O, var8, vv.N(var7, 0.26 * var9), vv.N(com.larpclient.c.t, 0.08 * var9));
         w1.T(var1, var10.P, var6, var10.L, var10.O, var8, vv.N(var7, 0.35 * var9), vv.N(var7, 0.35 * var9));
      }
   }

   @Override
   protected boolean accepts(j0 var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.E.contains(var1)) {
         return var4 >= this.X.navigationTop() && var4 < this.X.navigationBottom();
      } else {
         return !this.o.contains(var1) && !this.S.containsValue(var1)
            ? true
            : !this.Z() && var4 >= this.X.bodyTop() && var4 < this.X.bottom() && var2 >= this.X.contentX() && var2 < this.l();
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.i) {
         return true;
      }

      this.M = (boolean)1;
      var1 = this.M(var1);
      this.o();
      fp var3 = this.U();
      j0 var4 = var3 == null ? null : var3.t();
      if (var4 != null && var4.captureMouse(j2.L(var1))) {
         return true;
      }

      if (j2.L(var1) > 1 && LarpClient.N().t() == p4.f(j2.L(var1))) {
         this.onClose();
         return true;
      }

      if (var3 != null && var3.c()) {
         return var3.mouseClicked(var1, var2);
      }

      if (WorldMenuModule.G(this, var1.x(), var1.y(), j2.L(var1), this.X)) {
         if (var3 != null) {
            var3.y();
         }

         return true;
      } else {
         for (s8 var6 : this.E) {
            if (this.accepts(var6, var1.x(), var1.y())) {
               if (var6.a && this.N.equals("Modules")) {
                  return var6.click(var1.x(), var1.y(), j2.L(var1));
               }

               return super.mouseClicked(var1, var2);
            }
         }

         if (this.y() > 0.2 && this.A.contains(var1.x(), var1.y()) && var1.x() < this.X.moduleInspector(this.I).right()) {
            return super.mouseClicked(var1, var2);
         }

         int var8 = this.t9 != null
            ? this.X.containsWorkspace(var1.x(), var1.y())
            : (
               this.y() > 0.2
                     && (!this.f || this.X.columns())
                     && var1.x() >= this.X.moduleInspectorClipLeft(this.I)
                     && var1.x() < this.X.moduleInspector(this.I).right()
                     && var1.y() >= this.y()
                     && var1.y() < this.q()
                  ? 1
                  : 0
            );
         if (var3 != null && var8 != 0) {
            if (this.q != null) {
               this.q.blur();
            }

            this.q = this.g = null;
            return this.f && this.t9 == null ? true : var3.mouseClicked(var1, var2);
         } else {
            if (var3 != null) {
               var3.y();
            }

            return super.mouseClicked(var1, var2);
         }
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.i) {
         return true;
      } else {
         Vector2f var9 = this.l(var1, var3);
         var1 = var9.x;
         var3 = var9.y;
         this.o();
         fp var10 = this.U();
         if ((var10 == null || !var10.c()) && this.X.containsNavigation(var1, var3) && var3 >= this.X.navigationTop() && var3 < this.X.navigationBottom()) {
            this.O -= (int)(var7 * 28.0);
            this.o();
            return true;
         } else if (var10 != null && !this.f && (!this.X.detachedInspector() || this.y() > 0.2) && var10.mouseScrolled(var1, var3, var5, var7)) {
            return true;
         } else if (this.N.equals("Modules") && !this.Z() && var1 >= this.X.contentX() && var1 < this.l() && var3 >= this.X.bodyTop() && var3 < this.X.bottom()
            )
          {
            this.x -= (int)(var7 * 27.0);
            this.o();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(j0 var1) {
      if (this.E.contains(var1)) {
         if (var1.T < this.X.navigationTop()) {
            this.O = this.O - (this.X.navigationTop() - var1.T);
         } else if (var1.T + var1.O > this.X.navigationBottom()) {
            this.O = this.O + (var1.T + var1.O - this.X.navigationBottom());
         }

         this.o();
      }

      if (this.o.contains(var1) || this.S.containsValue(var1)) {
         for (Entry var3 : this.S.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (j0)var3.getKey();
               break;
            }
         }

         int var4 = this.B.containsKey(var1) ? this.B.get(var1).T : var1.T;
         if (var4 < this.X.bodyTop()) {
            this.x = this.x - (this.X.bodyTop() - var4);
         } else if (var1.T + var1.O > this.X.bottom()) {
            this.x = this.x + (var1.T + var1.O - this.X.bottom());
         }

         this.F.p(this.x);
         this.o();
      }
   }

   static boolean z(int var0, j0 var1, j0 var2, Runnable var3) {
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
      if (this.i) {
         return true;
      }

      fp var2 = this.U();
      if (z(j2.P(var1), var2 == null ? null : var2.t(), this.q, this::onClose)) {
         return true;
      }

      if (this.P != null
         && this.N.equals("Modules")
         && this.q == this.P
         && (var2 == null || !var2.c())
         && j2.P(var1) == 258
         && (var1.modifiers() & 1) == 0
         && this.P.d()) {
         return true;
      }

      if (j2.P(var1) != 258 || var2 != null && var2.c()) {
         return var2 == null || var2.t() == null && !var2.c() ? super.keyPressed(var1) : var2.keyPressed(var1);
      }

      ArrayList var3 = new ArrayList();
      this.J.stream().filter(var0 -> var0.R && var0.V).forEach(var3::add);
      if (var2 != null && !this.f && (!this.X.detachedInspector() || this.y() > 0.2)) {
         var3.addAll(var2.j());
      }

      j0 var4 = var2 != null && var2.t() != null ? var2.t() : this.q;
      int var5 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
      if (this.q != null) {
         this.q.blur();
      }

      this.q = null;
      if (var2 != null) {
         var2.y();
      }

      if (!var3.isEmpty()) {
         j0 var7 = (j0)var3.get(Math.floorMod(var6 + var5, var3.size()));
         if (this.J.contains(var7)) {
            this.q = var7;
            var7.F = var7.X = (boolean)1;
            this.focusChanged(var7);
         } else {
            var2.h(var7);
         }
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.i) {
         return true;
      }

      fp var2 = this.U();
      return var2 != null && var2.t() != null ? var2.charTyped(var1) : super.charTyped(var1);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (!this.i && !this.f) {
         Vector2f var6 = this.l(var1.x(), var1.y());
         Vector2f var7 = this.l(var1.x() - var2, var1.y() - var4);
         var1 = this.M(var1);
         var2 = var6.x - var7.x;
         var4 = var6.y - var7.y;
         fp var8 = this.U();
         return var8 != null && var8.mouseDragged(var1, var2, var4) || super.mouseDragged(var1, var2, var4);
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      this.M = (boolean)0;
      var1 = this.M(var1);
      if (this.U() != null) {
         this.U().mouseReleased(var1);
      }

      return super.mouseReleased(var1);
   }

   public void tick() {
      this.g();
      if (this.R && System.nanoTime() >= this.V) {
         this.R = (boolean)0;
         this.L();
      }

      if (!this.i && this.U() != null) {
         this.U().tick();
      }
   }

   void b() {
      if (this.d != q_.N) {
         bh.C = this.d.name();
      }

      bh.c = new bh.v(this.N, this.n == null ? "" : this.n.getName(), this.t, this.x, this.Q.L);
   }

   public void removed() {
      if (this.h != null && !this.i) {
         this.h.C();
      }

      if (this.t9 != null) {
         this.t9.suspend();
      }

      this.Q.suspend();
      this.b();
      aq var1 = LarpClient.N().h();
      if (this.Y) {
         this.P();
      }

      var1.z();
      _q.c("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}

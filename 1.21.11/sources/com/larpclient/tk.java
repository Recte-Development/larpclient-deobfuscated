package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.WorldMenuModule;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_10799;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class tk extends t1 {
   private final class_437 O;
   private final List<ls> u = new ArrayList<>();
   private final List<ls> n = new ArrayList<>();
   private final Map<ls, Module> Z = new IdentityHashMap<>();
   private final Map<ls, li> M = new IdentityHashMap<>();
   private final Map<ls, l5> r = new IdentityHashMap<>();
   private int N;
   private boolean R;
   private boolean S5;
   private long a;
   private final t2 T = new t2();
   private final ty j = new ty();
   private tf H;
   private t6 S3;
   private bu U;
   private eh w;
   private yc e;
   private int E;
   private Module b;
   private String s = "Modules";
   private String I = "";
   private String t = "";
   private boolean Q;
   private boolean g;
   private boolean h;
   private static tk m;
   private Object V;
   private lr o;
   private int c;
   private int S;
   private double i;
   private double L = 1.0;
   private boolean A;
   private boolean D;
   private boolean l;
   private final qb v = new qb(0.0);
   private final qb Y = new qb(1.0);
   private lo W;
   private ls z;
   private ls q;
   private ls k;
   private ls B;
   private final ht x = new ht();
   private static final m f = new m();
   private final qb d = new qb(0.0);
   private final qb F = new qb(0.0);
   private final qb X = new qb(0.0);
   private final qb P = new qb(0.0);
   private boolean C;
   private jr p;
   private final Matrix3x2f J = new Matrix3x2f();

   public tk() {
      this(null);
   }

   public tk(class_437 var1) {
      super("Larp client");
      this.O = var1;
      this.e = LarpClient.b().e().F().I ? yc.e : yc.p(hz.k);
   }

   public void V() {
      this.p = new jr();
   }

   public static tk W(class_437 var0, String var1, boolean var2) {
      tk var3 = new tk(var0);
      var3.A = (boolean)1;
      var3.s = "Configs";
      var3.H = new tf(var3);
      if (var1 != null) {
         var3.H.c.q = var1;
      }

      var3.H.JT = var2;
      return var3;
   }

   public static tk a(class_437 var0) {
      tk var1 = W(var0, null, false);
      var1.Q = (boolean)1;
      return var1;
   }

   public static tk Q(class_437 var0, String var1) {
      tk var2 = new tk(var0);
      var2.A = (boolean)1;
      var2.s = "Settings";
      var2.j.C(var1);
      return var2;
   }

   public static tk V(class_437 var0, k.l var1) {
      tk var2 = Q(var0, "HUD");
      var2.j.A(var1);
      return var2;
   }

   class_310 c() {
      return this.field_22787;
   }

   boolean b(tf var1) {
      return this.S3 == var1;
   }

   public void method_25419() {
      if (!this.D) {
         if (this.S3 != null) {
            this.S3.suspend();
         }

         this.T.y();
         c_.b(c_.d.Y);
         this.D = (boolean)1;
         this.d.a(0.0, this.v());
         if (!WorldMenuModule.G(this)) {
            this.V = this.field_22787.field_1687;
            m = this;
            this.o = this.H();
            if (this.O instanceof hh && !LarpClient.b().e().F().A) {
               q8.G("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.field_22787.method_1507(new class_442());
            } else {
               this.field_22787.method_1507(this.O);
            }
         }
      }
   }

   private int v() {
      return LarpClient.b().e().F().G;
   }

   private lr H() {
      y0 var1 = LarpClient.b().e().F();
      String var2 = var1.R;
      if (!var2.equals("Reverse") && var1.k) {
         ArrayList var3 = new ArrayList();
         float var4 = this.J.m00();
         Vector2f var5 = this.J.transformPosition(this.U.left(), this.U.top(), new Vector2f());
         var3.add(new lr.b(var5.x, var5.y, this.U.width() * var4, this.U.height() * var4, ez.j * var4));
         if (this.b != null && this.S3 == null && this.U.detachedInspector() && this.d() > 0.5) {
            bu var6 = this.U.moduleInspector(this.i);
            Vector2f var7 = this.J.transformPosition(var6.inspectorX(), var6.top(), new Vector2f());
            var3.add(new lr.b(var7.x, var7.y, (var6.right() - var6.inspectorX()) * var4, var6.height() * var4, ez.j * var4));
         }

         return lr.w(var2, var3, this.v());
      } else {
         return null;
      }
   }

   public static boolean n() {
      return m != null;
   }

   public static void h(class_332 var0, float var1) {
      tk var2 = m;
      if (var2 != null) {
         class_310 var3 = class_310.method_1551();
         lr var4 = var2.o;
         if (var3.field_1755 == var2.O
            && var3.field_1687 == var2.V
            && var3.method_18506() == null
            && var2.field_22789 == var0.method_51421()
            && var2.field_22790 == var0.method_51443()
            && var2.w.equals(A(var3))
            && (var4 == null ? !(var2.d.u() <= 0.0) : !var4.k())) {
            try {
               var0.method_71048();
               if (var4 != null) {
                  y0 var5 = LarpClient.b().e().F();

                  try (ez.m var6 = ez.Q(var5.Gr, var5.G7, var5.O)) {
                     i(var0, var0.method_51421(), var0.method_51443(), 1.0 - var4.X(), var5);
                  }

                  var4.Z(var0);
               } else {
                  var2.method_25394(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var11) {
               i();
               q8.u("GUI", "Window exit rendering failed; input remains released", var11);
            }
         } else {
            if (var4 != null) {
               q8.v("GUI", "Close effect {}", var4.k() ? "finished" : "stopped by a screen, world or window size change");
            }

            i();
         }
      }
   }

   private static void i() {
      if (m != null && m.o != null) {
         m.o = null;
         lr.v();
      }

      m = null;
   }

   private static eh A(class_310 var0) {
      return eh.paired(var0.method_22683(), LarpClient.b().e().F().f);
   }

   protected void method_25426() {
      this.w = A(this.field_22787);
      this.J.set(this.w.transform());
      i();
      if (!this.A) {
         hz.y var1 = hz.Z;
         this.s = var1.page();
         this.I = var1.query();
         this.c = var1.moduleScroll();
         this.b = LarpClient.b().G().j(var1.module());
         this.b = ChooseASkyEffectAndAdjustItsAppearanceModule.z(this.b, LarpClient.b().G().M(), LarpClient.b().e().F());
         if (this.b == null && !var1.module().isEmpty()) {
            q8.G("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.b != null && this.I.isBlank() && this.e != yc.e) {
            this.e = yc.w(this.b);
         }

         this.A = (boolean)1;
      }

      if (!yc.A().contains(this.e)) {
         this.e = yc.I;
      }

      if (this.b != null && !(this.b instanceof ChooseASkyEffectAndAdjustItsAppearanceModule) && com.larpclient.module.lf.Y(this.b)) {
         this.b = null;
      }

      this.U = this.s.equals("Modules")
         ? bu.calculateModules(this.w.width(), this.w.height(), this.b == null ? 0.0 : 1.0)
         : bu.calculate(this.w.width(), this.w.height());
      this.D = (boolean)0;
      this.d.w(this.p == null ? 0.0 : 1.0);
      this.d.a(1.0, this.v());
      if (!this.g) {
         this.g = (boolean)1;
         c_.b(c_.d.R);
      }

      this.l = (boolean)0;
      this.F.w(this.b == null ? 0.0 : 1.0);
      this.v.w(this.c);
      this.d();
      this.v.w(this.c);
      if (this.b != null) {
         int var2 = this.T.r == null ? hz.Z.settingScroll() : this.T.f;
         this.T.J(this, this.U);
         this.T.T(this.b);
         this.T.f = var2;
         this.F.w(1.0);
      }

      this.J();
      q8.v(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.field_22789,
         this.field_22790,
         this.field_22787.method_22683().method_4495(),
         this.w.width(),
         this.w.height(),
         this.w.scale(),
         this.s
      );
   }

   private void J() {
      this.S3 = switch (this.s) {
         case "Configs" -> {
            if (this.H == null) {
               this.H = new tf(this);
            }

            yield this.H;
         }
         case "Settings" -> this.j;
         default -> null;
      };
      if (this.S3 != null) {
         this.S3.J(this, this.U);
      }

      if (this.Q && this.S3 == this.H) {
         this.Q = (boolean)0;
         this.H.b("Discover");
      }
   }

   private void N(String var1) {
      if (!this.s.equals(var1)) {
         c_.b(c_.d.o);
      }

      if (this.l) {
         this.b = null;
         this.l = (boolean)0;
         this.F.w(0.0);
      }

      if (this.S3 != null) {
         this.S3.suspend();
      }

      this.T.suspend();
      this.s = var1;
      this.d();
      this.J();
      if (this.S3 == null && this.b != null) {
         this.T.J(this, this.U);
         this.T.T(this.b);
      }

      this.D();
      q8.v("GUI", "Destination switched to {}", var1);
   }

   void f() {
      this.j.C("HUD");
      this.j.A(null);
      this.j.f = 0;
      this.N("Settings");
   }

   void R() {
      if (this.field_22787.field_1724 != null && this.field_22787.field_1687 != null) {
         this.D();
         this.field_22787.method_1507(new tj(this));
      } else {
         this.t = "Join a world to edit the HUD.";
      }
   }

   void H() {
      if (this.b != null && !this.l) {
         c_.b(c_.d.g);
         this.l = (boolean)1;
         this.T.y();
         this.F.a(0.0, 270);
      }
   }

   private void w() {
      if (this.l && this.F.u() == 0.0) {
         this.b = null;
         this.l = (boolean)0;
         this.T.suspend();
      }
   }

   private void B(yc var1) {
      boolean var2 = var1.z(this.e, () -> {
         if (this.s.equals("Modules")) {
            c_.b(c_.d.o);
         }

         this.Y.d(220);
         y0 var2x = LarpClient.b().e().F();
         if (var2x.I != (var1 == yc.e ? 1 : 0)) {
            var2x.I = (boolean)(var1 == yc.e ? 1 : 0);
            this.S5 = (boolean)1;
            this.g();
         }

         this.e = var1;
         this.c = 0;
         this.v.w(0.0);
         this.b = null;
         this.l = (boolean)0;
         this.I = "";
         this.N("Modules");
      });
      if (!var2 && !this.s.equals("Modules")) {
         this.N("Modules");
      }
   }

   private void d() {
      this.t();
      this.u.clear();
      this.n.clear();
      this.Z.clear();
      this.M.clear();
      List var1 = yc.A();
      this.E = var1.size();

      for (yc var3 : var1) {
         ls var4 = new ls(var3.t, () -> this.B(var3));
         var4.X = null;
         var4.n = var3.W;
         var4.S = (boolean)1;
         var4.t = (boolean)1;
         this.u.add(var4);
         this.K.add(var4);
      }

      for (String var5 : new String[]{"Editor", "Configs", "Settings"}) {
         ls var6 = new ls(var5, () -> {
            if (var5.equals("Editor")) {
               this.R();
            } else {
               this.N(var5);
            }
         });
         var6.X = null;
         var6.n = var5.equals("Editor") ? "panels-top-left" : (var5.equals("Configs") ? "folder" : "settings");
         var6.S = (boolean)1;
         var6.t = (boolean)1;
         this.u.add(var6);
         this.K.add(var6);
      }

      this.B = new ls("Account", () -> {
         this.j.C("Account");
         this.N("Settings");
      });
      this.K.add(this.B);
      this.q = new ls("Israel mode", this::Z);
      this.q.S = (boolean)1;
      this.K.add(this.q);
      this.k = new ls("Larp Preset", this::c);
      this.k.S = (boolean)1;
      this.K.add(this.k);
      this.W = new lo("Search modules...", var1x -> {
         this.I = var1x;
         this.c = 0;
         this.v.w(0.0);
         this.K();
      }, 256);
      this.W.G(var0 -> qi.E(ChooseASkyEffectAndAdjustItsAppearanceModule.C(LarpClient.b().G().Y(), LarpClient.b().e().F()), var0));
      this.W.h = this.I;
      this.W.R = "search";
      this.K.add(this.W);
      this.z = new ls("", this::H);
      this.z.n = "x";
      this.z.S = (boolean)1;
      this.K.add(this.z);
      this.z.X = null;
      this.K();
   }

   private void K() {
      this.R = (boolean)0;
      int var1 = -1;
      int var2 = this.G != null && this.G.q ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.n.size(); var5++) {
         if (this.G == this.n.get(var5) || this.G == this.M.get(this.n.get(var5))) {
            var1 = var5;
            var4 = this.Z.get(this.n.get(var5));
            var3 = (byte)(this.G == this.M.get(this.n.get(var5)) ? 1 : 0);
         }
      }

      if (this.n.contains(this.G) || this.M.containsValue(this.G)) {
         this.G.blur();
         this.G = null;
      }

      this.K.removeAll(this.n);
      this.K.removeAll(this.M.values());
      this.n.clear();
      this.Z.clear();
      this.M.clear();
      this.r.clear();
      y0 var13 = LarpClient.b().e().F();
      String var6 = "";

      for (Module var8 : this.e.T(LarpClient.b().G().Y(), this.I, var13)) {
         String var9 = this.e.U(this.I) ? yc.q(var8) : "";
         int var10 = !var9.equals(var6) ? 1 : 0;
         var6 = var9;
         ls var11 = new ls(var8.getDisplayName(), () -> {
            int var2x = this.b == null ? 1 : 0;
            int var3x = this.b != var8 ? 1 : 0;
            if (var3x != 0) {
               c_.b(c_.d.D);
            }

            this.l = (boolean)0;
            this.b = var8;
            if (var2x != 0) {
               this.T.J(this, this.U.moduleInspector(this.i));
            }

            if (var3x != 0) {
               this.T.T(var8);
            }

            this.F.a(1.0, 270);
            this.M();
         });
         var11.X = null;
         var11.j = var11.i = (boolean)1;
         var11.Y = () -> {
            var8.toggle();
            c_.b(var8.isEnabled() ? c_.d.d : c_.d.n);
         };
         var11.W = var8.isEnabled();
         this.n.add(var11);
         this.Z.put(var11, var8);
         if (var10 != 0 && !var6.isEmpty()) {
            this.r.put(var11, l5.q(var6));
         }

         li var12 = new li(() -> var13.H(var8.getName()), () -> {
            var13.p(var8.getName(), !var13.H(var8.getName()));
            this.S5 = (boolean)1;
            this.g();
            this.R = (boolean)1;
            this.a = System.nanoTime() + 160000000L;
         });
         this.M.put(var11, var12);
         this.K.add(var12);
         this.K.add(var11);
      }

      this.M();
      if (var1 >= 0 && var2 != 0 && !this.n.isEmpty()) {
         ls var14 = this.n.get(Math.min(var1, this.n.size() - 1));

         for (ls var16 : this.n) {
            if (this.Z.get(var16) == var4) {
               var14 = var16;
               break;
            }
         }

         this.G = var3 != 0 ? this.M.get(var14) : var14;
         this.G.M = this.G.q = (boolean)1;
         this.focusChanged(this.G);
      } else if (var1 >= 0 && var2 != 0) {
         this.G = this.W;
         this.G.M = this.G.q = (boolean)1;
      }
   }

   private boolean g() {
      LarpClient.b().e().g();
      this.S5 = (boolean)0;
      return true;
   }

   private void Z() {
      LarpClient var1 = LarpClient.b();
      qk var2 = var1.e();
      f_ var3 = var2.F().B;
      boolean var4 = var2.V(!var3.B(), var1.G().M());
      if (!var4) {
         this.t = "Israel mode could not be changed. Click to retry; see the client log.";
         q8.L("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.t.startsWith("Israel mode")) {
         this.t = "";
      }

      if (this.b != null && this.S3 == null) {
         this.T.O();
      }
   }

   private void c() {
      LarpClient var1 = LarpClient.b();
      qk var2 = var1.e();
      if (!var2.h(!var2.F().d.C(), var1.G().M())) {
         this.t = "Larp Preset could not be changed. Click to retry; see the client log.";
         q8.L("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.t.startsWith("Larp Preset")) {
         this.t = "";
      }

      if (this.b != null && this.S3 == null) {
         this.T.O();
      }
   }

   private Vector2f u(double var1, double var3) {
      return new Matrix3x2f(this.J).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private class_11909 w(class_11909 var1) {
      Vector2f var2 = this.u(var1.comp_4798(), var1.comp_4799());
      return new class_11909(var2.x, var2.y, var1.comp_4800());
   }

   private t6 z() {
      return this.S3 != null ? this.S3 : (this.b != null ? this.T : null);
   }

   private boolean J() {
      return this.S3 == null && this.b != null && !this.l && !this.U.columns();
   }

   private int T() {
      return this.U.moduleListRight(this.i);
   }

   private double d() {
      return this.U.detachedInspector() ? Math.clamp(this.i * 2.0 - 1.0, 0.0, 1.0) : this.i;
   }

   private int z() {
      return this.U.detachedInspector() ? this.U.top() : this.U.bodyTop();
   }

   private int E() {
      return this.U.detachedInspector() ? this.U.top() + this.U.height() : this.U.bottom();
   }

   private void M() {
      y0 var1 = LarpClient.b().e().F();
      if (!this.h) {
         eh var2 = A(this.field_22787);
         if (!var2.equals(this.w)) {
            this.w = var2;
            q8.v("GUI", "ClickGUI size {}%: workspace {}x{} / factor {}", Math.round(var1.f * 100.0), this.w.width(), this.w.height(), this.w.scale());
         }
      }

      double var14 = this.d.a(this.D ? 0.0 : 1.0, var1.G);
      this.i = this.F.a(this.s.equals("Modules") && this.b != null && !this.l ? 1.0 : 0.0, 270);
      this.U = (this.s.equals("Modules") ? bu.calculateModules(this.w.width(), this.w.height(), this.i) : bu.calculate(this.w.width(), this.w.height()))
         .offset(0, var1.K.equals("Slide") ? (int)Math.round((1.0 - var14) * 8.0) : 0);
      int var4 = this.U.navigationPitch(this.u.size());
      this.S = Math.clamp(this.S, 0, this.U.navigationScrollLimit(this.u.size()));
      int var5 = this.U.navigationTop() - this.S;

      for (int var6 = 0; var6 < this.u.size(); var6++) {
         if (var6 == this.E) {
            var5 += this.U.navigationGap();
         }

         ls var7 = this.u.get(var6);
         var7.bounds(this.U.left() + 4, var5, this.U.navigationRight() - this.U.left() - 4);
         var7.B = 1.0;
         var7.w = var4 - 1;
         var5 += var4;
         var7.o = this.s.equals("Modules") ? var7.O.equals(this.e.t) : var7.O.equals(this.s);
      }

      this.B.bounds(this.U.left() + 4, this.U.accountY(), this.U.navigationRight() - this.U.left() - 4);
      this.B.w = 30;
      this.q.bounds(this.U.presetLeft(), this.U.themeSwitchY(), this.U.presetWidth());
      this.q.w = this.U.themeSwitchHeight();
      this.q.W = LarpClient.b().e().F().B.B();
      this.k.bounds(this.U.larpPresetLeft(), this.U.themeSwitchY(), this.U.presetWidth());
      this.k.w = this.U.themeSwitchHeight();
      this.k.W = LarpClient.b().e().F().d.C();
      this.W.I = this.s.equals("Modules");
      this.W.bounds(this.U.contentX(), this.U.top() + 12, this.U.right() - this.U.contentX());
      this.W.w = 26;
      this.L = this.Y.u();
      int var16 = Math.max(1, this.U.bottom() - this.U.bodyTop());
      int var17 = this.U.moduleGridColumns();
      int var8 = 0;
      int var9 = 0;
      int var10 = var17 * this.U.moduleCardWidth() + (var17 - 1) * 6;

      for (ls var12 : this.n) {
         l5 var13 = this.r.get(var12);
         if (var13 != null) {
            if (var8 > 0) {
               var8 = 0;
               var9 += 38;
            }

            var13.bounds(this.U.moduleCardX(0, this.L), var9, var10);
            var9 += var13.w;
         }

         var12.bounds(this.U.moduleCardX(var8, this.L), var9, this.U.moduleCardWidth());
         if (++var8 == var17) {
            var8 = 0;
            var9 += 38;
         }
      }

      this.N = var9 + (var8 > 0 ? 38 : 0);
      this.c = Math.clamp(this.c, 0, Math.max(0, this.N - var16));
      var5 = this.U.bodyTop() - (int)Math.round(this.v.a(this.c, 150));

      for (ls var20 : this.n) {
         var20.I = (boolean)(this.s.equals("Modules") && !this.J() ? 1 : 0);
         var20.g += var5;
         var20.w = 34;
         this.M.get(var20).V(var20);
         l5 var22 = this.r.get(var20);
         if (var22 != null) {
            var22.I = var20.I;
            var22.g += var5;
         }
      }

      this.z.I = (boolean)(this.s.equals("Modules") && this.b != null ? 1 : 0);
      bu var19 = this.U.moduleInspector(this.i);
      this.z.bounds(var19.right() - 30, var19.bodyTop() + 1, 22);
      this.z.w = 22;
      if (this.S3 != null) {
         this.S3.L(this.U);
      } else if (this.b != null) {
         bu var21 = this.U.moduleInspector(this.i);
         this.T.Q(var21, this.U.moduleInspectorClipLeft(this.i), var21.right());
      }
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      long var5 = c5.X();
      this.w();
      this.M();
      double var7 = this.d.u();
      y0 var9 = LarpClient.b().e().F();
      boolean var10 = WorldMenuModule.X(this);
      t_.O(var9.p);
      if (!var10 && this.field_22787.field_1687 != null && var9.T.equals("Everywhere")) {
         t_.i();
      }

      if (!var10) {
         i(var1, this.field_22789, this.field_22790, var7, var9);
      }

      this.J.set(this.w.transform()).mul(sd.X(var9.K, var7, this.U.left() + this.U.width() / 2.0, this.U.top() + this.U.height() / 2.0));
      if (this.p != null) {
         ix.g var11 = this.D ? this.p.V() : this.p.U(System.nanoTime(), this.field_22787.method_18506() == null && this.field_22787.method_1569(), qx.x());
         this.J
            .set(this.w.transform())
            .mul(var11.matrix(this.w.width() / 2.0, this.w.height() / 2.0))
            .mul(sd.X(var9.K, var7, this.U.left() + this.U.width() / 2.0, this.U.top() + this.U.height() / 2.0));
      }

      Vector2f var117 = this.u(var2, var3);
      var2 = Math.round(var117.x);
      var3 = Math.round(var117.y);
      var1.method_51448().pushMatrix();
      var1.method_51448().mul(this.J);

      try (
         ez.m var12 = ez.G(var9.Gr, var10 ? 1.0 : var9.G7, var9.O, var9.S());
         uw.h var13 = uw.S();
         fp var14 = fp.O(sd.Q(var9.K, var7));
      ) {
         if (!var10 && ez.x().accentGlow()) {
            com.larpclient.p.z(var1, this.U.left(), this.U.top(), this.U.width(), this.U.height());
         }

         com.larpclient.p.T(var1, this.U.left(), this.U.top(), this.U.width(), this.U.height());
         this.k(var1, this.z() != null && this.z().N() ? -1 : var2, var3);
         com.larpclient.p.Y(var1, this.U.left(), this.U.top(), this.U.width(), this.U.height());
         this.q.render(var1, this.z() != null && this.z().N() ? -1 : var2, var3);
         this.k.render(var1, this.z() != null && this.z().N() ? -1 : var2, var3);
         if (this.S3 != null) {
            this.S3.method_25394(var1, var2, var3, var4);
         } else {
            this.W.render(var1, var2, var3);
            var1.method_44379(this.U.moduleClipLeft(), this.U.bodyTop(), this.T(), this.U.bottom());

            try (fp var15 = fp.O(0.35 + 0.65 * this.L)) {
               for (l5 var17 : this.r.values()) {
                  if (var17.g + var17.w > this.U.bodyTop() && var17.g < this.U.bottom()) {
                     var17.render(var1, -1, var3);
                  }
               }

               for (ls var127 : this.n) {
                  Module var18 = this.Z.get(var127);
                  var127.o = (boolean)(var18 == this.b ? 1 : 0);
                  var127.W = var18.isEnabled();
                  var127.T = this.I.isBlank() ? var18.getDescription() : yc.w(var18).t + " · " + var18.getDescription();
                  if (var127.g + var127.w > this.U.bodyTop() && var127.g < this.U.bottom()) {
                     int var19 = var3 >= this.U.bodyTop() && var3 < this.U.bottom() && var2 >= this.U.contentX() && var2 < this.T() ? var2 : -1;
                     var127.render(var1, var19, var3);
                     this.M.get(var127).render(var1, var19, var3);
                  }
               }

               if (this.n.isEmpty() && !this.J()) {
                  String var123 = this.e == yc.e && this.I.isBlank() ? "Star a module to add it here." : "No matches";
                  uw.G(var1, uw.J(var123, this.T() - this.U.contentX() - 16), this.U.moduleListLeft(this.L) + 8, this.U.bodyTop() + 12, ez.PH);
               }
            } finally {
               var1.method_44380();
            }

            if (!this.J()) {
               com.larpclient.p.t(
                  var1, this.U.moduleScrollbarX(this.i), this.U.bodyTop(), Math.max(1, this.U.bottom() - this.U.bodyTop()), this.N, (int)Math.round(this.v.u())
               );
            }

            if (this.b != null) {
               bu var118 = this.U.moduleInspector(this.i);
               int var124 = this.U.columns() ? var118.inspectorX() : var118.contentX();
               double var128 = this.d();
               if (this.U.detachedInspector()) {
                  try (fp var133 = fp.O(var128)) {
                     com.larpclient.p.W(var1, var124, var118.top(), var118.right() - var124, var118.height());
                  }
               }

               var1.method_44379(this.U.moduleInspectorClipLeft(this.i), this.z(), var118.right(), this.E());

               try (fp var134 = fp.O(var128)) {
                  if (!this.U.detachedInspector()) {
                     com.larpclient.p.F(var1, var124, this.U.bodyTop(), var118.right() - var124, this.U.bottom() - this.U.bodyTop());
                  }

                  this.T.method_25394(var1, var2, var3, var4);
                  this.z.render(var1, var2, var3);
               } finally {
                  var1.method_44380();
               }

               var1.method_44379(this.U.moduleInspectorClipLeft(this.i), var118.top() + 12, var118.right(), this.E());

               try (fp var135 = fp.O(var128)) {
                  this.T.k(var1, var2, var3);
               } finally {
                  var1.method_44380();
               }
            }

            for (ls var125 : this.n) {
               li var129 = this.M.get(var125);
               boolean var131 = this.accepts(var129, var2, var3);
               if ((this.z() == null || !this.z().N())
                  && var129.I
                  && (var131 || var129.M && var129.q)
                  && var129.g >= this.U.bodyTop()
                  && var129.g + var129.w <= this.U.bottom()) {
                  var129.n(var1, this.w.width(), this.w.height());
                  break;
               }
            }
         }

         t6 var120 = this.z();
         String var126 = x(var120, var120 == null ? "" : var120.S(var2, var3));
         String var130 = LarpClient.b().e().n();
         if (!var130.isBlank()) {
            var126 = var130;
         }

         if (!this.t.isBlank()) {
            var126 = this.t;
         }

         if (var126.isBlank() && (this.q.contains(var2, var3) || this.q.M && this.q.q)) {
            var126 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var126.isBlank() && (this.k.contains(var2, var3) || this.k.M && this.k.q)) {
            var126 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var126.isBlank()) {
            var126 = "Enter opens settings · Space toggles · Tab navigates · Right click resets a setting";
         }

         int var132 = this.U.profileLeft();
         String var136 = "Profile: " + LarpClient.b().e().d();
         uw.X(var1, uw.X(var136, this.U.profileWidth(), uw.c.M), var132, this.U.profileY(), ez.PH, uw.c.M);
         uw.X(var1, uw.X(var126, this.U.helpWidth(), uw.c.M), this.U.helpLeft(), this.U.helpY(), this.t.isBlank() ? ez.PH : -24915, uw.c.M);
         double var121 = sd.v(var9.K, var7);
         if (var121 > 0.01) {
            ws.r(var1, this.U.left(), this.U.top(), this.U.width(), this.U.height(), ez.j, ei.D(-1, var121));
         }

         if (var10) {
            WorldMenuModule.P(var1, this, this.U, var2, var3);
         }
      } finally {
         var1.method_51448().popMatrix();
      }

      c5.w("ClickGUI", var5);
   }

   public double[] h() {
      float var1 = this.w.scale();
      return new double[]{this.U.left() * var1, this.U.top() * var1, (this.U.left() + this.U.width()) * var1, (this.U.top() + this.U.height()) * var1};
   }

   public class_437 n() {
      return this.O;
   }

   public boolean C() {
      return this.D && this.d.u() <= 0.0;
   }

   public boolean V(double var1, double var3) {
      Vector2f var5 = this.u(var1, var3);
      if (var5.x >= this.U.left() && var5.x < this.U.left() + this.U.width() && var5.y >= this.U.top() && var5.y < this.U.top() + this.U.height()) {
         return true;
      }

      if (this.b != null && this.U.detachedInspector() && this.d() > 0.2) {
         bu var6 = this.U.moduleInspector(this.i);
         if (var5.x >= var6.inspectorX() && var5.x < var6.right() && var5.y >= var6.top() && var5.y < var6.top() + var6.height()) {
            return true;
         }
      }

      return WorldMenuModule.C(this.U, var5.x, var5.y);
   }

   public boolean R() {
      t6 var1 = this.z();
      return Z(this.G) || var1 != null && Z(var1.c());
   }

   static boolean Z(lc var0) {
      return var0 instanceof lo var2 && var2.M || var0 instanceof l9 var1 && var1.P();
   }

   public void Q() {
      t6 var1 = this.z();
      lc var2 = var1 == null ? null : var1.c();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.G != null && this.G != var2) {
         this.G.key(256);
      }

      this.z();
   }

   public void z() {
      t6 var1 = this.z();
      if (var1 != null) {
         var1.y();
      }

      if (this.G != null) {
         this.G.blur();
      }

      this.G = this.y = null;
   }

   private static void i(class_332 var0, int var1, int var2, double var3, y0 var5) {
      int var6 = ei.D(-16777216, var5.X);
      switch (var5.L) {
         case "None":
            break;
         case "Gradient":
            int var11 = ei.R(var6, var3);
            int var10 = ei.R(ei.W(var6, ei.D(ez.a, 0.5), 0.35), var3);
            gu.R(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            tg.c var9 = tg.c.valueOf(var5.L.toUpperCase(Locale.ROOT));
            if (!tg.E(var0, var1, var2, var3, ez.a, ez.f, var5.Gr, var5.k, var9, var5.X)) {
               gu.Z(var0, 0, 0, var1, var2, ei.R(var6, var3));
            }
            break;
         default:
            gu.Z(var0, 0, 0, var1, var2, ei.R(var6, var3));
      }
   }

   static String x(t6 var0, String var1) {
      return var0 instanceof tf var2 && !var2.JK.isBlank() ? var2.JK : var1;
   }

   private void k(class_332 var1, int var2, int var3) {
      int var4 = this.U.contentX() - this.U.left() - 10;
      var1.method_44379(this.U.left(), this.U.top(), this.U.contentX() - 10, this.U.navigationTop());

      try {
         var1.method_25293(class_10799.field_56883, gc.z(), this.U.left() + 7, this.U.top() + 10, 0.0F, 0.0F, 22, 22, 64, 64, 64, 64, fp.R(-1));

         try (fp var5 = fp.O(1.0)) {
            int var6 = this.U.left() + 35;
            int var7 = var4 - 39;
            uw.X(var1, uw.X("Larp Client", var7, uw.c.Y), var6, this.U.top() + 10, ez.H, uw.c.Y);
            String var8 = "Version ";
            int var9 = var6 + uw.C(var8, uw.c.M);
            uw.X(var1, var8, var6, this.U.top() + 24, ez.t, uw.c.M);
            uw.X(var1, uw.X(LarpClient.z, Math.max(1, var6 + var7 - var9), uw.c.M), var9, this.U.top() + 24, ez.X(var9, ez.d), uw.c.M);
            f(var1, "FEATURES", this.U.left() + 12, this.U.navigationTop() - 12, this.U.navigationRight());
         }
      } finally {
         var1.method_44380();
      }

      var1.method_44379(this.U.left(), this.U.navigationTop(), this.U.contentX() - 10, this.U.navigationBottom());

      try {
         try (fp var49 = fp.O(1.0)) {
            f(var1, "OTHERS", this.U.left() + 12, this.u.get(this.E).g - 12, this.U.navigationRight());
         }

         this.E(var1);

         for (ls var52 : this.u) {
            var52.render(var1, var3 >= this.U.navigationTop() && var3 < this.U.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.method_44380();
      }

      if (this.U.navigationScrollLimit(this.u.size()) > 0) {
         com.larpclient.p.t(
            var1,
            this.U.contentX() - 12,
            this.U.navigationTop(),
            this.U.navigationBottom() - this.U.navigationTop(),
            this.u.size() * this.U.navigationPitch(this.u.size()) + this.U.navigationGap(),
            this.S
         );
      }

      var1.method_44379(this.U.left(), this.U.accountY(), this.U.contentX() - 10, this.U.accountY() + this.B.w);

      try {
         double var51 = this.B.Q(var2, var3);
         if (var51 > 0.001) {
            gu.N(var1, this.B.D, this.B.g, this.B.d, this.B.w, ez.V, ei.D(ez.H, var51 * 0.045));
         }

         bh var53 = bh.current();
         if (!f.G(var1, this.U.left() + 8, this.B.g + 5, 20)) {
            this.x.K(var1, this.U.left() + 8, this.B.g + 5, 20);
         }

         gu.Q(var1, this.U.left() + 25, this.B.g + 24, 4, ez.d);
         gu.Q(var1, this.U.left() + 25, this.B.g + 24, 3, var53.state().Y());

         try (fp var54 = fp.O(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.field_22787.method_1548().method_1676();
            uw.N(var1, uw.H(var10, var55, uw.r.V), this.U.left() + 34, this.B.g + 6, ez.H, uw.r.V);
            uw.X(var1, uw.X(var53.state().V(), var55, uw.c.M), this.U.left() + 34, this.B.g + 18, ez.PH, uw.c.M);
         }

         if (this.B.M && this.B.q) {
            com.larpclient.p.C(var1, this.B.D, this.B.g, this.B.d, this.B.w, ez.V);
         }
      } finally {
         var1.method_44380();
      }
   }

   private static void f(class_332 var0, String var1, int var2, int var3, int var4) {
      uw.X(var0, var1, var2, var3, ei.D(ez.PH, 0.8), uw.c.H);
   }

   private void E(class_332 var1) {
      ls var2 = null;

      for (ls var4 : this.u) {
         if (var4.o) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.P.a(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.g - this.U.navigationTop() + this.S;
         if (!this.C) {
            this.X.w(var5);
            this.C = (boolean)1;
         }

         this.X.a(var5, 200);
      }

      if (this.C && !(var9 < 0.01)) {
         ls var10 = this.u.getFirst();
         float var6 = this.U.navigationTop() - this.S + (float)this.X.u();
         int var7 = ez.X(var10.D, ez.d);
         int var8 = ez.V;
         com.larpclient.p.a(var1, var10.D, var6, var10.d, var10.w, var8, 9.0F, ei.D(var7, 0.22 * var9));
         ws.u(var1, var10.D, var6, var10.d, var10.w, var8, ei.D(var7, 0.26 * var9), ei.D(ez.f, 0.08 * var9));
         ws.A(var1, var10.D, var6, var10.d, var10.w, var8, ei.D(var7, 0.35 * var9), ei.D(var7, 0.35 * var9));
      }
   }

   @Override
   protected boolean accepts(lc var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.u.contains(var1)) {
         return var4 >= this.U.navigationTop() && var4 < this.U.navigationBottom();
      } else {
         return !this.n.contains(var1) && !this.M.containsValue(var1)
            ? true
            : !this.J() && var4 >= this.U.bodyTop() && var4 < this.U.bottom() && var2 >= this.U.contentX() && var2 < this.T();
      }
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.D) {
         return true;
      }

      this.h = (boolean)1;
      var1 = this.w(var1);
      this.M();
      t6 var3 = this.z();
      lc var4 = var3 == null ? null : var3.c();
      if (var4 != null && var4.captureMouse(s2.m(var1))) {
         return true;
      }

      if (s2.m(var1) > 1 && LarpClient.b().V() == s5.K(s2.m(var1))) {
         this.method_25419();
         return true;
      }

      if (var3 != null && var3.N()) {
         return var3.method_25402(var1, var2);
      }

      if (WorldMenuModule.M(this, var1.comp_4798(), var1.comp_4799(), s2.m(var1), this.U)) {
         if (var3 != null) {
            var3.y();
         }

         return true;
      } else {
         for (ls var6 : this.u) {
            if (this.accepts(var6, var1.comp_4798(), var1.comp_4799())) {
               if (var6.o && this.s.equals("Modules")) {
                  return var6.click(var1.comp_4798(), var1.comp_4799(), s2.m(var1));
               }

               return super.method_25402(var1, var2);
            }
         }

         if (this.d() > 0.2 && this.z.contains(var1.comp_4798(), var1.comp_4799()) && var1.comp_4798() < this.U.moduleInspector(this.i).right()) {
            return super.method_25402(var1, var2);
         }

         int var8 = this.S3 != null
            ? this.U.containsWorkspace(var1.comp_4798(), var1.comp_4799())
            : (
               this.d() > 0.2
                     && (!this.l || this.U.columns())
                     && var1.comp_4798() >= this.U.moduleInspectorClipLeft(this.i)
                     && var1.comp_4798() < this.U.moduleInspector(this.i).right()
                     && var1.comp_4799() >= this.z()
                     && var1.comp_4799() < this.E()
                  ? 1
                  : 0
            );
         if (var3 != null && var8 != 0) {
            if (this.G != null) {
               this.G.blur();
            }

            this.G = this.y = null;
            return this.l && this.S3 == null ? true : var3.method_25402(var1, var2);
         } else {
            if (var3 != null) {
               var3.y();
            }

            return super.method_25402(var1, var2);
         }
      }
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (this.D) {
         return true;
      } else {
         Vector2f var9 = this.u(var1, var3);
         var1 = var9.x;
         var3 = var9.y;
         this.M();
         t6 var10 = this.z();
         if ((var10 == null || !var10.N()) && this.U.containsNavigation(var1, var3) && var3 >= this.U.navigationTop() && var3 < this.U.navigationBottom()) {
            this.S -= (int)(var7 * 28.0);
            this.M();
            return true;
         } else if (var10 != null && !this.l && (!this.U.detachedInspector() || this.d() > 0.2) && var10.method_25401(var1, var3, var5, var7)) {
            return true;
         } else if (this.s.equals("Modules") && !this.J() && var1 >= this.U.contentX() && var1 < this.T() && var3 >= this.U.bodyTop() && var3 < this.U.bottom()
            )
          {
            this.c -= (int)(var7 * 27.0);
            this.M();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(lc var1) {
      if (this.u.contains(var1)) {
         if (var1.g < this.U.navigationTop()) {
            this.S = this.S - (this.U.navigationTop() - var1.g);
         } else if (var1.g + var1.w > this.U.navigationBottom()) {
            this.S = this.S + (var1.g + var1.w - this.U.navigationBottom());
         }

         this.M();
      }

      if (this.n.contains(var1) || this.M.containsValue(var1)) {
         for (Entry var3 : this.M.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (lc)var3.getKey();
               break;
            }
         }

         int var4 = this.r.containsKey(var1) ? this.r.get(var1).g : var1.g;
         if (var4 < this.U.bodyTop()) {
            this.c = this.c - (this.U.bodyTop() - var4);
         } else if (var1.g + var1.w > this.U.bottom()) {
            this.c = this.c + (var1.g + var1.w - this.U.bottom());
         }

         this.v.w(this.c);
         this.M();
      }
   }

   static boolean A(int var0, lc var1, lc var2, Runnable var3) {
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
   public boolean method_25404(class_11908 var1) {
      if (this.D) {
         return true;
      }

      t6 var2 = this.z();
      if (A(s2.i(var1), var2 == null ? null : var2.c(), this.G, this::method_25419)) {
         return true;
      }

      if (this.W != null
         && this.s.equals("Modules")
         && this.G == this.W
         && (var2 == null || !var2.N())
         && s2.i(var1) == 258
         && (var1.comp_4797() & 1) == 0
         && this.W.T()) {
         return true;
      }

      if (s2.i(var1) != 258 || var2 != null && var2.N()) {
         return var2 == null || var2.c() == null && !var2.N() ? super.method_25404(var1) : var2.method_25404(var1);
      }

      ArrayList var3 = new ArrayList();
      this.K.stream().filter(var0 -> var0.I && var0.N).forEach(var3::add);
      if (var2 != null && !this.l && (!this.U.detachedInspector() || this.d() > 0.2)) {
         var3.addAll(var2.e());
      }

      lc var4 = var2 != null && var2.c() != null ? var2.c() : this.G;
      int var5 = (var1.comp_4797() & 1) != 0 ? -1 : 1;
      int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
      if (this.G != null) {
         this.G.blur();
      }

      this.G = null;
      if (var2 != null) {
         var2.y();
      }

      if (!var3.isEmpty()) {
         lc var7 = (lc)var3.get(Math.floorMod(var6 + var5, var3.size()));
         if (this.K.contains(var7)) {
            this.G = var7;
            var7.M = var7.q = (boolean)1;
            this.focusChanged(var7);
         } else {
            var2.H(var7);
         }
      }

      return true;
   }

   @Override
   public boolean method_25400(class_11905 var1) {
      if (this.D) {
         return true;
      }

      t6 var2 = this.z();
      return var2 != null && var2.c() != null ? var2.method_25400(var1) : super.method_25400(var1);
   }

   @Override
   public boolean method_25403(class_11909 var1, double var2, double var4) {
      if (!this.D && !this.l) {
         Vector2f var6 = this.u(var1.comp_4798(), var1.comp_4799());
         Vector2f var7 = this.u(var1.comp_4798() - var2, var1.comp_4799() - var4);
         var1 = this.w(var1);
         var2 = var6.x - var7.x;
         var4 = var6.y - var7.y;
         t6 var8 = this.z();
         return var8 != null && var8.method_25403(var1, var2, var4) || super.method_25403(var1, var2, var4);
      } else {
         return true;
      }
   }

   @Override
   public boolean method_25406(class_11909 var1) {
      this.h = (boolean)0;
      var1 = this.w(var1);
      if (this.z() != null) {
         this.z().method_25406(var1);
      }

      return super.method_25406(var1);
   }

   public void method_25393() {
      this.w();
      if (this.R && System.nanoTime() >= this.a) {
         this.R = (boolean)0;
         this.K();
      }

      if (!this.D && this.z() != null) {
         this.z().method_25393();
      }
   }

   void D() {
      if (this.e != yc.e) {
         hz.k = this.e.name();
      }

      hz.Z = new hz.y(this.s, this.b == null ? "" : this.b.getName(), this.I, this.c, this.T.f);
   }

   public void method_25432() {
      if (this.p != null && !this.D) {
         this.p.w();
      }

      if (this.S3 != null) {
         this.S3.suspend();
      }

      this.T.suspend();
      this.D();
      qk var1 = LarpClient.b().e();
      if (this.S5) {
         this.g();
      }

      var1.U();
      q8.v("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}

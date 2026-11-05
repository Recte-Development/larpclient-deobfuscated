package com.larpclient;

import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.HUDModule;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class pc extends pw {
   static final int s = 8;
   static final int Z = 6;
   static final int f = 20;
   static final int X = 14;
   static final int V = 36;
   static final int h = 46;
   static final int o = 20;
   static final int b = 18;
   static final int O = 90;
   static final int u = 122;
   static final int C = 160;
   private static final int z = -401337324;
   private static final int d = -16119286;
   private final Screen i;
   private final List<pc.d> p = new ArrayList<>();
   private final u7 N = new u7(0.0);
   private final Matrix3x2f J = new Matrix3x2f();
   private ry j;
   private p_ B;
   private pc.j m;
   private pc.j q;
   private pc.m g;
   private String H = "";
   private String c = "";
   private int D;
   private int v;
   private long U;
   private pc.d P;
   private int a;
   private int w;
   private boolean t;
   private boolean Y;
   private boolean T;

   public static Screen o(Screen var0) {
      return C().h ? new pc(var0) : new p0(var0);
   }

   public static boolean s(Screen var0) {
      return var0 instanceof p0 || var0 instanceof pc;
   }

   private static j8 C() {
      return LarpClient.y().v().E().p;
   }

   public pc(Screen var1) {
      super("Larp client");
      this.i = var1;
   }

   static int n(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((var0 - 16 - (var2 - 1) * 6) / var2, 90, 122);
   }

   static int[] Y(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] V(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp(var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp(var1, 0, Math.max(0, var4 - 20))};
   }

   protected void init() {
      this.j = this.v();
      this.J.set(this.j.transform());
      this.t = (boolean)0;
      if (!this.Y) {
         this.Y = (boolean)1;
         this.N.c(0.0);
         hx.w(hx.t.O);
      }

      this.N.y(1.0, this.w());
      if (this.p.isEmpty()) {
         this.p();
      }

      this.E();
      String var1 = this.B == null ? null : this.B.V;
      this.B = new p_("Search modules...", var1x -> {
         this.H = var1x;

         for (pc.d var3 : this.p) {
            var3.u();
            var3.p = 0;
         }
      }, 64);
      this.B.S = "search";
      this.B.V = var1 == null ? this.H : var1;
      this.m = new pc.j(() -> "Settings", this::B, true);
      this.q = new pc.j(() -> "Reset layout", this::V, true);
      this.g = new pc.m(this::f);
      this.S();
      r4.g("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.p.size(), this.j.width(), this.j.height());
   }

   private ry v() {
      return ry.from(this.minecraft.getWindow(), LarpClient.y().v().E().Y);
   }

   private int w() {
      return LarpClient.y().v().E().U;
   }

   private void p() {
      this.p.clear();

      for (lw var4 : lw.values()) {
         if (var4 != lw.l) {
            this.p.add(new pc.d(var4));
         }
      }

      this.p.add(new pc.d(lw.l));
   }

   private void E() {
      ArrayList var1 = new ArrayList<>(this.p);
      var1.sort(Comparator.comparingInt(var0 -> var0.A));
      int var2 = this.v().width();
      int var3 = n(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         pc.d var5 = (pc.d)var1.get(var4);
         j8.o var6 = C().D(var5.k.name());
         int[] var7 = var6 == null ? Y(var4, var3, var2) : new int[]{var6.x(), var6.y()};
         var5.U = var7[0];
         var5.J = var7[1];
         var5.S = (boolean)(var6 != null && !var6.open() ? 0 : 1);
         var5.u();
      }
   }

   private void V() {
      C().S();
      this.E();
      this.T = (boolean)1;
      this.D();
      hx.w(hx.t.X);
      r4.V("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void f() {
      if (C().h) {
         C().h = (boolean)0;
         this.U = System.nanoTime();
         this.T = (boolean)1;
         this.D();
         hx.w(hx.t.F);
         r4.V("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void B() {
      this.F();
      this.minecraft.gui.setScreen(p0.x(this, "Interface"));
   }

   private void M(Module var1) {
      this.F();
      fc.K = lw.k(var1).name();
      fc.J = new fc.t("Modules", var1.getName(), "", 0, 0);
      this.minecraft.gui.setScreen(new p0(this));
   }

   private void D(pc.d var1) {
      try {
         C().r(var1.k.name(), new j8.o(var1.U, var1.J, var1.S));
      } catch (IllegalArgumentException var3) {
         r4.m("GUI", "Legacy panel {} place not saved: {}", var1.k.name(), var3.getMessage());
      }

      this.T = (boolean)1;
   }

   private void D() {
      if (this.T) {
         LarpClient.y().v().a();
         this.T = (boolean)0;
      }
   }

   public void onClose() {
      if (!this.t) {
         this.t = (boolean)1;
         this.F();
         hx.w(hx.t.I);
         this.minecraft.gui.setScreen(this.i);
      }
   }

   public void removed() {
      this.F();
      this.D();
      LarpClient.y().v().A();
      r4.g("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void tick() {
      int var1 = this.U != 0L && System.nanoTime() - this.U < 100000000L ? 1 : 0;
      if (!this.t && var1 == 0 && !C().h) {
         this.t = (boolean)1;
         r4.V("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.minecraft.gui.setScreen(new p0(this.i));
      } else {
         for (pc.d var3 : this.p) {
            for (pc.v var5 : var3.y) {
               var5.n();
            }
         }
      }
   }

   private void F() {
      if (this.W != null) {
         this.W.blur();
      }

      this.W = this.L = null;
   }

   private void S() {
      ry var1 = this.v();
      if (!var1.equals(this.j) && this.P == null && this.L == null) {
         this.j = var1;
      }

      int var2 = n(this.j.width(), this.p.size());
      this.B.bounds(this.j.width() / 2 - 95, 8, 190);
      this.B.O = 20;
      this.m.bounds(this.B.B + this.B.v + 6, 8, 58);
      this.m.O = 20;
      this.q.bounds(this.m.B + this.m.v + 4, 8, 72);
      this.q.O = 20;
      this.c = pe.A(LarpClient.y().b());
      this.v = j_.D(this.c, j_.w.P) + j_.D(" = menu", j_.w.P) + 22;
      int var3 = this.g.m();
      this.D = (this.j.width() - this.v - 6 - var3) / 2;
      this.g.bounds(this.D + this.v + 6, this.F(), var3);
      this.g.O = 18;
      this.k.clear();
      this.k.add(this.B);
      this.k.add(this.m);
      this.k.add(this.q);
      this.k.add(this.g);

      for (pc.d var5 : this.p) {
         int[] var6 = V(var5.U, var5.J, var5.I, this.j.width(), this.j.height());
         if (var5 != this.P) {
            var5.U = var6[0];
            var5.J = var6[1];
         }

         var5.p(var2, this.j.height());
         if (var5.k()) {
            for (pc.v var8 : var5.y) {
               if (var8.V) {
                  for (oq var10 : var8.A) {
                     if (var10.F) {
                        this.k.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f u(double var1, double var3) {
      return new Matrix3x2f(this.J).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.minecraft.level == null) {
         super.extractBackground(var1, var2, var3, var4);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = h_.U();
      this.S();
      fs var7 = LarpClient.y().v().E();
      double var8 = this.N.y(this.t ? 0.0 : 1.0, var7.U);
      this.J.set(this.j.transform()).mul(fm.q(var7.s, var8, this.j.width() / 2.0, this.j.height() / 2.0));
      if (var7.s.equals("Slide")) {
         this.J.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.u(var2, var3);
      var2 = Math.round(var10.x);
      var3 = Math.round(var10.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.J);

      try (r var11 = r.p(var8)) {
         int var12 = C().p;
         this.B.render(var1, var2, var3);
         this.m.render(var1, var2, var3);
         this.q.render(var1, var2, var3);
         pc.d var13 = this.p(var2, var3);
         this.g.render(var1, var13 == null ? var2 : -1, var3);

         for (pc.d var15 : this.p) {
            if (var15.k()) {
               var15.V(var1, var15 == var13 ? var2 : -1, var3, var12);
            }
         }

         if (this.W instanceof p9 var25 && var25.y()) {
            var25.x(var1, var2, var3);
         }

         String var26 = var13 != null ? var13.d(var2, var3) : (this.g.contains(var2, var3) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.m(var1, var26, var12);
      } finally {
         var1.pose().popMatrix();
      }

      h_.n("Legacy ClickGUI", var5);
   }

   private int F() {
      return this.j.height() - 46 + 20;
   }

   private void m(GuiGraphicsExtractor var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag a title to move it";
      var4 = j_.S(var4, this.j.width() - 40);
      int var5 = j_.a(var4);
      int var6 = this.F();
      j_.E(var1, var4, (this.j.width() - var5) / 2, var6 - 13, rv.G0);
      j_.w var7 = j_.w.P;
      int var8 = this.D;
      int var9 = this.v;
      fr.S(var1, var8 - 1, var6 - 1, var9 + 2, 20.0F, 10.0F, var3 | 0xFF000000);
      fr.S(var1, var8, var6, var9, 18.0F, 9.0F, -268106488);
      j_.x(var1, this.c, var8 + 11, var6 + 5, jd.readableAccent(var3, -16448248), var7);
      j_.x(var1, " = menu", var8 + 11 + j_.D(this.c, var7), var6 + 5, -1, var7);
   }

   private pc.d p(double var1, double var3) {
      for (int var5 = this.p.size() - 1; var5 >= 0; var5--) {
         if (this.p.get(var5).k() && this.p.get(var5).O(var1, var3)) {
            return this.p.get(var5);
         }
      }

      return null;
   }

   private void O(pc.d var1) {
      this.p.remove(var1);
      this.p.add(var1);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.t) {
         return true;
      }

      Vector2f var3 = this.u(var1.x(), var1.y());
      double var4 = var3.x;
      double var6 = var3.y;
      int var8 = r7.y(var1);
      this.S();
      if (this.W != null && this.W.captureMouse(var8)) {
         return true;
      }

      if (var8 > 1 && LarpClient.y().b() == pe.h(var8)) {
         this.onClose();
         return true;
      }

      if (this.W instanceof p9 var9 && var9.R(var4, var6)) {
         if (var9.N(var4, var6, var8)) {
            this.L = var9;
         }

         return true;
      } else {
         pc.d var12 = this.p(var4, var6);
         if (var12 != null) {
            this.O(var12);
            return var12.q(var4, var6, var8);
         }

         for (oq var11 : List.of(this.B, this.m, this.q, this.g)) {
            if (var11.contains(var4, var6)) {
               this.y(var11);
               var11.click(var4, var6, var8);
               return true;
            }
         }

         this.F();
         return false;
      }
   }

   private void y(oq var1) {
      if (this.W != null && this.W != var1) {
         this.W.blur();
      }

      this.W = var1;
      var1.A = (boolean)1;
      var1.h = (boolean)0;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.t) {
         return true;
      } else {
         Vector2f var6 = this.u(var1.x(), var1.y());
         if (this.P != null) {
            int[] var7 = V(Math.round(var6.x) - this.a, Math.round(var6.y) - this.w, this.P.I, this.j.width(), this.j.height());
            this.P.U = var7[0];
            this.P.J = var7[1];
            return true;
         } else if (this.L != null) {
            this.L.drag(var6.x, var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.P != null) {
         this.D(this.P);
         this.D();
         r4.g("GUI", "Legacy panel {} moved to {}, {}", this.P.k.C, this.P.U, this.P.J);
         this.P = null;
      }

      if (this.L != null) {
         this.L.release();
      }

      this.L = null;
      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.t) {
         return true;
      } else {
         Vector2f var9 = this.u(var1, var3);
         if (this.W instanceof p9 var10 && var10.R(var9.x, var9.y)) {
            var10.J(var7);
            return true;
         } else {
            pc.d var12 = this.p(var9.x, var9.y);
            if (var12 == null) {
               return false;
            }

            var12.p = var12.p - (int)Math.round(var7 * 20.0);
            this.S();
            return true;
         }
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.t) {
         return true;
      } else {
         return p0.z(r7.g(var1), this.W, null, this::onClose) ? true : super.keyPressed(var1);
      }
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.t) {
         return true;
      } else if (super.charTyped(var1)) {
         return true;
      } else if ((this.W == null || this.W == this.B) && Character.isLetterOrDigit(var1.codepoint())) {
         this.y(this.B);
         return this.B.character(var1.codepoint());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(oq var1) {
      for (pc.d var3 : this.p) {
         for (pc.v var5 : var3.y) {
            if (var5.V && var5.A.contains(var1)) {
               if (var1.L < var3.t()) {
                  var3.p = var3.p - (var3.t() - var1.L);
               } else if (var1.L + var1.O > var3.d) {
                  var3.p = var3.p + (var1.L + var1.O - var3.d);
               }

               this.S();
               return;
            }
         }
      }
   }

   private final class d {
      final lw k;
      final Map<Module, pc.v> Y = new IdentityHashMap<>();
      final List<pc.v> y = new ArrayList<>();
      final u7 M = new u7(-1.0);
      final u7 u = new u7(0.0);
      final u7 L = new u7(0.0);
      final int A;
      int U;
      int J;
      int I = 90;
      int p;
      int d;
      int T;
      boolean S = (boolean)1;

      d(lw var2) {
         this.k = var2;
         this.A = pc.this.p.size();
      }

      void u() {
         this.y.clear();
         List var1 = LarpClient.y().e().X();
         fs var2 = LarpClient.y().v().E();

         for (Module var4 : this.k.f(var1, "", var2)) {
            if (pc.this.H.isBlank() || d3.i(var4, pc.this.H) != Integer.MAX_VALUE) {
               this.y.add(this.Y.computeIfAbsent(var4, var1x -> pc.this.new v(var1x)));
            }
         }
      }

      boolean k() {
         return !this.y.isEmpty();
      }

      int t() {
         return this.J + 20;
      }

      boolean O(double var1, double var3) {
         return fr.Q(var1, var3, this.U, this.J, this.I, 20 + (this.S ? this.d - this.t() : 0));
      }

      void p(int var1, int var2) {
         boolean var3 = this.y.stream().anyMatch(var0 -> var0.V);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.M.V() < 0.0) {
            this.M.c(var4);
         }

         this.I = (int)Math.round(this.M.y(var4, 200));
         this.T = 0;

         for (pc.v var6 : this.y) {
            for (oq var8 : var6.A) {
               var8.F = (boolean)(this.S && var6.V && var6.P(var8) ? 1 : 0);
            }

            this.T = this.T + 14 + (var6.V ? var6.Y() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.t());
         this.d = this.t() + (this.S ? Math.min(this.T, var12) : 0);
         this.p = Math.clamp(this.p, 0, Math.max(0, this.T - (this.d - this.t())));
         int var13 = this.t() - this.p;

         for (pc.v var16 : this.y) {
            var16.d = var13;
            var13 += 14;
            if (var16.V) {
               var13 += 2;

               for (oq var10 : var16.A) {
                  if (var10.F) {
                     var10.bounds(this.U + 4, var13, this.I - 8);
                     if (var10 instanceof p9 var11) {
                        var11.Y(6, pc.this.j.height() - 40);
                     }

                     var13 += var10.O + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void V(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
         int var5 = 20 + this.d - this.t();
         fr.k(var1, this.U - 1, this.J - 1, this.I + 2, var5 + 2, -16119286);
         fr.k(var1, this.U, this.J, this.I, var5, -401337324);
         String var6 = j_.V(this.k.C, (int)((this.I - 10) / 1.2F), j_.w.Q);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.U + this.I / 2.0F, this.J + 5);
            var1.pose().scale(1.2F, 1.2F);
            j_.x(var1, var6, -j_.D(var6, j_.w.Q) / 2, 0, -1, j_.w.Q);
         } finally {
            var1.pose().popMatrix();
         }

         if (this.S) {
            int var7 = var3 >= this.t() && var3 < this.d ? 1 : 0;
            pc.v var8 = null;
            if (var7 != 0) {
               for (pc.v var10 : this.y) {
                  if (fr.Q(var2, var3, this.U, var10.d, this.I, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.t() - this.p;
            if (var8 != null) {
               if (this.L.V() < 0.05) {
                  this.u.c(var8.d - var29);
               }

               this.u.y(var8.d - var29, 160);
            }

            double var30 = this.L.y(var8 == null ? 0.0 : 1.0, 160);
            var1.enableScissor(this.U, this.t(), this.U + this.I, this.d);

            try {
               fs var12 = LarpClient.y().v().E();

               for (pc.v var14 : this.y) {
                  int var15 = var14.d + 14 + (var14.V ? var14.Y() : 0);
                  if (var15 >= this.t() && var14.d <= this.d && var14.m.isEnabled()) {
                     fr.k(var1, this.U, var14.d, this.I, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  fr.k(var1, this.U, var29 + (int)Math.round(this.u.V()), this.I, 14, oh.S(419430399, var30));
               }

               for (pc.v var34 : this.y) {
                  int var36 = var34.d + 14 + (var34.V ? var34.Y() : 0);
                  if (var36 >= this.t() && var34.d <= this.d) {
                     boolean var16 = var34.m.isEnabled();
                     int var17 = var16 ? jd.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = j_.S(var34.m.getDisplayName(), this.I - 14);
                     j_.E(var1, var18, this.U + (this.I - j_.a(var18)) / 2, var34.d + 3, var17);
                     if (var12.s(var34.m.getName()) && this.k != lw.l) {
                        fr.k(var1, this.U + 3, var34.d + 5, 3, 3, var16 ? var17 : oh.i(var4, 0.9));
                     }

                     if (var34.V) {
                        int var19 = var34.Y();
                        fr.k(var1, this.U, var34.d + 14, this.I, var19, 1342177280);
                        fr.k(var1, this.U, var34.d + 14, 1, var19, var4 | 0xFF000000);

                        for (oq var21 : var34.A) {
                           if (var21.F) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.disableScissor();
            }

            int var31 = this.d - this.t();
            if (this.T > var31 && var31 > 0) {
               int var33 = Math.max(12, var31 * var31 / this.T);
               int var35 = var31 - var33;
               int var37 = this.t() + (int)Math.round(var35 * ((double)this.p / (this.T - var31)));
               fr.k(var1, this.U + this.I - 2, var37, 2, var33, oh.i(var4, 0.8));
            }
         }
      }

      String d(int var1, int var2) {
         if (var2 < this.t()) {
            return "Drag to move · Right click folds " + this.k.C;
         }

         for (pc.v var4 : this.y) {
            if (fr.Q(var1, var2, this.U, var4.d, this.I, 14)) {
               String var8 = var4.m.getDescription();
               return var8.isBlank() ? var4.m.getDisplayName() : var8;
            }

            if (var4.V) {
               for (oq var7 : var4.A) {
                  if (var7.F && var7.contains(var1, var2)) {
                     return var7 instanceof p9 var5 ? var5.E() : "";
                  }
               }
            }
         }

         return "";
      }

      boolean q(double var1, double var3, int var5) {
         if (var3 < this.t()) {
            if (var5 == 0) {
               pc.this.F();
               pc.this.P = this;
               pc.this.a = (int)Math.round(var1) - this.U;
               pc.this.w = (int)Math.round(var3) - this.J;
            } else if (var5 == 1) {
               this.S = (boolean)(!this.S ? 1 : 0);
               pc.this.D(this);
               pc.this.D();
               hx.w(this.S ? hx.t.h : hx.t.S);
            }

            return true;
         } else if (this.S && !(var3 >= this.d)) {
            for (pc.v var7 : this.y) {
               if (fr.Q(var1, var3, this.U, var7.d, this.I, 14)) {
                  var7.W(var5);
                  return true;
               }

               if (var7.V) {
                  for (oq var9 : var7.A) {
                     if (var9.F && var9.contains(var1, var3)) {
                        pc.this.y(var9);
                        if (var9.click(var1, var3, var5)) {
                           pc.this.L = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            pc.this.F();
            return true;
         } else {
            return true;
         }
      }
   }

   private static final class j extends oq {
      private final Supplier<String> U;
      private final Runnable w;
      private final boolean K;

      j(Supplier<String> var1, Runnable var2, boolean var3) {
         this.U = var1;
         this.w = var2;
         this.K = var3;
         this.O = 14;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.F) {
            boolean var4 = this.contains(var2, var3);
            int var5 = this.A && this.h ? 1 : 0;
            if (this.K) {
               fr.k(var1, this.B - 1, this.L - 1, this.v + 2, this.O + 2, -16119286);
               fr.k(var1, this.B, this.L, this.v, this.O, var4 ? -266330076 : -401337324);
            } else if (var4) {
               fr.k(var1, this.B, this.L, this.v, this.O, 419430399);
            }

            if (var5 != 0) {
               fr.K(var1, this.B, this.L, this.v, this.O, rv.P);
            }

            String var6 = j_.S(this.U.get(), this.v - 8);
            j_.E(var1, var6, this.B + (this.v - j_.a(var6)) / 2, this.L + (this.O - 8) / 2, !var4 && !this.K ? rv.G0 : -1);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            hx.w(hx.t.X);
            this.w.run();
            return false;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.w.run();
         return true;
      }
   }

   private static final class m extends oq {
      private static final String P = "Legacy";
      private static final int j = 18;
      private static final int D = 10;
      private final Runnable G;
      private final u7 I = new u7(1.0);

      m(Runnable var1) {
         this.G = var1;
         this.O = 18;
      }

      int m() {
         return 11 + j_.D("Legacy", j_.w.P) + 6 + 18 + 8;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.I.y(pc.C().h ? 1.0 : 0.0, 100);
         fr.S(var1, this.B - 1, this.L - 1, this.v + 2, this.O + 2, this.O / 2 + 1, var4 ? -12961216 : -14803422);
         fr.S(var1, this.B, this.L, this.v, this.O, this.O / 2, -268106488);
         if (this.A && this.h) {
            fr.K(var1, this.B, this.L, this.v, this.O, rv.P);
         }

         j_.x(var1, "Legacy", this.B + 11, this.L + 5, var4 ? -1 : rv.G0, j_.w.P);
         int var7 = this.B + this.v - 8 - 18;
         int var8 = this.L + (this.O - 10) / 2;
         int var9 = pc.C().p | 0xFF000000;
         fr.S(var1, var7, var8, 18.0F, 10.0F, 5.0F, oh.j(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         fr.J(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.G.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.G.run();
         return true;
      }
   }

   private static final class o extends oq {
      private final String t;

      o(String var1) {
         this.t = var1;
         this.O = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.F) {
            j_.L(var1, j_.d(this.t.toUpperCase(Locale.ROOT), this.v - 4, j_.m.u), this.B + 2, this.L + 2, rv.r, j_.m.u);
         }
      }
   }

   private final class v {
      final Module m;
      final List<oq> A = new ArrayList<>();
      final Map<oq, BooleanSupplier> l = new IdentityHashMap<>();
      final Set<String> D = new HashSet<>();
      boolean V;
      int d;
      String E;

      v(Module var2) {
         this.m = var2;
      }

      boolean P(oq var1) {
         BooleanSupplier var2 = this.l.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int Y() {
         int var1 = 5;

         for (oq var3 : this.A) {
            if (var3.F) {
               var1 += var3.O + 2;
            }
         }

         return var1;
      }

      void W(int var1) {
         switch (var1) {
            case 0:
               this.m.toggle();
               hx.w(this.m.isEnabled() ? hx.t.N : hx.t.F);
               break;
            case 1:
               this.V = (boolean)(!this.V ? 1 : 0);
               if (this.V) {
                  this.g();
               } else if (this.A.contains(pc.this.W)) {
                  pc.this.F();
               }

               hx.w(this.V ? hx.t.h : hx.t.S);
               break;
            case 2:
               fs var2 = LarpClient.y().v().E();
               var2.V(this.m.getName(), !var2.s(this.m.getName()));
               LarpClient.y().v().a();

               for (pc.d var4 : pc.this.p) {
                  var4.u();
               }

               hx.w(hx.t.X);
         }
      }

      void n() {
         if (this.V && this.m instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.E, var1.w().getValue())) {
            this.g();
         }
      }

      void g() {
         if (this.A.contains(pc.this.W)) {
            pc.this.F();
         }

         this.A.clear();
         this.l.clear();
         this.E = this.m instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 ? var1.w().getValue() : null;
         List var8 = dp.s(this.m);
         Iterator var9 = var8.iterator();

         while (true) {
            dp.x var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.m instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     com.larpclient.module.la var10 = new com.larpclient.module.la(
                        this.m instanceof HUDModule ? "Toggle HUD" : "Bind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        this.m.getKeyBind()
                     );
                     var10.W(this.m::setKeyBind);
                     this.Q(new p9(var10), null);
                     if (this.m.keybindMode() != null) {
                        this.Q(new p9(this.m.keybindMode()), null);
                     }
                  }

                  this.Q(new pc.j(() -> "Reset settings", () -> {
                     for (com.larpclient.module.l6 var2 : this.m.getSettings()) {
                        var2.x();
                     }

                     if (this.m instanceof FakeSpawnerModule var3x) {
                        var3x.g();
                     }

                     this.g();
                     hx.w(hx.t.X);
                  }, false), null);
                  this.Q(new pc.j(() -> "Open full editor", () -> pc.this.M(this.m), false), null);
                  return;
               }

               var3 = (dp.x)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(com.larpclient.module.l6::R);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3.title().toLowerCase(Locale.ROOT);
               pc.j var6 = new pc.j(() -> (this.D.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.D.remove(var3.title())) {
                     this.D.add(var3.title());
                  }

                  this.g();
               }, false);
               this.Q(var6, var4);
            } while (!this.D.contains(var3.title()));

            if (var8.size() > 1) {
               this.Q(new pc.o(var3.title()), var4);
            }

            for (com.larpclient.module.l6 var12 : var3.settings()) {
               if (!(var12 instanceof com.larpclient.module.lc var7 && var7.b())) {
                  p9 var13 = new p9(var12);
                  this.Q(var13, var13::B);
               }
            }
         }
      }

      private void Q(oq var1, BooleanSupplier var2) {
         this.A.add(var1);
         if (var2 != null) {
            this.l.put(var1, var2);
         }
      }
   }
}

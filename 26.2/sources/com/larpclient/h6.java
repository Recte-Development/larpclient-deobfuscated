package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

abstract class h6 extends h3 {
   protected hf O;
   protected uc y;
   protected final List<jd> x = new ArrayList<>();
   protected final List<jx> u = new ArrayList<>();
   protected int Y;
   protected int L;
   protected int R;
   protected int l;
   protected int h;
   protected int g;
   protected boolean J;
   protected String m;
   protected String M = "";
   protected String W = "";
   private final List<jx> E = new ArrayList<>();
   private final List<jx> w = new ArrayList<>();
   private final jx r = new jx("", this::dismissDialog);
   private List<String> s = List.of();
   private List<String> G = List.of();
   private int U;
   private int j;
   private int H;
   private int C;
   private final jp N = new jp(1.0);
   private final jp V = new jp(0.0);
   private final jp S = new jp(0.0);
   private final jp I = new jp(0.0);
   private boolean q;
   private boolean b;
   private boolean k;
   private static final int d = 300;
   private static final int a = 14;
   private static final int z = 20;

   protected final void C() {
      this.N.F(0.0);
      this.N.k(1.0, 200);
   }

   protected double revealAmount() {
      return this.N.k(1.0, 200);
   }

   h6(String var1) {
      super(var1);
      this.m = var1;
      this.r.g = (boolean)1;
      this.r.l = "x";
      this.r.J = 18;
      this.r.i = (boolean)0;
   }

   final void P(hf var1, uc var2) {
      this.O = var1;
      this.y = var2;
      this.init(var1.width, var1.height);
      this.C();
   }

   void x(uc var1) {
      if (this.y == null || this.y.width() != var1.width() || this.y.height() != var1.height()) {
         this.q = (boolean)0;
      }

      this.y = var1;
      this.layout();
   }

   protected abstract void build();

   protected void init() {
      this.build();
   }

   protected void v() {
      this.u();
      this.x.clear();
      this.u.clear();
      this.E.clear();
      this.w.clear();
      this.M = this.W = "";
      this.h(this.r);
   }

   protected void dismissDialog() {
   }

   protected jx N(String var1, Runnable var2) {
      jx var3 = new jx(var1, var2);
      var3.G = (boolean)1;
      var3.J = 20;
      this.E.add(var3);
      this.h(var3);
      return var3;
   }

   protected jx option(String var1, String var2, Runnable var3) {
      jx var4 = this.a(var1, var3);
      var4.g = (boolean)1;
      var4.x = (boolean)0;
      var4.X = var2;
      var4.J = 30;
      this.w.add(var4);
      return var4;
   }

   protected void R() {
      this.h(new h6.j());
   }

   private boolean x(jd var1) {
      return var1 == this.r || var1 instanceof jx var2 && this.E.contains(var2);
   }

   protected void h(jd var1) {
      this.x.add(var1);
      this.p.add(var1);
   }

   protected void text(String var1) {
      this.h(new h6.c(var1));
   }

   protected void Y(Supplier<String> var1) {
      this.h(new h6.c(var1));
   }

   protected void F(Supplier<String> var1, IntSupplier var2) {
      this.h(new h6.c(var1, var2));
   }

   protected void T(String var1, Supplier<String> var2) {
      this.h(new h6.c(var1, var2));
   }

   protected jx a(String var1, Runnable var2) {
      jx var3 = new jx(var1, var2);
      var3.x = (boolean)(!var1.equals("Load") && !var1.startsWith("Save") && !var1.equals("Fetch preview") ? 0 : 1);
      var3.a = var1.startsWith("Delete");
      this.h(var3);
      return var3;
   }

   protected void O(String var1, boolean var2, Runnable var3) {
      jx var4 = new jx(var1, var3);
      var4.g = (boolean)1;
      var4.R = (boolean)1;
      var4.z = var2;
      this.u.add(var4);
      this.p.add(var4);
   }

   protected int bodyTop() {
      return this.y.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.y.contentX();
      int var2 = Math.max(1, (this.y.right() - var1) / Math.max(1, this.u.size()));

      for (jx var4 : this.u) {
         int var5 = Math.min(var2, Math.max(62, bj.u(var4.K) + 20));
         var4.bounds(var1, this.y.top() + 12, var5 - 4);
         var4.J = 24;
         var1 += var5;
      }

      this.r.i = this.J;
      if (this.J) {
         this.D();
      } else {
         this.L = this.y.contentX();
         this.l = this.y.right();
         this.R = this.bodyTop();
         this.h = this.y.bottom();
         this.N();
      }
   }

   private void D() {
      int var1 = this.y.bodyTop() + 8;
      int var2 = Math.max(1, this.y.bottom() - 8 - var1);
      int var3 = this.y.right() - this.y.contentX();
      this.H = Math.max(1, Math.min(300, var3 - 16));
      this.U = this.y.contentX() + (var3 - this.H) / 2;
      this.L = this.U + 14 - 8;
      this.l = this.U + this.H - 14 + 12;
      int var4 = Math.max(1, this.H - 28 - 22);
      this.s = this.M.isEmpty() ? List.of() : k(this.M, var4, var0 -> bj.e(var0, bj.u.N));
      this.G = this.W.isEmpty() ? List.of() : k(this.W, var4, var0 -> bj.e(var0, bj.u.M));
      int var5 = 14 + Math.max(14, this.s.size() * (bj.u.N.n() + 3) + (this.G.isEmpty() ? 0 : 1 + this.G.size() * (bj.u.M.n() + 3))) + 8;
      this.P();
      int var6 = Math.max(0, this.g - 4);
      int var7 = this.E.isEmpty() ? 10 : 46;
      this.C = Math.min(var2, var5 + var6 + var7);
      this.j = var1 + (var2 - this.C) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.R = this.j + var5;
      this.h = Math.max(this.R, this.j + this.C - var7);
      this.j(0);
      this.r.bounds(this.U + this.H - 14 - 18 + 4, this.j + 14 - 4, 18);
      int var8 = this.j + this.C - 14 - 20;
      List var9 = this.E.stream().map(var0 -> Math.max(64, bj.e(var0.K, bj.u.M) + 24)).toList();
      int var10 = Math.max(0, this.E.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.H - 28 - var10) / Math.max(1, var11));
      int var14 = this.U + this.H - 14;

      for (int var15 = this.E.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)(((Integer)var9.get(var15)).intValue() * var12));
         var14 -= var16;
         this.E.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void N() {
      if (!this.layoutCustomContent()) {
         this.P();
         this.j((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void P() {
      this.g = 0;

      for (jd var2 : this.x) {
         if (!this.x(var2)) {
            var2.i = this.componentShown(var2);
            if (!var2.i) {
               var2.blur();
            } else {
               var2.bounds(this.L + 8, 0, Math.max(1, this.l - this.L - 20));
               if (this.J && var2 instanceof jx var3 && this.w.contains(var3)) {
                  var2.S -= 8;
                  var2.A += 16;
               }

               if (var2 instanceof h6.c var11) {
                  if (this.J) {
                     var11.S -= 4;
                  }

                  var11.e();
               }
            }
         }
      }

      int var9 = 0;

      while (var9 < this.x.size()) {
         jd var10 = this.x.get(var9);
         if (!var10.i || this.x(var10)) {
            var9++;
         } else if (var10 instanceof ju var12 && var12.u()) {
            ArrayList var13 = new ArrayList();

            while (var9 < this.x.size()) {
               if (!(this.x.get(var9) instanceof ju var15) || !var15.u()) {
                  break;
               }

               if (var15.i) {
                  var13.add(var15);
               }

               var9++;
            }

            p9.c var16 = p9.P(Math.max(1, this.l - this.L - 20), var13.stream().map(ju::j).toList());

            for (int var19 = 0; var19 < var13.size(); var19++) {
               p9.g var20 = var16.cells().get(var19);
               ((ju)var13.get(var19)).bounds(this.L + 8 + var20.x(), this.g + var20.y(), var20.width());
            }

            this.g = this.g + var16.height() + 4;
         } else if (var10 instanceof jx var4 && var4.r != null) {
            ArrayList var5 = new ArrayList();

            while (var9 < this.x.size()) {
               if (!(this.x.get(var9) instanceof jx var6) || var6.r != var4.r) {
                  break;
               }

               if (var6.i) {
                  var5.add(var6);
               }

               var9++;
            }

            p9.c var14 = p9.I(Math.max(1, this.l - this.L - 20), var5.stream().map(var0 -> bj.e(var0.K, bj.u.X) + 18).toList());

            for (int var17 = 0; var17 < var5.size(); var17++) {
               p9.g var8 = var14.cells().get(var17);
               ((jx)var5.get(var17)).bounds(this.L + 8 + var8.x(), this.g + var8.y(), var8.width());
               ((jx)var5.get(var17)).J = 24;
            }

            this.g = this.g + var14.height() + 4;
         } else {
            var10.u = this.g;
            this.g = this.g + var10.J + 4;
            var9++;
         }
      }
   }

   private void j(int var1) {
      int var2 = this.g - (this.J ? 4 : 0) - (this.h - this.R);
      this.Y = Math.clamp(this.Y, 0, Math.max(0, var2));
      if (!this.q) {
         this.V.F(this.Y);
         this.q = (boolean)1;
      }

      int var3 = (int)Math.round(this.V.k(this.Y, 150));
      int var4 = this.R - var3 + var1;

      for (jd var6 : this.x) {
         if (var6.i && !this.x(var6)) {
            var6.u += var4;
            if (var6 instanceof ju var7) {
               var7.q(this.y.top() + 12, this.h);
            }
         }
      }
   }

   protected boolean componentShown(jd var1) {
      return var1 instanceof ju var3 ? var3.d() : !(var1 instanceof j1 var2 && !var2.t());
   }

   protected boolean layoutCustomContent() {
      return false;
   }

   protected boolean contentShown() {
      return true;
   }

   protected boolean showTitle() {
      return true;
   }

   protected int titleX() {
      return this.y.contentX() + 8;
   }

   protected int titleY() {
      return this.y.bodyTop() + 8;
   }

   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.horizontalInput(var2)) {
         var2 = -1;
      }

      if (this.J != this.k) {
         this.C();
         this.k = this.J;
      }

      this.layout();

      for (jx var6 : this.u) {
         if (var6.z) {
            if (!this.b) {
               this.S.F(var6.S);
               this.I.F(var6.A);
               this.b = (boolean)1;
            }

            int var7 = (int)Math.round(this.S.k(var6.S, 200));
            int var8 = (int)Math.round(this.I.k(var6.A, 200));
            int var9 = ho.S() ? ho.S(var7, ho.A) : uh.H(ho.u, 0.75);
            wp.e(var1, var7 + 7, var6.u + var6.J, Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, uh.H(var9, 0.45));
            wp.I(var1, var7 + 7, var6.u + var6.J, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (jx var24 : this.u) {
         var24.render(var1, this.J ? -1 : var2, var3);
      }

      if (this.showTitle()) {
         bj.k(var1, bj.i(this.m, Math.max(1, this.l - this.titleX() - 32), bj.u.N), this.titleX(), this.titleY(), ho.u, bj.u.N);
      }

      this.renderExtra(var1, this.J ? -1 : var2, var3);
      if (this.J) {
         this.W(var1, var2, var3);
      }

      var1.enableScissor(this.L, this.R, this.l, this.h);

      try (ua var22 = ua.K(this.revealAmount())) {
         for (jd var27 : this.x) {
            if (this.contentShown() && var27.i && !this.x(var27) && var27.u + var27.J > this.R && var27.u < this.h) {
               var27.render(var1, var3 >= this.R && var3 < this.h ? var2 : -1, var3);
            }
         }
      } finally {
         var1.disableScissor();
      }

      int var23 = this.h - this.R;
      if (this.contentShown()) {
         wp.p(var1, this.l - 3, this.R, Math.max(1, var23), this.g - (this.J ? 4 : 0), (int)Math.round(this.V.B()));
      }

      if (this.J) {
         try (ua var26 = ua.K(this.revealAmount())) {
            if (this.g - 4 > var23) {
               int var28 = uh.F(ho.Y, 1.2);
               if (this.Y > 0) {
                  wp.I(var1, this.U + 14, this.R - 1, this.H - 28, 1, 0, var28);
               }

               wp.I(var1, this.U + 14, this.h, this.H - 28, 1, 0, var28);
            }

            this.r.render(var1, var2, var3);

            for (jx var30 : this.E) {
               var30.render(var1, var2, var3);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.M(var1, var2, var3);
      }
   }

   private void W(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.revealAmount();
      wp.I(var1, this.y.contentX(), this.y.bodyTop(), this.y.right() - this.y.contentX(), this.y.bottom() - this.y.bodyTop(), ho.k, uh.F(-1728053248, var4));
      tb.Q(var1, this.U, this.j + 3, this.H, this.C, ho.k, 16.0F, uh.F(805306368, var4), uh.F(1342177280, var4));
      wp.S(var1, this.U, this.j, this.H, this.C, ho.k, uh.F(ho.dw, var4), 0.0);

      try (ua var6 = ua.K(var4)) {
         int var7 = this.j + 14;

         for (String var9 : this.s) {
            bj.k(var1, var9, this.U + 14, var7, ho.u, bj.u.N);
            var7 += bj.u.N.n() + 3;
         }

         var7++;

         for (String var14 : this.G) {
            bj.k(var1, var14, this.U + 14, var7, ho.Z, bj.u.M);
            var7 += bj.u.M.n() + 3;
         }
      }
   }

   private boolean W(double var1, double var3) {
      return this.J
         && !kv.f(var1, var3, this.U, this.j, this.H, this.C)
         && var1 >= this.y.contentX()
         && var1 < this.y.right()
         && var3 >= this.y.top()
         && var3 < this.y.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void M(GuiGraphicsExtractor var1, int var2, int var3) {
      ju var4 = this.A();
      if (var4 != null) {
         var4.J(var1, this.horizontalInput(var2) ? var2 : -1, var3);
      }
   }

   private ju A() {
      for (jd var2 : this.x) {
         if (var2 instanceof ju var3 && var3.m()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(jd var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.J && !this.x.contains(var1)) {
         return false;
      } else {
         return this.x(var1) ? this.J : !this.x.contains(var1) || this.contentShown() && var4 >= this.R && var4 < this.h && var2 >= this.L && var2 < this.l;
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.layout();
      ju var3 = this.A();
      if (var3 == null) {
         if (k2.w(var1) == 0 && this.W(var1.x(), var1.y()) && this.horizontalInput(var1.x()) && this.x.stream().noneMatch(var0 -> var0 instanceof j9)) {
            this.dismissDialog();
            return true;
         } else {
            return super.mouseClicked(var1, var2);
         }
      } else {
         if (this.horizontalInput(var1.x()) && var3.y(var1.x(), var1.y())) {
            var3.X(var1.x(), var1.y(), k2.w(var1));
            this.F = var3;
         } else {
            var3.d();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      ju var9 = this.A();
      if (var9 != null) {
         if (this.horizontalInput(var1) && var9.y(var1, var3)) {
            var9.N(var7);
         }

         return true;
      } else if (this.horizontalInput(var1) && this.contentShown() && !(var1 < this.L) && !(var1 >= this.l) && !(var3 < this.R) && !(var3 >= this.h)) {
         this.Y -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(jd var1) {
      if (this.x.contains(var1)) {
         if (var1.u < this.R) {
            this.Y = this.Y - (this.R - var1.u);
         } else if (var1.u + var1.J > this.h) {
            this.Y = this.Y + (var1.u + var1.J - this.h);
         }

         this.V.F(this.Y);
         this.layout();
      }
   }

   protected void U(jd var1) {
      this.o = var1;
      var1.T = var1.W = (boolean)1;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<jd> n() {
      return this.p.stream().filter(var1 -> var1.i && var1.p && (!this.x.contains(var1) || this.contentShown()) && (!this.J || this.x.contains(var1))).toList();
   }

   jd q() {
      return this.o;
   }

   void a() {
      if (this.o != null) {
         this.o.blur();
      }

      this.o = this.F = null;
   }

   void q(jd var1) {
      this.U(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.u();
   }

   String A(int var1, int var2) {
      if (this.J) {
         return "";
      } else if (this.o instanceof ju var3 && this.o.W) {
         return var3.G();
      } else if (this.horizontalInput(var1) && var2 >= this.R && var2 < this.h) {
         for (jd var7 : this.x) {
            if (var7 instanceof ju var5 && var5.contains(var1, var2)) {
               return var5.G();
            }
         }

         return "";
      } else {
         return "";
      }
   }

   boolean q() {
      return this.J || this.A() != null;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      ju var2 = this.A();
      if (k2.a(var1) == 256 && var2 != null) {
         var2.d();
         return true;
      }

      if (k2.a(var1) == 258) {
         List var3 = this.n();
         int var4 = (var1.modifiers() & 1) != 0 ? -1 : 1;
         int var5 = this.o == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.o);
         this.a();
         if (!var3.isEmpty()) {
            this.q((jd)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      this.O.onClose();
   }

   static List<String> k(String var0, int var1, ToIntFunction<String> var2) {
      ArrayList var3 = new ArrayList();

      for (String var7 : var0.split("\n", -1)) {
         String var8 = var7;

         while (!var8.isEmpty() && var2.applyAsInt(var8) > var1) {
            int var9 = var8.length();

            while (var9 > Character.charCount(var8.codePointAt(0)) && var2.applyAsInt(var8.substring(0, var9)) > var1) {
               var9 = var8.offsetByCodePoints(var9, -1);
            }

            int var10 = var8.lastIndexOf(32, var9 - 1);
            if (var10 > 0) {
               var9 = var10 + 1;
            }

            var3.add(var8.substring(0, var9));
            var8 = var8.substring(var9);
         }

         var3.add(var8);
      }

      return var3;
   }

   private static final class c extends jd {
      private final Supplier<String> o;
      private List<String> b = List.of();
      private int R = -1;
      private String B;
      private final String g;
      private final IntSupplier d;

      c(String var1) {
         this(() -> var1);
      }

      c(Supplier<String> var1) {
         this(var1, () -> ho.Z);
      }

      c(Supplier<String> var1, IntSupplier var2) {
         this(null, var1, var2);
      }

      c(String var1, Supplier<String> var2) {
         this(var1, var2, () -> ho.Z);
      }

      private c(String var1, Supplier<String> var2, IntSupplier var3) {
         this.g = var1;
         this.o = var2;
         this.d = var3;
         this.p = (boolean)0;
      }

      void e() {
         if (this.g == null) {
            String var1 = this.o.get();
            if (this.R != this.A || !var1.equals(this.B)) {
               this.R = this.A;
               this.B = var1;
               this.b = h6.k(var1, this.A - 8, bj::u);
               this.J = Math.max(22, this.b.size() * 12 + 6);
            }
         }
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.g != null) {
            String var6 = bj.T(this.o.get(), Math.max(1, this.A * 2 / 3));
            int var5 = bj.u(var6);
            bj.E(var1, bj.T(this.g, Math.max(1, this.A - var5 - 20)), this.S + 4, this.u + 7, ho.u);
            bj.E(var1, var6, this.S + this.A - 4 - var5, this.u + 7, this.d.getAsInt());
         } else {
            for (int var4 = 0; var4 < this.b.size(); var4++) {
               bj.E(var1, this.b.get(var4), this.S + 4, this.u + 4 + var4 * 12, this.d.getAsInt());
            }
         }
      }
   }

   private static final class j extends jd {
      j() {
         this.p = (boolean)0;
         this.J = 5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         wp.I(var1, this.S, this.u + 2, Math.max(1, this.A), 1, 0, uh.F(ho.Y, 1.2));
      }
   }
}

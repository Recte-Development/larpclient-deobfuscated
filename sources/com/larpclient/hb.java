package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

abstract class hb extends h8 {
   protected hw M;
   protected mp j;
   protected final List<b> K = new ArrayList<>();
   protected final List<u7> p = new ArrayList<>();
   protected int Z;
   protected int V;
   protected int y;
   protected int X;
   protected int L;
   protected int r;
   protected boolean W;
   protected String F;
   protected String Q = "";
   protected String E = "";
   private final List<u7> i = new ArrayList<>();
   private final List<u7> z = new ArrayList<>();
   private final u7 e = new u7("", this::dismissDialog);
   private List<String> a = List.of();
   private List<String> g = List.of();
   private int c;
   private int s;
   private int v;
   private int b;
   private final ef u = new ef(1.0);
   private final ef R = new ef(0.0);
   private final ef I = new ef(0.0);
   private final ef C = new ef(0.0);
   private boolean T;
   private boolean d;
   private boolean Y;
   private static final int m = 300;
   private static final int N = 14;
   private static final int t = 20;

   protected final void P() {
      this.u.Q(0.0);
      this.u.m(1.0, 200);
   }

   protected double revealAmount() {
      return this.u.m(1.0, 200);
   }

   hb(String var1) {
      super(var1);
      this.F = var1;
      this.e.r = (boolean)1;
      this.e.j = "x";
      this.e.W = 18;
      this.e.D = (boolean)0;
   }

   final void W(hw var1, mp var2) {
      this.M = var1;
      this.j = var2;
      this.init(var1.width, var1.height);
      this.P();
   }

   void w(mp var1) {
      if (this.j == null || this.j.width() != var1.width() || this.j.height() != var1.height()) {
         this.T = (boolean)0;
      }

      this.j = var1;
      this.layout();
   }

   protected abstract void build();

   protected void init() {
      this.build();
   }

   protected void d() {
      this.z();
      this.K.clear();
      this.p.clear();
      this.i.clear();
      this.z.clear();
      this.Q = this.E = "";
      this.w(this.e);
   }

   protected void dismissDialog() {
   }

   protected u7 x(String var1, Runnable var2) {
      u7 var3 = new u7(var1, var2);
      var3.I = (boolean)1;
      var3.W = 20;
      this.i.add(var3);
      this.w(var3);
      return var3;
   }

   protected u7 option(String var1, String var2, Runnable var3) {
      u7 var4 = this.U(var1, var3);
      var4.r = (boolean)1;
      var4.w = (boolean)0;
      var4.u = var2;
      var4.W = 30;
      this.z.add(var4);
      return var4;
   }

   protected void r() {
      this.w(new hb.x());
   }

   private boolean e(b var1) {
      return var1 == this.e || var1 instanceof u7 var2 && this.i.contains(var2);
   }

   protected void w(b var1) {
      this.K.add(var1);
      this.G.add(var1);
   }

   protected void text(String var1) {
      this.w(new hb.m(var1));
   }

   protected void Y(Supplier<String> var1) {
      this.w(new hb.m(var1));
   }

   protected u7 U(String var1, Runnable var2) {
      u7 var3 = new u7(var1, var2);
      var3.w = (boolean)(!var1.equals("Load") && !var1.startsWith("Save") && !var1.equals("Fetch preview") ? 0 : 1);
      var3.k = var1.startsWith("Delete");
      this.w(var3);
      return var3;
   }

   protected void f(String var1, boolean var2, Runnable var3) {
      u7 var4 = new u7(var1, var3);
      var4.r = (boolean)1;
      var4.O = (boolean)1;
      var4.U = var2;
      this.p.add(var4);
      this.G.add(var4);
   }

   protected int bodyTop() {
      return this.j.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.j.contentX();
      int var2 = Math.max(1, (this.j.right() - var1) / Math.max(1, this.p.size()));

      for (u7 var4 : this.p) {
         int var5 = Math.min(var2, Math.max(62, k6.P(var4.S) + 20));
         var4.bounds(var1, this.j.top() + 12, var5 - 4);
         var4.W = 24;
         var1 += var5;
      }

      this.e.D = this.W;
      if (this.W) {
         this.X();
      } else {
         this.V = this.j.contentX();
         this.X = this.j.right();
         this.y = this.bodyTop();
         this.L = this.j.bottom();
         this.a();
      }
   }

   private void X() {
      int var1 = this.j.bodyTop() + 8;
      int var2 = Math.max(1, this.j.bottom() - 8 - var1);
      int var3 = this.j.right() - this.j.contentX();
      this.v = Math.max(1, Math.min(300, var3 - 16));
      this.c = this.j.contentX() + (var3 - this.v) / 2;
      this.V = this.c + 14 - 8;
      this.X = this.c + this.v - 14 + 12;
      int var4 = Math.max(1, this.v - 28 - 22);
      this.a = this.Q.isEmpty() ? List.of() : x(this.Q, var4, var0 -> k6.a(var0, k6.d.m));
      this.g = this.E.isEmpty() ? List.of() : x(this.E, var4, var0 -> k6.a(var0, k6.d.K));
      int var5 = 14 + Math.max(14, this.a.size() * (k6.d.m.q() + 3) + (this.g.isEmpty() ? 0 : 1 + this.g.size() * (k6.d.K.q() + 3))) + 8;
      this.w();
      int var6 = Math.max(0, this.r - 4);
      int var7 = this.i.isEmpty() ? 10 : 46;
      this.b = Math.min(var2, var5 + var6 + var7);
      this.s = var1 + (var2 - this.b) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.y = this.s + var5;
      this.L = Math.max(this.y, this.s + this.b - var7);
      this.S(0);
      this.e.bounds(this.c + this.v - 14 - 18 + 4, this.s + 14 - 4, 18);
      int var8 = this.s + this.b - 14 - 20;
      List var9 = this.i.stream().map(var0 -> Math.max(64, k6.a(var0.S, k6.d.K) + 24)).toList();
      int var10 = Math.max(0, this.i.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.v - 28 - var10) / Math.max(1, var11));
      int var14 = this.c + this.v - 14;

      for (int var15 = this.i.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)(((Integer)var9.get(var15)).intValue() * var12));
         var14 -= var16;
         this.i.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void a() {
      if (!this.layoutCustomContent()) {
         this.w();
         this.S((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void w() {
      this.r = 0;

      for (b var2 : this.K) {
         if (!this.e(var2)) {
            var2.D = this.componentShown(var2);
            if (!var2.D) {
               var2.blur();
            } else {
               var2.bounds(this.V + 8, 0, Math.max(1, this.X - this.V - 20));
               if (this.W && var2 instanceof u7 var3 && this.z.contains(var3)) {
                  var2.q -= 8;
                  var2.A += 16;
               }

               if (var2 instanceof hb.m var10) {
                  if (this.W) {
                     var10.q -= 4;
                  }

                  var10.e();
               }
            }
         }
      }

      int var8 = 0;

      while (var8 < this.K.size()) {
         b var9 = this.K.get(var8);
         if (!var9.D || this.e(var9)) {
            var8++;
         } else if (var9 instanceof un var11 && var11.U()) {
            ArrayList var4 = new ArrayList();

            while (var8 < this.K.size()) {
               if (!(this.K.get(var8) instanceof un var5) || !var5.U()) {
                  break;
               }

               if (var5.D) {
                  var4.add(var5);
               }

               var8++;
            }

            e5.v var12 = e5.e(Math.max(1, this.X - this.V - 20), var4.stream().map(un::c).toList());

            for (int var13 = 0; var13 < var4.size(); var13++) {
               e5.c var7 = var12.cells().get(var13);
               ((un)var4.get(var13)).bounds(this.V + 8 + var7.x(), this.r + var7.y(), var7.width());
            }

            this.r = this.r + var12.height() + 4;
         } else {
            var9.J = this.r;
            this.r = this.r + var9.W + 4;
            var8++;
         }
      }
   }

   private void S(int var1) {
      int var2 = this.r - (this.W ? 4 : 0) - (this.L - this.y);
      this.Z = Math.clamp(this.Z, 0, Math.max(0, var2));
      if (!this.T) {
         this.R.Q(this.Z);
         this.T = (boolean)1;
      }

      int var3 = (int)Math.round(this.R.m(this.Z, 150));
      int var4 = this.y - var3 + var1;

      for (b var6 : this.K) {
         if (var6.D && !this.e(var6)) {
            var6.J += var4;
            if (var6 instanceof un var7) {
               var7.N(this.j.top() + 12, this.L);
            }
         }
      }
   }

   protected boolean componentShown(b var1) {
      return var1 instanceof un var3 ? var3.x() : !(var1 instanceof us var2 && !var2.D());
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
      return this.j.contentX() + 8;
   }

   protected int titleY() {
      return this.j.bodyTop() + 8;
   }

   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.horizontalInput(var2)) {
         var2 = -1;
      }

      if (this.W != this.Y) {
         this.P();
         this.Y = this.W;
      }

      this.layout();

      for (u7 var6 : this.p) {
         if (var6.U) {
            if (!this.d) {
               this.I.Q(var6.q);
               this.C.Q(var6.A);
               this.d = (boolean)1;
            }

            int var7 = (int)Math.round(this.I.m(var6.q, 200));
            int var8 = (int)Math.round(this.C.m(var6.A, 200));
            int var9 = com.larpclient.f.Y() ? com.larpclient.f.Q(var7, com.larpclient.f.u) : _3.A(com.larpclient.f.J, 0.75);
            pn.Z(var1, var7 + 7, var6.J + var6.W, Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, _3.A(var9, 0.45));
            pn.b(var1, var7 + 7, var6.J + var6.W, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (u7 var24 : this.p) {
         var24.render(var1, this.W ? -1 : var2, var3);
      }

      if (this.showTitle()) {
         k6.T(var1, k6.o(this.F, Math.max(1, this.X - this.titleX() - 32), k6.d.m), this.titleX(), this.titleY(), com.larpclient.f.J, k6.d.m);
      }

      this.renderExtra(var1, this.W ? -1 : var2, var3);
      if (this.W) {
         this.j(var1, var2, var3);
      }

      var1.enableScissor(this.V, this.y, this.X, this.L);

      try (de var22 = de.l(this.revealAmount())) {
         for (b var27 : this.K) {
            if (this.contentShown() && var27.D && !this.e(var27) && var27.J + var27.W > this.y && var27.J < this.L) {
               var27.render(var1, var3 >= this.y && var3 < this.L ? var2 : -1, var3);
            }
         }
      } finally {
         var1.disableScissor();
      }

      int var23 = this.L - this.y;
      if (this.contentShown()) {
         pn.u(var1, this.X - 3, this.y, Math.max(1, var23), this.r - (this.W ? 4 : 0), (int)Math.round(this.R.G()));
      }

      if (this.W) {
         try (de var26 = de.l(this.revealAmount())) {
            if (this.r - 4 > var23) {
               int var28 = _3.U(com.larpclient.f.H, 1.2);
               if (this.Z > 0) {
                  pn.b(var1, this.c + 14, this.y - 1, this.v - 28, 1, 0, var28);
               }

               pn.b(var1, this.c + 14, this.L, this.v - 28, 1, 0, var28);
            }

            this.e.render(var1, var2, var3);

            for (u7 var30 : this.i) {
               var30.render(var1, var2, var3);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.J(var1, var2, var3);
      }
   }

   private void j(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.revealAmount();
      pn.b(
         var1,
         this.j.contentX(),
         this.j.bodyTop(),
         this.j.right() - this.j.contentX(),
         this.j.bottom() - this.j.bodyTop(),
         com.larpclient.f.y,
         _3.U(-1728053248, var4)
      );
      tl.j(var1, this.c, this.s + 3, this.v, this.b, com.larpclient.f.y, 16.0F, _3.U(805306368, var4), _3.U(1342177280, var4));
      pn.d(var1, this.c, this.s, this.v, this.b, com.larpclient.f.y, _3.U(com.larpclient.f.c, var4), 0.0);

      try (de var6 = de.l(var4)) {
         int var7 = this.s + 14;

         for (String var9 : this.a) {
            k6.T(var1, var9, this.c + 14, var7, com.larpclient.f.J, k6.d.m);
            var7 += k6.d.m.q() + 3;
         }

         var7++;

         for (String var14 : this.g) {
            k6.T(var1, var14, this.c + 14, var7, com.larpclient.f.s, k6.d.K);
            var7 += k6.d.K.q() + 3;
         }
      }
   }

   private boolean g(double var1, double var3) {
      return this.W
         && !c7.T(var1, var3, this.c, this.s, this.v, this.b)
         && var1 >= this.j.contentX()
         && var1 < this.j.right()
         && var3 >= this.j.top()
         && var3 < this.j.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void J(GuiGraphicsExtractor var1, int var2, int var3) {
      un var4 = this.T();
      if (var4 != null) {
         var4.M(var1, this.horizontalInput(var2) ? var2 : -1, var3);
      }
   }

   private un T() {
      for (b var2 : this.K) {
         if (var2 instanceof un var3 && var3.g()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(b var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.W && !this.K.contains(var1)) {
         return false;
      } else {
         return this.e(var1) ? this.W : !this.K.contains(var1) || this.contentShown() && var4 >= this.y && var4 < this.L && var2 >= this.V && var2 < this.X;
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.layout();
      un var3 = this.T();
      if (var3 == null) {
         if (mo.Z(var1) == 0 && this.g(var1.x(), var1.y()) && this.horizontalInput(var1.x()) && this.K.stream().noneMatch(var0 -> var0 instanceof ur)) {
            this.dismissDialog();
            return true;
         } else {
            return super.mouseClicked(var1, var2);
         }
      } else {
         if (this.horizontalInput(var1.x()) && var3.e(var1.x(), var1.y())) {
            var3.C(var1.x(), var1.y(), mo.Z(var1));
            this.f = var3;
         } else {
            var3.M();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      un var9 = this.T();
      if (var9 != null) {
         if (this.horizontalInput(var1) && var9.e(var1, var3)) {
            var9.z(var7);
         }

         return true;
      } else if (this.horizontalInput(var1) && this.contentShown() && !(var1 < this.V) && !(var1 >= this.X) && !(var3 < this.y) && !(var3 >= this.L)) {
         this.Z -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(b var1) {
      if (this.K.contains(var1)) {
         if (var1.J < this.y) {
            this.Z = this.Z - (this.y - var1.J);
         } else if (var1.J + var1.W > this.L) {
            this.Z = this.Z + (var1.J + var1.W - this.L);
         }

         this.R.Q(this.Z);
         this.layout();
      }
   }

   protected void U(b var1) {
      this.q = var1;
      var1.l = var1.R = (boolean)1;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<b> r() {
      return this.G.stream().filter(var1 -> var1.D && var1.X && (!this.K.contains(var1) || this.contentShown()) && (!this.W || this.K.contains(var1))).toList();
   }

   b c() {
      return this.q;
   }

   void o() {
      if (this.q != null) {
         this.q.blur();
      }

      this.q = this.f = null;
   }

   void M(b var1) {
      this.U(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.z();
   }

   String B(int var1, int var2) {
      if (this.W) {
         return "";
      } else if (this.q instanceof un var3 && this.q.R) {
         return var3.P();
      } else if (this.horizontalInput(var1) && var2 >= this.y && var2 < this.L) {
         for (b var7 : this.K) {
            if (var7 instanceof un var5 && var5.contains(var1, var2)) {
               return var5.P();
            }
         }

         return "";
      } else {
         return "";
      }
   }

   boolean B() {
      return this.W || this.T() != null;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      un var2 = this.T();
      if (mo.O(var1) == 256 && var2 != null) {
         var2.M();
         return true;
      }

      if (mo.O(var1) == 258) {
         List var3 = this.r();
         int var4 = (var1.modifiers() & 1) != 0 ? -1 : 1;
         int var5 = this.q == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.q);
         this.o();
         if (!var3.isEmpty()) {
            this.M((b)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      this.M.onClose();
   }

   static List<String> x(String var0, int var1, ToIntFunction<String> var2) {
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

   private static final class m extends b {
      private final Supplier<String> g;
      private List<String> p = List.of();
      private int z = -1;
      private String m;

      m(String var1) {
         this(() -> var1);
      }

      m(Supplier<String> var1) {
         this.g = var1;
         this.X = (boolean)0;
      }

      void e() {
         String var1 = this.g.get();
         if (this.z != this.A || !var1.equals(this.m)) {
            this.z = this.A;
            this.m = var1;
            this.p = hb.x(var1, this.A - 8, k6::P);
            this.W = Math.max(22, this.p.size() * 12 + 6);
         }
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         for (int var4 = 0; var4 < this.p.size(); var4++) {
            k6.Z(var1, this.p.get(var4), this.q + 4, this.J + 4 + var4 * 12, com.larpclient.f.s);
         }
      }
   }

   private static final class x extends b {
      x() {
         this.X = (boolean)0;
         this.W = 5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         pn.b(var1, this.q, this.J + 2, Math.max(1, this.A), 1, 0, _3.U(com.larpclient.f.H, 1.2));
      }
   }
}

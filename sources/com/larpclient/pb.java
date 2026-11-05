package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

abstract class pb extends pw {
   protected p0 x;
   protected mj M;
   protected final List<oq> p = new ArrayList<>();
   protected final List<p7> c = new ArrayList<>();
   protected int e;
   protected int O;
   protected int t;
   protected int A;
   protected int n;
   protected int H;
   protected boolean h;
   protected String Y;
   protected String C = "";
   protected String B = "";
   private final List<p7> Q = new ArrayList<>();
   private final List<p7> l = new ArrayList<>();
   private final p7 P = new p7("", this::dismissDialog);
   private List<String> d = List.of();
   private List<String> Z = List.of();
   private int z;
   private int U;
   private int D;
   private int g;
   private final u7 J = new u7(1.0);
   private final u7 N = new u7(0.0);
   private final u7 T = new u7(0.0);
   private final u7 m = new u7(0.0);
   private boolean v;
   private boolean j;
   private boolean F;
   private static final int o = 300;
   private static final int u = 14;
   private static final int b = 20;

   protected final void D() {
      this.J.c(0.0);
      this.J.y(1.0, 200);
   }

   protected double revealAmount() {
      return this.J.y(1.0, 200);
   }

   pb(String var1) {
      super(var1);
      this.Y = var1;
      this.P.c = (boolean)1;
      this.P.e = "x";
      this.P.O = 18;
      this.P.F = (boolean)0;
   }

   final void p(p0 var1, mj var2) {
      this.x = var1;
      this.M = var2;
      this.init(var1.width, var1.height);
      this.D();
   }

   void i(mj var1) {
      if (this.M == null || this.M.width() != var1.width() || this.M.height() != var1.height()) {
         this.v = (boolean)0;
      }

      this.M = var1;
      this.layout();
   }

   protected abstract void build();

   protected void init() {
      this.build();
   }

   protected void f() {
      this.u();
      this.p.clear();
      this.c.clear();
      this.Q.clear();
      this.l.clear();
      this.C = this.B = "";
      this.e(this.P);
   }

   protected void dismissDialog() {
   }

   protected p7 Y(String var1, Runnable var2) {
      p7 var3 = new p7(var1, var2);
      var3.q = (boolean)1;
      var3.O = 20;
      this.Q.add(var3);
      this.e(var3);
      return var3;
   }

   protected p7 option(String var1, String var2, Runnable var3) {
      p7 var4 = this.L(var1, var3);
      var4.c = (boolean)1;
      var4.d = (boolean)0;
      var4.y = var2;
      var4.O = 30;
      this.l.add(var4);
      return var4;
   }

   protected void F() {
      this.e(new pb.z());
   }

   private boolean h(oq var1) {
      return var1 == this.P || var1 instanceof p7 var2 && this.Q.contains(var2);
   }

   protected void e(oq var1) {
      this.p.add(var1);
      this.k.add(var1);
   }

   protected void text(String var1) {
      this.e(new pb.b(var1));
   }

   protected void C(Supplier<String> var1) {
      this.e(new pb.b(var1));
   }

   protected p7 L(String var1, Runnable var2) {
      p7 var3 = new p7(var1, var2);
      var3.d = (boolean)(!var1.equals("Load") && !var1.startsWith("Save") && !var1.equals("Fetch preview") ? 0 : 1);
      var3.z = var1.startsWith("Delete");
      this.e(var3);
      return var3;
   }

   protected void o(String var1, boolean var2, Runnable var3) {
      p7 var4 = new p7(var1, var3);
      var4.c = (boolean)1;
      var4.U = (boolean)1;
      var4.R = var2;
      this.c.add(var4);
      this.k.add(var4);
   }

   protected int bodyTop() {
      return this.M.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.M.contentX();
      int var2 = Math.max(1, (this.M.right() - var1) / Math.max(1, this.c.size()));

      for (p7 var4 : this.c) {
         int var5 = Math.min(var2, Math.max(62, j_.a(var4.J) + 20));
         var4.bounds(var1, this.M.top() + 12, var5 - 4);
         var4.O = 24;
         var1 += var5;
      }

      this.P.F = this.h;
      if (this.h) {
         this.n();
      } else {
         this.O = this.M.contentX();
         this.A = this.M.right();
         this.t = this.bodyTop();
         this.n = this.M.bottom();
         this.d();
      }
   }

   private void n() {
      int var1 = this.M.bodyTop() + 8;
      int var2 = Math.max(1, this.M.bottom() - 8 - var1);
      int var3 = this.M.right() - this.M.contentX();
      this.D = Math.max(1, Math.min(300, var3 - 16));
      this.z = this.M.contentX() + (var3 - this.D) / 2;
      this.O = this.z + 14 - 8;
      this.A = this.z + this.D - 14 + 12;
      int var4 = Math.max(1, this.D - 28 - 22);
      this.d = this.C.isEmpty() ? List.of() : H(this.C, var4, var0 -> j_.o(var0, j_.m.a));
      this.Z = this.B.isEmpty() ? List.of() : H(this.B, var4, var0 -> j_.o(var0, j_.m.U));
      int var5 = 14 + Math.max(14, this.d.size() * (j_.m.a.C() + 3) + (this.Z.isEmpty() ? 0 : 1 + this.Z.size() * (j_.m.U.C() + 3))) + 8;
      this.J();
      int var6 = Math.max(0, this.H - 4);
      int var7 = this.Q.isEmpty() ? 10 : 46;
      this.g = Math.min(var2, var5 + var6 + var7);
      this.U = var1 + (var2 - this.g) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.t = this.U + var5;
      this.n = Math.max(this.t, this.U + this.g - var7);
      this.G(0);
      this.P.bounds(this.z + this.D - 14 - 18 + 4, this.U + 14 - 4, 18);
      int var8 = this.U + this.g - 14 - 20;
      List var9 = this.Q.stream().map(var0 -> Math.max(64, j_.o(var0.J, j_.m.U) + 24)).toList();
      int var10 = Math.max(0, this.Q.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.D - 28 - var10) / Math.max(1, var11));
      int var14 = this.z + this.D - 14;

      for (int var15 = this.Q.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)(((Integer)var9.get(var15)).intValue() * var12));
         var14 -= var16;
         this.Q.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void d() {
      if (!this.layoutCustomContent()) {
         this.J();
         this.G((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void J() {
      this.H = 0;

      for (oq var2 : this.p) {
         if (!this.h(var2)) {
            var2.F = this.componentShown(var2);
            if (!var2.F) {
               var2.blur();
            } else {
               var2.bounds(this.O + 8, 0, Math.max(1, this.A - this.O - 20));
               if (this.h && var2 instanceof p7 var3 && this.l.contains(var3)) {
                  var2.B -= 8;
                  var2.v += 16;
               }

               if (var2 instanceof pb.b var10) {
                  if (this.h) {
                     var10.B -= 4;
                  }

                  var10.o();
               }
            }
         }
      }

      int var8 = 0;

      while (var8 < this.p.size()) {
         oq var9 = this.p.get(var8);
         if (!var9.F || this.h(var9)) {
            var8++;
         } else if (var9 instanceof p9 var11 && var11.u()) {
            ArrayList var4 = new ArrayList();

            while (var8 < this.p.size()) {
               if (!(this.p.get(var8) instanceof p9 var5) || !var5.u()) {
                  break;
               }

               if (var5.F) {
                  var4.add(var5);
               }

               var8++;
            }

            dn.i var12 = dn.U(Math.max(1, this.A - this.O - 20), var4.stream().map(p9::X).toList());

            for (int var13 = 0; var13 < var4.size(); var13++) {
               dn.v var7 = var12.cells().get(var13);
               ((p9)var4.get(var13)).bounds(this.O + 8 + var7.x(), this.H + var7.y(), var7.width());
            }

            this.H = this.H + var12.height() + 4;
         } else {
            var9.L = this.H;
            this.H = this.H + var9.O + 4;
            var8++;
         }
      }
   }

   private void G(int var1) {
      int var2 = this.H - (this.h ? 4 : 0) - (this.n - this.t);
      this.e = Math.clamp(this.e, 0, Math.max(0, var2));
      if (!this.v) {
         this.N.c(this.e);
         this.v = (boolean)1;
      }

      int var3 = (int)Math.round(this.N.y(this.e, 150));
      int var4 = this.t - var3 + var1;

      for (oq var6 : this.p) {
         if (var6.F && !this.h(var6)) {
            var6.L += var4;
            if (var6 instanceof p9 var7) {
               var7.Y(this.M.top() + 12, this.n);
            }
         }
      }
   }

   protected boolean componentShown(oq var1) {
      return var1 instanceof p9 var3 ? var3.B() : !(var1 instanceof p8 var2 && !var2.v());
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
      return this.M.contentX() + 8;
   }

   protected int titleY() {
      return this.M.bodyTop() + 8;
   }

   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.horizontalInput(var2)) {
         var2 = -1;
      }

      if (this.h != this.F) {
         this.D();
         this.F = this.h;
      }

      this.layout();

      for (p7 var6 : this.c) {
         if (var6.R) {
            if (!this.j) {
               this.T.c(var6.B);
               this.m.c(var6.v);
               this.j = (boolean)1;
            }

            int var7 = (int)Math.round(this.T.y(var6.B, 200));
            int var8 = (int)Math.round(this.m.y(var6.v, 200));
            int var9 = rv.u() ? rv.a(var7, rv.w) : oh.i(rv.P, 0.75);
            ll.S(var1, var7 + 7, var6.L + var6.O, Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, oh.i(var9, 0.45));
            ll.N(var1, var7 + 7, var6.L + var6.O, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (p7 var24 : this.c) {
         var24.render(var1, this.h ? -1 : var2, var3);
      }

      if (this.showTitle()) {
         j_.L(var1, j_.d(this.Y, Math.max(1, this.A - this.titleX() - 32), j_.m.a), this.titleX(), this.titleY(), rv.P, j_.m.a);
      }

      this.renderExtra(var1, this.h ? -1 : var2, var3);
      if (this.h) {
         this.P(var1, var2, var3);
      }

      var1.enableScissor(this.O, this.t, this.A, this.n);

      try (r var22 = r.p(this.revealAmount())) {
         for (oq var27 : this.p) {
            if (this.contentShown() && var27.F && !this.h(var27) && var27.L + var27.O > this.t && var27.L < this.n) {
               var27.render(var1, var3 >= this.t && var3 < this.n ? var2 : -1, var3);
            }
         }
      } finally {
         var1.disableScissor();
      }

      int var23 = this.n - this.t;
      if (this.contentShown()) {
         ll.g(var1, this.A - 3, this.t, Math.max(1, var23), this.H - (this.h ? 4 : 0), (int)Math.round(this.N.V()));
      }

      if (this.h) {
         try (r var26 = r.p(this.revealAmount())) {
            if (this.H - 4 > var23) {
               int var28 = oh.S(rv.h, 1.2);
               if (this.e > 0) {
                  ll.N(var1, this.z + 14, this.t - 1, this.D - 28, 1, 0, var28);
               }

               ll.N(var1, this.z + 14, this.n, this.D - 28, 1, 0, var28);
            }

            this.P.render(var1, var2, var3);

            for (p7 var30 : this.Q) {
               var30.render(var1, var2, var3);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.S(var1, var2, var3);
      }
   }

   private void P(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.revealAmount();
      ll.N(var1, this.M.contentX(), this.M.bodyTop(), this.M.right() - this.M.contentX(), this.M.bottom() - this.M.bodyTop(), rv.Gj, oh.S(-1728053248, var4));
      ow.A(var1, this.z, this.U + 3, this.D, this.g, rv.Gj, 16.0F, oh.S(805306368, var4), oh.S(1342177280, var4));
      ll.w(var1, this.z, this.U, this.D, this.g, rv.Gj, oh.S(rv.Y, var4), 0.0);

      try (r var6 = r.p(var4)) {
         int var7 = this.U + 14;

         for (String var9 : this.d) {
            j_.L(var1, var9, this.z + 14, var7, rv.P, j_.m.a);
            var7 += j_.m.a.C() + 3;
         }

         var7++;

         for (String var14 : this.Z) {
            j_.L(var1, var14, this.z + 14, var7, rv.G0, j_.m.U);
            var7 += j_.m.U.C() + 3;
         }
      }
   }

   private boolean s(double var1, double var3) {
      return this.h
         && !fr.Q(var1, var3, this.z, this.U, this.D, this.g)
         && var1 >= this.M.contentX()
         && var1 < this.M.right()
         && var3 >= this.M.top()
         && var3 < this.M.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void S(GuiGraphicsExtractor var1, int var2, int var3) {
      p9 var4 = this.Z();
      if (var4 != null) {
         var4.x(var1, this.horizontalInput(var2) ? var2 : -1, var3);
      }
   }

   private p9 Z() {
      for (oq var2 : this.p) {
         if (var2 instanceof p9 var3 && var3.y()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(oq var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.h && !this.p.contains(var1)) {
         return false;
      } else {
         return this.h(var1) ? this.h : !this.p.contains(var1) || this.contentShown() && var4 >= this.t && var4 < this.n && var2 >= this.O && var2 < this.A;
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.layout();
      p9 var3 = this.Z();
      if (var3 == null) {
         if (r7.y(var1) == 0 && this.s(var1.x(), var1.y()) && this.horizontalInput(var1.x()) && this.p.stream().noneMatch(var0 -> var0 instanceof p_)) {
            this.dismissDialog();
            return true;
         } else {
            return super.mouseClicked(var1, var2);
         }
      } else {
         if (this.horizontalInput(var1.x()) && var3.R(var1.x(), var1.y())) {
            var3.N(var1.x(), var1.y(), r7.y(var1));
            this.L = var3;
         } else {
            var3.n();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      p9 var9 = this.Z();
      if (var9 != null) {
         if (this.horizontalInput(var1) && var9.R(var1, var3)) {
            var9.J(var7);
         }

         return true;
      } else if (this.horizontalInput(var1) && this.contentShown() && !(var1 < this.O) && !(var1 >= this.A) && !(var3 < this.t) && !(var3 >= this.n)) {
         this.e -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(oq var1) {
      if (this.p.contains(var1)) {
         if (var1.L < this.t) {
            this.e = this.e - (this.t - var1.L);
         } else if (var1.L + var1.O > this.n) {
            this.e = this.e + (var1.L + var1.O - this.n);
         }

         this.N.c(this.e);
         this.layout();
      }
   }

   protected void D(oq var1) {
      this.W = var1;
      var1.A = var1.h = (boolean)1;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<oq> S() {
      return this.k.stream().filter(var1 -> var1.F && var1.x && (!this.p.contains(var1) || this.contentShown()) && (!this.h || this.p.contains(var1))).toList();
   }

   oq y() {
      return this.W;
   }

   void M() {
      if (this.W != null) {
         this.W.blur();
      }

      this.W = this.L = null;
   }

   void x(oq var1) {
      this.D(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.u();
   }

   String E(int var1, int var2) {
      if (this.h) {
         return "";
      } else if (this.W instanceof p9 var3 && this.W.h) {
         return var3.E();
      } else if (this.horizontalInput(var1) && var2 >= this.t && var2 < this.n) {
         for (oq var7 : this.p) {
            if (var7 instanceof p9 var5 && var5.contains(var1, var2)) {
               return var5.E();
            }
         }

         return "";
      } else {
         return "";
      }
   }

   boolean m() {
      return this.h || this.Z() != null;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      p9 var2 = this.Z();
      if (r7.g(var1) == 256 && var2 != null) {
         var2.n();
         return true;
      }

      if (r7.g(var1) == 258) {
         List var3 = this.S();
         int var4 = (var1.modifiers() & 1) != 0 ? -1 : 1;
         int var5 = this.W == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.W);
         this.M();
         if (!var3.isEmpty()) {
            this.x((oq)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      this.x.onClose();
   }

   static List<String> H(String var0, int var1, ToIntFunction<String> var2) {
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

   private static final class b extends oq {
      private final Supplier<String> s;
      private List<String> r = List.of();
      private int k = -1;
      private String R;

      b(String var1) {
         this(() -> var1);
      }

      b(Supplier<String> var1) {
         this.s = var1;
         this.x = (boolean)0;
      }

      void o() {
         String var1 = this.s.get();
         if (this.k != this.v || !var1.equals(this.R)) {
            this.k = this.v;
            this.R = var1;
            this.r = pb.H(var1, this.v - 8, j_::a);
            this.O = Math.max(22, this.r.size() * 12 + 6);
         }
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         for (int var4 = 0; var4 < this.r.size(); var4++) {
            j_.E(var1, this.r.get(var4), this.B + 4, this.L + 4 + var4 * 12, rv.G0);
         }
      }
   }

   private static final class z extends oq {
      z() {
         this.x = (boolean)0;
         this.O = 5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         ll.N(var1, this.B, this.L + 2, Math.max(1, this.v), 1, 0, oh.S(rv.h, 1.2));
      }
   }
}

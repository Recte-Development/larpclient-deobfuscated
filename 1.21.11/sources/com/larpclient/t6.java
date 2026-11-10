package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_332;

abstract class t6 extends t1 {
   protected tk X;
   protected bu r;
   protected final List<lc> F = new ArrayList<>();
   protected final List<ls> S = new ArrayList<>();
   protected int f;
   protected int C;
   protected int i;
   protected int A;
   protected int l;
   protected int R;
   protected boolean e;
   protected String v;
   protected String h = "";
   protected String B = "";
   private final List<ls> x = new ArrayList<>();
   private final List<ls> n = new ArrayList<>();
   private final ls w = new ls("", this::dismissDialog);
   private List<String> H = List.of();
   private List<String> s = List.of();
   private int j;
   private int a;
   private int T;
   private int N;
   private final qb Z = new qb(1.0);
   private final qb I = new qb(0.0);
   private final qb D = new qb(0.0);
   private final qb z = new qb(0.0);
   private boolean p;
   private boolean W;
   private boolean b;
   private static final int P = 300;
   private static final int m = 14;
   private static final int O = 20;

   protected final void W() {
      this.Z.w(0.0);
      this.Z.a(1.0, 200);
   }

   protected double revealAmount() {
      return this.Z.a(1.0, 200);
   }

   t6(String var1) {
      super(var1);
      this.v = var1;
      this.w.S = (boolean)1;
      this.w.n = "x";
      this.w.w = 18;
      this.w.I = (boolean)0;
   }

   final void J(tk var1, bu var2) {
      this.X = var1;
      this.r = var2;
      this.method_25423(var1.field_22789, var1.field_22790);
      this.W();
   }

   void L(bu var1) {
      if (this.r == null || this.r.width() != var1.width() || this.r.height() != var1.height()) {
         this.p = (boolean)0;
      }

      this.r = var1;
      this.layout();
   }

   protected abstract void build();

   protected void method_25426() {
      this.build();
   }

   protected void o() {
      this.t();
      this.F.clear();
      this.S.clear();
      this.x.clear();
      this.n.clear();
      this.h = this.B = "";
      this.u(this.w);
   }

   protected void dismissDialog() {
   }

   protected ls k(String var1, Runnable var2) {
      ls var3 = new ls(var1, var2);
      var3.x = (boolean)1;
      var3.w = 20;
      this.x.add(var3);
      this.u(var3);
      return var3;
   }

   protected ls option(String var1, String var2, Runnable var3) {
      ls var4 = this.s(var1, var3);
      var4.S = (boolean)1;
      var4.v = (boolean)0;
      var4.T = var2;
      var4.w = 30;
      this.n.add(var4);
      return var4;
   }

   protected void A() {
      this.u(new t6.y());
   }

   private boolean A(lc var1) {
      return var1 == this.w || var1 instanceof ls var2 && this.x.contains(var2);
   }

   protected void u(lc var1) {
      this.F.add(var1);
      this.K.add(var1);
   }

   protected void text(String var1) {
      this.u(new t6.g(var1));
   }

   protected void o(Supplier<String> var1) {
      this.u(new t6.g(var1));
   }

   protected void x(Supplier<String> var1, IntSupplier var2) {
      this.u(new t6.g(var1, var2));
   }

   protected void d(String var1, Supplier<String> var2) {
      this.u(new t6.g(var1, var2));
   }

   protected ls s(String var1, Runnable var2) {
      ls var3 = new ls(var1, var2);
      var3.v = (boolean)(!var1.equals("Load") && !var1.startsWith("Save") && !var1.equals("Fetch preview") ? 0 : 1);
      var3.L = var1.startsWith("Delete");
      this.u(var3);
      return var3;
   }

   protected void S(String var1, boolean var2, Runnable var3) {
      ls var4 = new ls(var1, var3);
      var4.S = (boolean)1;
      var4.V = (boolean)1;
      var4.o = var2;
      this.S.add(var4);
      this.K.add(var4);
   }

   protected int bodyTop() {
      return this.r.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.r.contentX();
      int var2 = Math.max(1, (this.r.right() - var1) / Math.max(1, this.S.size()));

      for (ls var4 : this.S) {
         int var5 = Math.min(var2, Math.max(62, uw.r(var4.O) + 20));
         var4.bounds(var1, this.r.top() + 12, var5 - 4);
         var4.w = 24;
         var1 += var5;
      }

      this.w.I = this.e;
      if (this.e) {
         this.i();
      } else {
         this.C = this.r.contentX();
         this.A = this.r.right();
         this.i = this.bodyTop();
         this.l = this.r.bottom();
         this.q();
      }
   }

   private void i() {
      int var1 = this.r.bodyTop() + 8;
      int var2 = Math.max(1, this.r.bottom() - 8 - var1);
      int var3 = this.r.right() - this.r.contentX();
      this.T = Math.max(1, Math.min(300, var3 - 16));
      this.j = this.r.contentX() + (var3 - this.T) / 2;
      this.C = this.j + 14 - 8;
      this.A = this.j + this.T - 14 + 12;
      int var4 = Math.max(1, this.T - 28 - 22);
      this.H = this.h.isEmpty() ? List.of() : l(this.h, var4, var0 -> uw.C(var0, uw.c.v));
      this.s = this.B.isEmpty() ? List.of() : l(this.B, var4, var0 -> uw.C(var0, uw.c.M));
      int var5 = 14 + Math.max(14, this.H.size() * (uw.c.v.b() + 3) + (this.s.isEmpty() ? 0 : 1 + this.s.size() * (uw.c.M.b() + 3))) + 8;
      this.c();
      int var6 = Math.max(0, this.R - 4);
      int var7 = this.x.isEmpty() ? 10 : 46;
      this.N = Math.min(var2, var5 + var6 + var7);
      this.a = var1 + (var2 - this.N) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.i = this.a + var5;
      this.l = Math.max(this.i, this.a + this.N - var7);
      this.Q(0);
      this.w.bounds(this.j + this.T - 14 - 18 + 4, this.a + 14 - 4, 18);
      int var8 = this.a + this.N - 14 - 20;
      List var9 = this.x.stream().map(var0 -> Math.max(64, uw.C(var0.O, uw.c.M) + 24)).toList();
      int var10 = Math.max(0, this.x.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.T - 28 - var10) / Math.max(1, var11));
      int var14 = this.j + this.T - 14;

      for (int var15 = this.x.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)(((Integer)var9.get(var15)).intValue() * var12));
         var14 -= var16;
         this.x.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void q() {
      if (!this.layoutCustomContent()) {
         this.c();
         this.Q((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void c() {
      this.R = 0;

      for (lc var2 : this.F) {
         if (!this.A(var2)) {
            var2.I = this.componentShown(var2);
            if (!var2.I) {
               var2.blur();
            } else {
               var2.bounds(this.C + 8, 0, Math.max(1, this.A - this.C - 20));
               if (this.e && var2 instanceof ls var3 && this.n.contains(var3)) {
                  var2.D -= 8;
                  var2.d += 16;
               }

               if (var2 instanceof t6.g var11) {
                  if (this.e) {
                     var11.D -= 4;
                  }

                  var11.t();
               }
            }
         }
      }

      int var9 = 0;

      while (var9 < this.F.size()) {
         lc var10 = this.F.get(var9);
         if (!var10.I || this.A(var10)) {
            var9++;
         } else if (var10 instanceof l9 var12 && var12.i()) {
            ArrayList var13 = new ArrayList();

            while (var9 < this.F.size()) {
               if (!(this.F.get(var9) instanceof l9 var15) || !var15.i()) {
                  break;
               }

               if (var15.I) {
                  var13.add(var15);
               }

               var9++;
            }

            lz.r var16 = lz.x(Math.max(1, this.A - this.C - 20), var13.stream().map(l9::O).toList());

            for (int var19 = 0; var19 < var13.size(); var19++) {
               lz.v var20 = var16.cells().get(var19);
               ((l9)var13.get(var19)).bounds(this.C + 8 + var20.x(), this.R + var20.y(), var20.width());
            }

            this.R = this.R + var16.height() + 4;
         } else if (var10 instanceof ls var4 && var4.U != null) {
            ArrayList var5 = new ArrayList();

            while (var9 < this.F.size()) {
               if (!(this.F.get(var9) instanceof ls var6) || var6.U != var4.U) {
                  break;
               }

               if (var6.I) {
                  var5.add(var6);
               }

               var9++;
            }

            lz.r var14 = lz.b(Math.max(1, this.A - this.C - 20), var5.stream().map(var0 -> uw.C(var0.O, uw.c.o) + 18).toList());

            for (int var17 = 0; var17 < var5.size(); var17++) {
               lz.v var8 = var14.cells().get(var17);
               ((ls)var5.get(var17)).bounds(this.C + 8 + var8.x(), this.R + var8.y(), var8.width());
               ((ls)var5.get(var17)).w = 24;
            }

            this.R = this.R + var14.height() + 4;
         } else {
            var10.g = this.R;
            this.R = this.R + var10.w + 4;
            var9++;
         }
      }
   }

   private void Q(int var1) {
      int var2 = this.R - (this.e ? 4 : 0) - (this.l - this.i);
      this.f = Math.clamp(this.f, 0, Math.max(0, var2));
      if (!this.p) {
         this.I.w(this.f);
         this.p = (boolean)1;
      }

      int var3 = (int)Math.round(this.I.a(this.f, 150));
      int var4 = this.i - var3 + var1;

      for (lc var6 : this.F) {
         if (var6.I && !this.A(var6)) {
            var6.g += var4;
            if (var6 instanceof l9 var7) {
               var7.Z(this.r.top() + 12, this.l);
            }
         }
      }
   }

   protected boolean componentShown(lc var1) {
      return var1 instanceof l9 var3 ? var3.L() : !(var1 instanceof l5 var2 && !var2.H());
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
      return this.r.contentX() + 8;
   }

   protected int titleY() {
      return this.r.bodyTop() + 8;
   }

   protected void renderExtra(class_332 var1, int var2, int var3) {
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      if (!this.horizontalInput(var2)) {
         var2 = -1;
      }

      if (this.e != this.b) {
         this.W();
         this.b = this.e;
      }

      this.layout();

      for (ls var6 : this.S) {
         if (var6.o) {
            if (!this.W) {
               this.D.w(var6.D);
               this.z.w(var6.d);
               this.W = (boolean)1;
            }

            int var7 = (int)Math.round(this.D.a(var6.D, 200));
            int var8 = (int)Math.round(this.z.a(var6.d, 200));
            int var9 = ez.d() ? ez.X(var7, ez.d) : ei.D(ez.H, 0.75);
            com.larpclient.p.a(var1, var7 + 7, var6.g + var6.w, Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, ei.D(var9, 0.45));
            com.larpclient.p.B(var1, var7 + 7, var6.g + var6.w, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (ls var24 : this.S) {
         var24.render(var1, this.e ? -1 : var2, var3);
      }

      if (this.showTitle()) {
         uw.X(var1, uw.X(this.v, Math.max(1, this.A - this.titleX() - 32), uw.c.v), this.titleX(), this.titleY(), ez.H, uw.c.v);
      }

      this.renderExtra(var1, this.e ? -1 : var2, var3);
      if (this.e) {
         this.Q(var1, var2, var3);
      }

      var1.method_44379(this.C, this.i, this.A, this.l);

      try (fp var22 = fp.O(this.revealAmount())) {
         for (lc var27 : this.F) {
            if (this.contentShown() && var27.I && !this.A(var27) && var27.g + var27.w > this.i && var27.g < this.l) {
               var27.render(var1, var3 >= this.i && var3 < this.l ? var2 : -1, var3);
            }
         }
      } finally {
         var1.method_44380();
      }

      int var23 = this.l - this.i;
      if (this.contentShown()) {
         com.larpclient.p.t(var1, this.A - 3, this.i, Math.max(1, var23), this.R - (this.e ? 4 : 0), (int)Math.round(this.I.u()));
      }

      if (this.e) {
         try (fp var26 = fp.O(this.revealAmount())) {
            if (this.R - 4 > var23) {
               int var28 = ei.R(ez.o, 1.2);
               if (this.f > 0) {
                  com.larpclient.p.B(var1, this.j + 14, this.i - 1, this.T - 28, 1, 0, var28);
               }

               com.larpclient.p.B(var1, this.j + 14, this.l, this.T - 28, 1, 0, var28);
            }

            this.w.render(var1, var2, var3);

            for (ls var30 : this.x) {
               var30.render(var1, var2, var3);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.k(var1, var2, var3);
      }
   }

   private void Q(class_332 var1, int var2, int var3) {
      double var4 = this.revealAmount();
      com.larpclient.p.B(
         var1, this.r.contentX(), this.r.bodyTop(), this.r.right() - this.r.contentX(), this.r.bottom() - this.r.bodyTop(), ez.V, ei.R(-1728053248, var4)
      );
      ws.K(var1, this.j, this.a + 3, this.T, this.N, ez.V, 16.0F, ei.R(805306368, var4), ei.R(1342177280, var4));
      com.larpclient.p.N(var1, this.j, this.a, this.T, this.N, ez.V, ei.R(ez.p, var4), 0.0);

      try (fp var6 = fp.O(var4)) {
         int var7 = this.a + 14;

         for (String var9 : this.H) {
            uw.X(var1, var9, this.j + 14, var7, ez.H, uw.c.v);
            var7 += uw.c.v.b() + 3;
         }

         var7++;

         for (String var14 : this.s) {
            uw.X(var1, var14, this.j + 14, var7, ez.PH, uw.c.M);
            var7 += uw.c.M.b() + 3;
         }
      }
   }

   private boolean E(double var1, double var3) {
      return this.e
         && !gu.X(var1, var3, this.j, this.a, this.T, this.N)
         && var1 >= this.r.contentX()
         && var1 < this.r.right()
         && var3 >= this.r.top()
         && var3 < this.r.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void k(class_332 var1, int var2, int var3) {
      l9 var4 = this.P();
      if (var4 != null) {
         var4.S(var1, this.horizontalInput(var2) ? var2 : -1, var3);
      }
   }

   private l9 P() {
      for (lc var2 : this.F) {
         if (var2 instanceof l9 var3 && var3.j()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(lc var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.e && !this.F.contains(var1)) {
         return false;
      } else {
         return this.A(var1) ? this.e : !this.F.contains(var1) || this.contentShown() && var4 >= this.i && var4 < this.l && var2 >= this.C && var2 < this.A;
      }
   }

   @Override
   public boolean method_25402(class_11909 var1, boolean var2) {
      this.layout();
      l9 var3 = this.P();
      if (var3 == null) {
         if (s2.m(var1) == 0
            && this.E(var1.comp_4798(), var1.comp_4799())
            && this.horizontalInput(var1.comp_4798())
            && this.F.stream().noneMatch(var0 -> var0 instanceof lo)) {
            this.dismissDialog();
            return true;
         } else {
            return super.method_25402(var1, var2);
         }
      } else {
         if (this.horizontalInput(var1.comp_4798()) && var3.p(var1.comp_4798(), var1.comp_4799())) {
            var3.b(var1.comp_4798(), var1.comp_4799(), s2.m(var1));
            this.y = var3;
         } else {
            var3.f();
         }

         return true;
      }
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      l9 var9 = this.P();
      if (var9 != null) {
         if (this.horizontalInput(var1) && var9.p(var1, var3)) {
            var9.V(var7);
         }

         return true;
      } else if (this.horizontalInput(var1) && this.contentShown() && !(var1 < this.C) && !(var1 >= this.A) && !(var3 < this.i) && !(var3 >= this.l)) {
         this.f -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(lc var1) {
      if (this.F.contains(var1)) {
         if (var1.g < this.i) {
            this.f = this.f - (this.i - var1.g);
         } else if (var1.g + var1.w > this.l) {
            this.f = this.f + (var1.g + var1.w - this.l);
         }

         this.I.w(this.f);
         this.layout();
      }
   }

   protected void p(lc var1) {
      this.G = var1;
      var1.M = var1.q = (boolean)1;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<lc> e() {
      return this.K.stream().filter(var1 -> var1.I && var1.N && (!this.F.contains(var1) || this.contentShown()) && (!this.e || this.F.contains(var1))).toList();
   }

   lc c() {
      return this.G;
   }

   void y() {
      if (this.G != null) {
         this.G.blur();
      }

      this.G = this.y = null;
   }

   void H(lc var1) {
      this.p(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.t();
   }

   String S(int var1, int var2) {
      if (this.e) {
         return "";
      } else if (this.G instanceof l9 var3 && this.G.q) {
         return var3.J();
      } else if (this.horizontalInput(var1) && var2 >= this.i && var2 < this.l) {
         for (lc var7 : this.F) {
            if (var7 instanceof l9 var5 && var5.contains(var1, var2)) {
               return var5.J();
            }
         }

         return "";
      } else {
         return "";
      }
   }

   boolean N() {
      return this.e || this.P() != null;
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      l9 var2 = this.P();
      if (s2.i(var1) == 256 && var2 != null) {
         var2.f();
         return true;
      }

      if (s2.i(var1) == 258) {
         List var3 = this.e();
         int var4 = (var1.comp_4797() & 1) != 0 ? -1 : 1;
         int var5 = this.G == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.G);
         this.y();
         if (!var3.isEmpty()) {
            this.H((lc)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.method_25404(var1);
      }
   }

   public void method_25419() {
      this.X.method_25419();
   }

   static List<String> l(String var0, int var1, ToIntFunction<String> var2) {
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

   private static final class g extends lc {
      private final Supplier<String> X;
      private List<String> k = List.of();
      private int H = -1;
      private String o;
      private final String R;
      private final IntSupplier L;

      g(String var1) {
         this(() -> var1);
      }

      g(Supplier<String> var1) {
         this(var1, () -> ez.PH);
      }

      g(Supplier<String> var1, IntSupplier var2) {
         this(null, var1, var2);
      }

      g(String var1, Supplier<String> var2) {
         this(var1, var2, () -> ez.PH);
      }

      private g(String var1, Supplier<String> var2, IntSupplier var3) {
         this.R = var1;
         this.X = var2;
         this.L = var3;
         this.N = (boolean)0;
      }

      void t() {
         if (this.R == null) {
            String var1 = this.X.get();
            if (this.H != this.d || !var1.equals(this.o)) {
               this.H = this.d;
               this.o = var1;
               this.k = t6.l(var1, this.d - 8, uw::r);
               this.w = Math.max(22, this.k.size() * 12 + 6);
            }
         }
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         if (this.R != null) {
            String var6 = uw.J(this.X.get(), Math.max(1, this.d * 2 / 3));
            int var5 = uw.r(var6);
            uw.G(var1, uw.J(this.R, Math.max(1, this.d - var5 - 20)), this.D + 4, this.g + 7, ez.H);
            uw.G(var1, var6, this.D + this.d - 4 - var5, this.g + 7, this.L.getAsInt());
         } else {
            for (int var4 = 0; var4 < this.k.size(); var4++) {
               uw.G(var1, this.k.get(var4), this.D + 4, this.g + 4 + var4 * 12, this.L.getAsInt());
            }
         }
      }
   }

   private static final class y extends lc {
      y() {
         this.N = (boolean)0;
         this.w = 5;
      }

      @Override
      public void render(class_332 var1, int var2, int var3) {
         com.larpclient.p.B(var1, this.D, this.g + 2, Math.max(1, this.d), 1, 0, ei.R(ez.o, 1.2));
      }
   }
}

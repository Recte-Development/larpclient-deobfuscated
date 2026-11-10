package com.larpclient;

import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.Module;
import com.larpclient.module.aj;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;

public final class l9 extends lc {
   private final com.larpclient.module.lb<?> s;
   private final boolean A;
   private boolean F;
   private boolean p;
   private boolean R;
   private boolean e;
   private boolean Y;
   private boolean l;
   private boolean V;
   private static final long Z = 300000000L;
   private long U;
   private Module j;
   private boolean P;
   private String E = "";
   private long x;
   private long v;
   private String W;
   private final qb G = new qb(0.0);
   private final qb B = new qb(0.0);
   private final qb c;
   private final qb m;
   private final qb o;
   private int K = 1;
   private int u = -1;
   private int y;
   private int H;
   private int b;
   private int k;
   private int T;
   private int L;
   private int X;
   private int a;
   private final lo C;
   private final lo t;
   private boolean n;
   private List<String> r = List.of();
   private int S;

   public l9(com.larpclient.module.lb<?> var1) {
      this(var1, false);
   }

   public l9(com.larpclient.module.lb<?> var1, boolean var2) {
      this.A = var2;
      this.s = var1;
      this.C = var1 instanceof com.larpclient.module.li var3 && var3.isSearchable() ? new lo("Search options...", var1x -> this.X(), 96) : null;
      this.t = var1 instanceof aj var4 ? new lo(var4.m(), var0 -> {}, 128).T(this::w) : null;
      if (var1 instanceof com.larpclient.module.li var5) {
         this.r = var5.getVisibleModes();
      }

      this.c = new qb(var1 instanceof com.larpclient.module.l3 var6 && var6.getValue() ? 1 : 0);
      this.m = new qb(var1 instanceof com.larpclient.module.lo var7 ? var7.L() : 0.0);
      this.o = new qb(this.A() != null ? this.A().L() : 0.0);
      this.w = this.Q() > 0 ? this.Q() + 6 : (this.u() ? 38 : 26);
   }

   public boolean L() {
      return this.A || this.s.B();
   }

   public static l9 g(com.larpclient.module.lb<?> var0) {
      if (!(var0 instanceof com.larpclient.module.l3)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      }

      l9 var1 = new l9(var0);
      var1.F = (boolean)1;
      var1.w = 24;
      return var1;
   }

   public boolean i() {
      return this.F;
   }

   public int O() {
      String var1 = this.q();
      return uw.r(this.s.getDisplayName()) + 20 + (var1 == null ? 0 : i(var1) + 4);
   }

   public String J() {
      if (this.h() == null) {
         return this.s.F();
      }

      int var1 = this.p();
      String var2 = this.s instanceof com.larpclient.module.l3 ? "toggle" : "cycle";
      String var3 = var1 == -1
         ? "Middle click to bind a key or mouse button that " + var2 + "s this."
         : "Shortcut: " + s5.S(var1) + ". Middle click to change, then Escape/Delete to clear.";
      return this.s.F() + " " + var3;
   }

   private Module h() {
      if (!this.P) {
         this.P = (boolean)1;
         LarpClient var1 = LarpClient.b();
         if (var1 != null && var1.G() != null && je.O(this.s)) {
            this.j = je.S(var1.G().M(), this.s);
         }
      }

      return this.j;
   }

   private int p() {
      Module var1 = this.h();
      return var1 == null ? -1 : LarpClient.b().e().F().j.C(var1, this.s);
   }

   private void r(int var1) {
      Module var2 = this.h();
      if (var2 != null && LarpClient.b().e().L(var2, this.s, var1)) {
         c_.b(c_.d.q);
      }
   }

   private String q() {
      if (this.Y) {
         return "Press key…";
      }

      int var1 = this.p();
      return var1 == -1 ? null : s5.S(var1);
   }

   private static int i(String var0) {
      return uw.C(var0, uw.c.M) + 8;
   }

   private void O(class_332 var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.q();
      if (var7 == null) {
         uw.G(var1, uw.J(this.s.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(i(var7), Math.max(1, var2 / 2));
         String var9 = uw.J(this.s.getDisplayName(), Math.max(1, var2 - var8 - 4));
         uw.G(var1, var9, var3, var4, var5);
         int var10 = var3 + uw.r(var9) + 4;
         com.larpclient.p.B(var1, var10, var4 - 2, var8, 13, 3, ei.D(var5, 0.08));
         uw.X(var1, uw.X(var7, var8 - 8, uw.c.M), var10 + 4, var4, this.Y ? var6 : ei.D(var5, 0.7), uw.c.M);
      }
   }

   public boolean j() {
      return this.p && this.I;
   }

   public void f() {
      this.p = (boolean)0;
      this.R = (boolean)0;
      this.B.w(0.0);
      if (this.C != null) {
         this.C.blur();
      }
   }

   private int o() {
      return this.C == null ? 0 : Math.min(24, this.T);
   }

   private int X() {
      return Math.max(0, (this.T - this.o()) / this.T());
   }

   private int Q() {
      return this.s instanceof com.larpclient.module.li var1 ? var1.o() : 0;
   }

   private int T() {
      return this.Q() > 0 ? 30 : 20;
   }

   private void X() {
      this.r = ((com.larpclient.module.li)this.s).j(this.C.h);
      this.L = this.S = 0;
      this.Z(this.X, this.a);
   }

   private int n() {
      return Math.min(148, this.d / 2 - 4);
   }

   private int k() {
      return this.D + this.d - this.n() - 4;
   }

   private boolean u() {
      return this.s instanceof com.larpclient.module.lo var1 && (var1.S() || var1.k() != null);
   }

   private int s() {
      return this.u() ? this.D + 6 : this.k();
   }

   private int d() {
      return this.u() ? this.g + 29 : this.g + 11;
   }

   private int R() {
      return Math.max(1, this.u() ? this.d - 12 : this.n() - this.i() - 14);
   }

   private com.larpclient.module.lo A() {
      return this.s instanceof com.larpclient.module.lo var1 ? var1.k() : null;
   }

   private int i() {
      if (!this.u()) {
         return 44;
      }

      if (class_310.method_1551() == null) {
         return 44;
      }

      if (this.u < 0) {
         this.u = Math.clamp(uw.r(((com.larpclient.module.lo)this.s).I()) + 10, 44, 96);
      }

      return this.u;
   }

   private int w() {
      return this.D + this.d - 4 - this.i();
   }

   private String w() {
      if (this.s instanceof com.larpclient.module.lo var8) {
         com.larpclient.module.lo var11 = var8.k();
         long var3 = Double.doubleToLongBits(var8.getValue());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.getValue());
         if (this.W == null || this.x != var3 || this.v != var5) {
            this.W = !var8.S() && var11 == null && !var8.L() ? BigDecimal.valueOf(var8.getValue()).stripTrailingZeros().toPlainString() : var8.r();
            this.x = var3;
            this.v = var5;
         }

         return this.W;
      } else if (this.s instanceof com.larpclient.module.lw var7) {
         return s5.S(var7.getValue());
      } else {
         return this.s instanceof com.larpclient.module.li var1 ? var1.getModeLabel(var1.getValue()) : this.s.getValueAsString();
      }
   }

   public void Z(int var1, int var2) {
      if (this.C == null && this.s instanceof com.larpclient.module.li var3) {
         this.r = var3.getVisibleModes();
      }

      this.X = var1;
      this.a = var2;
      this.H = this.s instanceof com.larpclient.module.li ? this.k() : this.D;
      this.k = this.s instanceof com.larpclient.module.li ? this.n() : this.d;
      int var5 = this.s instanceof com.larpclient.module.li ? (this.C == null ? 0 : 24) + Math.max(1, Math.min(8, this.r.size())) * this.T() : 80;
      this.T = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.B.a(this.p ? 1.0 : 0.0, 160)) * 4.0);
      this.b = Math.clamp(this.g + this.w + var6, var1, Math.max(var1, var2 - this.T));
      if (this.s instanceof com.larpclient.module.li) {
         this.L = Math.clamp(this.L, 0, Math.max(0, this.r.size() - Math.max(1, this.X())));
      }

      if (this.C != null) {
         this.C.D = this.H + 3;
         this.C.g = this.b + 2;
         this.C.d = Math.max(1, this.k - 6);
         this.C.w = Math.max(0, this.o() - 4);
      }
   }

   public boolean p(double var1, double var3) {
      return this.j() && gu.X(var1, var3, this.H, this.b, this.k, this.T);
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      if (this.I) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.G.a(!var4 && !this.M ? 0.0 : 1.0, 150);
         int var7 = jx.readableAccent(ez.P(this.D), ez.d);
         if (this.F && this.s instanceof com.larpclient.module.l3 var22) {
            double var24 = this.c.a(var22.getValue() ? 1.0 : 0.0, 150);
            int var26 = jx.readableAccent(ez.P(this.D), ez.M | 0xFF000000);
            int var28 = ei.W(ez.B | 0xFF000000, ez.K | 0xFF000000, var5);
            int var29 = ei.W(var28, var26, var24);
            int var33 = ez.d() ? this.w / 2 : 7;
            if (var24 > 0.01) {
               com.larpclient.p.a(var1, this.D, this.g, this.d, this.w, var33, 5.0F, ei.D(var26, 0.22 * var24));
            }

            com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, var33, var29);
            com.larpclient.p.a(var1, this.D, this.g, this.d, this.w, var33, ei.W(ez.D, var26, var24));
            if (this.M && this.q) {
               com.larpclient.p.C(var1, this.D, this.g, this.d, this.w, var33);
            }

            this.O(var1, this.d - 16, this.D + 8, this.g + (this.w - 9) / 2, jx.controlInk(var29), var7);
         } else {
            if (var5 > 0.001 && ez.d()) {
               com.larpclient.p.B(var1, this.D, this.g, this.d, this.w, ez.m + 1, ei.D(ez.H, 0.028 * var5));
            }

            int var8 = this.Q();
            int var23 = var8 > 0 ? var8 + 8 : 0;
            int var10 = var8 > 0 ? this.g + (this.w - 26) / 2 : this.g;
            if (var8 > 0 && this.s instanceof com.larpclient.module.li var11) {
               var11.m(var1, var11.getValue(), this.D + 4, this.g + 3, var8);
            }

            this.O(
               var1,
               this.s instanceof com.larpclient.module.l3 ? this.d - 42 : (this.u() ? this.d - this.i() - 16 : this.d / 2 - 12 - var23),
               this.D + 4 + var23,
               var10 + 7,
               ez.H,
               var7
            );
            int var25 = this.M && this.q ? 1 : 0;
            if (this.s instanceof com.larpclient.module.l3 var27) {
               if (var25 != 0) {
                  com.larpclient.p.C(var1, this.D + this.d - 28, this.g + 6, 24, 13, 6);
               }

               com.larpclient.p.s(var1, this.D + this.d - 28, this.g + 6, this.c.a(var27.getValue() ? 1.0 : 0.0, 180));
            } else if (this.s instanceof com.larpclient.module.lo var13) {
               com.larpclient.p.x(var1, this.w(), this.g + 3, this.i(), 20, !this.l && var25 == 0 ? var5 * 0.3 : 1.0);
               String var31 = uw.J(this.l ? this.E + "_" : this.w(), this.i() - 10);
               uw.G(var1, var31, this.w() + this.i() - 6 - uw.r(var31), this.g + 7, this.l ? ez.H : ei.W(ez.PH, ez.H, var5));
               if (this.R) {
                  this.m.w(var13.L());
               }

               double var15 = this.m.a(var13.L(), 160);
               com.larpclient.module.lo var17 = this.A();
               if (ez.d()) {
                  double var37 = -1.0;
                  if (var17 != null) {
                     if (this.R) {
                        this.o.w(var17.L());
                     }

                     var37 = this.o.a(var17.L(), 160);
                  }

                  this.b(var1, var15, var37, Math.max(var5, this.R ? 1.0 : 0.0), var7);
                  return;
               }

               com.larpclient.p.B(var1, this.s(), this.d() + 1, this.R(), 2, 1, ez.D);
               if (var17 == null) {
                  com.larpclient.p.B(var1, this.s(), this.d() + 1, (int)(this.R() * var15), 2, 1, var7);
                  j(var1, this.s() + (int)(this.R() * var15), this.d() + 2, var5);
               } else {
                  if (this.R) {
                     this.o.w(var17.L());
                  }

                  double var18 = this.o.a(var17.L(), 160);
                  int var20 = (int)(this.R() * var15);
                  int var21 = (int)(this.R() * var18);
                  com.larpclient.p.B(var1, this.s() + var20, this.d() + 1, Math.max(0, var21 - var20), 2, 1, var7);
                  j(var1, this.s() + var20, this.d() + 2, var5);
                  j(var1, this.s() + var21, this.d() + 2, var5);
               }
            } else {
               int var32 = this.k();
               int var34 = this.n();
               com.larpclient.p.x(var1, var32, var10 + 2, var34, 20, !this.p && !this.e && var25 == 0 ? var5 * 0.3 : 1.0);
               if (this.s instanceof aj) {
                  this.t.bounds(var32, this.g + 2, var34);
                  this.t.w = 20;
                  if (!this.n) {
                     this.t.h = this.w();
                  }

                  this.t.render(var1, var2, var3);
               } else if (this.s instanceof com.larpclient.module.l0 var16) {
                  uw.G(var1, uw.J(String.format("#%08X", var16.getValue()), var34 - 28), var32 + 6, this.g + 6, ez.PH);
                  com.larpclient.p.B(var1, this.D + this.d - 23, this.g + 6, 14, 12, 3, var16.getValue());
                  ws.A(var1, this.D + this.d - 23, this.g + 6, 14.0F, 12.0F, 3.0F, ei.R(ez.o, 2.0), ei.R(ez.J, 2.5));
               } else {
                  int var36 = this.s instanceof com.larpclient.module.li var38 ? A(var1, var38, var38.getValue(), var32 + 6, var10 + 4) : 0;
                  int var39 = this.s instanceof com.larpclient.module.lw var40 && var40.getValue() < 0 ? 1 : 0;
                  String var41 = this.s instanceof com.larpclient.module.lw var43 && var43.D() ? "Press or click…" : "Press key…";
                  uw.G(var1, uw.J(this.e ? var41 : this.w(), var34 - 22 - var36), var32 + 6 + var36, var10 + 6, this.e ? var7 : (var39 != 0 ? ez.PH : ez.H));
                  if (this.s instanceof com.larpclient.module.li) {
                     lx.N(var1, this.p ? lx.G : lx.s, this.D + this.d - 20, var10 + 5, 14, ez.PH);
                  }
               }
            }
         }
      }
   }

   private void b(class_332 var1, double var2, double var4, double var6, int var8) {
      switch (ez.x().slider()) {
         case "Minimal":
         case "Classic":
            this.H(var1, var2, var4, var6, var8, ez.x().slider().equals("Classic"));
            return;
         default:
            float var23 = this.d() + 2;
            float var24 = this.R();
            float var11 = this.s();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            ws.r(var1, var11, var14, var24, var12, var13, ei.D(ez.H, 0.1));
            ws.A(var1, var11, var14, var24, var12, var13, ei.D(ez.D, 0.5), ei.D(ez.D, 0.5));
            float var16 = var15 != 0 ? (float)(var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)(var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = ei.W(var8, ez.f | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               com.larpclient.p.a(var1, var18, var14, var20, var12, var13, 5.0F, ei.D(var8, 0.35 * var6));
            }

            ws.u(var1, var18, var14, var20, var12, var13, var21, var8);
            ws.F(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               U(var1, var18 + var13, var23, var22, this.R && this.K == 1 ? 1.0 : 0.0, var8);
               U(var1, var18 + var20 - var13, var23, var22, this.R && this.K == 2 ? 1.0 : 0.0, var8);
            } else {
               U(var1, var18 + var20 - var13, var23, var22, this.R ? 1.0 : 0.0, var8);
            }
      }
   }

   private void H(class_332 var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = this.d() + 2;
      float var11 = this.R();
      float var12 = this.s();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      ws.r(var1, var12, var14, var11, var13, var13 / 2.0F, ei.D(ez.H, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)(var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)(var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            com.larpclient.p.a(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, ei.D(var8, 0.4 * var6));
         }

         ws.u(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, ei.W(var8, ez.f | 0xFF000000, 0.6), var8);
      } else {
         ws.r(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
      }

      float var19 = (var9 ? 7 : 6) + 2.0F * (float)var6;
      if (var15 != 0) {
         F(var1, var12 + var16, var10, var19, var8, var9);
      }

      F(var1, var12 + var17, var10, var19, var8, var9);
   }

   private static void F(class_332 var0, float var1, float var2, float var3, int var4, boolean var5) {
      float var6 = var1 - var3 / 2.0F;
      float var7 = var2 - var3 / 2.0F;
      ws.K(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      ws.r(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : ez.d | 0xFF000000);
      ws.X(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void U(class_332 var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      ws.K(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         com.larpclient.p.a(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, ei.D(var6, 0.45 * var4));
      }

      ws.r(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void j(class_332 var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      ws.r(var0, var1 - var5 / 2 - 1, var2 - var5 / 2 - 1, var5 + 2, var5 + 2, var5 / 2.0F + 1.0F, 855638016);
      ws.r(var0, var1 - var5 / 2, var2 - var5 / 2, var5, var5, var5 / 2.0F, -723722);
   }

   public void S(class_332 var1, int var2, int var3) {
      if (this.j()) {
         try (fp var4 = fp.O(this.B.a(1.0, 160))) {
            boolean var5 = ez.d();
            if (var5) {
               com.larpclient.p.N(var1, this.H, this.b, this.k, this.T, ez.X, ez.p, 1.0);
            } else {
               com.larpclient.p.o(var1, this.H, this.b, this.k, this.T);
               com.larpclient.p.W(var1, this.H, this.b, this.k, this.T, ez.m, ez.p, 1.0);
            }

            var1.method_44379(this.H, this.b, this.H + this.k, this.b + this.T);

            try {
               if (this.s instanceof com.larpclient.module.li var6) {
                  if (this.C != null) {
                     this.C.render(var1, var2, var3);
                  }

                  int var27 = this.b + this.o();
                  int var28 = Math.min(this.r.size() - this.L, this.X());
                  int var29 = this.T();
                  if (this.r.isEmpty() && this.X() > 0) {
                     uw.G(var1, "No matching blocks", this.H + 8, var27 + 6, ez.PH);
                  }

                  for (int var30 = 0; var30 < var28; var30++) {
                     String var31 = this.r.get(var30 + this.L);
                     int var33 = var27 + var30 * var29;
                     boolean var34 = gu.X(var2, var3, this.H, var33, this.k, var29);
                     boolean var35 = var6.G(var31);
                     int var16 = !var34 && (this.C == null || this.S != var30 + this.L) ? 0 : 1;
                     if (var5 && var35) {
                        com.larpclient.p.B(var1, this.H + 2, var33 + 1, this.k - 4, var29 - 2, ez.m, ez.i(this.H, var16 != 0 ? 1.3 : 1.0));
                     } else if (var16 != 0 || var35) {
                        com.larpclient.p.B(var1, this.H + 2, var33 + 1, this.k - 4, var29 - 2, ez.m, var5 ? ei.D(ez.H, 0.08) : ez.K);
                     }

                     int var17;
                     if (var6.o() > 0) {
                        var6.m(var1, var31, this.H + 5, var33 + 3, var29 - 6);
                        var17 = var29 - 4;
                     } else {
                        var17 = A(var1, var6, var31, this.H + 8, var33 + 2);
                     }

                     uw.G(
                        var1,
                        uw.J(var6.getModeLabel(var31), this.k - 16 - var17),
                        this.H + 8 + var17,
                        var33 + (var29 - 8) / 2,
                        !var35 && !var34 ? ei.W(ez.PH, ez.H, 0.5) : ez.H
                     );
                  }

                  com.larpclient.p.t(var1, this.H + this.k - 3, var27, this.X() * var29, this.r.size() * var29, this.L * var29);
               } else if (this.s instanceof com.larpclient.module.l0 var7) {
                  float[] var26 = Color.RGBtoHSB(var7.getValue() >> 16 & 0xFF, var7.getValue() >> 8 & 0xFF, var7.getValue() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.k - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.b + 6 + var11 * 18;
                     uw.G(var1, var9[var11], this.H + 5, var12, ez.PH);

                     for (byte var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / var10;

                        int var15 = switch (var11) {
                           case 0 -> ei.X(var14, 1.0F, 1.0F);
                           case 1 -> ei.X(var26[0], var14, var26[2]);
                           case 2 -> ei.X(var26[0], var26[1], var14);
                           default -> ei.W(ez.Z, var7.getValue() | 0xFF000000, var14);
                        };
                        gu.Z(var1, this.H + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var32 = var11 < 3 ? var26[var11] : (var7.getValue() >>> 24) / 255.0F;
                     gu.a(var1, this.H + 19 + (int)(var32 * var10), var12 - 1, 3, 12, ez.H);
                  }
               }
            } finally {
               var1.method_44380();
            }

            if (!var5) {
               ws.A(var1, this.H, this.b, this.k, this.T, ez.m, ei.R(ez.o, 1.5), ei.R(ez.J, 1.5));
            }
         }
      }
   }

   private static int A(class_332 var0, com.larpclient.module.li var1, String var2, int var3, int var4) {
      int var5 = var1.drawModeIcon(var0, var2, var3, var4);
      if (var5 > 0) {
         return var5;
      }

      class_1799 var6 = var1.getModeIcon(var2);
      if (var6.method_7960()) {
         return 0;
      }

      var0.method_51427(var6, var3, var4);
      return 18;
   }

   public boolean b(double var1, double var3, int var5) {
      if (!this.p(var1, var3)) {
         return false;
      }

      if (var5 != 0) {
         return true;
      }

      if (this.s instanceof com.larpclient.module.li var6) {
         if (this.C != null && var3 < this.b + this.o()) {
            this.C.click(var1, var3, var5);
            return true;
         }

         int var8 = (int)(var3 - this.b - this.o()) / this.T();
         if (var8 < 0 || var8 >= this.X() || var8 + this.L >= this.r.size()) {
            return true;
         }

         this.K(var6, this.r.get(var8 + this.L));
         this.f();
         c_.b(c_.d.q);
      } else if (this.s instanceof com.larpclient.module.l0) {
         this.y = Math.clamp((int)(var3 - this.b - 6.0) / 18, 0, 3);
         this.R = (boolean)1;
         this.drag(var1, var3);
      }

      return true;
   }

   private void K(com.larpclient.module.li var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawnerModule var4 = FakeSpawnerModule.i();
         if (var4 == null || !var4.W().contains(var1)) {
            throw var5;
         }

         b1.q(class_2561.method_43470(var5.getMessage()), true);
      }
   }

   public void V(double var1) {
      if (this.s instanceof com.larpclient.module.li) {
         this.L -= (int)var1;
         this.Z(this.X, this.a);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      }

      this.M = (boolean)1;
      if (this.e && (var5 == 0 || var5 == 1) && this.s instanceof com.larpclient.module.lw var6 && var6.D()) {
         if (System.nanoTime() - this.U < 300000000L) {
            return true;
         }

         var6.J(s5.K(var5));
         this.e = (boolean)0;
         c_.b(c_.d.q);
         return true;
      } else if (var5 == 2 && this.h() != null && !this.Y) {
         this.l = (boolean)0;
         this.n = (boolean)0;
         this.e = (boolean)0;
         this.f();
         if (this.t != null) {
            this.t.blur();
         }

         this.Y = (boolean)1;
         c_.b(c_.d.q);
         return true;
      } else if (var5 == 1) {
         this.l = (boolean)0;
         this.n = (boolean)0;
         this.e = (boolean)0;
         this.Y = (boolean)0;
         if (this.t != null) {
            this.t.blur();
         }

         if (this.s instanceof com.larpclient.module.li var12) {
            this.K(var12, var12.O());
         } else {
            this.s.T();
         }

         if (this.A() != null) {
            this.A().T();
         }

         c_.b(c_.d.q);
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.Y) {
            this.Y = (boolean)0;
            return true;
         }

         if (this.s instanceof com.larpclient.module.l3 var11) {
            var11.h();
            c_.b(var11.getValue() ? c_.d.d : c_.d.n);
         } else if (this.s instanceof com.larpclient.module.lo var13) {
            if (this.A() != null) {
               if (var3 >= this.g + 22) {
                  double var16 = this.G(var1);
                  this.K = Math.abs(var16 - var13.getValue()) > Math.abs(var16 - this.A().getValue()) ? 2 : 1;
                  this.R = (boolean)1;
                  this.drag(var1, var3);
               }
            } else if (this.u() ? !(var3 < this.g + 24) || !(var1 >= this.w() - 4) : !(var1 >= this.w() - 4)) {
               if (this.u() ? var3 >= this.g + 22 : var1 >= this.k() - 4) {
                  this.N();
                  this.R = (boolean)1;
                  this.drag(var1, var3);
               }
            } else {
               this.E = var13.getEditValueAsString();
               this.l = (boolean)1;
               this.V = (boolean)1;
            }
         } else if (this.s instanceof com.larpclient.module.li var8) {
            this.p = (boolean)(!this.p ? 1 : 0);
            c_.b(this.p ? c_.d.D : c_.d.g);
            if (this.C != null) {
               this.C.h = "";
               this.C.M = this.p;
            }

            this.r = var8.getVisibleModes();
            this.S = Math.max(0, this.r.indexOf(var8.getValue()));
            this.L = Math.max(0, this.S - 2);
            this.Z(this.X, this.a);
         } else if (this.s instanceof com.larpclient.module.l0) {
            this.p = (boolean)(!this.p ? 1 : 0);
            c_.b(this.p ? c_.d.D : c_.d.g);
         } else if (this.s instanceof aj) {
            this.t.bounds(this.k(), this.g + 2, this.n());
            this.t.w = 20;
            this.t.h = this.w();
            this.n = (boolean)1;
            this.t.click(this.k() + 2, this.g + 4, 0);
            this.t.T();
         } else if (this.s instanceof com.larpclient.module.lw) {
            this.e = (boolean)1;
            this.U = System.nanoTime();
            c_.b(c_.d.q);
         }

         return true;
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.R) {
         if (this.A() != null) {
            if (this.d(this.G(var1))) {
               c_.b(c_.d.E);
            }
         } else if (this.s instanceof com.larpclient.module.lo var5) {
            double var12 = var5.getValue();
            var5.a((var1 - this.s()) / this.R());
            if (var12 != var5.getValue()) {
               c_.b(c_.d.E);
            }
         }

         if (this.s instanceof com.larpclient.module.l0 var11) {
            double var14 = Math.clamp((var1 - this.H - 20.0) / Math.max(1, this.k - 28), 0.0, 1.0);
            int var8 = var11.getValue();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.y < 3) {
               var9[this.y] = (float)var14;
            }

            int var10 = this.y == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(ei.X(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24);
            if (var8 != var11.getValue()) {
               c_.b(c_.d.E);
            }
         }
      }
   }

   private double G(double var1) {
      com.larpclient.module.lo var3 = (com.larpclient.module.lo)this.s;
      return var3.A() + Math.clamp((var1 - this.s()) / this.R(), 0.0, 1.0) * (var3.G() - var3.A());
   }

   private boolean d(double var1) {
      com.larpclient.module.lo var3 = (com.larpclient.module.lo)this.s;
      com.larpclient.module.lo var4 = this.A();
      double var5 = var3.getValue();
      double var7 = var4.getValue();
      if (this.K == 1) {
         var3.q(Math.min(var1, var4.getValue()));
      } else {
         var4.q(Math.max(var1, var3.getValue()));
      }

      return var5 != var3.getValue() || var7 != var4.getValue();
   }

   private void N() {
      if (this.l) {
         try {
            ((com.larpclient.module.lo)this.s).setFromEditString(this.E);
         } catch (IllegalArgumentException var2) {
         }

         this.l = (boolean)0;
      }
   }

   private void w() {
      if (this.n && this.s instanceof aj var1) {
         try {
            var1.Y(this.t.h);
         } catch (IllegalArgumentException var3) {
         }

         this.n = (boolean)0;
         this.t.blur();
      }
   }

   @Override
   public void release() {
      this.R = (boolean)0;
   }

   @Override
   public void blur() {
      super.blur();
      this.e = (boolean)0;
      this.Y = (boolean)0;
      this.f();
      this.N();
      this.w();
   }

   public boolean P() {
      return this.M && (this.l || this.n || this.e || this.Y || this.p && this.C != null);
   }

   @Override
   public boolean captureMouse(int var1) {
      if (this.M && s5.z(var1)) {
         int var2 = s5.K(var1);
         if (this.e && this.s instanceof com.larpclient.module.lw var3) {
            var3.J(var2);
            this.e = (boolean)0;
            c_.b(c_.d.q);
            return true;
         } else if (this.Y) {
            this.Y = (boolean)0;
            this.r(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean character(int var1) {
      if (this.M && this.n) {
         return this.t.character(var1);
      }

      if (this.M && this.p && this.C != null) {
         return this.C.character(var1);
      }

      int var2 = this.s instanceof com.larpclient.module.lo var3 && var3.L() && "kKmMbBtT,$".indexOf(var1) >= 0 ? 1 : 0;
      if (this.M && this.l && ("0123456789.-+eE".indexOf(var1) >= 0 || var2 != 0)) {
         if (this.V) {
            this.E = "";
            this.V = (boolean)0;
         }

         if (this.E.length() < 24) {
            this.E = this.E + (char)var1;
            c_.b(c_.d.Q);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.M) {
         return false;
      }

      if (this.n) {
         if (var1 == 256) {
            this.n = (boolean)0;
            this.t.blur();
            return true;
         }

         if (var1 != 257 && var1 != 335) {
            return this.t.key(var1);
         }

         this.w();
         return true;
      } else {
         if (this.e && this.s instanceof com.larpclient.module.lw var8) {
            var8.J(var1 != 256 && var1 != 261 ? var1 : -1);
            this.e = (boolean)0;
            return true;
         }

         if (this.Y) {
            this.Y = (boolean)0;
            this.r(var1 != 256 && var1 != 261 ? var1 : -1);
            return true;
         }

         if (this.l) {
            if (var1 == 256) {
               this.l = (boolean)0;
            } else if (var1 == 257) {
               this.N();
            } else if (var1 == 259) {
               this.E = !this.V && !this.E.isEmpty() ? this.E.substring(0, this.E.length() - 1) : "";
               this.V = (boolean)0;
            }

            return true;
         } else {
            if (this.p && var1 == 256) {
               this.f();
               return true;
            }

            if (this.p && this.C != null && this.s instanceof com.larpclient.module.li var7) {
               if (var1 == 264 || var1 == 265) {
                  this.S = Math.clamp(this.S + (var1 == 264 ? 1 : -1), 0, Math.max(0, this.r.size() - 1));
                  if (this.S < this.L) {
                     this.L = this.S;
                  } else if (this.S >= this.L + Math.max(1, this.X())) {
                     this.L = this.S - Math.max(1, this.X()) + 1;
                  }

                  this.Z(this.X, this.a);
                  return true;
               } else {
                  if (var1 != 257 && var1 != 335) {
                     return this.C.key(var1);
                  }

                  if (!this.r.isEmpty()) {
                     this.K(var7, this.r.get(this.S));
                     this.f();
                     c_.b(c_.d.q);
                  }

                  return true;
               }
            } else if (this.s instanceof com.larpclient.module.li var2 && this.p && (var1 == 264 || var1 == 265)) {
               List var15 = var2.getVisibleModes();
               int var4 = Math.clamp(var15.indexOf(var2.getValue()) + (var1 == 264 ? 1 : -1), 0, var15.size() - 1);
               this.K(var2, (String)var15.get(var4));
               this.L = Math.max(0, var4 - 2);
               this.Z(this.X, this.a);
               return true;
            } else if (this.s instanceof com.larpclient.module.lo var5 && this.A() != null && (var1 == 262 || var1 == 263)) {
               double var14 = this.K == 1 ? var5.getValue() : this.A().getValue();
               if (this.d(var14 + (var1 == 262 ? 1 : -1) * var5.J())) {
                  c_.b(c_.d.E);
               }

               return true;
            } else if (this.s instanceof com.larpclient.module.lo var6 && (var1 == 262 || var1 == 263)) {
               double var13 = var6.getValue();
               var6.q(var13 + (var1 == 262 ? 1 : -1) * var6.J());
               if (var13 != var6.getValue()) {
                  c_.b(c_.d.E);
               }

               return true;
            } else {
               return var1 != 257 && var1 != 32 ? false : this.click(this.F ? this.D + this.d / 2.0 : this.D + 2, this.g + 2, 0);
            }
         }
      }
   }
}

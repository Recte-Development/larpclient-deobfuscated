package com.larpclient;

import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.Module;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class p9 extends oq {
   private final com.larpclient.module.l6<?> m;
   private final boolean V;
   private boolean g;
   private boolean l;
   private boolean X;
   private boolean d;
   private boolean M;
   private boolean s;
   private boolean P;
   private static final long Q = 300000000L;
   private long a;
   private Module f;
   private boolean u;
   private String k = "";
   private long p;
   private long H;
   private String y;
   private final u7 J = new u7(0.0);
   private final u7 b = new u7(0.0);
   private final u7 n;
   private final u7 w;
   private final u7 N;
   private int Y = 1;
   private int r = -1;
   private int E;
   private int t;
   private int S;
   private int T;
   private int R;
   private int o;
   private int D;
   private int Z;
   private final p_ W;
   private final p_ C;
   private boolean j;
   private List<String> c = List.of();
   private int I;

   public p9(com.larpclient.module.l6<?> var1) {
      this(var1, false);
   }

   public p9(com.larpclient.module.l6<?> var1, boolean var2) {
      this.V = var2;
      this.m = var1;
      this.W = var1 instanceof com.larpclient.module.lp var3 && var3.isSearchable() ? new p_("Search options...", var1x -> this.M(), 96) : null;
      this.C = var1 instanceof com.larpclient.module.li var4 ? new p_(var4.S(), var0 -> {}, 128).T(this::o) : null;
      if (var1 instanceof com.larpclient.module.lp var5) {
         this.c = var5.getVisibleModes();
      }

      this.n = new u7(var1 instanceof com.larpclient.module.ln var6 && var6.getValue() ? 1 : 0);
      this.w = new u7(var1 instanceof com.larpclient.module.lc var7 ? var7.g() : 0.0);
      this.N = new u7(this.f() != null ? this.f().g() : 0.0);
      this.O = this.k() ? 38 : 26;
   }

   public boolean B() {
      return this.V || this.m.R();
   }

   public static p9 O(com.larpclient.module.l6<?> var0) {
      if (!(var0 instanceof com.larpclient.module.ln)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      }

      p9 var1 = new p9(var0);
      var1.g = (boolean)1;
      var1.O = 24;
      return var1;
   }

   public boolean u() {
      return this.g;
   }

   public int X() {
      String var1 = this.G();
      return j_.a(this.m.getDisplayName()) + 20 + (var1 == null ? 0 : x(var1) + 4);
   }

   public String E() {
      if (this.X() == null) {
         return this.m.L();
      }

      int var1 = this.S();
      String var2 = this.m instanceof com.larpclient.module.ln ? "toggle" : "cycle";
      String var3 = var1 == -1
         ? "Middle click to bind a key or mouse button that " + var2 + "s this."
         : "Shortcut: " + pe.A(var1) + ". Middle click to change, then Escape/Delete to clear.";
      return this.m.L() + " " + var3;
   }

   private Module X() {
      if (!this.u) {
         this.u = (boolean)1;
         LarpClient var1 = LarpClient.y();
         if (var1 != null && var1.e() != null && m5.C(this.m)) {
            this.f = m5.r(var1.e().X(), this.m);
         }
      }

      return this.f;
   }

   private int S() {
      Module var1 = this.X();
      return var1 == null ? -1 : LarpClient.y().v().E().B.i(var1, this.m);
   }

   private void t(int var1) {
      Module var2 = this.X();
      if (var2 != null && LarpClient.y().v().c(var2, this.m, var1)) {
         hx.w(hx.t.X);
      }
   }

   private String G() {
      if (this.M) {
         return "Press key…";
      }

      int var1 = this.S();
      return var1 == -1 ? null : pe.A(var1);
   }

   private static int x(String var0) {
      return j_.o(var0, j_.m.U) + 8;
   }

   private void V(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.G();
      if (var7 == null) {
         j_.E(var1, j_.S(this.m.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(x(var7), Math.max(1, var2 / 2));
         String var9 = j_.S(this.m.getDisplayName(), Math.max(1, var2 - var8 - 4));
         j_.E(var1, var9, var3, var4, var5);
         int var10 = var3 + j_.a(var9) + 4;
         ll.N(var1, var10, var4 - 2, var8, 13, 3, oh.i(var5, 0.08));
         j_.L(var1, j_.d(var7, var8 - 8, j_.m.U), var10 + 4, var4, this.M ? var6 : oh.i(var5, 0.7), j_.m.U);
      }
   }

   public boolean y() {
      return this.l && this.F;
   }

   public void n() {
      this.l = (boolean)0;
      this.X = (boolean)0;
      this.b.c(0.0);
      if (this.W != null) {
         this.W.blur();
      }
   }

   private int A() {
      return this.W == null ? 0 : Math.min(24, this.R);
   }

   private int m() {
      return Math.max(0, (this.R - this.A()) / 20);
   }

   private void M() {
      this.c = ((com.larpclient.module.lp)this.m).G(this.W.V);
      this.o = this.I = 0;
      this.Y(this.D, this.Z);
   }

   private int C() {
      return Math.min(148, this.v / 2 - 4);
   }

   private int n() {
      return this.B + this.v - this.C() - 4;
   }

   private boolean k() {
      return this.m instanceof com.larpclient.module.lc var1 && (var1.H() || var1.R() != null);
   }

   private int F() {
      return this.k() ? this.B + 6 : this.n();
   }

   private int l() {
      return this.k() ? this.L + 29 : this.L + 11;
   }

   private int N() {
      return Math.max(1, this.k() ? this.v - 12 : this.C() - this.w() - 14);
   }

   private com.larpclient.module.lc f() {
      return this.m instanceof com.larpclient.module.lc var1 ? var1.R() : null;
   }

   private int w() {
      if (!this.k()) {
         return 44;
      }

      if (Minecraft.getInstance() == null) {
         return 44;
      }

      if (this.r < 0) {
         this.r = Math.clamp(j_.a(((com.larpclient.module.lc)this.m).T()) + 10, 44, 96);
      }

      return this.r;
   }

   private int p() {
      return this.B + this.v - 4 - this.w();
   }

   private String R() {
      if (this.m instanceof com.larpclient.module.lc var8) {
         com.larpclient.module.lc var11 = var8.R();
         long var3 = Double.doubleToLongBits(var8.getValue());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.getValue());
         if (this.y == null || this.p != var3 || this.H != var5) {
            this.y = !var8.H() && var11 == null ? BigDecimal.valueOf(var8.getValue()).stripTrailingZeros().toPlainString() : var8.n();
            this.p = var3;
            this.H = var5;
         }

         return this.y;
      } else if (this.m instanceof com.larpclient.module.la var7) {
         return pe.A(var7.getValue());
      } else {
         return this.m instanceof com.larpclient.module.lp var1 ? var1.getModeLabel(var1.getValue()) : this.m.getValueAsString();
      }
   }

   public void Y(int var1, int var2) {
      if (this.W == null && this.m instanceof com.larpclient.module.lp var3) {
         this.c = var3.getVisibleModes();
      }

      this.D = var1;
      this.Z = var2;
      this.t = this.m instanceof com.larpclient.module.lp ? this.n() : this.B;
      this.T = this.m instanceof com.larpclient.module.lp ? this.C() : this.v;
      int var5 = this.m instanceof com.larpclient.module.lp ? (this.W == null ? 0 : 24) + Math.max(1, Math.min(8, this.c.size())) * 20 : 80;
      this.R = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.b.y(this.l ? 1.0 : 0.0, 160)) * 4.0);
      this.S = Math.clamp(this.L + this.O + var6, var1, Math.max(var1, var2 - this.R));
      if (this.m instanceof com.larpclient.module.lp) {
         this.o = Math.clamp(this.o, 0, Math.max(0, this.c.size() - Math.max(1, this.m())));
      }

      if (this.W != null) {
         this.W.B = this.t + 3;
         this.W.L = this.S + 2;
         this.W.v = Math.max(1, this.T - 6);
         this.W.O = Math.max(0, this.A() - 4);
      }
   }

   public boolean R(double var1, double var3) {
      return this.y() && fr.Q(var1, var3, this.t, this.S, this.T, this.R);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.F) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.J.y(!var4 && !this.A ? 0.0 : 1.0, 150);
         int var7 = jd.readableAccent(rv.m(this.B), rv.w);
         if (this.g && this.m instanceof com.larpclient.module.ln var19) {
            double var21 = this.n.y(var19.getValue() ? 1.0 : 0.0, 150);
            int var25 = jd.readableAccent(rv.m(this.B), rv.H | 0xFF000000);
            int var27 = oh.j(rv.z | 0xFF000000, rv.Z | 0xFF000000, var5);
            int var28 = oh.j(var27, var25, var21);
            int var31 = rv.u() ? this.O / 2 : 7;
            if (var21 > 0.01) {
               ll.S(var1, this.B, this.L, this.v, this.O, var31, 5.0F, oh.i(var25, 0.22 * var21));
            }

            ll.N(var1, this.B, this.L, this.v, this.O, var31, var28);
            ll.u(var1, this.B, this.L, this.v, this.O, var31, oh.j(rv.f, var25, var21));
            if (this.A && this.h) {
               ll.w(var1, this.B, this.L, this.v, this.O, var31);
            }

            this.V(var1, this.v - 16, this.B + 8, this.L + (this.O - 9) / 2, jd.controlInk(var28), var7);
         } else {
            if (var5 > 0.001 && rv.u()) {
               ll.N(var1, this.B, this.L, this.v, this.O, rv.g + 1, oh.i(rv.P, 0.028 * var5));
            }

            this.V(
               var1,
               this.m instanceof com.larpclient.module.ln ? this.v - 42 : (this.k() ? this.v - this.w() - 16 : this.v / 2 - 12),
               this.B + 4,
               this.L + 7,
               rv.P,
               var7
            );
            int var8 = this.A && this.h ? 1 : 0;
            if (this.m instanceof com.larpclient.module.ln var20) {
               if (var8 != 0) {
                  ll.w(var1, this.B + this.v - 28, this.L + 6, 24, 13, 6);
               }

               ll.e(var1, this.B + this.v - 28, this.L + 6, this.n.y(var20.getValue() ? 1.0 : 0.0, 180));
            } else if (this.m instanceof com.larpclient.module.lc var10) {
               ll.j(var1, this.p(), this.L + 3, this.w(), 20, !this.s && var8 == 0 ? var5 * 0.3 : 1.0);
               String var23 = j_.S(this.s ? this.k + "_" : this.R(), this.w() - 10);
               j_.E(var1, var23, this.p() + this.w() - 6 - j_.a(var23), this.L + 7, this.s ? rv.P : oh.j(rv.G0, rv.P, var5));
               if (this.X) {
                  this.w.c(var10.g());
               }

               double var12 = this.w.y(var10.g(), 160);
               com.larpclient.module.lc var14 = this.f();
               if (rv.u()) {
                  double var32 = -1.0;
                  if (var14 != null) {
                     if (this.X) {
                        this.N.c(var14.g());
                     }

                     var32 = this.N.y(var14.g(), 160);
                  }

                  this.X(var1, var12, var32, Math.max(var5, this.X ? 1.0 : 0.0), var7);
                  return;
               }

               ll.N(var1, this.F(), this.l() + 1, this.N(), 2, 1, rv.f);
               if (var14 == null) {
                  ll.N(var1, this.F(), this.l() + 1, (int)(this.N() * var12), 2, 1, var7);
                  P(var1, this.F() + (int)(this.N() * var12), this.l() + 2, var5);
               } else {
                  if (this.X) {
                     this.N.c(var14.g());
                  }

                  double var15 = this.N.y(var14.g(), 160);
                  int var17 = (int)(this.N() * var12);
                  int var18 = (int)(this.N() * var15);
                  ll.N(var1, this.F() + var17, this.l() + 1, Math.max(0, var18 - var17), 2, 1, var7);
                  P(var1, this.F() + var17, this.l() + 2, var5);
                  P(var1, this.F() + var18, this.l() + 2, var5);
               }
            } else {
               int var24 = this.n();
               int var26 = this.C();
               ll.j(var1, var24, this.L + 2, var26, 20, !this.l && !this.d && var8 == 0 ? var5 * 0.3 : 1.0);
               if (this.m instanceof com.larpclient.module.li) {
                  this.C.bounds(var24, this.L + 2, var26);
                  this.C.O = 20;
                  if (!this.j) {
                     this.C.V = this.R();
                  }

                  this.C.render(var1, var2, var3);
               } else if (this.m instanceof com.larpclient.module.lv var13) {
                  j_.E(var1, j_.S(String.format("#%08X", var13.getValue()), var26 - 28), var24 + 6, this.L + 6, rv.G0);
                  ll.N(var1, this.B + this.v - 23, this.L + 6, 14, 12, 3, var13.getValue());
                  ow.y(var1, this.B + this.v - 23, this.L + 6, 14.0F, 12.0F, 3.0F, oh.S(rv.h, 2.0), oh.S(rv.C, 2.5));
               } else {
                  int var30 = this.m instanceof com.larpclient.module.lp var33 ? o(var1, var33, var33.getValue(), var24 + 6, this.L + 4) : 0;
                  int var34 = this.m instanceof com.larpclient.module.la var35 && var35.getValue() < 0 ? 1 : 0;
                  String var36 = this.m instanceof com.larpclient.module.la var38 && var38.b() ? "Press or click…" : "Press key…";
                  j_.E(var1, j_.S(this.d ? var36 : this.R(), var26 - 22 - var30), var24 + 6 + var30, this.L + 6, this.d ? var7 : (var34 != 0 ? rv.G0 : rv.P));
                  if (this.m instanceof com.larpclient.module.lp) {
                     ou.u(var1, this.l ? ou.i : ou.k, this.B + this.v - 20, this.L + 5, 14, rv.G0);
                  }
               }
            }
         }
      }
   }

   private void X(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8) {
      switch (rv.D().slider()) {
         case "Minimal":
         case "Classic":
            this.N(var1, var2, var4, var6, var8, rv.D().slider().equals("Classic"));
            return;
         default:
            float var23 = this.l() + 2;
            float var24 = this.N();
            float var11 = this.F();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            ow.P(var1, var11, var14, var24, var12, var13, oh.i(rv.P, 0.1));
            ow.y(var1, var11, var14, var24, var12, var13, oh.i(rv.f, 0.5), oh.i(rv.f, 0.5));
            float var16 = var15 != 0 ? (float)(var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)(var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = oh.j(var8, rv.GQ | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               ll.S(var1, var18, var14, var20, var12, var13, 5.0F, oh.i(var8, 0.35 * var6));
            }

            ow.Z(var1, var18, var14, var20, var12, var13, var21, var8);
            ow.s(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               a(var1, var18 + var13, var23, var22, this.X && this.Y == 1 ? 1.0 : 0.0, var8);
               a(var1, var18 + var20 - var13, var23, var22, this.X && this.Y == 2 ? 1.0 : 0.0, var8);
            } else {
               a(var1, var18 + var20 - var13, var23, var22, this.X ? 1.0 : 0.0, var8);
            }
      }
   }

   private void N(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = this.l() + 2;
      float var11 = this.N();
      float var12 = this.F();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      ow.P(var1, var12, var14, var11, var13, var13 / 2.0F, oh.i(rv.P, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)(var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)(var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            ll.S(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, oh.i(var8, 0.4 * var6));
         }

         ow.Z(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, oh.j(var8, rv.GQ | 0xFF000000, 0.6), var8);
      } else {
         ow.P(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
      }

      float var19 = (var9 ? 7 : 6) + 2.0F * (float)var6;
      if (var15 != 0) {
         k(var1, var12 + var16, var10, var19, var8, var9);
      }

      k(var1, var12 + var17, var10, var19, var8, var9);
   }

   private static void k(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4, boolean var5) {
      float var6 = var1 - var3 / 2.0F;
      float var7 = var2 - var3 / 2.0F;
      ow.A(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      ow.P(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : rv.w | 0xFF000000);
      ow.A(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void a(GuiGraphicsExtractor var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      ow.A(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         ll.S(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, oh.i(var6, 0.45 * var4));
      }

      ow.P(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void P(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      ow.P(var0, var1 - var5 / 2 - 1, var2 - var5 / 2 - 1, var5 + 2, var5 + 2, var5 / 2.0F + 1.0F, 855638016);
      ow.P(var0, var1 - var5 / 2, var2 - var5 / 2, var5, var5, var5 / 2.0F, -723722);
   }

   public void x(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.y()) {
         try (r var4 = com.larpclient.r.p(this.b.y(1.0, 160))) {
            boolean var5 = rv.u();
            if (var5) {
               ll.w(var1, this.t, this.S, this.T, this.R, rv.N, rv.Y, 1.0);
            } else {
               ll.D(var1, this.t, this.S, this.T, this.R);
               ll.Y(var1, this.t, this.S, this.T, this.R, rv.g, rv.Y, 1.0);
            }

            var1.enableScissor(this.t, this.S, this.t + this.T, this.S + this.R);

            try {
               if (this.m instanceof com.larpclient.module.lp var6) {
                  if (this.W != null) {
                     this.W.render(var1, var2, var3);
                  }

                  int var26 = this.S + this.A();
                  int var27 = Math.min(this.c.size() - this.o, this.m());
                  if (this.c.isEmpty() && this.m() > 0) {
                     j_.E(var1, "No matching blocks", this.t + 8, var26 + 6, rv.G0);
                  }

                  for (int var28 = 0; var28 < var27; var28++) {
                     String var29 = this.c.get(var28 + this.o);
                     int var30 = var26 + var28 * 20;
                     boolean var32 = fr.Q(var2, var3, this.t, var30, this.T, 20);
                     boolean var33 = var6.i(var29);
                     int var34 = !var32 && (this.W == null || this.I != var28 + this.o) ? 0 : 1;
                     if (var5 && var33) {
                        ll.N(var1, this.t + 2, var30 + 1, this.T - 4, 18, rv.g, rv.u(this.t, var34 != 0 ? 1.3 : 1.0));
                     } else if (var34 != 0 || var33) {
                        ll.N(var1, this.t + 2, var30 + 1, this.T - 4, 18, rv.g, var5 ? oh.i(rv.P, 0.08) : rv.Z);
                     }

                     int var16 = o(var1, var6, var29, this.t + 8, var30 + 2);
                     j_.E(
                        var1,
                        j_.S(var6.getModeLabel(var29), this.T - 16 - var16),
                        this.t + 8 + var16,
                        var30 + 6,
                        !var33 && !var32 ? oh.j(rv.G0, rv.P, 0.5) : rv.P
                     );
                  }

                  ll.g(var1, this.t + this.T - 3, var26, this.m() * 20, this.c.size() * 20, this.o * 20);
               } else if (this.m instanceof com.larpclient.module.lv var7) {
                  float[] var25 = Color.RGBtoHSB(var7.getValue() >> 16 & 0xFF, var7.getValue() >> 8 & 0xFF, var7.getValue() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.T - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.S + 6 + var11 * 18;
                     j_.E(var1, var9[var11], this.t + 5, var12, rv.G0);

                     for (byte var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / var10;

                        int var15 = switch (var11) {
                           case 0 -> oh.O(var14, 1.0F, 1.0F);
                           case 1 -> oh.O(var25[0], var14, var25[2]);
                           case 2 -> oh.O(var25[0], var25[1], var14);
                           default -> oh.j(rv.s, var7.getValue() | 0xFF000000, var14);
                        };
                        fr.k(var1, this.t + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var31 = var11 < 3 ? var25[var11] : (var7.getValue() >>> 24) / 255.0F;
                     fr.K(var1, this.t + 19 + (int)(var31 * var10), var12 - 1, 3, 12, rv.P);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            if (!var5) {
               ow.y(var1, this.t, this.S, this.T, this.R, rv.g, oh.S(rv.h, 1.5), oh.S(rv.C, 1.5));
            }
         }
      }
   }

   private static int o(GuiGraphicsExtractor var0, com.larpclient.module.lp var1, String var2, int var3, int var4) {
      int var5 = var1.drawModeIcon(var0, var2, var3, var4);
      if (var5 > 0) {
         return var5;
      }

      ItemStack var6 = var1.getModeIcon(var2);
      if (var6.isEmpty()) {
         return 0;
      }

      var0.item(var6, var3, var4);
      return 18;
   }

   public boolean N(double var1, double var3, int var5) {
      if (!this.R(var1, var3)) {
         return false;
      }

      if (var5 != 0) {
         return true;
      }

      if (this.m instanceof com.larpclient.module.lp var6) {
         if (this.W != null && var3 < this.S + this.A()) {
            this.W.click(var1, var3, var5);
            return true;
         }

         int var8 = (int)(var3 - this.S - this.A()) / 20;
         if (var8 < 0 || var8 >= this.m() || var8 + this.o >= this.c.size()) {
            return true;
         }

         this.k(var6, this.c.get(var8 + this.o));
         this.n();
         hx.w(hx.t.X);
      } else if (this.m instanceof com.larpclient.module.lv) {
         this.E = Math.clamp((int)(var3 - this.S - 6.0) / 18, 0, 3);
         this.X = (boolean)1;
         this.drag(var1, var3);
      }

      return true;
   }

   private void k(com.larpclient.module.lp var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawnerModule var4 = FakeSpawnerModule.w();
         if (var4 == null || !var4.K().contains(var1)) {
            throw var5;
         }

         jb.v(Component.literal(var5.getMessage()), true);
      }
   }

   public void J(double var1) {
      if (this.m instanceof com.larpclient.module.lp) {
         this.o -= (int)var1;
         this.Y(this.D, this.Z);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      }

      this.A = (boolean)1;
      if (this.d && (var5 == 0 || var5 == 1) && this.m instanceof com.larpclient.module.la var6 && var6.b()) {
         if (System.nanoTime() - this.a < 300000000L) {
            return true;
         }

         var6.k(pe.h(var5));
         this.d = (boolean)0;
         hx.w(hx.t.X);
         return true;
      } else if (var5 == 2 && this.X() != null && !this.M) {
         this.s = (boolean)0;
         this.j = (boolean)0;
         this.d = (boolean)0;
         this.n();
         if (this.C != null) {
            this.C.blur();
         }

         this.M = (boolean)1;
         hx.w(hx.t.X);
         return true;
      } else if (var5 == 1) {
         this.s = (boolean)0;
         this.j = (boolean)0;
         this.d = (boolean)0;
         this.M = (boolean)0;
         if (this.C != null) {
            this.C.blur();
         }

         if (this.m instanceof com.larpclient.module.lp var12) {
            this.k(var12, var12.C());
         } else {
            this.m.x();
         }

         if (this.f() != null) {
            this.f().x();
         }

         hx.w(hx.t.X);
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.M) {
            this.M = (boolean)0;
            return true;
         }

         if (this.m instanceof com.larpclient.module.ln var11) {
            var11.S();
            hx.w(var11.getValue() ? hx.t.N : hx.t.F);
         } else if (this.m instanceof com.larpclient.module.lc var13) {
            if (this.f() != null) {
               if (var3 >= this.L + 22) {
                  double var16 = this.v(var1);
                  this.Y = Math.abs(var16 - var13.getValue()) > Math.abs(var16 - this.f().getValue()) ? 2 : 1;
                  this.X = (boolean)1;
                  this.drag(var1, var3);
               }
            } else if (this.k() ? !(var3 < this.L + 24) || !(var1 >= this.p() - 4) : !(var1 >= this.p() - 4)) {
               if (this.k() ? var3 >= this.L + 22 : var1 >= this.n() - 4) {
                  this.B();
                  this.X = (boolean)1;
                  this.drag(var1, var3);
               }
            } else {
               this.k = var13.getEditValueAsString();
               this.s = (boolean)1;
               this.P = (boolean)1;
            }
         } else if (this.m instanceof com.larpclient.module.lp var8) {
            this.l = (boolean)(!this.l ? 1 : 0);
            hx.w(this.l ? hx.t.h : hx.t.S);
            if (this.W != null) {
               this.W.V = "";
               this.W.A = this.l;
            }

            this.c = var8.getVisibleModes();
            this.I = Math.max(0, this.c.indexOf(var8.getValue()));
            this.o = Math.max(0, this.I - 2);
            this.Y(this.D, this.Z);
         } else if (this.m instanceof com.larpclient.module.lv) {
            this.l = (boolean)(!this.l ? 1 : 0);
            hx.w(this.l ? hx.t.h : hx.t.S);
         } else if (this.m instanceof com.larpclient.module.li) {
            this.C.bounds(this.n(), this.L + 2, this.C());
            this.C.O = 20;
            this.C.V = this.R();
            this.j = (boolean)1;
            this.C.click(this.n() + 2, this.L + 4, 0);
            this.C.d();
         } else if (this.m instanceof com.larpclient.module.la) {
            this.d = (boolean)1;
            this.a = System.nanoTime();
            hx.w(hx.t.X);
         }

         return true;
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.X) {
         if (this.f() != null) {
            if (this.z(this.v(var1))) {
               hx.w(hx.t.D);
            }
         } else if (this.m instanceof com.larpclient.module.lc var5) {
            double var12 = var5.getValue();
            var5.o((var1 - this.F()) / this.N());
            if (var12 != var5.getValue()) {
               hx.w(hx.t.D);
            }
         }

         if (this.m instanceof com.larpclient.module.lv var11) {
            double var14 = Math.clamp((var1 - this.t - 20.0) / Math.max(1, this.T - 28), 0.0, 1.0);
            int var8 = var11.getValue();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.E < 3) {
               var9[this.E] = (float)var14;
            }

            int var10 = this.E == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(oh.O(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24);
            if (var8 != var11.getValue()) {
               hx.w(hx.t.D);
            }
         }
      }
   }

   private double v(double var1) {
      com.larpclient.module.lc var3 = (com.larpclient.module.lc)this.m;
      return var3.t() + Math.clamp((var1 - this.F()) / this.N(), 0.0, 1.0) * (var3.T() - var3.t());
   }

   private boolean z(double var1) {
      com.larpclient.module.lc var3 = (com.larpclient.module.lc)this.m;
      com.larpclient.module.lc var4 = this.f();
      double var5 = var3.getValue();
      double var7 = var4.getValue();
      if (this.Y == 1) {
         var3.C(Math.min(var1, var4.getValue()));
      } else {
         var4.C(Math.max(var1, var3.getValue()));
      }

      return var5 != var3.getValue() || var7 != var4.getValue();
   }

   private void B() {
      if (this.s) {
         try {
            ((com.larpclient.module.lc)this.m).setFromEditString(this.k);
         } catch (IllegalArgumentException var2) {
         }

         this.s = (boolean)0;
      }
   }

   private void o() {
      if (this.j && this.m instanceof com.larpclient.module.li var1) {
         try {
            var1.i(this.C.V);
         } catch (IllegalArgumentException var3) {
         }

         this.j = (boolean)0;
         this.C.blur();
      }
   }

   @Override
   public void release() {
      this.X = (boolean)0;
   }

   @Override
   public void blur() {
      super.blur();
      this.d = (boolean)0;
      this.M = (boolean)0;
      this.n();
      this.B();
      this.o();
   }

   public boolean C() {
      return this.A && (this.s || this.j || this.d || this.M || this.l && this.W != null);
   }

   @Override
   public boolean captureMouse(int var1) {
      if (this.A && pe.Y(var1)) {
         int var2 = pe.h(var1);
         if (this.d && this.m instanceof com.larpclient.module.la var3) {
            var3.k(var2);
            this.d = (boolean)0;
            hx.w(hx.t.X);
            return true;
         } else if (this.M) {
            this.M = (boolean)0;
            this.t(var2);
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
      if (this.A && this.j) {
         return this.C.character(var1);
      }

      if (this.A && this.l && this.W != null) {
         return this.W.character(var1);
      }

      if (this.A && this.s && "0123456789.-+eE".indexOf(var1) >= 0) {
         if (this.P) {
            this.k = "";
            this.P = (boolean)0;
         }

         if (this.k.length() < 24) {
            this.k = this.k + (char)var1;
            hx.w(hx.t.i);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.A) {
         return false;
      }

      if (this.j) {
         if (var1 == 256) {
            this.j = (boolean)0;
            this.C.blur();
            return true;
         }

         if (var1 != 257 && var1 != 335) {
            return this.C.key(var1);
         }

         this.o();
         return true;
      } else {
         if (this.d && this.m instanceof com.larpclient.module.la var8) {
            var8.k(var1 != 256 && var1 != 261 ? var1 : -1);
            this.d = (boolean)0;
            return true;
         }

         if (this.M) {
            this.M = (boolean)0;
            this.t(var1 != 256 && var1 != 261 ? var1 : -1);
            return true;
         }

         if (this.s) {
            if (var1 == 256) {
               this.s = (boolean)0;
            } else if (var1 == 257) {
               this.B();
            } else if (var1 == 259) {
               this.k = !this.P && !this.k.isEmpty() ? this.k.substring(0, this.k.length() - 1) : "";
               this.P = (boolean)0;
            }

            return true;
         } else {
            if (this.l && var1 == 256) {
               this.n();
               return true;
            }

            if (this.l && this.W != null && this.m instanceof com.larpclient.module.lp var7) {
               if (var1 == 264 || var1 == 265) {
                  this.I = Math.clamp(this.I + (var1 == 264 ? 1 : -1), 0, Math.max(0, this.c.size() - 1));
                  if (this.I < this.o) {
                     this.o = this.I;
                  } else if (this.I >= this.o + Math.max(1, this.m())) {
                     this.o = this.I - Math.max(1, this.m()) + 1;
                  }

                  this.Y(this.D, this.Z);
                  return true;
               } else {
                  if (var1 != 257 && var1 != 335) {
                     return this.W.key(var1);
                  }

                  if (!this.c.isEmpty()) {
                     this.k(var7, this.c.get(this.I));
                     this.n();
                     hx.w(hx.t.X);
                  }

                  return true;
               }
            } else if (this.m instanceof com.larpclient.module.lp var2 && this.l && (var1 == 264 || var1 == 265)) {
               List var15 = var2.getVisibleModes();
               int var4 = Math.clamp(var15.indexOf(var2.getValue()) + (var1 == 264 ? 1 : -1), 0, var15.size() - 1);
               this.k(var2, (String)var15.get(var4));
               this.o = Math.max(0, var4 - 2);
               this.Y(this.D, this.Z);
               return true;
            } else if (this.m instanceof com.larpclient.module.lc var5 && this.f() != null && (var1 == 262 || var1 == 263)) {
               double var14 = this.Y == 1 ? var5.getValue() : this.f().getValue();
               if (this.z(var14 + (var1 == 262 ? 1 : -1) * var5.P())) {
                  hx.w(hx.t.D);
               }

               return true;
            } else if (this.m instanceof com.larpclient.module.lc var6 && (var1 == 262 || var1 == 263)) {
               double var13 = var6.getValue();
               var6.C(var13 + (var1 == 262 ? 1 : -1) * var6.P());
               if (var13 != var6.getValue()) {
                  hx.w(hx.t.D);
               }

               return true;
            } else {
               return var1 != 257 && var1 != 32 ? false : this.click(this.g ? this.B + this.v / 2.0 : this.B + 2, this.L + 2, 0);
            }
         }
      }
   }
}

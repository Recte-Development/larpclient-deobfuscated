package com.larpclient;

import com.larpclient.module.FakeSpawnerModule;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dk;
import com.larpclient.module.dv;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class un extends b {
   private final df<?> t;
   private final boolean B;
   private boolean g;
   private boolean h;
   private boolean y;
   private boolean m;
   private boolean E;
   private boolean u;
   private boolean T;
   private static final long Z = 300000000L;
   private long f;
   private Module V;
   private boolean a;
   private String e = "";
   private long P;
   private long Q;
   private String F;
   private final ef o = new ef(0.0);
   private final ef i = new ef(0.0);
   private final ef w;
   private final ef s;
   private final ef G;
   private int Y = 1;
   private int H = -1;
   private int r;
   private int U;
   private int j;
   private int v;
   private int M;
   private int O;
   private int x;
   private int K;
   private final ur k;
   private final ur n;
   private boolean c;
   private List<String> S = List.of();
   private int b;

   public un(df<?> var1) {
      this(var1, false);
   }

   public un(df<?> var1, boolean var2) {
      this.B = var2;
      this.t = var1;
      this.k = var1 instanceof com.larpclient.module.d6 var3 && var3.isSearchable() ? new ur("Search options...", var1x -> this.B(), 96) : null;
      this.n = var1 instanceof com.larpclient.module.ds var4 ? new ur(var4.X(), var0 -> {}, 128).R(this::Q) : null;
      if (var1 instanceof com.larpclient.module.d6 var5) {
         this.S = var5.getVisibleModes();
      }

      this.w = new ef(var1 instanceof dh var6 && var6.P() ? 1 : 0);
      this.s = new ef(var1 instanceof com.larpclient.module.d9 var7 ? var7.i() : 0.0);
      this.G = new ef(this.H() != null ? this.H().i() : 0.0);
      this.W = this.Y() ? 38 : 26;
   }

   public boolean x() {
      return this.B || this.t.B();
   }

   public static un o(df<?> var0) {
      if (!(var0 instanceof dh)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      }

      un var1 = new un(var0);
      var1.g = (boolean)1;
      var1.W = 24;
      return var1;
   }

   public boolean U() {
      return this.g;
   }

   public int c() {
      String var1 = this.O();
      return k6.P(this.t.getDisplayName()) + 20 + (var1 == null ? 0 : G(var1) + 4);
   }

   public String P() {
      if (this.w() == null) {
         return this.t.A();
      }

      int var1 = this.i();
      String var2 = this.t instanceof dh ? "toggle" : "cycle";
      String var3 = var1 == -1
         ? "Middle click to bind a key or mouse button that " + var2 + "s this."
         : "Shortcut: " + cc.u(var1) + ". Middle click to change, then Escape/Delete to clear.";
      return this.t.A() + " " + var3;
   }

   private Module w() {
      if (!this.a) {
         this.a = (boolean)1;
         LarpClient var1 = LarpClient.F();
         if (var1 != null && var1.x() != null && um.h(this.t)) {
            this.V = um.A(var1.x().C(), this.t);
         }
      }

      return this.V;
   }

   private int i() {
      Module var1 = this.w();
      return var1 == null ? -1 : LarpClient.F().f().t().s.h(var1, this.t);
   }

   private void y(int var1) {
      Module var2 = this.w();
      if (var2 != null && LarpClient.F().f().s(var2, this.t, var1)) {
         kb.d(kb.n.w);
      }
   }

   private String O() {
      if (this.E) {
         return "Press key…";
      }

      int var1 = this.i();
      return var1 == -1 ? null : cc.u(var1);
   }

   private static int G(String var0) {
      return k6.a(var0, k6.d.K) + 8;
   }

   private void K(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.O();
      if (var7 == null) {
         k6.Z(var1, k6.j(this.t.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(G(var7), Math.max(1, var2 / 2));
         String var9 = k6.j(this.t.getDisplayName(), Math.max(1, var2 - var8 - 4));
         k6.Z(var1, var9, var3, var4, var5);
         int var10 = var3 + k6.P(var9) + 4;
         pn.b(var1, var10, var4 - 2, var8, 13, 3, _3.A(var5, 0.08));
         k6.T(var1, k6.o(var7, var8 - 8, k6.d.K), var10 + 4, var4, this.E ? var6 : _3.A(var5, 0.7), k6.d.K);
      }
   }

   public boolean g() {
      return this.h && this.D;
   }

   public void M() {
      this.h = (boolean)0;
      this.y = (boolean)0;
      this.i.Q(0.0);
      if (this.k != null) {
         this.k.blur();
      }
   }

   private int s() {
      return this.k == null ? 0 : Math.min(24, this.M);
   }

   private int z() {
      return Math.max(0, (this.M - this.s()) / 20);
   }

   private void B() {
      this.S = ((com.larpclient.module.d6)this.t).W(this.k.d);
      this.O = this.b = 0;
      this.N(this.x, this.K);
   }

   private int u() {
      return Math.min(148, this.A / 2 - 4);
   }

   private int N() {
      return this.q + this.A - this.u() - 4;
   }

   private boolean Y() {
      return this.t instanceof com.larpclient.module.d9 var1 && (var1.Z() || var1.m() != null);
   }

   private int y() {
      return this.Y() ? this.q + 6 : this.N();
   }

   private int S() {
      return this.Y() ? this.J + 29 : this.J + 11;
   }

   private int L() {
      return Math.max(1, this.Y() ? this.A - 12 : this.u() - this.V() - 14);
   }

   private com.larpclient.module.d9 H() {
      return this.t instanceof com.larpclient.module.d9 var1 ? var1.m() : null;
   }

   private int V() {
      if (!this.Y()) {
         return 44;
      }

      if (Minecraft.getInstance() == null) {
         return 44;
      }

      if (this.H < 0) {
         this.H = Math.clamp(k6.P(((com.larpclient.module.d9)this.t).h()) + 10, 44, 96);
      }

      return this.H;
   }

   private int m() {
      return this.q + this.A - 4 - this.V();
   }

   private String C() {
      if (this.t instanceof com.larpclient.module.d9 var8) {
         com.larpclient.module.d9 var11 = var8.m();
         long var3 = Double.doubleToLongBits(var8.P());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.P());
         if (this.F == null || this.P != var3 || this.Q != var5) {
            this.F = !var8.Z() && var11 == null ? BigDecimal.valueOf(var8.P()).stripTrailingZeros().toPlainString() : var8.b();
            this.P = var3;
            this.Q = var5;
         }

         return this.F;
      } else if (this.t instanceof dv var7) {
         return cc.u(var7.P());
      } else {
         return this.t instanceof com.larpclient.module.d6 var1 ? var1.getModeLabel(var1.P()) : this.t.getValueAsString();
      }
   }

   public void N(int var1, int var2) {
      if (this.k == null && this.t instanceof com.larpclient.module.d6 var3) {
         this.S = var3.getVisibleModes();
      }

      this.x = var1;
      this.K = var2;
      this.U = this.t instanceof com.larpclient.module.d6 ? this.N() : this.q;
      this.v = this.t instanceof com.larpclient.module.d6 ? this.u() : this.A;
      int var5 = this.t instanceof com.larpclient.module.d6 ? (this.k == null ? 0 : 24) + Math.max(1, Math.min(8, this.S.size())) * 20 : 80;
      this.M = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.i.m(this.h ? 1.0 : 0.0, 160)) * 4.0);
      this.j = Math.clamp(this.J + this.W + var6, var1, Math.max(var1, var2 - this.M));
      if (this.t instanceof com.larpclient.module.d6) {
         this.O = Math.clamp(this.O, 0, Math.max(0, this.S.size() - Math.max(1, this.z())));
      }

      if (this.k != null) {
         this.k.q = this.U + 3;
         this.k.J = this.j + 2;
         this.k.A = Math.max(1, this.v - 6);
         this.k.W = Math.max(0, this.s() - 4);
      }
   }

   public boolean e(double var1, double var3) {
      return this.g() && c7.T(var1, var3, this.U, this.j, this.v, this.M);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.D) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.o.m(!var4 && !this.l ? 0.0 : 1.0, 150);
         int var7 = com.larpclient.n.readableAccent(com.larpclient.f.z(this.q), com.larpclient.f.u);
         if (this.g && this.t instanceof dh var19) {
            double var21 = this.w.m(var19.P() ? 1.0 : 0.0, 150);
            int var25 = com.larpclient.n.readableAccent(com.larpclient.f.z(this.q), com.larpclient.f.i | 0xFF000000);
            int var27 = _3.D(com.larpclient.f.S | 0xFF000000, com.larpclient.f.x | 0xFF000000, var5);
            int var28 = _3.D(var27, var25, var21);
            int var31 = com.larpclient.f.Y() ? this.W / 2 : 7;
            if (var21 > 0.01) {
               pn.Z(var1, this.q, this.J, this.A, this.W, var31, 5.0F, _3.A(var25, 0.22 * var21));
            }

            pn.b(var1, this.q, this.J, this.A, this.W, var31, var28);
            pn.D(var1, this.q, this.J, this.A, this.W, var31, _3.D(com.larpclient.f.I, var25, var21));
            if (this.l && this.R) {
               pn.y(var1, this.q, this.J, this.A, this.W, var31);
            }

            this.K(var1, this.A - 16, this.q + 8, this.J + (this.W - 9) / 2, com.larpclient.n.controlInk(var28), var7);
         } else {
            if (var5 > 0.001 && com.larpclient.f.Y()) {
               pn.b(var1, this.q, this.J, this.A, this.W, com.larpclient.f.aj + 1, _3.A(com.larpclient.f.J, 0.028 * var5));
            }

            this.K(
               var1,
               this.t instanceof dh ? this.A - 42 : (this.Y() ? this.A - this.V() - 16 : this.A / 2 - 12),
               this.q + 4,
               this.J + 7,
               com.larpclient.f.J,
               var7
            );
            int var8 = this.l && this.R ? 1 : 0;
            if (this.t instanceof dh var20) {
               if (var8 != 0) {
                  pn.y(var1, this.q + this.A - 28, this.J + 6, 24, 13, 6);
               }

               pn.Y(var1, this.q + this.A - 28, this.J + 6, this.w.m(var20.P() ? 1.0 : 0.0, 180));
            } else if (this.t instanceof com.larpclient.module.d9 var10) {
               pn.S(var1, this.m(), this.J + 3, this.V(), 20, !this.u && var8 == 0 ? var5 * 0.3 : 1.0);
               String var23 = k6.j(this.u ? this.e + "_" : this.C(), this.V() - 10);
               k6.Z(
                  var1,
                  var23,
                  this.m() + this.V() - 6 - k6.P(var23),
                  this.J + 7,
                  this.u ? com.larpclient.f.J : _3.D(com.larpclient.f.s, com.larpclient.f.J, var5)
               );
               if (this.y) {
                  this.s.Q(var10.i());
               }

               double var12 = this.s.m(var10.i(), 160);
               com.larpclient.module.d9 var14 = this.H();
               if (com.larpclient.f.Y()) {
                  double var32 = -1.0;
                  if (var14 != null) {
                     if (this.y) {
                        this.G.Q(var14.i());
                     }

                     var32 = this.G.m(var14.i(), 160);
                  }

                  this.l(var1, var12, var32, Math.max(var5, this.y ? 1.0 : 0.0), var7);
                  return;
               }

               pn.b(var1, this.y(), this.S() + 1, this.L(), 2, 1, com.larpclient.f.I);
               if (var14 == null) {
                  pn.b(var1, this.y(), this.S() + 1, (int)(this.L() * var12), 2, 1, var7);
                  E(var1, this.y() + (int)(this.L() * var12), this.S() + 2, var5);
               } else {
                  if (this.y) {
                     this.G.Q(var14.i());
                  }

                  double var15 = this.G.m(var14.i(), 160);
                  int var17 = (int)(this.L() * var12);
                  int var18 = (int)(this.L() * var15);
                  pn.b(var1, this.y() + var17, this.S() + 1, Math.max(0, var18 - var17), 2, 1, var7);
                  E(var1, this.y() + var17, this.S() + 2, var5);
                  E(var1, this.y() + var18, this.S() + 2, var5);
               }
            } else {
               int var24 = this.N();
               int var26 = this.u();
               pn.S(var1, var24, this.J + 2, var26, 20, !this.h && !this.m && var8 == 0 ? var5 * 0.3 : 1.0);
               if (this.t instanceof com.larpclient.module.ds) {
                  this.n.bounds(var24, this.J + 2, var26);
                  this.n.W = 20;
                  if (!this.c) {
                     this.n.d = this.C();
                  }

                  this.n.render(var1, var2, var3);
               } else if (this.t instanceof dk var13) {
                  k6.Z(var1, k6.j(String.format("#%08X", var13.P()), var26 - 28), var24 + 6, this.J + 6, com.larpclient.f.s);
                  pn.b(var1, this.q + this.A - 23, this.J + 6, 14, 12, 3, var13.P());
                  tl.V(var1, this.q + this.A - 23, this.J + 6, 14.0F, 12.0F, 3.0F, _3.U(com.larpclient.f.H, 2.0), _3.U(com.larpclient.f.g, 2.5));
               } else {
                  int var30 = this.t instanceof com.larpclient.module.d6 var33 ? U(var1, var33, var33.P(), var24 + 6, this.J + 4) : 0;
                  int var34 = this.t instanceof dv var35 && var35.P() < 0 ? 1 : 0;
                  String var36 = this.t instanceof dv var38 && var38.s() ? "Press or click…" : "Press key…";
                  k6.Z(
                     var1,
                     k6.j(this.m ? var36 : this.C(), var26 - 22 - var30),
                     var24 + 6 + var30,
                     this.J + 6,
                     this.m ? var7 : (var34 != 0 ? com.larpclient.f.s : com.larpclient.f.J)
                  );
                  if (this.t instanceof com.larpclient.module.d6) {
                     tp.N(var1, this.h ? tp.b : tp.U, this.q + this.A - 20, this.J + 5, 14, com.larpclient.f.s);
                  }
               }
            }
         }
      }
   }

   private void l(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8) {
      switch (com.larpclient.f.O().slider()) {
         case "Minimal":
         case "Classic":
            this.w(var1, var2, var4, var6, var8, com.larpclient.f.O().slider().equals("Classic"));
            return;
         default:
            float var23 = this.S() + 2;
            float var24 = this.L();
            float var11 = this.y();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            tl.g(var1, var11, var14, var24, var12, var13, _3.A(com.larpclient.f.J, 0.1));
            tl.V(var1, var11, var14, var24, var12, var13, _3.A(com.larpclient.f.I, 0.5), _3.A(com.larpclient.f.I, 0.5));
            float var16 = var15 != 0 ? (float)(var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)(var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = _3.D(var8, com.larpclient.f.O | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               pn.Z(var1, var18, var14, var20, var12, var13, 5.0F, _3.A(var8, 0.35 * var6));
            }

            tl.A(var1, var18, var14, var20, var12, var13, var21, var8);
            tl.G(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               k(var1, var18 + var13, var23, var22, this.y && this.Y == 1 ? 1.0 : 0.0, var8);
               k(var1, var18 + var20 - var13, var23, var22, this.y && this.Y == 2 ? 1.0 : 0.0, var8);
            } else {
               k(var1, var18 + var20 - var13, var23, var22, this.y ? 1.0 : 0.0, var8);
            }
      }
   }

   private void w(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = this.S() + 2;
      float var11 = this.L();
      float var12 = this.y();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      tl.g(var1, var12, var14, var11, var13, var13 / 2.0F, _3.A(com.larpclient.f.J, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)(var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)(var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            pn.Z(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, _3.A(var8, 0.4 * var6));
         }

         tl.A(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, _3.D(var8, com.larpclient.f.O | 0xFF000000, 0.6), var8);
      } else {
         tl.g(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
      }

      float var19 = (var9 ? 7 : 6) + 2.0F * (float)var6;
      if (var15 != 0) {
         v(var1, var12 + var16, var10, var19, var8, var9);
      }

      v(var1, var12 + var17, var10, var19, var8, var9);
   }

   private static void v(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4, boolean var5) {
      float var6 = var1 - var3 / 2.0F;
      float var7 = var2 - var3 / 2.0F;
      tl.j(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      tl.g(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : com.larpclient.f.u | 0xFF000000);
      tl.M(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void k(GuiGraphicsExtractor var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      tl.j(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         pn.Z(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, _3.A(var6, 0.45 * var4));
      }

      tl.g(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void E(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      tl.g(var0, var1 - var5 / 2 - 1, var2 - var5 / 2 - 1, var5 + 2, var5 + 2, var5 / 2.0F + 1.0F, 855638016);
      tl.g(var0, var1 - var5 / 2, var2 - var5 / 2, var5, var5, var5 / 2.0F, -723722);
   }

   public void M(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.g()) {
         try (de var4 = de.l(this.i.m(1.0, 160))) {
            boolean var5 = com.larpclient.f.Y();
            if (var5) {
               pn.d(var1, this.U, this.j, this.v, this.M, com.larpclient.f.B, com.larpclient.f.c, 1.0);
            } else {
               pn.c(var1, this.U, this.j, this.v, this.M);
               pn.g(var1, this.U, this.j, this.v, this.M, com.larpclient.f.aj, com.larpclient.f.c, 1.0);
            }

            var1.enableScissor(this.U, this.j, this.U + this.v, this.j + this.M);

            try {
               if (this.t instanceof com.larpclient.module.d6 var6) {
                  if (this.k != null) {
                     this.k.render(var1, var2, var3);
                  }

                  int var26 = this.j + this.s();
                  int var27 = Math.min(this.S.size() - this.O, this.z());
                  if (this.S.isEmpty() && this.z() > 0) {
                     k6.Z(var1, "No matching blocks", this.U + 8, var26 + 6, com.larpclient.f.s);
                  }

                  for (int var28 = 0; var28 < var27; var28++) {
                     String var29 = this.S.get(var28 + this.O);
                     int var30 = var26 + var28 * 20;
                     boolean var32 = c7.T(var2, var3, this.U, var30, this.v, 20);
                     boolean var33 = var6.S(var29);
                     int var34 = !var32 && (this.k == null || this.b != var28 + this.O) ? 0 : 1;
                     if (var5 && var33) {
                        pn.b(var1, this.U + 2, var30 + 1, this.v - 4, 18, com.larpclient.f.aj, com.larpclient.f.V(this.U, var34 != 0 ? 1.3 : 1.0));
                     } else if (var34 != 0 || var33) {
                        pn.b(var1, this.U + 2, var30 + 1, this.v - 4, 18, com.larpclient.f.aj, var5 ? _3.A(com.larpclient.f.J, 0.08) : com.larpclient.f.x);
                     }

                     int var16 = U(var1, var6, var29, this.U + 8, var30 + 2);
                     k6.Z(
                        var1,
                        k6.j(var6.getModeLabel(var29), this.v - 16 - var16),
                        this.U + 8 + var16,
                        var30 + 6,
                        !var33 && !var32 ? _3.D(com.larpclient.f.s, com.larpclient.f.J, 0.5) : com.larpclient.f.J
                     );
                  }

                  pn.u(var1, this.U + this.v - 3, var26, this.z() * 20, this.S.size() * 20, this.O * 20);
               } else if (this.t instanceof dk var7) {
                  float[] var25 = Color.RGBtoHSB(var7.P() >> 16 & 0xFF, var7.P() >> 8 & 0xFF, var7.P() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.v - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.j + 6 + var11 * 18;
                     k6.Z(var1, var9[var11], this.U + 5, var12, com.larpclient.f.s);

                     for (byte var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / var10;

                        int var15 = switch (var11) {
                           case 0 -> _3.Z(var14, 1.0F, 1.0F);
                           case 1 -> _3.Z(var25[0], var14, var25[2]);
                           case 2 -> _3.Z(var25[0], var25[1], var14);
                           default -> _3.D(com.larpclient.f.K, var7.P() | 0xFF000000, var14);
                        };
                        c7.s(var1, this.U + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var31 = var11 < 3 ? var25[var11] : (var7.P() >>> 24) / 255.0F;
                     c7.t(var1, this.U + 19 + (int)(var31 * var10), var12 - 1, 3, 12, com.larpclient.f.J);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            if (!var5) {
               tl.V(var1, this.U, this.j, this.v, this.M, com.larpclient.f.aj, _3.U(com.larpclient.f.H, 1.5), _3.U(com.larpclient.f.g, 1.5));
            }
         }
      }
   }

   private static int U(GuiGraphicsExtractor var0, com.larpclient.module.d6 var1, String var2, int var3, int var4) {
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

   public boolean C(double var1, double var3, int var5) {
      if (!this.e(var1, var3)) {
         return false;
      }

      if (var5 != 0) {
         return true;
      }

      if (this.t instanceof com.larpclient.module.d6 var6) {
         if (this.k != null && var3 < this.j + this.s()) {
            this.k.click(var1, var3, var5);
            return true;
         }

         int var8 = (int)(var3 - this.j - this.s()) / 20;
         if (var8 < 0 || var8 >= this.z() || var8 + this.O >= this.S.size()) {
            return true;
         }

         this.O(var6, this.S.get(var8 + this.O));
         this.M();
         kb.d(kb.n.w);
      } else if (this.t instanceof dk) {
         this.r = Math.clamp((int)(var3 - this.j - 6.0) / 18, 0, 3);
         this.y = (boolean)1;
         this.drag(var1, var3);
      }

      return true;
   }

   private void O(com.larpclient.module.d6 var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawnerModule var4 = FakeSpawnerModule.T();
         if (var4 == null || !var4.Q().contains(var1)) {
            throw var5;
         }

         ws.c(Component.literal(var5.getMessage()), true);
      }
   }

   public void z(double var1) {
      if (this.t instanceof com.larpclient.module.d6) {
         this.O -= (int)var1;
         this.N(this.x, this.K);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      }

      this.l = (boolean)1;
      if (this.m && (var5 == 0 || var5 == 1) && this.t instanceof dv var6 && var6.s()) {
         if (System.nanoTime() - this.f < 300000000L) {
            return true;
         }

         var6.T(cc.M(var5));
         this.m = (boolean)0;
         kb.d(kb.n.w);
         return true;
      } else if (var5 == 2 && this.w() != null && !this.E) {
         this.u = (boolean)0;
         this.c = (boolean)0;
         this.m = (boolean)0;
         this.M();
         if (this.n != null) {
            this.n.blur();
         }

         this.E = (boolean)1;
         kb.d(kb.n.w);
         return true;
      } else if (var5 == 1) {
         this.u = (boolean)0;
         this.c = (boolean)0;
         this.m = (boolean)0;
         this.E = (boolean)0;
         if (this.n != null) {
            this.n.blur();
         }

         if (this.t instanceof com.larpclient.module.d6 var12) {
            this.O(var12, var12.a());
         } else {
            this.t.B();
         }

         if (this.H() != null) {
            this.H().B();
         }

         kb.d(kb.n.w);
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.E) {
            this.E = (boolean)0;
            return true;
         }

         if (this.t instanceof dh var11) {
            var11.T();
            kb.d(var11.P() ? kb.n.l : kb.n.O);
         } else if (this.t instanceof com.larpclient.module.d9 var13) {
            if (this.H() != null) {
               if (var3 >= this.J + 22) {
                  double var16 = this.w(var1);
                  this.Y = Math.abs(var16 - var13.P()) > Math.abs(var16 - this.H().P()) ? 2 : 1;
                  this.y = (boolean)1;
                  this.drag(var1, var3);
               }
            } else if (this.Y() ? !(var3 < this.J + 24) || !(var1 >= this.m() - 4) : !(var1 >= this.m() - 4)) {
               if (this.Y() ? var3 >= this.J + 22 : var1 >= this.N() - 4) {
                  this.d();
                  this.y = (boolean)1;
                  this.drag(var1, var3);
               }
            } else {
               this.e = var13.getEditValueAsString();
               this.u = (boolean)1;
               this.T = (boolean)1;
            }
         } else if (this.t instanceof com.larpclient.module.d6 var8) {
            this.h = (boolean)(!this.h ? 1 : 0);
            kb.d(this.h ? kb.n.T : kb.n.z);
            if (this.k != null) {
               this.k.d = "";
               this.k.l = this.h;
            }

            this.S = var8.getVisibleModes();
            this.b = Math.max(0, this.S.indexOf(var8.P()));
            this.O = Math.max(0, this.b - 2);
            this.N(this.x, this.K);
         } else if (this.t instanceof dk) {
            this.h = (boolean)(!this.h ? 1 : 0);
            kb.d(this.h ? kb.n.T : kb.n.z);
         } else if (this.t instanceof com.larpclient.module.ds) {
            this.n.bounds(this.N(), this.J + 2, this.u());
            this.n.W = 20;
            this.n.d = this.C();
            this.c = (boolean)1;
            this.n.click(this.N() + 2, this.J + 4, 0);
            this.n.u();
         } else if (this.t instanceof dv) {
            this.m = (boolean)1;
            this.f = System.nanoTime();
            kb.d(kb.n.w);
         }

         return true;
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.y) {
         if (this.H() != null) {
            if (this.T(this.w(var1))) {
               kb.d(kb.n.m);
            }
         } else if (this.t instanceof com.larpclient.module.d9 var5) {
            double var12 = var5.P();
            var5.O((var1 - this.y()) / this.L());
            if (var12 != var5.P()) {
               kb.d(kb.n.m);
            }
         }

         if (this.t instanceof dk var11) {
            double var14 = Math.clamp((var1 - this.U - 20.0) / Math.max(1, this.v - 28), 0.0, 1.0);
            int var8 = var11.P();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.r < 3) {
               var9[this.r] = (float)var14;
            }

            int var10 = this.r == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(_3.Z(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24);
            if (var8 != var11.P()) {
               kb.d(kb.n.m);
            }
         }
      }
   }

   private double w(double var1) {
      com.larpclient.module.d9 var3 = (com.larpclient.module.d9)this.t;
      return var3.v() + Math.clamp((var1 - this.y()) / this.L(), 0.0, 1.0) * (var3.J() - var3.v());
   }

   private boolean T(double var1) {
      com.larpclient.module.d9 var3 = (com.larpclient.module.d9)this.t;
      com.larpclient.module.d9 var4 = this.H();
      double var5 = var3.P();
      double var7 = var4.P();
      if (this.Y == 1) {
         var3.o(Math.min(var1, var4.P()));
      } else {
         var4.o(Math.max(var1, var3.P()));
      }

      return var5 != var3.P() || var7 != var4.P();
   }

   private void d() {
      if (this.u) {
         try {
            ((com.larpclient.module.d9)this.t).setFromEditString(this.e);
         } catch (IllegalArgumentException var2) {
         }

         this.u = (boolean)0;
      }
   }

   private void Q() {
      if (this.c && this.t instanceof com.larpclient.module.ds var1) {
         try {
            var1.v(this.n.d);
         } catch (IllegalArgumentException var3) {
         }

         this.c = (boolean)0;
         this.n.blur();
      }
   }

   @Override
   public void release() {
      this.y = (boolean)0;
   }

   @Override
   public void blur() {
      super.blur();
      this.m = (boolean)0;
      this.E = (boolean)0;
      this.M();
      this.d();
      this.Q();
   }

   public boolean s() {
      return this.l && (this.u || this.c || this.m || this.E || this.h && this.k != null);
   }

   @Override
   public boolean captureMouse(int var1) {
      if (this.l && cc.P(var1)) {
         int var2 = cc.M(var1);
         if (this.m && this.t instanceof dv var3) {
            var3.T(var2);
            this.m = (boolean)0;
            kb.d(kb.n.w);
            return true;
         } else if (this.E) {
            this.E = (boolean)0;
            this.y(var2);
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
      if (this.l && this.c) {
         return this.n.character(var1);
      }

      if (this.l && this.h && this.k != null) {
         return this.k.character(var1);
      }

      if (this.l && this.u && "0123456789.-+eE".indexOf(var1) >= 0) {
         if (this.T) {
            this.e = "";
            this.T = (boolean)0;
         }

         if (this.e.length() < 24) {
            this.e = this.e + (char)var1;
            kb.d(kb.n.C);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.l) {
         return false;
      }

      if (this.c) {
         if (var1 == 256) {
            this.c = (boolean)0;
            this.n.blur();
            return true;
         }

         if (var1 != 257 && var1 != 335) {
            return this.n.key(var1);
         }

         this.Q();
         return true;
      } else {
         if (this.m && this.t instanceof dv var8) {
            var8.T(var1 != 256 && var1 != 261 ? var1 : -1);
            this.m = (boolean)0;
            return true;
         }

         if (this.E) {
            this.E = (boolean)0;
            this.y(var1 != 256 && var1 != 261 ? var1 : -1);
            return true;
         }

         if (this.u) {
            if (var1 == 256) {
               this.u = (boolean)0;
            } else if (var1 == 257) {
               this.d();
            } else if (var1 == 259) {
               this.e = !this.T && !this.e.isEmpty() ? this.e.substring(0, this.e.length() - 1) : "";
               this.T = (boolean)0;
            }

            return true;
         } else {
            if (this.h && var1 == 256) {
               this.M();
               return true;
            }

            if (this.h && this.k != null && this.t instanceof com.larpclient.module.d6 var7) {
               if (var1 == 264 || var1 == 265) {
                  this.b = Math.clamp(this.b + (var1 == 264 ? 1 : -1), 0, Math.max(0, this.S.size() - 1));
                  if (this.b < this.O) {
                     this.O = this.b;
                  } else if (this.b >= this.O + Math.max(1, this.z())) {
                     this.O = this.b - Math.max(1, this.z()) + 1;
                  }

                  this.N(this.x, this.K);
                  return true;
               } else {
                  if (var1 != 257 && var1 != 335) {
                     return this.k.key(var1);
                  }

                  if (!this.S.isEmpty()) {
                     this.O(var7, this.S.get(this.b));
                     this.M();
                     kb.d(kb.n.w);
                  }

                  return true;
               }
            } else if (this.t instanceof com.larpclient.module.d6 var2 && this.h && (var1 == 264 || var1 == 265)) {
               List var15 = var2.getVisibleModes();
               int var4 = Math.clamp(var15.indexOf(var2.P()) + (var1 == 264 ? 1 : -1), 0, var15.size() - 1);
               this.O(var2, (String)var15.get(var4));
               this.O = Math.max(0, var4 - 2);
               this.N(this.x, this.K);
               return true;
            } else if (this.t instanceof com.larpclient.module.d9 var5 && this.H() != null && (var1 == 262 || var1 == 263)) {
               double var14 = this.Y == 1 ? var5.P() : this.H().P();
               if (this.T(var14 + (var1 == 262 ? 1 : -1) * var5.Q())) {
                  kb.d(kb.n.m);
               }

               return true;
            } else if (this.t instanceof com.larpclient.module.d9 var6 && (var1 == 262 || var1 == 263)) {
               double var13 = var6.P();
               var6.o(var13 + (var1 == 262 ? 1 : -1) * var6.Q());
               if (var13 != var6.P()) {
                  kb.d(kb.n.m);
               }

               return true;
            } else {
               return var1 != 257 && var1 != 32 ? false : this.click(this.g ? this.q + this.A / 2.0 : this.q + 2, this.J + 2, 0);
            }
         }
      }
   }
}

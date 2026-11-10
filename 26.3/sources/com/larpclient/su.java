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

public final class su extends j0 {
   private final com.larpclient.module.r<?> a;
   private final boolean W;
   private boolean E;
   private boolean D;
   private boolean B;
   private boolean K;
   private boolean y;
   private boolean U;
   private boolean g;
   private static final long m = 300000000L;
   private long Q;
   private Module p;
   private boolean Y;
   private String G = "";
   private long w;
   private long z;
   private String H;
   private final fq x = new fq(0.0);
   private final fq l = new fq(0.0);
   private final fq i;
   private final fq f;
   private final fq S;
   private int n = 1;
   private int v = -1;
   private int Z;
   private int s;
   private int A;
   private int r;
   private int q;
   private int d;
   private int k;
   private int C;
   private final sk M;
   private final sk I;
   private boolean t;
   private List<String> c = List.of();
   private int j;

   public su(com.larpclient.module.r<?> var1) {
      this(var1, false);
   }

   public su(com.larpclient.module.r<?> var1, boolean var2) {
      this.W = var2;
      this.a = var1;
      this.M = var1 instanceof com.larpclient.module.o var3 && var3.isSearchable() ? new sk("Search options...", var1x -> this.T(), 96) : null;
      this.I = var1 instanceof com.larpclient.module.e var4 ? new sk(var4.N(), var0 -> {}, 128).O(this::e) : null;
      if (var1 instanceof com.larpclient.module.o var5) {
         this.c = var5.getVisibleModes();
      }

      this.i = new fq(var1 instanceof com.larpclient.module.m var6 && var6.getValue() ? 1 : 0);
      this.f = new fq(var1 instanceof com.larpclient.module.b var7 ? var7.p() : 0.0);
      this.S = new fq(this.h() != null ? this.h().p() : 0.0);
      this.O = this.i() > 0 ? this.i() + 6 : (this.U() ? 38 : 26);
   }

   public boolean a() {
      return this.W || this.a.j();
   }

   public static su t(com.larpclient.module.r<?> var0) {
      if (!(var0 instanceof com.larpclient.module.m)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      }

      su var1 = new su(var0);
      var1.E = (boolean)1;
      var1.O = 24;
      return var1;
   }

   public boolean x() {
      return this.E;
   }

   public int j() {
      String var1 = this.F();
      return ic.H(this.a.getDisplayName()) + 20 + (var1 == null ? 0 : L(var1) + 4);
   }

   public String p() {
      if (this.g() == null) {
         return this.a.I();
      }

      int var1 = this.T();
      String var2 = this.a instanceof com.larpclient.module.m ? "toggle" : "cycle";
      String var3 = var1 == -1
         ? "Middle click to bind a key or mouse button that " + var2 + "s this."
         : "Shortcut: " + p4.n(var1) + ". Middle click to change, then Escape/Delete to clear.";
      return this.a.I() + " " + var3;
   }

   private Module g() {
      if (!this.Y) {
         this.Y = (boolean)1;
         LarpClient var1 = LarpClient.N();
         if (var1 != null && var1.j() != null && b1.d(this.a)) {
            this.p = b1.f(var1.j().d(), this.a);
         }
      }

      return this.p;
   }

   private int T() {
      Module var1 = this.g();
      return var1 == null ? -1 : LarpClient.N().h().k().Q.M(var1, this.a);
   }

   private void S(int var1) {
      Module var2 = this.g();
      if (var2 != null && LarpClient.N().h().V(var2, this.a, var1)) {
         ft.h(ft.e.K);
      }
   }

   private String F() {
      if (this.y) {
         return "Press key…";
      }

      int var1 = this.T();
      return var1 == -1 ? null : p4.n(var1);
   }

   private static int L(String var0) {
      return ic.f(var0, ic.n.I) + 8;
   }

   private void z(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.F();
      if (var7 == null) {
         ic.N(var1, ic.U(this.a.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(L(var7), Math.max(1, var2 / 2));
         String var9 = ic.U(this.a.getDisplayName(), Math.max(1, var2 - var8 - 4));
         ic.N(var1, var9, var3, var4, var5);
         int var10 = var3 + ic.H(var9) + 4;
         wc.b(var1, var10, var4 - 2, var8, 13, 3, vv.N(var5, 0.08));
         ic.N(var1, ic.o(var7, var8 - 8, ic.n.I), var10 + 4, var4, this.y ? var6 : vv.N(var5, 0.7), ic.n.I);
      }
   }

   public boolean n() {
      return this.D && this.R;
   }

   public void O() {
      this.D = (boolean)0;
      this.B = (boolean)0;
      this.l.p(0.0);
      if (this.M != null) {
         this.M.blur();
      }
   }

   private int p() {
      return this.M == null ? 0 : Math.min(24, this.q);
   }

   private int x() {
      return Math.max(0, (this.q - this.p()) / this.A());
   }

   private int i() {
      return this.a instanceof com.larpclient.module.o var1 ? var1.previewSize() : 0;
   }

   private int A() {
      return this.i() > 0 ? 30 : 20;
   }

   private void T() {
      this.c = ((com.larpclient.module.o)this.a).x(this.M.j);
      this.d = this.j = 0;
      this.B(this.k, this.C);
   }

   private int v() {
      return Math.min(148, this.L / 2 - 4);
   }

   private int w() {
      return this.P + this.L - this.v() - 4;
   }

   private boolean U() {
      return this.a instanceof com.larpclient.module.b var1 && (var1.w() || var1.Y() != null);
   }

   private int X() {
      return this.U() ? this.P + 6 : this.w();
   }

   private int u() {
      return this.U() ? this.T + 29 : this.T + 11;
   }

   private int m() {
      return Math.max(1, this.U() ? this.L - 12 : this.v() - this.J() - 14);
   }

   private com.larpclient.module.b h() {
      return this.a instanceof com.larpclient.module.b var1 ? var1.Y() : null;
   }

   private int J() {
      if (!this.U()) {
         return 44;
      }

      if (Minecraft.getInstance() == null) {
         return 44;
      }

      if (this.v < 0) {
         this.v = Math.clamp(ic.H(((com.larpclient.module.b)this.a).D()) + 10, 44, 96);
      }

      return this.v;
   }

   private int V() {
      return this.P + this.L - 4 - this.J();
   }

   private String D() {
      if (this.a instanceof com.larpclient.module.b var8) {
         com.larpclient.module.b var11 = var8.Y();
         long var3 = Double.doubleToLongBits(var8.getValue());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.getValue());
         if (this.H == null || this.w != var3 || this.z != var5) {
            this.H = !var8.w() && var11 == null && !var8.E() ? BigDecimal.valueOf(var8.getValue()).stripTrailingZeros().toPlainString() : var8.O();
            this.w = var3;
            this.z = var5;
         }

         return this.H;
      } else if (this.a instanceof com.larpclient.module.x var7) {
         return p4.n(var7.getValue());
      } else {
         return this.a instanceof com.larpclient.module.o var1 ? var1.getModeLabel(var1.getValue()) : this.a.getValueAsString();
      }
   }

   public void B(int var1, int var2) {
      if (this.M == null && this.a instanceof com.larpclient.module.o var3) {
         this.c = var3.getVisibleModes();
      }

      this.k = var1;
      this.C = var2;
      this.s = this.a instanceof com.larpclient.module.o ? this.w() : this.P;
      this.r = this.a instanceof com.larpclient.module.o ? this.v() : this.L;
      int var5 = this.a instanceof com.larpclient.module.o ? (this.M == null ? 0 : 24) + Math.max(1, Math.min(8, this.c.size())) * this.A() : 80;
      this.q = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.l.n(this.D ? 1.0 : 0.0, 160)) * 4.0);
      this.A = Math.clamp(this.T + this.O + var6, var1, Math.max(var1, var2 - this.q));
      if (this.a instanceof com.larpclient.module.o) {
         this.d = Math.clamp(this.d, 0, Math.max(0, this.c.size() - Math.max(1, this.x())));
      }

      if (this.M != null) {
         this.M.P = this.s + 3;
         this.M.T = this.A + 2;
         this.M.L = Math.max(1, this.r - 6);
         this.M.O = Math.max(0, this.p() - 4);
      }
   }

   public boolean W(double var1, double var3) {
      return this.n() && h5.H(var1, var3, this.s, this.A, this.r, this.q);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.R) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.x.n(!var4 && !this.F ? 0.0 : 1.0, 150);
         int var7 = w4.readableAccent(com.larpclient.c.c(this.P), com.larpclient.c.D);
         if (this.E && this.a instanceof com.larpclient.module.m var22) {
            double var24 = this.i.n(var22.getValue() ? 1.0 : 0.0, 150);
            int var26 = w4.readableAccent(com.larpclient.c.c(this.P), com.larpclient.c.m | 0xFF000000);
            int var28 = vv.Q(com.larpclient.c.R | 0xFF000000, com.larpclient.c.g | 0xFF000000, var5);
            int var29 = vv.Q(var28, var26, var24);
            int var33 = com.larpclient.c.J() ? this.O / 2 : 7;
            if (var24 > 0.01) {
               wc.b(var1, this.P, this.T, this.L, this.O, var33, 5.0F, vv.N(var26, 0.22 * var24));
            }

            wc.b(var1, this.P, this.T, this.L, this.O, var33, var29);
            wc.h(var1, this.P, this.T, this.L, this.O, var33, vv.Q(com.larpclient.c.n, var26, var24));
            if (this.F && this.X) {
               wc.U(var1, this.P, this.T, this.L, this.O, var33);
            }

            this.z(var1, this.L - 16, this.P + 8, this.T + (this.O - 9) / 2, w4.controlInk(var29), var7);
         } else {
            if (var5 > 0.001 && com.larpclient.c.J()) {
               wc.b(var1, this.P, this.T, this.L, this.O, com.larpclient.c.G + 1, vv.N(com.larpclient.c.H, 0.028 * var5));
            }

            int var8 = this.i();
            int var23 = var8 > 0 ? var8 + 8 : 0;
            int var10 = var8 > 0 ? this.T + (this.O - 26) / 2 : this.T;
            if (var8 > 0 && this.a instanceof com.larpclient.module.o var11) {
               var11.drawPreview(var1, var11.getValue(), this.P + 4, this.T + 3, var8);
            }

            this.z(
               var1,
               this.a instanceof com.larpclient.module.m ? this.L - 42 : (this.U() ? this.L - this.J() - 16 : this.L / 2 - 12 - var23),
               this.P + 4 + var23,
               var10 + 7,
               com.larpclient.c.H,
               var7
            );
            int var25 = this.F && this.X ? 1 : 0;
            if (this.a instanceof com.larpclient.module.m var27) {
               if (var25 != 0) {
                  wc.U(var1, this.P + this.L - 28, this.T + 6, 24, 13, 6);
               }

               wc.q(var1, this.P + this.L - 28, this.T + 6, this.i.n(var27.getValue() ? 1.0 : 0.0, 180));
            } else if (this.a instanceof com.larpclient.module.b var13) {
               wc.x(var1, this.V(), this.T + 3, this.J(), 20, !this.U && var25 == 0 ? var5 * 0.3 : 1.0);
               String var31 = ic.U(this.U ? this.G + "_" : this.D(), this.J() - 10);
               ic.N(
                  var1,
                  var31,
                  this.V() + this.J() - 6 - ic.H(var31),
                  this.T + 7,
                  this.U ? com.larpclient.c.H : vv.Q(com.larpclient.c.W, com.larpclient.c.H, var5)
               );
               if (this.B) {
                  this.f.p(var13.p());
               }

               double var15 = this.f.n(var13.p(), 160);
               com.larpclient.module.b var17 = this.h();
               if (com.larpclient.c.J()) {
                  double var37 = -1.0;
                  if (var17 != null) {
                     if (this.B) {
                        this.S.p(var17.p());
                     }

                     var37 = this.S.n(var17.p(), 160);
                  }

                  this.E(var1, var15, var37, Math.max(var5, this.B ? 1.0 : 0.0), var7);
                  return;
               }

               wc.b(var1, this.X(), this.u() + 1, this.m(), 2, 1, com.larpclient.c.n);
               if (var17 == null) {
                  wc.b(var1, this.X(), this.u() + 1, (int)(this.m() * var15), 2, 1, var7);
                  c(var1, this.X() + (int)(this.m() * var15), this.u() + 2, var5);
               } else {
                  if (this.B) {
                     this.S.p(var17.p());
                  }

                  double var18 = this.S.n(var17.p(), 160);
                  int var20 = (int)(this.m() * var15);
                  int var21 = (int)(this.m() * var18);
                  wc.b(var1, this.X() + var20, this.u() + 1, Math.max(0, var21 - var20), 2, 1, var7);
                  c(var1, this.X() + var20, this.u() + 2, var5);
                  c(var1, this.X() + var21, this.u() + 2, var5);
               }
            } else {
               int var32 = this.w();
               int var34 = this.v();
               wc.x(var1, var32, var10 + 2, var34, 20, !this.D && !this.K && var25 == 0 ? var5 * 0.3 : 1.0);
               if (this.a instanceof com.larpclient.module.e) {
                  this.I.bounds(var32, this.T + 2, var34);
                  this.I.O = 20;
                  if (!this.t) {
                     this.I.j = this.D();
                  }

                  this.I.render(var1, var2, var3);
               } else if (this.a instanceof com.larpclient.module.h var16) {
                  ic.N(var1, ic.U(String.format("#%08X", var16.getValue()), var34 - 28), var32 + 6, this.T + 6, com.larpclient.c.W);
                  wc.b(var1, this.P + this.L - 23, this.T + 6, 14, 12, 3, var16.getValue());
                  w1.T(var1, this.P + this.L - 23, this.T + 6, 14.0F, 12.0F, 3.0F, vv.O(com.larpclient.c.Xn, 2.0), vv.O(com.larpclient.c.XZ, 2.5));
               } else {
                  int var36 = this.a instanceof com.larpclient.module.o var38 ? T(var1, var38, var38.getValue(), var32 + 6, var10 + 4) : 0;
                  int var39 = this.a instanceof com.larpclient.module.x var40 && var40.getValue() < 0 ? 1 : 0;
                  String var41 = this.a instanceof com.larpclient.module.x var43 && var43.u() ? "Press or click…" : "Press key…";
                  ic.N(
                     var1,
                     ic.U(this.K ? var41 : this.D(), var34 - 22 - var36),
                     var32 + 6 + var36,
                     var10 + 6,
                     this.K ? var7 : (var39 != 0 ? com.larpclient.c.W : com.larpclient.c.H)
                  );
                  if (this.a instanceof com.larpclient.module.o) {
                     wz.D(var1, this.D ? wz.g : wz.H, this.P + this.L - 20, var10 + 5, 14, com.larpclient.c.W);
                  }
               }
            }
         }
      }
   }

   private void E(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8) {
      switch (com.larpclient.c.t().slider()) {
         case "Minimal":
         case "Classic":
            this.X(var1, var2, var4, var6, var8, com.larpclient.c.t().slider().equals("Classic"));
            return;
         default:
            float var23 = this.u() + 2;
            float var24 = this.m();
            float var11 = this.X();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            w1.a(var1, var11, var14, var24, var12, var13, vv.N(com.larpclient.c.H, 0.1));
            w1.T(var1, var11, var14, var24, var12, var13, vv.N(com.larpclient.c.n, 0.5), vv.N(com.larpclient.c.n, 0.5));
            float var16 = var15 != 0 ? (float)(var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)(var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = vv.Q(var8, com.larpclient.c.t | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               wc.b(var1, var18, var14, var20, var12, var13, 5.0F, vv.N(var8, 0.35 * var6));
            }

            w1.c(var1, var18, var14, var20, var12, var13, var21, var8);
            w1.p(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               k(var1, var18 + var13, var23, var22, this.B && this.n == 1 ? 1.0 : 0.0, var8);
               k(var1, var18 + var20 - var13, var23, var22, this.B && this.n == 2 ? 1.0 : 0.0, var8);
            } else {
               k(var1, var18 + var20 - var13, var23, var22, this.B ? 1.0 : 0.0, var8);
            }
      }
   }

   private void X(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = this.u() + 2;
      float var11 = this.m();
      float var12 = this.X();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      w1.a(var1, var12, var14, var11, var13, var13 / 2.0F, vv.N(com.larpclient.c.H, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)(var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)(var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            wc.b(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, vv.N(var8, 0.4 * var6));
         }

         w1.c(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, vv.Q(var8, com.larpclient.c.t | 0xFF000000, 0.6), var8);
      } else {
         w1.a(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
      }

      float var19 = (var9 ? 7 : 6) + 2.0F * (float)var6;
      if (var15 != 0) {
         t(var1, var12 + var16, var10, var19, var8, var9);
      }

      t(var1, var12 + var17, var10, var19, var8, var9);
   }

   private static void t(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4, boolean var5) {
      float var6 = var1 - var3 / 2.0F;
      float var7 = var2 - var3 / 2.0F;
      w1.h(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      w1.a(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : com.larpclient.c.D | 0xFF000000);
      w1.X(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void k(GuiGraphicsExtractor var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      w1.h(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         wc.b(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, vv.N(var6, 0.45 * var4));
      }

      w1.a(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void c(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      w1.a(var0, var1 - var5 / 2 - 1, var2 - var5 / 2 - 1, var5 + 2, var5 + 2, var5 / 2.0F + 1.0F, 855638016);
      w1.a(var0, var1 - var5 / 2, var2 - var5 / 2, var5, var5, var5 / 2.0F, -723722);
   }

   public void l(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.n()) {
         try (oa var4 = oa.j(this.l.n(1.0, 160))) {
            boolean var5 = com.larpclient.c.J();
            if (var5) {
               wc.g(var1, this.s, this.A, this.r, this.q, com.larpclient.c.X, com.larpclient.c.r, 1.0);
            } else {
               wc.F(var1, this.s, this.A, this.r, this.q);
               wc.t(var1, this.s, this.A, this.r, this.q, com.larpclient.c.G, com.larpclient.c.r, 1.0);
            }

            var1.enableScissor(this.s, this.A, this.s + this.r, this.A + this.q);

            try {
               if (this.a instanceof com.larpclient.module.o var6) {
                  if (this.M != null) {
                     this.M.render(var1, var2, var3);
                  }

                  int var27 = this.A + this.p();
                  int var28 = Math.min(this.c.size() - this.d, this.x());
                  int var29 = this.A();
                  if (this.c.isEmpty() && this.x() > 0) {
                     ic.N(var1, "No matching blocks", this.s + 8, var27 + 6, com.larpclient.c.W);
                  }

                  for (int var30 = 0; var30 < var28; var30++) {
                     String var31 = this.c.get(var30 + this.d);
                     int var33 = var27 + var30 * var29;
                     boolean var34 = h5.H(var2, var3, this.s, var33, this.r, var29);
                     boolean var35 = var6.w(var31);
                     int var16 = !var34 && (this.M == null || this.j != var30 + this.d) ? 0 : 1;
                     if (var5 && var35) {
                        wc.b(var1, this.s + 2, var33 + 1, this.r - 4, var29 - 2, com.larpclient.c.G, com.larpclient.c.Y(this.s, var16 != 0 ? 1.3 : 1.0));
                     } else if (var16 != 0 || var35) {
                        wc.b(var1, this.s + 2, var33 + 1, this.r - 4, var29 - 2, com.larpclient.c.G, var5 ? vv.N(com.larpclient.c.H, 0.08) : com.larpclient.c.g);
                     }

                     int var17;
                     if (var6.previewSize() > 0) {
                        var6.drawPreview(var1, var31, this.s + 5, var33 + 3, var29 - 6);
                        var17 = var29 - 4;
                     } else {
                        var17 = T(var1, var6, var31, this.s + 8, var33 + 2);
                     }

                     ic.N(
                        var1,
                        ic.U(var6.getModeLabel(var31), this.r - 16 - var17),
                        this.s + 8 + var17,
                        var33 + (var29 - 8) / 2,
                        !var35 && !var34 ? vv.Q(com.larpclient.c.W, com.larpclient.c.H, 0.5) : com.larpclient.c.H
                     );
                  }

                  wc.V(var1, this.s + this.r - 3, var27, this.x() * var29, this.c.size() * var29, this.d * var29);
               } else if (this.a instanceof com.larpclient.module.h var7) {
                  float[] var26 = Color.RGBtoHSB(var7.getValue() >> 16 & 0xFF, var7.getValue() >> 8 & 0xFF, var7.getValue() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.r - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.A + 6 + var11 * 18;
                     ic.N(var1, var9[var11], this.s + 5, var12, com.larpclient.c.W);

                     for (byte var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / var10;

                        int var15 = switch (var11) {
                           case 0 -> vv.b(var14, 1.0F, 1.0F);
                           case 1 -> vv.b(var26[0], var14, var26[2]);
                           case 2 -> vv.b(var26[0], var26[1], var14);
                           default -> vv.Q(com.larpclient.c.x, var7.getValue() | 0xFF000000, var14);
                        };
                        h5.N(var1, this.s + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var32 = var11 < 3 ? var26[var11] : (var7.getValue() >>> 24) / 255.0F;
                     h5.L(var1, this.s + 19 + (int)(var32 * var10), var12 - 1, 3, 12, com.larpclient.c.H);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            if (!var5) {
               w1.T(var1, this.s, this.A, this.r, this.q, com.larpclient.c.G, vv.O(com.larpclient.c.Xn, 1.5), vv.O(com.larpclient.c.XZ, 1.5));
            }
         }
      }
   }

   private static int T(GuiGraphicsExtractor var0, com.larpclient.module.o var1, String var2, int var3, int var4) {
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

   public boolean b(double var1, double var3, int var5) {
      if (!this.W(var1, var3)) {
         return false;
      }

      if (var5 != 0) {
         return true;
      }

      if (this.a instanceof com.larpclient.module.o var6) {
         if (this.M != null && var3 < this.A + this.p()) {
            this.M.click(var1, var3, var5);
            return true;
         }

         int var8 = (int)(var3 - this.A - this.p()) / this.A();
         if (var8 < 0 || var8 >= this.x() || var8 + this.d >= this.c.size()) {
            return true;
         }

         this.V(var6, this.c.get(var8 + this.d));
         this.O();
         ft.h(ft.e.K);
      } else if (this.a instanceof com.larpclient.module.h) {
         this.Z = Math.clamp((int)(var3 - this.A - 6.0) / 18, 0, 3);
         this.B = (boolean)1;
         this.drag(var1, var3);
      }

      return true;
   }

   private void V(com.larpclient.module.o var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawnerModule var4 = FakeSpawnerModule.O();
         if (var4 == null || !var4.c().contains(var1)) {
            throw var5;
         }

         iw.V(Component.literal(var5.getMessage()), true);
      }
   }

   public void L(double var1) {
      if (this.a instanceof com.larpclient.module.o) {
         this.d -= (int)var1;
         this.B(this.k, this.C);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      }

      this.F = (boolean)1;
      if (this.K && (var5 == 0 || var5 == 1) && this.a instanceof com.larpclient.module.x var6 && var6.u()) {
         if (System.nanoTime() - this.Q < 300000000L) {
            return true;
         }

         var6.b(p4.f(var5));
         this.K = (boolean)0;
         ft.h(ft.e.K);
         return true;
      } else if (var5 == 2 && this.g() != null && !this.y) {
         this.U = (boolean)0;
         this.t = (boolean)0;
         this.K = (boolean)0;
         this.O();
         if (this.I != null) {
            this.I.blur();
         }

         this.y = (boolean)1;
         ft.h(ft.e.K);
         return true;
      } else if (var5 == 1) {
         this.U = (boolean)0;
         this.t = (boolean)0;
         this.K = (boolean)0;
         this.y = (boolean)0;
         if (this.I != null) {
            this.I.blur();
         }

         if (this.a instanceof com.larpclient.module.o var12) {
            this.V(var12, var12.f());
         } else {
            this.a.u();
         }

         if (this.h() != null) {
            this.h().u();
         }

         ft.h(ft.e.K);
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.y) {
            this.y = (boolean)0;
            return true;
         }

         if (this.a instanceof com.larpclient.module.m var11) {
            var11.q();
            ft.h(var11.getValue() ? ft.e.y : ft.e.J);
         } else if (this.a instanceof com.larpclient.module.b var13) {
            if (this.h() != null) {
               if (var3 >= this.T + 22) {
                  double var16 = this.W(var1);
                  this.n = Math.abs(var16 - var13.getValue()) > Math.abs(var16 - this.h().getValue()) ? 2 : 1;
                  this.B = (boolean)1;
                  this.drag(var1, var3);
               }
            } else if (this.U() ? !(var3 < this.T + 24) || !(var1 >= this.V() - 4) : !(var1 >= this.V() - 4)) {
               if (this.U() ? var3 >= this.T + 22 : var1 >= this.w() - 4) {
                  this.H();
                  this.B = (boolean)1;
                  this.drag(var1, var3);
               }
            } else {
               this.G = var13.getEditValueAsString();
               this.U = (boolean)1;
               this.g = (boolean)1;
            }
         } else if (this.a instanceof com.larpclient.module.o var8) {
            this.D = (boolean)(!this.D ? 1 : 0);
            ft.h(this.D ? ft.e.Q : ft.e.X);
            if (this.M != null) {
               this.M.j = "";
               this.M.F = this.D;
            }

            this.c = var8.getVisibleModes();
            this.j = Math.max(0, this.c.indexOf(var8.getValue()));
            this.d = Math.max(0, this.j - 2);
            this.B(this.k, this.C);
         } else if (this.a instanceof com.larpclient.module.h) {
            this.D = (boolean)(!this.D ? 1 : 0);
            ft.h(this.D ? ft.e.Q : ft.e.X);
         } else if (this.a instanceof com.larpclient.module.e) {
            this.I.bounds(this.w(), this.T + 2, this.v());
            this.I.O = 20;
            this.I.j = this.D();
            this.t = (boolean)1;
            this.I.click(this.w() + 2, this.T + 4, 0);
            this.I.d();
         } else if (this.a instanceof com.larpclient.module.x) {
            this.K = (boolean)1;
            this.Q = System.nanoTime();
            ft.h(ft.e.K);
         }

         return true;
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.B) {
         if (this.h() != null) {
            if (this.t(this.W(var1))) {
               ft.h(ft.e.C);
            }
         } else if (this.a instanceof com.larpclient.module.b var5) {
            double var12 = var5.getValue();
            var5.I((var1 - this.X()) / this.m());
            if (var12 != var5.getValue()) {
               ft.h(ft.e.C);
            }
         }

         if (this.a instanceof com.larpclient.module.h var11) {
            double var14 = Math.clamp((var1 - this.s - 20.0) / Math.max(1, this.r - 28), 0.0, 1.0);
            int var8 = var11.getValue();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.Z < 3) {
               var9[this.Z] = (float)var14;
            }

            int var10 = this.Z == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(vv.b(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24);
            if (var8 != var11.getValue()) {
               ft.h(ft.e.C);
            }
         }
      }
   }

   private double W(double var1) {
      com.larpclient.module.b var3 = (com.larpclient.module.b)this.a;
      return var3.e() + Math.clamp((var1 - this.X()) / this.m(), 0.0, 1.0) * (var3.C() - var3.e());
   }

   private boolean t(double var1) {
      com.larpclient.module.b var3 = (com.larpclient.module.b)this.a;
      com.larpclient.module.b var4 = this.h();
      double var5 = var3.getValue();
      double var7 = var4.getValue();
      if (this.n == 1) {
         var3.O(Math.min(var1, var4.getValue()));
      } else {
         var4.O(Math.max(var1, var3.getValue()));
      }

      return var5 != var3.getValue() || var7 != var4.getValue();
   }

   private void H() {
      if (this.U) {
         try {
            ((com.larpclient.module.b)this.a).setFromEditString(this.G);
         } catch (IllegalArgumentException var2) {
         }

         this.U = (boolean)0;
      }
   }

   private void e() {
      if (this.t && this.a instanceof com.larpclient.module.e var1) {
         try {
            var1.T(this.I.j);
         } catch (IllegalArgumentException var3) {
         }

         this.t = (boolean)0;
         this.I.blur();
      }
   }

   @Override
   public void release() {
      this.B = (boolean)0;
   }

   @Override
   public void blur() {
      super.blur();
      this.K = (boolean)0;
      this.y = (boolean)0;
      this.O();
      this.H();
      this.e();
   }

   public boolean T() {
      return this.F && (this.U || this.t || this.K || this.y || this.D && this.M != null);
   }

   @Override
   public boolean captureMouse(int var1) {
      if (this.F && p4.P(var1)) {
         int var2 = p4.f(var1);
         if (this.K && this.a instanceof com.larpclient.module.x var3) {
            var3.b(var2);
            this.K = (boolean)0;
            ft.h(ft.e.K);
            return true;
         } else if (this.y) {
            this.y = (boolean)0;
            this.S(var2);
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
      if (this.F && this.t) {
         return this.I.character(var1);
      }

      if (this.F && this.D && this.M != null) {
         return this.M.character(var1);
      }

      int var2 = this.a instanceof com.larpclient.module.b var3 && var3.E() && "kKmMbBtT,$".indexOf(var1) >= 0 ? 1 : 0;
      if (this.F && this.U && ("0123456789.-+eE".indexOf(var1) >= 0 || var2 != 0)) {
         if (this.g) {
            this.G = "";
            this.g = (boolean)0;
         }

         if (this.G.length() < 24) {
            this.G = this.G + (char)var1;
            ft.h(ft.e.t);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.F) {
         return false;
      }

      if (this.t) {
         if (var1 == 256) {
            this.t = (boolean)0;
            this.I.blur();
            return true;
         }

         if (var1 != 257 && var1 != 335) {
            return this.I.key(var1);
         }

         this.e();
         return true;
      } else {
         if (this.K && this.a instanceof com.larpclient.module.x var8) {
            var8.b(var1 != 256 && var1 != 261 ? var1 : -1);
            this.K = (boolean)0;
            return true;
         }

         if (this.y) {
            this.y = (boolean)0;
            this.S(var1 != 256 && var1 != 261 ? var1 : -1);
            return true;
         }

         if (this.U) {
            if (var1 == 256) {
               this.U = (boolean)0;
            } else if (var1 == 257) {
               this.H();
            } else if (var1 == 259) {
               this.G = !this.g && !this.G.isEmpty() ? this.G.substring(0, this.G.length() - 1) : "";
               this.g = (boolean)0;
            }

            return true;
         } else {
            if (this.D && var1 == 256) {
               this.O();
               return true;
            }

            if (this.D && this.M != null && this.a instanceof com.larpclient.module.o var7) {
               if (var1 == 264 || var1 == 265) {
                  this.j = Math.clamp(this.j + (var1 == 264 ? 1 : -1), 0, Math.max(0, this.c.size() - 1));
                  if (this.j < this.d) {
                     this.d = this.j;
                  } else if (this.j >= this.d + Math.max(1, this.x())) {
                     this.d = this.j - Math.max(1, this.x()) + 1;
                  }

                  this.B(this.k, this.C);
                  return true;
               } else {
                  if (var1 != 257 && var1 != 335) {
                     return this.M.key(var1);
                  }

                  if (!this.c.isEmpty()) {
                     this.V(var7, this.c.get(this.j));
                     this.O();
                     ft.h(ft.e.K);
                  }

                  return true;
               }
            } else if (this.a instanceof com.larpclient.module.o var2 && this.D && (var1 == 264 || var1 == 265)) {
               List var15 = var2.getVisibleModes();
               int var4 = Math.clamp(var15.indexOf(var2.getValue()) + (var1 == 264 ? 1 : -1), 0, var15.size() - 1);
               this.V(var2, (String)var15.get(var4));
               this.d = Math.max(0, var4 - 2);
               this.B(this.k, this.C);
               return true;
            } else if (this.a instanceof com.larpclient.module.b var5 && this.h() != null && (var1 == 262 || var1 == 263)) {
               double var14 = this.n == 1 ? var5.getValue() : this.h().getValue();
               if (this.t(var14 + (var1 == 262 ? 1 : -1) * var5.g())) {
                  ft.h(ft.e.C);
               }

               return true;
            } else if (this.a instanceof com.larpclient.module.b var6 && (var1 == 262 || var1 == 263)) {
               double var13 = var6.getValue();
               var6.O(var13 + (var1 == 262 ? 1 : -1) * var6.g());
               if (var13 != var6.getValue()) {
                  ft.h(ft.e.C);
               }

               return true;
            } else {
               return var1 != 257 && var1 != 32 ? false : this.click(this.E ? this.P + this.L / 2.0 : this.P + 2, this.T + 2, 0);
            }
         }
      }
   }
}

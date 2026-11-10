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

public final class ju extends jd {
   private final com.larpclient.module.q<?> K;
   private final boolean D;
   private boolean e;
   private boolean O;
   private boolean H;
   private boolean Y;
   private boolean a;
   private boolean k;
   private boolean n;
   private static final long y = 300000000L;
   private long R;
   private Module b;
   private boolean q;
   private String h = "";
   private long V;
   private long P;
   private String G;
   private final jp L = new jp(0.0);
   private final jp c = new jp(0.0);
   private final jp Q;
   private final jp v;
   private final jp M;
   private int s = 1;
   private int B = -1;
   private int N;
   private int d;
   private int t;
   private int r;
   private int E;
   private int U;
   private int w;
   private int m;
   private final j9 j;
   private final j9 I;
   private boolean C;
   private List<String> Z = List.of();
   private int o;

   public ju(com.larpclient.module.q<?> var1) {
      this(var1, false);
   }

   public ju(com.larpclient.module.q<?> var1, boolean var2) {
      this.D = var2;
      this.K = var1;
      this.j = var1 instanceof com.larpclient.module.l var3 && var3.isSearchable() ? new j9("Search options...", var1x -> this.U(), 96) : null;
      this.I = var1 instanceof com.larpclient.module.g var4 ? new j9(var4.a(), var0 -> {}, 128).B(this::a) : null;
      if (var1 instanceof com.larpclient.module.l var5) {
         this.Z = var5.getVisibleModes();
      }

      this.Q = new jp(var1 instanceof com.larpclient.module.s var6 && var6.getValue() ? 1 : 0);
      this.v = new jp(var1 instanceof com.larpclient.module.e var7 ? var7.I() : 0.0);
      this.M = new jp(this.o() != null ? this.o().I() : 0.0);
      this.J = this.K() > 0 ? this.K() + 6 : (this.C() ? 38 : 26);
   }

   public boolean d() {
      return this.D || this.K.W();
   }

   public static ju v(com.larpclient.module.q<?> var0) {
      if (!(var0 instanceof com.larpclient.module.s)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      }

      ju var1 = new ju(var0);
      var1.e = (boolean)1;
      var1.J = 24;
      return var1;
   }

   public boolean u() {
      return this.e;
   }

   public int j() {
      String var1 = this.A();
      return bj.u(this.K.getDisplayName()) + 20 + (var1 == null ? 0 : u(var1) + 4);
   }

   public String G() {
      if (this.N() == null) {
         return this.K.I();
      }

      int var1 = this.F();
      String var2 = this.K instanceof com.larpclient.module.s ? "toggle" : "cycle";
      String var3 = var1 == -1
         ? "Middle click to bind a key or mouse button that " + var2 + "s this."
         : "Shortcut: " + bc.G(var1) + ". Middle click to change, then Escape/Delete to clear.";
      return this.K.I() + " " + var3;
   }

   private Module N() {
      if (!this.q) {
         this.q = (boolean)1;
         LarpClient var1 = LarpClient.R();
         if (var1 != null && var1.z() != null && wr.l(this.K)) {
            this.b = wr.i(var1.z().h(), this.K);
         }
      }

      return this.b;
   }

   private int F() {
      Module var1 = this.N();
      return var1 == null ? -1 : LarpClient.R().o().g().K.o(var1, this.K);
   }

   private void i(int var1) {
      Module var2 = this.N();
      if (var2 != null && LarpClient.R().o().m(var2, this.K, var1)) {
         sr.v(sr.e.q);
      }
   }

   private String A() {
      if (this.a) {
         return "Press key…";
      }

      int var1 = this.F();
      return var1 == -1 ? null : bc.G(var1);
   }

   private static int u(String var0) {
      return bj.e(var0, bj.u.M) + 8;
   }

   private void T(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.A();
      if (var7 == null) {
         bj.E(var1, bj.T(this.K.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(u(var7), Math.max(1, var2 / 2));
         String var9 = bj.T(this.K.getDisplayName(), Math.max(1, var2 - var8 - 4));
         bj.E(var1, var9, var3, var4, var5);
         int var10 = var3 + bj.u(var9) + 4;
         wp.I(var1, var10, var4 - 2, var8, 13, 3, uh.H(var5, 0.08));
         bj.k(var1, bj.i(var7, var8 - 8, bj.u.M), var10 + 4, var4, this.a ? var6 : uh.H(var5, 0.7), bj.u.M);
      }
   }

   public boolean m() {
      return this.O && this.i;
   }

   public void d() {
      this.O = (boolean)0;
      this.H = (boolean)0;
      this.c.F(0.0);
      if (this.j != null) {
         this.j.blur();
      }
   }

   private int P() {
      return this.j == null ? 0 : Math.min(24, this.E);
   }

   private int r() {
      return Math.max(0, (this.E - this.P()) / this.N());
   }

   private int K() {
      return this.K instanceof com.larpclient.module.l var1 ? var1.previewSize() : 0;
   }

   private int N() {
      return this.K() > 0 ? 30 : 20;
   }

   private void U() {
      this.Z = ((com.larpclient.module.l)this.K).b(this.j.H);
      this.U = this.o = 0;
      this.q(this.w, this.m);
   }

   private int Y() {
      return Math.min(148, this.A / 2 - 4);
   }

   private int Q() {
      return this.S + this.A - this.Y() - 4;
   }

   private boolean C() {
      return this.K instanceof com.larpclient.module.e var1 && (var1.M() || var1.w() != null);
   }

   private int c() {
      return this.C() ? this.S + 6 : this.Q();
   }

   private int q() {
      return this.C() ? this.u + 29 : this.u + 11;
   }

   private int o() {
      return Math.max(1, this.C() ? this.A - 12 : this.Y() - this.p() - 14);
   }

   private com.larpclient.module.e o() {
      return this.K instanceof com.larpclient.module.e var1 ? var1.w() : null;
   }

   private int p() {
      if (!this.C()) {
         return 44;
      }

      if (Minecraft.getInstance() == null) {
         return 44;
      }

      if (this.B < 0) {
         this.B = Math.clamp(bj.u(((com.larpclient.module.e)this.K).W()) + 10, 44, 96);
      }

      return this.B;
   }

   private int A() {
      return this.S + this.A - 4 - this.p();
   }

   private String a() {
      if (this.K instanceof com.larpclient.module.e var8) {
         com.larpclient.module.e var11 = var8.w();
         long var3 = Double.doubleToLongBits(var8.getValue());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.getValue());
         if (this.G == null || this.V != var3 || this.P != var5) {
            this.G = !var8.M() && var11 == null && !var8.L() ? BigDecimal.valueOf(var8.getValue()).stripTrailingZeros().toPlainString() : var8.i();
            this.V = var3;
            this.P = var5;
         }

         return this.G;
      } else if (this.K instanceof com.larpclient.module.v var7) {
         return bc.G(var7.getValue());
      } else {
         return this.K instanceof com.larpclient.module.l var1 ? var1.getModeLabel(var1.getValue()) : this.K.getValueAsString();
      }
   }

   public void q(int var1, int var2) {
      if (this.j == null && this.K instanceof com.larpclient.module.l var3) {
         this.Z = var3.getVisibleModes();
      }

      this.w = var1;
      this.m = var2;
      this.d = this.K instanceof com.larpclient.module.l ? this.Q() : this.S;
      this.r = this.K instanceof com.larpclient.module.l ? this.Y() : this.A;
      int var5 = this.K instanceof com.larpclient.module.l ? (this.j == null ? 0 : 24) + Math.max(1, Math.min(8, this.Z.size())) * this.N() : 80;
      this.E = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.c.k(this.O ? 1.0 : 0.0, 160)) * 4.0);
      this.t = Math.clamp(this.u + this.J + var6, var1, Math.max(var1, var2 - this.E));
      if (this.K instanceof com.larpclient.module.l) {
         this.U = Math.clamp(this.U, 0, Math.max(0, this.Z.size() - Math.max(1, this.r())));
      }

      if (this.j != null) {
         this.j.S = this.d + 3;
         this.j.u = this.t + 2;
         this.j.A = Math.max(1, this.r - 6);
         this.j.J = Math.max(0, this.P() - 4);
      }
   }

   public boolean y(double var1, double var3) {
      return this.m() && kv.f(var1, var3, this.d, this.t, this.r, this.E);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.i) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.L.k(!var4 && !this.T ? 0.0 : 1.0, 150);
         int var7 = lv.readableAccent(ho.Q(this.S), ho.A);
         if (this.e && this.K instanceof com.larpclient.module.s var22) {
            double var24 = this.Q.k(var22.getValue() ? 1.0 : 0.0, 150);
            int var26 = lv.readableAccent(ho.Q(this.S), ho.f | 0xFF000000);
            int var28 = uh.q(ho.B | 0xFF000000, ho.e | 0xFF000000, var5);
            int var29 = uh.q(var28, var26, var24);
            int var33 = ho.S() ? this.J / 2 : 7;
            if (var24 > 0.01) {
               wp.e(var1, this.S, this.u, this.A, this.J, var33, 5.0F, uh.H(var26, 0.22 * var24));
            }

            wp.I(var1, this.S, this.u, this.A, this.J, var33, var29);
            wp.q(var1, this.S, this.u, this.A, this.J, var33, uh.q(ho.c, var26, var24));
            if (this.T && this.W) {
               wp.t(var1, this.S, this.u, this.A, this.J, var33);
            }

            this.T(var1, this.A - 16, this.S + 8, this.u + (this.J - 9) / 2, lv.controlInk(var29), var7);
         } else {
            if (var5 > 0.001 && ho.S()) {
               wp.I(var1, this.S, this.u, this.A, this.J, ho.Q + 1, uh.H(ho.u, 0.028 * var5));
            }

            int var8 = this.K();
            int var23 = var8 > 0 ? var8 + 8 : 0;
            int var10 = var8 > 0 ? this.u + (this.J - 26) / 2 : this.u;
            if (var8 > 0 && this.K instanceof com.larpclient.module.l var11) {
               var11.drawPreview(var1, var11.getValue(), this.S + 4, this.u + 3, var8);
            }

            this.T(
               var1,
               this.K instanceof com.larpclient.module.s ? this.A - 42 : (this.C() ? this.A - this.p() - 16 : this.A / 2 - 12 - var23),
               this.S + 4 + var23,
               var10 + 7,
               ho.u,
               var7
            );
            int var25 = this.T && this.W ? 1 : 0;
            if (this.K instanceof com.larpclient.module.s var27) {
               if (var25 != 0) {
                  wp.t(var1, this.S + this.A - 28, this.u + 6, 24, 13, 6);
               }

               wp.E(var1, this.S + this.A - 28, this.u + 6, this.Q.k(var27.getValue() ? 1.0 : 0.0, 180));
            } else if (this.K instanceof com.larpclient.module.e var13) {
               wp.u(var1, this.A(), this.u + 3, this.p(), 20, !this.k && var25 == 0 ? var5 * 0.3 : 1.0);
               String var31 = bj.T(this.k ? this.h + "_" : this.a(), this.p() - 10);
               bj.E(var1, var31, this.A() + this.p() - 6 - bj.u(var31), this.u + 7, this.k ? ho.u : uh.q(ho.Z, ho.u, var5));
               if (this.H) {
                  this.v.F(var13.I());
               }

               double var15 = this.v.k(var13.I(), 160);
               com.larpclient.module.e var17 = this.o();
               if (ho.S()) {
                  double var37 = -1.0;
                  if (var17 != null) {
                     if (this.H) {
                        this.M.F(var17.I());
                     }

                     var37 = this.M.k(var17.I(), 160);
                  }

                  this.K(var1, var15, var37, Math.max(var5, this.H ? 1.0 : 0.0), var7);
                  return;
               }

               wp.I(var1, this.c(), this.q() + 1, this.o(), 2, 1, ho.c);
               if (var17 == null) {
                  wp.I(var1, this.c(), this.q() + 1, (int)(this.o() * var15), 2, 1, var7);
                  j(var1, this.c() + (int)(this.o() * var15), this.q() + 2, var5);
               } else {
                  if (this.H) {
                     this.M.F(var17.I());
                  }

                  double var18 = this.M.k(var17.I(), 160);
                  int var20 = (int)(this.o() * var15);
                  int var21 = (int)(this.o() * var18);
                  wp.I(var1, this.c() + var20, this.q() + 1, Math.max(0, var21 - var20), 2, 1, var7);
                  j(var1, this.c() + var20, this.q() + 2, var5);
                  j(var1, this.c() + var21, this.q() + 2, var5);
               }
            } else {
               int var32 = this.Q();
               int var34 = this.Y();
               wp.u(var1, var32, var10 + 2, var34, 20, !this.O && !this.Y && var25 == 0 ? var5 * 0.3 : 1.0);
               if (this.K instanceof com.larpclient.module.g) {
                  this.I.bounds(var32, this.u + 2, var34);
                  this.I.J = 20;
                  if (!this.C) {
                     this.I.H = this.a();
                  }

                  this.I.render(var1, var2, var3);
               } else if (this.K instanceof com.larpclient.module.i var16) {
                  bj.E(var1, bj.T(String.format("#%08X", var16.getValue()), var34 - 28), var32 + 6, this.u + 6, ho.Z);
                  wp.I(var1, this.S + this.A - 23, this.u + 6, 14, 12, 3, var16.getValue());
                  tb.s(var1, this.S + this.A - 23, this.u + 6, 14.0F, 12.0F, 3.0F, uh.F(ho.Y, 2.0), uh.F(ho.S, 2.5));
               } else {
                  int var36 = this.K instanceof com.larpclient.module.l var38 ? u(var1, var38, var38.getValue(), var32 + 6, var10 + 4) : 0;
                  int var39 = this.K instanceof com.larpclient.module.v var40 && var40.getValue() < 0 ? 1 : 0;
                  String var41 = this.K instanceof com.larpclient.module.v var43 && var43.s() ? "Press or click…" : "Press key…";
                  bj.E(var1, bj.T(this.Y ? var41 : this.a(), var34 - 22 - var36), var32 + 6 + var36, var10 + 6, this.Y ? var7 : (var39 != 0 ? ho.Z : ho.u));
                  if (this.K instanceof com.larpclient.module.l) {
                     ur.O(var1, this.O ? ur.e : ur.A, this.S + this.A - 20, var10 + 5, 14, ho.Z);
                  }
               }
            }
         }
      }
   }

   private void K(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8) {
      switch (ho.m().slider()) {
         case "Minimal":
         case "Classic":
            this.q(var1, var2, var4, var6, var8, ho.m().slider().equals("Classic"));
            return;
         default:
            float var23 = this.q() + 2;
            float var24 = this.o();
            float var11 = this.c();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            tb.E(var1, var11, var14, var24, var12, var13, uh.H(ho.u, 0.1));
            tb.s(var1, var11, var14, var24, var12, var13, uh.H(ho.c, 0.5), uh.H(ho.c, 0.5));
            float var16 = var15 != 0 ? (float)(var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)(var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = uh.q(var8, ho.ds | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               wp.e(var1, var18, var14, var20, var12, var13, 5.0F, uh.H(var8, 0.35 * var6));
            }

            tb.d(var1, var18, var14, var20, var12, var13, var21, var8);
            tb.u(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               s(var1, var18 + var13, var23, var22, this.H && this.s == 1 ? 1.0 : 0.0, var8);
               s(var1, var18 + var20 - var13, var23, var22, this.H && this.s == 2 ? 1.0 : 0.0, var8);
            } else {
               s(var1, var18 + var20 - var13, var23, var22, this.H ? 1.0 : 0.0, var8);
            }
      }
   }

   private void q(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = this.q() + 2;
      float var11 = this.o();
      float var12 = this.c();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      tb.E(var1, var12, var14, var11, var13, var13 / 2.0F, uh.H(ho.u, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)(var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)(var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            wp.e(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, uh.H(var8, 0.4 * var6));
         }

         tb.d(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, uh.q(var8, ho.ds | 0xFF000000, 0.6), var8);
      } else {
         tb.E(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
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
      tb.Q(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      tb.E(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : ho.A | 0xFF000000);
      tb.J(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void s(GuiGraphicsExtractor var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      tb.Q(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         wp.e(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, uh.H(var6, 0.45 * var4));
      }

      tb.E(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void j(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      tb.E(var0, var1 - var5 / 2 - 1, var2 - var5 / 2 - 1, var5 + 2, var5 + 2, var5 / 2.0F + 1.0F, 855638016);
      tb.E(var0, var1 - var5 / 2, var2 - var5 / 2, var5, var5, var5 / 2.0F, -723722);
   }

   public void J(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.m()) {
         try (ua var4 = ua.K(this.c.k(1.0, 160))) {
            boolean var5 = ho.S();
            if (var5) {
               wp.S(var1, this.d, this.t, this.r, this.E, ho.D, ho.dw, 1.0);
            } else {
               wp.R(var1, this.d, this.t, this.r, this.E);
               wp.Z(var1, this.d, this.t, this.r, this.E, ho.Q, ho.dw, 1.0);
            }

            var1.enableScissor(this.d, this.t, this.d + this.r, this.t + this.E);

            try {
               if (this.K instanceof com.larpclient.module.l var6) {
                  if (this.j != null) {
                     this.j.render(var1, var2, var3);
                  }

                  int var27 = this.t + this.P();
                  int var28 = Math.min(this.Z.size() - this.U, this.r());
                  int var29 = this.N();
                  if (this.Z.isEmpty() && this.r() > 0) {
                     bj.E(var1, "No matching blocks", this.d + 8, var27 + 6, ho.Z);
                  }

                  for (int var30 = 0; var30 < var28; var30++) {
                     String var31 = this.Z.get(var30 + this.U);
                     int var33 = var27 + var30 * var29;
                     boolean var34 = kv.f(var2, var3, this.d, var33, this.r, var29);
                     boolean var35 = var6.O(var31);
                     int var16 = !var34 && (this.j == null || this.o != var30 + this.U) ? 0 : 1;
                     if (var5 && var35) {
                        wp.I(var1, this.d + 2, var33 + 1, this.r - 4, var29 - 2, ho.Q, ho.q(this.d, var16 != 0 ? 1.3 : 1.0));
                     } else if (var16 != 0 || var35) {
                        wp.I(var1, this.d + 2, var33 + 1, this.r - 4, var29 - 2, ho.Q, var5 ? uh.H(ho.u, 0.08) : ho.e);
                     }

                     int var17;
                     if (var6.previewSize() > 0) {
                        var6.drawPreview(var1, var31, this.d + 5, var33 + 3, var29 - 6);
                        var17 = var29 - 4;
                     } else {
                        var17 = u(var1, var6, var31, this.d + 8, var33 + 2);
                     }

                     bj.E(
                        var1,
                        bj.T(var6.getModeLabel(var31), this.r - 16 - var17),
                        this.d + 8 + var17,
                        var33 + (var29 - 8) / 2,
                        !var35 && !var34 ? uh.q(ho.Z, ho.u, 0.5) : ho.u
                     );
                  }

                  wp.p(var1, this.d + this.r - 3, var27, this.r() * var29, this.Z.size() * var29, this.U * var29);
               } else if (this.K instanceof com.larpclient.module.i var7) {
                  float[] var26 = Color.RGBtoHSB(var7.getValue() >> 16 & 0xFF, var7.getValue() >> 8 & 0xFF, var7.getValue() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.r - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.t + 6 + var11 * 18;
                     bj.E(var1, var9[var11], this.d + 5, var12, ho.Z);

                     for (byte var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / var10;

                        int var15 = switch (var11) {
                           case 0 -> uh.n(var14, 1.0F, 1.0F);
                           case 1 -> uh.n(var26[0], var14, var26[2]);
                           case 2 -> uh.n(var26[0], var26[1], var14);
                           default -> uh.q(ho.t, var7.getValue() | 0xFF000000, var14);
                        };
                        kv.d(var1, this.d + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var32 = var11 < 3 ? var26[var11] : (var7.getValue() >>> 24) / 255.0F;
                     kv.I(var1, this.d + 19 + (int)(var32 * var10), var12 - 1, 3, 12, ho.u);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            if (!var5) {
               tb.s(var1, this.d, this.t, this.r, this.E, ho.Q, uh.F(ho.Y, 1.5), uh.F(ho.S, 1.5));
            }
         }
      }
   }

   private static int u(GuiGraphicsExtractor var0, com.larpclient.module.l var1, String var2, int var3, int var4) {
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

   public boolean X(double var1, double var3, int var5) {
      if (!this.y(var1, var3)) {
         return false;
      }

      if (var5 != 0) {
         return true;
      }

      if (this.K instanceof com.larpclient.module.l var6) {
         if (this.j != null && var3 < this.t + this.P()) {
            this.j.click(var1, var3, var5);
            return true;
         }

         int var8 = (int)(var3 - this.t - this.P()) / this.N();
         if (var8 < 0 || var8 >= this.r() || var8 + this.U >= this.Z.size()) {
            return true;
         }

         this.I(var6, this.Z.get(var8 + this.U));
         this.d();
         sr.v(sr.e.q);
      } else if (this.K instanceof com.larpclient.module.i) {
         this.N = Math.clamp((int)(var3 - this.t - 6.0) / 18, 0, 3);
         this.H = (boolean)1;
         this.drag(var1, var3);
      }

      return true;
   }

   private void I(com.larpclient.module.l var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawnerModule var4 = FakeSpawnerModule.M();
         if (var4 == null || !var4.S().contains(var1)) {
            throw var5;
         }

         k4.f(Component.literal(var5.getMessage()), true);
      }
   }

   public void N(double var1) {
      if (this.K instanceof com.larpclient.module.l) {
         this.U -= (int)var1;
         this.q(this.w, this.m);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      }

      this.T = (boolean)1;
      if (this.Y && (var5 == 0 || var5 == 1) && this.K instanceof com.larpclient.module.v var6 && var6.s()) {
         if (System.nanoTime() - this.R < 300000000L) {
            return true;
         }

         var6.u(bc.Z(var5));
         this.Y = (boolean)0;
         sr.v(sr.e.q);
         return true;
      } else if (var5 == 2 && this.N() != null && !this.a) {
         this.k = (boolean)0;
         this.C = (boolean)0;
         this.Y = (boolean)0;
         this.d();
         if (this.I != null) {
            this.I.blur();
         }

         this.a = (boolean)1;
         sr.v(sr.e.q);
         return true;
      } else if (var5 == 1) {
         this.k = (boolean)0;
         this.C = (boolean)0;
         this.Y = (boolean)0;
         this.a = (boolean)0;
         if (this.I != null) {
            this.I.blur();
         }

         if (this.K instanceof com.larpclient.module.l var12) {
            this.I(var12, var12.A());
         } else {
            this.K.u();
         }

         if (this.o() != null) {
            this.o().u();
         }

         sr.v(sr.e.q);
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.a) {
            this.a = (boolean)0;
            return true;
         }

         if (this.K instanceof com.larpclient.module.s var11) {
            var11.c();
            sr.v(var11.getValue() ? sr.e.J : sr.e.v);
         } else if (this.K instanceof com.larpclient.module.e var13) {
            if (this.o() != null) {
               if (var3 >= this.u + 22) {
                  double var16 = this.F(var1);
                  this.s = Math.abs(var16 - var13.getValue()) > Math.abs(var16 - this.o().getValue()) ? 2 : 1;
                  this.H = (boolean)1;
                  this.drag(var1, var3);
               }
            } else if (this.C() ? !(var3 < this.u + 24) || !(var1 >= this.A() - 4) : !(var1 >= this.A() - 4)) {
               if (this.C() ? var3 >= this.u + 22 : var1 >= this.Q() - 4) {
                  this.v();
                  this.H = (boolean)1;
                  this.drag(var1, var3);
               }
            } else {
               this.h = var13.getEditValueAsString();
               this.k = (boolean)1;
               this.n = (boolean)1;
            }
         } else if (this.K instanceof com.larpclient.module.l var8) {
            this.O = (boolean)(!this.O ? 1 : 0);
            sr.v(this.O ? sr.e.S : sr.e.d);
            if (this.j != null) {
               this.j.H = "";
               this.j.T = this.O;
            }

            this.Z = var8.getVisibleModes();
            this.o = Math.max(0, this.Z.indexOf(var8.getValue()));
            this.U = Math.max(0, this.o - 2);
            this.q(this.w, this.m);
         } else if (this.K instanceof com.larpclient.module.i) {
            this.O = (boolean)(!this.O ? 1 : 0);
            sr.v(this.O ? sr.e.S : sr.e.d);
         } else if (this.K instanceof com.larpclient.module.g) {
            this.I.bounds(this.Q(), this.u + 2, this.Y());
            this.I.J = 20;
            this.I.H = this.a();
            this.C = (boolean)1;
            this.I.click(this.Q() + 2, this.u + 4, 0);
            this.I.j();
         } else if (this.K instanceof com.larpclient.module.v) {
            this.Y = (boolean)1;
            this.R = System.nanoTime();
            sr.v(sr.e.q);
         }

         return true;
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.H) {
         if (this.o() != null) {
            if (this.F(this.F(var1))) {
               sr.v(sr.e.Q);
            }
         } else if (this.K instanceof com.larpclient.module.e var5) {
            double var12 = var5.getValue();
            var5.B((var1 - this.c()) / this.o());
            if (var12 != var5.getValue()) {
               sr.v(sr.e.Q);
            }
         }

         if (this.K instanceof com.larpclient.module.i var11) {
            double var14 = Math.clamp((var1 - this.d - 20.0) / Math.max(1, this.r - 28), 0.0, 1.0);
            int var8 = var11.getValue();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.N < 3) {
               var9[this.N] = (float)var14;
            }

            int var10 = this.N == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(uh.n(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24);
            if (var8 != var11.getValue()) {
               sr.v(sr.e.Q);
            }
         }
      }
   }

   private double F(double var1) {
      com.larpclient.module.e var3 = (com.larpclient.module.e)this.K;
      return var3.e() + Math.clamp((var1 - this.c()) / this.o(), 0.0, 1.0) * (var3.m() - var3.e());
   }

   private boolean F(double var1) {
      com.larpclient.module.e var3 = (com.larpclient.module.e)this.K;
      com.larpclient.module.e var4 = this.o();
      double var5 = var3.getValue();
      double var7 = var4.getValue();
      if (this.s == 1) {
         var3.X(Math.min(var1, var4.getValue()));
      } else {
         var4.X(Math.max(var1, var3.getValue()));
      }

      return var5 != var3.getValue() || var7 != var4.getValue();
   }

   private void v() {
      if (this.k) {
         try {
            ((com.larpclient.module.e)this.K).setFromEditString(this.h);
         } catch (IllegalArgumentException var2) {
         }

         this.k = (boolean)0;
      }
   }

   private void a() {
      if (this.C && this.K instanceof com.larpclient.module.g var1) {
         try {
            var1.k(this.I.H);
         } catch (IllegalArgumentException var3) {
         }

         this.C = (boolean)0;
         this.I.blur();
      }
   }

   @Override
   public void release() {
      this.H = (boolean)0;
   }

   @Override
   public void blur() {
      super.blur();
      this.Y = (boolean)0;
      this.a = (boolean)0;
      this.d();
      this.v();
      this.a();
   }

   public boolean k() {
      return this.T && (this.k || this.C || this.Y || this.a || this.O && this.j != null);
   }

   @Override
   public boolean captureMouse(int var1) {
      if (this.T && bc.j(var1)) {
         int var2 = bc.Z(var1);
         if (this.Y && this.K instanceof com.larpclient.module.v var3) {
            var3.u(var2);
            this.Y = (boolean)0;
            sr.v(sr.e.q);
            return true;
         } else if (this.a) {
            this.a = (boolean)0;
            this.i(var2);
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
      if (this.T && this.C) {
         return this.I.character(var1);
      }

      if (this.T && this.O && this.j != null) {
         return this.j.character(var1);
      }

      int var2 = this.K instanceof com.larpclient.module.e var3 && var3.L() && "kKmMbBtT,$".indexOf(var1) >= 0 ? 1 : 0;
      if (this.T && this.k && ("0123456789.-+eE".indexOf(var1) >= 0 || var2 != 0)) {
         if (this.n) {
            this.h = "";
            this.n = (boolean)0;
         }

         if (this.h.length() < 24) {
            this.h = this.h + (char)var1;
            sr.v(sr.e.g);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.T) {
         return false;
      }

      if (this.C) {
         if (var1 == 256) {
            this.C = (boolean)0;
            this.I.blur();
            return true;
         }

         if (var1 != 257 && var1 != 335) {
            return this.I.key(var1);
         }

         this.a();
         return true;
      } else {
         if (this.Y && this.K instanceof com.larpclient.module.v var8) {
            var8.u(var1 != 256 && var1 != 261 ? var1 : -1);
            this.Y = (boolean)0;
            return true;
         }

         if (this.a) {
            this.a = (boolean)0;
            this.i(var1 != 256 && var1 != 261 ? var1 : -1);
            return true;
         }

         if (this.k) {
            if (var1 == 256) {
               this.k = (boolean)0;
            } else if (var1 == 257) {
               this.v();
            } else if (var1 == 259) {
               this.h = !this.n && !this.h.isEmpty() ? this.h.substring(0, this.h.length() - 1) : "";
               this.n = (boolean)0;
            }

            return true;
         } else {
            if (this.O && var1 == 256) {
               this.d();
               return true;
            }

            if (this.O && this.j != null && this.K instanceof com.larpclient.module.l var7) {
               if (var1 == 264 || var1 == 265) {
                  this.o = Math.clamp(this.o + (var1 == 264 ? 1 : -1), 0, Math.max(0, this.Z.size() - 1));
                  if (this.o < this.U) {
                     this.U = this.o;
                  } else if (this.o >= this.U + Math.max(1, this.r())) {
                     this.U = this.o - Math.max(1, this.r()) + 1;
                  }

                  this.q(this.w, this.m);
                  return true;
               } else {
                  if (var1 != 257 && var1 != 335) {
                     return this.j.key(var1);
                  }

                  if (!this.Z.isEmpty()) {
                     this.I(var7, this.Z.get(this.o));
                     this.d();
                     sr.v(sr.e.q);
                  }

                  return true;
               }
            } else if (this.K instanceof com.larpclient.module.l var2 && this.O && (var1 == 264 || var1 == 265)) {
               List var15 = var2.getVisibleModes();
               int var4 = Math.clamp(var15.indexOf(var2.getValue()) + (var1 == 264 ? 1 : -1), 0, var15.size() - 1);
               this.I(var2, (String)var15.get(var4));
               this.U = Math.max(0, var4 - 2);
               this.q(this.w, this.m);
               return true;
            } else if (this.K instanceof com.larpclient.module.e var5 && this.o() != null && (var1 == 262 || var1 == 263)) {
               double var14 = this.s == 1 ? var5.getValue() : this.o().getValue();
               if (this.F(var14 + (var1 == 262 ? 1 : -1) * var5.L())) {
                  sr.v(sr.e.Q);
               }

               return true;
            } else if (this.K instanceof com.larpclient.module.e var6 && (var1 == 262 || var1 == 263)) {
               double var13 = var6.getValue();
               var6.X(var13 + (var1 == 262 ? 1 : -1) * var6.L());
               if (var13 != var6.getValue()) {
                  sr.v(sr.e.Q);
               }

               return true;
            } else {
               return var1 != 257 && var1 != 32 ? false : this.click(this.e ? this.S + this.A / 2.0 : this.S + 2, this.u + 2, 0);
            }
         }
      }
   }
}

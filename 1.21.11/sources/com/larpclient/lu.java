package com.larpclient;

import net.minecraft.class_11908;
import net.minecraft.class_332;
import net.minecraft.class_437;

public final class lu extends tx {
   private final s6 B;
   private final qb a;
   private final qb[] P = new qb[]{new qb(0.0), new qb(0.0), new qb(0.0)};
   private final double[] l = new double[3];
   private String p = "";
   private long d;
   private long w;
   private lv N;
   private lv e;
   private lv Y;
   private br F;
   private final lv[] T = new lv[3];

   public lu(class_437 var1) {
      super("Menu atmosphere", var1);
      this.B = new s6(LarpClient.b().e().F().t);
      this.a = new qb(this.B.P().ordinal());
   }

   @Override
   public tl atmosphere() {
      return this.B.P();
   }

   protected void method_25426() {
      this.t();
      this.F = br.calculate(this.field_22789, this.field_22790);
      this.Q(this.F.width(), this.F.height());
      this.w = System.nanoTime();

      for (int var1 = 0; var1 < this.T.length; var1++) {
         int var2 = var1;
         tl var3 = tl.values()[var1];
         ue.c var4 = this.F.card(var1);
         lv var5 = this.T[var1] = this.f(var3.G, () -> this.B.u(var2), var4.x(), var4.y(), var4.width());
         var5.w = var4.height();
         var5.y = (boolean)1;
         var5.J = var3x -> this.c(var3x, var2, var3);
      }

      this.N = this.f("‹", () -> this.B.T(-1), this.h + 16, this.F.statusY() - 3, 24);
      this.e = this.f("›", () -> this.B.T(1), this.h + this.W - 40, this.F.statusY() - 3, 24);
      this.N.w = this.e.w = 20;
      this.Y = this.C("", this::E, this.F.motion());
      this.C("Back", this::method_25419, this.F.back());
      this.C("Apply", this::s, this.F.apply());
      this.Y.J = var1x -> uw.N(
         var1x, uw.R("Motion: " + (this.V() ? "On" : "Reduced"), this.Y.d - 12), this.Y.D + this.Y.d / 2, this.Y.g + 8, ei.R(ez.H, this.Y.S)
      );
   }

   private lv C(String var1, Runnable var2, ue.c var3) {
      lv var4 = this.f(var1, var2, var3.x(), var3.y(), var3.width());
      var4.y = (boolean)1;
      return var4;
   }

   private boolean V() {
      return LarpClient.b().e().F().k;
   }

   private void E() {
      qk var1 = LarpClient.b().e();
      y0 var2 = var1.F();
      var2.k = (boolean)(!var2.k ? 1 : 0);
      if (!var1.N()) {
         var2.k = (boolean)(!var2.k ? 1 : 0);
         this.p = "Could not save motion preference.";
      }
   }

   private void c(class_332 var1, int var2, tl var3) {
      lv var4 = this.T[var2];
      double var5 = this.V() ? this.P[var2].a(var4.Y ? 1.0 : 0.0, 280) : (var4.Y ? 1.0 : 0.0);
      int var7 = var4.w - 28;
      ly.n(var1, var3, var4.D + 3, var4.g + 3, var4.d - 6, var7, var4.S, false);
      uw.N(var1, uw.R(var3.G, var4.d - 12), var4.D + var4.d / 2, var4.g + var4.w - 17, ei.R(ez.H, var4.S));
      if (var3 == this.B.T() && var7 >= 24) {
         com.larpclient.p.B(var1, var4.D + 7, var4.g + 7, 38, 16, 3, ei.R(-535485155, var4.S));
         uw.z(var1, "Saved", var4.D + 12, var4.g + 11, ei.R(-7873104, var4.S));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((var4.d - 16) * var5));
         com.larpclient.p.B(var1, var4.D + (var4.d - var8) / 2, var4.g + var4.w - 3, var8, 2, 1, ei.R(ez.a, var5 * var4.S));
      }
   }

   private void s() {
      qk var1 = LarpClient.b().e();
      y0 var2 = var1.F();
      tl var3 = var2.t;
      boolean var4 = var2.z;
      var2.t = this.B.p();
      var2.z = (boolean)0;
      if (!var1.N()) {
         var2.t = var3;
         var2.z = var4;
         this.p = "Could not save background.";
      } else {
         q8.G("Menu", "Applied atmosphere {}", var2.t.G);
         this.method_25419();
      }
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      this.Z(var1);
      var3 = this.a(var3);

      try {
         boolean var5 = this.V();
         double var6 = (System.nanoTime() - this.w) / 1000000.0;
         double var8 = this.b();
         uw.N(var1, "Menu atmosphere", this.field_22789 / 2, this.o + 16, ei.R(ez.H, var8));
         if (this.H >= 220) {
            uw.N(var1, "Preview a scene, then apply it.", this.field_22789 / 2, this.o + 35, ei.R(ez.PH, var8));
         }

         for (int var10 = 0; var10 < this.T.length; var10++) {
            lv var11 = this.T[var10];
            this.l[var10] = ix.z(var6, 60 + var10 * 65, 380.0, var5);
            var11.Y = (boolean)(var10 == this.B.P().ordinal() ? 1 : 0);
            var11.R = (1.0 - this.l[var10]) * 7.0;
            var11.N = (boolean)(!this.q() && this.l[var10] > 0.85 ? 1 : 0);
         }

         this.N.N = (boolean)(!this.q() && this.B.P().ordinal() > 0 ? 1 : 0);
         this.e.N = (boolean)(!this.q() && this.B.P().ordinal() < 2 ? 1 : 0);
         String var18 = this.p.isEmpty() ? "Saved: " + this.B.T().G : this.p;
         uw.N(var1, uw.R(var18, this.W - 104), this.field_22789 / 2, this.F.statusY(), ei.R(this.p.isEmpty() ? ez.PH : -24915, var8));
         double var19 = var5 ? this.a.a(this.B.P().ordinal(), 320) : this.B.P().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            gu.Q(var1, this.field_22789 / 2 + (var13 - 1) * 10, this.F.statusY() + 16, 1, ei.R(ez.t, var8));
         }

         gu.Q(var1, this.field_22789 / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.F.statusY() + 16, 2, ei.R(ez.a, var8));
         this.drawControls(var1, var2, var3);
      } finally {
         this.j(var1);
      }
   }

   @Override
   protected void drawControls(class_332 var1, int var2, int var3) {
      for (lc var5 : this.K) {
         lv var6 = (lv)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.T.length; var9++) {
            if (var6 == this.T[var9]) {
               var7 = this.l[var9];
            }
         }

         var6.S = this.b() * var7;
         if (this.q()) {
            var6.N = (boolean)0;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean method_25401(double var1, double var3, double var5, double var7) {
      if (!this.q() && !(this.b() <= 0.15)) {
         long var9 = System.nanoTime();
         if (var7 != 0.0 && var9 - this.d >= 150000000L) {
            this.d = var9;
            this.B.T(var7 > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean method_25404(class_11908 var1) {
      if (this.q() || this.b() <= 0.15) {
         return true;
      }

      if (s2.i(var1) != 263 && s2.i(var1) != 262) {
         return super.method_25404(var1);
      }

      this.B.T(s2.i(var1) == 263 ? -1 : 1);
      return true;
   }
}

package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public final class pl extends pj {
   private final xk Y;
   private final u7 j;
   private final u7[] x = new u7[]{new u7(0.0), new u7(0.0), new u7(0.0)};
   private final double[] D = new double[3];
   private String y = "";
   private long N;
   private long q;
   private pd h;
   private pd l;
   private pd C;
   private h9 g;
   private final pd[] X = new pd[3];

   public pl(Screen var1) {
      super("Menu atmosphere", var1);
      this.Y = new xk(LarpClient.y().v().E().A);
      this.j = new u7(this.Y.E().ordinal());
   }

   @Override
   public f7 atmosphere() {
      return this.Y.E();
   }

   protected void init() {
      this.u();
      this.g = h9.calculate(this.width, this.height);
      this.p(this.g.width(), this.g.height());
      this.q = System.nanoTime();

      for (int var1 = 0; var1 < this.X.length; var1++) {
         int var2 = var1;
         f7 var3 = f7.values()[var1];
         o6.b var4 = this.g.card(var1);
         pd var5 = this.X[var1] = this.r(var3.f, () -> this.Y.e(var2), var4.x(), var4.y(), var4.width());
         var5.O = var4.height();
         var5.N = (boolean)1;
         var5.z = var3x -> this.d(var3x, var2, var3);
      }

      this.h = this.r("‹", () -> this.Y.X(-1), this.V + 16, this.g.statusY() - 3, 24);
      this.l = this.r("›", () -> this.Y.X(1), this.V + this.s - 40, this.g.statusY() - 3, 24);
      this.h.O = this.l.O = 20;
      this.C = this.v("", this::g, this.g.motion());
      this.v("Back", this::onClose, this.g.back());
      this.v("Apply", this::N, this.g.apply());
      this.C.z = var1x -> j_.n(
         var1x, j_.x("Motion: " + (this.C() ? "On" : "Reduced"), this.C.v - 12), this.C.B + this.C.v / 2, this.C.L + 8, oh.S(rv.P, this.C.f)
      );
   }

   private pd v(String var1, Runnable var2, o6.b var3) {
      pd var4 = this.r(var1, var2, var3.x(), var3.y(), var3.width());
      var4.N = (boolean)1;
      return var4;
   }

   private boolean C() {
      return LarpClient.y().v().E().o;
   }

   private void g() {
      mo var1 = LarpClient.y().v();
      fs var2 = var1.E();
      var2.o = (boolean)(!var2.o ? 1 : 0);
      if (!var1.Y()) {
         var2.o = (boolean)(!var2.o ? 1 : 0);
         this.y = "Could not save motion preference.";
      }
   }

   private void d(GuiGraphicsExtractor var1, int var2, f7 var3) {
      pd var4 = this.X[var2];
      double var5 = this.C() ? this.x[var2].y(var4.p ? 1.0 : 0.0, 280) : (var4.p ? 1.0 : 0.0);
      int var7 = var4.O - 28;
      dd.v(var1, var3, var4.B + 3, var4.L + 3, var4.v - 6, var7, var4.f, false);
      j_.n(var1, j_.x(var3.f, var4.v - 12), var4.B + var4.v / 2, var4.L + var4.O - 17, oh.S(rv.P, var4.f));
      if (var3 == this.Y.k() && var7 >= 24) {
         ll.N(var1, var4.B + 7, var4.L + 7, 38, 16, 3, oh.S(-535485155, var4.f));
         j_.c(var1, "Saved", var4.B + 12, var4.L + 11, oh.S(-7873104, var4.f));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((var4.v - 16) * var5));
         ll.N(var1, var4.B + (var4.v - var8) / 2, var4.L + var4.O - 3, var8, 2, 1, oh.S(rv.e, var5 * var4.f));
      }
   }

   private void N() {
      mo var1 = LarpClient.y().v();
      fs var2 = var1.E();
      f7 var3 = var2.A;
      boolean var4 = var2.W;
      var2.A = this.Y.C();
      var2.W = (boolean)0;
      if (!var1.Y()) {
         var2.A = var3;
         var2.W = var4;
         this.y = "Could not save background.";
      } else {
         r4.V("Menu", "Applied atmosphere {}", var2.A.f);
         this.onClose();
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.e(var1);
      var3 = this.B(var3);

      try {
         boolean var5 = this.C();
         double var6 = (System.nanoTime() - this.q) / 1000000.0;
         double var8 = this.B();
         j_.n(var1, "Menu atmosphere", this.width / 2, this.o + 16, oh.S(rv.P, var8));
         if (this.a >= 220) {
            j_.n(var1, "Preview a scene, then apply it.", this.width / 2, this.o + 35, oh.S(rv.G0, var8));
         }

         for (int var10 = 0; var10 < this.X.length; var10++) {
            pd var11 = this.X[var10];
            this.D[var10] = r3.o(var6, 60 + var10 * 65, 380.0, var5);
            var11.p = (boolean)(var10 == this.Y.E().ordinal() ? 1 : 0);
            var11.n = (1.0 - this.D[var10]) * 7.0;
            var11.x = (boolean)(!this.G() && this.D[var10] > 0.85 ? 1 : 0);
         }

         this.h.x = (boolean)(!this.G() && this.Y.E().ordinal() > 0 ? 1 : 0);
         this.l.x = (boolean)(!this.G() && this.Y.E().ordinal() < 2 ? 1 : 0);
         String var18 = this.y.isEmpty() ? "Saved: " + this.Y.k().f : this.y;
         j_.n(var1, j_.x(var18, this.s - 104), this.width / 2, this.g.statusY(), oh.S(this.y.isEmpty() ? rv.G0 : -24915, var8));
         double var19 = var5 ? this.j.y(this.Y.E().ordinal(), 320) : this.Y.E().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            fr.J(var1, this.width / 2 + (var13 - 1) * 10, this.g.statusY() + 16, 1, oh.S(rv.r, var8));
         }

         fr.J(var1, this.width / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.g.statusY() + 16, 2, oh.S(rv.e, var8));
         this.drawControls(var1, var2, var3);
      } finally {
         this.V(var1);
      }
   }

   @Override
   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (oq var5 : this.k) {
         pd var6 = (pd)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.X.length; var9++) {
            if (var6 == this.X[var9]) {
               var7 = this.D[var9];
            }
         }

         var6.f = this.B() * var7;
         if (this.G()) {
            var6.x = (boolean)0;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.G() && !(this.B() <= 0.15)) {
         long var9 = System.nanoTime();
         if (var7 != 0.0 && var9 - this.N >= 150000000L) {
            this.N = var9;
            this.Y.X(var7 > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.G() || this.B() <= 0.15) {
         return true;
      }

      if (r7.g(var1) != 263 && r7.g(var1) != 262) {
         return super.keyPressed(var1);
      }

      this.Y.X(r7.g(var1) == 263 ? -1 : 1);
      return true;
   }
}

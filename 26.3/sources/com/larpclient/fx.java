package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public final class fx extends fi {
   private final o_ s;
   private final fq h;
   private final fq[] x = new fq[]{new fq(0.0), new fq(0.0), new fq(0.0)};
   private final double[] Q = new double[3];
   private String p = "";
   private long u;
   private long F;
   private si Z;
   private si a;
   private si Y;
   private im j;
   private final si[] d = new si[3];

   public fx(Screen var1) {
      super("Menu atmosphere", var1);
      this.s = new o_(LarpClient.N().h().k().A);
      this.h = new fq(this.s.m().ordinal());
   }

   @Override
   public pz atmosphere() {
      return this.s.m();
   }

   protected void init() {
      this.W();
      this.j = im.calculate(this.width, this.height);
      this.c(this.j.width(), this.j.height());
      this.F = System.nanoTime();

      for (int var1 = 0; var1 < this.d.length; var1++) {
         int var2 = var1;
         pz var3 = pz.values()[var1];
         _c.f var4 = this.j.card(var1);
         si var5 = this.d[var1] = this.Z(var3.J, () -> this.s.I(var2), var4.x(), var4.y(), var4.width());
         var5.O = var4.height();
         var5.C = (boolean)1;
         var5.q = var3x -> this.R(var3x, var2, var3);
      }

      this.Z = this.Z("‹", () -> this.s.G(-1), this.V + 16, this.j.statusY() - 3, 24);
      this.a = this.Z("›", () -> this.s.G(1), this.V + this.w - 40, this.j.statusY() - 3, 24);
      this.Z.O = this.a.O = 20;
      this.Y = this.N("", this::r, this.j.motion());
      this.N("Back", this::onClose, this.j.back());
      this.N("Apply", this::K, this.j.apply());
      this.Y.q = var1x -> ic.s(
         var1x, ic.W("Motion: " + (this.D() ? "On" : "Reduced"), this.Y.L - 12), this.Y.P + this.Y.L / 2, this.Y.T + 8, vv.O(c.H, this.Y.n)
      );
   }

   private si N(String var1, Runnable var2, _c.f var3) {
      si var4 = this.Z(var1, var2, var3.x(), var3.y(), var3.width());
      var4.C = (boolean)1;
      return var4;
   }

   private boolean D() {
      return LarpClient.N().h().k().w;
   }

   private void r() {
      aq var1 = LarpClient.N().h();
      i3 var2 = var1.k();
      var2.w = (boolean)(!var2.w ? 1 : 0);
      if (!var1.P()) {
         var2.w = (boolean)(!var2.w ? 1 : 0);
         this.p = "Could not save motion preference.";
      }
   }

   private void R(GuiGraphicsExtractor var1, int var2, pz var3) {
      si var4 = this.d[var2];
      double var5 = this.D() ? this.x[var2].n(var4.l ? 1.0 : 0.0, 280) : (var4.l ? 1.0 : 0.0);
      int var7 = var4.O - 28;
      na.h(var1, var3, var4.P + 3, var4.T + 3, var4.L - 6, var7, var4.n, false);
      ic.s(var1, ic.W(var3.J, var4.L - 12), var4.P + var4.L / 2, var4.T + var4.O - 17, vv.O(c.H, var4.n));
      if (var3 == this.s.G() && var7 >= 24) {
         wc.b(var1, var4.P + 7, var4.T + 7, 38, 16, 3, vv.O(-535485155, var4.n));
         ic.H(var1, "Saved", var4.P + 12, var4.T + 11, vv.O(-7873104, var4.n));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((var4.L - 16) * var5));
         wc.b(var1, var4.P + (var4.L - var8) / 2, var4.T + var4.O - 3, var8, 2, 1, vv.O(c.y, var5 * var4.n));
      }
   }

   private void K() {
      aq var1 = LarpClient.N().h();
      i3 var2 = var1.k();
      pz var3 = var2.A;
      boolean var4 = var2.I;
      var2.A = this.s.K();
      var2.I = (boolean)0;
      if (!var1.P()) {
         var2.A = var3;
         var2.I = var4;
         this.p = "Could not save background.";
      } else {
         _q.z("Menu", "Applied atmosphere {}", var2.A.J);
         this.onClose();
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.J(var1);
      var3 = this.i(var3);

      try {
         boolean var5 = this.D();
         double var6 = (System.nanoTime() - this.F) / 1000000.0;
         double var8 = this.c();
         ic.s(var1, "Menu atmosphere", this.width / 2, this.n + 16, vv.O(c.H, var8));
         if (this.W >= 220) {
            ic.s(var1, "Preview a scene, then apply it.", this.width / 2, this.n + 35, vv.O(c.W, var8));
         }

         for (int var10 = 0; var10 < this.d.length; var10++) {
            si var11 = this.d[var10];
            this.Q[var10] = fr.O(var6, 60 + var10 * 65, 380.0, var5);
            var11.l = (boolean)(var10 == this.s.m().ordinal() ? 1 : 0);
            var11.p = (1.0 - this.Q[var10]) * 7.0;
            var11.V = (boolean)(!this.t() && this.Q[var10] > 0.85 ? 1 : 0);
         }

         this.Z.V = (boolean)(!this.t() && this.s.m().ordinal() > 0 ? 1 : 0);
         this.a.V = (boolean)(!this.t() && this.s.m().ordinal() < 2 ? 1 : 0);
         String var18 = this.p.isEmpty() ? "Saved: " + this.s.G().J : this.p;
         ic.s(var1, ic.W(var18, this.w - 104), this.width / 2, this.j.statusY(), vv.O(this.p.isEmpty() ? c.W : -24915, var8));
         double var19 = var5 ? this.h.n(this.s.m().ordinal(), 320) : this.s.m().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            h5.M(var1, this.width / 2 + (var13 - 1) * 10, this.j.statusY() + 16, 1, vv.O(c.f, var8));
         }

         h5.M(var1, this.width / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.j.statusY() + 16, 2, vv.O(c.y, var8));
         this.drawControls(var1, var2, var3);
      } finally {
         this.T(var1);
      }
   }

   @Override
   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (j0 var5 : this.J) {
         si var6 = (si)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.d.length; var9++) {
            if (var6 == this.d[var9]) {
               var7 = this.Q[var9];
            }
         }

         var6.n = this.c() * var7;
         if (this.t()) {
            var6.V = (boolean)0;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.t() && !(this.c() <= 0.15)) {
         long var9 = System.nanoTime();
         if (var7 != 0.0 && var9 - this.u >= 150000000L) {
            this.u = var9;
            this.s.G(var7 > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.t() || this.c() <= 0.15) {
         return true;
      }

      if (j2.P(var1) != 263 && j2.P(var1) != 262) {
         return super.keyPressed(var1);
      }

      this.s.G(j2.P(var1) == 263 ? -1 : 1);
      return true;
   }
}

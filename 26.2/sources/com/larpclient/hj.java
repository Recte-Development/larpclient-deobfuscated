package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public final class hj extends hg {
   private final mq q;
   private final jp m;
   private final jp[] Z = new jp[]{new jp(0.0), new jp(0.0), new jp(0.0)};
   private final double[] N = new double[3];
   private String W = "";
   private long Q;
   private long U;
   private j5 e;
   private j5 b;
   private j5 z;
   private i L;
   private final j5[] M = new j5[3];

   public hj(Screen var1) {
      super("Menu atmosphere", var1);
      this.q = new mq(LarpClient.R().o().g().FH);
      this.m = new jp(this.q.u().ordinal());
   }

   @Override
   public t2 atmosphere() {
      return this.q.u();
   }

   protected void init() {
      this.u();
      this.L = i.calculate(this.width, this.height);
      this.h(this.L.width(), this.L.height());
      this.U = System.nanoTime();

      for (int var1 = 0; var1 < this.M.length; var1++) {
         int var2 = var1;
         t2 var3 = t2.values()[var1];
         p1.e var4 = this.L.card(var1);
         j5 var5 = this.M[var1] = this.N(var3.z, () -> this.q.p(var2), var4.x(), var4.y(), var4.width());
         var5.J = var4.height();
         var5.I = (boolean)1;
         var5.c = var3x -> this.f(var3x, var2, var3);
      }

      this.e = this.N("‹", () -> this.q.m(-1), this.X + 16, this.L.statusY() - 3, 24);
      this.b = this.N("›", () -> this.q.m(1), this.X + this.G - 40, this.L.statusY() - 3, 24);
      this.e.J = this.b.J = 20;
      this.z = this.u("", this::t, this.L.motion());
      this.u("Back", this::onClose, this.L.back());
      this.u("Apply", this::i, this.L.apply());
      this.z.c = var1x -> bj.n(
         var1x, bj.G("Motion: " + (this.z() ? "On" : "Reduced"), this.z.A - 12), this.z.S + this.z.A / 2, this.z.u + 8, uh.F(ho.u, this.z.G)
      );
   }

   private j5 u(String var1, Runnable var2, p1.e var3) {
      j5 var4 = this.N(var1, var2, var3.x(), var3.y(), var3.width());
      var4.I = (boolean)1;
      return var4;
   }

   private boolean z() {
      return LarpClient.R().o().g().L;
   }

   private void t() {
      gg var1 = LarpClient.R().o();
      hb var2 = var1.g();
      var2.L = (boolean)(!var2.L ? 1 : 0);
      if (!var1.r()) {
         var2.L = (boolean)(!var2.L ? 1 : 0);
         this.W = "Could not save motion preference.";
      }
   }

   private void f(GuiGraphicsExtractor var1, int var2, t2 var3) {
      j5 var4 = this.M[var2];
      double var5 = this.z() ? this.Z[var2].k(var4.v ? 1.0 : 0.0, 280) : (var4.v ? 1.0 : 0.0);
      int var7 = var4.J - 28;
      l2.z(var1, var3, var4.S + 3, var4.u + 3, var4.A - 6, var7, var4.G, false);
      bj.n(var1, bj.G(var3.z, var4.A - 12), var4.S + var4.A / 2, var4.u + var4.J - 17, uh.F(ho.u, var4.G));
      if (var3 == this.q.D() && var7 >= 24) {
         wp.I(var1, var4.S + 7, var4.u + 7, 38, 16, 3, uh.F(-535485155, var4.G));
         bj.h(var1, "Saved", var4.S + 12, var4.u + 11, uh.F(-7873104, var4.G));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((var4.A - 16) * var5));
         wp.I(var1, var4.S + (var4.A - var8) / 2, var4.u + var4.J - 3, var8, 2, 1, uh.F(ho.J, var5 * var4.G));
      }
   }

   private void i() {
      gg var1 = LarpClient.R().o();
      hb var2 = var1.g();
      t2 var3 = var2.FH;
      boolean var4 = var2.M;
      var2.FH = this.q.s();
      var2.M = (boolean)0;
      if (!var1.r()) {
         var2.FH = var3;
         var2.M = var4;
         this.W = "Could not save background.";
      } else {
         h.A("Menu", "Applied atmosphere {}", var2.FH.z);
         this.onClose();
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.e(var1);
      var3 = this.f(var3);

      try {
         boolean var5 = this.z();
         double var6 = (System.nanoTime() - this.U) / 1000000.0;
         double var8 = this.N();
         bj.n(var1, "Menu atmosphere", this.width / 2, this.S + 16, uh.F(ho.u, var8));
         if (this.B >= 220) {
            bj.n(var1, "Preview a scene, then apply it.", this.width / 2, this.S + 35, uh.F(ho.Z, var8));
         }

         for (int var10 = 0; var10 < this.M.length; var10++) {
            j5 var11 = this.M[var10];
            this.N[var10] = mb.m(var6, 60 + var10 * 65, 380.0, var5);
            var11.v = (boolean)(var10 == this.q.u().ordinal() ? 1 : 0);
            var11.d = (1.0 - this.N[var10]) * 7.0;
            var11.p = (boolean)(!this.T() && this.N[var10] > 0.85 ? 1 : 0);
         }

         this.e.p = (boolean)(!this.T() && this.q.u().ordinal() > 0 ? 1 : 0);
         this.b.p = (boolean)(!this.T() && this.q.u().ordinal() < 2 ? 1 : 0);
         String var18 = this.W.isEmpty() ? "Saved: " + this.q.D().z : this.W;
         bj.n(var1, bj.G(var18, this.G - 104), this.width / 2, this.L.statusY(), uh.F(this.W.isEmpty() ? ho.Z : -24915, var8));
         double var19 = var5 ? this.m.k(this.q.u().ordinal(), 320) : this.q.u().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            kv.Q(var1, this.width / 2 + (var13 - 1) * 10, this.L.statusY() + 16, 1, uh.F(ho.j, var8));
         }

         kv.Q(var1, this.width / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.L.statusY() + 16, 2, uh.F(ho.J, var8));
         this.drawControls(var1, var2, var3);
      } finally {
         this.B(var1);
      }
   }

   @Override
   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (jd var5 : this.p) {
         j5 var6 = (j5)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.M.length; var9++) {
            if (var6 == this.M[var9]) {
               var7 = this.N[var9];
            }
         }

         var6.G = this.N() * var7;
         if (this.T()) {
            var6.p = (boolean)0;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.T() && !(this.N() <= 0.15)) {
         long var9 = System.nanoTime();
         if (var7 != 0.0 && var9 - this.Q >= 150000000L) {
            this.Q = var9;
            this.q.m(var7 > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.T() || this.N() <= 0.15) {
         return true;
      }

      if (k2.a(var1) != 263 && k2.a(var1) != 262) {
         return super.keyPressed(var1);
      }

      this.q.m(k2.a(var1) == 263 ? -1 : 1);
      return true;
   }
}

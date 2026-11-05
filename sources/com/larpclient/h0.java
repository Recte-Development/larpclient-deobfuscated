package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public final class h0 extends hz {
   private final ci y;
   private final ef Q;
   private final ef[] H = new ef[]{new ef(0.0), new ef(0.0), new ef(0.0)};
   private final double[] n = new double[3];
   private String h = "";
   private long e;
   private long M;
   private ue N;
   private ue I;
   private ue O;
   private ey m;
   private final ue[] j = new ue[3];

   public h0(Screen var1) {
      super("Menu atmosphere", var1);
      this.y = new ci(LarpClient.F().f().t().n);
      this.Q = new ef(this.y.I().ordinal());
   }

   @Override
   public _w atmosphere() {
      return this.y.I();
   }

   protected void init() {
      this.z();
      this.m = ey.calculate(this.width, this.height);
      this.r(this.m.width(), this.m.height());
      this.M = System.nanoTime();

      for (int var1 = 0; var1 < this.j.length; var1++) {
         int var2 = var1;
         _w var3 = _w.values()[var1];
         p5.w var4 = this.m.card(var1);
         ue var5 = this.j[var1] = this.s(var3.S, () -> this.y.a(var2), var4.x(), var4.y(), var4.width());
         var5.W = var4.height();
         var5.K = (boolean)1;
         var5.v = var3x -> this.T(var3x, var2, var3);
      }

      this.N = this.s("‹", () -> this.y.k(-1), this.R + 16, this.m.statusY() - 3, 24);
      this.I = this.s("›", () -> this.y.k(1), this.R + this.U - 40, this.m.statusY() - 3, 24);
      this.N.W = this.I.W = 20;
      this.O = this.L("", this::D, this.m.motion());
      this.L("Back", this::onClose, this.m.back());
      this.L("Apply", this::l, this.m.apply());
      this.O.v = var1x -> k6.M(
         var1x, k6.n("Motion: " + (this.t() ? "On" : "Reduced"), this.O.A - 12), this.O.q + this.O.A / 2, this.O.J + 8, _3.U(com.larpclient.f.J, this.O.g)
      );
   }

   private ue L(String var1, Runnable var2, p5.w var3) {
      ue var4 = this.s(var1, var2, var3.x(), var3.y(), var3.width());
      var4.K = (boolean)1;
      return var4;
   }

   private boolean t() {
      return LarpClient.F().f().t().V;
   }

   private void D() {
      w9 var1 = LarpClient.F().f();
      xv var2 = var1.t();
      var2.V = (boolean)(!var2.V ? 1 : 0);
      if (!var1.S()) {
         var2.V = (boolean)(!var2.V ? 1 : 0);
         this.h = "Could not save motion preference.";
      }
   }

   private void T(GuiGraphicsExtractor var1, int var2, _w var3) {
      ue var4 = this.j[var2];
      double var5 = this.t() ? this.H[var2].m(var4.f ? 1.0 : 0.0, 280) : (var4.f ? 1.0 : 0.0);
      int var7 = var4.W - 28;
      dg.T(var1, var3, var4.q + 3, var4.J + 3, var4.A - 6, var7, var4.g, false);
      k6.M(var1, k6.n(var3.S, var4.A - 12), var4.q + var4.A / 2, var4.J + var4.W - 17, _3.U(com.larpclient.f.J, var4.g));
      if (var3 == this.y.W() && var7 >= 24) {
         pn.b(var1, var4.q + 7, var4.J + 7, 38, 16, 3, _3.U(-535485155, var4.g));
         k6.G(var1, "Saved", var4.q + 12, var4.J + 11, _3.U(-7873104, var4.g));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((var4.A - 16) * var5));
         pn.b(var1, var4.q + (var4.A - var8) / 2, var4.J + var4.W - 3, var8, 2, 1, _3.U(com.larpclient.f.C, var5 * var4.g));
      }
   }

   private void l() {
      w9 var1 = LarpClient.F().f();
      xv var2 = var1.t();
      _w var3 = var2.n;
      boolean var4 = var2.r;
      var2.n = this.y.Z();
      var2.r = (boolean)0;
      if (!var1.S()) {
         var2.n = var3;
         var2.r = var4;
         this.h = "Could not save background.";
      } else {
         mt.K("Menu", "Applied atmosphere {}", var2.n.S);
         this.onClose();
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.W(var1);
      var3 = this.T(var3);

      try {
         boolean var5 = this.t();
         double var6 = (System.nanoTime() - this.M) / 1000000.0;
         double var8 = this.A();
         k6.M(var1, "Menu atmosphere", this.width / 2, this.C + 16, _3.U(com.larpclient.f.J, var8));
         if (this.l >= 220) {
            k6.M(var1, "Preview a scene, then apply it.", this.width / 2, this.C + 35, _3.U(com.larpclient.f.s, var8));
         }

         for (int var10 = 0; var10 < this.j.length; var10++) {
            ue var11 = this.j[var10];
            this.n[var10] = m8.d(var6, 60 + var10 * 65, 380.0, var5);
            var11.f = (boolean)(var10 == this.y.I().ordinal() ? 1 : 0);
            var11.E = (1.0 - this.n[var10]) * 7.0;
            var11.X = (boolean)(!this.o() && this.n[var10] > 0.85 ? 1 : 0);
         }

         this.N.X = (boolean)(!this.o() && this.y.I().ordinal() > 0 ? 1 : 0);
         this.I.X = (boolean)(!this.o() && this.y.I().ordinal() < 2 ? 1 : 0);
         String var18 = this.h.isEmpty() ? "Saved: " + this.y.W().S : this.h;
         k6.M(var1, k6.n(var18, this.U - 104), this.width / 2, this.m.statusY(), _3.U(this.h.isEmpty() ? com.larpclient.f.s : -24915, var8));
         double var19 = var5 ? this.Q.m(this.y.I().ordinal(), 320) : this.y.I().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            c7.K(var1, this.width / 2 + (var13 - 1) * 10, this.m.statusY() + 16, 1, _3.U(com.larpclient.f.G, var8));
         }

         c7.K(var1, this.width / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.m.statusY() + 16, 2, _3.U(com.larpclient.f.C, var8));
         this.drawControls(var1, var2, var3);
      } finally {
         this.i(var1);
      }
   }

   @Override
   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (b var5 : this.G) {
         ue var6 = (ue)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.j.length; var9++) {
            if (var6 == this.j[var9]) {
               var7 = this.n[var9];
            }
         }

         var6.g = this.A() * var7;
         if (this.o()) {
            var6.X = (boolean)0;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.o() && !(this.A() <= 0.15)) {
         long var9 = System.nanoTime();
         if (var7 != 0.0 && var9 - this.e >= 150000000L) {
            this.e = var9;
            this.y.k(var7 > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.o() || this.A() <= 0.15) {
         return true;
      }

      if (mo.O(var1) != 263 && mo.O(var1) != 262) {
         return super.keyPressed(var1);
      }

      this.y.k(mo.O(var1) == 263 ? -1 : 1);
      return true;
   }
}

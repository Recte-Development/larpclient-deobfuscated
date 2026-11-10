package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class fi extends fs implements d7 {
   protected final Screen S;
   protected int V;
   protected int n;
   protected int w;
   protected int W;
   private final fq N = new fq(0.0);
   private boolean m;
   private boolean E;
   private long I;
   private double D = 1.0;
   private double G;
   private double L = 1.0;

   protected fi(String var1, Screen var2) {
      super(var1);
      this.S = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected si Z(String var1, Runnable var2, int var3, int var4, int var5) {
      si var6 = new si(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.O = 24;
      this.J.add(var6);
      return var6;
   }

   protected void c(int var1, int var2) {
      this.w = Math.min(var1, this.width - 24);
      this.W = Math.min(var2, this.height - 24);
      this.V = (this.width - this.w) / 2;
      this.n = (this.height - this.W) / 2;
   }

   protected void J(GuiGraphicsExtractor var1) {
      if (!this.m) {
         this.m = (boolean)1;
         this.N.p(0.0);
      }

      double var2 = LarpClient.N().h().k().w ? this.N.n(1.0, 260) : 1.0;
      if (this.E) {
         var2 = this.L * (1.0 - fq.y.x.q((System.nanoTime() - this.I) / 1.6E8));
      }

      this.D = var2;
      this.G = LarpClient.N().h().k().w ? (1.0 - var2) * 12.0 : 0.0;
      var1.pose().pushMatrix();
      var1.pose().translate(0.0F, (float)this.G);
      wc.F(var1, this.V, this.n, this.w, this.W);
      wc.b(var1, this.V, this.n, this.w, this.W, 7, vv.O(-451076058, var2));
   }

   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (j0 var5 : this.J) {
         if (var5.R) {
            if (var5 instanceof si var6) {
               var6.n = this.D;
            }

            if (this.E) {
               var5.V = (boolean)0;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void onClose() {
      if (!this.E) {
         if (!LarpClient.N().h().k().w) {
            this.minecraft.gui.setScreen(this.S);
         } else {
            this.E = (boolean)1;
            this.L = this.D;
            this.I = System.nanoTime();

            for (j0 var2 : this.J) {
               var2.V = (boolean)0;
            }
         }
      }
   }

   public void tick() {
      if (this.E && System.nanoTime() - this.I >= 160000000L) {
         this.minecraft.gui.setScreen(this.S);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return !this.m || this.D <= 0.15 || this.E || super.keyPressed(var1);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      return !this.m || this.D <= 0.15 || this.E || super.charTyped(var1);
   }

   private MouseButtonEvent T(MouseButtonEvent var1) {
      return new MouseButtonEvent(var1.x(), var1.y() - this.G, var1.buttonInfo());
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !this.m || this.D <= 0.15 || this.E || super.mouseClicked(this.T(var1), var2);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      return this.E || super.mouseDragged(this.T(var1), var2, var4);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      return super.mouseReleased(this.T(var1));
   }

   protected int i(int var1) {
      return (int)Math.round(var1 - this.G);
   }

   protected double q(double var1) {
      return var1 - this.G;
   }

   protected void T(GuiGraphicsExtractor var1) {
      var1.pose().popMatrix();
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.E;
   }

   protected boolean t() {
      return this.E;
   }

   protected double c() {
      return this.D;
   }
}

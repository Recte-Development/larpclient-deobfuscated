package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class hz extends h8 implements z2 {
   protected final Screen x;
   protected int R;
   protected int C;
   protected int U;
   protected int l;
   private final ef v = new ef(0.0);
   private boolean W;
   private boolean K;
   private long b;
   private double a = 1.0;
   private double g;
   private double J = 1.0;

   protected hz(String var1, Screen var2) {
      super(var1);
      this.x = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected ue s(String var1, Runnable var2, int var3, int var4, int var5) {
      ue var6 = new ue(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.W = 24;
      this.G.add(var6);
      return var6;
   }

   protected void r(int var1, int var2) {
      this.U = Math.min(var1, this.width - 24);
      this.l = Math.min(var2, this.height - 24);
      this.R = (this.width - this.U) / 2;
      this.C = (this.height - this.l) / 2;
   }

   protected void W(GuiGraphicsExtractor var1) {
      if (!this.W) {
         this.W = (boolean)1;
         this.v.Q(0.0);
      }

      double var2 = LarpClient.F().f().t().V ? this.v.m(1.0, 260) : 1.0;
      if (this.K) {
         var2 = this.J * (1.0 - ef.q.A.k((System.nanoTime() - this.b) / 1.6E8));
      }

      this.a = var2;
      this.g = LarpClient.F().f().t().V ? (1.0 - var2) * 12.0 : 0.0;
      var1.pose().pushMatrix();
      var1.pose().translate(0.0F, (float)this.g);
      pn.c(var1, this.R, this.C, this.U, this.l);
      pn.b(var1, this.R, this.C, this.U, this.l, 7, _3.U(-451076058, var2));
   }

   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (b var5 : this.G) {
         if (var5.D) {
            if (var5 instanceof ue var6) {
               var6.g = this.a;
            }

            if (this.K) {
               var5.X = (boolean)0;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void onClose() {
      if (!this.K) {
         if (!LarpClient.F().f().t().V) {
            this.minecraft.gui.setScreen(this.x);
         } else {
            this.K = (boolean)1;
            this.J = this.a;
            this.b = System.nanoTime();

            for (b var2 : this.G) {
               var2.X = (boolean)0;
            }
         }
      }
   }

   public void tick() {
      if (this.K && System.nanoTime() - this.b >= 160000000L) {
         this.minecraft.gui.setScreen(this.x);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return !this.W || this.a <= 0.15 || this.K || super.keyPressed(var1);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      return !this.W || this.a <= 0.15 || this.K || super.charTyped(var1);
   }

   private MouseButtonEvent d(MouseButtonEvent var1) {
      return new MouseButtonEvent(var1.x(), var1.y() - this.g, var1.buttonInfo());
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !this.W || this.a <= 0.15 || this.K || super.mouseClicked(this.d(var1), var2);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      return this.K || super.mouseDragged(this.d(var1), var2, var4);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      return super.mouseReleased(this.d(var1));
   }

   protected int T(int var1) {
      return (int)Math.round(var1 - this.g);
   }

   protected double P(double var1) {
      return var1 - this.g;
   }

   protected void i(GuiGraphicsExtractor var1) {
      var1.pose().popMatrix();
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.K;
   }

   protected boolean o() {
      return this.K;
   }

   protected double A() {
      return this.a;
   }
}

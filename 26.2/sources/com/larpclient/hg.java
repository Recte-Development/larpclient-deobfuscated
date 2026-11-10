package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class hg extends h3 implements ma {
   protected final Screen P;
   protected int X;
   protected int S;
   protected int G;
   protected int B;
   private final jp r = new jp(0.0);
   private boolean u;
   private boolean Y;
   private long n;
   private double w = 1.0;
   private double D;
   private double j = 1.0;

   protected hg(String var1, Screen var2) {
      super(var1);
      this.P = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected j5 N(String var1, Runnable var2, int var3, int var4, int var5) {
      j5 var6 = new j5(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.J = 24;
      this.p.add(var6);
      return var6;
   }

   protected void h(int var1, int var2) {
      this.G = Math.min(var1, this.width - 24);
      this.B = Math.min(var2, this.height - 24);
      this.X = (this.width - this.G) / 2;
      this.S = (this.height - this.B) / 2;
   }

   protected void e(GuiGraphicsExtractor var1) {
      if (!this.u) {
         this.u = (boolean)1;
         this.r.F(0.0);
      }

      double var2 = LarpClient.R().o().g().L ? this.r.k(1.0, 260) : 1.0;
      if (this.Y) {
         var2 = this.j * (1.0 - jp.m.h.s((System.nanoTime() - this.n) / 1.6E8));
      }

      this.w = var2;
      this.D = LarpClient.R().o().g().L ? (1.0 - var2) * 12.0 : 0.0;
      var1.pose().pushMatrix();
      var1.pose().translate(0.0F, (float)this.D);
      wp.R(var1, this.X, this.S, this.G, this.B);
      wp.I(var1, this.X, this.S, this.G, this.B, 7, uh.F(-451076058, var2));
   }

   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (jd var5 : this.p) {
         if (var5.i) {
            if (var5 instanceof j5 var6) {
               var6.G = this.w;
            }

            if (this.Y) {
               var5.p = (boolean)0;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void onClose() {
      if (!this.Y) {
         if (!LarpClient.R().o().g().L) {
            this.minecraft.gui.setScreen(this.P);
         } else {
            this.Y = (boolean)1;
            this.j = this.w;
            this.n = System.nanoTime();

            for (jd var2 : this.p) {
               var2.p = (boolean)0;
            }
         }
      }
   }

   public void tick() {
      if (this.Y && System.nanoTime() - this.n >= 160000000L) {
         this.minecraft.gui.setScreen(this.P);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return !this.u || this.w <= 0.15 || this.Y || super.keyPressed(var1);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      return !this.u || this.w <= 0.15 || this.Y || super.charTyped(var1);
   }

   private MouseButtonEvent y(MouseButtonEvent var1) {
      return new MouseButtonEvent(var1.x(), var1.y() - this.D, var1.buttonInfo());
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !this.u || this.w <= 0.15 || this.Y || super.mouseClicked(this.y(var1), var2);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      return this.Y || super.mouseDragged(this.y(var1), var2, var4);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      return super.mouseReleased(this.y(var1));
   }

   protected int f(int var1) {
      return (int)Math.round(var1 - this.D);
   }

   protected double I(double var1) {
      return var1 - this.D;
   }

   protected void B(GuiGraphicsExtractor var1) {
      var1.pose().popMatrix();
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.Y;
   }

   protected boolean T() {
      return this.Y;
   }

   protected double N() {
      return this.w;
   }
}

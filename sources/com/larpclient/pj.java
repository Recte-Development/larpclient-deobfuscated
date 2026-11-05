package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class pj extends pw implements hd {
   protected final Screen U;
   protected int V;
   protected int o;
   protected int s;
   protected int a;
   private final u7 M = new u7(0.0);
   private boolean p;
   private boolean B;
   private long u;
   private double H = 1.0;
   private double O;
   private double K = 1.0;

   protected pj(String var1, Screen var2) {
      super(var1);
      this.U = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected pd r(String var1, Runnable var2, int var3, int var4, int var5) {
      pd var6 = new pd(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.O = 24;
      this.k.add(var6);
      return var6;
   }

   protected void p(int var1, int var2) {
      this.s = Math.min(var1, this.width - 24);
      this.a = Math.min(var2, this.height - 24);
      this.V = (this.width - this.s) / 2;
      this.o = (this.height - this.a) / 2;
   }

   protected void e(GuiGraphicsExtractor var1) {
      if (!this.p) {
         this.p = (boolean)1;
         this.M.c(0.0);
      }

      double var2 = LarpClient.y().v().E().o ? this.M.y(1.0, 260) : 1.0;
      if (this.B) {
         var2 = this.K * (1.0 - u7.p.N.m((System.nanoTime() - this.u) / 1.6E8));
      }

      this.H = var2;
      this.O = LarpClient.y().v().E().o ? (1.0 - var2) * 12.0 : 0.0;
      var1.pose().pushMatrix();
      var1.pose().translate(0.0F, (float)this.O);
      ll.D(var1, this.V, this.o, this.s, this.a);
      ll.N(var1, this.V, this.o, this.s, this.a, 7, oh.S(-451076058, var2));
   }

   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (oq var5 : this.k) {
         if (var5.F) {
            if (var5 instanceof pd var6) {
               var6.f = this.H;
            }

            if (this.B) {
               var5.x = (boolean)0;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void onClose() {
      if (!this.B) {
         if (!LarpClient.y().v().E().o) {
            this.minecraft.gui.setScreen(this.U);
         } else {
            this.B = (boolean)1;
            this.K = this.H;
            this.u = System.nanoTime();

            for (oq var2 : this.k) {
               var2.x = (boolean)0;
            }
         }
      }
   }

   public void tick() {
      if (this.B && System.nanoTime() - this.u >= 160000000L) {
         this.minecraft.gui.setScreen(this.U);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return !this.p || this.H <= 0.15 || this.B || super.keyPressed(var1);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      return !this.p || this.H <= 0.15 || this.B || super.charTyped(var1);
   }

   private MouseButtonEvent B(MouseButtonEvent var1) {
      return new MouseButtonEvent(var1.x(), var1.y() - this.O, var1.buttonInfo());
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !this.p || this.H <= 0.15 || this.B || super.mouseClicked(this.B(var1), var2);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      return this.B || super.mouseDragged(this.B(var1), var2, var4);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      return super.mouseReleased(this.B(var1));
   }

   protected int B(int var1) {
      return (int)Math.round(var1 - this.O);
   }

   protected double b(double var1) {
      return var1 - this.O;
   }

   protected void V(GuiGraphicsExtractor var1) {
      var1.pose().popMatrix();
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.B;
   }

   protected boolean G() {
      return this.B;
   }

   protected double B() {
      return this.H;
   }
}

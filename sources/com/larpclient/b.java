package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class b {
   public int q;
   public int J;
   public int A;
   public int W = 24;
   public boolean D = (boolean)1;
   public boolean X = (boolean)1;
   public boolean l;
   public boolean R;

   public void bounds(int var1, int var2, int var3) {
      this.q = var1;
      this.J = var2;
      this.A = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.D && this.X && c7.T(var1, var3, this.q, this.J, this.A, this.W);
   }

   public abstract void render(GuiGraphicsExtractor var1, int var2, int var3);

   public boolean click(double var1, double var3, int var5) {
      return false;
   }

   public void drag(double var1, double var3) {
   }

   public void release() {
   }

   public boolean key(int var1) {
      return false;
   }

   public boolean character(int var1) {
      return false;
   }

   public boolean captureMouse(int var1) {
      return false;
   }

   public void blur() {
      this.l = (boolean)0;
      this.R = (boolean)0;
      this.release();
   }
}

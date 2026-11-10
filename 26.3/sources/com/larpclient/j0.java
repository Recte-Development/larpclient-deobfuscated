package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class j0 {
   public int P;
   public int T;
   public int L;
   public int O = 24;
   public boolean R = (boolean)1;
   public boolean V = (boolean)1;
   public boolean F;
   public boolean X;

   public void bounds(int var1, int var2, int var3) {
      this.P = var1;
      this.T = var2;
      this.L = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.R && this.V && h5.H(var1, var3, this.P, this.T, this.L, this.O);
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
      this.F = (boolean)0;
      this.X = (boolean)0;
      this.release();
   }
}

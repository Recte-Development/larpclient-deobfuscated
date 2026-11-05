package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class oq {
   public int B;
   public int L;
   public int v;
   public int O = 24;
   public boolean F = (boolean)1;
   public boolean x = (boolean)1;
   public boolean A;
   public boolean h;

   public void bounds(int var1, int var2, int var3) {
      this.B = var1;
      this.L = var2;
      this.v = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.F && this.x && fr.Q(var1, var3, this.B, this.L, this.v, this.O);
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
      this.A = (boolean)0;
      this.h = (boolean)0;
      this.release();
   }
}

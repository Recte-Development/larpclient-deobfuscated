package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class jd {
   public int S;
   public int u;
   public int A;
   public int J = 24;
   public boolean i = (boolean)1;
   public boolean p = (boolean)1;
   public boolean T;
   public boolean W;

   public void bounds(int var1, int var2, int var3) {
      this.S = var1;
      this.u = var2;
      this.A = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.i && this.p && kv.f(var1, var3, this.S, this.u, this.A, this.J);
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
      this.T = (boolean)0;
      this.W = (boolean)0;
      this.release();
   }
}

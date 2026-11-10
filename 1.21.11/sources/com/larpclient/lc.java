package com.larpclient;

import net.minecraft.class_332;

public abstract class lc {
   public int D;
   public int g;
   public int d;
   public int w = 24;
   public boolean I = (boolean)1;
   public boolean N = (boolean)1;
   public boolean M;
   public boolean q;

   public void bounds(int var1, int var2, int var3) {
      this.D = var1;
      this.g = var2;
      this.d = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.I && this.N && gu.X(var1, var3, this.D, this.g, this.d, this.w);
   }

   public abstract void render(class_332 var1, int var2, int var3);

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
      this.M = (boolean)0;
      this.q = (boolean)0;
      this.release();
   }
}

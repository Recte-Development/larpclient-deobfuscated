package com.larpclient;

public final class ci {
   private final _w u;
   private int G;

   public ci(_w var1) {
      this.u = var1;
      this.G = var1.ordinal();
   }

   public _w I() {
      return _w.values()[this.G];
   }

   public _w W() {
      return this.u;
   }

   public void a(int var1) {
      this.G = Math.clamp(var1, 0, _w.values().length - 1);
   }

   public void k(int var1) {
      this.a(this.G + Integer.signum(var1));
   }

   public _w N() {
      return this.u;
   }

   public _w Z() {
      return this.I();
   }
}

package com.larpclient;

public enum z5 {
   o("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   k("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   f("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   r("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   W("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String g;
   public final String U;
   public final double N;
   public final double e;
   public final double s;
   public final float L;
   public final float H;
   public final float i;

   z5(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.g = var3;
      this.U = var4;
      this.N = var5;
      this.e = var7;
      this.s = var9;
      this.L = var11;
      this.H = var12;
      this.i = var13;
   }

   public static z5 y(String var0) {
      for (z5 var4 : values()) {
         if (var4.g.equals(var0)) {
            return var4;
         }
      }

      return o;
   }

   public boolean o() {
      return this == r || this == W;
   }

   public boolean v() {
      return this != W;
   }
}

package com.larpclient;

public enum co {
   q("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   E("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   R("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   F("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   o("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String L;
   public final String P;
   public final double n;
   public final double f;
   public final double M;
   public final float r;
   public final float y;
   public final float S;

   co(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.L = var3;
      this.P = var4;
      this.n = var5;
      this.f = var7;
      this.M = var9;
      this.r = var11;
      this.y = var12;
      this.S = var13;
   }

   public static co o(String var0) {
      for (co var4 : values()) {
         if (var4.L.equals(var0)) {
            return var4;
         }
      }

      return q;
   }

   public boolean v() {
      return this == F || this == o;
   }

   public boolean f() {
      return this != o;
   }
}

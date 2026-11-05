package com.larpclient;

public enum yi {
   x("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   s("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   q("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   m("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   D("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String t;
   public final String h;
   public final double H;
   public final double p;
   public final double b;
   public final float r;
   public final float K;
   public final float i;

   yi(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.t = var3;
      this.h = var4;
      this.H = var5;
      this.p = var7;
      this.b = var9;
      this.r = var11;
      this.K = var12;
      this.i = var13;
   }

   public static yi F(String var0) {
      for (yi var4 : values()) {
         if (var4.t.equals(var0)) {
            return var4;
         }
      }

      return x;
   }

   public boolean E() {
      return this == m || this == D;
   }

   public boolean x() {
      return this != D;
   }
}

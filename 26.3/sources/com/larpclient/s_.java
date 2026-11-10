package com.larpclient;

public enum s_ {
   e("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   O("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   I("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   r("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   s("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String N;
   public final String p;
   public final double o;
   public final double c;
   public final double v;
   public final float E;
   public final float k;
   public final float Y;

   s_(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.N = var3;
      this.p = var4;
      this.o = var5;
      this.c = var7;
      this.v = var9;
      this.E = var11;
      this.k = var12;
      this.Y = var13;
   }

   public static s_ C(String var0) {
      for (s_ var4 : values()) {
         if (var4.N.equals(var0)) {
            return var4;
         }
      }

      return e;
   }

   public boolean d() {
      return this == r || this == s;
   }

   public boolean D() {
      return this != s;
   }
}

package com.larpclient;

public enum tc {
   c("Bayonet", "bayonet", 1.0, 6.2, 0.85, 0.0F, 0.0F, 0.0F),
   S("Stiletto", "stiletto", 1.0, 3.933333333333333, 1.0, 0.0F, 0.0F, 0.0F),
   D("Butterfly", "butterfly", 0.7666666666666667, 4.1, 1.0, -0.021F, 0.0F, 0.0F),
   x("Luxury Watch", "dress_watch", 0.3, 5.2, 1.0, 0.0F, 0.0F, 0.0F),
   k("Ledger Nano S", "ledger_nano_s", 1.0, 1.0, 1.0, 0.0F, 0.0F, 0.0F);

   public final String P;
   public final String A;
   public final double z;
   public final double v;
   public final double R;
   public final float Z;
   public final float X;
   public final float d;

   tc(String var3, String var4, double var5, double var7, double var9, float var11, float var12, float var13) {
      this.P = var3;
      this.A = var4;
      this.z = var5;
      this.v = var7;
      this.R = var9;
      this.Z = var11;
      this.X = var12;
      this.d = var13;
   }

   public static tc A(String var0) {
      for (tc var4 : values()) {
         if (var4.P.equals(var0)) {
            return var4;
         }
      }

      return c;
   }

   public boolean L() {
      return this == x || this == k;
   }

   public boolean z() {
      return this != k;
   }
}

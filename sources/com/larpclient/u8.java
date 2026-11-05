package com.larpclient;

public enum u8 {
   o(5.0, 48, 12.0, 0.6, 0.38, 12, 0.32),
   z(8.0, 96, 16.0, 0.82, 0.55, 18, 0.44),
   S(10.0, 128, 18.0, 0.94, 0.65, 24, 0.5);

   public final double I;
   public final double x;
   public final double w;
   public final double B;
   public final double m;
   public final int n;
   public final int h;

   u8(double var3, int var5, double var6, double var8, double var10, int var12, double var13) {
      this.I = var3;
      this.n = var5;
      this.x = var6;
      this.w = var8;
      this.B = var10;
      this.h = var12;
      this.m = var13;
   }

   public int n() {
      return Math.min(this.n, _k.m(this.n));
   }

   public int z() {
      return Math.max(8, _k.m(this.h));
   }

   public boolean O() {
      return _k.t != m5.n.N;
   }

   public static u8 R(String var0) {
      return switch (var0) {
         case "Subtle" -> o;
         case "Cinematic" -> S;
         default -> z;
      };
   }
}

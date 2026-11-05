package com.larpclient;

public enum ol {
   G(5.0, 48, 12.0, 0.6, 0.38, 12, 0.32),
   r(8.0, 96, 16.0, 0.82, 0.55, 18, 0.44),
   R(10.0, 128, 18.0, 0.94, 0.65, 24, 0.5);

   public final double p;
   public final double h;
   public final double D;
   public final double j;
   public final double c;
   public final int y;
   public final int J;

   ol(double var3, int var5, double var6, double var8, double var10, int var12, double var13) {
      this.p = var3;
      this.y = var5;
      this.h = var6;
      this.D = var8;
      this.j = var10;
      this.J = var12;
      this.c = var13;
   }

   public int t() {
      return Math.min(this.y, lo.H(this.y));
   }

   public int d() {
      return Math.max(8, lo.H(this.J));
   }

   public boolean y() {
      return lo.q != jk.x.i;
   }

   public static ol W(String var0) {
      return switch (var0) {
         case "Subtle" -> G;
         case "Cinematic" -> R;
         default -> r;
      };
   }
}

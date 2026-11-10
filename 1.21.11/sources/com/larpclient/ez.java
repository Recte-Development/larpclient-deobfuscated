package com.larpclient;

public final class ez {
   public static final int h = -1381654;
   public static final int i = -6250336;
   public static int a = -1381654;
   public static int f = -6250336;
   public static int Z = -434562791;
   public static int M = -920509918;
   public static int K = -534173399;
   public static int H = -1381654;
   public static int PH = -6250336;
   public static int D = -13882324;
   public static int C = -1341058799;
   public static int t = -8947849;
   public static int B = -921036262;
   public static int p = -98426334;
   public static int d = -15132391;
   public static final int c = -1381654;
   public static final int R = -7873104;
   public static final int L = -996986;
   public static final int Q = -24915;
   public static int Pi = 2;
   public static int m = 3;
   public static int X = 3;
   public static int V = 5;
   public static int j = 7;
   public static int U = 5;
   public static int g = 0;
   public static int PX = 0;
   public static final int n = 100;
   public static final int PR = 160;
   public static final int F = 180;
   public static final int l = 150;
   public static final int z = 160;
   public static final int N = 160;
   public static final int u = 200;
   public static final int G = 280;
   public static final int r = 270;
   public static final int y = 4;
   public static final int v = 26;
   public static final int A = 38;
   public static int PC = -533976020;
   public static int P = -233038818;
   public static int I = 1075978786;
   public static int k = -8947844;
   public static int o = 569043690;
   public static int J = 166390506;
   public static final int Pv = 8;
   public static final int s = 12;
   public static final int T = 8;
   public static final int W = 11;
   public static final int E = 22;
   public static final int e = 22;
   public static final int P6 = 20;
   public static final int q = 44;
   public static final int S = 44;
   private static boolean b;
   private static boolean Y;
   private static long w;
   private static double O = 1.0;
   private static ez.j x = ez.j.DEFAULT;

   public static ez.m F(boolean var0, double var1) {
      return Q(var0, var1, false);
   }

   public static int X() {
      return ei.R(d, O);
   }

   public static boolean d() {
      return Y;
   }

   public static int X(int var0, int var1) {
      return jx.readableAccent(P(var0), var1 | 0xFF000000);
   }

   public static int P(int var0) {
      return b ? V(0.5 + 0.5 * Math.sin(w / 800.0 + var0 * 0.008)) : a;
   }

   public static ez.j x() {
      return x;
   }

   public static ez.m Q(boolean var0, double var1, boolean var3) {
      return G(var0, var1, var3, ez.j.DEFAULT);
   }

   public static ez.m G(boolean var0, double var1, boolean var3, ez.j var4) {
      int[] var5 = v();
      boolean var6 = b;
      boolean var7 = Y;
      long var8 = w;
      double var10 = O;
      ez.j var12 = x;
      x = var4;
      b = var3;
      Y = (boolean)1;
      w = System.currentTimeMillis();
      O = var1;
      if (var0) {
         Z = -592136;
         M = -1;
         K = -921100;
         H = -15263974;
         PH = -10592664;
         D = -2302750;
         C = -1447443;
         t = -7697772;
         B = -1;
         p = -1;
         d = -1;
         PC = -1052685;
         P = -1184272;
         I = -855307;
         k = -6710880;
         o = 385875968;
         J = 520093696;
         g = -1250065;
         PX = 706485284;
      } else {
         int var13 = w(-16119283, 0.09);
         int var14 = w(-15263973, 0.11);
         int var15 = w(-14079698, 0.2);
         H = -1184272;
         PH = -6645085;
         t = -9934735;
         Z = var14;
         D = var15;
         M = ei.W(var14, ei.W(var13, H, 0.035), 0.62);
         K = ei.W(var14, ei.W(var13, H, 0.06), 0.62);
         C = var13;
         B = ei.W(var13, H, 0.04);
         p = ei.W(var14, H, 0.025);
         d = var14;
         PC = ei.W(var14, H, 0.05);
         P = var13;
         I = ei.D(var14, 0.25);
         k = -6908257;
         o = ei.D(ei.W(var15, H, 0.12), 0.75);
         J = ei.D(var15, 0.6);
         g = ei.D(var13, 0.45);
         PX = -1946157056;
      }

      g = ei.R(g, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      Z = ei.R(Z, var1);
      P = ei.R(P, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      Pi = L(4, var16);
      m = L(6, var16);
      X = L(8, var16);
      V = L(7, var16);
      j = L(16, var16);
      U = L(10, var16);
      return new ez.m(var5, var6, var7, var8, var10, var12);
   }

   public static double v() {
      int var0 = a >>> 16 & 0xFF;
      int var1 = a >>> 8 & 0xFF;
      int var2 = a & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / var3);
   }

   static int w(int var0, double var1) {
      int var3 = a >>> 16 & 0xFF;
      int var4 = a >>> 8 & 0xFF;
      int var5 = a & 0xFF;
      double var6 = 0.2126 * var3 + 0.7152 * var4 + 0.0722 * var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round(var8[var11] + (var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] v() {
      return new int[]{Z, M, K, H, PH, D, C, t, B, p, d, PC, P, I, k, o, J, g, PX, Pi, m, X, V, j, U};
   }

   private static int L(int var0, double var1) {
      return (int)Math.round(var0 * var1);
   }

   public static double h() {
      return O;
   }

   public static int i(int var0, double var1) {
      return ei.D(P(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int V(double var0) {
      return ei.W(a, f, var0);
   }

   public record j(String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift) {
      public static final ez.j DEFAULT = new ez.j("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }

   public record m(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, ez.j look) implements AutoCloseable {
      @Override
      public void close() {
         ez.Z = this.previous[0];
         ez.M = this.previous[1];
         ez.K = this.previous[2];
         ez.H = this.previous[3];
         ez.PH = this.previous[4];
         ez.D = this.previous[5];
         ez.C = this.previous[6];
         ez.t = this.previous[7];
         ez.B = this.previous[8];
         ez.p = this.previous[9];
         ez.d = this.previous[10];
         ez.PC = this.previous[11];
         ez.P = this.previous[12];
         ez.I = this.previous[13];
         ez.k = this.previous[14];
         ez.o = this.previous[15];
         ez.J = this.previous[16];
         ez.g = this.previous[17];
         ez.PX = this.previous[18];
         ez.Pi = this.previous[19];
         ez.m = this.previous[20];
         ez.X = this.previous[21];
         ez.V = this.previous[22];
         ez.j = this.previous[23];
         ez.U = this.previous[24];
         ez.b = this.animatedAccent;
         ez.Y = this.style;
         ez.w = this.millis;
         ez.O = this.opacity;
         ez.x = this.look;
      }
   }
}

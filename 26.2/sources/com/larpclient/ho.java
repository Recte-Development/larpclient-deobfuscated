package com.larpclient;

public final class ho {
   public static final int dj = -1381654;
   public static final int l = -6250336;
   public static int J = -1381654;
   public static int ds = -6250336;
   public static int t = -434562791;
   public static int f = -920509918;
   public static int e = -534173399;
   public static int u = -1381654;
   public static int Z = -6250336;
   public static int c = -13882324;
   public static int dy = -1341058799;
   public static int j = -8947849;
   public static int B = -921036262;
   public static int dw = -98426334;
   public static int A = -15132391;
   public static final int K = -1381654;
   public static final int m = -7873104;
   public static final int U = -996986;
   public static final int p = -24915;
   public static int d = 2;
   public static int Q = 3;
   public static int D = 3;
   public static int k = 5;
   public static int x = 7;
   public static int dr = 5;
   public static int F = 0;
   public static int v = 0;
   public static final int C = 100;
   public static final int q = 160;
   public static final int W = 180;
   public static final int P = 150;
   public static final int h = 160;
   public static final int z = 160;
   public static final int M = 200;
   public static final int X = 280;
   public static final int n = 270;
   public static final int y = 4;
   public static final int r = 26;
   public static final int E = 38;
   public static int o = -533976020;
   public static int dB = -233038818;
   public static int a = 1075978786;
   public static int s = -8947844;
   public static int Y = 569043690;
   public static int S = 166390506;
   public static final int G = 8;
   public static final int L = 12;
   public static final int i = 8;
   public static final int g = 11;
   public static final int V = 22;
   public static final int I = 22;
   public static final int N = 20;
   public static final int w = 44;
   public static final int O = 44;
   private static boolean dv;
   private static boolean b;
   private static long T;
   private static double H = 1.0;
   private static ho.p R = ho.p.DEFAULT;

   public static ho.y t(boolean var0, double var1) {
      return K(var0, var1, false);
   }

   public static int c() {
      return uh.F(A, H);
   }

   public static boolean S() {
      return b;
   }

   public static int S(int var0, int var1) {
      return lv.readableAccent(Q(var0), var1 | 0xFF000000);
   }

   public static int Q(int var0) {
      return dv ? B(0.5 + 0.5 * Math.sin(T / 800.0 + var0 * 0.008)) : J;
   }

   public static ho.p m() {
      return R;
   }

   public static ho.y K(boolean var0, double var1, boolean var3) {
      return V(var0, var1, var3, ho.p.DEFAULT);
   }

   public static ho.y V(boolean var0, double var1, boolean var3, ho.p var4) {
      int[] var5 = n();
      boolean var6 = dv;
      boolean var7 = b;
      long var8 = T;
      double var10 = H;
      ho.p var12 = R;
      R = var4;
      dv = var3;
      b = (boolean)1;
      T = System.currentTimeMillis();
      H = var1;
      if (var0) {
         t = -592136;
         f = -1;
         e = -921100;
         u = -15263974;
         Z = -10592664;
         c = -2302750;
         dy = -1447443;
         j = -7697772;
         B = -1;
         dw = -1;
         A = -1;
         o = -1052685;
         dB = -1184272;
         a = -855307;
         s = -6710880;
         Y = 385875968;
         S = 520093696;
         F = -1250065;
         v = 706485284;
      } else {
         int var13 = V(-16119283, 0.09);
         int var14 = V(-15263973, 0.11);
         int var15 = V(-14079698, 0.2);
         u = -1184272;
         Z = -6645085;
         j = -9934735;
         t = var14;
         c = var15;
         f = uh.q(var14, uh.q(var13, u, 0.035), 0.62);
         e = uh.q(var14, uh.q(var13, u, 0.06), 0.62);
         dy = var13;
         B = uh.q(var13, u, 0.04);
         dw = uh.q(var14, u, 0.025);
         A = var14;
         o = uh.q(var14, u, 0.05);
         dB = var13;
         a = uh.H(var14, 0.25);
         s = -6908257;
         Y = uh.H(uh.q(var15, u, 0.12), 0.75);
         S = uh.H(var15, 0.6);
         F = uh.H(var13, 0.45);
         v = -1946157056;
      }

      F = uh.F(F, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      t = uh.F(t, var1);
      dB = uh.F(dB, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      d = Z(4, var16);
      Q = Z(6, var16);
      D = Z(8, var16);
      k = Z(7, var16);
      x = Z(16, var16);
      dr = Z(10, var16);
      return new ho.y(var5, var6, var7, var8, var10, var12);
   }

   public static double s() {
      int var0 = J >>> 16 & 0xFF;
      int var1 = J >>> 8 & 0xFF;
      int var2 = J & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / var3);
   }

   static int V(int var0, double var1) {
      int var3 = J >>> 16 & 0xFF;
      int var4 = J >>> 8 & 0xFF;
      int var5 = J & 0xFF;
      double var6 = 0.2126 * var3 + 0.7152 * var4 + 0.0722 * var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round(var8[var11] + (var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] n() {
      return new int[]{t, f, e, u, Z, c, dy, j, B, dw, A, o, dB, a, s, Y, S, F, v, d, Q, D, k, x, dr};
   }

   private static int Z(int var0, double var1) {
      return (int)Math.round(var0 * var1);
   }

   public static double H() {
      return H;
   }

   public static int q(int var0, double var1) {
      return uh.H(Q(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int B(double var0) {
      return uh.q(J, ds, var0);
   }

   public record p(String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift) {
      public static final ho.p DEFAULT = new ho.p("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }

   public record y(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, ho.p look) implements AutoCloseable {
      @Override
      public void close() {
         ho.t = this.previous[0];
         ho.f = this.previous[1];
         ho.e = this.previous[2];
         ho.u = this.previous[3];
         ho.Z = this.previous[4];
         ho.c = this.previous[5];
         ho.dy = this.previous[6];
         ho.j = this.previous[7];
         ho.B = this.previous[8];
         ho.dw = this.previous[9];
         ho.A = this.previous[10];
         ho.o = this.previous[11];
         ho.dB = this.previous[12];
         ho.a = this.previous[13];
         ho.s = this.previous[14];
         ho.Y = this.previous[15];
         ho.S = this.previous[16];
         ho.F = this.previous[17];
         ho.v = this.previous[18];
         ho.d = this.previous[19];
         ho.Q = this.previous[20];
         ho.D = this.previous[21];
         ho.k = this.previous[22];
         ho.x = this.previous[23];
         ho.dr = this.previous[24];
         ho.dv = this.animatedAccent;
         ho.b = this.style;
         ho.T = this.millis;
         ho.H = this.opacity;
         ho.R = this.look;
      }
   }
}

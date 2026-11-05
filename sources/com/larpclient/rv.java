package com.larpclient;

public final class rv {
   public static final int j = -1381654;
   public static final int O = -6250336;
   public static int e = -1381654;
   public static int GQ = -6250336;
   public static int s = -434562791;
   public static int H = -920509918;
   public static int Z = -534173399;
   public static int P = -1381654;
   public static int G0 = -6250336;
   public static int f = -13882324;
   public static int q = -1341058799;
   public static int r = -8947849;
   public static int z = -921036262;
   public static int Y = -98426334;
   public static int w = -15132391;
   public static final int b = -1381654;
   public static final int G = -7873104;
   public static final int R = -996986;
   public static final int GA = -24915;
   public static int U = 2;
   public static int g = 3;
   public static int N = 3;
   public static int Gj = 5;
   public static int T = 7;
   public static int S = 5;
   public static int i = 5;
   public static int d = 0;
   public static int p = 0;
   public static final int v = 100;
   public static final int u = 160;
   public static final int a = 180;
   public static final int D = 150;
   public static final int L = 160;
   public static final int Q = 160;
   public static final int GN = 200;
   public static final int I = 280;
   public static final int x = 270;
   public static final int M = 4;
   public static final int J = 26;
   public static final int E = 38;
   public static int K = -533976020;
   public static int Gp = -233038818;
   public static int V = 1075978786;
   public static int l = -8947844;
   public static int h = 569043690;
   public static int C = 166390506;
   public static final int F = 8;
   public static final int A = 12;
   public static final int k = 8;
   public static final int c = 11;
   public static final int m = 22;
   public static final int t = 22;
   public static final int o = 20;
   public static final int W = 44;
   public static final int y = 44;
   private static boolean n;
   private static boolean G9;
   private static long G2;
   private static double B = 1.0;
   private static rv.t X = rv.t.DEFAULT;

   public static rv.s O(boolean var0, double var1) {
      return z(var0, var1, false);
   }

   public static int B() {
      return oh.S(w, B);
   }

   public static boolean u() {
      return G9;
   }

   public static int a(int var0, int var1) {
      return jd.readableAccent(m(var0), var1 | 0xFF000000);
   }

   public static int m(int var0) {
      return n ? d(0.5 + 0.5 * Math.sin(G2 / 800.0 + var0 * 0.008)) : e;
   }

   public static rv.t D() {
      return X;
   }

   public static rv.s z(boolean var0, double var1, boolean var3) {
      return v(var0, var1, var3, rv.t.DEFAULT);
   }

   public static rv.s v(boolean var0, double var1, boolean var3, rv.t var4) {
      int[] var5 = Q();
      boolean var6 = n;
      boolean var7 = G9;
      long var8 = G2;
      double var10 = B;
      rv.t var12 = X;
      X = var4;
      n = var3;
      G9 = (boolean)1;
      G2 = System.currentTimeMillis();
      B = var1;
      if (var0) {
         s = -592136;
         H = -1;
         Z = -921100;
         P = -15263974;
         G0 = -10592664;
         f = -2302750;
         q = -1447443;
         r = -7697772;
         z = -1;
         Y = -1;
         w = -1;
         K = -1052685;
         Gp = -1184272;
         V = -855307;
         l = -6710880;
         h = 385875968;
         C = 520093696;
         d = -1250065;
         p = 706485284;
      } else {
         int var13 = R(-16119283, 0.09);
         int var14 = R(-15263973, 0.11);
         int var15 = R(-14079698, 0.2);
         P = -1184272;
         G0 = -6645085;
         r = -9934735;
         s = var14;
         f = var15;
         H = oh.j(var14, oh.j(var13, P, 0.035), 0.62);
         Z = oh.j(var14, oh.j(var13, P, 0.06), 0.62);
         q = var13;
         z = oh.j(var13, P, 0.04);
         Y = oh.j(var14, P, 0.025);
         w = var14;
         K = oh.j(var14, P, 0.05);
         Gp = var13;
         V = oh.i(var14, 0.25);
         l = -6908257;
         h = oh.i(oh.j(var15, P, 0.12), 0.75);
         C = oh.i(var15, 0.6);
         d = oh.i(var13, 0.45);
         p = -1946157056;
      }

      d = oh.S(d, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      s = oh.S(s, var1);
      Gp = oh.S(Gp, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      U = x(4, var16);
      g = x(6, var16);
      N = x(8, var16);
      Gj = x(7, var16);
      T = x(16, var16);
      S = x(10, var16);
      i = x(12, var16);
      return new rv.s(var5, var6, var7, var8, var10, var12);
   }

   public static double S() {
      int var0 = e >>> 16 & 0xFF;
      int var1 = e >>> 8 & 0xFF;
      int var2 = e & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / var3);
   }

   static int R(int var0, double var1) {
      int var3 = e >>> 16 & 0xFF;
      int var4 = e >>> 8 & 0xFF;
      int var5 = e & 0xFF;
      double var6 = 0.2126 * var3 + 0.7152 * var4 + 0.0722 * var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round(var8[var11] + (var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] Q() {
      return new int[]{s, H, Z, P, G0, f, q, r, z, Y, w, K, Gp, V, l, h, C, d, p, U, g, N, Gj, T, S, i};
   }

   private static int x(int var0, double var1) {
      return (int)Math.round(var0 * var1);
   }

   public static double e() {
      return B;
   }

   public static int u(int var0, double var1) {
      return oh.i(m(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int d(double var0) {
      return oh.j(e, GQ, var0);
   }

   public record s(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, rv.t look) implements AutoCloseable {
      @Override
      public void close() {
         rv.s = this.previous[0];
         rv.H = this.previous[1];
         rv.Z = this.previous[2];
         rv.P = this.previous[3];
         rv.G0 = this.previous[4];
         rv.f = this.previous[5];
         rv.q = this.previous[6];
         rv.r = this.previous[7];
         rv.z = this.previous[8];
         rv.Y = this.previous[9];
         rv.w = this.previous[10];
         rv.K = this.previous[11];
         rv.Gp = this.previous[12];
         rv.V = this.previous[13];
         rv.l = this.previous[14];
         rv.h = this.previous[15];
         rv.C = this.previous[16];
         rv.d = this.previous[17];
         rv.p = this.previous[18];
         rv.U = this.previous[19];
         rv.g = this.previous[20];
         rv.N = this.previous[21];
         rv.Gj = this.previous[22];
         rv.T = this.previous[23];
         rv.S = this.previous[24];
         rv.i = this.previous[25];
         rv.n = this.animatedAccent;
         rv.G9 = this.style;
         rv.G2 = this.millis;
         rv.B = this.opacity;
         rv.X = this.look;
      }
   }

   public record t(String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift) {
      public static final rv.t DEFAULT = new rv.t("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }
}

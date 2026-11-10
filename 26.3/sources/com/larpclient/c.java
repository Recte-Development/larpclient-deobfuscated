package com.larpclient;

public final class c {
   public static final int w = -1381654;
   public static final int E = -6250336;
   public static int y = -1381654;
   public static int t = -6250336;
   public static int x = -434562791;
   public static int m = -920509918;
   public static int g = -534173399;
   public static int H = -1381654;
   public static int W = -6250336;
   public static int n = -13882324;
   public static int I = -1341058799;
   public static int f = -8947849;
   public static int R = -921036262;
   public static int r = -98426334;
   public static int D = -15132391;
   public static final int K = -1381654;
   public static final int Xj = -7873104;
   public static final int V = -996986;
   public static final int M = -24915;
   public static int U = 2;
   public static int G = 3;
   public static int X = 3;
   public static int i = 5;
   public static int O = 7;
   public static int k = 5;
   public static int u = 0;
   public static int Z = 0;
   public static final int X5 = 100;
   public static final int Q = 160;
   public static final int o = 180;
   public static final int S = 150;
   public static final int z = 160;
   public static final int Xf = 160;
   public static final int p = 200;
   public static final int s = 280;
   public static final int c = 270;
   public static final int L = 4;
   public static final int Y = 26;
   public static final int a = 38;
   public static int J = -533976020;
   public static int N = -233038818;
   public static int h = 1075978786;
   public static int d = -8947844;
   public static int Xn = 569043690;
   public static int XZ = 166390506;
   public static final int q = 8;
   public static final int Xy = 12;
   public static final int B = 8;
   public static final int j = 11;
   public static final int Xv = 22;
   public static final int b = 22;
   public static final int e = 20;
   public static final int C = 44;
   public static final int v = 44;
   private static boolean P;
   private static boolean A;
   private static long F;
   private static double T = 1.0;
   private static c.j l = c.j.DEFAULT;

   public static c.z o(boolean var0, double var1) {
      return C(var0, var1, false);
   }

   public static int k() {
      return vv.O(D, T);
   }

   public static boolean J() {
      return A;
   }

   public static int e(int var0, int var1) {
      return w4.readableAccent(c(var0), var1 | 0xFF000000);
   }

   public static int c(int var0) {
      return P ? A(0.5 + 0.5 * Math.sin(F / 800.0 + var0 * 0.008)) : y;
   }

   public static c.j t() {
      return l;
   }

   public static c.z C(boolean var0, double var1, boolean var3) {
      return N(var0, var1, var3, c.j.DEFAULT);
   }

   public static c.z N(boolean var0, double var1, boolean var3, c.j var4) {
      int[] var5 = F();
      boolean var6 = P;
      boolean var7 = A;
      long var8 = F;
      double var10 = T;
      c.j var12 = l;
      l = var4;
      P = var3;
      A = (boolean)1;
      F = System.currentTimeMillis();
      T = var1;
      if (var0) {
         x = -592136;
         m = -1;
         g = -921100;
         H = -15263974;
         W = -10592664;
         n = -2302750;
         I = -1447443;
         f = -7697772;
         R = -1;
         r = -1;
         D = -1;
         J = -1052685;
         N = -1184272;
         h = -855307;
         d = -6710880;
         Xn = 385875968;
         XZ = 520093696;
         u = -1250065;
         Z = 706485284;
      } else {
         int var13 = q(-16119283, 0.09);
         int var14 = q(-15263973, 0.11);
         int var15 = q(-14079698, 0.2);
         H = -1184272;
         W = -6645085;
         f = -9934735;
         x = var14;
         n = var15;
         m = vv.Q(var14, vv.Q(var13, H, 0.035), 0.62);
         g = vv.Q(var14, vv.Q(var13, H, 0.06), 0.62);
         I = var13;
         R = vv.Q(var13, H, 0.04);
         r = vv.Q(var14, H, 0.025);
         D = var14;
         J = vv.Q(var14, H, 0.05);
         N = var13;
         h = vv.N(var14, 0.25);
         d = -6908257;
         Xn = vv.N(vv.Q(var15, H, 0.12), 0.75);
         XZ = vv.N(var15, 0.6);
         u = vv.N(var13, 0.45);
         Z = -1946157056;
      }

      u = vv.O(u, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      x = vv.O(x, var1);
      N = vv.O(N, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      U = A(4, var16);
      G = A(6, var16);
      X = A(8, var16);
      i = A(7, var16);
      O = A(16, var16);
      k = A(10, var16);
      return new c.z(var5, var6, var7, var8, var10, var12);
   }

   public static double F() {
      int var0 = y >>> 16 & 0xFF;
      int var1 = y >>> 8 & 0xFF;
      int var2 = y & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / var3);
   }

   static int q(int var0, double var1) {
      int var3 = y >>> 16 & 0xFF;
      int var4 = y >>> 8 & 0xFF;
      int var5 = y & 0xFF;
      double var6 = 0.2126 * var3 + 0.7152 * var4 + 0.0722 * var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round(var8[var11] + (var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] F() {
      return new int[]{x, m, g, H, W, n, I, f, R, r, D, J, N, h, d, Xn, XZ, u, Z, U, G, X, i, O, k};
   }

   private static int A(int var0, double var1) {
      return (int)Math.round(var0 * var1);
   }

   public static double G() {
      return T;
   }

   public static int Y(int var0, double var1) {
      return vv.N(c(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int A(double var0) {
      return vv.Q(y, t, var0);
   }

   public record j(String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift) {
      public static final c.j DEFAULT = new c.j("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }

   public record z(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, c.j look) implements AutoCloseable {
      @Override
      public void close() {
         com.larpclient.c.x = this.previous[0];
         com.larpclient.c.m = this.previous[1];
         com.larpclient.c.g = this.previous[2];
         com.larpclient.c.H = this.previous[3];
         com.larpclient.c.W = this.previous[4];
         com.larpclient.c.n = this.previous[5];
         com.larpclient.c.I = this.previous[6];
         com.larpclient.c.f = this.previous[7];
         com.larpclient.c.R = this.previous[8];
         com.larpclient.c.r = this.previous[9];
         com.larpclient.c.D = this.previous[10];
         com.larpclient.c.J = this.previous[11];
         com.larpclient.c.N = this.previous[12];
         com.larpclient.c.h = this.previous[13];
         com.larpclient.c.d = this.previous[14];
         com.larpclient.c.Xn = this.previous[15];
         com.larpclient.c.XZ = this.previous[16];
         com.larpclient.c.u = this.previous[17];
         com.larpclient.c.Z = this.previous[18];
         com.larpclient.c.U = this.previous[19];
         com.larpclient.c.G = this.previous[20];
         com.larpclient.c.X = this.previous[21];
         com.larpclient.c.i = this.previous[22];
         com.larpclient.c.O = this.previous[23];
         com.larpclient.c.k = this.previous[24];
         com.larpclient.c.P = this.animatedAccent;
         com.larpclient.c.A = this.style;
         com.larpclient.c.F = this.millis;
         com.larpclient.c.T = this.opacity;
         com.larpclient.c.l = this.look;
      }
   }
}

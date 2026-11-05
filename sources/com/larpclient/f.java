package com.larpclient;

public final class f {
   public static final int Y = -1381654;
   public static final int ai = -6250336;
   public static int C = -1381654;
   public static int O = -6250336;
   public static int K = -434562791;
   public static int i = -920509918;
   public static int x = -534173399;
   public static int J = -1381654;
   public static int s = -6250336;
   public static int I = -13882324;
   public static int m = -1341058799;
   public static int G = -8947849;
   public static int S = -921036262;
   public static int c = -98426334;
   public static int u = -15132391;
   public static final int W = -1381654;
   public static final int R = -7873104;
   public static final int t = -996986;
   public static final int q = -24915;
   public static int L = 2;
   public static int aj = 3;
   public static int B = 3;
   public static int y = 5;
   public static int r = 7;
   public static int D = 5;
   public static int l = 5;
   public static int v = 0;
   public static int b = 0;
   public static final int N = 100;
   public static final int T = 160;
   public static final int E = 180;
   public static final int P = 150;
   public static final int k = 160;
   public static final int aA = 160;
   public static final int aG = 200;
   public static final int d = 280;
   public static final int o = 270;
   public static final int A = 4;
   public static final int p = 26;
   public static final int f = 38;
   public static int V = -533976020;
   public static int aB = -233038818;
   public static int h = 1075978786;
   public static int ac = -8947844;
   public static int H = 569043690;
   public static int g = 166390506;
   public static final int U = 8;
   public static final int F = 12;
   public static final int w = 8;
   public static final int ap = 11;
   public static final int n = 22;
   public static final int j = 22;
   public static final int aU = 20;
   public static final int X = 44;
   public static final int Z = 44;
   private static boolean z;
   private static boolean e;
   private static long Q;
   private static double M = 1.0;
   private static f.a a = f.a.DEFAULT;

   public static f.s Z(boolean var0, double var1) {
      return W(var0, var1, false);
   }

   public static int h() {
      return _3.U(u, M);
   }

   public static boolean Y() {
      return e;
   }

   public static int Q(int var0, int var1) {
      return com.larpclient.n.readableAccent(z(var0), var1 | 0xFF000000);
   }

   public static int z(int var0) {
      return z ? d(0.5 + 0.5 * Math.sin(Q / 800.0 + var0 * 0.008)) : C;
   }

   public static f.a O() {
      return a;
   }

   public static f.s W(boolean var0, double var1, boolean var3) {
      return F(var0, var1, var3, f.a.DEFAULT);
   }

   public static f.s F(boolean var0, double var1, boolean var3, f.a var4) {
      int[] var5 = e();
      boolean var6 = z;
      boolean var7 = e;
      long var8 = Q;
      double var10 = M;
      f.a var12 = a;
      a = var4;
      z = var3;
      e = (boolean)1;
      Q = System.currentTimeMillis();
      M = var1;
      if (var0) {
         K = -592136;
         i = -1;
         x = -921100;
         J = -15263974;
         s = -10592664;
         I = -2302750;
         m = -1447443;
         G = -7697772;
         S = -1;
         c = -1;
         u = -1;
         V = -1052685;
         aB = -1184272;
         h = -855307;
         ac = -6710880;
         H = 385875968;
         g = 520093696;
         v = -1250065;
         b = 706485284;
      } else {
         int var13 = h(-16119283, 0.09);
         int var14 = h(-15263973, 0.11);
         int var15 = h(-14079698, 0.2);
         J = -1184272;
         s = -6645085;
         G = -9934735;
         K = var14;
         I = var15;
         i = _3.D(var14, _3.D(var13, J, 0.035), 0.62);
         x = _3.D(var14, _3.D(var13, J, 0.06), 0.62);
         m = var13;
         S = _3.D(var13, J, 0.04);
         c = _3.D(var14, J, 0.025);
         u = var14;
         V = _3.D(var14, J, 0.05);
         aB = var13;
         h = _3.A(var14, 0.25);
         ac = -6908257;
         H = _3.A(_3.D(var15, J, 0.12), 0.75);
         g = _3.A(var15, 0.6);
         v = _3.A(var13, 0.45);
         b = -1946157056;
      }

      v = _3.U(v, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      K = _3.U(K, var1);
      aB = _3.U(aB, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      L = Q(4, var16);
      aj = Q(6, var16);
      B = Q(8, var16);
      y = Q(7, var16);
      r = Q(16, var16);
      D = Q(10, var16);
      l = Q(12, var16);
      return new f.s(var5, var6, var7, var8, var10, var12);
   }

   public static double U() {
      int var0 = C >>> 16 & 0xFF;
      int var1 = C >>> 8 & 0xFF;
      int var2 = C & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / var3);
   }

   static int h(int var0, double var1) {
      int var3 = C >>> 16 & 0xFF;
      int var4 = C >>> 8 & 0xFF;
      int var5 = C & 0xFF;
      double var6 = 0.2126 * var3 + 0.7152 * var4 + 0.0722 * var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round(var8[var11] + (var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] e() {
      return new int[]{K, i, x, J, s, I, m, G, S, c, u, V, aB, h, ac, H, g, v, b, L, aj, B, y, r, D, l};
   }

   private static int Q(int var0, double var1) {
      return (int)Math.round(var0 * var1);
   }

   public static double J() {
      return M;
   }

   public static int V(int var0, double var1) {
      return _3.A(z(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int d(double var0) {
      return _3.D(C, O, var0);
   }

   public record a(String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift) {
      public static final f.a DEFAULT = new f.a("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }

   public record s(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, f.a look) implements AutoCloseable {
      @Override
      public void close() {
         com.larpclient.f.K = this.previous[0];
         com.larpclient.f.i = this.previous[1];
         com.larpclient.f.x = this.previous[2];
         com.larpclient.f.J = this.previous[3];
         com.larpclient.f.s = this.previous[4];
         com.larpclient.f.I = this.previous[5];
         com.larpclient.f.m = this.previous[6];
         com.larpclient.f.G = this.previous[7];
         com.larpclient.f.S = this.previous[8];
         com.larpclient.f.c = this.previous[9];
         com.larpclient.f.u = this.previous[10];
         com.larpclient.f.V = this.previous[11];
         com.larpclient.f.aB = this.previous[12];
         com.larpclient.f.h = this.previous[13];
         com.larpclient.f.ac = this.previous[14];
         com.larpclient.f.H = this.previous[15];
         com.larpclient.f.g = this.previous[16];
         com.larpclient.f.v = this.previous[17];
         com.larpclient.f.b = this.previous[18];
         com.larpclient.f.L = this.previous[19];
         com.larpclient.f.aj = this.previous[20];
         com.larpclient.f.B = this.previous[21];
         com.larpclient.f.y = this.previous[22];
         com.larpclient.f.r = this.previous[23];
         com.larpclient.f.D = this.previous[24];
         com.larpclient.f.l = this.previous[25];
         com.larpclient.f.z = this.animatedAccent;
         com.larpclient.f.e = this.style;
         com.larpclient.f.Q = this.millis;
         com.larpclient.f.M = this.opacity;
         com.larpclient.f.a = this.look;
      }
   }
}

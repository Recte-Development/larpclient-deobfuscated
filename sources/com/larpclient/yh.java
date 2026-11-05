package com.larpclient;

public final class yh {
   private static final long s = 511L;
   private static final long T = 68719476735L;

   public static boolean b(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static yh.b b(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new yh.b(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new yh.b(var11, Integer.numberOfTrailingZeros(var2), false) : new yh.b(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp(var6, 1, 9) && var9 != 0L && var2 != 0
            ? new yh.b(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static boolean S(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return lr.H(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int Z(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (var5 - var4 + 1));
   }

   private yh() {
   }

   public record b(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }

   public static final class j {
      private long m = -1L;
      private long x;

      public boolean H() {
         return this.m >= 0L;
      }

      public boolean B(long var1, int var3) {
         if (this.m < 0L) {
            this.m = var1 + Math.max(0, var3);
         }

         return var1 >= this.m && var1 >= this.x;
      }

      public boolean R(long var1) {
         return var1 >= this.x;
      }

      public void B(long var1, boolean var3) {
         this.x = var1 + 100L;
         if (var3) {
            this.m = -1L;
         }
      }

      public void o() {
         this.m = -1L;
      }

      public void k() {
         this.m = -1L;
         this.x = 0L;
      }
   }

   public static final class k {
      public static final long A = 1000L;
      public static final long u = 4000L;
      public static final int Z = 6;
      private yh.k.o P = yh.k.o.r;
      private long F;
      private long C;
      private int V;
      private boolean t;

      public void R(long var1, boolean var3) {
         this.P = yh.k.o.o;
         this.F = this.C = var1;
         this.V = 0;
         this.t = var3;
      }

      public yh.k.o D() {
         return this.P;
      }

      public boolean f() {
         return this.P != yh.k.o.r;
      }

      public int H() {
         return this.V;
      }

      public boolean Z() {
         return this.t;
      }

      public boolean j(long var1, boolean var3, int var4) {
         if (this.P == yh.k.o.o && var3) {
            this.P = yh.k.o.d;
            this.C = var1 + Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean U(long var1, boolean var3) {
         return this.P == yh.k.o.d && var3 && var1 >= this.C && this.V < 6;
      }

      public void n(long var1, int var3) {
         this.V++;
         this.C = var1 + Math.max(0, var3);
      }

      public boolean t(long var1, int var3) {
         if (this.V > 0 && this.t) {
            this.P = yh.k.o.c;
            this.C = var1 + Math.max(0, var3);
            return true;
         } else {
            this.P = yh.k.o.r;
            return false;
         }
      }

      public boolean x(long var1, boolean var3) {
         return this.P == yh.k.o.c && var3 && var1 >= this.C;
      }

      public boolean c(long var1) {
         return this.f() && var1 - this.F > (this.P == yh.k.o.o ? 1000L : 4000L);
      }

      public void g() {
         this.P = yh.k.o.r;
         this.V = 0;
         this.t = (boolean)0;
      }

      public enum o {
         r,
         o,
         d,
         c;
      }
   }

   public static final class x {
      public static final long y = 2000L;
      private yh.x.m L = yh.x.m.H;
      private int S = -1;
      private int Y = -1;
      private long J;
      private long F;

      public void K(long var1, int var3, int var4, int var5) {
         this.S = var3;
         this.Y = var4;
         this.L = var3 == var4 ? yh.x.m.M : yh.x.m.i;
         this.J = var1;
         this.F = var1 + Math.max(0, var5);
      }

      public yh.x.m m() {
         return this.L;
      }

      public boolean D() {
         return this.L != yh.x.m.H;
      }

      public int V() {
         return this.S;
      }

      public int r() {
         return this.Y;
      }

      public boolean a(long var1) {
         return this.D() && var1 >= this.F;
      }

      public void q(long var1, int var3) {
         this.L = switch (this.L) {
            case i -> yh.x.m.M;
            case M -> this.S == this.Y ? yh.x.m.H : yh.x.m.K;
            default -> yh.x.m.H;
         };
         this.F = var1 + Math.max(0, var3);
      }

      public boolean Y(int var1) {
         return this.S != this.Y && var1 == this.Y;
      }

      public boolean h(long var1) {
         return this.D() && var1 - this.J > 2000L;
      }

      public void D() {
         this.L = yh.x.m.H;
         this.S = this.Y = -1;
      }

      public enum m {
         H,
         i,
         M,
         K;
      }
   }
}

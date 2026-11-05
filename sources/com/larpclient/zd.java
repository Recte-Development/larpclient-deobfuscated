package com.larpclient;

public final class zd {
   private static final long t = 511L;
   private static final long m = 68719476735L;

   public static boolean V(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static zd.m j(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new zd.m(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new zd.m(var11, Integer.numberOfTrailingZeros(var2), false) : new zd.m(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp(var6, 1, 9) && var9 != 0L && var2 != 0
            ? new zd.m(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static boolean l(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return _0.j(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int f(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (var5 - var4 + 1));
   }

   private zd() {
   }

   public static final class d {
      private long v = -1L;
      private long p;

      public boolean c() {
         return this.v >= 0L;
      }

      public boolean f(long var1, int var3) {
         if (this.v < 0L) {
            this.v = var1 + Math.max(0, var3);
         }

         return var1 >= this.v && var1 >= this.p;
      }

      public boolean p(long var1) {
         return var1 >= this.p;
      }

      public void B(long var1, boolean var3) {
         this.p = var1 + 100L;
         if (var3) {
            this.v = -1L;
         }
      }

      public void B() {
         this.v = -1L;
      }

      public void v() {
         this.v = -1L;
         this.p = 0L;
      }
   }

   public static final class g {
      public static final long h = 2000L;
      private zd.g.j b = zd.g.j.N;
      private int x = -1;
      private int d = -1;
      private long f;
      private long F;

      public void r(long var1, int var3, int var4, int var5) {
         this.x = var3;
         this.d = var4;
         this.b = var3 == var4 ? zd.g.j.K : zd.g.j.l;
         this.f = var1;
         this.F = var1 + Math.max(0, var5);
      }

      public zd.g.j U() {
         return this.b;
      }

      public boolean o() {
         return this.b != zd.g.j.N;
      }

      public int Y() {
         return this.x;
      }

      public int L() {
         return this.d;
      }

      public boolean v(long var1) {
         return this.o() && var1 >= this.F;
      }

      public void i(long var1, int var3) {
         this.b = switch (this.b) {
            case l -> zd.g.j.K;
            case K -> this.x == this.d ? zd.g.j.N : zd.g.j.T;
            default -> zd.g.j.N;
         };
         this.F = var1 + Math.max(0, var3);
      }

      public boolean B(int var1) {
         return this.x != this.d && var1 == this.d;
      }

      public boolean M(long var1) {
         return this.o() && var1 - this.f > 2000L;
      }

      public void Z() {
         this.b = zd.g.j.N;
         this.x = this.d = -1;
      }

      public enum j {
         N,
         l,
         K,
         T;
      }
   }

   public record m(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }

   public static final class s {
      public static final long q = 1000L;
      public static final long w = 4000L;
      public static final int n = 6;
      private zd.s.z x;
      private long E;
      private long X;
      private int O;
      private boolean m;

      public s() {
         this.x = zd.s.z.x;
      }

      public void T(long var1, boolean var3) {
         this.x = zd.s.z.D;
         this.E = this.X = var1;
         this.O = 0;
         this.m = var3;
      }

      public zd.s.z Q() {
         return this.x;
      }

      public boolean c() {
         return this.x != zd.s.z.x;
      }

      public int T() {
         return this.O;
      }

      public boolean F() {
         return this.m;
      }

      public boolean E(long var1, boolean var3, int var4) {
         if (this.x == zd.s.z.D && var3) {
            this.x = zd.s.z.H;
            this.X = var1 + Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean r(long var1, boolean var3) {
         return this.x == zd.s.z.H && var3 && var1 >= this.X && this.O < 6;
      }

      public void b(long var1, int var3) {
         this.O++;
         this.X = var1 + Math.max(0, var3);
      }

      public boolean Q(long var1, int var3) {
         if (this.O > 0 && this.m) {
            this.x = zd.s.z.m;
            this.X = var1 + Math.max(0, var3);
            return true;
         } else {
            this.x = zd.s.z.x;
            return false;
         }
      }

      public boolean L(long var1, boolean var3) {
         return this.x == zd.s.z.m && var3 && var1 >= this.X;
      }

      public boolean n(long var1) {
         return this.c() && var1 - this.E > (this.x == zd.s.z.D ? 1000L : 4000L);
      }

      public void G() {
         this.x = zd.s.z.x;
         this.O = 0;
         this.m = (boolean)0;
      }

      public enum z {
         x,
         D,
         H,
         m;
      }
   }
}

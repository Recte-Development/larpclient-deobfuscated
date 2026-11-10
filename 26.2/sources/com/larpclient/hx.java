package com.larpclient;

public final class hx {
   private static final long B = 511L;
   private static final long k = 68719476735L;

   public static boolean n(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static hx.n E(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new hx.n(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new hx.n(var11, Integer.numberOfTrailingZeros(var2), false) : new hx.n(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp(var6, 1, 9) && var9 != 0L && var2 != 0
            ? new hx.n(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static hx.n u(hx.n var0, int var1, boolean var2, boolean var3) {
      var1 &= 511;
      return var0 != null && var0.equips() && var0.source() >= 9 && var1 != 0 && var2 && !var3 ? new hx.n(Integer.numberOfTrailingZeros(var1), 40, true) : var0;
   }

   public static boolean u(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return l9.u(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int C(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (var5 - var4 + 1));
   }

   private hx() {
   }

   public static final class j {
      public static final long Q = 1000L;
      public static final long r = 4000L;
      public static final int u = 6;
      private hx.j.b D = hx.j.b.x;
      private long G;
      private long w;
      private int a;
      private boolean t;

      public void o(long var1, boolean var3) {
         this.D = hx.j.b.m;
         this.G = this.w = var1;
         this.a = 0;
         this.t = var3;
      }

      public hx.j.b I() {
         return this.D;
      }

      public boolean b() {
         return this.D != hx.j.b.x;
      }

      public int b() {
         return this.a;
      }

      public boolean v() {
         return this.t;
      }

      public boolean S(long var1, boolean var3, int var4) {
         if (this.D == hx.j.b.m && var3) {
            this.D = hx.j.b.H;
            this.w = var1 + Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean M(long var1, boolean var3) {
         return this.D == hx.j.b.H && var3 && var1 >= this.w && this.a < 6;
      }

      public void t(long var1, int var3) {
         this.a++;
         this.w = var1 + Math.max(0, var3);
      }

      public boolean H(long var1, int var3) {
         if (this.a > 0 && this.t) {
            this.D = hx.j.b.k;
            this.w = var1 + Math.max(0, var3);
            return true;
         } else {
            this.D = hx.j.b.x;
            return false;
         }
      }

      public boolean H(long var1, boolean var3) {
         return this.D == hx.j.b.k && var3 && var1 >= this.w;
      }

      public boolean v(long var1) {
         return this.b() && var1 - this.G > (this.D == hx.j.b.m ? 1000L : 4000L);
      }

      public void p() {
         this.D = hx.j.b.x;
         this.a = 0;
         this.t = (boolean)0;
      }

      public enum b {
         x,
         m,
         H,
         k;
      }
   }

   public record n(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }

   public static final class o {
      public static final long g = 2000L;
      private hx.o.h i = hx.o.h.W;
      private int O = -1;
      private int x = -1;
      private long X;
      private long L;

      public void A(long var1, int var3, int var4, int var5) {
         this.O = var3;
         this.x = var4;
         this.i = var3 == var4 ? hx.o.h.G : hx.o.h.c;
         this.X = var1;
         this.L = var1 + Math.max(0, var5);
      }

      public hx.o.h s() {
         return this.i;
      }

      public boolean x() {
         return this.i != hx.o.h.W;
      }

      public int B() {
         return this.O;
      }

      public int Z() {
         return this.x;
      }

      public boolean Y(long var1) {
         return this.x() && var1 >= this.L;
      }

      public void o(long var1, int var3) {
         this.i = switch (this.i) {
            case c -> hx.o.h.G;
            case G -> this.O == this.x ? hx.o.h.W : hx.o.h.o;
            default -> hx.o.h.W;
         };
         this.L = var1 + Math.max(0, var3);
      }

      public boolean K(int var1) {
         return this.O != this.x && var1 == this.x;
      }

      public boolean l(long var1) {
         return this.x() && var1 - this.X > 2000L;
      }

      public void Y() {
         this.i = hx.o.h.W;
         this.O = this.x = -1;
      }

      public enum h {
         W,
         c,
         G,
         o;
      }
   }

   public static final class q {
      public static final long b = 750L;
      private long I = -1L;
      private long J;
      private long P = -1L;

      public boolean A() {
         return this.I >= 0L;
      }

      public boolean x(long var1, int var3) {
         if (this.I < 0L) {
            this.I = var1 + Math.max(0, var3);
         }

         return var1 >= this.I && var1 >= this.J;
      }

      public boolean k(long var1) {
         return var1 >= this.J;
      }

      public void Q(long var1, boolean var3) {
         this.J = var1 + 100L;
         if (var3) {
            this.I = -1L;
         }
      }

      public void g(long var1) {
         this.Q(var1, true);
         this.P = var1 + 750L;
      }

      public boolean s() {
         return this.P >= 0L;
      }

      public boolean w(long var1) {
         return this.P >= 0L && var1 < this.P;
      }

      public void L() {
         this.P = -1L;
      }

      public void z() {
         this.I = -1L;
      }

      public void R() {
         this.I = -1L;
         this.J = 0L;
         this.P = -1L;
      }
   }
}

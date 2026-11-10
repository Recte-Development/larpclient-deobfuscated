package com.larpclient;

public final class ga {
   private static final long k = 511L;
   private static final long P = 68719476735L;

   public static boolean k(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static ga.r K(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new ga.r(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new ga.r(var11, Integer.numberOfTrailingZeros(var2), false) : new ga.r(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp(var6, 1, 9) && var9 != 0L && var2 != 0
            ? new ga.r(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static ga.r y(ga.r var0, int var1, boolean var2, boolean var3) {
      var1 &= 511;
      return var0 != null && var0.equips() && var0.source() >= 9 && var1 != 0 && var2 && !var3 ? new ga.r(Integer.numberOfTrailingZeros(var1), 40, true) : var0;
   }

   public static boolean L(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return hb.G(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int x(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (var5 - var4 + 1));
   }

   private ga() {
   }

   public static final class h {
      public static final long Y = 2000L;
      private ga.h.c f = ga.h.c.x;
      private int v = -1;
      private int B = -1;
      private long O;
      private long A;

      public void e(long var1, int var3, int var4, int var5) {
         this.v = var3;
         this.B = var4;
         this.f = var3 == var4 ? ga.h.c.Y : ga.h.c.X;
         this.O = var1;
         this.A = var1 + Math.max(0, var5);
      }

      public ga.h.c D() {
         return this.f;
      }

      public boolean U() {
         return this.f != ga.h.c.x;
      }

      public int j() {
         return this.v;
      }

      public int H() {
         return this.B;
      }

      public boolean U(long var1) {
         return this.U() && var1 >= this.A;
      }

      public void u(long var1, int var3) {
         this.f = switch (this.f) {
            case X -> ga.h.c.Y;
            case Y -> this.v == this.B ? ga.h.c.x : ga.h.c.g;
            default -> ga.h.c.x;
         };
         this.A = var1 + Math.max(0, var3);
      }

      public boolean k(int var1) {
         return this.v != this.B && var1 == this.B;
      }

      public boolean X(long var1) {
         return this.U() && var1 - this.O > 2000L;
      }

      public void z() {
         this.f = ga.h.c.x;
         this.v = this.B = -1;
      }

      public enum c {
         x,
         X,
         Y,
         g;
      }
   }

   public static final class o {
      public static final long n = 1000L;
      public static final long T = 4000L;
      public static final int I = 6;
      private ga.o.a r = ga.o.a.t;
      private long B;
      private long H;
      private int W;
      private boolean X;

      public void F(long var1, boolean var3) {
         this.r = ga.o.a.w;
         this.B = this.H = var1;
         this.W = 0;
         this.X = var3;
      }

      public ga.o.a T() {
         return this.r;
      }

      public boolean y() {
         return this.r != ga.o.a.t;
      }

      public int L() {
         return this.W;
      }

      public boolean R() {
         return this.X;
      }

      public boolean v(long var1, boolean var3, int var4) {
         if (this.r == ga.o.a.w && var3) {
            this.r = ga.o.a.R;
            this.H = var1 + Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean c(long var1, boolean var3) {
         return this.r == ga.o.a.R && var3 && var1 >= this.H && this.W < 6;
      }

      public void y(long var1, int var3) {
         this.W++;
         this.H = var1 + Math.max(0, var3);
      }

      public boolean s(long var1, int var3) {
         if (this.W > 0 && this.X) {
            this.r = ga.o.a.Y;
            this.H = var1 + Math.max(0, var3);
            return true;
         } else {
            this.r = ga.o.a.t;
            return false;
         }
      }

      public boolean j(long var1, boolean var3) {
         return this.r == ga.o.a.Y && var3 && var1 >= this.H;
      }

      public boolean o(long var1) {
         return this.y() && var1 - this.B > (this.r == ga.o.a.w ? 1000L : 4000L);
      }

      public void O() {
         this.r = ga.o.a.t;
         this.W = 0;
         this.X = (boolean)0;
      }

      public enum a {
         t,
         w,
         R,
         Y;
      }
   }

   public record r(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }

   public static final class s {
      public static final long a = 750L;
      private long u = -1L;
      private long V;
      private long b = -1L;

      public boolean E() {
         return this.u >= 0L;
      }

      public boolean B(long var1, int var3) {
         if (this.u < 0L) {
            this.u = var1 + Math.max(0, var3);
         }

         return var1 >= this.u && var1 >= this.V;
      }

      public boolean h(long var1) {
         return var1 >= this.V;
      }

      public void h(long var1, boolean var3) {
         this.V = var1 + 100L;
         if (var3) {
            this.u = -1L;
         }
      }

      public void A(long var1) {
         this.h(var1, true);
         this.b = var1 + 750L;
      }

      public boolean v() {
         return this.b >= 0L;
      }

      public boolean p(long var1) {
         return this.b >= 0L && var1 < this.b;
      }

      public void B() {
         this.b = -1L;
      }

      public void p() {
         this.u = -1L;
      }

      public void d() {
         this.u = -1L;
         this.V = 0L;
         this.b = -1L;
      }
   }
}

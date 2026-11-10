package com.larpclient;

public final class qx {
   private static final long T = 511L;
   private static final long c = 68719476735L;

   public static boolean a(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static qx.w o(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new qx.w(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new qx.w(var11, Integer.numberOfTrailingZeros(var2), false) : new qx.w(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp(var6, 1, 9) && var9 != 0L && var2 != 0
            ? new qx.w(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static qx.w t(qx.w var0, int var1, boolean var2, boolean var3) {
      var1 &= 511;
      return var0 != null && var0.equips() && var0.source() >= 9 && var1 != 0 && var2 && !var3 ? new qx.w(Integer.numberOfTrailingZeros(var1), 40, true) : var0;
   }

   public static boolean T(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return d9.F(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int p(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (var5 - var4 + 1));
   }

   private qx() {
   }

   public static final class j {
      public static final long T = 2000L;
      private qx.j.x v = qx.j.x.F;
      private int O = -1;
      private int x = -1;
      private long k;
      private long D;

      public void P(long var1, int var3, int var4, int var5) {
         this.O = var3;
         this.x = var4;
         this.v = var3 == var4 ? qx.j.x.C : qx.j.x.v;
         this.k = var1;
         this.D = var1 + Math.max(0, var5);
      }

      public qx.j.x n() {
         return this.v;
      }

      public boolean h() {
         return this.v != qx.j.x.F;
      }

      public int V() {
         return this.O;
      }

      public int T() {
         return this.x;
      }

      public boolean C(long var1) {
         return this.h() && var1 >= this.D;
      }

      public void r(long var1, int var3) {
         this.v = switch (this.v) {
            case v -> qx.j.x.C;
            case C -> this.O == this.x ? qx.j.x.F : qx.j.x.W;
            default -> qx.j.x.F;
         };
         this.D = var1 + Math.max(0, var3);
      }

      public boolean i(int var1) {
         return this.O != this.x && var1 == this.x;
      }

      public boolean b(long var1) {
         return this.h() && var1 - this.k > 2000L;
      }

      public void p() {
         this.v = qx.j.x.F;
         this.O = this.x = -1;
      }

      public enum x {
         F,
         v,
         C,
         W;
      }
   }

   public static final class q {
      public static final long x = 1000L;
      public static final long Y = 4000L;
      public static final int P = 6;
      private qx.q.m K = qx.q.m.a;
      private long a;
      private long k;
      private int p;
      private boolean u;

      public void p(long var1, boolean var3) {
         this.K = qx.q.m.M;
         this.a = this.k = var1;
         this.p = 0;
         this.u = var3;
      }

      public qx.q.m g() {
         return this.K;
      }

      public boolean k() {
         return this.K != qx.q.m.a;
      }

      public int E() {
         return this.p;
      }

      public boolean o() {
         return this.u;
      }

      public boolean Z(long var1, boolean var3, int var4) {
         if (this.K == qx.q.m.M && var3) {
            this.K = qx.q.m.N;
            this.k = var1 + Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean L(long var1, boolean var3) {
         return this.K == qx.q.m.N && var3 && var1 >= this.k && this.p < 6;
      }

      public void s(long var1, int var3) {
         this.p++;
         this.k = var1 + Math.max(0, var3);
      }

      public boolean P(long var1, int var3) {
         if (this.p > 0 && this.u) {
            this.K = qx.q.m.z;
            this.k = var1 + Math.max(0, var3);
            return true;
         } else {
            this.K = qx.q.m.a;
            return false;
         }
      }

      public boolean q(long var1, boolean var3) {
         return this.K == qx.q.m.z && var3 && var1 >= this.k;
      }

      public boolean C(long var1) {
         return this.k() && var1 - this.a > (this.K == qx.q.m.M ? 1000L : 4000L);
      }

      public void t() {
         this.K = qx.q.m.a;
         this.p = 0;
         this.u = (boolean)0;
      }

      public enum m {
         a,
         M,
         N,
         z;
      }
   }

   public static final class r {
      public static final long T = 750L;
      private long M = -1L;
      private long p;
      private long L = -1L;

      public boolean m() {
         return this.M >= 0L;
      }

      public boolean b(long var1, int var3) {
         if (this.M < 0L) {
            this.M = var1 + Math.max(0, var3);
         }

         return var1 >= this.M && var1 >= this.p;
      }

      public boolean M(long var1) {
         return var1 >= this.p;
      }

      public void c(long var1, boolean var3) {
         this.p = var1 + 100L;
         if (var3) {
            this.M = -1L;
         }
      }

      public void Z(long var1) {
         this.c(var1, true);
         this.L = var1 + 750L;
      }

      public boolean s() {
         return this.L >= 0L;
      }

      public boolean b(long var1) {
         return this.L >= 0L && var1 < this.L;
      }

      public void E() {
         this.L = -1L;
      }

      public void s() {
         this.M = -1L;
      }

      public void a() {
         this.M = -1L;
         this.p = 0L;
         this.L = -1L;
      }
   }

   public record w(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }
}

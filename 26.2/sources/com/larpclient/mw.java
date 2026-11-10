package com.larpclient;

public final class mw {
   private long c;
   private long F;
   private long b;
   private long a = -1L;
   private long M = -1L;
   private long y = -1L;
   private long x = -1L;

   public void h() {
      this.c++;
      this.F = this.b = 0L;
   }

   public void L() {
      this.F++;
   }

   public void g() {
      this.b++;
   }

   public boolean A() {
      return this.a == this.c && this.y == this.F;
   }

   public boolean d() {
      return this.M == this.c && this.x == this.b;
   }

   public void u() {
      this.a = this.c;
      this.y = this.F;
   }

   public void M() {
      this.M = this.c;
      this.x = this.b;
   }

   public void E() {
      this.a = this.M = -1L;
   }
}

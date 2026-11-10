package com.larpclient;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class fq {
   private final LongSupplier O;
   private double F;
   private double x;
   private long j;
   private double f = 1.0;
   private fq.y G = fq.y.u;

   public fq(double var1) {
      this(var1, System::nanoTime);
   }

   public fq(double var1, LongSupplier var3) {
      this.O = Objects.requireNonNull(var3);
      this.F = this.x = var1;
      this.j = var3.getAsLong();
   }

   public void p(double var1) {
      this.F = this.x = var1;
      this.j = this.O.getAsLong();
   }

   public double K() {
      return this.F + (this.x - this.F) * this.G.q((this.O.getAsLong() - this.j) / this.f);
   }

   public void m(int var1) {
      if (this.K() >= 1.0) {
         this.p(0.0);
      }

      this.r(1.0, var1, fq.y.u);
   }

   public double n(double var1, int var3) {
      return this.r(var1, var3, fq.y.u);
   }

   public double r(double var1, int var3, fq.y var4) {
      if (this.x != var1) {
         this.F = this.K();
         this.x = var1;
         this.j = this.O.getAsLong();
         this.f = Math.max(1, var3) * 1000000.0;
         this.G = var4;
      }

      return this.K();
   }

   public enum y {
      n,
      b,
      X,
      Y,
      x,
      u,
      k,
      O,
      q,
      j;

      public double q(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case n -> var3;
            case b -> var3 * var3;
            case X -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case Y -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case x -> var3 * var3 * var3;
            case u -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case k -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case O -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case q -> Math.sin(var3 * Math.PI / 2.0);
            case j -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}

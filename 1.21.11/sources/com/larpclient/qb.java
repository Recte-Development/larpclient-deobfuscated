package com.larpclient;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class qb {
   private final LongSupplier X;
   private double j;
   private double V;
   private long S;
   private double I = 1.0;
   private qb.y l = qb.y.E;

   public qb(double var1) {
      this(var1, System::nanoTime);
   }

   public qb(double var1, LongSupplier var3) {
      this.X = Objects.requireNonNull(var3);
      this.j = this.V = var1;
      this.S = var3.getAsLong();
   }

   public void w(double var1) {
      this.j = this.V = var1;
      this.S = this.X.getAsLong();
   }

   public double u() {
      return this.j + (this.V - this.j) * this.l.f((this.X.getAsLong() - this.S) / this.I);
   }

   public void d(int var1) {
      if (this.u() >= 1.0) {
         this.w(0.0);
      }

      this.G(1.0, var1, qb.y.E);
   }

   public double a(double var1, int var3) {
      return this.G(var1, var3, qb.y.E);
   }

   public double G(double var1, int var3, qb.y var4) {
      if (this.V != var1) {
         this.j = this.u();
         this.V = var1;
         this.S = this.X.getAsLong();
         this.I = Math.max(1, var3) * 1000000.0;
         this.l = var4;
      }

      return this.u();
   }

   public enum y {
      T,
      K,
      z,
      o,
      R,
      E,
      f,
      m,
      J,
      y;

      public double f(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case T -> var3;
            case K -> var3 * var3;
            case z -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case o -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case R -> var3 * var3 * var3;
            case E -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case f -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case m -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case J -> Math.sin(var3 * Math.PI / 2.0);
            case y -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}

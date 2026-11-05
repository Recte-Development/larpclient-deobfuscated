package com.larpclient;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class u7 {
   private final LongSupplier q;
   private double u;
   private double P;
   private long W;
   private double O = 1.0;
   private u7.p Y = u7.p.y;

   public u7(double var1) {
      this(var1, System::nanoTime);
   }

   public u7(double var1, LongSupplier var3) {
      this.q = Objects.requireNonNull(var3);
      this.u = this.P = var1;
      this.W = var3.getAsLong();
   }

   public void c(double var1) {
      this.u = this.P = var1;
      this.W = this.q.getAsLong();
   }

   public double V() {
      return this.u + (this.P - this.u) * this.Y.m((this.q.getAsLong() - this.W) / this.O);
   }

   public void P(int var1) {
      if (this.V() >= 1.0) {
         this.c(0.0);
      }

      this.c(1.0, var1, u7.p.y);
   }

   public double y(double var1, int var3) {
      return this.c(var1, var3, u7.p.y);
   }

   public double c(double var1, int var3, u7.p var4) {
      if (this.P != var1) {
         this.u = this.V();
         this.P = var1;
         this.W = this.q.getAsLong();
         this.O = Math.max(1, var3) * 1000000.0;
         this.Y = var4;
      }

      return this.V();
   }

   public enum p {
      T,
      p,
      Z,
      w,
      N,
      y,
      x,
      f,
      D,
      Y;

      public double m(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case T -> var3;
            case p -> var3 * var3;
            case Z -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case w -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case N -> var3 * var3 * var3;
            case y -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case x -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case f -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case D -> Math.sin(var3 * Math.PI / 2.0);
            case Y -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}

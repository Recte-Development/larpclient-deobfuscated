package com.larpclient;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class ef {
   private final LongSupplier K;
   private double d;
   private double P;
   private long T;
   private double B = 1.0;
   private ef.q I = ef.q.m;

   public ef(double var1) {
      this(var1, System::nanoTime);
   }

   public ef(double var1, LongSupplier var3) {
      this.K = Objects.requireNonNull(var3);
      this.d = this.P = var1;
      this.T = var3.getAsLong();
   }

   public void Q(double var1) {
      this.d = this.P = var1;
      this.T = this.K.getAsLong();
   }

   public double G() {
      return this.d + (this.P - this.d) * this.I.k((this.K.getAsLong() - this.T) / this.B);
   }

   public void s(int var1) {
      if (this.G() >= 1.0) {
         this.Q(0.0);
      }

      this.y(1.0, var1, ef.q.m);
   }

   public double m(double var1, int var3) {
      return this.y(var1, var3, ef.q.m);
   }

   public double y(double var1, int var3, ef.q var4) {
      if (this.P != var1) {
         this.d = this.G();
         this.P = var1;
         this.T = this.K.getAsLong();
         this.B = Math.max(1, var3) * 1000000.0;
         this.I = var4;
      }

      return this.G();
   }

   public enum q {
      d,
      H,
      w,
      g,
      A,
      m,
      U,
      h,
      X,
      N;

      public double k(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case d -> var3;
            case H -> var3 * var3;
            case w -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case g -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case A -> var3 * var3 * var3;
            case m -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case U -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case h -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case X -> Math.sin(var3 * Math.PI / 2.0);
            case N -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}

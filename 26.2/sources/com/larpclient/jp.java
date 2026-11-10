package com.larpclient;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class jp {
   private final LongSupplier t;
   private double b;
   private double h;
   private long k;
   private double B = 1.0;
   private jp.m s = jp.m.S;

   public jp(double var1) {
      this(var1, System::nanoTime);
   }

   public jp(double var1, LongSupplier var3) {
      this.t = Objects.requireNonNull(var3);
      this.b = this.h = var1;
      this.k = var3.getAsLong();
   }

   public void F(double var1) {
      this.b = this.h = var1;
      this.k = this.t.getAsLong();
   }

   public double B() {
      return this.b + (this.h - this.b) * this.s.s((this.t.getAsLong() - this.k) / this.B);
   }

   public void Y(int var1) {
      if (this.B() >= 1.0) {
         this.F(0.0);
      }

      this.y(1.0, var1, jp.m.S);
   }

   public double k(double var1, int var3) {
      return this.y(var1, var3, jp.m.S);
   }

   public double y(double var1, int var3, jp.m var4) {
      if (this.h != var1) {
         this.b = this.B();
         this.h = var1;
         this.k = this.t.getAsLong();
         this.B = Math.max(1, var3) * 1000000.0;
         this.s = var4;
      }

      return this.B();
   }

   public enum m {
      g,
      O,
      X,
      C,
      h,
      S,
      u,
      R,
      U,
      A;

      public double s(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case g -> var3;
            case O -> var3 * var3;
            case X -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case C -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case h -> var3 * var3 * var3;
            case S -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case u -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case R -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case U -> Math.sin(var3 * Math.PI / 2.0);
            case A -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}

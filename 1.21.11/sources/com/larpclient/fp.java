package com.larpclient;

public final class fp implements AutoCloseable {
   private static final ThreadLocal<Double> Y = ThreadLocal.withInitial(() -> 1.0);
   private final double B = Y.get();
   private boolean e;

   private fp(double var1) {
      Y.set(this.B * Math.clamp(var1, 0.0, 1.0));
   }

   public static fp O(double var0) {
      return new fp(var0);
   }

   public static int R(int var0) {
      return ei.R(var0, Y.get());
   }

   @Override
   public void close() {
      if (!this.e) {
         Y.set(this.B);
         this.e = (boolean)1;
      }
   }
}

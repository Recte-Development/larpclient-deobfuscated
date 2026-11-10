package com.larpclient;

public final class oa implements AutoCloseable {
   private static final ThreadLocal<Double> Y = ThreadLocal.withInitial(() -> 1.0);
   private final double M = Y.get();
   private boolean G;

   private oa(double var1) {
      Y.set(this.M * Math.clamp(var1, 0.0, 1.0));
   }

   public static oa j(double var0) {
      return new oa(var0);
   }

   public static int e(int var0) {
      return vv.O(var0, Y.get());
   }

   @Override
   public void close() {
      if (!this.G) {
         Y.set(this.M);
         this.G = (boolean)1;
      }
   }
}

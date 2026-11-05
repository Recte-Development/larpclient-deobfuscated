package com.larpclient;

public final class r implements AutoCloseable {
   private static final ThreadLocal<Double> O = ThreadLocal.withInitial(() -> 1.0);
   private final double B = O.get();
   private boolean d;

   private r(double var1) {
      O.set(this.B * Math.clamp(var1, 0.0, 1.0));
   }

   public static r p(double var0) {
      return new r(var0);
   }

   public static int D(int var0) {
      return oh.S(var0, O.get());
   }

   @Override
   public void close() {
      if (!this.d) {
         O.set(this.B);
         this.d = (boolean)1;
      }
   }
}

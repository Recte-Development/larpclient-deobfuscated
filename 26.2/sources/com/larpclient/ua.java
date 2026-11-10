package com.larpclient;

public final class ua implements AutoCloseable {
   private static final ThreadLocal<Double> f = ThreadLocal.withInitial(() -> 1.0);
   private final double u = f.get();
   private boolean v;

   private ua(double var1) {
      f.set(this.u * Math.clamp(var1, 0.0, 1.0));
   }

   public static ua K(double var0) {
      return new ua(var0);
   }

   public static int I(int var0) {
      return uh.F(var0, f.get());
   }

   @Override
   public void close() {
      if (!this.v) {
         f.set(this.u);
         this.v = (boolean)1;
      }
   }
}

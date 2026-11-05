package com.larpclient;

public final class de implements AutoCloseable {
   private static final ThreadLocal<Double> A = ThreadLocal.withInitial(() -> 1.0);
   private final double a = A.get();
   private boolean u;

   private de(double var1) {
      A.set(this.a * Math.clamp(var1, 0.0, 1.0));
   }

   public static de l(double var0) {
      return new de(var0);
   }

   public static int h(int var0) {
      return _3.U(var0, A.get());
   }

   @Override
   public void close() {
      if (!this.u) {
         A.set(this.a);
         this.u = (boolean)1;
      }
   }
}

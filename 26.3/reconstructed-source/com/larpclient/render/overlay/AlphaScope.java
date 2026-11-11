package com.larpclient.render.overlay;

import com.larpclient.util.render.ColorUtil;

public final class AlphaScope implements AutoCloseable {
   private static final ThreadLocal<Double> cURRENT_ALPHA = ThreadLocal.withInitial(() -> 1.0);
   private final double previousAlpha = cURRENT_ALPHA.get();
   private boolean closed;

   private AlphaScope(double var1) {
      cURRENT_ALPHA.set(this.previousAlpha * Math.clamp(var1, 0.0, 1.0));
   }

   public static AlphaScope push(double var0) {
      return new AlphaScope(var0);
   }

   public static int applyAlpha(int var0) {
      return ColorUtil.multiplyAlpha(var0, cURRENT_ALPHA.get());
   }

   @Override
   public void close() {
      if (!this.closed) {
         cURRENT_ALPHA.set(this.previousAlpha);
         this.closed = (boolean)1;
      }
   }
}

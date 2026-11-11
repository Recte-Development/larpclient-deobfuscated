package com.larpclient.render.overlay;

public final class IntroAnimation {
   public static final IntroAnimation iNSTANCE = new IntroAnimation();
   public static final int sPIN_DURATION_MS = 900;
   public static final int tYPING_START_MS = 750;
   public static final int cHAR_INTERVAL_MS = 75;
   public static final int hOLD_START_MS = 750 + "Larp Client".length() * 75 + 425;
   public static final int fADE_OUT_MS = 400;
   public static final int dISSOLVE_MS = 500;
   private long startNanos = -1L;
   private long finishNanos = -1L;

   public void start(long var1) {
      if (this.startNanos < 0L) {
         this.startNanos = var1;
      }
   }

   public void finish(long var1) {
      if (this.finishNanos < 0L) {
         this.finishNanos = var1;
      }
   }

   public boolean isFinishing() {
      return this.finishNanos >= 0L;
   }

   public double elapsedMs(long var1) {
      return this.startNanos < 0L ? 0.0 : Math.max(0L, var1 - this.startNanos) / 1000000.0;
   }

   public double start(long var1) {
      return this.startNanos >= 0L && this.finishNanos >= 0L
         ? Math.max(0L, var1 - Math.max(this.finishNanos, this.startNanos + hOLD_START_MS * 1000000L)) / 1000000.0
         : 0.0;
   }

   public double start(long var1, boolean var3) {
      return var3 ? smoothstep(this.elapsedMs(var1) / 450.0) : 1.0;
   }

   public double scale(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * isDissolved(this.elapsedMs(var1) / 900.0) : 1.0;
   }

   public double rotation(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - isDissolved(this.elapsedMs(var1) / 900.0)) : 0.0;
   }

   public int visibleChars(long var1, boolean var3) {
      return var3 ? Math.clamp((int)((this.elapsedMs(var1) - 750.0) / 75.0), 0, "Larp Client".length()) : "Larp Client".length();
   }

   public double overlayAlpha(long var1, boolean var3) {
      return this.finishNanos < 0L ? 1.0 : (var3 ? 1.0 - smoothstep(this.start(var1) / 400.0) : 0.0);
   }

   public double dissolveProgress(long var1, boolean var3) {
      if (this.finishNanos < 0L) {
         return 0.0;
      } else {
         return var3 ? smoothstep((this.start(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean isDissolved(long var1, boolean var3) {
      return this.dissolveProgress(var1, var3) >= 1.0;
   }

   public IntroAnimation.Phase phase(long var1, boolean var3) {
      if (this.startNanos < 0L) {
         return IntroAnimation.Phase.NOT_STARTED;
      }

      if (this.finishNanos >= 0L && (!var3 || !(this.elapsedMs(var1) < hOLD_START_MS))) {
         if (this.overlayAlpha(var1, var3) > 0.0) {
            return IntroAnimation.Phase.FADING_OUT;
         } else {
            return this.isDissolved(var1, var3) ? IntroAnimation.Phase.DONE : IntroAnimation.Phase.DISSOLVING;
         }
      } else {
         return IntroAnimation.Phase.RUNNING;
      }
   }

   public static double smoothstep(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double isDissolved(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public enum Phase {
      NOT_STARTED,
      RUNNING,
      FADING_OUT,
      DISSOLVING,
      DONE;
   }
}

package com.larpclient.util.misc;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class RegionAttemptScheduler {
   static final long attemptTimeoutMs = 12000L;
   static final long extendedTimeoutMs = 15000L;
   static final long minAttemptGapMs = 3000L;
   static final long attemptWindowMs = 60000L;
   static final int maxAttemptsPerWindow = 4;
   static final int maxConsecutiveFailures = 5;
   static final long[] backoffStepsMs = new long[]{10000L, 20000L, 40000L, 60000L};
   static final long retryDelayMs = 10000L;
   static final long shortDelayMs = 1500L;
   static final long mediumDelayMs = 3000L;
   static final long cooldownMarginMs = 600L;
   private static final long never = -2305843009213693952L;
   private final ArrayDeque<Long> recentAttempts = new ArrayDeque<>();
   private boolean inFlight;
   private long attemptDeadline = -2305843009213693952L;
   private long lastAttemptAt = -2305843009213693952L;
   private long blockedUntil = -2305843009213693952L;
   private long lastSuccessAt = -2305843009213693952L;
   private long learnedCooldownMs = -1L;
   private int backoffIndex;
   private int consecutiveFailures;
   public static final List<String> regionLabels = List.of("Random", "Asia", "East", "EU Central", "EU West", "Oceania", "West");
   static final List<String> regionKeys = List.of("asia", "east", "eu central", "eu west", "oceania", "west");
   static final long recentActivityWindowMs = 15000L;
   public static final double closeRange = 5.0;
   public static final float minSafeHealth = 8.0F;
   public static final float lowHealth = 10.0F;

   public void reset() {
      this.recentAttempts.clear();
      this.inFlight = (boolean)0;
      this.attemptDeadline = this.lastAttemptAt = this.blockedUntil = this.lastSuccessAt = -2305843009213693952L;
      this.learnedCooldownMs = -1L;
      this.backoffIndex = this.consecutiveFailures = 0;
   }

   public boolean isInFlight() {
      return this.inFlight;
   }

   public long nextAllowedAt(long var1) {
      this.pruneAttempts(var1);
      long var3 = Math.max(this.blockedUntil, this.lastAttemptAt + 3000L);
      if (this.recentAttempts.size() >= 4) {
         var3 = Math.max(var3, this.recentAttempts.peekFirst() + 60000L);
      }

      return var3;
   }

   public boolean canAttempt(long var1) {
      return !this.inFlight && var1 >= this.nextAllowedAt(var1);
   }

   public void recordAttempt(long var1) {
      this.pruneAttempts(var1);
      this.recentAttempts.addLast(var1);
      this.lastAttemptAt = var1;
      this.inFlight = (boolean)1;
      this.attemptDeadline = var1 + 12000L;
   }

   public long millisUntilNext(long var1) {
      return this.inFlight ? Math.max(0L, this.attemptDeadline - var1) : Math.max(0L, this.nextAllowedAt(var1) - var1);
   }

   public boolean hasTimedOut(long var1) {
      return this.inFlight && var1 >= this.attemptDeadline;
   }

   public void extendDeadline(long var1) {
      if (this.inFlight) {
         this.attemptDeadline = Math.max(this.attemptDeadline, var1 + 15000L);
      }

      this.consecutiveFailures = 0;
   }

   public boolean recordFailure(long var1) {
      this.inFlight = (boolean)0;
      this.consecutiveFailures++;
      this.blockedUntil = Math.max(this.blockedUntil, var1 + this.extendDeadline());
      return this.consecutiveFailures >= 5;
   }

   public void recordCooldown(long var1, long var3) {
      this.inFlight = (boolean)0;
      this.consecutiveFailures = 0;
      if (var3 < 0L) {
         this.blockedUntil = Math.max(this.blockedUntil, var1 + this.extendDeadline());
      } else {
         this.backoffIndex = 0;
         this.blockedUntil = Math.max(var1 + 1L, var1 + var3 + 600L);
         if (this.lastSuccessAt != -2305843009213693952L) {
            this.learnedCooldownMs = Math.clamp(var1 - this.lastSuccessAt + var3, 5000, 600000);
         }
      }
   }

   public void blockFor(long var1, long var3) {
      this.inFlight = (boolean)0;
      this.blockedUntil = Math.max(this.blockedUntil, var1 + var3);
   }

   public void recordSuccess(long var1) {
      this.inFlight = (boolean)0;
      this.consecutiveFailures = this.backoffIndex = 0;
      this.lastSuccessAt = var1;
      this.blockedUntil = this.learnedCooldownMs > 0L ? var1 + this.learnedCooldownMs + 600L : -2305843009213693952L;
   }

   public long getLearnedCooldown() {
      return this.learnedCooldownMs;
   }

   private long extendDeadline() {
      return backoffStepsMs[Math.min(this.backoffIndex++, backoffStepsMs.length - 1)];
   }

   private void pruneAttempts(long var1) {
      while (!this.recentAttempts.isEmpty() && var1 - this.recentAttempts.peekFirst() >= 60000L) {
         this.recentAttempts.removeFirst();
      }
   }

   public static RegionAttemptScheduler.Action chooseAction(
      boolean var0, boolean var1, boolean var2, boolean var3, double var4, long var6, boolean var8, long var9
   ) {
      if (!var0) {
         return RegionAttemptScheduler.Action.NONE;
      }

      if (var2) {
         return var3 && var4 >= 12.0 ? RegionAttemptScheduler.Action.RETREAT : RegionAttemptScheduler.Action.HOLD;
      }

      if (var1) {
         return RegionAttemptScheduler.Action.FLEE;
      }

      int var11 = var9 >= 0L && var9 < 15000L ? 1 : 0;
      return var8 && var4 <= 5.0 && var6 > 3000L && var11 == 0 ? RegionAttemptScheduler.Action.ENGAGE : RegionAttemptScheduler.Action.HOLD;
   }

   public static boolean millisUntilNext(boolean var0, boolean var1, double var2, double var4, float var6) {
      return !var0 && !var1 && var6 > 8.0F && var2 <= var4;
   }

   public static boolean shouldRetreat(boolean var0, boolean var1, float var2) {
      return !var0 && !var1 && var2 > 8.0F;
   }

   public static boolean isInRange(double var0, boolean var2, boolean var3, float var4, boolean var5) {
      int var6 = var4 <= 10.0F ? 1 : 0;
      if (var5) {
         return var0 <= (var2 ? 16 : (var6 != 0 ? 12 : 10));
      } else if (!var3 && var0 > 3.0) {
         return false;
      } else {
         return var2 ? var0 <= 7.0 : var0 <= 5.0 || var6 != 0 && var0 <= 8.0;
      }
   }

   public static double[] computeEscapePoint(double var0, double var2, List<double[]> var4, double var5) {
      double var7 = 0.0;
      double var9 = 0.0;

      for (double[] var12 : var4) {
         double var13 = var0 - var12[0];
         double var15 = var2 - var12[1];
         double var17 = Math.hypot(var13, var15);
         if (var17 < 0.001) {
            var13 = 1.0;
            var15 = 0.0;
            var17 = 1.0;
         }

         var7 += var13 / var17 / Math.max(1.0, var17);
         var9 += var15 / var17 / Math.max(1.0, var17);
      }

      double var19 = Math.hypot(var7, var9);
      return !var4.isEmpty() && !(var19 < 1.0E-9) ? new double[]{var0 + var7 / var19 * var5, var2 + var9 / var19 * var5} : null;
   }

   public enum Action {
      NONE,
      FLEE,
      HOLD,
      RETREAT,
      ENGAGE;
   }

   public static final class Point {
      private final Map<String, Integer> x = new HashMap<>();
      private int z;

      public void getX() {
         this.x.clear();
         this.z = 0;
      }

      public String getZ(String var1) {
         String var2 = var1 == null ? "" : var1.trim().toLowerCase(Locale.ROOT);
         if (RegionAttemptScheduler.regionKeys.contains(var2)) {
            return var2;
         }

         for (int var3 = 0; var3 < RegionAttemptScheduler.regionKeys.size(); var3++) {
            String var4 = RegionAttemptScheduler.regionKeys.get(Math.floorMod(this.z + var3, RegionAttemptScheduler.regionKeys.size()));
            if (this.x.getOrDefault(var4, 0) < 2) {
               this.z = Math.floorMod(this.z + var3 + 1, RegionAttemptScheduler.regionKeys.size());
               return var4;
            }
         }

         this.x.clear();
         String var5 = RegionAttemptScheduler.regionKeys.get(Math.floorMod(this.z, RegionAttemptScheduler.regionKeys.size()));
         this.z = Math.floorMod(this.z + 1, RegionAttemptScheduler.regionKeys.size());
         return var5;
      }

      public void distanceTo(String var1) {
         if (var1 != null && RegionAttemptScheduler.regionKeys.contains(var1)) {
            this.x.merge(var1, 1, Integer::sum);
         }
      }

      public void directionTo(String var1) {
         if (var1 != null) {
            this.x.remove(var1);
         }
      }
   }
}

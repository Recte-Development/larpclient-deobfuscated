package com.larpclient.util.data;

public final class RodAttemptTracker {
   private RodAttemptTracker.Phase phase = RodAttemptTracker.Phase.IDLE;
   private boolean autoReel;
   private boolean cooldownActive;
   private long deadline;
   private long cooldownEnd;
   private int ticks;

   public RodAttemptTracker.Phase getPhase() {
      return this.phase;
   }

   public boolean isActive() {
      return this.phase != RodAttemptTracker.Phase.IDLE;
   }

   public void startAttempt(long var1) {
      this.isActive();
      this.cooldownActive = (boolean)1;
      this.cooldownEnd = var1 + 250000000L;
   }

   public boolean isCooldownOver(long var1) {
      if (this.cooldownActive && var1 - this.cooldownEnd >= 0L) {
         this.cooldownActive = (boolean)0;
      }

      return !this.cooldownActive;
   }

   public void startAttempt(long var1, long var3, boolean var5) {
      if (this.isActive()) {
         throw new IllegalStateException("Rod attempt already active");
      }

      this.autoReel = var5;
      this.deadline = var1 + var3;
      this.ticks = 0;
      this.phase = RodAttemptTracker.Phase.AWAITING_CAST;
   }

   public RodAttemptTracker.Action update(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.isActive()) {
         return RodAttemptTracker.Action.CONTINUE;
      }

      if (var1 - this.deadline < 0L && ++this.ticks < 60) {
         if (this.phase == RodAttemptTracker.Phase.AWAITING_CAST) {
            if (!var3) {
               return RodAttemptTracker.Action.CONTINUE;
            }

            this.phase = RodAttemptTracker.Phase.CAST;
         }

         if (!var3 || var5) {
            return RodAttemptTracker.Action.ABORT;
         }

         if (this.phase == RodAttemptTracker.Phase.REELED) {
            return RodAttemptTracker.Action.CONTINUE;
         }

         if (var4) {
            if (!this.autoReel) {
               return RodAttemptTracker.Action.ABORT;
            }

            this.phase = RodAttemptTracker.Phase.HOOKED;
            return RodAttemptTracker.Action.REEL;
         } else {
            return !var6 && !var7 ? RodAttemptTracker.Action.CONTINUE : RodAttemptTracker.Action.ABORT;
         }
      } else {
         return RodAttemptTracker.Action.ABORT;
      }
   }

   public void markReeled() {
      if (this.phase != RodAttemptTracker.Phase.HOOKED) {
         throw new IllegalStateException("No confirmed rod hook");
      }

      this.phase = RodAttemptTracker.Phase.REELED;
   }

   public void isActive() {
      this.phase = RodAttemptTracker.Phase.IDLE;
      this.cooldownActive = (boolean)0;
      this.ticks = 0;
   }

   public enum Action {
      CONTINUE,
      ABORT,
      REEL;
   }

   public enum Phase {
      IDLE,
      AWAITING_CAST,
      CAST,
      HOOKED,
      REELED;
   }

   public static final class Sample {
      private long first;
      private boolean second;

      public boolean setValue(long var1) {
         return !this.second || var1 - this.first >= 0L;
      }

      public void getFirst(long var1, long var3) {
         this.first = var1 + var3;
         this.second = (boolean)1;
      }

      public void setValue(long var1, long var3) {
         this.first = var1 + var3;
         this.second = (boolean)1;
      }

      public void getSecond() {
         this.second = (boolean)0;
      }
   }
}

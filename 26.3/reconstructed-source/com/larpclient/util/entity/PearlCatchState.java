package com.larpclient.util.entity;

public final class PearlCatchState {
   public static final int tIMEOUT_TICKS = 30;
   private PearlCatchState.Phase phase = PearlCatchState.Phase.IDLE;
   private boolean startRequested;
   private long deadline;
   private long throwTick;
   private long waitStart = -1L;

   public PearlCatchState.Phase getPhase() {
      return this.phase;
   }

   public boolean isActive() {
      return this.phase != PearlCatchState.Phase.IDLE;
   }

   public boolean requestStart() {
      if (this.isActive()) {
         return false;
      }

      this.startRequested = (boolean)1;
      return true;
   }

   public boolean consumeStartRequest() {
      boolean var1 = this.startRequested;
      this.startRequested = (boolean)0;
      return var1;
   }

   public void beginWait(long var1) {
      if (this.waitStart < 0L) {
         this.waitStart = var1;
      }
   }

   public boolean isWaiting() {
      return this.waitStart >= 0L;
   }

   public boolean hasWaitTimedOut(long var1) {
      return this.waitStart >= 0L && var1 - this.waitStart > 30L;
   }

   public void clearWait() {
      this.waitStart = -1L;
   }

   public void beginPearlThrow(long var1, int var3) {
      if (this.isActive()) {
         throw new IllegalStateException("Pearl catch already active");
      }

      this.startRequested = (boolean)0;
      this.waitStart = -1L;
      this.phase = PearlCatchState.Phase.PEARL_THROWN;
      this.deadline = var1 + var3;
   }

   public void beginWindCharge(long var1, int var3) {
      if (this.phase != PearlCatchState.Phase.PEARL_THROWN) {
         throw new IllegalStateException("No pearl throw pending");
      }

      this.phase = PearlCatchState.Phase.WIND_CHARGE;
      this.throwTick = var1;
      this.deadline = var1 + var3;
   }

   public void extendWindCharge(long var1, int var3) {
      if (this.phase != PearlCatchState.Phase.WIND_CHARGE) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.deadline = var1 + var3;
   }

   public void hasWaitTimedOut() {
      if (this.phase != PearlCatchState.Phase.WIND_CHARGE) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.phase = PearlCatchState.Phase.IDLE;
   }

   public long isActive(long var1) {
      return this.phase == PearlCatchState.Phase.WIND_CHARGE ? var1 - this.throwTick : -1L;
   }

   public boolean isDeadlineReached(long var1) {
      return this.isActive() && var1 - this.deadline >= 0L;
   }

   public void reset() {
      this.phase = PearlCatchState.Phase.IDLE;
      this.startRequested = (boolean)0;
      this.waitStart = -1L;
   }

   public enum Phase {
      IDLE,
      PEARL_THROWN,
      WIND_CHARGE;
   }
}

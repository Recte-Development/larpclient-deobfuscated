package com.larpclient.util.inventory;

import com.larpclient.qx$j$Phase;

public final class SwapLogic {
   private static final long sLOT_MASK = 511L;
   private static final long iNVENTORY_MASK = 68719476735L;

   public static boolean canAct(boolean var0, boolean var1, boolean var2, double var3, double var5, boolean var7, double var8) {
      return !var0 && (var2 || var1 || var3 + (var7 ? var5 : 0.0) <= var8);
   }

   public static SwapLogic.SwapAction selectSwap(long var0, int var2, boolean var3, boolean var4, boolean var5, int var6) {
      var0 &= 68719476735L;
      long var7 = var0 & 511L;
      long var9 = var0 & -512L;
      var2 &= 511;
      if (var3 && var4 && var7 != 0L) {
         return new SwapLogic.SwapAction(Long.numberOfTrailingZeros(var7), 40, true);
      } else if (var3 && var0 != 0L) {
         int var11 = Long.numberOfTrailingZeros(var9 != 0L ? var9 : var0);
         return var4 && var2 != 0 ? new SwapLogic.SwapAction(var11, Integer.numberOfTrailingZeros(var2), false) : new SwapLogic.SwapAction(var11, 40, true);
      } else {
         return !var3 && var5 && Long.bitCount(var7) < Math.clamp(var6, 1, 9) && var9 != 0L && var2 != 0
            ? new SwapLogic.SwapAction(Long.numberOfTrailingZeros(var9), Integer.numberOfTrailingZeros(var2), false)
            : null;
      }
   }

   public static SwapLogic.SwapAction overrideWithHotbar(SwapLogic.SwapAction var0, int var1, boolean var2, boolean var3) {
      var1 &= 511;
      return var0 != null && var0.equips() && var0.source() >= 9 && var1 != 0 && var2 && !var3
         ? new SwapLogic.SwapAction(Integer.numberOfTrailingZeros(var1), 40, true)
         : var0;
   }

   public static boolean shouldRun(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      return HotbarSwapPlanner.noModifiersHeld(var0, var1, var2, var3, var4, var5, var6);
   }

   public static int randomInRange(int var0, int var1, double var2) {
      int var4 = Math.max(0, Math.min(var0, var1));
      int var5 = Math.max(0, Math.max(var0, var1));
      return var4 + (int)Math.floor(Math.clamp(var2, 0.0, 0.999999) * (var5 - var4 + 1));
   }

   private SwapLogic() {
   }

   public static final class ActionCooldown {
      public static final long hOLD_MS = 750L;
      private long readyTime = -1L;
      private long cooldownUntil;
      private long holdUntil = -1L;

      public boolean hasReadyTime() {
         return this.readyTime >= 0L;
      }

      public boolean isHolding(long var1, int var3) {
         if (this.readyTime < 0L) {
            this.readyTime = var1 + Math.max(0, var3);
         }

         return var1 >= this.readyTime && var1 >= this.cooldownUntil;
      }

      public boolean isOffCooldown(long var1) {
         return var1 >= this.cooldownUntil;
      }

      public void startCooldown(long var1, boolean var3) {
         this.cooldownUntil = var1 + 100L;
         if (var3) {
            this.readyTime = -1L;
         }
      }

      public void startHold(long var1) {
         this.startCooldown(var1, true);
         this.holdUntil = var1 + 750L;
      }

      public boolean clearReadyTime() {
         return this.holdUntil >= 0L;
      }

      public boolean isHolding(long var1) {
         return this.holdUntil >= 0L && var1 < this.holdUntil;
      }

      public void clearHold() {
         this.holdUntil = -1L;
      }

      public void clearReadyTime() {
         this.readyTime = -1L;
      }

      public void reset() {
         this.readyTime = -1L;
         this.cooldownUntil = 0L;
         this.holdUntil = -1L;
      }
   }

   public static final class SlotSwitchTracker {
      public static final long tIMEOUT_MS = 2000L;
      private qx$j$Phase phase = qx$j$Phase.IDLE;
      private int fromSlot = -1;
      private int toSlot = -1;
      private long startTime;
      private long deadline;

      public void begin(long var1, int var3, int var4, int var5) {
         this.fromSlot = var3;
         this.toSlot = var4;
         this.phase = var3 == var4 ? qx$j$Phase.HOLDING : qx$j$Phase.SWITCHING;
         this.startTime = var1;
         this.deadline = var1 + Math.max(0, var5);
      }

      public qx$j$Phase getPhase() {
         return this.phase;
      }

      public boolean isActive() {
         return this.phase != qx$j$Phase.IDLE;
      }

      public int getFromSlot() {
         return this.fromSlot;
      }

      public int getToSlot() {
         return this.toSlot;
      }

      public boolean isDue(long var1) {
         return this.isActive() && var1 >= this.deadline;
      }

      public void advance(long var1, int var3) {
         this.phase = switch (this.phase) {
            case SWITCHING -> qx$j$Phase.HOLDING;
            case HOLDING -> this.fromSlot == this.toSlot ? qx$j$Phase.IDLE : qx$j$Phase.RETURNING;
            default -> qx$j$Phase.IDLE;
         };
         this.deadline = var1 + Math.max(0, var3);
      }

      public boolean isTargetSlot(int var1) {
         return this.fromSlot != this.toSlot && var1 == this.toSlot;
      }

      public boolean isTimedOut(long var1) {
         return this.isActive() && var1 - this.startTime > 2000L;
      }

      public void reset() {
         this.phase = qx$j$Phase.IDLE;
         this.fromSlot = this.toSlot = -1;
      }
   }

   public record SwapAction(int source, int button, boolean equips) {
      public int menuSlot() {
         return this.source < 9 ? this.source + 36 : this.source;
      }
   }

   public static final class SwapSequence {
      public static final long sTART_TIMEOUT_MS = 1000L;
      public static final long tIMEOUT_MS = 4000L;
      public static final int mAX_REPEATS = 6;
      private SwapLogic.SwapSequence.Phase phase = SwapLogic.SwapSequence.Phase.IDLE;
      private long startTime;
      private long nextTime;
      private int repeatCount;
      private boolean finishAfterRepeats;

      public void begin(long var1, boolean var3) {
         this.phase = SwapLogic.SwapSequence.Phase.STARTED;
         this.startTime = this.nextTime = var1;
         this.repeatCount = 0;
         this.finishAfterRepeats = var3;
      }

      public SwapLogic.SwapSequence.Phase getPhase() {
         return this.phase;
      }

      public boolean isActive() {
         return this.phase != SwapLogic.SwapSequence.Phase.IDLE;
      }

      public int getRepeatCount() {
         return this.repeatCount;
      }

      public boolean isFinishAfterRepeats() {
         return this.finishAfterRepeats;
      }

      public boolean arm(long var1, boolean var3, int var4) {
         if (this.phase == SwapLogic.SwapSequence.Phase.STARTED && var3) {
            this.phase = SwapLogic.SwapSequence.Phase.REPEATING;
            this.nextTime = var1 + Math.max(0, var4);
            return true;
         } else {
            return false;
         }
      }

      public boolean canRepeat(long var1, boolean var3) {
         return this.phase == SwapLogic.SwapSequence.Phase.REPEATING && var3 && var1 >= this.nextTime && this.repeatCount < 6;
      }

      public void recordRepeat(long var1, int var3) {
         this.repeatCount++;
         this.nextTime = var1 + Math.max(0, var3);
      }

      public boolean startFinish(long var1, int var3) {
         if (this.repeatCount > 0 && this.finishAfterRepeats) {
            this.phase = SwapLogic.SwapSequence.Phase.FINISHING;
            this.nextTime = var1 + Math.max(0, var3);
            return true;
         } else {
            this.phase = SwapLogic.SwapSequence.Phase.IDLE;
            return false;
         }
      }

      public boolean canFinish(long var1, boolean var3) {
         return this.phase == SwapLogic.SwapSequence.Phase.FINISHING && var3 && var1 >= this.nextTime;
      }

      public boolean isTimedOut(long var1) {
         return this.isActive() && var1 - this.startTime > (this.phase == SwapLogic.SwapSequence.Phase.STARTED ? 1000L : 4000L);
      }

      public void reset() {
         this.phase = SwapLogic.SwapSequence.Phase.IDLE;
         this.repeatCount = 0;
         this.finishAfterRepeats = (boolean)0;
      }

      public enum Phase {
         IDLE,
         STARTED,
         REPEATING,
         FINISHING;
      }
   }
}

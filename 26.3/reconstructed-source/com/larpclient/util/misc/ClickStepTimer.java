package com.larpclient.util.misc;

import java.util.random.RandomGenerator;

public final class ClickStepTimer {
   private ClickStepTimer.Phase phase = ClickStepTimer.Phase.IDLE;
   private int ticks;
   private int delay = 1;

   public ClickStepTimer.Phase getPhase() {
      return this.phase;
   }

   public int getTicks() {
      return this.ticks;
   }

   public boolean isDelayElapsed() {
      return this.ticks >= this.delay;
   }

   public boolean isTimedOut() {
      return this.ticks >= this.delay + 20;
   }

   public void tick() {
      if (this.ticks < Integer.MAX_VALUE) {
         this.ticks++;
      }
   }

   public void setPhase(ClickStepTimer.Phase var1, int var2) {
      this.phase = var1;
      this.delay = Math.max(0, var2);
      this.ticks = 0;
   }

   public void reset() {
      this.setPhase(ClickStepTimer.Phase.IDLE, 1);
   }

   public static int setPhase(int var0, int var1, boolean var2, RandomGenerator var3) {
      int var4 = Math.min(var0, var1);
      int var5 = Math.max(var0, var1);
      return Math.max(0, var2 ? var3.nextInt(var4, var5 + 1) + var3.nextInt(-2, 3) : var4);
   }

   public static int jitterDelay(int var0, boolean var1, RandomGenerator var2) {
      return Math.max(0, var0 + (var1 ? var2.nextInt(-1, 2) : 0));
   }

   public static boolean hasChanged(int var0, int var1, long var2, long var4) {
      return var0 != var1 && var2 != var4;
   }

   public static boolean namesMatch(String var0, String var1) {
      return var0 != null && var1 != null && var0.trim().equalsIgnoreCase(var1.trim());
   }

   public static boolean isGreenPane(String var0) {
      return "lime_stained_glass_pane".equals(var0) || "green_stained_glass_pane".equals(var0);
   }

   public static boolean getTicks(String var0) {
      if (var0 != null) {
         switch (var0) {
            case "air":
            case "lime_stained_glass_pane":
            case "green_stained_glass_pane":
            case "red_stained_glass_pane":
            case "black_stained_glass_pane":
            case "gray_stained_glass_pane":
               break;
            default:
               return true;
         }
      }

      return false;
   }

   public static final class HomeReplaceTimer {
      private long armedAtNanos;
      private int homeSlot;

      public void arm(int var1, long var2) {
         if (var1 >= 1 && var1 <= 5) {
            this.homeSlot = var1;
            this.armedAtNanos = var2;
         } else {
            throw new IllegalArgumentException("Home slot");
         }
      }

      public int pollDue(long var1) {
         if (this.homeSlot != 0 && var1 - this.armedAtNanos >= 1000000000L) {
            int var3 = this.homeSlot;
            this.clear();
            return var3;
         } else {
            return 0;
         }
      }

      public void clear() {
         this.homeSlot = 0;
         this.armedAtNanos = 0L;
      }

      public boolean isArmed() {
         return this.homeSlot != 0;
      }
   }

   public enum Phase {
      IDLE,
      SEND_COMMAND,
      AWAIT_MENU,
      MOVE_ITEMS,
      SETTLE,
      CONFIRM,
      AWAIT_CLEAR,
      RESERVED_1,
      RESERVED_2,
      RESERVED_3;
   }
}

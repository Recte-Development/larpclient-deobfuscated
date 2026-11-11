package com.larpclient.util.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class PendingBlockTracker {
   public static final int mAX_ENTRIES = 8;
   public static final int gRACE_TICKS = 40;
   private final List<PendingBlockTracker.PendingBlock> entries = new ArrayList<>(8);

   public void track(BlockPos var1, long var2, int var4) {
      this.entries.removeIf(var1x -> var1x.pos.equals(var1));
      if (this.entries.size() == 8) {
         this.entries.remove(0);
      }

      this.entries.add(new PendingBlockTracker.PendingBlock(var1.immutable(), var2, Math.max(0, var4)));
   }

   public List<BlockPos> positions() {
      ArrayList var1 = new ArrayList(this.entries.size());

      for (PendingBlockTracker.PendingBlock var3 : this.entries) {
         var1.add(var3.pos);
      }

      return var1;
   }

   public PendingBlockTracker.PollResult poll(BlockPos var1, boolean var2, long var3, int var5) {
      PendingBlockTracker.PendingBlock var6 = this.find(var1);
      if (var6 == null) {
         return PendingBlockTracker.PollResult.NONE;
      }

      if (!var2) {
         if (var6.dueTick < 0L && var3 - var6.trackedTick <= 40L) {
            return PendingBlockTracker.PollResult.WAITING;
         }

         this.entries.remove(var6);
         return PendingBlockTracker.PollResult.NONE;
      } else {
         if (var6.dueTick < 0L) {
            var6.dueTick = Math.max(var3 + var6.delayTicks, var6.trackedTick + 1L);
         }

         if (var3 >= var6.dueTick + Math.max(1, var5)) {
            this.entries.remove(var6);
            return PendingBlockTracker.PollResult.NONE;
         } else {
            return var3 >= var6.dueTick ? PendingBlockTracker.PollResult.DUE : PendingBlockTracker.PollResult.WAITING;
         }
      }
   }

   public long ageOf(BlockPos var1, long var2) {
      PendingBlockTracker.PendingBlock var4 = this.find(var1);
      return var4 == null ? -1L : var2 - var4.trackedTick;
   }

   public void remove(BlockPos var1) {
      this.entries.removeIf(var1x -> var1x.pos.equals(var1));
   }

   public boolean positions() {
      return this.entries.isEmpty();
   }

   public void clear() {
      this.entries.clear();
   }

   private PendingBlockTracker.PendingBlock find(BlockPos var1) {
      for (PendingBlockTracker.PendingBlock var3 : this.entries) {
         if (var3.pos.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private static final class PendingBlock {
      final BlockPos pos;
      final long trackedTick;
      final int delayTicks;
      long dueTick = -1L;

      PendingBlock(BlockPos var1, long var2, int var4) {
         this.pos = var1;
         this.trackedTick = var2;
         this.delayTicks = var4;
      }
   }

   public enum PollResult {
      WAITING,
      DUE,
      NONE;
   }
}

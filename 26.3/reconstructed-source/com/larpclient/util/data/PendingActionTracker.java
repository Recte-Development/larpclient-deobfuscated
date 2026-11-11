package com.larpclient.util.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;

public final class PendingActionTracker {
   private final Map<Integer, Long> pendingEntities = new HashMap<>();
   private BlockPos pendingBlock;
   private long pendingBlockExpiry;
   private long nextEntityTick;
   private long nextBlockTick;

   public void prune(long var1, IntPredicate var3, Predicate<BlockPos> var4) {
      this.pendingEntities.entrySet().removeIf(var3x -> var1 >= var3x.getValue() || !var3.test(var3x.getKey()));
      if (this.pendingBlock != null && (var1 >= this.pendingBlockExpiry || var4.test(this.pendingBlock))) {
         this.pendingBlock = null;
      }
   }

   public boolean canTargetEntity(long var1, int var3) {
      return var1 >= this.nextEntityTick && !this.pendingEntities.containsKey(var3);
   }

   public boolean canTargetBlock(long var1) {
      return var1 >= this.nextBlockTick && this.pendingBlock == null && this.pendingEntities.isEmpty();
   }

   public boolean hasPendingEntities() {
      return !this.pendingEntities.isEmpty();
   }

   public boolean hasPendingBlock() {
      return this.pendingBlock != null;
   }

   public void canTargetEntity(long var1, int var3, int var4) {
      this.markEntityWithTtl(var1, var3, var4, 10);
   }

   public void markBlock(long var1, BlockPos var3, int var4, boolean var5) {
      this.markBlockWithTtl(var1, var3, var4, var5, 10);
   }

   public void markEntityWithTtl(long var1, int var3, int var4, int var5) {
      this.nextEntityTick = var1 + Math.max(1, var4);
      this.pendingEntities.put(var3, var1 + Math.clamp(var5, 2, 40));
   }

   public void markBlockWithTtl(long var1, BlockPos var3, int var4, boolean var5, int var6) {
      this.nextBlockTick = var1 + Math.max(1, var4);
      if (var5) {
         this.pendingBlock = var3.immutable();
         this.pendingBlockExpiry = var1 + Math.clamp(var6, 2, 40);
      }
   }

   public void delayBlocks(long var1, int var3) {
      this.nextBlockTick = var1 + Math.max(1, var3);
   }

   public void prune() {
      this.pendingEntities.clear();
      this.pendingBlock = null;
      this.nextEntityTick = this.nextBlockTick = this.pendingBlockExpiry = 0L;
   }
}

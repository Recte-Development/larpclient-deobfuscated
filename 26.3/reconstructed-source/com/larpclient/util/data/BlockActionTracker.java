package com.larpclient.util.data;

import java.util.Objects;
import net.minecraft.core.BlockPos;

public final class BlockActionTracker {
   private BlockPos pos;
   private Object first;
   private Object second;
   private int startTick;
   private int lastTick;
   private int holdTick;
   private boolean holding;

   public BlockPos getPos() {
      return this.pos;
   }

   public void reset() {
      this.pos = null;
      this.first = this.second = null;
      this.holding = (boolean)0;
   }

   public void track(BlockPos var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.pos, var1) || this.first != var2 || this.second != var3 || var4 - this.lastTick != 1) {
         this.pos = var1 == null ? null : var1.immutable();
         this.first = var2;
         this.second = var3;
         this.startTick = var4;
         this.holding = (boolean)0;
      }

      this.lastTick = var4;
   }

   public boolean shouldFire(int var1, int var2, boolean var3) {
      if (this.pos == null || var1 < this.startTick) {
         return false;
      }

      if (!var3) {
         this.holding = (boolean)0;
         return false;
      }

      if (!this.holding) {
         this.holding = (boolean)1;
         this.holdTick = var1;
      }

      return var1 - this.startTick >= Math.max(2, var2) && var1 - this.holdTick >= 1;
   }
}

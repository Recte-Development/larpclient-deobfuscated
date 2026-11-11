package com.larpclient.render.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class BlockHighlightFader {
   public static final int mAX_ENTRIES = 4;
   public static final double dEFAULT_FADE_IN = 0.06;
   public static final double dEFAULT_HOLD = 0.06;
   public static final double dEFAULT_FADE_OUT = 0.18;
   private final List<BlockHighlightFader.TrackedBlock> entries = new ArrayList<>(4);
   private double lastTime = Double.NaN;

   public void clear() {
      this.entries.clear();
      this.lastTime = Double.NaN;
   }

   public List<BlockHighlightFader.FadedBlock> update(BlockPos var1, double var2) {
      return this.updateWithTimings(var1, var2, 0.06, 0.18);
   }

   public List<BlockHighlightFader.FadedBlock> updateWithTimings(BlockPos var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.lastTime || var2 - this.lastTime > 1.0) {
         this.clear();
      }

      this.lastTime = var2;
      this.entries.removeIf(var4x -> var2 - var4x.lastSeen >= var8);
      if (var1 != null) {
         BlockHighlightFader.TrackedBlock var10 = this.entries.stream().filter(var1x -> var1x.pos.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.entries.size() == 4) {
               this.entries.removeFirst();
            }

            this.entries.add(new BlockHighlightFader.TrackedBlock(var1, var2));
         } else {
            if (var2 - var10.lastSeen > var4) {
               var10.startAlpha = entryAlpha(var10, var2, var4, var6);
               var10.fadeInStart = var2;
            }

            var10.lastSeen = var2;
            this.entries.remove(var10);
            this.entries.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.entries.size());

      for (BlockHighlightFader.TrackedBlock var12 : this.entries) {
         double var13 = entryAlpha(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new BlockHighlightFader.FadedBlock(var12.pos, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double entryAlpha(BlockHighlightFader.TrackedBlock var0, double var1, double var3, double var5) {
      return (var0.startAlpha + (1.0 - var0.startAlpha) * update((var1 - var0.fadeInStart) / 0.06)) * (1.0 - update((var1 - var0.lastSeen - var3) / var5));
   }

   private static double update(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public record FadedBlock(BlockPos pos, double alpha) {
   }

   private static final class TrackedBlock {
      final BlockPos pos;
      double fadeInStart;
      double startAlpha;
      double lastSeen;

      TrackedBlock(BlockPos var1, double var2) {
         this.pos = var1.immutable();
         this.fadeInStart = this.lastSeen = var2;
      }
   }
}

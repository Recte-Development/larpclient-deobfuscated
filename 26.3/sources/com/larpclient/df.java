package com.larpclient;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.function.LongConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.world.level.ChunkPos;

public final class df {
   public static void C(LongSet var0, BlockPos var1, int var2, int var3, int var4, LongConsumer var5) {
      int var6 = SectionPos.blockToSectionCoord(var1.getX());
      int var7 = SectionPos.blockToSectionCoord(var1.getZ());
      int var8 = Math.clamp(SectionPos.blockToSectionCoord(var1.getY()), var3, var4);
      LongIterator var9 = var0.iterator();

      while (var9.hasNext()) {
         long var10 = var9.nextLong();
         int var12 = ChunkPos.getX(var10);
         int var13 = ChunkPos.getZ(var10);
         if (ChunkTrackingView.isInViewDistance(var6, var7, var2, var12, var13)) {
            var5.accept(SectionPos.asLong(var12, var8, var13));
         }
      }
   }

   private df() {
   }
}

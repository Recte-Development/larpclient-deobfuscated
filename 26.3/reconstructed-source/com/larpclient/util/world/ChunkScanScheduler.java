package com.larpclient.util.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ChunkScanScheduler<C> {
   private C currentChunk;
   private int chunkX;
   private int chunkZ;
   private int nextSection;
   private int offsetCursor;
   private int radius;
   private List<int[]> sortedOffsets = List.of();

   public void reset() {
      this.currentChunk = null;
      this.nextSection = this.offsetCursor = 0;
   }

   public void invalidate(int var1, int var2) {
      if (this.currentChunk != null && this.chunkX == var1 && this.chunkZ == var2) {
         this.currentChunk = null;
      }
   }

   public void tick(ChunkScanScheduler.ChunkSource<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.radius != var4) {
            ArrayList var6 = new ArrayList();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.sortedOffsets = List.copyOf(var6);
            this.radius = var4;
            this.offsetCursor = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.currentChunk != null) {
            var12++;
            if (Math.abs((long)this.chunkX - var2) > var4
               || Math.abs((long)this.chunkZ - var3) > var4
               || var1.loaded(this.chunkX, this.chunkZ) != this.currentChunk) {
               this.currentChunk = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.currentChunk == null) {
               int var11 = this.offsetCursor++ % var10;
               this.offsetCursor %= var10;
               this.chunkX = var2 + this.sortedOffsets.get(var11)[0];
               this.chunkZ = var3 + this.sortedOffsets.get(var11)[1];
               var12++;
               if (!var1.due(this.chunkX, this.chunkZ)) {
                  continue;
               }

               this.currentChunk = (C)var1.loaded(this.chunkX, this.chunkZ);
               if (this.currentChunk == null) {
                  continue;
               }

               this.nextSection = 0;
               var1.begin(this.currentChunk);
            }

            if (this.nextSection >= var1.sections(this.currentChunk)) {
               var1.finish(this.chunkX, this.chunkZ);
               this.currentChunk = null;
            } else {
               int var15 = this.nextSection++;
               var13++;
               if (var1.candidate(this.currentChunk, var15)) {
                  var14++;
                  var1.scan(this.currentChunk, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface ChunkSource<C> {
      C loaded(int var1, int var2);

      boolean due(int var1, int var2);

      int sections(C var1);

      boolean candidate(C var1, int var2);

      void begin();

      default void begin(C var1) {
         this.begin();
      }

      void scan(C var1, int var2);

      void finish(int var1, int var2);
   }
}

package com.larpclient.util.world;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class ChunkChangeTracker {
   public static final int mAX_PER_TICK = 2048;
   public static final int mAX_TRACKED = 2048;
   public static final int mAX_EVENTS = 64;
   public static final long eXPIRY_TICKS = 600L;
   private final LinkedHashMap<Long, ChunkChangeTracker.TrackedBlock> tracked = new LinkedHashMap<>();
   private final ArrayDeque<ChunkChangeTracker.ChangeEvent> events = new ArrayDeque<>();
   private long currentTick = Long.MIN_VALUE;
   private int tickUpdateCount;
   private long chunks;
   private long updates;
   private long clues;
   private long terrainReplacements;
   private long otherReplacements;
   private long budgetDrops;
   private long evictions;
   private long expired;
   private long omittedDetails;

   public void reset() {
      this.tracked.clear();
      this.events.clear();
      this.currentTick = Long.MIN_VALUE;
      this.tickUpdateCount = 0;
      this.chunks = this.updates = this.clues = this.terrainReplacements = this.otherReplacements = this.budgetDrops = this.evictions = this.expired = this.omittedDetails = 0L;
   }

   public void countChunk() {
      this.chunks++;
   }

   public boolean hasBudget(long var1) {
      if (this.currentTick != var1) {
         this.currentTick = var1;
         this.tickUpdateCount = 0;
      }

      return this.tickUpdateCount < 2048;
   }

   public void recordDrops(ChunkChangeTracker.Source var1, long var2) {
      this.budgetDrops = this.budgetDrops + Math.max(0L, var2);
      if (var1 == ChunkChangeTracker.Source.BLOCK_UPDATE) {
         this.updates = this.updates + Math.max(0L, var2);
      }
   }

   private void pushEvent(ChunkChangeTracker.ChangeEvent var1) {
      if (this.events.size() == 64) {
         this.events.removeFirst();
         this.omittedDetails++;
      }

      this.events.addLast(var1);
   }

   public void recordUpdate(long var1, long var3, String var5, boolean var6, boolean var7, ChunkChangeTracker.Source var8, long var9) {
      if (var8 == ChunkChangeTracker.Source.BLOCK_UPDATE) {
         this.updates++;
      }

      if (!this.hasBudget(var9)) {
         this.budgetDrops++;
      } else {
         this.tickUpdateCount++;
         ChunkChangeTracker.TrackedBlock var11 = this.tracked.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.tracked.remove(var1);
            this.expired++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.clues++;
            }

            if (var11 == null && var6) {
               this.pushEvent(new ChunkChangeTracker.ChangeEvent(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.terrainReplacements++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.otherReplacements++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.pushEvent(new ChunkChangeTracker.ChangeEvent(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.tracked.size() == 2048) {
               this.tracked.remove(this.tracked.keySet().iterator().next());
               this.evictions++;
            }

            this.tracked.put(var1, new ChunkChangeTracker.TrackedBlock(var3, var5, var6, var9));
         }
      }
   }

   public void expireOld(long var1) {
      this.tracked.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.expired++;
         return true;
      });
      this.events.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.omittedDetails++;
         return true;
      });
   }

   public void clearChunk(long var1) {
      this.tracked.values().removeIf(var2 -> var2.chunk() == var1);
      this.events.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> positionsInChunk(long var1) {
      return this.tracked.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<ChunkChangeTracker.ChangeEvent> drainEvents(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.events.isEmpty(); var3++) {
         var2.add(this.events.removeFirst());
      }

      return List.copyOf(var2);
   }

   public ChunkChangeTracker.Snapshot snapshot() {
      return new ChunkChangeTracker.Snapshot(
         this.chunks,
         this.updates,
         this.clues,
         this.terrainReplacements,
         this.otherReplacements,
         this.budgetDrops,
         this.evictions,
         this.expired,
         this.omittedDetails,
         this.tracked.size()
      );
   }

   public record ChangeEvent(long position, long chunk, String before, String after, String transition, ChunkChangeTracker.Source source, long tick) {
   }

   public record Snapshot(
      long chunks,
      long updates,
      long clues,
      long terrainReplacements,
      long otherReplacements,
      long budgetDrops,
      long evictions,
      long expired,
      long omittedDetails,
      int tracked
   ) {
   }

   public enum Source {
      CHUNK_SCAN,
      BLOCK_UPDATE;
   }

   private record TrackedBlock(long chunk, String identity, boolean clue, long tick) {
   }
}

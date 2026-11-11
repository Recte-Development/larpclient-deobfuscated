package com.larpclient.util.data;

import com.larpclient.util.world.BuildScanResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class SpawnerScanCache {
   private final Map<Long, SpawnerScanCache.ScanResult> scanned = new HashMap<>();
   private final Map<Long, SpawnerScanCache.ScanResult> flagged = new HashMap<>();
   private final Map<Long, Long> scanTimes = new HashMap<>();
   private final LinkedHashMap<Long, Long> alertTimes = new LinkedHashMap<>();
   public static final int mAX_ENTRIES = 1024;
   private static final long aLERT_COOLDOWN = 1200L;
   private static final long aLERT_TTL = 6000L;
   private final Map<Long, SpawnerScanCache.ScanResult> flaggedView = Collections.unmodifiableMap(this.flagged);

   public Map<Long, SpawnerScanCache.ScanResult> snapshotScanned() {
      return Map.copyOf(this.scanned);
   }

   public void clearScanTime(long var1) {
      this.scanTimes.remove(var1);
   }

   public Map<Long, SpawnerScanCache.ScanResult> snapshotFlagged() {
      return Map.copyOf(this.flagged);
   }

   public Map<Long, SpawnerScanCache.ScanResult> getFlaggedView() {
      return this.flaggedView;
   }

   public int flaggedCount() {
      return this.flagged.size();
   }

   public int scannedCount() {
      return this.scanTimes.size();
   }

   public int alertCount() {
      return this.alertTimes.size();
   }

   public boolean isScanDue(long var1, long var3, long var5) {
      Long var7 = this.scanTimes.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean record_(long var1, SpawnerScanCache.ScanResult var3, boolean var4, long var5) {
      if (this.scanned.size() >= 1024 && !this.scanned.containsKey(var1)) {
         return false;
      }

      this.scanTimes.put(var1, var5);
      this.scanned.put(var1, var3);
      return this.updateFlagged(var1, var3, var4, var5);
   }

   public boolean updateFlagged(long var1, SpawnerScanCache.ScanResult var3, boolean var4, long var5) {
      if (!this.scanned.containsKey(var1)) {
         return false;
      }

      if (!var4) {
         this.flagged.remove(var1);
         return false;
      }

      int var7 = this.flagged.put(var1, var3) == null ? 1 : 0;
      Long var8 = this.alertTimes.get(var1);
      if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
         this.alertTimes.remove(var1);
         this.alertTimes.put(var1, var5);

         while (this.alertTimes.size() > 1024) {
            this.alertTimes.remove(this.alertTimes.keySet().iterator().next());
         }

         return true;
      } else {
         return false;
      }
   }

   public void remove(long var1) {
      this.flagged.remove(var1);
      this.scanned.remove(var1);
      this.scanTimes.remove(var1);
   }

   public void retain(LongPredicate var1, long var2) {
      this.scanned.keySet().removeIf(var1x -> !var1.test(var1x));
      this.flagged.keySet().removeIf(var1x -> !var1.test(var1x));
      this.scanTimes.keySet().removeIf(var1x -> !var1.test(var1x));
      this.alertTimes.values().removeIf(var2x -> var2 - var2x >= 6000L);
   }

   public void clear() {
      this.scanned.clear();
      this.flagged.clear();
      this.scanTimes.clear();
      this.alertTimes.clear();
   }

   public record ScanResult(int mature, int buds, int spawners, int sampleY, BuildScanResult base, GeodeClueScore sus, List<SpawnerScanCache.SpawnerHit> hits) {
      public ScanResult(int mature, int buds, int spawners, int sampleY, BuildScanResult base, GeodeClueScore sus, List<SpawnerScanCache.SpawnerHit> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public ScanResult(int var1, int var2, int var3, int var4, BuildScanResult var5) {
         this(var1, var2, var3, var4, var5, GeodeClueScore.EMPTY, List.of());
      }

      public ScanResult(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, BuildScanResult.EMPTY);
      }

      public boolean matureAmethyst(int var1, int var2) {
         long var3 = (long)this.mature + this.buds;
         return this.mature >= var1 && var3 > 0L && this.mature * 100L >= var3 * var2;
      }

      public int maturePercent() {
         long var1 = (long)this.mature + this.buds;
         return var1 == 0L ? 0 : (int)(this.mature * 100L / var1);
      }
   }

   public record SpawnerHit(int x, int y, int z, boolean trial, String name) {
      private final int x2;
      private final int y2;
      private final int z2;

      public SpawnerHit(int x, int y, int z, boolean trial, String name) {
         this.x2 = x;
         this.y2 = y;
         this.z2 = z;
         this.trial = trial;
         this.name = name;
      }
   }
}

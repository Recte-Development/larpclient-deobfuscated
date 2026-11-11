package com.larpclient.module.world;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.data.FoundLocation;
import com.larpclient.util.data.SpawnerScanCache;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.BlockClassifier;
import com.larpclient.util.world.BuildScanResult;
import com.larpclient.util.world.ChunkChangeTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;

public final class BaseFinder extends ChunkFindingModule {
   private final ModeSetting detectionMode = this.addSetting(
      new ModeSetting(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final NumberSetting minStorage = this.addSetting(
      new NumberSetting("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final NumberSetting minUtilities = this.addSetting(
      new NumberSetting("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final NumberSetting minEnderChests = this.addSetting(
      new NumberSetting("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0)
   );
   private final NumberSetting minScanY = this.addSetting(new NumberSetting("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final NumberSetting maxScanY = this.addSetting(
      new NumberSetting("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0)
   );
   private final BooleanSetting terrainClues = this.addSetting(
      new BooleanSetting("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true)
   );
   private final BooleanSetting baseBoxes = this.addSetting(
      new BooleanSetting("Base Boxes", "Outline the detected building evidence as well as its chunk", true)
   );
   private final ModeSetting baseStyle = this.addSetting(
      new ModeSetting("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled")
   );
   private final BlockClassifier.BuildScanner evidenceCounter = new BlockClassifier.BuildScanner();
   private final BooleanSetting visibilityDiagnostic = this.addSetting(
      new BooleanSetting("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final ChunkChangeTracker diagnosticTracker = new ChunkChangeTracker();
   private ClientLevel diagnosticLevel;
   private long diagnosticTick;
   private boolean diagnosticFailed;
   private static BaseFinder instance;
   private boolean inEnd;
   private final BooleanSetting honeyClues = this.addSetting(
      new BooleanSetting("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true)
   );
   private final Map<Long, Long> chunkFirstSeen = new HashMap<>();
   private boolean honeyEligible;
   private int scanMinY;
   private int scanMaxY;
   private boolean terrainEnabled;
   private boolean honeyEnabled;

   public BaseFinder() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (Setting var2 : this.detectionSettings()) {
         var2.onChange(var1 -> this.reset("detection changed"));
      }

      this.minStorage.visibleWhen(() -> !this.detectionMode.getValue().equals("Conservative"));
      this.minUtilities.visibleWhen(() -> this.detectionMode.getValue().equals("Balanced"));
      this.terrainClues.visibleWhen(() -> this.detectionMode.getValue().equals("Broad"));
      this.baseStyle.visibleWhen(this.baseBoxes::getValue);
      this.cacheSettings();
      instance = this;
      this.visibilityDiagnostic.onChange(var1 -> {
         if (this.diagnosticLevel != null) {
            this.logDiagnostic("setting changed");
         }

         this.resetDiagnostic();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.onChunkLoad(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.diagnosticLevel) {
            this.diagnosticTracker.clearChunk(var2x.getPos().pack());
         }

         if (getMc() != null && var1 == getMc().level) {
            this.chunkFirstSeen.remove(var2x.getPos().pack());
         }
      });
   }

   @Override
   public List<Setting<?>> detectionSettings() {
      return List.of(
         this.detectionMode, this.minStorage, this.minUtilities, this.minEnderChests, this.minScanY, this.maxScanY, this.terrainClues, this.honeyClues
      );
   }

   @Override
   public List<Setting<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.baseBoxes);
      var1.add(this.baseStyle);
      return List.copyOf(var1);
   }

   public static AABB evidenceBounds(ChunkPos var0, SpawnerScanCache.ScanResult var1) {
      AABB var2 = var1.base().bounds();
      return var2 == null ? null : var2.move(var0.getMinBlockX(), 0.0, var0.getMinBlockZ()).inflate(0.08);
   }

   @Override
   protected void drawEvidence(ChunkPos var1, SpawnerScanCache.ScanResult var2, EspRenderer var3, int var4) {
      if (this.baseBoxes.getValue()) {
         AABB var5 = evidenceBounds(var1, var2);
         if (var5 != null) {
            var3.drawBox(var5, this.baseStyle.getValue(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return this.isCandidate(var1, 16);
   }

   private boolean isCandidate(BlockState var1, int var2) {
      if (BlockClassifier.isFullHoneyHive(var1)) {
         return this.honeyClues.getValue();
      } else {
         BlockClassifier.Category var3 = BlockClassifier.classify(var1, var2, this.inEnd);
         if (var3 == BlockClassifier.Category.DEDICATED) {
            return true;
         } else {
            return this.detectionMode.getValue().equals("Conservative")
               ? false
               : var3 == BlockClassifier.Category.STORAGE
                  || var3 == BlockClassifier.Category.UTILITY
                  || this.detectionMode.getValue().equals("Broad")
                     && (var3 == BlockClassifier.Category.CRAFTED || var3 == BlockClassifier.Category.ALTERED && this.terrainClues.getValue());
         }
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      LevelChunkSection var3 = var1.getSections()[var2];
      int var4 = var1.getMinY() + var2 * 16 + 15;
      return !var3.hasOnlyAir() && var3.maybeHas(var2x -> this.isCandidate(var2x, var4));
   }

   @Override
   protected int minimumY() {
      return Math.min(this.minScanY.getValue().intValue(), this.maxScanY.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.minScanY.getValue().intValue(), this.maxScanY.getValue().intValue()));
   }

   @Override
   protected void beforeScan() {
      ChunkPos var1 = getMc().player.chunkPosition();
      this.chunkFirstSeen
         .keySet()
         .removeIf(
            var2 -> {
               ChunkPos var3 = ChunkPos.unpack(var2);
               return Math.abs((long)var3.x() - var1.x()) > this.getScanRadius()
                  || Math.abs((long)var3.z() - var1.z()) > this.getScanRadius()
                  || getMc().level.getChunkSource().getChunkNow(var3.x(), var3.z()) == null;
            }
         );
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      long var2 = this.chunkFirstSeen.computeIfAbsent(var1.getPos().pack(), var1x -> this.getTickCount());
      this.honeyEligible = (boolean)(this.getTickCount() - var2 >= 200L ? 1 : 0);
   }

   @Override
   protected void beginScan() {
      this.evidenceCounter.clear();
      this.honeyEligible = (boolean)0;
      this.inEnd = (boolean)(getMc() != null && getMc().level != null && getMc().level.dimension().equals(Level.END) ? 1 : 0);
      this.cacheSettings();
   }

   private void cacheSettings() {
      this.scanMinY = this.minimumY();
      this.scanMaxY = this.maximumY();
      this.terrainEnabled = (boolean)(this.detectionMode.getValue().equals("Broad") && this.terrainClues.getValue() ? 1 : 0);
      this.honeyEnabled = this.honeyClues.getValue();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(BlockState var1, int var2, int var3, int var4) {
      if (var3 >= this.scanMinY && var3 <= this.scanMaxY) {
         BlockClassifier.Category var5 = this.evidenceCounter.classify(var1, var3, this.inEnd);
         if (var5 != BlockClassifier.Category.NONE) {
            if (var5 != BlockClassifier.Category.DEDICATED || !BlockClassifier.isFullHoneyHive(var1) || this.honeyEnabled && this.honeyEligible) {
               this.evidenceCounter.addCategorized(var5, var1, var2, var3, var4, this.inEnd, this.terrainEnabled);
            }
         }
      }
   }

   @Override
   protected SpawnerScanCache.ScanResult evidence() {
      BuildScanResult var1 = this.evidenceCounter
         .bestResultWithEnderThreshold(
            this.detectionMode.getValue(),
            this.minStorage.getValue().intValue(),
            this.minUtilities.getValue().intValue(),
            this.minEnderChests.getValue().intValue()
         );
      return new SpawnerScanCache.ScanResult(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(SpawnerScanCache.ScanResult var1) {
      return var1.base()
         .qualifies(
            this.detectionMode.getValue(),
            this.minStorage.getValue().intValue(),
            this.minUtilities.getValue().intValue(),
            this.minEnderChests.getValue().intValue()
         );
   }

   @Override
   protected String describe(SpawnerScanCache.ScanResult var1) {
      return var1.base().describe();
   }

   @Override
   protected FoundLocation alert(ChunkPos var1, SpawnerScanCache.ScanResult var2) {
      return FoundLocation.base(var1.getMinBlockX(), var1.getMinBlockZ(), var2.base());
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.evidenceCounter.clear();
      this.chunkFirstSeen.clear();
      this.honeyEligible = (boolean)0;
      this.cacheSettings();
      if (this.diagnosticLevel != null) {
         this.logDiagnostic("scan reset or lifecycle cleanup");
      }

      this.resetDiagnostic();
   }

   public List<Setting<?>> isDiagnosticActive() {
      return List.of(this.visibilityDiagnostic);
   }

   private void resetDiagnostic() {
      this.diagnosticTracker.reset();
      this.diagnosticLevel = null;
      this.diagnosticTick = 0L;
      this.diagnosticFailed = (boolean)0;
   }

   private boolean isDiagnosticActive(ClientLevel var1) {
      if (this.visibilityDiagnostic.getValue()
         && !this.diagnosticFailed
         && this.isRuntimeActive()
         && this.isInGame()
         && getMc().level == var1
         && getMc().isSameThread()) {
         if (this.diagnosticLevel != var1) {
            if (this.diagnosticLevel != null) {
               this.logDiagnostic("world changed");
            }

            this.resetDiagnostic();
            this.diagnosticLevel = var1;
            LarpLog.info(
               "BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed"
            );
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isInScanRange(BlockPos var1) {
      if (var1.getY() >= this.minimumY() && var1.getY() <= this.maximumY()) {
         ChunkPos var2 = getMc().player.chunkPosition();
         return Math.abs((long)(var1.getX() >> 4) - var2.x()) <= this.getScanRadius() && Math.abs((long)(var1.getZ() >> 4) - var2.z()) <= this.getScanRadius();
      } else {
         return false;
      }
   }

   public static boolean isStructureBlock(BlockState var0, int var1, boolean var2) {
      BlockClassifier.Category var3 = BlockClassifier.classify(var0, var1, var2);
      return var3 != BlockClassifier.Category.NONE && var3 != BlockClassifier.Category.ALTERED || var0.is(Blocks.SPAWNER);
   }

   public static boolean isTerrainBlock(BlockState var0) {
      return var0.is(Blocks.DEEPSLATE) || var0.is(Blocks.STONE) || var0.is(Blocks.NETHERRACK) || var0.is(Blocks.END_STONE);
   }

   private void recordObservation(BlockPos var1, BlockState var2, ChunkChangeTracker.Source var3) {
      if (this.isInScanRange(var1)) {
         if (!this.diagnosticTracker.hasBudget(this.diagnosticTick)) {
            this.diagnosticTracker.recordDrops(var3, 1L);
         } else {
            this.diagnosticTracker
               .recordUpdate(
                  var1.asLong(),
                  ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4),
                  var2.toString(),
                  isStructureBlock(var2, var1.getY(), this.diagnosticLevel.dimension().equals(Level.END)),
                  isTerrainBlock(var2),
                  var3,
                  this.diagnosticTick
               );
         }
      }
   }

   private void onChunkLoad(ClientLevel var1, LevelChunk var2) {
      try {
         if (!this.isDiagnosticActive(var1)) {
            return;
         }

         ChunkPos var3 = getMc().player.chunkPosition();
         ChunkPos var4 = var2.getPos();
         if (Math.abs((long)var4.x() - var3.x()) > this.getScanRadius() || Math.abs((long)var4.z() - var3.z()) > this.getScanRadius()) {
            return;
         }

         this.diagnosticTracker.countChunk();

         for (long var6 : this.diagnosticTracker.positionsInChunk(var4.pack())) {
            BlockPos var8 = BlockPos.of(var6);
            this.recordObservation(var8, var2.getBlockState(var8), ChunkChangeTracker.Source.CHUNK_SCAN);
         }

         int var10 = 0;

         for (BlockEntity var7 : var2.getBlockEntities().values()) {
            if (var10 == 256) {
               this.diagnosticTracker.recordDrops(ChunkChangeTracker.Source.CHUNK_SCAN, var2.getBlockEntities().size() - var10);
               break;
            }

            var10++;
            if (!var7.isRemoved()) {
               this.recordObservation(var7.getBlockPos(), var7.getBlockState(), ChunkChangeTracker.Source.CHUNK_SCAN);
            }
         }
      } catch (RuntimeException var9) {
         this.pauseDiagnostic(var9);
      }
   }

   public static void onBlockUpdate(ClientLevel var0, BlockPos var1, BlockState var2) {
      if (instance != null && instance.isEnabled()) {
         BaseFinder var3 = instance;
         if (var3 != null) {
            try {
               if (!var3.isDiagnosticActive(var0)) {
                  return;
               }

               if (var0.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4) == null) {
                  return;
               }

               var3.recordObservation(var1, var2, ChunkChangeTracker.Source.BLOCK_UPDATE);
            } catch (RuntimeException var5) {
               var3.pauseDiagnostic(var5);
            }
         }
      }
   }

   private void pauseDiagnostic(RuntimeException var1) {
      this.diagnosticFailed = (boolean)1;
      LarpLog.errorOnce("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void logDiagnostic(String var1) {
      LarpLog.info("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.diagnosticTracker.snapshot());

      for (ChunkChangeTracker.ChangeEvent var3 : this.diagnosticTracker.drainEvents(8)) {
         BlockPos var4 = BlockPos.of(var3.position());
         LarpLog.info(
            "BaseFinding",
            "Visibility observation [{}, {}, {}] {} tick {}: {} -> {} ({})",
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            var3.source(),
            var3.tick(),
            var3.before(),
            var3.after(),
            var3.transition()
         );
      }
   }

   @Override
   public void onTick() {
      super.onTick();
      int var10000 = this.isDiagnosticActive(getMc().level);
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.diagnosticTick++;
         long var12;
         var10000 = (var12 = this.diagnosticTick % 20L - 0L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.diagnosticTracker.expireOld(this.diagnosticTick);
         }

         long var13;
         var10000 = (var13 = this.diagnosticTick % 200L - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
         boolean var6 = false;
         int var5 = var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.logDiagnostic("10 seconds");
         }
      }
   }

   @Override
   public String getStatus() {
      return super.getStatus()
         + (
            !this.visibilityDiagnostic.getValue()
               ? ""
               : (this.diagnosticFailed ? " · diagnostic paused" : " · diagnostic: " + this.diagnosticTracker.snapshot().tracked() + " observations")
         );
   }
}

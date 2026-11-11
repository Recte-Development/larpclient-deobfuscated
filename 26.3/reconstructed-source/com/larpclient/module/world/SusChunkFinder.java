package com.larpclient.module.world;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.data.FoundLocation;
import com.larpclient.util.data.GeodeClueScore;
import com.larpclient.util.data.LightCaptureTracker;
import com.larpclient.util.data.LightLevelCache;
import com.larpclient.util.data.SpawnerScanCache;
import com.larpclient.util.world.BuildScanResult;
import com.larpclient.util.world.ChunkClusterer;
import com.larpclient.util.world.PlantScanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

public final class SusChunkFinder extends ChunkFindingModule {
   private final NumberSetting maxScanY = this.addSetting(
      new NumberSetting("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0)
   );
   private final NumberSetting minMatureClusters = this.addSetting(
      new NumberSetting("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final NumberSetting minMaturePercent = this.addSetting(
      new NumberSetting("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0)
   );
   private final NumberSetting sensitivity = this.addSetting(
      new NumberSetting("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0)
   );
   private final NumberSetting minScanY = this.addSetting(new NumberSetting("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final BooleanSetting amethystLight = this.addSetting(
      new BooleanSetting("Amethyst Light", "Server light 5 beside received amethyst; possible grown clusters", true)
   );
   private final BooleanSetting kelp = this.addSetting(new BooleanSetting("Kelp", "Tall or max-age kelp columns", true));
   private final BooleanSetting bamboo = this.addSetting(new BooleanSetting("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final BooleanSetting berries = this.addSetting(new BooleanSetting("Berries", "Fully mature sweet berry bushes", true));
   private final BooleanSetting vines = this.addSetting(new BooleanSetting("Vines", "Hanging vines at least 7 blocks long", true));
   private final BooleanSetting dripstone = this.addSetting(new BooleanSetting("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final BooleanSetting smartMode = this.addSetting(new BooleanSetting("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final NumberSetting mergeRadius = this.addSetting(new NumberSetting("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final BooleanSetting centroidMarker = this.addSetting(new BooleanSetting("Centroid Marker", "Show the centre of qualifying zones", true));
   private GeodeClueScore clues = GeodeClueScore.EMPTY;
   private List<ChunkClusterer.Cluster> zones = List.of();
   private final Set<Long> labeledZones = new HashSet<>();
   private LightLevelCache lightData;
   private int sampleY;
   private LevelChunk currentChunk;

   public SusChunkFinder() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      LightCaptureTracker.register();

      for (Setting var2 : this.detectionSettings()) {
         var2.onChange(var1 -> this.reset("detection changed"));
      }

      this.mergeRadius.visibleWhen(this.smartMode::getValue);
      this.centroidMarker.visibleWhen(this.smartMode::getValue);
   }

   @Override
   public List<Setting<?>> detectionSettings() {
      return List.of(
         this.maxScanY,
         this.minMatureClusters,
         this.minMaturePercent,
         this.sensitivity,
         this.minScanY,
         this.amethystLight,
         this.kelp,
         this.bamboo,
         this.berries,
         this.vines,
         this.dripstone,
         this.smartMode,
         this.mergeRadius
      );
   }

   @Override
   public List<Setting<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.centroidMarker);
      return List.copyOf(var1);
   }

   public static boolean isAmethyst(BlockState var0) {
      return var0.is(Blocks.AMETHYST_CLUSTER)
         || var0.is(Blocks.SMALL_AMETHYST_BUD)
         || var0.is(Blocks.MEDIUM_AMETHYST_BUD)
         || var0.is(Blocks.LARGE_AMETHYST_BUD);
   }

   private boolean isClueEnabled(PlantScanner.PlantType var1) {
      return switch (var1) {
         case KELP -> this.kelp.getValue();
         case BAMBOO -> this.bamboo.getValue();
         case SWEET_BERRY_BUSH -> this.berries.getValue();
         case VINE -> this.vines.getValue();
         case POINTED_DRIPSTONE -> this.dripstone.getValue();
      };
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return isAmethyst(var1) || PlantScanner.isTrackedPlant(var1);
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
   protected void beginScan() {
      this.clues = GeodeClueScore.EMPTY;
      this.sampleY = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.zones = List.of();
      this.labeledZones.clear();
      this.lightData = null;
      this.currentChunk = null;
   }

   @Override
   protected void beforeScan() {
      this.lightData = LightCaptureTracker.getTracker(getMc().level);

      for (long var2 : this.lightData.retainChunks()) {
         this.forgetChunk(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      ChunkPos var3 = var1.getPos();
      return super.sectionCandidate(var1, var2)
         || this.amethystLight.getValue() && this.lightData != null && this.lightData.hasSection(var3.x(), (var1.getMinY() >> 4) + var2, var3.z());
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      this.currentChunk = var1;
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.is(Blocks.AMETHYST_CLUSTER)) {
            this.countPrimary(var2);
         } else if (isAmethyst(var1)) {
            this.forgetChunk();
         }
      }
   }

   private BlockState getBlockAt(BlockPos var1) {
      if (var1.getY() < this.minimumY() || var1.getY() > this.maximumY()) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.currentChunk != null && var1.getX() >> 4 == this.currentChunk.getPos().x() && var1.getZ() >> 4 == this.currentChunk.getPos().z()) {
         return this.currentChunk.getBlockState(var1);
      }

      LevelChunk var2 = getMc().level.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      return var2 == null ? Blocks.AIR.defaultBlockState() : var2.getBlockState(var1);
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (PlantScanner.isTrackedPlant(var2)
            || this.amethystLight.getValue() && this.clues.light() < 16 && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            if (this.amethystLight.getValue()
               && this.clues.light() < 16
               && this.lightData != null
               && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))
               && PlantScanner.isAmethystClusterTip(var2, this.lightData.getLevel(var6.getX(), var4, var6.getZ()), var6, this::getBlockAt)) {
               this.clues = this.clues.plus(new GeodeClueScore(1, 0, 0, 0, 0, 0));
               this.sampleY = var4;
            }

            if (PlantScanner.isTrackedPlant(var2)) {
               PlantScanner.PlantType var7 = PlantScanner.classifyPlant(var2, var6, this::getBlockAt);
               if (var7 != null && this.isClueEnabled(var7)) {
                  this.clues = this.clues.plus(switch (var7) {
                     case KELP -> new GeodeClueScore(0, 1, 0, 0, 0, 0);
                     case BAMBOO -> new GeodeClueScore(0, 0, 1, 0, 0, 0);
                     case SWEET_BERRY_BUSH -> new GeodeClueScore(0, 0, 0, 1, 0, 0);
                     case VINE -> new GeodeClueScore(0, 0, 0, 0, 1, 0);
                     case POINTED_DRIPSTONE -> new GeodeClueScore(0, 0, 0, 0, 0, 1);
                  });
                  this.sampleY = var4;
               }
            }
         }
      }
   }

   @Override
   protected SpawnerScanCache.ScanResult evidence() {
      SpawnerScanCache.ScanResult var1 = super.evidence();
      return new SpawnerScanCache.ScanResult(
         var1.mature(), var1.buds(), 0, this.sampleY == Integer.MIN_VALUE ? var1.sampleY() : this.sampleY, BuildScanResult.EMPTY, this.clues, List.of()
      );
   }

   @Override
   protected boolean qualifies(SpawnerScanCache.ScanResult var1) {
      return var1.matureAmethyst(this.minMatureClusters.getValue().intValue(), this.minMaturePercent.getValue().intValue())
         || var1.sus().score() >= this.isAmethyst();
   }

   private int isAmethyst() {
      return this.sensitivity.getValue().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.smartMode.getValue() ? this.mergeRadius.getValue().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map var1 = this.getTracker().snapshotScanned();
      HashSet var2 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2x);
         }
      });
      ArrayList var3 = new ArrayList();

      for (ChunkClusterer.Cluster var5 : ChunkClusterer.clusterChunks(var2, this.zoneRadius())) {
         GeodeClueScore var6 = GeodeClueScore.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            SpawnerScanCache.ScanResult var11 = (SpawnerScanCache.ScanResult)var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= this.qualifies(var11);
         }

         int var12 = var7 == 0 && var6.score() < this.isAmethyst() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.reportEvidence(var10, (SpawnerScanCache.ScanResult)var1.get(var10), (boolean)var12);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.zones = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.labeledZones.clear();
   }

   @Override
   protected void drawEvidence(ChunkPos var1, SpawnerScanCache.ScanResult var2, EspRenderer var3, int var4) {
      if (this.smartMode.getValue() && this.centroidMarker.getValue()) {
         for (ChunkClusterer.Cluster var6 : this.zones) {
            if (var6.members().contains(var1.pack())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.labeledZones.size() < 24 && this.labeledZones.add(var7)) {
                  var3.drawLabel(new Vec3(var6.centerX(), var2.sampleY() + 2, var6.centerZ()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(SpawnerScanCache.ScanResult var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected FoundLocation alert(ChunkPos var1, SpawnerScanCache.ScanResult var2) {
      return FoundLocation.chunk("Suspicious area", this.describe(var2), var1.getMinBlockX(), var1.getMinBlockZ());
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}

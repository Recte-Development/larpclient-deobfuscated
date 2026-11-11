package com.larpclient.module.world;

import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.data.FoundLocation;
import com.larpclient.util.data.SpawnerScanCache;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.ChunkClusterer;
import com.larpclient.util.world.ChunkScanScheduler;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class ChunkFindingModule extends Module {
   private final NumberSetting scanRadius = this.addSetting(new NumberSetting("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final NumberSetting sectionsPerTick = this.addSetting(
      new NumberSetting("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0)
   );
   private final NumberSetting rescanSeconds = this.addSetting(
      new NumberSetting("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0)
   );
   private final NumberSetting markerY = this.addSetting(new NumberSetting("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final NumberSetting maxMarkers = this.addSetting(new NumberSetting("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final ModeSetting markerStyle = this.addSetting(
      new ModeSetting("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled")
   );
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final BooleanSetting tracers = this.addSetting(new BooleanSetting("Tracers", "Lines from the camera to chunk markers", false));
   private final BooleanSetting notifications = this.addSetting(
      new BooleanSetting("Notifications", "Deduplicated discoveries, at most one alert per second", true)
   );
   private final BooleanSetting chatAlerts = this.addSetting(new BooleanSetting("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final BooleanSetting alertSound = this.addSetting(new BooleanSetting("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final EspRenderer renderer = new EspRenderer(var1x -> this.addSetting(var1x));
   private final ChunkScanScheduler<LevelChunk> chunkScanner = new ChunkScanScheduler<>();
   private final SpawnerScanCache findingTracker = new SpawnerScanCache();
   private final ArrayDeque<ChunkFindingModule.PendingAlert> alertQueue = new ArrayDeque<>();
   private List<ChunkFindingModule.Marker> markers = List.of();
   private final Map<Long, Long> alertedChunks = new HashMap<>();
   private ClientLevel level;
   private long tickCount;
   private int primaryCount;
   private int secondaryCount;
   private int tertiaryCount;
   private int firstY;
   private int overflowCount;
   private final ChunkScanScheduler.ChunkSource<LevelChunk> scanCallback = new ChunkScanScheduler.ChunkSource<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return ChunkFindingModule.this.level.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return ChunkFindingModule.this.findingTracker
            .isScanDue(ChunkPos.pack(var1, var2), ChunkFindingModule.this.tickCount, ChunkFindingModule.this.rescanSeconds.getValue().longValue() * 20L);
      }

      public int sectionCount(LevelChunk var1) {
         return var1.getSections().length;
      }

      @Override
      public void begin() {
         ChunkFindingModule.this.primaryCount = ChunkFindingModule.this.secondaryCount = ChunkFindingModule.this.tertiaryCount = 0;
         ChunkFindingModule.this.firstY = Integer.MIN_VALUE;
         ChunkFindingModule.this.beginScan();
      }

      public void beginChunk(LevelChunk var1) {
         this.begin();
         ChunkFindingModule.this.beginChunk(var1);
      }

      public boolean isSectionCandidate(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         return var3 + 15L >= ChunkFindingModule.this.minimumY()
            && var3 <= ChunkFindingModule.this.maximumY()
            && ChunkFindingModule.this.sectionCandidate(var1, var2);
      }

      public void scanSection(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         LevelChunkSection var4 = var1.getSections()[var2];
         int var5 = (int)Math.min(15L, (long)ChunkFindingModule.this.maximumY() - var3);
         int var6 = (int)Math.max(0L, (long)ChunkFindingModule.this.minimumY() - var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  ChunkFindingModule.this.scanBlock(var1, var4.getBlockState(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         SpawnerScanCache.ScanResult var3 = ChunkFindingModule.this.evidence();
         long var4 = ChunkPos.pack(var1, var2);
         if (ChunkFindingModule.this.findingTracker.record_(var4, var3, ChunkFindingModule.this.qualifies(var3), ChunkFindingModule.this.tickCount)) {
            ChunkFindingModule.this.queueAlert(var4, var3);
         }
      }
   };

   protected ChunkFindingModule(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.renderer.drawLine(() -> true, () -> true, () -> true, () -> this.markerStyle.isMode("Filled"));
      this.scanRadius.onChange(var1x -> this.reset("radius changed"));
      this.rescanSeconds.onChange(var1x -> this.reset("rescan interval changed"));
      this.notifications.onChange(var1x -> {
         this.alertQueue.clear();
         this.overflowCount = 0;
      });
      this.chatAlerts.visibleWhen(this.notifications::getValue);
      this.alertSound.visibleWhen(this.notifications::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.renderMarkers());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.level) {
            ChunkPos var3 = var2x.getPos();
            this.findingTracker.remove(var3.pack());
            this.chunkScanner.invalidate(var3.x(), var3.z());
            this.alertQueue.removeIf(var1xx -> var1xx.key() == var3.pack());
         }
      });
   }

   protected abstract boolean candidate(BlockState var1);

   protected abstract boolean qualifies(SpawnerScanCache.ScanResult var1);

   protected abstract String describe(SpawnerScanCache.ScanResult var1);

   protected abstract FoundLocation alert(ChunkPos var1, SpawnerScanCache.ScanResult var2);

   protected abstract int tint();

   protected int minimumY() {
      return Integer.MIN_VALUE;
   }

   protected void beginScan() {
   }

   protected void beginChunk(LevelChunk var1) {
   }

   protected void beforeScan() {
   }

   protected void afterScan() {
   }

   protected void beginDraw() {
   }

   protected int zoneRadius() {
      return 0;
   }

   protected final SpawnerScanCache getTracker() {
      return this.findingTracker;
   }

   protected final long getTickCount() {
      return this.tickCount;
   }

   protected final void forgetChunk(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      this.chunkScanner.invalidate(var3.x(), var3.z());
      this.findingTracker.clearScanTime(var1);
   }

   protected final void reportEvidence(long var1, SpawnerScanCache.ScanResult var3, boolean var4) {
      if (this.findingTracker.updateFlagged(var1, var3, var4, this.tickCount)) {
         this.queueAlert(var1, var3);
      }
   }

   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      LevelChunkSection var3 = var1.getSections()[var2];
      return !var3.hasOnlyAir() && var3.maybeHas(this::candidate);
   }

   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      this.count(var2, var3, var4, var5);
   }

   protected void clearEvidence() {
   }

   protected SpawnerScanCache.ScanResult evidence() {
      return new SpawnerScanCache.ScanResult(this.primaryCount, this.secondaryCount, this.tertiaryCount, this.firstY);
   }

   public List<Setting<?>> detectionSettings() {
      return List.of();
   }

   protected final int getScanRadius() {
      return this.scanRadius.getValue().intValue();
   }

   public List<Setting<?>> scanSettings() {
      return List.of(this.scanRadius, this.sectionsPerTick, this.rescanSeconds);
   }

   public List<Setting<?>> markerSettings() {
      return List.of(this.markerY, this.maxMarkers, this.markerStyle, this.labels, this.tracers);
   }

   public List<Setting<?>> reset() {
      return List.of(this.notifications, this.chatAlerts, this.alertSound);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(BlockState var1, int var2);

   protected void count(BlockState var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(ChunkPos var1, SpawnerScanCache.ScanResult var2, EspRenderer var3, int var4) {
   }

   protected final void countPrimary(int var1) {
      this.primaryCount++;
      if (this.firstY == Integer.MIN_VALUE) {
         this.firstY = var1;
      }
   }

   protected final void forgetChunk() {
      this.secondaryCount++;
   }

   protected final void countTertiary(int var1) {
      this.tertiaryCount++;
      if (this.firstY == Integer.MIN_VALUE) {
         this.firstY = var1;
      }
   }

   protected final void reset(String var1) {
      if (this.findingTracker.scannedCount() > 0 || !this.alertQueue.isEmpty()) {
         LarpLog.debug("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.findingTracker.flaggedCount());
      }

      this.chunkScanner.reset();
      this.clearEvidence();
      this.findingTracker.clear();
      this.alertQueue.clear();
      this.alertedChunks.clear();
      NotificationManager.getInstance().dismiss(this.getName());
      this.markers = List.of();
      this.overflowCount = 0;
      this.tickCount = 0L;
   }

   @Override
   public void onEnable() {
      this.reset("activation");
      this.level = getMc().level;
      LarpLog.info("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.scanRadius.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.reset("disable or world suspension");
      this.level = null;
      LarpLog.debug("BaseFinding", "{} released world scan state", this.getName());
   }

   private void queueAlert(long var1, SpawnerScanCache.ScanResult var3) {
      ChunkPos var4 = ChunkPos.unpack(var1);
      LarpLog.debug("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.x(), var4.z(), this.describe(var3));
      if (this.notifications.getValue()) {
         if (this.alertQueue.size() < 64) {
            this.alertQueue.addLast(new ChunkFindingModule.PendingAlert(var1, var3));
         } else {
            this.overflowCount++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.level != minecraft.level) {
            this.reset("world changed");
            this.level = minecraft.level;
         }

         this.tickCount++;
         this.alertedChunks.values().removeIf(var1x -> this.tickCount - var1x >= 1200L);
         ChunkPos var1 = minecraft.player.chunkPosition();
         int var2 = this.scanRadius.getValue().intValue();
         int var3 = var1.x();
         int var4 = var1.z();
         this.findingTracker.retain(var4x -> {
            int var6x = ChunkPos.getX(var4x);
            int var7 = ChunkPos.getZ(var4x);
            return Math.abs((long)var6x - var3) <= var2 && Math.abs((long)var7 - var4) <= var2 && this.level.getChunkSource().getChunkNow(var6x, var7) != null;
         }, this.tickCount);
         this.beforeScan();
         this.chunkScanner.tick(this.scanCallback, var3, var4, var2, this.sectionsPerTick.getValue().intValue());
         this.afterScan();
         Map var5 = this.findingTracker.getFlaggedView();
         ArrayList var6 = new ArrayList(var5.size());

         for (Entry var8 : var5.entrySet()) {
            var6.add(
               new ChunkFindingModule.Marker((Long)var8.getKey(), (SpawnerScanCache.ScanResult)var8.getValue(), this.chunkDistanceSq((Long)var8.getKey()))
            );
         }

         var6.sort(Comparator.comparingDouble(var0 -> var0.distanceSq));
         int var13 = Math.min(var6.size(), this.maxMarkers.getValue().intValue());
         int var10 = var6.size();
         int var9 = var13;
         int var10001 = Integer.compare(var9, var10);
         this.markers = (var10001 | -var10001) >>> 31 == 0 ? var6 : List.copyOf(var6.subList(0, var13));
         long var15;
         int var10000 = (var15 = this.tickCount % 20L - 0L) == 0L ? 0 : (var15 < 0L ? -1 : 1);
         boolean var12 = false;
         int var11 = var10000;
         var10000 = Integer.signum(var11);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.deliverAlert();
         }
      }
   }

   private double chunkDistanceSq(long var1) {
      double var3 = ChunkPos.getX(var1) * 16 + 8.0 - minecraft.player.getX();
      double var5 = ChunkPos.getZ(var1) * 16 + 8.0 - minecraft.player.getZ();
      return var3 * var3 + var5 * var5;
   }

   private void deliverAlert() {
      if (this.notifications.getValue()) {
         Map var1 = this.findingTracker.snapshotFlagged();

         while (!this.alertQueue.isEmpty()) {
            ChunkFindingModule.PendingAlert var2 = this.alertQueue.removeFirst();
            if (var1.containsKey(var2.key())) {
               ChunkPos var3 = ChunkPos.unpack(var2.key());
               ChunkClusterer.Cluster var4 = ChunkClusterer.clusterChunks(var1.keySet(), this.zoneRadius())
                  .stream()
                  .filter(var1x -> var1x.members().contains(var2.key()))
                  .findFirst()
                  .orElseThrow();
               this.alertQueue.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.alertedChunks::containsKey);
               if (this.alertedChunks.size() + var4.members().size() > 1024) {
                  this.alertedChunks.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.alertedChunks.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.alertedChunks.size() < 1024) {
                     this.alertedChunks.put(var7, this.tickCount);
                  }
               }

               if (!var5) {
                  FoundLocation var9 = this.alert(var3, var2.evidence());
                  if (var4.members().size() > 1) {
                     var9 = var9.zone((int)Math.round(var4.centerX()), (int)Math.round(var4.centerZ()), var4.members().size());
                  }

                  double var10 = Math.hypot(var9.blockX() + 0.5 - minecraft.player.getX(), var9.blockZ() + 0.5 - minecraft.player.getZ());
                  NotificationManager.getInstance().notifyDefault(this.getName(), var9.summary(var10), NotificationManager.Type.INFO);
                  if (this.chatAlerts.getValue()) {
                     minecraft.gui
                        .chatListener()
                        .handleSystemMessage(var9.chatLine(this.getName(), ChunkPos.getX(var2.key()), ChunkPos.getZ(var2.key()), this.tint(), var10), false);
                  }

                  if (this.alertSound.getValue()) {
                     minecraft.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.overflowCount > 0) {
            NotificationManager.getInstance()
               .notifyDefault(this.getName(), this.overflowCount + " additional discoveries; see chunk markers", NotificationManager.Type.INFO);
            this.overflowCount = 0;
         }
      }
   }

   private void renderMarkers() {
      if (this.isRuntimeActive() && this.isInGame() && this.level == minecraft.level) {
         try {
            Map var1 = this.findingTracker.getFlaggedView();
            double var2 = Math.clamp(this.markerY.getValue(), this.level.getMinY(), this.level.getMaxY() - 1);
            double var4 = Math.sqrt(2.0) * (this.scanRadius.getValue() + 1.0) * 16.0;
            Vec3 var6 = minecraft.gameRenderer.mainCamera().position();
            int var7 = 0;
            this.beginDraw();

            for (ChunkFindingModule.Marker var9 : this.markers) {
               long var10 = var9.chunkKey;
               SpawnerScanCache.ScanResult var12 = (SpawnerScanCache.ScanResult)var1.get(var10);
               ChunkPos var13 = ChunkPos.unpack(var10);
               if (var12 != null && this.level.getChunkSource().getChunkNow(var13.x(), var13.z()) != null) {
                  double var14 = Math.sqrt(this.chunkDistanceSq(var10));
                  int var16 = this.renderer.resolveColor(this.tint(), var14, var4, 1.0, var13.hashCode());
                  AABB var17 = new AABB(var13.getMinBlockX(), var2, var13.getMinBlockZ(), var13.getMinBlockX() + 16, var2 + 0.35, var13.getMinBlockZ() + 16);
                  this.drawEvidence(var13, var12, this.renderer, var16);
                  this.renderer.drawBox(var17, this.markerStyle.getValue(), var16);
                  Vec3 var18 = new Vec3(var13.getMinBlockX() + 8, var2 + 0.4, var13.getMinBlockZ() + 8);
                  if (this.tracers.getValue()) {
                     this.renderer.drawLine(var6, var18, var16);
                  }

                  if (this.labels.getValue() && var7++ < 24) {
                     this.renderer.drawLabel(var18.add(0.0, 0.8, 0.0), this.labelFor(var9, var12), var16);
                  }
               }
            }
         } catch (RuntimeException var19) {
            LarpLog.errorOnce("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var19);
         }
      }
   }

   private String labelFor(ChunkFindingModule.Marker var1, SpawnerScanCache.ScanResult var2) {
      if (var1.evidence != var2) {
         return this.describe(var2);
      }

      if (var1.cachedLabel == null) {
         var1.cachedLabel = this.describe(var2);
      }

      return var1.cachedLabel;
   }

   @Override
   public String getStatus() {
      return this.findingTracker.flaggedCount() + " chunks";
   }

   private static final class Marker {
      final long chunkKey;
      final SpawnerScanCache.ScanResult evidence;
      final double distanceSq;
      String cachedLabel;

      Marker(long var1, SpawnerScanCache.ScanResult var3, double var4) {
         this.chunkKey = var1;
         this.evidence = var3;
         this.distanceSq = var4;
      }
   }

   private record PendingAlert(long key, SpawnerScanCache.ScanResult evidence) {
   }
}

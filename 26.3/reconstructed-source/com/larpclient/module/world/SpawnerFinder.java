package com.larpclient.module.world;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.data.FoundLocation;
import com.larpclient.util.data.GeodeClueScore;
import com.larpclient.util.data.SpawnerScanCache;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.BuildScanResult;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class SpawnerFinder extends ChunkFindingModule {
   public static final int mAX_HITS_PER_CHUNK = 64;
   public static final int mAX_DRAWN_SPAWNERS = 256;
   public static final int mAX_LABELS = 24;
   public static final int mAX_RINGS = 16;
   private final BooleanSetting trialSpawners = this.addSetting(new BooleanSetting("Trial Spawners", "Also find received trial spawners", false));
   private final BooleanSetting spawnerBoxes = this.addSetting(new BooleanSetting("Spawner Boxes", "Outline exact received spawner positions", true));
   private final BooleanSetting spawnerLabels = this.addSetting(new BooleanSetting("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final BooleanSetting rangeRings = this.addSetting(
      new BooleanSetting("Range Rings", "16-block reference ring; server activation ranges can differ", true)
   );
   private final BooleanSetting spawnerTracers = this.addSetting(
      new BooleanSetting("Spawner Tracers", "Lines from the camera to exact spawner positions", false)
   );
   private final List<SpawnerScanCache.SpawnerHit> hits = new ArrayList<>();
   private int drawnCount;
   private int labelCount;
   private int ringCount;

   public SpawnerFinder() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.trialSpawners.onChange(var1 -> this.reset("spawner types changed"));
   }

   @Override
   public List<Setting<?>> detectionSettings() {
      return List.of(this.trialSpawners);
   }

   @Override
   public List<Setting<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.spawnerBoxes, this.spawnerLabels, this.rangeRings, this.spawnerTracers));
      return List.copyOf(var1);
   }

   public static boolean isSpawner(BlockState var0) {
      return var0.is(Blocks.SPAWNER);
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return isSpawner(var1) || this.trialSpawners.getValue() && var1.is(Blocks.TRIAL_SPAWNER);
   }

   @Override
   protected void beginScan() {
      this.hits.clear();
   }

   @Override
   protected void clearEvidence() {
      this.hits.clear();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (this.candidate(var1)) {
         this.countTertiary(var2);
      }
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.countTertiary(var4);
         if (this.hits.size() < 64) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            boolean var7 = var2.is(Blocks.TRIAL_SPAWNER);
            String var8 = var7 ? "Trial Spawner" : "Spawner";

            try {
               BlockEntity var9 = var1.getBlockEntity(var6);
               Entity var10 = null;
               if (var9 instanceof SpawnerBlockEntity var11) {
                  var10 = var11.getSpawner().getOrCreateDisplayEntity(getMc().level, var6);
               } else if (var9 instanceof TrialSpawnerBlockEntity var12) {
                  TrialSpawner var13 = var12.getTrialSpawner();
                  var10 = var13.getStateData().getOrCreateDisplayEntity(var13, getMc().level, var12.getState());
               }

               if (var10 != null) {
                  var8 = var10.getType().getDescription().getString() + (var7 ? " Trial Spawner" : " Spawner");
               }
            } catch (RuntimeException var14) {
               LarpLog.errorOnce("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var14);
            }

            this.hits.add(new SpawnerScanCache.SpawnerHit(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected SpawnerScanCache.ScanResult evidence() {
      SpawnerScanCache.ScanResult var1 = super.evidence();
      return new SpawnerScanCache.ScanResult(0, 0, var1.spawners(), var1.sampleY(), BuildScanResult.EMPTY, GeodeClueScore.EMPTY, this.hits);
   }

   @Override
   protected boolean qualifies(SpawnerScanCache.ScanResult var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(SpawnerScanCache.ScanResult var1) {
      String var2 = var1.spawners() == 1 ? "" : "s";
      if (!var1.hits().isEmpty()) {
         SpawnerScanCache.SpawnerHit var3 = var1.hits().getFirst();
         return var3.name() + " · " + var1.spawners() + " block" + var2 + " · Y:" + var3.y2();
      } else {
         return var1.spawners() + " spawner block" + var2 + "; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected FoundLocation alert(ChunkPos var1, SpawnerScanCache.ScanResult var2) {
      return FoundLocation.spawners(var1.getMinBlockX(), var1.getMinBlockZ(), var2);
   }

   @Override
   protected void beginDraw() {
      this.drawnCount = this.labelCount = this.ringCount = 0;
   }

   @Override
   protected void drawEvidence(ChunkPos var1, SpawnerScanCache.ScanResult var2, EspRenderer var3, int var4) {
      for (SpawnerScanCache.SpawnerHit var6 : var2.hits()) {
         if (this.drawnCount >= 256) {
            break;
         }

         BlockPos var7 = new BlockPos(var1.getMinBlockX() + var6.x2(), var6.y2(), var1.getMinBlockZ() + var6.z2());
         BlockState var8 = getMc().level.getBlockState(var7);
         if (var6.trial() ? this.trialSpawners.getValue() && var8.is(Blocks.TRIAL_SPAWNER) : isSpawner(var8)) {
            this.drawnCount++;
            Vec3 var9 = Vec3.atCenterOf(var7);
            if (this.spawnerBoxes.getValue()) {
               var3.drawBox(new AABB(var7).inflate(0.03), "Box", var4);
            }

            if (this.spawnerLabels.getValue() && this.labelCount++ < 24) {
               var3.drawLabel(
                  var9.add(0.0, 1.0, 0.0),
                  var6.name() + " · " + Math.round(var9.distanceTo(getMc().player.position())) + "m · " + var7.getX() + ", " + var7.getY() + ", " + var7.getZ(),
                  var4
               );
            }

            if (this.spawnerTracers.getValue()) {
               var3.drawLine(getMc().gameRenderer.mainCamera().position(), var9, var4);
            }

            if (this.rangeRings.getValue() && this.ringCount++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.drawLine(
                     var9.add(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0), var9.add(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0), var4
                  );
               }
            }
         }
      }
   }

   @Override
   protected int tint() {
      return -943189;
   }
}

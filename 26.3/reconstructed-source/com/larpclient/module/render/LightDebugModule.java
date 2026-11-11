package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.world.LightLevelOverlay;
import com.larpclient.util.misc.LarpLog;
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
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;

public final class LightDebugModule extends Module {
   private final NumberSetting maxY = this.addSetting(new NumberSetting("Max Y", "Highest layer to inspect, starting at Y -64", -50.0, -64.0, 16.0, 1.0));
   private final NumberSetting chunkRadius = this.addSetting(
      new NumberSetting("Chunk Radius", "Loaded square radius around your current chunk", 8.0, 1.0, 12.0, 1.0)
   );
   private final NumberSetting alpha = this.addSetting(new NumberSetting("Alpha", "Opacity of light-strength colored block volumes", 160.0, 0.0, 255.0, 1.0));
   private final NumberSetting renderLimit = this.addSetting(
      new NumberSetting("Render Limit", "Nearest chunk light runs drawn per frame; all scan data is retained", 16384.0, 1024.0, 65536.0, 1024.0)
   );
   private final Map<Long, List<LightLevelOverlay.LightRun>> lightRuns = new HashMap<>();
   private List<LightDebugModule.RunChunk> runChunks = List.of();
   private ClientLevel level;
   private ChunkPos centerChunk;
   private int tickCounter;
   private int chunkIndex;
   private int cellIndex;
   private List<ChunkPos> scanQueue = List.of();
   private byte[] scanBuffer;
   private int scanMinY;
   private int scanMaxY;
   private boolean rescanPending;
   private final MutableBlockPos mutablePos = new MutableBlockPos();
   private static final int sCAN_BUDGET = 32768;

   public LightDebugModule() {
      super("Light Debug", "Show underground block light brighter than the sky", Category.RENDER, -1);
      this.maxY.onChange(var1 -> this.resetScan());
      this.chunkRadius.onChange(var1 -> this.resetScan());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderGizmos());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.level) {
            this.lightRuns.remove(var2.getPos().pack());
            this.rebuildRunChunks();
         }
      });
   }

   public List<Setting<?>> getRangeSettings() {
      return List.of(this.maxY, this.chunkRadius);
   }

   public List<Setting<?>> renderGizmos() {
      return List.of(this.alpha, this.renderLimit);
   }

   private void resetScan() {
      this.lightRuns.clear();
      this.runChunks = List.of();
      this.scanQueue = List.of();
      this.scanBuffer = null;
      this.centerChunk = null;
      this.chunkIndex = this.cellIndex = this.tickCounter = 0;
      this.rescanPending = (boolean)1;
   }

   @Override
   public void onEnable() {
      this.resetScan();
      this.level = getMc().level;
      LarpLog.info("BaseFinding", "Light Debug active: block light above sky, Y -64..{}, radius {}", this.maxY.getValue(), this.chunkRadius.getValue());
   }

   @Override
   public void onDisable() {
      LarpLog.debug("BaseFinding", "Light Debug cleanup: {} lit chunks", this.lightRuns.size());
      this.resetScan();
      this.level = null;
   }

   private void startScan(ChunkPos var1) {
      this.centerChunk = var1;
      this.scanMinY = Math.max(-64, this.level.getMinY());
      this.scanMaxY = Math.min(this.maxY.getValue().intValue(), this.level.getMaxY());
      int var2 = this.chunkRadius.getValue().intValue();
      ArrayList var3 = new ArrayList();
      this.lightRuns
         .keySet()
         .removeIf(
            var2x -> {
               ChunkPos var3x = ChunkPos.unpack(var2x);
               return Math.abs((long)var3x.x() - this.centerChunk.x()) > var2
                  || Math.abs((long)var3x.z() - this.centerChunk.z()) > var2
                  || this.level.getChunkSource().getChunkNow(var3x.x(), var3x.z()) == null;
            }
         );

      for (int var4 = -var2; var4 <= var2; var4++) {
         for (int var5 = -var2; var5 <= var2; var5++) {
            ChunkPos var6 = new ChunkPos(var1.x() + var4, var1.z() + var5);
            if (this.level.getChunkSource().getChunkNow(var6.x(), var6.z()) != null) {
               var3.add(var6);
            }
         }
      }

      var3.sort(
         Comparator.comparingLong(var1x -> (long)(var1x.x() - var1.x()) * (var1x.x() - var1.x()) + (long)(var1x.z() - var1.z()) * (var1x.z() - var1.z()))
      );
      this.scanQueue = List.copyOf(var3);
      this.chunkIndex = this.cellIndex = 0;
      this.scanBuffer = null;
      this.rescanPending = (boolean)0;
      this.rebuildRunChunks();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.level != minecraft.level) {
            this.resetScan();
            this.level = minecraft.level;
         }

         ChunkPos var1 = minecraft.player.chunkPosition();
         int var10000 = ++this.tickCounter % 16;
         boolean var12 = false;
         int var11 = var10000;
         var10000 = Integer.signum(var11);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.rescanPending = (boolean)1;
         }

         int var17 = var1.equals(this.centerChunk);
         boolean var14 = false;
         byte var13 = (byte)var17;
         var17 = Integer.signum(var13);
         if ((var17 | -var17) >>> 31 == 0) {
            this.scanQueue = List.of();
            this.scanBuffer = null;
            this.startScan(var1);
         } else {
            var17 = this.chunkIndex;
            int var8 = this.scanQueue.size();
            int var7 = var17;
            if (Integer.compare(var7, var8) >>> 31 == 0) {
               boolean var10 = false;
               byte var9 = this.rescanPending;
               var17 = Integer.signum(var9);
               if (((var17 | -var17) >>> 31 ^ 1) == 0) {
                  this.startScan(var1);
               }
            }
         }

         byte var2 = 0;
         int var3 = 32768;

         while (var3 > 0 && this.chunkIndex < this.scanQueue.size()) {
            ChunkPos var4 = this.scanQueue.get(this.chunkIndex);
            if (this.scanMaxY >= this.scanMinY && this.level.getChunkSource().getChunkNow(var4.x(), var4.z()) != null) {
               if (this.scanBuffer == null) {
                  this.scanBuffer = new byte[(this.scanMaxY - this.scanMinY + 1) * 256];
               }

               int var5 = Math.min(this.scanBuffer.length, this.cellIndex + var3);
               var3 -= var5 - this.cellIndex;

               while (this.cellIndex < var5) {
                  this.mutablePos
                     .set(var4.getMinBlockX() + (this.cellIndex & 15), this.scanMinY + (this.cellIndex >> 8), var4.getMinBlockZ() + (this.cellIndex >> 4 & 15));
                  int var6 = this.level.getBrightness(LightLayer.BLOCK, this.mutablePos);
                  if (var6 > 0 && LightLevelOverlay.isAboveThreshold(var6, this.level.getBrightness(LightLayer.SKY, this.mutablePos))) {
                     this.scanBuffer[this.cellIndex] = (byte)var6;
                  }

                  this.cellIndex++;
               }

               if (this.cellIndex == this.scanBuffer.length) {
                  List var15 = LightLevelOverlay.parseLightLayers(this.scanBuffer, var4.x(), var4.z(), this.scanMinY);
                  if (var15.isEmpty()) {
                     this.lightRuns.remove(var4.pack());
                  } else {
                     this.lightRuns.put(var4.pack(), var15);
                  }

                  this.chunkIndex++;
                  this.cellIndex = 0;
                  this.scanBuffer = null;
                  var2 = 1;
               }
            } else {
               this.lightRuns.remove(var4.pack());
               this.chunkIndex++;
               this.cellIndex = 0;
               this.scanBuffer = null;
               var2 = 1;
            }
         }

         if (var2 != 0) {
            this.rebuildRunChunks();
         }
      }
   }

   private void rebuildRunChunks() {
      if (this.centerChunk == null) {
         this.runChunks = List.of();
      } else {
         this.runChunks = this.lightRuns.entrySet().stream().sorted(Comparator.<Entry<Long, List<LightLevelOverlay.LightRun>>>comparingDouble(var1 -> {
            ChunkPos var2 = ChunkPos.unpack(var1.getKey());
            double var3 = (double)var2.x() - this.centerChunk.x();
            double var5 = (double)var2.z() - this.centerChunk.z();
            return var3 * var3 + var5 * var5;
         }).thenComparingLong(Entry::getKey)).map(var0 -> new LightDebugModule.RunChunk(var0.getKey(), var0.getValue())).toList();
      }
   }

   private void renderGizmos() {
      if (this.isRuntimeActive() && this.isInGame() && this.level == minecraft.level && !(this.alpha.getValue() <= 0.0)) {
         try {
            int var1 = 0;
            int var2 = this.renderLimit.getValue().intValue();

            for (LightDebugModule.RunChunk var4 : this.runChunks) {
               ChunkPos var5 = ChunkPos.unpack(var4.key());
               if (this.level.getChunkSource().getChunkNow(var5.x(), var5.z()) != null) {
                  for (LightLevelOverlay.LightRun var7 : var4.runs()) {
                     if (var1++ >= var2) {
                        return;
                     }

                     int var8 = LightLevelOverlay.lightColor(var7.light(), this.alpha.getValue().intValue());
                     Gizmos.cuboid(
                           new AABB(var7.blockX(), var7.blockY(), var7.blockZ(), var7.blockX() + var7.length(), var7.blockY() + 1, var7.blockZ() + 1),
                           GizmoStyle.fill(var8)
                        )
                        .setAlwaysOnTop();
                  }
               }
            }
         } catch (RuntimeException var9) {
            LarpLog.errorOnce("BaseFinding", "Light Debug render failed; scan continues", var9);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.runChunks.size() + " lit chunks";
   }

   private record RunChunk(long key, List<LightLevelOverlay.LightRun> runs) {
   }
}

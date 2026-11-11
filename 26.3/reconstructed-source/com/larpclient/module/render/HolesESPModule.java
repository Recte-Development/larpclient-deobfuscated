package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NetherPortalFrames;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class HolesESPModule extends Module {
   public static final int mAX_READS_PER_TICK = 8192;
   public static final int mAX_STEPS_PER_TICK = 2048;
   public static final int mAX_TOTAL_HOLES = 65536;
   private static final int mAX_HOLES_PER_CHUNK = 8192;
   private final NumberSetting opacity = this.addSetting(new NumberSetting("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final ColorSetting oneByOneColor = this.addSetting(new ColorSetting("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final ColorSetting threeByOneColor = this.addSetting(new ColorSetting("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final NumberSetting markerLimit = this.addSetting(
      new NumberSetting("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0)
   );
   private final Map<Long, Map<BlockPos, Integer>> holesByChunk = new HashMap<>();
   private final Set<Long> queuedChunks = new HashSet<>();
   private final ArrayDeque<Long> scanQueue = new ArrayDeque<>();
   private final Map<Long, NetherPortalFrames.CellType> cellCache = new HashMap<>();
   private final Set<Long> dirtyChunks = new HashSet<>();
   private Map<BlockPos, Integer> currentHoles;
   private List<HolesESPModule.Hole> visibleHoles = List.of();
   private ClientLevel level;
   private LevelChunk currentChunk;
   private long currentChunkKey;
   private int scanIndex;
   private int readsThisTick;
   private int tickCounter;
   private int totalHoles;
   private boolean ceilingLogged;
   private static HolesESPModule instance;

   public HolesESPModule() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      instance = this;
      this.markerLimit.onChange(var1 -> this.rebuildVisibleHoles());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderHoles());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.level == var1) {
            this.invalidateAround(var2.getPos());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.level == var1) {
            if (!this.isInGame()) {
               this.reset();
               this.level = null;
            } else {
               long var3 = var2.getPos().pack();
               this.removeChunkHoles(var3);
               this.queuedChunks.remove(var3);
               this.scanQueue.remove(var3);
               if (this.currentChunk != null && this.currentChunkKey == var3) {
                  this.clearCurrentChunk();
               }

               this.invalidateAround(var2.getPos());
               this.rebuildVisibleHoles();
            }
         }
      });
   }

   public List<Setting<?>> getColorSettings() {
      return List.of(this.opacity, this.oneByOneColor, this.threeByOneColor);
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.markerLimit);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   @Override
   public void onEnable() {
      this.reset();
      this.level = getMc().level;
      LarpLog.info("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.reset();
      this.level = null;
      LarpLog.debug("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void reset() {
      this.holesByChunk.clear();
      this.queuedChunks.clear();
      this.scanQueue.clear();
      this.cellCache.clear();
      this.dirtyChunks.clear();
      this.visibleHoles = List.of();
      this.totalHoles = this.tickCounter = 0;
      this.ceilingLogged = (boolean)0;
      this.clearCurrentChunk();
   }

   private void clearCurrentChunk() {
      this.currentChunk = null;
      this.currentHoles = null;
      this.scanIndex = 0;
   }

   private void removeChunkHoles(long var1) {
      Map var3 = this.holesByChunk.remove(var1);
      if (var3 != null) {
         this.totalHoles = this.totalHoles - var3.size();
      }
   }

   private int rebuildVisibleHoles() {
      return Math.clamp(((Integer)getMc().options.renderDistance().get()).intValue(), 1, 32);
   }

   private boolean isInRange(ChunkPos var1) {
      ChunkPos var2 = getMc().player.chunkPosition();
      int var3 = this.rebuildVisibleHoles();
      return Math.abs((long)var1.x() - var2.x()) <= var3 && Math.abs((long)var1.z() - var2.z()) <= var3;
   }

   private void queueChunk(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      if (this.isInRange(var3) && this.level.getChunkSource().getChunkNow(var3.x(), var3.z()) != null) {
         if (this.queuedChunks.add(var1)) {
            this.scanQueue.addLast(var1);
         }
      }
   }

   private void invalidateAround(ChunkPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = ChunkPos.pack(var1.x() + var2, var1.z() + var3);
            if (this.currentChunk != null && this.currentChunkKey == var4) {
               this.clearCurrentChunk();
            }

            this.removeChunkHoles(var4);
            this.queueChunk(var4);
         }
      }
   }

   public static void onBlockUpdate(ClientLevel var0, BlockPos var1) {
      if (instance != null && instance.isEnabled()) {
         HolesESPModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && var2.level == var0 && getMc().isSameThread()) {
            if (var2.dirtyChunks.size() < 4096) {
               var2.dirtyChunks.add(ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4));
            } else {
               var2.holesByChunk.clear();
               var2.totalHoles = 0;
               var2.tickCounter = 0;
            }
         }
      }
   }

   private NetherPortalFrames.CellType classifyCell(BlockPos var1) {
      if (this.level.isOutsideBuildHeight(var1)) {
         return NetherPortalFrames.CellType.OTHER;
      }

      NetherPortalFrames.CellType var2 = this.cellCache.get(var1.asLong());
      if (var2 != null) {
         return var2;
      }

      LevelChunk var3 = this.level.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      if (var3 == null) {
         return NetherPortalFrames.CellType.OTHER;
      }

      this.readsThisTick++;
      VoxelShape var4 = var3.getBlockState(var1).getCollisionShape(this.level, var1);
      NetherPortalFrames.CellType var5 = var4.equals(Shapes.block()) ? NetherPortalFrames.CellType.SOLID : NetherPortalFrames.CellType.EMPTY;
      this.cellCache.put(var1.asLong(), var5);
      return var5;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.level != minecraft.level) {
            this.reset();
            this.level = minecraft.level;
         }

         int var10000 = this.tickCounter;
         byte var15 = 1;
         int var14 = this.tickCounter;
         this.tickCounter = var14 + var15;
         var10000 %= 20;
         boolean var17 = false;
         int var16 = var10000;
         var10000 = Integer.signum(var16);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.pruneChunks();
         }

         if (!this.dirtyChunks.isEmpty()) {
            for (long var2 : this.dirtyChunks) {
               this.invalidateAround(ChunkPos.unpack(var2));
            }

            this.dirtyChunks.clear();
            this.rebuildVisibleHoles();
         }

         this.cellCache.clear();
         this.readsThisTick = 0;
         byte var18 = 0;

         for (int var19 = 0; var19 < 2048 && this.readsThisTick < 8064; var19++) {
            if (this.currentChunk == null) {
               Long var3 = this.scanQueue.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.queuedChunks.remove(var3);
               ChunkPos var4 = ChunkPos.unpack(var3);
               this.currentChunk = this.level.getChunkSource().getChunkNow(var4.x(), var4.z());
               if (this.currentChunk == null || !this.isInRange(var4)) {
                  this.currentChunk = null;
                  continue;
               }

               this.currentChunkKey = var3;
               this.currentHoles = new HashMap<>();
               this.scanIndex = 0;
            }

            int var20 = this.level.getHeight();
            if (this.scanIndex >= 256 * var20) {
               this.removeChunkHoles(this.currentChunkKey);
               this.holesByChunk.put(this.currentChunkKey, this.currentHoles);
               this.totalHoles = this.totalHoles + this.currentHoles.size();
               this.clearCurrentChunk();
               var18 = 1;
            } else {
               int var21 = this.scanIndex;
               int var5 = this.level.getMinY() + var21 % var20;
               int var6 = this.currentChunk.getSectionIndex(var5);
               if (this.currentChunk.getSections()[var6].hasOnlyAir()) {
                  this.scanIndex = this.scanIndex + Math.min(16 - (var5 & 15), var20 - var21 % var20);
               } else {
                  this.scanIndex++;
                  int var7 = var21 / var20;
                  BlockPos var8 = new BlockPos(
                     this.currentChunk.getPos().getMinBlockX() + var7 / 16, var5, this.currentChunk.getPos().getMinBlockZ() + var7 % 16
                  );
                  int var9 = NetherPortalFrames.findFrameAxis(var8, this.level.getMaxY(), this::classifyCell);
                  if (var9 >= 0) {
                     if (this.currentHoles.size() < 8192 && this.totalHoles + this.currentHoles.size() < 65536) {
                        this.currentHoles.put(var8, var9);
                     } else if (!this.ceilingLogged) {
                        this.ceilingLogged = (boolean)1;
                        LarpLog.info("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.cellCache.clear();
         boolean var11 = false;
         byte var10 = var18;
         var10000 = Integer.signum(var10);
         if ((var10000 | -var10000) >>> 31 == 0) {
            var10000 = this.tickCounter % 20;
            boolean var13 = false;
            int var12 = var10000;
            var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 != 0) {
               return;
            }
         }

         this.rebuildVisibleHoles();
      }
   }

   private void pruneChunks() {
      for (long var2 : new ArrayList<>(this.holesByChunk.keySet())) {
         ChunkPos var4 = ChunkPos.unpack(var2);
         if (!this.isInRange(var4) || this.level.getChunkSource().getChunkNow(var4.x(), var4.z()) == null) {
            this.removeChunkHoles(var2);
         }
      }

      if (this.currentChunk != null
         && (
            !this.isInRange(this.currentChunk.getPos())
               || this.level.getChunkSource().getChunkNow(this.currentChunk.getPos().x(), this.currentChunk.getPos().z()) != this.currentChunk
         )) {
         this.clearCurrentChunk();
      }

      this.scanQueue.removeIf(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         int var3x = this.isInRange(var2x) && this.level.getChunkSource().getChunkNow(var2x.x(), var2x.z()) != null ? 0 : 1;
         if (var3x != 0) {
            this.queuedChunks.remove(var1);
         }

         return (boolean)var3x;
      });
      ChunkPos var8 = minecraft.player.chunkPosition();
      int var9 = this.rebuildVisibleHoles();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.x() - var9; var10 <= var8.x() + var9; var10++) {
         for (int var5 = var8.z() - var9; var5 <= var8.z() + var9; var5++) {
            long var6 = ChunkPos.pack(var10, var5);
            if (!this.holesByChunk.containsKey(var6)
               && (this.currentChunk == null || this.currentChunkKey != var6)
               && this.level.getChunkSource().getChunkNow(var10, var5) != null
               && !this.queuedChunks.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         return Math.pow(var2x.x() - var8.x(), 2.0) + Math.pow(var2x.z() - var8.z(), 2.0);
      }));

      for (long var12 : var3) {
         this.queueChunk(var12);
      }
   }

   private void rebuildVisibleHoles() {
      if (this.level != null && getMc().player != null) {
         Vec3 var1 = minecraft.player.position();
         this.visibleHoles = this.holesByChunk
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESPModule.Hole(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1x -> var1x.pos().distToCenterSqr(var1)))
            .limit(this.markerLimit.getValue().longValue())
            .toList();
      } else {
         this.visibleHoles = List.of();
      }
   }

   private void renderHoles() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.level == minecraft.level) {
            int var1 = Math.clamp(this.opacity.getValue().intValue(), 0, 255);
            if (var1 != 0) {
               ChunkPos var2 = minecraft.player.chunkPosition();
               int var3 = this.rebuildVisibleHoles();

               for (HolesESPModule.Hole var5 : this.visibleHoles) {
                  if (Math.abs((long)(var5.pos().getX() >> 4) - var2.x()) <= var3 && Math.abs((long)(var5.pos().getZ() >> 4) - var2.z()) <= var3) {
                     int var6 = var5.kind() == 0 ? this.oneByOneColor.getValue() : this.threeByOneColor.getValue();
                     Gizmos.cuboid(NetherPortalFrames.frameBox(var5.pos(), var5.kind()), GizmoStyle.fill(var1 << 24 | var6 & 16777215)).setAlwaysOnTop();
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.totalHoles + " passages";
   }

   private record Hole(BlockPos pos, int kind) {
   }
}

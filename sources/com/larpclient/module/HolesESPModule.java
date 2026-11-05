package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.za;
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
   public static final int V = 8192;
   public static final int a = 2048;
   public static final int w = 65536;
   private static final int s = 8192;
   private final d9 J = this.addSetting(new d9("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final dk S = this.addSetting(new dk("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final dk e = this.addSetting(new dk("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final d9 j = this.addSetting(new d9("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0));
   private final Map<Long, Map<BlockPos, Integer>> b = new HashMap<>();
   private final Set<Long> k = new HashSet<>();
   private final ArrayDeque<Long> x = new ArrayDeque<>();
   private final Map<Long, za.y> c = new HashMap<>();
   private final Set<Long> o = new HashSet<>();
   private Map<BlockPos, Integer> Q;
   private List<HolesESPModule.x> i = List.of();
   private ClientLevel P;
   private LevelChunk D;
   private long L;
   private int W;
   private int T;
   private int n;
   private int M;
   private boolean C;
   private static HolesESPModule l;

   public HolesESPModule() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      l = this;
      this.j.h(var1 -> this.U());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.I());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.P == var1) {
            this.P(var2.getPos());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.P == var1) {
            if (!this.isInGame()) {
               this.X();
               this.P = null;
            } else {
               long var3 = var2.getPos().pack();
               this.E(var3);
               this.k.remove(var3);
               this.x.remove(var3);
               if (this.D != null && this.L == var3) {
                  this.Y();
               }

               this.P(var2.getPos());
               this.U();
            }
         }
      });
   }

   public List<df<?>> m() {
      return List.of(this.J, this.S, this.e);
   }

   public List<df<?>> X() {
      return List.of(this.j);
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.m);
   }

   @Override
   public void onEnable() {
      this.X();
      this.P = getMc().level;
      mt.K("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.X();
      this.P = null;
      mt.U("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void X() {
      this.b.clear();
      this.k.clear();
      this.x.clear();
      this.c.clear();
      this.o.clear();
      this.i = List.of();
      this.M = this.n = 0;
      this.C = (boolean)0;
      this.Y();
   }

   private void Y() {
      this.D = null;
      this.Q = null;
      this.W = 0;
   }

   private void E(long var1) {
      Map var3 = this.b.remove(var1);
      if (var3 != null) {
         this.M = this.M - var3.size();
      }
   }

   private int v() {
      return Math.clamp(((Integer)getMc().options.renderDistance().get()).intValue(), 1, 32);
   }

   private boolean y(ChunkPos var1) {
      ChunkPos var2 = getMc().player.chunkPosition();
      int var3 = this.v();
      return Math.abs((long)var1.x() - var2.x()) <= var3 && Math.abs((long)var1.z() - var2.z()) <= var3;
   }

   private void Q(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      if (this.y(var3) && this.P.getChunkSource().getChunkNow(var3.x(), var3.z()) != null) {
         if (this.k.add(var1)) {
            this.x.addLast(var1);
         }
      }
   }

   private void P(ChunkPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = ChunkPos.pack(var1.x() + var2, var1.z() + var3);
            if (this.D != null && this.L == var4) {
               this.Y();
            }

            this.E(var4);
            this.Q(var4);
         }
      }
   }

   public static void Q(ClientLevel var0, BlockPos var1) {
      if (l != null && l.isEnabled()) {
         HolesESPModule var2 = l;
         if (var2 != null && var2.isRuntimeActive() && var2.P == var0 && getMc().isSameThread()) {
            if (var2.o.size() < 4096) {
               var2.o.add(ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4));
            } else {
               var2.b.clear();
               var2.M = 0;
               var2.n = 0;
            }
         }
      }
   }

   private za.y x(BlockPos var1) {
      if (this.P.isOutsideBuildHeight(var1)) {
         return za.y.b;
      }

      za.y var2 = this.c.get(var1.asLong());
      if (var2 != null) {
         return var2;
      }

      LevelChunk var3 = this.P.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      if (var3 == null) {
         return za.y.b;
      }

      this.T++;
      VoxelShape var4 = var3.getBlockState(var1).getCollisionShape(this.P, var1);
      za.y var5 = var4.equals(Shapes.block()) ? za.y.z : za.y.B;
      this.c.put(var1.asLong(), var5);
      return var5;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.P != mc.level) {
            this.X();
            this.P = mc.level;
         }

         int var10000 = this.n;
         byte var11 = 1;
         int var10 = this.n;
         this.n = var10 + var11;
         if (var10000 % 20 == 0) {
            this.C();
         }

         int var22 = this.o.isEmpty();
         boolean var13 = false;
         byte var12 = (byte)var22;
         var22 = Integer.signum(var12);
         if ((var22 | -var22) >>> 31 == 0) {
            for (long var2 : this.o) {
               this.P(ChunkPos.unpack(var2));
            }

            this.o.clear();
            this.U();
         }

         this.c.clear();
         this.T = 0;
         byte var18 = 0;

         for (int var19 = 0; var19 < 2048 && this.T < 8064; var19++) {
            if (this.D == null) {
               Long var3 = this.x.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.k.remove(var3);
               ChunkPos var4 = ChunkPos.unpack(var3);
               this.D = this.P.getChunkSource().getChunkNow(var4.x(), var4.z());
               if (this.D == null || !this.y(var4)) {
                  this.D = null;
                  continue;
               }

               this.L = var3;
               this.Q = new HashMap<>();
               this.W = 0;
            }

            int var20 = this.P.getHeight();
            if (this.W >= 256 * var20) {
               this.E(this.L);
               this.b.put(this.L, this.Q);
               this.M = this.M + this.Q.size();
               this.Y();
               var18 = 1;
            } else {
               int var21 = this.W;
               int var5 = this.P.getMinY() + var21 % var20;
               int var6 = this.D.getSectionIndex(var5);
               if (this.D.getSections()[var6].hasOnlyAir()) {
                  this.W = this.W + Math.min(16 - (var5 & 15), var20 - var21 % var20);
               } else {
                  this.W++;
                  int var7 = var21 / var20;
                  BlockPos var8 = new BlockPos(this.D.getPos().getMinBlockX() + var7 / 16, var5, this.D.getPos().getMinBlockZ() + var7 % 16);
                  int var9 = za.s(var8, this.P.getMaxY(), this::x);
                  if (var9 >= 0) {
                     if (this.Q.size() < 8192 && this.M + this.Q.size() < 65536) {
                        this.Q.put(var8, var9);
                     } else if (!this.C) {
                        this.C = (boolean)1;
                        mt.K("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.c.clear();
         boolean var15 = false;
         byte var14 = var18;
         var22 = Integer.signum(var14);
         if ((var22 | -var22) >>> 31 == 0) {
            var22 = this.n % 20;
            boolean var17 = false;
            int var16 = var22;
            var22 = Integer.signum(var16);
            if ((var22 | -var22) >>> 31 != 0) {
               return;
            }
         }

         this.U();
      }
   }

   private void C() {
      for (long var2 : new ArrayList<>(this.b.keySet())) {
         ChunkPos var4 = ChunkPos.unpack(var2);
         if (!this.y(var4) || this.P.getChunkSource().getChunkNow(var4.x(), var4.z()) == null) {
            this.E(var2);
         }
      }

      if (this.D != null && (!this.y(this.D.getPos()) || this.P.getChunkSource().getChunkNow(this.D.getPos().x(), this.D.getPos().z()) != this.D)) {
         this.Y();
      }

      this.x.removeIf(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         int var3x = this.y(var2x) && this.P.getChunkSource().getChunkNow(var2x.x(), var2x.z()) != null ? 0 : 1;
         if (var3x != 0) {
            this.k.remove(var1);
         }

         return (boolean)var3x;
      });
      ChunkPos var8 = mc.player.chunkPosition();
      int var9 = this.v();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.x() - var9; var10 <= var8.x() + var9; var10++) {
         for (int var5 = var8.z() - var9; var5 <= var8.z() + var9; var5++) {
            long var6 = ChunkPos.pack(var10, var5);
            if (!this.b.containsKey(var6)
               && (this.D == null || this.L != var6)
               && this.P.getChunkSource().getChunkNow(var10, var5) != null
               && !this.k.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         return Math.pow(var2x.x() - var8.x(), 2.0) + Math.pow(var2x.z() - var8.z(), 2.0);
      }));

      for (long var12 : var3) {
         this.Q(var12);
      }
   }

   private void U() {
      if (this.P != null && getMc().player != null) {
         Vec3 var1 = mc.player.position();
         this.i = this.b
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESPModule.x(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1x -> var1x.pos().distToCenterSqr(var1)))
            .limit(this.j.P().longValue())
            .toList();
      } else {
         this.i = List.of();
      }
   }

   private void I() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.P == mc.level) {
            int var1 = Math.clamp(this.J.P().intValue(), 0, 255);
            if (var1 != 0) {
               ChunkPos var2 = mc.player.chunkPosition();
               int var3 = this.v();

               for (HolesESPModule.x var5 : this.i) {
                  if (Math.abs((long)(var5.pos().getX() >> 4) - var2.x()) <= var3 && Math.abs((long)(var5.pos().getZ() >> 4) - var2.z()) <= var3) {
                     int var6 = var5.kind() == 0 ? this.S.P() : this.e.P();
                     Gizmos.cuboid(za.A(var5.pos(), var5.kind()), GizmoStyle.fill(var1 << 24 | var6 & 16777215)).setAlwaysOnTop();
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.M + " passages";
   }

   private record x(BlockPos pos, int kind) {
   }
}

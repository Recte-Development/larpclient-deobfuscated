package com.larpclient.module;

import com.larpclient.g4;
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
   public static final int v = 8192;
   public static final int j = 2048;
   public static final int a = 65536;
   private static final int K = 8192;
   private final e A = this.addSetting(new e("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final com.larpclient.module.i d = this.addSetting(new com.larpclient.module.i("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final com.larpclient.module.i x = this.addSetting(new com.larpclient.module.i("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final e b = this.addSetting(new e("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0));
   private final Map<Long, Map<BlockPos, Integer>> G = new HashMap<>();
   private final Set<Long> m = new HashSet<>();
   private final ArrayDeque<Long> F = new ArrayDeque<>();
   private final Map<Long, g4.u> f = new HashMap<>();
   private final Set<Long> y = new HashSet<>();
   private Map<BlockPos, Integer> o;
   private List<HolesESPModule.i> u = List.of();
   private ClientLevel Q;
   private LevelChunk q;
   private long k;
   private int z;
   private int U;
   private int O;
   private int e;
   private boolean g;
   private static HolesESPModule i;

   public HolesESPModule() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      i = this;
      this.b.o(var1 -> this.j());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.A());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.Q == var1) {
            this.I(var2.getPos());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.Q == var1) {
            if (!this.isInGame()) {
               this.C();
               this.Q = null;
            } else {
               long var3 = var2.getPos().pack();
               this.X(var3);
               this.m.remove(var3);
               this.F.remove(var3);
               if (this.q != null && this.k == var3) {
                  this.u();
               }

               this.I(var2.getPos());
               this.j();
            }
         }
      });
   }

   public List<q<?>> l() {
      return List.of(this.A, this.d, this.x);
   }

   public List<q<?>> G() {
      return List.of(this.b);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.k);
   }

   @Override
   public void onEnable() {
      this.C();
      this.Q = getMc().level;
      com.larpclient.h.A("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.C();
      this.Q = null;
      com.larpclient.h.U("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void C() {
      this.G.clear();
      this.m.clear();
      this.F.clear();
      this.f.clear();
      this.y.clear();
      this.u = List.of();
      this.e = this.O = 0;
      this.g = (boolean)0;
      this.u();
   }

   private void u() {
      this.q = null;
      this.o = null;
      this.z = 0;
   }

   private void X(long var1) {
      Map var3 = this.G.remove(var1);
      if (var3 != null) {
         this.e = this.e - var3.size();
      }
   }

   private int u() {
      return Math.clamp(((Integer)getMc().options.renderDistance().get()).intValue(), 1, 32);
   }

   private boolean G(ChunkPos var1) {
      ChunkPos var2 = getMc().player.chunkPosition();
      int var3 = this.u();
      return Math.abs((long)var1.x() - var2.x()) <= var3 && Math.abs((long)var1.z() - var2.z()) <= var3;
   }

   private void U(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      if (this.G(var3) && this.Q.getChunkSource().getChunkNow(var3.x(), var3.z()) != null) {
         if (this.m.add(var1)) {
            this.F.addLast(var1);
         }
      }
   }

   private void I(ChunkPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = ChunkPos.pack(var1.x() + var2, var1.z() + var3);
            if (this.q != null && this.k == var4) {
               this.u();
            }

            this.X(var4);
            this.U(var4);
         }
      }
   }

   public static void K(ClientLevel var0, BlockPos var1) {
      if (i != null && i.isEnabled()) {
         HolesESPModule var2 = i;
         if (var2 != null && var2.isRuntimeActive() && var2.Q == var0 && getMc().isSameThread()) {
            if (var2.y.size() < 4096) {
               var2.y.add(ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4));
            } else {
               var2.G.clear();
               var2.e = 0;
               var2.O = 0;
            }
         }
      }
   }

   private g4.u b(BlockPos var1) {
      if (this.Q.isOutsideBuildHeight(var1)) {
         return g4.u.Z;
      }

      g4.u var2 = this.f.get(var1.asLong());
      if (var2 != null) {
         return var2;
      }

      LevelChunk var3 = this.Q.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      if (var3 == null) {
         return g4.u.Z;
      }

      this.U++;
      VoxelShape var4 = var3.getBlockState(var1).getCollisionShape(this.Q, var1);
      g4.u var5 = var4.equals(Shapes.block()) ? g4.u.O : g4.u.M;
      this.f.put(var1.asLong(), var5);
      return var5;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.Q != mc.level) {
            this.C();
            this.Q = mc.level;
         }

         int var10000 = this.O;
         byte var13 = 1;
         int var12 = this.O;
         this.O = var12 + var13;
         var10000 %= 20;
         boolean var15 = false;
         int var14 = var10000;
         var10000 = Integer.signum(var14);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.f();
         }

         int var24 = this.y.isEmpty();
         boolean var17 = false;
         byte var16 = (byte)var24;
         var24 = Integer.signum(var16);
         if ((var24 | -var24) >>> 31 == 0) {
            for (long var2 : this.y) {
               this.I(ChunkPos.unpack(var2));
            }

            this.y.clear();
            this.j();
         }

         this.f.clear();
         this.U = 0;
         byte var18 = 0;

         for (int var19 = 0; var19 < 2048 && this.U < 8064; var19++) {
            if (this.q == null) {
               Long var3 = this.F.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.m.remove(var3);
               ChunkPos var4 = ChunkPos.unpack(var3);
               this.q = this.Q.getChunkSource().getChunkNow(var4.x(), var4.z());
               if (this.q == null || !this.G(var4)) {
                  this.q = null;
                  continue;
               }

               this.k = var3;
               this.o = new HashMap<>();
               this.z = 0;
            }

            int var20 = this.Q.getHeight();
            if (this.z >= 256 * var20) {
               this.X(this.k);
               this.G.put(this.k, this.o);
               this.e = this.e + this.o.size();
               this.u();
               var18 = 1;
            } else {
               int var21 = this.z;
               int var5 = this.Q.getMinY() + var21 % var20;
               int var6 = this.q.getSectionIndex(var5);
               if (this.q.getSections()[var6].hasOnlyAir()) {
                  this.z = this.z + Math.min(16 - (var5 & 15), var20 - var21 % var20);
               } else {
                  this.z++;
                  int var7 = var21 / var20;
                  BlockPos var8 = new BlockPos(this.q.getPos().getMinBlockX() + var7 / 16, var5, this.q.getPos().getMinBlockZ() + var7 % 16);
                  int var9 = g4.K(var8, this.Q.getMaxY(), this::b);
                  if (var9 >= 0) {
                     if (this.o.size() < 8192 && this.e + this.o.size() < 65536) {
                        this.o.put(var8, var9);
                     } else if (!this.g) {
                        this.g = (boolean)1;
                        com.larpclient.h.A("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.f.clear();
         if (var18 == 0) {
            var24 = this.O % 20;
            boolean var11 = false;
            int var10 = var24;
            var24 = Integer.signum(var10);
            if ((var24 | -var24) >>> 31 != 0) {
               return;
            }
         }

         this.j();
      }
   }

   private void f() {
      for (long var2 : new ArrayList<>(this.G.keySet())) {
         ChunkPos var4 = ChunkPos.unpack(var2);
         if (!this.G(var4) || this.Q.getChunkSource().getChunkNow(var4.x(), var4.z()) == null) {
            this.X(var2);
         }
      }

      if (this.q != null && (!this.G(this.q.getPos()) || this.Q.getChunkSource().getChunkNow(this.q.getPos().x(), this.q.getPos().z()) != this.q)) {
         this.u();
      }

      this.F.removeIf(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         int var3x = this.G(var2x) && this.Q.getChunkSource().getChunkNow(var2x.x(), var2x.z()) != null ? 0 : 1;
         if (var3x != 0) {
            this.m.remove(var1);
         }

         return (boolean)var3x;
      });
      ChunkPos var8 = mc.player.chunkPosition();
      int var9 = this.u();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.x() - var9; var10 <= var8.x() + var9; var10++) {
         for (int var5 = var8.z() - var9; var5 <= var8.z() + var9; var5++) {
            long var6 = ChunkPos.pack(var10, var5);
            if (!this.G.containsKey(var6)
               && (this.q == null || this.k != var6)
               && this.Q.getChunkSource().getChunkNow(var10, var5) != null
               && !this.m.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         return Math.pow(var2x.x() - var8.x(), 2.0) + Math.pow(var2x.z() - var8.z(), 2.0);
      }));

      for (long var12 : var3) {
         this.U(var12);
      }
   }

   private void j() {
      if (this.Q != null && getMc().player != null) {
         Vec3 var1 = mc.player.position();
         this.u = this.G
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESPModule.i(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1x -> var1x.pos().distToCenterSqr(var1)))
            .limit(this.b.getValue().longValue())
            .toList();
      } else {
         this.u = List.of();
      }
   }

   private void A() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.Q == mc.level) {
            int var1 = Math.clamp(this.A.getValue().intValue(), 0, 255);
            if (var1 != 0) {
               ChunkPos var2 = mc.player.chunkPosition();
               int var3 = this.u();

               for (HolesESPModule.i var5 : this.u) {
                  if (Math.abs((long)(var5.pos().getX() >> 4) - var2.x()) <= var3 && Math.abs((long)(var5.pos().getZ() >> 4) - var2.z()) <= var3) {
                     int var6 = var5.kind() == 0 ? this.d.getValue() : this.x.getValue();
                     Gizmos.cuboid(g4.t(var5.pos(), var5.kind()), GizmoStyle.fill(var1 << 24 | var6 & 16777215)).setAlwaysOnTop();
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.e + " passages";
   }

   private record i(BlockPos pos, int kind) {
   }
}

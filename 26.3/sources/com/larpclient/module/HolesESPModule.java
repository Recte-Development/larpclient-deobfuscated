package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.os;
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
   public static final int x = 8192;
   public static final int j = 2048;
   public static final int a = 65536;
   private static final int u = 8192;
   private final b l = this.addSetting(new b("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final h d = this.addSetting(new h("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final h A = this.addSetting(new h("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final b g = this.addSetting(new b("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0));
   private final Map<Long, Map<BlockPos, Integer>> q = new HashMap<>();
   private final Set<Long> V = new HashSet<>();
   private final ArrayDeque<Long> p = new ArrayDeque<>();
   private final Map<Long, os.b> T = new HashMap<>();
   private final Set<Long> b = new HashSet<>();
   private Map<BlockPos, Integer> Y;
   private List<HolesESPModule.k> k = List.of();
   private ClientLevel o;
   private LevelChunk E;
   private long c;
   private int S;
   private int y;
   private int O;
   private int m;
   private boolean K;
   private static HolesESPModule Q;

   public HolesESPModule() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      Q = this;
      this.g.w(var1 -> this.n());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.j());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.o == var1) {
            this.B(var2.getPos());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.o == var1) {
            if (!this.isInGame()) {
               this.f();
               this.o = null;
            } else {
               long var3 = var2.getPos().pack();
               this.d(var3);
               this.V.remove(var3);
               this.p.remove(var3);
               if (this.E != null && this.c == var3) {
                  this.k();
               }

               this.B(var2.getPos());
               this.n();
            }
         }
      });
   }

   public List<r<?>> F() {
      return List.of(this.l, this.d, this.A);
   }

   public List<r<?>> p() {
      return List.of(this.g);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.E);
   }

   @Override
   public void onEnable() {
      this.f();
      this.o = getMc().level;
      _q.z("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.f();
      this.o = null;
      _q.c("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void f() {
      this.q.clear();
      this.V.clear();
      this.p.clear();
      this.T.clear();
      this.b.clear();
      this.k = List.of();
      this.m = this.O = 0;
      this.K = (boolean)0;
      this.k();
   }

   private void k() {
      this.E = null;
      this.Y = null;
      this.S = 0;
   }

   private void d(long var1) {
      Map var3 = this.q.remove(var1);
      if (var3 != null) {
         this.m = this.m - var3.size();
      }
   }

   private int n() {
      return Math.clamp(((Integer)getMc().options.renderDistance().get()).intValue(), 1, 32);
   }

   private boolean E(ChunkPos var1) {
      ChunkPos var2 = getMc().player.chunkPosition();
      int var3 = this.n();
      return Math.abs((long)var1.x() - var2.x()) <= var3 && Math.abs((long)var1.z() - var2.z()) <= var3;
   }

   private void X(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      if (this.E(var3) && this.o.getChunkSource().getChunkNow(var3.x(), var3.z()) != null) {
         if (this.V.add(var1)) {
            this.p.addLast(var1);
         }
      }
   }

   private void B(ChunkPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = ChunkPos.pack(var1.x() + var2, var1.z() + var3);
            if (this.E != null && this.c == var4) {
               this.k();
            }

            this.d(var4);
            this.X(var4);
         }
      }
   }

   public static void m(ClientLevel var0, BlockPos var1) {
      if (Q != null && Q.isEnabled()) {
         HolesESPModule var2 = Q;
         if (var2 != null && var2.isRuntimeActive() && var2.o == var0 && getMc().isSameThread()) {
            if (var2.b.size() < 4096) {
               var2.b.add(ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4));
            } else {
               var2.q.clear();
               var2.m = 0;
               var2.O = 0;
            }
         }
      }
   }

   private os.b y(BlockPos var1) {
      if (this.o.isOutsideBuildHeight(var1)) {
         return os.b.T;
      }

      os.b var2 = this.T.get(var1.asLong());
      if (var2 != null) {
         return var2;
      }

      LevelChunk var3 = this.o.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      if (var3 == null) {
         return os.b.T;
      }

      this.y++;
      VoxelShape var4 = var3.getBlockState(var1).getCollisionShape(this.o, var1);
      os.b var5 = var4.equals(Shapes.block()) ? os.b.c : os.b.N;
      this.T.put(var1.asLong(), var5);
      return var5;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.o != mc.level) {
            this.f();
            this.o = mc.level;
         }

         int var10000 = this.O;
         byte var15 = 1;
         int var14 = this.O;
         this.O = var14 + var15;
         var10000 %= 20;
         boolean var17 = false;
         int var16 = var10000;
         var10000 = Integer.signum(var16);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.s();
         }

         if (!this.b.isEmpty()) {
            for (long var2 : this.b) {
               this.B(ChunkPos.unpack(var2));
            }

            this.b.clear();
            this.n();
         }

         this.T.clear();
         this.y = 0;
         byte var18 = 0;

         for (int var19 = 0; var19 < 2048 && this.y < 8064; var19++) {
            if (this.E == null) {
               Long var3 = this.p.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.V.remove(var3);
               ChunkPos var4 = ChunkPos.unpack(var3);
               this.E = this.o.getChunkSource().getChunkNow(var4.x(), var4.z());
               if (this.E == null || !this.E(var4)) {
                  this.E = null;
                  continue;
               }

               this.c = var3;
               this.Y = new HashMap<>();
               this.S = 0;
            }

            int var20 = this.o.getHeight();
            if (this.S >= 256 * var20) {
               this.d(this.c);
               this.q.put(this.c, this.Y);
               this.m = this.m + this.Y.size();
               this.k();
               var18 = 1;
            } else {
               int var21 = this.S;
               int var5 = this.o.getMinY() + var21 % var20;
               int var6 = this.E.getSectionIndex(var5);
               if (this.E.getSections()[var6].hasOnlyAir()) {
                  this.S = this.S + Math.min(16 - (var5 & 15), var20 - var21 % var20);
               } else {
                  this.S++;
                  int var7 = var21 / var20;
                  BlockPos var8 = new BlockPos(this.E.getPos().getMinBlockX() + var7 / 16, var5, this.E.getPos().getMinBlockZ() + var7 % 16);
                  int var9 = os.d(var8, this.o.getMaxY(), this::y);
                  if (var9 >= 0) {
                     if (this.Y.size() < 8192 && this.m + this.Y.size() < 65536) {
                        this.Y.put(var8, var9);
                     } else if (!this.K) {
                        this.K = (boolean)1;
                        _q.z("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.T.clear();
         boolean var11 = false;
         byte var10 = var18;
         var10000 = Integer.signum(var10);
         if ((var10000 | -var10000) >>> 31 == 0) {
            var10000 = this.O % 20;
            boolean var13 = false;
            int var12 = var10000;
            var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 != 0) {
               return;
            }
         }

         this.n();
      }
   }

   private void s() {
      for (long var2 : new ArrayList<>(this.q.keySet())) {
         ChunkPos var4 = ChunkPos.unpack(var2);
         if (!this.E(var4) || this.o.getChunkSource().getChunkNow(var4.x(), var4.z()) == null) {
            this.d(var2);
         }
      }

      if (this.E != null && (!this.E(this.E.getPos()) || this.o.getChunkSource().getChunkNow(this.E.getPos().x(), this.E.getPos().z()) != this.E)) {
         this.k();
      }

      this.p.removeIf(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         int var3x = this.E(var2x) && this.o.getChunkSource().getChunkNow(var2x.x(), var2x.z()) != null ? 0 : 1;
         if (var3x != 0) {
            this.V.remove(var1);
         }

         return (boolean)var3x;
      });
      ChunkPos var8 = mc.player.chunkPosition();
      int var9 = this.n();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.x() - var9; var10 <= var8.x() + var9; var10++) {
         for (int var5 = var8.z() - var9; var5 <= var8.z() + var9; var5++) {
            long var6 = ChunkPos.pack(var10, var5);
            if (!this.q.containsKey(var6)
               && (this.E == null || this.c != var6)
               && this.o.getChunkSource().getChunkNow(var10, var5) != null
               && !this.V.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         return Math.pow(var2x.x() - var8.x(), 2.0) + Math.pow(var2x.z() - var8.z(), 2.0);
      }));

      for (long var12 : var3) {
         this.X(var12);
      }
   }

   private void n() {
      if (this.o != null && getMc().player != null) {
         Vec3 var1 = mc.player.position();
         this.k = this.q
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESPModule.k(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1x -> var1x.pos().distToCenterSqr(var1)))
            .limit(this.g.getValue().longValue())
            .toList();
      } else {
         this.k = List.of();
      }
   }

   private void j() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.o == mc.level) {
            int var1 = Math.clamp(this.l.getValue().intValue(), 0, 255);
            if (var1 != 0) {
               ChunkPos var2 = mc.player.chunkPosition();
               int var3 = this.n();

               for (HolesESPModule.k var5 : this.k) {
                  if (Math.abs((long)(var5.pos().getX() >> 4) - var2.x()) <= var3 && Math.abs((long)(var5.pos().getZ() >> 4) - var2.z()) <= var3) {
                     int var6 = var5.kind() == 0 ? this.d.getValue() : this.A.getValue();
                     Gizmos.cuboid(os.M(var5.pos(), var5.kind()), GizmoStyle.fill(var1 << 24 | var6 & 16777215)).setAlwaysOnTop();
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.m + " passages";
   }

   private record k(BlockPos pos, int kind) {
   }
}

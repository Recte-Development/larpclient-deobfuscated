package com.larpclient.module;

import com.larpclient.e7;
import com.larpclient.r4;
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
   public static final int s = 8192;
   public static final int i = 2048;
   public static final int L = 65536;
   private static final int K = 8192;
   private final lc Z = this.addSetting(new lc("Opacity", "Fill alpha of passage boxes, from 0 to 255", 60.0, 0.0, 255.0, 1.0));
   private final lv h = this.addSetting(new lv("1x1 Color", "One-block-wide enclosed passages", -6283024));
   private final lv E = this.addSetting(new lv("3x1 Color", "Three-block-wide enclosed passages", -12058494));
   private final lc H = this.addSetting(new lc("Marker Limit", "Nearest boxes submitted in dense terrain", 4096.0, 256.0, 16384.0, 256.0));
   private final Map<Long, Map<BlockPos, Integer>> y = new HashMap<>();
   private final Set<Long> g = new HashSet<>();
   private final ArrayDeque<Long> P = new ArrayDeque<>();
   private final Map<Long, e7.o> u = new HashMap<>();
   private final Set<Long> M = new HashSet<>();
   private Map<BlockPos, Integer> Q;
   private List<HolesESPModule.q> C = List.of();
   private ClientLevel r;
   private LevelChunk c;
   private long w;
   private int j;
   private int Y;
   private int b;
   private int a;
   private boolean F;
   private static HolesESPModule l;

   public HolesESPModule() {
      super("Holes ESP", "Find enclosed vertical passages in received terrain", Category.RENDER, -1);
      l = this;
      this.H.W(var1 -> this.y());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.Z());
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2) -> {
         if (this.isRuntimeActive() && this.r == var1) {
            this.B(var2.getPos());
         }
      });
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (this.r == var1) {
            if (!this.isInGame()) {
               this.H();
               this.r = null;
            } else {
               long var3 = var2.getPos().pack();
               this.U(var3);
               this.g.remove(var3);
               this.P.remove(var3);
               if (this.c != null && this.w == var3) {
                  this.g();
               }

               this.B(var2.getPos());
               this.y();
            }
         }
      });
   }

   public List<l6<?>> Q() {
      return List.of(this.Z, this.h, this.E);
   }

   public List<l6<?>> p() {
      return List.of(this.H);
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.k);
   }

   @Override
   public void onEnable() {
      this.H();
      this.r = getMc().level;
      r4.V("BaseFinding", "Holes ESP active: four-level collision passages, {} reads/tick", 8192);
   }

   @Override
   public void onDisable() {
      this.H();
      this.r = null;
      r4.g("BaseFinding", "Holes ESP released scan and marker state");
   }

   private void H() {
      this.y.clear();
      this.g.clear();
      this.P.clear();
      this.u.clear();
      this.M.clear();
      this.C = List.of();
      this.a = this.b = 0;
      this.F = (boolean)0;
      this.g();
   }

   private void g() {
      this.c = null;
      this.Q = null;
      this.j = 0;
   }

   private void U(long var1) {
      Map var3 = this.y.remove(var1);
      if (var3 != null) {
         this.a = this.a - var3.size();
      }
   }

   private int c() {
      return Math.clamp(((Integer)getMc().options.renderDistance().get()).intValue(), 1, 32);
   }

   private boolean D(ChunkPos var1) {
      ChunkPos var2 = getMc().player.chunkPosition();
      int var3 = this.c();
      return Math.abs((long)var1.x() - var2.x()) <= var3 && Math.abs((long)var1.z() - var2.z()) <= var3;
   }

   private void H(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      if (this.D(var3) && this.r.getChunkSource().getChunkNow(var3.x(), var3.z()) != null) {
         if (this.g.add(var1)) {
            this.P.addLast(var1);
         }
      }
   }

   private void B(ChunkPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            long var4 = ChunkPos.pack(var1.x() + var2, var1.z() + var3);
            if (this.c != null && this.w == var4) {
               this.g();
            }

            this.U(var4);
            this.H(var4);
         }
      }
   }

   public static void e(ClientLevel var0, BlockPos var1) {
      if (l != null && l.isEnabled()) {
         HolesESPModule var2 = l;
         if (var2 != null && var2.isRuntimeActive() && var2.r == var0 && getMc().isSameThread()) {
            if (var2.M.size() < 4096) {
               var2.M.add(ChunkPos.pack(var1.getX() >> 4, var1.getZ() >> 4));
            } else {
               var2.y.clear();
               var2.a = 0;
               var2.b = 0;
            }
         }
      }
   }

   private e7.o Y(BlockPos var1) {
      if (this.r.isOutsideBuildHeight(var1)) {
         return e7.o.K;
      }

      e7.o var2 = this.u.get(var1.asLong());
      if (var2 != null) {
         return var2;
      }

      LevelChunk var3 = this.r.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      if (var3 == null) {
         return e7.o.K;
      }

      this.Y++;
      VoxelShape var4 = var3.getBlockState(var1).getCollisionShape(this.r, var1);
      e7.o var5 = var4.equals(Shapes.block()) ? e7.o.i : e7.o.f;
      this.u.put(var1.asLong(), var5);
      return var5;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.r != mc.level) {
            this.H();
            this.r = mc.level;
         }

         int var10000 = this.b;
         byte var15 = 1;
         int var14 = this.b;
         this.b = var14 + var15;
         if (var10000 % 20 == 0) {
            this.O();
         }

         int var22 = this.M.isEmpty();
         boolean var17 = false;
         byte var16 = (byte)var22;
         var22 = Integer.signum(var16);
         if ((var22 | -var22) >>> 31 == 0) {
            for (long var2 : this.M) {
               this.B(ChunkPos.unpack(var2));
            }

            this.M.clear();
            this.y();
         }

         this.u.clear();
         this.Y = 0;
         byte var18 = 0;

         for (int var19 = 0; var19 < 2048 && this.Y < 8064; var19++) {
            if (this.c == null) {
               Long var3 = this.P.pollFirst();
               if (var3 == null) {
                  break;
               }

               this.g.remove(var3);
               ChunkPos var4 = ChunkPos.unpack(var3);
               this.c = this.r.getChunkSource().getChunkNow(var4.x(), var4.z());
               if (this.c == null || !this.D(var4)) {
                  this.c = null;
                  continue;
               }

               this.w = var3;
               this.Q = new HashMap<>();
               this.j = 0;
            }

            int var20 = this.r.getHeight();
            if (this.j >= 256 * var20) {
               this.U(this.w);
               this.y.put(this.w, this.Q);
               this.a = this.a + this.Q.size();
               this.g();
               var18 = 1;
            } else {
               int var21 = this.j;
               int var5 = this.r.getMinY() + var21 % var20;
               int var6 = this.c.getSectionIndex(var5);
               if (this.c.getSections()[var6].hasOnlyAir()) {
                  this.j = this.j + Math.min(16 - (var5 & 15), var20 - var21 % var20);
               } else {
                  this.j++;
                  int var7 = var21 / var20;
                  BlockPos var8 = new BlockPos(this.c.getPos().getMinBlockX() + var7 / 16, var5, this.c.getPos().getMinBlockZ() + var7 % 16);
                  int var9 = e7.j(var8, this.r.getMaxY(), this::Y);
                  if (var9 >= 0) {
                     if (this.Q.size() < 8192 && this.a + this.Q.size() < 65536) {
                        this.Q.put(var8, var9);
                     } else if (!this.F) {
                        this.F = (boolean)1;
                        r4.V("BaseFinding", "Holes ESP dense-result ceiling reached; nearest retained markers remain visible");
                     }
                  }
               }
            }
         }

         this.u.clear();
         boolean var11 = false;
         byte var10 = var18;
         var22 = Integer.signum(var10);
         if ((var22 | -var22) >>> 31 == 0) {
            var22 = this.b % 20;
            boolean var13 = false;
            int var12 = var22;
            var22 = Integer.signum(var12);
            if ((var22 | -var22) >>> 31 != 0) {
               return;
            }
         }

         this.y();
      }
   }

   private void O() {
      for (long var2 : new ArrayList<>(this.y.keySet())) {
         ChunkPos var4 = ChunkPos.unpack(var2);
         if (!this.D(var4) || this.r.getChunkSource().getChunkNow(var4.x(), var4.z()) == null) {
            this.U(var2);
         }
      }

      if (this.c != null && (!this.D(this.c.getPos()) || this.r.getChunkSource().getChunkNow(this.c.getPos().x(), this.c.getPos().z()) != this.c)) {
         this.g();
      }

      this.P.removeIf(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         int var3x = this.D(var2x) && this.r.getChunkSource().getChunkNow(var2x.x(), var2x.z()) != null ? 0 : 1;
         if (var3x != 0) {
            this.g.remove(var1);
         }

         return (boolean)var3x;
      });
      ChunkPos var8 = mc.player.chunkPosition();
      int var9 = this.c();
      ArrayList var3 = new ArrayList();

      for (int var10 = var8.x() - var9; var10 <= var8.x() + var9; var10++) {
         for (int var5 = var8.z() - var9; var5 <= var8.z() + var9; var5++) {
            long var6 = ChunkPos.pack(var10, var5);
            if (!this.y.containsKey(var6)
               && (this.c == null || this.w != var6)
               && this.r.getChunkSource().getChunkNow(var10, var5) != null
               && !this.g.contains(var6)) {
               var3.add(var6);
            }
         }
      }

      var3.sort(Comparator.comparingDouble(var1 -> {
         ChunkPos var2x = ChunkPos.unpack(var1);
         return Math.pow(var2x.x() - var8.x(), 2.0) + Math.pow(var2x.z() - var8.z(), 2.0);
      }));

      for (long var12 : var3) {
         this.H(var12);
      }
   }

   private void y() {
      if (this.r != null && getMc().player != null) {
         Vec3 var1 = mc.player.position();
         this.C = this.y
            .values()
            .stream()
            .flatMap(var0 -> var0.entrySet().stream())
            .map(var0 -> new HolesESPModule.q(var0.getKey(), var0.getValue()))
            .sorted(Comparator.comparingDouble(var1x -> var1x.pos().distToCenterSqr(var1)))
            .limit(this.H.getValue().longValue())
            .toList();
      } else {
         this.C = List.of();
      }
   }

   private void Z() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.r == mc.level) {
            int var1 = Math.clamp(this.Z.getValue().intValue(), 0, 255);
            if (var1 != 0) {
               ChunkPos var2 = mc.player.chunkPosition();
               int var3 = this.c();

               for (HolesESPModule.q var5 : this.C) {
                  if (Math.abs((long)(var5.pos().getX() >> 4) - var2.x()) <= var3 && Math.abs((long)(var5.pos().getZ() >> 4) - var2.z()) <= var3) {
                     int var6 = var5.kind() == 0 ? this.h.getValue() : this.E.getValue();
                     Gizmos.cuboid(e7.X(var5.pos(), var5.kind()), GizmoStyle.fill(var1 << 24 | var6 & 16777215)).setAlwaysOnTop();
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.a + " passages";
   }

   private record q(BlockPos pos, int kind) {
   }
}

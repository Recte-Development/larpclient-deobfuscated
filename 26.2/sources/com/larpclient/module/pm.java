package com.larpclient.module;

import com.larpclient.g0;
import com.larpclient.md;
import com.larpclient.s0;
import com.larpclient.tu;
import com.larpclient.w_;
import com.larpclient.z4;
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

public abstract class pm extends Module {
   private final e o = this.addSetting(new e("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final e m = this.addSetting(new e("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0));
   private final e D = this.addSetting(new e("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0));
   private final e H = this.addSetting(new e("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final e r = this.addSetting(new e("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final l U = this.addSetting(new l("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled"));
   private final s L = this.addSetting(new s("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final s K = this.addSetting(new s("Tracers", "Lines from the camera to chunk markers", false));
   private final s i = this.addSetting(new s("Notifications", "Deduplicated discoveries, at most one alert per second", true));
   private final s N = this.addSetting(new s("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final s w = this.addSetting(new s("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final z4 V = new z4(var1x -> this.addSetting(var1x));
   private final tu<LevelChunk> b = new tu<>();
   private final g0 f = new g0();
   private final ArrayDeque<pm.f> v = new ArrayDeque<>();
   private List<pm.r> y = List.of();
   private final Map<Long, Long> M = new HashMap<>();
   private ClientLevel X;
   private long T;
   private int a;
   private int F;
   private int G;
   private int t;
   private int s;
   private final tu.o<LevelChunk> J = new tu.o<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return pm.this.X.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return pm.this.f.V(ChunkPos.pack(var1, var2), pm.this.T, pm.this.D.getValue().longValue() * 20L);
      }

      public int E(LevelChunk var1) {
         return var1.getSections().length;
      }

      @Override
      public void begin() {
         pm.this.a = pm.this.F = pm.this.G = 0;
         pm.this.t = Integer.MIN_VALUE;
         pm.this.beginScan();
      }

      public void X(LevelChunk var1) {
         this.begin();
         pm.this.beginChunk(var1);
      }

      public boolean m(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         return var3 + 15L >= pm.this.minimumY() && var3 <= pm.this.maximumY() && pm.this.sectionCandidate(var1, var2);
      }

      public void i(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         LevelChunkSection var4 = var1.getSections()[var2];
         int var5 = (int)Math.min(15L, (long)pm.this.maximumY() - var3);
         int var6 = (int)Math.max(0L, (long)pm.this.minimumY() - var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  pm.this.scanBlock(var1, var4.getBlockState(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         g0.t var3 = pm.this.evidence();
         long var4 = ChunkPos.pack(var1, var2);
         if (pm.this.f.G(var4, var3, pm.this.qualifies(var3), pm.this.T)) {
            pm.this.C(var4, var3);
         }
      }
   };

   protected pm(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.V.s(() -> true, () -> true, () -> true, () -> this.U.O("Filled"));
      this.o.o(var1x -> this.n("radius changed"));
      this.D.o(var1x -> this.n("rescan interval changed"));
      this.i.o(var1x -> {
         this.v.clear();
         this.s = 0;
      });
      this.N.P(this.i::getValue);
      this.w.P(this.i::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.e());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.X) {
            ChunkPos var3 = var2x.getPos();
            this.f.u(var3.pack());
            this.b.M(var3.x(), var3.z());
            this.v.removeIf(var1xx -> var1xx.key() == var3.pack());
         }
      });
   }

   protected abstract boolean candidate(BlockState var1);

   protected abstract boolean qualifies(g0.t var1);

   protected abstract String describe(g0.t var1);

   protected abstract md alert(ChunkPos var1, g0.t var2);

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

   protected final g0 O() {
      return this.f;
   }

   protected final long K() {
      return this.T;
   }

   protected final void c(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      this.b.M(var3.x(), var3.z());
      this.f.q(var1);
   }

   protected final void E(long var1, g0.t var3, boolean var4) {
      if (this.f.d(var1, var3, var4, this.T)) {
         this.C(var1, var3);
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

   protected g0.t evidence() {
      return new g0.t(this.a, this.F, this.G, this.t);
   }

   public List<q<?>> detectionSettings() {
      return List.of();
   }

   protected final int Y() {
      return this.o.getValue().intValue();
   }

   public List<q<?>> h() {
      return List.of(this.o, this.m, this.D);
   }

   public List<q<?>> markerSettings() {
      return List.of(this.H, this.r, this.U, this.L, this.K);
   }

   public List<q<?>> M() {
      return List.of(this.i, this.N, this.w);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(BlockState var1, int var2);

   protected void count(BlockState var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(ChunkPos var1, g0.t var2, z4 var3, int var4) {
   }

   protected final void a(int var1) {
      this.a++;
      if (this.t == Integer.MIN_VALUE) {
         this.t = var1;
      }
   }

   protected final void F() {
      this.F++;
   }

   protected final void R(int var1) {
      this.G++;
      if (this.t == Integer.MIN_VALUE) {
         this.t = var1;
      }
   }

   protected final void n(String var1) {
      if (this.f.P() > 0 || !this.v.isEmpty()) {
         com.larpclient.h.U("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.f.t());
      }

      this.b.y();
      this.clearEvidence();
      this.f.q();
      this.v.clear();
      this.M.clear();
      s0.s().G(this.getName());
      this.y = List.of();
      this.s = 0;
      this.T = 0L;
   }

   @Override
   public void onEnable() {
      this.n("activation");
      this.X = getMc().level;
      com.larpclient.h.A("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.o.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.n("disable or world suspension");
      this.X = null;
      com.larpclient.h.U("BaseFinding", "{} released world scan state", this.getName());
   }

   private void C(long var1, g0.t var3) {
      ChunkPos var4 = ChunkPos.unpack(var1);
      com.larpclient.h.U("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.x(), var4.z(), this.describe(var3));
      if (this.i.getValue()) {
         if (this.v.size() < 64) {
            this.v.addLast(new pm.f(var1, var3));
         } else {
            this.s++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.X != mc.level) {
            this.n("world changed");
            this.X = mc.level;
         }

         this.T++;
         this.M.values().removeIf(var1x -> this.T - var1x >= 1200L);
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.o.getValue().intValue();
         int var3 = var1.x();
         int var4 = var1.z();
         this.f.N(var4x -> {
            int var6x = ChunkPos.getX(var4x);
            int var7 = ChunkPos.getZ(var4x);
            return Math.abs((long)var6x - var3) <= var2 && Math.abs((long)var7 - var4) <= var2 && this.X.getChunkSource().getChunkNow(var6x, var7) != null;
         }, this.T);
         this.beforeScan();
         this.b.n(this.J, var3, var4, var2, this.m.getValue().intValue());
         this.afterScan();
         Map var5 = this.f.K();
         ArrayList var6 = new ArrayList(var5.size());

         for (Entry var8 : var5.entrySet()) {
            var6.add(new pm.r((Long)var8.getKey(), (g0.t)var8.getValue(), this.S((Long)var8.getKey())));
         }

         var6.sort(Comparator.comparingDouble(var0 -> var0.Z));
         int var13 = Math.min(var6.size(), this.r.getValue().intValue());
         int var12 = var6.size();
         int var11 = var13;
         int var10001 = Integer.compare(var11, var12);
         this.y = (var10001 | -var10001) >>> 31 == 0 ? var6 : List.copyOf(var6.subList(0, var13));
         long var15;
         int var10000 = (var15 = this.T % 20L - 0L) == 0L ? 0 : (var15 < 0L ? -1 : 1);
         boolean var10 = false;
         int var9 = var10000;
         var10000 = Integer.signum(var9);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.n();
         }
      }
   }

   private double S(long var1) {
      double var3 = ChunkPos.getX(var1) * 16 + 8.0 - mc.player.getX();
      double var5 = ChunkPos.getZ(var1) * 16 + 8.0 - mc.player.getZ();
      return var3 * var3 + var5 * var5;
   }

   private void n() {
      if (this.i.getValue()) {
         Map var1 = this.f.N();

         while (!this.v.isEmpty()) {
            pm.f var2 = this.v.removeFirst();
            if (var1.containsKey(var2.key())) {
               ChunkPos var3 = ChunkPos.unpack(var2.key());
               w_.w var4 = w_.m(var1.keySet(), this.zoneRadius()).stream().filter(var1x -> var1x.members().contains(var2.key())).findFirst().orElseThrow();
               this.v.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.M::containsKey);
               if (this.M.size() + var4.members().size() > 1024) {
                  this.M.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.M.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.M.size() < 1024) {
                     this.M.put(var7, this.T);
                  }
               }

               if (!var5) {
                  md var9 = this.alert(var3, var2.evidence());
                  if (var4.members().size() > 1) {
                     var9 = var9.zone((int)Math.round(var4.x()), (int)Math.round(var4.z()), var4.members().size());
                  }

                  double var10 = Math.hypot(var9.x() + 0.5 - mc.player.getX(), var9.z() + 0.5 - mc.player.getZ());
                  s0.s().V(this.getName(), var9.summary(var10), s0.u.B);
                  if (this.N.getValue()) {
                     mc.gui
                        .chatListener()
                        .handleSystemMessage(var9.chatLine(this.getName(), ChunkPos.getX(var2.key()), ChunkPos.getZ(var2.key()), this.tint(), var10), false);
                  }

                  if (this.w.getValue()) {
                     mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.s > 0) {
            s0.s().V(this.getName(), this.s + " additional discoveries; see chunk markers", s0.u.B);
            this.s = 0;
         }
      }
   }

   private void e() {
      if (this.isRuntimeActive() && this.isInGame() && this.X == mc.level) {
         try {
            Map var1 = this.f.K();
            double var2 = Math.clamp(this.H.getValue(), this.X.getMinY(), this.X.getMaxY() - 1);
            double var4 = Math.sqrt(2.0) * (this.o.getValue() + 1.0) * 16.0;
            Vec3 var6 = mc.gameRenderer.mainCamera().position();
            int var7 = 0;
            this.beginDraw();

            for (pm.r var9 : this.y) {
               long var10 = var9.P;
               g0.t var12 = (g0.t)var1.get(var10);
               ChunkPos var13 = ChunkPos.unpack(var10);
               if (var12 != null && this.X.getChunkSource().getChunkNow(var13.x(), var13.z()) != null) {
                  double var14 = Math.sqrt(this.S(var10));
                  int var16 = this.V.c(this.tint(), var14, var4, 1.0, var13.hashCode());
                  AABB var17 = new AABB(var13.getMinBlockX(), var2, var13.getMinBlockZ(), var13.getMinBlockX() + 16, var2 + 0.35, var13.getMinBlockZ() + 16);
                  this.drawEvidence(var13, var12, this.V, var16);
                  this.V.R(var17, this.U.getValue(), var16);
                  Vec3 var18 = new Vec3(var13.getMinBlockX() + 8, var2 + 0.4, var13.getMinBlockZ() + 8);
                  if (this.K.getValue()) {
                     this.V.S(var6, var18, var16);
                  }

                  if (this.L.getValue() && var7++ < 24) {
                     this.V.d(var18.add(0.0, 0.8, 0.0), this.f(var9, var12), var16);
                  }
               }
            }
         } catch (RuntimeException var19) {
            com.larpclient.h.w("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var19);
         }
      }
   }

   private String f(pm.r var1, g0.t var2) {
      if (var1.E != var2) {
         return this.describe(var2);
      }

      if (var1.N == null) {
         var1.N = this.describe(var2);
      }

      return var1.N;
   }

   @Override
   public String getStatus() {
      return this.f.t() + " chunks";
   }

   private record f(long key, g0.t evidence) {
   }

   private static final class r {
      final long P;
      final g0.t E;
      final double Z;
      String N;

      r(long var1, g0.t var3, double var4) {
         this.P = var1;
         this.E = var3;
         this.Z = var4;
      }
   }
}

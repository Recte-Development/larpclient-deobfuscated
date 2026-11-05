package com.larpclient.module;

import com.larpclient.dq;
import com.larpclient.mt;
import com.larpclient.oj;
import com.larpclient.pt;
import com.larpclient.tc;
import java.util.ArrayDeque;
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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class az extends Module {
   private final d9 N = this.addSetting(new d9("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final d9 l = this.addSetting(new d9("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0));
   private final d9 k = this.addSetting(new d9("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0));
   private final d9 U = this.addSetting(new d9("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final d9 z = this.addSetting(new d9("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final d6 X = this.addSetting(new d6("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled"));
   private final dh Y = this.addSetting(new dh("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final dh x = this.addSetting(new dh("Tracers", "Lines from the camera to chunk markers", false));
   private final dh G = this.addSetting(new dh("Notifications", "Deduplicated discoveries, at most one alert per second", true));
   private final dh e = this.addSetting(new dh("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final dh y = this.addSetting(new dh("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final dq v = new dq(var1x -> this.addSetting(var1x));
   private final pt<LevelChunk> g = new pt<>();
   private final com.larpclient.d6 n = new com.larpclient.d6();
   private final ArrayDeque<az.v> F = new ArrayDeque<>();
   private List<Long> S = List.of();
   private final Map<Long, Long> B = new HashMap<>();
   private ClientLevel V;
   private long f;
   private int A;
   private int m;
   private int R;
   private int P;
   private int r;
   private final pt.d<LevelChunk> i = new pt.d<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return az.this.V.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return az.this.n.N(ChunkPos.pack(var1, var2), az.this.f, az.this.k.P().longValue() * 20L);
      }

      public int d(LevelChunk var1) {
         return var1.getSections().length;
      }

      @Override
      public void begin() {
         az.this.A = az.this.m = az.this.R = 0;
         az.this.P = Integer.MIN_VALUE;
         az.this.beginScan();
      }

      public void P(LevelChunk var1) {
         this.begin();
         az.this.beginChunk(var1);
      }

      public boolean y(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         return var3 + 15L >= az.this.minimumY() && var3 <= az.this.maximumY() && az.this.sectionCandidate(var1, var2);
      }

      public void z(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         LevelChunkSection var4 = var1.getSections()[var2];
         int var5 = (int)Math.min(15L, (long)az.this.maximumY() - var3);
         int var6 = (int)Math.max(0L, (long)az.this.minimumY() - var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  az.this.scanBlock(var1, var4.getBlockState(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         com.larpclient.d6.f var3 = az.this.evidence();
         long var4 = ChunkPos.pack(var1, var2);
         if (az.this.n.V(var4, var3, az.this.qualifies(var3), az.this.f)) {
            az.this.z(var4, var3);
         }
      }
   };

   protected az(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.v.o(() -> true, () -> true, () -> true, () -> this.X.S("Filled"));
      this.N.h(var1x -> this.s("radius changed"));
      this.k.h(var1x -> this.s("rescan interval changed"));
      this.G.h(var1x -> {
         this.F.clear();
         this.r = 0;
      });
      this.e.H(this.G::P);
      this.y.H(this.G::P);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.o());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.V) {
            ChunkPos var3 = var2x.getPos();
            this.n.h(var3.pack());
            this.g.i(var3.x(), var3.z());
            this.F.removeIf(var1xx -> var1xx.key() == var3.pack());
         }
      });
   }

   protected abstract boolean candidate(BlockState var1);

   protected abstract boolean qualifies(com.larpclient.d6.f var1);

   protected abstract String describe(com.larpclient.d6.f var1);

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

   protected final com.larpclient.d6 Z() {
      return this.n;
   }

   protected final long I() {
      return this.f;
   }

   protected final void R(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      this.g.i(var3.x(), var3.z());
      this.n.x(var1);
   }

   protected final void K(long var1, com.larpclient.d6.f var3, boolean var4) {
      if (this.n.R(var1, var3, var4, this.f)) {
         this.z(var1, var3);
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

   protected com.larpclient.d6.f evidence() {
      return new com.larpclient.d6.f(this.A, this.m, this.R, this.P);
   }

   public List<df<?>> detectionSettings() {
      return List.of();
   }

   protected final int y() {
      return this.N.P().intValue();
   }

   public List<df<?>> h() {
      return List.of(this.N, this.l, this.k);
   }

   public List<df<?>> markerSettings() {
      return List.of(this.U, this.z, this.X, this.Y, this.x);
   }

   public List<df<?>> I() {
      return List.of(this.G, this.e, this.y);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(BlockState var1, int var2);

   protected void count(BlockState var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(ChunkPos var1, com.larpclient.d6.f var2, dq var3, int var4) {
   }

   protected final void Q(int var1) {
      this.A++;
      if (this.P == Integer.MIN_VALUE) {
         this.P = var1;
      }
   }

   protected final void R() {
      this.m++;
   }

   protected final void b(int var1) {
      this.R++;
      if (this.P == Integer.MIN_VALUE) {
         this.P = var1;
      }
   }

   protected final void s(String var1) {
      if (this.n.h() > 0 || !this.F.isEmpty()) {
         mt.U("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.n.C());
      }

      this.g.r();
      this.clearEvidence();
      this.n.f();
      this.F.clear();
      this.B.clear();
      oj.K().B(this.getName());
      this.S = List.of();
      this.r = 0;
      this.f = 0L;
   }

   @Override
   public void onEnable() {
      this.s("activation");
      this.V = getMc().level;
      mt.K("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.N.P().intValue());
   }

   @Override
   public void onDisable() {
      this.s("disable or world suspension");
      this.V = null;
      mt.U("BaseFinding", "{} released world scan state", this.getName());
   }

   private void z(long var1, com.larpclient.d6.f var3) {
      ChunkPos var4 = ChunkPos.unpack(var1);
      mt.U("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.x(), var4.z(), this.describe(var3));
      if (this.G.P()) {
         if (this.F.size() < 64) {
            this.F.addLast(new az.v(var1, this.describe(var3)));
         } else {
            this.r++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.V != mc.level) {
            this.s("world changed");
            this.V = mc.level;
         }

         this.f++;
         this.B.values().removeIf(var1x -> this.f - var1x >= 1200L);
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.N.P().intValue();
         this.n
            .Y(
               var3x -> {
                  ChunkPos var5x = ChunkPos.unpack(var3x);
                  return Math.abs((long)var5x.x() - var1.x()) <= var2
                     && Math.abs((long)var5x.z() - var1.z()) <= var2
                     && this.V.getChunkSource().getChunkNow(var5x.x(), var5x.z()) != null;
               },
               this.f
            );
         this.beforeScan();
         this.g.k(this.i, var1.x(), var1.z(), var2, this.l.P().intValue());
         this.afterScan();
         this.S = this.n.z().keySet().stream().sorted(Comparator.comparingDouble(this::R)).limit(this.z.P().intValue()).toList();
         long var6;
         int var10000 = (var6 = this.f % 20L - 0L) == 0L ? 0 : (var6 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.a();
         }
      }
   }

   private double R(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      double var4 = var3.getMinBlockX() + 8.0 - mc.player.getX();
      double var6 = var3.getMinBlockZ() + 8.0 - mc.player.getZ();
      return var4 * var4 + var6 * var6;
   }

   private void a() {
      if (this.G.P()) {
         Map var1 = this.n.z();

         while (!this.F.isEmpty()) {
            az.v var2 = this.F.removeFirst();
            if (var1.containsKey(var2.key())) {
               ChunkPos var3 = ChunkPos.unpack(var2.key());
               tc.s var4 = tc.Q(var1.keySet(), this.zoneRadius()).stream().filter(var1x -> var1x.members().contains(var2.key())).findFirst().orElseThrow();
               this.F.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.B::containsKey);
               if (this.B.size() + var4.members().size() > 1024) {
                  this.B.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.B.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.B.size() < 1024) {
                     this.B.put(var7, this.f);
                  }
               }

               if (!var5) {
                  String var9 = var4.members().size() > 1
                     ? "Zone ("
                        + var4.members().size()
                        + " chunks) at X:"
                        + Math.round(var4.x())
                        + " Z:"
                        + Math.round(var4.z())
                        + " · "
                        + Math.round(Math.hypot(var4.x() - mc.player.getX(), var4.z() - mc.player.getZ()))
                        + "m"
                     : "Chunk [" + var3.x() + ", " + var3.z() + "] at X:" + var3.getMinBlockX() + " Z:" + var3.getMinBlockZ();
                  oj.K().x(this.getName(), var9, oj.u.O);
                  if (this.e.P()) {
                     mc.gui.chatListener().handleSystemMessage(Component.literal("[Larp/" + this.getName() + "] " + var9 + ": " + var2.evidence()), false);
                  }

                  if (this.y.P()) {
                     mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.r > 0) {
            oj.K().x(this.getName(), this.r + " additional discoveries; see chunk markers", oj.u.O);
            this.r = 0;
         }
      }
   }

   private void o() {
      if (this.isRuntimeActive() && this.isInGame() && this.V == mc.level) {
         try {
            Map var1 = this.n.z();
            double var2 = Math.clamp(this.U.P(), this.V.getMinY(), this.V.getMaxY() - 1);
            double var4 = Math.sqrt(2.0) * (this.N.P() + 1.0) * 16.0;
            Vec3 var6 = mc.gameRenderer.mainCamera().position();
            int var7 = 0;
            this.beginDraw();

            for (long var9 : this.S) {
               com.larpclient.d6.f var11 = (com.larpclient.d6.f)var1.get(var9);
               ChunkPos var12 = ChunkPos.unpack(var9);
               if (var11 != null && this.V.getChunkSource().getChunkNow(var12.x(), var12.z()) != null) {
                  double var13 = Math.sqrt(this.R(var9));
                  int var15 = this.v.w(this.tint(), var13, var4, 1.0, var12.hashCode());
                  AABB var16 = new AABB(var12.getMinBlockX(), var2, var12.getMinBlockZ(), var12.getMinBlockX() + 16, var2 + 0.35, var12.getMinBlockZ() + 16);
                  this.drawEvidence(var12, var11, this.v, var15);
                  this.v.O(var16, this.X.P(), var15);
                  Vec3 var17 = new Vec3(var12.getMinBlockX() + 8, var2 + 0.4, var12.getMinBlockZ() + 8);
                  if (this.x.P()) {
                     this.v.n(var6, var17, var15);
                  }

                  if (this.Y.P() && var7++ < 24) {
                     this.v.d(var17.add(0.0, 0.8, 0.0), this.describe(var11), var15);
                  }
               }
            }
         } catch (RuntimeException var18) {
            mt.B("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var18);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.n.C() + " chunks";
   }

   private record v(long key, String evidence) {
   }
}

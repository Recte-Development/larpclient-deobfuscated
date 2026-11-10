package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.dy;
import com.larpclient.fl;
import com.larpclient.ha;
import com.larpclient.ib;
import com.larpclient.j5;
import com.larpclient.s3;
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

public abstract class ke extends Module {
   private final b K = this.addSetting(new b("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final b u = this.addSetting(new b("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0));
   private final b V = this.addSetting(new b("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0));
   private final b c = this.addSetting(new b("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final b L = this.addSetting(new b("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final o A = this.addSetting(new o("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled"));
   private final m D = this.addSetting(new m("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final m F = this.addSetting(new m("Tracers", "Lines from the camera to chunk markers", false));
   private final m l = this.addSetting(new m("Notifications", "Deduplicated discoveries, at most one alert per second", true));
   private final m r = this.addSetting(new m("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final m z = this.addSetting(new m("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final j5 J = new j5(var1x -> this.addSetting(var1x));
   private final ha<LevelChunk> f = new ha<>();
   private final dy y = new dy();
   private final ArrayDeque<ke.h> W = new ArrayDeque<>();
   private List<ke.r> X = List.of();
   private final Map<Long, Long> U = new HashMap<>();
   private ClientLevel h;
   private long R;
   private int j;
   private int p;
   private int E;
   private int d;
   private int Q;
   private final ha.j<LevelChunk> Z = new ha.j<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return ke.this.h.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return ke.this.y.N(ChunkPos.pack(var1, var2), ke.this.R, ke.this.V.getValue().longValue() * 20L);
      }

      public int j(LevelChunk var1) {
         return var1.getSections().length;
      }

      @Override
      public void begin() {
         ke.this.j = ke.this.p = ke.this.E = 0;
         ke.this.d = Integer.MIN_VALUE;
         ke.this.beginScan();
      }

      public void r(LevelChunk var1) {
         this.begin();
         ke.this.beginChunk(var1);
      }

      public boolean m(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         return var3 + 15L >= ke.this.minimumY() && var3 <= ke.this.maximumY() && ke.this.sectionCandidate(var1, var2);
      }

      public void y(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         LevelChunkSection var4 = var1.getSections()[var2];
         int var5 = (int)Math.min(15L, (long)ke.this.maximumY() - var3);
         int var6 = (int)Math.max(0L, (long)ke.this.minimumY() - var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  ke.this.scanBlock(var1, var4.getBlockState(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         dy.h var3 = ke.this.evidence();
         long var4 = ChunkPos.pack(var1, var2);
         if (ke.this.y.u(var4, var3, ke.this.qualifies(var3), ke.this.R)) {
            ke.this.J(var4, var3);
         }
      }
   };

   protected ke(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.J.q(() -> true, () -> true, () -> true, () -> this.A.w("Filled"));
      this.K.w(var1x -> this.T("radius changed"));
      this.V.w(var1x -> this.T("rescan interval changed"));
      this.l.w(var1x -> {
         this.W.clear();
         this.Q = 0;
      });
      this.r.a(this.l::getValue);
      this.z.a(this.l::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.e());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.h) {
            ChunkPos var3 = var2x.getPos();
            this.y.o(var3.pack());
            this.f.Z(var3.x(), var3.z());
            this.W.removeIf(var1xx -> var1xx.key() == var3.pack());
         }
      });
   }

   protected abstract boolean candidate(BlockState var1);

   protected abstract boolean qualifies(dy.h var1);

   protected abstract String describe(dy.h var1);

   protected abstract s3 alert(ChunkPos var1, dy.h var2);

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

   protected final dy d() {
      return this.y;
   }

   protected final long t() {
      return this.R;
   }

   protected final void A(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      this.f.Z(var3.x(), var3.z());
      this.y.P(var1);
   }

   protected final void Q(long var1, dy.h var3, boolean var4) {
      if (this.y.O(var1, var3, var4, this.R)) {
         this.J(var1, var3);
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

   protected dy.h evidence() {
      return new dy.h(this.j, this.p, this.E, this.d);
   }

   public List<com.larpclient.module.r<?>> detectionSettings() {
      return List.of();
   }

   protected final int z() {
      return this.K.getValue().intValue();
   }

   public List<com.larpclient.module.r<?>> r() {
      return List.of(this.K, this.u, this.V);
   }

   public List<com.larpclient.module.r<?>> markerSettings() {
      return List.of(this.c, this.L, this.A, this.D, this.F);
   }

   public List<com.larpclient.module.r<?>> T() {
      return List.of(this.l, this.r, this.z);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(BlockState var1, int var2);

   protected void count(BlockState var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(ChunkPos var1, dy.h var2, j5 var3, int var4) {
   }

   protected final void B(int var1) {
      this.j++;
      if (this.d == Integer.MIN_VALUE) {
         this.d = var1;
      }
   }

   protected final void A() {
      this.p++;
   }

   protected final void U(int var1) {
      this.E++;
      if (this.d == Integer.MIN_VALUE) {
         this.d = var1;
      }
   }

   protected final void T(String var1) {
      if (this.y.z() > 0 || !this.W.isEmpty()) {
         _q.c("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.y.x());
      }

      this.f.X();
      this.clearEvidence();
      this.y.y();
      this.W.clear();
      this.U.clear();
      ib.L().i(this.getName());
      this.X = List.of();
      this.Q = 0;
      this.R = 0L;
   }

   @Override
   public void onEnable() {
      this.T("activation");
      this.h = getMc().level;
      _q.z("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.K.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.T("disable or world suspension");
      this.h = null;
      _q.c("BaseFinding", "{} released world scan state", this.getName());
   }

   private void J(long var1, dy.h var3) {
      ChunkPos var4 = ChunkPos.unpack(var1);
      _q.c("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.x(), var4.z(), this.describe(var3));
      if (this.l.getValue()) {
         if (this.W.size() < 64) {
            this.W.addLast(new ke.h(var1, var3));
         } else {
            this.Q++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.h != mc.level) {
            this.T("world changed");
            this.h = mc.level;
         }

         this.R++;
         this.U.values().removeIf(var1x -> this.R - var1x >= 1200L);
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.K.getValue().intValue();
         int var3 = var1.x();
         int var4 = var1.z();
         this.y.F(var4x -> {
            int var6x = ChunkPos.getX(var4x);
            int var7 = ChunkPos.getZ(var4x);
            return Math.abs((long)var6x - var3) <= var2 && Math.abs((long)var7 - var4) <= var2 && this.h.getChunkSource().getChunkNow(var6x, var7) != null;
         }, this.R);
         this.beforeScan();
         this.f.b(this.Z, var3, var4, var2, this.u.getValue().intValue());
         this.afterScan();
         Map var5 = this.y.q();
         ArrayList var6 = new ArrayList(var5.size());

         for (Entry var8 : var5.entrySet()) {
            var6.add(new ke.r((Long)var8.getKey(), (dy.h)var8.getValue(), this.f((Long)var8.getKey())));
         }

         var6.sort(Comparator.comparingDouble(var0 -> var0.S));
         int var13 = Math.min(var6.size(), this.L.getValue().intValue());
         int var10 = var6.size();
         int var9 = var13;
         int var10001 = Integer.compare(var9, var10);
         this.X = (var10001 | -var10001) >>> 31 == 0 ? var6 : List.copyOf(var6.subList(0, var13));
         long var15;
         int var10000 = (var15 = this.R % 20L - 0L) == 0L ? 0 : (var15 < 0L ? -1 : 1);
         boolean var12 = false;
         int var11 = var10000;
         var10000 = Integer.signum(var11);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.M();
         }
      }
   }

   private double f(long var1) {
      double var3 = ChunkPos.getX(var1) * 16 + 8.0 - mc.player.getX();
      double var5 = ChunkPos.getZ(var1) * 16 + 8.0 - mc.player.getZ();
      return var3 * var3 + var5 * var5;
   }

   private void M() {
      if (this.l.getValue()) {
         Map var1 = this.y.c();

         while (!this.W.isEmpty()) {
            ke.h var2 = this.W.removeFirst();
            if (var1.containsKey(var2.key())) {
               ChunkPos var3 = ChunkPos.unpack(var2.key());
               fl.g var4 = fl.f(var1.keySet(), this.zoneRadius()).stream().filter(var1x -> var1x.members().contains(var2.key())).findFirst().orElseThrow();
               this.W.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.U::containsKey);
               if (this.U.size() + var4.members().size() > 1024) {
                  this.U.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.U.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.U.size() < 1024) {
                     this.U.put(var7, this.R);
                  }
               }

               if (!var5) {
                  s3 var9 = this.alert(var3, var2.evidence());
                  if (var4.members().size() > 1) {
                     var9 = var9.zone((int)Math.round(var4.x()), (int)Math.round(var4.z()), var4.members().size());
                  }

                  double var10 = Math.hypot(var9.x() + 0.5 - mc.player.getX(), var9.z() + 0.5 - mc.player.getZ());
                  ib.L().t(this.getName(), var9.summary(var10), ib.v.f);
                  if (this.r.getValue()) {
                     mc.gui
                        .chatListener()
                        .handleSystemMessage(var9.chatLine(this.getName(), ChunkPos.getX(var2.key()), ChunkPos.getZ(var2.key()), this.tint(), var10), false);
                  }

                  if (this.z.getValue()) {
                     mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.Q > 0) {
            ib.L().t(this.getName(), this.Q + " additional discoveries; see chunk markers", ib.v.f);
            this.Q = 0;
         }
      }
   }

   private void e() {
      if (this.isRuntimeActive() && this.isInGame() && this.h == mc.level) {
         try {
            Map var1 = this.y.q();
            double var2 = Math.clamp(this.c.getValue(), this.h.getMinY(), this.h.getMaxY() - 1);
            double var4 = Math.sqrt(2.0) * (this.K.getValue() + 1.0) * 16.0;
            Vec3 var6 = mc.gameRenderer.mainCamera().position();
            int var7 = 0;
            this.beginDraw();

            for (ke.r var9 : this.X) {
               long var10 = var9.D;
               dy.h var12 = (dy.h)var1.get(var10);
               ChunkPos var13 = ChunkPos.unpack(var10);
               if (var12 != null && this.h.getChunkSource().getChunkNow(var13.x(), var13.z()) != null) {
                  double var14 = Math.sqrt(this.f(var10));
                  int var16 = this.J.p(this.tint(), var14, var4, 1.0, var13.hashCode());
                  AABB var17 = new AABB(var13.getMinBlockX(), var2, var13.getMinBlockZ(), var13.getMinBlockX() + 16, var2 + 0.35, var13.getMinBlockZ() + 16);
                  this.drawEvidence(var13, var12, this.J, var16);
                  this.J.L(var17, this.A.getValue(), var16);
                  Vec3 var18 = new Vec3(var13.getMinBlockX() + 8, var2 + 0.4, var13.getMinBlockZ() + 8);
                  if (this.F.getValue()) {
                     this.J.q(var6, var18, var16);
                  }

                  if (this.D.getValue() && var7++ < 24) {
                     this.J.P(var18.add(0.0, 0.8, 0.0), this.a(var9, var12), var16);
                  }
               }
            }
         } catch (RuntimeException var19) {
            _q.g("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var19);
         }
      }
   }

   private String a(ke.r var1, dy.h var2) {
      if (var1.v != var2) {
         return this.describe(var2);
      }

      if (var1.k == null) {
         var1.k = this.describe(var2);
      }

      return var1.k;
   }

   @Override
   public String getStatus() {
      return this.y.x() + " chunks";
   }

   private record h(long key, dy.h evidence) {
   }

   private static final class r {
      final long D;
      final dy.h v;
      final double S;
      String k;

      r(long var1, dy.h var3, double var4) {
         this.D = var1;
         this.v = var3;
         this.S = var4;
      }
   }
}

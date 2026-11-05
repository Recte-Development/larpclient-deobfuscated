package com.larpclient.module;

import com.larpclient.ek;
import com.larpclient.hg;
import com.larpclient.mi;
import com.larpclient.oa;
import com.larpclient.r4;
import com.larpclient.xz;
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

public abstract class tf extends Module {
   private final lc U = this.addSetting(new lc("Scan Radius", "Loaded chunks around the player", 4.0, 1.0, 12.0, 1.0));
   private final lc b = this.addSetting(new lc("Sections Per Tick", "At most 4096 block reads per matching section", 2.0, 1.0, 8.0, 1.0));
   private final lc k = this.addSetting(new lc("Rescan Seconds", "Minimum interval between complete scans of a chunk", 2.0, 1.0, 30.0, 1.0));
   private final lc W = this.addSetting(new lc("Marker Y", "Height of the thin chunk markers", 64.0, -64.0, 320.0, 1.0));
   private final lc a = this.addSetting(new lc("Max Markers", "Nearest qualifying chunks drawn", 64.0, 1.0, 256.0, 1.0));
   private final lp w = this.addSetting(new lp("Marker Style", "Chunk outline or translucent slab", "Filled", "Box", "Corners", "Filled"));
   private final ln u = this.addSetting(new ln("Labels", "Evidence labels on the nearest 24 chunk markers", true));
   private final ln D = this.addSetting(new ln("Tracers", "Lines from the camera to chunk markers", false));
   private final ln o = this.addSetting(new ln("Notifications", "Deduplicated discoveries, at most one alert per second", true));
   private final ln R = this.addSetting(new ln("Chat Alerts", "Include coordinates and observed evidence in local chat", true));
   private final ln F = this.addSetting(new ln("Alert Sound", "Play a local chime for each delivered discovery alert", false));
   private final mi N = new mi(var1x -> this.addSetting(var1x));
   private final xz<LevelChunk> Y = new xz<>();
   private final oa Q = new oa();
   private final ArrayDeque<tf.u> C = new ArrayDeque<>();
   private List<Long> g = List.of();
   private final Map<Long, Long> P = new HashMap<>();
   private ClientLevel H;
   private long O;
   private int G;
   private int n;
   private int z;
   private int l;
   private int x;
   private final xz.b<LevelChunk> K = new xz.b<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return tf.this.H.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         return tf.this.Q.S(ChunkPos.pack(var1, var2), tf.this.O, tf.this.k.getValue().longValue() * 20L);
      }

      public int q(LevelChunk var1) {
         return var1.getSections().length;
      }

      @Override
      public void begin() {
         tf.this.G = tf.this.n = tf.this.z = 0;
         tf.this.l = Integer.MIN_VALUE;
         tf.this.beginScan();
      }

      public void P(LevelChunk var1) {
         this.begin();
         tf.this.beginChunk(var1);
      }

      public boolean A(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         return var3 + 15L >= tf.this.minimumY() && var3 <= tf.this.maximumY() && tf.this.sectionCandidate(var1, var2);
      }

      public void s(LevelChunk var1, int var2) {
         int var3 = var1.getMinY() + var2 * 16;
         LevelChunkSection var4 = var1.getSections()[var2];
         int var5 = (int)Math.min(15L, (long)tf.this.maximumY() - var3);
         int var6 = (int)Math.max(0L, (long)tf.this.minimumY() - var3);

         for (int var7 = var6; var7 <= var5; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  tf.this.scanBlock(var1, var4.getBlockState(var8, var7, var9), var8, var3 + var7, var9);
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         oa.j var3 = tf.this.evidence();
         long var4 = ChunkPos.pack(var1, var2);
         if (tf.this.Q.Z(var4, var3, tf.this.qualifies(var3), tf.this.O)) {
            tf.this.M(var4, var3);
         }
      }
   };

   protected tf(String var1, String var2) {
      super(var1, var2, Category.RENDER, -1);
      this.N.J(() -> true, () -> true, () -> true, () -> this.w.i("Filled"));
      this.U.W(var1x -> this.C("radius changed"));
      this.k.W(var1x -> this.C("rescan interval changed"));
      this.o.W(var1x -> {
         this.C.clear();
         this.x = 0;
      });
      this.R.x(this.o::getValue);
      this.F.x(this.o::getValue);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.t());
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1x, var2x) -> {
         if (var1x == this.H) {
            ChunkPos var3 = var2x.getPos();
            this.Q.L(var3.pack());
            this.Y.z(var3.x(), var3.z());
            this.C.removeIf(var1xx -> var1xx.key() == var3.pack());
         }
      });
   }

   protected abstract boolean candidate(BlockState var1);

   protected abstract boolean qualifies(oa.j var1);

   protected abstract String describe(oa.j var1);

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

   protected final oa c() {
      return this.Q;
   }

   protected final long U() {
      return this.O;
   }

   protected final void l(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      this.Y.z(var3.x(), var3.z());
      this.Q.n(var1);
   }

   protected final void P(long var1, oa.j var3, boolean var4) {
      if (this.Q.I(var1, var3, var4, this.O)) {
         this.M(var1, var3);
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

   protected oa.j evidence() {
      return new oa.j(this.G, this.n, this.z, this.l);
   }

   public List<l6<?>> detectionSettings() {
      return List.of();
   }

   protected final int l() {
      return this.U.getValue().intValue();
   }

   public List<l6<?>> u() {
      return List.of(this.U, this.b, this.k);
   }

   public List<l6<?>> markerSettings() {
      return List.of(this.W, this.a, this.w, this.u, this.D);
   }

   public List<l6<?>> W() {
      return List.of(this.o, this.R, this.F);
   }

   protected int maximumY() {
      return Integer.MAX_VALUE;
   }

   protected abstract void count(BlockState var1, int var2);

   protected void count(BlockState var1, int var2, int var3, int var4) {
      this.count(var1, var3);
   }

   protected void drawEvidence(ChunkPos var1, oa.j var2, mi var3, int var4) {
   }

   protected final void v(int var1) {
      this.G++;
      if (this.l == Integer.MIN_VALUE) {
         this.l = var1;
      }
   }

   protected final void G() {
      this.n++;
   }

   protected final void S(int var1) {
      this.z++;
      if (this.l == Integer.MIN_VALUE) {
         this.l = var1;
      }
   }

   protected final void C(String var1) {
      if (this.Q.y() > 0 || !this.C.isEmpty()) {
         r4.g("BaseFinding", "{} cleanup: {} ({} flags)", this.getName(), var1, this.Q.x());
      }

      this.Y.V();
      this.clearEvidence();
      this.Q.U();
      this.C.clear();
      this.P.clear();
      hg.B().M(this.getName());
      this.g = List.of();
      this.x = 0;
      this.O = 0L;
   }

   @Override
   public void onEnable() {
      this.C("activation");
      this.H = getMc().level;
      r4.V("BaseFinding", "{} active: received-data scan, radius {}", this.getName(), this.U.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.C("disable or world suspension");
      this.H = null;
      r4.g("BaseFinding", "{} released world scan state", this.getName());
   }

   private void M(long var1, oa.j var3) {
      ChunkPos var4 = ChunkPos.unpack(var1);
      r4.g("BaseFinding", "{} found chunk [{}, {}]: {}", this.getName(), var4.x(), var4.z(), this.describe(var3));
      if (this.o.getValue()) {
         if (this.C.size() < 64) {
            this.C.addLast(new tf.u(var1, this.describe(var3)));
         } else {
            this.x++;
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.H != mc.level) {
            this.C("world changed");
            this.H = mc.level;
         }

         this.O++;
         this.P.values().removeIf(var1x -> this.O - var1x >= 1200L);
         ChunkPos var1 = mc.player.chunkPosition();
         int var2 = this.U.getValue().intValue();
         this.Q
            .l(
               var3x -> {
                  ChunkPos var5x = ChunkPos.unpack(var3x);
                  return Math.abs((long)var5x.x() - var1.x()) <= var2
                     && Math.abs((long)var5x.z() - var1.z()) <= var2
                     && this.H.getChunkSource().getChunkNow(var5x.x(), var5x.z()) != null;
               },
               this.O
            );
         this.beforeScan();
         this.Y.a(this.K, var1.x(), var1.z(), var2, this.b.getValue().intValue());
         this.afterScan();
         this.g = this.Q.E().keySet().stream().sorted(Comparator.comparingDouble(this::Q)).limit(this.a.getValue().intValue()).toList();
         long var6;
         int var10000 = (var6 = this.O % 20L - 0L) == 0L ? 0 : (var6 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.z();
         }
      }
   }

   private double Q(long var1) {
      ChunkPos var3 = ChunkPos.unpack(var1);
      double var4 = var3.getMinBlockX() + 8.0 - mc.player.getX();
      double var6 = var3.getMinBlockZ() + 8.0 - mc.player.getZ();
      return var4 * var4 + var6 * var6;
   }

   private void z() {
      if (this.o.getValue()) {
         Map var1 = this.Q.E();

         while (!this.C.isEmpty()) {
            tf.u var2 = this.C.removeFirst();
            if (var1.containsKey(var2.key())) {
               ChunkPos var3 = ChunkPos.unpack(var2.key());
               ek.g var4 = ek.L(var1.keySet(), this.zoneRadius()).stream().filter(var1x -> var1x.members().contains(var2.key())).findFirst().orElseThrow();
               this.C.removeIf(var1x -> var4.members().contains(var1x.key()));
               boolean var5 = var4.members().stream().anyMatch(this.P::containsKey);
               if (this.P.size() + var4.members().size() > 1024) {
                  this.P.entrySet().stream().min(Entry.comparingByValue()).ifPresent(var1x -> this.P.remove(var1x.getKey()));
               }

               for (long var7 : var4.members()) {
                  if (this.P.size() < 1024) {
                     this.P.put(var7, this.O);
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
                  hg.B().o(this.getName(), var9, hg.u.U);
                  if (this.R.getValue()) {
                     mc.gui.chatListener().handleSystemMessage(Component.literal("[Larp/" + this.getName() + "] " + var9 + ": " + var2.evidence()), false);
                  }

                  if (this.F.getValue()) {
                     mc.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                  }

                  return;
               }
            }
         }

         if (this.x > 0) {
            hg.B().o(this.getName(), this.x + " additional discoveries; see chunk markers", hg.u.U);
            this.x = 0;
         }
      }
   }

   private void t() {
      if (this.isRuntimeActive() && this.isInGame() && this.H == mc.level) {
         try {
            Map var1 = this.Q.E();
            double var2 = Math.clamp(this.W.getValue(), this.H.getMinY(), this.H.getMaxY() - 1);
            double var4 = Math.sqrt(2.0) * (this.U.getValue() + 1.0) * 16.0;
            Vec3 var6 = mc.gameRenderer.mainCamera().position();
            int var7 = 0;
            this.beginDraw();

            for (long var9 : this.g) {
               oa.j var11 = (oa.j)var1.get(var9);
               ChunkPos var12 = ChunkPos.unpack(var9);
               if (var11 != null && this.H.getChunkSource().getChunkNow(var12.x(), var12.z()) != null) {
                  double var13 = Math.sqrt(this.Q(var9));
                  int var15 = this.N.k(this.tint(), var13, var4, 1.0, var12.hashCode());
                  AABB var16 = new AABB(var12.getMinBlockX(), var2, var12.getMinBlockZ(), var12.getMinBlockX() + 16, var2 + 0.35, var12.getMinBlockZ() + 16);
                  this.drawEvidence(var12, var11, this.N, var15);
                  this.N.I(var16, this.w.getValue(), var15);
                  Vec3 var17 = new Vec3(var12.getMinBlockX() + 8, var2 + 0.4, var12.getMinBlockZ() + 8);
                  if (this.D.getValue()) {
                     this.N.D(var6, var17, var15);
                  }

                  if (this.u.getValue() && var7++ < 24) {
                     this.N.t(var17.add(0.0, 0.8, 0.0), this.describe(var11), var15);
                  }
               }
            }
         } catch (RuntimeException var18) {
            r4.T("BaseFinding", this.getName() + " marker rendering failed; scan remains active", var18);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.Q.x() + " chunks";
   }

   private record u(long key, String evidence) {
   }
}

package com.larpclient.module;

import com.larpclient.b9;
import com.larpclient.dy;
import com.larpclient.fl;
import com.larpclient.hl;
import com.larpclient.i2;
import com.larpclient.j5;
import com.larpclient.nm;
import com.larpclient.qq;
import com.larpclient.s3;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

public final class wk extends ke {
   private final b w = this.addSetting(new b("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0));
   private final b n = this.addSetting(
      new b("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final b a = this.addSetting(new b("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0));
   private final b T = this.addSetting(new b("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0));
   private final b B = this.addSetting(new b("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final m o = this.addSetting(new m("Amethyst Light", "Server light 5 beside received amethyst; possible grown clusters", true));
   private final m S = this.addSetting(new m("Kelp", "Tall or max-age kelp columns", true));
   private final m g = this.addSetting(new m("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final m N = this.addSetting(new m("Berries", "Fully mature sweet berry bushes", true));
   private final m m = this.addSetting(new m("Vines", "Hanging vines at least 7 blocks long", true));
   private final m q = this.addSetting(new m("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final m e = this.addSetting(new m("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final b b = this.addSetting(new b("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final m G = this.addSetting(new m("Centroid Marker", "Show the centre of qualifying zones", true));
   private hl M = hl.EMPTY;
   private List<fl.g> k = List.of();
   private final Set<Long> O = new HashSet<>();
   private b9 P;
   private int x;
   private LevelChunk i;

   public wk() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      nm.b();

      for (com.larpclient.module.r var2 : this.detectionSettings()) {
         var2.w(var1 -> this.T("detection changed"));
      }

      this.b.a(this.e::getValue);
      this.G.a(this.e::getValue);
   }

   @Override
   public List<com.larpclient.module.r<?>> detectionSettings() {
      return List.of(this.w, this.n, this.a, this.T, this.B, this.o, this.S, this.g, this.N, this.m, this.q, this.e, this.b);
   }

   @Override
   public List<com.larpclient.module.r<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.G);
      return List.copyOf(var1);
   }

   public static boolean I(BlockState var0) {
      return var0.is(Blocks.AMETHYST_CLUSTER)
         || var0.is(Blocks.SMALL_AMETHYST_BUD)
         || var0.is(Blocks.MEDIUM_AMETHYST_BUD)
         || var0.is(Blocks.LARGE_AMETHYST_BUD);
   }

   private boolean S(i2.o var1) {
      return switch (var1) {
         case p -> this.S.getValue();
         case O -> this.g.getValue();
         case m -> this.N.getValue();
         case b -> this.m.getValue();
         case S -> this.q.getValue();
      };
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return I(var1) || i2.o(var1);
   }

   @Override
   protected int minimumY() {
      return Math.min(this.B.getValue().intValue(), this.w.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.B.getValue().intValue(), this.w.getValue().intValue()));
   }

   @Override
   protected void beginScan() {
      this.M = hl.EMPTY;
      this.x = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.k = List.of();
      this.O.clear();
      this.P = null;
      this.i = null;
   }

   @Override
   protected void beforeScan() {
      this.P = nm.T(getMc().level);

      for (long var2 : this.P.X()) {
         this.A(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      ChunkPos var3 = var1.getPos();
      return super.sectionCandidate(var1, var2) || this.o.getValue() && this.P != null && this.P.D(var3.x(), (var1.getMinY() >> 4) + var2, var3.z());
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      this.i = var1;
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.is(Blocks.AMETHYST_CLUSTER)) {
            this.B(var2);
         } else if (I(var1)) {
            this.A();
         }
      }
   }

   private BlockState q(BlockPos var1) {
      if (var1.getY() < this.minimumY() || var1.getY() > this.maximumY()) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.i != null && var1.getX() >> 4 == this.i.getPos().x() && var1.getZ() >> 4 == this.i.getPos().z()) {
         return this.i.getBlockState(var1);
      }

      LevelChunk var2 = getMc().level.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      return var2 == null ? Blocks.AIR.defaultBlockState() : var2.getBlockState(var1);
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (i2.o(var2) || this.o.getValue() && this.M.light() < 16 && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            if (this.o.getValue()
               && this.M.light() < 16
               && this.P != null
               && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))
               && i2.A(var2, this.P.s(var6.getX(), var4, var6.getZ()), var6, this::q)) {
               this.M = this.M.plus(new hl(1, 0, 0, 0, 0, 0));
               this.x = var4;
            }

            if (i2.o(var2)) {
               i2.o var7 = i2.D(var2, var6, this::q);
               if (var7 != null && this.S(var7)) {
                  this.M = this.M.plus(switch (var7) {
                     case p -> new hl(0, 1, 0, 0, 0, 0);
                     case O -> new hl(0, 0, 1, 0, 0, 0);
                     case m -> new hl(0, 0, 0, 1, 0, 0);
                     case b -> new hl(0, 0, 0, 0, 1, 0);
                     case S -> new hl(0, 0, 0, 0, 0, 1);
                  });
                  this.x = var4;
               }
            }
         }
      }
   }

   @Override
   protected dy.h evidence() {
      dy.h var1 = super.evidence();
      return new dy.h(var1.mature(), var1.buds(), 0, this.x == Integer.MIN_VALUE ? var1.sampleY() : this.x, qq.EMPTY, this.M, List.of());
   }

   @Override
   protected boolean qualifies(dy.h var1) {
      return var1.matureAmethyst(this.n.getValue().intValue(), this.a.getValue().intValue()) || var1.sus().score() >= this.I();
   }

   private int I() {
      return this.T.getValue().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.e.getValue() ? this.b.getValue().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map var1 = this.d().C();
      HashSet var2 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2x);
         }
      });
      ArrayList var3 = new ArrayList();

      for (fl.g var5 : fl.f(var2, this.zoneRadius())) {
         hl var6 = hl.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            dy.h var11 = (dy.h)var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= this.qualifies(var11);
         }

         int var12 = var7 == 0 && var6.score() < this.I() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.Q(var10, (dy.h)var1.get(var10), (boolean)var12);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.k = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.O.clear();
   }

   @Override
   protected void drawEvidence(ChunkPos var1, dy.h var2, j5 var3, int var4) {
      if (this.e.getValue() && this.G.getValue()) {
         for (fl.g var6 : this.k) {
            if (var6.members().contains(var1.pack())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.O.size() < 24 && this.O.add(var7)) {
                  var3.P(new Vec3(var6.x(), var2.sampleY() + 2, var6.z()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(dy.h var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected s3 alert(ChunkPos var1, dy.h var2) {
      return s3.chunk("Suspicious area", this.describe(var2), var1.getMinBlockX(), var1.getMinBlockZ());
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}

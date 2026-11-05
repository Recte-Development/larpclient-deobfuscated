package com.larpclient.module;

import com.larpclient.ce;
import com.larpclient.dq;
import com.larpclient.ej;
import com.larpclient.tc;
import com.larpclient.tg;
import com.larpclient.xk;
import com.larpclient.xl;
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

public final class an extends az {
   private final d9 T = this.addSetting(new d9("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0));
   private final d9 D = this.addSetting(
      new d9("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final d9 M = this.addSetting(new d9("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0));
   private final d9 s = this.addSetting(new d9("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0));
   private final d9 h = this.addSetting(new d9("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final dh p = this.addSetting(new dh("Amethyst Light", "Server light 5 beside received amethyst; possible grown clusters", true));
   private final dh I = this.addSetting(new dh("Kelp", "Tall or max-age kelp columns", true));
   private final dh o = this.addSetting(new dh("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final dh j = this.addSetting(new dh("Berries", "Fully mature sweet berry bushes", true));
   private final dh C = this.addSetting(new dh("Vines", "Hanging vines at least 7 blocks long", true));
   private final dh K = this.addSetting(new dh("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final dh J = this.addSetting(new dh("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final d9 d = this.addSetting(new d9("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final dh O = this.addSetting(new dh("Centroid Marker", "Show the centre of qualifying zones", true));
   private xl b = xl.EMPTY;
   private List<tc.s> w = List.of();
   private final Set<Long> a = new HashSet<>();
   private tg H;
   private int u;
   private LevelChunk E;

   public an() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      ej.r();

      for (df var2 : this.detectionSettings()) {
         var2.h(var1 -> this.s("detection changed"));
      }

      this.d.H(this.J::P);
      this.O.H(this.J::P);
   }

   @Override
   public List<df<?>> detectionSettings() {
      return List.of(this.T, this.D, this.M, this.s, this.h, this.p, this.I, this.o, this.j, this.C, this.K, this.J, this.d);
   }

   @Override
   public List<df<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.O);
      return List.copyOf(var1);
   }

   public static boolean D(BlockState var0) {
      return var0.is(Blocks.AMETHYST_CLUSTER)
         || var0.is(Blocks.SMALL_AMETHYST_BUD)
         || var0.is(Blocks.MEDIUM_AMETHYST_BUD)
         || var0.is(Blocks.LARGE_AMETHYST_BUD);
   }

   private boolean y(xk.p var1) {
      return switch (var1) {
         case G -> this.I.P();
         case L -> this.o.P();
         case x -> this.j.P();
         case j -> this.C.P();
         case f -> this.K.P();
      };
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return D(var1) || xk.k(var1);
   }

   @Override
   protected int minimumY() {
      return Math.min(this.h.P().intValue(), this.T.P().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.h.P().intValue(), this.T.P().intValue()));
   }

   @Override
   protected void beginScan() {
      this.b = xl.EMPTY;
      this.u = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.w = List.of();
      this.a.clear();
      this.H = null;
      this.E = null;
   }

   @Override
   protected void beforeScan() {
      this.H = ej.B(getMc().level);

      for (long var2 : this.H.Q()) {
         this.R(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      ChunkPos var3 = var1.getPos();
      return super.sectionCandidate(var1, var2) || this.p.P() && this.H != null && this.H.i(var3.x(), (var1.getMinY() >> 4) + var2, var3.z());
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      this.E = var1;
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.is(Blocks.AMETHYST_CLUSTER)) {
            this.Q(var2);
         } else if (D(var1)) {
            this.R();
         }
      }
   }

   private BlockState b(BlockPos var1) {
      if (var1.getY() < this.minimumY() || var1.getY() > this.maximumY()) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.E != null && var1.getX() >> 4 == this.E.getPos().x() && var1.getZ() >> 4 == this.E.getPos().z()) {
         return this.E.getBlockState(var1);
      }

      LevelChunk var2 = getMc().level.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      return var2 == null ? Blocks.AIR.defaultBlockState() : var2.getBlockState(var1);
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (xk.k(var2) || this.p.P() && this.b.light() < 16 && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            if (this.p.P()
               && this.b.light() < 16
               && this.H != null
               && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))
               && xk.a(var2, this.H.s(var6.getX(), var4, var6.getZ()), var6, this::b)) {
               this.b = this.b.plus(new xl(1, 0, 0, 0, 0, 0));
               this.u = var4;
            }

            if (xk.k(var2)) {
               xk.p var7 = xk.v(var2, var6, this::b);
               if (var7 != null && this.y(var7)) {
                  this.b = this.b.plus(switch (var7) {
                     case G -> new xl(0, 1, 0, 0, 0, 0);
                     case L -> new xl(0, 0, 1, 0, 0, 0);
                     case x -> new xl(0, 0, 0, 1, 0, 0);
                     case j -> new xl(0, 0, 0, 0, 1, 0);
                     case f -> new xl(0, 0, 0, 0, 0, 1);
                  });
                  this.u = var4;
               }
            }
         }
      }
   }

   @Override
   protected com.larpclient.d6.f evidence() {
      com.larpclient.d6.f var1 = super.evidence();
      return new com.larpclient.d6.f(var1.mature(), var1.buds(), 0, this.u == Integer.MIN_VALUE ? var1.sampleY() : this.u, ce.EMPTY, this.b, List.of());
   }

   @Override
   protected boolean qualifies(com.larpclient.d6.f var1) {
      return var1.matureAmethyst(this.D.P().intValue(), this.M.P().intValue()) || var1.sus().score() >= this.K();
   }

   private int K() {
      return this.s.P().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.J.P() ? this.d.P().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map var1 = this.Z().c();
      HashSet var2 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2x);
         }
      });
      ArrayList var3 = new ArrayList();

      for (tc.s var5 : tc.Q(var2, this.zoneRadius())) {
         xl var6 = xl.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            com.larpclient.d6.f var11 = (com.larpclient.d6.f)var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= this.qualifies(var11);
         }

         int var12 = var7 == 0 && var6.score() < this.K() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.K(var10, (com.larpclient.d6.f)var1.get(var10), (boolean)var12);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.w = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.a.clear();
   }

   @Override
   protected void drawEvidence(ChunkPos var1, com.larpclient.d6.f var2, dq var3, int var4) {
      if (this.J.P() && this.O.P()) {
         for (tc.s var6 : this.w) {
            if (var6.members().contains(var1.pack())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.a.size() < 24 && this.a.add(var7)) {
                  var3.d(new Vec3(var6.x(), var2.sampleY() + 2, var6.z()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(com.larpclient.d6.f var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}

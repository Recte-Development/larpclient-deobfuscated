package com.larpclient.module;

import com.larpclient.b7;
import com.larpclient.cp;
import com.larpclient.g0;
import com.larpclient.ls;
import com.larpclient.md;
import com.larpclient.w_;
import com.larpclient.wo;
import com.larpclient.z4;
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

public final class pl extends pm {
   private final e c = this.addSetting(new e("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0));
   private final e d = this.addSetting(
      new e("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final e j = this.addSetting(new e("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0));
   private final e Q = this.addSetting(new e("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0));
   private final e l = this.addSetting(new e("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final s x = this.addSetting(new s("Amethyst Light", "Server light 5 beside received amethyst; possible grown clusters", true));
   private final s O = this.addSetting(new s("Kelp", "Tall or max-age kelp columns", true));
   private final s I = this.addSetting(new s("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final s Z = this.addSetting(new s("Berries", "Fully mature sweet berry bushes", true));
   private final s A = this.addSetting(new s("Vines", "Hanging vines at least 7 blocks long", true));
   private final s p = this.addSetting(new s("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final s h = this.addSetting(new s("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final e n = this.addSetting(new e("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final s Y = this.addSetting(new s("Centroid Marker", "Show the centre of qualifying zones", true));
   private b7 P = b7.EMPTY;
   private List<w_.w> W = List.of();
   private final Set<Long> z = new HashSet<>();
   private wo u;
   private int g;
   private LevelChunk C;

   public pl() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      ls.d();

      for (q var2 : this.detectionSettings()) {
         var2.o(var1 -> this.n("detection changed"));
      }

      this.n.P(this.h::getValue);
      this.Y.P(this.h::getValue);
   }

   @Override
   public List<q<?>> detectionSettings() {
      return List.of(this.c, this.d, this.j, this.Q, this.l, this.x, this.O, this.I, this.Z, this.A, this.p, this.h, this.n);
   }

   @Override
   public List<q<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.Y);
      return List.copyOf(var1);
   }

   public static boolean C(BlockState var0) {
      return var0.is(Blocks.AMETHYST_CLUSTER)
         || var0.is(Blocks.SMALL_AMETHYST_BUD)
         || var0.is(Blocks.MEDIUM_AMETHYST_BUD)
         || var0.is(Blocks.LARGE_AMETHYST_BUD);
   }

   private boolean M(cp.z var1) {
      return switch (var1) {
         case P -> this.O.getValue();
         case D -> this.I.getValue();
         case L -> this.Z.getValue();
         case k -> this.A.getValue();
         case o -> this.p.getValue();
      };
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return C(var1) || cp.x(var1);
   }

   @Override
   protected int minimumY() {
      return Math.min(this.l.getValue().intValue(), this.c.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.l.getValue().intValue(), this.c.getValue().intValue()));
   }

   @Override
   protected void beginScan() {
      this.P = b7.EMPTY;
      this.g = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.W = List.of();
      this.z.clear();
      this.u = null;
      this.C = null;
   }

   @Override
   protected void beforeScan() {
      this.u = ls.E(getMc().level);

      for (long var2 : this.u.J()) {
         this.c(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      ChunkPos var3 = var1.getPos();
      return super.sectionCandidate(var1, var2) || this.x.getValue() && this.u != null && this.u.w(var3.x(), (var1.getMinY() >> 4) + var2, var3.z());
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      this.C = var1;
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.is(Blocks.AMETHYST_CLUSTER)) {
            this.a(var2);
         } else if (C(var1)) {
            this.F();
         }
      }
   }

   private BlockState b(BlockPos var1) {
      if (var1.getY() < this.minimumY() || var1.getY() > this.maximumY()) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.C != null && var1.getX() >> 4 == this.C.getPos().x() && var1.getZ() >> 4 == this.C.getPos().z()) {
         return this.C.getBlockState(var1);
      }

      LevelChunk var2 = getMc().level.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      return var2 == null ? Blocks.AIR.defaultBlockState() : var2.getBlockState(var1);
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (cp.x(var2) || this.x.getValue() && this.P.light() < 16 && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            if (this.x.getValue()
               && this.P.light() < 16
               && this.u != null
               && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))
               && cp.i(var2, this.u.A(var6.getX(), var4, var6.getZ()), var6, this::b)) {
               this.P = this.P.plus(new b7(1, 0, 0, 0, 0, 0));
               this.g = var4;
            }

            if (cp.x(var2)) {
               cp.z var7 = cp.H(var2, var6, this::b);
               if (var7 != null && this.M(var7)) {
                  this.P = this.P.plus(switch (var7) {
                     case P -> new b7(0, 1, 0, 0, 0, 0);
                     case D -> new b7(0, 0, 1, 0, 0, 0);
                     case L -> new b7(0, 0, 0, 1, 0, 0);
                     case k -> new b7(0, 0, 0, 0, 1, 0);
                     case o -> new b7(0, 0, 0, 0, 0, 1);
                  });
                  this.g = var4;
               }
            }
         }
      }
   }

   @Override
   protected g0.t evidence() {
      g0.t var1 = super.evidence();
      return new g0.t(var1.mature(), var1.buds(), 0, this.g == Integer.MIN_VALUE ? var1.sampleY() : this.g, com.larpclient.tm.EMPTY, this.P, List.of());
   }

   @Override
   protected boolean qualifies(g0.t var1) {
      return var1.matureAmethyst(this.d.getValue().intValue(), this.j.getValue().intValue()) || var1.sus().score() >= this.s();
   }

   private int s() {
      return this.Q.getValue().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.h.getValue() ? this.n.getValue().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map var1 = this.O().A();
      HashSet var2 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2x);
         }
      });
      ArrayList var3 = new ArrayList();

      for (w_.w var5 : w_.m(var2, this.zoneRadius())) {
         b7 var6 = b7.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            g0.t var11 = (g0.t)var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= this.qualifies(var11);
         }

         int var12 = var7 == 0 && var6.score() < this.s() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.E(var10, (g0.t)var1.get(var10), (boolean)var12);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.W = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.z.clear();
   }

   @Override
   protected void drawEvidence(ChunkPos var1, g0.t var2, z4 var3, int var4) {
      if (this.h.getValue() && this.Y.getValue()) {
         for (w_.w var6 : this.W) {
            if (var6.members().contains(var1.pack())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.z.size() < 24 && this.z.add(var7)) {
                  var3.d(new Vec3(var6.x(), var2.sampleY() + 2, var6.z()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(g0.t var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected md alert(ChunkPos var1, g0.t var2) {
      return md.chunk("Suspicious area", this.describe(var2), var1.getMinBlockX(), var1.getMinBlockZ());
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}

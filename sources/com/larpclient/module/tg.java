package com.larpclient.module;

import com.larpclient.da;
import com.larpclient.ek;
import com.larpclient.fw;
import com.larpclient.j9;
import com.larpclient.mi;
import com.larpclient.oa;
import com.larpclient.ye;
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

public final class tg extends tf {
   private final lc t = this.addSetting(new lc("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0));
   private final lc Z = this.addSetting(
      new lc("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final lc f = this.addSetting(new lc("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0));
   private final lc j = this.addSetting(new lc("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0));
   private final lc E = this.addSetting(new lc("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final ln V = this.addSetting(new ln("Amethyst Light", "Server light 5 beside received amethyst; possible grown clusters", true));
   private final ln L = this.addSetting(new ln("Kelp", "Tall or max-age kelp columns", true));
   private final ln T = this.addSetting(new ln("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final ln y = this.addSetting(new ln("Berries", "Fully mature sweet berry bushes", true));
   private final ln s = this.addSetting(new ln("Vines", "Hanging vines at least 7 blocks long", true));
   private final ln q = this.addSetting(new ln("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final ln e = this.addSetting(new ln("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final lc d = this.addSetting(new lc("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final ln c = this.addSetting(new ln("Centroid Marker", "Show the centre of qualifying zones", true));
   private da v = da.EMPTY;
   private List<ek.g> A = List.of();
   private final Set<Long> M = new HashSet<>();
   private fw m;
   private int I;
   private LevelChunk X;

   public tg() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      ye.S();

      for (l6 var2 : this.detectionSettings()) {
         var2.W(var1 -> this.C("detection changed"));
      }

      this.d.x(this.e::getValue);
      this.c.x(this.e::getValue);
   }

   @Override
   public List<l6<?>> detectionSettings() {
      return List.of(this.t, this.Z, this.f, this.j, this.E, this.V, this.L, this.T, this.y, this.s, this.q, this.e, this.d);
   }

   @Override
   public List<l6<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.c);
      return List.copyOf(var1);
   }

   public static boolean i(BlockState var0) {
      return var0.is(Blocks.AMETHYST_CLUSTER)
         || var0.is(Blocks.SMALL_AMETHYST_BUD)
         || var0.is(Blocks.MEDIUM_AMETHYST_BUD)
         || var0.is(Blocks.LARGE_AMETHYST_BUD);
   }

   private boolean d(j9.t var1) {
      return switch (var1) {
         case G -> this.L.getValue();
         case J -> this.T.getValue();
         case R -> this.y.getValue();
         case y -> this.s.getValue();
         case C -> this.q.getValue();
      };
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return i(var1) || j9.o(var1);
   }

   @Override
   protected int minimumY() {
      return Math.min(this.E.getValue().intValue(), this.t.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.E.getValue().intValue(), this.t.getValue().intValue()));
   }

   @Override
   protected void beginScan() {
      this.v = da.EMPTY;
      this.I = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.A = List.of();
      this.M.clear();
      this.m = null;
      this.X = null;
   }

   @Override
   protected void beforeScan() {
      this.m = ye.T(getMc().level);

      for (long var2 : this.m.V()) {
         this.l(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(LevelChunk var1, int var2) {
      ChunkPos var3 = var1.getPos();
      return super.sectionCandidate(var1, var2) || this.V.getValue() && this.m != null && this.m.G(var3.x(), (var1.getMinY() >> 4) + var2, var3.z());
   }

   @Override
   protected void beginChunk(LevelChunk var1) {
      this.X = var1;
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.is(Blocks.AMETHYST_CLUSTER)) {
            this.v(var2);
         } else if (i(var1)) {
            this.G();
         }
      }
   }

   private BlockState Q(BlockPos var1) {
      if (var1.getY() < this.minimumY() || var1.getY() > this.maximumY()) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.X != null && var1.getX() >> 4 == this.X.getPos().x() && var1.getZ() >> 4 == this.X.getPos().z()) {
         return this.X.getBlockState(var1);
      }

      LevelChunk var2 = getMc().level.getChunkSource().getChunkNow(var1.getX() >> 4, var1.getZ() >> 4);
      return var2 == null ? Blocks.AIR.defaultBlockState() : var2.getBlockState(var1);
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (j9.o(var2) || this.V.getValue() && this.v.light() < 16 && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            if (this.V.getValue()
               && this.v.light() < 16
               && this.m != null
               && (var2.isAir() || var2.is(Blocks.AMETHYST_CLUSTER))
               && j9.L(var2, this.m.z(var6.getX(), var4, var6.getZ()), var6, this::Q)) {
               this.v = this.v.plus(new da(1, 0, 0, 0, 0, 0));
               this.I = var4;
            }

            if (j9.o(var2)) {
               j9.t var7 = j9.i(var2, var6, this::Q);
               if (var7 != null && this.d(var7)) {
                  this.v = this.v.plus(switch (var7) {
                     case G -> new da(0, 1, 0, 0, 0, 0);
                     case J -> new da(0, 0, 1, 0, 0, 0);
                     case R -> new da(0, 0, 0, 1, 0, 0);
                     case y -> new da(0, 0, 0, 0, 1, 0);
                     case C -> new da(0, 0, 0, 0, 0, 1);
                  });
                  this.I = var4;
               }
            }
         }
      }
   }

   @Override
   protected oa.j evidence() {
      oa.j var1 = super.evidence();
      return new oa.j(var1.mature(), var1.buds(), 0, this.I == Integer.MIN_VALUE ? var1.sampleY() : this.I, com.larpclient.l4.EMPTY, this.v, List.of());
   }

   @Override
   protected boolean qualifies(oa.j var1) {
      return var1.matureAmethyst(this.Z.getValue().intValue(), this.f.getValue().intValue()) || var1.sus().score() >= this.S();
   }

   private int S() {
      return this.j.getValue().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.e.getValue() ? this.d.getValue().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map var1 = this.c().H();
      HashSet var2 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2x);
         }
      });
      ArrayList var3 = new ArrayList();

      for (ek.g var5 : ek.L(var2, this.zoneRadius())) {
         da var6 = da.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            oa.j var11 = (oa.j)var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= this.qualifies(var11);
         }

         int var12 = var7 == 0 && var6.score() < this.S() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.P(var10, (oa.j)var1.get(var10), (boolean)var12);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.A = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.M.clear();
   }

   @Override
   protected void drawEvidence(ChunkPos var1, oa.j var2, mi var3, int var4) {
      if (this.e.getValue() && this.c.getValue()) {
         for (ek.g var6 : this.A) {
            if (var6.members().contains(var1.pack())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.M.size() < 24 && this.M.add(var7)) {
                  var3.t(new Vec3(var6.x(), var2.sampleY() + 2, var6.z()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(oa.j var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}

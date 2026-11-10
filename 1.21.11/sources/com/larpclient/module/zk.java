package com.larpclient.module;

import com.larpclient.c7;
import com.larpclient.fa;
import com.larpclient.fe;
import com.larpclient.fu;
import com.larpclient.h5;
import com.larpclient.i2;
import com.larpclient.l2;
import com.larpclient.la;
import com.larpclient.sv;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2818;

public final class zk extends z8 {
   private final lo f = this.addSetting(new lo("Max Scan Y", "Highest growth/light height to inspect, capped at Y60", 48.0, -64.0, 60.0, 1.0));
   private final lo N = this.addSetting(
      new lo("Min Mature Clusters", "Visible mature amethyst required per chunk; natural geodes can match", 8.0, 1.0, 128.0, 1.0)
   );
   private final lo Q = this.addSetting(new lo("Min Mature Percent", "Mature clusters as a share of clusters plus buds", 80.0, 50.0, 100.0, 5.0));
   private final lo A = this.addSetting(new lo("Sensitivity", "Higher requires more evidence: 5 points per step", 3.0, 1.0, 10.0, 1.0));
   private final lo s = this.addSetting(new lo("Min Scan Y", "Lowest growth/light height", -64.0, -64.0, 60.0, 1.0));
   private final l3 Y = this.addSetting(new l3("Amethyst Light", "Received amethyst light and low-light peaks, including hidden block clues", true));
   private final l3 V = this.addSetting(new l3("Kelp", "Tall or max-age kelp columns", true));
   private final l3 t = this.addSetting(new l3("Bamboo", "Bamboo columns at least 12 blocks tall", true));
   private final l3 x = this.addSetting(new l3("Berries", "Fully mature sweet berry bushes", true));
   private final l3 R = this.addSetting(new l3("Vines", "Hanging vines at least 7 blocks long", true));
   private final l3 k = this.addSetting(new l3("Dripstone", "Downward spikes at least 5 blocks long", true));
   private final l3 F = this.addSetting(new l3("Smart Mode", "Combine neighbouring clue chunks into scored zones", true));
   private final lo H = this.addSetting(new lo("Merge Radius", "Chunk distance linking clues into zones", 1.0, 1.0, 4.0, 1.0));
   private final l3 X = this.addSetting(new l3("Centroid Marker", "Show the centre of qualifying zones", true));
   private fe d = fe.EMPTY;
   private List<c7.a> i = List.of();
   private final Set<Long> O = new HashSet<>();
   private fa L;
   private int l;
   private class_2818 Z;

   public zk() {
      super("SusChunkFinder", "Mark possible player activity from growth and light clues");
      la.p();

      for (lb var2 : this.detectionSettings()) {
         var2.a(var1 -> this.m("detection changed"));
      }

      this.H.O(this.F::getValue);
      this.X.O(this.F::getValue);
   }

   @Override
   public List<lb<?>> detectionSettings() {
      return List.of(this.f, this.N, this.Q, this.A, this.s, this.Y, this.V, this.t, this.x, this.R, this.k, this.F, this.H);
   }

   @Override
   public List<lb<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.X);
      return List.copyOf(var1);
   }

   public static boolean T(class_2680 var0) {
      return var0.method_27852(class_2246.field_27161)
         || var0.method_27852(class_2246.field_27164)
         || var0.method_27852(class_2246.field_27163)
         || var0.method_27852(class_2246.field_27162);
   }

   private boolean s(fu.m var1) {
      return switch (var1) {
         case k -> this.V.getValue();
         case V -> this.t.getValue();
         case O -> this.x.getValue();
         case q -> this.R.getValue();
         case w -> this.k.getValue();
      };
   }

   @Override
   protected boolean candidate(class_2680 var1) {
      return T(var1) || fu.I(var1);
   }

   @Override
   protected int minimumY() {
      return Math.min(this.s.getValue().intValue(), this.f.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.s.getValue().intValue(), this.f.getValue().intValue()));
   }

   @Override
   protected void beginScan() {
      this.d = fe.EMPTY;
      this.l = Integer.MIN_VALUE;
   }

   @Override
   protected void clearEvidence() {
      this.beginScan();
      this.i = List.of();
      this.O.clear();
      this.L = null;
      this.Z = null;
   }

   @Override
   protected void beforeScan() {
      this.L = la.L(getMc().field_1687);

      for (long var2 : this.L.T()) {
         this.w(var2);
      }
   }

   @Override
   protected boolean sectionCandidate(class_2818 var1, int var2) {
      class_1923 var3 = var1.method_12004();
      return super.sectionCandidate(var1, var2)
         || this.Y.getValue() && this.L != null && this.L.w(var3.field_9181, (var1.method_31607() >> 4) + var2, var3.field_9180);
   }

   @Override
   protected void beginChunk(class_2818 var1) {
      this.Z = var1;
   }

   @Override
   protected void count(class_2680 var1, int var2) {
      if (var2 >= this.minimumY() && var2 <= this.maximumY()) {
         if (var1.method_27852(class_2246.field_27161)) {
            this.K(var2);
         } else if (T(var1)) {
            this.s();
         }
      }
   }

   private class_2680 b(class_2338 var1) {
      if (var1.method_10264() < this.minimumY() || var1.method_10264() > this.maximumY()) {
         return class_2246.field_10124.method_9564();
      }

      if (this.Z != null && var1.method_10263() >> 4 == this.Z.method_12004().field_9181 && var1.method_10260() >> 4 == this.Z.method_12004().field_9180) {
         return this.Z.method_8320(var1);
      }

      class_2818 var2 = getMc().field_1687.method_2935().method_21730(var1.method_10263() >> 4, var1.method_10260() >> 4);
      return var2 == null ? class_2246.field_10124.method_9564() : var2.method_8320(var1);
   }

   @Override
   protected void scanBlock(class_2818 var1, class_2680 var2, int var3, int var4, int var5) {
      if (var4 >= this.minimumY() && var4 <= this.maximumY()) {
         this.count(var2, var4);
         if (fu.I(var2) || this.Y.getValue() && this.d.light() < 16) {
            class_2338 var6 = new class_2338(var1.method_12004().method_8326() + var3, var4, var1.method_12004().method_8328() + var5);
            if (this.Y.getValue()
               && this.d.light() < 16
               && this.L != null
               && (
                  fu.K(var2, this.L.z(var6.method_10263(), var4, var6.method_10260()), var6, this::b)
                     || sv.m(var6, var1x -> this.L.z(var1x.method_10263(), var1x.method_10264(), var1x.method_10260()), this::b)
               )) {
               this.d = this.d.plus(new fe(1, 0, 0, 0, 0, 0));
               this.l = var4;
            }

            if (fu.I(var2)) {
               fu.m var7 = fu.t(var2, var6, this::b);
               if (var7 != null && this.s(var7)) {
                  this.d = this.d.plus(switch (var7) {
                     case k -> new fe(0, 1, 0, 0, 0, 0);
                     case V -> new fe(0, 0, 1, 0, 0, 0);
                     case O -> new fe(0, 0, 0, 1, 0, 0);
                     case q -> new fe(0, 0, 0, 0, 1, 0);
                     case w -> new fe(0, 0, 0, 0, 0, 1);
                  });
                  this.l = var4;
               }
            }
         }
      }
   }

   @Override
   protected h5.n evidence() {
      h5.n var1 = super.evidence();
      return new h5.n(var1.mature(), var1.buds(), 0, this.l == Integer.MIN_VALUE ? var1.sampleY() : this.l, com.larpclient.z.EMPTY, this.d, List.of());
   }

   @Override
   protected boolean qualifies(h5.n var1) {
      return var1.matureAmethyst(this.N.getValue().intValue(), this.Q.getValue().intValue()) || var1.sus().score() >= this.u();
   }

   private int u() {
      return this.A.getValue().intValue() * 5;
   }

   @Override
   protected int zoneRadius() {
      return this.F.getValue() ? this.H.getValue().intValue() : 0;
   }

   @Override
   protected void afterScan() {
      Map var1 = this.i().z();
      HashSet var2 = new HashSet();
      var1.forEach((var2x, var3x) -> {
         if (var3x.sus().score() > 0 || this.qualifies(var3x)) {
            var2.add(var2x);
         }
      });
      ArrayList var3 = new ArrayList();

      for (c7.a var5 : c7.L(var2, this.zoneRadius())) {
         fe var6 = fe.EMPTY;
         byte var7 = 0;

         for (long var9 : var5.members()) {
            h5.n var11 = (h5.n)var1.get(var9);
            var6 = var6.plus(var11.sus());
            var7 |= this.qualifies(var11);
         }

         int var12 = var7 == 0 && var6.score() < this.u() ? 0 : 1;

         for (long var10 : var5.members()) {
            this.m(var10, (h5.n)var1.get(var10), (boolean)var12);
         }

         if (var12 != 0) {
            var3.add(var5);
         }
      }

      this.i = List.copyOf(var3);
   }

   @Override
   protected void beginDraw() {
      this.O.clear();
   }

   @Override
   protected void drawEvidence(class_1923 var1, h5.n var2, l2 var3, int var4) {
      if (this.F.getValue() && this.X.getValue()) {
         for (c7.a var6 : this.i) {
            if (var6.members().contains(var1.method_8324())) {
               long var7 = var6.members().stream().min(Long::compare).orElseThrow();
               if (this.O.size() < 24 && this.O.add(var7)) {
                  var3.n(new class_243(var6.x(), var2.sampleY() + 2, var6.z()), "Possible activity · " + var6.members().size() + " chunks", var4);
               }
               break;
            }
         }
      }
   }

   @Override
   protected String describe(h5.n var1) {
      return var1.sus().score() > 0 ? var1.sus().describe() : var1.mature() + " mature, " + var1.buds() + " buds (" + var1.maturePercent() + "% mature)";
   }

   @Override
   protected i2 alert(class_1923 var1, h5.n var2) {
      return i2.chunk("Suspicious area", this.describe(var2), var1.method_8326(), var1.method_8328());
   }

   @Override
   protected int tint() {
      return -3498764;
   }
}

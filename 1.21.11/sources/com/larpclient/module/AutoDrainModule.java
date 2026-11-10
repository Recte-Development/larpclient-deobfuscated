package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q226fb0b431f37244;
import com.larp.obf.q4031f93fe72647cb.qf65e76ba6afee893;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.fs;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.w6;
import com.larpclient.wp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2263;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2404;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3612;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class AutoDrainModule extends Module {
   public static final long t = 120000L;
   public static final int F = 2;
   public static final long i = 60000L;
   private static final double v = 0.4;
   private static final int o = 512;
   private static final Map<class_2338, Long> u = new HashMap<>();
   private final lo U = this.addSetting(new lo("Range", "Furthest water to scoop, capped by your reach", 4.5, 2.0, 6.0, 0.5));
   private final lo Q = this.addSetting(new lo("Delay", "Ticks to wait between scoops", 2.0, 0.0, 20.0, 1.0));
   private final l3 N = this.addSetting(new l3("Waterlogged", "Also drain water inside stairs, slabs and other waterlogged blocks", false));
   private final l3 g = this.addSetting(new l3("Ignore My Water", "Leave water you emptied yourself in the last two minutes", true));
   private final fs T = new fs(this);
   private final Map<class_2338, int[]> S = new HashMap<>();
   private final Map<class_2338, Long> y = new HashMap<>();
   private class_746 O;
   private class_638 P;
   private class_2338 l;
   private class_243 j;
   private boolean x;
   private float c;
   private float G;
   private int M;
   private int V;
   private String I = "Ready";

   public AutoDrainModule() {
      super("Auto Drain", "Scoop up nearby still water with empty buckets", Category.PLAYER, -1);
   }

   public static int A(double var0, int var2, double var3, int var5) {
      int var6 = Double.compare(var3, var0);
      return var6 != 0 ? var6 : Integer.compare(var5, var2);
   }

   public static boolean b(int var0, boolean var1) {
      return var0 == 1 || var1;
   }

   public static boolean A(int var0) {
      return var0 >= 2;
   }

   public static void v(class_1657 var0, class_1268 var1) {
      class_310 var2 = class_310.method_1551();
      if (var0 == var2.field_1724 && var2.field_1687 != null && var0.method_5998(var1).method_31574(class_1802.field_8705)) {
         class_243 var3 = var0.method_33571();
         class_243 var4 = var3.method_1019(var0.method_5720().method_1021(var0.method_55754()));
         class_3965 var5 = var2.field_1687.method_17742(new class_3959(var3, var4, class_3960.field_17559, class_242.field_1348, var0));
         if (var5.method_17783() == class_240.field_1332) {
            long var6 = System.currentTimeMillis();
            if (u.size() >= 512) {
               u.clear();
            }

            u.put(var5.method_17777().method_10062(), var6);
            u.put(var5.method_17777().method_10093(var5.method_17780()).method_10062(), var6);
         }
      }
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.I : "";
   }

   @Override
   public void onEnable() {
      this.C();
      this.O = mc.field_1724;
      this.P = mc.field_1687;
      this.I = "Ready";
      q8.G(
         "AutoDrain",
         "Enabled; range {}, delay {} ticks, waterlogged={}, ignore own={}",
         this.U.getValue(),
         this.Q.getValue().intValue(),
         this.N.getValue(),
         this.g.getValue()
      );
   }

   @Override
   public void onDisable() {
      if (this.x && this.O != null && this.O == getMc().field_1724) {
         this.T.o(mc, true);
         this.O.method_36456(this.c);
         this.O.method_36457(this.G);
      }

      this.C();
      this.O = null;
      this.P = null;
   }

   private void C() {
      cr.p(this);
      this.x = (boolean)0;
      this.l = null;
      this.j = null;
      this.M = 0;
      this.V = 0;
      this.S.clear();
      this.y.clear();
   }

   private boolean p() {
      wp.z var1 = wp.o();
      return !cr.f() || mc.field_1755 != null || var1 != wp.z.D;
   }

   @Override
   public void onTick() {
      if (this.O != mc.field_1724 || this.P != mc.field_1687) {
         this.T.o(getMc(), false);
         this.C();
         u.clear();
         this.O = mc.field_1724;
         this.P = mc.field_1687;
      }

      if (this.O != null && this.P != null) {
         if (this.p()) {
            if (this.x) {
               this.I = "Paused";
            }
         } else {
            this.Z(System.currentTimeMillis());
            if (this.M > 0) {
               byte var5 = 1;
               int var4 = this.M;
               this.M = var4 - var5;
            } else {
               int var1 = fk.T(class_1802.field_8550, true);
               byte var7 = -1;
               int var6 = var1;
               int var10000 = Integer.compare(var6, var7);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.k("No empty buckets");
               } else {
                  byte var9 = -2;
                  int var8 = var1;
                  var10000 = Integer.compare(var8, var9);
                  class_1799 var2 = (var10000 | -var10000) >>> 31 == 0 ? this.O.method_6079() : this.O.method_31548().method_5438(var1);
                  var10000 = var2.method_7947();
                  int var10001 = this.O.method_31548().method_7376();
                  byte var11 = -1;
                  int var10 = var10001;
                  var10001 = Integer.compare(var10, var11);
                  if (!b(var10000, ((var10001 | -var10001) >>> 31 ^ 1) == 0)) {
                     this.k("Inventory full");
                  } else if (!this.s()) {
                     this.k(this.V > 0 ? "Drained " + this.V : "No water in reach");
                  } else {
                     if (!this.x) {
                        this.x = (boolean)1;
                        this.V = 0;
                        this.c = this.O.method_36454();
                        this.G = this.O.method_36455();
                        q8.v("AutoDrain", "Run started at {}", this.l);
                     }

                     this.I = "Draining";
                     float[] var3 = t9.X(this.O.method_33571(), this.j);
                     t9.O(this, t9.v.w, var3[0], var3[1], 1.0);
                     cr.y(this, this::s, this::N);
                  }
               }
            }
         }
      }
   }

   private boolean s() {
      double var1 = Math.min(this.U.getValue(), this.O.method_55754());
      class_243 var3 = this.O.method_33571();
      int var4 = (int)Math.ceil(var1);
      class_2338 var5 = class_2338.method_49638(var3);
      long var6 = System.currentTimeMillis();
      class_2338 var8 = null;
      class_243 var9 = null;
      double var10 = 0.0;

      for (class_2338 var13 : class_2338.method_10097(var5.method_10069(-var4, -var4, -var4), var5.method_10069(var4, var4, var4))) {
         if (this.q(var13, var6)) {
            class_243 var14 = class_243.method_24955(var13).method_1031(0.0, 0.4, 0.0);
            double var15 = var3.method_1022(var14);
            if (!(var15 > var1)
               && (var8 == null || A(var15, var13.method_10264(), var10, var8.method_10264()) < 0)
               && this.K(var13, var3, var14.method_1020(var3), var1)) {
               var8 = var13.method_10062();
               var9 = var14;
               var10 = var15;
            }
         }
      }

      this.l = var8;
      this.j = var9;
      return var8 != null;
   }

   private boolean q(class_2338 var1, long var2) {
      if (!this.P.method_8316(var1).method_33659(class_3612.field_15910)) {
         return false;
      }

      class_2680 var4 = this.P.method_8320(var1);
      if (!(var4.method_26204() instanceof class_2263)) {
         return false;
      }

      if (!(var4.method_26204() instanceof class_2404) && !this.N.getValue()) {
         return false;
      }

      if (this.O.method_5829().method_994(new class_238(var1))) {
         return false;
      }

      if (this.g.getValue()) {
         Long var5 = u.get(var1);
         if (var5 != null && var2 - var5 < 120000L) {
            return false;
         }
      }

      int[] var6 = this.S.get(var1);
      return var6 == null || !A(var6[0]);
   }

   private boolean K(class_2338 var1, class_243 var2, class_243 var3, double var4) {
      class_243 var6 = var2.method_1019(var3.method_1029().method_1021(var4));
      class_3965 var7 = this.P.method_17742(new class_3959(var2, var6, class_3960.field_17559, class_242.field_1345, this.O));
      return var7.method_17783() == class_240.field_1332 && var7.method_17777().equals(var1);
   }

   private boolean N() {
      if (this.x && this.l != null && !this.O.method_6115() && this.r()) {
         if (!this.P.method_8316(this.l).method_33659(class_3612.field_15910)) {
            return false;
         }

         double var1 = Math.min(this.U.getValue(), this.O.method_55754());
         return this.K(this.l, this.O.method_33571(), this.O.method_5720(), var1);
      } else {
         return false;
      }
   }

   private boolean r() {
      int var1 = fk.T(class_1802.field_8550, true);
      return var1 == -2 || var1 >= 0 && (var1 == this.O.method_31548().method_67532() || fs.o(this));
   }

   private class_1268 K() {
      int var1 = fk.T(class_1802.field_8550, true);
      if (var1 == -2) {
         return class_1268.field_5810;
      } else if (var1 < 0) {
         return null;
      } else {
         return var1 != this.O.method_31548().method_67532() && !this.T.m(this.O, var1) ? null : class_1268.field_5808;
      }
   }

   private void s() {
      if (this.N()) {
         class_1268 var1 = this.K();
         if (var1 != null) {
            class_2338 var2 = this.l;
            boolean var3 = mc.field_1761.method_2919(this.O, var1).method_23665();
            this.M = this.Q.getValue().intValue();
            if (!var3) {
               this.I = "Scoop rejected";
               q8.v("AutoDrain", "Scoop at {} rejected locally", var2);
            } else {
               w6.H(this.O, var1);
               ((qf65e76ba6afee893)mc).larpUseDelay(4);
               long var4 = System.currentTimeMillis();
               int[] var6 = this.S.computeIfAbsent(var2, var0 -> new int[1]);
               var6[0]++;
               this.y.put(var2, var4);
               this.V++;
               if (A(var6[0])) {
                  q8.v("AutoDrain", "{} refills; skipped for {} s", var2, 60L);
               }

               q8.v("AutoDrain", "Scooped {} ({} this run)", var2, this.V);
            }
         }
      }
   }

   private void k(String var1) {
      this.I = var1;
      if (this.x) {
         int var2 = this.O.method_31548().method_67532();
         this.T.o(mc, true);
         if (mc.field_1761 != null && var2 != this.O.method_31548().method_67532()) {
            ((q226fb0b431f37244)mc.field_1761).larpSyncCarriedItem();
         }

         t9.O(this, t9.v.w, this.c, this.G, 1.0);
         q8.v("AutoDrain", "Run ended after {} scoops: {}", this.V, var1);
         this.x = (boolean)0;
         this.l = null;
         this.j = null;
      }
   }

   private void Z(long var1) {
      u.values().removeIf(var2 -> var1 - var2 >= 120000L);
      this.y.entrySet().removeIf(var3 -> {
         int var4 = var1 - var3.getValue() >= 60000L ? 1 : 0;
         if (var4 != 0) {
            this.S.remove(var3.getKey());
         }

         return (boolean)var4;
      });
      if (this.S.size() > 512) {
         this.S.clear();
         this.y.clear();
      }
   }
}

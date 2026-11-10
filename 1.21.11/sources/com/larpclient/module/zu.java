package com.larpclient.module;

import com.larpclient.et;
import com.larpclient.h5;
import com.larpclient.i2;
import com.larpclient.l2;
import com.larpclient.q8;
import com.larpclient.y9;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Load;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_638;

public final class zu extends z8 {
   private final li l = this.addSetting(
      new li(
         "Detection",
         "Conservative: dedicated blocks. Balanced: storage plus utilities. Broad: more possible structures",
         "Balanced",
         "Conservative",
         "Balanced",
         "Broad"
      )
   );
   private final lo m = this.addSetting(
      new lo("Min Storage", "Ordinary storage required in one 16-block height band; dedicated clues bypass this", 4.0, 1.0, 64.0, 1.0)
   );
   private final lo Y = this.addSetting(
      new lo("Min Utilities", "Crafting/workstation blocks supporting ordinary storage in Balanced detection", 2.0, 1.0, 32.0, 1.0)
   );
   private final lo O = this.addSetting(new lo("Min Ender Chests", "Ender chests needed in one 16-block height band outside the End", 1.0, 1.0, 64.0, 1.0));
   private final lo H = this.addSetting(new lo("Min Scan Y", "Lowest received block height to inspect", -64.0, -64.0, 60.0, 1.0));
   private final lo V = this.addSetting(new lo("Max Scan Y", "Highest block height to inspect, capped at Y60", 60.0, -64.0, 60.0, 1.0));
   private final l3 x = this.addSetting(new l3("Terrain Clues", "Include deepslate placement evidence in Broad detection; never proof on its own", true));
   private final l3 R = this.addSetting(new l3("Base Boxes", "Outline the detected building evidence as well as its chunk", true));
   private final li W = this.addSetting(new li("Base Style", "Box style for the detected building evidence", "Box", "Box", "Corners", "Filled"));
   private final et.x s = new et.x();
   private final l3 i = this.addSetting(
      new l3("Visibility Diagnostic", "Experimental received-data observations in latest.log; does not recover hidden blocks", false)
   );
   private final y9 f = new y9();
   private class_638 F;
   private long I;
   private boolean v;
   private static zu X;
   private boolean A;
   private final l3 Z = this.addSetting(new l3("Honey Clues", "Full hives or nests after 10 seconds loaded; natural bee activity can qualify", true));
   private final Map<Long, Long> j = new HashMap<>();
   private boolean P;
   private int L;
   private int Q;
   private boolean N;
   private boolean z;

   public zu() {
      super("Base Finder", "Find base clues in loaded blocks");

      for (lb var2 : this.detectionSettings()) {
         var2.a(var1 -> this.m("detection changed"));
      }

      this.m.O(() -> !this.l.getValue().equals("Conservative"));
      this.Y.O(() -> this.l.getValue().equals("Balanced"));
      this.x.O(() -> this.l.getValue().equals("Broad"));
      this.W.O(this.R::getValue);
      this.q();
      X = this;
      this.i.a(var1 -> {
         if (this.F != null) {
            this.T("setting changed");
         }

         this.L();
      });
      ClientChunkEvents.CHUNK_LOAD.register((Load)(var1, var2x) -> this.g(var1, var2x));
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2x) -> {
         if (var1 == this.F) {
            this.f.k(var2x.method_12004().method_8324());
         }

         if (getMc() != null && var1 == getMc().field_1687) {
            this.j.remove(var2x.method_12004().method_8324());
         }
      });
   }

   @Override
   public List<lb<?>> detectionSettings() {
      return List.of(this.l, this.m, this.Y, this.O, this.H, this.V, this.x, this.Z);
   }

   @Override
   public List<lb<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.add(this.R);
      var1.add(this.W);
      return List.copyOf(var1);
   }

   public static class_238 R(class_1923 var0, h5.n var1) {
      class_238 var2 = var1.base().bounds();
      return var2 == null ? null : var2.method_989(var0.method_8326(), 0.0, var0.method_8328()).method_1014(0.08);
   }

   @Override
   protected void drawEvidence(class_1923 var1, h5.n var2, l2 var3, int var4) {
      if (this.R.getValue()) {
         class_238 var5 = R(var1, var2);
         if (var5 != null) {
            var3.X(var5, this.W.getValue(), var4);
         }
      }
   }

   @Override
   protected boolean candidate(class_2680 var1) {
      return this.F(var1, 16);
   }

   private boolean F(class_2680 var1, int var2) {
      if (et.l(var1)) {
         return this.Z.getValue();
      } else {
         et.g var3 = et.i(var1, var2, this.A);
         if (var3 == et.g.U) {
            return true;
         } else {
            return this.l.getValue().equals("Conservative")
               ? false
               : var3 == et.g.b || var3 == et.g.l || this.l.getValue().equals("Broad") && (var3 == et.g.I || var3 == et.g.k && this.x.getValue());
         }
      }
   }

   @Override
   protected boolean sectionCandidate(class_2818 var1, int var2) {
      class_2826 var3 = var1.method_12006()[var2];
      int var4 = var1.method_31607() + var2 * 16 + 15;
      return !var3.method_38292() && var3.method_19523(var2x -> this.F(var2x, var4));
   }

   @Override
   protected int minimumY() {
      return Math.min(this.H.getValue().intValue(), this.V.getValue().intValue());
   }

   @Override
   protected int maximumY() {
      return Math.min(60, Math.max(this.H.getValue().intValue(), this.V.getValue().intValue()));
   }

   @Override
   protected void beforeScan() {
      class_1923 var1 = getMc().field_1724.method_31476();
      this.j
         .keySet()
         .removeIf(
            var2 -> {
               class_1923 var3 = new class_1923(var2);
               return Math.abs((long)var3.field_9181 - var1.field_9181) > this.v()
                  || Math.abs((long)var3.field_9180 - var1.field_9180) > this.v()
                  || getMc().field_1687.method_2935().method_21730(var3.field_9181, var3.field_9180) == null;
            }
         );
   }

   @Override
   protected void beginChunk(class_2818 var1) {
      long var2 = this.j.computeIfAbsent(var1.method_12004().method_8324(), var1x -> this.H());
      this.P = (boolean)(this.H() - var2 >= 200L ? 1 : 0);
   }

   @Override
   protected void beginScan() {
      this.s.m();
      this.P = (boolean)0;
      this.A = (boolean)(getMc() != null && getMc().field_1687 != null && getMc().field_1687.method_27983().equals(class_1937.field_25181) ? 1 : 0);
      this.q();
   }

   private void q() {
      this.L = this.minimumY();
      this.Q = this.maximumY();
      this.N = (boolean)(this.l.getValue().equals("Broad") && this.x.getValue() ? 1 : 0);
      this.z = this.Z.getValue();
   }

   @Override
   protected void count(class_2680 var1, int var2) {
      this.count(var1, 0, var2, 0);
   }

   @Override
   protected void count(class_2680 var1, int var2, int var3, int var4) {
      if (var3 >= this.L && var3 <= this.Q) {
         et.g var5 = this.s.v(var1, var3, this.A);
         if (var5 != et.g.i) {
            if (var5 != et.g.U || !et.l(var1) || this.z && this.P) {
               this.s.C(var5, var1, var2, var3, var4, this.A, this.N);
            }
         }
      }
   }

   @Override
   protected h5.n evidence() {
      com.larpclient.z var1 = this.s.n(this.l.getValue(), this.m.getValue().intValue(), this.Y.getValue().intValue(), this.O.getValue().intValue());
      return new h5.n(0, 0, 0, var1.sampleY(), var1);
   }

   @Override
   protected boolean qualifies(h5.n var1) {
      return var1.base().qualifies(this.l.getValue(), this.m.getValue().intValue(), this.Y.getValue().intValue(), this.O.getValue().intValue());
   }

   @Override
   protected String describe(h5.n var1) {
      return var1.base().describe();
   }

   @Override
   protected i2 alert(class_1923 var1, h5.n var2) {
      return i2.base(var1.method_8326(), var1.method_8328(), var2.base());
   }

   @Override
   protected int tint() {
      return -6629451;
   }

   @Override
   protected void clearEvidence() {
      this.s.m();
      this.j.clear();
      this.P = (boolean)0;
      this.q();
      if (this.F != null) {
         this.T("scan reset or lifecycle cleanup");
      }

      this.L();
   }

   public List<lb<?>> l() {
      return List.of(this.i);
   }

   private void L() {
      this.f.J();
      this.F = null;
      this.I = 0L;
      this.v = (boolean)0;
   }

   private boolean G(class_638 var1) {
      if (this.i.getValue() && !this.v && this.isRuntimeActive() && this.isInGame() && getMc().field_1687 == var1 && getMc().method_18854()) {
         if (this.F != var1) {
            if (this.F != null) {
               this.T("world changed");
            }

            this.L();
            this.F = var1;
            q8.G("BaseFinding", "Visibility diagnostic active: received block-entity chunk snapshots and server-verified updates only; no bypass assumed");
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean R(class_2338 var1) {
      if (var1.method_10264() >= this.minimumY() && var1.method_10264() <= this.maximumY()) {
         class_1923 var2 = getMc().field_1724.method_31476();
         return Math.abs((long)(var1.method_10263() >> 4) - var2.field_9181) <= this.v()
            && Math.abs((long)(var1.method_10260() >> 4) - var2.field_9180) <= this.v();
      } else {
         return false;
      }
   }

   public static boolean Z(class_2680 var0, int var1, boolean var2) {
      et.g var3 = et.i(var0, var1, var2);
      return var3 != et.g.i && var3 != et.g.k || var0.method_27852(class_2246.field_10260);
   }

   public static boolean o(class_2680 var0) {
      return var0.method_27852(class_2246.field_28888)
         || var0.method_27852(class_2246.field_10340)
         || var0.method_27852(class_2246.field_10515)
         || var0.method_27852(class_2246.field_10471);
   }

   private void T(class_2338 var1, class_2680 var2, y9.b var3) {
      if (this.R(var1)) {
         if (!this.f.B(this.I)) {
            this.f.X(var3, 1L);
         } else {
            this.f
               .U(
                  var1.method_10063(),
                  class_1923.method_8331(var1.method_10263() >> 4, var1.method_10260() >> 4),
                  var2.toString(),
                  Z(var2, var1.method_10264(), this.F.method_27983().equals(class_1937.field_25181)),
                  o(var2),
                  var3,
                  this.I
               );
         }
      }
   }

   private void g(class_638 var1, class_2818 var2) {
      try {
         if (!this.G(var1)) {
            return;
         }

         class_1923 var3 = getMc().field_1724.method_31476();
         class_1923 var4 = var2.method_12004();
         if (Math.abs((long)var4.field_9181 - var3.field_9181) > this.v() || Math.abs((long)var4.field_9180 - var3.field_9180) > this.v()) {
            return;
         }

         this.f.P();

         for (long var6 : this.f.U(var4.method_8324())) {
            class_2338 var8 = class_2338.method_10092(var6);
            this.T(var8, var2.method_8320(var8), y9.b.u);
         }

         int var10 = 0;

         for (class_2586 var7 : var2.method_12214().values()) {
            if (var10 == 256) {
               this.f.X(y9.b.u, var2.method_12214().size() - var10);
               break;
            }

            var10++;
            if (!var7.method_11015()) {
               this.T(var7.method_11016(), var7.method_11010(), y9.b.u);
            }
         }
      } catch (RuntimeException var9) {
         this.c(var9);
      }
   }

   public static void a(class_638 var0, class_2338 var1, class_2680 var2) {
      if (X != null && X.isEnabled()) {
         zu var3 = X;
         if (var3 != null) {
            try {
               if (!var3.G(var0)) {
                  return;
               }

               if (var0.method_2935().method_21730(var1.method_10263() >> 4, var1.method_10260() >> 4) == null) {
                  return;
               }

               var3.T(var1, var2, y9.b.E);
            } catch (RuntimeException var5) {
               var3.c(var5);
            }
         }
      }
   }

   private void c(RuntimeException var1) {
      this.v = (boolean)1;
      q8.O("BaseFinding", "Visibility diagnostic failed; observations paused until reset, native updates and Base Finder remain active", var1);
   }

   private void T(String var1) {
      q8.G("BaseFinding", "Visibility diagnostic [{}]: {}", var1, this.f.V());

      for (y9.w var3 : this.f.E(8)) {
         class_2338 var4 = class_2338.method_10092(var3.position());
         q8.G(
            "BaseFinding",
            "Visibility observation [{}, {}, {}] {} tick {}: {} -> {} ({})",
            var4.method_10263(),
            var4.method_10264(),
            var4.method_10260(),
            var3.source(),
            var3.tick(),
            var3.before(),
            var3.after(),
            var3.transition()
         );
      }
   }

   @Override
   public void onTick() {
      super.onTick();
      int var10000 = this.G(getMc().field_1687);
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         this.I++;
         long var12;
         var10000 = (var12 = this.I % 20L - 0L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         var10000 = Integer.signum(var3);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.f.H(this.I);
         }

         long var13;
         var10000 = (var13 = this.I % 200L - 0L) == 0L ? 0 : (var13 < 0L ? -1 : 1);
         boolean var6 = false;
         int var5 = var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.T("10 seconds");
         }
      }
   }

   @Override
   public String getStatus() {
      return super.getStatus() + (!this.i.getValue() ? "" : (this.v ? " · diagnostic paused" : " · diagnostic: " + this.f.V().tracked() + " observations"));
   }
}

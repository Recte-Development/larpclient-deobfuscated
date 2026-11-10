package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.gd;
import com.larpclient.gh;
import com.larpclient.i3;
import com.larpclient.q8;
import com.larpclient.ti;
import com.larpclient.u9;
import com.larpclient.wc;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_746;

public final class HitFXModule extends Module {
   private static HitFXModule L;
   private final li b = this.addSetting(
      new li(
         "Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom"
      )
   );
   private final l3 U = this.addSetting(new l3("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final lo f = this.addSetting(new lo("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private class_243 Y = class_243.field_1353;
   private final ti k = new ti();
   private final li G = this.addSetting(
      new li(
         "Particle Type",
         "Accent shape for Clean; the other presets choose their own shapes",
         "Star",
         "Spark",
         "Glow",
         "Circle",
         "Star",
         "Triangle",
         "Streak",
         "Textured"
      )
   );
   private final lo v = this.addSetting(new lo("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0));
   private final lo z = this.addSetting(new lo("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final lo e = this.addSetting(new lo("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final lo o = this.addSetting(new lo("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0));
   private final lo X = this.addSetting(new lo("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final l3 F = this.addSetting(new l3("Theme Colors", "Use the current theme palette", true));
   private final l0 R = this.addSetting(new l0("Color", "Start color", ez.a));
   private final l0 Q = this.addSetting(new l0("Gradient", "End color", ez.f));
   private final l3 K = this.addSetting(new l3("Bloom", "Submit glow mask", true));
   private final l3 A = this.addSetting(new l3("Particles", "Emit particles", true));
   private final l3 l = this.addSetting(new l3("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final lo g = this.addSetting(new lo("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final lo c = this.addSetting(new lo("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final l3 I = this.addSetting(new l3("Entity Hit Color", "Tint hurt entity models", false));
   private final l3 M = this.addSetting(new l3("Hit Trails", "Additional streaks on impact", true));
   private final lo p = this.addSetting(new lo("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final lo W = this.addSetting(X("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final lo y = this.addSetting(X("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final lo h = this.addSetting(X("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final lo N = this.addSetting(X("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final lo n = this.addSetting(X("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final lo S = this.addSetting(X("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final l3 O = this.addSetting(new l3("Bloom Color Animation", "Cycle each sprite through rainbow colors", false));
   private Object H;
   private Object D;
   private int q = -1;

   public boolean D() {
      return this.isEnabled() ? this.isOperational() : false;
   }

   public boolean K() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean v() {
      return !this.isEnabled() ? false : this.isOperational() && this.I.getValue();
   }

   public int m() {
      return this.F.getValue() ? ez.a : this.R.getValue();
   }

   private int k() {
      return this.F.getValue() ? ez.f : this.Q.getValue();
   }

   private static lo X(String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new lo(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.getValue());
         }
      };
   }

   public HitFXModule() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      L = this;

      for (lb var4 : new lb[]{this.g, this.c, this.p, this.f}) {
         var4.O(() -> this.l.getValue());
      }

      this.G.O(() -> this.A.getValue() && this.b.G("Clean"));

      for (lb var11 : new lb[]{this.v, this.z, this.e, this.M}) {
         var11.O(() -> this.A.getValue() && !this.b.G("Bloom"));
      }

      for (lb var12 : new lb[]{this.W, this.y, this.h, this.N, this.n, this.S, this.O}) {
         var12.O(() -> this.A.getValue() && this.b.G("Bloom"));
      }

      this.R.O(() -> !this.F.getValue());
      this.Q.O(() -> !this.F.getValue() && (this.l.getValue() || this.A.getValue() && !this.b.G("Bloom")));
      this.O.O(() -> !this.F.getValue() && this.A.getValue() && this.b.G("Bloom"));
      this.F.a(var0 -> q8.G("HitFX", "Theme colors: {}", var0));
      this.b.a(var0 -> q8.G("HitFX", "Preset changed: {}", var0));
   }

   public lb<?> H() {
      return this.A;
   }

   public static HitFXModule H() {
      return L;
   }

   public lb<?> h() {
      return this.b;
   }

   public List<lb<?>> e() {
      return List.of(this.b, this.A, this.G, this.v, this.z, this.e, this.M, this.W, this.y, this.h);
   }

   public List<lb<?>> r() {
      return List.of(this.l, this.g, this.c);
   }

   public List<lb<?>> H() {
      return List.of(this.F, this.R, this.Q, this.K, this.O);
   }

   public List<lb<?>> a() {
      return List.of(this.f, this.p, this.N, this.n, this.S, this.U, this.I, this.o, this.X);
   }

   public String K() {
      return switch ((String)this.b.getValue()) {
         case "Bloom" -> "Bloom: soft glowing sprites with tumbling drift, gravity and a smooth fade. Choose a fixed color or animated rainbow.";
         case "Energy" -> "Energy: bright energy flares, drifting light and a delayed second ring.";
         case "Spark" -> "Spark: a dense spray of narrow sparks and streaks that slow and fade around the hit.";
         case "Petal" -> "Petal: a cloud of gently turning petals with light sparks and a soft glow.";
         case "Cash" -> "Cash: tumbling $100 bills with printed fronts and backs, curved paper and a gentle falling flutter.";
         case "Heavy" -> "Heavy: chunky tumbling shards, stronger outward motion and a delayed second ring.";
         default -> "Clean: a full, balanced burst of colorful accents with light sparks. Particle Type changes its accent shape.";
      };
   }

   @Override
   public void onEnable() {
      this.H = this.D = null;
      this.q = -1;
      q8.G("HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.b.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.b.G("Bloom");
         boolean var3 = false;
         byte var2 = (byte)var10000;
         var10000 = Integer.signum(var2);
         if ((var10000 | -var10000) >>> 31 != 0) {
            class_746 var1 = this.getPlayer();
            if (this.H != var1 || this.D != getMc().field_1687) {
               this.H = var1;
               this.D = getMc().field_1687;
               this.q = -1;
            }

            byte var5 = 10;
            int var4 = var1.field_6235;
            var10000 = Integer.compare(var4, var5);
            if ((var10000 | -var10000) >>> 31 == 0) {
               int var7 = this.q;
               int var6 = var1.field_6012;
               var10000 = Integer.compare(var6, var7);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.q = var1.field_6012;
                  this.u(var1);
               }
            }

            return;
         }
      }

      this.H = this.D = null;
      this.q = -1;
   }

   public void u(class_1309 var1) {
      if (this.isEnabled()) {
         class_746 var2 = this.getPlayer();
         class_243 var3 = var2 != null && var2 != var1 ? var2.method_33571() : var1.method_5829().method_1005();
         class_243 var4 = var2 != null && var2 != var1 ? var2.method_5828(1.0F) : new class_243(0.0, 0.0, 1.0);
         gh var5 = gh.sample(var1.method_5829(), var3, var4, (var0, var1x) -> i3.c(getMc().field_1687, var0, var1x));
         this.G(wc.i, var1.method_73189(), var1.method_17681(), var1.method_17682(), var5);
      }
   }

   public void K(gd var1, class_243 var2, double var3, double var5, class_243 var7) {
      if (this.isEnabled()) {
         this.G(var1, var2, var3, var5, new gh(var2.method_1031(0.0, var5 * 0.5, 0.0), var7, var2.method_1031(0.0, 0.035, 0.0), new class_243(0.0, 1.0, 0.0)));
      }
   }

   public void G(gd var1, class_243 var2, double var3, double var5, gh var7) {
      if (this.isEnabled()) {
         this.Y = var7.normal();
         this.k.f = this.o.getValue().intValue();
         this.k.k = this.X.getValue();
         class_243 var8 = var7.point();
         this.k.t = 1;
         this.k.z = ti.a.g;
         this.k.E = this.k.h = Math.max(0.08, var3 * 0.45);
         this.k.A = Math.min(0.4, var5 * 0.22);
         this.k.l = this.m();
         this.k.b = this.k();
         this.k.a = this.K.getValue();
         this.k.J = 0.0;
         this.k.K = ti.y.w;
         this.k.S = var7.groundY();
         this.k.m = var7.ringNormal();
         this.k.T = this.k.V = (boolean)0;
         this.k.H = 1.0;
         this.k.P = Double.NaN;
         double var9 = this.v.getValue() / 18.0;
         double var11 = this.z.getValue() / 0.08;
         double var13 = this.e.getValue() / 0.8;
         boolean var15 = this.b.G("Heavy");
         boolean var16 = this.b.G("Petal");
         boolean var17 = this.b.G("Energy");
         boolean var18 = this.b.G("Spark");
         this.k.p = var17 ? ti.i.R : ti.i.P;
         if (this.A.getValue() && this.b.G("Bloom")) {
            this.k.p = ti.i.j;
            this.k.z = ti.a.G;
            this.k.Q = gd.h.f;
            this.k.G = this.W.getValue().intValue();
            this.k.M = this.k.F = this.y.getValue() * 0.5;
            this.k.i = this.k.B = this.h.getValue();
            this.k.l = this.k.b = this.m();
            this.k.a = this.K.getValue();
            this.k.x = ti.d.l;
            this.k.O = this.N.getValue() * 20.0;
            this.k.r = 1.0;
            this.k.d = 0.0;
            this.k.R = this.n.getValue() * 400.0;
            this.k.j = -Math.log(0.98) * 20.0;
            this.k.o = this.k.g = this.k.c = 0.0;
            this.k.v = this.k.y = this.k.X = 0.0;
            this.k.T = (boolean)1;
            this.k.V = (boolean)(!this.F.getValue() && this.O.getValue() ? 1 : 0);
            this.k.D = u9.u.W;
            this.k.H = this.S.getValue();
            this.k.W = u9.o.d;
            this.k.P = Math.toRadians(20.0);
            this.k.w = Math.toRadians(80.0);
            var1.V(var2.method_1031(0.0, var5 * 0.5 + 0.1, 0.0), this.k);
         } else if (this.A.getValue() && this.b.G("Cash")) {
            this.Z(var1, var8, gd.h.m, 18.0 * var9, 0.17 * var11, 0.25 * var11, 0.9 * var13, 1.5 * var13, 1.25, 1.15, 0.22, ti.d.G);
         } else if (this.A.getValue()) {
            this.Z(
               var1,
               var8,
               gd.h.f,
               (var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               ti.d.l
            );
            this.Z(
               var1,
               var8,
               gd.h.P,
               (var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               ti.d.l
            );
            this.Z(
               var1,
               var8,
               var16 ? gd.h.e : (var15 ? gd.h.n : (var17 ? gd.h.t : (var18 ? gd.h.E : gd.h.valueOf(this.G.getValue())))),
               (var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               ti.d.G
            );
         }

         if (this.A.getValue() && this.M.getValue() && !this.b.G("Bloom")) {
            this.Z(var1, var8, gd.h.E, 6.0 * var9, 0.025 * var11, 0.055 * var11, 0.25 * var13, 0.5 * var13, 3.0, 0.1, 0.0, ti.d.l);
         }

         if (this.l.getValue()) {
            this.k.T = this.k.V = (boolean)0;
            this.k.K = ti.y.w;
            this.k.J = 0.0;
            this.k.H = 1.0;
            this.k.P = Double.NaN;
            this.k.p = var17 ? ti.i.R : ti.i.P;
            this.k.l = this.m();
            this.k.b = this.k();
            this.k.a = this.K.getValue();
            this.k.z = ti.a.G;
            this.k.Q = gd.h.d;
            this.k.G = 1;
            this.k.i = this.k.B = this.p.getValue();
            this.k.Z = this.g.getValue() * this.c.getValue();
            this.k.I = this.f.getValue();
            this.k.x = ti.d.l;
            this.k.D = u9.u.i;
            var1.V(var7.ring(), this.k);
            if (var15 || var17) {
               this.k.J = 0.075;
               this.k.Z *= 1.25;
               var1.V(var7.ring().method_1019(var7.ringNormal().method_1021(0.005)), this.k);
            }
         }

         if (this.U.getValue()) {
            cf.I(this.m());
         }
      }
   }

   private void Z(
      gd var1,
      class_243 var2,
      gd.h var3,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      ti.d var20
   ) {
      int var21 = var3 != gd.h.P && var3 != gd.h.E ? 0 : 1;
      int var22 = var3 == gd.h.f ? 1 : 0;
      this.k.K = var3 == gd.h.n ? ti.y.u : ti.y.w;
      this.k.J = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.k.z = var22 != 0 ? ti.a.g : ti.a.L;
      this.k.d = var3 == gd.h.m ? 0.65 : (var3 == gd.h.e ? 0.35 : 0.15);
      this.k.l = var21 != 0 ? ei.W(this.m(), -1, 0.65) : ei.D(this.m(), var22 != 0 ? 0.52 : 0.92);
      this.k.b = ei.D(this.k(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == gd.h.m) {
         this.k.l = this.k.b = -1;
      }

      this.k.a = (boolean)(this.K.getValue() && var20 == ti.d.l ? 1 : 0);
      this.k.r = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != gd.h.P && var3 != gd.h.E ? 0.0 : var14 * 0.95;
      this.k.o = this.Y.field_1352 * var23;
      this.k.g = this.Y.field_1351 * var23;
      this.k.c = this.Y.field_1350 * var23;
      this.k.Q = var3;
      this.k.G = (int)Math.round(var4);
      this.k.M = var6;
      this.k.F = var8;
      this.k.i = var10;
      this.k.B = var12;
      this.k.O = var21 != 0 ? var14 * 0.3 : var14;
      this.k.R = var16;
      this.k.v = var18;
      this.k.x = var20;
      this.k.j = var21 != 0 ? 2.4 : (var3 == gd.h.e ? 0.8 : 1.4);
      this.k.w = var3 == gd.h.m ? 3.2 : (var3 == gd.h.e ? 2.1 : (var3 == gd.h.n ? 5.0 : 3.0));
      this.k.D = var22 != 0 ? u9.u.i : u9.u.b;
      this.k.W = var22 != 0 ? u9.o.I : (var21 != 0 ? u9.o.D : u9.o.d);
      var1.V(var2, this.k);
   }

   @Override
   public void onDisable() {
      this.H = this.D = null;
      this.q = -1;
      wc.i.S(1);
   }
}

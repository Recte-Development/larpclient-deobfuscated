package com.larpclient.module;

import com.larpclient.et;
import com.larpclient.f4;
import com.larpclient.f8;
import com.larpclient.fk;
import com.larpclient.hq;
import com.larpclient.mu;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.yc;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class HitFXModule extends Module {
   private static HitFXModule T;
   private final lp i = this.addSetting(
      new lp(
         "Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom"
      )
   );
   private final ln c = this.addSetting(new ln("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final lc y = this.addSetting(new lc("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private Vec3 j = Vec3.ZERO;
   private final fk l = new fk();
   private final lp R = this.addSetting(
      new lp(
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
   private final lc w = this.addSetting(new lc("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0));
   private final lc r = this.addSetting(new lc("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final lc B = this.addSetting(new lc("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final lc z = this.addSetting(new lc("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0));
   private final lc C = this.addSetting(new lc("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final ln H = this.addSetting(new ln("Theme Colors", "Use the current theme palette", true));
   private final lv W = this.addSetting(new lv("Color", "Start color", rv.e));
   private final lv f = this.addSetting(new lv("Gradient", "End color", rv.GQ));
   private final ln O = this.addSetting(new ln("Bloom", "Submit glow mask", true));
   private final ln q = this.addSetting(new ln("Particles", "Emit particles", true));
   private final ln M = this.addSetting(new ln("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final lc E = this.addSetting(new lc("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final lc Q = this.addSetting(new lc("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final ln v = this.addSetting(new ln("Entity Hit Color", "Tint hurt entity models", false));
   private final ln m = this.addSetting(new ln("Hit Trails", "Additional streaks on impact", true));
   private final lc o = this.addSetting(new lc("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final lc S = this.addSetting(K("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final lc Z = this.addSetting(K("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final lc L = this.addSetting(K("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final lc b = this.addSetting(K("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final lc a = this.addSetting(K("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final lc F = this.addSetting(K("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final ln Y = this.addSetting(new ln("Bloom Color Animation", "Cycle each sprite through rainbow colors", false));
   private Object p;
   private Object K;
   private int k = -1;

   public boolean b() {
      return this.isOperational() && this.v.getValue();
   }

   public int p() {
      return this.H.getValue() ? rv.e : this.W.getValue();
   }

   private int j() {
      return this.H.getValue() ? rv.GQ : this.f.getValue();
   }

   private static lc K(String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new lc(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.getValue());
         }
      };
   }

   public HitFXModule() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      T = this;

      for (l6 var4 : new l6[]{this.E, this.Q, this.o, this.y}) {
         var4.x(() -> this.M.getValue());
      }

      this.R.x(() -> this.q.getValue() && this.i.i("Clean"));

      for (l6 var11 : new l6[]{this.w, this.r, this.B, this.m}) {
         var11.x(() -> this.q.getValue() && !this.i.i("Bloom"));
      }

      for (l6 var12 : new l6[]{this.S, this.Z, this.L, this.b, this.a, this.F, this.Y}) {
         var12.x(() -> this.q.getValue() && this.i.i("Bloom"));
      }

      this.W.x(() -> !this.H.getValue());
      this.f.x(() -> !this.H.getValue() && (this.M.getValue() || this.q.getValue() && !this.i.i("Bloom")));
      this.Y.x(() -> !this.H.getValue() && this.q.getValue() && this.i.i("Bloom"));
      this.H.W(var0 -> r4.V("HitFX", "Theme colors: {}", var0));
      this.i.W(var0 -> r4.V("HitFX", "Preset changed: {}", var0));
   }

   public l6<?> y() {
      return this.q;
   }

   public static HitFXModule y() {
      return T;
   }

   public l6<?> G() {
      return this.i;
   }

   public List<l6<?>> m() {
      return List.of(this.i, this.q, this.R, this.w, this.r, this.B, this.m, this.S, this.Z, this.L);
   }

   public List<l6<?>> T() {
      return List.of(this.M, this.E, this.Q);
   }

   public List<l6<?>> O() {
      return List.of(this.H, this.W, this.f, this.O, this.Y);
   }

   public List<l6<?>> p() {
      return List.of(this.y, this.o, this.b, this.a, this.F, this.c, this.v, this.z, this.C);
   }

   public String H() {
      return switch ((String)this.i.getValue()) {
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
      this.p = this.K = null;
      this.k = -1;
      r4.V("HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.i.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.i.i("Bloom");
         boolean var3 = false;
         byte var2 = (byte)var10000;
         var10000 = Integer.signum(var2);
         if ((var10000 | -var10000) >>> 31 != 0) {
            LocalPlayer var1 = this.getPlayer();
            if (this.p != var1 || this.K != getMc().level) {
               this.p = var1;
               this.K = getMc().level;
               this.k = -1;
            }

            byte var5 = 10;
            int var4 = var1.hurtTime;
            var10000 = Integer.compare(var4, var5);
            if ((var10000 | -var10000) >>> 31 == 0) {
               int var7 = this.k;
               int var6 = var1.tickCount;
               var10000 = Integer.compare(var6, var7);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.k = var1.tickCount;
                  this.S(var1);
               }
            }

            return;
         }
      }

      this.p = this.K = null;
      this.k = -1;
   }

   public void S(LivingEntity var1) {
      if (this.isEnabled()) {
         if (!HalloweenModule.p()) {
            LocalPlayer var2 = this.getPlayer();
            Vec3 var3 = var2 != null && var2 != var1 ? var2.getEyePosition() : var1.getBoundingBox().getCenter();
            Vec3 var4 = var2 != null && var2 != var1 ? var2.getViewVector(1.0F) : new Vec3(0.0, 0.0, 1.0);
            hq var5 = hq.sample(var1.getBoundingBox(), var3, var4, (var0, var1x) -> et.Q(getMc().level, var0, var1x));
            this.r(f8.D, var1.position(), var1.getBbWidth(), var1.getBbHeight(), var5);
         }
      }
   }

   public void r(f4 var1, Vec3 var2, double var3, double var5, Vec3 var7) {
      if (this.isEnabled()) {
         this.r(var1, var2, var3, var5, new hq(var2.add(0.0, var5 * 0.5, 0.0), var7, var2.add(0.0, 0.035, 0.0), new Vec3(0.0, 1.0, 0.0)));
      }
   }

   public void r(f4 var1, Vec3 var2, double var3, double var5, hq var7) {
      if (this.isEnabled()) {
         this.j = var7.normal();
         this.l.p = this.z.getValue().intValue();
         this.l.Q = this.C.getValue();
         Vec3 var8 = var7.point();
         this.l.g = 1;
         this.l.m = fk.y.D;
         this.l.E = this.l.j = Math.max(0.08, var3 * 0.45);
         this.l.r = Math.min(0.4, var5 * 0.22);
         this.l.B = this.p();
         this.l.v = this.j();
         this.l.G = this.O.getValue();
         this.l.c = 0.0;
         this.l.f = fk.q.k;
         this.l.q = var7.groundY();
         this.l.S = var7.ringNormal();
         this.l.M = this.l.i = (boolean)0;
         this.l.Z = 1.0;
         this.l.w = Double.NaN;
         double var9 = this.w.getValue() / 18.0;
         double var11 = this.r.getValue() / 0.08;
         double var13 = this.B.getValue() / 0.8;
         boolean var15 = this.i.i("Heavy");
         boolean var16 = this.i.i("Petal");
         boolean var17 = this.i.i("Energy");
         boolean var18 = this.i.i("Spark");
         this.l.I = var17 ? fk.w.K : fk.w.t;
         if (this.q.getValue() && this.i.i("Bloom")) {
            this.l.I = fk.w.Q;
            this.l.m = fk.y.P;
            this.l.D = f4.a.u;
            this.l.t = this.S.getValue().intValue();
            this.l.V = this.l.u = this.Z.getValue() * 0.5;
            this.l.Y = this.l.d = this.L.getValue();
            this.l.B = this.l.v = this.p();
            this.l.G = this.O.getValue();
            this.l.C = fk.h.u;
            this.l.e = this.b.getValue() * 20.0;
            this.l.F = 1.0;
            this.l.k = 0.0;
            this.l.H = this.a.getValue() * 400.0;
            this.l.b = -Math.log(0.98) * 20.0;
            this.l.h = this.l.x = this.l.W = 0.0;
            this.l.s = this.l.L = this.l.n = 0.0;
            this.l.M = (boolean)1;
            this.l.i = (boolean)(!this.H.getValue() && this.Y.getValue() ? 1 : 0);
            this.l.N = yc.d.u;
            this.l.Z = this.F.getValue();
            this.l.U = yc.y.W;
            this.l.w = Math.toRadians(20.0);
            this.l.y = Math.toRadians(80.0);
            var1.p(var2.add(0.0, var5 * 0.5 + 0.1, 0.0), this.l);
         } else if (this.q.getValue() && this.i.i("Cash")) {
            this.v(var1, var8, f4.a.h, 18.0 * var9, 0.17 * var11, 0.25 * var11, 0.9 * var13, 1.5 * var13, 1.25, 1.15, 0.22, fk.h.z);
         } else if (this.q.getValue()) {
            this.v(
               var1,
               var8,
               f4.a.u,
               (var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               fk.h.u
            );
            this.v(
               var1,
               var8,
               f4.a.d,
               (var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               fk.h.u
            );
            this.v(
               var1,
               var8,
               var16 ? f4.a.x : (var15 ? f4.a.y : (var17 ? f4.a.n : (var18 ? f4.a.b : f4.a.valueOf(this.R.getValue())))),
               (var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               fk.h.z
            );
         }

         if (this.q.getValue() && this.m.getValue() && !this.i.i("Bloom")) {
            this.v(var1, var8, f4.a.b, 6.0 * var9, 0.025 * var11, 0.055 * var11, 0.25 * var13, 0.5 * var13, 3.0, 0.1, 0.0, fk.h.u);
         }

         if (this.M.getValue()) {
            this.l.M = this.l.i = (boolean)0;
            this.l.f = fk.q.k;
            this.l.c = 0.0;
            this.l.Z = 1.0;
            this.l.w = Double.NaN;
            this.l.I = var17 ? fk.w.K : fk.w.t;
            this.l.B = this.p();
            this.l.v = this.j();
            this.l.G = this.O.getValue();
            this.l.m = fk.y.P;
            this.l.D = f4.a.U;
            this.l.t = 1;
            this.l.Y = this.l.d = this.o.getValue();
            this.l.o = this.E.getValue() * this.Q.getValue();
            this.l.z = this.y.getValue();
            this.l.C = fk.h.u;
            this.l.N = yc.d.i;
            var1.p(var7.ring(), this.l);
            if (var15 || var17) {
               this.l.c = 0.075;
               this.l.o *= 1.25;
               var1.p(var7.ring().add(var7.ringNormal().scale(0.005)), this.l);
            }
         }

         if (this.c.getValue()) {
            mu.T(this.p());
         }
      }
   }

   private void v(
      f4 var1, Vec3 var2, f4.a var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, fk.h var20
   ) {
      int var21 = var3 != f4.a.d && var3 != f4.a.b ? 0 : 1;
      int var22 = var3 == f4.a.u ? 1 : 0;
      this.l.f = var3 == f4.a.y ? fk.q.j : fk.q.k;
      this.l.c = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.l.m = var22 != 0 ? fk.y.D : fk.y.u;
      this.l.k = var3 == f4.a.h ? 0.65 : (var3 == f4.a.x ? 0.35 : 0.15);
      this.l.B = var21 != 0 ? oh.j(this.p(), -1, 0.65) : oh.i(this.p(), var22 != 0 ? 0.52 : 0.92);
      this.l.v = oh.i(this.j(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == f4.a.h) {
         this.l.B = this.l.v = -1;
      }

      this.l.G = (boolean)(this.O.getValue() && var20 == fk.h.u ? 1 : 0);
      this.l.F = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != f4.a.d && var3 != f4.a.b ? 0.0 : var14 * 0.95;
      this.l.h = this.j.x * var23;
      this.l.x = this.j.y * var23;
      this.l.W = this.j.z * var23;
      this.l.D = var3;
      this.l.t = (int)Math.round(var4);
      this.l.V = var6;
      this.l.u = var8;
      this.l.Y = var10;
      this.l.d = var12;
      this.l.e = var21 != 0 ? var14 * 0.3 : var14;
      this.l.H = var16;
      this.l.s = var18;
      this.l.C = var20;
      this.l.b = var21 != 0 ? 2.4 : (var3 == f4.a.x ? 0.8 : 1.4);
      this.l.y = var3 == f4.a.h ? 3.2 : (var3 == f4.a.x ? 2.1 : (var3 == f4.a.y ? 5.0 : 3.0));
      this.l.N = var22 != 0 ? yc.d.i : yc.d.R;
      this.l.U = var22 != 0 ? yc.y.T : (var21 != 0 ? yc.y.J : yc.y.W);
      var1.p(var2, this.l);
   }

   @Override
   public void onDisable() {
      this.p = this.K = null;
      this.k = -1;
      f8.D.A(1);
   }
}

package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.cg;
import com.larpclient.cj;
import com.larpclient.eb;
import com.larpclient.h;
import com.larpclient.hp;
import com.larpclient.mt;
import com.larpclient.pf;
import com.larpclient.w8;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class HitFXModule extends Module {
   private static HitFXModule g;
   private final d6 R = this.addSetting(
      new d6(
         "Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom"
      )
   );
   private final dh Q = this.addSetting(new dh("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final d9 B = this.addSetting(new d9("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private Vec3 p = Vec3.ZERO;
   private final pf z = new pf();
   private final d6 Z = this.addSetting(
      new d6(
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
   private final d9 K = this.addSetting(new d9("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0));
   private final d9 j = this.addSetting(new d9("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final d9 x = this.addSetting(new d9("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final d9 w = this.addSetting(new d9("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0));
   private final d9 V = this.addSetting(new d9("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final dh Y = this.addSetting(new dh("Theme Colors", "Use the current theme palette", true));
   private final dk u = this.addSetting(new dk("Color", "Start color", com.larpclient.f.C));
   private final dk e = this.addSetting(new dk("Gradient", "End color", com.larpclient.f.O));
   private final dh h = this.addSetting(new dh("Bloom", "Submit glow mask", true));
   private final dh v = this.addSetting(new dh("Particles", "Emit particles", true));
   private final dh G = this.addSetting(new dh("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final d9 N = this.addSetting(new d9("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final d9 n = this.addSetting(new d9("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final dh c = this.addSetting(new dh("Entity Hit Color", "Tint hurt entity models", false));
   private final dh a = this.addSetting(new dh("Hit Trails", "Additional streaks on impact", true));
   private final d9 A = this.addSetting(new d9("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final d9 T = this.addSetting(A("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final d9 H = this.addSetting(A("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final d9 L = this.addSetting(A("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final d9 f = this.addSetting(A("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final d9 o = this.addSetting(A("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final d9 O = this.addSetting(A("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final dh U = this.addSetting(new dh("Bloom Color Animation", "Cycle each sprite through rainbow colors", false));
   private Object D;
   private Object S;
   private int k = -1;

   public boolean q() {
      return this.isOperational() && this.c.P();
   }

   public int h() {
      return this.Y.P() ? com.larpclient.f.C : this.u.P();
   }

   private int V() {
      return this.Y.P() ? com.larpclient.f.O : this.e.P();
   }

   private static d9 A(String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new d9(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.P());
         }
      };
   }

   public HitFXModule() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      g = this;

      for (df var4 : new df[]{this.N, this.n, this.A, this.B}) {
         var4.H(() -> this.G.P());
      }

      this.Z.H(() -> this.v.P() && this.R.S("Clean"));

      for (df var11 : new df[]{this.K, this.j, this.x, this.a}) {
         var11.H(() -> this.v.P() && !this.R.S("Bloom"));
      }

      for (df var12 : new df[]{this.T, this.H, this.L, this.f, this.o, this.O, this.U}) {
         var12.H(() -> this.v.P() && this.R.S("Bloom"));
      }

      this.u.H(() -> !this.Y.P());
      this.e.H(() -> !this.Y.P() && (this.G.P() || this.v.P() && !this.R.S("Bloom")));
      this.U.H(() -> !this.Y.P() && this.v.P() && this.R.S("Bloom"));
      this.Y.h(var0 -> mt.K("HitFX", "Theme colors: {}", var0));
      this.R.h(var0 -> mt.K("HitFX", "Preset changed: {}", var0));
   }

   public df<?> y() {
      return this.v;
   }

   public static HitFXModule q() {
      return g;
   }

   public df<?> G() {
      return this.R;
   }

   public List<df<?>> S() {
      return List.of(this.R, this.v, this.Z, this.K, this.j, this.x, this.a, this.T, this.H, this.L);
   }

   public List<df<?>> N() {
      return List.of(this.G, this.N, this.n);
   }

   public List<df<?>> o() {
      return List.of(this.Y, this.u, this.e, this.h, this.U);
   }

   public List<df<?>> e() {
      return List.of(this.B, this.A, this.f, this.o, this.O, this.Q, this.c, this.w, this.V);
   }

   public String C() {
      return switch ((String)this.R.P()) {
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
      this.D = this.S = null;
      this.k = -1;
      mt.K("HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.R.P());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.R.S("Bloom");
         boolean var3 = false;
         byte var2 = (byte)var10000;
         var10000 = Integer.signum(var2);
         if ((var10000 | -var10000) >>> 31 != 0) {
            LocalPlayer var1 = this.getPlayer();
            if (this.D != var1 || this.S != getMc().level) {
               this.D = var1;
               this.S = getMc().level;
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
                  this.v(var1);
               }
            }

            return;
         }
      }

      this.D = this.S = null;
      this.k = -1;
   }

   public void v(LivingEntity var1) {
      if (this.isEnabled()) {
         if (!HalloweenModule.S()) {
            LocalPlayer var2 = this.getPlayer();
            Vec3 var3 = var2 != null && var2 != var1 ? var2.getEyePosition() : var1.getBoundingBox().getCenter();
            Vec3 var4 = var2 != null && var2 != var1 ? var2.getViewVector(1.0F) : new Vec3(0.0, 0.0, 1.0);
            cj var5 = cj.sample(var1.getBoundingBox(), var3, var4, (var0, var1x) -> w8.G(getMc().level, var0, var1x));
            this.k(com.larpclient.h.P, var1.position(), var1.getBbWidth(), var1.getBbHeight(), var5);
         }
      }
   }

   public void Z(cg var1, Vec3 var2, double var3, double var5, Vec3 var7) {
      if (this.isEnabled()) {
         this.k(var1, var2, var3, var5, new cj(var2.add(0.0, var5 * 0.5, 0.0), var7, var2.add(0.0, 0.035, 0.0), new Vec3(0.0, 1.0, 0.0)));
      }
   }

   public void k(cg var1, Vec3 var2, double var3, double var5, cj var7) {
      if (this.isEnabled()) {
         this.p = var7.normal();
         this.z.X = this.w.P().intValue();
         this.z.j = this.V.P();
         Vec3 var8 = var7.point();
         this.z.D = 1;
         this.z.A = pf.r.Y;
         this.z.F = this.z.U = Math.max(0.08, var3 * 0.45);
         this.z.I = Math.min(0.4, var5 * 0.22);
         this.z.E = this.h();
         this.z.N = this.V();
         this.z.i = this.h.P();
         this.z.t = 0.0;
         this.z.c = pf.l.e;
         this.z.n = var7.groundY();
         this.z.V = var7.ringNormal();
         this.z.w = this.z.Q = (boolean)0;
         this.z.H = 1.0;
         this.z.h = Double.NaN;
         double var9 = this.K.P() / 18.0;
         double var11 = this.j.P() / 0.08;
         double var13 = this.x.P() / 0.8;
         boolean var15 = this.R.S("Heavy");
         boolean var16 = this.R.S("Petal");
         boolean var17 = this.R.S("Energy");
         boolean var18 = this.R.S("Spark");
         this.z.R = var17 ? pf.t.B : pf.t.G;
         if (this.v.P() && this.R.S("Bloom")) {
            this.z.R = pf.t.c;
            this.z.A = pf.r.l;
            this.z.P = cg.u.a;
            this.z.g = this.T.P().intValue();
            this.z.C = this.z.d = this.H.P() * 0.5;
            this.z.M = this.z.O = this.L.P();
            this.z.E = this.z.N = this.h();
            this.z.i = this.h.P();
            this.z.p = pf.x.E;
            this.z.Z = this.f.P() * 20.0;
            this.z.b = 1.0;
            this.z.r = 0.0;
            this.z.W = this.o.P() * 400.0;
            this.z.q = -Math.log(0.98) * 20.0;
            this.z.z = this.z.K = this.z.v = 0.0;
            this.z.f = this.z.x = this.z.J = 0.0;
            this.z.w = (boolean)1;
            this.z.Q = (boolean)(!this.Y.P() && this.U.P() ? 1 : 0);
            this.z.S = hp.y.B;
            this.z.H = this.O.P();
            this.z.l = hp.u.F;
            this.z.h = Math.toRadians(20.0);
            this.z.e = Math.toRadians(80.0);
            var1.i(var2.add(0.0, var5 * 0.5 + 0.1, 0.0), this.z);
         } else if (this.v.P() && this.R.S("Cash")) {
            this.W(var1, var8, cg.u.f, 18.0 * var9, 0.17 * var11, 0.25 * var11, 0.9 * var13, 1.5 * var13, 1.25, 1.15, 0.22, pf.x.S);
         } else if (this.v.P()) {
            this.W(
               var1,
               var8,
               cg.u.a,
               (var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               pf.x.E
            );
            this.W(
               var1,
               var8,
               cg.u.M,
               (var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               pf.x.E
            );
            this.W(
               var1,
               var8,
               var16 ? cg.u.x : (var15 ? cg.u.z : (var17 ? cg.u.u : (var18 ? cg.u.B : cg.u.valueOf(this.Z.P())))),
               (var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               pf.x.S
            );
         }

         if (this.v.P() && this.a.P() && !this.R.S("Bloom")) {
            this.W(var1, var8, cg.u.B, 6.0 * var9, 0.025 * var11, 0.055 * var11, 0.25 * var13, 0.5 * var13, 3.0, 0.1, 0.0, pf.x.E);
         }

         if (this.G.P()) {
            this.z.w = this.z.Q = (boolean)0;
            this.z.c = pf.l.e;
            this.z.t = 0.0;
            this.z.H = 1.0;
            this.z.h = Double.NaN;
            this.z.R = var17 ? pf.t.B : pf.t.G;
            this.z.E = this.h();
            this.z.N = this.V();
            this.z.i = this.h.P();
            this.z.A = pf.r.l;
            this.z.P = cg.u.p;
            this.z.g = 1;
            this.z.M = this.z.O = this.A.P();
            this.z.B = this.N.P() * this.n.P();
            this.z.L = this.B.P();
            this.z.p = pf.x.E;
            this.z.S = hp.y.M;
            var1.i(var7.ring(), this.z);
            if (var15 || var17) {
               this.z.t = 0.075;
               this.z.B *= 1.25;
               var1.i(var7.ring().add(var7.ringNormal().scale(0.005)), this.z);
            }
         }

         if (this.Q.P()) {
            eb.C(this.h());
         }
      }
   }

   private void W(
      cg var1, Vec3 var2, cg.u var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, pf.x var20
   ) {
      int var21 = var3 != cg.u.M && var3 != cg.u.B ? 0 : 1;
      int var22 = var3 == cg.u.a ? 1 : 0;
      this.z.c = var3 == cg.u.z ? pf.l.I : pf.l.e;
      this.z.t = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.z.A = var22 != 0 ? pf.r.Y : pf.r.T;
      this.z.r = var3 == cg.u.f ? 0.65 : (var3 == cg.u.x ? 0.35 : 0.15);
      this.z.E = var21 != 0 ? _3.D(this.h(), -1, 0.65) : _3.A(this.h(), var22 != 0 ? 0.52 : 0.92);
      this.z.N = _3.A(this.V(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == cg.u.f) {
         this.z.E = this.z.N = -1;
      }

      this.z.i = (boolean)(this.h.P() && var20 == pf.x.E ? 1 : 0);
      this.z.b = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != cg.u.M && var3 != cg.u.B ? 0.0 : var14 * 0.95;
      this.z.z = this.p.x * var23;
      this.z.K = this.p.y * var23;
      this.z.v = this.p.z * var23;
      this.z.P = var3;
      this.z.g = (int)Math.round(var4);
      this.z.C = var6;
      this.z.d = var8;
      this.z.M = var10;
      this.z.O = var12;
      this.z.Z = var21 != 0 ? var14 * 0.3 : var14;
      this.z.W = var16;
      this.z.f = var18;
      this.z.p = var20;
      this.z.q = var21 != 0 ? 2.4 : (var3 == cg.u.x ? 0.8 : 1.4);
      this.z.e = var3 == cg.u.f ? 3.2 : (var3 == cg.u.x ? 2.1 : (var3 == cg.u.z ? 5.0 : 3.0));
      this.z.S = var22 != 0 ? hp.y.M : hp.y.H;
      this.z.l = var22 != 0 ? hp.u.Q : (var21 != 0 ? hp.u.C : hp.u.F);
      var1.i(var2, this.z);
   }

   @Override
   public void onDisable() {
      this.D = this.S = null;
      this.k = -1;
      com.larpclient.h.P.b(1);
   }
}

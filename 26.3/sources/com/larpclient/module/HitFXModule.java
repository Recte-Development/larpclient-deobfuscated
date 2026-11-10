package com.larpclient.module;

import com.larpclient._0;
import com.larpclient._q;
import com.larpclient.br;
import com.larpclient.pr;
import com.larpclient.qk;
import com.larpclient.v9;
import com.larpclient.vv;
import com.larpclient.wp;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class HitFXModule extends Module {
   private static HitFXModule y;
   private final o t = this.addSetting(
      new o("Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom")
   );
   private final m G = this.addSetting(new m("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final b D = this.addSetting(new b("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private Vec3 q = Vec3.ZERO;
   private final _0 b = new _0();
   private final o J = this.addSetting(
      new o(
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
   private final b Y = this.addSetting(new b("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0));
   private final b f = this.addSetting(new b("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final b N = this.addSetting(new b("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final b W = this.addSetting(new b("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0));
   private final b K = this.addSetting(new b("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final m d = this.addSetting(new m("Theme Colors", "Use the current theme palette", true));
   private final h F = this.addSetting(new h("Color", "Start color", com.larpclient.c.y));
   private final h u = this.addSetting(new h("Gradient", "End color", com.larpclient.c.t));
   private final m w = this.addSetting(new m("Bloom", "Submit glow mask", true));
   private final m R = this.addSetting(new m("Particles", "Emit particles", true));
   private final m A = this.addSetting(new m("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final b k = this.addSetting(new b("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final b s = this.addSetting(new b("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final m C = this.addSetting(new m("Entity Hit Color", "Tint hurt entity models", false));
   private final m h = this.addSetting(new m("Hit Trails", "Additional streaks on impact", true));
   private final b n = this.addSetting(new b("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final b O = this.addSetting(U("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final b Z = this.addSetting(U("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final b o = this.addSetting(U("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final b r = this.addSetting(U("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final b X = this.addSetting(U("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final b L = this.addSetting(U("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final m E = this.addSetting(new m("Bloom Color Animation", "Cycle each sprite through rainbow colors", false));
   private Object I;
   private Object p;
   private int c = -1;

   public boolean A() {
      return this.isEnabled() ? this.isOperational() : false;
   }

   public boolean b() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean T() {
      return !this.isEnabled() ? false : this.isOperational() && this.C.getValue();
   }

   public int i() {
      return this.d.getValue() ? com.larpclient.c.y : this.F.getValue();
   }

   private int z() {
      return this.d.getValue() ? com.larpclient.c.t : this.u.getValue();
   }

   private static b U(String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new b(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.getValue());
         }
      };
   }

   public HitFXModule() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      y = this;

      for (r var4 : new r[]{this.k, this.s, this.n, this.D}) {
         var4.a(() -> this.A.getValue());
      }

      this.J.a(() -> this.R.getValue() && this.t.w("Clean"));

      for (r var11 : new r[]{this.Y, this.f, this.N, this.h}) {
         var11.a(() -> this.R.getValue() && !this.t.w("Bloom"));
      }

      for (r var12 : new r[]{this.O, this.Z, this.o, this.r, this.X, this.L, this.E}) {
         var12.a(() -> this.R.getValue() && this.t.w("Bloom"));
      }

      this.F.a(() -> !this.d.getValue());
      this.u.a(() -> !this.d.getValue() && (this.A.getValue() || this.R.getValue() && !this.t.w("Bloom")));
      this.E.a(() -> !this.d.getValue() && this.R.getValue() && this.t.w("Bloom"));
      this.d.w(var0 -> _q.z("HitFX", "Theme colors: {}", var0));
      this.t.w(var0 -> _q.z("HitFX", "Preset changed: {}", var0));
   }

   public r<?> J() {
      return this.R;
   }

   public static HitFXModule U() {
      return y;
   }

   public r<?> G() {
      return this.t;
   }

   public List<r<?>> O() {
      return List.of(this.t, this.R, this.J, this.Y, this.f, this.N, this.h, this.O, this.Z, this.o);
   }

   public List<r<?>> I() {
      return List.of(this.A, this.k, this.s);
   }

   public List<r<?>> o() {
      return List.of(this.d, this.F, this.u, this.w, this.E);
   }

   public List<r<?>> x() {
      return List.of(this.D, this.n, this.r, this.X, this.L, this.G, this.C, this.W, this.K);
   }

   public String J() {
      return switch ((String)this.t.getValue()) {
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
      this.I = this.p = null;
      this.c = -1;
      _q.z("HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.t.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.t.w("Bloom");
         boolean var3 = false;
         byte var2 = (byte)var10000;
         var10000 = Integer.signum(var2);
         if ((var10000 | -var10000) >>> 31 != 0) {
            LocalPlayer var1 = this.getPlayer();
            if (this.I != var1 || this.p != getMc().level) {
               this.I = var1;
               this.p = getMc().level;
               this.c = -1;
            }

            byte var5 = 10;
            int var4 = var1.hurtTime;
            var10000 = Integer.compare(var4, var5);
            if ((var10000 | -var10000) >>> 31 == 0) {
               int var7 = this.c;
               int var6 = var1.tickCount;
               var10000 = Integer.compare(var6, var7);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.c = var1.tickCount;
                  this.m(var1);
               }
            }

            return;
         }
      }

      this.I = this.p = null;
      this.c = -1;
   }

   public void m(LivingEntity var1) {
      if (this.isEnabled()) {
         LocalPlayer var2 = this.getPlayer();
         Vec3 var3 = var2 != null && var2 != var1 ? var2.getEyePosition() : var1.getBoundingBox().getCenter();
         Vec3 var4 = var2 != null && var2 != var1 ? var2.getViewVector(1.0F) : new Vec3(0.0, 0.0, 1.0);
         qk var5 = qk.sample(var1.getBoundingBox(), var3, var4, (var0, var1x) -> v9.u(getMc().level, var0, var1x));
         this.H(wp.Q, var1.position(), var1.getBbWidth(), var1.getBbHeight(), var5);
      }
   }

   public void a(com.larpclient.l var1, Vec3 var2, double var3, double var5, Vec3 var7) {
      if (this.isEnabled()) {
         this.H(var1, var2, var3, var5, new qk(var2.add(0.0, var5 * 0.5, 0.0), var7, var2.add(0.0, 0.035, 0.0), new Vec3(0.0, 1.0, 0.0)));
      }
   }

   public void H(com.larpclient.l var1, Vec3 var2, double var3, double var5, qk var7) {
      if (this.isEnabled()) {
         this.q = var7.normal();
         this.b.A = this.W.getValue().intValue();
         this.b.k = this.K.getValue();
         Vec3 var8 = var7.point();
         this.b.r = 1;
         this.b.D = _0.y.M;
         this.b.l = this.b.e = Math.max(0.08, var3 * 0.45);
         this.b.x = Math.min(0.4, var5 * 0.22);
         this.b.F = this.i();
         this.b.t = this.z();
         this.b.M = this.w.getValue();
         this.b.V = 0.0;
         this.b.p = _0.e.g;
         this.b.g = var7.groundY();
         this.b.Y = var7.ringNormal();
         this.b.G = this.b.v = (boolean)0;
         this.b.H = 1.0;
         this.b.c = Double.NaN;
         double var9 = this.Y.getValue() / 18.0;
         double var11 = this.f.getValue() / 0.08;
         double var13 = this.N.getValue() / 0.8;
         boolean var15 = this.t.w("Heavy");
         boolean var16 = this.t.w("Petal");
         boolean var17 = this.t.w("Energy");
         boolean var18 = this.t.w("Spark");
         this.b.Z = var17 ? _0.j.o : _0.j.U;
         if (this.R.getValue() && this.t.w("Bloom")) {
            this.b.Z = _0.j.n;
            this.b.D = _0.y.G;
            this.b.L = com.larpclient.l.b.P;
            this.b.w = this.O.getValue().intValue();
            this.b.i = this.b.m = this.Z.getValue() * 0.5;
            this.b.Q = this.b.W = this.o.getValue();
            this.b.F = this.b.t = this.i();
            this.b.M = this.w.getValue();
            this.b.j = _0.v.c;
            this.b.I = this.r.getValue() * 20.0;
            this.b.O = 1.0;
            this.b.U = 0.0;
            this.b.n = this.X.getValue() * 400.0;
            this.b.q = -Math.log(0.98) * 20.0;
            this.b.o = this.b.P = this.b.S = 0.0;
            this.b.K = this.b.R = this.b.s = 0.0;
            this.b.G = (boolean)1;
            this.b.v = (boolean)(!this.d.getValue() && this.E.getValue() ? 1 : 0);
            this.b.C = pr.b.A;
            this.b.H = this.L.getValue();
            this.b.T = pr.g.Z;
            this.b.c = Math.toRadians(20.0);
            this.b.u = Math.toRadians(80.0);
            var1.Q(var2.add(0.0, var5 * 0.5 + 0.1, 0.0), this.b);
         } else if (this.R.getValue() && this.t.w("Cash")) {
            this.w(var1, var8, com.larpclient.l.b.O, 18.0 * var9, 0.17 * var11, 0.25 * var11, 0.9 * var13, 1.5 * var13, 1.25, 1.15, 0.22, _0.v.z);
         } else if (this.R.getValue()) {
            this.w(
               var1,
               var8,
               com.larpclient.l.b.P,
               (var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               _0.v.c
            );
            this.w(
               var1,
               var8,
               com.larpclient.l.b.i,
               (var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               _0.v.c
            );
            this.w(
               var1,
               var8,
               var16
                  ? com.larpclient.l.b.r
                  : (
                     var15
                        ? com.larpclient.l.b.u
                        : (var17 ? com.larpclient.l.b.x : (var18 ? com.larpclient.l.b.L : com.larpclient.l.b.valueOf(this.J.getValue())))
                  ),
               (var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               _0.v.z
            );
         }

         if (this.R.getValue() && this.h.getValue() && !this.t.w("Bloom")) {
            this.w(var1, var8, com.larpclient.l.b.L, 6.0 * var9, 0.025 * var11, 0.055 * var11, 0.25 * var13, 0.5 * var13, 3.0, 0.1, 0.0, _0.v.c);
         }

         if (this.A.getValue()) {
            this.b.G = this.b.v = (boolean)0;
            this.b.p = _0.e.g;
            this.b.V = 0.0;
            this.b.H = 1.0;
            this.b.c = Double.NaN;
            this.b.Z = var17 ? _0.j.o : _0.j.U;
            this.b.F = this.i();
            this.b.t = this.z();
            this.b.M = this.w.getValue();
            this.b.D = _0.y.G;
            this.b.L = com.larpclient.l.b.T;
            this.b.w = 1;
            this.b.Q = this.b.W = this.n.getValue();
            this.b.h = this.k.getValue() * this.s.getValue();
            this.b.z = this.D.getValue();
            this.b.j = _0.v.c;
            this.b.C = pr.b.b;
            var1.Q(var7.ring(), this.b);
            if (var15 || var17) {
               this.b.V = 0.075;
               this.b.h *= 1.25;
               var1.Q(var7.ring().add(var7.ringNormal().scale(0.005)), this.b);
            }
         }

         if (this.G.getValue()) {
            br.C(this.i());
         }
      }
   }

   private void w(
      com.larpclient.l var1,
      Vec3 var2,
      com.larpclient.l.b var3,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      _0.v var20
   ) {
      int var21 = var3 != com.larpclient.l.b.i && var3 != com.larpclient.l.b.L ? 0 : 1;
      int var22 = var3 == com.larpclient.l.b.P ? 1 : 0;
      this.b.p = var3 == com.larpclient.l.b.u ? _0.e.K : _0.e.g;
      this.b.V = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.b.D = var22 != 0 ? _0.y.M : _0.y.x;
      this.b.U = var3 == com.larpclient.l.b.O ? 0.65 : (var3 == com.larpclient.l.b.r ? 0.35 : 0.15);
      this.b.F = var21 != 0 ? vv.Q(this.i(), -1, 0.65) : vv.N(this.i(), var22 != 0 ? 0.52 : 0.92);
      this.b.t = vv.N(this.z(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == com.larpclient.l.b.O) {
         this.b.F = this.b.t = -1;
      }

      this.b.M = (boolean)(this.w.getValue() && var20 == _0.v.c ? 1 : 0);
      this.b.O = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != com.larpclient.l.b.i && var3 != com.larpclient.l.b.L ? 0.0 : var14 * 0.95;
      this.b.o = this.q.x * var23;
      this.b.P = this.q.y * var23;
      this.b.S = this.q.z * var23;
      this.b.L = var3;
      this.b.w = (int)Math.round(var4);
      this.b.i = var6;
      this.b.m = var8;
      this.b.Q = var10;
      this.b.W = var12;
      this.b.I = var21 != 0 ? var14 * 0.3 : var14;
      this.b.n = var16;
      this.b.K = var18;
      this.b.j = var20;
      this.b.q = var21 != 0 ? 2.4 : (var3 == com.larpclient.l.b.r ? 0.8 : 1.4);
      this.b.u = var3 == com.larpclient.l.b.O ? 3.2 : (var3 == com.larpclient.l.b.r ? 2.1 : (var3 == com.larpclient.l.b.u ? 5.0 : 3.0));
      this.b.C = var22 != 0 ? pr.b.b : pr.b.Y;
      this.b.T = var22 != 0 ? pr.g.p : (var21 != 0 ? pr.g.U : pr.g.Z);
      var1.Q(var2, this.b);
   }

   @Override
   public void onDisable() {
      this.I = this.p = null;
      this.c = -1;
      wp.Q.c(1);
   }
}

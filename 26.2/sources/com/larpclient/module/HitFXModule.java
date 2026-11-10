package com.larpclient.module;

import com.larpclient.b5;
import com.larpclient.c3;
import com.larpclient.cd;
import com.larpclient.ce;
import com.larpclient.gl;
import com.larpclient.ho;
import com.larpclient.kz;
import com.larpclient.st;
import com.larpclient.uh;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class HitFXModule extends Module {
   private static HitFXModule v;
   private final l x = this.addSetting(
      new l("Preset", "Choose a complete impact style; its behavior is explained below", "Clean", "Clean", "Energy", "Spark", "Petal", "Heavy", "Cash", "Bloom")
   );
   private final s M = this.addSetting(new s("Screen Impact", "Brief subtle exposure and edge pulse", false));
   private final e H = this.addSetting(new e("Ring Thickness", "Shockwave width", 0.045, 0.01, 0.2, 0.005));
   private Vec3 F = Vec3.ZERO;
   private final gl U = new gl();
   private final l O = this.addSetting(
      new l(
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
   private final e C = this.addSetting(new e("Amount", "Burst density: higher values emit more particles in every layer", 18.0, 1.0, 100.0, 1.0));
   private final e S = this.addSetting(new e("Size", "World-space particle size", 0.08, 0.02, 0.3, 0.01));
   private final e D = this.addSetting(new e("Lifetime", "Seconds", 0.8, 0.2, 4.0, 0.1));
   private final e l = this.addSetting(new e("Maximum", "HitFX particle limit; raise only if repeated bursts reach the limit", 1000.0, 100.0, 2000.0, 100.0));
   private final e a = this.addSetting(new e("Distance", "Render distance", 64.0, 8.0, 128.0, 8.0));
   private final s s = this.addSetting(new s("Theme Colors", "Use the current theme palette", true));
   private final i m = this.addSetting(new i("Color", "Start color", ho.J));
   private final i f = this.addSetting(new i("Gradient", "End color", ho.ds));
   private final s k = this.addSetting(new s("Bloom", "Submit glow mask", true));
   private final s R = this.addSetting(new s("Particles", "Emit particles", true));
   private final s z = this.addSetting(new s("Hit Rings", "Expanding surface or airborne impact rings", true));
   private final e P = this.addSetting(new e("Ring Size", "Final radius", 0.8, 0.2, 2.0, 0.1));
   private final e V = this.addSetting(new e("Ring Speed", "Expansion multiplier", 1.0, 0.5, 3.0, 0.1));
   private final s p = this.addSetting(new s("Entity Hit Color", "Tint hurt entity models", false));
   private final s G = this.addSetting(new s("Hit Trails", "Additional streaks on impact", true));
   private final e b = this.addSetting(new e("Ring Fade", "Ring lifetime in seconds", 0.7, 0.2, 3.0, 0.1));
   private final e i = this.addSetting(B("Bloom Count", "Soft sprites per impact", 15.0, 3.0, 30.0, 1.0));
   private final e I = this.addSetting(B("Bloom Size", "Full sprite width in blocks", 0.25, 0.05, 0.5, 0.01));
   private final e N = this.addSetting(B("Bloom Lifetime", "Seconds before the sprites expire", 1.5, 0.5, 3.0, 0.1));
   private final e q = this.addSetting(B("Bloom Spread", "Initial travel in blocks per tick", 0.08, 0.02, 0.15, 0.01));
   private final e Y = this.addSetting(B("Bloom Gravity", "Downward acceleration per tick", 0.015, 0.005, 0.03, 0.001));
   private final e K = this.addSetting(B("Bloom Fade Speed", "Higher values fade faster", 1.0, 0.3, 2.0, 0.1));
   private final s A = this.addSetting(new s("Bloom Color Animation", "Cycle each sprite through rainbow colors", false));
   private Object r;
   private Object t;
   private int T = -1;

   public boolean E() {
      return this.isEnabled() ? this.isOperational() : false;
   }

   public boolean G() {
      return this.isEnabled() ? this.isRuntimeActive() : false;
   }

   public boolean V() {
      return !this.isEnabled() ? false : this.isOperational() && this.p.getValue();
   }

   public int b() {
      return this.s.getValue() ? ho.J : this.m.getValue();
   }

   private int q() {
      return this.s.getValue() ? ho.ds : this.f.getValue();
   }

   private static e B(String var0, String var1, double var2, double var4, double var6, final double var8) {
      return new e(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, var8 >= 1.0 ? "%.0f" : (var8 >= 0.1 ? "%.1f" : (var8 >= 0.01 ? "%.2f" : "%.3f")), this.getValue());
         }
      };
   }

   public HitFXModule() {
      super("HitFX", "Add particles and rings to hits", Category.VISUALS, -1);
      v = this;

      for (q var4 : new q[]{this.P, this.V, this.b, this.H}) {
         var4.P(() -> this.z.getValue());
      }

      this.O.P(() -> this.R.getValue() && this.x.O("Clean"));

      for (q var11 : new q[]{this.C, this.S, this.D, this.G}) {
         var11.P(() -> this.R.getValue() && !this.x.O("Bloom"));
      }

      for (q var12 : new q[]{this.i, this.I, this.N, this.q, this.Y, this.K, this.A}) {
         var12.P(() -> this.R.getValue() && this.x.O("Bloom"));
      }

      this.m.P(() -> !this.s.getValue());
      this.f.P(() -> !this.s.getValue() && (this.z.getValue() || this.R.getValue() && !this.x.O("Bloom")));
      this.A.P(() -> !this.s.getValue() && this.R.getValue() && this.x.O("Bloom"));
      this.s.o(var0 -> com.larpclient.h.A("HitFX", "Theme colors: {}", var0));
      this.x.o(var0 -> com.larpclient.h.A("HitFX", "Preset changed: {}", var0));
   }

   public q<?> W() {
      return this.R;
   }

   public static HitFXModule N() {
      return v;
   }

   public q<?> Z() {
      return this.x;
   }

   public List<q<?>> a() {
      return List.of(this.x, this.R, this.O, this.C, this.S, this.D, this.G, this.i, this.I, this.N);
   }

   public List<q<?>> O() {
      return List.of(this.z, this.P, this.V);
   }

   public List<q<?>> R() {
      return List.of(this.s, this.m, this.f, this.k, this.A);
   }

   public List<q<?>> c() {
      return List.of(this.H, this.b, this.q, this.Y, this.K, this.M, this.p, this.l, this.a);
   }

   public String z() {
      return switch ((String)this.x.getValue()) {
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
      this.r = this.t = null;
      this.T = -1;
      com.larpclient.h.A(
         "HitFX", "Impact style={}; contact-local bursts, surface/airborne rings and tumbling Heavy shards; particle counts retained", this.x.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.x.O("Bloom");
         boolean var3 = false;
         byte var2 = (byte)var10000;
         var10000 = Integer.signum(var2);
         if ((var10000 | -var10000) >>> 31 != 0) {
            LocalPlayer var1 = this.getPlayer();
            if (this.r != var1 || this.t != getMc().level) {
               this.r = var1;
               this.t = getMc().level;
               this.T = -1;
            }

            byte var5 = 10;
            int var4 = var1.hurtTime;
            var10000 = Integer.compare(var4, var5);
            if ((var10000 | -var10000) >>> 31 == 0) {
               int var7 = this.T;
               int var6 = var1.tickCount;
               var10000 = Integer.compare(var6, var7);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  this.T = var1.tickCount;
                  this.M(var1);
               }
            }

            return;
         }
      }

      this.r = this.t = null;
      this.T = -1;
   }

   public void M(LivingEntity var1) {
      if (this.isEnabled()) {
         LocalPlayer var2 = this.getPlayer();
         Vec3 var3 = var2 != null && var2 != var1 ? var2.getEyePosition() : var1.getBoundingBox().getCenter();
         Vec3 var4 = var2 != null && var2 != var1 ? var2.getViewVector(1.0F) : new Vec3(0.0, 0.0, 1.0);
         ce var5 = ce.sample(var1.getBoundingBox(), var3, var4, (var0, var1x) -> b5.r(getMc().level, var0, var1x));
         this.D(kz.J, var1.position(), var1.getBbWidth(), var1.getBbHeight(), var5);
      }
   }

   public void Z(c3 var1, Vec3 var2, double var3, double var5, Vec3 var7) {
      if (this.isEnabled()) {
         this.D(var1, var2, var3, var5, new ce(var2.add(0.0, var5 * 0.5, 0.0), var7, var2.add(0.0, 0.035, 0.0), new Vec3(0.0, 1.0, 0.0)));
      }
   }

   public void D(c3 var1, Vec3 var2, double var3, double var5, ce var7) {
      if (this.isEnabled()) {
         this.F = var7.normal();
         this.U.L = this.l.getValue().intValue();
         this.U.E = this.a.getValue();
         Vec3 var8 = var7.point();
         this.U.F = 1;
         this.U.x = gl.x.R;
         this.U.D = this.U.P = Math.max(0.08, var3 * 0.45);
         this.U.z = Math.min(0.4, var5 * 0.22);
         this.U.j = this.b();
         this.U.Z = this.q();
         this.U.R = this.k.getValue();
         this.U.T = 0.0;
         this.U.t = gl.g.z;
         this.U.o = var7.groundY();
         this.U.c = var7.ringNormal();
         this.U.y = this.U.a = (boolean)0;
         this.U.l = 1.0;
         this.U.I = Double.NaN;
         double var9 = this.C.getValue() / 18.0;
         double var11 = this.S.getValue() / 0.08;
         double var13 = this.D.getValue() / 0.8;
         boolean var15 = this.x.O("Heavy");
         boolean var16 = this.x.O("Petal");
         boolean var17 = this.x.O("Energy");
         boolean var18 = this.x.O("Spark");
         this.U.d = var17 ? gl.k.D : gl.k.s;
         if (this.R.getValue() && this.x.O("Bloom")) {
            this.U.d = gl.k.T;
            this.U.x = gl.x.B;
            this.U.B = c3.a.Z;
            this.U.u = this.i.getValue().intValue();
            this.U.n = this.U.f = this.I.getValue() * 0.5;
            this.U.O = this.U.Q = this.N.getValue();
            this.U.j = this.U.Z = this.b();
            this.U.R = this.k.getValue();
            this.U.i = gl.c.E;
            this.U.s = this.q.getValue() * 20.0;
            this.U.V = 1.0;
            this.U.C = 0.0;
            this.U.W = this.Y.getValue() * 400.0;
            this.U.K = -Math.log(0.98) * 20.0;
            this.U.Y = this.U.r = this.U.g = 0.0;
            this.U.p = this.U.H = this.U.v = 0.0;
            this.U.y = (boolean)1;
            this.U.a = (boolean)(!this.s.getValue() && this.A.getValue() ? 1 : 0);
            this.U.M = st.z.f;
            this.U.l = this.K.getValue();
            this.U.e = st.c.X;
            this.U.I = Math.toRadians(20.0);
            this.U.m = Math.toRadians(80.0);
            var1.S(var2.add(0.0, var5 * 0.5 + 0.1, 0.0), this.U);
         } else if (this.R.getValue() && this.x.O("Cash")) {
            this.j(var1, var8, c3.a.R, 18.0 * var9, 0.17 * var11, 0.25 * var11, 0.9 * var13, 1.5 * var13, 1.25, 1.15, 0.22, gl.c.K);
         } else if (this.R.getValue()) {
            this.j(
               var1,
               var8,
               c3.a.Z,
               (var17 ? 10 : (!var18 && !var16 ? 6 : 4)) * var9,
               0.065 * var11,
               0.13 * var11,
               0.3 * var13,
               0.6 * var13,
               0.35,
               0.05,
               0.35,
               gl.c.E
            );
            this.j(
               var1,
               var8,
               c3.a.P,
               (var16 ? 6 : (var18 ? 36 : (var15 ? 18 : (var17 ? 12 : 8)))) * var9,
               0.018 * var11,
               0.04 * var11,
               0.28 * var13,
               0.55 * var13,
               var15 ? 4.2 : 3.2,
               0.8,
               0.08,
               gl.c.E
            );
            this.j(
               var1,
               var8,
               var16 ? c3.a.q : (var15 ? c3.a.T : (var17 ? c3.a.n : (var18 ? c3.a.Q : c3.a.valueOf(this.O.getValue())))),
               (var16 ? 52 : (var15 ? 38 : (var17 ? 34 : (var18 ? 14 : 36)))) * var9,
               (var18 ? 0.025 : (var15 ? 0.07 : 0.045)) * var11,
               (var18 ? 0.065 : (var15 ? 0.14 : 0.09)) * var11,
               (var18 ? 0.35 : 0.65) * var13,
               (var18 ? 0.7 : 1.2) * var13,
               var16 ? 0.65 : (var15 ? 1.8 : 1.1),
               var16 ? 0.15 : (var15 ? 1.5 : 0.45),
               var16 ? 0.35 : (var17 ? 0.4 : 0.1),
               gl.c.K
            );
         }

         if (this.R.getValue() && this.G.getValue() && !this.x.O("Bloom")) {
            this.j(var1, var8, c3.a.Q, 6.0 * var9, 0.025 * var11, 0.055 * var11, 0.25 * var13, 0.5 * var13, 3.0, 0.1, 0.0, gl.c.E);
         }

         if (this.z.getValue()) {
            this.U.y = this.U.a = (boolean)0;
            this.U.t = gl.g.z;
            this.U.T = 0.0;
            this.U.l = 1.0;
            this.U.I = Double.NaN;
            this.U.d = var17 ? gl.k.D : gl.k.s;
            this.U.j = this.b();
            this.U.Z = this.q();
            this.U.R = this.k.getValue();
            this.U.x = gl.x.B;
            this.U.B = c3.a.V;
            this.U.u = 1;
            this.U.O = this.U.Q = this.b.getValue();
            this.U.w = this.P.getValue() * this.V.getValue();
            this.U.X = this.H.getValue();
            this.U.i = gl.c.E;
            this.U.M = st.z.z;
            var1.S(var7.ring(), this.U);
            if (var15 || var17) {
               this.U.T = 0.075;
               this.U.w *= 1.25;
               var1.S(var7.ring().add(var7.ringNormal().scale(0.005)), this.U);
            }
         }

         if (this.M.getValue()) {
            cd.X(this.b());
         }
      }
   }

   private void j(
      c3 var1, Vec3 var2, c3.a var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, gl.c var20
   ) {
      int var21 = var3 != c3.a.P && var3 != c3.a.Q ? 0 : 1;
      int var22 = var3 == c3.a.Z ? 1 : 0;
      this.U.t = var3 == c3.a.T ? gl.g.e : gl.g.z;
      this.U.T = var22 == 0 && var21 == 0 ? 0.025 : 0.0;
      this.U.x = var22 != 0 ? gl.x.R : gl.x.C;
      this.U.C = var3 == c3.a.R ? 0.65 : (var3 == c3.a.q ? 0.35 : 0.15);
      this.U.j = var21 != 0 ? uh.q(this.b(), -1, 0.65) : uh.H(this.b(), var22 != 0 ? 0.52 : 0.92);
      this.U.Z = uh.H(this.q(), var22 != 0 ? 0.16 : 0.65);
      if (var3 == c3.a.R) {
         this.U.j = this.U.Z = -1;
      }

      this.U.R = (boolean)(this.k.getValue() && var20 == gl.c.E ? 1 : 0);
      this.U.V = var21 != 0 ? 0.65 : 1.0;
      double var23 = var3 != c3.a.P && var3 != c3.a.Q ? 0.0 : var14 * 0.95;
      this.U.Y = this.F.x * var23;
      this.U.r = this.F.y * var23;
      this.U.g = this.F.z * var23;
      this.U.B = var3;
      this.U.u = (int)Math.round(var4);
      this.U.n = var6;
      this.U.f = var8;
      this.U.O = var10;
      this.U.Q = var12;
      this.U.s = var21 != 0 ? var14 * 0.3 : var14;
      this.U.W = var16;
      this.U.p = var18;
      this.U.i = var20;
      this.U.K = var21 != 0 ? 2.4 : (var3 == c3.a.q ? 0.8 : 1.4);
      this.U.m = var3 == c3.a.R ? 3.2 : (var3 == c3.a.q ? 2.1 : (var3 == c3.a.T ? 5.0 : 3.0));
      this.U.M = var22 != 0 ? st.z.z : st.z.J;
      this.U.e = var22 != 0 ? st.c.u : (var21 != 0 ? st.c.Q : st.c.X);
      var1.S(var2, this.U);
   }

   @Override
   public void onDisable() {
      this.r = this.t = null;
      this.T = -1;
      kz.J.t(1);
   }
}

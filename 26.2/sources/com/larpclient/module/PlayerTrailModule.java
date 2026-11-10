package com.larpclient.module;

import com.larpclient.c3;
import com.larpclient.gl;
import com.larpclient.ho;
import com.larpclient.kz;
import com.larpclient.lt;
import com.larpclient.st;
import com.larpclient.t8;
import com.larpclient.ub;
import com.larpclient.uh;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public final class PlayerTrailModule extends Module {
   private static final int E = 3;
   private final l F = this.addSetting(new l("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust"));
   private final l v = this.addSetting(new l("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE"));
   private final l B = this.addSetting(new l("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY"));
   private final e t = this.addSetting(new e("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final e I = this.addSetting(new e("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final e L = this.addSetting(new e("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final e a = this.addSetting(new e("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final l i = this.addSetting(new l("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette"));
   private final l S = this.addSetting(new l("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final s Q = this.addSetting(new s("Player", "Trail behind yourself", true));
   private final s p = this.addSetting(new s("Entities", "Nearby living entities", false));
   private final s e = this.addSetting(new s("Projectiles", "Arrows and projectiles", true));
   private final s d = this.addSetting(new s("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final e l = this.addSetting(new e("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final e O = this.addSetting(new e("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final s V = this.addSetting(new s("Bloom", "Submit glow mask", true));
   private final s W = this.addSetting(new s("Smoothing", "Smooth trail curves", true));
   private final i q = this.addSetting(new i("Color", "Head color", ho.J));
   private final i D = this.addSetting(new i("Gradient", "Tail color", ho.ds));
   private final s j = this.addSetting(new s("Trail Particles", "Style particles shed from the whole body while moving", true));
   private final e X = this.addSetting(new e("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final gl o = new gl();

   public PlayerTrailModule() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (q var4 : new q[]{this.t, this.I, this.L, this.a, this.i, this.S}) {
         var4.P(() -> this.F.O("Ghost Echo"));
      }

      for (q var8 : new q[]{this.v, this.Q, this.p, this.e, this.d, this.l, this.O, this.W, this.D}) {
         var8.P(() -> !this.F.O("Ghost Echo"));
      }

      this.X.P(this.j::getValue);
      this.q.P(() -> !this.F.O("Ghost Echo") || this.i.O("Custom Tint") || this.i.O("Hologram") || this.i.O("Silhouette"));
      this.B.P(() -> !this.F.O("Ghost Echo") && !this.F.O("Comet") && !this.F.O("Stardust"));
      this.F.o(var0 -> {
         kz.J.t(3);
         kz.c.V();
         kz.b.k();
         kz.P.x();
         com.larpclient.h.A("Trails", "Style changed: {}", var0);
      });
   }

   public List<q<?>> f() {
      return List.of(this.F, this.Q, this.p, this.e, this.d, this.i);
   }

   public List<q<?>> M() {
      return List.of(this.l, this.q, this.D, this.V, this.a);
   }

   public List<q<?>> T() {
      return List.of(this.O, this.v, this.B, this.W, this.j, this.X, this.t, this.I, this.S);
   }

   public List<q<?>> n() {
      return List.of(this.L);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "Trails", "Style: {}; full body: {}; particles: {} at density {}", this.F.getValue(), this.d.getValue(), this.j.getValue(), this.X.getValue()
      );
      com.larpclient.h.A("GhostEcho", "Snapshot interval: {}s; lifetime: {}s; limit: {}", this.t.getValue(), this.I.getValue(), this.L.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.F.O("Ghost Echo");
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            lt var16 = kz.P;
            var16.Y = this.t.getValue();
            var16.I = this.I.getValue();
            var16.A = this.L.getValue().intValue();
            var16.d = this.a.getValue();
            var16.s = this.i.getValue();
            var16.f = this.S.getValue();
            var16.e = this.V.getValue();
            var16.o = this.q.getValue();
            var16.y();
            if (this.j.getValue()) {
               this.s(mc.player, mc.player.getBbHeight(), 1.0);
            }
         } else {
            int var18 = this.F.O("Comet");
            boolean var11 = false;
            byte var10 = (byte)var18;
            var18 = Integer.signum(var10);
            ub.f var1 = ((var18 | -var18) >>> 31 ^ 1) == 0 ? ub.f.k : (this.F.O("Stardust") ? ub.f.O : ub.f.valueOf(this.B.getValue()));
            t8 var2 = kz.b;
            var2.W = this.c();
            var2.c = st.z.valueOf(this.v.getValue());
            var2.i = var1;
            var2.w = this.l.getValue();
            var2.p = this.O.getValue();
            var2.b = this.V.getValue();
            var2.z = this.W.getValue();
            var2.l = this.q.getValue();
            var2.X = this.D.getValue();
            ub var3 = kz.c;
            var3.n = var2.c;
            var3.l = (boolean)(!this.F.O("Ribbon") ? 1 : 0);
            var3.r = var1;
            int var10001 = this.F.O("Ribbon");
            boolean var9 = false;
            byte var8 = (byte)var10001;
            var10001 = Integer.signum(var8);
            var3.X = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? gl.c.K : gl.c.E;
            var3.g = this.l.getValue();
            var3.D = this.O.getValue();
            var3.a = this.V.getValue();
            var3.G = this.W.getValue();
            var3.A = this.q.getValue();
            var3.m = this.D.getValue();
            int var20 = this.Q.getValue();
            boolean var13 = false;
            byte var12 = (byte)var20;
            var20 = Integer.signum(var12);
            if (((var20 | -var20) >>> 31 ^ 1) == 0) {
               this.o(mc.player, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (Entity var7 : mc.level.entitiesForRendering()) {
               if (var7 != mc.player && !(var7.distanceToSqr(mc.player) > 2304.0)) {
                  if (this.p.getValue() && var7 instanceof LivingEntity && var4 < 15) {
                     this.o(var7, 0.35);
                     var4++;
                  } else if (this.e.getValue() && var7 instanceof Projectile) {
                     var3.a(var7.getId(), var7.position().add(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private t8.y c() {
      return switch ((String)this.F.getValue()) {
         case "Glow" -> t8.y.t;
         case "Comet" -> t8.y.E;
         case "Helix" -> t8.y.F;
         case "Stardust" -> t8.y.I;
         default -> t8.y.B;
      };
   }

   private void o(Entity var1, double var2) {
      double var4 = this.d.getValue() ? var1.getBbHeight() : 0.3;
      kz.b.U(var1.getId(), var1.position(), var4, var1.getBbWidth());
      if (this.j.getValue()) {
         this.s(var1, var4, var2);
      }
   }

   private void s(Entity var1, double var2, double var4) {
      Vec3 var6 = var1.position();
      Vec3 var7 = new Vec3(var1.xo, var1.yo, var1.zo);
      double var8 = var6.distanceTo(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      double var11 = switch ((String)this.F.getValue()) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25 = this.F.O("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25) * this.X.getValue() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (int)var15 ? 1 : 0);
      if (var17 != 0) {
         gl var18 = this.o;
         this.O(var18, var2);
         var18.D = var18.P = var1.getBbWidth() * 1.15;
         double var19 = this.F.O("Comet") ? 7.0 : (this.F.O("Stardust") ? 1.2 : 3.0);
         var18.Y = (var7.x - var6.x) * var19;
         var18.g = (var7.z - var6.z) * var19;
         double var21 = this.F.O("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.B = this.L(var10.nextDouble());
            Vec3 var24 = var7.lerp(var6, var10.nextDouble()).add(0.0, var2 * var21, 0.0);
            kz.J.S(var24, var18);
         }
      }
   }

   private void O(gl var1, double var2) {
      int var4 = this.q.getValue();
      int var5 = this.D.getValue();
      var1.F = 3;
      var1.u = 1;
      var1.x = gl.x.l;
      var1.z = var2;
      var1.i = gl.c.E;
      var1.R = this.V.getValue();
      var1.L = (int)Math.round(140.0 + 260.0 * this.X.getValue());
      var1.j = uh.F(var4, 0.9);
      var1.Z = uh.F(var5, 0.25);
      var1.v = 0.0;
      var1.V = 1.0;
      var1.r = 0.0;
      var1.H = 0.0;
      var1.m = 3.0;
      switch ((String)this.F.getValue()) {
         case "Glow":
            var1.n = 0.025;
            var1.f = 0.06;
            var1.O = 0.45;
            var1.Q = 0.95;
            var1.s = 0.18;
            var1.C = 0.45;
            var1.W = -0.35;
            var1.K = 1.6;
            var1.p = 0.4;
            var1.M = st.z.C;
            var1.e = st.c.Q;
            break;
         case "Comet":
            var1.z = var2 * 0.5;
            var1.n = 0.018;
            var1.f = 0.038;
            var1.O = 0.25;
            var1.Q = 0.55;
            var1.s = 0.9;
            var1.C = 0.2;
            var1.W = 0.7;
            var1.K = 1.8;
            var1.p = 0.1;
            var1.M = st.z.z;
            var1.e = st.c.Q;
            var1.Z = uh.F(uh.q(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.n = 0.02;
            var1.f = 0.045;
            var1.O = 0.5;
            var1.Q = 0.9;
            var1.s = 0.45;
            var1.C = 0.05;
            var1.W = -0.05;
            var1.K = 1.4;
            var1.p = 0.15;
            var1.v = 3.2;
            var1.V = 1.2;
            var1.M = st.z.C;
            var1.e = st.c.u;
            break;
         case "Stardust":
            var1.n = 0.016;
            var1.f = 0.05;
            var1.O = 0.8;
            var1.Q = 1.7;
            var1.s = 0.06;
            var1.C = 0.02;
            var1.W = -0.03;
            var1.K = 2.5;
            var1.p = 0.15;
            var1.m = 1.2;
            var1.M = st.z.J;
            var1.e = st.c.T;
            var1.j = uh.F(uh.q(var4, -1, 0.35), 0.95);
            var1.Z = uh.F(var5, 0.35);
            break;
         default:
            var1.n = 0.02;
            var1.f = 0.045;
            var1.O = 0.55;
            var1.Q = 1.0;
            var1.s = 0.06;
            var1.C = 0.08;
            var1.W = -0.04;
            var1.K = 2.0;
            var1.p = 0.25;
            var1.M = st.z.C;
            var1.e = st.c.Q;
      }
   }

   private c3.a L(double var1) {
      return switch ((String)this.F.getValue()) {
         case "Glow" -> var1 < 0.72 ? c3.a.Z : c3.a.P;
         case "Comet" -> var1 < 0.6 ? c3.a.Q : (var1 < 0.85 ? c3.a.P : c3.a.e);
         case "Helix" -> var1 < 0.5 ? c3.a.n : c3.a.Z;
         case "Stardust" -> var1 < 0.5 ? c3.a.e : (var1 < 0.75 ? c3.a.n : c3.a.Z);
         case "Ghost Echo" -> var1 < 0.6 ? c3.a.n : c3.a.Z;
         default -> var1 < 0.7 ? c3.a.l : c3.a.Z;
      };
   }

   @Override
   public void onDisable() {
      kz.J.t(3);
      kz.c.V();
      kz.b.k();
      kz.P.x();
   }
}

package com.larpclient.module;

import com.larpclient._2;
import com.larpclient._3;
import com.larpclient.cg;
import com.larpclient.h;
import com.larpclient.hp;
import com.larpclient.mt;
import com.larpclient.pf;
import com.larpclient.uz;
import com.larpclient.z0;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public final class PlayerTrailModule extends Module {
   private static final int X = 3;
   private final d6 O = this.addSetting(new d6("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust"));
   private final d6 d = this.addSetting(new d6("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE"));
   private final d6 C = this.addSetting(new d6("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY"));
   private final d9 N = this.addSetting(new d9("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final d9 a = this.addSetting(new d9("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final d9 m = this.addSetting(new d9("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final d9 w = this.addSetting(new d9("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final d6 f = this.addSetting(
      new d6("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette")
   );
   private final d6 k = this.addSetting(new d6("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final dh x = this.addSetting(new dh("Player", "Trail behind yourself", true));
   private final dh J = this.addSetting(new dh("Entities", "Nearby living entities", false));
   private final dh q = this.addSetting(new dh("Projectiles", "Arrows and projectiles", true));
   private final dh P = this.addSetting(new dh("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final d9 v = this.addSetting(new d9("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final d9 B = this.addSetting(new d9("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final dh z = this.addSetting(new dh("Bloom", "Submit glow mask", true));
   private final dh i = this.addSetting(new dh("Smoothing", "Smooth trail curves", true));
   private final dk S = this.addSetting(new dk("Color", "Head color", com.larpclient.f.C));
   private final dk n = this.addSetting(new dk("Gradient", "Tail color", com.larpclient.f.O));
   private final dh F = this.addSetting(new dh("Trail Particles", "Style particles shed from the whole body while moving", true));
   private final d9 y = this.addSetting(new d9("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final pf h = new pf();

   public PlayerTrailModule() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (df var4 : new df[]{this.N, this.a, this.m, this.w, this.f, this.k}) {
         var4.H(() -> this.O.S("Ghost Echo"));
      }

      for (df var8 : new df[]{this.d, this.x, this.J, this.q, this.P, this.v, this.B, this.i, this.n}) {
         var8.H(() -> !this.O.S("Ghost Echo"));
      }

      this.y.H(this.F::P);
      this.S.H(() -> !this.O.S("Ghost Echo") || this.f.S("Custom Tint") || this.f.S("Hologram") || this.f.S("Silhouette"));
      this.C.H(() -> !this.O.S("Ghost Echo") && !this.O.S("Comet") && !this.O.S("Stardust"));
      this.O.h(var0 -> {
         com.larpclient.h.P.b(3);
         com.larpclient.h.e.p();
         com.larpclient.h.G.o();
         com.larpclient.h.H.C();
         mt.K("Trails", "Style changed: {}", var0);
      });
   }

   public List<df<?>> z() {
      return List.of(this.O, this.x, this.J, this.q, this.P, this.f);
   }

   public List<df<?>> X() {
      return List.of(this.v, this.S, this.n, this.z, this.w);
   }

   public List<df<?>> Z() {
      return List.of(this.B, this.d, this.C, this.i, this.F, this.y, this.N, this.a, this.k);
   }

   public List<df<?>> L() {
      return List.of(this.m);
   }

   @Override
   public void onEnable() {
      mt.K("Trails", "Style: {}; full body: {}; particles: {} at density {}", this.O.P(), this.P.P(), this.F.P(), this.y.P());
      mt.K("GhostEcho", "Snapshot interval: {}s; lifetime: {}s; limit: {}", this.N.P(), this.a.P(), this.m.P());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.O.S("Ghost Echo")) {
            z0 var16 = com.larpclient.h.H;
            var16.G = this.N.P();
            var16.g = this.a.P();
            var16.E = this.m.P().intValue();
            var16.s = this.w.P();
            var16.W = this.f.P();
            var16.d = this.k.P();
            var16.Y = this.z.P();
            var16.N = this.S.P();
            var16.v();
            int var21 = this.F.P();
            boolean var13 = false;
            byte var12 = (byte)var21;
            var21 = Integer.signum(var12);
            if (((var21 | -var21) >>> 31 ^ 1) == 0) {
               this.I(mc.player, mc.player.getBbHeight(), 1.0);
            }
         } else {
            int var10000 = this.O.S("Comet");
            boolean var11 = false;
            byte var10 = (byte)var10000;
            var10000 = Integer.signum(var10);
            uz.i var18;
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               var18 = uz.i.H;
            } else {
               int var19 = this.O.S("Stardust");
               boolean var15 = false;
               byte var14 = (byte)var19;
               var19 = Integer.signum(var14);
               var18 = ((var19 | -var19) >>> 31 ^ 1) == 0 ? uz.i.I : uz.i.valueOf(this.C.P());
            }

            uz.i var1 = var18;
            _2 var2 = com.larpclient.h.G;
            var2.T = this.e();
            var2.f = hp.y.valueOf(this.d.P());
            var2.K = var1;
            var2.S = this.v.P();
            var2.L = this.B.P();
            var2.J = this.z.P();
            var2.l = this.i.P();
            var2.o = this.S.P();
            var2.P = this.n.P();
            uz var3 = com.larpclient.h.e;
            var3.W = var2.f;
            int var10001 = this.O.S("Ribbon");
            boolean var9 = false;
            byte var8 = (byte)var10001;
            var10001 = Integer.signum(var8);
            var3.G = (boolean)((var10001 | -var10001) >>> 31 == 0 ? 1 : 0);
            var3.A = var1;
            var3.P = this.O.S("Ribbon") ? pf.x.S : pf.x.E;
            var3.x = this.v.P();
            var3.q = this.B.P();
            var3.d = this.z.P();
            var3.b = this.i.P();
            var3.C = this.S.P();
            var3.R = this.n.P();
            if (this.x.P()) {
               this.c(mc.player, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (Entity var7 : mc.level.entitiesForRendering()) {
               if (var7 != mc.player && !(var7.distanceToSqr(mc.player) > 2304.0)) {
                  if (this.J.P() && var7 instanceof LivingEntity && var4 < 15) {
                     this.c(var7, 0.35);
                     var4++;
                  } else if (this.q.P() && var7 instanceof Projectile) {
                     var3.I(var7.getId(), var7.position().add(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private _2.o e() {
      return switch ((String)this.O.P()) {
         case "Glow" -> _2.o.Q;
         case "Comet" -> _2.o.Z;
         case "Helix" -> _2.o.X;
         case "Stardust" -> _2.o.q;
         default -> _2.o.E;
      };
   }

   private void c(Entity var1, double var2) {
      double var4 = this.P.P() ? var1.getBbHeight() : 0.3;
      com.larpclient.h.G.H(var1.getId(), var1.position(), var4, var1.getBbWidth());
      if (this.F.P()) {
         this.I(var1, var4, var2);
      }
   }

   private void I(Entity var1, double var2, double var4) {
      Vec3 var6 = var1.position();
      Vec3 var7 = new Vec3(var1.xo, var1.yo, var1.zo);
      double var8 = var6.distanceTo(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      double var11 = switch ((String)this.O.P()) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25 = this.O.S("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25) * this.y.P() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (int)var15 ? 1 : 0);
      if (var17 != 0) {
         pf var18 = this.h;
         this.M(var18, var2);
         var18.F = var18.U = var1.getBbWidth() * 1.15;
         double var19 = this.O.S("Comet") ? 7.0 : (this.O.S("Stardust") ? 1.2 : 3.0);
         var18.z = (var7.x - var6.x) * var19;
         var18.v = (var7.z - var6.z) * var19;
         double var21 = this.O.S("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.P = this.M(var10.nextDouble());
            Vec3 var24 = var7.lerp(var6, var10.nextDouble()).add(0.0, var2 * var21, 0.0);
            com.larpclient.h.P.i(var24, var18);
         }
      }
   }

   private void M(pf var1, double var2) {
      int var4 = this.S.P();
      int var5 = this.n.P();
      var1.D = 3;
      var1.g = 1;
      var1.A = pf.r.e;
      var1.I = var2;
      var1.p = pf.x.E;
      var1.i = this.z.P();
      var1.X = (int)Math.round(140.0 + 260.0 * this.y.P());
      var1.E = _3.U(var4, 0.9);
      var1.N = _3.U(var5, 0.25);
      var1.J = 0.0;
      var1.b = 1.0;
      var1.K = 0.0;
      var1.x = 0.0;
      var1.e = 3.0;
      switch ((String)this.O.P()) {
         case "Glow":
            var1.C = 0.025;
            var1.d = 0.06;
            var1.M = 0.45;
            var1.O = 0.95;
            var1.Z = 0.18;
            var1.r = 0.45;
            var1.W = -0.35;
            var1.q = 1.6;
            var1.f = 0.4;
            var1.S = hp.y.e;
            var1.l = hp.u.C;
            break;
         case "Comet":
            var1.I = var2 * 0.5;
            var1.C = 0.018;
            var1.d = 0.038;
            var1.M = 0.25;
            var1.O = 0.55;
            var1.Z = 0.9;
            var1.r = 0.2;
            var1.W = 0.7;
            var1.q = 1.8;
            var1.f = 0.1;
            var1.S = hp.y.M;
            var1.l = hp.u.C;
            var1.N = _3.U(_3.D(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.C = 0.02;
            var1.d = 0.045;
            var1.M = 0.5;
            var1.O = 0.9;
            var1.Z = 0.45;
            var1.r = 0.05;
            var1.W = -0.05;
            var1.q = 1.4;
            var1.f = 0.15;
            var1.J = 3.2;
            var1.b = 1.2;
            var1.S = hp.y.e;
            var1.l = hp.u.Q;
            break;
         case "Stardust":
            var1.C = 0.016;
            var1.d = 0.05;
            var1.M = 0.8;
            var1.O = 1.7;
            var1.Z = 0.06;
            var1.r = 0.02;
            var1.W = -0.03;
            var1.q = 2.5;
            var1.f = 0.15;
            var1.e = 1.2;
            var1.S = hp.y.H;
            var1.l = hp.u.J;
            var1.E = _3.U(_3.D(var4, -1, 0.35), 0.95);
            var1.N = _3.U(var5, 0.35);
            break;
         default:
            var1.C = 0.02;
            var1.d = 0.045;
            var1.M = 0.55;
            var1.O = 1.0;
            var1.Z = 0.06;
            var1.r = 0.08;
            var1.W = -0.04;
            var1.q = 2.0;
            var1.f = 0.25;
            var1.S = hp.y.e;
            var1.l = hp.u.C;
      }
   }

   private cg.u M(double var1) {
      return switch ((String)this.O.P()) {
         case "Glow" -> var1 < 0.72 ? cg.u.a : cg.u.M;
         case "Comet" -> var1 < 0.6 ? cg.u.B : (var1 < 0.85 ? cg.u.M : cg.u.h);
         case "Helix" -> var1 < 0.5 ? cg.u.u : cg.u.a;
         case "Stardust" -> var1 < 0.5 ? cg.u.h : (var1 < 0.75 ? cg.u.u : cg.u.a);
         case "Ghost Echo" -> var1 < 0.6 ? cg.u.u : cg.u.a;
         default -> var1 < 0.7 ? cg.u.n : cg.u.a;
      };
   }

   @Override
   public void onDisable() {
      com.larpclient.h.P.b(3);
      com.larpclient.h.e.p();
      com.larpclient.h.G.o();
      com.larpclient.h.H.C();
   }
}

package com.larpclient.module;

import com.larpclient.f4;
import com.larpclient.f8;
import com.larpclient.f_;
import com.larpclient.fk;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.rw;
import com.larpclient.xg;
import com.larpclient.yc;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public final class PlayerTrailModule extends Module {
   private static final int C = 3;
   private final lp x = this.addSetting(new lp("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust"));
   private final lp W = this.addSetting(new lp("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE"));
   private final lp z = this.addSetting(new lp("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY"));
   private final lc Q = this.addSetting(new lc("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final lc S = this.addSetting(new lc("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final lc X = this.addSetting(new lc("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final lc b = this.addSetting(new lc("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final lp U = this.addSetting(
      new lp("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette")
   );
   private final lp i = this.addSetting(new lp("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final ln s = this.addSetting(new ln("Player", "Trail behind yourself", true));
   private final ln l = this.addSetting(new ln("Entities", "Nearby living entities", false));
   private final ln D = this.addSetting(new ln("Projectiles", "Arrows and projectiles", true));
   private final ln c = this.addSetting(new ln("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final lc a = this.addSetting(new lc("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final lc g = this.addSetting(new lc("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final ln r = this.addSetting(new ln("Bloom", "Submit glow mask", true));
   private final ln t = this.addSetting(new ln("Smoothing", "Smooth trail curves", true));
   private final lv q = this.addSetting(new lv("Color", "Head color", rv.e));
   private final lv o = this.addSetting(new lv("Gradient", "Tail color", rv.GQ));
   private final ln j = this.addSetting(new ln("Trail Particles", "Style particles shed from the whole body while moving", true));
   private final lc I = this.addSetting(new lc("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final fk w = new fk();

   public PlayerTrailModule() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (l6 var4 : new l6[]{this.Q, this.S, this.X, this.b, this.U, this.i}) {
         var4.x(() -> this.x.i("Ghost Echo"));
      }

      for (l6 var8 : new l6[]{this.W, this.s, this.l, this.D, this.c, this.a, this.g, this.t, this.o}) {
         var8.x(() -> !this.x.i("Ghost Echo"));
      }

      this.I.x(this.j::getValue);
      this.q.x(() -> !this.x.i("Ghost Echo") || this.U.i("Custom Tint") || this.U.i("Hologram") || this.U.i("Silhouette"));
      this.z.x(() -> !this.x.i("Ghost Echo") && !this.x.i("Comet") && !this.x.i("Stardust"));
      this.x.W(var0 -> {
         f8.D.A(3);
         f8.f.m();
         f8.X.U();
         f8.O.v();
         r4.V("Trails", "Style changed: {}", var0);
      });
   }

   public List<l6<?>> V() {
      return List.of(this.x, this.s, this.l, this.D, this.c, this.U);
   }

   public List<l6<?>> E() {
      return List.of(this.a, this.q, this.o, this.r, this.b);
   }

   public List<l6<?>> z() {
      return List.of(this.g, this.W, this.z, this.t, this.j, this.I, this.Q, this.S, this.i);
   }

   public List<l6<?>> Z() {
      return List.of(this.X);
   }

   @Override
   public void onEnable() {
      r4.V("Trails", "Style: {}; full body: {}; particles: {} at density {}", this.x.getValue(), this.c.getValue(), this.j.getValue(), this.I.getValue());
      r4.V("GhostEcho", "Snapshot interval: {}s; lifetime: {}s; limit: {}", this.Q.getValue(), this.S.getValue(), this.X.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.x.i("Ghost Echo")) {
            f_ var16 = f8.O;
            var16.V = this.Q.getValue();
            var16.u = this.S.getValue();
            var16.n = this.X.getValue().intValue();
            var16.W = this.b.getValue();
            var16.g = this.U.getValue();
            var16.x = this.i.getValue();
            var16.E = this.r.getValue();
            var16.B = this.q.getValue();
            var16.G();
            int var19 = this.j.getValue();
            boolean var9 = false;
            byte var8 = (byte)var19;
            var19 = Integer.signum(var8);
            if (((var19 | -var19) >>> 31 ^ 1) == 0) {
               this.l(mc.player, mc.player.getBbHeight(), 1.0);
            }
         } else {
            xg.c var10000;
            if (this.x.i("Comet")) {
               var10000 = xg.c.N;
            } else {
               byte var17 = this.x.i("Stardust");
               boolean var15 = false;
               byte var14 = var17;
               int var18 = Integer.signum(var14);
               var10000 = ((var18 | -var18) >>> 31 ^ 1) == 0 ? xg.c.W : xg.c.valueOf(this.z.getValue());
            }

            xg.c var1 = var10000;
            rw var2 = f8.X;
            var2.u = this.w();
            var2.L = yc.d.valueOf(this.W.getValue());
            var2.t = var1;
            var2.Y = this.a.getValue();
            var2.x = this.g.getValue();
            var2.C = this.r.getValue();
            var2.h = this.t.getValue();
            var2.g = this.q.getValue();
            var2.V = this.o.getValue();
            xg var3 = f8.f;
            var3.a = var2.L;
            int var10001 = this.x.i("Ribbon");
            boolean var11 = false;
            byte var10 = (byte)var10001;
            var10001 = Integer.signum(var10);
            var3.D = (boolean)((var10001 | -var10001) >>> 31 == 0 ? 1 : 0);
            var3.q = var1;
            int var22 = this.x.i("Ribbon");
            boolean var13 = false;
            byte var12 = (byte)var22;
            var22 = Integer.signum(var12);
            var3.I = ((var22 | -var22) >>> 31 ^ 1) == 0 ? fk.h.z : fk.h.u;
            var3.Q = this.a.getValue();
            var3.v = this.g.getValue();
            var3.s = this.r.getValue();
            var3.e = this.t.getValue();
            var3.T = this.q.getValue();
            var3.O = this.o.getValue();
            if (this.s.getValue()) {
               this.O(mc.player, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (Entity var7 : mc.level.entitiesForRendering()) {
               if (var7 != mc.player && !(var7.distanceToSqr(mc.player) > 2304.0)) {
                  if (this.l.getValue() && var7 instanceof LivingEntity && var4 < 15) {
                     this.O(var7, 0.35);
                     var4++;
                  } else if (this.D.getValue() && var7 instanceof Projectile) {
                     var3.w(var7.getId(), var7.position().add(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private rw.l w() {
      return switch ((String)this.x.getValue()) {
         case "Glow" -> rw.l.e;
         case "Comet" -> rw.l.L;
         case "Helix" -> rw.l.N;
         case "Stardust" -> rw.l.n;
         default -> rw.l.z;
      };
   }

   private void O(Entity var1, double var2) {
      double var4 = this.c.getValue() ? var1.getBbHeight() : 0.3;
      f8.X.e(var1.getId(), var1.position(), var4, var1.getBbWidth());
      if (this.j.getValue()) {
         this.l(var1, var4, var2);
      }
   }

   private void l(Entity var1, double var2, double var4) {
      Vec3 var6 = var1.position();
      Vec3 var7 = new Vec3(var1.xo, var1.yo, var1.zo);
      double var8 = var6.distanceTo(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      double var11 = switch ((String)this.x.getValue()) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25 = this.x.i("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25) * this.I.getValue() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (int)var15 ? 1 : 0);
      if (var17 != 0) {
         fk var18 = this.w;
         this.a(var18, var2);
         var18.E = var18.j = var1.getBbWidth() * 1.15;
         double var19 = this.x.i("Comet") ? 7.0 : (this.x.i("Stardust") ? 1.2 : 3.0);
         var18.h = (var7.x - var6.x) * var19;
         var18.W = (var7.z - var6.z) * var19;
         double var21 = this.x.i("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.D = this.s(var10.nextDouble());
            Vec3 var24 = var7.lerp(var6, var10.nextDouble()).add(0.0, var2 * var21, 0.0);
            f8.D.p(var24, var18);
         }
      }
   }

   private void a(fk var1, double var2) {
      int var4 = this.q.getValue();
      int var5 = this.o.getValue();
      var1.g = 3;
      var1.t = 1;
      var1.m = fk.y.f;
      var1.r = var2;
      var1.C = fk.h.u;
      var1.G = this.r.getValue();
      var1.p = (int)Math.round(140.0 + 260.0 * this.I.getValue());
      var1.B = oh.S(var4, 0.9);
      var1.v = oh.S(var5, 0.25);
      var1.n = 0.0;
      var1.F = 1.0;
      var1.x = 0.0;
      var1.L = 0.0;
      var1.y = 3.0;
      switch ((String)this.x.getValue()) {
         case "Glow":
            var1.V = 0.025;
            var1.u = 0.06;
            var1.Y = 0.45;
            var1.d = 0.95;
            var1.e = 0.18;
            var1.k = 0.45;
            var1.H = -0.35;
            var1.b = 1.6;
            var1.s = 0.4;
            var1.N = yc.d.c;
            var1.U = yc.y.J;
            break;
         case "Comet":
            var1.r = var2 * 0.5;
            var1.V = 0.018;
            var1.u = 0.038;
            var1.Y = 0.25;
            var1.d = 0.55;
            var1.e = 0.9;
            var1.k = 0.2;
            var1.H = 0.7;
            var1.b = 1.8;
            var1.s = 0.1;
            var1.N = yc.d.i;
            var1.U = yc.y.J;
            var1.v = oh.S(oh.j(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.V = 0.02;
            var1.u = 0.045;
            var1.Y = 0.5;
            var1.d = 0.9;
            var1.e = 0.45;
            var1.k = 0.05;
            var1.H = -0.05;
            var1.b = 1.4;
            var1.s = 0.15;
            var1.n = 3.2;
            var1.F = 1.2;
            var1.N = yc.d.c;
            var1.U = yc.y.T;
            break;
         case "Stardust":
            var1.V = 0.016;
            var1.u = 0.05;
            var1.Y = 0.8;
            var1.d = 1.7;
            var1.e = 0.06;
            var1.k = 0.02;
            var1.H = -0.03;
            var1.b = 2.5;
            var1.s = 0.15;
            var1.y = 1.2;
            var1.N = yc.d.R;
            var1.U = yc.y.a;
            var1.B = oh.S(oh.j(var4, -1, 0.35), 0.95);
            var1.v = oh.S(var5, 0.35);
            break;
         default:
            var1.V = 0.02;
            var1.u = 0.045;
            var1.Y = 0.55;
            var1.d = 1.0;
            var1.e = 0.06;
            var1.k = 0.08;
            var1.H = -0.04;
            var1.b = 2.0;
            var1.s = 0.25;
            var1.N = yc.d.c;
            var1.U = yc.y.J;
      }
   }

   private f4.a s(double var1) {
      return switch ((String)this.x.getValue()) {
         case "Glow" -> var1 < 0.72 ? f4.a.u : f4.a.d;
         case "Comet" -> var1 < 0.6 ? f4.a.b : (var1 < 0.85 ? f4.a.d : f4.a.g);
         case "Helix" -> var1 < 0.5 ? f4.a.n : f4.a.u;
         case "Stardust" -> var1 < 0.5 ? f4.a.g : (var1 < 0.75 ? f4.a.n : f4.a.u);
         case "Ghost Echo" -> var1 < 0.6 ? f4.a.n : f4.a.u;
         default -> var1 < 0.7 ? f4.a.D : f4.a.u;
      };
   }

   @Override
   public void onDisable() {
      f8.D.A(3);
      f8.f.m();
      f8.X.U();
      f8.O.v();
   }
}

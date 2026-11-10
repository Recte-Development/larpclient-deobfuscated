package com.larpclient.module;

import com.larpclient._0;
import com.larpclient._q;
import com.larpclient.pr;
import com.larpclient.pw;
import com.larpclient.vv;
import com.larpclient.vw;
import com.larpclient.wp;
import com.larpclient.ws;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public final class PlayerTrailModule extends Module {
   private static final int U = 3;
   private final o v = this.addSetting(new o("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust"));
   private final o y = this.addSetting(new o("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE"));
   private final o S = this.addSetting(new o("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY"));
   private final b s = this.addSetting(new b("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final b Q = this.addSetting(new b("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final b q = this.addSetting(new b("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final b f = this.addSetting(new b("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final o a = this.addSetting(new o("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette"));
   private final o t = this.addSetting(new o("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final m i = this.addSetting(new m("Player", "Trail behind yourself", true));
   private final m G = this.addSetting(new m("Entities", "Nearby living entities", false));
   private final m w = this.addSetting(new m("Projectiles", "Arrows and projectiles", true));
   private final m D = this.addSetting(new m("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final b e = this.addSetting(new b("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final b C = this.addSetting(new b("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final m b = this.addSetting(new m("Bloom", "Submit glow mask", true));
   private final m h = this.addSetting(new m("Smoothing", "Smooth trail curves", true));
   private final h A = this.addSetting(new h("Color", "Head color", com.larpclient.c.y));
   private final h N = this.addSetting(new h("Gradient", "Tail color", com.larpclient.c.t));
   private final m T = this.addSetting(new m("Trail Particles", "Style particles shed from the whole body while moving", true));
   private final b Y = this.addSetting(new b("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final _0 g = new _0();

   public PlayerTrailModule() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (r var4 : new r[]{this.s, this.Q, this.q, this.f, this.a, this.t}) {
         var4.a(() -> this.v.w("Ghost Echo"));
      }

      for (r var8 : new r[]{this.y, this.i, this.G, this.w, this.D, this.e, this.C, this.h, this.N}) {
         var8.a(() -> !this.v.w("Ghost Echo"));
      }

      this.Y.a(this.T::getValue);
      this.A.a(() -> !this.v.w("Ghost Echo") || this.a.w("Custom Tint") || this.a.w("Hologram") || this.a.w("Silhouette"));
      this.S.a(() -> !this.v.w("Ghost Echo") && !this.v.w("Comet") && !this.v.w("Stardust"));
      this.v.w(var0 -> {
         wp.Q.c(3);
         wp.Y.Y();
         wp.S.v();
         wp.V.t();
         _q.z("Trails", "Style changed: {}", var0);
      });
   }

   public List<r<?>> I() {
      return List.of(this.v, this.i, this.G, this.w, this.D, this.a);
   }

   public List<r<?>> e() {
      return List.of(this.e, this.A, this.N, this.b, this.f);
   }

   public List<r<?>> N() {
      return List.of(this.C, this.y, this.S, this.h, this.T, this.Y, this.s, this.Q, this.t);
   }

   public List<r<?>> F() {
      return List.of(this.q);
   }

   @Override
   public void onEnable() {
      _q.z("Trails", "Style: {}; full body: {}; particles: {} at density {}", this.v.getValue(), this.D.getValue(), this.T.getValue(), this.Y.getValue());
      _q.z("GhostEcho", "Snapshot interval: {}s; lifetime: {}s; limit: {}", this.s.getValue(), this.Q.getValue(), this.q.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.v.w("Ghost Echo")) {
            pw var16 = wp.V;
            var16.k = this.s.getValue();
            var16.e = this.Q.getValue();
            var16.E = this.q.getValue().intValue();
            var16.x = this.f.getValue();
            var16.I = this.a.getValue();
            var16.G = this.t.getValue();
            var16.a = this.b.getValue();
            var16.b = this.A.getValue();
            var16.g();
            int var20 = this.T.getValue();
            boolean var11 = false;
            byte var10 = (byte)var20;
            var20 = Integer.signum(var10);
            if (((var20 | -var20) >>> 31 ^ 1) == 0) {
               this.d(mc.player, mc.player.getBbHeight(), 1.0);
            }
         } else {
            int var10000 = this.v.w("Comet");
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            vw.p var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? vw.p.z : (this.v.w("Stardust") ? vw.p.V : vw.p.valueOf(this.S.getValue()));
            ws var2 = wp.S;
            var2.W = this.V();
            var2.n = pr.b.valueOf(this.y.getValue());
            var2.a = var1;
            var2.X = this.e.getValue();
            var2.f = this.C.getValue();
            var2.b = this.b.getValue();
            var2.Q = this.h.getValue();
            var2.v = this.A.getValue();
            var2.E = this.N.getValue();
            vw var3 = wp.Y;
            var3.W = var2.n;
            int var10001 = this.v.w("Ribbon");
            boolean var13 = false;
            byte var12 = (byte)var10001;
            var10001 = Integer.signum(var12);
            var3.g = (boolean)((var10001 | -var10001) >>> 31 == 0 ? 1 : 0);
            var3.v = var1;
            var3.V = this.v.w("Ribbon") ? _0.v.z : _0.v.c;
            var3.P = this.e.getValue();
            var3.d = this.C.getValue();
            var3.j = this.b.getValue();
            var3.B = this.h.getValue();
            var3.k = this.A.getValue();
            var3.a = this.N.getValue();
            int var18 = this.i.getValue();
            boolean var15 = false;
            byte var14 = (byte)var18;
            var18 = Integer.signum(var14);
            if (((var18 | -var18) >>> 31 ^ 1) == 0) {
               this.w(mc.player, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (Entity var7 : mc.level.entitiesForRendering()) {
               if (var7 != mc.player && !(var7.distanceToSqr(mc.player) > 2304.0)) {
                  if (this.G.getValue() && var7 instanceof LivingEntity && var4 < 15) {
                     this.w(var7, 0.35);
                     var4++;
                  } else if (this.w.getValue() && var7 instanceof Projectile) {
                     var3.Z(var7.getId(), var7.position().add(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private ws.m V() {
      return switch ((String)this.v.getValue()) {
         case "Glow" -> ws.m.p;
         case "Comet" -> ws.m.R;
         case "Helix" -> ws.m.q;
         case "Stardust" -> ws.m.r;
         default -> ws.m.b;
      };
   }

   private void w(Entity var1, double var2) {
      double var4 = this.D.getValue() ? var1.getBbHeight() : 0.3;
      wp.S.W(var1.getId(), var1.position(), var4, var1.getBbWidth());
      if (this.T.getValue()) {
         this.d(var1, var4, var2);
      }
   }

   private void d(Entity var1, double var2, double var4) {
      Vec3 var6 = var1.position();
      Vec3 var7 = new Vec3(var1.xo, var1.yo, var1.zo);
      double var8 = var6.distanceTo(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      double var11 = switch ((String)this.v.getValue()) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25 = this.v.w("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25) * this.Y.getValue() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (int)var15 ? 1 : 0);
      if (var17 != 0) {
         _0 var18 = this.g;
         this.u(var18, var2);
         var18.l = var18.e = var1.getBbWidth() * 1.15;
         double var19 = this.v.w("Comet") ? 7.0 : (this.v.w("Stardust") ? 1.2 : 3.0);
         var18.o = (var7.x - var6.x) * var19;
         var18.S = (var7.z - var6.z) * var19;
         double var21 = this.v.w("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.L = this.w(var10.nextDouble());
            Vec3 var24 = var7.lerp(var6, var10.nextDouble()).add(0.0, var2 * var21, 0.0);
            wp.Q.Q(var24, var18);
         }
      }
   }

   private void u(_0 var1, double var2) {
      int var4 = this.A.getValue();
      int var5 = this.N.getValue();
      var1.r = 3;
      var1.w = 1;
      var1.D = _0.y.y;
      var1.x = var2;
      var1.j = _0.v.c;
      var1.M = this.b.getValue();
      var1.A = (int)Math.round(140.0 + 260.0 * this.Y.getValue());
      var1.F = vv.O(var4, 0.9);
      var1.t = vv.O(var5, 0.25);
      var1.s = 0.0;
      var1.O = 1.0;
      var1.P = 0.0;
      var1.R = 0.0;
      var1.u = 3.0;
      switch ((String)this.v.getValue()) {
         case "Glow":
            var1.i = 0.025;
            var1.m = 0.06;
            var1.Q = 0.45;
            var1.W = 0.95;
            var1.I = 0.18;
            var1.U = 0.45;
            var1.n = -0.35;
            var1.q = 1.6;
            var1.K = 0.4;
            var1.C = pr.b.M;
            var1.T = pr.g.U;
            break;
         case "Comet":
            var1.x = var2 * 0.5;
            var1.i = 0.018;
            var1.m = 0.038;
            var1.Q = 0.25;
            var1.W = 0.55;
            var1.I = 0.9;
            var1.U = 0.2;
            var1.n = 0.7;
            var1.q = 1.8;
            var1.K = 0.1;
            var1.C = pr.b.b;
            var1.T = pr.g.U;
            var1.t = vv.O(vv.Q(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.i = 0.02;
            var1.m = 0.045;
            var1.Q = 0.5;
            var1.W = 0.9;
            var1.I = 0.45;
            var1.U = 0.05;
            var1.n = -0.05;
            var1.q = 1.4;
            var1.K = 0.15;
            var1.s = 3.2;
            var1.O = 1.2;
            var1.C = pr.b.M;
            var1.T = pr.g.p;
            break;
         case "Stardust":
            var1.i = 0.016;
            var1.m = 0.05;
            var1.Q = 0.8;
            var1.W = 1.7;
            var1.I = 0.06;
            var1.U = 0.02;
            var1.n = -0.03;
            var1.q = 2.5;
            var1.K = 0.15;
            var1.u = 1.2;
            var1.C = pr.b.Y;
            var1.T = pr.g.A;
            var1.F = vv.O(vv.Q(var4, -1, 0.35), 0.95);
            var1.t = vv.O(var5, 0.35);
            break;
         default:
            var1.i = 0.02;
            var1.m = 0.045;
            var1.Q = 0.55;
            var1.W = 1.0;
            var1.I = 0.06;
            var1.U = 0.08;
            var1.n = -0.04;
            var1.q = 2.0;
            var1.K = 0.25;
            var1.C = pr.b.M;
            var1.T = pr.g.U;
      }
   }

   private com.larpclient.l.b w(double var1) {
      return switch ((String)this.v.getValue()) {
         case "Glow" -> var1 < 0.72 ? com.larpclient.l.b.P : com.larpclient.l.b.i;
         case "Comet" -> var1 < 0.6 ? com.larpclient.l.b.L : (var1 < 0.85 ? com.larpclient.l.b.i : com.larpclient.l.b.Y);
         case "Helix" -> var1 < 0.5 ? com.larpclient.l.b.x : com.larpclient.l.b.P;
         case "Stardust" -> var1 < 0.5 ? com.larpclient.l.b.Y : (var1 < 0.75 ? com.larpclient.l.b.x : com.larpclient.l.b.P);
         case "Ghost Echo" -> var1 < 0.6 ? com.larpclient.l.b.x : com.larpclient.l.b.P;
         default -> var1 < 0.7 ? com.larpclient.l.b.y : com.larpclient.l.b.P;
      };
   }

   @Override
   public void onDisable() {
      wp.Q.c(3);
      wp.Y.Y();
      wp.S.v();
      wp.V.t();
   }
}

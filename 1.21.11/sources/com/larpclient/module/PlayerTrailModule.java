package com.larpclient.module;

import com.larpclient.ce;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.f0;
import com.larpclient.gd;
import com.larpclient.q8;
import com.larpclient.s9;
import com.larpclient.ti;
import com.larpclient.u9;
import com.larpclient.wc;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1676;
import net.minecraft.class_243;

public final class PlayerTrailModule extends Module {
   private static final int F = 3;
   private final li y = this.addSetting(new li("Style", "Movement effect", "Ribbon", "Ribbon", "Glow", "Comet", "Ghost Echo", "Helix", "Stardust"));
   private final li M = this.addSetting(new li("Alpha Curve", "Trail lifetime fade", "LINEAR", "LINEAR", "SMOOTH", "FADE_IN_OUT", "HOLD_FADE", "QUICK_FADE"));
   private final li o = this.addSetting(new li("Width Curve", "Strand taper", "TAPER_TAIL", "CONSTANT", "TAPER_TAIL", "TAPER_BOTH", "HEAD_HEAVY"));
   private final lo b = this.addSetting(new lo("Snapshot Interval", "Seconds between frozen poses", 1.2, 0.1, 4.0, 0.1));
   private final lo n = this.addSetting(new lo("Ghost Lifetime", "Seconds until fully faded", 2.4, 0.3, 8.0, 0.1));
   private final lo t = this.addSetting(new lo("Maximum Ghosts", "Hard snapshot limit", 6.0, 1.0, 12.0, 1.0));
   private final lo Y = this.addSetting(new lo("Ghost Opacity", "Starting opacity", 0.45, 0.05, 0.9, 0.05));
   private final li Z = this.addSetting(
      new li("Ghost Style", "Snapshot material", "Original", "Original", "Theme Tint", "Custom Tint", "Hologram", "Silhouette")
   );
   private final li P = this.addSetting(new li("Ghost Fade", "Lifetime opacity", "Smooth", "Smooth", "Linear"));
   private final l3 I = this.addSetting(new l3("Player", "Trail behind yourself", true));
   private final l3 K = this.addSetting(new l3("Entities", "Nearby living entities", false));
   private final l3 f = this.addSetting(new l3("Projectiles", "Arrows and projectiles", true));
   private final l3 E = this.addSetting(new l3("Full Body", "Span the whole body instead of a low band at the feet", true));
   private final lo T = this.addSetting(new lo("Width", "Strand thickness", 0.12, 0.02, 0.6, 0.01));
   private final lo N = this.addSetting(new lo("Lifetime", "Seconds", 1.2, 0.2, 5.0, 0.1));
   private final l3 z = this.addSetting(new l3("Bloom", "Submit glow mask", true));
   private final l3 W = this.addSetting(new l3("Smoothing", "Smooth trail curves", true));
   private final l0 X = this.addSetting(new l0("Color", "Head color", ez.a));
   private final l0 c = this.addSetting(new l0("Gradient", "Tail color", ez.f));
   private final l3 D = this.addSetting(new l3("Trail Particles", "Style particles shed from the whole body while moving", true));
   private final lo d = this.addSetting(new lo("Particle Density", "Particles per block travelled", 1.0, 0.25, 3.0, 0.05));
   private final ti L = new ti();

   public PlayerTrailModule() {
      super("Player Trail", "Leave fading trails and player copies", Category.VISUALS, -1);

      for (lb var4 : new lb[]{this.b, this.n, this.t, this.Y, this.Z, this.P}) {
         var4.O(() -> this.y.G("Ghost Echo"));
      }

      for (lb var8 : new lb[]{this.M, this.I, this.K, this.f, this.E, this.T, this.N, this.W, this.c}) {
         var8.O(() -> !this.y.G("Ghost Echo"));
      }

      this.d.O(this.D::getValue);
      this.X.O(() -> !this.y.G("Ghost Echo") || this.Z.G("Custom Tint") || this.Z.G("Hologram") || this.Z.G("Silhouette"));
      this.o.O(() -> !this.y.G("Ghost Echo") && !this.y.G("Comet") && !this.y.G("Stardust"));
      this.y.a(var0 -> {
         wc.i.S(3);
         wc.L.f();
         wc.O.d();
         wc.h.e();
         q8.G("Trails", "Style changed: {}", var0);
      });
   }

   public List<lb<?>> b() {
      return List.of(this.y, this.I, this.K, this.f, this.E, this.Z);
   }

   public List<lb<?>> W() {
      return List.of(this.T, this.X, this.c, this.z, this.Y);
   }

   public List<lb<?>> y() {
      return List.of(this.N, this.M, this.o, this.W, this.D, this.d, this.b, this.n, this.P);
   }

   public List<lb<?>> u() {
      return List.of(this.t);
   }

   @Override
   public void onEnable() {
      q8.G("Trails", "Style: {}; full body: {}; particles: {} at density {}", this.y.getValue(), this.E.getValue(), this.D.getValue(), this.d.getValue());
      q8.G("GhostEcho", "Snapshot interval: {}s; lifetime: {}s; limit: {}", this.b.getValue(), this.n.getValue(), this.t.getValue());
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = this.y.G("Ghost Echo");
         boolean var11 = false;
         byte var10 = (byte)var10000;
         var10000 = Integer.signum(var10);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            f0 var16 = wc.h;
            var16.a = this.b.getValue();
            var16.y = this.n.getValue();
            var16.d = this.t.getValue().intValue();
            var16.H = this.Y.getValue();
            var16.U = this.Z.getValue();
            var16.e = this.P.getValue();
            var16.D = this.z.getValue();
            var16.i = this.X.getValue();
            var16.Z();
            int var18 = this.D.getValue();
            boolean var13 = false;
            byte var12 = (byte)var18;
            var18 = Integer.signum(var12);
            if (((var18 | -var18) >>> 31 ^ 1) == 0) {
               this.a(mc.field_1724, mc.field_1724.method_17682(), 1.0);
            }
         } else {
            ce.b var1 = this.y.G("Comet") ? ce.b.P : (this.y.G("Stardust") ? ce.b.p : ce.b.valueOf(this.o.getValue()));
            s9 var2 = wc.O;
            var2.E = this.S();
            var2.b = u9.u.valueOf(this.M.getValue());
            var2.g = var1;
            var2.o = this.T.getValue();
            var2.z = this.N.getValue();
            var2.q = this.z.getValue();
            var2.a = this.W.getValue();
            var2.k = this.X.getValue();
            var2.l = this.c.getValue();
            ce var3 = wc.L;
            var3.W = var2.b;
            int var10001 = this.y.G("Ribbon");
            boolean var9 = false;
            byte var8 = (byte)var10001;
            var10001 = Integer.signum(var8);
            var3.V = (boolean)((var10001 | -var10001) >>> 31 == 0 ? 1 : 0);
            var3.J = var1;
            int var21 = this.y.G("Ribbon");
            boolean var15 = false;
            byte var14 = (byte)var21;
            var21 = Integer.signum(var14);
            var3.D = ((var21 | -var21) >>> 31 ^ 1) == 0 ? ti.d.G : ti.d.l;
            var3.d = this.T.getValue();
            var3.K = this.N.getValue();
            var3.R = this.z.getValue();
            var3.L = this.W.getValue();
            var3.T = this.X.getValue();
            var3.m = this.c.getValue();
            if (this.I.getValue()) {
               this.E(mc.field_1724, 1.0);
            }

            int var4 = 0;
            int var5 = 0;

            for (class_1297 var7 : mc.field_1687.method_18112()) {
               if (var7 != mc.field_1724 && !(var7.method_5858(mc.field_1724) > 2304.0)) {
                  if (this.K.getValue() && var7 instanceof class_1309 && var4 < 15) {
                     this.E(var7, 0.35);
                     var4++;
                  } else if (this.f.getValue() && var7 instanceof class_1676) {
                     var3.W(var7.method_5628(), var7.method_73189().method_1031(0.0, 0.15, 0.0));
                     if (++var5 >= 31) {
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private s9.s S() {
      return switch ((String)this.y.getValue()) {
         case "Glow" -> s9.s.h;
         case "Comet" -> s9.s.e;
         case "Helix" -> s9.s.V;
         case "Stardust" -> s9.s.Z;
         default -> s9.s.T;
      };
   }

   private void E(class_1297 var1, double var2) {
      double var4 = this.E.getValue() ? var1.method_17682() : 0.3;
      wc.O.F(var1.method_5628(), var1.method_73189(), var4, var1.method_17681());
      if (this.D.getValue()) {
         this.a(var1, var4, var2);
      }
   }

   private void a(class_1297 var1, double var2, double var4) {
      class_243 var6 = var1.method_73189();
      class_243 var7 = new class_243(var1.field_6014, var1.field_6036, var1.field_5969);
      double var8 = var6.method_1022(var7);
      if (var8 > 4.0) {
         var7 = var6;
         var8 = 0.0;
      }

      ThreadLocalRandom var10 = ThreadLocalRandom.current();

      double var11 = switch ((String)this.y.getValue()) {
         case "Glow" -> 8.0;
         case "Comet" -> 9.0;
         case "Helix" -> 6.0;
         case "Stardust" -> 12.0;
         default -> 4.0;
      };
      double var25 = this.y.G("Stardust") ? 0.12 : 0.0;
      double var15 = (var11 * var8 + var25) * this.d.getValue() * var4;
      int var17 = (int)var15 + (var10.nextDouble() < var15 - (int)var15 ? 1 : 0);
      if (var17 != 0) {
         ti var18 = this.L;
         this.q(var18, var2);
         var18.E = var18.h = var1.method_17681() * 1.15;
         double var19 = this.y.G("Comet") ? 7.0 : (this.y.G("Stardust") ? 1.2 : 3.0);
         var18.o = (var7.field_1352 - var6.field_1352) * var19;
         var18.c = (var7.field_1350 - var6.field_1350) * var19;
         double var21 = this.y.G("Comet") ? 0.55 : 0.5;

         for (int var23 = 0; var23 < Math.min(var17, 24); var23++) {
            var18.Q = this.D(var10.nextDouble());
            class_243 var24 = var7.method_35590(var6, var10.nextDouble()).method_1031(0.0, var2 * var21, 0.0);
            wc.i.V(var24, var18);
         }
      }
   }

   private void q(ti var1, double var2) {
      int var4 = this.X.getValue();
      int var5 = this.c.getValue();
      var1.t = 3;
      var1.G = 1;
      var1.z = ti.a.O;
      var1.A = var2;
      var1.x = ti.d.l;
      var1.a = this.z.getValue();
      var1.f = (int)Math.round(140.0 + 260.0 * this.d.getValue());
      var1.l = ei.R(var4, 0.9);
      var1.b = ei.R(var5, 0.25);
      var1.X = 0.0;
      var1.r = 1.0;
      var1.g = 0.0;
      var1.y = 0.0;
      var1.w = 3.0;
      switch ((String)this.y.getValue()) {
         case "Glow":
            var1.M = 0.025;
            var1.F = 0.06;
            var1.i = 0.45;
            var1.B = 0.95;
            var1.O = 0.18;
            var1.d = 0.45;
            var1.R = -0.35;
            var1.j = 1.6;
            var1.v = 0.4;
            var1.D = u9.u.M;
            var1.W = u9.o.D;
            break;
         case "Comet":
            var1.A = var2 * 0.5;
            var1.M = 0.018;
            var1.F = 0.038;
            var1.i = 0.25;
            var1.B = 0.55;
            var1.O = 0.9;
            var1.d = 0.2;
            var1.R = 0.7;
            var1.j = 1.8;
            var1.v = 0.1;
            var1.D = u9.u.i;
            var1.W = u9.o.D;
            var1.b = ei.R(ei.W(var4, var5, 0.6), 0.3);
            break;
         case "Helix":
            var1.M = 0.02;
            var1.F = 0.045;
            var1.i = 0.5;
            var1.B = 0.9;
            var1.O = 0.45;
            var1.d = 0.05;
            var1.R = -0.05;
            var1.j = 1.4;
            var1.v = 0.15;
            var1.X = 3.2;
            var1.r = 1.2;
            var1.D = u9.u.M;
            var1.W = u9.o.I;
            break;
         case "Stardust":
            var1.M = 0.016;
            var1.F = 0.05;
            var1.i = 0.8;
            var1.B = 1.7;
            var1.O = 0.06;
            var1.d = 0.02;
            var1.R = -0.03;
            var1.j = 2.5;
            var1.v = 0.15;
            var1.w = 1.2;
            var1.D = u9.u.b;
            var1.W = u9.o.G;
            var1.l = ei.R(ei.W(var4, -1, 0.35), 0.95);
            var1.b = ei.R(var5, 0.35);
            break;
         default:
            var1.M = 0.02;
            var1.F = 0.045;
            var1.i = 0.55;
            var1.B = 1.0;
            var1.O = 0.06;
            var1.d = 0.08;
            var1.R = -0.04;
            var1.j = 2.0;
            var1.v = 0.25;
            var1.D = u9.u.M;
            var1.W = u9.o.D;
      }
   }

   private gd.h D(double var1) {
      return switch ((String)this.y.getValue()) {
         case "Glow" -> var1 < 0.72 ? gd.h.f : gd.h.P;
         case "Comet" -> var1 < 0.6 ? gd.h.E : (var1 < 0.85 ? gd.h.P : gd.h.L);
         case "Helix" -> var1 < 0.5 ? gd.h.t : gd.h.f;
         case "Stardust" -> var1 < 0.5 ? gd.h.L : (var1 < 0.75 ? gd.h.t : gd.h.f);
         case "Ghost Echo" -> var1 < 0.6 ? gd.h.t : gd.h.f;
         default -> var1 < 0.7 ? gd.h.x : gd.h.f;
      };
   }

   @Override
   public void onDisable() {
      wc.i.S(3);
      wc.L.f();
      wc.O.d();
      wc.h.e();
   }
}

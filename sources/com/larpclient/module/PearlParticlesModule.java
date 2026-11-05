package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.ob;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.y2;
import com.larpclient.yc;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.phys.Vec3;

public final class PearlParticlesModule extends Module {
   private static PearlParticlesModule Q;
   private final lv W = this.addSetting(new lv("Color", "Particle tint", -8866817));
   private final lc S = this.A("Spawn Rate", "Particles per pearl each tick", 10.0, 1.0, 10.0, 1.0);
   private final lc y = this.A("Size", "Billboard width in blocks", 0.3, 0.05, 0.5, 0.01);
   private final lc z = this.A("Lifetime", "Seconds", 0.5, 0.5, 3.0, 0.1);
   private final lc B = this.A("Spread", "Initial velocity spread", 0.2, 0.01, 0.2, 0.01);
   private final lc n = this.A("Gravity", "Downward acceleration per tick", 0.008, 0.0, 0.03, 0.001);
   private final lc r = this.A("Fade Speed", "Opacity fade exponent", 2.0, 0.3, 2.0, 0.1);
   private final ln F = this.addSetting(new ln("Color Animation", "Animate the theme gradient", false));
   private final HashMap<Integer, ThrownEnderpearl> p = new HashMap<>();
   private final ArrayList<PearlParticlesModule.w> m = new ArrayList<>();
   private final Random R = new Random();
   private Object Z;
   private Object L;
   private Vec3 Y;
   private int H;

   public PearlParticlesModule() {
      super("Pearl Particles", "Add glowing trails to ender pearls", Category.VISUALS, -1);
      Q = this;
      this.W.x(() -> !this.F.getValue());
   }

   public List<l6<?>> a() {
      return List.of(this.S, this.y, this.z);
   }

   public List<l6<?>> L() {
      return List.of(this.F, this.W);
   }

   public List<l6<?>> I() {
      return List.of(this.B, this.n, this.r);
   }

   private lc A(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   public static PearlParticlesModule D() {
      return Q;
   }

   public void Y() {
      this.p.clear();
      this.m.clear();
      this.Z = this.L = null;
      this.Y = null;
      this.H = 0;
   }

   @Override
   public void onEnable() {
      this.Y();
      r4.V("PearlParticles", "Enabled; max 32 observed pearls, 4096 particles; source bloom sprite");
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label62: {
            var1 = this.getPlayer().position();
            if (this.Z == getMc().level && this.L == this.getPlayer()) {
               if (this.Y == null) {
                  break label62;
               }

               double var14;
               int var10000 = (var14 = var1.distanceToSqr(this.Y) - 256.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label62;
               }
            }

            this.Y();
         }

         this.Z = getMc().level;
         this.L = this.getPlayer();
         this.Y = var1;
         double var2 = ea.t();
         this.p.entrySet().removeIf(var0 -> getMc().level.getEntity(var0.getKey()) != var0.getValue() || var0.getValue().isRemoved());
         HashSet var4 = new HashSet();
         int var5 = 0;

         for (Entity var7 : getMc().level.entitiesForRendering()) {
            if (var7 instanceof ThrownEnderpearl var8 && !var8.isRemoved() && !(var8.distanceToSqr(this.getPlayer()) > 16384.0)) {
               if (++var5 > 32) {
                  break;
               }

               var4.add(var8.getId());
               if (this.p.get(var8.getId()) == var8) {
                  double var9 = this.B.getValue();

                  for (int var11 = 0; var11 < this.S.getValue().intValue() && this.m.size() < 4096; var11++) {
                     this.m
                        .add(
                           new PearlParticlesModule.w(
                              var8.position(),
                              new Vec3(
                                 (this.R.nextDouble() * 2.0 - 1.0) * var9,
                                 (this.R.nextDouble() * 2.0 - 1.0) * var9 * 0.25,
                                 (this.R.nextDouble() * 2.0 - 1.0) * var9
                              ),
                              this.n.getValue(),
                              var2,
                              this.H++
                           )
                        );
                  }
               }

               if (this.p.size() < 32 || this.p.containsKey(var8.getId())) {
                  this.p.put(var8.getId(), var8);
               }
            }
         }

         this.p.keySet().retainAll(var4);
         this.m.removeIf(var3 -> var3.K(var2, this.z.getValue()));
      } else {
         this.Y();
      }
   }

   public boolean e(double var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.Z == getMc().level) {
            for (PearlParticlesModule.w var4 : this.m) {
               double var5 = this.x(var4, var1);
               if (var5 >= 0.001 && oh.S(this.B(var4, var1), var5) >>> 24 != 0) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private double x(PearlParticlesModule.w var1, double var2) {
      double var4 = Math.clamp((var2 - var1.v) / this.z.getValue(), 0.0, 1.0);
      return Math.pow(1.0 - var4, this.r.getValue());
   }

   private int B(PearlParticlesModule.w var1, double var2) {
      return this.F.getValue() ? oh.j(rv.e, rv.GQ, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1.X * 0.04)) : this.W.getValue();
   }

   public void x(PoseStack var1, y2 var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.Z == getMc().level) {
            ob var7 = new ob(var1, var2, var3);

            for (PearlParticlesModule.w var9 : this.m) {
               double var10 = this.x(var9, var4);
               if (!(var10 < 0.001)) {
                  Vec3 var12 = var9.x.lerp(var9.S, ea.Z());
                  int var13 = this.B(var9, var4);
                  double var14 = Math.clamp((var4 - var9.v) / this.z.getValue(), 0.0, 1.0);
                  var13 = oh.j(var13, var13 & 0xFF000000 | 16777215, 0.22 * Math.pow(1.0 - var14, 4.0));
                  var7.C(var12, Y(this.y.getValue(), var14, var9.X), oh.S(var13, var10));
               }
            }
         }
      }
   }

   public static double Y(double var0, double var2, int var4) {
      int var5 = var4 * -1640531527;
      var5 ^= var5 >>> 16;
      double var6 = 0.74 + 0.26 * (var5 >>> 8 & 65535) / 65535.0;
      return var0 * var6 * (1.0 - 0.55 * yc.H(var2));
   }

   private static final class w {
      Vec3 S;
      Vec3 x;
      Vec3 g;
      final double B;
      final double v;
      final int X;

      w(Vec3 var1, Vec3 var2, double var3, double var5, int var7) {
         this.S = this.x = var1;
         this.g = var2;
         this.B = var3;
         this.v = var5;
         this.X = var7;
      }

      boolean K(double var1, double var3) {
         this.x = this.S;
         this.S = this.S.add(this.g);
         this.g = this.g.add(0.0, -this.B, 0.0).scale(0.96);
         return var1 - this.v >= var3;
      }
   }
}

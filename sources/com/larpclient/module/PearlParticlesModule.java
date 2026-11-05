package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.hp;
import com.larpclient.mt;
import com.larpclient.ty;
import com.larpclient.xh;
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
   private static PearlParticlesModule v;
   private final dk y = this.addSetting(new dk("Color", "Particle tint", -8866817));
   private final d9 r = this.i("Spawn Rate", "Particles per pearl each tick", 10.0, 1.0, 10.0, 1.0);
   private final d9 c = this.i("Size", "Billboard width in blocks", 0.3, 0.05, 0.5, 0.01);
   private final d9 t = this.i("Lifetime", "Seconds", 0.5, 0.5, 3.0, 0.1);
   private final d9 R = this.i("Spread", "Initial velocity spread", 0.2, 0.01, 0.2, 0.01);
   private final d9 M = this.i("Gravity", "Downward acceleration per tick", 0.008, 0.0, 0.03, 0.001);
   private final d9 d = this.i("Fade Speed", "Opacity fade exponent", 2.0, 0.3, 2.0, 0.1);
   private final dh D = this.addSetting(new dh("Color Animation", "Animate the theme gradient", false));
   private final HashMap<Integer, ThrownEnderpearl> O = new HashMap<>();
   private final ArrayList<PearlParticlesModule.g> l = new ArrayList<>();
   private final Random Y = new Random();
   private Object A;
   private Object e;
   private Vec3 F;
   private int n;

   public PearlParticlesModule() {
      super("Pearl Particles", "Add glowing trails to ender pearls", Category.VISUALS, -1);
      v = this;
      this.y.H(() -> !this.D.P());
   }

   public List<df<?>> J() {
      return List.of(this.r, this.c, this.t);
   }

   public List<df<?>> M() {
      return List.of(this.D, this.y);
   }

   public List<df<?>> N() {
      return List.of(this.R, this.M, this.d);
   }

   private d9 i(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   public static PearlParticlesModule r() {
      return v;
   }

   public void E() {
      this.O.clear();
      this.l.clear();
      this.A = this.e = null;
      this.F = null;
      this.n = 0;
   }

   @Override
   public void onEnable() {
      this.E();
      mt.K("PearlParticles", "Enabled; max 32 observed pearls, 4096 particles; source bloom sprite");
   }

   @Override
   public void onDisable() {
      this.E();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label63: {
            var1 = this.getPlayer().position();
            if (this.A == getMc().level && this.e == this.getPlayer()) {
               if (this.F == null) {
                  break label63;
               }

               double var14;
               int var10000 = (var14 = var1.distanceToSqr(this.F) - 256.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label63;
               }
            }

            this.E();
         }

         this.A = getMc().level;
         this.e = this.getPlayer();
         this.F = var1;
         double var2 = ty.P();
         this.O.entrySet().removeIf(var0 -> getMc().level.getEntity(var0.getKey()) != var0.getValue() || var0.getValue().isRemoved());
         HashSet var4 = new HashSet();
         int var5 = 0;

         for (Entity var7 : getMc().level.entitiesForRendering()) {
            if (var7 instanceof ThrownEnderpearl var8 && !var8.isRemoved() && !(var8.distanceToSqr(this.getPlayer()) > 16384.0)) {
               if (++var5 > 32) {
                  break;
               }

               var4.add(var8.getId());
               if (this.O.get(var8.getId()) == var8) {
                  double var9 = this.R.P();

                  for (int var11 = 0; var11 < this.r.P().intValue() && this.l.size() < 4096; var11++) {
                     this.l
                        .add(
                           new PearlParticlesModule.g(
                              var8.position(),
                              new Vec3(
                                 (this.Y.nextDouble() * 2.0 - 1.0) * var9,
                                 (this.Y.nextDouble() * 2.0 - 1.0) * var9 * 0.25,
                                 (this.Y.nextDouble() * 2.0 - 1.0) * var9
                              ),
                              this.M.P(),
                              var2,
                              this.n++
                           )
                        );
                  }
               }

               if (this.O.size() < 32 || this.O.containsKey(var8.getId())) {
                  this.O.put(var8.getId(), var8);
               }
            }
         }

         this.O.keySet().retainAll(var4);
         this.l.removeIf(var3 -> var3.I(var2, this.t.P()));
      } else {
         this.E();
      }
   }

   public boolean n(double var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.A == getMc().level) {
            for (PearlParticlesModule.g var4 : this.l) {
               double var5 = this.k(var4, var1);
               if (var5 >= 0.001 && _3.U(this.l(var4, var1), var5) >>> 24 != 0) {
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

   private double k(PearlParticlesModule.g var1, double var2) {
      double var4 = Math.clamp((var2 - var1.M) / this.t.P(), 0.0, 1.0);
      return Math.pow(1.0 - var4, this.d.P());
   }

   private int l(PearlParticlesModule.g var1, double var2) {
      return this.D.P() ? _3.D(com.larpclient.f.C, com.larpclient.f.O, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1.u * 0.04)) : this.y.P();
   }

   public void p(PoseStack var1, xh var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.A == getMc().level) {
            com.larpclient.j var7 = new com.larpclient.j(var1, var2, var3);

            for (PearlParticlesModule.g var9 : this.l) {
               double var10 = this.k(var9, var4);
               if (!(var10 < 0.001)) {
                  Vec3 var12 = var9.a.lerp(var9.H, ty.g());
                  int var13 = this.l(var9, var4);
                  double var14 = Math.clamp((var4 - var9.M) / this.t.P(), 0.0, 1.0);
                  var13 = _3.D(var13, var13 & 0xFF000000 | 16777215, 0.22 * Math.pow(1.0 - var14, 4.0));
                  var7.U(var12, c(this.c.P(), var14, var9.u), _3.U(var13, var10));
               }
            }
         }
      }
   }

   public static double c(double var0, double var2, int var4) {
      int var5 = var4 * -1640531527;
      var5 ^= var5 >>> 16;
      double var6 = 0.74 + 0.26 * (var5 >>> 8 & 65535) / 65535.0;
      return var0 * var6 * (1.0 - 0.55 * hp.R(var2));
   }

   private static final class g {
      Vec3 H;
      Vec3 a;
      Vec3 P;
      final double S;
      final double M;
      final int u;

      g(Vec3 var1, Vec3 var2, double var3, double var5, int var7) {
         this.H = this.a = var1;
         this.P = var2;
         this.S = var3;
         this.M = var5;
         this.u = var7;
      }

      boolean I(double var1, double var3) {
         this.a = this.H;
         this.H = this.H.add(this.P);
         this.P = this.P.add(0.0, -this.S, 0.0).scale(0.96);
         return var1 - this.M >= var3;
      }
   }
}

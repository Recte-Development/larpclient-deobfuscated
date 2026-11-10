package com.larpclient.module;

import com.larpclient._g;
import com.larpclient._q;
import com.larpclient.hz;
import com.larpclient.pr;
import com.larpclient.vv;
import com.larpclient.wh;
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
   private static PearlParticlesModule t;
   private final h g = this.addSetting(new h("Color", "Particle tint", -8866817));
   private final com.larpclient.module.b n = this.l("Spawn Rate", "Particles per pearl each tick", 10.0, 1.0, 10.0, 1.0);
   private final com.larpclient.module.b I = this.l("Size", "Billboard width in blocks", 0.3, 0.05, 0.5, 0.01);
   private final com.larpclient.module.b E = this.l("Lifetime", "Seconds", 0.5, 0.5, 3.0, 0.1);
   private final com.larpclient.module.b N = this.l("Spread", "Initial velocity spread", 0.2, 0.01, 0.2, 0.01);
   private final com.larpclient.module.b x = this.l("Gravity", "Downward acceleration per tick", 0.008, 0.0, 0.03, 0.001);
   private final com.larpclient.module.b Z = this.l("Fade Speed", "Opacity fade exponent", 2.0, 0.3, 2.0, 0.1);
   private final m Y = this.addSetting(new m("Color Animation", "Animate the theme gradient", false));
   private final HashMap<Integer, ThrownEnderpearl> B = new HashMap<>();
   private final ArrayList<PearlParticlesModule.b> o = new ArrayList<>();
   private final Random p = new Random();
   private Object F;
   private Object e;
   private Vec3 a;
   private int z;

   public PearlParticlesModule() {
      super("Pearl Particles", "Add glowing trails to ender pearls", Category.VISUALS, -1);
      t = this;
      this.g.a(() -> !this.Y.getValue());
   }

   public List<r<?>> C() {
      return List.of(this.n, this.I, this.E);
   }

   public List<r<?>> X() {
      return List.of(this.Y, this.g);
   }

   public List<r<?>> x() {
      return List.of(this.N, this.x, this.Z);
   }

   private com.larpclient.module.b l(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.b(var1, var2, var3, var5, var7, var9));
   }

   public static PearlParticlesModule w() {
      return t;
   }

   public void j() {
      this.B.clear();
      this.o.clear();
      this.F = this.e = null;
      this.a = null;
      this.z = 0;
   }

   @Override
   public void onEnable() {
      this.j();
      _q.z("PearlParticles", "Enabled; max 32 observed pearls, 4096 particles; source bloom sprite");
   }

   @Override
   public void onDisable() {
      this.j();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label62: {
            var1 = this.getPlayer().position();
            if (this.F == getMc().level && this.e == this.getPlayer()) {
               if (this.a == null) {
                  break label62;
               }

               double var14;
               int var10000 = (var14 = var1.distanceToSqr(this.a) - 256.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label62;
               }
            }

            this.j();
         }

         this.F = getMc().level;
         this.e = this.getPlayer();
         this.a = var1;
         double var2 = hz.S();
         this.B.entrySet().removeIf(var0 -> getMc().level.getEntity(var0.getKey()) != var0.getValue() || var0.getValue().isRemoved());
         HashSet var4 = new HashSet();
         int var5 = 0;

         for (Entity var7 : getMc().level.entitiesForRendering()) {
            if (var7 instanceof ThrownEnderpearl var8 && !var8.isRemoved() && !(var8.distanceToSqr(this.getPlayer()) > 16384.0)) {
               if (++var5 > 32) {
                  break;
               }

               var4.add(var8.getId());
               if (this.B.get(var8.getId()) == var8) {
                  double var9 = this.N.getValue();

                  for (int var11 = 0; var11 < this.n.getValue().intValue() && this.o.size() < 4096; var11++) {
                     this.o
                        .add(
                           new PearlParticlesModule.b(
                              var8.position(),
                              new Vec3(
                                 (this.p.nextDouble() * 2.0 - 1.0) * var9,
                                 (this.p.nextDouble() * 2.0 - 1.0) * var9 * 0.25,
                                 (this.p.nextDouble() * 2.0 - 1.0) * var9
                              ),
                              this.x.getValue(),
                              var2,
                              this.z++
                           )
                        );
                  }
               }

               if (this.B.size() < 32 || this.B.containsKey(var8.getId())) {
                  this.B.put(var8.getId(), var8);
               }
            }
         }

         this.B.keySet().retainAll(var4);
         this.o.removeIf(var3 -> var3.V(var2, this.E.getValue()));
      } else {
         this.j();
      }
   }

   public boolean P(double var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.F == getMc().level) {
            for (PearlParticlesModule.b var4 : this.o) {
               double var5 = this.j(var4, var1);
               if (var5 >= 0.001 && vv.O(this.j(var4, var1), var5) >>> 24 != 0) {
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

   private double j(PearlParticlesModule.b var1, double var2) {
      double var4 = Math.clamp((var2 - var1.e) / this.E.getValue(), 0.0, 1.0);
      return Math.pow(1.0 - var4, this.Z.getValue());
   }

   private int j(PearlParticlesModule.b var1, double var2) {
      return this.Y.getValue() ? vv.Q(com.larpclient.c.y, com.larpclient.c.t, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1.r * 0.04)) : this.g.getValue();
   }

   public void Y(PoseStack var1, wh var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.F == getMc().level) {
            _g var7 = new _g(var1, var2, var3);

            for (PearlParticlesModule.b var9 : this.o) {
               double var10 = this.j(var9, var4);
               if (!(var10 < 0.001)) {
                  Vec3 var12 = var9.D.lerp(var9.j, hz.C());
                  int var13 = this.j(var9, var4);
                  double var14 = Math.clamp((var4 - var9.e) / this.E.getValue(), 0.0, 1.0);
                  var13 = vv.Q(var13, var13 & 0xFF000000 | 16777215, 0.22 * Math.pow(1.0 - var14, 4.0));
                  var7.K(var12, P(this.I.getValue(), var14, var9.r), vv.O(var13, var10));
               }
            }
         }
      }
   }

   public static double P(double var0, double var2, int var4) {
      int var5 = var4 * -1640531527;
      var5 ^= var5 >>> 16;
      double var6 = 0.74 + 0.26 * (var5 >>> 8 & 65535) / 65535.0;
      return var0 * var6 * (1.0 - 0.55 * pr.k(var2));
   }

   private static final class b {
      Vec3 j;
      Vec3 D;
      Vec3 m;
      final double a;
      final double e;
      final int r;

      b(Vec3 var1, Vec3 var2, double var3, double var5, int var7) {
         this.j = this.D = var1;
         this.m = var2;
         this.a = var3;
         this.e = var5;
         this.r = var7;
      }

      boolean V(double var1, double var3) {
         this.D = this.j;
         this.j = this.j.add(this.m);
         this.m = this.m.add(0.0, -this.a, 0.0).scale(0.96);
         return var1 - this.e >= var3;
      }
   }
}

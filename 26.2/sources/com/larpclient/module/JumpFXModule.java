package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cf;
import com.larpclient.gl;
import com.larpclient.ho;
import com.larpclient.j0;
import com.larpclient.jv;
import com.larpclient.kj;
import com.larpclient.lp;
import com.larpclient.lw;
import com.larpclient.p3;
import com.larpclient.uh;
import com.larpclient.wy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class JumpFXModule extends Module {
   private static JumpFXModule Z;
   private final l j = this.addSetting(
      new l(
         "Mode",
         "Terrain outlines, stars and Clarity jump effects",
         "Star of David",
         "Terrain",
         "Star of David",
         "Ripple",
         "Shockwave",
         "Rune",
         "Bloom",
         "Corona",
         "Vortex",
         "Spectrum",
         "Cloud",
         "End Rod",
         "Crit",
         "Flame",
         "Soul Flame",
         "Happy Villager"
      )
   );
   private final l y = this.addSetting(new l("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final e P = this.addSetting(new e("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final e Y = this.addSetting(new e("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final e J = this.addSetting(new e("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final e Q = this.addSetting(new e("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final l E = this.addSetting(new l("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom"));
   private final i n = this.addSetting(new i("Color", "Custom outline color", -15204097));
   private final e e = this.addSetting(new e("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final e U = this.addSetting(new e("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05));
   private final i x = this.addSetting(new i("Circle Colour", "Primary circle color", -9452545));
   private final i H = this.addSetting(new i("Circle Colour 2", "Secondary circle color", -3703297));
   private final e a = this.addSetting(new e("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final e d = this.addSetting(new e("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final s u = this.addSetting(new s("Circle Through Walls", "Show circles through terrain", false));
   private final e b = this.addSetting(new e("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final e v = this.addSetting(new e("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final s V = this.addSetting(new s("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final j0.p t = new j0.p();
   private final ArrayList<Particle> i = new ArrayList<>();
   private JumpFXModule.z c;
   private final p3 f = new p3();
   private final lw W = new lw();
   private Object w;
   private Object r;

   public JumpFXModule() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      Z = this;
      this.n.P(() -> this.X() && this.E.O("Custom"));

      for (q var2 : List.of(this.P, this.Y, this.Q, this.E, this.e)) {
         var2.P(this::X);
      }

      for (q var5 : List.of(this.x, this.H, this.a, this.d, this.u)) {
         var5.P(() -> j0.v(this.j.getValue()) >= 0);
      }

      for (e var6 : List.of(this.b, this.v)) {
         var6.P(() -> j0.g(this.j.getValue()));
      }

      this.V.P(() -> j0.U(this.j.getValue()));
      this.U.P(() -> this.X() || j0.v(this.j.getValue()) >= 0);
      this.J.P(() -> this.j.O("Terrain"));
      this.j.o(var1 -> {
         this.m();
         com.larpclient.h.A("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public q<?> K() {
      return this.j;
   }

   public List<q<?>> K() {
      return List.of(this.j, this.y, this.V);
   }

   public List<q<?>> M() {
      return List.of(this.P, this.Y, this.J, this.Q, this.a, this.d, this.u, this.b, this.v);
   }

   public List<q<?>> R() {
      return List.of(this.E, this.n, this.e, this.U, this.x, this.H);
   }

   public static JumpFXModule f() {
      return Z;
   }

   @Override
   public void onEnable() {
      this.q();
      com.larpclient.h.A("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.j.getValue(), this.y.getValue());
   }

   @Override
   public void onDisable() {
      this.q();
   }

   private boolean X() {
      return this.j.O("Terrain") || this.j.O("Star of David");
   }

   private void m() {
      this.W.I();
      this.t.B();
      this.c = null;

      for (Particle var2 : this.i) {
         var2.remove();
      }

      this.i.clear();
      jv.x();
   }

   public void q() {
      this.f.S();
      this.m();
      this.w = this.r = null;
   }

   public void i() {
      if (this.isEnabled()) {
         this.c = null;
         int var1 = j0.v(this.j.getValue());
         if (this.isRuntimeActive() && var1 >= 0 && !this.t.I().isEmpty()) {
            this.c = new JumpFXModule.z(
               var1, this.x.getValue(), this.a.getValue(), this.d.getValue(), this.u.getValue(), this.U.getValue().floatValue(), cf.F()
            );
            jv.G(this.H.getValue());
         }
      }
   }

   private boolean I() {
      LocalPlayer var1 = this.getPlayer();
      Module var2 = LarpClient.R().z().d("Fly");
      Module var3 = LarpClient.R().z().d("Freecam");
      return var1 != null
         && var1.isAlive()
         && !var1.isSpectator()
         && !var1.isInWater()
         && !var1.isInLava()
         && !var1.onClimbable()
         && !var1.isPassenger()
         && !var1.isFallFlying()
         && !var1.getAbilities().flying
         && (var2 == null || !var2.isOperational())
         && (var3 == null || !var3.isOperational());
   }

   private p3.u m() {
      LocalPlayer var1 = this.getPlayer();
      if (this.w != getMc().level || this.r != var1) {
         this.q();
         this.w = getMc().level;
         this.r = var1;
      }

      return new p3.u(this.w, var1, var1.position(), var1.onGround(), this.I());
   }

   public void s() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.W(this.f.a(this.m()));
         }
      }
   }

   public void T() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.W.V(cf.I());
            this.t.G(cf.I(), this.d.getValue());
            this.i.removeIf(var0 -> !var0.isAlive());
            this.W(this.f.C(this.m()));
         } else {
            this.q();
         }
      }
   }

   private void W(p3.b var1) {
      if (p3.b(this.y.getValue(), var1.event())) {
         if (!this.X()) {
            if (!this.V.getValue() || this.getPlayer().isSprinting()) {
               int var7 = j0.v(this.j.getValue());
               if (var7 >= 0) {
                  this.t.J(new j0.n(var1.source(), cf.I(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  ParticleOptions var3 = j0.d(this.j.getValue());
                  if (var3 == null) {
                     return;
                  }

                  for (Vec3 var5 : j0.A(var1.source(), this.b.getValue(), this.v.getValue().intValue())) {
                     if (this.i.size() == 384) {
                        this.i.removeFirst().remove();
                     }

                     Particle var6 = getMc().particleEngine.createParticle(var3, var5.x, var5.y, var5.z, 0.0, 0.02, 0.0);
                     if (var6 != null) {
                        this.i.add(var6);
                     }
                  }
               }
            }
         } else {
            lw.q var2 = this.A(var1.source(), cf.I(), this.j.O("Terrain") ? this.i(var1.source().y) : null);
            if (var2 != null) {
               this.W.U(var2);
            }
         }
      }
   }

   public lw.q A(Vec3 var1, double var2, lp.e var4) {
      if (this.isEnabled()) {
         if (!this.X()) {
            return null;
         }

         boolean var5 = this.j.O("Star of David");
         List var6 = var5 ? wy.p : lp.k(var1, this.P.getValue(), var4);
         if (var6.isEmpty()) {
            return null;
         }

         int var7 = this.E.O("Theme") ? ho.J : (this.E.O("White") ? -1 : this.n.getValue());
         int var8 = this.E.O("Theme") ? ho.ds : var7;
         return new lw.q(
            var1,
            var2,
            this.P.getValue(),
            this.Y.getValue(),
            this.J.getValue(),
            this.Q.getValue(),
            var7,
            var8,
            this.e.getValue(),
            this.U.getValue(),
            var6,
            var5 ? lw.u.b : lw.u.h
         );
      } else {
         return null;
      }
   }

   private lp.e i(double var1) {
      ClientLevel var3 = getMc().level;
      CollisionContext var4 = CollisionContext.of(this.getPlayer());
      return (var4x, var5) -> {
         ArrayList var6 = new ArrayList();
         int var7 = (int)Math.floor(var1);

         for (int var8 = var7 - 3; var8 <= var7 + 3; var8++) {
            BlockPos var9 = new BlockPos(var4x, var8, var5);
            if (var3.hasChunkAt(var9)) {
               BlockState var10 = var3.getBlockState(var9);
               if (var10.getFluidState().isEmpty()) {
                  for (AABB var12 : var10.getCollisionShape(var3, var9, var4).toAabbs()) {
                     var6.add(var12.move(var4x, var8, var5));
                     if (var6.size() > 32) {
                        return var6;
                     }
                  }
               }
            }
         }

         return var6;
      };
   }

   public boolean d() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && (this.c != null && this.c.glow() > 0.0F || this.W.T().stream().anyMatch(var0 -> var0.glow() > 0.0));
   }

   public void Z(kj var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.c != null) {
               JumpFXModule.z var3 = this.c;
               float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
               if (var4 > 0.0F) {
                  for (j0.n var6 : this.t.I()) {
                     float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                     if (var7 >= 0.0F && var7 < 1.0F) {
                        var1.R(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                     }
                  }
               }
            }

            var1.blend(gl.c.E);
            double var31 = cf.F();

            for (lw.q var33 : this.W.T()) {
               if (!var2 || !(var33.glow() <= 0.0)) {
                  double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
                  int var9 = uh.q(var33.from(), var33.to(), var34);
                  int var10 = var33.style() == lw.u.b ? 1 : 0;
                  double var11 = var10 != 0 ? lw.L(var33, var31) : 1.0;

                  for (lp.b var14 : var33.edges()) {
                     double var15 = (var10 != 0 ? lw.l(var33, var31) : lw.i(var33, var14.distance(), var31)) * var33.opacity();
                     if (!(var15 < 0.003)) {
                        Vec3 var17 = var14.from();
                        Vec3 var18 = var14.to();
                        double var19 = var17.x;
                        double var21 = var17.y;
                        double var23 = var17.z;
                        double var25 = var18.x;
                        double var27 = var18.y;
                        double var29 = var18.z;
                        if (var10 != 0) {
                           var19 = wy.E(var33.source().x, var19, var11);
                           var23 = wy.E(var33.source().z, var23, var11);
                           var25 = wy.E(var33.source().x, var25, var11);
                           var29 = wy.E(var33.source().z, var29, var11);
                           var21 = var27 = var33.source().y + 0.012;
                        }

                        c(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                     }
                  }
               }
            }

            var1.blend(gl.c.K);
         }
      }
   }

   private static void c(
      kj var0, lw.q var1, boolean var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      if (var2) {
         int var18 = uh.F(var3, var4 * var1.glow() * 0.2);
         var0.k(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + var22 * 1.5);
            int var21 = uh.F(var3, var4 * var1.glow() * 0.065 / (var22 + 1));
            var0.k(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = uh.F(uh.q(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.k(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private record z(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}

package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.__;
import com.larpclient.ea;
import com.larpclient.fk;
import com.larpclient.h0;
import com.larpclient.jr;
import com.larpclient.m1;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rv;
import com.larpclient.x_;
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
   private static JumpFXModule u;
   private final lp j = this.addSetting(
      new lp(
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
   private final lp v = this.addSetting(new lp("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final lc T = this.addSetting(new lc("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final lc h = this.addSetting(new lc("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final lc B = this.addSetting(new lc("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final lc W = this.addSetting(new lc("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final lp L = this.addSetting(new lp("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom"));
   private final lv R = this.addSetting(new lv("Color", "Custom outline color", -15204097));
   private final lc r = this.addSetting(new lc("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final lc w = this.addSetting(new lc("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05));
   private final lv E = this.addSetting(new lv("Circle Colour", "Primary circle color", -9452545));
   private final lv z = this.addSetting(new lv("Circle Colour 2", "Secondary circle color", -3703297));
   private final lc Y = this.addSetting(new lc("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final lc d = this.addSetting(new lc("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final ln n = this.addSetting(new ln("Circle Through Walls", "Show circles through terrain", false));
   private final lc H = this.addSetting(new lc("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final lc t = this.addSetting(new lc("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final ln c = this.addSetting(new ln("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final com.larpclient.l2.s A = new com.larpclient.l2.s();
   private final ArrayList<Particle> o = new ArrayList<>();
   private JumpFXModule.t D;
   private final __ K = new __();
   private final h0 Z = new h0();
   private Object l;
   private Object J;

   public JumpFXModule() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      u = this;
      this.R.x(() -> this.k() && this.L.i("Custom"));

      for (l6 var2 : List.of(this.T, this.h, this.W, this.L, this.r)) {
         var2.x(this::k);
      }

      for (l6 var5 : List.of(this.E, this.z, this.Y, this.d, this.n)) {
         var5.x(() -> com.larpclient.l2.N(this.j.getValue()) >= 0);
      }

      for (lc var6 : List.of(this.H, this.t)) {
         var6.x(() -> com.larpclient.l2.V(this.j.getValue()));
      }

      this.c.x(() -> com.larpclient.l2.n(this.j.getValue()));
      this.w.x(() -> this.k() || com.larpclient.l2.N(this.j.getValue()) >= 0);
      this.B.x(() -> this.j.i("Terrain"));
      this.j.W(var1 -> {
         this.t();
         r4.V("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public l6<?> l() {
      return this.j;
   }

   public List<l6<?>> X() {
      return List.of(this.j, this.v, this.c);
   }

   public List<l6<?>> C() {
      return List.of(this.T, this.h, this.B, this.W, this.Y, this.d, this.n, this.H, this.t);
   }

   public List<l6<?>> T() {
      return List.of(this.L, this.R, this.r, this.w, this.E, this.z);
   }

   public static JumpFXModule m() {
      return u;
   }

   @Override
   public void onEnable() {
      this.p();
      r4.V("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.j.getValue(), this.v.getValue());
   }

   @Override
   public void onDisable() {
      this.p();
   }

   private boolean k() {
      return this.j.i("Terrain") || this.j.i("Star of David");
   }

   private void t() {
      this.Z.Q();
      this.A.O();
      this.D = null;

      for (Particle var2 : this.o) {
         var2.remove();
      }

      this.o.clear();
      m1.n();
   }

   public void p() {
      this.K.g();
      this.t();
      this.l = this.J = null;
   }

   public void F() {
      if (this.isEnabled()) {
         this.D = null;
         int var1 = com.larpclient.l2.N(this.j.getValue());
         if (this.isRuntimeActive() && var1 >= 0 && !this.A.F().isEmpty()) {
            this.D = new JumpFXModule.t(
               var1, this.E.getValue(), this.Y.getValue(), this.d.getValue(), this.n.getValue(), this.w.getValue().floatValue(), ea.y()
            );
            m1.W(this.z.getValue());
         }
      }
   }

   private boolean y() {
      LocalPlayer var1 = this.getPlayer();
      Module var2 = LarpClient.y().e().O("Fly");
      Module var3 = LarpClient.y().e().O("Freecam");
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

   private __.a o() {
      LocalPlayer var1 = this.getPlayer();
      if (this.l != getMc().level || this.J != var1) {
         this.p();
         this.l = getMc().level;
         this.J = var1;
      }

      return new __.a(this.l, var1, var1.position(), var1.onGround(), this.y());
   }

   public void v() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.D(this.K.d(this.o()));
         }
      }
   }

   public void H() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.Z.z(ea.t());
            this.A.W(ea.t(), this.d.getValue());
            this.o.removeIf(var0 -> !var0.isAlive());
            this.D(this.K.J(this.o()));
         } else {
            this.p();
         }
      }
   }

   private void D(__.z var1) {
      if (__.p(this.v.getValue(), var1.event())) {
         if (!this.k()) {
            if (!this.c.getValue() || this.getPlayer().isSprinting()) {
               int var7 = com.larpclient.l2.N(this.j.getValue());
               if (var7 >= 0) {
                  this.A.M(new com.larpclient.l2.k(var1.source(), ea.t(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  ParticleOptions var3 = com.larpclient.l2.i(this.j.getValue());
                  if (var3 == null) {
                     return;
                  }

                  for (Vec3 var5 : com.larpclient.l2.s(var1.source(), this.H.getValue(), this.t.getValue().intValue())) {
                     if (this.o.size() == 384) {
                        this.o.removeFirst().remove();
                     }

                     Particle var6 = getMc().particleEngine.createParticle(var3, var5.x, var5.y, var5.z, 0.0, 0.02, 0.0);
                     if (var6 != null) {
                        this.o.add(var6);
                     }
                  }
               }
            }
         } else {
            h0.n var2 = this.L(var1.source(), ea.t(), this.j.i("Terrain") ? this.R(var1.source().y) : null);
            if (var2 != null) {
               this.Z.q(var2);
            }
         }
      }
   }

   public h0.n L(Vec3 var1, double var2, jr.t var4) {
      if (this.isEnabled()) {
         if (!this.k()) {
            return null;
         }

         boolean var5 = this.j.i("Star of David");
         List var6 = var5 ? x_.a : jr.n(var1, this.T.getValue(), var4);
         if (var6.isEmpty()) {
            return null;
         }

         int var7 = this.L.i("Theme") ? rv.e : (this.L.i("White") ? -1 : this.R.getValue());
         int var8 = this.L.i("Theme") ? rv.GQ : var7;
         return new h0.n(
            var1,
            var2,
            this.T.getValue(),
            this.h.getValue(),
            this.B.getValue(),
            this.W.getValue(),
            var7,
            var8,
            this.r.getValue(),
            this.w.getValue(),
            var6,
            var5 ? h0.j.L : h0.j.w
         );
      } else {
         return null;
      }
   }

   private jr.t R(double var1) {
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

   public boolean A() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && (this.D != null && this.D.glow() > 0.0F || this.Z.d().stream().anyMatch(var0 -> var0.glow() > 0.0));
   }

   public void d(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.D != null) {
               JumpFXModule.t var3 = this.D;
               float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
               if (var4 > 0.0F) {
                  for (com.larpclient.l2.k var6 : this.A.F()) {
                     float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                     if (var7 >= 0.0F && var7 < 1.0F) {
                        var1.a(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                     }
                  }
               }
            }

            var1.blend(fk.h.u);
            double var31 = ea.y();

            for (h0.n var33 : this.Z.d()) {
               if (!var2 || !(var33.glow() <= 0.0)) {
                  double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
                  int var9 = oh.j(var33.from(), var33.to(), var34);
                  int var10 = var33.style() == h0.j.L ? 1 : 0;
                  double var11 = var10 != 0 ? h0.d(var33, var31) : 1.0;

                  for (jr.l var14 : var33.edges()) {
                     double var15 = (var10 != 0 ? h0.t(var33, var31) : h0.T(var33, var14.distance(), var31)) * var33.opacity();
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
                           var19 = x_.n(var33.source().x, var19, var11);
                           var23 = x_.n(var33.source().z, var23, var11);
                           var25 = x_.n(var33.source().x, var25, var11);
                           var29 = x_.n(var33.source().z, var29, var11);
                           var21 = var27 = var33.source().y + 0.012;
                        }

                        q(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                     }
                  }
               }
            }

            var1.blend(fk.h.z);
         }
      }
   }

   private static void q(
      r6 var0, h0.n var1, boolean var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      if (var2) {
         int var18 = oh.S(var3, var4 * var1.glow() * 0.2);
         var0.p(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + var22 * 1.5);
            int var21 = oh.S(var3, var4 * var1.glow() * 0.065 / (var22 + 1));
            var0.p(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = oh.S(oh.j(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.p(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private record t(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}

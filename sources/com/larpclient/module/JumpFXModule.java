package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._3;
import com.larpclient.en;
import com.larpclient.ki;
import com.larpclient.mt;
import com.larpclient.ot;
import com.larpclient.pf;
import com.larpclient.t_;
import com.larpclient.ty;
import com.larpclient.x9;
import com.larpclient.zg;
import com.larpclient.zm;
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
   private static JumpFXModule o;
   private final d6 K = this.addSetting(
      new d6(
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
   private final d6 b = this.addSetting(new d6("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final d9 A = this.addSetting(new d9("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final d9 J = this.addSetting(new d9("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final d9 g = this.addSetting(new d9("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final d9 s = this.addSetting(new d9("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.P());
      }
   });
   private final d6 X = this.addSetting(new d6("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom"));
   private final dk i = this.addSetting(new dk("Color", "Custom outline color", -15204097));
   private final d9 L = this.addSetting(new d9("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final d9 d = this.addSetting(new d9("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05));
   private final dk N = this.addSetting(new dk("Circle Colour", "Primary circle color", -9452545));
   private final dk e = this.addSetting(new dk("Circle Colour 2", "Secondary circle color", -3703297));
   private final d9 H = this.addSetting(new d9("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final d9 l = this.addSetting(new d9("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final dh Q = this.addSetting(new dh("Circle Through Walls", "Show circles through terrain", false));
   private final d9 r = this.addSetting(new d9("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final d9 v = this.addSetting(new d9("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final dh w = this.addSetting(new dh("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final zm.x m = new zm.x();
   private final ArrayList<Particle> a = new ArrayList<>();
   private JumpFXModule.d P;
   private final ot y = new ot();
   private final en S = new en();
   private Object V;
   private Object c;

   public JumpFXModule() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      o = this;
      this.i.H(() -> this.j() && this.X.S("Custom"));

      for (df var2 : List.of(this.A, this.J, this.s, this.X, this.L)) {
         var2.H(this::j);
      }

      for (df var5 : List.of(this.N, this.e, this.H, this.l, this.Q)) {
         var5.H(() -> zm.m(this.K.P()) >= 0);
      }

      for (d9 var6 : List.of(this.r, this.v)) {
         var6.H(() -> zm.B(this.K.P()));
      }

      this.w.H(() -> zm.L(this.K.P()));
      this.d.H(() -> this.j() || zm.m(this.K.P()) >= 0);
      this.g.H(() -> this.K.S("Terrain"));
      this.K.h(var1 -> {
         this.c();
         mt.K("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public df<?> I() {
      return this.K;
   }

   public List<df<?>> Y() {
      return List.of(this.K, this.b, this.w);
   }

   public List<df<?>> H() {
      return List.of(this.A, this.J, this.g, this.s, this.H, this.l, this.Q, this.r, this.v);
   }

   public List<df<?>> k() {
      return List.of(this.X, this.i, this.L, this.d, this.N, this.e);
   }

   public static JumpFXModule m() {
      return o;
   }

   @Override
   public void onEnable() {
      this.o();
      mt.K("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.K.P(), this.b.P());
   }

   @Override
   public void onDisable() {
      this.o();
   }

   private boolean j() {
      return this.K.S("Terrain") || this.K.S("Star of David");
   }

   private void c() {
      this.S.F();
      this.m.R();
      this.P = null;

      for (Particle var2 : this.a) {
         var2.remove();
      }

      this.a.clear();
      t_.F();
   }

   public void o() {
      this.y.M();
      this.c();
      this.V = this.c = null;
   }

   public void l() {
      if (this.isEnabled()) {
         this.P = null;
         int var1 = zm.m(this.K.P());
         if (this.isRuntimeActive() && var1 >= 0 && !this.m.s().isEmpty()) {
            this.P = new JumpFXModule.d(var1, this.N.P(), this.H.P(), this.l.P(), this.Q.P(), this.d.P().floatValue(), ty.K());
            t_.z(this.e.P());
         }
      }
   }

   private boolean U() {
      LocalPlayer var1 = this.getPlayer();
      Module var2 = LarpClient.F().x().k("Fly");
      Module var3 = LarpClient.F().x().k("Freecam");
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

   private ot.v y() {
      LocalPlayer var1 = this.getPlayer();
      if (this.V != getMc().level || this.c != var1) {
         this.o();
         this.V = getMc().level;
         this.c = var1;
      }

      return new ot.v(this.V, var1, var1.position(), var1.onGround(), this.U());
   }

   public void a() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.E(this.y.C(this.y()));
         }
      }
   }

   public void F() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.S.z(ty.P());
            this.m.y(ty.P(), this.l.P());
            this.a.removeIf(var0 -> !var0.isAlive());
            this.E(this.y.Q(this.y()));
         } else {
            this.o();
         }
      }
   }

   private void E(ot.q var1) {
      if (ot.P(this.b.P(), var1.event())) {
         if (!this.j()) {
            if (!this.w.P() || this.getPlayer().isSprinting()) {
               int var7 = zm.m(this.K.P());
               if (var7 >= 0) {
                  this.m.Y(new zm.w(var1.source(), ty.P(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  ParticleOptions var3 = zm.m(this.K.P());
                  if (var3 == null) {
                     return;
                  }

                  for (Vec3 var5 : zm.q(var1.source(), this.r.P(), this.v.P().intValue())) {
                     if (this.a.size() == 384) {
                        this.a.removeFirst().remove();
                     }

                     Particle var6 = getMc().particleEngine.createParticle(var3, var5.x, var5.y, var5.z, 0.0, 0.02, 0.0);
                     if (var6 != null) {
                        this.a.add(var6);
                     }
                  }
               }
            }
         } else {
            en.j var2 = this.a(var1.source(), ty.P(), this.K.S("Terrain") ? this.J(var1.source().y) : null);
            if (var2 != null) {
               this.S.G(var2);
            }
         }
      }
   }

   public en.j a(Vec3 var1, double var2, zg.i var4) {
      if (this.isEnabled()) {
         if (!this.j()) {
            return null;
         }

         boolean var5 = this.K.S("Star of David");
         List var6 = var5 ? x9.x : zg.l(var1, this.A.P(), var4);
         if (var6.isEmpty()) {
            return null;
         }

         int var7 = this.X.S("Theme") ? com.larpclient.f.C : (this.X.S("White") ? -1 : this.i.P());
         int var8 = this.X.S("Theme") ? com.larpclient.f.O : var7;
         return new en.j(var1, var2, this.A.P(), this.J.P(), this.g.P(), this.s.P(), var7, var8, this.L.P(), this.d.P(), var6, var5 ? en.o.E : en.o.p);
      } else {
         return null;
      }
   }

   private zg.i J(double var1) {
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

   public boolean I() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && (this.P != null && this.P.glow() > 0.0F || this.S.J().stream().anyMatch(var0 -> var0.glow() > 0.0));
   }

   public void j(ki var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.P != null) {
               JumpFXModule.d var3 = this.P;
               float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
               if (var4 > 0.0F) {
                  for (zm.w var6 : this.m.s()) {
                     float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                     if (var7 >= 0.0F && var7 < 1.0F) {
                        var1.O(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                     }
                  }
               }
            }

            var1.blend(pf.x.E);
            double var31 = ty.K();

            for (en.j var33 : this.S.J()) {
               if (!var2 || !(var33.glow() <= 0.0)) {
                  double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
                  int var9 = _3.D(var33.from(), var33.to(), var34);
                  int var10 = var33.style() == en.o.E ? 1 : 0;
                  double var11 = var10 != 0 ? en.Z(var33, var31) : 1.0;

                  for (zg.r var14 : var33.edges()) {
                     double var15 = (var10 != 0 ? en.V(var33, var31) : en.M(var33, var14.distance(), var31)) * var33.opacity();
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
                           var19 = x9.P(var33.source().x, var19, var11);
                           var23 = x9.P(var33.source().z, var23, var11);
                           var25 = x9.P(var33.source().x, var25, var11);
                           var29 = x9.P(var33.source().z, var29, var11);
                           var21 = var27 = var33.source().y + 0.012;
                        }

                        J(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                     }
                  }
               }
            }

            var1.blend(pf.x.S);
         }
      }
   }

   private static void J(
      ki var0, en.j var1, boolean var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      if (var2) {
         int var18 = _3.U(var3, var4 * var1.glow() * 0.2);
         var0.b(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + var22 * 1.5);
            int var21 = _3.U(var3, var4 * var1.glow() * 0.065 / (var22 + 1));
            var0.b(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = _3.U(_3.D(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.b(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private record d(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}

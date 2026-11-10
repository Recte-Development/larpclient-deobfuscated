package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._0;
import com.larpclient._q;
import com.larpclient.a5;
import com.larpclient.b4;
import com.larpclient.f6;
import com.larpclient.fz;
import com.larpclient.hz;
import com.larpclient.j6;
import com.larpclient.nt;
import com.larpclient.vv;
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
   private static JumpFXModule h;
   private final o V = this.addSetting(
      new o(
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
   private final o p = this.addSetting(new o("Trigger", "Movement transition", "Land", "Jump", "Land", "Both"));
   private final b q = this.addSetting(new b("Maximum Radius", "Blocks", 1.0, 1.0, 8.0, 0.25));
   private final b j = this.addSetting(new b("Pulse Duration", "Seconds", 1.0, 0.3, 3.0, 0.05));
   private final b H = this.addSetting(new b("Wave Width", "Soft wave band in blocks", 0.9, 0.5, 3.0, 0.05));
   private final b w = this.addSetting(new b("Outline Thickness", "World-space stroke width", 0.04, 0.005, 0.08, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final o k = this.addSetting(new o("Color Theme", "Theme gradient, white or custom color", "Custom", "Theme", "White", "Custom"));
   private final h Q = this.addSetting(new h("Color", "Custom outline color", -15204097));
   private final b x = this.addSetting(new b("Opacity", "Outline opacity", 0.35, 0.05, 1.0, 0.05));
   private final b J = this.addSetting(new b("Glow Intensity", "Soft strokes and quality-controlled bloom", 2.0, 0.0, 2.0, 0.05));
   private final h N = this.addSetting(new h("Circle Colour", "Primary circle color", -9452545));
   private final h W = this.addSetting(new h("Circle Colour 2", "Secondary circle color", -3703297));
   private final b v = this.addSetting(new b("Circle Size", "Circle scale in blocks", 1.6, 0.5, 6.0, 0.1));
   private final b T = this.addSetting(new b("Circle Duration", "Seconds", 0.9, 0.2, 3.0, 0.1));
   private final m C = this.addSetting(new m("Circle Through Walls", "Show circles through terrain", false));
   private final b L = this.addSetting(new b("Radius", "Particle ring radius in blocks", 0.6, 0.1, 3.0, 0.1));
   private final b D = this.addSetting(new b("Points", "Particles per jump or landing", 16.0, 4.0, 64.0, 1.0));
   private final m E = this.addSetting(new m("Only Sprinting", "Emit Clarity effects only while sprinting", false));
   private final j6.v F = new j6.v();
   private final ArrayList<Particle> a = new ArrayList<>();
   private JumpFXModule.c s;
   private final f6 U = new f6();
   private final a5 o = new a5();
   private Object Z;
   private Object P;

   public JumpFXModule() {
      super("JumpFX", "Show glowing effects on jumps and landings", Category.VISUALS, -1);
      h = this;
      this.Q.a(() -> this.D() && this.k.w("Custom"));

      for (r var2 : List.of(this.q, this.j, this.w, this.k, this.x)) {
         var2.a(this::D);
      }

      for (r var5 : List.of(this.N, this.W, this.v, this.T, this.C)) {
         var5.a(() -> j6.r(this.V.getValue()) >= 0);
      }

      for (b var6 : List.of(this.L, this.D)) {
         var6.a(() -> j6.V(this.V.getValue()));
      }

      this.E.a(() -> j6.Y(this.V.getValue()));
      this.J.a(() -> this.D() || j6.r(this.V.getValue()) >= 0);
      this.H.a(() -> this.V.w("Terrain"));
      this.V.w(var1 -> {
         this.k();
         _q.z("JumpFX", "Mode changed: {}; active pulses cleared", var1);
      });
   }

   public r<?> W() {
      return this.V;
   }

   public List<r<?>> h() {
      return List.of(this.V, this.p, this.E);
   }

   public List<r<?>> D() {
      return List.of(this.q, this.j, this.H, this.w, this.v, this.T, this.C, this.L, this.D);
   }

   public List<r<?>> I() {
      return List.of(this.k, this.Q, this.x, this.J, this.N, this.W);
   }

   public static JumpFXModule f() {
      return h;
   }

   @Override
   public void onEnable() {
      this.x();
      _q.z("JumpFX", "Enabled; mode={}, trigger={}, bounded world effects and native particles", this.V.getValue(), this.p.getValue());
   }

   @Override
   public void onDisable() {
      this.x();
   }

   private boolean D() {
      return this.V.w("Terrain") || this.V.w("Star of David");
   }

   private void k() {
      this.o.L();
      this.F.N();
      this.s = null;

      for (Particle var2 : this.a) {
         var2.remove();
      }

      this.a.clear();
      nt.a();
   }

   public void x() {
      this.U.r();
      this.k();
      this.Z = this.P = null;
   }

   public void X() {
      if (this.isEnabled()) {
         this.s = null;
         int var1 = j6.r(this.V.getValue());
         if (this.isRuntimeActive() && var1 >= 0 && !this.F.h().isEmpty()) {
            this.s = new JumpFXModule.c(
               var1, this.N.getValue(), this.v.getValue(), this.T.getValue(), this.C.getValue(), this.J.getValue().floatValue(), hz.Q()
            );
            nt.q(this.W.getValue());
         }
      }
   }

   private boolean T() {
      LocalPlayer var1 = this.getPlayer();
      Module var2 = LarpClient.N().j().U("Fly");
      Module var3 = LarpClient.N().j().U("Freecam");
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

   private f6.i B() {
      LocalPlayer var1 = this.getPlayer();
      if (this.Z != getMc().level || this.P != var1) {
         this.x();
         this.Z = getMc().level;
         this.P = var1;
      }

      return new f6.i(this.Z, var1, var1.position(), var1.onGround(), this.T());
   }

   public void e() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.n(this.U.O(this.B()));
         }
      }
   }

   public void m() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame()) {
            this.o.P(hz.S());
            this.F.n(hz.S(), this.T.getValue());
            this.a.removeIf(var0 -> !var0.isAlive());
            this.n(this.U.i(this.B()));
         } else {
            this.x();
         }
      }
   }

   private void n(f6.t var1) {
      if (f6.M(this.p.getValue(), var1.event())) {
         if (!this.D()) {
            if (!this.E.getValue() || this.getPlayer().isSprinting()) {
               int var7 = j6.r(this.V.getValue());
               if (var7 >= 0) {
                  this.F.N(new j6.m(var1.source(), hz.S(), ThreadLocalRandom.current().nextFloat()));
               } else {
                  ParticleOptions var3 = j6.B(this.V.getValue());
                  if (var3 == null) {
                     return;
                  }

                  for (Vec3 var5 : j6.Z(var1.source(), this.L.getValue(), this.D.getValue().intValue())) {
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
            a5.h var2 = this.b(var1.source(), hz.S(), this.V.w("Terrain") ? this.k(var1.source().y) : null);
            if (var2 != null) {
               this.o.X(var2);
            }
         }
      }
   }

   public a5.h b(Vec3 var1, double var2, com.larpclient.n.f var4) {
      if (this.isEnabled()) {
         if (!this.D()) {
            return null;
         }

         boolean var5 = this.V.w("Star of David");
         List var6 = var5 ? b4.i : com.larpclient.n.N(var1, this.q.getValue(), var4);
         if (var6.isEmpty()) {
            return null;
         }

         int var7 = this.k.w("Theme") ? com.larpclient.c.y : (this.k.w("White") ? -1 : this.Q.getValue());
         int var8 = this.k.w("Theme") ? com.larpclient.c.t : var7;
         return new a5.h(
            var1,
            var2,
            this.q.getValue(),
            this.j.getValue(),
            this.H.getValue(),
            this.w.getValue(),
            var7,
            var8,
            this.x.getValue(),
            this.J.getValue(),
            var6,
            var5 ? a5.p.J : a5.p.l
         );
      } else {
         return null;
      }
   }

   private com.larpclient.n.f k(double var1) {
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

   public boolean q() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && (this.s != null && this.s.glow() > 0.0F || this.o.A().stream().anyMatch(var0 -> var0.glow() > 0.0));
   }

   public void S(fz var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.s != null) {
               JumpFXModule.c var3 = this.s;
               float var4 = var2 ? Math.clamp(var3.glow() * 0.2F, 0.0F, 1.0F) : 1.0F;
               if (var4 > 0.0F) {
                  for (j6.m var6 : this.F.h()) {
                     float var7 = (float)((var3.now() - var6.born()) / var3.duration());
                     if (var7 >= 0.0F && var7 < 1.0F) {
                        var1.q(var6.source(), var3.size(), var7, var6.seed(), var3.primary(), var3.style(), var3.walls(), var4);
                     }
                  }
               }
            }

            var1.blend(_0.v.c);
            double var31 = hz.Q();

            for (a5.h var33 : this.o.A()) {
               if (!var2 || !(var33.glow() <= 0.0)) {
                  double var34 = Math.clamp((var31 - var33.born()) / var33.duration(), 0.0, 1.0);
                  int var9 = vv.Q(var33.from(), var33.to(), var34);
                  int var10 = var33.style() == a5.p.J ? 1 : 0;
                  double var11 = var10 != 0 ? a5.U(var33, var31) : 1.0;

                  for (com.larpclient.n.p var14 : var33.edges()) {
                     double var15 = (var10 != 0 ? a5.i(var33, var31) : a5.z(var33, var14.distance(), var31)) * var33.opacity();
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
                           var19 = b4.T(var33.source().x, var19, var11);
                           var23 = b4.T(var33.source().z, var23, var11);
                           var25 = b4.T(var33.source().x, var25, var11);
                           var29 = b4.T(var33.source().z, var29, var11);
                           var21 = var27 = var33.source().y + 0.012;
                        }

                        g(var1, var33, var2, var9, var15, var19, var21, var23, var25, var27, var29);
                     }
                  }
               }
            }

            var1.blend(_0.v.z);
         }
      }
   }

   private static void g(
      fz var0, a5.h var1, boolean var2, int var3, double var4, double var6, double var8, double var10, double var12, double var14, double var16
   ) {
      if (var2) {
         int var18 = vv.O(var3, var4 * var1.glow() * 0.2);
         var0.E(var6, var8, var10, var12, var14, var16, var1.thickness() * 2.0, var1.thickness() * 2.0, var18, var18);
      } else {
         for (int var22 = 0; var22 < 3 && var1.glow() > 0.0; var22++) {
            double var19 = var1.thickness() * (2.0 + var22 * 1.5);
            int var21 = vv.O(var3, var4 * var1.glow() * 0.065 / (var22 + 1));
            var0.E(var6, var8, var10, var12, var14, var16, var19, var19, var21, var21);
         }

         int var23 = vv.O(vv.Q(var3, var3 & 0xFF000000 | 16777215, 0.4), var4);
         var0.E(var6, var8, var10, var12, var14, var16, var1.thickness(), var1.thickness(), var23, var23);
      }
   }

   private record c(int style, int primary, double size, double duration, boolean walls, float glow, double now) {
   }
}

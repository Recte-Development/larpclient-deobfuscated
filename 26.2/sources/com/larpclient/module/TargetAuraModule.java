package com.larpclient.module;

import com.larpclient.c3;
import com.larpclient.cf;
import com.larpclient.gl;
import com.larpclient.ho;
import com.larpclient.kj;
import com.larpclient.kz;
import com.larpclient.p5;
import com.larpclient.st;
import com.larpclient.u_;
import com.larpclient.uh;
import com.larpclient.zj;
import com.larpclient.zn;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class TargetAuraModule extends Module {
   private static TargetAuraModule z;
   private final l j = this.addSetting(
      new l("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final l r = this.addSetting(new l("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAuraModule.j(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAuraModule.j(var1));
      }
   });
   private final e F = this.addSetting(new e("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final e B = this.addSetting(new e("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final e t = this.addSetting(new e("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final s X = this.addSetting(new s("Bloom", "Explicit glow pass", true));
   private final s I = this.addSetting(new s("Theme Colors", "Follow current theme", true));
   private final i w = this.addSetting(new i("Color", "Primary color", ho.J));
   private final i y = this.addSetting(new i("Gradient", "Secondary color", ho.ds));
   private final gl k = new gl();
   private double U;
   private int W;
   private final p5 R = new p5();
   private final u_.p[] V = new u_.p[18];
   private double i;
   private final ArrayList<TargetAuraModule.c> c;
   private final ArrayList<TargetAuraModule.o> P;
   private boolean x;
   private boolean S;
   private Object d;
   private int D;
   private Vec3 a;

   private static String j(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAuraModule() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.V.length; var1++) {
         this.V[var1] = new u_.p(65);
      }

      this.i = Double.NaN;
      this.c = new ArrayList<>(10);
      this.P = new ArrayList<>(8);
      this.D = -1;
      z = this;
      this.w.P(() -> !this.I.getValue());
      this.y.P(() -> !this.I.getValue());
      this.j.o(var0 -> com.larpclient.h.A("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAuraModule B() {
      return z;
   }

   public List<q<?>> P() {
      return List.of(this.j, this.r, this.F, this.B, this.t, this.X);
   }

   public List<q<?>> C() {
      return List.of(this.I, this.w, this.y);
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.x && this.S;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "TargetAura",
         "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off",
         this.j.getValue(),
         this.B.getValue(),
         this.t.getValue()
      );
   }

   @Override
   public void onTick() {
      LivingEntity var1 = zn.S(this.r.getValue());
      if (var1 == null) {
         this.U = 0.0;
      } else {
         this.v(var1, var1.position());
         this.U = this.U + this.t.getValue() * 0.05;
         int var2 = (int)this.U;
         this.U -= var2;
         boolean var5 = false;
         int var4 = var2;
         int var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 != 0) {
            gl var3 = this.Q(var2, var1.getBbWidth(), var1.getBbHeight());
            kz.J.S(var1.position().add(0.0, this.k(var1.getBbHeight()), 0.0), var3);
         }
      }
   }

   private double k(double var1) {
      if (this.j.O("Halo")) {
         return var1 + 0.22;
      } else {
         return this.j.O("Star of David") && Double.isFinite(this.i) ? this.i : var1 * 0.65;
      }
   }

   public gl Q(int var1, double var2, double var4) {
      this.k.F = 2;
      this.k.u = var1;
      this.k.x = gl.x.l;
      boolean var6 = this.j.O("Star of David");
      this.k.D = this.k.P = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.k.z = var4 * (this.j.O("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.k.j = this.I.getValue() ? ho.J : this.w.getValue();
      this.k.Z = this.I.getValue() ? ho.ds : this.y.getValue();
      this.k.O = 0.85;
      this.k.Q = 1.4;
      this.k.n = 0.055;
      this.k.f = 0.13;
      this.k.L = 128;
      this.k.W = -0.05;
      this.k.s = 0.1;
      this.k.C = 0.12;
      this.k.p = 0.3;
      this.k.v = 1.5;
      this.k.R = this.X.getValue();
      this.k.B = c3.a.Z;
      this.k.i = gl.c.E;
      this.k.V = 1.0;
      if (var6) {
         this.k.v = 0.6;
         this.k.p = 0.18;
      }

      if (!this.j.O("Halo")) {
         this.k.j = uh.F(this.k.j, 0.85);
         this.k.Z = uh.F(this.k.Z, 0.65);
         this.k.f = 0.11;
         if (++this.W % 9 == 0) {
            this.k.B = c3.a.P;
            this.k.n = 0.025;
            this.k.f = 0.045;
            this.k.O = 0.18;
            this.k.Q = 0.35;
            this.k.s = 0.75;
            this.k.V = 0.35;
            this.k.p = 0.08;
            this.k.j = uh.q(this.k.j, this.k.j & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.k;
   }

   public void x(float var1) {
      if (this.isEnabled()) {
         this.x = (boolean)0;
         this.i = Double.NaN;
         this.c.clear();
         this.P.clear();

         for (u_.p var5 : this.V) {
            var5.q();
         }

         if (this.isRuntimeActive()) {
            LivingEntity var24 = zn.S(this.r.getValue());
            double var25 = this.R.E(cf.F(), this.F.getValue());
            if (var24 == null) {
               this.a = null;
               this.D = -1;
            } else {
               Vec3 var26 = var24.getPosition(var1);
               this.v(var24, var26);
               this.x = (boolean)1;
               this.S = this.X.getValue();
               double var6 = var24.getBbWidth() * 0.75 + 0.35;
               double var8 = var24.getBbHeight();
               int var10 = this.I.getValue() ? ho.J : this.w.getValue();
               int var11 = this.I.getValue() ? ho.ds : this.y.getValue();
               if (this.j.O("Orbit") || this.j.O("Helix")) {
                  boolean var27 = this.j.O("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     u_.p var14 = this.V[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = var15 / 64.0;
                        Vec3 var18 = R(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : E(var16);
                        double var21 = this.B.getValue() * var19 * 3.4;
                        int var23 = uh.F(uh.q(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65)));
                        var14.n(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + var13 * Math.PI) : 1.0;
                     this.U(R(var26, var6, var8, var25, var13, var31, var27), this.B.getValue() * (var27 ? 1.6 : 2.1), uh.q(var11, var10, var31));
                  }

                  this.M(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.B.getValue() * 2.0, uh.F(var11, 0.55));
               } else if (this.j.O("Energy")) {
                  this.s(this.V[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                  this.s(this.V[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                  this.U(i(var26, var6, var8 * 0.48, var8, var25, cf.F(), 1.0), this.B.getValue() * 2.1, var10);
                  this.U(i(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, cf.F(), 1.0), this.B.getValue() * 2.1, var11);
                  this.M(var26.add(0.0, 0.055, 0.0), var6 * 1.1, this.B.getValue() * 2.5, uh.F(var10, 0.6));
               } else if (this.j.O("Atom")) {
                  this.w(var26, var6, var8, var25, var10, var11);
               } else if (this.j.O("Vortex")) {
                  this.N(var26, var6, var8, var25, var10, var11);
               } else if (this.j.O("Scanner")) {
                  this.G(var26, var6, var8, var25, var10, var11);
               } else if (this.j.O("Star of David")) {
                  this.F(var26, var6, var8, var25, var10, var11);
               } else {
                  Vec3 var12 = var26.add(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                  this.M(var12, var6 * 1.05, this.B.getValue() * 3.0, uh.F(var10, 0.95));
                  this.M(var12, var6 * 1.05, this.B.getValue() * 6.0, uh.F(var11, 0.25));
                  this.s(this.V[0], var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, var10, var11);
                  this.U(i(var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, cf.F(), 1.0), this.B.getValue() * 1.8, var10);
                  this.M(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.B.getValue() * 2.0, uh.F(var11, 0.55));
               }

               Vec3 var28 = Minecraft.getInstance().gameRenderer.mainCamera().position();

               for (u_.p var33 : this.V) {
                  var33.x(var28);
               }
            }
         }
      }
   }

   private void v(LivingEntity var1, Vec3 var2) {
      Level var3 = var1.level();
      if (this.d != var3 || this.D != var1.getId() || this.a != null && this.a.distanceToSqr(var2) > 64.0) {
         this.U = 0.0;
         kz.J.t(2);
      }

      this.d = var3;
      this.D = var1.getId();
      this.a = var2;
   }

   private void M(Vec3 var1, double var2, double var4, int var6) {
      this.c.add(new TargetAuraModule.c(var1, var2, var4, var6));
   }

   private void U(Vec3 var1, double var2, int var4) {
      this.P.add(new TargetAuraModule.o(var1, var2, uh.q(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static Vec3 R(Vec3 var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.add(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void w(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      Vec3 var10 = var1.add(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.B.getValue();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + var15 * 0.17) + var15 * 2.1;
         int var20 = uh.q(var8, var9, var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.V[var15].n(H(var10, var11, var4, var16, var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, uh.F(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = var24 / 32.0;
            this.V[var15 + 3]
               .n(
                  H(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * E(var22) * 3.2,
                  uh.F(uh.q(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.U(H(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.M(var1.add(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, uh.F(var9, 0.5));
   }

   public static Vec3 H(Vec3 var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.add(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void N(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.B.getValue();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = U(var6 * 0.12 + var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = var15 / 64.0;
            double var18 = e(var16);
            double var20 = Math.exp(-X((var16 - var13) / 0.09));
            this.V[var12]
               .n(
                  r(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  uh.F(uh.q(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.U(r(var1, var2, var4, var6, var12, var13), var10 * 1.9 * e(var13), uh.q(var8, var9, var13));
      }

      this.M(var1.add(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, uh.F(var8, 0.55));
      this.M(var1.add(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, uh.F(var9, 0.32));
   }

   public static Vec3 r(Vec3 var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.add(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double e(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void G(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.B.getValue();
      double var12 = var6 * 0.45;
      double var14 = x(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.M(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, uh.F(var8, 0.95));
      this.M(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, uh.F(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = var22 / 6.0;
         this.M(
            var1.add(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            uh.F(uh.q(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + var25 * Math.PI;
         this.U(var1.add(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double x(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void F(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.B.getValue();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = x(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.i = var20;
      Vec3 var26 = var1.add(0.0, var20, 0.0);
      this.J(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = var27 / 2.0;
         this.J(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var28, 0.0),
            var16 * (1.0 - 0.04 * var28),
            var18,
            var6,
            var8,
            var9,
            (var27 == 1 ? 0.42 : 0.16) * var24,
            0.7,
            var27 * 6,
            false
         );
      }

      this.M(var26, var16 * 1.12, var10 * 2.4, uh.F(var8, 0.85));
      this.M(var26, var16 * 1.24, var10 * 1.2, uh.F(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = var30 / 4.0;
         this.M(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            uh.F(uh.q(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void J(Vec3 var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.B.getValue();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = U(var6 * 0.09 * (var18 == 0 ? 1 : -1) + var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            Vec3 var23 = l(var1, var2, var4, var18 + var22 * 2);
            Vec3 var24 = l(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = var25 / 16.0;
               double var28 = (var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-X(var28 - Math.rint(var28)) / 0.006);
               this.V[var14 + var18 * 3 + var22].n(var23.lerp(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, uh.F(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.U(l(var1, var2, var4, var18 + var33 * 2).lerp(l(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - var33), var16 * 2.3, var19);
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.U(l(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static Vec3 l(Vec3 var0, double var1, double var3, int var5) {
      double var6 = var3 + Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.add(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double U(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double X(double var0) {
      return var0 * var0;
   }

   private static double E(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * st.h(var0 / 0.08);
   }

   private static Vec3 i(Vec3 var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.add(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void X(kj var1, float var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.x && (!var3 || this.S)) {
            long var4 = zj.m();
            var1.blend(gl.c.E);

            for (u_.p var9 : this.V) {
               var1.b(var9, true);
            }

            for (TargetAuraModule.c var12 : this.c) {
               var1.I(var12.center(), var12.radius(), var12.width(), var12.color());
            }

            for (TargetAuraModule.o var13 : this.P) {
               Vec3 var14 = var13.center();
               var1.P(var14.x, var14.y, var14.z, var13.size() * (var3 ? 1.8 : 2.6), 0.0, uh.F(var13.color(), var3 ? 0.35 : 0.18));
               if (!var3) {
                  var1.P(var14.x, var14.y, var14.z, var13.size(), 0.0, var13.color());
               }
            }

            var1.blend(gl.c.K);
            zj.l("TargetAura", var4);
         }
      }
   }

   private void s(u_.p var1, Vec3 var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = var13 / 32.0;
         Vec3 var16 = i(var2, var3, var5, var7, var9, cf.F(), var14);
         double var17 = this.B.getValue() * E(var14) * 3.4;
         int var19 = uh.F(uh.q(var12, var11, var14), Math.pow(var14, 0.65));
         var1.n(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.U = 0.0;
      this.W = 0;
      this.x = (boolean)0;
      this.i = Double.NaN;
      this.a = null;
      this.d = null;
      this.D = -1;
      this.c.clear();
      this.P.clear();

      for (u_.p var4 : this.V) {
         var4.q();
      }

      this.R.x();
      kz.J.t(2);
   }

   private record c(Vec3 center, double radius, double width, int color) {
   }

   private record o(Vec3 center, double size, int color) {
   }
}

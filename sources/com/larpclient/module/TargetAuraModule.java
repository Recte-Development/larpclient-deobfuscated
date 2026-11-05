package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.ef;
import com.larpclient.f4;
import com.larpclient.f8;
import com.larpclient.fk;
import com.larpclient.h3;
import com.larpclient.h_;
import com.larpclient.hj;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rv;
import com.larpclient.yc;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class TargetAuraModule extends Module {
   private static TargetAuraModule W;
   private final lp p = this.addSetting(
      new lp("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final lp w = this.addSetting(new lp("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAuraModule.C(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAuraModule.C(var1));
      }
   });
   private final lc d = this.addSetting(new lc("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final lc a = this.addSetting(new lc("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final lc q = this.addSetting(new lc("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final ln o = this.addSetting(new ln("Bloom", "Explicit glow pass", true));
   private final ln t = this.addSetting(new ln("Theme Colors", "Follow current theme", true));
   private final lv F = this.addSetting(new lv("Color", "Primary color", rv.e));
   private final lv X = this.addSetting(new lv("Gradient", "Secondary color", rv.GQ));
   private final fk P = new fk();
   private double Z;
   private int Q;
   private final hj b = new hj();
   private final h3.b[] r = new h3.b[18];
   private double c;
   private final ArrayList<TargetAuraModule.r> J;
   private final ArrayList<TargetAuraModule.l> E;
   private boolean f;
   private boolean M;
   private Object T;
   private int h;
   private Vec3 D;

   private static String C(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAuraModule() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.r.length; var1++) {
         this.r[var1] = new h3.b(65);
      }

      this.c = Double.NaN;
      this.J = new ArrayList<>(10);
      this.E = new ArrayList<>(8);
      this.h = -1;
      W = this;
      this.F.x(() -> !this.t.getValue());
      this.X.x(() -> !this.t.getValue());
      this.p.W(var0 -> r4.V("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAuraModule X() {
      return W;
   }

   public List<l6<?>> c() {
      return List.of(this.p, this.w, this.d, this.a, this.q, this.o);
   }

   public List<l6<?>> E() {
      return List.of(this.t, this.F, this.X);
   }

   public boolean i() {
      return !this.isEnabled() ? false : this.f && this.M;
   }

   @Override
   public void onEnable() {
      r4.V(
         "TargetAura",
         "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off",
         this.p.getValue(),
         this.a.getValue(),
         this.q.getValue()
      );
   }

   @Override
   public void onTick() {
      LivingEntity var1 = ef.j(this.w.getValue());
      if (var1 == null) {
         this.Z = 0.0;
      } else {
         this.w(var1, var1.position());
         this.Z = this.Z + this.q.getValue() * 0.05;
         int var2 = (int)this.Z;
         this.Z -= var2;
         boolean var5 = false;
         int var4 = var2;
         int var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 != 0) {
            fk var3 = this.i(var2, var1.getBbWidth(), var1.getBbHeight());
            f8.D.p(var1.position().add(0.0, this.o(var1.getBbHeight()), 0.0), var3);
         }
      }
   }

   private double o(double var1) {
      if (this.p.i("Halo")) {
         return var1 + 0.22;
      } else {
         return this.p.i("Star of David") && Double.isFinite(this.c) ? this.c : var1 * 0.65;
      }
   }

   public fk i(int var1, double var2, double var4) {
      this.P.g = 2;
      this.P.t = var1;
      this.P.m = fk.y.f;
      boolean var6 = this.p.i("Star of David");
      this.P.E = this.P.j = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.P.r = var4 * (this.p.i("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.P.B = this.t.getValue() ? rv.e : this.F.getValue();
      this.P.v = this.t.getValue() ? rv.GQ : this.X.getValue();
      this.P.Y = 0.85;
      this.P.d = 1.4;
      this.P.V = 0.055;
      this.P.u = 0.13;
      this.P.p = 128;
      this.P.H = -0.05;
      this.P.e = 0.1;
      this.P.k = 0.12;
      this.P.s = 0.3;
      this.P.n = 1.5;
      this.P.G = this.o.getValue();
      this.P.D = f4.a.u;
      this.P.C = fk.h.u;
      this.P.F = 1.0;
      if (var6) {
         this.P.n = 0.6;
         this.P.s = 0.18;
      }

      if (!this.p.i("Halo")) {
         this.P.B = oh.S(this.P.B, 0.85);
         this.P.v = oh.S(this.P.v, 0.65);
         this.P.u = 0.11;
         if (++this.Q % 9 == 0) {
            this.P.D = f4.a.d;
            this.P.V = 0.025;
            this.P.u = 0.045;
            this.P.Y = 0.18;
            this.P.d = 0.35;
            this.P.e = 0.75;
            this.P.F = 0.35;
            this.P.s = 0.08;
            this.P.B = oh.j(this.P.B, this.P.B & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.P;
   }

   public void r(float var1) {
      if (this.isEnabled()) {
         this.f = (boolean)0;
         this.c = Double.NaN;
         this.J.clear();
         this.E.clear();

         for (h3.b var5 : this.r) {
            var5.H();
         }

         if (this.isRuntimeActive()) {
            LivingEntity var24 = ef.j(this.w.getValue());
            double var25 = this.b.d(ea.y(), this.d.getValue());
            if (var24 == null) {
               this.D = null;
               this.h = -1;
            } else {
               Vec3 var26 = var24.getPosition(var1);
               this.w(var24, var26);
               this.f = (boolean)1;
               this.M = this.o.getValue();
               double var6 = var24.getBbWidth() * 0.75 + 0.35;
               double var8 = var24.getBbHeight();
               int var10 = this.t.getValue() ? rv.e : this.F.getValue();
               int var11 = this.t.getValue() ? rv.GQ : this.X.getValue();
               if (this.p.i("Orbit") || this.p.i("Helix")) {
                  boolean var27 = this.p.i("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     h3.b var14 = this.r[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = var15 / 64.0;
                        Vec3 var18 = h(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : w(var16);
                        double var21 = this.a.getValue() * var19 * 3.4;
                        int var23 = oh.S(oh.j(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65)));
                        var14.P(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + var13 * Math.PI) : 1.0;
                     this.q(h(var26, var6, var8, var25, var13, var31, var27), this.a.getValue() * (var27 ? 1.6 : 2.1), oh.j(var11, var10, var31));
                  }

                  this.v(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.a.getValue() * 2.0, oh.S(var11, 0.55));
               } else if (this.p.i("Energy")) {
                  this.K(this.r[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                  this.K(this.r[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                  this.q(S(var26, var6, var8 * 0.48, var8, var25, ea.y(), 1.0), this.a.getValue() * 2.1, var10);
                  this.q(S(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, ea.y(), 1.0), this.a.getValue() * 2.1, var11);
                  this.v(var26.add(0.0, 0.055, 0.0), var6 * 1.1, this.a.getValue() * 2.5, oh.S(var10, 0.6));
               } else if (this.p.i("Atom")) {
                  this.e(var26, var6, var8, var25, var10, var11);
               } else if (this.p.i("Vortex")) {
                  this.J(var26, var6, var8, var25, var10, var11);
               } else if (this.p.i("Scanner")) {
                  this.x(var26, var6, var8, var25, var10, var11);
               } else if (this.p.i("Star of David")) {
                  this.B(var26, var6, var8, var25, var10, var11);
               } else {
                  Vec3 var12 = var26.add(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                  this.v(var12, var6 * 1.05, this.a.getValue() * 3.0, oh.S(var10, 0.95));
                  this.v(var12, var6 * 1.05, this.a.getValue() * 6.0, oh.S(var11, 0.25));
                  this.K(this.r[0], var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, var10, var11);
                  this.q(S(var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, ea.y(), 1.0), this.a.getValue() * 1.8, var10);
                  this.v(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.a.getValue() * 2.0, oh.S(var11, 0.55));
               }

               Vec3 var28 = Minecraft.getInstance().gameRenderer.mainCamera().position();

               for (h3.b var33 : this.r) {
                  var33.n(var28);
               }
            }
         }
      }
   }

   private void w(LivingEntity var1, Vec3 var2) {
      Level var3 = var1.level();
      if (this.T != var3 || this.h != var1.getId() || this.D != null && this.D.distanceToSqr(var2) > 64.0) {
         this.Z = 0.0;
         f8.D.A(2);
      }

      this.T = var3;
      this.h = var1.getId();
      this.D = var2;
   }

   private void v(Vec3 var1, double var2, double var4, int var6) {
      this.J.add(new TargetAuraModule.r(var1, var2, var4, var6));
   }

   private void q(Vec3 var1, double var2, int var4) {
      this.E.add(new TargetAuraModule.l(var1, var2, oh.j(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static Vec3 h(Vec3 var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.add(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void e(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      Vec3 var10 = var1.add(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.a.getValue();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + var15 * 0.17) + var15 * 2.1;
         int var20 = oh.j(var8, var9, var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.r[var15].P(Y(var10, var11, var4, var16, var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, oh.S(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = var24 / 32.0;
            this.r[var15 + 3]
               .P(
                  Y(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * w(var22) * 3.2,
                  oh.S(oh.j(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.q(Y(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.v(var1.add(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, oh.S(var9, 0.5));
   }

   public static Vec3 Y(Vec3 var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.add(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void J(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.a.getValue();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = r(var6 * 0.12 + var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = var15 / 64.0;
            double var18 = A(var16);
            double var20 = Math.exp(-z((var16 - var13) / 0.09));
            this.r[var12]
               .P(
                  M(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  oh.S(oh.j(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.q(M(var1, var2, var4, var6, var12, var13), var10 * 1.9 * A(var13), oh.j(var8, var9, var13));
      }

      this.v(var1.add(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, oh.S(var8, 0.55));
      this.v(var1.add(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, oh.S(var9, 0.32));
   }

   public static Vec3 M(Vec3 var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.add(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double A(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void x(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.a.getValue();
      double var12 = var6 * 0.45;
      double var14 = f(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.v(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, oh.S(var8, 0.95));
      this.v(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, oh.S(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = var22 / 6.0;
         this.v(
            var1.add(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            oh.S(oh.j(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + var25 * Math.PI;
         this.q(var1.add(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double f(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void B(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.a.getValue();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = f(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.c = var20;
      Vec3 var26 = var1.add(0.0, var20, 0.0);
      this.R(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = var27 / 2.0;
         this.R(
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

      this.v(var26, var16 * 1.12, var10 * 2.4, oh.S(var8, 0.85));
      this.v(var26, var16 * 1.24, var10 * 1.2, oh.S(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = var30 / 4.0;
         this.v(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            oh.S(oh.j(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void R(Vec3 var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.a.getValue();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = r(var6 * 0.09 * (var18 == 0 ? 1 : -1) + var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            Vec3 var23 = c(var1, var2, var4, var18 + var22 * 2);
            Vec3 var24 = c(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = var25 / 16.0;
               double var28 = (var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-z(var28 - Math.rint(var28)) / 0.006);
               this.r[var14 + var18 * 3 + var22].P(var23.lerp(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, oh.S(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.q(c(var1, var2, var4, var18 + var33 * 2).lerp(c(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - var33), var16 * 2.3, var19);
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.q(c(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static Vec3 c(Vec3 var0, double var1, double var3, int var5) {
      double var6 = var3 + Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.add(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double r(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double z(double var0) {
      return var0 * var0;
   }

   private static double w(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * yc.H(var0 / 0.08);
   }

   private static Vec3 S(Vec3 var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.add(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void l(r6 var1, float var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.f && (!var3 || this.M)) {
            long var4 = h_.U();
            var1.blend(fk.h.u);

            for (h3.b var9 : this.r) {
               var1.y(var9, true);
            }

            for (TargetAuraModule.r var12 : this.J) {
               var1.C(var12.center(), var12.radius(), var12.width(), var12.color());
            }

            for (TargetAuraModule.l var13 : this.E) {
               Vec3 var14 = var13.center();
               var1.m(var14.x, var14.y, var14.z, var13.size() * (var3 ? 1.8 : 2.6), 0.0, oh.S(var13.color(), var3 ? 0.35 : 0.18));
               if (!var3) {
                  var1.m(var14.x, var14.y, var14.z, var13.size(), 0.0, var13.color());
               }
            }

            var1.blend(fk.h.z);
            h_.n("TargetAura", var4);
         }
      }
   }

   private void K(h3.b var1, Vec3 var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = var13 / 32.0;
         Vec3 var16 = S(var2, var3, var5, var7, var9, ea.y(), var14);
         double var17 = this.a.getValue() * w(var14) * 3.4;
         int var19 = oh.S(oh.j(var12, var11, var14), Math.pow(var14, 0.65));
         var1.P(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.Z = 0.0;
      this.Q = 0;
      this.f = (boolean)0;
      this.c = Double.NaN;
      this.D = null;
      this.T = null;
      this.h = -1;
      this.J.clear();
      this.E.clear();

      for (h3.b var4 : this.r) {
         var4.H();
      }

      this.b.q();
      f8.D.A(2);
   }

   private record l(Vec3 center, double size, int color) {
   }

   private record r(Vec3 center, double radius, double width, int color) {
   }
}

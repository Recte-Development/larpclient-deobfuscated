package com.larpclient.module;

import com.larpclient._0;
import com.larpclient._q;
import com.larpclient.b0;
import com.larpclient.fz;
import com.larpclient.he;
import com.larpclient.hz;
import com.larpclient.pr;
import com.larpclient.vg;
import com.larpclient.vv;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class TargetAuraModule extends Module {
   private static TargetAuraModule H;
   private final o D = this.addSetting(
      new o("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final o y = this.addSetting(new o("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAuraModule.c(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAuraModule.c(var1));
      }
   });
   private final b g = this.addSetting(new b("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final b L = this.addSetting(new b("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final b J = this.addSetting(new b("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final m e = this.addSetting(new m("Bloom", "Explicit glow pass", true));
   private final m c = this.addSetting(new m("Theme Colors", "Follow current theme", true));
   private final h I = this.addSetting(new h("Color", "Primary color", com.larpclient.c.y));
   private final h o = this.addSetting(new h("Gradient", "Secondary color", com.larpclient.c.t));
   private final _0 i = new _0();
   private double U;
   private int P;
   private final b0 Z = new b0();
   private final com.larpclient.x.c[] s = new com.larpclient.x.c[18];
   private double p;
   private final ArrayList<TargetAuraModule.j> S;
   private final ArrayList<TargetAuraModule.d> k;
   private boolean W;
   private boolean M;
   private Object X;
   private int G;
   private Vec3 x;

   private static String c(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAuraModule() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.s.length; var1++) {
         this.s[var1] = new com.larpclient.x.c(65);
      }

      this.p = Double.NaN;
      this.S = new ArrayList<>(10);
      this.k = new ArrayList<>(8);
      this.G = -1;
      H = this;
      this.I.a(() -> !this.c.getValue());
      this.o.a(() -> !this.c.getValue());
      this.D.w(var0 -> _q.z("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAuraModule v() {
      return H;
   }

   public List<r<?>> G() {
      return List.of(this.D, this.y, this.g, this.L, this.J, this.e);
   }

   public List<r<?>> A() {
      return List.of(this.c, this.I, this.o);
   }

   public boolean q() {
      return !this.isEnabled() ? false : this.W && this.M;
   }

   @Override
   public void onEnable() {
      _q.z(
         "TargetAura",
         "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off",
         this.D.getValue(),
         this.L.getValue(),
         this.J.getValue()
      );
   }

   @Override
   public void onTick() {
      LivingEntity var1 = vg.j(this.y.getValue());
      if (var1 == null) {
         this.U = 0.0;
      } else {
         this.J(var1, var1.position());
         this.U = this.U + this.J.getValue() * 0.05;
         int var2 = (int)this.U;
         this.U -= var2;
         boolean var5 = false;
         int var4 = var2;
         int var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 != 0) {
            _0 var3 = this.P(var2, var1.getBbWidth(), var1.getBbHeight());
            wp.Q.Q(var1.position().add(0.0, this.x(var1.getBbHeight()), 0.0), var3);
         }
      }
   }

   private double x(double var1) {
      if (this.D.w("Halo")) {
         return var1 + 0.22;
      } else {
         return this.D.w("Star of David") && Double.isFinite(this.p) ? this.p : var1 * 0.65;
      }
   }

   public _0 P(int var1, double var2, double var4) {
      this.i.r = 2;
      this.i.w = var1;
      this.i.D = _0.y.y;
      boolean var6 = this.D.w("Star of David");
      this.i.l = this.i.e = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.i.x = var4 * (this.D.w("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.i.F = this.c.getValue() ? com.larpclient.c.y : this.I.getValue();
      this.i.t = this.c.getValue() ? com.larpclient.c.t : this.o.getValue();
      this.i.Q = 0.85;
      this.i.W = 1.4;
      this.i.i = 0.055;
      this.i.m = 0.13;
      this.i.A = 128;
      this.i.n = -0.05;
      this.i.I = 0.1;
      this.i.U = 0.12;
      this.i.K = 0.3;
      this.i.s = 1.5;
      this.i.M = this.e.getValue();
      this.i.L = com.larpclient.l.b.P;
      this.i.j = _0.v.c;
      this.i.O = 1.0;
      if (var6) {
         this.i.s = 0.6;
         this.i.K = 0.18;
      }

      if (!this.D.w("Halo")) {
         this.i.F = vv.O(this.i.F, 0.85);
         this.i.t = vv.O(this.i.t, 0.65);
         this.i.m = 0.11;
         if (++this.P % 9 == 0) {
            this.i.L = com.larpclient.l.b.i;
            this.i.i = 0.025;
            this.i.m = 0.045;
            this.i.Q = 0.18;
            this.i.W = 0.35;
            this.i.I = 0.75;
            this.i.O = 0.35;
            this.i.K = 0.08;
            this.i.F = vv.Q(this.i.F, this.i.F & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.i;
   }

   public void Q(float var1) {
      if (this.isEnabled()) {
         this.W = (boolean)0;
         this.p = Double.NaN;
         this.S.clear();
         this.k.clear();

         for (com.larpclient.x.c var5 : this.s) {
            var5.w();
         }

         if (this.isRuntimeActive()) {
            LivingEntity var24 = vg.j(this.y.getValue());
            double var25 = this.Z.v(hz.Q(), this.g.getValue());
            if (var24 == null) {
               this.x = null;
               this.G = -1;
            } else {
               Vec3 var26 = var24.getPosition(var1);
               this.J(var24, var26);
               this.W = (boolean)1;
               this.M = this.e.getValue();
               double var6 = var24.getBbWidth() * 0.75 + 0.35;
               double var8 = var24.getBbHeight();
               int var10 = this.c.getValue() ? com.larpclient.c.y : this.I.getValue();
               int var11 = this.c.getValue() ? com.larpclient.c.t : this.o.getValue();
               if (this.D.w("Orbit") || this.D.w("Helix")) {
                  boolean var27 = this.D.w("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     com.larpclient.x.c var14 = this.s[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = var15 / 64.0;
                        Vec3 var18 = S(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : q(var16);
                        double var21 = this.L.getValue() * var19 * 3.4;
                        int var23 = vv.O(vv.Q(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65)));
                        var14.J(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + var13 * Math.PI) : 1.0;
                     this.I(S(var26, var6, var8, var25, var13, var31, var27), this.L.getValue() * (var27 ? 1.6 : 2.1), vv.Q(var11, var10, var31));
                  }

                  this.A(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.L.getValue() * 2.0, vv.O(var11, 0.55));
               } else if (this.D.w("Energy")) {
                  this.I(this.s[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                  this.I(this.s[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                  this.I(X(var26, var6, var8 * 0.48, var8, var25, hz.Q(), 1.0), this.L.getValue() * 2.1, var10);
                  this.I(X(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, hz.Q(), 1.0), this.L.getValue() * 2.1, var11);
                  this.A(var26.add(0.0, 0.055, 0.0), var6 * 1.1, this.L.getValue() * 2.5, vv.O(var10, 0.6));
               } else if (this.D.w("Atom")) {
                  this.Y(var26, var6, var8, var25, var10, var11);
               } else if (this.D.w("Vortex")) {
                  this.m(var26, var6, var8, var25, var10, var11);
               } else if (this.D.w("Scanner")) {
                  this.F(var26, var6, var8, var25, var10, var11);
               } else if (this.D.w("Star of David")) {
                  this.B(var26, var6, var8, var25, var10, var11);
               } else {
                  Vec3 var12 = var26.add(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                  this.A(var12, var6 * 1.05, this.L.getValue() * 3.0, vv.O(var10, 0.95));
                  this.A(var12, var6 * 1.05, this.L.getValue() * 6.0, vv.O(var11, 0.25));
                  this.I(this.s[0], var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, var10, var11);
                  this.I(X(var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, hz.Q(), 1.0), this.L.getValue() * 1.8, var10);
                  this.A(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.L.getValue() * 2.0, vv.O(var11, 0.55));
               }

               Vec3 var28 = Minecraft.getInstance().gameRenderer.mainCamera().position();

               for (com.larpclient.x.c var33 : this.s) {
                  var33.T(var28);
               }
            }
         }
      }
   }

   private void J(LivingEntity var1, Vec3 var2) {
      Level var3 = var1.level();
      if (this.X != var3 || this.G != var1.getId() || this.x != null && this.x.distanceToSqr(var2) > 64.0) {
         this.U = 0.0;
         wp.Q.c(2);
      }

      this.X = var3;
      this.G = var1.getId();
      this.x = var2;
   }

   private void A(Vec3 var1, double var2, double var4, int var6) {
      this.S.add(new TargetAuraModule.j(var1, var2, var4, var6));
   }

   private void I(Vec3 var1, double var2, int var4) {
      this.k.add(new TargetAuraModule.d(var1, var2, vv.Q(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static Vec3 S(Vec3 var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.add(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void Y(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      Vec3 var10 = var1.add(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.L.getValue();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + var15 * 0.17) + var15 * 2.1;
         int var20 = vv.Q(var8, var9, var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.s[var15].J(C(var10, var11, var4, var16, var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, vv.O(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = var24 / 32.0;
            this.s[var15 + 3]
               .J(
                  C(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * q(var22) * 3.2,
                  vv.O(vv.Q(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.I(C(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.A(var1.add(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, vv.O(var9, 0.5));
   }

   public static Vec3 C(Vec3 var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.add(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void m(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.L.getValue();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = o(var6 * 0.12 + var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = var15 / 64.0;
            double var18 = S(var16);
            double var20 = Math.exp(-Q((var16 - var13) / 0.09));
            this.s[var12]
               .J(
                  h(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  vv.O(vv.Q(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.I(h(var1, var2, var4, var6, var12, var13), var10 * 1.9 * S(var13), vv.Q(var8, var9, var13));
      }

      this.A(var1.add(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, vv.O(var8, 0.55));
      this.A(var1.add(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, vv.O(var9, 0.32));
   }

   public static Vec3 h(Vec3 var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.add(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double S(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void F(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.L.getValue();
      double var12 = var6 * 0.45;
      double var14 = Q(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.A(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, vv.O(var8, 0.95));
      this.A(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, vv.O(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = var22 / 6.0;
         this.A(
            var1.add(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            vv.O(vv.Q(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + var25 * Math.PI;
         this.I(var1.add(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double Q(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void B(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.L.getValue();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = Q(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.p = var20;
      Vec3 var26 = var1.add(0.0, var20, 0.0);
      this.v(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = var27 / 2.0;
         this.v(
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

      this.A(var26, var16 * 1.12, var10 * 2.4, vv.O(var8, 0.85));
      this.A(var26, var16 * 1.24, var10 * 1.2, vv.O(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = var30 / 4.0;
         this.A(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            vv.O(vv.Q(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void v(Vec3 var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.L.getValue();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = o(var6 * 0.09 * (var18 == 0 ? 1 : -1) + var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            Vec3 var23 = z(var1, var2, var4, var18 + var22 * 2);
            Vec3 var24 = z(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = var25 / 16.0;
               double var28 = (var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-Q(var28 - Math.rint(var28)) / 0.006);
               this.s[var14 + var18 * 3 + var22].J(var23.lerp(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, vv.O(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.I(z(var1, var2, var4, var18 + var33 * 2).lerp(z(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - var33), var16 * 2.3, var19);
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.I(z(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static Vec3 z(Vec3 var0, double var1, double var3, int var5) {
      double var6 = var3 + Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.add(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double o(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double Q(double var0) {
      return var0 * var0;
   }

   private static double q(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * pr.k(var0 / 0.08);
   }

   private static Vec3 X(Vec3 var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.add(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void s(fz var1, float var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.W && (!var3 || this.M)) {
            long var4 = he.s();
            var1.blend(_0.v.c);

            for (com.larpclient.x.c var9 : this.s) {
               var1.n(var9, true);
            }

            for (TargetAuraModule.j var12 : this.S) {
               var1.q(var12.center(), var12.radius(), var12.width(), var12.color());
            }

            for (TargetAuraModule.d var13 : this.k) {
               Vec3 var14 = var13.center();
               var1.K(var14.x, var14.y, var14.z, var13.size() * (var3 ? 1.8 : 2.6), 0.0, vv.O(var13.color(), var3 ? 0.35 : 0.18));
               if (!var3) {
                  var1.K(var14.x, var14.y, var14.z, var13.size(), 0.0, var13.color());
               }
            }

            var1.blend(_0.v.z);
            he.T("TargetAura", var4);
         }
      }
   }

   private void I(com.larpclient.x.c var1, Vec3 var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = var13 / 32.0;
         Vec3 var16 = X(var2, var3, var5, var7, var9, hz.Q(), var14);
         double var17 = this.L.getValue() * q(var14) * 3.4;
         int var19 = vv.O(vv.Q(var12, var11, var14), Math.pow(var14, 0.65));
         var1.J(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.U = 0.0;
      this.P = 0;
      this.W = (boolean)0;
      this.p = Double.NaN;
      this.x = null;
      this.X = null;
      this.G = -1;
      this.S.clear();
      this.k.clear();

      for (com.larpclient.x.c var4 : this.s) {
         var4.w();
      }

      this.Z.a();
      wp.Q.c(2);
   }

   private record d(Vec3 center, double size, int color) {
   }

   private record j(Vec3 center, double radius, double width, int color) {
   }
}

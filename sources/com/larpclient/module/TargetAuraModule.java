package com.larpclient.module;

import com.larpclient._3;
import com.larpclient._5;
import com.larpclient.__;
import com.larpclient.cg;
import com.larpclient.h;
import com.larpclient.hp;
import com.larpclient.ki;
import com.larpclient.kx;
import com.larpclient.mt;
import com.larpclient.pf;
import com.larpclient.ty;
import com.larpclient.uv;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class TargetAuraModule extends Module {
   private static TargetAuraModule N;
   private final d6 b = this.addSetting(
      new d6("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final d6 e = this.addSetting(new d6("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAuraModule.p(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAuraModule.p(var1));
      }
   });
   private final d9 r = this.addSetting(new d9("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final d9 t = this.addSetting(new d9("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final d9 i = this.addSetting(new d9("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final dh E = this.addSetting(new dh("Bloom", "Explicit glow pass", true));
   private final dh n = this.addSetting(new dh("Theme Colors", "Follow current theme", true));
   private final dk C = this.addSetting(new dk("Color", "Primary color", com.larpclient.f.C));
   private final dk L = this.addSetting(new dk("Gradient", "Secondary color", com.larpclient.f.O));
   private final pf S = new pf();
   private double W;
   private int Y;
   private final _5 U = new _5();
   private final __.l[] B = new __.l[18];
   private double x;
   private final ArrayList<TargetAuraModule.p> M;
   private final ArrayList<TargetAuraModule.u> Z;
   private boolean o;
   private boolean G;
   private Object Q;
   private int a;
   private Vec3 q;

   private static String p(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAuraModule() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.B.length; var1++) {
         this.B[var1] = new __.l(65);
      }

      this.x = Double.NaN;
      this.M = new ArrayList<>(10);
      this.Z = new ArrayList<>(8);
      this.a = -1;
      N = this;
      this.C.H(() -> !this.n.P());
      this.L.H(() -> !this.n.P());
      this.b.h(var0 -> mt.K("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAuraModule k() {
      return N;
   }

   public List<df<?>> l() {
      return List.of(this.b, this.e, this.r, this.t, this.i, this.E);
   }

   public List<df<?>> V() {
      return List.of(this.n, this.C, this.L);
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.o && this.G;
   }

   @Override
   public void onEnable() {
      mt.K("TargetAura", "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off", this.b.P(), this.t.P(), this.i.P());
   }

   @Override
   public void onTick() {
      LivingEntity var1 = uv.Z(this.e.P());
      if (var1 == null) {
         this.W = 0.0;
      } else {
         this.O(var1, var1.position());
         this.W = this.W + this.i.P() * 0.05;
         int var2 = (int)this.W;
         this.W -= var2;
         boolean var5 = false;
         int var4 = var2;
         int var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 != 0) {
            pf var3 = this.k(var2, var1.getBbWidth(), var1.getBbHeight());
            h.P.i(var1.position().add(0.0, this.C(var1.getBbHeight()), 0.0), var3);
         }
      }
   }

   private double C(double var1) {
      if (this.b.S("Halo")) {
         return var1 + 0.22;
      } else {
         return this.b.S("Star of David") && Double.isFinite(this.x) ? this.x : var1 * 0.65;
      }
   }

   public pf k(int var1, double var2, double var4) {
      this.S.D = 2;
      this.S.g = var1;
      this.S.A = pf.r.e;
      boolean var6 = this.b.S("Star of David");
      this.S.F = this.S.U = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.S.I = var4 * (this.b.S("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.S.E = this.n.P() ? com.larpclient.f.C : this.C.P();
      this.S.N = this.n.P() ? com.larpclient.f.O : this.L.P();
      this.S.M = 0.85;
      this.S.O = 1.4;
      this.S.C = 0.055;
      this.S.d = 0.13;
      this.S.X = 128;
      this.S.W = -0.05;
      this.S.Z = 0.1;
      this.S.r = 0.12;
      this.S.f = 0.3;
      this.S.J = 1.5;
      this.S.i = this.E.P();
      this.S.P = cg.u.a;
      this.S.p = pf.x.E;
      this.S.b = 1.0;
      if (var6) {
         this.S.J = 0.6;
         this.S.f = 0.18;
      }

      if (!this.b.S("Halo")) {
         this.S.E = _3.U(this.S.E, 0.85);
         this.S.N = _3.U(this.S.N, 0.65);
         this.S.d = 0.11;
         if (++this.Y % 9 == 0) {
            this.S.P = cg.u.M;
            this.S.C = 0.025;
            this.S.d = 0.045;
            this.S.M = 0.18;
            this.S.O = 0.35;
            this.S.Z = 0.75;
            this.S.b = 0.35;
            this.S.f = 0.08;
            this.S.E = _3.D(this.S.E, this.S.E & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.S;
   }

   public void N(float var1) {
      if (this.isEnabled()) {
         this.o = (boolean)0;
         this.x = Double.NaN;
         this.M.clear();
         this.Z.clear();

         for (__.l var5 : this.B) {
            var5.y();
         }

         if (this.isRuntimeActive()) {
            LivingEntity var24 = uv.Z(this.e.P());
            double var25 = this.U.p(ty.K(), this.r.P());
            if (var24 == null) {
               this.q = null;
               this.a = -1;
            } else {
               Vec3 var26 = var24.getPosition(var1);
               this.O(var24, var26);
               this.o = (boolean)1;
               this.G = this.E.P();
               double var6 = var24.getBbWidth() * 0.75 + 0.35;
               double var8 = var24.getBbHeight();
               int var10 = this.n.P() ? com.larpclient.f.C : this.C.P();
               int var11 = this.n.P() ? com.larpclient.f.O : this.L.P();
               if (this.b.S("Orbit") || this.b.S("Helix")) {
                  boolean var27 = this.b.S("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     __.l var14 = this.B[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = var15 / 64.0;
                        Vec3 var18 = A(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : K(var16);
                        double var21 = this.t.P() * var19 * 3.4;
                        int var23 = _3.U(_3.D(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65)));
                        var14.y(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + var13 * Math.PI) : 1.0;
                     this.X(A(var26, var6, var8, var25, var13, var31, var27), this.t.P() * (var27 ? 1.6 : 2.1), _3.D(var11, var10, var31));
                  }

                  this.O(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.t.P() * 2.0, _3.U(var11, 0.55));
               } else if (this.b.S("Energy")) {
                  this.H(this.B[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                  this.H(this.B[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                  this.X(t(var26, var6, var8 * 0.48, var8, var25, ty.K(), 1.0), this.t.P() * 2.1, var10);
                  this.X(t(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, ty.K(), 1.0), this.t.P() * 2.1, var11);
                  this.O(var26.add(0.0, 0.055, 0.0), var6 * 1.1, this.t.P() * 2.5, _3.U(var10, 0.6));
               } else if (this.b.S("Atom")) {
                  this.a(var26, var6, var8, var25, var10, var11);
               } else if (this.b.S("Vortex")) {
                  this.K(var26, var6, var8, var25, var10, var11);
               } else if (this.b.S("Scanner")) {
                  this.p(var26, var6, var8, var25, var10, var11);
               } else if (this.b.S("Star of David")) {
                  this.Y(var26, var6, var8, var25, var10, var11);
               } else {
                  Vec3 var12 = var26.add(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                  this.O(var12, var6 * 1.05, this.t.P() * 3.0, _3.U(var10, 0.95));
                  this.O(var12, var6 * 1.05, this.t.P() * 6.0, _3.U(var11, 0.25));
                  this.H(this.B[0], var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, var10, var11);
                  this.X(t(var26, var6 * 1.05, var12.y - var26.y, var8 * 0.08, var25 * 0.65, ty.K(), 1.0), this.t.P() * 1.8, var10);
                  this.O(var26.add(0.0, 0.055, 0.0), var6 * 1.05, this.t.P() * 2.0, _3.U(var11, 0.55));
               }

               Vec3 var28 = Minecraft.getInstance().gameRenderer.mainCamera().position();

               for (__.l var33 : this.B) {
                  var33.a(var28);
               }
            }
         }
      }
   }

   private void O(LivingEntity var1, Vec3 var2) {
      Level var3 = var1.level();
      if (this.Q != var3 || this.a != var1.getId() || this.q != null && this.q.distanceToSqr(var2) > 64.0) {
         this.W = 0.0;
         h.P.b(2);
      }

      this.Q = var3;
      this.a = var1.getId();
      this.q = var2;
   }

   private void O(Vec3 var1, double var2, double var4, int var6) {
      this.M.add(new TargetAuraModule.p(var1, var2, var4, var6));
   }

   private void X(Vec3 var1, double var2, int var4) {
      this.Z.add(new TargetAuraModule.u(var1, var2, _3.D(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static Vec3 A(Vec3 var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.add(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void a(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      Vec3 var10 = var1.add(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.t.P();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + var15 * 0.17) + var15 * 2.1;
         int var20 = _3.D(var8, var9, var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.B[var15].y(j(var10, var11, var4, var16, var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, _3.U(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = var24 / 32.0;
            this.B[var15 + 3]
               .y(
                  j(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * K(var22) * 3.2,
                  _3.U(_3.D(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.X(j(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.O(var1.add(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, _3.U(var9, 0.5));
   }

   public static Vec3 j(Vec3 var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.add(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void K(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.t.P();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = Y(var6 * 0.12 + var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = var15 / 64.0;
            double var18 = Z(var16);
            double var20 = Math.exp(-S((var16 - var13) / 0.09));
            this.B[var12]
               .y(
                  r(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  _3.U(_3.D(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.X(r(var1, var2, var4, var6, var12, var13), var10 * 1.9 * Z(var13), _3.D(var8, var9, var13));
      }

      this.O(var1.add(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, _3.U(var8, 0.55));
      this.O(var1.add(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, _3.U(var9, 0.32));
   }

   public static Vec3 r(Vec3 var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.add(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double Z(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void p(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.t.P();
      double var12 = var6 * 0.45;
      double var14 = M(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.O(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, _3.U(var8, 0.95));
      this.O(var1.add(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, _3.U(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = var22 / 6.0;
         this.O(
            var1.add(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            _3.U(_3.D(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + var25 * Math.PI;
         this.X(var1.add(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double M(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void Y(Vec3 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.t.P();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = M(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.x = var20;
      Vec3 var26 = var1.add(0.0, var20, 0.0);
      this.a(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = var27 / 2.0;
         this.a(
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

      this.O(var26, var16 * 1.12, var10 * 2.4, _3.U(var8, 0.85));
      this.O(var26, var16 * 1.24, var10 * 1.2, _3.U(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = var30 / 4.0;
         this.O(
            var1.add(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            _3.U(_3.D(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void a(Vec3 var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.t.P();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = Y(var6 * 0.09 * (var18 == 0 ? 1 : -1) + var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            Vec3 var23 = P(var1, var2, var4, var18 + var22 * 2);
            Vec3 var24 = P(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = var25 / 16.0;
               double var28 = (var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-S(var28 - Math.rint(var28)) / 0.006);
               this.B[var14 + var18 * 3 + var22].y(var23.lerp(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, _3.U(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.X(P(var1, var2, var4, var18 + var33 * 2).lerp(P(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - var33), var16 * 2.3, var19);
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.X(P(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static Vec3 P(Vec3 var0, double var1, double var3, int var5) {
      double var6 = var3 + Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.add(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double Y(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double S(double var0) {
      return var0 * var0;
   }

   private static double K(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * hp.R(var0 / 0.08);
   }

   private static Vec3 t(Vec3 var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.add(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void v(ki var1, float var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.o && (!var3 || this.G)) {
            long var4 = kx.G();
            var1.blend(pf.x.E);

            for (__.l var9 : this.B) {
               var1.M(var9, true);
            }

            for (TargetAuraModule.p var12 : this.M) {
               var1.r(var12.center(), var12.radius(), var12.width(), var12.color());
            }

            for (TargetAuraModule.u var13 : this.Z) {
               Vec3 var14 = var13.center();
               var1.t(var14.x, var14.y, var14.z, var13.size() * (var3 ? 1.8 : 2.6), 0.0, _3.U(var13.color(), var3 ? 0.35 : 0.18));
               if (!var3) {
                  var1.t(var14.x, var14.y, var14.z, var13.size(), 0.0, var13.color());
               }
            }

            var1.blend(pf.x.S);
            kx.u("TargetAura", var4);
         }
      }
   }

   private void H(__.l var1, Vec3 var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = var13 / 32.0;
         Vec3 var16 = t(var2, var3, var5, var7, var9, ty.K(), var14);
         double var17 = this.t.P() * K(var14) * 3.4;
         int var19 = _3.U(_3.D(var12, var11, var14), Math.pow(var14, 0.65));
         var1.y(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.W = 0.0;
      this.Y = 0;
      this.o = (boolean)0;
      this.x = Double.NaN;
      this.q = null;
      this.Q = null;
      this.a = -1;
      this.M.clear();
      this.Z.clear();

      for (__.l var4 : this.B) {
         var4.y();
      }

      this.U.q();
      h.P.b(2);
   }

   private record p(Vec3 center, double radius, double width, int color) {
   }

   private record u(Vec3 center, double size, int color) {
   }
}

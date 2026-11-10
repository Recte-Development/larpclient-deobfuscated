package com.larpclient.module;

import com.larpclient.c3;
import com.larpclient.c5;
import com.larpclient.e5;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.gd;
import com.larpclient.q8;
import com.larpclient.qd;
import com.larpclient.qo;
import com.larpclient.ti;
import com.larpclient.u9;
import com.larpclient.wc;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_310;

public final class TargetAuraModule extends Module {
   private static TargetAuraModule D;
   private final li n = this.addSetting(
      new li("Style", "Composed target effect", "Orbit", "Orbit", "Helix", "Energy", "Halo", "Atom", "Vortex", "Scanner", "Star of David")
   );
   private final li G = this.addSetting(new li("Source", "Read-only target source", "Auto", "Auto", "Aura", "Combat", "Crosshair") {
      @Override
      public void setValue(String var1) {
         super.setValue(TargetAuraModule.A(var1));
      }

      @Override
      public boolean acceptsValue(String var1) {
         return super.acceptsValue(TargetAuraModule.A(var1));
      }
   });
   private final lo t = this.addSetting(new lo("Speed", "Cycles per second", 0.65, 0.1, 3.0, 0.05));
   private final lo R = this.addSetting(new lo("Width", "Trail width", 0.045, 0.01, 0.15, 0.005));
   private final lo F = this.addSetting(new lo("Particles Per Second", "Persistent aura emission", 18.0, 0.0, 60.0, 1.0));
   private final l3 y = this.addSetting(new l3("Bloom", "Explicit glow pass", true));
   private final l3 p = this.addSetting(new l3("Theme Colors", "Follow current theme", true));
   private final l0 O = this.addSetting(new l0("Color", "Primary color", ez.a));
   private final l0 i = this.addSetting(new l0("Gradient", "Secondary color", ez.f));
   private final ti f = new ti();
   private double r;
   private int m;
   private final c3 u = new c3();
   private final qd.x[] V = new qd.x[18];
   private double I;
   private final ArrayList<TargetAuraModule.h> h;
   private final ArrayList<TargetAuraModule.l> b;
   private boolean Y;
   private boolean d;
   private Object M;
   private int Z;
   private class_243 w;

   private static String A(String var0) {
      return "KillAura".equalsIgnoreCase(var0) ? "Aura" : var0;
   }

   public TargetAuraModule() {
      super("TargetAura", "Show glowing effects around your target", Category.VISUALS, -1);

      for (int var1 = 0; var1 < this.V.length; var1++) {
         this.V[var1] = new qd.x(65);
      }

      this.I = Double.NaN;
      this.h = new ArrayList<>(10);
      this.b = new ArrayList<>(8);
      this.Z = -1;
      D = this;
      this.O.O(() -> !this.p.getValue());
      this.i.O(() -> !this.p.getValue());
      this.n.a(var0 -> q8.G("TargetAura", "Style changed: {}", var0));
   }

   public static TargetAuraModule p() {
      return D;
   }

   public List<lb<?>> y() {
      return List.of(this.n, this.G, this.t, this.R, this.F, this.y);
   }

   public List<lb<?>> l() {
      return List.of(this.p, this.O, this.i);
   }

   public boolean z() {
      return !this.isEnabled() ? false : this.Y && this.d;
   }

   @Override
   public void onEnable() {
      q8.G(
         "TargetAura",
         "Expanded body-height aura: style={}, width={}, particle rate={}/s; readable base with bloom off",
         this.n.getValue(),
         this.R.getValue(),
         this.F.getValue()
      );
   }

   @Override
   public void onTick() {
      class_1309 var1 = e5.U(this.G.getValue());
      if (var1 == null) {
         this.r = 0.0;
      } else {
         this.m(var1, var1.method_73189());
         this.r = this.r + this.F.getValue() * 0.05;
         int var2 = (int)this.r;
         this.r -= var2;
         boolean var5 = false;
         int var4 = var2;
         int var10000 = Integer.signum(var4);
         if ((var10000 | -var10000) >>> 31 != 0) {
            ti var3 = this.L(var2, var1.method_17681(), var1.method_17682());
            wc.i.V(var1.method_73189().method_1031(0.0, this.D(var1.method_17682()), 0.0), var3);
         }
      }
   }

   private double D(double var1) {
      if (this.n.G("Halo")) {
         return var1 + 0.22;
      } else {
         return this.n.G("Star of David") && Double.isFinite(this.I) ? this.I : var1 * 0.65;
      }
   }

   public ti L(int var1, double var2, double var4) {
      this.f.t = 2;
      this.f.G = var1;
      this.f.z = ti.a.O;
      boolean var6 = this.n.G("Star of David");
      this.f.E = this.f.h = (var2 * 0.75 + 0.35) * (var6 ? 2.6 : 2.0);
      this.f.A = var4 * (this.n.G("Halo") ? 0.08 : (var6 ? 0.1 : 0.8));
      this.f.l = this.p.getValue() ? ez.a : this.O.getValue();
      this.f.b = this.p.getValue() ? ez.f : this.i.getValue();
      this.f.i = 0.85;
      this.f.B = 1.4;
      this.f.M = 0.055;
      this.f.F = 0.13;
      this.f.f = 128;
      this.f.R = -0.05;
      this.f.O = 0.1;
      this.f.d = 0.12;
      this.f.v = 0.3;
      this.f.X = 1.5;
      this.f.a = this.y.getValue();
      this.f.Q = gd.h.f;
      this.f.x = ti.d.l;
      this.f.r = 1.0;
      if (var6) {
         this.f.X = 0.6;
         this.f.v = 0.18;
      }

      if (!this.n.G("Halo")) {
         this.f.l = ei.R(this.f.l, 0.85);
         this.f.b = ei.R(this.f.b, 0.65);
         this.f.F = 0.11;
         if (++this.m % 9 == 0) {
            this.f.Q = gd.h.P;
            this.f.M = 0.025;
            this.f.F = 0.045;
            this.f.i = 0.18;
            this.f.B = 0.35;
            this.f.O = 0.75;
            this.f.r = 0.35;
            this.f.v = 0.08;
            this.f.l = ei.W(this.f.l, this.f.l & 0xFF000000 | 16777215, 0.35);
         }
      }

      return this.f;
   }

   public void G(float var1) {
      if (this.isEnabled()) {
         this.Y = (boolean)0;
         this.I = Double.NaN;
         this.h.clear();
         this.b.clear();

         for (qd.x var5 : this.V) {
            var5.T();
         }

         if (this.isRuntimeActive()) {
            class_1309 var24 = e5.U(this.G.getValue());
            double var25 = this.u.u(qo.x(), this.t.getValue());
            if (var24 == null) {
               this.w = null;
               this.Z = -1;
            } else {
               class_243 var26 = var24.method_30950(var1);
               this.m(var24, var26);
               this.Y = (boolean)1;
               this.d = this.y.getValue();
               double var6 = var24.method_17681() * 0.75 + 0.35;
               double var8 = var24.method_17682();
               int var10 = this.p.getValue() ? ez.a : this.O.getValue();
               int var11 = this.p.getValue() ? ez.f : this.i.getValue();
               if (this.n.G("Orbit") || this.n.G("Helix")) {
                  boolean var27 = this.n.G("Helix");

                  for (int var13 = 0; var13 < 2; var13++) {
                     qd.x var14 = this.V[var13];

                     for (int var15 = 0; var15 <= 64; var15++) {
                        double var16 = var15 / 64.0;
                        class_243 var18 = M(var26, var6, var8, var25, var13, var16, var27);
                        double var19 = var27 ? Math.sin(Math.PI * var16) : W(var16);
                        double var21 = this.R.getValue() * var19 * 3.4;
                        int var23 = ei.R(ei.W(var11, var10, var16), 0.92 * (var27 ? Math.sin(Math.PI * var16) : Math.pow(var16, 0.65)));
                        var14.x(var18, var21, var23);
                     }

                     double var31 = var27 ? 0.5 + 0.44 * Math.sin(var25 * 0.33 + var13 * Math.PI) : 1.0;
                     this.X(M(var26, var6, var8, var25, var13, var31, var27), this.R.getValue() * (var27 ? 1.6 : 2.1), ei.W(var11, var10, var31));
                  }

                  this.i(var26.method_1031(0.0, 0.055, 0.0), var6 * 1.05, this.R.getValue() * 2.0, ei.R(var11, 0.55));
               } else if (this.n.G("Energy")) {
                  this.Z(this.V[0], var26, var6, var8 * 0.48, var8, var25, var10, var11);
                  this.Z(this.V[1], var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, var11, var10);
                  this.X(W(var26, var6, var8 * 0.48, var8, var25, qo.x(), 1.0), this.R.getValue() * 2.1, var10);
                  this.X(W(var26, var6 * 0.92, var8 * 0.88, var8, -var25 * 0.83 + 2.1, qo.x(), 1.0), this.R.getValue() * 2.1, var11);
                  this.i(var26.method_1031(0.0, 0.055, 0.0), var6 * 1.1, this.R.getValue() * 2.5, ei.R(var10, 0.6));
               } else if (this.n.G("Atom")) {
                  this.x(var26, var6, var8, var25, var10, var11);
               } else if (this.n.G("Vortex")) {
                  this.p(var26, var6, var8, var25, var10, var11);
               } else if (this.n.G("Scanner")) {
                  this.U(var26, var6, var8, var25, var10, var11);
               } else if (this.n.G("Star of David")) {
                  this.g(var26, var6, var8, var25, var10, var11);
               } else {
                  class_243 var12 = var26.method_1031(0.0, var8 + 0.22 + Math.sin(var25 * 0.6) * 0.045, 0.0);
                  this.i(var12, var6 * 1.05, this.R.getValue() * 3.0, ei.R(var10, 0.95));
                  this.i(var12, var6 * 1.05, this.R.getValue() * 6.0, ei.R(var11, 0.25));
                  this.Z(this.V[0], var26, var6 * 1.05, var12.field_1351 - var26.field_1351, var8 * 0.08, var25 * 0.65, var10, var11);
                  this.X(W(var26, var6 * 1.05, var12.field_1351 - var26.field_1351, var8 * 0.08, var25 * 0.65, qo.x(), 1.0), this.R.getValue() * 1.8, var10);
                  this.i(var26.method_1031(0.0, 0.055, 0.0), var6 * 1.05, this.R.getValue() * 2.0, ei.R(var11, 0.55));
               }

               class_243 var28 = class_310.method_1551().field_1773.method_19418().method_71156();

               for (qd.x var33 : this.V) {
                  var33.k(var28);
               }
            }
         }
      }
   }

   private void m(class_1309 var1, class_243 var2) {
      class_1937 var3 = var1.method_73183();
      if (this.M != var3 || this.Z != var1.method_5628() || this.w != null && this.w.method_1025(var2) > 64.0) {
         this.r = 0.0;
         wc.i.S(2);
      }

      this.M = var3;
      this.Z = var1.method_5628();
      this.w = var2;
   }

   private void i(class_243 var1, double var2, double var4, int var6) {
      this.h.add(new TargetAuraModule.h(var1, var2, var4, var6));
   }

   private void X(class_243 var1, double var2, int var4) {
      this.b.add(new TargetAuraModule.l(var1, var2, ei.W(var4, var4 & 0xFF000000 | 16777215, 0.55)));
   }

   public static class_243 M(class_243 var0, double var1, double var3, double var5, int var7, double var8, boolean var10) {
      double var11 = var5 + var7 * Math.PI + (var10 ? var8 * Math.PI * 4.0 : -(1.0 - var8) * Math.PI * 1.85);
      double var13 = Math.sin(var11 * 2.3 + var5 * 0.23 + var7);
      double var15 = var1 * (1.0 + 0.045 * var13);
      double var17 = var10 ? var3 * (0.06 + 0.97 * var8) : 0.54 * var3 + Math.sin(var11 * 1.2) * var3 * 0.34;
      var17 += Math.sin(var11 * 1.7 - var5 * 0.31) * var3 * 0.025;
      return var0.method_1031(Math.cos(var11) * var15, var17, Math.sin(var11) * var15);
   }

   private void x(class_243 var1, double var2, double var4, double var6, int var8, int var9) {
      class_243 var10 = var1.method_1031(0.0, var4 * 0.55, 0.0);
      double var11 = var2 * 1.15;
      double var13 = this.R.getValue();

      for (int var15 = 0; var15 < 3; var15++) {
         double var16 = var6 * 0.18 + var15 * Math.PI * 2.0 / 3.0;
         double var18 = var6 * (1.0 + var15 * 0.17) + var15 * 2.1;
         int var20 = ei.W(var8, var9, var15 / 2.0);

         for (int var21 = 0; var21 <= 48; var21++) {
            this.V[var15].x(d(var10, var11, var4, var16, var21 / 48.0 * Math.PI * 2.0), var13 * 1.3, ei.R(var20, 0.24));
         }

         for (int var24 = 0; var24 <= 32; var24++) {
            double var22 = var24 / 32.0;
            this.V[var15 + 3]
               .x(
                  d(var10, var11, var4, var16, var18 - (1.0 - var22) * Math.PI * 1.1),
                  var13 * W(var22) * 3.2,
                  ei.R(ei.W(var9, var8, var22), 0.95 * Math.pow(var22, 0.65))
               );
         }

         this.X(d(var10, var11, var4, var16, var18), var13 * 2.0, var20);
      }

      this.i(var1.method_1031(0.0, 0.055, 0.0), var2 * 1.05, var13 * 2.0, ei.R(var9, 0.5));
   }

   public static class_243 d(class_243 var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.toRadians(56.0);
      double var11 = Math.min(var1 * Math.sin(var9), var3 * 0.42);
      double var13 = Math.cos(var7);
      double var15 = Math.sin(var7);
      double var17 = var1 * Math.cos(var9) * var15;
      return var0.method_1031(Math.cos(var5) * var1 * var13 - Math.sin(var5) * var17, var11 * var15, Math.sin(var5) * var1 * var13 + Math.cos(var5) * var17);
   }

   private void p(class_243 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.R.getValue();

      for (int var12 = 0; var12 < 3; var12++) {
         double var13 = T(var6 * 0.12 + var12 / 3.0);

         for (int var15 = 0; var15 <= 64; var15++) {
            double var16 = var15 / 64.0;
            double var18 = s(var16);
            double var20 = Math.exp(-L((var16 - var13) / 0.09));
            this.V[var12]
               .x(
                  U(var1, var2, var4, var6, var12, var16),
                  var10 * (1.6 + 1.6 * var16) * (1.0 + 0.7 * var20) * var18,
                  ei.R(ei.W(var8, var9, var16), (0.38 + 0.57 * var20) * var18)
               );
         }

         this.X(U(var1, var2, var4, var6, var12, var13), var10 * 1.9 * s(var13), ei.W(var8, var9, var13));
      }

      this.i(var1.method_1031(0.0, 0.055, 0.0), var2 * 0.8, var10 * 2.0, ei.R(var8, 0.55));
      this.i(var1.method_1031(0.0, var4 * 1.1, 0.0), var2 * 1.36, var10 * 1.6, ei.R(var9, 0.32));
   }

   public static class_243 U(class_243 var0, double var1, double var3, double var5, int var7, double var8) {
      double var10 = var5 * 0.9 + var7 * Math.PI * 2.0 / 3.0 - var8 * Math.PI * 2.3;
      double var12 = var1 * (0.72 + 0.62 * Math.pow(var8, 1.3));
      return var0.method_1031(Math.cos(var10) * var12, var3 * (0.02 + 1.08 * var8), Math.sin(var10) * var12);
   }

   private static double s(double var0) {
      return Math.pow(Math.sin(Math.PI * Math.clamp(var0, 0.0, 1.0)), 0.6);
   }

   private void U(class_243 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.R.getValue();
      double var12 = var6 * 0.45;
      double var14 = r(var4, var12);
      double var16 = Math.sin(var12);
      double var18 = Math.abs(var16) * var4 * 0.32;
      double var20 = Math.min(1.0, var18 * 8.0);
      this.i(var1.method_1031(0.0, var14, 0.0), var2 * 1.02, var10 * 2.6, ei.R(var8, 0.95));
      this.i(var1.method_1031(0.0, var14, 0.0), var2 * 1.02, var10 * 7.0, ei.R(var9, 0.28));

      for (int var22 = 1; var22 <= 6 && var20 > 0.02; var22++) {
         double var23 = var22 / 6.0;
         this.i(
            var1.method_1031(0.0, var14 - Math.signum(var16) * var18 * var23, 0.0),
            var2 * (1.02 - 0.04 * var23),
            var10 * 1.6,
            ei.R(ei.W(var8, var9, var23), 0.55 * Math.pow(1.0 - var23, 1.4) * var20)
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         double var26 = var6 * 1.1 + var25 * Math.PI;
         this.X(var1.method_1031(Math.cos(var26) * var2 * 1.02, var14, Math.sin(var26) * var2 * 1.02), var10 * 1.8, var25 == 0 ? var8 : var9);
      }
   }

   public static double r(double var0, double var2) {
      return 0.04 + (0.5 - 0.5 * Math.cos(var2)) * (var0 + 0.06);
   }

   private void g(class_243 var1, double var2, double var4, double var6, int var8, int var9) {
      double var10 = this.R.getValue();
      double var12 = var6 * 0.45;
      double var14 = Math.sin(var12);
      double var16 = var2 * 1.3 * (1.0 + 0.025 * Math.sin(var6 * 0.5));
      double var18 = var6 * 0.1;
      double var20 = r(var4, var12);
      double var22 = Math.abs(var14) * var4 * 0.32;
      double var24 = Math.min(1.0, var22 * 8.0);
      this.I = var20;
      class_243 var26 = var1.method_1031(0.0, var20, 0.0);
      this.m(var26, var16, var18, var6, var8, var9, 1.0, 1.0, 0, true);

      for (int var27 = 1; var27 <= 2 && var24 > 0.02; var27++) {
         double var28 = var27 / 2.0;
         this.m(
            var1.method_1031(0.0, var20 - Math.signum(var14) * var22 * var28, 0.0),
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

      this.i(var26, var16 * 1.12, var10 * 2.4, ei.R(var8, 0.85));
      this.i(var26, var16 * 1.24, var10 * 1.2, ei.R(var9, 0.45));

      for (int var30 = 1; var30 <= 4 && var24 > 0.02; var30++) {
         double var31 = var30 / 4.0;
         this.i(
            var1.method_1031(0.0, var20 - Math.signum(var14) * var22 * var31, 0.0),
            var16 * 1.12 * (1.0 - 0.04 * var31),
            var10 * 1.4,
            ei.R(ei.W(var8, var9, var31), 0.4 * Math.pow(1.0 - var31, 1.4) * var24)
         );
      }
   }

   private void m(class_243 var1, double var2, double var4, double var6, int var8, int var9, double var10, double var12, int var14, boolean var15) {
      double var16 = this.R.getValue();

      for (int var18 = 0; var18 < 2; var18++) {
         int var19 = var18 == 0 ? var8 : var9;
         double var20 = T(var6 * 0.09 * (var18 == 0 ? 1 : -1) + var18 * 0.5);

         for (int var22 = 0; var22 < 3; var22++) {
            class_243 var23 = q(var1, var2, var4, var18 + var22 * 2);
            class_243 var24 = q(var1, var2, var4, var18 + var22 * 2 + 2);

            for (int var25 = 0; var25 <= 16; var25++) {
               double var26 = var25 / 16.0;
               double var28 = (var22 + var26) / 3.0 - var20;
               double var30 = Math.exp(-L(var28 - Math.rint(var28)) / 0.006);
               this.V[var14 + var18 * 3 + var22]
                  .x(var23.method_35590(var24, var26), var16 * (2.6 + 1.6 * var30) * var12, ei.R(var19, (0.72 + 0.28 * var30) * var10));
            }
         }

         if (var15) {
            int var33 = (int)(var20 * 3.0);
            this.X(q(var1, var2, var4, var18 + var33 * 2).method_35590(q(var1, var2, var4, var18 + var33 * 2 + 2), var20 * 3.0 - var33), var16 * 2.3, var19);
         }
      }

      if (var15) {
         for (int var32 = 0; var32 < 6; var32++) {
            this.X(q(var1, var2, var4, var32), var16 * 1.5, var32 % 2 == 0 ? var8 : var9);
         }
      }
   }

   public static class_243 q(class_243 var0, double var1, double var3, int var5) {
      double var6 = var3 + Math.floorMod(var5, 6) * Math.PI / 3.0;
      return var0.method_1031(Math.cos(var6) * var1, 0.0, Math.sin(var6) * var1);
   }

   private static double T(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double L(double var0) {
      return var0 * var0;
   }

   private static double W(double var0) {
      return (0.12 + 0.88 * Math.pow(var0, 0.8)) * u9.L(var0 / 0.08);
   }

   private static class_243 W(class_243 var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var7 - (1.0 - var11) * 2.4;
      double var15 = Math.sin(var13 * 2.1 + var9 * 0.7);
      double var17 = var1 * (1.0 + var15 * 0.045);
      return var0.method_1031(Math.cos(var13) * var17, var3 + var5 * 0.045 * Math.sin(var13 * 1.6), Math.sin(var13) * var17);
   }

   public void G(f1 var1, float var2, boolean var3) {
      if (this.isEnabled()) {
         if (this.Y && (!var3 || this.d)) {
            long var4 = c5.X();
            var1.blend(ti.d.l);

            for (qd.x var9 : this.V) {
               var1.G(var9, true);
            }

            for (TargetAuraModule.h var12 : this.h) {
               var1.r(var12.center(), var12.radius(), var12.width(), var12.color());
            }

            for (TargetAuraModule.l var13 : this.b) {
               class_243 var14 = var13.center();
               var1.V(var14.field_1352, var14.field_1351, var14.field_1350, var13.size() * (var3 ? 1.8 : 2.6), 0.0, ei.R(var13.color(), var3 ? 0.35 : 0.18));
               if (!var3) {
                  var1.V(var14.field_1352, var14.field_1351, var14.field_1350, var13.size(), 0.0, var13.color());
               }
            }

            var1.blend(ti.d.G);
            c5.w("TargetAura", var4);
         }
      }
   }

   private void Z(qd.x var1, class_243 var2, double var3, double var5, double var7, double var9, int var11, int var12) {
      for (int var13 = 0; var13 <= 32; var13++) {
         double var14 = var13 / 32.0;
         class_243 var16 = W(var2, var3, var5, var7, var9, qo.x(), var14);
         double var17 = this.R.getValue() * W(var14) * 3.4;
         int var19 = ei.R(ei.W(var12, var11, var14), Math.pow(var14, 0.65));
         var1.x(var16, var17, var19);
      }
   }

   @Override
   public void onDisable() {
      this.r = 0.0;
      this.m = 0;
      this.Y = (boolean)0;
      this.I = Double.NaN;
      this.w = null;
      this.M = null;
      this.Z = -1;
      this.h.clear();
      this.b.clear();

      for (qd.x var4 : this.V) {
         var4.T();
      }

      this.u.K();
      wc.i.S(2);
   }

   private record h(class_243 center, double radius, double width, int color) {
   }

   private record l(class_243 center, double size, int color) {
   }
}

package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import net.minecraft.class_12075;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PetsModule extends Module {
   private static PetsModule X;
   private final com.larpclient.module.li c = this.addSetting(
      new com.larpclient.module.li("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final com.larpclient.module.l3 K = this.addSetting(
      new com.larpclient.module.l3("Realism", "Detailed anatomy, natural finishes and reactive gaze", false)
   );
   private final com.larpclient.module.li J = this.addSetting(
      new com.larpclient.module.li(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final com.larpclient.module.lo H = this.addSetting(new com.larpclient.module.lo("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final com.larpclient.module.lo N = this.addSetting(new com.larpclient.module.lo("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.li L = this.addSetting(
      new com.larpclient.module.li("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind")
   );
   private final com.larpclient.module.l3 f = this.addSetting(new com.larpclient.module.l3("Sparkle trail", "Sparks follow the companion", true));
   private final com.larpclient.module.l3 r = this.addSetting(new com.larpclient.module.l3("In first person", "Show the companion in first person", true));
   private final com.larpclient.module.l3 T = this.addSetting(
      new com.larpclient.module.l3("Speech bubbles", "Show notifications above an on-screen companion", true)
   );
   private final com.larpclient.module.li Q = this.addSetting(
      new com.larpclient.module.li("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom")
   );
   private final com.larpclient.module.l0 P = this.addSetting(new com.larpclient.module.l0("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, PetsModule.a> p = new LinkedHashMap<>();
   private Object R;
   private Object I;
   private class_243 j;
   private class_243 h = class_243.field_1353;
   private float x = Float.NaN;
   private float B;
   private float D;
   private double Y;
   private double G;
   private double A;
   private double g;
   private double z;
   private double w;
   private double o = 6.5;
   private double s;
   private double i;
   private double u;
   private int S;
   private boolean F;
   private String b;
   private final ArrayDeque<PetsModule.d> O = new ArrayDeque<>();
   private final ArrayDeque<PetsModule.o> d = new ArrayDeque<>();
   private PetsModule.o E;
   private long U;
   private long a;
   private PetsModule.t M;
   private final Random v = new Random(1279349328L);
   private double C;

   public PetsModule() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      X = this;
      this.c.O(() -> !this.K.getValue());
      this.N.O(() -> !this.K.getValue());
      this.J.O(() -> this.K.getValue());
      this.H.O(() -> this.K.getValue());
      this.p.put("Dragon", new PetsModule.a("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.p.put("Jellyfish", new PetsModule.a("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.p.put("Rune Orb", new PetsModule.a("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.p.put("Ghost", new PetsModule.a("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.T.a(var1 -> {
         if (!var1) {
            this.C();
            this.d.clear();
            this.E = null;
         }
      });
      this.Q.O(() -> this.K.getValue() && !T(this.J.getValue()));
      this.P.O(() -> this.Q.B() && this.Q.G("Custom"));
   }

   public static PetsModule F() {
      return X;
   }

   public List<com.larpclient.module.lb<?>> k() {
      return List.of(this.K, this.c, this.J, this.N, this.H);
   }

   public List<com.larpclient.module.lb<?>> G() {
      return List.of(this.L, this.r, this.f, this.T);
   }

   public List<com.larpclient.module.lb<?>> D() {
      ArrayList var1 = new ArrayList<>(List.of(this.Q, this.P));

      for (PetsModule.a var3 : this.p.values()) {
         var1.addAll(List.of(var3.j, var3.F, var3.C));
      }

      return List.copyOf(var1);
   }

   public List<com.larpclient.module.lb<?>> q() {
      ArrayList var1 = new ArrayList();

      for (PetsModule.a var3 : this.p.values()) {
         var1.addAll(List.of(var3.Q, var3.A));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.p();
      q8.v("Pets", "Local companion activated: {}", this.c.getValue());
   }

   @Override
   public void onDisable() {
      this.p();
   }

   public void p() {
      this.M = null;
      this.a = 0L;
      this.C();
      this.R = this.I = null;
      this.j = null;
      this.h = class_243.field_1353;
      this.x = Float.NaN;
      this.M = null;
      this.O.clear();
      this.d.clear();
      this.E = null;
      this.a = 0L;
      this.u = qo.x();
      this.b = null;
      this.Y = this.G = this.A = this.g = this.w = this.s = this.i = 0.0;
      this.z = 1.0;
      this.o = 6.5;
      this.S = 0;
      this.F = (boolean)0;
      this.C = 0.0;
      this.v.setSeed(1279349328L);
   }

   private void C() {
      PetsModule.o var1 = this.E;
      long var2 = this.U;
      List var4 = List.copyOf(this.d);
      this.E = null;
      this.d.clear();
      wt var5 = wt.T();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.z(var1.title, var1.text, var1.type, var6);
         }
      }

      for (PetsModule.o var7 : var4) {
         var5.z(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void j() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            this.g = Math.max(this.g, 1.5);
            this.z = 0.0;
         }
      }
   }

   public void V(float var1, class_12075 var2) {
      if (this.isEnabled()) {
         this.M = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            class_310 var3 = getMc();
            boolean var4 = this.K.getValue();
            String var5 = var4 ? this.J.getValue() : this.c.getValue();
            boolean var6 = T(var5);
            if (this.R != var3.field_1687 || this.I != var3.field_1724 || !Objects.equals(this.b, var5 + ":" + var4)) {
               this.p();
               this.R = var3.field_1687;
               this.I = var3.field_1724;
               this.b = var5 + ":" + var4;
               this.u = qo.x();
            }

            double var7 = Math.clamp(qo.x() - this.u, 0.0, 0.1);
            this.u = qo.x();
            this.i += var7;
            double var9 = z(var5, var4 ? this.H.getValue() : this.N.getValue());
            class_243 var11 = var3.field_1724.method_5836(var1);
            class_243 var12 = var3.field_1724.method_18798();
            float var13 = (float)Math.toRadians(var3.field_1724.method_5705(var1));
            if (Float.isNaN(this.x)) {
               this.x = var13;
            }

            if (var12.method_37267() * 20.0 > 1.0) {
               this.x = this.x + X((float)Math.atan2(-var12.field_1352, var12.field_1350) - this.x) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.field_1690.field_1904.method_1434() || var3.field_1690.field_1886.method_1434()) && this.j != null) {
               class_243 var14 = this.j.method_1020(var11);
               if (var14.method_1033() < 6.0
                  && var14.method_1033() > 0.001
                  && var14.method_1029().method_1026(var3.field_1724.method_5828(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.x = this.x + X(var13 - this.x) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            class_243 var47 = new class_243(-Math.sin(this.x), 0.0, Math.cos(this.x));
            class_243 var15 = new class_243(-var47.field_1350, 0.0, var47.field_1352);

            class_243 var16 = switch ((String)this.L.getValue()) {
               case "Orbit" -> var11.method_1031(
                  Math.cos(this.i * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.i * 1.7) * 0.15, Math.sin(this.i * 0.9) * 1.3 * var9
               );
               case "Behind" -> var11.method_1019(var47.method_1021(-1.4 * var9)).method_1019(var15.method_1021(0.35)).method_1031(0.0, -0.95, 0.0);
               default -> var11.method_1019(var15.method_1021(0.72 * var9)).method_1019(var47.method_1021(-0.35)).method_1031(0.0, 0.28 * var9, 0.0);
            };
            class_3965 var48 = var3.field_1687.method_17742(new class_3959(var11, var16, class_3960.field_17558, class_242.field_1348, var3.field_1724));
            if (var48.method_17783() == class_240.field_1332) {
               var16 = var48.method_17784().method_1020(var16.method_1020(var11).method_1029().method_1021(0.3 * var9));
            }

            if (this.j == null || this.j.method_1025(var16) > 400.0) {
               this.j = var16;
               this.h = class_243.field_1353;
               this.O.clear();
            }

            int var49 = var3.field_1724.field_6235;
            if (!var6 && var49 > this.S && var49 >= 6) {
               this.A = 1.0;
               this.h = this.h.method_1019(this.j.method_1020(var11).method_1029().method_1021(2.5));
            }

            this.S = var49;
            this.A = Math.max(0.0, this.A - var7 * 1.6);
            this.g = Math.max(0.0, this.g - var7);
            if (w6.l(var3.field_1724) && !this.F) {
               this.g = Math.max(this.g, 1.1);
               this.z = 0.0;
            }

            this.F = w6.l(var3.field_1724);
            this.z = Math.min(1.0, this.z + var7 / 0.65);
            this.h = this.h.method_1019(var16.method_1020(this.j).method_1021(26.0 * var7)).method_1021(Math.pow(0.004, var7));
            this.j = this.j.method_1019(this.h.method_1021(var7));
            double var19 = this.h.method_1033();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.h.field_1352, this.h.field_1350) : this.x;
            this.B = this.B + X(var21 - this.B) * (float)Math.min(1.0, var7 * 6.0);
            this.D = this.D + ((float)Math.clamp(-this.h.field_1351 * 0.35, -0.6, 0.6) - this.D) * (float)Math.min(1.0, var7 * 5.0);
            class_243 var22 = this.j
               .method_1031(0.0, Math.sin(this.i * 2.1) * 0.05 * var9 + (!var6 && this.z < 1.0 ? Math.sin(this.z * Math.PI) * 0.12 * var9 : 0.0), 0.0);
            class_243 var23 = (var3.field_1765 != null && var3.field_1765.method_17783() != class_240.field_1333
                  ? var3.field_1765.method_17784()
                  : var11.method_1019(var3.field_1724.method_5828(var1).method_1021(6.0)))
               .method_1020(var22);
            double var24 = X((float)(Math.atan2(-var23.field_1352, var23.field_1350) - this.B));
            double var26 = -Math.atan2(var23.field_1351, var23.method_37267());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.Y = this.Y + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.i * 0.6) * 0.35) - this.Y) * (1.0 - Math.exp(-4.0 * var7));
            this.G = this.G + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.G) * (1.0 - Math.exp(-4.0 * var7));
            this.s = this.s + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.s) * Math.min(1.0, var7 * 3.0);
            this.o = this.o + (6.5 + Math.min(var19, 2.5) * 0.6 - this.s * 3.0 - this.o) * Math.min(1.0, var7 * 2.0);
            this.w = (this.w + var7 * this.o) % (Math.PI * 2);
            double var29 = Math.sin(this.w) * (1.0 - this.s * 0.75) + this.s * 0.15;
            int var31 = var3.field_1690.method_31044().method_31034() && !this.r.getValue() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  j_.H();
               }

               PetsModule.a var36 = this.p.getOrDefault(var5, this.p.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = ez.a;
                  var34 = this.f.getValue() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;

                  var33 = switch ((String)this.Q.getValue()) {
                     case "Theme" -> ez.a;
                     case "Custom" -> this.P.getValue();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.x();
                  var33 = var36.z();
                  var34 = var36.Q.getValue() / 100.0;
               }

               this.O.removeIf(var1x -> this.i - var1x.birth > var1x.lifetime);
               if (this.f.getValue() && var7 > 0.0) {
                  this.C = this.C + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.C >= 1.0) {
                     this.C--;
                     class_243 var50 = var22.method_1031(
                        this.v.nextGaussian() * 0.06 * var9, this.v.nextGaussian() * 0.06 * var9, this.v.nextGaussian() * 0.06 * var9
                     );
                     this.O.addLast(new PetsModule.d(var50, this.i, 0.8 + this.v.nextDouble() * 0.8, 0.8 + this.v.nextDouble() * 0.8));

                     while (this.O.size() > 96) {
                        this.O.removeFirst();
                     }
                  }
               }

               if (!this.f.getValue()) {
                  this.O.clear();
                  this.C = 0.0;
               }

               class_3965 var51 = var3.field_1687
                  .method_17742(new class_3959(var22, var22.method_1031(0.0, -4.5, 0.0), class_3960.field_17558, class_242.field_1347, var3.field_1724));
               class_243 var53 = var51.method_17783() == class_240.field_1332 ? var51.method_17784().method_1031(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.field_1351 - var53.field_1351) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(b9.v()).mul(b9.T());
               Vector3f var42 = new Quaternionf(var2.field_63081).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               eg.o var43 = eg.y(
                  var22.method_1031(0.0, 0.32 * var9, 0.0),
                  var2.field_63078,
                  var41,
                  var42,
                  var3.method_22683().method_4486(),
                  var3.method_22683().method_4502(),
                  false
               );
               if (var43 != null) {
                  this.a = System.nanoTime();
               }

               double var44 = this.z < 1.0 ? (1.0 - Math.pow(1.0 - this.z, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6 ? new Quaternionf().rotationY(-this.B) : new Quaternionf().rotationY((float)(-this.B + var44)).rotateX(this.D);
               this.M = new PetsModule.t(
                  var5,
                  var36.j.getValue(),
                  var4,
                  var22,
                  var2.field_63078.method_1020(var22).method_1029(),
                  var46,
                  var9,
                  this.i,
                  var19,
                  var29,
                  this.Y,
                  this.G,
                  Math.max(this.g, this.A),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.O),
                  var43
               );
            }
         }
      }
   }

   private static float X(float var0) {
      return (float)Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public static double z(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean T(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.M != null && this.M.glowStrength > 0.0;
   }

   public void p(f1 var1, boolean var2) {
      if (this.isEnabled()) {
         PetsModule.t var3 = this.M;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.r(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, ei.D(-16777216, var3.shadowAlpha));
            }

            jm.M(var1, var3, var2);

            for (PetsModule.d var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.V(
                  var5.point.field_1352,
                  var5.point.field_1351 + 0.08 * (1.0 - var6),
                  var5.point.field_1350,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  ei.D(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean P(String var1, String var2, wt.s var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case a -> -996986;
            case u -> -1415819;
            default -> ez.a;
         };
         if (this.isRuntimeActive()
            && wc.l()
            && !getMc().field_1690.field_1842
            && !(getMc().field_1755 instanceof tk)
            && this.T.getValue()
            && this.M != null
            && this.M.label != null
            && System.nanoTime() - this.a <= 250000000L) {
            this.d.removeIf(var1x -> var1x.title.equals(var1));
            if (this.E != null && this.E.title.equals(var1)) {
               this.E = null;
            }

            this.d.addLast(new PetsModule.o(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.d.size() > 4) {
               this.d.removeFirst();
            }

            this.g = Math.max(this.g, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void Y(String var1) {
      this.d.removeIf(var1x -> var1x.title.equals(var1));
      if (this.E != null && this.E.title.equals(var1)) {
         this.E = null;
      }
   }

   public void i(class_332 var1) {
      if (this.isEnabled()) {
         PetsModule.t var2 = this.M;
         if (this.isRuntimeActive() && this.T.getValue() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.E != null && (var3 - this.U) / 1000000L > this.E.duration + 220L) {
               this.E = null;
            }

            if (this.E == null && !this.d.isEmpty()) {
               this.E = this.d.removeFirst();
               this.U = var3;
            }

            if (this.E != null) {
               double var5 = (var3 - this.U) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - this.E.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = uw.n(this.E.title, 220, uw.r.V);
               String var14 = uw.n(this.E.text, 220, uw.r.n);
               int var15 = Math.max(90, Math.max(uw.z(var13, uw.r.V), uw.i(var14)) + 24);
               int var16 = Math.clamp(Math.round(var2.label.x() - var15 / 2.0F), 4, Math.max(4, getMc().method_22683().method_4486() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.y()) - 42);
               var1.method_51448().pushMatrix();

               try {
                  var1.method_51448().translate(var2.label.x(), var2.label.y());
                  var1.method_51448().scale((float)var11, (float)var11);
                  var1.method_51448().translate(-var2.label.x(), -var2.label.y());
                  gu.N(var1, var16, var17, var15, 34.0F, 7.0F, ei.R(-185207568, var7));
                  int var18 = Math.clamp(Math.round(var2.label.x()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.method_25294(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, ei.R(-185207568, var7));
                  }

                  gu.N(var1, var16 + 8, var17 + 9, 3.0F, 3.0F, 1.0F, ei.R(this.E.color, var7));
                  uw.D(var1, var13, var16 + 16, var17 + 5, ei.R(-14539990, var7), uw.r.V);
                  uw.z(var1, var14, var16 + 8, var17 + 19, ei.R(-11381667, var7));
               } finally {
                  var1.method_51448().popMatrix();
               }
            }
         }
      }
   }

   private final class a {
      final com.larpclient.module.li j;
      final com.larpclient.module.l3 F;
      final com.larpclient.module.l0 C;
      final com.larpclient.module.lo Q;
      final com.larpclient.module.l0 A;

      a(String var2, int var3, String... var4) {
         this.j = PetsModule.this.addSetting(new com.larpclient.module.li(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.F = PetsModule.this.addSetting(new com.larpclient.module.l3(var2 + " natural colours", "Use the finish palette", false));
         this.C = PetsModule.this.addSetting(new com.larpclient.module.l0(var2 + " colour", "Main companion color", -1459895));
         this.Q = PetsModule.this.addSetting(new com.larpclient.module.lo(var2 + " glow", "Magic glow strength in percent", var3, 0.0, 100.0, 5.0));
         this.A = PetsModule.this.addSetting(new com.larpclient.module.l0(var2 + " glow colour", "Companion glow color", -1459895));
         this.j.O(() -> PetsModule.this.c.G(var2) && !PetsModule.this.K.getValue());
         this.F.O(() -> this.j.B());
         this.C.O(() -> this.j.B() && !this.F.getValue());
         this.Q.O(() -> this.j.B());
         this.A.O(() -> this.j.B() && this.Q.getValue() > 0.0);
      }

      int x() {
         int var1;
         if (this.F.getValue()) {
            var1 = switch ((String)this.j.getValue()) {
               case "Scales" -> -10252932;
               case "Obsidian" -> -13356471;
               case "Gold" -> -2247330;
               case "Energy" -> -5341185;
               case "Crystal" -> -6496791;
               case "Wisp" -> -2037525;
               case "Silk" -> -4412714;
               default -> -5054483;
            };
         } else {
            var1 = this.C.getValue().equals(this.C.O()) ? ez.a : this.C.getValue();
         }
         return switch ((String)this.j.getValue()) {
            case "Gold" -> ei.W(var1, -12168, 0.4);
            case "Obsidian" -> ei.W(var1, -15724259, 0.65);
            case "Crystal" -> ei.W(var1, -1639937, 0.25);
            case "Silk" -> ei.W(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int z() {
         return this.A.getValue().equals(this.A.O()) ? ez.a : this.A.getValue();
      }
   }

   private record d(class_243 point, double birth, double lifetime, double scale) {
   }

   private record o(String title, String text, int color, long duration, wt.s type) {
   }

   public record t(
      String style,
      String finish,
      boolean realism,
      class_243 center,
      class_243 view,
      Quaternionf rotation,
      double scale,
      double time,
      double speed,
      double wings,
      double gazeYaw,
      double gazePitch,
      double reaction,
      int body,
      int glow,
      double glowStrength,
      class_243 shadow,
      double shadowAlpha,
      List<PetsModule.d> sparks,
      eg.o label
   ) {
   }
}

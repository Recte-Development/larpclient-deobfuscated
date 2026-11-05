package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PetsModule extends Module {
   private static PetsModule G;
   private final com.larpclient.module.lp T = this.addSetting(
      new com.larpclient.module.lp("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final com.larpclient.module.ln O = this.addSetting(
      new com.larpclient.module.ln("Realism", "Detailed anatomy, natural finishes and reactive gaze", false)
   );
   private final com.larpclient.module.lp E = this.addSetting(
      new com.larpclient.module.lp(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final com.larpclient.module.lc n = this.addSetting(new com.larpclient.module.lc("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final com.larpclient.module.lc u = this.addSetting(new com.larpclient.module.lc("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.lp f = this.addSetting(
      new com.larpclient.module.lp("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind")
   );
   private final com.larpclient.module.ln y = this.addSetting(new com.larpclient.module.ln("Sparkle trail", "Sparks follow the companion", true));
   private final com.larpclient.module.ln M = this.addSetting(new com.larpclient.module.ln("In first person", "Show the companion in first person", true));
   private final com.larpclient.module.ln m = this.addSetting(
      new com.larpclient.module.ln("Speech bubbles", "Show notifications above an on-screen companion", true)
   );
   private final com.larpclient.module.lp s = this.addSetting(
      new com.larpclient.module.lp("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom")
   );
   private final com.larpclient.module.lv l = this.addSetting(new com.larpclient.module.lv("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, PetsModule.k> K = new LinkedHashMap<>();
   private Object x;
   private Object A;
   private Vec3 Y;
   private Vec3 C = Vec3.ZERO;
   private float B = Float.NaN;
   private float W;
   private float p;
   private double Q;
   private double r;
   private double N;
   private double L;
   private double U;
   private double Z;
   private double q = 6.5;
   private double k;
   private double w;
   private double P;
   private int g;
   private boolean i;
   private String v;
   private final ArrayDeque<PetsModule.w> o = new ArrayDeque<>();
   private final ArrayDeque<PetsModule.l> t = new ArrayDeque<>();
   private PetsModule.l D;
   private long h;
   private long H;
   private PetsModule.m J;
   private final Random V = new Random(1279349328L);
   private double e;

   public PetsModule() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      G = this;
      this.T.x(() -> !this.O.getValue());
      this.u.x(() -> !this.O.getValue());
      this.E.x(() -> this.O.getValue());
      this.n.x(() -> this.O.getValue());
      this.K.put("Dragon", new PetsModule.k("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.K.put("Jellyfish", new PetsModule.k("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.K.put("Rune Orb", new PetsModule.k("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.K.put("Ghost", new PetsModule.k("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.m.W(var1 -> {
         if (!var1) {
            this.D();
            this.t.clear();
            this.D = null;
         }
      });
      this.s.x(() -> this.O.getValue() && !l(this.E.getValue()));
      this.l.x(() -> this.s.R() && this.s.i("Custom"));
   }

   public static PetsModule s() {
      return G;
   }

   public List<com.larpclient.module.l6<?>> n() {
      return List.of(this.O, this.T, this.E, this.u, this.n);
   }

   public List<com.larpclient.module.l6<?>> v() {
      return List.of(this.f, this.M, this.y, this.m);
   }

   public List<com.larpclient.module.l6<?>> J() {
      ArrayList var1 = new ArrayList<>(List.of(this.s, this.l));

      for (PetsModule.k var3 : this.K.values()) {
         var1.addAll(List.of(var3.m, var3.w, var3.q));
      }

      return List.copyOf(var1);
   }

   public List<com.larpclient.module.l6<?>> x() {
      ArrayList var1 = new ArrayList();

      for (PetsModule.k var3 : this.K.values()) {
         var1.addAll(List.of(var3.e, var3.P));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.N();
      r4.g("Pets", "Local companion activated: {}", this.T.getValue());
   }

   @Override
   public void onDisable() {
      this.N();
   }

   public void N() {
      this.J = null;
      this.H = 0L;
      this.D();
      this.x = this.A = null;
      this.Y = null;
      this.C = Vec3.ZERO;
      this.B = Float.NaN;
      this.J = null;
      this.o.clear();
      this.t.clear();
      this.D = null;
      this.H = 0L;
      this.P = ea.y();
      this.v = null;
      this.Q = this.r = this.N = this.L = this.Z = this.k = this.w = 0.0;
      this.U = 1.0;
      this.q = 6.5;
      this.g = 0;
      this.i = (boolean)0;
      this.e = 0.0;
      this.V.setSeed(1279349328L);
   }

   private void D() {
      PetsModule.l var1 = this.D;
      long var2 = this.h;
      List var4 = List.copyOf(this.t);
      this.D = null;
      this.t.clear();
      hg var5 = hg.B();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.J(var1.title, var1.text, var1.type, var6);
         }
      }

      for (PetsModule.l var7 : var4) {
         var5.J(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void u() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            this.L = Math.max(this.L, 1.5);
            this.U = 0.0;
         }
      }
   }

   public void e(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.J = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            boolean var4 = this.O.getValue();
            String var5 = var4 ? this.E.getValue() : this.T.getValue();
            boolean var6 = l(var5);
            if (this.x != var3.level || this.A != var3.player || !Objects.equals(this.v, var5 + ":" + var4)) {
               this.N();
               this.x = var3.level;
               this.A = var3.player;
               this.v = var5 + ":" + var4;
               this.P = ea.y();
            }

            double var7 = Math.clamp(ea.y() - this.P, 0.0, 0.1);
            this.P = ea.y();
            this.w += var7;
            double var9 = l(var5, var4 ? this.n.getValue() : this.u.getValue());
            Vec3 var11 = var3.player.getEyePosition(var1);
            Vec3 var12 = var3.player.getDeltaMovement();
            float var13 = (float)Math.toRadians(var3.player.getViewYRot(var1));
            if (Float.isNaN(this.B)) {
               this.B = var13;
            }

            if (var12.horizontalDistance() * 20.0 > 1.0) {
               this.B = this.B + D((float)Math.atan2(-var12.x, var12.z) - this.B) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.options.keyUse.isDown() || var3.options.keyAttack.isDown()) && this.Y != null) {
               Vec3 var14 = this.Y.subtract(var11);
               if (var14.length() < 6.0 && var14.length() > 0.001 && var14.normalize().dot(var3.player.getViewVector(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.B = this.B + D(var13 - this.B) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            Vec3 var47 = new Vec3(-Math.sin(this.B), 0.0, Math.cos(this.B));
            Vec3 var15 = new Vec3(-var47.z, 0.0, var47.x);

            Vec3 var16 = switch ((String)this.f.getValue()) {
               case "Orbit" -> var11.add(Math.cos(this.w * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.w * 1.7) * 0.15, Math.sin(this.w * 0.9) * 1.3 * var9);
               case "Behind" -> var11.add(var47.scale(-1.4 * var9)).add(var15.scale(0.35)).add(0.0, -0.95, 0.0);
               default -> var11.add(var15.scale(0.72 * var9)).add(var47.scale(-0.35)).add(0.0, 0.28 * var9, 0.0);
            };
            BlockHitResult var48 = var3.level.clip(new ClipContext(var11, var16, Block.COLLIDER, Fluid.NONE, var3.player));
            if (var48.getType() == Type.BLOCK) {
               var16 = var48.getLocation().subtract(var16.subtract(var11).normalize().scale(0.3 * var9));
            }

            if (this.Y == null || this.Y.distanceToSqr(var16) > 400.0) {
               this.Y = var16;
               this.C = Vec3.ZERO;
               this.o.clear();
            }

            int var49 = var3.player.hurtTime;
            if (!var6 && var49 > this.g && var49 >= 6) {
               this.N = 1.0;
               this.C = this.C.add(this.Y.subtract(var11).normalize().scale(2.5));
            }

            this.g = var49;
            this.N = Math.max(0.0, this.N - var7 * 1.6);
            this.L = Math.max(0.0, this.L - var7);
            if (hp.o(var3.player) && !this.i) {
               this.L = Math.max(this.L, 1.1);
               this.U = 0.0;
            }

            this.i = hp.o(var3.player);
            this.U = Math.min(1.0, this.U + var7 / 0.65);
            this.C = this.C.add(var16.subtract(this.Y).scale(26.0 * var7)).scale(Math.pow(0.004, var7));
            this.Y = this.Y.add(this.C.scale(var7));
            double var19 = this.C.length();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.C.x, this.C.z) : this.B;
            this.W = this.W + D(var21 - this.W) * (float)Math.min(1.0, var7 * 6.0);
            this.p = this.p + ((float)Math.clamp(-this.C.y * 0.35, -0.6, 0.6) - this.p) * (float)Math.min(1.0, var7 * 5.0);
            Vec3 var22 = this.Y.add(0.0, Math.sin(this.w * 2.1) * 0.05 * var9 + (!var6 && this.U < 1.0 ? Math.sin(this.U * Math.PI) * 0.12 * var9 : 0.0), 0.0);
            Vec3 var23 = (var3.hitResult != null && var3.hitResult.getType() != Type.MISS
                  ? var3.hitResult.getLocation()
                  : var11.add(var3.player.getViewVector(var1).scale(6.0)))
               .subtract(var22);
            double var24 = D((float)(Math.atan2(-var23.x, var23.z) - this.W));
            double var26 = -Math.atan2(var23.y, var23.horizontalDistance());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.Q = this.Q + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.w * 0.6) * 0.35) - this.Q) * (1.0 - Math.exp(-4.0 * var7));
            this.r = this.r + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.r) * (1.0 - Math.exp(-4.0 * var7));
            this.k = this.k + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.k) * Math.min(1.0, var7 * 3.0);
            this.q = this.q + (6.5 + Math.min(var19, 2.5) * 0.6 - this.k * 3.0 - this.q) * Math.min(1.0, var7 * 2.0);
            this.Z = (this.Z + var7 * this.q) % (Math.PI * 2);
            double var29 = Math.sin(this.Z) * (1.0 - this.k * 0.75) + this.k * 0.15;
            int var31 = var3.options.getCameraType().isFirstPerson() && !this.M.getValue() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  _i.h();
               }

               PetsModule.k var36 = this.K.getOrDefault(var5, this.K.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = rv.e;
                  var34 = this.y.getValue() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;

                  var33 = switch ((String)this.s.getValue()) {
                     case "Theme" -> rv.e;
                     case "Custom" -> this.l.getValue();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.y();
                  var33 = var36.E();
                  var34 = var36.e.getValue() / 100.0;
               }

               this.o.removeIf(var1x -> this.w - var1x.birth > var1x.lifetime);
               if (this.y.getValue() && var7 > 0.0) {
                  this.e = this.e + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.e >= 1.0) {
                     this.e--;
                     Vec3 var50 = var22.add(this.V.nextGaussian() * 0.06 * var9, this.V.nextGaussian() * 0.06 * var9, this.V.nextGaussian() * 0.06 * var9);
                     this.o.addLast(new PetsModule.w(var50, this.w, 0.8 + this.V.nextDouble() * 0.8, 0.8 + this.V.nextDouble() * 0.8));

                     while (this.o.size() > 96) {
                        this.o.removeFirst();
                     }
                  }
               }

               if (!this.y.getValue()) {
                  this.o.clear();
                  this.e = 0.0;
               }

               BlockHitResult var51 = var3.level.clip(new ClipContext(var22, var22.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
               Vec3 var53 = var51.getType() == Type.BLOCK ? var51.getLocation().add(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.y - var53.y) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(var2.projectionMatrix).mul(var2.viewRotationMatrix);
               Vector3f var42 = new Quaternionf(var2.orientation).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               _o.t var43 = _o.p(
                  var22.add(0.0, 0.32 * var9, 0.0),
                  var2.pos,
                  var41,
                  var42,
                  var3.getWindow().getGuiScaledWidth(),
                  var3.getWindow().getGuiScaledHeight(),
                  RenderSystem.getDevice().getDeviceInfo().isZZeroToOne()
               );
               if (var43 != null) {
                  this.H = System.nanoTime();
               }

               double var44 = this.U < 1.0 ? (1.0 - Math.pow(1.0 - this.U, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6 ? new Quaternionf().rotationY(-this.W) : new Quaternionf().rotationY((float)(-this.W + var44)).rotateX(this.p);
               this.J = new PetsModule.m(
                  var5,
                  var36.m.getValue(),
                  var4,
                  var22,
                  var2.pos.subtract(var22).normalize(),
                  var46,
                  var9,
                  this.w,
                  var19,
                  var29,
                  this.Q,
                  this.r,
                  Math.max(this.L, this.N),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.o),
                  var43
               );
            }
         }
      }
   }

   private static float D(float var0) {
      return (float)Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public static double l(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean l(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean y() {
      return !this.isEnabled() ? false : this.J != null && this.J.glowStrength > 0.0;
   }

   public void e(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         PetsModule.m var3 = this.J;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.C(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, oh.i(-16777216, var3.shadowAlpha));
            }

            oe.w(var1, var3, var2);

            for (PetsModule.w var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.m(
                  var5.point.x,
                  var5.point.y + 0.08 * (1.0 - var6),
                  var5.point.z,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  oh.i(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean e(String var1, String var2, hg.u var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case Z -> -996986;
            case y -> -1415819;
            default -> rv.e;
         };
         if (this.isRuntimeActive()
            && f8.c()
            && !getMc().gameRenderer.gameRenderState().guiRenderState.isHudHidden
            && !(getMc().gui.screen() instanceof p0)
            && this.m.getValue()
            && this.J != null
            && this.J.label != null
            && System.nanoTime() - this.H <= 250000000L) {
            this.t.removeIf(var1x -> var1x.title.equals(var1));
            if (this.D != null && this.D.title.equals(var1)) {
               this.D = null;
            }

            this.t.addLast(new PetsModule.l(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.t.size() > 4) {
               this.t.removeFirst();
            }

            this.L = Math.max(this.L, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void T(String var1) {
      this.t.removeIf(var1x -> var1x.title.equals(var1));
      if (this.D != null && this.D.title.equals(var1)) {
         this.D = null;
      }
   }

   public void E(GuiGraphicsExtractor var1) {
      if (this.isEnabled()) {
         PetsModule.m var2 = this.J;
         if (this.isRuntimeActive() && this.m.getValue() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.D != null && (var3 - this.h) / 1000000L > this.D.duration + 220L) {
               this.D = null;
            }

            if (this.D == null && !this.t.isEmpty()) {
               this.D = this.t.removeFirst();
               this.h = var3;
            }

            if (this.D != null) {
               double var5 = (var3 - this.h) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - this.D.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = j_.Y(this.D.title, 220, j_.w.Q);
               String var14 = j_.Y(this.D.text, 220, j_.w.H);
               int var15 = Math.max(90, Math.max(j_.E(var13, j_.w.Q), j_.L(var14)) + 24);
               int var16 = Math.clamp(Math.round(var2.label.x() - var15 / 2.0F), 4, Math.max(4, getMc().getWindow().getGuiScaledWidth() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.y()) - 42);
               var1.pose().pushMatrix();

               try {
                  var1.pose().translate(var2.label.x(), var2.label.y());
                  var1.pose().scale((float)var11, (float)var11);
                  var1.pose().translate(-var2.label.x(), -var2.label.y());
                  fr.S(var1, var16, var17, var15, 34.0F, 7.0F, oh.S(-185207568, var7));
                  int var18 = Math.clamp(Math.round(var2.label.x()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.fill(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, oh.S(-185207568, var7));
                  }

                  fr.S(var1, var16 + 8, var17 + 9, 3.0F, 3.0F, 1.0F, oh.S(this.D.color, var7));
                  j_.y(var1, var13, var16 + 16, var17 + 5, oh.S(-14539990, var7), j_.w.Q);
                  j_.c(var1, var14, var16 + 8, var17 + 19, oh.S(-11381667, var7));
               } finally {
                  var1.pose().popMatrix();
               }
            }
         }
      }
   }

   private final class k {
      final com.larpclient.module.lp m;
      final com.larpclient.module.ln w;
      final com.larpclient.module.lv q;
      final com.larpclient.module.lc e;
      final com.larpclient.module.lv P;

      k(String var2, int var3, String... var4) {
         this.m = PetsModule.this.addSetting(new com.larpclient.module.lp(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.w = PetsModule.this.addSetting(new com.larpclient.module.ln(var2 + " natural colours", "Use the finish palette", false));
         this.q = PetsModule.this.addSetting(new com.larpclient.module.lv(var2 + " colour", "Main companion color", -1459895));
         this.e = PetsModule.this.addSetting(new com.larpclient.module.lc(var2 + " glow", "Magic glow strength in percent", var3, 0.0, 100.0, 5.0));
         this.P = PetsModule.this.addSetting(new com.larpclient.module.lv(var2 + " glow colour", "Companion glow color", -1459895));
         this.m.x(() -> PetsModule.this.T.i(var2) && !PetsModule.this.O.getValue());
         this.w.x(() -> this.m.R());
         this.q.x(() -> this.m.R() && !this.w.getValue());
         this.e.x(() -> this.m.R());
         this.P.x(() -> this.m.R() && this.e.getValue() > 0.0);
      }

      int y() {
         int var1;
         if (this.w.getValue()) {
            var1 = switch ((String)this.m.getValue()) {
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
            var1 = this.q.getValue().equals(this.q.C()) ? rv.e : this.q.getValue();
         }
         return switch ((String)this.m.getValue()) {
            case "Gold" -> oh.j(var1, -12168, 0.4);
            case "Obsidian" -> oh.j(var1, -15724259, 0.65);
            case "Crystal" -> oh.j(var1, -1639937, 0.25);
            case "Silk" -> oh.j(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int E() {
         return this.P.getValue().equals(this.P.C()) ? rv.e : this.P.getValue();
      }
   }

   private record l(String title, String text, int color, long duration, hg.u type) {
   }

   public record m(
      String style,
      String finish,
      boolean realism,
      Vec3 center,
      Vec3 view,
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
      Vec3 shadow,
      double shadowAlpha,
      List<PetsModule.w> sparks,
      _o.t label
   ) {
   }

   private record w(Vec3 point, double birth, double lifetime, double scale) {
   }
}

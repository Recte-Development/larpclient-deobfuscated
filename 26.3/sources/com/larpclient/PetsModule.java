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
   private static PetsModule B;
   private final com.larpclient.module.o E = this.addSetting(
      new com.larpclient.module.o("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final com.larpclient.module.m r = this.addSetting(
      new com.larpclient.module.m("Realism", "Detailed anatomy, natural finishes and reactive gaze", false)
   );
   private final com.larpclient.module.o D = this.addSetting(
      new com.larpclient.module.o(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final com.larpclient.module.b u = this.addSetting(new com.larpclient.module.b("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final com.larpclient.module.b N = this.addSetting(new com.larpclient.module.b("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.o R = this.addSetting(
      new com.larpclient.module.o("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind")
   );
   private final com.larpclient.module.m P = this.addSetting(new com.larpclient.module.m("Sparkle trail", "Sparks follow the companion", true));
   private final com.larpclient.module.m s = this.addSetting(new com.larpclient.module.m("In first person", "Show the companion in first person", true));
   private final com.larpclient.module.m W = this.addSetting(
      new com.larpclient.module.m("Speech bubbles", "Show notifications above an on-screen companion", true)
   );
   private final com.larpclient.module.o K = this.addSetting(
      new com.larpclient.module.o("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom")
   );
   private final com.larpclient.module.h V = this.addSetting(new com.larpclient.module.h("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, PetsModule.f> U = new LinkedHashMap<>();
   private Object b;
   private Object T;
   private Vec3 d;
   private Vec3 X = Vec3.ZERO;
   private float S = Float.NaN;
   private float z;
   private float n;
   private double a;
   private double H;
   private double x;
   private double m;
   private double Y;
   private double A;
   private double h = 6.5;
   private double C;
   private double f;
   private double q;
   private int I;
   private boolean j;
   private String Z;
   private final ArrayDeque<PetsModule.y> i = new ArrayDeque<>();
   private final ArrayDeque<PetsModule.u> e = new ArrayDeque<>();
   private PetsModule.u g;
   private long t;
   private long L;
   private PetsModule.o k;
   private final Random l = new Random(1279349328L);
   private double F;

   public PetsModule() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      B = this;
      this.E.a(() -> !this.r.getValue());
      this.N.a(() -> !this.r.getValue());
      this.D.a(() -> this.r.getValue());
      this.u.a(() -> this.r.getValue());
      this.U.put("Dragon", new PetsModule.f("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.U.put("Jellyfish", new PetsModule.f("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.U.put("Rune Orb", new PetsModule.f("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.U.put("Ghost", new PetsModule.f("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.W.w(var1 -> {
         if (!var1) {
            this.m();
            this.e.clear();
            this.g = null;
         }
      });
      this.K.a(() -> this.r.getValue() && !v(this.D.getValue()));
      this.V.a(() -> this.K.j() && this.K.w("Custom"));
   }

   public static PetsModule X() {
      return B;
   }

   public List<com.larpclient.module.r<?>> t() {
      return List.of(this.r, this.E, this.D, this.N, this.u);
   }

   public List<com.larpclient.module.r<?>> c() {
      return List.of(this.R, this.s, this.P, this.W);
   }

   public List<com.larpclient.module.r<?>> G() {
      ArrayList var1 = new ArrayList<>(List.of(this.K, this.V));

      for (PetsModule.f var3 : this.U.values()) {
         var1.addAll(List.of(var3.Q, var3.n, var3.j));
      }

      return List.copyOf(var1);
   }

   public List<com.larpclient.module.r<?>> f() {
      ArrayList var1 = new ArrayList();

      for (PetsModule.f var3 : this.U.values()) {
         var1.addAll(List.of(var3.u, var3.m));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.l();
      _q.c("Pets", "Local companion activated: {}", this.E.getValue());
   }

   @Override
   public void onDisable() {
      this.l();
   }

   public void l() {
      this.k = null;
      this.L = 0L;
      this.m();
      this.b = this.T = null;
      this.d = null;
      this.X = Vec3.ZERO;
      this.S = Float.NaN;
      this.k = null;
      this.i.clear();
      this.e.clear();
      this.g = null;
      this.L = 0L;
      this.q = hz.Q();
      this.Z = null;
      this.a = this.H = this.x = this.m = this.A = this.C = this.f = 0.0;
      this.Y = 1.0;
      this.h = 6.5;
      this.I = 0;
      this.j = (boolean)0;
      this.F = 0.0;
      this.l.setSeed(1279349328L);
   }

   private void m() {
      PetsModule.u var1 = this.g;
      long var2 = this.t;
      List var4 = List.copyOf(this.e);
      this.g = null;
      this.e.clear();
      ib var5 = ib.L();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.l(var1.title, var1.text, var1.type, var6);
         }
      }

      for (PetsModule.u var7 : var4) {
         var5.l(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void N() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            this.m = Math.max(this.m, 1.5);
            this.Y = 0.0;
         }
      }
   }

   public void f(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.k = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            boolean var4 = this.r.getValue();
            String var5 = var4 ? this.D.getValue() : this.E.getValue();
            boolean var6 = v(var5);
            if (this.b != var3.level || this.T != var3.player || !Objects.equals(this.Z, var5 + ":" + var4)) {
               this.l();
               this.b = var3.level;
               this.T = var3.player;
               this.Z = var5 + ":" + var4;
               this.q = hz.Q();
            }

            double var7 = Math.clamp(hz.Q() - this.q, 0.0, 0.1);
            this.q = hz.Q();
            this.f += var7;
            double var9 = S(var5, var4 ? this.u.getValue() : this.N.getValue());
            Vec3 var11 = var3.player.getEyePosition(var1);
            Vec3 var12 = var3.player.getDeltaMovement();
            float var13 = (float)Math.toRadians(var3.player.getViewYRot(var1));
            if (Float.isNaN(this.S)) {
               this.S = var13;
            }

            if (var12.horizontalDistance() * 20.0 > 1.0) {
               this.S = this.S + o((float)Math.atan2(-var12.x, var12.z) - this.S) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.options.keyUse.isDown() || var3.options.keyAttack.isDown()) && this.d != null) {
               Vec3 var14 = this.d.subtract(var11);
               if (var14.length() < 6.0 && var14.length() > 0.001 && var14.normalize().dot(var3.player.getViewVector(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.S = this.S + o(var13 - this.S) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            Vec3 var47 = new Vec3(-Math.sin(this.S), 0.0, Math.cos(this.S));
            Vec3 var15 = new Vec3(-var47.z, 0.0, var47.x);

            Vec3 var16 = switch ((String)this.R.getValue()) {
               case "Orbit" -> var11.add(Math.cos(this.f * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.f * 1.7) * 0.15, Math.sin(this.f * 0.9) * 1.3 * var9);
               case "Behind" -> var11.add(var47.scale(-1.4 * var9)).add(var15.scale(0.35)).add(0.0, -0.95, 0.0);
               default -> var11.add(var15.scale(0.72 * var9)).add(var47.scale(-0.35)).add(0.0, 0.28 * var9, 0.0);
            };
            BlockHitResult var48 = var3.level.clip(new ClipContext(var11, var16, Block.COLLIDER, Fluid.NONE, var3.player));
            if (var48.getType() == Type.BLOCK) {
               var16 = var48.getLocation().subtract(var16.subtract(var11).normalize().scale(0.3 * var9));
            }

            if (this.d == null || this.d.distanceToSqr(var16) > 400.0) {
               this.d = var16;
               this.X = Vec3.ZERO;
               this.i.clear();
            }

            int var49 = var3.player.hurtTime;
            if (!var6 && var49 > this.I && var49 >= 6) {
               this.x = 1.0;
               this.X = this.X.add(this.d.subtract(var11).normalize().scale(2.5));
            }

            this.I = var49;
            this.x = Math.max(0.0, this.x - var7 * 1.6);
            this.m = Math.max(0.0, this.m - var7);
            if (h8.S(var3.player) && !this.j) {
               this.m = Math.max(this.m, 1.1);
               this.Y = 0.0;
            }

            this.j = h8.S(var3.player);
            this.Y = Math.min(1.0, this.Y + var7 / 0.65);
            this.X = this.X.add(var16.subtract(this.d).scale(26.0 * var7)).scale(Math.pow(0.004, var7));
            this.d = this.d.add(this.X.scale(var7));
            double var19 = this.X.length();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.X.x, this.X.z) : this.S;
            this.z = this.z + o(var21 - this.z) * (float)Math.min(1.0, var7 * 6.0);
            this.n = this.n + ((float)Math.clamp(-this.X.y * 0.35, -0.6, 0.6) - this.n) * (float)Math.min(1.0, var7 * 5.0);
            Vec3 var22 = this.d.add(0.0, Math.sin(this.f * 2.1) * 0.05 * var9 + (!var6 && this.Y < 1.0 ? Math.sin(this.Y * Math.PI) * 0.12 * var9 : 0.0), 0.0);
            Vec3 var23 = (var3.hitResult != null && var3.hitResult.getType() != Type.MISS
                  ? var3.hitResult.getLocation()
                  : var11.add(var3.player.getViewVector(var1).scale(6.0)))
               .subtract(var22);
            double var24 = o((float)(Math.atan2(-var23.x, var23.z) - this.z));
            double var26 = -Math.atan2(var23.y, var23.horizontalDistance());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.a = this.a + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.f * 0.6) * 0.35) - this.a) * (1.0 - Math.exp(-4.0 * var7));
            this.H = this.H + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.H) * (1.0 - Math.exp(-4.0 * var7));
            this.C = this.C + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.C) * Math.min(1.0, var7 * 3.0);
            this.h = this.h + (6.5 + Math.min(var19, 2.5) * 0.6 - this.C * 3.0 - this.h) * Math.min(1.0, var7 * 2.0);
            this.A = (this.A + var7 * this.h) % (Math.PI * 2);
            double var29 = Math.sin(this.A) * (1.0 - this.C * 0.75) + this.C * 0.15;
            int var31 = var3.options.getCameraType().isFirstPerson() && !this.s.getValue() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  fn.G();
               }

               PetsModule.f var36 = this.U.getOrDefault(var5, this.U.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = c.y;
                  var34 = this.P.getValue() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;

                  var33 = switch ((String)this.K.getValue()) {
                     case "Theme" -> c.y;
                     case "Custom" -> this.V.getValue();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.J();
                  var33 = var36.C();
                  var34 = var36.u.getValue() / 100.0;
               }

               this.i.removeIf(var1x -> this.f - var1x.birth > var1x.lifetime);
               if (this.P.getValue() && var7 > 0.0) {
                  this.F = this.F + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.F >= 1.0) {
                     this.F--;
                     Vec3 var50 = var22.add(this.l.nextGaussian() * 0.06 * var9, this.l.nextGaussian() * 0.06 * var9, this.l.nextGaussian() * 0.06 * var9);
                     this.i.addLast(new PetsModule.y(var50, this.f, 0.8 + this.l.nextDouble() * 0.8, 0.8 + this.l.nextDouble() * 0.8));

                     while (this.i.size() > 96) {
                        this.i.removeFirst();
                     }
                  }
               }

               if (!this.P.getValue()) {
                  this.i.clear();
                  this.F = 0.0;
               }

               BlockHitResult var51 = var3.level.clip(new ClipContext(var22, var22.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
               Vec3 var53 = var51.getType() == Type.BLOCK ? var51.getLocation().add(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.y - var53.y) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(var2.projectionMatrix).mul(var2.viewRotationMatrix);
               Vector3f var42 = new Quaternionf(var2.orientation).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               iy.a var43 = iy.A(
                  var22.add(0.0, 0.32 * var9, 0.0),
                  var2.pos,
                  var41,
                  var42,
                  var3.getWindow().getGuiScaledWidth(),
                  var3.getWindow().getGuiScaledHeight(),
                  RenderSystem.getDevice().getDeviceInfo().isZZeroToOne()
               );
               if (var43 != null) {
                  this.L = System.nanoTime();
               }

               double var44 = this.Y < 1.0 ? (1.0 - Math.pow(1.0 - this.Y, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6 ? new Quaternionf().rotationY(-this.z) : new Quaternionf().rotationY((float)(-this.z + var44)).rotateX(this.n);
               this.k = new PetsModule.o(
                  var5,
                  var36.Q.getValue(),
                  var4,
                  var22,
                  var2.pos.subtract(var22).normalize(),
                  var46,
                  var9,
                  this.f,
                  var19,
                  var29,
                  this.a,
                  this.H,
                  Math.max(this.m, this.x),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.i),
                  var43
               );
            }
         }
      }
   }

   private static float o(float var0) {
      return (float)Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public static double S(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean v(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean G() {
      return !this.isEnabled() ? false : this.k != null && this.k.glowStrength > 0.0;
   }

   public void E(fz var1, boolean var2) {
      if (this.isEnabled()) {
         PetsModule.o var3 = this.k;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.q(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, vv.N(-16777216, var3.shadowAlpha));
            }

            sh.s(var1, var3, var2);

            for (PetsModule.y var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.K(
                  var5.point.x,
                  var5.point.y + 0.08 * (1.0 - var6),
                  var5.point.z,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  vv.N(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean O(String var1, String var2, ib.v var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case o -> -996986;
            case V -> -1415819;
            default -> c.y;
         };
         if (this.isRuntimeActive()
            && wp.Q()
            && !getMc().gameRenderer.gameRenderState().guiRenderState.isHudHidden
            && !(getMc().gui.screen() instanceof f8)
            && this.W.getValue()
            && this.k != null
            && this.k.label != null
            && System.nanoTime() - this.L <= 250000000L) {
            this.e.removeIf(var1x -> var1x.title.equals(var1));
            if (this.g != null && this.g.title.equals(var1)) {
               this.g = null;
            }

            this.e.addLast(new PetsModule.u(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.e.size() > 4) {
               this.e.removeFirst();
            }

            this.m = Math.max(this.m, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void L(String var1) {
      this.e.removeIf(var1x -> var1x.title.equals(var1));
      if (this.g != null && this.g.title.equals(var1)) {
         this.g = null;
      }
   }

   public void P(GuiGraphicsExtractor var1) {
      if (this.isEnabled()) {
         PetsModule.o var2 = this.k;
         if (this.isRuntimeActive() && this.W.getValue() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.g != null && (var3 - this.t) / 1000000L > this.g.duration + 220L) {
               this.g = null;
            }

            if (this.g == null && !this.e.isEmpty()) {
               this.g = this.e.removeFirst();
               this.t = var3;
            }

            if (this.g != null) {
               double var5 = (var3 - this.t) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - this.g.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = ic.P(this.g.title, 220, ic.b.R);
               String var14 = ic.P(this.g.text, 220, ic.b.c);
               int var15 = Math.max(90, Math.max(ic.z(var13, ic.b.R), ic.E(var14)) + 24);
               int var16 = Math.clamp(Math.round(var2.label.x() - var15 / 2.0F), 4, Math.max(4, getMc().getWindow().getGuiScaledWidth() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.y()) - 42);
               var1.pose().pushMatrix();

               try {
                  var1.pose().translate(var2.label.x(), var2.label.y());
                  var1.pose().scale((float)var11, (float)var11);
                  var1.pose().translate(-var2.label.x(), -var2.label.y());
                  h5.q(var1, var16, var17, var15, 34.0F, 7.0F, vv.O(-185207568, var7));
                  int var18 = Math.clamp(Math.round(var2.label.x()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.fill(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, vv.O(-185207568, var7));
                  }

                  h5.q(var1, var16 + 8, var17 + 9, 3.0F, 3.0F, 1.0F, vv.O(this.g.color, var7));
                  ic.Y(var1, var13, var16 + 16, var17 + 5, vv.O(-14539990, var7), ic.b.R);
                  ic.H(var1, var14, var16 + 8, var17 + 19, vv.O(-11381667, var7));
               } finally {
                  var1.pose().popMatrix();
               }
            }
         }
      }
   }

   private final class f {
      final com.larpclient.module.o Q;
      final com.larpclient.module.m n;
      final com.larpclient.module.h j;
      final com.larpclient.module.b u;
      final com.larpclient.module.h m;

      f(String var2, int var3, String... var4) {
         this.Q = PetsModule.this.addSetting(new com.larpclient.module.o(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.n = PetsModule.this.addSetting(new com.larpclient.module.m(var2 + " natural colours", "Use the finish palette", false));
         this.j = PetsModule.this.addSetting(new com.larpclient.module.h(var2 + " colour", "Main companion color", -1459895));
         this.u = PetsModule.this.addSetting(new com.larpclient.module.b(var2 + " glow", "Magic glow strength in percent", var3, 0.0, 100.0, 5.0));
         this.m = PetsModule.this.addSetting(new com.larpclient.module.h(var2 + " glow colour", "Companion glow color", -1459895));
         this.Q.a(() -> PetsModule.this.E.w(var2) && !PetsModule.this.r.getValue());
         this.n.a(() -> this.Q.j());
         this.j.a(() -> this.Q.j() && !this.n.getValue());
         this.u.a(() -> this.Q.j());
         this.m.a(() -> this.Q.j() && this.u.getValue() > 0.0);
      }

      int J() {
         int var1;
         if (this.n.getValue()) {
            var1 = switch ((String)this.Q.getValue()) {
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
            var1 = this.j.getValue().equals(this.j.f()) ? c.y : this.j.getValue();
         }
         return switch ((String)this.Q.getValue()) {
            case "Gold" -> vv.Q(var1, -12168, 0.4);
            case "Obsidian" -> vv.Q(var1, -15724259, 0.65);
            case "Crystal" -> vv.Q(var1, -1639937, 0.25);
            case "Silk" -> vv.Q(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int C() {
         return this.m.getValue().equals(this.m.f()) ? c.y : this.m.getValue();
      }
   }

   public record o(
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
      List<PetsModule.y> sparks,
      iy.a label
   ) {
   }

   private record u(String title, String text, int color, long duration, ib.v type) {
   }

   private record y(Vec3 point, double birth, double lifetime, double scale) {
   }
}

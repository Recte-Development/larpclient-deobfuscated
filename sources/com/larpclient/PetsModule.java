package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dk;
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
   private static PetsModule V;
   private final com.larpclient.module.d6 r = this.addSetting(
      new com.larpclient.module.d6("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final dh o = this.addSetting(new dh("Realism", "Detailed anatomy, natural finishes and reactive gaze", false));
   private final com.larpclient.module.d6 T = this.addSetting(
      new com.larpclient.module.d6(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final com.larpclient.module.d9 Z = this.addSetting(new com.larpclient.module.d9("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final com.larpclient.module.d9 H = this.addSetting(new com.larpclient.module.d9("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.d6 A = this.addSetting(
      new com.larpclient.module.d6("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind")
   );
   private final dh C = this.addSetting(new dh("Sparkle trail", "Sparks follow the companion", true));
   private final dh O = this.addSetting(new dh("In first person", "Show the companion in first person", true));
   private final dh m = this.addSetting(new dh("Speech bubbles", "Show notifications above an on-screen companion", true));
   private final com.larpclient.module.d6 z = this.addSetting(
      new com.larpclient.module.d6("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom")
   );
   private final dk h = this.addSetting(new dk("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, PetsModule.c> x = new LinkedHashMap<>();
   private Object U;
   private Object u;
   private Vec3 L;
   private Vec3 E = Vec3.ZERO;
   private float R = Float.NaN;
   private float n;
   private float g;
   private double d;
   private double l;
   private double M;
   private double P;
   private double j;
   private double X;
   private double k = 6.5;
   private double D;
   private double c;
   private double q;
   private int G;
   private boolean W;
   private String K;
   private final ArrayDeque<PetsModule.a> w = new ArrayDeque<>();
   private final ArrayDeque<PetsModule.p> e = new ArrayDeque<>();
   private PetsModule.p i;
   private long J;
   private long v;
   private PetsModule.b y;
   private final Random S = new Random(1279349328L);
   private double N;

   public PetsModule() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      V = this;
      this.r.H(() -> !this.o.P());
      this.H.H(() -> !this.o.P());
      this.T.H(() -> this.o.P());
      this.Z.H(() -> this.o.P());
      this.x.put("Dragon", new PetsModule.c("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.x.put("Jellyfish", new PetsModule.c("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.x.put("Rune Orb", new PetsModule.c("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.x.put("Ghost", new PetsModule.c("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.m.h(var1 -> {
         if (!var1) {
            this.N();
            this.e.clear();
            this.i = null;
         }
      });
      this.z.H(() -> this.o.P() && !R(this.T.P()));
      this.h.H(() -> this.z.B() && this.z.S("Custom"));
   }

   public static PetsModule t() {
      return V;
   }

   public List<df<?>> J() {
      return List.of(this.o, this.r, this.T, this.H, this.Z);
   }

   public List<df<?>> Y() {
      return List.of(this.A, this.O, this.C, this.m);
   }

   public List<df<?>> j() {
      ArrayList var1 = new ArrayList<>(List.of(this.z, this.h));

      for (PetsModule.c var3 : this.x.values()) {
         var1.addAll(List.of(var3.u, var3.s, var3.I));
      }

      return List.copyOf(var1);
   }

   public List<df<?>> S() {
      ArrayList var1 = new ArrayList();

      for (PetsModule.c var3 : this.x.values()) {
         var1.addAll(List.of(var3.A, var3.b));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.B();
      mt.U("Pets", "Local companion activated: {}", this.r.P());
   }

   @Override
   public void onDisable() {
      this.B();
   }

   public void B() {
      this.y = null;
      this.v = 0L;
      this.N();
      this.U = this.u = null;
      this.L = null;
      this.E = Vec3.ZERO;
      this.R = Float.NaN;
      this.y = null;
      this.w.clear();
      this.e.clear();
      this.i = null;
      this.v = 0L;
      this.q = ty.K();
      this.K = null;
      this.d = this.l = this.M = this.P = this.X = this.D = this.c = 0.0;
      this.j = 1.0;
      this.k = 6.5;
      this.G = 0;
      this.W = (boolean)0;
      this.N = 0.0;
      this.S.setSeed(1279349328L);
   }

   private void N() {
      PetsModule.p var1 = this.i;
      long var2 = this.J;
      List var4 = List.copyOf(this.e);
      this.i = null;
      this.e.clear();
      oj var5 = oj.K();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.v(var1.title, var1.text, var1.type, var6);
         }
      }

      for (PetsModule.p var7 : var4) {
         var5.v(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void O() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            this.P = Math.max(this.P, 1.5);
            this.j = 0.0;
         }
      }
   }

   public void r(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.y = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            boolean var4 = this.o.P();
            String var5 = var4 ? this.T.P() : this.r.P();
            boolean var6 = R(var5);
            if (this.U != var3.level || this.u != var3.player || !Objects.equals(this.K, var5 + ":" + var4)) {
               this.B();
               this.U = var3.level;
               this.u = var3.player;
               this.K = var5 + ":" + var4;
               this.q = ty.K();
            }

            double var7 = Math.clamp(ty.K() - this.q, 0.0, 0.1);
            this.q = ty.K();
            this.c += var7;
            double var9 = T(var5, var4 ? this.Z.P() : this.H.P());
            Vec3 var11 = var3.player.getEyePosition(var1);
            Vec3 var12 = var3.player.getDeltaMovement();
            float var13 = (float)Math.toRadians(var3.player.getViewYRot(var1));
            if (Float.isNaN(this.R)) {
               this.R = var13;
            }

            if (var12.horizontalDistance() * 20.0 > 1.0) {
               this.R = this.R + q((float)Math.atan2(-var12.x, var12.z) - this.R) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.options.keyUse.isDown() || var3.options.keyAttack.isDown()) && this.L != null) {
               Vec3 var14 = this.L.subtract(var11);
               if (var14.length() < 6.0 && var14.length() > 0.001 && var14.normalize().dot(var3.player.getViewVector(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.R = this.R + q(var13 - this.R) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            Vec3 var47 = new Vec3(-Math.sin(this.R), 0.0, Math.cos(this.R));
            Vec3 var15 = new Vec3(-var47.z, 0.0, var47.x);

            Vec3 var16 = switch ((String)this.A.P()) {
               case "Orbit" -> var11.add(Math.cos(this.c * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.c * 1.7) * 0.15, Math.sin(this.c * 0.9) * 1.3 * var9);
               case "Behind" -> var11.add(var47.scale(-1.4 * var9)).add(var15.scale(0.35)).add(0.0, -0.95, 0.0);
               default -> var11.add(var15.scale(0.72 * var9)).add(var47.scale(-0.35)).add(0.0, 0.28 * var9, 0.0);
            };
            BlockHitResult var48 = var3.level.clip(new ClipContext(var11, var16, Block.COLLIDER, Fluid.NONE, var3.player));
            if (var48.getType() == Type.BLOCK) {
               var16 = var48.getLocation().subtract(var16.subtract(var11).normalize().scale(0.3 * var9));
            }

            if (this.L == null || this.L.distanceToSqr(var16) > 400.0) {
               this.L = var16;
               this.E = Vec3.ZERO;
               this.w.clear();
            }

            int var49 = var3.player.hurtTime;
            if (!var6 && var49 > this.G && var49 >= 6) {
               this.M = 1.0;
               this.E = this.E.add(this.L.subtract(var11).normalize().scale(2.5));
            }

            this.G = var49;
            this.M = Math.max(0.0, this.M - var7 * 1.6);
            this.P = Math.max(0.0, this.P - var7);
            if (_8.Q(var3.player) && !this.W) {
               this.P = Math.max(this.P, 1.1);
               this.j = 0.0;
            }

            this.W = _8.Q(var3.player);
            this.j = Math.min(1.0, this.j + var7 / 0.65);
            this.E = this.E.add(var16.subtract(this.L).scale(26.0 * var7)).scale(Math.pow(0.004, var7));
            this.L = this.L.add(this.E.scale(var7));
            double var19 = this.E.length();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.E.x, this.E.z) : this.R;
            this.n = this.n + q(var21 - this.n) * (float)Math.min(1.0, var7 * 6.0);
            this.g = this.g + ((float)Math.clamp(-this.E.y * 0.35, -0.6, 0.6) - this.g) * (float)Math.min(1.0, var7 * 5.0);
            Vec3 var22 = this.L.add(0.0, Math.sin(this.c * 2.1) * 0.05 * var9 + (!var6 && this.j < 1.0 ? Math.sin(this.j * Math.PI) * 0.12 * var9 : 0.0), 0.0);
            Vec3 var23 = (var3.hitResult != null && var3.hitResult.getType() != Type.MISS
                  ? var3.hitResult.getLocation()
                  : var11.add(var3.player.getViewVector(var1).scale(6.0)))
               .subtract(var22);
            double var24 = q((float)(Math.atan2(-var23.x, var23.z) - this.n));
            double var26 = -Math.atan2(var23.y, var23.horizontalDistance());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.d = this.d + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.c * 0.6) * 0.35) - this.d) * (1.0 - Math.exp(-4.0 * var7));
            this.l = this.l + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.l) * (1.0 - Math.exp(-4.0 * var7));
            this.D = this.D + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.D) * Math.min(1.0, var7 * 3.0);
            this.k = this.k + (6.5 + Math.min(var19, 2.5) * 0.6 - this.D * 3.0 - this.k) * Math.min(1.0, var7 * 2.0);
            this.X = (this.X + var7 * this.k) % (Math.PI * 2);
            double var29 = Math.sin(this.X) * (1.0 - this.D * 0.75) + this.D * 0.15;
            int var31 = var3.options.getCameraType().isFirstPerson() && !this.O.P() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  ww.n();
               }

               PetsModule.c var36 = this.x.getOrDefault(var5, this.x.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = com.larpclient.f.C;
                  var34 = this.C.P() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;

                  var33 = switch ((String)this.z.P()) {
                     case "Theme" -> com.larpclient.f.C;
                     case "Custom" -> this.h.P();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.K();
                  var33 = var36.V();
                  var34 = var36.A.P() / 100.0;
               }

               this.w.removeIf(var1x -> this.c - var1x.birth > var1x.lifetime);
               if (this.C.P() && var7 > 0.0) {
                  this.N = this.N + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.N >= 1.0) {
                     this.N--;
                     Vec3 var50 = var22.add(this.S.nextGaussian() * 0.06 * var9, this.S.nextGaussian() * 0.06 * var9, this.S.nextGaussian() * 0.06 * var9);
                     this.w.addLast(new PetsModule.a(var50, this.c, 0.8 + this.S.nextDouble() * 0.8, 0.8 + this.S.nextDouble() * 0.8));

                     while (this.w.size() > 96) {
                        this.w.removeFirst();
                     }
                  }
               }

               if (!this.C.P()) {
                  this.w.clear();
                  this.N = 0.0;
               }

               BlockHitResult var51 = var3.level.clip(new ClipContext(var22, var22.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
               Vec3 var53 = var51.getType() == Type.BLOCK ? var51.getLocation().add(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.y - var53.y) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(var2.projectionMatrix).mul(var2.viewRotationMatrix);
               Vector3f var42 = new Quaternionf(var2.orientation).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               di.a var43 = di.k(
                  var22.add(0.0, 0.32 * var9, 0.0),
                  var2.pos,
                  var41,
                  var42,
                  var3.getWindow().getGuiScaledWidth(),
                  var3.getWindow().getGuiScaledHeight(),
                  RenderSystem.getDevice().getDeviceInfo().isZZeroToOne()
               );
               if (var43 != null) {
                  this.v = System.nanoTime();
               }

               double var44 = this.j < 1.0 ? (1.0 - Math.pow(1.0 - this.j, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6 ? new Quaternionf().rotationY(-this.n) : new Quaternionf().rotationY((float)(-this.n + var44)).rotateX(this.g);
               this.y = new PetsModule.b(
                  var5,
                  var36.u.P(),
                  var4,
                  var22,
                  var2.pos.subtract(var22).normalize(),
                  var46,
                  var9,
                  this.c,
                  var19,
                  var29,
                  this.d,
                  this.l,
                  Math.max(this.P, this.M),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.w),
                  var43
               );
            }
         }
      }
   }

   private static float q(float var0) {
      return (float)Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public static double T(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean R(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean t() {
      return !this.isEnabled() ? false : this.y != null && this.y.glowStrength > 0.0;
   }

   public void k(ki var1, boolean var2) {
      if (this.isEnabled()) {
         PetsModule.b var3 = this.y;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.r(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, _3.A(-16777216, var3.shadowAlpha));
            }

            kd.I(var1, var3, var2);

            for (PetsModule.a var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.t(
                  var5.point.x,
                  var5.point.y + 0.08 * (1.0 - var6),
                  var5.point.z,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  _3.A(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean z(String var1, String var2, oj.u var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case B -> -996986;
            case i -> -1415819;
            default -> com.larpclient.f.C;
         };
         if (this.isRuntimeActive()
            && com.larpclient.h.V()
            && !getMc().gameRenderer.gameRenderState().guiRenderState.isHudHidden
            && !(getMc().gui.screen() instanceof hw)
            && this.m.P()
            && this.y != null
            && this.y.label != null
            && System.nanoTime() - this.v <= 250000000L) {
            this.e.removeIf(var1x -> var1x.title.equals(var1));
            if (this.i != null && this.i.title.equals(var1)) {
               this.i = null;
            }

            this.e.addLast(new PetsModule.p(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.e.size() > 4) {
               this.e.removeFirst();
            }

            this.P = Math.max(this.P, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void F(String var1) {
      this.e.removeIf(var1x -> var1x.title.equals(var1));
      if (this.i != null && this.i.title.equals(var1)) {
         this.i = null;
      }
   }

   public void F(GuiGraphicsExtractor var1) {
      if (this.isEnabled()) {
         PetsModule.b var2 = this.y;
         if (this.isRuntimeActive() && this.m.P() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.i != null && (var3 - this.J) / 1000000L > this.i.duration + 220L) {
               this.i = null;
            }

            if (this.i == null && !this.e.isEmpty()) {
               this.i = this.e.removeFirst();
               this.J = var3;
            }

            if (this.i != null) {
               double var5 = (var3 - this.J) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - this.i.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = k6.Q(this.i.title, 220, k6.v.B);
               String var14 = k6.Q(this.i.text, 220, k6.v.K);
               int var15 = Math.max(90, Math.max(k6.w(var13, k6.v.B), k6.x(var14)) + 24);
               int var16 = Math.clamp(Math.round(var2.label.x() - var15 / 2.0F), 4, Math.max(4, getMc().getWindow().getGuiScaledWidth() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.y()) - 42);
               var1.pose().pushMatrix();

               try {
                  var1.pose().translate(var2.label.x(), var2.label.y());
                  var1.pose().scale((float)var11, (float)var11);
                  var1.pose().translate(-var2.label.x(), -var2.label.y());
                  c7.n(var1, var16, var17, var15, 34.0F, 7.0F, _3.U(-185207568, var7));
                  int var18 = Math.clamp(Math.round(var2.label.x()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.fill(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, _3.U(-185207568, var7));
                  }

                  c7.n(var1, var16 + 8, var17 + 9, 3.0F, 3.0F, 1.0F, _3.U(this.i.color, var7));
                  k6.r(var1, var13, var16 + 16, var17 + 5, _3.U(-14539990, var7), k6.v.B);
                  k6.G(var1, var14, var16 + 8, var17 + 19, _3.U(-11381667, var7));
               } finally {
                  var1.pose().popMatrix();
               }
            }
         }
      }
   }

   private record a(Vec3 point, double birth, double lifetime, double scale) {
   }

   public record b(
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
      List<PetsModule.a> sparks,
      di.a label
   ) {
   }

   private final class c {
      final com.larpclient.module.d6 u;
      final dh s;
      final dk I;
      final com.larpclient.module.d9 A;
      final dk b;

      c(String var2, int var3, String... var4) {
         this.u = PetsModule.this.addSetting(new com.larpclient.module.d6(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.s = PetsModule.this.addSetting(new dh(var2 + " natural colours", "Use the finish palette", false));
         this.I = PetsModule.this.addSetting(new dk(var2 + " colour", "Main companion color", -1459895));
         this.A = PetsModule.this.addSetting(new com.larpclient.module.d9(var2 + " glow", "Magic glow strength in percent", var3, 0.0, 100.0, 5.0));
         this.b = PetsModule.this.addSetting(new dk(var2 + " glow colour", "Companion glow color", -1459895));
         this.u.H(() -> PetsModule.this.r.S(var2) && !PetsModule.this.o.P());
         this.s.H(() -> this.u.B());
         this.I.H(() -> this.u.B() && !this.s.P());
         this.A.H(() -> this.u.B());
         this.b.H(() -> this.u.B() && this.A.P() > 0.0);
      }

      int K() {
         int var1;
         if (this.s.P()) {
            var1 = switch ((String)this.u.P()) {
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
            var1 = this.I.P().equals(this.I.a()) ? com.larpclient.f.C : this.I.P();
         }
         return switch ((String)this.u.P()) {
            case "Gold" -> _3.D(var1, -12168, 0.4);
            case "Obsidian" -> _3.D(var1, -15724259, 0.65);
            case "Crystal" -> _3.D(var1, -1639937, 0.25);
            case "Silk" -> _3.D(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int V() {
         return this.b.P().equals(this.b.a()) ? com.larpclient.f.C : this.b.P();
      }
   }

   private record p(String title, String text, int color, long duration, oj.u type) {
   }
}

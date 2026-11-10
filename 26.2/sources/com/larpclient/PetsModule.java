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
   private static PetsModule F;
   private final com.larpclient.module.l V = this.addSetting(
      new com.larpclient.module.l("Pet", "Companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Ghost", "Tung Tung Tung Sahur")
   );
   private final com.larpclient.module.s r = this.addSetting(
      new com.larpclient.module.s("Realism", "Detailed anatomy, natural finishes and reactive gaze", false)
   );
   private final com.larpclient.module.l d = this.addSetting(
      new com.larpclient.module.l(
         "Realistic Pet", "Detailed companion style", "Tung Tung Tung Sahur", "Dragon", "Jellyfish", "Rune Orb", "Bee", "Ghost", "Tung Tung Tung Sahur"
      )
   );
   private final com.larpclient.module.e P = this.addSetting(new com.larpclient.module.e("Realistic Size", "Detailed companion size", 1.0, 0.5, 2.5, 0.05));
   private final com.larpclient.module.e j = this.addSetting(new com.larpclient.module.e("Size", "Pet size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.l v = this.addSetting(
      new com.larpclient.module.l("Position", "Where the companion follows", "Shoulder", "Shoulder", "Orbit", "Behind")
   );
   private final com.larpclient.module.s l = this.addSetting(new com.larpclient.module.s("Sparkle trail", "Sparks follow the companion", true));
   private final com.larpclient.module.s b = this.addSetting(new com.larpclient.module.s("In first person", "Show the companion in first person", true));
   private final com.larpclient.module.s U = this.addSetting(
      new com.larpclient.module.s("Speech bubbles", "Show notifications above an on-screen companion", true)
   );
   private final com.larpclient.module.l q = this.addSetting(
      new com.larpclient.module.l("Color mode", "Realistic companion palette", "Natural", "Natural", "Theme", "Custom")
   );
   private final com.larpclient.module.i f = this.addSetting(new com.larpclient.module.i("Custom color", "Realistic companion glow", -1459895));
   private final Map<String, PetsModule.d> i = new LinkedHashMap<>();
   private Object T;
   private Object o;
   private Vec3 x;
   private Vec3 K = Vec3.ZERO;
   private float A = Float.NaN;
   private float a;
   private float G;
   private double C;
   private double Q;
   private double z;
   private double w;
   private double N;
   private double I;
   private double X = 6.5;
   private double Y;
   private double M;
   private double p;
   private int O;
   private boolean R;
   private String J;
   private final ArrayDeque<PetsModule.s> c = new ArrayDeque<>();
   private final ArrayDeque<PetsModule.v> k = new ArrayDeque<>();
   private PetsModule.v u;
   private long H;
   private long n;
   private PetsModule.m m;
   private final Random W = new Random(1279349328L);
   private double S;

   public PetsModule() {
      super("Pets", "A local companion with reactive motion and speech bubbles", Category.RENDER, -1);
      F = this;
      this.V.P(() -> !this.r.getValue());
      this.j.P(() -> !this.r.getValue());
      this.d.P(() -> this.r.getValue());
      this.P.P(() -> this.r.getValue());
      this.i.put("Dragon", new PetsModule.d("Dragon", 50, "Scales", "Obsidian", "Gold"));
      this.i.put("Jellyfish", new PetsModule.d("Jellyfish", 70, "Glass", "Energy", "Crystal"));
      this.i.put("Rune Orb", new PetsModule.d("Rune Orb", 70, "Glass", "Crystal", "Obsidian"));
      this.i.put("Ghost", new PetsModule.d("Ghost", 50, "Wisp", "Silk", "Glass"));
      this.U.o(var1 -> {
         if (!var1) {
            this.N();
            this.k.clear();
            this.u = null;
         }
      });
      this.q.P(() -> this.r.getValue() && !O(this.d.getValue()));
      this.f.P(() -> this.q.W() && this.q.O("Custom"));
   }

   public static PetsModule I() {
      return F;
   }

   public List<com.larpclient.module.q<?>> y() {
      return List.of(this.r, this.V, this.d, this.j, this.P);
   }

   public List<com.larpclient.module.q<?>> F() {
      return List.of(this.v, this.b, this.l, this.U);
   }

   public List<com.larpclient.module.q<?>> r() {
      ArrayList var1 = new ArrayList<>(List.of(this.q, this.f));

      for (PetsModule.d var3 : this.i.values()) {
         var1.addAll(List.of(var3.x, var3.t, var3.J));
      }

      return List.copyOf(var1);
   }

   public List<com.larpclient.module.q<?>> X() {
      ArrayList var1 = new ArrayList();

      for (PetsModule.d var3 : this.i.values()) {
         var1.addAll(List.of(var3.o, var3.z));
      }

      return List.copyOf(var1);
   }

   @Override
   public void onEnable() {
      this.Q();
      h.U("Pets", "Local companion activated: {}", this.V.getValue());
   }

   @Override
   public void onDisable() {
      this.Q();
   }

   public void Q() {
      this.m = null;
      this.n = 0L;
      this.N();
      this.T = this.o = null;
      this.x = null;
      this.K = Vec3.ZERO;
      this.A = Float.NaN;
      this.m = null;
      this.c.clear();
      this.k.clear();
      this.u = null;
      this.n = 0L;
      this.p = cf.F();
      this.J = null;
      this.C = this.Q = this.z = this.w = this.I = this.Y = this.M = 0.0;
      this.N = 1.0;
      this.X = 6.5;
      this.O = 0;
      this.R = (boolean)0;
      this.S = 0.0;
      this.W.setSeed(1279349328L);
   }

   private void N() {
      PetsModule.v var1 = this.u;
      long var2 = this.H;
      List var4 = List.copyOf(this.k);
      this.u = null;
      this.k.clear();
      s0 var5 = s0.s();
      if (var1 != null) {
         long var6 = var1.duration - (System.nanoTime() - var2) / 1000000L;
         if (var6 > 0L) {
            var5.Z(var1.title, var1.text, var1.type, var6);
         }
      }

      for (PetsModule.v var7 : var4) {
         var5.Z(var7.title, var7.text, var7.type, var7.duration);
      }
   }

   public void e() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            this.w = Math.max(this.w, 1.5);
            this.N = 0.0;
         }
      }
   }

   public void s(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.m = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            Minecraft var3 = getMc();
            boolean var4 = this.r.getValue();
            String var5 = var4 ? this.d.getValue() : this.V.getValue();
            boolean var6 = O(var5);
            if (this.T != var3.level || this.o != var3.player || !Objects.equals(this.J, var5 + ":" + var4)) {
               this.Q();
               this.T = var3.level;
               this.o = var3.player;
               this.J = var5 + ":" + var4;
               this.p = cf.F();
            }

            double var7 = Math.clamp(cf.F() - this.p, 0.0, 0.1);
            this.p = cf.F();
            this.M += var7;
            double var9 = z(var5, var4 ? this.P.getValue() : this.j.getValue());
            Vec3 var11 = var3.player.getEyePosition(var1);
            Vec3 var12 = var3.player.getDeltaMovement();
            float var13 = (float)Math.toRadians(var3.player.getViewYRot(var1));
            if (Float.isNaN(this.A)) {
               this.A = var13;
            }

            if (var12.horizontalDistance() * 20.0 > 1.0) {
               this.A = this.A + o((float)Math.atan2(-var12.x, var12.z) - this.A) * (float)Math.min(1.0, var7 * 3.0);
            } else if ((var3.options.keyUse.isDown() || var3.options.keyAttack.isDown()) && this.x != null) {
               Vec3 var14 = this.x.subtract(var11);
               if (var14.length() < 6.0 && var14.length() > 0.001 && var14.normalize().dot(var3.player.getViewVector(var1)) > Math.cos(Math.toRadians(28.0))) {
                  this.A = this.A + o(var13 - this.A) * (float)Math.min(1.0, var7 * 6.0);
               }
            }

            Vec3 var47 = new Vec3(-Math.sin(this.A), 0.0, Math.cos(this.A));
            Vec3 var15 = new Vec3(-var47.z, 0.0, var47.x);

            Vec3 var16 = switch ((String)this.v.getValue()) {
               case "Orbit" -> var11.add(Math.cos(this.M * 0.9) * 1.3 * var9, -0.15 + Math.sin(this.M * 1.7) * 0.15, Math.sin(this.M * 0.9) * 1.3 * var9);
               case "Behind" -> var11.add(var47.scale(-1.4 * var9)).add(var15.scale(0.35)).add(0.0, -0.95, 0.0);
               default -> var11.add(var15.scale(0.72 * var9)).add(var47.scale(-0.35)).add(0.0, 0.28 * var9, 0.0);
            };
            BlockHitResult var48 = var3.level.clip(new ClipContext(var11, var16, Block.COLLIDER, Fluid.NONE, var3.player));
            if (var48.getType() == Type.BLOCK) {
               var16 = var48.getLocation().subtract(var16.subtract(var11).normalize().scale(0.3 * var9));
            }

            if (this.x == null || this.x.distanceToSqr(var16) > 400.0) {
               this.x = var16;
               this.K = Vec3.ZERO;
               this.c.clear();
            }

            int var49 = var3.player.hurtTime;
            if (!var6 && var49 > this.O && var49 >= 6) {
               this.z = 1.0;
               this.K = this.K.add(this.x.subtract(var11).normalize().scale(2.5));
            }

            this.O = var49;
            this.z = Math.max(0.0, this.z - var7 * 1.6);
            this.w = Math.max(0.0, this.w - var7);
            if (ks.M(var3.player) && !this.R) {
               this.w = Math.max(this.w, 1.1);
               this.N = 0.0;
            }

            this.R = ks.M(var3.player);
            this.N = Math.min(1.0, this.N + var7 / 0.65);
            this.K = this.K.add(var16.subtract(this.x).scale(26.0 * var7)).scale(Math.pow(0.004, var7));
            this.x = this.x.add(this.K.scale(var7));
            double var19 = this.K.length();
            float var21 = var19 > 0.6 ? (float)Math.atan2(-this.K.x, this.K.z) : this.A;
            this.a = this.a + o(var21 - this.a) * (float)Math.min(1.0, var7 * 6.0);
            this.G = this.G + ((float)Math.clamp(-this.K.y * 0.35, -0.6, 0.6) - this.G) * (float)Math.min(1.0, var7 * 5.0);
            Vec3 var22 = this.x.add(0.0, Math.sin(this.M * 2.1) * 0.05 * var9 + (!var6 && this.N < 1.0 ? Math.sin(this.N * Math.PI) * 0.12 * var9 : 0.0), 0.0);
            Vec3 var23 = (var3.hitResult != null && var3.hitResult.getType() != Type.MISS
                  ? var3.hitResult.getLocation()
                  : var11.add(var3.player.getViewVector(var1).scale(6.0)))
               .subtract(var22);
            double var24 = o((float)(Math.atan2(-var23.x, var23.z) - this.a));
            double var26 = -Math.atan2(var23.y, var23.horizontalDistance());
            int var28 = Math.abs(var24) < 1.6 ? 1 : 0;
            this.C = this.C + ((var28 != 0 ? Math.clamp(var24, -1.1, 1.1) : Math.sin(this.M * 0.6) * 0.35) - this.C) * (1.0 - Math.exp(-4.0 * var7));
            this.Q = this.Q + ((var28 != 0 ? Math.clamp(var26, -0.6, 0.6) : 0.0) - this.Q) * (1.0 - Math.exp(-4.0 * var7));
            this.Y = this.Y + (Math.clamp((var19 - 2.5) / 3.0, 0.0, 1.0) - this.Y) * Math.min(1.0, var7 * 3.0);
            this.X = this.X + (6.5 + Math.min(var19, 2.5) * 0.6 - this.Y * 3.0 - this.X) * Math.min(1.0, var7 * 2.0);
            this.I = (this.I + var7 * this.X) % (Math.PI * 2);
            double var29 = Math.sin(this.I) * (1.0 - this.Y * 0.75) + this.Y * 0.15;
            int var31 = var3.options.getCameraType().isFirstPerson() && !this.b.getValue() ? 0 : 1;
            if (var31 != 0) {
               if (var6) {
                  k5.H();
               }

               PetsModule.d var36 = this.i.getOrDefault(var5, this.i.get("Dragon"));
               int var32;
               int var33;
               double var34;
               if (var6) {
                  var32 = -1;
                  var33 = ho.J;
                  var34 = this.l.getValue() ? 0.5 : 0.0;
               } else if (var4) {
                  int var37 = switch (var5) {
                     case "Dragon" -> -10976403;
                     case "Ghost" -> -1840922;
                     case "Jellyfish" -> -7288099;
                     case "Rune Orb" -> -4025601;
                     default -> -2116013;
                  };
                  var32 = var37;

                  var33 = switch ((String)this.q.getValue()) {
                     case "Theme" -> ho.J;
                     case "Custom" -> this.f.getValue();
                     default -> var37;
                  };
                  var34 = 0.6;
               } else {
                  var32 = var36.T();
                  var33 = var36.C();
                  var34 = var36.o.getValue() / 100.0;
               }

               this.c.removeIf(var1x -> this.M - var1x.birth > var1x.lifetime);
               if (this.l.getValue() && var7 > 0.0) {
                  this.S = this.S + var7 * (15.0 + Math.min(36.0, var19 * 18.0));

                  while (this.S >= 1.0) {
                     this.S--;
                     Vec3 var50 = var22.add(this.W.nextGaussian() * 0.06 * var9, this.W.nextGaussian() * 0.06 * var9, this.W.nextGaussian() * 0.06 * var9);
                     this.c.addLast(new PetsModule.s(var50, this.M, 0.8 + this.W.nextDouble() * 0.8, 0.8 + this.W.nextDouble() * 0.8));

                     while (this.c.size() > 96) {
                        this.c.removeFirst();
                     }
                  }
               }

               if (!this.l.getValue()) {
                  this.c.clear();
                  this.S = 0.0;
               }

               BlockHitResult var51 = var3.level.clip(new ClipContext(var22, var22.add(0.0, -4.5, 0.0), Block.COLLIDER, Fluid.ANY, var3.player));
               Vec3 var53 = var51.getType() == Type.BLOCK ? var51.getLocation().add(0.0, 0.012, 0.0) : null;
               double var55 = var53 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var22.y - var53.y) / 4.5), 2.0) * 0.4;
               Matrix4f var41 = new Matrix4f(var2.projectionMatrix).mul(var2.viewRotationMatrix);
               Vector3f var42 = new Quaternionf(var2.orientation).transform(new Vector3f(0.0F, 1.0F, 0.0F));
               k7.o var43 = k7.I(
                  var22.add(0.0, 0.32 * var9, 0.0),
                  var2.pos,
                  var41,
                  var42,
                  var3.getWindow().getGuiScaledWidth(),
                  var3.getWindow().getGuiScaledHeight(),
                  RenderSystem.getDevice().getDeviceInfo().isZZeroToOne()
               );
               if (var43 != null) {
                  this.n = System.nanoTime();
               }

               double var44 = this.N < 1.0 ? (1.0 - Math.pow(1.0 - this.N, 3.0)) * Math.PI * 2.0 : 0.0;
               Quaternionf var46 = var6 ? new Quaternionf().rotationY(-this.a) : new Quaternionf().rotationY((float)(-this.a + var44)).rotateX(this.G);
               this.m = new PetsModule.m(
                  var5,
                  var36.x.getValue(),
                  var4,
                  var22,
                  var2.pos.subtract(var22).normalize(),
                  var46,
                  var9,
                  this.M,
                  var19,
                  var29,
                  this.C,
                  this.Q,
                  Math.max(this.w, this.z),
                  var32,
                  var33,
                  var34,
                  var53,
                  var55,
                  List.copyOf(this.c),
                  var43
               );
            }
         }
      }
   }

   private static float o(float var0) {
      return (float)Math.atan2(Math.sin(var0), Math.cos(var0));
   }

   public static double z(String var0, double var1) {
      return var1 * (var0.equals("Tung Tung Tung Sahur") ? 1.2 : 1.0);
   }

   static boolean O(String var0) {
      return var0.equals("Tung Tung Tung Sahur");
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.m != null && this.m.glowStrength > 0.0;
   }

   public void X(kj var1, boolean var2) {
      if (this.isEnabled()) {
         PetsModule.m var3 = this.m;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            if (!var2 && var3.shadow != null) {
               var1.I(var3.shadow, 0.18 * var3.scale, 0.16 * var3.scale, uh.H(-16777216, var3.shadowAlpha));
            }

            kw.C(var1, var3, var2);

            for (PetsModule.s var5 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var5.birth) / var5.lifetime, 0.0, 1.0);
               var1.P(
                  var5.point.x,
                  var5.point.y + 0.08 * (1.0 - var6),
                  var5.point.z,
                  0.035 * var3.scale * var6 * var5.scale,
                  var3.time,
                  uh.H(var3.glow, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   public boolean l(String var1, String var2, s0.u var3, long var4) {
      if (this.isEnabled()) {
         int var6 = switch (var3) {
            case p -> -996986;
            case M -> -1415819;
            default -> ho.J;
         };
         if (this.isRuntimeActive()
            && kz.x()
            && !getMc().gameRenderer.gameRenderState().guiRenderState.isHudHidden
            && !(getMc().gui.screen() instanceof hf)
            && this.U.getValue()
            && this.m != null
            && this.m.label != null
            && System.nanoTime() - this.n <= 250000000L) {
            this.k.removeIf(var1x -> var1x.title.equals(var1));
            if (this.u != null && this.u.title.equals(var1)) {
               this.u = null;
            }

            this.k.addLast(new PetsModule.v(var1, var2 == null ? "" : var2, var6, Math.max(0L, var4), var3));

            while (this.k.size() > 4) {
               this.k.removeFirst();
            }

            this.w = Math.max(this.w, 0.6);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void A(String var1) {
      this.k.removeIf(var1x -> var1x.title.equals(var1));
      if (this.u != null && this.u.title.equals(var1)) {
         this.u = null;
      }
   }

   public void b(GuiGraphicsExtractor var1) {
      if (this.isEnabled()) {
         PetsModule.m var2 = this.m;
         if (this.isRuntimeActive() && this.U.getValue() && var2 != null && var2.label != null) {
            long var3 = System.nanoTime();
            if (this.u != null && (var3 - this.H) / 1000000L > this.u.duration + 220L) {
               this.u = null;
            }

            if (this.u == null && !this.k.isEmpty()) {
               this.u = this.k.removeFirst();
               this.H = var3;
            }

            if (this.u != null) {
               double var5 = (var3 - this.H) / 1000000.0;
               double var7 = Math.clamp(var5 / 120.0, 0.0, 1.0) * (1.0 - Math.clamp((var5 - this.u.duration) / 220.0, 0.0, 1.0));
               double var9 = Math.clamp(var5 / 220.0, 0.0, 1.0);
               double var11 = 1.0 + 2.70158 * Math.pow(var9 - 1.0, 3.0) + 1.70158 * Math.pow(var9 - 1.0, 2.0);
               String var13 = bj.v(this.u.title, 220, bj.j.T);
               String var14 = bj.v(this.u.text, 220, bj.j.c);
               int var15 = Math.max(90, Math.max(bj.G(var13, bj.j.T), bj.q(var14)) + 24);
               int var16 = Math.clamp(Math.round(var2.label.x() - var15 / 2.0F), 4, Math.max(4, getMc().getWindow().getGuiScaledWidth() - var15 - 4));
               int var17 = Math.max(4, Math.round(var2.label.y()) - 42);
               var1.pose().pushMatrix();

               try {
                  var1.pose().translate(var2.label.x(), var2.label.y());
                  var1.pose().scale((float)var11, (float)var11);
                  var1.pose().translate(-var2.label.x(), -var2.label.y());
                  kv.f(var1, var16, var17, var15, 34.0F, 7.0F, uh.F(-185207568, var7));
                  int var18 = Math.clamp(Math.round(var2.label.x()), var16 + 10, var16 + var15 - 10);

                  for (int var19 = 0; var19 < 6; var19++) {
                     var1.fill(var18 - 5 + var19, var17 + 34 + var19, var18 + 5 - var19, var17 + 35 + var19, uh.F(-185207568, var7));
                  }

                  kv.f(var1, var16 + 8, var17 + 9, 3.0F, 3.0F, 1.0F, uh.F(this.u.color, var7));
                  bj.z(var1, var13, var16 + 16, var17 + 5, uh.F(-14539990, var7), bj.j.T);
                  bj.h(var1, var14, var16 + 8, var17 + 19, uh.F(-11381667, var7));
               } finally {
                  var1.pose().popMatrix();
               }
            }
         }
      }
   }

   private final class d {
      final com.larpclient.module.l x;
      final com.larpclient.module.s t;
      final com.larpclient.module.i J;
      final com.larpclient.module.e o;
      final com.larpclient.module.i z;

      d(String var2, int var3, String... var4) {
         this.x = PetsModule.this.addSetting(new com.larpclient.module.l(var2 + " finish", "Companion surface finish", var4[0], var4));
         this.t = PetsModule.this.addSetting(new com.larpclient.module.s(var2 + " natural colours", "Use the finish palette", false));
         this.J = PetsModule.this.addSetting(new com.larpclient.module.i(var2 + " colour", "Main companion color", -1459895));
         this.o = PetsModule.this.addSetting(new com.larpclient.module.e(var2 + " glow", "Magic glow strength in percent", var3, 0.0, 100.0, 5.0));
         this.z = PetsModule.this.addSetting(new com.larpclient.module.i(var2 + " glow colour", "Companion glow color", -1459895));
         this.x.P(() -> PetsModule.this.V.O(var2) && !PetsModule.this.r.getValue());
         this.t.P(() -> this.x.W());
         this.J.P(() -> this.x.W() && !this.t.getValue());
         this.o.P(() -> this.x.W());
         this.z.P(() -> this.x.W() && this.o.getValue() > 0.0);
      }

      int T() {
         int var1;
         if (this.t.getValue()) {
            var1 = switch ((String)this.x.getValue()) {
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
            var1 = this.J.getValue().equals(this.J.A()) ? ho.J : this.J.getValue();
         }
         return switch ((String)this.x.getValue()) {
            case "Gold" -> uh.q(var1, -12168, 0.4);
            case "Obsidian" -> uh.q(var1, -15724259, 0.65);
            case "Crystal" -> uh.q(var1, -1639937, 0.25);
            case "Silk" -> uh.q(var1, -1122056, 0.18);
            default -> var1;
         };
      }

      int C() {
         return this.z.getValue().equals(this.z.A()) ? ho.J : this.z.getValue();
      }
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
      List<PetsModule.s> sparks,
      k7.o label
   ) {
   }

   private record s(Vec3 point, double birth, double lifetime, double scale) {
   }

   private record v(String title, String text, int color, long duration, s0.u type) {
   }
}

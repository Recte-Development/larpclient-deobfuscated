package com.larpclient;

import com.larpclient.module.FlameHandsModule;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class oj {
   public static final int J = 768;
   private static final Identifier p = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png");
   private static final Identifier i = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame_glow.png");
   private static final oj.c[] R = new oj.c[]{new oj.c(new Random()), new oj.c(new Random())};
   private static Object j;
   private static Object E;
   private static int U = -1;
   private static boolean K;
   private static Vec3 N;

   public static void G() {
      for (oj.c var3 : R) {
         var3.g();
      }

      E = null;
      j = null;
      N = null;
      K = (boolean)0;
   }

   public static void B() {
      Minecraft var0 = Minecraft.getInstance();
      if (j != var0.level || E != var0.player || U != nl.q) {
         G();
         j = var0.level;
         E = var0.player;
         U = nl.q;
      }

      if (var0.player != null) {
         Vec3 var1 = var0.player.position();
         if (N != null && N.distanceToSqr(var1) > 64.0) {
            for (oj.c var5 : R) {
               var5.g();
            }

            _q.c("FlameHands", "Cleared histories after player relocation");
         }

         N = var1;
      }

      if (var0.player == null || var0.level == null || !var0.options.getCameraType().isFirstPerson()) {
         for (oj.c var9 : R) {
            var9.g();
         }
      }
   }

   public static void L(HumanoidArm var0, PoseStack var1, SubmitNodeCollector var2, boolean var3) {
      FlameHandsModule var4 = FlameHandsModule.w();
      Minecraft var5 = Minecraft.getInstance();
      if (var4 != null && var4.isRuntimeActive() && var5.player != null && var5.level != null && var5.options.getCameraType().isFirstPerson()) {
         B();
         oj.c var6 = R[var0 == HumanoidArm.RIGHT ? 0 : 1];
         if (!var4.L(var0 == var5.player.getMainArm(), var3)) {
            var6.g();
         } else if (!K) {
            try {
               oj.x.K();
               Camera var7 = var5.gameRenderer.mainCamera();
               Vec3 var13 = var5.player.getDeltaMovement();
               Vector3f var14 = new Vector3f((float)var13.x, (float)var13.y, (float)var13.z).rotate(new Quaternionf(var7.rotation()).conjugate());
               oj.q var15 = var4.b();
               var6.i(hz.Q(), var7.yRot(), var7.xRot(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.R(var1.last().pose(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.order(2).submitCustomGeometry(new PoseStack(), oj.x.P(var4.a.w("Glow")), (var1x, var2x) -> n(var16, var1x, var2x));
               he.z("Flame hand particles", var16.length / 6);
            } catch (RuntimeException var12) {
               for (oj.c var11 : R) {
                  var11.g();
               }

               K = (boolean)1;
               _q.o("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var12);
            }
         }
      }
   }

   private static void n(double[] var0, Pose var1, VertexConsumer var2) {
      for (byte var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos(var8) * var7;
         float var10 = (float)Math.sin(var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         o(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         o(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         o(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         o(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void o(VertexConsumer var0, Pose var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.addVertex(var1, var2, var3, var4).setColor(var5).setUv(var6, var7);
   }

   public static float v(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int M(double var0, oj.q var2) {
      return switch (var2.colorMode) {
         case "Gradient" -> S(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int S(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (byte var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((var0 >>> var5 & 0xFF) * (1.0F - var3) + (var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private oj() {
   }

   public static final class c {
      private final RandomGenerator y;
      private final ArrayList<oj.e> l = new ArrayList<>();
      private double L = Double.NaN;
      private double X;
      private float d;
      private float Y;

      public c(RandomGenerator var1) {
         this.y = var1;
      }

      public int d() {
         return this.l.size();
      }

      public void g() {
         this.l.clear();
         this.L = Double.NaN;
         this.X = 0.0;
      }

      public void i(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, oj.q var9) {
         if (!Double.isFinite(var1)) {
            this.g();
         } else if (Double.isFinite(this.L) && !(var1 < this.L) && !(var1 - this.L > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.L, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.L = var1;
               float var11 = oj.v(var3 - this.d);
               float var12 = var4 - this.Y;
               this.d = var3;
               this.Y = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.l.size(); var20++) {
                  oj.e var21 = this.l.get(var20);
                  if (var1 - var21.O >= var21.N) {
                     this.l.remove(var20--);
                  } else {
                     var21.G = var21.G + var21.u * var17;
                     var21.z = var21.z + var21.o * var17;
                     var21.E = var21.E + var21.W * var17;
                     var21.u = (var21.u + (var14 + this.W(-var19, var19)) * var17) * var18;
                     var21.o = (var21.o + (var15 + this.W(-var19, var19) * 0.5F) * var17) * var18;
                     var21.W = (var21.W + (var16 + this.W(-var19, var19)) * var17) * var18;
                     var21.P = var21.P + var21.i * var10;
                  }
               }

               this.X += var10;
               double var26 = 1.0 / var9.density;

               while (this.X + 1.0E-8 >= var26) {
                  this.X = Math.max(0.0, this.X - var26);
                  float var22 = this.y.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  oj.e var25 = new oj.e();
                  var25.G = this.W(-var24, var24);
                  var25.z = var22 * var23 + this.W(-0.015F, 0.015F);
                  var25.E = -var22 * var23 * 0.35F + this.W(-0.015F, 0.015F);
                  var25.u = this.W(-0.001F, 0.001F);
                  var25.o = this.W(0.003F, 0.008F);
                  var25.W = this.W(-0.001F, 0.001F);
                  var25.l = var9.size * this.W(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.N = Math.max(0.1F, var9.lifetime * this.W(0.8F, 1.2F));
                  var25.O = var1;
                  var25.I = oj.M(var1, var9);
                  var25.P = this.W(0.0F, 360.0F);
                  var25.i = this.W(-90.0F, 90.0F);
                  if (this.l.size() >= 768) {
                     this.l.removeFirst();
                  }

                  this.l.add(var25);
               }
            }
         } else {
            this.g();
            this.L = var1;
            this.d = var3;
            this.Y = var4;
         }
      }

      private float W(float var1, float var2) {
         return var1 + (var2 - var1) * this.y.nextFloat();
      }

      public double[] R(Matrix4f var1, oj.q var2) {
         double[] var3 = new double[this.l.size() * 6];
         int var4 = 0;

         for (oj.e var6 : this.l) {
            Vector3f var7 = new Vector3f(var6.G, var6.z, var6.E);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.L - var6.O) / var6.N, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? oj.S(var6.I, var2.secondary, var8) : var6.I;
            int var10 = Math.round((var9 >>> 24) * (1.0F - var8));
            var3[var4++] = var7.x;
            var3[var4++] = var7.y;
            var3[var4++] = var7.z;
            var3[var4++] = var6.l;
            var3[var4++] = (float)Math.toRadians(var6.P);
            var3[var4++] = var9 & 16777215 | var10 << 24;
         }

         return var3;
      }
   }

   private static final class e {
      float G;
      float z;
      float E;
      float u;
      float o;
      float W;
      float l;
      float N;
      float P;
      float i;
      double O;
      int I;
   }

   public record q(float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode) {
   }

   private static final class x {
      private static final RenderPipeline a = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "flame_hands"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      private static int U = -1;
      private static final RenderType I = Q("bloom", oj.p);
      private static final RenderType x = Q("glow", oj.i);

      private static RenderType Q(String var0, Identifier var1) {
         return RenderType.create("larp_flame_hand_" + var0, RenderSetup.builder(a).withTexture("Sampler0", var1).createRenderSetup());
      }

      private static RenderType P(boolean var0) {
         return var0 ? x : I;
      }

      private static void K() {
         if (U != nl.q) {
            se.j(a);
            U = nl.q;
            _q.c("FlameHands", "Reference additive sprite material validated for resource generation {}", U);
         }
      }
   }
}

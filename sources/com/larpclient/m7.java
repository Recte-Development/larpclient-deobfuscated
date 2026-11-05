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

public final class m7 {
   public static final int s = 768;
   private static final Identifier I = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png");
   private static final Identifier W = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame_glow.png");
   private static final m7.s[] p = new m7.s[]{new m7.s(new Random()), new m7.s(new Random())};
   private static Object C;
   private static Object P;
   private static int S = -1;
   private static boolean f;
   private static Vec3 N;

   public static void V() {
      for (m7.s var3 : p) {
         var3.J();
      }

      P = null;
      C = null;
      N = null;
      f = (boolean)0;
   }

   public static void g() {
      Minecraft var0 = Minecraft.getInstance();
      if (C != var0.level || P != var0.player || S != jk.v) {
         V();
         C = var0.level;
         P = var0.player;
         S = jk.v;
      }

      if (var0.player != null) {
         Vec3 var1 = var0.player.position();
         if (N != null && N.distanceToSqr(var1) > 64.0) {
            for (m7.s var5 : p) {
               var5.J();
            }

            r4.g("FlameHands", "Cleared histories after player relocation");
         }

         N = var1;
      }

      if (var0.player == null || var0.level == null || !var0.options.getCameraType().isFirstPerson()) {
         for (m7.s var9 : p) {
            var9.J();
         }
      }
   }

   public static void d(HumanoidArm var0, PoseStack var1, SubmitNodeCollector var2, boolean var3) {
      FlameHandsModule var4 = FlameHandsModule.Z();
      Minecraft var5 = Minecraft.getInstance();
      if (var4 != null && var4.isRuntimeActive() && var5.player != null && var5.level != null && var5.options.getCameraType().isFirstPerson()) {
         g();
         m7.s var6 = p[var0 == HumanoidArm.RIGHT ? 0 : 1];
         if (!var4.R(var0 == var5.player.getMainArm(), var3)) {
            var6.J();
         } else if (!f) {
            try {
               m7.k.W();
               Camera var7 = var5.gameRenderer.mainCamera();
               Vec3 var13 = var5.player.getDeltaMovement();
               Vector3f var14 = new Vector3f((float)var13.x, (float)var13.y, (float)var13.z).rotate(new Quaternionf(var7.rotation()).conjugate());
               m7.n var15 = var4.J();
               var6.g(ea.y(), var7.yRot(), var7.xRot(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.Y(var1.last().pose(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.order(2).submitCustomGeometry(new PoseStack(), m7.k.D(var4.z.i("Glow")), (var1x, var2x) -> U(var16, var1x, var2x));
               h_.S("Flame hand particles", var16.length / 6);
            } catch (RuntimeException var12) {
               for (m7.s var11 : p) {
                  var11.J();
               }

               f = (boolean)1;
               r4.r("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var12);
            }
         }
      }
   }

   private static void U(double[] var0, Pose var1, VertexConsumer var2) {
      for (byte var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos(var8) * var7;
         float var10 = (float)Math.sin(var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         W(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         W(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         W(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         W(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void W(VertexConsumer var0, Pose var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.addVertex(var1, var2, var3, var4).setColor(var5).setUv(var6, var7);
   }

   public static float v(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int i(double var0, m7.n var2) {
      return switch (var2.colorMode) {
         case "Gradient" -> k(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int k(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (byte var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((var0 >>> var5 & 0xFF) * (1.0F - var3) + (var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private m7() {
   }

   private static final class k {
      private static final RenderPipeline R = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "flame_hands"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      private static int E = -1;
      private static final RenderType X = t("bloom", m7.I);
      private static final RenderType Y = t("glow", m7.W);

      private static RenderType t(String var0, Identifier var1) {
         return RenderType.create("larp_flame_hand_" + var0, RenderSetup.builder(R).withTexture("Sampler0", var1).createRenderSetup());
      }

      private static RenderType D(boolean var0) {
         return var0 ? Y : X;
      }

      private static void W() {
         if (E != jk.v) {
            e4.U(R);
            E = jk.v;
            r4.g("FlameHands", "Reference additive sprite material validated for resource generation {}", E);
         }
      }
   }

   public record n(float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode) {
   }

   private static final class q {
      float X;
      float D;
      float r;
      float w;
      float T;
      float K;
      float M;
      float b;
      float A;
      float m;
      double N;
      int i;
   }

   public static final class s {
      private final RandomGenerator W;
      private final ArrayList<m7.q> o = new ArrayList<>();
      private double b = Double.NaN;
      private double q;
      private float a;
      private float H;

      public s(RandomGenerator var1) {
         this.W = var1;
      }

      public int Z() {
         return this.o.size();
      }

      public void J() {
         this.o.clear();
         this.b = Double.NaN;
         this.q = 0.0;
      }

      public void g(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, m7.n var9) {
         if (!Double.isFinite(var1)) {
            this.J();
         } else if (Double.isFinite(this.b) && !(var1 < this.b) && !(var1 - this.b > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.b, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.b = var1;
               float var11 = m7.v(var3 - this.a);
               float var12 = var4 - this.H;
               this.a = var3;
               this.H = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.o.size(); var20++) {
                  m7.q var21 = this.o.get(var20);
                  if (var1 - var21.N >= var21.b) {
                     this.o.remove(var20--);
                  } else {
                     var21.X = var21.X + var21.w * var17;
                     var21.D = var21.D + var21.T * var17;
                     var21.r = var21.r + var21.K * var17;
                     var21.w = (var21.w + (var14 + this.f(-var19, var19)) * var17) * var18;
                     var21.T = (var21.T + (var15 + this.f(-var19, var19) * 0.5F) * var17) * var18;
                     var21.K = (var21.K + (var16 + this.f(-var19, var19)) * var17) * var18;
                     var21.A = var21.A + var21.m * var10;
                  }
               }

               this.q += var10;
               double var26 = 1.0 / var9.density;

               while (this.q + 1.0E-8 >= var26) {
                  this.q = Math.max(0.0, this.q - var26);
                  float var22 = this.W.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  m7.q var25 = new m7.q();
                  var25.X = this.f(-var24, var24);
                  var25.D = var22 * var23 + this.f(-0.015F, 0.015F);
                  var25.r = -var22 * var23 * 0.35F + this.f(-0.015F, 0.015F);
                  var25.w = this.f(-0.001F, 0.001F);
                  var25.T = this.f(0.003F, 0.008F);
                  var25.K = this.f(-0.001F, 0.001F);
                  var25.M = var9.size * this.f(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.b = Math.max(0.1F, var9.lifetime * this.f(0.8F, 1.2F));
                  var25.N = var1;
                  var25.i = m7.i(var1, var9);
                  var25.A = this.f(0.0F, 360.0F);
                  var25.m = this.f(-90.0F, 90.0F);
                  if (this.o.size() >= 768) {
                     this.o.removeFirst();
                  }

                  this.o.add(var25);
               }
            }
         } else {
            this.J();
            this.b = var1;
            this.a = var3;
            this.H = var4;
         }
      }

      private float f(float var1, float var2) {
         return var1 + (var2 - var1) * this.W.nextFloat();
      }

      public double[] Y(Matrix4f var1, m7.n var2) {
         double[] var3 = new double[this.o.size() * 6];
         int var4 = 0;

         for (m7.q var6 : this.o) {
            Vector3f var7 = new Vector3f(var6.X, var6.D, var6.r);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.b - var6.N) / var6.b, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? m7.k(var6.i, var2.secondary, var8) : var6.i;
            int var10 = Math.round((var9 >>> 24) * (1.0F - var8));
            var3[var4++] = var7.x;
            var3[var4++] = var7.y;
            var3[var4++] = var7.z;
            var3[var4++] = var6.M;
            var3[var4++] = (float)Math.toRadians(var6.A);
            var3[var4++] = var9 & 16777215 | var10 << 24;
         }

         return var3;
      }
   }
}

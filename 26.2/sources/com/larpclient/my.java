package com.larpclient;

import com.larpclient.module.FlameHandsModule;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
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

public final class my {
   public static final int l = 768;
   private static final Identifier R = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png");
   private static final Identifier G = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame_glow.png");
   private static final my.s[] z = new my.s[]{new my.s(new Random()), new my.s(new Random())};
   private static Object O;
   private static Object i;
   private static int P = -1;
   private static boolean K;
   private static Vec3 Q;

   public static void K() {
      for (my.s var3 : z) {
         var3.u();
      }

      i = null;
      O = null;
      Q = null;
      K = (boolean)0;
   }

   public static void A() {
      Minecraft var0 = Minecraft.getInstance();
      if (O != var0.level || i != var0.player || P != wa.s) {
         K();
         O = var0.level;
         i = var0.player;
         P = wa.s;
      }

      if (var0.player != null) {
         Vec3 var1 = var0.player.position();
         if (Q != null && Q.distanceToSqr(var1) > 64.0) {
            for (my.s var5 : z) {
               var5.u();
            }

            com.larpclient.h.U("FlameHands", "Cleared histories after player relocation");
         }

         Q = var1;
      }

      if (var0.player == null || var0.level == null || !var0.options.getCameraType().isFirstPerson()) {
         for (my.s var9 : z) {
            var9.u();
         }
      }
   }

   public static void M(HumanoidArm var0, PoseStack var1, SubmitNodeCollector var2, boolean var3) {
      FlameHandsModule var4 = FlameHandsModule.U();
      Minecraft var5 = Minecraft.getInstance();
      if (var4 != null && var4.isRuntimeActive() && var5.player != null && var5.level != null && var5.options.getCameraType().isFirstPerson()) {
         A();
         my.s var6 = z[var0 == HumanoidArm.RIGHT ? 0 : 1];
         if (!var4.g(var0 == var5.player.getMainArm(), var3)) {
            var6.u();
         } else if (!K) {
            try {
               my.h.L();
               Camera var7 = var5.gameRenderer.mainCamera();
               Vec3 var13 = var5.player.getDeltaMovement();
               Vector3f var14 = new Vector3f((float)var13.x, (float)var13.y, (float)var13.z).rotate(new Quaternionf(var7.rotation()).conjugate());
               my.j var15 = var4.M();
               var6.N(cf.F(), var7.yRot(), var7.xRot(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.U(var1.last().pose(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.order(2).submitCustomGeometry(new PoseStack(), my.h.w(var4.P.O("Glow")), (var1x, var2x) -> y(var16, var1x, var2x));
               zj.s("Flame hand particles", var16.length / 6);
            } catch (RuntimeException var12) {
               for (my.s var11 : z) {
                  var11.u();
               }

               K = (boolean)1;
               com.larpclient.h.m("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var12);
            }
         }
      }
   }

   private static void y(double[] var0, Pose var1, VertexConsumer var2) {
      for (byte var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos(var8) * var7;
         float var10 = (float)Math.sin(var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         e(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         e(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         e(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         e(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void e(VertexConsumer var0, Pose var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.addVertex(var1, var2, var3, var4).setColor(var5).setUv(var6, var7);
   }

   public static float S(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int N(double var0, my.j var2) {
      return switch (var2.colorMode) {
         case "Gradient" -> l(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int l(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (byte var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((var0 >>> var5 & 0xFF) * (1.0F - var3) + (var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private my() {
   }

   private static final class b {
      float t;
      float s;
      float D;
      float E;
      float J;
      float m;
      float T;
      float F;
      float A;
      float H;
      double Z;
      int y;
   }

   private static final class h {
      private static final RenderPipeline S = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "flame_hands"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      private static int q = -1;
      private static final RenderType f = B("bloom", my.R);
      private static final RenderType V = B("glow", my.G);

      private static RenderType B(String var0, Identifier var1) {
         return RenderType.create("larp_flame_hand_" + var0, RenderSetup.builder(S).withTexture("Sampler0", var1).createRenderSetup());
      }

      private static RenderType w(boolean var0) {
         return var0 ? V : f;
      }

      private static void L() {
         if (q != wa.s) {
            ai.U(S);
            q = wa.s;
            com.larpclient.h.U("FlameHands", "Reference additive sprite material validated for resource generation {}", q);
         }
      }
   }

   public record j(float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode) {
   }

   public static final class s {
      private final RandomGenerator D;
      private final ArrayList<my.b> W = new ArrayList<>();
      private double Y = Double.NaN;
      private double w;
      private float M;
      private float X;

      public s(RandomGenerator var1) {
         this.D = var1;
      }

      public int T() {
         return this.W.size();
      }

      public void u() {
         this.W.clear();
         this.Y = Double.NaN;
         this.w = 0.0;
      }

      public void N(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, my.j var9) {
         if (!Double.isFinite(var1)) {
            this.u();
         } else if (Double.isFinite(this.Y) && !(var1 < this.Y) && !(var1 - this.Y > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.Y, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.Y = var1;
               float var11 = my.S(var3 - this.M);
               float var12 = var4 - this.X;
               this.M = var3;
               this.X = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.W.size(); var20++) {
                  my.b var21 = this.W.get(var20);
                  if (var1 - var21.Z >= var21.F) {
                     this.W.remove(var20--);
                  } else {
                     var21.t = var21.t + var21.E * var17;
                     var21.s = var21.s + var21.J * var17;
                     var21.D = var21.D + var21.m * var17;
                     var21.E = (var21.E + (var14 + this.P(-var19, var19)) * var17) * var18;
                     var21.J = (var21.J + (var15 + this.P(-var19, var19) * 0.5F) * var17) * var18;
                     var21.m = (var21.m + (var16 + this.P(-var19, var19)) * var17) * var18;
                     var21.A = var21.A + var21.H * var10;
                  }
               }

               this.w += var10;
               double var26 = 1.0 / var9.density;

               while (this.w + 1.0E-8 >= var26) {
                  this.w = Math.max(0.0, this.w - var26);
                  float var22 = this.D.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  my.b var25 = new my.b();
                  var25.t = this.P(-var24, var24);
                  var25.s = var22 * var23 + this.P(-0.015F, 0.015F);
                  var25.D = -var22 * var23 * 0.35F + this.P(-0.015F, 0.015F);
                  var25.E = this.P(-0.001F, 0.001F);
                  var25.J = this.P(0.003F, 0.008F);
                  var25.m = this.P(-0.001F, 0.001F);
                  var25.T = var9.size * this.P(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.F = Math.max(0.1F, var9.lifetime * this.P(0.8F, 1.2F));
                  var25.Z = var1;
                  var25.y = my.N(var1, var9);
                  var25.A = this.P(0.0F, 360.0F);
                  var25.H = this.P(-90.0F, 90.0F);
                  if (this.W.size() >= 768) {
                     this.W.removeFirst();
                  }

                  this.W.add(var25);
               }
            }
         } else {
            this.u();
            this.Y = var1;
            this.M = var3;
            this.X = var4;
         }
      }

      private float P(float var1, float var2) {
         return var1 + (var2 - var1) * this.D.nextFloat();
      }

      public double[] U(Matrix4f var1, my.j var2) {
         double[] var3 = new double[this.W.size() * 6];
         int var4 = 0;

         for (my.b var6 : this.W) {
            Vector3f var7 = new Vector3f(var6.t, var6.s, var6.D);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.Y - var6.Z) / var6.F, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? my.l(var6.y, var2.secondary, var8) : var6.y;
            int var10 = Math.round((var9 >>> 24) * (1.0F - var8));
            var3[var4++] = var7.x;
            var3[var4++] = var7.y;
            var3[var4++] = var7.z;
            var3[var4++] = var6.T;
            var3[var4++] = (float)Math.toRadians(var6.A);
            var3[var4++] = var9 & 16777215 | var10 << 24;
         }

         return var3;
      }
   }
}

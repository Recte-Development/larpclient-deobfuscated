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

public final class ma {
   public static final int I = 768;
   private static final Identifier c = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png");
   private static final Identifier w = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame_glow.png");
   private static final ma.w[] i = new ma.w[]{new ma.w(new Random()), new ma.w(new Random())};
   private static Object Y;
   private static Object Z;
   private static int n = -1;
   private static boolean j;
   private static Vec3 o;

   public static void B() {
      for (ma.w var3 : i) {
         var3.I();
      }

      Z = null;
      Y = null;
      o = null;
      j = (boolean)0;
   }

   public static void a() {
      Minecraft var0 = Minecraft.getInstance();
      if (Y != var0.level || Z != var0.player || n != m5.k) {
         B();
         Y = var0.level;
         Z = var0.player;
         n = m5.k;
      }

      if (var0.player != null) {
         Vec3 var1 = var0.player.position();
         if (o != null && o.distanceToSqr(var1) > 64.0) {
            for (ma.w var5 : i) {
               var5.I();
            }

            mt.U("FlameHands", "Cleared histories after player relocation");
         }

         o = var1;
      }

      if (var0.player == null || var0.level == null || !var0.options.getCameraType().isFirstPerson()) {
         for (ma.w var9 : i) {
            var9.I();
         }
      }
   }

   public static void U(HumanoidArm var0, PoseStack var1, SubmitNodeCollector var2, boolean var3) {
      FlameHandsModule var4 = FlameHandsModule.R();
      Minecraft var5 = Minecraft.getInstance();
      if (var4 != null && var4.isRuntimeActive() && var5.player != null && var5.level != null && var5.options.getCameraType().isFirstPerson()) {
         a();
         ma.w var6 = i[var0 == HumanoidArm.RIGHT ? 0 : 1];
         if (!var4.j(var0 == var5.player.getMainArm(), var3)) {
            var6.I();
         } else if (!j) {
            try {
               ma.s.W();
               Camera var7 = var5.gameRenderer.mainCamera();
               Vec3 var13 = var5.player.getDeltaMovement();
               Vector3f var14 = new Vector3f((float)var13.x, (float)var13.y, (float)var13.z).rotate(new Quaternionf(var7.rotation()).conjugate());
               ma.d var15 = var4.e();
               var6.p(ty.K(), var7.yRot(), var7.xRot(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.i(var1.last().pose(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.order(2).submitCustomGeometry(new PoseStack(), ma.s.K(var4.Z.S("Glow")), (var1x, var2x) -> b(var16, var1x, var2x));
               kx.s("Flame hand particles", var16.length / 6);
            } catch (RuntimeException var12) {
               for (ma.w var11 : i) {
                  var11.I();
               }

               j = (boolean)1;
               mt.P("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var12);
            }
         }
      }
   }

   private static void b(double[] var0, Pose var1, VertexConsumer var2) {
      for (byte var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos(var8) * var7;
         float var10 = (float)Math.sin(var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         p(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         p(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         p(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         p(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void p(VertexConsumer var0, Pose var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.addVertex(var1, var2, var3, var4).setColor(var5).setUv(var6, var7);
   }

   public static float P(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int X(double var0, ma.d var2) {
      return switch (var2.colorMode) {
         case "Gradient" -> B(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int B(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (byte var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((var0 >>> var5 & 0xFF) * (1.0F - var3) + (var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private ma() {
   }

   private static final class a {
      float J;
      float p;
      float X;
      float j;
      float x;
      float Y;
      float H;
      float s;
      float w;
      float T;
      double F;
      int z;
   }

   public record d(float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode) {
   }

   private static final class s {
      private static final RenderPipeline P = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "flame_hands"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      private static int S = -1;
      private static final RenderType s = G("bloom", ma.c);
      private static final RenderType r = G("glow", ma.w);

      private static RenderType G(String var0, Identifier var1) {
         return RenderType.create("larp_flame_hand_" + var0, RenderSetup.builder(P).withTexture("Sampler0", var1).createRenderSetup());
      }

      private static RenderType K(boolean var0) {
         return var0 ? r : s;
      }

      private static void W() {
         if (S != m5.k) {
            ea.z(P);
            S = m5.k;
            mt.U("FlameHands", "Reference additive sprite material validated for resource generation {}", S);
         }
      }
   }

   public static final class w {
      private final RandomGenerator j;
      private final ArrayList<ma.a> C = new ArrayList<>();
      private double S = Double.NaN;
      private double a;
      private float H;
      private float K;

      public w(RandomGenerator var1) {
         this.j = var1;
      }

      public int Z() {
         return this.C.size();
      }

      public void I() {
         this.C.clear();
         this.S = Double.NaN;
         this.a = 0.0;
      }

      public void p(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, ma.d var9) {
         if (!Double.isFinite(var1)) {
            this.I();
         } else if (Double.isFinite(this.S) && !(var1 < this.S) && !(var1 - this.S > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.S, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.S = var1;
               float var11 = ma.P(var3 - this.H);
               float var12 = var4 - this.K;
               this.H = var3;
               this.K = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.C.size(); var20++) {
                  ma.a var21 = this.C.get(var20);
                  if (var1 - var21.F >= var21.s) {
                     this.C.remove(var20--);
                  } else {
                     var21.J = var21.J + var21.j * var17;
                     var21.p = var21.p + var21.x * var17;
                     var21.X = var21.X + var21.Y * var17;
                     var21.j = (var21.j + (var14 + this.G(-var19, var19)) * var17) * var18;
                     var21.x = (var21.x + (var15 + this.G(-var19, var19) * 0.5F) * var17) * var18;
                     var21.Y = (var21.Y + (var16 + this.G(-var19, var19)) * var17) * var18;
                     var21.w = var21.w + var21.T * var10;
                  }
               }

               this.a += var10;
               double var26 = 1.0 / var9.density;

               while (this.a + 1.0E-8 >= var26) {
                  this.a = Math.max(0.0, this.a - var26);
                  float var22 = this.j.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  ma.a var25 = new ma.a();
                  var25.J = this.G(-var24, var24);
                  var25.p = var22 * var23 + this.G(-0.015F, 0.015F);
                  var25.X = -var22 * var23 * 0.35F + this.G(-0.015F, 0.015F);
                  var25.j = this.G(-0.001F, 0.001F);
                  var25.x = this.G(0.003F, 0.008F);
                  var25.Y = this.G(-0.001F, 0.001F);
                  var25.H = var9.size * this.G(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.s = Math.max(0.1F, var9.lifetime * this.G(0.8F, 1.2F));
                  var25.F = var1;
                  var25.z = ma.X(var1, var9);
                  var25.w = this.G(0.0F, 360.0F);
                  var25.T = this.G(-90.0F, 90.0F);
                  if (this.C.size() >= 768) {
                     this.C.removeFirst();
                  }

                  this.C.add(var25);
               }
            }
         } else {
            this.I();
            this.S = var1;
            this.H = var3;
            this.K = var4;
         }
      }

      private float G(float var1, float var2) {
         return var1 + (var2 - var1) * this.j.nextFloat();
      }

      public double[] i(Matrix4f var1, ma.d var2) {
         double[] var3 = new double[this.C.size() * 6];
         int var4 = 0;

         for (ma.a var6 : this.C) {
            Vector3f var7 = new Vector3f(var6.J, var6.p, var6.X);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.S - var6.F) / var6.s, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? ma.B(var6.z, var2.secondary, var8) : var6.z;
            int var10 = Math.round((var9 >>> 24) * (1.0F - var8));
            var3[var4++] = var7.x;
            var3[var4++] = var7.y;
            var3[var4++] = var7.z;
            var3[var4++] = var6.H;
            var3[var4++] = (float)Math.toRadians(var6.w);
            var3[var4++] = var9 & 16777215 | var10 << 24;
         }

         return var3;
      }
   }
}

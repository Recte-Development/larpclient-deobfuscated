package com.larpclient;

import com.larpclient.module.EchoHandModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class pm {
   private static final RenderPipeline t = z("echo_hand", false);
   private static final RenderPipeline V = z("echo_hand", true);
   private static final RenderPipeline C = z("hand_flame", false);
   private static final RenderPipeline S = z("hand_flame", true);
   private static TextureTarget K;
   private static TextureTarget A;
   private static TextureTarget s;
   private static TextureTarget i;
   private static GpuBuffer W;
   private static int u = -1;
   private static boolean k;
   private static boolean H;
   private static boolean m;
   private static long Z;
   private static Object T;
   private static Object M;
   private static String j;
   private static double Y;
   private static double N;
   private static double r;
   private static float e = 0.016666668F;
   private static final pm.p c = new pm.p();

   private static RenderPipeline z(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder();

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withUniform("Sampler" + var3, p7.X);
      }

      var2.withUniform("EchoParameters", UniformType.UNIFORM_BUFFER);
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(V("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(V("core/echo_hand"))
         .withFragmentShader(V("core/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(w(var0)) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static BlendFunction w(String var0) {
      return var0.equals("hand_flame") ? BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA : BlendFunction.LIGHTNING;
   }

   public static boolean G() {
      Minecraft var0 = Minecraft.getInstance();
      EchoHandModule var1 = EchoHandModule.e();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator()
         && (var1.S() || var1.w.getValue())
         && (!var1.S() || !var1.N.getValue() || !var0.player.getMainHandItem().isEmpty() || !var0.player.getOffhandItem().isEmpty());
   }

   public static void j() {
      if (!G()) {
         C();
      } else if (T != null && (T != Minecraft.getInstance().level || M != Minecraft.getInstance().player)) {
         C();
      }
   }

   public static boolean y() {
      if (u != nl.q) {
         C();
         u = nl.q;
         k = (boolean)0;
      }

      if (!G()) {
         C();
         return false;
      }

      if (k) {
         return false;
      }

      try {
         Minecraft var0 = Minecraft.getInstance();
         EchoHandModule var1 = EchoHandModule.e();
         RenderTarget var2 = var0.gameRenderer.mainRenderTarget();
         if (jf.b == null && jf.q == null) {
            int var3 = K != null
                  && K.width == var2.width
                  && K.height == var2.height
                  && T == var0.level
                  && M == var0.player
                  && var1.r.getValue().equals(j)
                  && !(w(var0.player.getX(), var0.player.getY(), var0.player.getZ(), Y, N, r) > 256.0)
               ? 0
               : 1;
            if (var3 != 0) {
               C();
               R(var2.width, var2.height);
               T = var0.level;
               M = var0.player;
               j = var1.r.getValue();
            }

            Y = var0.player.getX();
            N = var0.player.getY();
            r = var0.player.getZ();
            if (var0.isPaused() != m) {
               m = (boolean)(!m ? 1 : 0);
               _q.c("EchoHand", "Game {}; history keeps settling", m ? "paused" : "resumed");
            }

            float var4 = var0.player.getYRot();
            float var5 = var0.player.getXRot();
            float var6 = ij.G(var4);
            if (!Float.isNaN(var6)) {
               var4 = var6;
               var5 = ij.M();
            }

            c.T(var4, var5, var1.S() ? 1.0F : var1.T.getValue().floatValue());
            nl.G(se.j, var2, K);
            K.copyDepthFrom(var2);
            H = (boolean)1;
            return true;
         } else {
            C();
            return false;
         }
      } catch (RuntimeException var7) {
         k("capture", var7);
         return false;
      }
   }

   public static void I() {
      if (H) {
         H = (boolean)0;

         try {
            EchoHandModule var0 = EchoHandModule.e();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            nl.G(se.j, var1, A);
            A.copyDepthFrom(var1);
            long var2 = System.nanoTime();
            e = Z == 0L ? 0.016666668F : (float)Math.clamp((var2 - Z) / 1.0E9, 0.0, 0.1);
            Z = var2;
            a(var0.S() ? C : t, i, var0, 0.0F);
            TextureTarget var4 = s;
            s = i;
            i = var4;
            a(var0.S() ? S : V, var1, var0, 1.0F);
         } catch (RuntimeException var5) {
            k("history/composite", var5);
         }
      }
   }

   public static void X() {
      H = (boolean)0;
      C();
   }

   private static void R(int var0, int var1) {
      try {
         se.j(t);
         se.j(V);
         se.j(C);
         se.j(S);
         K = p7.m("Larp EchoHand before", var0, var1, true, GpuFormat.RGBA8_UNORM);
         A = p7.m("Larp EchoHand after", var0, var1, true, GpuFormat.RGBA8_UNORM);
         s = p7.m("Larp EchoHand history", var0, var1, false, GpuFormat.RGBA8_UNORM);
         i = p7.m("Larp EchoHand next", var0, var1, false, GpuFormat.RGBA8_UNORM);
         W = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(s.getColorTexture(), new Vector4f());
         var2.clearColorTexture(i.getColorTexture(), new Vector4f());
         _q.z("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var3) {
         C();
         throw var3;
      }
   }

   private static void a(RenderPipeline var0, RenderTarget var1, EchoHandModule var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat(K.width).putFloat(K.height).putFloat((var2.S() ? var2.K : var2.M).getValue().floatValue()).putFloat(var2.S.getValue().floatValue());
         var6.putFloat(var2.c.getValue().floatValue()).putFloat(var2.T.getValue().floatValue()).putFloat(var3).putFloat((float)hz.Q());
         var6.putFloat(c.J).putFloat(c.L).putFloat(var2.R.getValue() ? 1.0F : 0.0F).putFloat(e);
         int var7 = var2.B();
         var6.putFloat((var7 >> 16 & 0xFF) / 255.0F).putFloat((var7 >> 8 & 0xFF) / 255.0F).putFloat((var7 & 0xFF) / 255.0F).putFloat((var7 >>> 24) / 255.0F);
         var6.putFloat(var2.O.getValue().floatValue())
            .putFloat(var2.a.getValue().floatValue())
            .putFloat(var2.F.getValue().floatValue())
            .putFloat(var2.G.getValue().floatValue());
         var6.putFloat(var2.s.getValue().floatValue()).putFloat(var2.j.getValue().floatValue()).putFloat(var2.Z.getValue().floatValue()).putFloat(0.0F).flip();
         var4.writeToBuffer(W.slice(), var6);
      } catch (Throwable var11) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var9) {
               var11.addSuppressed(var9);
            }
         }

         throw var11;
      }

      if (var5 != null) {
         var5.close();
      }

      RenderPass var12 = var4.createRenderPass(() -> "Larp EchoHand history/composite", var1.getColorTextureView(), Optional.empty());

      try {
         p7.K(var12, var0);
         var12.setUniform("EchoParameters", W.slice());
         GpuSampler var13 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         p7.t(var12, "Sampler0", A.getColorTextureView(), var13);
         p7.t(var12, "Sampler1", K.getColorTextureView(), var13);
         p7.t(var12, "Sampler2", s.getColorTextureView(), var13);
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         p7.t(var12, "Sampler3", A.getDepthTextureView(), var14);
         p7.t(var12, "Sampler4", K.getDepthTextureView(), var14);
         var12.draw(3, 1, 0, 0);
      } catch (Throwable var10) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }
         }

         throw var10;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   private static void k(String var0, RuntimeException var1) {
      k = (boolean)1;
      C();
      _q.o("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void C() {
      TextureTarget[] var0 = new TextureTarget[]{K, A, s, i};
      i = null;
      s = null;
      A = null;
      K = null;
      GpuBuffer var1 = W;
      W = null;
      H = (boolean)0;
      M = null;
      T = null;
      j = null;
      c.u();
      Z = 0L;
      RuntimeException var2 = null;

      for (TextureTarget var6 : var0) {
         if (var6 != null) {
            try {
               var6.destroyBuffers();
            } catch (RuntimeException var9) {
               if (var2 == null) {
                  var2 = var9;
               } else {
                  var2.addSuppressed(var9);
               }
            }
         }
      }

      if (var1 != null) {
         try {
            var1.close();
         } catch (RuntimeException var8) {
            if (var2 == null) {
               var2 = var8;
            } else {
               var2.addSuppressed(var8);
            }
         }
      }

      if (var2 != null) {
         _q.o("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double w(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static Identifier V(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static final class p {
      public float J;
      public float L;
      private float O;
      private float m;
      private float D;
      private float g;
      private boolean H;

      public void T(float var1, float var2, float var3) {
         if (!this.H) {
            this.O = var1;
            this.m = var2;
            this.H = (boolean)1;
         } else {
            float var4 = Mth.wrapDegrees(var1 - this.O);
            float var5 = var2 - this.m;
            this.O = var1;
            this.m = var2;
            float var6 = Mth.clamp(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = Mth.clamp(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.D = (this.D + (var6 - this.J) * 0.18F) * 0.74F;
            this.g = (this.g + (var7 - this.L) * 0.18F) * 0.74F;
            this.J = Mth.clamp((this.J + this.D) * 0.92F, -0.075F, 0.075F);
            this.L = Mth.clamp((this.L + this.g) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void u() {
         this.H = (boolean)0;
         this.J = this.L = this.O = this.m = this.D = this.g = 0.0F;
      }
   }
}

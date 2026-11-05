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

public final class m1 {
   private static final RenderPipeline L = m("echo_hand", false);
   private static final RenderPipeline P = m("echo_hand", true);
   private static final RenderPipeline u = m("hand_flame", false);
   private static final RenderPipeline x = m("hand_flame", true);
   private static TextureTarget W;
   private static TextureTarget h;
   private static TextureTarget J;
   private static TextureTarget Y;
   private static GpuBuffer g;
   private static int N = -1;
   private static boolean o;
   private static boolean a;
   private static Object v;
   private static Object e;
   private static String M;
   private static double i;
   private static double I;
   private static double d;
   private static final m1.o D = new m1.o();

   private static RenderPipeline m(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder();

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withUniform("Sampler" + var3, hj.G);
      }

      var2.withUniform("EchoParameters", UniformType.UNIFORM_BUFFER);
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(x("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(x("core/echo_hand"))
         .withFragmentShader(x("core/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.LIGHTNING) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   public static boolean b() {
      Minecraft var0 = Minecraft.getInstance();
      EchoHandModule var1 = EchoHandModule.v();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator()
         && (var1.d() || var1.v.P())
         && (!var1.d() || !var1.T.P() || !var0.player.getMainHandItem().isEmpty() || !var0.player.getOffhandItem().isEmpty());
   }

   public static void M() {
      if (!b()) {
         f();
      } else if (v != null && (v != Minecraft.getInstance().level || e != Minecraft.getInstance().player)) {
         f();
      }
   }

   public static boolean x() {
      if (N != m5.k) {
         f();
         N = m5.k;
         o = (boolean)0;
      }

      if (!b()) {
         f();
         return false;
      }

      if (o) {
         return false;
      }

      try {
         Minecraft var0 = Minecraft.getInstance();
         EchoHandModule var1 = EchoHandModule.v();
         RenderTarget var2 = var0.gameRenderer.mainRenderTarget();
         if (xn.j == null && xn.o == null) {
            int var3 = W != null
                  && W.width == var2.width
                  && W.height == var2.height
                  && v == var0.level
                  && e == var0.player
                  && var1.N.P().equals(M)
                  && !(G(var0.player.getX(), var0.player.getY(), var0.player.getZ(), i, I, d) > 256.0)
               ? 0
               : 1;
            if (var3 != 0) {
               f();
               Q(var2.width, var2.height);
               v = var0.level;
               e = var0.player;
               M = var1.N.P();
            }

            i = var0.player.getX();
            I = var0.player.getY();
            d = var0.player.getZ();
            if (!var0.isPaused()) {
               D.Z(var0.player.getYRot(), var0.player.getXRot(), var1.d() ? 1.0F : var1.p.P().floatValue());
            }

            m5.l(ea.m, var2, W);
            W.copyDepthFrom(var2);
            a = (boolean)1;
            return true;
         } else {
            f();
            return false;
         }
      } catch (RuntimeException var4) {
         R("capture", var4);
         return false;
      }
   }

   public static void X() {
      if (a) {
         a = (boolean)0;

         try {
            EchoHandModule var0 = EchoHandModule.v();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            m5.l(ea.m, var1, h);
            h.copyDepthFrom(var1);
            if (!Minecraft.getInstance().isPaused()) {
               r(var0.d() ? u : L, Y, var0, 0.0F);
               TextureTarget var2 = J;
               J = Y;
               Y = var2;
            }

            r(var0.d() ? x : P, var1, var0, 1.0F);
         } catch (RuntimeException var3) {
            R("history/composite", var3);
         }
      }
   }

   public static void r() {
      a = (boolean)0;
      f();
   }

   private static void Q(int var0, int var1) {
      try {
         ea.z(L);
         ea.z(P);
         ea.z(u);
         ea.z(x);
         W = hj.j("Larp EchoHand before", var0, var1, true, GpuFormat.RGBA8_UNORM);
         h = hj.j("Larp EchoHand after", var0, var1, true, GpuFormat.RGBA8_UNORM);
         J = hj.j("Larp EchoHand history", var0, var1, false, GpuFormat.RGBA8_UNORM);
         Y = hj.j("Larp EchoHand next", var0, var1, false, GpuFormat.RGBA8_UNORM);
         g = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(J.getColorTexture(), new Vector4f());
         var2.clearColorTexture(Y.getColorTexture(), new Vector4f());
         mt.K("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var3) {
         f();
         throw var3;
      }
   }

   private static void r(RenderPipeline var0, RenderTarget var1, EchoHandModule var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat(W.width).putFloat(W.height).putFloat((var2.d() ? var2.s : var2.R).P().floatValue()).putFloat(var2.J.P().floatValue());
         var6.putFloat(var2.g.P().floatValue()).putFloat(var2.p.P().floatValue()).putFloat(var3).putFloat((float)ty.K());
         var6.putFloat(D.x).putFloat(D.X).putFloat(var2.z.P() ? 1.0F : 0.0F).putFloat(0.0F);
         int var7 = var2.U();
         var6.putFloat((var7 >> 16 & 0xFF) / 255.0F).putFloat((var7 >> 8 & 0xFF) / 255.0F).putFloat((var7 & 0xFF) / 255.0F).putFloat((var7 >>> 24) / 255.0F);
         var6.putFloat(var2.b.P().floatValue()).putFloat(var2.n.P().floatValue()).putFloat(var2.y.P().floatValue()).putFloat(var2.t.P().floatValue());
         var6.putFloat(var2.Y.P().floatValue()).putFloat(var2.A.P().floatValue()).putFloat(var2.W.P().floatValue()).putFloat(0.0F).flip();
         var4.writeToBuffer(g.slice(), var6);
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
         hj.P(var12, var0);
         var12.setUniform("EchoParameters", g.slice());
         GpuSampler var13 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         hj.u(var12, "Sampler0", h.getColorTextureView(), var13);
         hj.u(var12, "Sampler1", W.getColorTextureView(), var13);
         hj.u(var12, "Sampler2", J.getColorTextureView(), var13);
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         hj.u(var12, "Sampler3", h.getDepthTextureView(), var14);
         hj.u(var12, "Sampler4", W.getDepthTextureView(), var14);
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

   private static void R(String var0, RuntimeException var1) {
      o = (boolean)1;
      f();
      mt.P("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void f() {
      TextureTarget[] var0 = new TextureTarget[]{W, h, J, Y};
      Y = null;
      J = null;
      h = null;
      W = null;
      GpuBuffer var1 = g;
      g = null;
      a = (boolean)0;
      e = null;
      v = null;
      M = null;
      D.P();
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
         mt.P("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double G(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static Identifier x(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static final class o {
      public float x;
      public float X;
      private float b;
      private float Y;
      private float n;
      private float o;
      private boolean I;

      public void Z(float var1, float var2, float var3) {
         if (!this.I) {
            this.b = var1;
            this.Y = var2;
            this.I = (boolean)1;
         } else {
            float var4 = Mth.wrapDegrees(var1 - this.b);
            float var5 = var2 - this.Y;
            this.b = var1;
            this.Y = var2;
            float var6 = Mth.clamp(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = Mth.clamp(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.n = (this.n + (var6 - this.x) * 0.18F) * 0.74F;
            this.o = (this.o + (var7 - this.X) * 0.18F) * 0.74F;
            this.x = Mth.clamp((this.x + this.n) * 0.92F, -0.075F, 0.075F);
            this.X = Mth.clamp((this.X + this.o) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void P() {
         this.I = (boolean)0;
         this.x = this.X = this.b = this.Y = this.n = this.o = 0.0F;
      }
   }
}

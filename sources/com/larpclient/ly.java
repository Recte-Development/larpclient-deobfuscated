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

public final class ly {
   private static final RenderPipeline Z = O("echo_hand", false);
   private static final RenderPipeline T = O("echo_hand", true);
   private static final RenderPipeline O = O("hand_flame", false);
   private static final RenderPipeline W = O("hand_flame", true);
   private static TextureTarget n;
   private static TextureTarget z;
   private static TextureTarget D;
   private static TextureTarget v;
   private static GpuBuffer r;
   private static int P = -1;
   private static boolean y;
   private static boolean E;
   private static Object I;
   private static Object X;
   private static String c;
   private static double g;
   private static double H;
   private static double R;
   private static final ly.h S = new ly.h();

   private static RenderPipeline O(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder();

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withUniform("Sampler" + var3, of.t);
      }

      var2.withUniform("EchoParameters", UniformType.UNIFORM_BUFFER);
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(u("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(u("core/echo_hand"))
         .withFragmentShader(u("core/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(BlendFunction.LIGHTNING) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   public static boolean D() {
      Minecraft var0 = Minecraft.getInstance();
      EchoHandModule var1 = EchoHandModule.p();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator()
         && (var1.U() || var1.e.getValue())
         && (!var1.U() || !var1.E.getValue() || !var0.player.getMainHandItem().isEmpty() || !var0.player.getOffhandItem().isEmpty());
   }

   public static void W() {
      if (!D()) {
         d();
      } else if (I != null && (I != Minecraft.getInstance().level || X != Minecraft.getInstance().player)) {
         d();
      }
   }

   public static boolean Z() {
      if (P != jk.v) {
         d();
         P = jk.v;
         y = (boolean)0;
      }

      if (!D()) {
         d();
         return false;
      }

      if (y) {
         return false;
      }

      try {
         Minecraft var0 = Minecraft.getInstance();
         EchoHandModule var1 = EchoHandModule.p();
         RenderTarget var2 = var0.gameRenderer.mainRenderTarget();
         if (_m.M == null && _m.U == null) {
            int var3 = n != null
                  && n.width == var2.width
                  && n.height == var2.height
                  && I == var0.level
                  && X == var0.player
                  && var1.j.getValue().equals(c)
                  && !(k(var0.player.getX(), var0.player.getY(), var0.player.getZ(), g, H, R) > 256.0)
               ? 0
               : 1;
            if (var3 != 0) {
               d();
               m(var2.width, var2.height);
               I = var0.level;
               X = var0.player;
               c = var1.j.getValue();
            }

            g = var0.player.getX();
            H = var0.player.getY();
            R = var0.player.getZ();
            if (!var0.isPaused()) {
               S.k(var0.player.getYRot(), var0.player.getXRot(), var1.U() ? 1.0F : var1.z.getValue().floatValue());
            }

            jk.y(e4.g, var2, n);
            n.copyDepthFrom(var2);
            E = (boolean)1;
            return true;
         } else {
            d();
            return false;
         }
      } catch (RuntimeException var4) {
         j("capture", var4);
         return false;
      }
   }

   public static void b() {
      if (E) {
         E = (boolean)0;

         try {
            EchoHandModule var0 = EchoHandModule.p();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            jk.y(e4.g, var1, z);
            z.copyDepthFrom(var1);
            if (!Minecraft.getInstance().isPaused()) {
               H(var0.U() ? O : Z, v, var0, 0.0F);
               TextureTarget var2 = D;
               D = v;
               v = var2;
            }

            H(var0.U() ? W : T, var1, var0, 1.0F);
         } catch (RuntimeException var3) {
            j("history/composite", var3);
         }
      }
   }

   public static void h() {
      E = (boolean)0;
      d();
   }

   private static void m(int var0, int var1) {
      try {
         e4.U(Z);
         e4.U(T);
         e4.U(O);
         e4.U(W);
         n = of.T("Larp EchoHand before", var0, var1, true, GpuFormat.RGBA8_UNORM);
         z = of.T("Larp EchoHand after", var0, var1, true, GpuFormat.RGBA8_UNORM);
         D = of.T("Larp EchoHand history", var0, var1, false, GpuFormat.RGBA8_UNORM);
         v = of.T("Larp EchoHand next", var0, var1, false, GpuFormat.RGBA8_UNORM);
         r = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(D.getColorTexture(), new Vector4f());
         var2.clearColorTexture(v.getColorTexture(), new Vector4f());
         r4.V("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var3) {
         d();
         throw var3;
      }
   }

   private static void H(RenderPipeline var0, RenderTarget var1, EchoHandModule var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat(n.width).putFloat(n.height).putFloat((var2.U() ? var2.O : var2.S).getValue().floatValue()).putFloat(var2.d.getValue().floatValue());
         var6.putFloat(var2.D.getValue().floatValue()).putFloat(var2.z.getValue().floatValue()).putFloat(var3).putFloat((float)ea.y());
         var6.putFloat(S.B).putFloat(S.u).putFloat(var2.J.getValue() ? 1.0F : 0.0F).putFloat(0.0F);
         int var7 = var2.f();
         var6.putFloat((var7 >> 16 & 0xFF) / 255.0F).putFloat((var7 >> 8 & 0xFF) / 255.0F).putFloat((var7 & 0xFF) / 255.0F).putFloat((var7 >>> 24) / 255.0F);
         var6.putFloat(var2.P.getValue().floatValue())
            .putFloat(var2.f.getValue().floatValue())
            .putFloat(var2.F.getValue().floatValue())
            .putFloat(var2.A.getValue().floatValue());
         var6.putFloat(var2.i.getValue().floatValue()).putFloat(var2.Y.getValue().floatValue()).putFloat(var2.H.getValue().floatValue()).putFloat(0.0F).flip();
         var4.writeToBuffer(r.slice(), var6);
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
         of.z(var12, var0);
         var12.setUniform("EchoParameters", r.slice());
         GpuSampler var13 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         of.h(var12, "Sampler0", z.getColorTextureView(), var13);
         of.h(var12, "Sampler1", n.getColorTextureView(), var13);
         of.h(var12, "Sampler2", D.getColorTextureView(), var13);
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         of.h(var12, "Sampler3", z.getDepthTextureView(), var14);
         of.h(var12, "Sampler4", n.getDepthTextureView(), var14);
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

   private static void j(String var0, RuntimeException var1) {
      y = (boolean)1;
      d();
      r4.r("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void d() {
      TextureTarget[] var0 = new TextureTarget[]{n, z, D, v};
      v = null;
      D = null;
      z = null;
      n = null;
      GpuBuffer var1 = r;
      r = null;
      E = (boolean)0;
      X = null;
      I = null;
      c = null;
      S.m();
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
         r4.r("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double k(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static Identifier u(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static final class h {
      public float B;
      public float u;
      private float v;
      private float L;
      private float W;
      private float X;
      private boolean y;

      public void k(float var1, float var2, float var3) {
         if (!this.y) {
            this.v = var1;
            this.L = var2;
            this.y = (boolean)1;
         } else {
            float var4 = Mth.wrapDegrees(var1 - this.v);
            float var5 = var2 - this.L;
            this.v = var1;
            this.L = var2;
            float var6 = Mth.clamp(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = Mth.clamp(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.W = (this.W + (var6 - this.B) * 0.18F) * 0.74F;
            this.X = (this.X + (var7 - this.u) * 0.18F) * 0.74F;
            this.B = Mth.clamp((this.B + this.W) * 0.92F, -0.075F, 0.075F);
            this.u = Mth.clamp((this.u + this.X) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void m() {
         this.y = (boolean)0;
         this.B = this.u = this.v = this.L = this.W = this.X = 0.0F;
      }
   }
}

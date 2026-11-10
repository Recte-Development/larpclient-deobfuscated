package com.larpclient;

import com.larpclient.module.EchoHandModule;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.pipeline.BindGroupLayout.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class ly {
   private static final RenderPipeline u = J("echo_hand", false);
   private static final RenderPipeline g = J("echo_hand", true);
   private static final RenderPipeline w = J("hand_flame", false);
   private static final RenderPipeline E = J("hand_flame", true);
   private static TextureTarget L;
   private static TextureTarget b;
   private static TextureTarget p;
   private static TextureTarget l;
   private static GpuBuffer T;
   private static int H = -1;
   private static boolean m;
   private static boolean z;
   private static boolean G;
   private static long C;
   private static Object f;
   private static Object x;
   private static String a;
   private static double I;
   private static double B;
   private static double A;
   private static float r = 0.016666668F;
   private static final ly.c s = new ly.c();

   private static RenderPipeline J(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder();

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withSampler("Sampler" + var3);
      }

      var2.withUniform("EchoParameters", UniformType.UNIFORM_BUFFER);
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(M("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(M("core/echo_hand"))
         .withFragmentShader(M("core/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(X(var0)) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static BlendFunction X(String var0) {
      return var0.equals("hand_flame") ? BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA : BlendFunction.LIGHTNING;
   }

   public static boolean r() {
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
         && (var1.s() || var1.q.getValue())
         && (!var1.s() || !var1.T.getValue() || !var0.player.getMainHandItem().isEmpty() || !var0.player.getOffhandItem().isEmpty());
   }

   public static void H() {
      if (!r()) {
         a();
      } else if (f != null && (f != Minecraft.getInstance().level || x != Minecraft.getInstance().player)) {
         a();
      }
   }

   public static boolean K() {
      if (H != wa.s) {
         a();
         H = wa.s;
         m = (boolean)0;
      }

      if (!r()) {
         a();
         return false;
      }

      if (m) {
         return false;
      }

      try {
         Minecraft var0 = Minecraft.getInstance();
         EchoHandModule var1 = EchoHandModule.e();
         RenderTarget var2 = var0.gameRenderer.mainRenderTarget();
         if (RenderSystem.outputColorTextureOverride == null && RenderSystem.outputDepthTextureOverride == null) {
            int var3 = L != null
                  && L.width == var2.width
                  && L.height == var2.height
                  && f == var0.level
                  && x == var0.player
                  && var1.l.getValue().equals(a)
                  && !(P(var0.player.getX(), var0.player.getY(), var0.player.getZ(), I, B, A) > 256.0)
               ? 0
               : 1;
            if (var3 != 0) {
               a();
               f(var2.width, var2.height);
               f = var0.level;
               x = var0.player;
               a = var1.l.getValue();
            }

            I = var0.player.getX();
            B = var0.player.getY();
            A = var0.player.getZ();
            if (var0.isPaused() != G) {
               G = (boolean)(!G ? 1 : 0);
               h.U("EchoHand", "Game {}; history keeps settling", G ? "paused" : "resumed");
            }

            float var4 = var0.player.getYRot();
            float var5 = var0.player.getXRot();
            float var6 = p_.M(var4);
            if (!Float.isNaN(var6)) {
               var4 = var6;
               var5 = p_.f();
            }

            s.y(var4, var5, var1.s() ? 1.0F : var1.o.getValue().floatValue());
            wa.Y(ai.C, var2, L);
            L.copyDepthFrom(var2);
            z = (boolean)1;
            return true;
         } else {
            a();
            return false;
         }
      } catch (RuntimeException var7) {
         t("capture", var7);
         return false;
      }
   }

   public static void l() {
      if (z) {
         z = (boolean)0;

         try {
            EchoHandModule var0 = EchoHandModule.e();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            wa.Y(ai.C, var1, b);
            b.copyDepthFrom(var1);
            long var2 = System.nanoTime();
            r = C == 0L ? 0.016666668F : (float)Math.clamp((var2 - C) / 1.0E9, 0.0, 0.1);
            C = var2;
            m(var0.s() ? w : u, l, var0, 0.0F);
            TextureTarget var4 = p;
            p = l;
            l = var4;
            m(var0.s() ? E : g, var1, var0, 1.0F);
         } catch (RuntimeException var5) {
            t("history/composite", var5);
         }
      }
   }

   public static void g() {
      z = (boolean)0;
      a();
   }

   private static void f(int var0, int var1) {
      try {
         ai.U(u);
         ai.U(g);
         ai.U(w);
         ai.U(E);
         L = mx.n("Larp EchoHand before", var0, var1, true, GpuFormat.RGBA8_UNORM);
         b = mx.n("Larp EchoHand after", var0, var1, true, GpuFormat.RGBA8_UNORM);
         p = mx.n("Larp EchoHand history", var0, var1, false, GpuFormat.RGBA8_UNORM);
         l = mx.n("Larp EchoHand next", var0, var1, false, GpuFormat.RGBA8_UNORM);
         T = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(p.getColorTexture(), new Vector4f());
         var2.clearColorTexture(l.getColorTexture(), new Vector4f());
         h.A("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var3) {
         a();
         throw var3;
      }
   }

   private static void m(RenderPipeline var0, RenderTarget var1, EchoHandModule var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat(L.width).putFloat(L.height).putFloat((var2.s() ? var2.W : var2.X).getValue().floatValue()).putFloat(var2.Q.getValue().floatValue());
         var6.putFloat(var2.a.getValue().floatValue()).putFloat(var2.o.getValue().floatValue()).putFloat(var3).putFloat((float)cf.F());
         var6.putFloat(s.R).putFloat(s.E).putFloat(var2.C.getValue() ? 1.0F : 0.0F).putFloat(r);
         int var7 = var2.c();
         var6.putFloat((var7 >> 16 & 0xFF) / 255.0F).putFloat((var7 >> 8 & 0xFF) / 255.0F).putFloat((var7 & 0xFF) / 255.0F).putFloat((var7 >>> 24) / 255.0F);
         var6.putFloat(var2.r.getValue().floatValue())
            .putFloat(var2.t.getValue().floatValue())
            .putFloat(var2.j.getValue().floatValue())
            .putFloat(var2.F.getValue().floatValue());
         var6.putFloat(var2.d.getValue().floatValue()).putFloat(var2.B.getValue().floatValue()).putFloat(var2.g.getValue().floatValue()).putFloat(0.0F).flip();
         var4.writeToBuffer(T.slice(), var6);
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
         mx.i(var12, var0);
         var12.setUniform("EchoParameters", T.slice());
         GpuSampler var13 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         mx.x(var12, "Sampler0", b.getColorTextureView(), var13);
         mx.x(var12, "Sampler1", L.getColorTextureView(), var13);
         mx.x(var12, "Sampler2", p.getColorTextureView(), var13);
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         mx.x(var12, "Sampler3", b.getDepthTextureView(), var14);
         mx.x(var12, "Sampler4", L.getDepthTextureView(), var14);
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

   private static void t(String var0, RuntimeException var1) {
      m = (boolean)1;
      a();
      h.m("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void a() {
      TextureTarget[] var0 = new TextureTarget[]{L, b, p, l};
      l = null;
      p = null;
      b = null;
      L = null;
      GpuBuffer var1 = T;
      T = null;
      z = (boolean)0;
      x = null;
      f = null;
      a = null;
      s.S();
      C = 0L;
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
         h.m("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double P(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static Identifier M(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static final class c {
      public float R;
      public float E;
      private float m;
      private float e;
      private float l;
      private float Z;
      private boolean X;

      public void y(float var1, float var2, float var3) {
         if (!this.X) {
            this.m = var1;
            this.e = var2;
            this.X = (boolean)1;
         } else {
            float var4 = Mth.wrapDegrees(var1 - this.m);
            float var5 = var2 - this.e;
            this.m = var1;
            this.e = var2;
            float var6 = Mth.clamp(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = Mth.clamp(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.l = (this.l + (var6 - this.R) * 0.18F) * 0.74F;
            this.Z = (this.Z + (var7 - this.E) * 0.18F) * 0.74F;
            this.R = Mth.clamp((this.R + this.l) * 0.92F, -0.075F, 0.075F);
            this.E = Mth.clamp((this.E + this.Z) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void S() {
         this.X = (boolean)0;
         this.R = this.E = this.m = this.e = this.l = this.Z = 0.0F;
      }
   }
}
